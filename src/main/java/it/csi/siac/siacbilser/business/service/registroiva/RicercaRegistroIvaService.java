/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaRegistroIvaService extends CheckedAccountBaseService<RicercaRegistroIva, RicercaRegistroIvaResponse> {
	
	/** The registro iva dad. */
	@Autowired 
	private RegistroIvaDad registroIvaDad;
	
	/** The registro iva. */
	private RegistroIva registroIva;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		registroIva= req.getRegistroIva();
		
		checkNotNull(registroIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro Iva"));
		checkNotNull(registroIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(registroIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
	}
	
	@Override
	protected void init() {
		registroIvaDad.setEnte(registroIva.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	@Override
	public RicercaRegistroIvaResponse executeService(RicercaRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<RegistroIva> listaRegistroIva = registroIvaDad.ricercaRegistroIva(registroIva);
		res.setListaRegistroIva(listaRegistroIva);
		res.setCardinalitaComplessiva(listaRegistroIva.size());
	}

}
