/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabili;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.business.service.movgest.CalcolaDisponibilitaAIncassareAccSubaccService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaAIncassareAccSubacc;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaAIncassareAccSubaccResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * The Class AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliService extends CrudPreDocumentoDiEntrataBaseService<AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabili, AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliResponse> {
	
	@Autowired
	private AccertamentoBilDad accertamentoBilDad;
	@Autowired
	private ServiceExecutor serviceExecutor;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoEntrata();
		bilancio = req.getBilancio();
		gestisciModificaImportoAccertamento = req.isGestisciModificaImportoAccertamento();
		
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));
		
		checkEntita(preDoc.getEnte(), "ente", false);
		checkEntita(bilancio, "bilancio", false);
		checkEntita(preDoc.getCausaleEntrata(), "causale", false);
		
		checkNotNull(preDoc.getStatoOperativoPreDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo predocumento"), false);
		checkNotNull(preDoc.getDataDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data documento predocumento"), false);
		checkNotNull(preDoc.getDataCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data competenza predocumento"), false);
		checkNotNull(preDoc.getPeriodoCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo competenza predocumento"), false);
		
		if(preDoc.getAccertamento() != null) {
			// TODO: controllare i dati
			checkCondition(preDoc.getAccertamento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid acertamento predocumento"));
			checkCondition(preDoc.getAccertamento().getAnnoMovimento()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno accertamento predocumento"));
			checkNotNull(preDoc.getAccertamento().getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero accertamento predocumento"));
		}
		checkEntitaFacoltativa(preDoc.getSoggetto(), "soggetto");
		checkEntitaFacoltativa(preDoc.getAttoAmministrativo(), "provvedimento predocumento");
		checkCondition(preDoc.getProvvisorioDiCassa() == null ||  
				(preDoc.getProvvisorioDiCassa().getAnno() != null && preDoc.getProvvisorioDiCassa().getNumero() != null) ||
				(preDoc.getProvvisorioDiCassa().getAnno() == null && preDoc.getProvvisorioDiCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
	}
	
	@Override
	@Transactional
	public AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliResponse executeService(AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabili serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
		preDocumentoEntrataDad.setEnte(preDoc.getEnte());
		
		//inizializzo msgOperazione per i messaggi di errore
		msgOperazione= "aggiornamento";
	}

	@Override
	protected void execute() {
		
		// TODO: aggiungere
		// I controlli sulle disponibilita' sono delegati qui per sicurezza
		checkDisponibilitaCapitolo();
		checkDisponibilitaAccertamento();
		checkDisponibilitaSubAccertamento();
		
		preDocumentoEntrataDad.aggiornaAnagraficaPreDocumento(preDoc);
		
		StatoOperativoPreDocumento statoOperativoPreDocumento = aggiornaStatoOperativoPreDocumento(preDoc, false);
		preDoc.setStatoOperativoPreDocumento(statoOperativoPreDocumento);
		
		res.setPreDocumentoEntrata(preDoc);
	}
	
	private void checkDisponibilitaCapitolo() {
		final String methodName = "checkDisponibilitaCapitolo";
		if(entitaConUid(preDoc.getAccertamento()) || entitaConUid(preDoc.getSubAccertamento())) {
			log.debug(methodName, "E' presente l'accertamento: ignoro le disponibilita' del capitolo");
			return;
		}
		if(!entitaConUid(preDoc.getCapitoloEntrataGestione())) {
			log.debug(methodName, "Capitolo non inizializzato: ignoro il controllo di disponibilita'");
			return;
		}
		BigDecimal disponibilitaAccertareAnno1 = importiCapitoloDad.findImportoDerivato(preDoc.getCapitoloEntrataGestione().getUid(), ImportoDerivatoFunctionEnum.disponibilitaAccertareAnno1);
		checkBusinessCondition(disponibilitaAccertareAnno1.compareTo(preDoc.getImportoNotNull()) >= 0,
				ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Inserimento predisposizione incasso", "capitolo"));
	}
	
	private void checkDisponibilitaAccertamento() {
		if(!entitaConUid(preDoc.getAccertamento()) || entitaConUid(preDoc.getSubAccertamento())) {
			// Calcolo via subaccertamento se necessario
			return;
		}
		checkDisponibilitaAccertamentoSubAccertamento(preDoc.getAccertamento());
	}
	
	private void checkDisponibilitaSubAccertamento() {
		if(!entitaConUid(preDoc.getSubAccertamento())) {
			// Calcolo via subaccertamento se necessario
			return;
		}
		checkDisponibilitaAccertamentoSubAccertamento(preDoc.getSubAccertamento());
	}
	
	private void checkDisponibilitaAccertamentoSubAccertamento(Accertamento acc) {
		final String methodName = "checkDisponibilitaAccertamentoSubAccertamento";
		boolean isSubAccertamento = acc instanceof SubAccertamento;
		
		BigDecimal importoPrecedente = caricaImportoPrecedentePredocumento(isSubAccertamento);
		BigDecimal importoNuovo = preDoc.getImportoNotNull();
		
		ottieniDisponibilitaIncassare(acc);
		boolean isDisponibilitaSufficiente = acc.getDisponibilitaIncassare().add(importoPrecedente).compareTo(importoNuovo)>=0;
		
		log.info(methodName, "disponibilita: " + acc.getDisponibilitaIncassare() + " + importoPrecedente: " + importoPrecedente 
				+ " >= importoNuovo: " + importoNuovo + " -> isDisponibilitaSufficiente: " + isDisponibilitaSufficiente);
		if(isDisponibilitaSufficiente) {
			log.debug(methodName, "Disponibilita sufficiente. Non devo inserire modifiche di importo.");
			return;
		}
		// Se sono subaccertamento, devo ottenere anche la disponibilita' dell'accertamento
		if(isSubAccertamento) {
			ottieniDisponibilitaIncassare(preDoc.getAccertamento());
		}
		gestioneModificaImporto(acc, importoPrecedente, acc.getDisponibilitaIncassare());
	}

	private void ottieniDisponibilitaIncassare(Accertamento acc) {
		CalcolaDisponibilitaAIncassareAccSubacc reqCDAIAS = new CalcolaDisponibilitaAIncassareAccSubacc();
		reqCDAIAS.setDataOra(new Date());
		reqCDAIAS.setRichiedente(req.getRichiedente());
		
		Integer uidMovimentoGestione = acc instanceof SubAccertamento
				? Integer.valueOf(acc.getUid())
				: accertamentoBilDad.findUidTestataByUidMovimento(acc.getUid());
		reqCDAIAS.setUidMovimentoGestione(uidMovimentoGestione);
		
		// Fornita la classe al posto di effettuare l'autowiring per evitare eventuali problemi in fase di startup del servizio via JUnit
		CalcolaDisponibilitaAIncassareAccSubaccResponse resCDAIAS = serviceExecutor.executeServiceSuccess(CalcolaDisponibilitaAIncassareAccSubaccService.class, reqCDAIAS);
		acc.setDisponibilitaIncassare(resCDAIAS.getDisponibilitaAIncassare() != null ? resCDAIAS.getDisponibilitaAIncassare() : BigDecimal.ZERO);
		acc.setMotivazioneDisponibilitaIncassare(resCDAIAS.getMotivazioneDisponibilitaAIncassare());
	}
	
	@Override
	protected void caricaImportoAccertamentoSub(Accertamento accertamentoOSub) {
		boolean isSubAccertamento = accertamentoOSub instanceof SubAccertamento;
		
		Map<String, BigDecimal> importiTestata = accertamentoBilDad.ottieniImporti(preDoc.getAccertamento().getUid());
		copiaImporti(preDoc.getAccertamento(), importiTestata);
		// predoc.getAccertamento
		if(isSubAccertamento) {
			Map<String, BigDecimal> importiSub = accertamentoBilDad.ottieniImportiSub(accertamentoOSub.getUid());
			copiaImporti(accertamentoOSub, importiSub);
		}
	}
	
}
