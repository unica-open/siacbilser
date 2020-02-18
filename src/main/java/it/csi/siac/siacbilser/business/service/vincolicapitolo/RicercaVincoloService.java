/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.vincolicapitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.VincoloCapitoliDad;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class RicercaVincoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaVincoloService extends CheckedAccountBaseService<RicercaVincolo, RicercaVincoloResponse> {
	
	/** The vincolo capitoli dad. */
	@Autowired
	private VincoloCapitoliDad vincoloCapitoliDad;
	@Autowired private BilancioDad bilancioDad;
	private Bilancio bilancio;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getVincolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Vincolo"));
		checkNotNull(req.getVincolo().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente vincolo"));		
		checkCondition(req.getVincolo().getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente vincolo"));		

		checkNotNull(req.getParametriPaginazione(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Parametri paginazione"));
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		vincoloCapitoliDad.setEnte(req.getVincolo().getEnte());
		vincoloCapitoliDad.setLoginOperazione(loginOperazione);
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaVincoloResponse executeService(RicercaVincolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		caricaBilancio();
		ListaPaginata<VincoloCapitoli> vincoli = vincoloCapitoliDad.ricercaSinteticaVincoloCapitoli(req.getVincolo(), req.getCapitolo() , req.getTipiCapitolo(), bilancio, req.getParametriPaginazione());
		res.setVincoloCapitoli(vincoli);
		res.setCardinalitaComplessiva(vincoli.size());
	}
	
	
	/**
	 * Carica bilancio.
	 */
	private void caricaBilancio() {
		this.bilancio = req.getBilancio();
		if(this.bilancio == null || this.bilancio.getUid() == 0) {
			return;
		}
		this.bilancio = bilancioDad.getBilancioByUid(this.bilancio.getUid());
	}

}
