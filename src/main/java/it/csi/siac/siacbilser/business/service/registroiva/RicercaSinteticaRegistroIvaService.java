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
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaRegistroIvaService extends CheckedAccountBaseService<RicercaSinteticaRegistroIva, RicercaSinteticaRegistroIvaResponse> {
	
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
		
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getNumeroPagina()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
		checkCondition(req.getParametriPaginazione().getElementiPerPagina()>0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina parametri paginazione"));
		
		checkNotNull(registroIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(registroIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	@Override
	public RicercaSinteticaRegistroIvaResponse executeService(RicercaSinteticaRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		ListaPaginata<RegistroIva> listaRegistroIva = registroIvaDad.ricercaSinteticaRegistroIva(registroIva,  req.getParametriPaginazione());
		res.setListaRegistroIva(listaRegistroIva);
	}

}
