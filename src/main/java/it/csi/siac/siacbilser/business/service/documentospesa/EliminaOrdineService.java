/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.OrdineDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOrdineResponse;
import it.csi.siac.siacfin2ser.model.Ordine;

/**
 * Elimina un {@link Ordine}.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class EliminaOrdineService extends CheckedAccountBaseService<EliminaOrdine, EliminaOrdineResponse> {
	
	@Autowired 
	private OrdineDad ordineDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getOrdine(), "Ordine");
	}
	
	@Override
	protected void init() {
		ordineDad.setEnte(ente);
		ordineDad.setLoginOperazione(loginOperazione);
	}
	
	

	@Override
	@Transactional
	public EliminaOrdineResponse executeService(EliminaOrdine serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		ordineDad.eliminaOrdine(req.getOrdine());
		res.setOrdine(req.getOrdine());
	}

	
}
