/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionicodifica;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.processi.GestoreProcessiVariazioneBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.TipoAzione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceVariazioneCodificheService.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceVariazioneCodificheService extends VariazioneCodificheBaseService<InserisceVariazioneCodifiche, InserisceVariazioneCodificheResponse> {


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {

		variazione = req.getVariazioneCodificaCapitolo();
		checkParamVariazione();


		//checkNotNull(req.getAnnullaVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annulla variazione"));
		checkNotNull(req.getInvioOrganoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("invio organo amministrativo"));
		checkNotNull(req.getInvioOrganoLegislativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("invio organo legislativo"));

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
	public InserisceVariazioneCodificheResponse executeService(InserisceVariazioneCodifiche serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		// CR-2304: soppiantato dal controllo all'ultimo step
//		checkNecessarioAttoAmministrativo();

		//caricaDettaglioImportiCapitoli();
		//checkImporti();

		checkProvvedimento();

		Integer numeroVariazione = variazioniDad.staccaNumeroVariazione();
		variazione.setNumero(numeroVariazione);

		//SIAC-8332-REGP
		variazione.setStatoOperativoVariazioneDiBilancio(GestoreProcessiVariazioneBilancio.getStatoAvvioProcessoVariazioneDiCodfiche());
		
		variazioniDad.inserisciVariazioneCodifica(variazione);


		res.setVariazioneCodificaCapitolo(variazione);

		avviaProcessoVariazioneDiBilancio();

	}



	/**
	 * Avvia processo variazione di bilancio.
	 * @deprecated by SIAC-8332
	 */
	@Deprecated
	private void avviaProcessoVariazioneDiBilancio() {
		ExecAzioneRichiesta execAzioneRichiesta = new ExecAzioneRichiesta();
		execAzioneRichiesta.setRichiedente(req.getRichiedente());
		execAzioneRichiesta.setDataOra(new Date());

		//AzioneRichiesta azioneRichiesta = getAzioneRichiesta(36);
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		Azione azione = new Azione();
		azioneRichiesta.setAzione(azione);
		azione.setTipo(TipoAzione.AVVIO_PROCESSO);
		azione.setNomeProcesso("VariazioneDiBilancio");
		azione.setNomeTask("VariazioneDiBilancio-InserimentoVariazione");	//VariazioneDiBilancio-AggiornamentoVariazione (per aggiornamento)	//VariazioneDiBilancio-DefinizioneDellaVariazione

		setVariabileProcesso(azioneRichiesta, "tipoVariazioneBilancio", "Codifica"); //Tipo_Variazione_Bilancio
		setVariabileProcesso(azioneRichiesta, "descrizione", variazione.getNumero() + " - " + variazione.getDescrizione() + " - "+ variazione.getTipoVariazione().getDescrizione());// + " "+ variazione.getNote());
		setVariabileProcesso(azioneRichiesta, "descrizioneBreve", variazione.getNumero() + " - " + variazione.getDescrizione() + " - "+ variazione.getTipoVariazione().getDescrizione());
		setVariabileProcesso(azioneRichiesta, "siacAnnoEsercizioProcesso","SIAC-ANNO-ESERCIZIO-" + variazione.getBilancio().getAnno()); //Testo
		setVariabileProcesso(azioneRichiesta, "siacEnteProprietarioProcesso","SIAC-ENTE-PROPRIETARIO-" + variazione.getEnte().getUid()); //Testo
		setVariabileProcesso(azioneRichiesta, "variazioneDiBilancioDem", variazione.getUid() +"|"+variazione.getNumero());
		setVariabileProcesso(azioneRichiesta, "siacSacProcesso", "strutture"); //sac sta per StrutturaAmministravivaContabile
		setVariabileProcesso(azioneRichiesta, "invioGiunta", req.getInvioOrganoAmministrativo()); //Boolean //invioOrganoAmministrativo
		setVariabileProcesso(azioneRichiesta, "invioConsiglio", req.getInvioOrganoLegislativo()); //Boolean //invioOrganoLegislativo
		setVariabileProcesso(azioneRichiesta, "annullaVariazione", Boolean.FALSE); //Boolean
		
		// CR-2304
		setVariabileProcesso(azioneRichiesta, "quadraturaVariazioneDiBilancio", res.getIsProvvedimentoPresente()); // Boolean

		execAzioneRichiesta.setAzioneRichiesta(azioneRichiesta);

		ExecAzioneRichiestaResponse execAzioneRichiestaResponse = coreService.execAzioneRichiesta(execAzioneRichiesta);
		log.logXmlTypeObject(execAzioneRichiestaResponse, "Risposta ottenuta dal servizio ExecAzioneRichiesta.");
		checkServiceResponseFallimento(execAzioneRichiestaResponse);

	}


	private void checkProvvedimento() {
		boolean isProvvedimentoPresente = isProvvedimentoPresente();

		res.setIsProvvedimentoPresente(isProvvedimentoPresente);
	}
}
