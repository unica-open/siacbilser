/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.base.BaseResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumento;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoResponseInterface;

public abstract class BaseElaboraDocumentoGenericoService<ED extends ElaboraDocumento, EDRES extends BaseResponse & ElaboraDocumentoResponseInterface> 
	extends BaseElaboraDocumentoService<ED, EDRES> {

	@Override
	protected void checkServiceParameters(ED edReq) throws ServiceParamError {
		assertParamNotNull(edReq.getCodiceTipoDocumento(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo documento"));
	}
	
	@Override
	protected String getCodiceTipoDocumento(ED edReq) {
		return edReq.getCodiceTipoDocumento();
	}

	@Override
	protected IntegMapId getRequestMapId() {
		return IntegMapId.ElaboraDocumento_ElaboraFile;
	}
}
