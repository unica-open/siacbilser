/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionicodifica;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.ControllaClassificatoriModificabiliCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipoAzione;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class DefinisceVariazioneCodificheService.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefinisceVariazioneCodificheService extends VariazioneCodificheBaseService<DefinisceVariazioneCodifiche, DefinisceVariazioneCodificheResponse> {
		
	/** The capitolo dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloDad;
	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	/** The controlla classificatori modificabili capitolo service. */
	@Autowired
	private ControllaClassificatoriModificabiliCapitoloService controllaClassificatoriModificabiliCapitoloService;
	
	/**
	 * Cache delle response al servizio di controllaClassificatoriModificabiliCapitoloService.
	 * Serve perchè il servizio viene richiamato in un loop in cui si presumono n chiamate con lo stesso identico input.
	 * 
	 */
	private Map<String, ControllaClassificatoriModificabiliCapitoloResponse> cacheClassificatoriModificabili = new HashMap<String, ControllaClassificatoriModificabiliCapitoloResponse>();

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		variazione = req.getVariazioneCodificaCapitolo();
		//checkParamVariazione();
	
		checkNotNull(req.getIdAttivita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id attivita"), false);
		checkNotNull(variazione, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione"));
		checkCondition(variazione.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id variazione"));
		checkNotNull(variazione.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente variazione"));
		checkCondition(variazione.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id ente variazione"));
		checkNotNull(variazione.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio variazione"));
		checkCondition(variazione.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id bilancio variazione"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloDad.setLoginOperazione(loginOperazione);
		capitoloDad.setEnte(variazione.getEnte());
		variazioniDad.setEnte(variazione.getEnte());
		variazioniDad.setBilancio(variazione.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(variazione.getEnte());
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.variazionicodifica.VariazioneCodificheBaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public DefinisceVariazioneCodificheResponse executeService(DefinisceVariazioneCodifiche serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		caricaDettaglioVariazionePerUid();
		caricaTipoCapitolo();
		checkNecessarioAttoAmministrativo();
				
		checkFaseDiBilancio();	
		
		eliminaClassificatoriGerarchiciNonFoglia(variazione);
		variazione.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.DEFINITIVA);
		variazioniDad.aggiornaVariazioneCodificaCapitolo(variazione);
				
		aggiornaCodificheCapitolo();

		
		res.setVariazioneCodificaCapitolo(variazione);	
		
		definisciProcessoVariazioneDiBilancio(); 
		
	}
	
	
	



	/**
	 * Caricamento dettaglio della variazione.
	 */
	private void caricaDettaglioVariazionePerUid() {
		variazione = variazioniDad.findVariazioneCodificaCapitoloByUid(req.getVariazioneCodificaCapitolo().getUid());
		if(variazione==null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("VariazioneCodificaCapitolo", "uid:"+req.getVariazioneCodificaCapitolo().getUid()));
		}
	}

	/**
	 * Check fase di bilancio.
	 */
	private void checkFaseDiBilancio() {
		/*
		 L’Applicazione di una variazione (Tipo Variazione – modulo BIL) deve essere coerente in base alla Fase di Bilancio in cui ci si trova:
			•	Variazione in previsione: si può fare quando siamo in fase di previsione o esercizio provvisorio;
			•	Variazione in gestione: si può fare quando siamo in fase di esercizio provvisorio, gestione o assestamento;
			•	Variazione in assestamento: si può fare solo quando siamo in fase di assestamento.		 
		 */
		/*		 
		 La definizione di una variazione è regolato dalla Fase di Bilancio in cui ci si trova nell’anno 
		 di esercizio della variazione, dallo stato della variazione stessa e dalla sua applicazione:
			•	se l’applicazione non è coerente con la fase di Bilancio (vedi par. 2.5.2) 
				la variazione non è definibile e il sistema visualizza il messaggio <BIL_ERR_0046, Definizione non possibile perché fase di bilancio incongruente>.
			•	se lo stato della variazione selezionata non è coerente con il diagramma degli stati (vedasi [7])  
				il sistema visualizza il messaggio < BIL_ERR_0045, Definizione non possibile perché stato incongruente>. 
		 */
		
	}
	
	

	/**
	 * Aggiorna codifiche capitolo.
	 */
	private void aggiornaCodificheCapitolo() {
		for(DettaglioVariazioneCodificaCapitolo dettVarImp : variazione.getListaDettaglioVariazioneCodifica()) {			
		
			//TODO Scommentare il seguente pezzo per la Jira sulla modifica dei classificatori non modificabili alla definizione della variazione! 
			
			ControllaClassificatoriModificabiliCapitoloResponse resCCM = controllaClassificatoriModificabiliCapitolo(dettVarImp.getCapitolo());
						
			eliminaClassificatoriNonModificabili(dettVarImp, resCCM);			
			
//			if(!classificatoriNonValidi.isEmpty()){
//				throw new BusinessException(ErroreBil.CLASSIFICATORI_NON_MODIFICABILI.getErrore(Utility.listToCommaSeparatedFullyCapitalizedString(classificatoriNonValidi)));
//			}
			
			
			capitoloDad.aggiornaClassificatoriCapitolo(dettVarImp.getCapitolo(), dettVarImp.getClassificatoriGenerici(),dettVarImp.getClassificatoriGerarchici());
			
			capitoloDad.aggiornaDatiDiBaseCapitolo(dettVarImp.getCapitolo());
		}
		
	}

	/**
	 * Elimina i classificatori NON validi dal dettaglio di variazione codifiche.
	 * Sono NON validi i classificatori che sono contenuti nel dettaglio di variazione 
	 * delle codifiche (parametro dettVarImp) ma che non si possono variare.
	 * 
	 * @param dettVarImp the dett var imp
	 * @param resCCM the res ccm
	 */
	private void eliminaClassificatoriNonModificabili(DettaglioVariazioneCodificaCapitolo dettVarImp,
			ControllaClassificatoriModificabiliCapitoloResponse resCCM) {
		
		final String methodName = "eliminaClassificatoriNonModificabili";
		boolean unicoArticolo = resCCM.getStessoNumCapArt() == null  || resCCM.getStessoNumCapArt().longValue() <= 1;
		
		List<ClassificatoreGenerico> classificatoriGenericiModificabili = new ArrayList<ClassificatoreGenerico>();
		for(ClassificatoreGenerico classificatoreGenerico : dettVarImp.getClassificatoriGenerici()){
			ClassificatoreGenerico cg = classificatoriDad.ricercaClassificatoreById(classificatoreGenerico.getUid());
			cg.setDataFineValidita(classificatoreGenerico.getDataFineValidita());
			
			TipologiaClassificatore  tipologiaClassificatore = TipologiaClassificatore.fromCodice(cg.getTipoClassificatore().getCodice());
			if(unicoArticolo || resCCM.isModificabileMassivo(tipologiaClassificatore)){
				classificatoriGenericiModificabili.add(cg);
				
				log.debug(methodName, "Aggiunto classificatore generico "+ tipologiaClassificatore + ": "+ cg.getUid());		
			}
		}
		dettVarImp.setClassificatoriGenerici(classificatoriGenericiModificabili);
		
		List<ClassificatoreGerarchico> classificatoriGerarchiciModificabili = new ArrayList<ClassificatoreGerarchico>();
		for(ClassificatoreGerarchico classificatoreGerarchico : dettVarImp.getClassificatoriGerarchici()){
			ClassificatoreGerarchico cg = classificatoriDad.ricercaClassificatoreById(classificatoreGerarchico.getUid());
			cg.setDataFineValidita(classificatoreGerarchico.getDataFineValidita());
			
			TipologiaClassificatore  tipologiaClassificatore = TipologiaClassificatore.fromCodice(cg.getTipoClassificatore().getCodice());
			if(unicoArticolo || resCCM.isModificabileMassivo(tipologiaClassificatore)){
				classificatoriGerarchiciModificabili.add(cg);
				log.debug(methodName, "Aggiunto classificatore gerarchico "+ tipologiaClassificatore + ": "+ cg.getUid());
			}
		}
		dettVarImp.setClassificatoriGerarchici(classificatoriGerarchiciModificabili);
		
	}
	
	/**
	 * Controllo della modificabilit&agrave; dei classificatori associati al capitolo.
	 * 
	 * La risposta viene messa in una cache per evitare n chiamate con gli stessi identici parametri.
	 *
	 * @param capitolo the capitolo
	 * @return the controlla classificatori modificabili capitolo response
	 */
	public ControllaClassificatoriModificabiliCapitoloResponse controllaClassificatoriModificabiliCapitolo(Capitolo capitolo) {
		
		String cacheKey = ""+variazione.getBilancio().getUid()+capitolo.getNumeroCapitolo()+capitolo.getNumeroArticolo()+capitolo.getTipoCapitolo();
		
		if(cacheClassificatoriModificabili.containsKey(cacheKey)) {
			return cacheClassificatoriModificabili.get(cacheKey);
		}
			
		ControllaClassificatoriModificabiliCapitolo reqCCM = new ControllaClassificatoriModificabiliCapitolo();
		reqCCM.setRichiedente(req.getRichiedente());
		reqCCM.setEnte(variazione.getEnte());
		reqCCM.setBilancio(variazione.getBilancio());
		
		reqCCM.setModalitaAggiornamento(true);

		reqCCM.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		reqCCM.setNumeroArticolo(capitolo.getNumeroArticolo());
		reqCCM.setTipoCapitolo(capitolo.getTipoCapitolo());
		
		
		ControllaClassificatoriModificabiliCapitoloResponse resCCM = executeExternalService(controllaClassificatoriModificabiliCapitoloService, reqCCM);
		
		cacheClassificatoriModificabili.put(cacheKey, resCCM);
		return resCCM;
			
		
		
		
		

	}
	
	
	

	/**
	 * Popola il tipo di capitolo di tutti i DettaglioVariazioneImportoCapitolo partendo da loro uid.
	 */
	protected void caricaTipoCapitolo() {
		for(DettaglioVariazioneCodificaCapitolo dettVarImp : variazione.getListaDettaglioVariazioneCodifica()) {			
			Capitolo capitolo = dettVarImp.getCapitolo();
			TipoCapitolo tipoCapitolo = capitoloDad.findTipoCapitolo(capitolo.getUid());
			capitolo.setTipoCapitolo(tipoCapitolo);			
		}
		
	}
	
	

	/**
	 * Definisci processo variazione di bilancio.
	 */
	private void definisciProcessoVariazioneDiBilancio() {
		ExecAzioneRichiesta execAzioneRichiesta = new ExecAzioneRichiesta();
		execAzioneRichiesta.setRichiedente(req.getRichiedente());
		execAzioneRichiesta.setDataOra(new Date());
		
		//AzioneRichiesta azioneRichiesta = getAzioneRichiesta(36);
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		Azione azione = new Azione();
		azioneRichiesta.setAzione(azione);
		azioneRichiesta.setIdAttivita(req.getIdAttivita()); //ID DELL'attività ovvero id istanza. Ad esempio: "VariazioneDiBilancio--1.0--12--VariazioneDiBilancio_AggiornamentoVariazione--it1--mainActivityInstance--noLoop"
		azione.setTipo(TipoAzione.ATTIVITA_PROCESSO);
		azione.setNomeProcesso("VariazioneDiBilancio");
		azione.setNomeTask("VariazioneDiBilancio-DefinizioneDellaVariazione");
		
		
		execAzioneRichiesta.setAzioneRichiesta(azioneRichiesta);
		ExecAzioneRichiestaResponse execAzioneRichiestaResponse = coreService.execAzioneRichiesta(execAzioneRichiesta);
		log.logXmlTypeObject(execAzioneRichiestaResponse, "Risposta ottenuta dal servizio ExecAzioneRichiesta.");
		checkServiceResponseFallimento(execAzioneRichiestaResponse);
	}


	
	
	
}
