/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.Helper;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.EntitaConsultabileDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.ContoCorrente;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Definizione del predocumento via l'elenco: helper per il completamento
 * @author Marchino Alessandro
 *
 */
public class CompletaDefiniscePreDocumentoEntrataCompletamentoHelper implements Helper<Void> {

	private final PreDocumentoEntrataDad preDocumentoEntrataDad;
	private final MovimentoGestioneServiceCallGroup mgscg;
	
	// Dati per ricerca
	private final Ente ente;
	private final CausaleEntrata causaleEntrata;
	private final Date dataCompetenzaDa;
	private final Date dataCompetenzaA;
	//SIAC-6780
	private ContoCorrentePredocumentoEntrata contoCorrente;
	private List<Integer> uidPredocumentiDaCompletare = new ArrayList<Integer>();
	
	// Dati per aggiornamento
	private final Accertamento accertamento;
	private final SubAccertamento subAccertamento;
	private final AttoAmministrativo attoAmministrativo;
	private final Soggetto soggetto;
	
	private final BigDecimal disponibilitaIncassare;
	
	//SIAC-6780
	private final ProvvisorioDiCassa provvisorioDiCassa;
	
	private final LogSrvUtil log;
	private final List<Messaggio> messaggi;
	
	private boolean skip;
	private BigDecimal sommaPredoc;
	private BigDecimal sommaPredocSenzaProvCassaCompletamento;
	// Per il log
	private List<PreDocumentoEntrata> preDocumentiCompletati;
	
	/**
	 * Construttore dell'helper.
	 *
	 * @param preDocumentoEntrataDad il dad del predocumento di entrata
	 * @param mgscg il call group per il movimento di gestione
	 * @param ente l'ente
	 * @param causaleEntrata la causale di entrata
	 * @param dataCompetenzaDa la data di competenza da
	 * @param dataCompetenzaA la data di competenza a
	 * @param contoCorrente the conto corrente
	 * @param uidPredocumentiDaFiltrare the uid predocumenti da filtrare
	 * @param accertamento l'accertamento
	 * @param subAccertamento lil subaccertamento
	 * @param attoAmministrativo l'atto amministrativo
	 * @param soggetto il soggetto
	 * @param disponibilitaIncassare la disponibilitì&agrave; ad incassare dell'accertamento o del sub
	 * @param provvisorioDiCassa the provvisorio di cassa
	 */
	public CompletaDefiniscePreDocumentoEntrataCompletamentoHelper(
			PreDocumentoEntrataDad preDocumentoEntrataDad,
			MovimentoGestioneServiceCallGroup mgscg,
			Ente ente,
			CausaleEntrata causaleEntrata,
			Date dataCompetenzaDa,
			Date dataCompetenzaA,
			ContoCorrentePredocumentoEntrata contoCorrente,
			List<Integer> uidPredocumentiDaFiltrare,
			Accertamento accertamento,
			SubAccertamento subAccertamento,
			AttoAmministrativo attoAmministrativo,
			Soggetto soggetto,
			BigDecimal disponibilitaIncassare,
			//SIAC-6780
			ProvvisorioDiCassa provvisorioDiCassa) {
		
		this.preDocumentoEntrataDad = preDocumentoEntrataDad;
		this.mgscg = mgscg;
		
		this.ente = ente;
		this.causaleEntrata = causaleEntrata;
		this.dataCompetenzaDa = dataCompetenzaDa;
		this.dataCompetenzaA = dataCompetenzaA;
		this.contoCorrente = contoCorrente;
		this.uidPredocumentiDaCompletare = uidPredocumentiDaFiltrare;
		
		this.accertamento = accertamento;
		this.subAccertamento = subAccertamento;
		this.attoAmministrativo = attoAmministrativo;
		this.soggetto = soggetto;
		this.disponibilitaIncassare = disponibilitaIncassare;
		this.provvisorioDiCassa = provvisorioDiCassa;
		
		this.messaggi = new ArrayList<Messaggio>();
		this.log = new LogSrvUtil(getClass());
		
		this.skip = false;
		this.preDocumentiCompletati = new ArrayList<PreDocumentoEntrata>();
	}
	
	@Override
	public Void helpExecute() {
		final String methodName = "helpExecute";
		// 1. Somma da completare e controllo capienza
		
		calcoloSommaDaCompletare();
		
		calcoloSommaDaCompletareSenzaProvCassa();
		
		checkCapienza();
		log.debug(methodName, "Controllo preliminare somma da completare e capienza completato");
		
		// Controllo se devo uscire
		if(skip) {
			log.info(methodName, "Completamento dei predocumenti non necessario");
			return null;
		}
		
		// 2. Ciclo completa PreDocumenti
		associaAccertamento();
		log.debug(methodName, "Associazione dell'accertamento " + accertamento.getUid() + " (subaccertamento " + (subAccertamento != null && subAccertamento.getUid() != 0 ? subAccertamento.getUid() : "null") + ") avvenuta con successo");
		
		// 3. Fina fase di completamento
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
		messaggi.add(new Messaggio("Elaborazione Completamento e Definizione Predisposizioni di Incasso", descrizione));
	}

	/**
	 * Viene calcolata la somma degli importi di tutti i pre-documenti in stato INCOMPLETO che rispettano i parametri di ricerca.
	 * <ul>
	 *     <li>SE la somma risultasse nulla vuol dire che non ci sono pre-documenti da completare e si passa alla seconda fase,
	 *     quella della Elaborazione Definizione indicandolo nel log (&rdquo;non sono presenti predisposizioni pagamento da completare&ldquo;)</li>
	 *     <li>SE la somma &eacute; &gt; 0 anche questo importo andr&agrave; indicato nel log (&rdquo;Elaboro predisposizioni incasso per &euro; 999.999.999,99&rdquo;)</li>
	 * </ol>
	 */
	private void calcoloSommaDaCompletare() {
		final String methodName = "calcoloSommaDaCompletare";
		
		// Calcolo dell'importo dei predoc
//		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
//		preDoc.setEnte(ente);
//		preDoc.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
//		preDoc.setCausaleEntrata(causaleEntrata);
//		
//		//SIAC-6780
//		preDoc.setProvvisorioDiCassa(provvisorioDiCassa);
//		preDoc.setContoCorrente(contoCorrente);
		
		
		sommaPredoc = preDocumentoEntrataDad.getImportoPredocByIds(this.uidPredocumentiDaCompletare);
		

		log.debug(methodName, "Somma dei predocumenti associati ai parametri di ricerca: " + sommaPredoc);
	}
	
	/**
	 * Viene calcolata la somma degli importi di tutti i pre-documenti in stato INCOMPLETO che rispettano i parametri di ricerca.
	 * <ul>
	 *     <li>SE la somma risultasse nulla vuol dire che non ci sono pre-documenti da completare e si passa alla seconda fase,
	 *     quella della Elaborazione Definizione indicandolo nel log (&rdquo;non sono presenti predisposizioni pagamento da completare&ldquo;)</li>
	 *     <li>SE la somma &eacute; &gt; 0 anche questo importo andr&agrave; indicato nel log (&rdquo;Elaboro predisposizioni incasso per &euro; 999.999.999,99&rdquo;)</li>
	 * </ol>
	 */
	private void calcoloSommaDaCompletareSenzaProvCassa() {
		final String methodName = "calcoloSommaDaCompletareSenzaProvCassa";
		if(this.provvisorioDiCassa == null || this.provvisorioDiCassa.getUid() == 0) {
			return;
		}
		sommaPredocSenzaProvCassaCompletamento = preDocumentoEntrataDad.getImportoPredocByIdsNoProvcassa(this.uidPredocumentiDaCompletare, this.provvisorioDiCassa);
		

		log.debug(methodName, "Somma dei predocumenti associati ai parametri di ricerca: " + sommaPredoc);
	}
	
	/**
	 * Controllo della capienza.
	 * <p>
	 * Confrontare la disponibilit&agrave; a incassare dell’accertamento con la somma degli importi dei PreDocumenti da elaborare.
	 * <br/>
	 * Se non viene superato il controllo deve essere inserita una MODIFICA di importo sull'accertamento a copertura della somma da completare
	 * e viene indicato sul log (&rdquo;Accertamento non capiente, creata modifica per &euro; 99.999.999,00&ldquo;).
	 */
	private void checkCapienza() {
		final String methodName = "checkCapienza";
		boolean provvisorioPresente = provvisorioDiCassa != null && provvisorioDiCassa.getUid() != 0;  
		//SIAC-7457
		if(provvisorioPresente && (provvisorioDiCassa.getImportoDaRegolarizzare() == null || this.sommaPredocSenzaProvCassaCompletamento == null || provvisorioDiCassa.getImportoDaRegolarizzare().compareTo(sommaPredocSenzaProvCassaCompletamento) < 0) ) {
			skip = true;
			return;			
		}
		// Controllo che ci sia dell'importo da completare
		if(BigDecimal.ZERO.compareTo(sommaPredoc) == 0) {
			addMessaggio("Elaborazione Convalida non eseguita: non ci sono Predisposizione di Incasso da completare");
			skip = true;
			return;
		}
		addMessaggio("Elaborazione predisposizioni di incasso per " + Utility.formatCurrency(sommaPredoc));
		// Controllo di non sfondare la disponibilita' dell'accertamento
		if(disponibilitaIncassare.compareTo(sommaPredoc) < 0) {
			log.debug(methodName, "Necessario inserire modifica sull'accertamento");
			BigDecimal importoModifica = inserisciModifica();
			addMessaggio("Accertamento non capiente, creata modifica per " + Utility.formatCurrency(importoModifica));
		}
	}
	
	/**
	 * Inserimento della modifica della capienza
	 */
	private BigDecimal inserisciModifica() {
		BigDecimal importoModifica = sommaPredoc.subtract(disponibilitaIncassare);
		
		if(subAccertamento != null) {
			// Ho subAccertamento
			inserisciModificaSubAccertamento(importoModifica);
			return importoModifica;
		}
		// Ho l'accertamento (niente sub)
		inserisciModificaAccertamento(importoModifica);
		return importoModifica;
	}
	
	/**
	 * In caso di subaccertamento:
	 * <ol>
	 *     <li>verificare se la modifica di sub &eacute; &lt;= al disponibile a subaccertare e inserire una 'modifica di importo' di subaccertamento
	 *     pari alla differenza tra importoAttuale del subaccertamento e disponibilit&agrave; a incassare del subaccertamento;</li>
	 *     <li>altrimenti se modifica di sub &eacute; &gt; al disponibile a subaccertare occorrer&agrave; inserire contenstualmente e subito prima una modifica
	 *     di accertamento pari alla differenza (tra importo modifica e disponibile a subaccertare).</li>
	 * </ol>
	 * @param importoModifica l'importo della modifica
	 */
	private void inserisciModificaSubAccertamento(BigDecimal importoModifica) {
		if(importoModifica.compareTo(accertamento.getDisponibilitaIncassare()) > 0){
			// Inserimento contestuale della modifica accertamento
			BigDecimal importoModificaAccertamento = importoModifica.subtract(accertamento.getDisponibilitaIncassare());
			mgscg.inserisciModificaImportoMovimentoGestioneEntrataSuccess(accertamento, null, importoModificaAccertamento, CostantiFin.MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO);
		}
		
		mgscg.inserisciModificaImportoMovimentoGestioneEntrataSuccess(accertamento, subAccertamento, importoModifica, CostantiFin.MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO);
	}
	
	/**
	 * In caso di subaccertamento:
	 * <ol>
	 *     <li>inserire una 'modifica di importo' di accertamento pari alla differenza tra importoAttuale dell'accertamento e disponibilit&agrave; a incassare dell'accertamento</li>
	 * </ol>
	 * @param importoModifica l'importo della modifica
	 */
	private void inserisciModificaAccertamento(BigDecimal importoModifica) {
		mgscg.inserisciModificaImportoMovimentoGestioneEntrataSuccess(accertamento, null, importoModifica, CostantiFin.MODIFICA_AUTOMATICA_PREDISPOSIZIONE_INCASSO);
	}

	/**
	 * Associazione dell'accertamento
	 */
	private void associaAccertamento() {
		PreDocumentoEntrata tmp = new PreDocumentoEntrata();
		tmp.setAccertamento(accertamento);
		tmp.setSubAccertamento(subAccertamento);
		tmp.setAttoAmministrativo(attoAmministrativo);
		tmp.setSoggetto(soggetto);
		tmp.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.COMPLETO);
		tmp.setEnte(ente);
		
		//SIAC-6780
		tmp.setProvvisorioDiCassa(provvisorioDiCassa);
		
		//SIAC-7561
		//devo controllare anche gli stati operativi in stato completo per assicurarmi che abbiano un provvisorio associato
		StatoOperativoPreDocumento[] arrStOpPreDoc = { StatoOperativoPreDocumento.INCOMPLETO , StatoOperativoPreDocumento.COMPLETO };
		
		preDocumentiCompletati = preDocumentoEntrataDad.associaMovgestAttoAmmSoggettoByIds(
				tmp,
				null,
				null,
				null,
				null,
				uidPredocumentiDaCompletare,
				arrStOpPreDoc);
	}
	
	/**
	 * Scrivere nel log "Elaborazione Convalida conclusa"
	 */
	private void scriviLog() {
		int maxMessageLength = 2500;
		// Log dei predocumenti elaborati e saltati (per comodita')
		
		// Aggiungo tutti i numeri dei predoc in uno string builder
		StringBuilder sb = createStringBuilderNumeri();
		// Splitto lo stringbuilder in sottomessaggi di lunghezza fissa
		List<String> messages = splitMessageByLength(sb.toString(), maxMessageLength);
		
		for(String message : messages) {
			// Loggo ogni messaggio
			addMessaggio(message);
		}
		// Loggo il messaggio terminale
		addMessaggio("Elaborazione Completa conclusa");
	}
	
	/**
	 * Creazione dello StringBuilder con tutti i numeri dei predoc
	 * @return i numeri dei predoc
	 */
	private StringBuilder createStringBuilderNumeri() {
		StringBuilder sb = new StringBuilder();
		sb.append("Completamento predocumenti: elaborati predocumenti ");
		boolean first = true;
		for(PreDocumentoEntrata pde : preDocumentiCompletati) {
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

