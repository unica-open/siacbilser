/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseRicercaMutuoServiceRequest;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseRicercaMutuoServiceResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

public abstract class BaseRicercaMutuoService<BRMSREQ extends BaseRicercaMutuoServiceRequest, BRMSRES extends BaseRicercaMutuoServiceResponse> 
	extends BaseMutuoService<BRMSREQ, BRMSRES> {
	
	@Override
	@Transactional
	public BRMSRES executeService(BRMSREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
	}
	@Override
	protected void init() {
		mutuo.setEnte(ente);
	}	

}
