/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.quadroeconomico;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaQuadroEconomicoValido;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaQuadroEconomicoValidoResponse;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;



/**
 * Servizio di ricerca dei Quadri Economico validi
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/01/2018
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuadroEconomicoValidoService extends CrudQuadroEconomicoBaseService<RicercaQuadroEconomicoValido, RicercaQuadroEconomicoValidoResponse> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {
	}
	
	@Override
	@Transactional
	public RicercaQuadroEconomicoValidoResponse executeService(RicercaQuadroEconomicoValido serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		List<QuadroEconomico> listaQuadroEconomico = quadroEconomicoDad.ricercaQuadroEconomicoValidi();
		res.setListaQuadroEconomico(listaQuadroEconomico);
		res.setCardinalitaComplessiva(listaQuadroEconomico.size());
		
	}

}
