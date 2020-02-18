/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.ricevuta;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportElaborationException;
import it.csi.siac.siacbilser.business.service.stampa.base.SyncReportBaseService;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomaleResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRicevutaRichiestaEconomaleService extends
	SyncReportBaseService<StampaRicevutaRichiestaEconomale, StampaRicevutaRichiestaEconomaleResponse, StampaRicevutaRichiestaEconomaleReportHandler> {

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRichiestaEconomale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiesta economale"));
		checkCondition(req.getRichiestaEconomale().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid richiesta economale"));
	}
	
	@Override
	protected void initReportHandler() {
		// TODO Auto-generated method stub
		//da request leggo uid e lo metto nel reportHandler
		
		reportHandler.setRichiestaEconomale(req.getRichiestaEconomale());
		reportHandler.setBilancio(req.getBilancio());
	}
	
	@Override
	protected void preStartElaboration() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void postElaborationSuccess() {
		File report = reportHandler.getGeneraReportResponse().getReport();
		res.setReport(report);
	}

	@Override
	protected void postElaborationError(ReportElaborationException e) {
		res.addErrori(reportHandler.getGeneraReportResponse().getErrori());
		
		
	}


}
