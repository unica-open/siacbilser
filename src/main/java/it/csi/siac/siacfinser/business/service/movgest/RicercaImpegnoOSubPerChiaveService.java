/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneService;
import it.csi.siac.siacfinser.business.service.util.Utility;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoOSubPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoOSubPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOptSubDad;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;



@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegnoOSubPerChiaveService extends RicercaAttributiMovimentoGestioneService<RicercaImpegnoOSubPerChiave, RicercaImpegnoOSubPerChiaveResponse> {
	
	@Autowired
	ImpegnoOptSubDad impegnoDad;
	
	
	@Override
	protected void init() {
		final String methodName = "RicercaImpegnoOSubPerChiaveService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	@Override
	public void execute() {
		final String methodName = "RicercaImpegnoOSubPerChiaveService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getpRicercaImpegnoK() " + req.getpRicercaImpegnoK());
		
		//1. Vengono letti i valori ricevuti in input dalla request
		String annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio().toString();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Impegno impegno = null;

		BigDecimal numeroSub = req.getNumeroSubImpegno();
		
		//2. Si richiama il metodo interno di ricerca per chiave per impegni o accertamenti:
		impegno = (Impegno) impegnoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoImpegno, numeroImpegno,numeroSub , CostantiFin.MOVGEST_TIPO_IMPEGNO, true);
		
		if(null!=impegno){
			
			impegno = completaDatiImpegnoESubDopoRicercaMovimentoPk(impegno, richiedente, annoEsercizio, req.getEnte().getUid(), numeroSub);
			
			//componiamo la respose esito positivo:
			res.setImpegno(impegno);
			res.setBilancio(impegno.getCapitoloUscitaGestione().getBilancio());
			res.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
			res.setEsito(Esito.SUCCESSO);
			Utility.logXmlTypeObject(res, "WAWA");
		} else {
			//componiamo la respose esito negativo:
			res.setBilancio(null);
			res.setCapitoloUscitaGestione(null);
			res.setImpegno(null);
			res.setEsito(Esito.FALLIMENTO);
		}
	}
	
	protected Impegno completaDatiImpegnoESubDopoRicercaMovimentoPk(Impegno impegno,Richiedente richiedente,String annoEsercizio,Integer idEnte,BigDecimal numeroSub){
		//si invoca il metodo completaDatiRicercaImpegnoPk che si occupa di vestire i dati ottenuti:
		impegno = completaDatiRicercaImpegnoPkCUSTOM(richiedente, impegno, annoEsercizio,numeroSub);
		
		
		
		return impegno;
	}
	
	protected Impegno completaDatiRicercaImpegnoPkCUSTOM(Richiedente richiedente,Impegno impegno, String annoEsercizio,BigDecimal numeroSub) {
		
		HashMap<String, AttoAmministrativo> cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
		
		CapitoloUscitaGestione capitoloUscitaGestione = caricaCapitoloUscitaGestione(richiedente, impegno.getChiaveCapitoloUscitaGestione(), true);
		
		if (null != capitoloUscitaGestione){
			impegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
		}
		
		
		if (null != impegno.getAttoAmmAnno() && null != impegno.getAttoAmmNumero() && null != impegno.getAttoAmmTipoAtto()) {
			// leggo l'idStruttura amministrativa, che puo' anche essere nullo:
			Integer idStrutturaAmministrativa = impegno.getIdStrutturaAmministrativa();
			
			AttoAmministrativo attoAmministrativo = estraiAttoAmministrativoCaching(richiedente, impegno.getAttoAmmAnno(),impegno.getAttoAmmNumero(), impegno.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
			
			if (null != attoAmministrativo){
				impegno.setAttoAmministrativo(attoAmministrativo);
			}
				
		}
		

		List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesaImp = impegno.getListaModificheMovimentoGestioneSpesa();
		if (null != elencoModificheMovimentoGestioneSpesaImp && elencoModificheMovimentoGestioneSpesaImp.size() > 0) {
			
			for (ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaImp : elencoModificheMovimentoGestioneSpesaImp) {
				
				if (null != modificaMovimentoGestioneSpesaImp.getAttoAmmAnno() && null != modificaMovimentoGestioneSpesaImp.getAttoAmmNumero() && null != modificaMovimentoGestioneSpesaImp.getAttoAmmTipoAtto()) {
					// leggo l'idStruttura amministrativa, che puo' anche essere
					// nullo:
					Integer idStrutturaAmministrativa = modificaMovimentoGestioneSpesaImp.getIdStrutturaAmministrativa();
					
					AttoAmministrativo attoAmministrativoModificaImp = estraiAttoAmministrativoCaching(richiedente,modificaMovimentoGestioneSpesaImp.getAttoAmmAnno(),modificaMovimentoGestioneSpesaImp.getAttoAmmNumero(),modificaMovimentoGestioneSpesaImp.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
					
					if (null != attoAmministrativoModificaImp){
						modificaMovimentoGestioneSpesaImp.setAttoAmministrativo(attoAmministrativoModificaImp);
					}
						
				}
			}
		}

		List<SubImpegno> elencoSubImpegni = impegno.getElencoSubImpegni();
		if (null != elencoSubImpegni && elencoSubImpegni.size() > 0) {
			for (SubImpegno subImpegno : elencoSubImpegni) {
				
				
				if(numeroSub!=null && subImpegno.getNumeroBigDecimal()!=null && subImpegno.getNumeroBigDecimal().intValue()==numeroSub.intValue()){
					//E' il sub richiesto
					
					
					if (null != subImpegno.getAttoAmmAnno() && null != subImpegno.getAttoAmmNumero() && null != subImpegno.getAttoAmmTipoAtto()) {
						
						// leggo l'idStruttura amministrativa, che puo' anche essere
						// nullo:
						Integer idStrutturaAmministrativa = subImpegno.getIdStrutturaAmministrativa();
						
						AttoAmministrativo attoAmministrativoSub = estraiAttoAmministrativoCaching(richiedente, subImpegno.getAttoAmmAnno(),subImpegno.getAttoAmmNumero(),subImpegno.getAttoAmmTipoAtto(),idStrutturaAmministrativa, cacheAttoAmm );
						
						if (null != attoAmministrativoSub){
							subImpegno.setAttoAmministrativo(attoAmministrativoSub);
						}
							
					}

					List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesaSubImp = subImpegno.getListaModificheMovimentoGestioneSpesa();
					
					if (null != elencoModificheMovimentoGestioneSpesaSubImp && elencoModificheMovimentoGestioneSpesaSubImp.size() > 0) {
						for (ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaSubImp : elencoModificheMovimentoGestioneSpesaSubImp) {
							if (null != modificaMovimentoGestioneSpesaSubImp.getAttoAmmAnno() && null != modificaMovimentoGestioneSpesaSubImp.getAttoAmmNumero() && null != modificaMovimentoGestioneSpesaSubImp.getAttoAmmTipoAtto()) {
								
								// leggo l'idStruttura amministrativa, che puo'
								// anche essere nullo:
								Integer idStrutturaAmministrativa = modificaMovimentoGestioneSpesaSubImp.getIdStrutturaAmministrativa();
								
								AttoAmministrativo attoAmministrativoModificaSubImp = estraiAttoAmministrativoCaching(richiedente,modificaMovimentoGestioneSpesaSubImp.getAttoAmmAnno(),modificaMovimentoGestioneSpesaSubImp.getAttoAmmNumero(),modificaMovimentoGestioneSpesaSubImp.getAttoAmmTipoAtto(),idStrutturaAmministrativa,cacheAttoAmm);
								
								if (null != attoAmministrativoModificaSubImp){
									modificaMovimentoGestioneSpesaSubImp.setAttoAmministrativo(attoAmministrativoModificaSubImp);
								}

							}
						}
					}
					
					
				}
				
				
			}
		}
		

		// completa dati per eventuali VINCOLI
		
		if (impegno.getVincoliImpegno() != null && !impegno.getVincoliImpegno().isEmpty()) {

			for (VincoloImpegno vincoloImpegno : impegno.getVincoliImpegno()) {
				// carico i capitoli dei vincoli
				
				CapitoloEntrataGestione capitoloEntrataGestione = caricaCapitoloEntrataGestione(richiedente, vincoloImpegno.getAccertamento().getChiaveCapitoloEntrataGestione(), true);
				
				// vincoloImpegno.getAccertamento().setCapitoloEntrataGestione(capitoloEntrataGestione);

				Accertamento acc = (Accertamento) accertamentoDad.ricercaMovimentoPk(richiedente,capitoloEntrataGestione.getEnte(),annoEsercizio, vincoloImpegno.getAccertamento().getAnnoMovimento(), vincoloImpegno.getAccertamento().getNumeroBigDecimal(),CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, false);
				
				vincoloImpegno.setAccertamento(acc);

				vincoloImpegno.getAccertamento().setCapitoloEntrataGestione(capitoloEntrataGestione);

			}

		}
		

		// calcola disponibilita a VINCOLARE
		// SE impegno.stato = Annullato disponibilitaVincolare = 0
		// ALTRIMENTI disponibilitaVincolare = importoAttuale -
		// Somma(Vincoli.importo)

		DisponibilitaMovimentoGestioneContainer disponibilitaVincolare;

		if (!impegno.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)) {
			BigDecimal sommaVincoli = BigDecimal.ZERO;
			if (impegno.getVincoliImpegno() != null) {
				for (VincoloImpegno vincolo : impegno.getVincoliImpegno()) {
					sommaVincoli = sommaVincoli.add(vincolo.getImporto());
				}
			}
			disponibilitaVincolare = new DisponibilitaMovimentoGestioneContainer(impegno.getImportoAttuale().subtract(sommaVincoli), "Disponibilita' calcolata come differenza tra importo attuale (" + impegno.getImportoAttuale()
					+ ") e totale dei vincoli (" + sommaVincoli + ")");
		} else {
			disponibilitaVincolare = new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Se lo stato e' ANNULLATO, la disponibilita' deve essere ZERO");
		}
		impegno.setDisponibilitaVincolare(disponibilitaVincolare.getDisponibilita());
		impegno.setMotivazioneDisponibilitaVincolare(disponibilitaVincolare.getMotivazione());
		

		return impegno;
	}
	
	@Override
	@Transactional(timeout=360, readOnly=true)
	public RicercaImpegnoOSubPerChiaveResponse executeService(RicercaImpegnoOSubPerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaImpegnoOSubPerChiaveService : checkServiceParam()";
	
		//dati di input presi da request:
		Integer annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Ente ente = req.getRichiedente().getAccount().getEnte();

		if(null==req.getpRicercaImpegnoK()){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("RICERCA_IMPEGNO_K"));			
		} else if(null==annoEsercizio && null==annoImpegno && null==numeroImpegno && null==ente){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));
		}  else if(null==annoEsercizio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_ESERCIZIO"));
		} else if(null==annoImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_IMPEGNO"));
		} else if(null==numeroImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO_IMPEGNO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
	}	
	
	
}
