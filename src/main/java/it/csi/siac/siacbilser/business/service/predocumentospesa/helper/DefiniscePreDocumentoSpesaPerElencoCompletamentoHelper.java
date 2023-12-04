/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.Helper;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Definizione del predocumento via l'elenco: helper per il completamento
 * @author Marchino Alessandro
 *
 */
public class DefiniscePreDocumentoSpesaPerElencoCompletamentoHelper implements Helper<Void> {

	private final PreDocumentoSpesaDad preDocumentoSpesaDad;
	
	private final ElencoDocumentiAllegato elencoDocumentiAllegato;
	private final Impegno impegno;
	private final SubImpegno subImpegno;
	private final BigDecimal disponibilitaLiquidare;
	
	private final LogSrvUtil log;
	private final List<Messaggio> messaggi;
	private final List<Errore> errori;
	
	private boolean skip;
	private BigDecimal sommaPredoc;
	private Long contaPredoc;
	
	/**
	 * Construttore dell'helper
	 * @param preDocumentoSpesaDad il dad del predocumento
	 * @param elencoDocumentiAllegato l'elenco
	 * @param impegno l'impegno
	 * @param subImpegno il subimpegno
	 * @param disponibilitaLiquidare la disponibilit&agrave; dell'impegno/sub
	 */
	public DefiniscePreDocumentoSpesaPerElencoCompletamentoHelper(
			PreDocumentoSpesaDad preDocumentoSpesaDad,
			ElencoDocumentiAllegato elencoDocumentiAllegato,
			Impegno impegno,
			SubImpegno subImpegno,
			BigDecimal disponibilitaLiquidare) {
		this.preDocumentoSpesaDad = preDocumentoSpesaDad;
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
		this.impegno = impegno;
		this.subImpegno = subImpegno;
		this.disponibilitaLiquidare = disponibilitaLiquidare;
		
		this.messaggi = new ArrayList<Messaggio>();
		this.errori = new ArrayList<Errore>();
		this.log = new LogSrvUtil(getClass());
		
		this.skip = false;
	}
	
	@Override
	public Void helpExecute() {
		final String methodName = "helpExecute";
		// 1. Somma da completare e controllo capienza
		calcoloCapienza();
		checkCapienza();
		log.debug(methodName, "Controllo preliminare somma da completare e capienza completato");
		// Controllo se devo uscire
		if(skip) {
			log.info(methodName, "Completamento dei predocumenti dell'elenco " + elencoDocumentiAllegato.getUid() + " non necessario");
			return null;
		}
		
		// 2. Ciclo completa PreDocumenti
		associaImpegno();
		log.debug(methodName, "Associazione dell'impegno " + impegno.getUid() + " (subimpegno " + (subImpegno != null && subImpegno.getUid() != 0 ? subImpegno.getUid() : "null") + ") avvenuta con successo");
		
		// 3. Fina fase di completamento
		scriviLog();
		
		return null;
	}
	
	

	public List<Errore> getErrori() {
		return errori;
	}
	
	/**
	 * @return the messaggi
	 */
	public List<Messaggio> getMessaggi() {
		return this.messaggi;
	}
	
	/**
	 * Aggiunge il messaggio
	 * @param descrizione descrizione del messaggio
	 */
	private void addMessaggio(String descrizione) {
		messaggi.add(new Messaggio("Elaborazione Convalida per Elenco Predisposizioni Pagamento", descrizione));
	}

	/**
	 * Somma da Completare e Controllo Capienza:
	 * <ol>
	 *     <li>Leggere, sommare l'importo (SOMMA PREDOC) e conteggiare (CONTA PREDOC) tutti i PreDocumenti di spesa:
	 *     <ul>
	 *         <li>In stato INCOMPLETO</li>
	 *         <li>Collegati all'Elenco passato in input</li>
	 *     </ul>
	 *     </li>
	 *     <li>Inserire la seguente riga di log
	 *     <p>
	 *     "Richiesto completamento di " CONTA PREDOC " PreDocumenti per una somma di &euro; " SOMMA PREDOC
	 *     </li>
	 *     <li>Verificare l'eseguibilità della fase:
	 *     <p>
	 *     SE SOMMA PREDOC = 0
	 *     <ul>
	 *         <li>Scrivere nel log "Elaborazione Convalida non eseguita: non ci sono PreDocumenti Incompleti per l'elenco indicato"</li>
	 *         <li>Saltare alla Fase di Definizione</li>
	 *     </ul>
	 *     <p>ALTRIMENTI SE SOMMA PREDOC > DISPONIBILIT&Agrave; A LIQUIDARE
	 *     <ul>
	 *         <li>L'elaborazione viene interrotta con errore "ELABORAZIONE CONCLUSA CON ERRORE: la somma dei PreDocumenti da elaborare &eacute; superiore alla disponibilit&agrave; dell'impegno"
	 *     </ul>
	 *     ALTRIMENTI
	 *     <ul>
	 *         <li>l'elaborazione prosegue regolarmente</li>
	 *     </ul>
	 * </ol>
	 */
	private void calcoloCapienza() {
		final String methodName = "controlloCapienza";
		// Calcolo dell'importo dei predoc
		sommaPredoc = preDocumentoSpesaDad.findImportoPreDocumentoByElencoIdAndStatiOperativi(elencoDocumentiAllegato.getUid(), null, null, StatoOperativoPreDocumento.INCOMPLETO);
		// Calcolo del numero dei predoc
		contaPredoc = preDocumentoSpesaDad.countPreDocumentoByElencoIdAndStatiOperativi(elencoDocumentiAllegato.getUid(), null, null, StatoOperativoPreDocumento.INCOMPLETO);
		
		String messaggio = new StringBuilder()
				.append("Richiesto completamento di ")
				.append(contaPredoc)
				.append(" PreDocumenti per una somma di ")
				.append(Utility.formatCurrency(sommaPredoc))
				.toString();
		log.debug(methodName, messaggio);
		
		addMessaggio(messaggio);
	}
	
	/**
	 * Controllo della capienza
	 */
	private void checkCapienza() {
		// Controllo che ci sia dell'importo da completare
		if(BigDecimal.ZERO.compareTo(sommaPredoc) == 0) {
			addMessaggio("Elaborazione Convalida non eseguita: non ci sono PreDocumenti Incompleti per l’elenco indicato");
			skip = true;
			return;
		}
		// Controllo di non sfondare la disponibilita' dell'impegno
		if(disponibilitaLiquidare.compareTo(sommaPredoc) < 0) {
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("la somma dei PreDocumenti da elaborare &eacute; superiore alla disponibilit&agrave; dell'impegno"));
		}
	}

	/**
	 * Associazione dell'impegno
	 */
	private void associaImpegno() {
		// FIXME: associare solo l'impegno e gestire come servizio esterno lo stato?
		preDocumentoSpesaDad.associaImpegnoByElencoAndStatiOperativi(impegno, subImpegno, StatoOperativoPreDocumento.COMPLETO, elencoDocumentiAllegato.getUid(), StatoOperativoPreDocumento.INCOMPLETO);
	}
	
	/**
	 * Scrivere nel log "Elaborazione Convalida conclusa"
	 * 
	 */
	private void scriviLog() {
		addMessaggio("Elaborazione Convalida conclusa");
	}

}
