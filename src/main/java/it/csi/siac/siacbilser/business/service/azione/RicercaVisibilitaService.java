/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.azione;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.azione.RicercaVisibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.azione.RicercaVisibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.visibilita.VisibilitaDad;
import it.csi.siac.siacbilser.model.visibilita.Visibilita;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Servizio per la ricerca dell'azione per chiave
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaVisibilitaService extends CheckedAccountBaseService<RicercaVisibilita, RicercaVisibilitaResponse> {
	
	@Autowired
	private VisibilitaDad visibilitaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getVisibilita(), "azione");
		checkCondition(
				(req.getVisibilita().getAzione() != null && req.getVisibilita().getAzione().getUid() != 0)
				|| StringUtils.isNotBlank(req.getVisibilita().getFunzionalita()),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("azione o funzionalita'"));
	}
	
	@Override
	protected void init() {
		visibilitaDad.setEnte(ente, false);
	}
	
	@Override
	@Transactional(readOnly= true)
	public RicercaVisibilitaResponse executeService(RicercaVisibilita serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		List<Visibilita> visibilita = visibilitaDad.getListVisibilita(req.getVisibilita(), req.getVisibilitaModelDetails());
		res.setListaVisibilita(visibilita);
	}

}
