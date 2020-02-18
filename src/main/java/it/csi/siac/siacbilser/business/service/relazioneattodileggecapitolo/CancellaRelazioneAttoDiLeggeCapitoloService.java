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
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.AttoDiLeggeDad;
import it.csi.siac.siacbilser.integration.exception.RelazioneAttoCapitoloNonTrovatoException;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class CancellaRelazioneAttoDiLeggeCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CancellaRelazioneAttoDiLeggeCapitoloService extends CheckedAccountBaseService<CancellaRelazioneAttoDiLeggeCapitolo, CancellaRelazioneAttoDiLeggeCapitoloResponse> {
	
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
		checkNotNull(req.getAttoDiLeggeCapitolo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto di legge capitolo"),true);
		checkNotNull(req.getAttoDiLeggeCapitolo().getUid(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid relazione atto di legge capitolo"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public CancellaRelazioneAttoDiLeggeCapitoloResponse executeService(CancellaRelazioneAttoDiLeggeCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		String methodName = "execute";

		try {
			AttoDiLeggeCapitolo rel = attoDiLeggeDad.cancellaRelazioneAttoCapitolo(req.getAttoDiLeggeCapitolo());
			res.setAttoDiLeggeCapitolo(rel);
			res.setEsito(Esito.SUCCESSO);
		} catch (RelazioneAttoCapitoloNonTrovatoException e) {
			log.warn(methodName, "RelazioneAttoCapitoloNonTrovatoException", e);
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("cancellazione", String.format("Relazione Atto di Legge - capitolo: %s", req.getAttoDiLeggeCapitolo().getUid())));
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
