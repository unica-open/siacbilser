/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio.report.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.stampa.base.BaseExcelReportService;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitoloModelDetail;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReportResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccommonser.util.misc.TimeoutValue;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VariazioneBilancioExcelReportService 
	extends BaseExcelReportService<VariazioneBilancioExcelReport, VariazioneBilancioExcelReportResponse, VariazioneBilancioExcelReportHandler> {
	
	@Autowired
	private VariazioniDad variazioniDad;
	
	private VariazioneImportoCapitolo variazioneImportoCapitolo;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkEntita(req.getVariazioneImportoCapitolo(),"variazione di bilancio");
	}
	
	@Override
	//SIAC-8050 si porta il timeout a 9 minuti per permettere eventualisituazioni straordinarie non contemplate
	@Transactional(timeout=TimeoutValue.INTERVAL_1_MINUTE * 9, propagation=Propagation.REQUIRED)
	public VariazioneBilancioExcelReportResponse executeService(VariazioneBilancioExcelReport serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		variazioniDad.setEnte(req.getEnte());
	}

	@Override
	protected void initReportHandler() {
		super.initReportHandler();
		reportHandler.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
	}
	
	@Override
	protected void preStartElaboration() {
		checkEsistenzaVariazione();
	}

	private void checkEsistenzaVariazione() {
		VariazioneImportoCapitolo variazione = variazioniDad.findVariazioneImportoCapitoloByUidModelDetail(req.getVariazioneImportoCapitolo().getUid(), VariazioneImportoCapitoloModelDetail.Bilancio);
		checkBusinessCondition(variazione != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Variazione", "uid " + req.getVariazioneImportoCapitolo().getUid()));
		
		variazioneImportoCapitolo = variazione;
	}

	@Override
	protected void postElaborationSuccess() {
		final String methodName = "postElaborationSuccess";
		log.info(methodName, "post start elaborazione avvenuta con successo");
		
		byte[] bytes = reportHandler.getBytes();
		if(bytes == null) {
			res.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(" impossibile ottenere un report per la variazione."));
			throw new BusinessException("Stampa fallita");
		}
		
		res.setReport(bytes);
		res.setContentType(reportHandler.getContentType());
		res.setExtension(reportHandler.getExtension());
	}
}
