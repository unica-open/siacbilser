/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaAccSubAcc;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccertamentiSubAccertamentiService extends AbstractBaseService< RicercaAccertamentiSubAccertamenti,  RicercaAccertamentiSubAccertamentiResponse>{

	@Autowired
	ProvvedimentoService provvedimentoService;


	@Override
	protected void init() {
		final String methodName = "RicercaAccertamentiSubAccertamentiService : init()";
		log.debug(methodName, "- Begin");
	}	

	
	@Override
	@Transactional(readOnly=true)
	public RicercaAccertamentiSubAccertamentiResponse executeService(RicercaAccertamentiSubAccertamenti serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	
	
	@Override
	public void execute() {
		
		//1. Leggiamo i dati ricevuti dalla request:
		ParametroRicercaAccSubAcc paramRic = req.getParametroRicercaAccSubAcc();
		int numPagina = req.getNumPagina();
		int numRisPerPagina = req.getNumRisultatiPerPagina();

		//2. Viene invocato il metodo che si occupa di ricercare e restituire l'elenco di Accertamenti e SubAccertamenti che trovano riscontro
		//rispetto al filtro di ricerca ricevuto in input al servio:
		List<Accertamento> listaAccertamenti = new ArrayList<Accertamento>();
		
		Integer totAccertamentiTrovati = accertamentoOttimizzatoDad.getCountRicercaAccertamentiSubAccertamenti(req.getEnte(), req.getRichiedente(), paramRic, req.getNumPagina(), req.getNumRisultatiPerPagina());
		listaAccertamenti = accertamentoOttimizzatoDad.ricercaAccertamentiSubAccertamentiOPT(req.getEnte(), req.getRichiedente(), paramRic, numPagina, numRisPerPagina);
		
				
		//Instanzio un mappa che serve per non invocare piu' di una volta il servizio provvedimentoService.ricercaProvvedimento
		//per gli accertamenti o subaccertamenti trovati con lo stesso provvedimento
		HashMap<String, AttoAmministrativo> mapAtti = new HashMap<String, AttoAmministrativo>();
		//Instanzio un mappa che serve per non invocare piu' di una volta il servizio capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione
		//per gli accertamenti o subaccertamenti trovati con lo stesso capitolo
		Map<Integer, CapitoloEntrataGestione> mapCap = new HashMap<Integer, CapitoloEntrataGestione>();

		//3. Dobbiamo vestire gli accertamenti e i subaccertamenti con i dati relativi a provvedimento e capitolo:
		if(listaAccertamenti!=null && listaAccertamenti.size()>0){
			for(Accertamento itAccertamento : listaAccertamenti){
				CapitoloEntrataGestione capitoloEntrataGestione = estraiCapitoloEntrataGestioneMovimentoLight(req.getRichiedente(), itAccertamento, mapCap);
				if(capitoloEntrataGestione!=null){
					itAccertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);
				}
				if(itAccertamento.getAttoAmmAnno()!=null && itAccertamento.getAttoAmmNumero()!=null && itAccertamento.getAttoAmmTipoAtto()!=null){
					AttoAmministrativo attoAmministrativo =  estraiAttoAmministrativoMvgCaching(req.getRichiedente(), itAccertamento, mapAtti);
					itAccertamento.setAttoAmministrativo(attoAmministrativo);
				}
				// SUB-IMPEGNI
				if(itAccertamento.getElencoSubAccertamenti()!=null && itAccertamento.getElencoSubAccertamenti().size()>0){
					for(SubAccertamento itSub : itAccertamento.getElencoSubAccertamenti()){
						if(null!=itSub.getAttoAmmAnno() && null!=itSub.getAttoAmmNumero() && null!=itSub.getAttoAmmTipoAtto()){
							AttoAmministrativo attoAmministrativoSub = estraiAttoAmministrativoMvgCaching(req.getRichiedente(),itSub, mapAtti);
							itSub.setAttoAmministrativo(attoAmministrativoSub);
						}
					}
				}
			}
		}
		
		//List<Accertamento> elencoPaginato = getPaginata(listaAccertamenti, req.getNumPagina(), req.getNumRisultatiPerPagina());
		//Costruiamo la response di ritorno:
		res.setNumPagina(numPagina);
		res.setNumRisultati(totAccertamentiTrovati);
		res.setListaAccertamenti(listaAccertamenti);
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(req.getParametroRicercaAccSubAcc().getAnnoEsercizio());
		res.setBilancio(bilancio);
		res.setEsito(Esito.SUCCESSO);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		if(req.getParametroRicercaAccSubAcc()==null)
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Parametro ricerca accertamenti e sub-accertamenti"));

		if(req.getParametroRicercaAccSubAcc().getAnnoEsercizio()==null || req.getParametroRicercaAccSubAcc().getAnnoEsercizio()==0)
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno esercizio"));		

		if(!req.getParametroRicercaAccSubAcc().getIsRicercaDaAccertamento()){
			
			Integer uidProvvedimento = req.getParametroRicercaAccSubAcc().getUidProvvedimento();

			if(uidProvvedimento==null){
				
				Integer annoAttoAmministrativo = req.getParametroRicercaAccSubAcc().getAnnoProvvedimento();
				Integer numeroAttoAmministrativo = req.getParametroRicercaAccSubAcc().getNumeroProvvedimento();
				TipoAtto tipoAttoAmministrativo = req.getParametroRicercaAccSubAcc().getTipoProvvedimento();
					
				String elencoParametriAttoNonInizializzati = "";
				if(annoAttoAmministrativo!=null){
					if(numeroAttoAmministrativo==null && tipoAttoAmministrativo==null){
						if(elencoParametriAttoNonInizializzati.length() > 0)
							elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + ", NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO";
						else
							elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + "NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO";
					}
				} else {
					if(numeroAttoAmministrativo!=null || tipoAttoAmministrativo!=null){
						if(elencoParametriAttoNonInizializzati.length() > 0)
							elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + ", ANNO_PROVVEDIMENTO";
						else
							elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + "ANNO_PROVVEDIMENTO";
					}			
				}
				
				
				if(elencoParametriAttoNonInizializzati.length() > 0)
					checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParametriAttoNonInizializzati));	
			}
			
			
		}
	}	

	private CapitoloEntrataGestione estraiCapitoloEntrataGestioneMovimentoLight(Richiedente richiedente, Accertamento accertamento, Map<Integer, CapitoloEntrataGestione> mapCap){

		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();

		if(mapCap.get(accertamento.getChiaveCapitoloEntrataGestione())==null)
		{
			//capitoloEntrataGestione = caricaCapitoloEntrataGestione(richiedente, accertamento.getChiaveCapitoloEntrataGestione(), true);
			capitoloEntrataGestione = caricaCapitoloEntrataGestioneOnlyKey(richiedente, accertamento.getChiaveCapitoloEntrataGestione());

			mapCap.put(accertamento.getChiaveCapitoloEntrataGestione(), capitoloEntrataGestione);
		}else
		{
			capitoloEntrataGestione = mapCap.get(accertamento.getChiaveCapitoloEntrataGestione());
		}	

		return capitoloEntrataGestione;
	}


}