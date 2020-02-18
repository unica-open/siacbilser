/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaImpegniSubimpegniService extends AbstractBaseService< RicercaSinteticaImpegniSubImpegni, RicercaSinteticaImpegniSubimpegniResponse>{
	

	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Override
	protected void init() {
		final String methodName = "RicercaSinteticaImpegniSubimpegniService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaImpegniSubimpegniResponse executeService(RicercaSinteticaImpegniSubImpegni serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		ParametroRicercaImpSub criteri = req.getParametroRicercaImpSub();	
		
		checkNotNull(criteri, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore());
		
		boolean valorizzatoAnnoEsercizio = criteri.getAnnoEsercizio() != null && criteri.getAnnoEsercizio() != 0;
		
		checkCondition(valorizzatoAnnoEsercizio , ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
		
		checkParametriPaginazione(req);
		
		//Commento mentre silvia/maspes verifica sugli stipendi come fare, la chiamata da qui ha solo l'anno provvedimento
//		if(!criteri.getIsRicercaDaImpegno()){
//			boolean valorizzatoAnnoAttoAmministrativo = (req.getParametroRicercaImpSub().getAnnoProvvedimento()!= null ? true : false ) ;
//			boolean valorizzatoNumeroAttoAmministrativo = (req.getParametroRicercaImpSub().getNumeroProvvedimento() !=null ? true :false);
//			boolean valorizzatoTipoAttoAmministrativo = ((req.getParametroRicercaImpSub().getTipoProvvedimento() != null &&
//					 !"null".equals(req.getParametroRicercaImpSub().getTipoProvvedimento().getCodice())) ? true : false)  ;
//			
//			checkCondition(valorizzatoAnnoAttoAmministrativo &&
//					valorizzatoNumeroAttoAmministrativo && valorizzatoTipoAttoAmministrativo,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo"));
//			
//		}
	}
	
	@Override
	public void execute() {
			
		// 1. Leggiamo i dati ricevuti dalla request:
		ParametroRicercaImpSub paramRic = req.getParametroRicercaImpSub();
		int numPagina = req.getNumPagina();
		int numRisPerPagina = req.getNumRisultatiPerPagina();
		
			
		// 2. Viene invocato il metodo che si occupa di ricercare e restituire l'elenco di Impegni e SubImpegni che trovano riscontro
		// rispetto al filtro di ricerca ricevuto in input al servio, leggo prima il numero degi impegni x settare il totale degli impegni trovati
		// poi ricerco paginando
		Integer totImpegniTrovati = impegnoOttimizzatoDad.getCountRicercaImpegniSubImpegni(req.getEnte(), req.getRichiedente(), paramRic);
		
		List<Impegno> listaImpegni = new ArrayList<Impegno>();
		
		if(totImpegniTrovati!=null && totImpegniTrovati > 0){
			listaImpegni = impegnoOttimizzatoDad.ricercaSinteticaImpegniSubImpegni(req.getEnte(), req.getRichiedente(), paramRic, numPagina, numRisPerPagina);
			listaImpegni = impegnoOttimizzatoDad.completaPresenzaSub(listaImpegni);
		}
		
		
		res.setNumPagina(numPagina);
		res.setNumRisultati(totImpegniTrovati);
		res.setListaImpegni(listaImpegni);
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(req.getParametroRicercaImpSub().getAnnoEsercizio());
		res.setBilancio(bilancio);
	}
	
	
}


