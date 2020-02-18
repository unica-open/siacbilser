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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.AttoDiLeggeDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaRelazioneAttoDiLeggeCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaRelazioneAttoDiLeggeCapitoloService extends CheckedAccountBaseService<RicercaRelazioneAttoDiLeggeCapitolo, RicercaRelazioneAttoDiLeggeCapitoloResponse> {
	
	/** The atto di legge dad. */
	@Autowired
	AttoDiLeggeDad attoDiLeggeDad = null;

	 
	
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
		
		checkNotNull(req.getEnte(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkNotNull(req.getRicercaAttiDiLeggeCapitolo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ricerca"),true);
		checkNotNull(req.getRicercaAttiDiLeggeCapitolo().getBilancio(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"),true);
		checkCondition(req.getRicercaAttiDiLeggeCapitolo().getBilancio().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"),true);
		checkNotNull(req.getRicercaAttiDiLeggeCapitolo().getCapitolo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"),true);
		checkCondition(req.getRicercaAttiDiLeggeCapitolo().getCapitolo().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"),true);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public RicercaRelazioneAttoDiLeggeCapitoloResponse executeService(RicercaRelazioneAttoDiLeggeCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		res.setElencoAttiLeggeCapitolo(attoDiLeggeDad.ricercaRelazioniCapitolo(req.getRicercaAttiDiLeggeCapitolo()));
		
		res.setEsito(Esito.SUCCESSO);
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
