/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativi;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativiResponse;

public abstract class BaseElaboraAttiAmministrativiSyncService 
	extends BaseElaboraAttiAmministrativiService<ElaboraAttiAmministrativi, ElaboraAttiAmministrativiResponse> {
	
	@Override
	protected ElaboraAttiAmministrativiResponse execute(ElaboraAttiAmministrativi edReq) {
		return super.syncExecute(edReq);
	}

	@Override
	protected IntegMapId getResponseMapId() {
		return IntegMapId.ElaboraAttiAmministrativiResponse_ElaboraFileResponse;
	}
}
