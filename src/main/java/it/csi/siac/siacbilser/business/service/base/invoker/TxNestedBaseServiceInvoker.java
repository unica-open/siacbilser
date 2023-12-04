/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.invoker;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Invoca un BaseService.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public class TxNestedBaseServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse> implements ServiceInvoker<REQ, RES> {
	
	private ExtendedBaseService<REQ, RES> service;
	
	/**
	 * Instantiates a new base service invoker.
	 *
	 * @param service the service
	 */
	public TxNestedBaseServiceInvoker(ExtendedBaseService<REQ, RES> service) {
		this.service = service;
	}

	@Override
	public RES invokeService(REQ req) {
		return service.executeServiceTxNested(req);
	}

}
