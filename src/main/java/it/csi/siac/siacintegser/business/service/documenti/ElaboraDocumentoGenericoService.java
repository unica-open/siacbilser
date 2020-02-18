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
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraDocumentoGenericoService extends BaseElaboraDocumentoGenericoService<ElaboraDocumento, ElaboraDocumentoResponse> {
	
	@Override
	protected ElaboraDocumentoResponse execute(ElaboraDocumento edReq) {
		return super.syncExecute(edReq);
	}

	@Override
	protected IntegMapId getResponseMapId() {
		return IntegMapId.ElaboraDocumentoResponse_ElaboraFileResponse;
	}
}
