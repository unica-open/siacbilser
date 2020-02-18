/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.Helper;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.DefiniscePreDocumentoEntrataService;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Completamento e definizione del predocumento di incasso: helper per la definizione
 * @author Marchino Alessandro
 */
public class CompletaDefiniscePreDocumentoEntrataDefinizioneHelper implements Helper<Void> {

	private final PreDocumentoEntrataDad preDocumentoEntrataDad;
	private final ServiceExecutor serviceExecutor;
	
	private final Richiedente richiedente;
	private final Bilancio bilancio;
	private final CausaleEntrata causaleEntrata;
	private final Date dataCompetenzaDa;
	private final Date dataCompetenzaA;
	
	private final LogUtil log;
	private final List<Messaggio> messaggi;
	
	private List<PreDocumentoEntrata> preDocumentiEntrata;
	
	private boolean skip;
	// Per il log
	private List<PreDocumentoEntrata> predocumentiElaborati;
	private List<PreDocumentoEntrata> predocumentiSaltati;
	
	/**
	 * Construttore dell'helper
	 */
	public CompletaDefiniscePreDocumentoEntrataDefinizioneHelper(
			PreDocumentoEntrataDad preDocumentoEntrataDad,
			ServiceExecutor serviceExecutor,
			Richiedente richiedente,
			Bilancio bilancio,
			CausaleEntrata causaleEntrata,
			Date dataCompetenzaDa,
			Date dataCompetenzaA) {
		this.preDocumentoEntrataDad = preDocumentoEntrataDad;
		this.serviceExecutor = serviceExecutor;
		
		this.richiedente = richiedente;
		this.bilancio = bilancio;
		this.causaleEntrata = causaleEntrata;
		this.dataCompetenzaDa = dataCompetenzaDa;
		this.dataCompetenzaA = dataCompetenzaA;
		
		this.log = new LogUtil(getClass());
		this.messaggi = new ArrayList<Messaggio>();
		
		this.predocumentiElaborati = new ArrayList<PreDocumentoEntrata>();
		this.predocumentiSaltati = new ArrayList<PreDocumentoEntrata>();
	}
	
	@Override
	public Void helpExecute() {
		final String methodName = "helpExecute";
		
		// 1. Calcolo i predocumenti
		leggiPredocumenti();
		
		if(skip) {
			addMessaggio("Elaborazione Definisce non eseguita: non ci sono Predisposizione di Incasso da definire");
			log.info(methodName, "Definizione dei predocumenti non necessario");
			return null;
		}
		
		// 2. Ciclo definisci predocumenti
		definisciPredocumenti();
		log.debug(methodName, "Definizione dei predocumenti avvenuta con successo");
		
		// 3. Fine fase di definizione
		scriviLog();
		
		return null;
	}
	
	/**
	 * Caricamento dei predocumenti
	 */
	private void leggiPredocumenti() {
		// Caricamento dei predocumenti in stato COMPLETATO
		preDocumentiEntrata = preDocumentoEntrataDad.findPreDocumentiByCausaleDataCompetenzaStati(
				causaleEntrata != null ? causaleEntrata.getUid() : null,
				dataCompetenzaDa,
				dataCompetenzaA,
				Arrays.asList(StatoOperativoPreDocumento.COMPLETO),
				// Model detail
				PreDocumentoEntrataModelDetail.Causale, PreDocumentoEntrataModelDetail.Classif, PreDocumentoEntrataModelDetail.Accertamento,
				PreDocumentoEntrataModelDetail.ProvvisorioDiCassa, PreDocumentoEntrataModelDetail.Sogg, PreDocumentoEntrataModelDetail.Stato, PreDocumentoEntrataModelDetail.AttoAmm);
		skip = preDocumentiEntrata == null || preDocumentiEntrata.isEmpty();
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
		messaggi.add(new Messaggio("Elaborazione Completamento e Definizione Predisposizioni di Incasso", descrizione));
	}
	
	/**
	 * Per tutti i PreDocumenti letti
	 * <ul>
	 *     <li>Effettuare la Definizione come descritto nel metodo Definisce PreDocumento Entrata</li>
	 * </ul>
	 * Qualunque errore che si riscontrasse deve essere scritto nel log.
	 * <p>
	 * Eventuali errori gravi bloccanti devono interrompere l'intera elaborazione con messaggio "ERRORE BLOCCANTE: Definizione non eseguita per: " messaggioErrore
	 */
	private void definisciPredocumenti() {
		final String methodName = "definisciPredocumenti";
		// Carico i predocumenti
		
		// Creo la request
		DefiniscePreDocumentoEntrata req = new DefiniscePreDocumentoEntrata();
		req.setBilancio(bilancio);
		req.setDataOra(new Date());
		req.setPreDocumentiEntrata(preDocumentiEntrata);
		req.setRichiedente(richiedente);
		// Skippo il caricamento a valle: ho gia' tutti i dati qua
		req.setSkipCaricamentoDettaglioPredocumento(true);
		
		// Invoco il servizio esterno
		DefiniscePreDocumentoEntrataResponse res = serviceExecutor.executeServiceSuccess(DefiniscePreDocumentoEntrataService.class, req);
		log.debug(methodName, "Definizione del predocumento avvenuta con successo? " + !res.hasErrori());
		
		predocumentiElaborati = res.getPredocumentiElaborati();
		predocumentiSaltati = res.getPredocumentiSaltati();
		
		// Aggiungo tutti i messaggi nella response
		messaggi.addAll(res.getMessaggi());
	}
	
	/**
	 * A conclusione del ciclo (buon fine)
	 * <ul>
	 *     <li>scrivere nel log "Elaborazione Definizione conclusa"</li>
	 * </ul>
	 */
	private void scriviLog() {
		// Log dei predocumenti elaborati e saltati (per comodita')
		elaboraLogPredocumenti("Definizione predocumenti: elaborati predocumenti ", predocumentiElaborati);
		elaboraLogPredocumenti("Definizione predocumenti: saltati predocumenti ", predocumentiSaltati);
		
		addMessaggio("Elaborazione Definizione conclusa");
	}
	
	/**
	 * Elaborazione del log dei predocumenti
	 * 
	 * @param intestazione l'intestazione del messaggio
	 * @param predocumenti i predocumento da iterare
	 */
	private void elaboraLogPredocumenti(String intestazione, Iterable<PreDocumentoEntrata> predocumenti) {
		int maxMessageLength = 2500;
		
		// Aggiungo tutti i numeri dei predoc in uno string builder
		StringBuilder sb = createStringBuilderNumeri(intestazione, predocumenti);
		// Splitto lo stringbuilder in sottomessaggi di lunghezza fissa
		List<String> messages = splitMessageByLength(sb.toString(), maxMessageLength);
		
		for(String message : messages) {
			// Loggo ogni messaggio
			addMessaggio(message);
		}
	}
	
	/**
	 * Creazione dello StringBuilder con tutti i numeri dei predoc
	 * @param intestazione l'intestazione del messaggio
	 * @param predocumenti i predocumento da iterare
	 * @return i numeri dei predoc
	 */
	private StringBuilder createStringBuilderNumeri(String intestazione, Iterable<PreDocumentoEntrata> predocumenti) {
		StringBuilder sb = new StringBuilder();
		sb.append(intestazione);
		boolean first = true;
		for(PreDocumentoEntrata pde : predocumenti) {
			// Aggiungo la virgola per tutti tranne che per il primo
			if(!first) {
				sb.append(", ");
			}
			first = false;
			// Aggiungo il numero
			sb.append(pde.getNumero());
		}
		return sb;
	}
	
	/**
	 * Split del messaggio per lunghezza massima
	 * @param msg il messaggio
	 * @param maxLength la lunghezza massima del sotto-messaggio
	 * @return i sotto-messaggi
	 */
	private List<String> splitMessageByLength(String msg, int maxLength) {
		List<String> result = new ArrayList<String>();
		// Se la stringa e' sufficientemente corta, la restituisco
		if(msg.length() <= maxLength) {
			result.add(msg);
			return result;
		}
		String tmp = msg;
		// Finche' il messaggio e' troppo lungo...
		while(tmp.length() > maxLength) {
			// Recupero l'indice dell'ultima virgola prima del cut-off (con padding per [...])
			int lastCommaIndex = tmp.lastIndexOf(',', maxLength - 6);
			// Se ho la virgola (dovrei sempre averla)...
			if(lastCommaIndex > -1) {
				// Ottengo la sottostringa
				String substring = tmp.substring(0, lastCommaIndex);
				// Aggiungo [...]
				result.add(substring + " [...]");
				// Prendo la sottostringa, aggiungendo un indice che mi consideri lo spazio dopo la virgola
				tmp = tmp.substring(lastCommaIndex + 2);
			} else {
				// Fallback per evitare ricorsione infinita
				result.add(tmp);
				return result;
			}
		}
		// Aggiungo il residuo
		result.add(tmp);
		return result;
	}
	
}
