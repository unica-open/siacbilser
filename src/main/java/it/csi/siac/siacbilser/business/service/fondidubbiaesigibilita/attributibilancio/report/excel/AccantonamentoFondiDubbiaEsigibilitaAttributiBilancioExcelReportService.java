/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.gestione.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioGestioneExcelReportHandler;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.previsione.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelReportHandler;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto.creditistralciati.AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelReportHandler;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto.creditistralciati.CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.base.BaseExcelReportService;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportResponse;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.TipologiaEstrazioniFogliDiCalcolo;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportService 
	extends BaseExcelReportService
		<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport, 
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportResponse, 
		BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler>
			 {
	
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
	@Autowired private EnteDad enteDad;

	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	private Ente ente;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio", false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportResponse executeService(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReport serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void preStartElaboration() {
		checkEsistenzaAccantonamento();
		// SIAC-8679
		controlloTipologiaPerTipoAccantonamento();
		loadEnte();
	}
	
	private void loadEnte() {
		ente = enteDad.getEnteByUid(req.getEnte().getUid());
		checkBusinessCondition(ente != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Ente", "uid " + req.getEnte().getUid()));
	}
	
	private void checkEsistenzaAccantonamento() {
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid(),
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
		checkBusinessCondition(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Attributi bilancio", "uid " + req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid()));
	}

	@Override
	protected void initReportHandler() {
		super.initReportHandler();
		reportHandler.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
	}

	@Override
	protected void postElaborationSuccess() {
		final String methodName = "postElaborationSuccess";
		log.info(methodName, "post start elaborazione avvenuta con successo");
		
		byte[] bytes = reportHandler.getBytes();
		checkBusinessCondition(bytes != null, ErroreCore.ERRORE_DI_SISTEMA.getErrore(" impossibile ottenere un report per l'FCDE. Stampa fallita"));
		
		res.setReport(bytes);
		res.setContentType(reportHandler.getContentType());
		res.setExtension(reportHandler.getExtension());
	}
	
	@Override  
	protected Class<BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler> 
		getReportHandlerClass() {

		switch(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getTipoAccantonamentoFondiDubbiaEsigibilita()) {
			case PREVISIONE: 
				return cast(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler.class);
			case GESTIONE: 
				return cast(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioGestioneExcelReportHandler.class);
			case RENDICONTO: 
				return gestisciEstrazioniRendiconto(req.getTipologia());
			default:		
				throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Tipo accantonamento non censito: " + accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.getTipoAccantonamentoFondiDubbiaEsigibilita()));
		}
	}

	private Class<BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler> gestisciEstrazioniRendiconto(TipologiaEstrazioniFogliDiCalcolo tipologiaEstrazioniFogliDiCalcolo){
		// Nessuna tipologia equivale all'estrazione principale
		if(tipologiaEstrazioniFogliDiCalcolo == null) {
			return cast(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelReportHandler.class);
		}
		
		switch(tipologiaEstrazioniFogliDiCalcolo) {
			case CREDITI_STRALCIATI: 
				return cast(CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelReportHandler.class);
			case ACCERTAMENTI_ANNI_SUCCESSIVI: 
				return cast(AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelReportHandler.class);
			default:
				throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Tipologia estrazione non censita: " + tipologiaEstrazioniFogliDiCalcolo.getCodice()));
		}
	}
	
	@SuppressWarnings("unchecked")
	private Class<BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler> cast(Class<?> cls) {
		
		return (Class<BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler>) (Class<?>) cls;
	}
	
	// SIAC-8679
	private void controlloTipologiaPerTipoAccantonamento(){
		TipologiaEstrazioniFogliDiCalcolo tipologia = req.getTipologia();
		
		// Se non ho tipologia sto lavorando con il foglio dei capitoli inerenti alla versione
		if(tipologia == null) return;
		
		List<TipologiaEstrazioniFogliDiCalcolo> tipologieConsentite = TipologiaEstrazioniFogliDiCalcolo
				.estraiTipologiePerTipoAccantonamento(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio
						.getTipoAccantonamentoFondiDubbiaEsigibilita()
				);
		
		checkBusinessCondition(tipologieConsentite.contains(tipologia),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore(tipologia.getCodice() + " (" + tipologia.getTipoAccantonamento().getDescrizione() + ")",
						"per il tipo: " + accantonamentoFondiDubbiaEsigibilitaAttributiBilancio
								.getTipoAccantonamentoFondiDubbiaEsigibilita().getDescrizione())
		);
	}
}
