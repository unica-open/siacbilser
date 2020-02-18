/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.msg;

import it.csi.siac.siaccorser.model.ServiceRequest;

public abstract class PagoPABaseServiceRequest<REQ> extends ServiceRequest {
	private REQ req;

	public REQ getPagoPARequest() {
		return req;
	}

	public void setPagoPARequest(REQ request) {
		this.req = request;
	}

}
