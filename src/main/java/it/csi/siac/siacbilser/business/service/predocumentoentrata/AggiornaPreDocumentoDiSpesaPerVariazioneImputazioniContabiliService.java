/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.predocumentospesa.CrudPreDocumentoDiSpesaBaseService;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabili;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabiliResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabiliService extends CrudPreDocumentoDiSpesaBaseService<AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabili, AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabiliResponse> {
	
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		preDoc = req.getPreDocumentoSpesa();
		bilancio = req.getBilancio();
		
		checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));
		
		checkEntita(preDoc.getEnte(), "ente", false);
		checkEntita(bilancio, "bilancio", false);
		checkEntita(preDoc.getCausaleSpesa(), "causale", false);
		checkEntita(preDoc.getContoTesoreria(), "conto tesoreria", false);
		
		checkNotNull(preDoc.getStatoOperativoPreDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo predocumento"), false);
		checkNotNull(preDoc.getDataDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data documento predocumento"), false);
		checkNotNull(preDoc.getDataCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data competenza predocumento"), false);
		checkNotNull(preDoc.getPeriodoCompetenza(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo competenza predocumento"), false);
		
		if(preDoc.getImpegno() != null) {
			// TODO: controllare i dati
			checkCondition(preDoc.getImpegno().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid impegno predocumento"));
			checkCondition(preDoc.getImpegno().getAnnoMovimento() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno impegno predocumento"));
			checkNotNull(preDoc.getImpegno().getNumeroBigDecimal(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero impegno predocumento"));
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
	public AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabiliResponse executeService(AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabili serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		preDocumentoSpesaDad.setLoginOperazione(loginOperazione);
		preDocumentoSpesaDad.setEnte(preDoc.getEnte());
		
		//inizializzo msgOperazione per i messaggi di errore
		msgOperazione= "aggiornamento";
	}

	@Override
	protected void execute() {
		
		// TODO: aggiungere
		// I controlli sulle disponibilita' sono delegati qui per sicurezza
		checkDisponibilitaCapitolo();
		checkDisponibilitaImpegno();
		checkDisponibilitaSubImpegno();
		
		preDocumentoSpesaDad.aggiornaAnagraficaPreDocumento(preDoc);
		
		StatoOperativoPreDocumento statoOperativoPreDocumento = aggiornaStatoOperativoPreDocumento(preDoc, false);
		preDoc.setStatoOperativoPreDocumento(statoOperativoPreDocumento);
		
		res.setPreDocumentoSpesa(preDoc);
	}
	
	private void checkDisponibilitaCapitolo() {
		final String methodName = "checkDisponibilitaCapitolo";
		if(entitaConUid(preDoc.getImpegno()) || entitaConUid(preDoc.getSubImpegno())) {
			log.debug(methodName, "E' presente l'impegno: ignoro le disponibilita' del capitolo");
			return;
		}
		if(!entitaConUid(preDoc.getCapitoloUscitaGestione())) {
			log.debug(methodName, "Capitolo non inizializzato: ignoro il controllo di disponibilita'");
			return;
		}
		BigDecimal disponibilitaImpegnareAnno1 = importiCapitoloDad.findImportoDerivato(preDoc.getCapitoloUscitaGestione().getUid(), ImportoDerivatoFunctionEnum.disponibilitaImpegnareAnno1);
		checkBusinessCondition(disponibilitaImpegnareAnno1.compareTo(preDoc.getImportoNotNull()) >= 0,
				ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Inserimento predisposizione pagamento", "capitolo"));
	}
	
	private void checkDisponibilitaImpegno() {
		if(!entitaConUid(preDoc.getImpegno()) || entitaConUid(preDoc.getSubImpegno())) {
			// Calcolo via subaccertamento se necessario
			return;
		}
		checkDisponibilitaImpegnoSubImpegno(preDoc.getImpegno());
	}
	
	private void checkDisponibilitaSubImpegno() {
		if(!entitaConUid(preDoc.getSubImpegno())) {
			// Calcolo via subaccertamento se necessario
			return;
		}
		checkDisponibilitaImpegnoSubImpegno(preDoc.getSubImpegno());
	}
	
	private void checkDisponibilitaImpegnoSubImpegno(Impegno acc) {
		final String methodName = "checkDisponibilitaImpegnoSubImpegno";
		boolean isSubImpegno = acc instanceof SubImpegno;
		
		BigDecimal importoPrecedente = caricaImportoPrecedentePredocumento(isSubImpegno);
		BigDecimal importoNuovo = preDoc.getImportoNotNull();
		
		DisponibilitaMovimentoGestioneContainer disponibilita = ottieniDisponibilitaLiquidare(acc);
		boolean isDisponibilitaSufficiente = disponibilita.getDisponibilita().add(importoPrecedente).compareTo(importoNuovo)>=0;
		
		log.info(methodName, "Disponibilita: " + disponibilita.getDisponibilita() + " - motivazione: " + disponibilita.getMotivazione());
		log.info(methodName, "disponibilita: " + disponibilita + " + importoPrecedente: " + importoPrecedente 
				+ " >= importoNuovo: " + importoNuovo + " -> isDisponibilitaSufficiente: " + isDisponibilitaSufficiente);
		if(isDisponibilitaSufficiente) {
			log.debug(methodName, "Disponibilita sufficiente. Non devo inserire modifiche di importo.");
			return;
		}
		// Se sono subaccertamento, devo ottenere anche la disponibilita' dell'accertamento
		if(isSubImpegno) {
			DisponibilitaMovimentoGestioneContainer disponibilitaAccertamento = ottieniDisponibilitaLiquidare(preDoc.getImpegno());
			preDoc.getImpegno().setDisponibilitaLiquidare(disponibilitaAccertamento.getDisponibilita());
			// SIAC-6695
			preDoc.getImpegno().setMotivazioneDisponibilitaLiquidare(disponibilitaAccertamento.getMotivazione());
		}
	}

	private DisponibilitaMovimentoGestioneContainer ottieniDisponibilitaLiquidare(Impegno acc) {
		DisponibilitaMovimentoGestioneContainer disponibilita;
		if(acc instanceof SubImpegno) {
			disponibilita = impegnoBilDad.ottieniDisponibilitaLiquidare(null, (SubImpegno)acc, req.getAnnoBilancio());
		} else {
			disponibilita = impegnoBilDad.ottieniDisponibilitaLiquidare(acc, null, req.getAnnoBilancio());
		}
		return disponibilita != null ? disponibilita : new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Disponibilita' non ottenuta dal DAD");
	}
	
}
