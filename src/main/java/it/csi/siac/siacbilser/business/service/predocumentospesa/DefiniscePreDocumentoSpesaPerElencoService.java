/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.HelperExecutor;
import it.csi.siac.siacbilser.business.service.predocumentospesa.helper.DefiniscePreDocumentoSpesaPerElencoCompletamentoHelper;
import it.csi.siac.siacbilser.business.service.predocumentospesa.helper.DefiniscePreDocumentoSpesaPerElencoDefinizioneHelper;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;


/**
 * The Class DefiniscePreDocumentoSpesaPerElencoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefiniscePreDocumentoSpesaPerElencoService extends CheckedAccountBaseService<DefiniscePreDocumentoSpesaPerElenco, DefiniscePreDocumentoSpesaPerElencoResponse> {
	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	private PreDocumentoSpesaDad preDocumentoSpesaDad;
	
	@Autowired
	private HelperExecutor helperExecutor;

	private Impegno impegno;
	private SubImpegno subImpegno;
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	private BigDecimal disponibilitaLiquidare;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getElencoDocumentiAllegato(), "elenco");
		checkEntita(req.getBilancio(), "bilancio");
		checkEntita(req.getImpegno(), "impegno");
		// Il subimpegno e' facoltativo
	}
	
	@Override
	protected void init() {
		preDocumentoSpesaDad.setEnte(ente);
		preDocumentoSpesaDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public DefiniscePreDocumentoSpesaPerElencoResponse executeService(DefiniscePreDocumentoSpesaPerElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public DefiniscePreDocumentoSpesaPerElencoResponse executeServiceTxRequiresNew(DefiniscePreDocumentoSpesaPerElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		// 1. operazioni preliminari
		checkSubImpegno();
		checkImpegno();
		checkElenco();
		preparaLogOperazioniPreliminari();
		log.debug(methodName, "Operazioni preliminari terminate");
		
		// 2. completamento
		completaPredocumenti();
		log.debug(methodName, "Completamento terminato");
		
		// 3. definizione
		definizionePredocumenti();
		log.debug(methodName, "Definizione terminata");
	}

	/**
	 * Viene controllata l'esistenza e lo stato dell'impegno che deve essere <strong>DEFINITIVO</strong>: se l'impegno indicato non esiste
	 * l'elaborazione non viene eseguita e viene esposto l'errore <code>&lt;FIN_ERR_0002. Impegno Inesistente&gt;</code>
	 * <p>
	 * Una volta trovato &eacute; necessario verificare che l'impegno sia collegato a una <strong>CLASSE di SOGGETTI</strong> e non a un soggetto<sup>1</sup>.
	 * In caso contrario l'elaborazione non viene eseguita e viene esposto l'errore <code>&lt;FIN_ERR_0002. Impegno non Coerente: deve essere collegato a una classe&gt;</code>.
	 * <p>
	 * In questa ricerca viene anche calcolata la <strong>DISPONIBILIT&Agrave; A LIQUIDARE</strong> dell'impegno.
	 * <p>
	 * <sup>1</sup>Se c'&egrave; il soggetto tutti i PreDocumenti dovrebbero essere collegati allo stesso soggetto il che &egrave; impossibile,
	 * solo la classe garantisce la coerenza dei dati.
	 */
	private void checkImpegno() {
		impegno = impegnoBilDad.findImpegnoByUid(req.getImpegno().getUid(), ImpegnoModelDetail.Soggetto, ImpegnoModelDetail.ClasseSoggetto, ImpegnoModelDetail.DisponibilitaLiquidare);
		if(impegno == null) {
			// Se l'impegno non esiste viene esposto l'errore FIN_ERR_0002
			throw new BusinessException(ErroreFin.MOV_NON_ESISTENTE.getErrore("Impegno"));
		}
		if(!entitaConUid(impegno.getClasseSoggetto())) {
			// Se non ho la classe soggetto viene esposto l'errore FIN_ERR_0002
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impegno non coerente: deve essere collegato a una classe"));
		}
		if(entitaConUid(impegno.getSoggetto())) {
			// Se ho il soggetto viene esposto l'errore FIN_ERR_0002
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impegno non coerente: non deve essere collegato ad un soggetto"));
		}
		disponibilitaLiquidare = impegno.getDisponibilitaLiquidare();
	}
	
	/**
	 * Analogamento l'impegno
	 */
	private void checkSubImpegno() {
		final String methodName = "checkSubImpegno";
		if(!entitaConUid(req.getSubImpegno())) {
			// Non ho il subimpegno: esco
			log.debug(methodName, "Nessun subimpegno collegato");
			return;
		}
		
		subImpegno = impegnoBilDad.findSubImpegnoByUid(req.getSubImpegno().getUid(), SubImpegnoModelDetail.Soggetto, SubImpegnoModelDetail.ClasseSoggetto, SubImpegnoModelDetail.DisponibilitaLiquidare);
		if(subImpegno == null) {
			// Se il subimpegno non esiste viene esposto l'errore FIN_ERR_0002
			throw new BusinessException(ErroreFin.MOV_NON_ESISTENTE.getErrore("Subimpegno"));
		}
		if(!entitaConUid(subImpegno.getClasseSoggetto())) {
			// Se non ho la classe soggetto viene esposto l'errore FIN_ERR_0002
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impegno non coerente: deve essere collegato a una classe"));
		}
		if(entitaConUid(subImpegno.getSoggetto())) {
			// Se ho il soggetto viene esposto l'errore FIN_ERR_0002
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impegno non coerente: non deve essere collegato ad un soggetto"));
		}
		disponibilitaLiquidare = subImpegno.getDisponibilitaLiquidare();
	}
	
	/**
	 * Viene controllata l'esistenza: se non esiste l'elaborazione non viene eseguita e viene esposto l'errore
	 * <code>&lt;FIN_ERR_0146: elenco in stato incongruente o assente&gt;</code>
	 */
	private void checkElenco() {
		elencoDocumentiAllegato = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoLightById(req.getElencoDocumentiAllegato().getUid());
		if(elencoDocumentiAllegato == null) {
			// Se l'elenco non esiste viene esposto l'errore FIN_ERR_0146
			throw new BusinessException(it.csi.siac.siacfin2ser.model.errore.ErroreFin.ELENCO_CON_STATO_INCONGRUENTE_O_ASSENTE.getErrore("Subimpegno"));
		}
	}
	
	/**
	 * Inserire le seguenti righe di log:
	 * <ol type="I">
	 *     <li>"Elaborazione Convalida per Elenco Predisposizioni Pagamento"</li>
	 *     <li>"Parametri: Elenco " ANNO/NUMERO-ELE " Impegno " ANNO/NUMERO-IMP " disponibile a liquidare " DISPONIBILIT&Agrave; A LIQUIDARE</li>
	 * </ol>
	 */
	private void preparaLogOperazioniPreliminari() {
		StringBuilder sb = new StringBuilder()
				.append("Parametri: Elenco ")
				.append(elencoDocumentiAllegato.getAnno())
				.append("/")
				.append(elencoDocumentiAllegato.getNumero())
				.append(" Impegno ")
				.append(impegno.getAnnoMovimento())
				.append("/")
				.append(impegno.getNumeroBigDecimal().toPlainString());
		if(entitaConUid(subImpegno)) {
			sb.append("-")
				.append(subImpegno.getNumeroBigDecimal().toPlainString());
		}
		sb.append(" disponibile a liquidare ")
			.append(Utility.formatCurrency(disponibilitaLiquidare != null ? disponibilitaLiquidare : BigDecimal.ZERO));
		
		res.addMessaggio("Elaborazione Convalida per Elenco Predisposizioni Pagamento", sb.toString());
	}
	
	/**
	 * Completamento dei predocumenti di spesa
	 */
	private void completaPredocumenti() {
		DefiniscePreDocumentoSpesaPerElencoCompletamentoHelper helper = new DefiniscePreDocumentoSpesaPerElencoCompletamentoHelper(
				preDocumentoSpesaDad,
				elencoDocumentiAllegato,
				impegno,
				subImpegno,
				disponibilitaLiquidare);
		helperExecutor.executeHelperTxRequiresNew(helper);

		addMessaggi(helper.getMessaggi());
		addErrori(helper.getErrori());
	}
	
	/**
	 * Definizione dei predocumenti di spesa
	 */
	private void definizionePredocumenti() {
		DefiniscePreDocumentoSpesaPerElencoDefinizioneHelper helper = new DefiniscePreDocumentoSpesaPerElencoDefinizioneHelper(
				impegnoBilDad,
				preDocumentoSpesaDad,
				serviceExecutor,
				req.getRichiedente(),
				req.getBilancio(),
				elencoDocumentiAllegato,
				impegno,
				subImpegno);
		helperExecutor.executeHelperTxRequiresNew(helper);

		addMessaggi(helper.getMessaggi());
		addErrori(helper.getErrori());
	}

	private void addMessaggi(List<Messaggio> messaggi) {
		for(Messaggio messaggio : messaggi) {
			res.addMessaggio(messaggio.getCodice(), messaggio.getDescrizione());
		}
	}

	private void addErrori(List<Errore> errori) {
		for(Errore errore : errori) { 
			res.addErrore(errore);
		}
	}

}
