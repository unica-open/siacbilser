/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.StampaRegistroIvaReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistoIvaDatoIvaVendite;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaDatiIva;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaRiepilogo;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaRiepilogoIva;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;


/**
 * Handler per la stampa del registro IVA di tipo vendite.
 * 
 */
public abstract class StampaRegistroIvaVenditeReportHandler extends StampaRegistroIvaReportHandler<SubdocumentoIvaEntrata> {
	
	@Autowired
	protected DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	protected SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	protected SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	
	protected List<SubdocumentoIvaEntrata> listaSubdocumentoIvaEntrata;
	
	/**
	 * Popola i campi comuni a livello dell'istanza.
	 */
	@Override
	protected void popolaDatiAggiuntiviAPartireDaiDatiDiInput() {
		final String methodName = "popolaCampiComuni";
		// Ottengo la lista dei subdocumenti iva
		ottieniListaSubdocumentoIvaEntrata();
		log.debug(methodName, "Subdocumenti iva trovati: " + listaSubdocumentoIvaEntrata.size());
	}

	/**
	 * Ottiene la lista dei SubdocumentiIvaEntrata iterando n volte la ricerca sintetica.
	 * 
	 * @return la lista dei subdocumenti di entrata
	 */
	protected abstract void ottieniListaSubdocumentoIvaEntrata();
	
	/**
	 * Ottiene la lista dei SubdocumentiIvaEntrata del registro nello specificato periodo iterando n volte la ricerca sintetica
	 * 
	 * @return la lista dei subdocumenti di entrata
	 */
	protected abstract List<SubdocumentoIvaEntrata> ottieniListaSubdocumentoIvaEntrataNelPeriodo(Periodo p);
	
	/**
	 * Compila la sezione 1 della stampa.
	 * 
	 * @param titoloSezione il titolo della sezione
	 */
	@Override
	protected void compilaSezione1() {
		StampaRegistroIvaDatiIva sezione1 = new StampaRegistroIvaDatiIva();
		result.setDatiIva(sezione1);
		// Popolo la sezione
		popolamentoSezione1(sezione1);
		sortSezione1(sezione1);
	}
	
	/**
	 * Compila la sezione 2 della stampa.
	 * 
	 * @param titoloSezione il titolo della sezione
	 */
	@Override
	protected void compilaSezione2() {
		List<StampaRegistroIvaRiepilogo> sezione2 = new ArrayList<StampaRegistroIvaRiepilogo>();
		result.setListaRiepilogo(sezione2);
		// Popolo la sezione
		popolamentoSezione2(sezione2);
	}
	
	/**
	 * Popolamento della sezione 1.
	 * 
	 * @param sezione1 la sezione da popolare
	 */
	protected void popolamentoSezione1(StampaRegistroIvaDatiIva sezione1) {
		final String methodName = "popolamentoSezione1";
		/*
		 * La lista di elementi è composta dal numero dei codici Aliquota Iva utilizzati nell’anno di esercizio
		 * da tutti i Registri Iva di tipo [tipoRegistro] del Gruppo selezionato.
		 * Per ogni codice si riportano anche i corrispondenti valori di percAliquota, Tipo Operazione Iva  e descrizione.
		 */
		List<DatiIva> listaRighe = new ArrayList<DatiIva>();
		
		// Inizializzo i campi per i totali
		// Somma colonne I1
		BigDecimal sumImponibile = BigDecimal.ZERO;
		// Somma colonne L1
		BigDecimal sumIVA = BigDecimal.ZERO;
		// Somma colonne O1
		BigDecimal sumTotale = BigDecimal.ZERO;
		
		for(SubdocumentoIvaEntrata sie : listaSubdocumentoIvaEntrata) {
			log.debug(methodName, "SubdocumentoIvaEntrata.uid: " + sie.getUid());
			// Ottengo i dati del documento
			Documento<?,?> d = sie.getDocumentoCollegato(); //di solito il Documento restituito e' sempre di tipo Entrata. Ad eccezioni delle Controregistrazioni Intrastat. Vedi servizio InserisceControregistrazioneService.
			if(d == null || d.getUid() == 0) {
				throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nessun documento collegato al subdocumento " + sie.getUid() + " trovato"));
			}
			d = documentoEntrataDad.findDocumentoEntrataById(d.getUid());
			log.debug(methodName, "SubdocumentoIvaEntrata.documentoCollegato.uid: " + d.getUid());
			SubdocumentoEntrata se = sie.getSubdocumento();
			log.debug(methodName, "SubdocumentoIvaEntrata.subdocumentoEntrata == null? " + (se == null ? "true" : ("false. uid: " + se.getUid())));
			AttivitaIva ati = sie.getAttivitaIva();
			log.debug(methodName, "SubdocumentoIvaEntrata.attivitaIva == null? " + (ati == null ? "true" : ("false. uid: " + ati.getUid())));
			
			// Costruisco la riga
			for(AliquotaSubdocumentoIva asi : sie.getListaAliquotaSubdocumentoIva()) {
				final int uidAliquota = asi.getUid();
				log.debug(methodName, "AliquotaSubdocumentoIva.uid: " + uidAliquota);
				StampaRegistoIvaDatoIvaVendite riga = new StampaRegistoIvaDatoIvaVendite();
				
				// Popolo la riga
				
				// A1 - numProtocolloDef - Subdocumento Iva Entrata
				// B1 - dataProtocolloDef - Subdocumento Iva Entrata
				// P1 - flagRilevanteIRAP - Subdocumento Iva Entrata - Se = 0 nel layout si visualizza “No”, se =1 si visualizza “Si”
				// Q1 - numOrdinativoDoc - Subdocumento Iva Entrata
				riga.setSubdocumentoIva(sie);
				log.debug(methodName, uidAliquota + " - A1 - numProtocolloDef - " + sie.getNumeroProtocolloDefinitivo());
				log.debug(methodName, uidAliquota + " - B1 - dataProtocolloDef - " + sie.getDataProtocolloDefinitivo());
				log.debug(methodName, uidAliquota + " - P1 - flagRilevanteIRAP - " + sie.getFlagRilevanteIRAP());
				log.debug(methodName, uidAliquota + " - Q1 - numOrdinativoDoc - " + sie.getNumeroOrdinativoDocumento());
				
				// C1 - dataEmissione - DocumentoEntrata
				// D1 - numero - DocumentoEntrata
				// E1 - codice - TipoDocumento - Codice + descrizione separati dal carattere ‘-‘
				// F1 - codice - Soggetto
				// G1 - denominazione - Soggetto
				riga.setDocumento(d);
				log.debug(methodName, uidAliquota + " - C1 - dataEmissione - " + d.getDataEmissione());
				log.debug(methodName, uidAliquota + " - D1 - numero - " + d.getNumero());
				log.debug(methodName, uidAliquota + " - E1 - tipoDocumento - " + (d.getTipoDocumento() != null ? d.getTipoDocumento().getCodice() : "null"));
				log.debug(methodName, uidAliquota + " - F1 - soggetto - codice - " + (d.getSoggetto() != null ? d.getSoggetto().getCodiceSoggetto() : "null"));
				log.debug(methodName, uidAliquota + " - G1 - soggetto - denominazione -" + (d.getSoggetto() != null ? d.getSoggetto().getDenominazione() : "null"));
				
				// R1 - numero - Subdocumento - Opzionale, può anche essere nullo. E’ riferito al Subdocumento finanizario
				riga.setSubdocumento(se);
				log.debug(methodName, uidAliquota + " - R1 - numeroSubdocumento - " + (se != null ? se.getNumero() : "null"));
				
				// H1 - codice - Attività Iva - Solo se il codice è significativo (stringa), altrimenti si prende il campo “descrizione”
				riga.setAttivitaIva(ati);
				log.debug(methodName, uidAliquota + " - H1 - attivitaIva - codice - " + (ati != null ? ati.getCodice() : "null"));
				
				//JIRA 3246
//				BigDecimal importoTotaleAsi = d.getTipoDocumento().isNotaCredito()? asi.getTotale().negate() : asi.getTotale();
//				BigDecimal imponibileAsi =  d.getTipoDocumento().isNotaCredito() ? asi.getImponibile().negate() : asi.getImponibile();
//				BigDecimal impostaAsi =  d.getTipoDocumento().isNotaCredito() ?asi.getImposta().negate() : asi.getImposta() ;
				//SIAC-3791
				BigDecimal importoTotaleAsi = asi.getTotale().setScale(2, RoundingMode.HALF_DOWN);
				BigDecimal imponibileAsi = asi.getImponibile().setScale(2, RoundingMode.HALF_DOWN);
				BigDecimal impostaAsi = asi.getImposta().setScale(2, RoundingMode.HALF_DOWN) ;
//				asi.setTotale(importoTotaleAsi);
//				asi.setImposta(impostaAsi);
//				asi.setImponibile(imponibileAsi);
				
				// I1 - imponibile - Aliquota Subdocumento Iva
				sumImponibile = sumImponibile.add(imponibileAsi);
				log.debug(methodName, uidAliquota + " - I1 - imponibile - " + imponibileAsi);
				// L1 - imposta - Aliquota Subdocumento Iva
				sumIVA = sumIVA.add(impostaAsi);
				log.debug(methodName, uidAliquota + " - L1 - imposta - " + impostaAsi);
				// M1 - codice - Aliquota Iva
				// N1 - descrizione - Aliquota Iva

				riga.setAliquotaSubdocumentoIva(asi);
				log.debug(methodName, uidAliquota + " - M1 - aliquotaIva - codice - " + asi.getAliquotaIva().getCodice());
				log.debug(methodName, uidAliquota + " - N1 - aliquotaIva - descrizione - " + asi.getAliquotaIva().getDescrizione());
				
				// O1 - totale - Aliquota Subdocumento Iva
				riga.setTotale(importoTotaleAsi);
				sumTotale = sumTotale.add(importoTotaleAsi);
				log.debug(methodName, uidAliquota + " - O1 - totale - " + asi.getTotale());
				
				// Aggiungo la riga nella lista
				listaRighe.add(riga);
			}
		}
		
		sezione1.setListaDatiIva(listaRighe);
		sezione1.setTotaleImponibile(sumImponibile);
		sezione1.setTotaleImposta(sumIVA);
		sezione1.setTotaleTotale(sumTotale);
		
		log.debug(methodName, "Totale imponibile: " + sumImponibile);
		log.debug(methodName, "Totale imposta: " + sumIVA);
		log.debug(methodName, "Totale totale: " + sumTotale);
	}
	
	/**
	 * Ordina i dati della sezione 1, secondo la logica <em>ordinati rispettivamente per data registrazione e numero protocollo</em>.
	 * 
	 * @param sezione1 la sezione da ordinare
	 */
	protected abstract void sortSezione1(StampaRegistroIvaDatiIva sezione1);
	
	/**
	 * Compilazione della sezione 2
	 * 
	 * @param sezione2 la sezione da popolare
	 */
	protected void popolamentoSezione2(List<StampaRegistroIvaRiepilogo> sezione2) {
		final String methodName = "popolamentoSezione2";
		//popolo A2, B2, C2, E2
		Map<String, StampaRegistroIvaRiepilogoIva> mapAliquotePeriodoInStampa = popolaRigheRiepilogo(listaSubdocumentoIvaEntrata);
		
		log.debug(methodName, "Accorpamento righe terminato per il periodo oggetto di stampa: " + getPeriodo().getDescrizione() + ". Calcolo dei totali");
		// popolo G2, H2,I2,L2,
		//sumImponibile, sumIVA, sumTotale, sumProgressivoImponibile, sumProgressivoIVA sumTotaleProgressivo

		//e' il riepilo del periodo della ricerca (quello scelto dalla mschera di ricerca )
		StampaRegistroIvaRiepilogo riepilogoSezioneRicercata = new StampaRegistroIvaRiepilogo();
		
		//il periodo e' quello della ricerca 
		riepilogoSezioneRicercata.setPeriodo(getPeriodo());
		riepilogoSezioneRicercata = calcolaEPopolaTotali(riepilogoSezioneRicercata, mapAliquotePeriodoInStampa,getPeriodo());
		sezione2.add(riepilogoSezioneRicercata);

		//CR-3545 : mostro anche i riepiloghi precedenti
		Periodo p = getPeriodo().precedente();
		List<StampaRegistroIvaRiepilogo> listaRiepiloghiIvaPeriodiPrecedenti = new ArrayList<StampaRegistroIvaRiepilogo>();
		
		//CR 3545: mostro anche i riepiloghi precedenti
		while(p!=null){
			StampaRegistroIvaRiepilogo stampaRiepilogoIvaMesiPrecedenti = new StampaRegistroIvaRiepilogo();
			List<SubdocumentoIvaEntrata> listaSubdocumentiIvaEntrataPeriodo = ottieniListaSubdocumentoIvaEntrataNelPeriodo(p);
				//popolo A2, B2, C2, E2
				Map<String, StampaRegistroIvaRiepilogoIva> mapAliquotePeriodoPrecedente = popolaRigheRiepilogo(listaSubdocumentiIvaEntrataPeriodo);
				// popolo G2, H2,I2,L2 e totali
				StampaRegistroIvaRiepilogo s = calcolaEPopolaTotali(stampaRiepilogoIvaMesiPrecedenti, mapAliquotePeriodoPrecedente,p);
				s.setPeriodo(p);
				listaRiepiloghiIvaPeriodiPrecedenti.add(s);
				p = p.precedente();			
		}
		sezione2.addAll(listaRiepiloghiIvaPeriodiPrecedenti);
	}
	


	/**
	 * Calcola e popola i valori totali per ogni aliquota e, 
	 * se il riepilogo &egrave relativo al periodo che si sta stampando, calcola e popola il totale 
	 * 
	 * @return Map<String,StampaRegistroIvaRiepilogoIva> la mappa contenente un oggetto 
	 * per ogni aliquota usata nel registro
	 *  
	 * */
	private StampaRegistroIvaRiepilogo calcolaEPopolaTotali(StampaRegistroIvaRiepilogo sezione,Map<String, StampaRegistroIvaRiepilogoIva> map, Periodo p) {
		final String methodName = "calcolaEPopolaTotali";
		
		log.debug(methodName, "periodo p : " + p.getCodice());
		
		List<DatiIva> listaRighe = new ArrayList<DatiIva>();
		// Inizializzo i campi per i totali
		// Somma colonne E2
		BigDecimal sumImponibile = BigDecimal.ZERO;
		// Somma colonne F2
		BigDecimal sumIVA = BigDecimal.ZERO;
		// Somma colonne G2
		BigDecimal sumTotale = BigDecimal.ZERO;
		// Somma colonne H2
		BigDecimal sumProgressivoImponibile = BigDecimal.ZERO;
		// Somma colonne I2
		BigDecimal sumProgressivoIVA = BigDecimal.ZERO;
		// Somma colonne L2
		BigDecimal sumTotaleProgressivo = BigDecimal.ZERO;
		
		for(StampaRegistroIvaRiepilogoIva riga : map.values()) {
			final String codiceAliquota = riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice();
			
			// Somma colonne E2
			log.debug(methodName, codiceAliquota + " - totaleImponibile - old " + sumImponibile + " - added "
					+ riga.getImponibile());
			sumImponibile = sumImponibile.add(riga.getImponibile());
			log.debug(methodName, codiceAliquota + " - totaleImponibile - new " + sumImponibile);
			
			// Somma colonne F2
			log.debug(methodName, codiceAliquota + " - totaleIVA - old " + sumIVA + " - added " + riga.getIva());
			sumIVA = sumIVA.add(riga.getIva());
			log.debug(methodName, codiceAliquota + " - totaleIVA - new " + sumIVA);
			
			// G2 - Totale: è dato dalla sommatoria dei valori di E2 e F2 - G2=E2+F2
			riga.setTotale(riga.getImponibile().add(riga.getIva()));
			
			// Somma colonne G2
			log.debug(methodName, codiceAliquota + " - totale - old " + sumTotale + " - added " + riga.getTotale());
			sumTotale = sumTotale.add(riga.getTotale());
			log.debug(methodName, codiceAliquota + " - totaleTotale - new " + sumTotale);
			
			ProgressiviIva piPrecedente = ottieniProgressiviIvaPrecedente(riga.getAliquotaSubdocumentoIva().getAliquotaIva(), p);
			if(!tipoStampaBozza) {
				BigDecimal totaleImponibileProgressivoIva = obtainTotaleImponibile(piPrecedente).setScale(2, RoundingMode.HALF_DOWN);
				BigDecimal totaleIvaProgressivoIva = obtainTotaleIva(piPrecedente).setScale(2, RoundingMode.HALF_DOWN);
				
				// H2 - calcolo i totali del progressivo
//				riga.setProgressivoImponibile(riga.getProgressivoImponibile().add(totaleImponibileProgressivoIva));
				riga.setProgressivoImponibile(totaleImponibileProgressivoIva);
				// I2 - calcolo i totali del progressivo
//				riga.setProgressivoIva(riga.getProgressivoIva().add(totaleIvaProgressivoIva));
				riga.setProgressivoIva(totaleIvaProgressivoIva);
				
				log.debug(methodName,
						codiceAliquota + " - progressivoImponibile - new " + riga.getProgressivoImponibile());
				// I2 - calcolo i totali del progressivo
				log.debug(methodName, codiceAliquota + " - progressivoIva - new " + riga.getProgressivoIva());
			}
			
			// H2 - Progr. Imponibile
			/*
			 * CASO 1: Tipo Stampa=Bozza (valore 0)
			 * Il valore del campo coincide con quello dell’Imponibile (H2=E2)
			 * 
			 * CASO 2: Tipo Stampa=Definitiva (valore 1)
			 * Il valore del campo viene calcolato in questo modo:
			 * Valore dell’imponibile per il periodo selezionato per il corrispondente codice aliquota  (ovvero E2)
			 * + sommatoria dei valori dell’imponibile per il corrispondente codice aliquota dei periodi precedenti
			 * (ovviamente sempre per lo stesso Ente, Anno Esercizio e Registro Iva).
			 * Il valore da sommare a E2 viene ricavato accedendo all’entità “Progressivi Iva” e prendendo il valore del campo “totImponibileDef”.
			 */
			riga.setProgressivoImponibile(riga.getImponibile().add(riga.getProgressivoImponibile()));
			log.debug(methodName, codiceAliquota + " - totaleProgressivoImponibile - old " + sumProgressivoImponibile
					+ " - added " + riga.getProgressivoImponibile());
			// Somma colonne H2
			sumProgressivoImponibile = sumProgressivoImponibile.add(riga.getProgressivoImponibile());
			log.debug(methodName,
					codiceAliquota + " - totaleProgressivoImponibile - new " + sumProgressivoImponibile);
			// I2 - Progr. IVA
			/*
			 * CASO 1: Tipo Stampa=Bozza (valore 0)
			 * Il valore del campo coincide con quello dell’IVA (I2=F2)
			 * 
			 * CASO 2: Tipo Stampa=Definitiva (valore 1)
			 * Il valore del campo viene calcolato in questo modo:
			 * Valore dell’IVA per il periodo selezionato per il corrispondente codice aliquota  (ovvero F2)
			 * + sommatoria dei valori dell’IVA per il corrispondente codice aliquota dei periodi precedenti
			 * (ovviamente sempre per lo stesso Ente, Anno Esercizio e Registro Iva).
			 * Il valore da sommare a F2 viene ricavato accedendo all’entità “Progressivi Iva” e prendendo il valore del campo “totIvaDef”.
			 */
			riga.setProgressivoIva(riga.getIva().add(riga.getProgressivoIva()));
			log.debug(methodName, codiceAliquota + " - totaleProgressivoIva - old " + sumProgressivoIVA + " - added "
					+ riga.getProgressivoIva());
			// Somma colonne I2
			sumProgressivoIVA = sumProgressivoIVA.add(riga.getProgressivoIva());
			log.debug(methodName, codiceAliquota + " - totaleProgressivoIva - new " + sumProgressivoIVA);
			
			
			// Imposto i parziali nel progressivo attuale
			ProgressiviIva piAttuale = ottieniProgressiviIvaAttuale(riga.getAliquotaSubdocumentoIva().getAliquotaIva());
			popolaTotaleImponibile(piAttuale, riga);
			popolaTotaleIva(piAttuale, riga);
			
			// L2 - Totale Progressivo
			/*
			 * Tipo Stampa=Bozza (valore 0) e Tipo Stampa=Definitiva (valore 1)
			 * 
			 * In entrambi i casi il valore di L2 è dato dalla sommatoria di H2 e I2: L2=H2+I2
			 */
			riga.setTotaleProgressivo(riga.getProgressivoImponibile().add(riga.getProgressivoIva()));
			log.debug(methodName, codiceAliquota + " - totaleTotaleProgressivo - old " + sumTotaleProgressivo
					+ " - added " + riga.getTotaleProgressivo());
			// Somma colonne L2
			sumTotaleProgressivo = sumTotaleProgressivo.add(riga.getTotaleProgressivo());
			log.debug(methodName, codiceAliquota + " - totaleTotaleProgressivo - new " + sumTotaleProgressivo);
			
			//SIAC-3701 E2,F2,G2 = 0,sia bozza che definitiva per i periodi precedenti 
			//testo  DELLA jira
			//In fase di stampa del riepilogo aliquote correttamente riporta anche la situazione delle aliquote movimentate nel mese precedente ma per le nuove righe è necessario osservare le regole già presenti per le colonne esposte: 
			//- colonne Imponibile, IVA,Totale riportano sempre la situazione del mese corrente (sia per la stampa BOZZA e sia per la stampa DEFINTIVA): le righe che visualizzano le aliquote movimentate solo nei mesi precedenti riporteranno l'importo a 0; 
			//- colonne Progr. Imponibile, Prog. IVA, Totale Progressivo riportano sempre il dato del mese corrente se stampa in BOZZA mentre riportano sempre il dato progressivo se la stampa è DEFINITIVA quindi inserendo le righe nuove: se la stampa è in BOZZA gli importi sono sempre a 0, se la stampa è DEFINITIVA gli importi riportano sempre il dato progressivo dei mesi precedenti.
			if(!getPeriodo().equals(p)){
				
//
//				//totali 
//				//E' IMPORTANTE NON INVERTIRE L'ORDINE I TOTALI DEVONO ESSERE PRIMA DEI SINGOLI CAMPI
//				//PRIMA RIGA
//				sumImponibile = BigDecimal.ZERO;
//				sumIVA = BigDecimal.ZERO;
//				sumTotale = BigDecimal.ZERO;
//				sumProgressivoImponibile = isTipoStampaBozza() ? BigDecimal.ZERO : sumProgressivoImponibile;
//				sumProgressivoIVA = isTipoStampaBozza() ? BigDecimal.ZERO : sumProgressivoIVA;
//				sumTotaleProgressivo = isTipoStampaBozza() ? BigDecimal.ZERO : sumTotaleProgressivo;
				//E2
				riga.setImponibile(BigDecimal.ZERO);

				//F2
				riga.setIva(BigDecimal.ZERO);
				//G2
				riga.setTotale(BigDecimal.ZERO);
//				
//				//H2 MOSTRO IL VALORE  SOLO SE TIPO STAMPA DEFINITIVA 
//				riga.setProgressivoImponibile(isTipoStampaBozza() ? BigDecimal.ZERO : riga.getImponibile().add(riga.getProgressivoImponibile()));
//				
//				//I2 MOSTRO IL VALORE  SOLO SE TIPO STAMPA DEFINITIVA
//				riga.setProgressivoIva(isTipoStampaBozza() ? BigDecimal.ZERO : riga.getProgressivoIva());
//				
//				//L2 MOSTRO IL VALORE  SOLO SE TIPO STAMPA DEFINITIVA
//				riga.setTotaleProgressivo(isTipoStampaBozza()? BigDecimal.ZERO : riga.getProgressivoImponibile().add(riga.getProgressivoIva()));
			}
//			
			listaRighe.add(riga);
		}
		// Popolo la sezione

		//RIGA TOTALI
		sezione.setTotaleImponibile(sumImponibile);
		sezione.setTotaleIVA(sumIVA);
		sezione.setTotaleTotale(sumTotale);
		sezione.setTotaleProgressivoImponibile(sumProgressivoImponibile);
		sezione.setTotaleProgressivoIva(sumProgressivoIVA);
		sezione.setTotaleTotaleProgressivo(sumTotaleProgressivo);

		sezione.setListaRiepiloghiIva(listaRighe);
			
		return sezione;

	}

	/**
	 * Popola i valori della riga del riepilogo che dipendono dai subdocumenti di entrata
	 * @param periodoPrecedente 
	 * 
	 * @return Map<String,StampaRegistroIvaRiepilogoIva> la mappa contenente un oggetto 
	 * per ogni aliquota usata nel registro
	 *  
	 * */	
	private Map<String, StampaRegistroIvaRiepilogoIva> popolaRigheRiepilogo(List<SubdocumentoIvaEntrata> listaSubdocumentoIvaEntrataPeriodo) {
		Map<String, StampaRegistroIvaRiepilogoIva> map = new HashMap<String, StampaRegistroIvaRiepilogoIva>();
		
		for(SubdocumentoIvaEntrata sie : listaSubdocumentoIvaEntrataPeriodo) {
//			DocumentoEntrabta d = sie.getDocumentoCollegatoCastato();
			for(AliquotaSubdocumentoIva asi : sie.getListaAliquotaSubdocumentoIva()) {
				AliquotaIva ali = asi.getAliquotaIva();
				// Ottengo la riga dalla mappa
				StampaRegistroIvaRiepilogoIva riga = map.get(ali.getCodice());
				
				
				if(riga == null) {
					riga = new StampaRegistroIvaRiepilogoIva();
					map.put(ali.getCodice(), riga);
					// Popolo l'intestazione un'unica volta
					
					// A2 - codice - Aliquota Iva
					// B2 - percAliquota - Aliquota Iva
					// C2 - descrizione - Aliquota Iva
					// DA NON INSERIRE!! D2 - percIndetraibilità - Aliquota Iva
					riga.setAliquotaSubdocumentoIva(asi);
					
					// Inizializzo i totali
					// E2 - Imponibile
					riga.setImponibile(BigDecimal.ZERO);
					// F2 - IVA
					riga.setIva(BigDecimal.ZERO);
					// G2 - Totale
					riga.setTotale(BigDecimal.ZERO);
					// H2 - Progressivo Imponibile
					riga.setProgressivoImponibile(BigDecimal.ZERO);
					// I2 - Progr. IVA
					riga.setProgressivoIva(BigDecimal.ZERO);
					// L2 - Totale Progressivo
					riga.setTotaleProgressivo(BigDecimal.ZERO);
				}

				// E2 - Imponibile: è dato dalla sommatoria dei valori degli imponibili (V. I1) per il corrispondente codice aliquota.
				// Il valore così ricavato di  E2 viene memorizzato nell’entità “Progressivi Iva”
//				BigDecimal imponibileConSegno = d.getTipoDocumento().isNotaCredito() ? asi.getImponibile().negate() : asi.getImponibile();
//				riga.setImponibile(riga.getImponibile().add(imponibileConSegno));
				//SIAC-3791
				BigDecimal imponibile = asi.getImponibile().setScale(2, RoundingMode.HALF_DOWN);
				riga.setImponibile(riga.getImponibile().add(imponibile));
				
				// F2 - IVA: è dato dalla sommatoria dei valori dell’imposta (V. L1) per il corrispondente codice aliquota.
				// Il valore così ricavato di  F2 viene memorizzato nell’entità “Progressivi Iva”
//				BigDecimal impostaConSegno = d.getTipoDocumento().isNotaCredito() ? asi.getImposta().negate() : asi.getImposta();
//				riga.setIva(riga.getIva().add(impostaConSegno));
				//SIAC-3791
				BigDecimal iva = asi.getImposta().setScale(2, RoundingMode.HALF_DOWN);
				riga.setIva(riga.getIva().add(iva));
				
				// G2 - Fuori dal loop
				
				// L2 - Fuori dal loop
				
			}
		}
		return map;
	}
	
	/**
	 * Ottiene il totale imponibile a partire dal progressivo iva.
	 * 
	 * @param progressiviIva il progressivo da cui estrarre il dato
	 * 
	 * @return il totale da considerare
	 */
	protected abstract BigDecimal obtainTotaleImponibile(ProgressiviIva progressiviIva);
	
	/**
	 * Ottiene il totale iva a partire dal progressivo iva.
	 * 
	 * @param progressiviIva il progressivo da cui estrarre il dato
	 * 
	 * @return il totale da considerare
	 */
	protected abstract BigDecimal obtainTotaleIva(ProgressiviIva progressiviIva);
	
	/**
	 * Popola il totale dell'imponibile nel nuovo progressivo-
	 * 
	 * @param progressiviIva il progressivo da popolare
	 * @param riga           i valori tramite cui popolare il progressivo
	 */
//	protected abstract void popolaTotaleImponibile(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga);
	protected void popolaTotaleImponibile(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga) {
		final String methodName = "popolaTotaleImponibile";
		if(progressiviIva.getTotaleImponibileDefinitivo() != null && progressiviIva.getTotaleImponibileDefinitivo().compareTo(BigDecimal.ZERO) != 0){
			log.debug(methodName, "importo già impostato per aliquota" + riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleImponibileDefinitivo " + progressiviIva.getTotaleImponibileDefinitivo());
			return;
		}
		progressiviIva.setTotaleImponibileDefinitivo(riga.getProgressivoImponibile());
		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + "periodo: " + progressiviIva.getPeriodo().getCodice() + " - TotaleImponibileDefinitivo " + progressiviIva.getTotaleImponibileDefinitivo());
	}

	/**
	 * Popola il totale dell'iva nel nuovo progressivo-
	 * 
	 * @param progressiviIva il progressivo da popolare
	 * @param riga           i valori tramite cui popolare il progressivo
	 */
//	protected abstract void popolaTotaleIva(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga);
	protected void popolaTotaleIva(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga) {
		final String methodName = "popolaTotaleIva";
		if(progressiviIva.getTotaleIvaDefinitivo() != null && progressiviIva.getTotaleIvaDefinitivo().compareTo(BigDecimal.ZERO) != 0){
			log.debug(methodName, "importo già impostato per aliquota" + riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleIvaDefinitivo " + progressiviIva.getTotaleIvaDefinitivo());
			return;
		}
		progressiviIva.setTotaleIvaDefinitivo(riga.getProgressivoIva());
		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleIvaDefinitivo " + progressiviIva.getTotaleIvaDefinitivo());
	}
	
	@Override
	protected List<SubdocumentoIvaEntrata> getListaSubdocumentiIva() {
		return listaSubdocumentoIvaEntrata;
	}
}