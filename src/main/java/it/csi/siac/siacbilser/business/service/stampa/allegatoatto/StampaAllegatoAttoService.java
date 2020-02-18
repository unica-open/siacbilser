/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportElaborationException;
import it.csi.siac.siacbilser.business.service.stampa.base.SyncReportBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAttoResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaAllegatoAttoService extends SyncReportBaseService<StampaAllegatoAtto, StampaAllegatoAttoResponse, StampaAllegatoAttoReportHandler> {
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	private AllegatoAtto allegatoAtto;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkCondition(req.getEnte() == null || req.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		checkCondition(req.getBilancio() == null || req.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		
		checkNotNull(req.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"), false);
		checkCondition(req.getAllegatoAtto() == null || req.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"), false);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public StampaAllegatoAttoResponse executeService(StampaAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		allegatoAttoDad.setEnte(req.getEnte());
	}

	@Override
	protected void initReportHandler() {
		reportHandler.setAllegatoAtto(allegatoAtto);
		reportHandler.setEnte(req.getEnte());
		reportHandler.setRichiedente(req.getRichiedente());
		
		reportHandler.setBilancio(req.getBilancio());
		reportHandler.setTipoStampa(TipoStampaAllegatoAtto.ALLEGATO);
		reportHandler.setAnnoEsercizio(req.getAnnoEsercizio());
	}
	
	@Override
	protected void preStartElaboration() {
		caricaDettaglioAllegatoAtto();
		//SIAC-6261
		checkDatiDurc();
	}
	
	private void checkDatiDurc() {
		List<Integer> uidsSubdocConConfermaDurc = allegatoAttoDad.getUidsSubdocWithImpegnoConfermaDurc(allegatoAtto);
		if(uidsSubdocConConfermaDurc == null || uidsSubdocConConfermaDurc.isEmpty()) {
			return;
		}
		Map<String, Date> mappaSoggettoData = subdocumentoSpesaDad.getDataFineValiditaDurcAndSoggettoCodePiuRecenteBySubdocIds(uidsSubdocConConfermaDurc);
		Date now = new Date();
		//SIAC-7143
		String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String dateFineDurc = null;
		
		for (String soggettoCode : mappaSoggettoData.keySet()) {
			Date dataFineValiditaDurc = mappaSoggettoData.get(soggettoCode);
			//SIAC-7143
			dateFineDurc = new SimpleDateFormat("yyyy-MM-dd").format(dataFineValiditaDurc);
			if(dataFineValiditaDurc== null  || dateNow.compareTo(dateFineDurc) > 0){
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il soggetto " + soggettoCode + " presenta dati Durc non validi."));
			}
		}
	}
	
	private void caricaDettaglioAllegatoAtto() {
		AllegatoAtto allegatoAttoTrovato = allegatoAttoDad.findAllegatoAttoById(req.getAllegatoAtto().getUid());
		if(allegatoAttoTrovato == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("allegato atto", "uid " + req.getAllegatoAtto().getUid()));
		}
		allegatoAtto = allegatoAttoTrovato;
	}

	@Override
	protected void postElaborationSuccess() {
		final String methodName = "postElaborationSuccess";
		log.info(methodName, "post start elaborazione avvenuta con successo");
		stampaDettaglioOperazione();
		GeneraReportResponse generaReportResponse = reportHandler.getGeneraReportResponse();
		if(generaReportResponse.hasErrori()) {
			res.addErrori(generaReportResponse.getErrori());
			throw new BusinessException("Stampa fallita");
		}
		
		File report = generaReportResponse.getReport();
		res.setReport(report);
	}

	@Override
	protected void postElaborationError(ReportElaborationException e) {
		final String methodName = "postElaborationError";
		log.info(methodName, "post start elaborazione avvenuta con errori: " + e.getMessage());
		// Rilancio l'eccezione
		throw e;
	}
	//stampa nei log i dettaglio (info) dell'allegato atto
	private void stampaDettaglioOperazione() {
		StringBuilder sb = new StringBuilder();
		sb.append("Elaborazione Stampa Allegato per ");
		sb.append("Atto ");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getAnno() !=0) ? allegatoAtto.getAttoAmministrativo().getAnno() :" ");
		sb.append("/");
		sb.append((allegatoAtto.getAttoAmministrativo() !=null && allegatoAtto.getAttoAmministrativo().getNumero() !=0) ? allegatoAtto.getAttoAmministrativo().getNumero() :" ");
		sb.append("-");
		sb.append(allegatoAtto.getVersioneInvioFirmaNotNull());
		log.debug("stampaDettaglioOperazione", sb.toString());

	}
}
