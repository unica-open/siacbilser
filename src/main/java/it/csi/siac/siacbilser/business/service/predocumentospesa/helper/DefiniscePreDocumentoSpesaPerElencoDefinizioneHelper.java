/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.Helper;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Definizione del predocumento via l'elenco: helper per il completamento
 * @author Marchino Alessandro
 *
 */
public class DefiniscePreDocumentoSpesaPerElencoDefinizioneHelper implements Helper<Void> {

	private final ImpegnoBilDad impegnoBilDad;
	private final PreDocumentoSpesaDad preDocumentoSpesaDad;
	private final ServiceExecutor serviceExecutor;
	
	private final Richiedente richiedente;
	private final Bilancio bilancio;
	private final ElencoDocumentiAllegato elencoDocumentiAllegato;
	private final Impegno impegno;
	private final SubImpegno subImpegno;
	
	private final LogUtil log;
	private final List<Messaggio> messaggi;
	
	private boolean skip;
	private BigDecimal sommaPredoc;
	private Long contaPredoc;
	
	/**
	 * Construttore dell'helper
	 */
	public DefiniscePreDocumentoSpesaPerElencoDefinizioneHelper(
			ImpegnoBilDad impegnoBilDad,
			PreDocumentoSpesaDad preDocumentoSpesaDad,
			ServiceExecutor serviceExecutor,
			Richiedente richiedente,
			Bilancio bilancio,
			ElencoDocumentiAllegato elencoDocumentiAllegato,
			Impegno impegno,
			SubImpegno subImpegno) {
		this.impegnoBilDad = impegnoBilDad;
		this.preDocumentoSpesaDad = preDocumentoSpesaDad;
		this.serviceExecutor = serviceExecutor;
		
		this.richiedente = richiedente;
		this.bilancio = bilancio;
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
		this.impegno = impegno;
		this.subImpegno = subImpegno;
		
		this.log = new LogUtil(getClass());
		this.messaggi = new ArrayList<Messaggio>();
	}
	
	@Override
	public Void helpExecute() {
		final String methodName = "helpExecute";
		// 1. Controllo preliminare capienza
		calcoloDisponibilitaImpegno();
		log.debug(methodName, "Controllo disponibilita' impegno soddisfatto");
		
		// 2. Ricerca predocumenti da definire
		calcoloImporti();
		checkImporti();
		log.debug(methodName, "Ricerca predocumenti da definire completata");
		// Controllo se devo uscire
		if(skip) {
			log.info(methodName, "Definizione dei predocumenti dell'elenco " + elencoDocumentiAllegato.getUid() + " non necessario");
			return null;
		}
		
		// 3. Ciclo definisci predocumenti
		definisciPredocumenti();
		log.debug(methodName, "Definizione dei predocumenti afferenti l'elenco " + elencoDocumentiAllegato.getUid() + " avvenuta con successo");
		
		// 4. Fine fase di definizione
		scriviLog();
		
		return null;
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
	 * Calcolare la DISPONIBILIT&Agrave; A LIQUIDARE dell'impegno
	 * <p>
	 * SE DISPONIBILIT&Agrave; A LIQUIDARE &lt; 0
	 * <ul>
	 *     <li>Scrivere nel log "ERRORE BLOCCANTE: il disponibile dell'impegno &egrave; di €" DISPONIBILIT&Agrave; A LIQUIDARE
	 *     " Impossibile proseguire con la Definizione. Controllare e correggere i dati prima di sottomettere nuovamente"</li>
	 *     <li>Interrompere l'elaborazione</li>
	 * </ul>
	 * @param annoBilancio 
	 */
	private void calcoloDisponibilitaImpegno() {
		DisponibilitaMovimentoGestioneContainer disponibilitaLiquidare = impegnoBilDad.ottieniDisponibilitaLiquidare(impegno, subImpegno, bilancio.getAnno());
		if(disponibilitaLiquidare == null || disponibilitaLiquidare.getDisponibilita() == null || BigDecimal.ZERO.compareTo(disponibilitaLiquidare.getDisponibilita()) > 0) {
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("ERRORE BLOCCANTE: il disponibile dell'impegno è di " + Utility.formatCurrency(disponibilitaLiquidare.getDisponibilita())
				+ " Impossibile proseguire con la Definizione. Controllare e correggere i dati prima di sottomettere nuovamente"));
		}
	}
	
	/**
	 * <ol>
	 *     <li>Leggere, sommare l'importo (SOMMA PREDOC) e conteggiare (CONTA PREDOC) tutti i PreDocumenti di spesa:
	 *     <ul>
	 *         <li>In stato COMPLETATO</li>
	 *         <li>Collegati all'Elenco e all'impegno passati in input</li>
	 *     </ul>
	 *     <li>Inserire la seguente riga di log: "Richiesta definizione di " CONTA PREDOC " PreDocumenti per una somma di &euro; " SOMMA PREDOC</li>
	 * </ol>
	 */
	private void calcoloImporti() {
		final String methodName = "calcoloImporti";
		// Calcolo dell'importo dei predoc
		sommaPredoc = preDocumentoSpesaDad.findImportoPreDocumentoByElencoIdAndStatiOperativi(elencoDocumentiAllegato.getUid(),
				impegno.getUid(), subImpegno != null ? subImpegno.getUid() : null, StatoOperativoPreDocumento.COMPLETO);
		// Calcolo del numero dei predoc
		contaPredoc = preDocumentoSpesaDad.countPreDocumentoByElencoIdAndStatiOperativi(elencoDocumentiAllegato.getUid(),
				impegno.getUid(), subImpegno != null ? subImpegno.getUid() : null, StatoOperativoPreDocumento.COMPLETO);
		
		String messaggio = new StringBuilder()
				.append("Richiesta definizione di ")
				.append(contaPredoc)
				.append(" PreDocumenti per una somma di ")
				.append(Utility.formatCurrency(sommaPredoc))
				.toString();
		log.debug(methodName, messaggio);
		
		addMessaggio(messaggio);
	}

	/**
	 * Verificare l'eseguibilità della fase:
	 * SE SOMMA PREDOC = 0
	 * <ul>
	 *     <li>Scrivere nel log "Elaborazione Definizione non eseguita: non ci sono PreDocumenti Completi per l'elenco e l'impegno indicati"</li>
	 *     <li>Chiudere l'elaborazione</li>
	 * </ul>
	 * ALTRIMENTI
	 * <ul>
	 *     <li>l'elaborazione prosegue regolarmente</li>
	 * </ul>
	 */
	private void checkImporti() {
		// Controllo che ci sia dell'importo da completare
		if(BigDecimal.ZERO.compareTo(sommaPredoc) == 0) {
			addMessaggio("Elaborazione Definizione non eseguita: non ci sono PreDocumenti Completi per l'elenco e l'impegno indicati");
			skip = true;
			return;
		}
	}
	
	/**
	 * Per tutti i PreDocumenti letti
	 * <ul>
	 *     <li>Effettuare la Definizione come descritto nel metodo Definisce PreDocumento Spesa</li>
	 * </ul>
	 * Qualunque errore che si riscontrasse deve essere scritto nel log.
	 * <p>
	 * Eventuali errori gravi bloccanti devono interrompere l'intera elaborazione con messaggio "ERRORE BLOCCANTE: Definizione non eseguita per: " messaggioErrore
	 */
	private void definisciPredocumenti() {
		final String methodName = "definisciPredocumenti";
		// Carico i predocumenti
		List<PreDocumentoSpesa> preDocumentiSpesa = preDocumentoSpesaDad.findPreDocumentoByElencoIdAndStatiOperativi(elencoDocumentiAllegato.getUid(),
				impegno.getUid(), subImpegno != null ? subImpegno.getUid() : null, Arrays.asList(StatoOperativoPreDocumento.COMPLETO),
				// Model detail
				PreDocumentoSpesaModelDetail.Causale, PreDocumentoSpesaModelDetail.Classif, PreDocumentoSpesaModelDetail.ContoTesoreria,
				PreDocumentoSpesaModelDetail.Impegno, PreDocumentoSpesaModelDetail.ModPag, PreDocumentoSpesaModelDetail.ProvvisorioDiCassa,
				PreDocumentoSpesaModelDetail.Sogg, PreDocumentoSpesaModelDetail.VoceMutuo,
				//SIAC-6784
				PreDocumentoSpesaModelDetail.ElencoDocumentiAllegato
				);
		
		// Creo la request
		DefiniscePreDocumentoSpesa req = new DefiniscePreDocumentoSpesa();
		req.setBilancio(bilancio);
		req.setDataOra(new Date());
		req.setPreDocumentiSpesa(preDocumentiSpesa);
		req.setRichiedente(richiedente);
		// Skippo il caricamento a valle: ho gia' tutti i dati qua
		req.setSkipCaricamentoDettaglioPredocumento(true);
		
		// Invoco il servizio esterno
		DefiniscePreDocumentoSpesaResponse res = serviceExecutor.executeServiceSuccess(DefiniscePreDocumentoSpesaService.class, req);
		log.debug(methodName, "Definizione del predocumento avvenuta con successo? " + !res.hasErrori());
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
		addMessaggio("Elaborazione Definizione conclusa");
	}
}
