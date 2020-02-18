/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBanca;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBancaResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.soggetto.modpag.Banca;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaBancaService extends AbstractBaseService<RicercaBanca, RicercaBancaResponse> {

	
	
	@Autowired
	SoggettoFinDad soggettoDad;
	

//	@Override
//	@Transactional(readOnly=true)
//	public RicercaBancaResponse executeService(
//			RicercaBanca serviceRequest) {
//		
//		return super.executeService(serviceRequest);
//	}
	
	
	@Override
	public void execute() 
	{
		String abi = req.getIban().substring(5, 10);
		String cab = req.getIban().substring(10, 15);
		
		Banca banca = soggettoDad.ricercaBanca(abi, cab, req.getEnte().getUid());
		
		res.setBanca(banca);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		

		checkNotNull(req.getRichiedente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"));
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkNotNull(req.getIban(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("iban"));
		
	}
}
