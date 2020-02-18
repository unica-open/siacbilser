/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.CartaContabileDad;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCartaContabilePerChiaveService extends AbstractBaseService<RicercaCartaContabilePerChiave, RicercaCartaContabilePerChiaveResponse> {
	
	@Autowired
	CartaContabileDad cartaContabileDad;
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
		
	@Autowired
	DocumentoSpesaService documentoSpesaService;
	
	@Override
	protected void init() {
		final String methodName="init";
		log.debug(methodName, " - Begin");
	}	
	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaCartaContabilePerChiaveResponse executeService(RicercaCartaContabilePerChiave serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "execute";
		log.debug(methodName, " - Begin");
		
		long startTot = System.currentTimeMillis();
		
		long totUno = 0;
		
		long totAttoAmministrativo = 0;
		long totCapitolo = 0;
		long totRicercaDettaglioQuotaSpesa = 0;
		
		try {
			
			long startUno = System.currentTimeMillis();
			
			Ente ente = req.getEnte();
			Richiedente richiedente = req.getRichiedente();
			RicercaCartaContabileK pk = req.getpRicercaCartaContabileK();
			
			// Aggiunte queste righe che altrimenti andava in errore in caso di richiamo senza aver settato dataOra
			if(null==req.getDataOra())	req.setDataOra(new Date());
			
			DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, null);
			
			CartaContabile cartaContabile = cartaContabileDad.ricercaCartaContabile(pk, richiedente, Constanti.AMBITO_FIN,ente, datiOperazione,
					req.getCercaMdpCessionePerChiaveModPag());
				
			// estraggo l'atto amministrativo della carta contabile
			
			Integer annoAttoAmministrativo = cartaContabile.getAttoAmministrativo().getAnno();
			Integer numeroAttoAmministrativo = cartaContabile.getAttoAmministrativo().getNumero();
			TipoAtto tipoAttoAmministrativo = cartaContabile.getAttoAmministrativo().getTipoAtto();
			Integer idStrutturaAmministrativa = leggiIdStrutturaAmministrativa(cartaContabile.getAttoAmministrativo());
			
			long stopUno = System.currentTimeMillis();
			totUno = stopUno - startUno;
			
			DatiOpzionaliCapitoli datiOpzionaliCapitoli = req.getDatiOpzionaliCapitoli();
			
			// Non richiedo NESSUN importo derivato.
			//datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
			// Non richiedo NESSUN classificatore
			//datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
			
			long startDue = System.currentTimeMillis();
			
			// caching su atto amministrativo:
			HashMap<String, AttoAmministrativo> cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
			
			//caching su capitolo:
			HashMap<Integer, CapitoloUscitaGestione> cacheCapitolo = new HashMap<Integer, CapitoloUscitaGestione>();
			
			if (annoAttoAmministrativo!=null && numeroAttoAmministrativo!=null && tipoAttoAmministrativo!=null) {
				// estrae atto in modalita di caching
				
				long startAtto = System.currentTimeMillis();
				AttoAmministrativo attoAmministrativoEstratto = estraiAttoAmministrativoCaching(richiedente, annoAttoAmministrativo.toString(), numeroAttoAmministrativo, tipoAttoAmministrativo,idStrutturaAmministrativa, cacheAttoAmm);
				long stopAtto = System.currentTimeMillis();
				totAttoAmministrativo = totAttoAmministrativo + (stopAtto-startAtto);
				
				if(attoAmministrativoEstratto!=null)
					cartaContabile.setAttoAmministrativo(attoAmministrativoEstratto);
			}
			
			
			//scorre le righe carta
			if(cartaContabile.getListaPreDocumentiCarta()!=null && cartaContabile.getListaPreDocumentiCarta().size()>0){
				for(PreDocumentoCarta preDocumentoCarta : cartaContabile.getListaPreDocumentiCarta()){
					if(preDocumentoCarta.getImpegno()!=null){
						// estraggo il capitolo uscita gestione 
						// dell'impegno legato al pre-documento
						
						long startCapitolo = System.currentTimeMillis();
						CapitoloUscitaGestione capitoloUscitaGestione =  caricaCapitoloUscitaGestioneSinteticaCaching(richiedente, preDocumentoCarta.getImpegno().getChiaveCapitoloUscitaGestione(), cacheCapitolo,datiOpzionaliCapitoli);
						long stopCapitolo = System.currentTimeMillis();
						totCapitolo = totCapitolo + (stopCapitolo-startCapitolo);
						
						if(capitoloUscitaGestione!=null){
							preDocumentoCarta.getImpegno().setCapitoloUscitaGestione(capitoloUscitaGestione);
						}
						
						// estraggo l'atto amministrativo 
						// dell'impegno legato al pre-documento
						if(preDocumentoCarta.getImpegno().getAttoAmmAnno()!=null && preDocumentoCarta.getImpegno().getAttoAmmNumero()!=null && preDocumentoCarta.getImpegno().getAttoAmmTipoAtto()!=null){
							// estrae atto in modalita di caching
							Integer idStrutturaAmministrativaPreDoc = preDocumentoCarta.getImpegno().getIdStrutturaAmministrativa();
							
							long startAtto = System.currentTimeMillis();
							AttoAmministrativo attoAmministrativoImpegno = estraiAttoAmministrativoCaching(richiedente, preDocumentoCarta.getImpegno().getAttoAmmAnno(), preDocumentoCarta.getImpegno().getAttoAmmNumero(), preDocumentoCarta.getImpegno().getAttoAmmTipoAtto(),idStrutturaAmministrativaPreDoc, cacheAttoAmm);
							long stopAtto = System.currentTimeMillis();
							totAttoAmministrativo = totAttoAmministrativo + (stopAtto-startAtto);
							
							if(attoAmministrativoImpegno!=null){
								preDocumentoCarta.getImpegno().setAttoAmministrativo(attoAmministrativoImpegno);
							}
								
						}
						
						// estraggo l'atto amministrativo delle eventuali 
						// modifiche dell'impegno legato al pre-documento
						List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesaImp = preDocumentoCarta.getImpegno().getListaModificheMovimentoGestioneSpesa();
						if(elencoModificheMovimentoGestioneSpesaImp!=null && elencoModificheMovimentoGestioneSpesaImp.size() > 0){
							for(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaImp : elencoModificheMovimentoGestioneSpesaImp){
								if(modificaMovimentoGestioneSpesaImp.getAttoAmmAnno()!=null && modificaMovimentoGestioneSpesaImp.getAttoAmmNumero()!=null && modificaMovimentoGestioneSpesaImp.getAttoAmmTipoAtto()!=null){
									// estrae atto in modalita di caching
									Integer idStrutturaAmministrativaModMovGest = modificaMovimentoGestioneSpesaImp.getIdStrutturaAmministrativa();
									
									long startAtto = System.currentTimeMillis();
									AttoAmministrativo attoAmministrativoModificaImp = estraiAttoAmministrativoCaching(richiedente, modificaMovimentoGestioneSpesaImp.getAttoAmmAnno(), modificaMovimentoGestioneSpesaImp.getAttoAmmNumero(), modificaMovimentoGestioneSpesaImp.getAttoAmmTipoAtto(),idStrutturaAmministrativaModMovGest, cacheAttoAmm);
									long stopAtto = System.currentTimeMillis();
									totAttoAmministrativo = totAttoAmministrativo + (stopAtto-startAtto);
									
									
									if(attoAmministrativoModificaImp!=null){
										modificaMovimentoGestioneSpesaImp.setAttoAmministrativo(attoAmministrativoModificaImp);
									}
										
								}
							}
						}
						// estraggo i subimpegni di impegno legati al pre-documento
						List<SubImpegno> elencoSubImpegni = preDocumentoCarta.getImpegno().getElencoSubImpegni();
						if(elencoSubImpegni!=null && elencoSubImpegni.size() > 0){
							for(SubImpegno subImpegno : elencoSubImpegni){
								if(subImpegno.getAttoAmmAnno()!=null && subImpegno.getAttoAmmNumero()!=null && subImpegno.getAttoAmmTipoAtto()!=null){
									// estrae atto in modalita di caching
									Integer idStrutturaAmministrativaSub = subImpegno.getIdStrutturaAmministrativa();
									
									long startAtto = System.currentTimeMillis();
									AttoAmministrativo attoAmministrativoSub = estraiAttoAmministrativoCaching(richiedente, subImpegno.getAttoAmmAnno(), subImpegno.getAttoAmmNumero(), subImpegno.getAttoAmmTipoAtto(),idStrutturaAmministrativaSub, cacheAttoAmm);
									long stopAtto = System.currentTimeMillis();
									totAttoAmministrativo = totAttoAmministrativo + (stopAtto-startAtto);
									
									if(attoAmministrativoSub!=null){
										subImpegno.setAttoAmministrativo(attoAmministrativoSub);
									}
										
								}
			
								List<ModificaMovimentoGestioneSpesa> elencoModificheMovimentoGestioneSpesaSubImp = subImpegno.getListaModificheMovimentoGestioneSpesa();
								if(elencoModificheMovimentoGestioneSpesaSubImp!=null && elencoModificheMovimentoGestioneSpesaSubImp.size() > 0){
									for(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaSubImp : elencoModificheMovimentoGestioneSpesaSubImp){
										if(modificaMovimentoGestioneSpesaSubImp.getAttoAmmAnno()!=null && modificaMovimentoGestioneSpesaSubImp.getAttoAmmNumero()!=null && modificaMovimentoGestioneSpesaSubImp.getAttoAmmTipoAtto()!=null){
											// estrae atto in modalita di caching
											Integer idStrutturaAmministrativaSub = subImpegno.getIdStrutturaAmministrativa();
											
											long startAtto = System.currentTimeMillis();
											AttoAmministrativo attoAmministrativoModificaSubImp = estraiAttoAmministrativoCaching(richiedente, modificaMovimentoGestioneSpesaSubImp.getAttoAmmAnno(), modificaMovimentoGestioneSpesaSubImp.getAttoAmmNumero(), modificaMovimentoGestioneSpesaSubImp.getAttoAmmTipoAtto(),idStrutturaAmministrativaSub, cacheAttoAmm);
											long stopAtto = System.currentTimeMillis();
											totAttoAmministrativo = totAttoAmministrativo + (stopAtto-startAtto);
											
											if(attoAmministrativoModificaSubImp!=null){
												modificaMovimentoGestioneSpesaSubImp.setAttoAmministrativo(attoAmministrativoModificaSubImp);
											}
												
										}
									}
								}
							}
						}
					}
					
					// estrazione degli eventuali sub-documenti legati al PreDocumentoCarta : inizio
					if(preDocumentoCarta.getListaIdSubDocumentiCollegati()!=null && preDocumentoCarta.getListaIdSubDocumentiCollegati().size()>0){
						List<SubdocumentoSpesa> listaSubDocumentiSpesaCollegati = new ArrayList<SubdocumentoSpesa>();
						for(Integer idSubDocumentoSpesa : preDocumentoCarta.getListaIdSubDocumentiCollegati()){
							RicercaDettaglioQuotaSpesa ricercaDettaglioQuotaSpesa = new RicercaDettaglioQuotaSpesa();
							ricercaDettaglioQuotaSpesa.setRichiedente(richiedente);
							
							SubdocumentoSpesa subdocumentoSpesaInput = new SubdocumentoSpesa();
							subdocumentoSpesaInput.setUid(idSubDocumentoSpesa);
							ricercaDettaglioQuotaSpesa.setSubdocumentoSpesa(subdocumentoSpesaInput);
							// ricerca i subdocumentispesa correlati
							
							long startDettaglioQuotaSpesa = System.currentTimeMillis();
							RicercaDettaglioQuotaSpesaResponse ricercaDettaglioQuotaSpesaResponse = documentoSpesaService.ricercaDettaglioQuotaSpesa(ricercaDettaglioQuotaSpesa);
							long stopDettaglioQuotaSpesa = System.currentTimeMillis();
							totRicercaDettaglioQuotaSpesa = totRicercaDettaglioQuotaSpesa + (stopDettaglioQuotaSpesa-startDettaglioQuotaSpesa);
							
							
							if(ricercaDettaglioQuotaSpesaResponse.isFallimento()){
								// uscita con KO
								res.setErrori(ricercaDettaglioQuotaSpesaResponse.getErrori());
								res.setEsito(Esito.FALLIMENTO);
								res.setCartaContabile(null);
								return;
							}
							
							listaSubDocumentiSpesaCollegati.add(ricercaDettaglioQuotaSpesaResponse.getSubdocumentoSpesa());
						}
						
						if(listaSubDocumentiSpesaCollegati!=null && listaSubDocumentiSpesaCollegati.size()>0){
							preDocumentoCarta.setListaSubDocumentiSpesaCollegati(listaSubDocumentiSpesaCollegati);
						}
					}
					// estrazione degli eventuali sub-documenti legati al PreDocumentoCarta : fine								 

				}
			}
			
			long stopDue = System.currentTimeMillis();
			
			long stopTot = System.currentTimeMillis();
			
			long tot = stopTot-startTot;
			long totDue = stopDue-startDue;
			
			
			CommonUtils.println("tot:                           " + tot);
			CommonUtils.println("totUno:                        " + totUno  );
			CommonUtils.println("totDue:                        " + totDue );
			CommonUtils.println("totAttoAmministrativo:         " + totAttoAmministrativo);
			CommonUtils.println("totCapitolo:                   " + totCapitolo);
			CommonUtils.println("totRicercaDettaglioQuotaSpesa: " + totRicercaDettaglioQuotaSpesa);

			
			// Esito con OK
			res.setEsito(Esito.SUCCESSO);
			res.setCartaContabile(cartaContabile);

		} catch (Exception e) {
			log.error(methodName, e);
		}
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="checkServiceParam";
		log.debug(methodName, " - Begin");

		// verifica parametri passati in input
		if (req.getpRicercaCartaContabileK() == null) {
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Chiave primaria ordinativo pagamento"));			
		}  else {
			CartaContabile cartCont = req.getpRicercaCartaContabileK().getCartaContabile();
			if (cartCont == null) {
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Carta contabile"));
			}
			if (cartCont.getNumero() == null) {
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero carta contabile"));
			}
			Bilancio bil = req.getpRicercaCartaContabileK().getBilancio();
			if (bil == null) {
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Bilancio"));
			}
		    if (bil.getAnno() == 0) {
		    	checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno bilancio"));
		    }
		}
		Ente ente = req.getRichiedente().getAccount().getEnte();
		if (ente == null) checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
	}
	
	
}