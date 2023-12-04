/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import it.csi.siac.siacbilser.business.service.mutuo.report.excel.base.BaseMutuoExcelReportHandler;
import it.csi.siac.siacbilser.business.service.mutuo.report.excel.base.BaseMutuoExcelRow;
import it.csi.siac.siacbilser.business.service.stampa.base.BaseExcelReportService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.BaseMutuoExcelReportResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class BaseMutuoExcelReportService
		<BMERREQ extends BaseMutuoExcelReport, 
		BMERRES extends BaseMutuoExcelReportResponse,
		BMERH extends BaseMutuoExcelReportHandler<? extends BaseMutuoExcelRow>> 
		extends BaseExcelReportService<BMERREQ, BMERRES, BMERH> {
	
	@Override
	protected void initReportHandler() {
		super.initReportHandler();
		reportHandler.setMutuo(req.getMutuo());
	}

	@Override
	protected void postElaborationSuccess() {
		byte[] bytes = reportHandler.getBytes();
		checkBusinessCondition(bytes != null, 
				ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile ottenere il report"));
		
		res.setReport(bytes);
		res.setContentType(reportHandler.getContentType());
		res.setExtension(reportHandler.getExtension());
	}
	
	@Override
	protected void preStartElaboration() {
	}
}
