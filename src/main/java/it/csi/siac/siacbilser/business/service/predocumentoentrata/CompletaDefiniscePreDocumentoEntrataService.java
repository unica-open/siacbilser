/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;
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
	private AttoAmministrativoDad attoAmministrativoDad;
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
	
	private List<Integer> uidPredocumentiDaCompletareeDefinire = new ArrayList<Integer>();
	
	//SIAC-6780
	private ProvvisorioDiCassa provvisorioCassa;
	
	@Autowired
	protected ProvvisorioService provvisorioService;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		checkEntita(req.getAccertamento(), "accertamento");
		checkEntita(req.getSoggetto(), "soggetto");
		boolean hasCriteri = (req.getUidPredocumentiDaFiltrare() != null && !req.getUidPredocumentiDaFiltrare().isEmpty()) || req.getRicercaSinteticaPredocumentoEntrata() != null;
		checkCondition(hasCriteri, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("criteri per caricare i predocumenti da completare e definire"));
		
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
		
		caricaUidsDaCompletareEDefinire();
		
		checkUidsDaCompletareEDefinire();
		
		
		// 1. operazioni preliminari
		checkAccertamento();
		checkSubAccertamento();
		caricaDatiAccertamentoSubAccertamento();
		checkAttoAmministrativo();
		checkSoggetto();
		checkCoerenzaSoggettoAccertamento();
		
		//SIAC-6780
		//controllo che sia presente un provvisorio e che il totale dei predocumenti
		//da completare sia corrispondente al valore del provvisorio
		caricaProvvisorioDiCassa();
		
		preparaLogOperazioniPreliminari();
		log.debug(methodName, "Operazioni preliminari terminate");
		
		// 2. completamento
		completaPredocumenti();
		log.debug(methodName, "Completamento terminato");
		
		// 3. definizione
		definizionePredocumenti();
		log.debug(methodName, "Definizione terminata");
	}
	
	private void checkUidsDaCompletareEDefinire() {
		final String methodName= "checkUidsDaCompletareEDefinire";
		if(this.uidPredocumentiDaCompletareeDefinire == null || this.uidPredocumentiDaCompletareeDefinire.isEmpty()) {
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore(" impossibile reperire su base dati dei predocumenti da completare o definire"));
		}
		List<String> ss = new ArrayList<String>();
		for (Integer ui : this.uidPredocumentiDaCompletareeDefinire) {
			ss.add(Integer.toString(ui));
		}
		log.debug(methodName, "Trovati uid:" + StringUtils.join(ss, ","));
		
	}

	private void caricaUidsDaCompletareEDefinire() {
		
		if((req.getUidPredocumentiDaFiltrare() != null && !req.getUidPredocumentiDaFiltrare().isEmpty())) {
			this.uidPredocumentiDaCompletareeDefinire = req.getUidPredocumentiDaFiltrare();
			return;
		}
		
		RicercaSinteticaPreDocumentoEntrata reqRic = req.getRicercaSinteticaPredocumentoEntrata();
		
		PreDocumentoEntrata preDoc = reqRic.getPreDocumentoEntrata();
		preDoc.setEnte(ente);
		
		this.uidPredocumentiDaCompletareeDefinire = preDocumentoEntrataDad.ricercaSinteticaPreDocumentoUids(preDoc, reqRic.getTipoCausale(),
				reqRic.getDataCompetenzaDa(), reqRic.getDataCompetenzaA(), reqRic.getDataTrasmissioneDa(), reqRic.getDataTrasmissioneA(),
				reqRic.getCausaleEntrataMancante(),  reqRic.getSoggettoMancante(), reqRic.getProvvedimentoMancante(), reqRic.getContoCorrenteMancante(), reqRic.getNonAnnullati(),
				reqRic.getOrdinativoIncasso(), req.getUidPredocumentiDaFiltrare(), reqRic.getParametriPaginazione());
		
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
	 * carica il provvisorio di cassa 
	 */
	protected void caricaProvvisorioDiCassa() {
		final String methodName = "caricaProvvisorioDiCassa";
		if(req.getProvvisorioCassa() == null || req.getProvvisorioCassa().getNumero()== null || req.getProvvisorioCassa().getAnno() == null){
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa = ricercaProvvisorioDiCassa();
		if(provvisorioDiCassa == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", req.getProvvisorioCassa().getAnno() 
					 +  "/" + req.getProvvisorioCassa().getNumero()), Esito.FALLIMENTO);
		}
		
		log.debug(methodName, "trovato provvisorio con uid: " + provvisorioDiCassa.getUid() + " e numero " + provvisorioDiCassa.getNumero());
		
		this.provvisorioCassa = provvisorioDiCassa;
	}
	

	private ProvvisorioDiCassa ricercaProvvisorioDiCassa() {
		RicercaProvvisoriDiCassa ricercaProvvisoriDiCassa = new RicercaProvvisoriDiCassa();
		ricercaProvvisoriDiCassa.setEnte(ente);
		ricercaProvvisoriDiCassa.setRichiedente(req.getRichiedente());
		
		ParametroRicercaProvvisorio parametroRicercaProvvisorio = new ParametroRicercaProvvisorio();
		parametroRicercaProvvisorio.setAnno(req.getProvvisorioCassa().getAnno());
		parametroRicercaProvvisorio.setNumero(req.getProvvisorioCassa().getNumero());
		parametroRicercaProvvisorio.setTipoProvvisorio(TipoProvvisorioDiCassa.E);
		ricercaProvvisoriDiCassa.setParametroRicercaProvvisorio(parametroRicercaProvvisorio);
		//SIAC-7421
		ricercaProvvisoriDiCassa.setNumPagina(1);
		ricercaProvvisoriDiCassa.setNumRisultatiPerPagina(5);
		
		RicercaProvvisoriDiCassaResponse resRPC = provvisorioService.ricercaProvvisoriDiCassa(ricercaProvvisoriDiCassa);

		log.logXmlTypeObject(resRPC, "Risposta ottenuta dal servizio ricercaProvvisorioDiCassa.");
		checkServiceResponseFallimento(resRPC);

		return resRPC == null || resRPC.getElencoProvvisoriDiCassa().isEmpty()
			? null
				: resRPC.getElencoProvvisoriDiCassa().get(0);
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
		parametri.setCaricaSub(subAccertamento != null && subAccertamento.getNumeroBigDecimal() != null);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse resRAPC = mgscg.ricercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, new DatiOpzionaliCapitoli(), subAccertamento);
		log.logXmlTypeObject(resRAPC, "Risposta ottenuta dal servizio RicercaAccertamentoPerChiaveOttimizzato.");
		checkServiceResponseFallimento(resRAPC);
		res.addErrori(resRAPC.getErrori());
		
		Accertamento acc = resRAPC.getAccertamento();
		checkBusinessCondition(acc != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal().toPlainString()));
		accertamento = acc;
		disponibilitaIncassare = accertamento.getDisponibilitaIncassare();

		if(subAccertamento != null) {
			checkBusinessCondition(accertamento.getElencoSubAccertamenti() != null && !accertamento.getElencoSubAccertamenti().isEmpty(),
				ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento",
					accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal().toPlainString() + "-" + subAccertamento.getNumeroBigDecimal().toPlainString()));
			SubAccertamento tmp = null;
			for(Iterator<SubAccertamento> it = accertamento.getElencoSubAccertamenti().iterator(); it.hasNext() && tmp == null;) {
				SubAccertamento sa = it.next();
				if(sa.getUid() == subAccertamento.getUid()) {
					tmp = sa;
				}
			}
			checkBusinessCondition(tmp != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento",
					accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal().toPlainString() + "-" + subAccertamento.getNumeroBigDecimal().toPlainString()));
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
		attoAmministrativo = attoAmministrativoDad.findProvvedimentoByIdModelDetail(req.getAttoAmministrativo().getUid());
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
				.append(accertamento.getNumeroBigDecimal().toPlainString());
		if(subAccertamento != null) {
			sb.append("-")
				.append(subAccertamento.getNumeroBigDecimal().toPlainString());
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
		
		if(provvisorioCassa != null && provvisorioCassa.getUid() != 0) {
			sb.append(" Provvisorio di Cassa")
				.append(provvisorioCassa.getAnno())
				.append("/")
				.append(provvisorioCassa.getNumero());
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
				req.getContoCorrente(),
				this.uidPredocumentiDaCompletareeDefinire,
				// Dati per aggiornamento
				accertamento,
				subAccertamento,
				attoAmministrativo,
				soggetto,
				disponibilitaIncassare,
				provvisorioCassa);
		helperExecutor.executeHelperTxRequiresNew(helper);
		addMessaggi(helper.getMessaggi());
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
				req.getDataCompetenzaA(),
				req.getContoCorrente(),
				this.uidPredocumentiDaCompletareeDefinire);
		helperExecutor.executeHelperTxRequiresNew(helper);
		addMessaggi(helper.getMessaggi());
	}

	private void addMessaggi(List<Messaggio> messaggiHelper) {
		for(Messaggio messaggio : messaggiHelper) {
			res.addMessaggio(messaggio.getCodice(), messaggio.getDescrizione());
		}
	}
}
