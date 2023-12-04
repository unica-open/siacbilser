/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipodocumentofel;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoDocumentoFELDad;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

public abstract class BaseTipoDocumentoFELService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES>{

	
	@Autowired
	protected  TipoDocumentoFELDad tipoDocumentoFELDad;

	@Override
	protected void init() {
		tipoDocumentoFELDad.setEnte(ente);
		tipoDocumentoFELDad.setLoginOperazione(loginOperazione);
	}
 
}
