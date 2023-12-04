/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.msg;

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.RicercaProvvisoriPagoPARequest;

public class RicercaProvvisori extends PagoPABaseServiceRequest<RicercaProvvisoriPagoPARequest> {

	public RicercaProvvisori(RicercaProvvisoriPagoPARequest request) {
		this.setPagoPARequest(request);
	}
}
