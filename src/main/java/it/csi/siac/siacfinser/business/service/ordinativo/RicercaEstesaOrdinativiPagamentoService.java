/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaOrdinativiPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaOrdinativiPagamentoResponse;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;
import it.csi.siac.siacfinser.model.ordinativo.RicercaEstesaOrdinativoDiPagamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaEstesaOrdinativiPagamentoService extends AbstractBaseService<RicercaEstesaOrdinativiPagamento, RicercaEstesaOrdinativiPagamentoResponse> {


	@Autowired
	OrdinativoPagamentoDad ordinativoPagamentoDad; 
	
	@Override
	protected void init() {
		final String methodName="RicercaEstesaOrdinativiPagamentoService : init()";
		log.debug(methodName, " - Begin");

	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaEstesaOrdinativiPagamentoResponse executeService(RicercaEstesaOrdinativiPagamento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	public void execute() {
		String methodName = "RicercaEstesaOrdinativiPagamentoService - execute()";
		log.debug(methodName, " - Begin");
		
		ente = req.getEnte();
		
		RicercaProvvedimentoResponse resAtto = ricercaProvvedimento(req.getAtto(), req.getRichiedente());
		
		List<RicercaEstesaOrdinativoDiPagamento> ordinativi = ordinativoPagamentoDad.ricercaEstesaOrdinativiDiPagamento(req.getAnnoEsercizio(), resAtto.getListaAttiAmministrativi().get(0), ente.getUid());

		if(ordinativi == null || ordinativi.isEmpty())
			throw new BusinessException(ErroreCore.NESSUN_DATO_REPERITO.getErrore(), Esito.FALLIMENTO);

		// Response per esito KO
		res.setEsito(Esito.SUCCESSO);
		res.setNumRisultati(ordinativi.size());
		res.setNumPagina(req.getNumPagina());
		res.setOrdinativiPagamento(ordinativi);
			

	}
	

}