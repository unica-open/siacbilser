/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.msg;

import it.csi.epay.epaywso.types.ResponseType;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceResponse;

public abstract class PagoPABaseServiceResponse<RES extends ResponseType> extends ServiceResponse {
	private RES res;

	public RES getPagoPAResponse() {
		return res;
	}

	public void setPagoPAResponse(RES res) {
		this.res = res;
	}
	
	public String getErroriMsg(){
		StringBuilder msg = new StringBuilder();
		for(Errore e : getErrori()){
			msg.append("; ").append(e.getTesto());
		}
		return msg.substring(2);
	}
}
