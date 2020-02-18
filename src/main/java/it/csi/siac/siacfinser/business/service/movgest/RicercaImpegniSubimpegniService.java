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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegniSubimpegniService extends AbstractBaseService< RicercaImpegniSubImpegni,  RicercaImpegniSubimpegniResponse>{
	
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
	public RicercaImpegniSubimpegniResponse executeService(RicercaImpegniSubImpegni serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		ParametroRicercaImpSub criteri = req.getParametroRicercaImpSub();	
		
		checkNotNull(criteri, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore());
		
		boolean valorizzatoAnnoEsercizio = criteri.getAnnoEsercizio() != null && criteri.getAnnoEsercizio() != 0;
		
		checkCondition(valorizzatoAnnoEsercizio , ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
		
		checkParametriPaginazione(req);
		
		if(!criteri.getIsRicercaDaImpegno()){
			boolean valorizzatoAnnoAttoAmministrativo = (req.getParametroRicercaImpSub().getAnnoProvvedimento()!= null ? true : false ) ;
			boolean valorizzatoNumeroAttoAmministrativo = (req.getParametroRicercaImpSub().getNumeroProvvedimento() !=null ? true :false);
			boolean valorizzatoTipoAttoAmministrativo = ((req.getParametroRicercaImpSub().getTipoProvvedimento() != null &&
					 !"null".equals(req.getParametroRicercaImpSub().getTipoProvvedimento().getCodice())) ? true : false)  ;
			
			checkCondition(valorizzatoAnnoAttoAmministrativo &&
					valorizzatoNumeroAttoAmministrativo && valorizzatoTipoAttoAmministrativo,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo"));
			
		}
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
		Integer totImpegniTrovati = impegnoOttimizzatoDad.getCountRicercaImpegniSubImpegni(req.getEnte(), req.getRichiedente(), paramRic);
		List<Impegno> listaImpegni = new ArrayList<Impegno>();
		if(totImpegniTrovati!=null && totImpegniTrovati > 0){
			listaImpegni = impegnoOttimizzatoDad.ricercaImpegniSubImpegni(req.getEnte(), req.getRichiedente(), paramRic, numPagina, numRisPerPagina);
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
	
	
	private CapitoloUscitaGestione estraiCapitoloUscitaGestioneMovimentoLight(Richiedente richiedente, Impegno impegno, Map<Integer, CapitoloUscitaGestione> mapCap){
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		
		if(mapCap.get(impegno.getChiaveCapitoloUscitaGestione())==null)
		{
			//capitoloUscitaGestione = caricaCapitoloUscitaGestione(richiedente, impegno.getChiaveCapitoloUscitaGestione(), true);
			capitoloUscitaGestione = caricaCapitoloUscitaGestioneOnlyKey(richiedente, impegno.getChiaveCapitoloUscitaGestione());
		
			mapCap.put(impegno.getChiaveCapitoloUscitaGestione(), capitoloUscitaGestione);
		}else
		{
			capitoloUscitaGestione = mapCap.get(impegno.getChiaveCapitoloUscitaGestione());
		}	

		return capitoloUscitaGestione;
	}

	
}


