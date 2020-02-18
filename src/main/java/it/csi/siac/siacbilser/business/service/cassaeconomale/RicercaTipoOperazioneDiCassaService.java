/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoOperazioneDiCassaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoOperazioneDiCassaService extends CheckedAccountBaseService<RicercaTipoOperazioneDiCassa, RicercaTipoOperazioneDiCassaResponse> {
		
	
	@Autowired
	private TipoOperazioneDiCassaDad tipoOperazioneDiCassaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCassaEconomale(), "cassa economale", false);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		tipoOperazioneDiCassaDad.setEnte(ente);
		tipoOperazioneDiCassaDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public RicercaTipoOperazioneDiCassaResponse executeService(RicercaTipoOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<TipoOperazioneCassa> listaTipiOperazione = tipoOperazioneDiCassaDad.ricercaTipoOperazioneCassa(req.getCassaEconomale());
		res.setTipiOperazione(listaTipiOperazione);
	}

	
	
}
