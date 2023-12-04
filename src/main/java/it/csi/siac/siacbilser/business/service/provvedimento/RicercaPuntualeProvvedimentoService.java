/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.provvedimento;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaPuntualeProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaPuntualeProvvedimentoResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeProvvedimentoService 
	extends BaseRicercaProvvedimentoService<RicercaPuntualeProvvedimento, RicercaPuntualeProvvedimentoResponse> {

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public RicercaPuntualeProvvedimentoResponse executeService(RicercaPuntualeProvvedimento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaAtti(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ricerca atti"),true);
		
		if (req.getRicercaAtti().getUid() == null) {
			checkNotNull(req.getRicercaAtti().getAnnoAtto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto"), true);
			checkNotNull(req.getRicercaAtti().getNumeroAtto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero atto"), true);
			checkNotNull(req.getRicercaAtti().getTipoAtto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo atto"), true);
		}
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		res.setAttoAmministrativo(attoAmministrativoDad.ricercaPuntuale(req.getRicercaAtti()));
	}

}
