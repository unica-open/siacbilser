/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import it.csi.siac.siacintegser.business.service.base.ElaboraFileServicesEnum;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.base.BaseResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativi;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoResponseInterface;

public abstract class BaseElaboraAttiAmministrativiService<EAA extends ElaboraAttiAmministrativi, EDRI extends BaseResponse & ElaboraDocumentoResponseInterface> 
	extends BaseElaboraDocumentoService<EAA, EDRI> {
	
	@Override
	protected String getCodiceTipoDocumento(EAA eaaReq) {
		return ElaboraFileServicesEnum.FLUSSO_ATTI_AMMINISTRATIVI.getCodice();
	}

	@Override
	protected IntegMapId getRequestMapId() {
		return IntegMapId.ElaboraAttiAmministrativi_ElaboraFile;
	}
	
}
