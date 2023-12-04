/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.ripartizione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.mutuo.BaseMutuoExcelReportService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RipartizioneMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RipartizioneMutuoExcelReportResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RipartizioneMutuoExcelReportService extends BaseMutuoExcelReportService
	<RipartizioneMutuoExcelReport, RipartizioneMutuoExcelReportResponse, RipartizioneMutuoExcelReportHandler> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(req.getMutuo().getElencoRipartizioneMutuo(), ErroreCore.ENTITA_NON_TROVATA.getErrore("Elenco ripartizione"), false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public RipartizioneMutuoExcelReportResponse executeService(RipartizioneMutuoExcelReport serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected Class<RipartizioneMutuoExcelReportHandler> getReportHandlerClass() {
		return RipartizioneMutuoExcelReportHandler.class; 
	}
}
