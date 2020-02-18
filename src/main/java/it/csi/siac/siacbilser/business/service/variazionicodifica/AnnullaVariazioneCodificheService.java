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

import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaVariazioneCodificheResponse;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiesta;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.TipoAzione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaVariazioneCodificheService.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaVariazioneCodificheService extends VariazioneCodificheBaseService<AnnullaVariazioneCodifiche, AnnullaVariazioneCodificheResponse> {
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		variazione = req.getVariazioneCodificaCapitolo();
		checkParamVariazione();
		
		checkNotNull(req.getIdAttivita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id attivita"));
		
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
	public AnnullaVariazioneCodificheResponse executeService(AnnullaVariazioneCodifiche serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		variazione.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.ANNULLATA);
		variazioniDad.aggiornaVariazioneCodificaCapitolo(variazione);		
		
		res.setVariazioneCodificaCapitolo(variazione);
		
		annullaProcessoVariazioneDiBilancio();
	}
	
	
	/**
	 * Annulla processo variazione di bilancio.
	 */
	private void annullaProcessoVariazioneDiBilancio() {
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
		
		setVariabileProcesso(azioneRichiesta, "annullaVariazione", Boolean.TRUE); //Boolean
		
				
		execAzioneRichiesta.setAzioneRichiesta(azioneRichiesta);
		coreService.execAzioneRichiesta(execAzioneRichiesta);
	}

}
