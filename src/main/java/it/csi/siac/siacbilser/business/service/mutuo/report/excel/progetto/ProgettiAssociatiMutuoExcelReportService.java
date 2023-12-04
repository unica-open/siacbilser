/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.progetto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.mutuo.BaseMutuoExcelReportService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.ProgettiAssociatiMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.ProgettiAssociatiMutuoExcelReportResponse;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProgettiAssociatiMutuoExcelReportService extends BaseMutuoExcelReportService
	<ProgettiAssociatiMutuoExcelReport, ProgettiAssociatiMutuoExcelReportResponse, ProgettiAssociatiMutuoExcelReportHandler> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		
		checkNotNull(req.getMutuo().getElencoProgettiAssociati(), ErroreCore.ENTITA_NON_TROVATA.getErrore("Elenco progetti associati"), false);
		checkCondition(CollectionUtil.isNotEmpty(req.getMutuo().getElencoProgettiAssociati()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco progetti associati"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public ProgettiAssociatiMutuoExcelReportResponse executeService(ProgettiAssociatiMutuoExcelReport serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected Class<ProgettiAssociatiMutuoExcelReportHandler> getReportHandlerClass() {
		return ProgettiAssociatiMutuoExcelReportHandler.class; 
	}
}
