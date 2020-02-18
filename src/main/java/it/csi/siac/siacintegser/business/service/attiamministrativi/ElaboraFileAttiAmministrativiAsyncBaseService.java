/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncBaseService;

public abstract class ElaboraFileAttiAmministrativiAsyncBaseService<EFAA extends ElaboraFileAttiAmministrativiService> 
	extends ElaboraFileAsyncBaseService<ElaboraFileAttiAmministrativiAsyncResponseHandler, EFAA> { 
	
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
		// Nessuna operazione da effettuare
	}
	
	@Override
	protected String getNomeAzione() {
		return "OP-FLUSSO-ATTI_AMM";
	}

}
