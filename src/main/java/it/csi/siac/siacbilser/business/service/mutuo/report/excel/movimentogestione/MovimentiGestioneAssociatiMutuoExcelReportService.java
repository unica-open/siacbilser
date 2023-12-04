/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.mutuo.BaseMutuoExcelReportService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.MovimentiGestioneAssociatiMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.MovimentiGestioneAssociatiMutuoExcelReportResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MovimentiGestioneAssociatiMutuoExcelReportService extends BaseMutuoExcelReportService
		<MovimentiGestioneAssociatiMutuoExcelReport, 
		MovimentiGestioneAssociatiMutuoExcelReportResponse, 
		MovimentiGestioneAssociatiMutuoExcelReportHandler> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkCondition(
				req.getMutuo().getElencoImpegniAssociati() != null ||
				req.getMutuo().getElencoAccertamentiAssociati() != null, 
				ErroreCore.ENTITA_NON_TROVATA.getErrore("Elenco rate"), false);		
	}
	
	@Override
	@Transactional(readOnly = true)
	public MovimentiGestioneAssociatiMutuoExcelReportResponse executeService(MovimentiGestioneAssociatiMutuoExcelReport serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected Class<MovimentiGestioneAssociatiMutuoExcelReportHandler> getReportHandlerClass() {
		return MovimentiGestioneAssociatiMutuoExcelReportHandler.class; 
	}
}
