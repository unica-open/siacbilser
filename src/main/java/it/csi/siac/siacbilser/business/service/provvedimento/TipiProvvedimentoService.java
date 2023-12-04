/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.provvedimento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class TipiProvvedimentoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TipiProvvedimentoService extends CheckedAccountBaseService<TipiProvvedimento, TipiProvvedimentoResponse> {
	
	/** The provvedimento dad. */
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		attoAmministrativoDad.setLoginOperazione(loginOperazione);
		attoAmministrativoDad.setEnte(req.getEnte());
	}
	
	 
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"),true);
		checkCondition(req.getEnte().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"),true);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public TipiProvvedimentoResponse executeService(TipiProvvedimento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		res.setElencoTipi(attoAmministrativoDad.getElencoTipi());
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Gets the provvedimento dad.
	 *
	 * @return the provvedimento dad
	 */
	public AttoAmministrativoDad getAttoAmministrativoDad() {
		return attoAmministrativoDad;
	}

	/**
	 * Sets the provvedimento dad.
	 *
	 * @param attoAmministrativoDad the new provvedimento dad
	 */
	public void setAttoAmministrativoDad(AttoAmministrativoDad attoAmministrativoDad) {
		this.attoAmministrativoDad = attoAmministrativoDad;
	}

	
}
