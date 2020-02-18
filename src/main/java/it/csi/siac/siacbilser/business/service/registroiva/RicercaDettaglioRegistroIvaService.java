/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioRegistroIvaService extends CheckedAccountBaseService<RicercaDettaglioRegistroIva, RicercaDettaglioRegistroIvaResponse> {
	
	/** The registro iva dad. */
	@Autowired
	private RegistroIvaDad registroIvaDad;
	
	/** The registro. */
	private RegistroIva registro;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		registro=req.getRegistroIva();
		
		checkNotNull(registro, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro"));
		checkCondition(registro.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid registro"));
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	@Override
	public RicercaDettaglioRegistroIvaResponse executeService(RicercaDettaglioRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		RegistroIva registroIva = registroIvaDad.findRegistroIvaById(registro.getUid());
		res.setRegistroIva(registroIva);
	}

}
