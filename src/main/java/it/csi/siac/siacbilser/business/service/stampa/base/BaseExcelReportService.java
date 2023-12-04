/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.base;

import it.csi.siac.siacbilser.business.service.excel.base.BaseExcelReportHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.excel.ExcelReportServiceRequest;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.excel.ExcelReportServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


public abstract class 
	BaseExcelReportService
	<EREQ extends ExcelReportServiceRequest, 
	ERES extends ExcelReportServiceResponse, 
	ERH extends BaseExcelReportHandler> 
	extends SyncReportBaseService<EREQ, ERES, ERH> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
	}
	
	
	@Override
	protected void initReportHandler() {
		reportHandler.setEnte(req.getEnte());
		reportHandler.setXlsx(Boolean.TRUE.equals(req.getXlsx()));
	}
}
