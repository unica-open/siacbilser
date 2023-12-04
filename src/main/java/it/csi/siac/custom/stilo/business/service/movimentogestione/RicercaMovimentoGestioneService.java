/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service.movimentogestione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.custom.stilo.frontend.webservice.msg.RicercaMovimentoGestione;
import it.csi.siac.custom.stilo.frontend.webservice.msg.RicercaMovimentoGestioneResponse;
import it.csi.siac.custom.stilo.integration.dad.MovimentoGestioneStiloDad;
import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfinser.model.MovimentoGestione;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMovimentoGestioneService extends ExtendedBaseService<RicercaMovimentoGestione, RicercaMovimentoGestioneResponse>{
	
	@Autowired
	private MovimentoGestioneStiloDad movimentoGestioneStiloDad;

	@Override
	protected void init() {
		final String methodName = "RicercaMovimentoGestioneService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaMovimentoGestioneResponse executeService(RicercaMovimentoGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
	}
	
	@Override
	public void execute() {
		List<MovimentoGestione> movGestList = movimentoGestioneStiloDad.ricercaMovimentiGestionePerProvvedimento(req.getAnnoBilancio(),req.getNumeroProvvedimento(),
				req.getAnnoProvvedimento(),	 req.getTipoProvvedimento() , req.getStrutturaAmministrativoContabileProvvedimento() ,req.getEnteProprietarioId());
		res.setMovimentiGestione(movGestList);
	}
}


