/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.msg;

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.TestataTrasmissioneFlussiType;

public class LeggiEsitoFlussi extends PagoPABaseServiceRequest<TestataTrasmissioneFlussiType> {
	
	public LeggiEsitoFlussi(TestataTrasmissioneFlussiType request) {
		this.setPagoPARequest(request);
	}
}
