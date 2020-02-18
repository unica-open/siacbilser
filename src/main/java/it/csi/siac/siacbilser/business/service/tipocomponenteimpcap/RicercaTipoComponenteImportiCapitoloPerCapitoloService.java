/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipocomponenteimpcap;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitoloResponse;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoComponenteImportiCapitoloPerCapitoloService extends BaseTipoComponenteImportiCapitoloService<RicercaTipoComponenteImportiCapitoloPerCapitolo, RicercaTipoComponenteImportiCapitoloPerCapitoloResponse> {

	@Override
	@Transactional(readOnly = true)
	public RicercaTipoComponenteImportiCapitoloPerCapitoloResponse executeService(RicercaTipoComponenteImportiCapitoloPerCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitolo(), "capitolo");
		checkNotNull(req.getAnnoBilancio(), "Anno Bilancio");
	}
	
	
	@Override
	protected void execute() {
		// FIXME
		List<TipoComponenteImportiCapitolo> listaTipoComponenteImportiCapitolo = tipoComponenteImportiCapitoloDad.ricercaTipoComponenteImportiCapitolo(
				new TipoComponenteImportiCapitolo(),
				null,
				null,
				null,
				req.getAnnoBilancio(),
				req.isSoloValidiPerBilancio(),
				TipoComponenteImportiCapitoloModelDetail.values());
		res.setListaTipoComponenteImportiCapitolo(listaTipoComponenteImportiCapitolo);
	}
}
