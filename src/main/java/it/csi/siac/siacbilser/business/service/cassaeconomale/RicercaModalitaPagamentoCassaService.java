/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.OperazioneDiCassaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassaResponse;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaModalitaPagamentoCassaService extends CheckedAccountBaseService<RicercaModalitaPagamentoCassa, RicercaModalitaPagamentoCassaResponse> {
		
	
	@Autowired
	private OperazioneDiCassaDad operazioneDiCassaDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		operazioneDiCassaDad.setEnte(ente);
		operazioneDiCassaDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaModalitaPagamentoCassaResponse executeService(RicercaModalitaPagamentoCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<ModalitaPagamentoCassa> listaModalitaPagamentoCassa = operazioneDiCassaDad.ricercaModalitaPagamentoCassa();
		res.setModalitaPagamentoCassa(listaModalitaPagamentoCassa);
	}

	
	
}
