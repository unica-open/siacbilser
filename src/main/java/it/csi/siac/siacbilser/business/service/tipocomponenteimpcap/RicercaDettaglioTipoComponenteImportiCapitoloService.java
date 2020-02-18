/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipocomponenteimpcap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;




@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioTipoComponenteImportiCapitoloService extends BaseTipoComponenteImportiCapitoloService<RicercaDettaglioTipoComponenteImportiCapitolo, RicercaDettaglioTipoComponenteImportiCapitoloResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getTipoComponenteImportiCapitolo(), "tipo componente importi capitolo");
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioTipoComponenteImportiCapitoloResponse executeService(RicercaDettaglioTipoComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		res.setTipoComponenteImportiCapitolo(
				tipoComponenteImportiCapitoloDad.ricercaDettaglioTipoComponenteImportiCapitolo(req.getTipoComponenteImportiCapitolo()));
	}
}
