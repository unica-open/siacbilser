/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi;

import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncBaseService;

public abstract class ElaboraFileAttiAmministrativiAsyncBaseService<EFAA extends ElaboraFileAttiAmministrativiService> 
	extends ElaboraFileAsyncBaseService<ElaboraFileAttiAmministrativiAsyncResponseHandler, EFAA> { 
	
	@Override
	protected String getNomeAzione() {
		return "OP-FLUSSO-ATTI_AMM";
	}

}
