/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaCronoprogrammaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaCronoprogrammaService extends CheckedAccountBaseService<AnnullaCronoprogramma, AnnullaCronoprogrammaResponse> {

	/** The cronoprogramma. */
	private Cronoprogramma cronoprogramma;
	
	/** The cronoprogramma dad. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		//parametri del cronoprogramma
		cronoprogramma  = req.getCronoprogramma();
		
		checkNotNull(cronoprogramma, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma"));		
		checkCondition(cronoprogramma.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid cronoprogramma"));		

				
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		cronoprogrammaDad.aggiornaStatoOperativoCronoprogramma(cronoprogramma, StatoOperativoCronoprogramma.ANNULLATO);
		cronoprogramma.setStatoOperativoCronoprogramma(StatoOperativoCronoprogramma.ANNULLATO);
		res.setCronoprogramma(cronoprogramma);
	}

}
