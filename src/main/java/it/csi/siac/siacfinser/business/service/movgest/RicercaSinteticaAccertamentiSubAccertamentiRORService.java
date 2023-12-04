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
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamentiRORResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaAccSubAcc;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaAccertamentiSubAccertamentiRORService extends AbstractBaseService< RicercaSinteticaAccertamentiSubAccertamenti,  RicercaSinteticaAccertamentiSubAccertamentiRORResponse>{

	@Autowired
	ProvvedimentoService provvedimentoService;

	@Override
	protected void init() {
		final String methodName = "RicercaSinteticaAccertamentiSubAccertamentiService : init()";
		log.debug(methodName, "- Begin");
	}	

	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaAccertamentiSubAccertamentiRORResponse executeService(RicercaSinteticaAccertamentiSubAccertamenti serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		
		//1. Leggiamo i dati ricevuti dalla request:
		ParametroRicercaAccSubAcc paramRic = req.getParametroRicercaAccSubAcc();
		int numPagina = req.getNumPagina();
		int numRisPerPagina = req.getNumRisultatiPerPagina();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, null);

		//2. Viene invocato il metodo che si occupa di ricercare e restituire l'elenco di Accertamenti e SubAccertamenti che trovano riscontro
		//rispetto al filtro di ricerca ricevuto in input al servio:
		List<SubAccertamento> listaAccertamenti = new ArrayList<SubAccertamento>();
		
		Integer totAccertamentiTrovati = accertamentoOttimizzatoDad.getCountRicercaAccertamentiSubAccertamentiROR(req.getEnte(), req.getRichiedente(), paramRic);
		
		if(totAccertamentiTrovati!=null && totAccertamentiTrovati > 0){
			listaAccertamenti = accertamentoOttimizzatoDad.ricercaSinteticaAccertamentiSubAccertamentiROR(req.getEnte(), req.getRichiedente(), paramRic, numPagina, numRisPerPagina);
			//listaAccertamenti = accertamentoOttimizzatoDad.completaPresenzaSub(listaAccertamenti);
		}
		
		
		//List<Accertamento> elencoPaginato = getPaginata(listaAccertamenti, req.getNumPagina(), req.getNumRisultatiPerPagina());
		//Costruiamo la response di ritorno:
		res.setNumPagina(numPagina);
		res.setNumRisultati(totAccertamentiTrovati);
		res.setListaSubAccertamenti(listaAccertamenti);
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(req.getParametroRicercaAccSubAcc().getAnnoEsercizio());
		res.setBilancio(bilancio);
		res.setEsito(Esito.SUCCESSO);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		ParametroRicercaAccSubAcc criteri = req.getParametroRicercaAccSubAcc();	
		
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

}