/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.StampaLiquidazioneIvaReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaDatiERiepilogo;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaDatoIva;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaIntestazione;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaReportModel;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaRiepilogo;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaRiepilogoIva;
import it.csi.siac.siacbilser.integration.dad.ProgressiviIvaDad;
import it.csi.siac.siacbilser.integration.dad.StampaIvaDad;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;

public abstract class StampaLiquidazioneIvaBaseDataCompiler<SI extends SubdocumentoIva<?, ?, ?>> {
	
	protected LogUtil log = new LogUtil(getClass());
	
	@Autowired
	private ProgressiviIvaDad progressiviIvaDad;
	@Autowired
	private StampaIvaDad stampaIvaDad;
	
	protected StampaLiquidazioneIvaReportHandler handler;
	protected StampaLiquidazioneIvaReportModel result;
	protected StampaLiquidazioneIvaIntestazione intestazione;
	
	protected StampaLiquidazioneIvaDatiERiepilogo pagina;
	
	protected List<RegistroIva> listaRegistroIva;
	protected List<SI> listaSubdocumentoIva;
	
	protected BigDecimal totaleIva = BigDecimal.ZERO;
	
//	private Map<String, ProgressiviIva> cacheProgressivi = new HashMap<String, ProgressiviIva>();
	
	protected Map<Integer, TipoStampa> mapTipoStampaRegistro = new HashMap<Integer, TipoStampa>();
	
	/**
	 * Compila i dati per il popolamento delle sezioni.
	 * 
	 * @param handler          l'handler del report
	 * @param result           il model del report
	 * @param listaRegistroIva la lista dei registri
	 */
	@Transactional
	public void compileData(StampaLiquidazioneIvaReportHandler handler, StampaLiquidazioneIvaReportModel result,
			List<RegistroIva> listaRegistroIva) {
		final String methodName = "compileData";
		// Inizializzo i dati
		initializeData(handler, result, intestazione, listaRegistroIva);
		// Creo e imposto la pagina nel model
		createAndSetPage();
		// Ottengo la lista dei subdocumenti iva
		log.debug(methodName, "Numero totale registri per cui trovare i subdocumenti: " + this.listaRegistroIva.size());
		ottieniListaSubdocumentoIva();
		log.debug(methodName, "Numero totale subdocumenti trovati: " + listaSubdocumentoIva.size());
		
		impostaTipoStampaRegistroGiaEffettuata();
		// Popolamento sezioni
		popolaSezione1();
		log.debug(methodName, "Sezione 1 popolata correttamente");
		popolaSezione2();
		log.debug(methodName, "Sezione 2 popolata correttamente");
	}

	private void impostaTipoStampaRegistroGiaEffettuata() {
		String methodName = "impostaTipoStampaRegistroGiaEffettuata";
		for(RegistroIva registroIva : listaRegistroIva){
			boolean stampeDefinitivePresenti = stampaIvaDad.presentiStampeByRegistroTipoStampaPeriodoETipoStampaIva(registroIva, TipoStampa.DEFINITIVA, handler.getPeriodo(), handler.getAnnoEsercizio(),TipoStampaIva.REGISTRO);
			if(stampeDefinitivePresenti){
				log.debug(methodName, "presente stampa definitiva");
				mapTipoStampaRegistro.put(registroIva.getUid(), TipoStampa.DEFINITIVA);
				continue;
			}
			boolean stampeBozzaPresenti = stampaIvaDad.presentiStampeByRegistroTipoStampaPeriodoETipoStampaIva(registroIva, TipoStampa.BOZZA, handler.getPeriodo(), handler.getAnnoEsercizio(), TipoStampaIva.REGISTRO);
			if(stampeBozzaPresenti){
				log.debug(methodName, "presente stampa bozza");
				mapTipoStampaRegistro.put(registroIva.getUid(), TipoStampa.BOZZA);
			}
			
		}
		
	}

	/**
	 * Inizializza i dati con quanto fornito in input dal chiamante.
	 * 
	 * @param handler          l'handler del report
	 * @param result           il model del report
	 * @param intestazione     l'intestazione della sezione
	 * @param listaRegistroIva la lista dei registri
	 */
	private void initializeData(StampaLiquidazioneIvaReportHandler handler, StampaLiquidazioneIvaReportModel result,
			StampaLiquidazioneIvaIntestazione intestazione, List<RegistroIva> listaRegistroIva) {
		this.handler = handler;
		this.result = result;
		this.intestazione = intestazione;
		this.listaRegistroIva = listaRegistroIva;
		
		progressiviIvaDad.setEnte(handler.getEnte());
		stampaIvaDad.setEnte(handler.getEnte());
	}
	
	/**
	 * Crea la pagina e la imposta all'interno del model.
	 */
	protected abstract void createAndSetPage();
	
	/**
	 * Ottiene la lista dei subdocumenti iva.
	 */
	protected abstract void ottieniListaSubdocumentoIva();

	/**
	 * Popola la sezione 1 della pagina.
	 */
	protected abstract void popolaSezione1();
	
//	/**
//	 * Carica il progressivo iva per registro, aliquota, periodo. Lo imposta inoltre nella cache, se non gi&agrave; presente.
//	 * 
//	 * @param registroIva il registro
//	 * @param aliquotaIva l'aliquota
//	 * @param periodo     il periodo
//	 * @param cache       la cache di progressivi
//	 * 
//	 * @return il progressivo relativo ai dati di input
//	 */
//	protected ProgressiviIva caricaProgressiviIvaCached(RegistroIva registroIva, AliquotaIva aliquotaIva, Periodo periodo) {
//		final String methodName = "caricaProgressiviIva";
//		// Controllo se caricare il progressivo
//		final String key = computeKeyRegistroAliquotaPeriodo(registroIva, aliquotaIva, periodo);
//		
//		if(cacheProgressivi.containsKey(key)){
//			log.debug(methodName, "Progressivo con chiave " + key + " per periodo precedente presente in cache");
//			return cacheProgressivi.get(key);
//		}
//		
//		log.debug(methodName, "Progressivo con chiave " + key + " per periodo precedente NON presente in cache. Lo ricerco su DB.");
//		ProgressiviIva pi = caricaProgressiviIva(registroIva, aliquotaIva, periodo);
//		cacheProgressivi.put(key, pi);
//		
//		return pi;
//	}

	protected String computeKeyRegistroAliquotaPeriodo(RegistroIva registroIva, AliquotaIva aliquotaIva, Periodo periodo) {
		final String methodName = "computeKeyRegistroAliquotaPeriodo";
		final String progressiviIvaIdentifier = "RegistroIva[uid:" + registroIva.getUid() + "]___AliquotaIva[uid:" + aliquotaIva.getCodice() + "]___Periodo[codice:" + periodo.getCodice() + "]___AnnoEsercizio[" + handler.getAnnoEsercizio()+"]";
		log.debug(methodName, "Chiave per progressivo: " + progressiviIvaIdentifier);
		return progressiviIvaIdentifier;
	}
	
//	/**
//	 * Ottiene il progressivo iva dal db. Oppure ne istanzia uno con i totali a zero se non presente.
//	 * 
//	 * @param registroIva il registro del progressivo
//	 * @param aliquotaIva l'aliquota del progressivo
//	 * @param periodo     il periodo del progressivo
//	 * 
//	 * @return il progressivo corrispondente, se presente
//	 */
//	protected ProgressiviIva inizializzaProgressiviIva(RegistroIva registroIva, AliquotaIva aliquotaIva, Periodo periodo) {
//		final String methodName = "caricaProgressiviIva";
//		ProgressiviIva pi = new ProgressiviIva();
//		pi = new ProgressiviIva();
//		pi.setRegistroIva(registroIva);
//		pi.setAliquotaIva(aliquotaIva);
//		pi.setPeriodo(periodo);
//		pi.setAnnoEsercizio(handler.getAnnoEsercizio());
//		pi.setEnte(handler.getEnte());
//		
////		ProgressiviIva pi = progressiviIvaDad.findProgressiviIvaByRegistroAndAliquotaIvaAndPeriodoAndAnnoAndEnte(registroIva,
////				aliquotaIva, periodo, handler.getAnnoEsercizio());
////		
////		if(pi == null) {
////			log.info(methodName, "Progressivo non presente in archivio! Ne istanzio uno vuoto con i totali a zero. Chiave: "+computeKeyRegistroAliquotaPeriodo(registroIva, aliquotaIva, periodo));
////			pi = new ProgressiviIva();
////			pi.setRegistroIva(registroIva);
////			pi.setAliquotaIva(aliquotaIva);
////			pi.setPeriodo(periodo);
////			pi.setAnnoEsercizio(handler.getAnnoEsercizio());
////			pi.setEnte(handler.getEnte());
////		}
//		
//		return pi;
//	}
	
	/**
	 * Controlla se la riga sia da saltare o meno.
	 * <br>
	 * L'implementazione di default non fa saltare la compilazione della riga.
	 * 
	 * @param pi il progressivo da cui calcolare se la riga sia da saltare
	 * 
	 * @return <code>true</code> nel caso in cui la riga sia da saltare; <code>false</code> in caso contrario
	 */
	protected boolean isRigaToSkip(BigDecimal importo1, BigDecimal importo2) {
		return false;
	}
	
	/**
	 * Ottiene l'imposta a partire da progressivi e aliquota iva.
	 * 
	 * @param progressiviIva il progressivo
	 * @param aliquotaIva    l'aliquota
	 * 
	 * @return l'imposta
	 */
	protected abstract BigDecimal obtainImpostaFromProgressiviIvaAndAliquotaIva(ProgressiviIva progressiviIva, AliquotaIva aliquotaIva);
	
	/**
	 * Popola la sezione 2 della pagina.
	 */
	protected void popolaSezione2() {
		final String methodName = "popolaSezione2";
		StampaLiquidazioneIvaRiepilogo sezione2 = new StampaLiquidazioneIvaRiepilogo();
		pagina.setRiepilogo(sezione2);
		// La lista di elementi è composta dal numero dei codici Aliquota Iva selezionati nella sezione 1.
		// Per ogni codice si riportano anche i corrispondenti valori di percAliquota, Tipo Operazione Iva  e descrizione.
		
		List<StampaLiquidazioneIvaDatoIva> righeSezione1 = pagina.getListaDatiIva();
		Map<String, StampaLiquidazioneIvaRiepilogoIva> map = new HashMap<String, StampaLiquidazioneIvaRiepilogoIva>();
		
		for(StampaLiquidazioneIvaDatoIva rigaSezione1 : righeSezione1) {
			String codiceRiga = rigaSezione1.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice();
			StampaLiquidazioneIvaRiepilogoIva riga = map.get(codiceRiga);
			if(riga == null) {
				log.debug(methodName, "Inizializzazione della riga per codice " + codiceRiga);
				// Inizializzo la riga e compongo i dati univoci
				riga = new StampaLiquidazioneIvaRiepilogoIva();
				map.put(codiceRiga, riga);
				
				// C2 - codice - Aliquota Iva
				// D2 - percAliquota - Aliquota Iva
				// E2 - Tipo Operazione Iva (enum) - Aliquota Iva
				// F2 - descrizione - Aliquota Iva
				riga.setAliquotaSubdocumentoIva(rigaSezione1.getAliquotaSubdocumentoIva());
				log.debug(methodName, codiceRiga + " - C2 - aliquotaIva - codice - " + rigaSezione1.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice());
				log.debug(methodName, codiceRiga + " - D2 - aliquotaIva - percAliquota - " + rigaSezione1.getAliquotaSubdocumentoIva().getAliquotaIva().getPercentualeAliquota());
				log.debug(methodName, codiceRiga + " - E2 - aliquotaIva - tipoOperazioneIva - " + rigaSezione1.getAliquotaSubdocumentoIva().getAliquotaIva().getTipoOperazioneIva());
				log.debug(methodName, codiceRiga + " - F2 - aliquotaIva - descrizione - " + rigaSezione1.getAliquotaSubdocumentoIva().getAliquotaIva().getDescrizione());
				
				// Inizializzazione dei totali
				// G2 - totImponibileDef
				riga.setImponibile(BigDecimal.ZERO);
				// H2 - totIvaDef
				riga.setImposta(BigDecimal.ZERO);
			}
			// G2 - totImponibileDef - Progressivi Iva - Il valore di G2 è dato dalla sommatoria dei valori di G1 per il corrispondente codice aliquota
			BigDecimal imponibileToAdd = rigaSezione1.getImponibile();
			BigDecimal imponibileRigaOld = riga.getImponibile();
			BigDecimal imponibileRigaNew = imponibileRigaOld.add(imponibileToAdd);
			riga.setImponibile(imponibileRigaNew);
			log.debug(methodName, codiceRiga + " - G2 - totImponibileDef - vecchio imponibile: " + imponibileRigaOld + " - imponibile da aggiungere: " + imponibileToAdd
					+ " - nuovo imponibile: " + imponibileRigaNew);
			// H2 - totIvaDef - Progressivi Iva - Il valore di H2 è dato dalla sommatoria dei valori di H1 per il corrispondente codice aliquota
			BigDecimal impostaToAdd = rigaSezione1.getImposta();
			BigDecimal impostaRigaOld = riga.getImposta();
			BigDecimal impostaRigaNew = impostaRigaOld.add(impostaToAdd);
			riga.setImposta(impostaRigaNew);
			log.debug(methodName, codiceRiga + " - H2 - totIvaDef - vecchia imposta: " + impostaRigaOld + " - imposta da aggiungere: " + impostaToAdd
					+ " - nuova imposta: " + impostaRigaNew);
		}
		
		// Inizializzo i campi per i totali
		// Somma colonne E2
		BigDecimal sumImponibile = BigDecimal.ZERO;
		// Somma colonne F2
		BigDecimal sumIVA = BigDecimal.ZERO;
		
		List<DatiIva> listaRighe = new ArrayList<DatiIva>();
		
		for(StampaLiquidazioneIvaRiepilogoIva riga : map.values()) {
			// <<Sum (Imponibile)>> - Sommatoria dei valori della colonna “G2”
			sumImponibile = sumImponibile.add(riga.getImponibile());
			// <<Sum (IVA)>> - Sommatoria dei valori della colonna “H2”
			sumIVA = sumIVA.add(riga.getImposta());
			
			listaRighe.add(riga);
		}
		
		totaleIva = sumIVA;
		sezione2.setListaRiepilogoIva(listaRighe);
		sezione2.setTotaleImponibile(sumImponibile);
		log.debug(methodName, "E2 - Totale imponibile: " + sumImponibile);
		sezione2.setTotaleImposta(sumIVA);
		log.debug(methodName, "F2 - Totale imposta: " + sumIVA);
	}

	/**
	 * Controlla se il valore fornito in input sia <code>null</code> oppure zero.
	 * 
	 * @param value il valore da controllare
	 * 
	 * @return <code>true</code> se il valore &eacute; <code>null</code> oppure pari a {@link BigDecimal#ZERO}; <code>false</code> in caso contrario
	 */
	protected boolean isNullOrZero(BigDecimal value) {
		return value == null || BigDecimal.ZERO.compareTo(value) == 0;
	}
	
	/**
	 * @return the totaleIva
	 */
	public BigDecimal getTotaleIva() {
		return totaleIva;
	}
}
