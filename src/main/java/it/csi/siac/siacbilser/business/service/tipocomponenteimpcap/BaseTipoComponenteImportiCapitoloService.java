/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipocomponenteimpcap;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoComponenteImportiCapitoloDad;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

public abstract class BaseTipoComponenteImportiCapitoloService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES>{

	@Autowired
	protected TipoComponenteImportiCapitoloDad tipoComponenteImportiCapitoloDad;

	@Override
	protected void init() {
		tipoComponenteImportiCapitoloDad.setEnte(ente);
		tipoComponenteImportiCapitoloDad.setLoginOperazione(loginOperazione);
	}
}
