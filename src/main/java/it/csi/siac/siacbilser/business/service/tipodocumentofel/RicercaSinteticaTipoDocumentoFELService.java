/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipodocumentofel;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaTipoDocumentoFELService extends BaseTipoDocumentoFELService<RicercaSinteticaTipoDocumentoFEL, RicercaSinteticaTipoDocumentoFELResponse> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkParametriPaginazione(req.getParametriPaginazione());
 
	}
	
	@Override
	protected void init() {
		super.init();

		if (req.getTipoDocFEL() == null) {
			req.setTipoDocFEL(new TipoDocFEL());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaTipoDocumentoFELResponse executeService(RicercaSinteticaTipoDocumentoFEL serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		res.setListaTipoDocFEL( 
				tipoDocumentoFELDad.ricercaSinteticaTipoDocumentoFEL(
						req.getTipoDocFEL(), 
						req.getParametriPaginazione()));
	}

}
