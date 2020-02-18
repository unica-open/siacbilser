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
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistoIvaDatoIvaAcquisti;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaDatiIva;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaRiepilogo;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaRiepilogoIva;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaSpesaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOperazioneIva;

/**
 * 
 * Classe di gestione base del report della stampa registro iva acquisti (comprende corrispettivi, acquisti iva immediata, acquisti iva differita pagati,
 * acquisti iva differita non pagati)
 * @author Pro Logic
 *
 */
public abstract class StampaRegistroIvaAcquistiReportHandler extends StampaRegistroIvaReportHandler<SubdocumentoIvaSpesa> {
	
	@Autowired
	protected DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	protected SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	protected SubdocumentoIvaSpesaDad subdocumentoIvaSpesaDad;
	
	protected List<SubdocumentoIvaSpesa> listaSubdocumentoIvaSpesa;
	
	/**
	 * Popola i campi comuni a livello dell'istanza.
	 */
	@Override
	protected void popolaDatiAggiuntiviAPartireDaiDatiDiInput() {
		final String methodName = "popolaCampiComuni";
		// Ottengo la lista dei subdocumenti iva
		ottieniListaSubdocumentoIvaSpesa();
		log.debug(methodName, "Subdocumenti iva trovati: " + listaSubdocumentoIvaSpesa.size());
	}

	/**
	 * Ottiene la lista dei SubdocumentiIvaSpesa iterando n volte la ricerca sintetica.
	 * 
	 * @return la lista dei subdocumenti di spesa
	 */
	protected abstract void ottieniListaSubdocumentoIvaSpesa();
	
	/**
	 * Ottiene la lista dei SubdocumentiIvaSpesa iterando n volte la ricerca sintetica.
	 * 
	 * @return la lista dei subdocumenti di spesa
	 */
	protected abstract List<SubdocumentoIvaSpesa> ottieniListaSubdocumentoIvaSpesaNelPeriodo(Periodo p);
	
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
		List<DatiIva> listaRigheSezione1 = new ArrayList<DatiIva>();
		
		// Inizializzo i campi per i totali
		// Somma colonne I1
		BigDecimal sumImponibile = BigDecimal.ZERO;
		// Somma colonne L1
		BigDecimal sumIVA = BigDecimal.ZERO;
		// Somma colonne O1
		BigDecimal sumTotale = BigDecimal.ZERO;
		
		for(SubdocumentoIvaSpesa sis : listaSubdocumentoIvaSpesa){
			log.debug(methodName, "SubdocumentoIvaSpesa.uid: " + sis.getUid());
			
			Documento<?,?> d = sis.getDocumentoCollegato();
			
			if(d == null || d.getUid() == 0) {
				throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nessun documento collegato al subdocumento iva [uid:" + sis.getUid() + "] trovato."));
			}
			
			if(!(d instanceof DocumentoSpesa)){
				throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("documento collegato [uid: "+d.getUid()+"] al subdocumento iva [uid:" + sis.getUid() + "] non e' di tipo spesa. Deve essere di tipo entrata. Documento trovato: "+ d.getDescAnnoNumeroUidTipoDocUidSoggettoStato()));
			}
			
			DocumentoSpesa ds = (DocumentoSpesa) d;
			
			ds = documentoSpesaDad.findDocumentoSpesaById(ds.getUid());
			log.debug(methodName, "SubdocumentoIvaSpesa.documentoCollegato.uid: " + ds.getUid());
			SubdocumentoSpesa ss = sis.getSubdocumento();
			log.debug(methodName, "SubdocumentoIvaSpesa.subdocumentoSpesa == null? " + (ss == null ? "true" : ("false. uid: " + ss.getUid())));
			AttivitaIva ati = sis.getAttivitaIva();
			log.debug(methodName, "SubdocumentoIvaSpesa.attivitaIva == null? " + (ati == null ? "true" : ("false. uid: " + ati.getUid())));
			// Ottengo i dati del documento
			for(AliquotaSubdocumentoIva asi: sis.getListaAliquotaSubdocumentoIva()){
				final int uidAliquota = asi.getUid();
				log.debug(methodName, "AliquotaSubdocumentoIva.uid: " + uidAliquota);
				//nuova riga
				StampaRegistoIvaDatoIvaAcquisti riga = new StampaRegistoIvaDatoIvaAcquisti();
				
				//popolo la riga
				
				// A1 - numProtocolloDef - Subdocumento Iva Spesa
				// B1 - dataProtocolloDef - Subdocumento Iva Spesa
				// P1 - flagRilevanteIRAP - Subdocumento Iva Spesa - Se = 0 nel layout si visualizza “No”, se =1 si visualizza “Si”
				// Q1 - numOrdinativoDoc - Subdocumento Iva Spesa
				riga.setSubdocumentoIva(sis);
				log.debug(methodName, uidAliquota + " - A1 - numProtocolloDef - " + sis.getNumeroProtocolloDefinitivo());
				log.debug(methodName, uidAliquota + " - B1 - dataProtocolloDef - " + sis.getDataProtocolloDefinitivo());
				log.debug(methodName, uidAliquota + " - P1 - flagRilevanteIRAP - " + sis.getFlagRilevanteIRAP());
				log.debug(methodName, uidAliquota + " - Q1 - numOrdinativoDoc - " + sis.getNumeroOrdinativoDocumento());
				
				// C1 - dataEmissione - DocumentoEntrata
				// D1 - numero - DocumentoEntrata
				// E1 - codice - TipoDocumento - Codice + descrizione separati dal carattere ‘-‘
				// F1 - codice - Soggetto
				// G1 - denominazione - Soggetto
				riga.setDocumento(ds);
				log.debug(methodName, uidAliquota + " - C1 - dataEmissione - " + ds.getDataEmissione());
				log.debug(methodName, uidAliquota + " - D1 - numero - " + ds.getNumero());
				log.debug(methodName, uidAliquota + " - E1 - tipoDocumento - " + (ds.getTipoDocumento() != null ? ds.getTipoDocumento().getCodice() : "null"));
				log.debug(methodName, uidAliquota + " - F1 - soggetto - codice - " + (ds.getSoggetto() != null ? ds.getSoggetto().getCodiceSoggetto() : "null"));
				log.debug(methodName, uidAliquota + " - G1 - soggetto - denominazione -" + (ds.getSoggetto() != null ? ds.getSoggetto().getDenominazione() : "null"));
				
				// R1 - numero - Subdocumento - Opzionale, può anche essere nullo. E’ riferito al Subdocumento finanzario
				riga.setSubdocumento(ss);
				log.debug(methodName, uidAliquota + " - R1 - numeroSubdocumento - " + (ss != null ? ss.getNumero() : "null"));
				
				// H1 - codice - Attività Iva - Solo se il codice è significativo (stringa), altrimenti si prende il campo “descrizione”
				riga.setAttivitaIva(ati);
				log.debug(methodName, uidAliquota + " - H1 - attivitaIva - codice - " + (ati != null ? ati.getCodice() : "null"));
				
				//JIRA 3246, poi SIAC-3791
//				BigDecimal importoTotaleAsi = d.getTipoDocumento().isNotaCredito()? asi.getTotale().negate() : asi.getTotale();
//				BigDecimal imponibileAsi =  d.getTipoDocumento().isNotaCredito() ? asi.getImponibile().negate() : asi.getImponibile();
//				BigDecimal impostaAsi =  d.getTipoDocumento().isNotaCredito() ?asi.getImposta().negate() : asi.getImposta() ;
				
				//dovrebbero già essere arrotondati su db, ma alcuni dati vecchi potrebbero avere problemi
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
				//JIRA 3246
				riga.setTotale(importoTotaleAsi);
				sumTotale = sumTotale.add(importoTotaleAsi);
				log.debug(methodName, uidAliquota + " - O1 - totale - " + asi.getTotale());
				
				//aggiungo la riga alla lista
				listaRigheSezione1.add(riga);
			}
		}
		sezione1.setListaDatiIva(listaRigheSezione1);
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
		
		Map<String, StampaRegistroIvaRiepilogoIva> mappaAliquotePeriodoInStampa = accorpaRigheAliquota(listaSubdocumentoIvaSpesa);
		
		log.debug(methodName, "Accorpamento righe terminato per il periodo oggetto di stampa. Calcolo dei totali");
		
		//e' il riepilo del periodo della ricerca (quello scelto dalla mschera di ricerca )
		StampaRegistroIvaRiepilogo riepilogoSezioneRicercata = new StampaRegistroIvaRiepilogo();
		
		//il periodo e' quello della ricerca 
		riepilogoSezioneRicercata.setPeriodo(getPeriodo());
		riepilogoSezioneRicercata = calcolaEPopolaTotaliSezione2(riepilogoSezioneRicercata, mappaAliquotePeriodoInStampa, getPeriodo());
		sezione2.add(riepilogoSezioneRicercata);
		
		//CR-3545 : mostro anche i riepiloghi precedenti
		Periodo p = getPeriodo().precedente();
		
		List<StampaRegistroIvaRiepilogo> listaRiepiloghiIvaPeriodiPrecedenti = new ArrayList<StampaRegistroIvaRiepilogo>();
				
		//CR 3545: mostro anche i riepiloghi precedenti
		while(p!=null){
			StampaRegistroIvaRiepilogo stampaRiepilogoIvaMesiPrecedenti = new StampaRegistroIvaRiepilogo();
			List<SubdocumentoIvaSpesa> listaSubdocumentiIvaSpesaPeriodo = ottieniListaSubdocumentoIvaSpesaNelPeriodo(p);
				//popolo A2, B2, C2, E2
				Map<String, StampaRegistroIvaRiepilogoIva> mapPeriodiPrecedenti = accorpaRigheAliquota(listaSubdocumentiIvaSpesaPeriodo);
				log.debug(methodName, "Accorpamento righe terminato per il periodo "+ p.getDescrizione() + ". Calcolo dei totali");
				// popolo G2, H2,I2,L2 e totali
				StampaRegistroIvaRiepilogo s = calcolaEPopolaTotaliSezione2(stampaRiepilogoIvaMesiPrecedenti, mapPeriodiPrecedenti, p);
				s.setPeriodo(p);
				listaRiepiloghiIvaPeriodiPrecedenti.add(s);
			p = p.precedente();

		}
		sezione2.addAll(listaRiepiloghiIvaPeriodiPrecedenti);
	}

	private StampaRegistroIvaRiepilogo calcolaEPopolaTotaliSezione2(StampaRegistroIvaRiepilogo sezione,	Map<String, StampaRegistroIvaRiepilogoIva> map, Periodo periodo) {

		final String methodName = "calcolaEPopolaTotaliSezione2";
		// Totali Esenti - Inizializzati prima
		BigDecimal totaleImponibileEsente = BigDecimal.ZERO;
		BigDecimal totaleProgressivoImponibileEsente = BigDecimal.ZERO;

		// Inizializzo i campi per i totali
		// Somma colonne E2
		BigDecimal totaleImponibile = BigDecimal.ZERO;
		// Somma colonne F2
		BigDecimal totaleIVA = BigDecimal.ZERO;
		// Somma colonne G2
		BigDecimal totaleTotale = BigDecimal.ZERO;
		// Somma colonne H2
		BigDecimal totaleProgressivoImponibile = BigDecimal.ZERO;
		// Somma colonne I2
		BigDecimal totaleProgressivoIva = BigDecimal.ZERO;
		// Somma colonne L2
		BigDecimal totaleTotaleProgressivo = BigDecimal.ZERO;

		// Totali indetraibili
		BigDecimal totaleImponibileIndetraibile = BigDecimal.ZERO;
		BigDecimal totaleIVAIndetraibile = BigDecimal.ZERO;
		BigDecimal totaleProgressivoImponibileIndetraibile = BigDecimal.ZERO;
		BigDecimal totaleProgressivoIvaIndetraibile = BigDecimal.ZERO;

		// Totali detraibili
		BigDecimal totaleImponibileDetraibile = BigDecimal.ZERO;
		BigDecimal totaleIVADetraibile = BigDecimal.ZERO;
		BigDecimal totaleProgressivoImponibileDetraibile = BigDecimal.ZERO;
		BigDecimal totaleProgressivoIvaDetraibile = BigDecimal.ZERO;

		List<DatiIva> listaRighe = new ArrayList<DatiIva>();

		for (StampaRegistroIvaRiepilogoIva riga : map.values()) {
			// Somma colonne E2
			final String codiceAliquota = riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice();
			log.debug(methodName, codiceAliquota + " - totaleImponibile - old " + totaleImponibile + " - added "
					+ riga.getImponibile());
			totaleImponibile = totaleImponibile.add(riga.getImponibile());
			log.debug(methodName, codiceAliquota + " - totaleImponibile - new " + totaleImponibile);
			// Somma colonne F2
			log.debug(methodName, codiceAliquota + " - totaleIVA - old " + totaleIVA + " - added " + riga.getIva());
			totaleIVA = totaleIVA.add(riga.getIva());
			log.debug(methodName, codiceAliquota + " - totaleIVA - new " + totaleIVA);

			// G2 - Totale: è dato dalla sommatoria dei valori di E2 e F2 -
			// G2=E2+F2
			riga.setTotale(riga.getImponibile().add(riga.getIva()));
			// Somma colonne G2
			log.debug(methodName, codiceAliquota + " - totale - old " + totaleTotale + " - added " + riga.getTotale());
			totaleTotale = totaleTotale.add(riga.getTotale());
			log.debug(methodName, codiceAliquota + " - totaleTotale - new " + totaleTotale);

			ProgressiviIva piPrecedente = ottieniProgressiviIvaPrecedente(
					riga.getAliquotaSubdocumentoIva().getAliquotaIva(), periodo);

			if (!tipoStampaBozza) {
				BigDecimal totaleImponibileProgressivoIva = obtainTotaleImponibile(piPrecedente).setScale(2, RoundingMode.HALF_DOWN);
				BigDecimal totaleIvaProgressivoIva = obtainTotaleIva(piPrecedente).setScale(2, RoundingMode.HALF_DOWN);
				// H2 - calcolo i totali del progressivo
				riga.setProgressivoImponibile(totaleImponibileProgressivoIva);
				log.debug(methodName,
						codiceAliquota + " - progressivoImponibile - new " + riga.getProgressivoImponibile());
				// I2 - calcolo i totali del progressivo
				riga.setProgressivoIva(totaleIvaProgressivoIva);
				log.debug(methodName, codiceAliquota + " - progressivoIva - new " + riga.getProgressivoIva());
			}

			// H2 - Progr. Imponibile
			/*
			 * CASO 1: Tipo Stampa=Bozza (valore 0) Il valore del campo coincide
			 * con quello dell’Imponibile (H2=E2)
			 * 
			 * CASO 2: Tipo Stampa=Definitiva (valore 1) Il valore del campo
			 * viene calcolato in questo modo: Valore dell’imponibile per il
			 * periodo selezionato per il corrispondente codice aliquota (ovvero
			 * E2) + sommatoria dei valori dell’imponibile per il corrispondente
			 * codice aliquota dei periodi precedenti (ovviamente sempre per lo
			 * stesso Ente, Anno Esercizio e Registro Iva). Il valore da sommare
			 * a E2 viene ricavato accedendo all’entità “Progressivi Iva” e
			 * prendendo il valore del campo “totImponibileDef”.
			 */
			riga.setProgressivoImponibile(riga.getImponibile().add(riga.getProgressivoImponibile()));
			// Somma colonne H2
			log.debug(methodName, codiceAliquota + " - totaleProgressivoImponibile - old " + totaleProgressivoImponibile
					+ " - added " + riga.getProgressivoImponibile());
			totaleProgressivoImponibile = totaleProgressivoImponibile.add(riga.getProgressivoImponibile());
			log.debug(methodName,
					codiceAliquota + " - totaleProgressivoImponibile - new " + totaleProgressivoImponibile);

			// Totali Esenti
			// Il valore è dato dalla sommatoria dei soli valori della colonna
			// corrispondenti ai codici aliquota delle tipologie: N.I. - ES -
			// F.C.I.
			// (entità enum “Tipo Operazione Iva” associata ad “Aliquota Iva”)
			boolean isEsente = TipoOperazioneIva.NON_IMPONIBILE.equals(riga.getAliquotaSubdocumentoIva().getAliquotaIva().getTipoOperazioneIva())
					|| TipoOperazioneIva.ESENTE.equals(riga.getAliquotaSubdocumentoIva().getAliquotaIva().getTipoOperazioneIva())
					|| TipoOperazioneIva.ESCLUSO_FCI.equals(riga.getAliquotaSubdocumentoIva().getAliquotaIva().getTipoOperazioneIva());
			log.debug(methodName,codiceAliquota + " - TipoOperazioneIVa: "	+ riga.getAliquotaSubdocumentoIva().getAliquotaIva().getTipoOperazioneIva()+ " - isEsente? " + isEsente);
			if (isEsente) {
				// E2 - Imponibile
				log.debug(methodName, codiceAliquota + " - E2 - Imponibile esente - old " + totaleImponibileEsente
						+ " - added " + riga.getImponibile());
				totaleImponibileEsente = totaleImponibileEsente.add(riga.getImponibile());
				log.debug(methodName, codiceAliquota + " - E2 - Imponibile esente - new " + totaleImponibileEsente);
				// H2 - Progressivo Imponibile
				log.debug(methodName, codiceAliquota + " - H2 - Progressivo Imponibile esente - old "
						+ totaleProgressivoImponibileEsente + " - added " + riga.getProgressivoImponibile());
				totaleProgressivoImponibileEsente = totaleProgressivoImponibileEsente
						.add(riga.getProgressivoImponibile());
				log.debug(methodName, codiceAliquota + " - H2 - Progressivo Imponibile esente - new "
						+ totaleProgressivoImponibileEsente);
			}
			// I2 - Progr. IVA
			/*
			 * CASO 1: Tipo Stampa=Bozza (valore 0) Il valore del campo coincide
			 * con quello dell’IVA (I2=F2)
			 * 
			 * CASO 2: Tipo Stampa=Definitiva (valore 1) Il valore del campo
			 * viene calcolato in questo modo: Valore dell’IVA per il periodo
			 * selezionato per il corrispondente codice aliquota (ovvero F2) +
			 * sommatoria dei valori dell’IVA per il corrispondente codice
			 * aliquota dei periodi precedenti (ovviamente sempre per lo stesso
			 * Ente, Anno Esercizio e Registro Iva). Il valore da sommare a F2
			 * viene ricavato accedendo all’entità “Progressivi Iva” e prendendo
			 * il valore del campo “totIvaDef”.
			 */
			riga.setProgressivoIva(riga.getIva().add(riga.getProgressivoIva()));
			// Somma colonne H2
			log.debug(methodName, codiceAliquota + " - totaleProgressivoIva - old " + totaleProgressivoIva + " - added "
					+ riga.getProgressivoIva());
			totaleProgressivoIva = totaleProgressivoIva.add(riga.getProgressivoIva());
			log.debug(methodName, codiceAliquota + " - totaleProgressivoIva - new " + totaleProgressivoIva);

			// Imposto i parziali nel progressivo attuale
			ProgressiviIva piAttuale = ottieniProgressiviIvaAttuale(riga.getAliquotaSubdocumentoIva().getAliquotaIva());
			popolaTotaleImponibile(piAttuale, riga);
			popolaTotaleIva(piAttuale, riga);

			// L2 - Totale Progressivo
			/*
			 * Tipo Stampa=Bozza (valore 0) e Tipo Stampa=Definitiva (valore 1)
			 * 
			 * In entrambi i casi il valore di L2 è dato dalla sommatoria di H2
			 * e I2: L2=H2+I2
			 */
			riga.setTotaleProgressivo(riga.getProgressivoImponibile().add(riga.getProgressivoIva()));
			// Somma colonne L2
			log.debug(methodName, codiceAliquota + " - totaleTotaleProgressivo - old " + totaleTotaleProgressivo
					+ " - added " + riga.getTotaleProgressivo());
			totaleTotaleProgressivo = totaleTotaleProgressivo.add(riga.getTotaleProgressivo());
			log.debug(methodName, codiceAliquota + " - totaleTotaleProgressivo - new " + totaleTotaleProgressivo);

			// Totali indetraibili
			// Il valore è dato dalla sommatoria dei valori della colonna,
			// ognuno moltiplicato per il valore corrispondente
			// della % di indetraibilità riferito al proprio codice aliquota.

			// E2 - Imponibile
			BigDecimal nowTotaleImponibileIndetraibile = riga.getImponibile()
					.multiply(riga.ottieniPercentualeIndetraibilita()).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName, codiceAliquota + " - totaleImponibileIndetraibile - old "
					+ totaleImponibileIndetraibile + " - added " + nowTotaleImponibileIndetraibile);
			totaleImponibileIndetraibile = totaleImponibileIndetraibile.add(nowTotaleImponibileIndetraibile);
			log.debug(methodName,
					codiceAliquota + " - totaleImponibileIndetraibile - new " + totaleImponibileIndetraibile);
			// F2 - Iva
			log.debug(methodName, codiceAliquota + " - totaleIVAIndetraibile - old " + totaleIVAIndetraibile);
			totaleIVAIndetraibile = totaleIVAIndetraibile.add(riga.getIva()
					.multiply(riga.ottieniPercentualeIndetraibilita()).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED)).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName, codiceAliquota + " - totaleIVAIndetraibile - new " + totaleIVAIndetraibile);
			// H2 - Progressivo Imponibile
			log.debug(methodName, codiceAliquota + " - totaleProgressivoImponibileIndetraibile - old "
					+ totaleProgressivoImponibileIndetraibile);
			totaleProgressivoImponibileIndetraibile = totaleProgressivoImponibileIndetraibile
					.add(riga.getProgressivoImponibile().multiply(riga.ottieniPercentualeIndetraibilita())
							.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED)).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName, codiceAliquota + " - totaleProgressivoImponibileIndetraibile - new "
					+ totaleProgressivoImponibileIndetraibile);
			// L2 - Progressivo Iva
			log.debug(methodName,
					codiceAliquota + " - totaleProgressivoIvaIndetraibile - old " + totaleProgressivoIvaIndetraibile);
			totaleProgressivoIvaIndetraibile = totaleProgressivoIvaIndetraibile.add(riga.getProgressivoIva()
					.multiply(riga.ottieniPercentualeIndetraibilita()).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED)).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName,
					codiceAliquota + " - totaleProgressivoIvaIndetraibile - new " + totaleProgressivoIvaIndetraibile);

			// Totali detraibili
			// Il valore è dato dalla sommatoria dei valori della colonna,
			// ognuno moltiplicato per
			// (100 - il valore corrispondente della % di indetraibilità
			// riferito al proprio codice aliquota).

			// E2 - Imponibile
			BigDecimal nowTotaleImponibileDetraibile = riga.getImponibile()
					.multiply(BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(riga.ottieniPercentualeIndetraibilita()))
					.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName, codiceAliquota + " - totaleImponibileDetraibile - old " + totaleImponibileDetraibile
					+ " - added " + nowTotaleImponibileDetraibile);
			totaleImponibileDetraibile = totaleImponibileDetraibile.add(nowTotaleImponibileDetraibile);
			log.debug(methodName, codiceAliquota + " - totaleImponibileDetraibile - new " + totaleImponibileDetraibile);
			// F2 - Iva
			BigDecimal nowTotaleIVADetraibile = riga.getIva()
					.multiply(BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(riga.ottieniPercentualeIndetraibilita()))
					.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName, codiceAliquota + " - totaleIVADetraibile - old " + totaleIVADetraibile + " - added "
					+ nowTotaleIVADetraibile);
			totaleIVADetraibile = totaleIVADetraibile.add(nowTotaleIVADetraibile);
			log.debug(methodName, codiceAliquota + " - totaleIVADetraibile - new " + totaleIVADetraibile);
			// H2 - Progressivo Imponibile
			BigDecimal nowTotaleProgressivoImponibileDetraibile = riga.getProgressivoImponibile()
					.multiply(BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(riga.ottieniPercentualeIndetraibilita()))
					.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName, codiceAliquota + " - totaleProgressivoImponibileDetraibile - old "
					+ totaleProgressivoImponibileDetraibile + " - added " + nowTotaleProgressivoImponibileDetraibile);
			totaleProgressivoImponibileDetraibile = totaleProgressivoImponibileDetraibile
					.add(nowTotaleProgressivoImponibileDetraibile);
			log.debug(methodName, codiceAliquota + " - totaleProgressivoImponibileDetraibile - new "
					+ totaleProgressivoImponibileDetraibile);
			// L2 - Progressivo Iva
			BigDecimal nowTotaleProgressivoIvaDetraibile = riga.getProgressivoIva()
					.multiply(BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(riga.ottieniPercentualeIndetraibilita()))
					.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).setScale(2, RoundingMode.HALF_DOWN);
			log.debug(methodName, codiceAliquota + " - totaleProgressivoIvaDetraibile - old "
					+ totaleProgressivoIvaDetraibile + " - added " + nowTotaleProgressivoIvaDetraibile);
			totaleProgressivoIvaDetraibile = totaleProgressivoIvaDetraibile.add(nowTotaleProgressivoIvaDetraibile);
			log.debug(methodName,
					codiceAliquota + " - totaleProgressivoIvaDetraibile - new " + totaleProgressivoIvaDetraibile);
			
			//SIAC-3701 E2,F2,G2 = 0,sia bozza che definitiva per i periodi precedenti 
			//testo  DELLA jira
			//In fase di stampa del riepilogo aliquote correttamente riporta anche la situazione delle aliquote movimentate nel mese precedente ma per le nuove righe è necessario osservare le regole già presenti per le colonne esposte: 
			//- colonne Imponibile, IVA,Totale riportano sempre la situazione del mese corrente (sia per la stampa BOZZA e sia per la stampa DEFINTIVA): le righe che visualizzano le aliquote movimentate solo nei mesi precedenti riporteranno l'importo a 0; 
			//- colonne Progr. Imponibile, Prog. IVA, Totale Progressivo riportano sempre il dato del mese corrente se stampa in BOZZA mentre riportano sempre il dato progressivo se la stampa è DEFINITIVA quindi inserendo le righe nuove: se la stampa è in BOZZA gli importi sono sempre a 0, se la stampa è DEFINITIVA gli importi riportano sempre il dato progressivo dei mesi precedenti.
			if(!getPeriodo().equals(periodo)){
//				//totali 
//				//MESSI IN QUESTA POSIZIONE PER EVITARE ERRORI DI REGRESSIONE 
//				//E' IMPORTANTE NON INVERTIRE L'ORDINE I TOTALI DEVONO ESSERE PRIMA DEI SINGOLI CAMPI
//				//PRIMA RIGA
//				totaleImponibileIndetraibile = BigDecimal.ZERO;//sempre a zero sia bozza che def
//				totaleIVAIndetraibile=BigDecimal.ZERO;//sempre a zero sia bozza che def
//				totaleProgressivoImponibileIndetraibile = isTipoStampaBozza() ? BigDecimal.ZERO : totaleProgressivoImponibileIndetraibile;
//				totaleProgressivoIvaIndetraibile = isTipoStampaBozza() ? BigDecimal.ZERO : totaleProgressivoIvaIndetraibile;
//				
//				//SECONDA RIGA
//				totaleImponibileDetraibile =BigDecimal.ZERO;
//				totaleIVADetraibile = BigDecimal.ZERO;
//				totaleProgressivoImponibileDetraibile = isTipoStampaBozza() ? BigDecimal.ZERO : totaleProgressivoImponibileDetraibile;
//				totaleProgressivoIvaDetraibile = isTipoStampaBozza() ? BigDecimal.ZERO : totaleProgressivoIvaDetraibile;
//
//				//TERZA RIGA
//				totaleImponibileEsente =BigDecimal.ZERO;
//				totaleProgressivoImponibileEsente = isTipoStampaBozza() ? BigDecimal.ZERO : totaleProgressivoImponibileEsente;
//
//				//RIGA #4
//				totaleImponibile = BigDecimal.ZERO;
//				totaleIVA = BigDecimal.ZERO;
//				totaleTotale = BigDecimal.ZERO;
//				totaleProgressivoImponibile = isTipoStampaBozza() ? BigDecimal.ZERO : totaleProgressivoImponibile;
//				totaleProgressivoIva = isTipoStampaBozza() ? BigDecimal.ZERO : totaleProgressivoIva;
//				totaleTotaleProgressivo = isTipoStampaBozza() ? BigDecimal.ZERO : totaleTotaleProgressivo;
//
//				
//				//E2
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
//				//L2 MOSTRO IL VALORE  SOLO SE TIPO STAMPA DEFINITIVA
//				riga.setTotaleProgressivo(isTipoStampaBozza()? BigDecimal.ZERO : riga.getProgressivoImponibile().add(riga.getProgressivoIva()));
//				
//				
			} 

			listaRighe.add(riga);
		}
		sezione.setListaRiepiloghiIva(listaRighe);
		//PRIMA RIGA dei totali nel footer della tabella riepilogo  TOTALI INDETRAIBILI
		sezione.setTotaleImponibileIndetraibile(totaleImponibileIndetraibile);
		sezione.setTotaleIVAIndetraibile(totaleIVAIndetraibile);
		sezione.setTotaleProgressivoImponibileIndetraibile(totaleProgressivoImponibileIndetraibile);//progressivo
		sezione.setTotaleProgressivoIvaIndetraibile(totaleProgressivoIvaIndetraibile);//progressivo

		//SECONDA riga detraibili 
		sezione.setTotaleImponibileDetraibile(totaleImponibileDetraibile);
		sezione.setTotaleIVADetraibile(totaleIVADetraibile);
		sezione.setTotaleProgressivoImponibileDetraibile(totaleProgressivoImponibileDetraibile);//progressivo
		sezione.setTotaleProgressivoIvaDetraibile(totaleProgressivoIvaDetraibile);//progressivo

		//TERZA RIGA TOTALI ES/NI/FCI...
		sezione.setTotaleImponibileEsente(totaleImponibileEsente);
		sezione.setTotaleProgressivoImponibileEsente(totaleProgressivoImponibileEsente);//progressivo

		//RIGA #4
		sezione.setTotaleImponibile(totaleImponibile);
		sezione.setTotaleIVA(totaleIVA);
		sezione.setTotaleTotale(totaleTotale);
		sezione.setTotaleProgressivoImponibile(totaleProgressivoImponibile);//progressivo
		sezione.setTotaleProgressivoIva(totaleProgressivoIva);//progressivo
		sezione.setTotaleTotaleProgressivo(totaleTotaleProgressivo);//progressivo

	
		// Valore di “pro-rata” definito sull’entità “Gruppo Attività Iva” del
		// Registro Iva da stampare per l’Anno Esercizio
		// definito nei par. di input (campo da visualizzare “percProRata”,
		// entità “ProRata e Chiusura Gruppo Iva”).
		BigDecimal percentualeProrata = BigDecimal.ZERO;
		if (gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva() != null && !gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva().isEmpty()) {
			for(ProRataEChiusuraGruppoIva proRata : gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva()){
				if(proRata.getAnnoEsercizio().equals(getAnnoEsercizio())){
					percentualeProrata = proRata.getPercentualeProRata();
					break;
				}
			}
		}

		// Il calcolo dell’iva indetraibile sulla base della % di pro-rata è il
		// seguente:
		// <<Sum (IVA detraibile)>> * <<percProrata>> / 100
		BigDecimal ivaIndetraibileCausaProRata = totaleIVADetraibile.multiply(percentualeProrata)
				.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).setScale(2, RoundingMode.HALF_DOWN);

		sezione.setPercProrata(percentualeProrata);
		sezione.setIvaIndetraibileCausaProRata(ivaIndetraibileCausaProRata);
		return sezione;
	}

	private Map<String, StampaRegistroIvaRiepilogoIva> accorpaRigheAliquota(List<SubdocumentoIvaSpesa> listaSubdocumenti) {
		final String methodName = "accorpaRigheAliquota";
		Map<String, StampaRegistroIvaRiepilogoIva> map = new HashMap<String, StampaRegistroIvaRiepilogoIva>();
		for(SubdocumentoIvaSpesa sis : listaSubdocumenti) {
//			DocumentoSpesa d = sis.getDocumentoCollegatoCastato();
			for(AliquotaSubdocumentoIva asi : sis.getListaAliquotaSubdocumentoIva()) {
				AliquotaIva ai = asi.getAliquotaIva();
				final String codiceAliquota = ai.getCodice();
				StampaRegistroIvaRiepilogoIva riga  = map.get(codiceAliquota);
				
				if(riga == null) {
					riga = new StampaRegistroIvaRiepilogoIva();
					map.put(codiceAliquota, riga);
				
					// A2 - codice - Aliquota Iva
					// B2 - percAliquota - Aliquota Iva
					// C2 - descrizione - Aliquota Iva
					// D2 - precIndetraibilita - AliquotaIva
					riga.setAliquotaSubdocumentoIva(asi);
					log.debug(methodName, codiceAliquota + " - A2 - codice - Aliquota Iva - " + asi.getAliquotaIva().getCodice());
					log.debug(methodName, codiceAliquota + " - B2 - percAliquota - Aliquota Iva - " + asi.getAliquotaIva().getPercentualeAliquota());
					log.debug(methodName, codiceAliquota + " - C2 - descrizione - Aliquota Iva - " + asi.getAliquotaIva().getDescrizione());
					log.debug(methodName, codiceAliquota + " - D2 - precIndetraibilita - Aliquota Iva - " + asi.getAliquotaIva().getPercentualeIndetraibilita());
					
					// E2 - Imponibile
					riga.setImponibile(BigDecimal.ZERO);
					// F2 - IVA
					riga.setIva(BigDecimal.ZERO);
					// G2 - Totale
					riga.setTotale(BigDecimal.ZERO);
					// H2 - Progressivo Imponibile
					riga.setProgressivoImponibile(BigDecimal.ZERO);
					// I2 - Progressivo Iva
					riga.setProgressivoIva(BigDecimal.ZERO);
					// L2 - Progressivo Totale
					riga.setTotaleProgressivo(BigDecimal.ZERO);
				}
				
					// E2 - Imponibile: è dato dalla sommatoria dei valori degli imponibili (V. I1) per il corrispondente codice aliquota.
					// Il valore così ricavato di  E2 viene memorizzato nell’entità “Progressivi Iva”
				 	BigDecimal imponibile = asi.getImponibile().setScale(2, RoundingMode.HALF_DOWN);
					log.debug(methodName, codiceAliquota + " - E2 - Imponibile - old " + riga.getImponibile() + " - added " + imponibile);
					//BigDecimal imponibileConSegno = d.getTipoDocumento().isNotaCredito() ? asi.getImponibile().negate() : asi.getImponibile();
					//riga.setImponibile(riga.getImponibile().add(imponibileConSegno));
					//SIAC-3791
					riga.setImponibile(riga.getImponibile().add(imponibile));
					log.debug(methodName, codiceAliquota + " - E2 - Imponibile - new " + riga.getImponibile());
					
					//F2 - IVA: è dato dalla sommatoria dei valori dell’imposta (V. L1) per il corrispondente codice aliquota.
					// Il valore così ricavato di  F2 viene memorizzato nell’entità “Progressivi Iva” (V. par. 2.5.5.2)
					BigDecimal iva = asi.getImposta().setScale(2, RoundingMode.HALF_DOWN);
					log.debug(methodName, codiceAliquota + " - F2 - IVA - old " + riga.getIva() + " - added " + iva);
					//BigDecimal impostaConSegno = d.getTipoDocumento().isNotaCredito() ? asi.getImposta().negate() : asi.getImposta();
					//riga.setIva(riga.getIva().add(impostaConSegno));
					//SIAC-3791
					riga.setIva(riga.getIva().add(iva));
					log.debug(methodName, codiceAliquota + " - F2 - IVA - new " + riga.getIva());
					// G2 - fuori dal loop
					
					// L2 - Fuori dal loop				
				
			}
		}
		return map;
	}
	
	/**
	 * Ottiene il totale imponibile a partire dal progressivo iva.
	 * 
	 * @param progressivoIva il progressivo da cui estrarre il dato
	 * 
	 * @return il totale da considerare
	 */
	protected abstract BigDecimal obtainTotaleImponibile(ProgressiviIva progressivoIva);
	
	/**
	 * Ottiene il totale iva a partire dal progressivo iva.
	 * 
	 * @param progressivoIva il progressivo da cui estrarre il dato
	 * 
	 * @return il totale da considerare
	 */
	protected abstract BigDecimal obtainTotaleIva(ProgressiviIva progressivoIva);
	
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
		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleImponibileDefinitivo " + progressiviIva.getTotaleImponibileDefinitivo());
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
	protected List<SubdocumentoIvaSpesa> getListaSubdocumentiIva() {
		return listaSubdocumentoIvaSpesa;
	}
}