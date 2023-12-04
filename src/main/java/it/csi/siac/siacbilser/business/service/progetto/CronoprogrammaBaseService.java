/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaSpesaResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class CronoprogrammaBaseService.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CronoprogrammaBaseService<REQ extends ServiceRequest, RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
		
	
	/** The cronoprogramma. */
	protected Cronoprogramma cronoprogramma;
	
	/** The progetto. */
	protected Progetto progetto;
	
	/** The atto amministrativo. */
	private AttoAmministrativo attoAmministrativo;
	
	
	/** The cronoprogramma dad. */
	@Autowired
	protected ProgettoDad progettoDad;
	
	/** The cronoprogramma dad. */
	@Autowired
	protected CronoprogrammaDad cronoprogrammaDad;
	
	/** The provvedimento dad. */
	@Autowired
	protected AttoAmministrativoDad attoAmministrativoDad;
	
	/** The inserisce riga spesa service. */
	@Autowired
	protected InserisceRigaSpesaService inserisceRigaSpesaService;
	
	/** The inserisce riga entrata service. */
	@Autowired
	protected InserisceRigaEntrataService inserisceRigaEntrataService;
	
	/** The aggiorna riga spesa service. */
	@Autowired
	protected AggiornaRigaSpesaService aggiornaRigaSpesaService;
	
	/** The aggiorna riga entrata service. */
	@Autowired
	protected AggiornaRigaEntrataService aggiornaRigaEntrataService;
	
	/** The cancella riga spesa service. */
	@Autowired
	protected CancellaRigaSpesaService cancellaRigaSpesaService;
	
	/** The cancella riga entrata service. */
	@Autowired
	protected CancellaRigaEntrataService cancellaRigaEntrataService;
	
	/** The cancella relazione riga entrata service. */
	//SIAC-8791
	@Autowired
	protected CancellaRelazioneRigaEntrataService cancellaRelazioneRigaEntrataService;
	
	/** The cancella relazione riga spesa service. */
	//SIAC-8791
		@Autowired
	protected CancellaRelazioneRigaSpesaService cancellaRelazioneRigaSpesaService;

	/**
	 * Inserisci dettaglio entrata.
	 *
	 * @param dett the dett
	 * @return the dettaglio entrata cronoprogramma
	 */
	protected DettaglioEntrataCronoprogramma inserisciDettaglioEntrata(DettaglioEntrataCronoprogramma dett) {
		InserisceRigaEntrata irreq = new InserisceRigaEntrata();
		irreq.setRichiedente(req.getRichiedente());
		Cronoprogramma cron = new Cronoprogramma();
		cron.setUid(cronoprogramma.getUid());
		dett.setCronoprogramma(cron);
		dett.setEnte(cronoprogramma.getEnte());
		irreq.setDettaglioEntrataCronoprogramma(dett);
		InserisceRigaEntrataResponse irres = executeExternalServiceSuccess(inserisceRigaEntrataService, irreq);
		dett = irres.getDettaglioEntrataCronoprogramma();
		return dett;
	}
	
	/**
	 * Aggiorna dettaglio entrata.
	 *
	 * @param dett the dett
	 * @return the dettaglio entrata cronoprogramma
	 */
	protected DettaglioEntrataCronoprogramma aggiornaDettaglioEntrata(DettaglioEntrataCronoprogramma dett) {
		AggiornaRigaEntrata irreq = new AggiornaRigaEntrata();
		irreq.setRichiedente(req.getRichiedente());
		Cronoprogramma cron = new Cronoprogramma();
		cron.setUid(cronoprogramma.getUid());
		dett.setCronoprogramma(cron);
		dett.setEnte(cronoprogramma.getEnte());
		irreq.setDettaglioEntrataCronoprogramma(dett);
		AggiornaRigaEntrataResponse irres = executeExternalServiceSuccess(aggiornaRigaEntrataService, irreq);
		dett = irres.getDettaglioEntrataCronoprogramma();
		return dett;
	}
	
	/**
	 * Cancella dettaglio entrata.
	 *
	 * @param dett the dett
	 * @return the dettaglio entrata cronoprogramma
	 */
	protected DettaglioEntrataCronoprogramma cancellaDettaglioEntrata(DettaglioEntrataCronoprogramma dett) {
		CancellaRigaEntrata irreq = new CancellaRigaEntrata();
		irreq.setRichiedente(req.getRichiedente());
		Cronoprogramma cron = new Cronoprogramma();
		cron.setUid(cronoprogramma.getUid());
		dett.setCronoprogramma(cron);
		dett.setEnte(cronoprogramma.getEnte());
		irreq.setDettaglioEntrataCronoprogramma(dett);
		CancellaRigaEntrataResponse irres = executeExternalServiceSuccess(cancellaRigaEntrataService, irreq);
		dett = irres.getDettaglioEntrataCronoprogramma();
		return dett;
	}
	
	/**
	 * Cancella la relazione con il capitolo esistente su dettaglio entrata.
	 *
	 * @param dett the dett
	 * @return the dettaglio entrata cronoprogramma
	 */
	//SIAC-8791
	protected DettaglioEntrataCronoprogramma cancellaRelazioneDettaglioEntrata(DettaglioEntrataCronoprogramma dett) {
		CancellaRelazioneRigaEntrata irreq = new CancellaRelazioneRigaEntrata();
		irreq.setRichiedente(req.getRichiedente());
		Cronoprogramma cron = new Cronoprogramma();
		cron.setUid(cronoprogramma.getUid());
		dett.setCronoprogramma(cron);
		dett.setEnte(cronoprogramma.getEnte());
		irreq.setDettaglioEntrataCronoprogramma(dett);
		CancellaRelazioneRigaEntrataResponse irres = executeExternalServiceSuccess(cancellaRelazioneRigaEntrataService, irreq);
		dett = irres.getDettaglioEntrataCronoprogramma();
		return dett;
	}
	
	protected void checkDettaglioUscita(DettaglioUscitaCronoprogramma dett) {
		boolean qeValorizzato = dett.getQuadroEconomico() != null && dett.getQuadroEconomico().getUid() != 0;
		if(!qeValorizzato && dett.getImportoQuadroEconomico() == null) {
			return;
		}
		String descDettaglio = new StringBuilder()
				.append("Riga spesa ")
				.append(dett.getDescrizioneCapitolo())
				.append(" anno spesa: ")
				.append(dett.getAnnoCompetenza()!= null? dett.getAnnoCompetenza().toString() : "null")
				.append(" anno entrata: ")
				.append(dett.getAnnoEntrata()!= null? dett.getAnnoEntrata().toString() : "null")
				.toString();
		if(!Boolean.TRUE.equals(cronoprogramma.getGestioneQuadroEconomico())) {
			//SIAC-6781
			//throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("i dati del quadro economico (quadro economico ed importo) non possono essere collegati ad un cronoprogramma con gestione quadro economico false. Controllare la riga: " + descDettaglio));
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("Il cronoprogramma non ha la gestione del quadro economico ma esiste la  " + descDettaglio + " con quadro economico. Verificare il dettaglio."));
		}
	}
	
	
	
	/**
	 * Inserisci dettaglio uscita.
	 *
	 * @param dett the dett
	 * @return the dettaglio uscita cronoprogramma
	 */
	protected DettaglioUscitaCronoprogramma inserisciDettaglioUscita(DettaglioUscitaCronoprogramma dett) {
		InserisceRigaSpesa irreq = new InserisceRigaSpesa();
		irreq.setRichiedente(req.getRichiedente());
		Cronoprogramma cron = new Cronoprogramma();
		cron.setUid(cronoprogramma.getUid());
		dett.setCronoprogramma(cron);
		dett.setEnte(cronoprogramma.getEnte());
		irreq.setDettaglioUscitaCronoprogramma(dett);
		InserisceRigaSpesaResponse irres = executeExternalServiceSuccess(inserisceRigaSpesaService, irreq);
		dett = irres.getDettaglioUscitaCronoprogramma();
		return dett;
	}
	
	/**
	 * Aggiorna dettaglio uscita.
	 *
	 * @param dett the dett
	 * @return the dettaglio uscita cronoprogramma
	 */
	protected DettaglioUscitaCronoprogramma aggiornaDettaglioUscita(DettaglioUscitaCronoprogramma dett) {
		AggiornaRigaSpesa irreq = new AggiornaRigaSpesa();
		irreq.setRichiedente(req.getRichiedente());
		Cronoprogramma cron = new Cronoprogramma();
		cron.setUid(cronoprogramma.getUid());
		dett.setCronoprogramma(cron);
		dett.setEnte(cronoprogramma.getEnte());
		irreq.setDettaglioUscitaCronoprogramma(dett);
		AggiornaRigaSpesaResponse irres = executeExternalServiceSuccess(aggiornaRigaSpesaService, irreq);
		dett = irres.getDettaglioUscitaCronoprogramma();
		return dett;
	}
	
	/**
	 * Cancella dettaglio uscita.
	 *
	 * @param dett the dett
	 * @return the dettaglio uscita cronoprogramma
	 */
	protected DettaglioUscitaCronoprogramma cancellaDettaglioUscita(DettaglioUscitaCronoprogramma dett) {
		CancellaRigaSpesa irreq = new CancellaRigaSpesa();
		irreq.setRichiedente(req.getRichiedente());
		Cronoprogramma cron = new Cronoprogramma();
		cron.setUid(cronoprogramma.getUid());
		dett.setCronoprogramma(cron);
		dett.setEnte(cronoprogramma.getEnte());
		irreq.setDettaglioUscitaCronoprogramma(dett);
		CancellaRigaSpesaResponse irres = executeExternalServiceSuccess(cancellaRigaSpesaService, irreq);
		dett = irres.getDettaglioUscitaCronoprogramma();
		return dett;
	}
	
	
	/**
	 * Cancella la relazione con il capitolo esistente su dettaglio spesa.
	 *
	 * @param dett the dett
	 * @return the dettaglio spesa cronoprogramma
	 */
	//SIAC-8791
	protected DettaglioUscitaCronoprogramma cancellaRelazioneDettaglioUscita(DettaglioUscitaCronoprogramma dett) {
		CancellaRelazioneRigaSpesa irreq = new CancellaRelazioneRigaSpesa();
		irreq.setRichiedente(req.getRichiedente());
		Cronoprogramma cron = new Cronoprogramma();
		cron.setUid(cronoprogramma.getUid());
		dett.setCronoprogramma(cron);
		dett.setEnte(cronoprogramma.getEnte());
		irreq.setDettaglioUscitaCronoprogramma(dett);
		CancellaRelazioneRigaSpesaResponse irres = executeExternalServiceSuccess(cancellaRelazioneRigaSpesaService, irreq);
		dett = irres.getDettaglioUscitaCronoprogramma();
		return dett;
	}
	
	/**
	 * Per ogni cronoprogramma, somma l'importo delle spese per anno di 
	 * riferimento dell'entrata e lo confronta con la somma degli importi delle 
	 * entrate raggruppate per anno di competenza.<br/>
	 * Se la somma delle spese è maggiore delle entrate il sistema risponderà con 
	 * il seguente messaggio: <code>BIL_ERR_0107. Totale delle spese superiore al totale delle entrate di riferimento</code>.<br/>
	 * 
	 * Inoltre:<br/>
	 * Per ogni riga relativa alle spese, verifica che l'anno delle Entrate 
	 * sia uguale ad uno degli anni di competenza presente nella tabella delle entrate.
	 * Se la verifica ha esito negativo 
	 * il sistema risponderà con il seguente messaggio: <code>BIL_ERR_0106. Spese con anno riferimento entrata non presente</code>.
	 * 
	 */
	protected void checkTotaleSpeseInferioriAlTotaleEntratePerAnno() {
		Map<Integer,BigDecimal> entrate = calcolaTotaleEntratePerAnno();
		Map<Integer,BigDecimal> uscite = calcolaTotaleUscitePerAnnoEntrata();
		for (Entry<Integer, BigDecimal> entry  : uscite.entrySet()) {
			Integer annoEntrata = entry.getKey();
			BigDecimal stanziamentoUscita = entry.getValue();
			BigDecimal stanziamentoEntrata = entrate.get(annoEntrata);
			
			if(stanziamentoEntrata == null) {
				throw new BusinessException(ErroreBil.SPESE_CON_ANNO_RIFERIMENTO_FINANZIAMENTO_NON_PRESENTE.getErrore(), Esito.FALLIMENTO);
			}
			
			if(stanziamentoUscita.compareTo(stanziamentoEntrata)>0) {
				throw new BusinessException(ErroreBil.TOTALE_DELLE_SPESE_SUPERIORE_AL_TOTALE_DEI_FINANZIEMENTI_DI_RIFERIMENTO.getErrore(), Esito.FALLIMENTO);
			}
		}
		
		
	}

	/**
	 * Se il totale delle spese inserite è diverso dal totale delle entrate viene restututo
	 * l'errore <code>BIL_ERR_0105. Totale spese diverso dal totale entrate</code>.
	 * 
	 */
	protected void checkTotaleSpeseUgualeTotaleEntrata() {
		BigDecimal totaleEntrate = calcolaTotaleEntrate();
		BigDecimal totaleUscite = calcolaTotaleUscite();
		
		if(totaleEntrate.compareTo(totaleUscite)!=0){
			throw new BusinessException(ErroreBil.TOTALE_SPESE_DIVERSO_DAL_TOTALE_ENTRATE.getErrore(), Esito.FALLIMENTO);
		}
		
		
	}

	/**
	 * Calcola totale entrate.
	 *
	 * @return the big decimal
	 */
	private BigDecimal calcolaTotaleEntrate() {
		final String methodName = "calcolaTotaleEntrate";
		BigDecimal totaleEntrate = BigDecimal.ZERO;
		List<DettaglioEntrataCronoprogramma> entrate = cronoprogramma.getCapitoliEntrata();
		for (DettaglioEntrataCronoprogramma dett : entrate) {
			if(dett.getDataFineValidita()==null){
				totaleEntrate = totaleEntrate.add(dett.getStanziamento());
			}
		}
		log.info(methodName, "returning: "+totaleEntrate);
		return totaleEntrate;
	}
	
	/**
	 * Calcola totale entrate per anno.
	 *
	 * @return the map
	 */
	private Map<Integer,BigDecimal> calcolaTotaleEntratePerAnno() {
		final String methodName = "calcolaTotaleEntratePerAnno";
		Map<Integer,BigDecimal> m = new HashMap<Integer,BigDecimal>();		
		for (DettaglioEntrataCronoprogramma dett : cronoprogramma.getCapitoliEntrata()) {
			if(dett.getDataFineValidita()==null){
				if(!m.containsKey(dett.getAnnoCompetenza())){
					m.put(dett.getAnnoCompetenza(), dett.getStanziamento());
				} else {
					BigDecimal stanziamento = m.get(dett.getAnnoCompetenza());
					m.put(dett.getAnnoCompetenza(), stanziamento.add(dett.getStanziamento()));
				}				
			}
		}
		log.info(methodName, "returning: "+m);
		return m;
	}
	
	/**
	 * Calcola totale uscite.
	 *
	 * @return the big decimal
	 */
	private BigDecimal calcolaTotaleUscite() {
		final String methodName = "calcolaTotaleUscite";
		BigDecimal totaleUscite = BigDecimal.ZERO;
		List<DettaglioUscitaCronoprogramma> entrate = cronoprogramma.getCapitoliUscita();
		for (DettaglioUscitaCronoprogramma dett : entrate) {
			if(dett.getDataFineValidita()==null){
				totaleUscite = totaleUscite.add(dett.getStanziamento());
			}
		}
		log.info(methodName, "returning: "+totaleUscite);
		return totaleUscite;
	}
	
	/**
	 * Calcola totale uscite per anno entrata.
	 *
	 * @return the map
	 */
	private Map<Integer,BigDecimal> calcolaTotaleUscitePerAnnoEntrata() {
		final String methodName = "calcolaTotaleUscitePerAnnoEntrata";
		Map<Integer,BigDecimal> m = new HashMap<Integer,BigDecimal>();
		
		for (DettaglioUscitaCronoprogramma dett : cronoprogramma.getCapitoliUscita()) {
			if(dett.getDataFineValidita()==null){
				if(!m.containsKey(dett.getAnnoEntrata())){
					m.put(dett.getAnnoEntrata(), dett.getStanziamento());
				} else {
					BigDecimal stanziamento = m.get(dett.getAnnoEntrata());
					m.put(dett.getAnnoEntrata(), stanziamento.add(dett.getStanziamento()));
				}				
			}
		}
		log.info(methodName, "returning: "+m);
		return m;
	}
	
	//SIAC-6255
	protected StatoOperativoCronoprogramma determinaStatoOperativoCronoprogramma() {
		return this.attoAmministrativo != null && StatoOperativoAtti.DEFINITIVO.equals(this.attoAmministrativo.getStatoOperativoAtti())? StatoOperativoCronoprogramma.VALIDO : StatoOperativoCronoprogramma.PROVVISORIO;
	}

	/**
	 * @param attoConStato
	 */
	protected void checkStatoAttoAmministrativo() {
		if(StatoOperativo.ANNULLATO.name().equals(this.attoAmministrativo.getStatoOperativo())) {
			throw new BusinessException(ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore());
		}
	}

	/**
	 * Carica atto amministrativo.
	 */
	protected void caricaAttoAmministrativo() {
		this.attoAmministrativo = attoAmministrativoDad.findProvvedimentoById(cronoprogramma.getAttoAmministrativo().getUid());
		if(this.attoAmministrativo == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("atto amministrativo", "uid " + cronoprogramma.getAttoAmministrativo().getUid()));
		}
	}
	
	/**
	 * Check importi dettagli uscita cronoprogramma.
	 * Sull’inserimento delle spese previste, occorre aggiungere il seguente controllo: 
	 * il totale delle spese previste non deve superare l’importo del progetto che è presente sulla testata
	 */
	protected void checkImportiDettagliUscitaCronoprogramma() {
		BigDecimal valoreComplessivoProgetto = progettoDad.caricaValoreComplessivoProgetto(progetto);
		if(BigDecimal.ZERO.compareTo(valoreComplessivoProgetto) == 0) {
			//SCELTA IMPLEMENTATIVA: il valore complessivo del progetto non e' obbligatorio, se e' presente il valore di default non lo metto
			return;
		}
		BigDecimal importoRigheSpesa = BigDecimal.ZERO;
		for (DettaglioUscitaCronoprogramma dett : cronoprogramma.getCapitoliUscita()) {
			if(dett.getUid()!=0 && dett.getDataFineValidita()!=null) {
				//questo cronoprogramma verra' cancellato: non va conteggiato
				continue;
			}
			importoRigheSpesa = importoRigheSpesa.add(dett.getStanziamento());			
		}
		
		if(importoRigheSpesa.compareTo(valoreComplessivoProgetto) > 0) {
			throw new BusinessException(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" totale delle spese previste", "superato il valore complessivo del progetto (valore complessivo = " + valoreComplessivoProgetto.toPlainString() + ")."));
		}
		
	}
	
	/**
	 * Check coerenza bilancio progetto associato.
	 */
	protected void checkCoerenzaBilancioProgettoAssociato() {
		Bilancio bilancioProgetto = progettoDad.getBilancioProgetto(progetto);
		if(bilancioProgetto == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("progetto associato al cronoprogramma", "impossibile reperire un bilancio"));
		}
		if(bilancioProgetto.getUid() != cronoprogramma.getBilancio().getUid()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("progetto e cronoprogramma non hanno bilancio congruente."));
		}
	}

}
