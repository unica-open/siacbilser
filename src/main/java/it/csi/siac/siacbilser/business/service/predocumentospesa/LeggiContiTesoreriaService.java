/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ContoTesoreriaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;

// TODO: Auto-generated Javadoc
/**
 * The Class LeggiContiTesoreriaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiContiTesoreriaService extends CheckedAccountBaseService< LeggiContiTesoreria,  LeggiContiTesoreriaResponse> {
	
	/** The conto tesoreria dad. */
	@Autowired
	private ContoTesoreriaDad contoTesoreriaDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public LeggiContiTesoreriaResponse executeService(LeggiContiTesoreria serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<ContoTesoreria> listaContoTesoreria = contoTesoreriaDad.ricercaContiTesoreriaByEnte(req.getEnte());
		
		res.setContiTesoreria(listaContoTesoreria);
		res.setCardinalitaComplessiva(listaContoTesoreria.size());
		res.setDataOra(new Date());
		res.setEsito(Esito.SUCCESSO);
	}

}
