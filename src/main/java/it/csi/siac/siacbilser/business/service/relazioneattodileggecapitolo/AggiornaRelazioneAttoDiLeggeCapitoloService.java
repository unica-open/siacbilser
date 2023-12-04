/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.AttoDiLeggeDad;
import it.csi.siac.siacbilser.integration.exception.RelazioneAttoCapitoloNonTrovatoException;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaRelazioneAttoDiLeggeCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRelazioneAttoDiLeggeCapitoloService extends CheckedAccountBaseService<AggiornaRelazioneAttoDiLeggeCapitolo, AggiornaRelazioneAttoDiLeggeCapitoloResponse> {
	
	/** The atto di legge dad. */
	@Autowired
	AttoDiLeggeDad attoDiLeggeDad = null;

	/** The ricerca atto di legge service. */
	@Autowired
	RicercaRelazioneAttoDiLeggeCapitoloService ricercaAttoDiLeggeService = null;

	 
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		attoDiLeggeDad.setLoginOperazione(loginOperazione);
		attoDiLeggeDad.setEnte(req.getEnte());
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getAttoDiLegge(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto di legge"),true);
		checkNotNull(req.getAttoDiLegge().getUid(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("identificativo atto di legge"),true);
		checkNotNull(req.getCapitolo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"));
		checkNotNull(req.getCapitolo().getUid(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("identificativo capitolo"));		
		checkNotNull(req.getAttoDiLeggeCapitolo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("relazione atto capitolo"));		
		checkNotNull(req.getAttoDiLeggeCapitolo().getUid(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("identificativo relazione atto capitolo"));
		
		checkCondition(!(req.getAttoDiLeggeCapitolo().getDataFineFinanz() == null &&
						req.getAttoDiLeggeCapitolo().getDataInizioFinanz() == null &&
						req.getAttoDiLeggeCapitolo().getDescrizione() == null &&
						req.getAttoDiLeggeCapitolo().getGerarchia() == null), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("non e' stato specificato alcun attributo della relazione da aggiornare"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AggiornaRelazioneAttoDiLeggeCapitoloResponse executeService(AggiornaRelazioneAttoDiLeggeCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		String methodName = "execute";
		
		// Per sicurezza
		if(req.getAttoDiLeggeCapitolo().getDescrizione().length() > 500) {
			req.getAttoDiLeggeCapitolo().setDescrizione(req.getAttoDiLeggeCapitolo().getDescrizione().substring(0, Math.min(req.getAttoDiLeggeCapitolo().getDescrizione().length(), 500)));
		}
		
		try {
			AttoDiLeggeCapitolo rel = attoDiLeggeDad.updateRelazioneAttoCapitolo(req.getAttoDiLegge(), req.getAttoDiLeggeCapitolo(), req.getCapitolo());
			res.setAttoDiLeggeCapitolo(rel);
			res.setEsito(Esito.SUCCESSO);
		} catch (RelazioneAttoCapitoloNonTrovatoException e) {
			log.warn(methodName, "RelazioneAttoCapitoloNonTrovatoException", e);
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("aggiornamento", String.format("Relazione Atto di Legge - capitolo: %s", req.getAttoDiLeggeCapitolo().getUid())));
			res.setEsito(Esito.FALLIMENTO);
		}
		
	}

	/**
	 * Gets the atto di legge dad.
	 *
	 * @return the atto di legge dad
	 */
	public AttoDiLeggeDad getAttoDiLeggeDad() {
		return attoDiLeggeDad;
	}


	/**
	 * Sets the atto di legge dad.
	 *
	 * @param attoDiLeggeDad the new atto di legge dad
	 */
	public void setAttoDiLeggeDad(AttoDiLeggeDad attoDiLeggeDad) {
		this.attoDiLeggeDad = attoDiLeggeDad;
	}

	
}
