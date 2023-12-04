/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.pianoammortamento;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.mutuo.BaseMutuoExcelReportService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.PianoAmmortamentoMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.PianoAmmortamentoMutuoExcelReportResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PianoAmmortamentoMutuoExcelReportService extends BaseMutuoExcelReportService
	<PianoAmmortamentoMutuoExcelReport, PianoAmmortamentoMutuoExcelReportResponse, PianoAmmortamentoMutuoExcelReportHandler> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(req.getMutuo().getElencoRate(), ErroreCore.ENTITA_NON_TROVATA.getErrore("Elenco rate"), false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PianoAmmortamentoMutuoExcelReportResponse executeService(PianoAmmortamentoMutuoExcelReport serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected Class<PianoAmmortamentoMutuoExcelReportHandler> getReportHandlerClass() {
		return PianoAmmortamentoMutuoExcelReportHandler.class; 
	}
}
