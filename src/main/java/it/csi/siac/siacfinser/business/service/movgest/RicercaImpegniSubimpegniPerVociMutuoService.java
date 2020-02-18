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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegniPerVociMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniPerVociMutuoResponse;
import it.csi.siac.siacfinser.integration.entity.ext.IdMovgestSubmovegest;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegniSubimpegniPerVociMutuoService extends AbstractBaseService< RicercaImpegniSubImpegniPerVociMutuo,  RicercaImpegniSubimpegniPerVociMutuoResponse>{
	
	@Autowired
	CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Override
	protected void init() {
		final String methodName = "RicercaImpegniSubimpegniService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaImpegniSubimpegniPerVociMutuoResponse executeService(RicercaImpegniSubImpegniPerVociMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
			
		// 1. Leggiamo i dati ricevuti dalla request:
		ParametroRicercaImpSub paramRic = req.getParametroRicercaImpSub();
		int numPagina = req.getNumPagina();
		int numRisPerPagina = req.getNumRisultatiPerPagina();
		
		long startUNO = System.currentTimeMillis();
		
		// 2. Viene invocato il metodo che si occupa di ricercare e restituire l'elenco di Impegni e SubImpegni che trovano riscontro
		// rispetto al filtro di ricerca ricevuto in input al servio, leggo prima il numero degi impegni x settare il totale degli impegni trovati
		// poi ricerco paginando
		
		//Conteggio risultati
		Integer totImpegniTrovati = 0;
		List<IdMovgestSubmovegest> listaIdImpegni = impegnoOttimizzatoDad.ricercaImpegniSubImpegniPerVociMutuo(paramRic, req.getEnte());
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){
			totImpegniTrovati = listaIdImpegni.size();
		}else {
			totImpegniTrovati = 0;
		}
		//
		
		List<Impegno> listaImpegni = new ArrayList<Impegno>();
		if(totImpegniTrovati!=null && totImpegniTrovati > 0){
			listaImpegni = impegnoOttimizzatoDad.ricercaImpegniSubImpegniPerVociMutuo(req.getEnte(), req.getRichiedente(), paramRic, numPagina, numRisPerPagina,listaIdImpegni);
		}
		
		long stopUno = System.currentTimeMillis();
		long timeUno = stopUno - startUNO;
		
		// Instanzio un mappa che serve per non invocare piu' di una volta il servizio provvedimentoService.ricercaProvvedimento
		// per gli impegni o subimpegni trovati con lo stesso provvedimento
		HashMap<String, AttoAmministrativo> mapAtti = new HashMap<String, AttoAmministrativo>();
		// Instanzio un mappa che serve per non invocare piu' di una volta il servizio capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione
		// per gli impegni o subimpegni trovati con lo stesso capitolo
		Map<Integer, CapitoloUscitaGestione> mapCap = new HashMap<Integer, CapitoloUscitaGestione>();
		
		long totCAP = 0;
		long startDUE = System.currentTimeMillis();
		
		
		// 3. Dobbiamo vestire gli impegni e i subimpegni con i dati relativi a provvedimento e capitolo:
		if(listaImpegni!=null && !listaImpegni.isEmpty()){
			for(Impegno itImpegno : listaImpegni){
				
				
				long startCAP = System.currentTimeMillis();
				CapitoloUscitaGestione capitoloUscitaGestione = estraiCapitoloUscitaGestioneMovimentoLight(req.getRichiedente(), itImpegno,mapCap);
				long endCAP = System.currentTimeMillis();
				totCAP = totCAP + (endCAP-startCAP);
				
				if(null!=capitoloUscitaGestione){
					itImpegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
				}
				if(null!=itImpegno.getAttoAmmAnno() && null!=itImpegno.getAttoAmmNumero() && null!=itImpegno.getAttoAmmTipoAtto()){
					AttoAmministrativo attoAmministrativo = estraiAttoAmministrativoMvgCaching(req.getRichiedente(),itImpegno, mapAtti);
					itImpegno.setAttoAmministrativo(attoAmministrativo);
				}
				// SUB-IMPEGNI
				if(itImpegno.getElencoSubImpegni()!=null && itImpegno.getElencoSubImpegni().size()>0){
					for(SubImpegno itSub : itImpegno.getElencoSubImpegni()){
						if(null!=itSub.getAttoAmmAnno() && null!=itSub.getAttoAmmNumero() && null!=itSub.getAttoAmmTipoAtto()){
							AttoAmministrativo attoAmministrativoSub = estraiAttoAmministrativoMvgCaching(req.getRichiedente(), itSub, mapAtti);
							itSub.setAttoAmministrativo(attoAmministrativoSub);
						}
					}
				}
			}
		}
		
		long endDUE = System.currentTimeMillis();
		long timeDUE = endDUE - startDUE;
		
		long totATTO = timeDUE - totCAP;

		res.setNumPagina(numPagina);
		res.setNumRisultati(totImpegniTrovati);
		res.setListaImpegni(listaImpegni);
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(req.getParametroRicercaImpSub().getAnnoEsercizio());
		res.setBilancio(bilancio);
	}
	
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaImpegniSubimpegniService : checkServiceParam()";
		
		
		if(req.getParametroRicercaImpSub()==null)
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Parametro ricerca impegni e subimpegni"));
		
		if(req.getParametroRicercaImpSub().getAnnoEsercizio()==null || req.getParametroRicercaImpSub().getAnnoEsercizio()==0)
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno esercizio"));		

		if(!req.getParametroRicercaImpSub().getIsRicercaDaImpegno()){
			Integer annoAttoAmministrativo = (req.getParametroRicercaImpSub().getAnnoProvvedimento()!= null ? req.getParametroRicercaImpSub().getAnnoProvvedimento() : null ) ;
			Integer numeroAttoAmministrativo = (req.getParametroRicercaImpSub().getNumeroProvvedimento() !=null ? req.getParametroRicercaImpSub().getNumeroProvvedimento() :null);
			TipoAtto tipoAttoAmministrativo = (req.getParametroRicercaImpSub().getTipoProvvedimento() != null? req.getParametroRicercaImpSub().getTipoProvvedimento() : null)  ;
			
			if(!(null==annoAttoAmministrativo && null==numeroAttoAmministrativo && (null!=tipoAttoAmministrativo && "null".equals(tipoAttoAmministrativo.getCodice())))){
				String elencoParametriAttoNonInizializzati = "";
	
				if(null==annoAttoAmministrativo){
					if(elencoParametriAttoNonInizializzati.length() > 0)
						elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + ", ANNO_PROVVEDIMENTO";
					else
						elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + "ANNO_PROVVEDIMENTO";
				}
				
				if(null==numeroAttoAmministrativo && (null!=tipoAttoAmministrativo && "null".equals(tipoAttoAmministrativo.getCodice()))){
					if(elencoParametriAttoNonInizializzati.length() > 0)
						elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + ", NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO";
					else
						elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + "NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO";
				}
				
				if(elencoParametriAttoNonInizializzati.length() > 0)
					checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParametriAttoNonInizializzati));	
			}
		}
	}	
	
	private CapitoloUscitaGestione estraiCapitoloUscitaGestioneMovimentoLight(Richiedente richiedente, Impegno impegno, Map<Integer, CapitoloUscitaGestione> mapCap){
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		
		if(mapCap.get(impegno.getChiaveCapitoloUscitaGestione())==null)
		{
			capitoloUscitaGestione = caricaCapitoloUscitaGestione(richiedente, impegno.getChiaveCapitoloUscitaGestione(), true);
		
			mapCap.put(impegno.getChiaveCapitoloUscitaGestione(), capitoloUscitaGestione);
		}else
		{
			capitoloUscitaGestione = mapCap.get(impegno.getChiaveCapitoloUscitaGestione());
		}	

		return capitoloUscitaGestione;
	}
	
}


