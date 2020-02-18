/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionicodifica;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodificheResponse;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipoAzione;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaVariazioneCodificheService.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaVariazioneCodificheService extends VariazioneCodificheBaseService<AggiornaVariazioneCodifiche, AggiornaVariazioneCodificheResponse> {


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {

		variazione = req.getVariazioneCodificaCapitolo();
		checkParamVariazione();


		if(Boolean.TRUE.equals(req.getAnnullaVariazione())){
			variazione.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.ANNULLATA);
			req.setEvolviProcesso(Boolean.TRUE);
		}

		if(Boolean.TRUE.equals(req.getEvolviProcesso())){
			checkNotNull(req.getIdAttivita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id attivita"));
			checkNotNull(req.getAnnullaVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annulla variazione"));
			checkNotNull(req.getInvioOrganoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("invio organo amministrativo"));
			checkNotNull(req.getInvioOrganoLegislativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("invio organo legislativo"));


		}


	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		variazioniDad.setEnte(variazione.getEnte());
		variazioniDad.setBilancio(variazione.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(variazione.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.variazionicodifica.VariazioneCodificheBaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AggiornaVariazioneCodificheResponse executeService(AggiornaVariazioneCodifiche serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		// CR-2304: soppiantato dal controllo all'ultimo step
//		checkNecessarioAttoAmministrativo();

		checkProvvedimento();

		if(Boolean.TRUE.equals(req.getEvolviProcesso())) { //Evolve il processo solo se richiesto!
			ExecAzioneRichiestaResponse execAzioneRichiestaResponse = aggiornaProcessoVariazioneDiBilancio();
			impostaStatoOperativoVariazioneDiBilancio(execAzioneRichiestaResponse);
			checkProcessoEvolutoAlTaskSuccessivo(execAzioneRichiestaResponse);
		}


		variazioniDad.aggiornaVariazioneCodificaCapitolo(variazione);

		res.setVariazioneCodificaCapitolo(variazione);

	}

	/**
	 * Aggiorna processo variazione di bilancio.
	 *
	 * @return the exec azione richiesta response
	 */
	private ExecAzioneRichiestaResponse aggiornaProcessoVariazioneDiBilancio() {
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
		azione.setNomeTask("VariazioneDiBilancio-AggiornamentoVariazione");	//VariazioneDiBilancio-AggiornamentoVariazione (per aggiornamento)	//VariazioneDiBilancio-DefinizioneDellaVariazione

		//setVariabileProcesso(azioneRichiesta, "tipoVariazioneBilancio", "Importo"); //Tipo_Variazione_Bilancio //NON VARIA
//		setVariabileProcesso(azioneRichiesta, "descrizione", variazione.getDescrizione() + " "+ variazione.getNote());
//		setVariabileProcesso(azioneRichiesta, "descrizioneBreve", variazione.getDescrizione());

		setVariabileProcesso(azioneRichiesta, "descrizione", variazione.getNumero() + " - " + variazione.getDescrizione() + " - "+ variazione.getTipoVariazione().getDescrizione() );//+ " "+ variazione.getNote());
		setVariabileProcesso(azioneRichiesta, "descrizioneBreve", variazione.getNumero() + " - " + variazione.getDescrizione() + " - "+ variazione.getTipoVariazione().getDescrizione());


		//setVariabileProcesso(azioneRichiesta, "siacAnnoEsercizioProcesso", "SIAC-ANNO-ESERCIZIO-" + req.getVariazioneImportoCapitolo().getBilancio().getAnno()+""); //Testo //NON VARIA
		//setVariabileProcesso(azioneRichiesta, "variazioneDiBilancioDem", variazione.getNumero()); //NON VARIA

		setVariabileProcesso(azioneRichiesta, "siacSacProcesso", "strutture"); //sac sta per StrutturaAmministravivaContabile
		setVariabileProcesso(azioneRichiesta, "invioGiunta", req.getInvioOrganoAmministrativo()); //Boolean //invioOrganoAmministrativo
		setVariabileProcesso(azioneRichiesta, "invioConsiglio", req.getInvioOrganoLegislativo()); //Boolean //invioOrganoLegislativo
		setVariabileProcesso(azioneRichiesta, "annullaVariazione", req.getAnnullaVariazione()); //Boolean
		
		// CR-2304
		setVariabileProcesso(azioneRichiesta, "quadraturaVariazioneDiBilancio", res.getIsProvvedimentoPresente()); //Boolean

		setVariabileProcesso(azioneRichiesta, "statoVariazioneDiBilancio", StatoOperativoVariazioneDiBilancio.BOZZA.toString());

		execAzioneRichiesta.setAzioneRichiesta(azioneRichiesta);
		ExecAzioneRichiestaResponse execAzioneRichiestaResponse = coreService.execAzioneRichiesta(execAzioneRichiesta);
		log.logXmlTypeObject(execAzioneRichiestaResponse, "Risposta ottenuta dal servizio ExecAzioneRichiesta.");
		checkServiceResponseFallimento(execAzioneRichiestaResponse);

		res.setIdAttivita(execAzioneRichiestaResponse.getIdTask());

		return execAzioneRichiestaResponse;
	}

	/**
	 * Imposta lo stato operativo della variazione di Bilncio a partire dal valore della di processo <code>statoVariazioneDiBilancio</code>.
	 *
	 * @param execAzioneRichiestaResponse the exec azione richiesta response
	 */
	private void impostaStatoOperativoVariazioneDiBilancio(ExecAzioneRichiestaResponse execAzioneRichiestaResponse) {
		final String methodName = "impostaStatoOperativoVariazioneDiBilancio";

		VariabileProcesso variabileProcesso = execAzioneRichiestaResponse.findVariabileProcessoByName("statoVariazioneDiBilancio");

		try{
			String statoVariazioneDiBilancio = (String) variabileProcesso.getValore();
			StatoOperativoVariazioneDiBilancio statoOperativoVariazioneDiBilancio = StatoOperativoVariazioneDiBilancio.byVariableName(statoVariazioneDiBilancio);
			
			log.info(methodName, "Nuovo stato della variazione di Bilancio: "+statoOperativoVariazioneDiBilancio);
			variazione.setStatoOperativoVariazioneDiBilancio(statoOperativoVariazioneDiBilancio);
		} catch(RuntimeException re){
			String msg = "Variabile di processo statoVariazioneDiBilancio non valida: "+ToStringBuilder.reflectionToString(variabileProcesso);
			log.error(methodName, msg, re);
			throw new BusinessException(msg);
		}
	}
	
	/**
	 * Controlla se il processo si è passato al task successivo o se è tornato allo stesso stato di partenza.
	 * Se il task è uguale a quello di partenza viene inserito l'errore nella request.
	 *
	 * @param execAzioneRichiestaResponse response del servizio execAzioneRichiesta
	 */
	private void checkProcessoEvolutoAlTaskSuccessivo(ExecAzioneRichiestaResponse execAzioneRichiestaResponse) {
		final String methodName = "checkProcessoEvolutoAlTaskSuccessivo";
		String nomeTaskPrecedente = getNomeTaskFromAttivitaId();
		String nomeTaskSuccessivo = execAzioneRichiestaResponse.getNomeTask();

		log.info(methodName, "nomeTaskPrecedente: " + nomeTaskPrecedente + " nomeTaskSuccessivo: " + nomeTaskSuccessivo);
		if(nomeTaskPrecedente.equals(nomeTaskSuccessivo)){
			//Se il nome del task precedente è uguale a quello successivo significa che il processo ha evoluto il suo stato ma è tornato indietro allo stesso task
			res.setEsito(Esito.FALLIMENTO);
			if(!Boolean.TRUE.equals(res.getIsProvvedimentoPresente())) {
				res.addErrore(ErroreBil.PROVVEDIMENTO_VARIAZIONE_NON_PRESENTE.getErrore());
			}
		}
	}
	
	/**
	 * Ottiene VariazioneDiBilancio_AggiornamentoVariazione
	 * a partire da
	 * VariazioneDiBilancio--1.0--12--VariazioneDiBilancio_AggiornamentoVariazione--it1--mainActivityInstance--noLoop
	 *
	 * @return nome del task
	 */
	private String getNomeTaskFromAttivitaId() {
		try {
			return req.getIdAttivita().split("--")[3];
		} catch(RuntimeException re){
			String msg = "Formato idAttività non valido. Impossibile ricavare il nome del task.";
			log.error("getNomeTaskFromAttivitaId",msg + " idAttivita: "+ req.getIdAttivita(),re);
			throw new BusinessException(msg);
		}

	}

	private void checkProvvedimento() {
		boolean isProvvedimentoPresente = isProvvedimentoPresente();

		res.setIsProvvedimentoPresente(isProvvedimentoPresente);
	}

}
