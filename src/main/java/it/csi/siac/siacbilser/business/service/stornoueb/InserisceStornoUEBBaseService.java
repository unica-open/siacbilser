/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stornoueb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEBResponse;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceStornoUEBBaseService.
 * @deprecated da eliminare insieme alle UEB
 */
@Deprecated
public abstract class InserisceStornoUEBBaseService extends CheckedAccountBaseService<InserisceStornoUEB, InserisceStornoUEBResponse> {
	

	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;	
	
	/** The provvedimento dad. */
	@Autowired
	protected ProvvedimentoDad provvedimentoDad;
	
	/** The importi capitolo dad. */
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;
	
	/** The storno ueb. */
	protected StornoUEB stornoUEB;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		variazioniDad.setEnte(req.getEnte());
		variazioniDad.setBilancio(req.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(req.getEnte());
	}
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente uid"),false);
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"),false);
		
			
		stornoUEB = req.getStornoUEB();
		
		checkNotNull(stornoUEB,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("storno ueb"), true);
		stornoUEB.setBilancio(req.getBilancio());
		stornoUEB.setEnte(req.getEnte());
		
		checkNotNull(stornoUEB.getAttoAmministrativo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo storno ueb"), true);
		checkCondition(stornoUEB.getAttoAmministrativo().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid atto amministrativo storno ueb"));
		
		checkNotNull(stornoUEB.getAttoAmministrativo().getAnno(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto amministrativo storno ueb"));
		checkNotNull(stornoUEB.getAttoAmministrativo().getNumero(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero atto amministrativo storno ueb"));
		
		
		checkNotNull(stornoUEB.getListaDettaglioVariazioneImporto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista dettaglio variazione importo storno ueb"));
		checkCondition(!stornoUEB.getListaDettaglioVariazioneImporto().isEmpty(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista dettaglio variazione importo storno ueb (vuota)"));
		
		for(DettaglioVariazioneImportoCapitolo vi : stornoUEB.getListaDettaglioVariazioneImporto()){
//			checkNotNull(vi.getAnnoCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno di competenza importo"));
			checkCondition(vi.getStanziamento()==null || vi.getStanziamento().compareTo(BigDecimal.ZERO)<=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione stanziamento (deve essere minore di 0)"));
			checkCondition(vi.getStanziamentoCassa()==null || vi.getStanziamentoCassa().compareTo(BigDecimal.ZERO)<=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione stanziamento cassa (deve essere minore di 0)"));
			checkCondition(vi.getStanziamentoResiduo()==null || vi.getStanziamentoResiduo().compareTo(BigDecimal.ZERO)<=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione stanziamento residuo (deve essere minore di 0)"));
		}
	
		
//		checkCondition((stornoUEB instanceof StornoUEBEntrata || stornoUEB instanceof StornoUEBUscita), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("storno ueb deve essere di tipo entrata o uscita"));
		
		
		checkNotNull((stornoUEB).getCapitoloSorgente(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo sorgente"));
		checkCondition((stornoUEB).getCapitoloSorgente().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo sorgente"));
		checkNotNull((stornoUEB).getCapitoloDestinazione(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo destinazione"));
		checkCondition((stornoUEB).getCapitoloDestinazione().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo destinazione"));
		
		checkCondition((stornoUEB).getCapitoloSorgente().getUid()!=(stornoUEB).getCapitoloDestinazione().getUid(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo sorgente deve essere diverso da uid capitolo destinazione"));
		
		checkNotNull(stornoUEB.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("note"));		
		checkNotNull(stornoUEB.getStatoOperativoVariazioneDiBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo variazione"));
		
		if(stornoUEB.getData()==null){
			stornoUEB.setData(new Date());
		}
	}
	
	@Override
	@Transactional
	public InserisceStornoUEBResponse executeService(InserisceStornoUEB serviceRequest) {
		return super.executeService(serviceRequest);
	}
	 
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkTipoCapitolo();
		
		stornoUEB.setCapitoloSorgente(caricaNumeriCapitolo(stornoUEB.getCapitoloSorgente()));
		stornoUEB.setCapitoloDestinazione(caricaNumeriCapitolo(stornoUEB.getCapitoloDestinazione()));
		
		
		List<ImportiCapitolo> importiCapSorgente = caricaDettaglioImportiCapitoloSorgente();
		List<ImportiCapitolo> importiCapDestinazione =  caricaDettaglioImportiCapitoloDestinazione();
		
		
		checkImporti(importiCapSorgente,importiCapDestinazione);
		
		Integer numeroVariazione = variazioniDad.staccaNumeroVariazione();
		stornoUEB.setNumero(numeroVariazione);				
		
		variazioniDad.inserisciStorno(stornoUEB /*, stornoUEB.getAttoAmministrativo(), stornoUEB.getCapitoloSorgente(), stornoUEB.getCapitoloDestinazione()*/);
		
		aggiornaImportiCapitolo(stornoUEB.getCapitoloSorgente(),importiCapSorgente,
				stornoUEB.getCapitoloDestinazione(), importiCapDestinazione, stornoUEB.getListaDettaglioVariazioneImporto());
		
				
//		stornoUEB.setNumero(numeroVariazione);
		res.setStornoUEB(stornoUEB);	
	}
	

	private Capitolo<?, ?> caricaNumeriCapitolo(Capitolo<?, ?> capitolo) {
		return variazioniDad.popolaNumeriCapitolo(capitolo);
	}


	/**
	 * Controlla che i capitoli sorgente e destinazione siano o tutti e due di tipo entrata o tutti e due di tipo uscita.
	 */
	protected abstract void checkTipoCapitolo();
	



	/**
	 * Implementa il check degli importi a seconda che si tratti di uno 
	 * storno di capitoli di entrata o uno storno di capitoli di uscita.
	 *
	 * @param importiCapSorgente the importi cap sorgente
	 * @param importiCapDestinazione the importi cap destinazione
	 */
	protected abstract void checkImporti(List<ImportiCapitolo> importiCapSorgente, List<ImportiCapitolo> importiCapDestinazione);



	
	
	
	/**
	 * Find by anno competenza.
	 *
	 * @param importiCapSorgente the importi cap sorgente
	 * @param annoCompetenza the anno competenza
	 * @return the importi capitolo
	 */
	protected ImportiCapitolo findByAnnoCompetenza(List<ImportiCapitolo> importiCapSorgente, Integer annoCompetenza) {
		for (ImportiCapitolo importiCapitolo : importiCapSorgente) {
			if(annoCompetenza.equals(importiCapitolo.getAnnoCompetenza())){
				return importiCapitolo;
			}
		}
		return null;
	}
	
	/**
	 * Aggiorna importi capitolo.
	 *
	 * @param capitoloSorgente the capitolo sorgente
	 * @param importiCapSorgente the importi cap sorgente
	 * @param capitoloDestinazione the capitolo destinazione
	 * @param importiCapDestinazione the importi cap destinazione
	 * @param dettagliVariazioneImporto the dettagli variazione importo
	 */
	@SuppressWarnings("rawtypes")
	protected void aggiornaImportiCapitolo(Capitolo capitoloSorgente, List<ImportiCapitolo> importiCapSorgente, Capitolo capitoloDestinazione, List<ImportiCapitolo> importiCapDestinazione, List<DettaglioVariazioneImportoCapitolo> dettagliVariazioneImporto ) {
		// SIAC-6883
//		for(DettaglioVariazioneImportoCapitolo dettVarImp : dettagliVariazioneImporto){
//			Integer annoCompetenza = dettVarImp.getAnnoCompetenza();
//			
//			//Sottraggo gli importi dal capitolo sorgente (gli importi dentro dettVarImp sono tutti negativi)
//			ImportiCapitolo importiCapSorgAnno = findByAnnoCompetenza(importiCapSorgente, annoCompetenza);
//			
//			if(importiCapSorgAnno!=null){
//				
//				importiCapSorgAnno = importiCapSorgAnno.addVariazione(dettVarImp);
//
//				importiCapitoloDad.aggiornaImportiCapitolo(capitoloSorgente, importiCapSorgAnno, annoCompetenza);
//			}
//			
//			//Sommo gli importi al capitolo destinazione
//			ImportiCapitolo importiCapDestAnno = findByAnnoCompetenza(importiCapDestinazione, annoCompetenza);
//			
//			if(importiCapDestAnno!=null){
//				
//				importiCapDestAnno = importiCapDestAnno.subtractVariazione(dettVarImp);
//			
//				importiCapitoloDad.aggiornaImportiCapitolo(capitoloDestinazione, importiCapDestAnno, annoCompetenza);
//			}
//			
//			
//		}
	}
	
	
	/**
	 * Carica dettaglio importi capitolo.
	 *
	 * @param capitolo the capitolo
	 * @return the list
	 */
	protected List<ImportiCapitolo> caricaDettaglioImportiCapitolo(@SuppressWarnings("rawtypes") Capitolo capitolo) {
		
		//METODO1-> ricerca dettaglio per ottenere gli importi (troppo pesante)
			/*RicercaDettaglioCapitoloEntrataGestione serviceRequest = new RicercaDettaglioCapitoloEntrataGestione();
			serviceRequest.setEnte(req.getEnte());
			serviceRequest.setRichiedente(req.getRichiedente());
			serviceRequest.setDataOra(new Date());
			RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloEGest = new RicercaDettaglioCapitoloEGest();
			ricercaDettaglioCapitoloEGest.setChiaveCapitolo(capitolo.getUid());
			serviceRequest.setRicercaDettaglioCapitoloEGest(ricercaDettaglioCapitoloEGest);
			
			RicercaDettaglioCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.executeService(serviceRequest);
			
			return response.getListaImportiCapitoloEG();*/
		
		//METODO2-> Accesso diretto agli importi
		
		List<ImportiCapitolo> result = new ArrayList<ImportiCapitolo>();
		
		Integer annoBilancio = req.getBilancio().getAnno();
		
		ImportiCapitolo importiCapitoloEG =  importiCapitoloDad.findImportiCapitolo(capitolo, annoBilancio, ImportiCapitolo.class, EnumSet.allOf(ImportiCapitoloEnum.class));			
		result.add(importiCapitoloEG);
		
		//Anno esercizio + 1
		ImportiCapitolo importiCapitoloEG1 = importiCapitoloDad.findImportiCapitolo(capitolo, annoBilancio+1, ImportiCapitolo.class, EnumSet.allOf(ImportiCapitoloEnum.class));		
		result.add(importiCapitoloEG1);

		//Anno esercizio + 2
		ImportiCapitolo importiCapitoloEG2 = importiCapitoloDad.findImportiCapitolo(capitolo, annoBilancio+2, ImportiCapitolo.class, EnumSet.allOf(ImportiCapitoloEnum.class));	
		result.add(importiCapitoloEG2);
		
		return result;
		
	}
	
	/**
	 * Carica dettaglio importi capitolo sorgente.
	 *
	 * @return the list
	 */
	protected List<ImportiCapitolo> caricaDettaglioImportiCapitoloSorgente() {
		return caricaDettaglioImportiCapitolo(stornoUEB.getCapitoloSorgente());
	}
	
	/**
	 * Carica dettaglio importi capitolo destinazione.
	 *
	 * @return the list
	 */
	protected List<ImportiCapitolo> caricaDettaglioImportiCapitoloDestinazione() {
		return caricaDettaglioImportiCapitolo(stornoUEB.getCapitoloDestinazione());
	}

	

}
