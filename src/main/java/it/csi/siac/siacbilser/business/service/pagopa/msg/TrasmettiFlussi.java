/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.msg;

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.TrasmettiFlussiPagoPARequest;

public class TrasmettiFlussi extends PagoPABaseServiceRequest<TrasmettiFlussiPagoPARequest> {

	public TrasmettiFlussi(TrasmettiFlussiPagoPARequest request) {
		this.setPagoPARequest(request);
	}
}
