/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumento;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoAsyncResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraDocumentoGenericoAsyncService extends BaseElaboraDocumentoGenericoService<ElaboraDocumento, ElaboraDocumentoAsyncResponse> {

	@Override
	protected ElaboraDocumentoAsyncResponse execute(ElaboraDocumento edReq) {
		return super.asyncExecute(edReq);
	}

	@Override
	protected IntegMapId getResponseMapId() {
		return IntegMapId.ElaboraDocumentoAsyncResponse_AsyncServiceResponse;
	}
}
