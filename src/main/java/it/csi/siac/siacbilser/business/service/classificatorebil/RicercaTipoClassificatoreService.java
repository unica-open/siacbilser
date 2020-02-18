/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoClassificatoreService extends CheckedAccountBaseService<RicercaTipoClassificatore, RicercaTipoClassificatoreResponse> {

	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAnno(), "anno", false);
		checkNotNull(req.getTipologieClassificatore(), "tipologie classificatori");
		checkCondition(!req.getTipologieClassificatore().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipologie classificatori"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaTipoClassificatoreResponse executeService(RicercaTipoClassificatore serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		List<TipoClassificatore> tipiClassificatore = classificatoriDad.findByTipologiaClassificatore(ente, req.getTipologieClassificatore(), req.getAnno());
		res.setTipiClassificatore(tipiClassificatore);
		res.setCardinalitaComplessiva(tipiClassificatore.size());
	}

}
