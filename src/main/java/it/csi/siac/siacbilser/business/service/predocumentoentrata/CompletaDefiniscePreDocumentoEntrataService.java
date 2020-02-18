/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.HelperExecutor;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.helper.CompletaDefiniscePreDocumentoEntrataCompletamentoHelper;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.helper.CompletaDefiniscePreDocumentoEntrataDefinizioneHelper;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


/**
 * The Class DefiniscePreDocumentoEntrataPerElencoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompletaDefiniscePreDocumentoEntrataService extends CheckedAccountBaseService<CompletaDefiniscePreDocumentoEntrata, CompletaDefiniscePreDocumentoEntrataResponse> {
	
	@Autowired
	private AccertamentoBilDad accertamentoBilDad;
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	@Autowired
	private SoggettoDad soggettoDad;
	
	@Autowired
	private HelperExecutor helperExecutor;
	
	private MovimentoGestioneServiceCallGroup mgscg;

	private Accertamento accertamento;
	private SubAccertamento subAccertamento;
	private AttoAmministrativo attoAmministrativo;
	private Soggetto soggetto;
	private BigDecimal disponibilitaIncassare;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		checkEntita(req.getAccertamento(), "accertamento");
		checkEntita(req.getSoggetto(), "soggetto");
		
		// Per la ricerca, almeno uno tra la data di competenza e la causale
		checkCondition(req.getDataCompetenzaDa() != null || req.getDataCompetenzaA() != null || entitaConUid(req.getCausaleEntrata()),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("almeno uno tra data di competenza e causale deve essere presente"));
		
		checkCondition(req.getDataCompetenzaDa() == null || req.getDataCompetenzaA() == null
				|| !req.getDataCompetenzaA().before(req.getDataCompetenzaDa()),
				ErroreCore.VALORE_NON_VALIDO.getErrore("Data competenza", "la data di competenza da non deve essere inferiore la data di competenza a"));
	}
	
	@Override
	protected void init() {
		preDocumentoEntrataDad.setEnte(ente);
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
		
		mgscg = new MovimentoGestioneServiceCallGroup(serviceExecutor, req.getRichiedente(), ente, req.getBilancio());
	}
	
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public CompletaDefiniscePreDocumentoEntrataResponse executeService(CompletaDefiniscePreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public CompletaDefiniscePreDocumentoEntrataResponse executeServiceTxRequiresNew(CompletaDefiniscePreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		// 1. operazioni preliminari
		checkAccertamento();
		checkSubAccertamento();
		caricaDatiAccertamentoSubAccertamento();
		checkAttoAmministrativo();
		checkSoggetto();
		checkCoerenzaSoggettoAccertamento();
		
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
	 * Viene controllata l'esistenza e lo stato dell'accertamento che deve essere <strong>DEFINITIVO</strong>: se l'accertamento indicato non esiste
	 * l'elaborazione non viene eseguita e viene esposto l'errore <code>&lt;FIN_ERR_0002. Accertamento Inesistente&gt;</code>
	 * <p>
	 * In questa ricerca viene anche calcolata la <strong>DISPONIBILIT&Agrave; A INCASSARE</strong> dell'accertamento.
	 * <p>
	 * <sup>1</sup>Se c'&egrave; il soggetto tutti i PreDocumenti dovrebbero essere collegati allo stesso soggetto il che &egrave; impossibile,
	 * solo la classe garantisce la coerenza dei dati.
	 */
	private void checkAccertamento() {
		final String methodName = "caricaDatiAccertamentoSubAccertamento";
		accertamento = accertamentoBilDad.findAccertamentoByUid(req.getAccertamento().getUid());
		// Controllo sull'accertamento
		checkBusinessCondition(accertamento != null, ErroreFin.MOV_NON_ESISTENTE.getErrore("Accertamento"));
//		disponibilitaIncassare = accertamento.getDisponibilitaIncassare();
		log.debug(methodName, "Trovato accertamento con uid " + accertamento.getUid());
	}
	
	/**
	 * Analogamente l'accertamento
	 */
	private void checkSubAccertamento() {
		final String methodName = "checkSubAccertamento";
		if(!entitaConUid(req.getSubAccertamento())) {
			// Non ho il subaccertamento: esco
			log.debug(methodName, "Nessun subaccertamento collegato");
			return;
		}
		
		subAccertamento = accertamentoBilDad.findSubAccertamentoByUid(req.getSubAccertamento().getUid());
		// Controllo sul subaccertamento
		checkBusinessCondition(subAccertamento != null, ErroreFin.MOV_NON_ESISTENTE.getErrore("Subaccertamento"));
		//disponibilitaIncassare = subAccertamento.getDisponibilitaIncassare();
		log.debug(methodName, "Trovato subaccertamento con uid " + subAccertamento.getUid());
	}
	
	/**
	 * Caricamento dei dati di accertamento e subaccertamento da servizio
	 */
	private void caricaDatiAccertamentoSubAccertamento() {
		
		RicercaAttributiMovimentoGestioneOttimizzato parametri = new RicercaAttributiMovimentoGestioneOttimizzato();
		parametri.setEscludiSubAnnullati(true);
		parametri.setCaricaSub(subAccertamento != null && subAccertamento.getNumero() != null);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse resRAPC = mgscg.ricercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, new DatiOpzionaliCapitoli(), subAccertamento);
		log.logXmlTypeObject(resRAPC, "Risposta ottenuta dal servizio RicercaAccertamentoPerChiaveOttimizzato.");
		checkServiceResponseFallimento(resRAPC);
		res.addErrori(resRAPC.getErrori());
		
		Accertamento acc = resRAPC.getAccertamento();
		checkBusinessCondition(acc != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero().toPlainString()));
		accertamento = acc;
		disponibilitaIncassare = accertamento.getDisponibilitaIncassare();

		if(subAccertamento != null) {
			checkBusinessCondition(accertamento.getElencoSubAccertamenti() != null && !accertamento.getElencoSubAccertamenti().isEmpty(),
				ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento",
					accertamento.getAnnoMovimento() + "/" + accertamento.getNumero().toPlainString() + "-" + subAccertamento.getNumero().toPlainString()));
			SubAccertamento tmp = null;
			for(Iterator<SubAccertamento> it = accertamento.getElencoSubAccertamenti().iterator(); it.hasNext() && tmp == null;) {
				SubAccertamento sa = it.next();
				if(sa.getUid() == subAccertamento.getUid()) {
					tmp = sa;
				}
			}
			checkBusinessCondition(tmp != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento",
					accertamento.getAnnoMovimento() + "/" + accertamento.getNumero().toPlainString() + "-" + subAccertamento.getNumero().toPlainString()));
			subAccertamento = tmp;
		}
	
	}
	
	/**
	 * Viene controllata l'esistenza del provvedimento: se l'atto indicato non esiste
	 * l'elaborazione non viene eseguita e viene esposto l'errore <code>&lt;FIN_ERR_0002. Provvedimento Inesistente&gt;</code>
	 */
	private void checkAttoAmministrativo() {
		final String methodName = "checkAttoAmministrativo";
		if(!entitaConUid(req.getAttoAmministrativo())) {
			// Non ho il provvedimento: esco
			log.debug(methodName, "Nessun atto amministrativo collegato");
			return;
		}
		attoAmministrativo = provvedimentoDad.findProvvedimentoByIdModelDetail(req.getAttoAmministrativo().getUid());
		// Controllo sull'atto
		checkBusinessCondition(attoAmministrativo != null, ErroreFin.MOV_NON_ESISTENTE.getErrore("Provvedimento"));
		log.debug(methodName, "Trovato attoAmministrativo con uid " + attoAmministrativo.getUid());
	}
	
	/**
	 * Viene controllata l'esistenza del aoggetto: se il soggetto indicato non esiste
	 * l'elaborazione non viene eseguita e viene esposto l'errore <code>&lt;FIN_ERR_0002. Soggetto Inesistente&gt;</code>
	 */
	private void checkSoggetto() {
		final String methodName = "checkSoggetto";
		soggetto = soggettoDad.findSoggettoById(req.getSoggetto().getUid());
		// Controllo sull'atto
		checkBusinessCondition(soggetto != null, ErroreFin.MOV_NON_ESISTENTE.getErrore("Soggetto"));
		log.debug(methodName, "Trovato soggetto con uid " + soggetto.getUid());
	}
	
	/**
	 * L'accertamento e il soggetto devono essere compatibili
	 */
	private void checkCoerenzaSoggettoAccertamento() {
		final String methodName = "checkCoerenzaSoggettoAccertamento";
		
		if(subAccertamento != null) {
			log.debug(methodName, "controllo la coerenza del soggetto subaccertamento");
			// IL soggetto del subaccertamento deve coincidere con il soggetto richiesto
			Soggetto soggettoAssociato = subAccertamento.getSoggetto();
			checkBusinessCondition(entitaConUid(soggettoAssociato) && soggettoAssociato.getUid() == soggetto.getUid(),
					it.csi.siac.siacfin2ser.model.errore.ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore(
							"soggetto", "subAccertamento", "per la predisposizione di incasso", "il soggetto deve essere lo stesso del subAccertamento"));
			return;
		}
		// Ho l'accertamento e non il sub
		Soggetto soggettoAssociato = accertamento.getSoggetto();
		
		if(entitaConUid(soggettoAssociato)){
			log.debug(methodName, "controllo la coerenza del soggetto accertamento");
			checkBusinessCondition(soggettoAssociato.getUid() == soggetto.getUid(),
					it.csi.siac.siacfin2ser.model.errore.ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore(
							"soggetto", "accertamento", "per la predisposizione di incasso", "il soggetto deve essere lo stesso dell'accertamento"));
			return;
		}
		// Non ho un soggetto ma solo la classe
		log.debug(methodName, "controllo la coerenza della classe soggetto accertamento");
		//SIAC-6207: non voglio controllare la cloasse soggetto
//		checkClasseSoggetto();
	}
	
	/**
	 * Controllo di coerenza della classe soggetto
	 */
	private void checkClasseSoggetto() {
		final String methodName = "checkClasseSoggetto";
		// Ottiene la classe soggetto
		ClasseSoggetto classeSoggettoMovimentoGestione = accertamento.getClasseSoggetto();
		if(classeSoggettoMovimentoGestione == null) {
			// Movimento senza classe, esco dal controllo
			log.debug(methodName, "Classe soggetto non presente per il movimento di gestione");
			return;
		}
		// Ottengo le classi del soggetto
		List<ClasseSoggetto> listaClasseSoggetto = soggettoDad.findClasseSoggetto(soggetto.getUid());
		// Se il soggetto non ha classi non posso continuare
		checkBusinessCondition(listaClasseSoggetto != null && !listaClasseSoggetto.isEmpty(),
				it.csi.siac.siacfin2ser.model.errore.ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore(
						"soggetto", "l'accertamento", "per la predisposizione di incasso", "il soggetto deve possedere una classificazione"));
		
		// Per ogni classificazione
		for(ClasseSoggetto cs : listaClasseSoggetto) {
			// Controllo che le classificazioni siano coerenti
			if(cs.getCodice().equalsIgnoreCase(classeSoggettoMovimentoGestione.getCodice())) {
				// Il codice e' coerente, esco
				log.debug(methodName, "ho trovato un codice classe corrispondente: " + cs.getCodice());
				return;
			}
		}
		// Aggiunge il messaggio
		res.addMessaggio(it.csi.siac.siacfin2ser.model.errore.ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore(
						"soggetto", "l'accertamento", "per la predisposizione di incasso",
						"il soggetto deve appartenere alla classificazione dell'accertamento (" + classeSoggettoMovimentoGestione.getCodice() + ")"));
	}
	
	/**
	 * Inserire le seguenti righe di log:
	 * <ol type="I">
	 *     <li>"Elaborazione Convalida per Elenco Predisposizioni Incasso"</li>
	 *     <li>"Parametri: Elenco " ANNO/NUMERO-ELE " Impegno " ANNO/NUMERO-IMP " disponibile a liquidare " DISPONIBILIT&Agrave; A LIQUIDARE</li>
	 * </ol>
	 */
	private void preparaLogOperazioniPreliminari() {
		final String methodName = "preparaLogOperazioniPreliminari";
		StringBuilder sb = new StringBuilder()
				.append("Parametri:")
				.append(" Accertamento ")
				.append(accertamento.getAnnoMovimento())
				.append("/")
				.append(accertamento.getNumero().toPlainString());
		if(subAccertamento != null) {
			sb.append("-")
				.append(subAccertamento.getNumero().toPlainString());
		}
		sb.append(" Soggetto ")
			.append(soggetto.getCodiceSoggetto());
		if(attoAmministrativo != null) {
			sb.append(" Provvedimento ")
				.append(attoAmministrativo.getAnno())
				.append("/")
				.append(attoAmministrativo.getNumero());
			if(attoAmministrativo.getTipoAtto() != null && StringUtils.isNotBlank(attoAmministrativo.getTipoAtto().getCodice())) {
				sb.append("/")
					.append(attoAmministrativo.getTipoAtto().getCodice());
			}
			if(attoAmministrativo.getStrutturaAmmContabile() != null && StringUtils.isNotBlank(attoAmministrativo.getStrutturaAmmContabile().getCodice())) {
				sb.append("/")
					.append(attoAmministrativo.getStrutturaAmmContabile().getCodice());
			}
		}
		
		sb.append(" Disponibile a incassare ")
			.append(Utility.formatCurrency(disponibilitaIncassare != null ? disponibilitaIncassare : BigDecimal.ZERO));
		
		log.info(methodName, sb.toString());
		res.addMessaggio("Elaborazione Completamento e Definizione Predisposizioni di Incasso", sb.toString());
	}
	
	/**
	 * Completamento dei predocumenti di spesa
	 */
	private void completaPredocumenti() {
		CompletaDefiniscePreDocumentoEntrataCompletamentoHelper helper = new CompletaDefiniscePreDocumentoEntrataCompletamentoHelper(
				preDocumentoEntrataDad,
				mgscg,
				// Dati per ricerca
				ente,
				req.getCausaleEntrata(),
				req.getDataCompetenzaDa(),
				req.getDataCompetenzaA(),
				
				// Dati per aggiornamento
				accertamento,
				subAccertamento,
				attoAmministrativo,
				soggetto,
				disponibilitaIncassare);
		helperExecutor.executeHelperTxRequiresNew(helper);
		List<Messaggio> messaggiHelper = helper.getMessaggi();
		for(Messaggio messaggio : messaggiHelper) {
			res.addMessaggio(messaggio.getCodice(), messaggio.getDescrizione());
		}
	}
	
	/**
	 * Definizione dei predocumenti di spesa
	 */
	private void definizionePredocumenti() {
		CompletaDefiniscePreDocumentoEntrataDefinizioneHelper helper = new CompletaDefiniscePreDocumentoEntrataDefinizioneHelper(
				preDocumentoEntrataDad,
				serviceExecutor,
				// Per la ricerca e l'invocazione della definizione
				req.getRichiedente(),
				req.getBilancio(),
				req.getCausaleEntrata(),
				req.getDataCompetenzaDa(),
				req.getDataCompetenzaA());
		helperExecutor.executeHelperTxRequiresNew(helper);
		List<Messaggio> messaggiHelper = helper.getMessaggi();
		for(Messaggio messaggio : messaggiHelper) {
			res.addMessaggio(messaggio.getCodice(), messaggio.getDescrizione());
		}
	}
}
