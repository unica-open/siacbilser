/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.variazionidibilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportElaborationException;
import it.csi.siac.siacbilser.business.service.stampa.base.SyncReportBaseService;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitoloModelDetail;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancio;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancioResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaExcelVariazioneDiBilancioService extends SyncReportBaseService<StampaExcelVariazioneDiBilancio, StampaExcelVariazioneDiBilancioResponse, StampaExcelVariazioneDiBilancioReportHandler> {
	
	@Autowired
	private VariazioniDad variazioniDad;
	
	private VariazioneImportoCapitolo variazioneImportoCapitolo;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkEntita(req.getVariazioneImportoCapitolo(),"variazione di bilancio");
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public StampaExcelVariazioneDiBilancioResponse executeService(StampaExcelVariazioneDiBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		variazioniDad.setEnte(req.getEnte());
	}

	@Override
	protected void initReportHandler() {
		reportHandler.setEnte(req.getEnte());
		reportHandler.setRowCount(0);
		reportHandler.setSheetTitle("variazione di bilancio");
		reportHandler.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		reportHandler.setXLSX(Boolean.TRUE.equals(req.getXlsx()));
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

	@Override
	protected void postElaborationError(ReportElaborationException e) {
		final String methodName = "postElaborationError";
		log.info(methodName, "post start elaborazione avvenuta con errori: " + e.getMessage());
		throw e;
	}
}
