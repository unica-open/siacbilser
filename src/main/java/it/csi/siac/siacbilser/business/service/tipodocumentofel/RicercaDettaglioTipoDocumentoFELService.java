/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipodocumentofel;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFELResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;




@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioTipoDocumentoFELService extends BaseTipoDocumentoFELService<RicercaDettaglioTipoDocumentoFEL, RicercaDettaglioTipoDocumentoFELResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//checkEntita(req.getTipoDocFEL(), "tipo documento");
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioTipoDocumentoFELResponse executeService(RicercaDettaglioTipoDocumentoFEL serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		res.setTipoDocFEL(
				tipoDocumentoFELDad.ricercaDettaglioTipoDocumentoFEL(req.getTipoDocFEL()));
	}
}
