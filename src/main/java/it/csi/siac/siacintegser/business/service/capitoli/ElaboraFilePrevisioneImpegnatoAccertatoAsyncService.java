/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.capitoli;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncBaseService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFilePrevisioneImpegnatoAccertatoAsyncService extends ElaboraFileAsyncBaseService<ElaboraFilePrevisioneImpegnatoAccertatoAsyncResponseHandler, ElaboraFilePrevisioneImpegnatoAccertatoService> { 

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		String methodName = "checkServiceParam";
		service.checkServiceParam();
		log.debug(methodName, "Errori riscontrati: "+ service.getServiceResponse().getErrori());
		res.addErrori(service.getServiceResponse().getErrori());
	}

	@Override
	protected void preStartService() {
		//Nessun check sui dati da effettuare.
	}

	@Override
	protected void postStartService() {

	}
	
	@Override
	protected String getNomeAzione() {
		return "OP-FLUSSO-PREVISIONE-IMP-ACC";
	}

}
