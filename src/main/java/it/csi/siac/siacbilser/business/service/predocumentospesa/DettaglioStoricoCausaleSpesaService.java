/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CausaleDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;


// TODO: Auto-generated Javadoc
/**
 * The Class DettaglioStoricoCausaleSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DettaglioStoricoCausaleSpesaService extends CheckedAccountBaseService<DettaglioStoricoCausaleSpesa, DettaglioStoricoCausaleSpesaResponse> {
	
	/** The causale dad. */
	@Autowired
	private CausaleDad causaleDad;
	
	/** The causale spesa. */
	private CausaleSpesa causaleSpesa;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		causaleSpesa = req.getCausale();
		checkNotNull(causaleSpesa, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("causale"));
		checkCondition(causaleSpesa.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid causale"));

		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public DettaglioStoricoCausaleSpesaResponse executeService(DettaglioStoricoCausaleSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<CausaleSpesa> listaCausaleSpesa = causaleDad.dettaglioStoricoCausaleSpesa(causaleSpesa);
		res.setCausaliSpesa(listaCausaleSpesa);
		
	}

}
