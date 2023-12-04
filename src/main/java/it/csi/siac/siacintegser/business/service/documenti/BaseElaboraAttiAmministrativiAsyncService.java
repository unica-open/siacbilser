/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativi;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativiAsyncResponse;

public abstract class BaseElaboraAttiAmministrativiAsyncService 
	extends BaseElaboraAttiAmministrativiService<ElaboraAttiAmministrativi, ElaboraAttiAmministrativiAsyncResponse> {

	@Override
	protected ElaboraAttiAmministrativiAsyncResponse execute(ElaboraAttiAmministrativi edReq) {
		return super.asyncExecute(edReq);
	}

	@Override
	protected IntegMapId getResponseMapId() {
		return IntegMapId.ElaboraAttiAmministrativiAsyncResponse_AsyncServiceResponse;
	}
}
