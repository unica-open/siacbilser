/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.StoricoImpegnoAccertamentoDad;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaStoricoImpegnoAccertamentoPerChiaveService extends AbstractBaseService<RicercaStoricoImpegnoAccertamentoPerChiave, RicercaStoricoImpegnoAccertamentoPerChiaveResponse> {
	
	@Autowired
	protected StoricoImpegnoAccertamentoDad storicoImpegnoAccertamentoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getStoricoImpegnoAccertamento(), "storico");
	}

	
	@Override
	protected void init() {
	}
	
	
	
	@Override
	@Transactional
	public RicercaStoricoImpegnoAccertamentoPerChiaveResponse executeService(RicercaStoricoImpegnoAccertamentoPerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "InserisceImpegniService : execute()";
		log.debug(methodName, "- Begin");
		StoricoImpegnoAccertamento ricercaDettaglio = storicoImpegnoAccertamentoDad.ricercaDettaglio(req.getStoricoImpegnoAccertamento());
		if(ricercaDettaglio == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("storico"));
		}
		res.setStoricoImpegnoAccertamento(ricercaDettaglio);
		res.setEsito(Esito.SUCCESSO);
	}
	
}