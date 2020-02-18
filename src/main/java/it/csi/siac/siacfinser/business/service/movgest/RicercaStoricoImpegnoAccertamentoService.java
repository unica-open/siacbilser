/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dad.StoricoImpegnoAccertamentoDad;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaStoricoImpegnoAccertamentoService extends AbstractBaseService<RicercaStoricoImpegnoAccertamento, RicercaStoricoImpegnoAccertamentoResponse> {
	
	@Autowired
	protected StoricoImpegnoAccertamentoDad storicoImpegnoAccertamentoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		checkEntita(req.getEnte(), "ente");
	}

	
	@Override
	protected void init() {
	}
	
	
	
	@Override
	@Transactional
	public RicercaStoricoImpegnoAccertamentoResponse executeService(RicercaStoricoImpegnoAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "RicercaStoricoImpegnoAccertamento : execute()";
		log.debug(methodName, "- Begin");
		Long conteggioRecords = storicoImpegnoAccertamentoDad.calcolaNumeroStoricoImpegnoAccertamentoDaEstrarre(req.getEnte(), req.getParametroRicercaStoricoImpegnoAccertamento());
			
		List<StoricoImpegnoAccertamento> elencoStoricoImpegnoAccertamento = storicoImpegnoAccertamentoDad.ricercaSinteticaStorico(req.getEnte(), req.getParametroRicercaStoricoImpegnoAccertamento(), req.getNumPagina(), req.getNumRisultatiPerPagina());
		if(elencoStoricoImpegnoAccertamento == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("storico"));
		}
		res.setElencoStoricoImpegnoAccertamento(elencoStoricoImpegnoAccertamento);
		res.setNumRisultati(conteggioRecords.intValue());
		res.setNumPagina(req.getNumPagina());
	}
	
}