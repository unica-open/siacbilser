/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaReversaliOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaReversaliOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollegaReversaliOrdinativoPagamentoService
		extends AbstractBaseService<CollegaReversaliOrdinativoPagamento, CollegaReversaliOrdinativoPagamentoResponse> {

	@Autowired
	private OrdinativoPagamentoDad ordinativoPagamentoDad;
	
	@Override
	protected void init() {
		final String methodName = "ReintroitoOrdinativoPagamentoService : init()";
		log.debug(methodName, "- Begin");
	}

	@Override
	@Transactional
	public CollegaReversaliOrdinativoPagamentoResponse executeService(
			CollegaReversaliOrdinativoPagamento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute() {
		ordinativoPagamentoDad.collegaOrdinativi(req.getIdOrdinativoPagamento(), req.getIdOrdinativiIncasso(),
				TipoRelazione.ORDINATIVO_SUBORDINATO.getCodice(), req.getRichiedente());
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getIdOrdinativoPagamento(), "idOrdinativoPagamento");
		checkNotNull(req.getIdOrdinativiIncasso(), "idOrdinativiIncasso");
		checkCondition(req.getIdOrdinativiIncasso().length > 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("idOrdinativiIncasso"));
	}
}
