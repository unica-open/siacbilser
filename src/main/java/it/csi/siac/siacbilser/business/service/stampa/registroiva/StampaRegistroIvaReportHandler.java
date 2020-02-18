/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.registroiva.InserisceStampaIvaService;
import it.csi.siac.siacbilser.business.service.stampa.base.JAXBBaseReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaIntestazione;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaReportModel;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaTitolo;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.ProgressiviIvaDad;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Handler di base per la stampa del registro IVA.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/07/2014
 *
 */
public abstract class StampaRegistroIvaReportHandler<S extends SubdocumentoIva<?, ?, S>> extends JAXBBaseReportHandler<StampaRegistroIvaReportModel> {
	
	@Autowired
	protected ProgressiviIvaDad progressiviIvaDad;
	@Autowired
	private EnteDad enteDad;
	
	@Autowired
	private InserisceStampaIvaService inserisceStampaIvaService;
	
	protected Integer annoEsercizio;
	protected Bilancio bilancio;
	protected Boolean documentiIncassati;
	protected Boolean documentiPagati;
	protected GruppoAttivitaIva gruppoAttivitaIva;
	protected Periodo periodo;
	protected Integer primaPaginaDaStampare;
	protected RegistroIva registroIva;
	protected TipoChiusura tipoChiusura;
	protected TipoStampa tipoStampa;
	protected boolean tipoStampaBozza; 
	
	protected Map<String, ProgressiviIva> cacheProgressivi = new HashMap<String, ProgressiviIva>();
	protected Map<String, ProgressiviIva> cacheProgressiviAttuali = new HashMap<String, ProgressiviIva>();
	
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,timeout=AsyncBaseService.TIMEOUT)
	public void elaborate() {	
		super.elaborate();
	}
	
	@Override
	public String getCodiceTemplate() {
		return StampaRegistroIvaReportHandlers.byReportHandlerClass(this.getClass()).getCodiceTemplate();
	}
	
	@Override
	protected void elaborateData() {
		final String methodName = "doCompile";
		// SIAC-6110
		this.cleanReportContent = true;
		// SIAC-6232
		this.prepersistReportXml = true;
		
		// Popolo i campi comuni
		popolaDatiBaseAPartireDaiDatiDiInput();
		
		// Popolo i DAD
		popolaDad();
		
		log.debug(methodName, "Compilazione intestazione");
		compilaIntestazione();
		
		log.debug(methodName, "Compilazione titolo");
		// Compilo il titolo, uguale per le due sezioni
		creaTitoloSezione();
		
		log.debug(methodName, "Compilazione sezione 1");
		compilaSezione1();
		
		log.debug(methodName, "Compilazione sezione 2");
		compilaSezione2();
		
		// Aggiorno i progressivi
		if(TipoStampa.DEFINITIVA.equals(tipoStampa)) {
			inserisciProgressiviIvaCorrenti();
		}

	}
	
	/**
	 * Popola i dati di base.
	 */
	protected void popolaDatiBaseAPartireDaiDatiDiInput() {
		final String methodName = "popolaDatiBase";
		setAnnoEsercizio(getBilancio().getAnno());
		log.debug(methodName, "Anno di esercizio: " + getAnnoEsercizio());
		
		// Controllo se il tipo di stampa sia bozza
		setTipoStampaBozza(TipoStampa.BOZZA == getTipoStampa());
		log.debug(methodName, "Il tipo di stampa e' bozza? " + isTipoStampaBozza());
		
		popolaDatiAggiuntiviAPartireDaiDatiDiInput();
	}
	
	/**
	 * Popola gli eventuali dati aggiuntivi
	 */
	protected void popolaDatiAggiuntiviAPartireDaiDatiDiInput() {
		// Empty
		// Da override-are in caso di gestione particolare
	}
	
	/**
	 * Popola i dati dei DAD.
	 */
	private void popolaDad() {
		// Imposto l'ente ovunque
		progressiviIvaDad.setEnte(getEnte());
		progressiviIvaDad.setLoginOperazione(getRichiedente().getOperatore().getCodiceFiscale());
	}
	
	/**
	 * Compila i dati della sezione 1.
	 */
	protected abstract void compilaSezione1();
	/**
	 * Compila i dati della sezione 2.
	 */
	protected abstract void compilaSezione2();

	/**
	 * Compila l'intestazione della stampa
	 */
	protected void compilaIntestazione() {
		final String methodName = "compilaIntestazione";
		StampaRegistroIvaIntestazione intestazione = new StampaRegistroIvaIntestazione();
		
		// Ottengo i dati dell'ente da db
		// Ottengo soggetto associato all'ente e indirizzo
		Soggetto soggetto = enteDad.getSoggettoByEnte(getEnte());
		IndirizzoSoggetto indirizzoSoggetto = enteDad.getIndirizzoSoggettoPrincipaleIvaByEnte(getEnte());
		
		log.debug(methodName, "Impostazione ente con uid " + (getEnte() != null ? getEnte().getUid() : "null"));
		intestazione.setEnte(getEnte());
		log.debug(methodName, "Impostazione soggetto con uid " + (soggetto != null ? soggetto.getUid() : "null"));
		intestazione.setSoggetto(soggetto);
		log.debug(methodName, "Impostazione indirizzo soggetto con denominazione " + (indirizzoSoggetto != null ? indirizzoSoggetto.getDenominazione() : "null"));
		intestazione.setIndirizzoSoggetto(indirizzoSoggetto);
		
		// Ottengo il tipoStampa direttamente dall'handler
		log.debug(methodName, "Impostazione tipo stampa " + getTipoStampa());
		intestazione.setTipoStampa(getTipoStampa());
		
		// L'anno di riferimento è presente all'interno del bilancio fornito in input
		log.debug(methodName, "Impostazione anno di riferimento contabile " + getAnnoEsercizio());
		intestazione.setAnnoDiRiferimentoContabile(getAnnoEsercizio());
		
		log.debug(methodName, "Impostazione prima pagina da stampare " + getPrimaPaginaDaStampare());
		intestazione.setNumeroDiPagina(getPrimaPaginaDaStampare());
		
		// Imposto i dati nel model
		result.setIntestazione(intestazione);
	}
	
	/**
	 * Aggiorna i progressivi sulla base dati.
	 */
	protected void inserisciProgressiviIvaCorrenti() {
		final String methodName = "inserisciProgressiviIvaCorrenti";
		for(ProgressiviIva pi : cacheProgressiviAttuali.values()) {
			log.debug(methodName, "Inserimento progressivo per registro " + pi.getRegistroIva().getUid() + " e aliquota " + pi.getAliquotaIva().getUid()
					+ " per periodo " + annoEsercizio + "/" + periodo.getCodice() +" totale imponibile definitivo: " + pi.getTotaleImponibileDefinitivo());
//			progressiviIvaDad.inserisciProgressiviIva(pi);

			// Se ho già un record del progressivo, allora lo aggiorno. Altrimenti ne inserisco uno nuovo
			ProgressiviIva pidb = progressiviIvaDad.findProgressiviIvaByRegistroAndAliquotaIvaAndPeriodoAndAnnoAndEnte(pi.getRegistroIva(), pi.getAliquotaIva(),
					pi.getPeriodo(), pi.getAnnoEsercizio());
			if(pidb == null) {
				log.debug(methodName, "Inserisci");
				progressiviIvaDad.inserisciProgressiviIva(pi);
			} else {
				pidb.setTotaleImponibileDefinitivo(pi.getTotaleImponibileDefinitivo());
				pidb.setTotaleIvaDefinitivo(pi.getTotaleIvaDefinitivo());
				
				progressiviIvaDad.aggiornaProgressiviIva(pidb);
			}
		
			
			//----------------------
			
			
			
			log.debug(methodName, "Inserito progressivo con uid " + pi.getUid());
		}
	}
	
	/**
	 * Compila il titolo della sezione 1.
	 * 
	 * @return il titolo creato
	 */
	protected void creaTitoloSezione() {
		final String methodName = "creaTitoloSezione";
		StampaRegistroIvaTitolo stampaRegistroIvaTitolo = new StampaRegistroIvaTitolo();
		
		// <<codice gruppo>>: codice del gruppo di attività iva del registro selezionato
		// <<descrizione gruppo>>: descrizione del gruppo di attività iva del registro selezionato
		log.debug(methodName, "Impostazione gruppo attivita iva con uid " + (getGruppoAttivitaIva() != null ? getGruppoAttivitaIva().getUid() : "null"));
		stampaRegistroIvaTitolo.setGruppoAttivitaIva(getGruppoAttivitaIva());
		
		// <<codice registro>>: codice del registro iva da stampare
		// <<descrizione registro>>: descrizione del registro iva da stampare
		log.debug(methodName, "Impostazione registro iva con uid " + (getRegistroIva() != null ? getRegistroIva().getUid() : "null"));
		stampaRegistroIvaTitolo.setRegistroIva(getRegistroIva());
		
		// <<periodo>>: è il periodo scelto per la stampa del registro
		log.debug(methodName, "Impostazione periodo " + getPeriodo());
		stampaRegistroIvaTitolo.setPeriodo(periodo);
		
		// <<anno contabile>>: è l’anno di riferimento “contabile” (che può essere diverso dall’anno in cui viene elaborata la stampa)
		log.debug(methodName, "Impostazione anno contabile " + getAnnoEsercizio());
		stampaRegistroIvaTitolo.setAnnoContabile(getAnnoEsercizio());
		
		result.setTitolo(stampaRegistroIvaTitolo);
	}
	
	/**
	 * Ottiene i ProgressiviIva relativi al registro e all'aliquota per il periodo precedente.
	 * 
	 * @param ali l'aliquota del progressivo
	 * 
	 * @return il progressivo corrispondente
	 */
	protected ProgressiviIva ottieniProgressiviIvaPrecedente(AliquotaIva ali, Periodo periodo) {
		final String methodName = "ottieniProgressiviIvaPrecedente";
		// Chiave: hex(uidRegistro)+hex(uidAliquota)+codicePeriodo+anno
		// Eg: RI1A_AIB2F1_PM1_AE2014
		final String progressiviIvaIdentifier = buildIdentifier(getRegistroIva(), ali, periodo, getAnnoEsercizio());
		ProgressiviIva pi = cacheProgressivi.get(progressiviIvaIdentifier);
		if(pi == null) {
			log.debug(methodName, "Progressivo con chiave " + progressiviIvaIdentifier + " per periodo precedente non presente in cache");
			pi = ottieniProgressiviIvaDaServizio(ali, periodo);
			if(pi == null) {
				log.debug(methodName, "Progressivo con chiave " + progressiviIvaIdentifier + " per periodo precedente non presente in archivio");
				pi = new ProgressiviIva();
				
				pi.setRegistroIva(getRegistroIva());
				pi.setAliquotaIva(ali);
				pi.setPeriodo(getPeriodo());
				pi.setAnnoEsercizio(getAnnoEsercizio());
				pi.setEnte(getEnte());
			}
			cacheProgressivi.put(progressiviIvaIdentifier, pi);
		}
		return pi;
	}
	
	/**
	 * Ottiene il progressivo iva dal servizio.
	 * 
	 * @param ali l'aliquota del progressivo
	 * 
	 * @return il progressivo corrispondente, se presente
	 */
	private ProgressiviIva ottieniProgressiviIvaDaServizio(AliquotaIva ali, Periodo p) {
		// Se il periodo ha ordinale 1, allora non ho nulla da cercare
		if(p.getOrdinalePeriodo().equals(Integer.valueOf(1))) {
			return null;
		}
		
		ProgressiviIva pi = progressiviIvaDad.findProgressiviIvaByRegistroAndAliquotaIvaAndPeriodoAndAnnoAndEnte(getRegistroIva(),
				ali, p.precedente(), getAnnoEsercizio());
		
		return pi;
	}
	
	/**
	 * Ottiene i ProgressiviIva relativi al registro e all'aliquota e al periodo corrente.
	 * 
	 * @param ali l'aliquota del progressivo
	 * 
	 * @return il progressivo corrispondente
	 */
	protected ProgressiviIva ottieniProgressiviIvaAttuale(AliquotaIva ali) {
		final String methodName = "ottieniProgressiviIvaAttuale";
		// Chiave: hex(uidRegistro)+hex(uidAliquota)+codicePeriodo+anno
		// Eg: RI1A_AIB2F1_PM1_AE2014
		final String progressiviIvaIdentifier = buildIdentifier(getRegistroIva(), ali, getPeriodo(), getAnnoEsercizio());
		ProgressiviIva pi = cacheProgressiviAttuali.get(progressiviIvaIdentifier);
		if(pi == null) {
			log.debug(methodName, "Progressivo con chiave " + progressiviIvaIdentifier + " per periodo attuale non presente in cache");
			pi = new ProgressiviIva();
			
			pi.setRegistroIva(getRegistroIva());
			pi.setAliquotaIva(ali);
			pi.setPeriodo(getPeriodo());
			pi.setAnnoEsercizio(getAnnoEsercizio());
			pi.setEnte(getEnte());
			cacheProgressiviAttuali.put(progressiviIvaIdentifier, pi);
		}else{
			log.debug(methodName, "Progressivo con chiave " + progressiviIvaIdentifier + " per periodo attuale gia' presente in cache");
			log.debug(methodName, "Progressivo con chiave " + progressiviIvaIdentifier + " getTotaleImponibileDefinitivo " + pi.getTotaleImponibileDefinitivo());
			log.debug(methodName, "Progressivo con chiave " + progressiviIvaIdentifier + " getTotaleIvaDefinitivo: " + pi.getTotaleIvaDefinitivo());
		}
		return pi;
	}
	
	/**
	 * Costruisce l'identificativo per il progressivo
	 * 
	 * @param ri  il registro iva
	 * @param ali l'aliquota iva
	 * @param p   il periodo
	 * @param ae  l'anno di esercizio
	 * 
	 * @return l'identificativo dei parametri
	 */
	private String buildIdentifier(RegistroIva ri, AliquotaIva ali, Periodo p, Integer ae) {
		return "RI" + Integer.toString(ri.getUid()) + "_AI" + Integer.toString(ali.getUid()) +
				"_P" + p.getCodice() + "_AE" + ae;
	}
	
	@Override
	protected void handleResponse(GeneraReportResponse res) {
		final String methodName = "handleResponse";
		log.debug(methodName, "numero di pagine generata: "+ res.getNumeroPagineGenerate());
		
		// Salvo la stampa Iva
		persistiStampaIva(res);
	}
	
	
	
	
	
	
	
	
	/**
	 * Persiste la stampa IVA su database.
	 * 
	 * @param res la risposta del metodo di generazione del report
	 */
	private void persistiStampaIva(GeneraReportResponse res) {
		final String methodName = "persistiStampaIva";
		log.debug(methodName, "Persistenza della stampa");
		InserisceStampaIva reqISI = new InserisceStampaIva();
		reqISI.setDataOra(new Date());
		reqISI.setRichiedente(richiedente);
		reqISI.setStampaIva(creaStampaIva(res));
		
		InserisceStampaIvaResponse resISI = inserisceStampaIvaService.executeService(reqISI);
		if(resISI == null) {
			log.error(methodName, "Response del servizio InserisceStampaIva null");
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il servizio InserisceStampaIva ha risposto null"));
		}
		if(resISI.hasErrori()) {
			log.error(methodName, "Errori nell'invocazione del servizio InserisceStampaIva");
			for(Errore e : resISI.getErrori()) {
				log.error(methodName, "Errore: " + e.getTesto());
			}
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il servizio InserisceStampaIva e' terminato con errori"));
		}
		log.info(methodName, "Stampa terminata con successo. Uid record inserito su database: " + resISI.getStampaIva().getUid());
	}
	
	/**
	 * Crea una StampaIva per la persistenza.
	 * 
	 * @param res la response della generazione del report
	 * @return la stampa iva creata
	 */
	private StampaIva creaStampaIva(GeneraReportResponse res) {
		StampaIva result = new StampaIva();
		
		result.setListaRegistroIva(Arrays.asList(registroIva));
		result.setAnnoEsercizio(annoEsercizio);
		
		File file = res.getReport();
		
		result.setCodice(file.getCodice());
		
		result.setEnte(ente);
		result.setFlagIncassati(documentiIncassati);
		result.setFlagPagati(documentiPagati);
		result.setTipoStampa(tipoStampa);
		result.setPeriodo(periodo);
		result.setTipoStampaIva(TipoStampaIva.REGISTRO);
		
		List<File> listaFile = new ArrayList<File>();
		
		listaFile.add(file);
		result.setFiles(listaFile);
		
		impostaDatiStampaProvvisorioDefinitivo(result, res);
		
		return result;
	}
	
	/**
	 * Imposta i dati sul provvisorio e definitivo nella stampa.
	 * 
	 * @param stampaIva      la stampa
	 * @param reportResponse il risultato della generazione del report
	 */
	protected void impostaDatiStampaProvvisorioDefinitivo(StampaIva stampaIva, GeneraReportResponse reportResponse) {
		final String methodName = "impostaDatiStampaProvvisorioDefinitivo";
		Integer numeroPagineGenerate = reportResponse.getNumeroPagineGenerate();
		Integer ultimaPagina = numeroPagineGenerate.intValue() + primaPaginaDaStampare.intValue() - 1;
		log.debug(methodName, "Prima pagina da stampare: " + primaPaginaDaStampare + ". Numero di pagine generate per il report: " + numeroPagineGenerate
				+ ". Ultima pagina stampata: " + ultimaPagina);
		if(tipoStampaBozza) {
			// Sono in bozza: imposto i dati del provvisorio
			stampaIva.setFlagStampaDefinitivo(Boolean.FALSE);
			stampaIva.setFlagStampaProvvisorio(Boolean.TRUE);
			stampaIva.setUltimaPaginaStampaDefinitiva(null);
			stampaIva.setUltimaPaginaStampaProvvisoria(ultimaPagina);
			stampaIva.setUltimoNumProtocolloDefinitivo(null);
			stampaIva.setUltimoNumProtocolloProvvisorio(null);
			stampaIva.setUltimaDataProtocolloDefinitiva(null);
			stampaIva.setUltimaDataProtocolloProvvisoria(null);
		} else {
			// Sono in definitivo
			stampaIva.setFlagStampaDefinitivo(Boolean.TRUE);
			stampaIva.setFlagStampaProvvisorio(Boolean.FALSE);
			stampaIva.setUltimaPaginaStampaDefinitiva(ultimaPagina);
			stampaIva.setUltimaPaginaStampaProvvisoria(null);
			stampaIva.setUltimoNumProtocolloDefinitivo(ottieniUltimoNumeroProtocolloDefinitivo());
			stampaIva.setUltimoNumProtocolloProvvisorio(null);
			stampaIva.setUltimaDataProtocolloDefinitiva(ottieniUltimaDataProtocolloDefinitivo());
			stampaIva.setUltimaDataProtocolloProvvisoria(null);
		}
	}
	
	/**
	 * Ottiene l'ultimo numero di protocollo definitivo
	 * @return il numero di protocollo
	 */
	private Integer ottieniUltimoNumeroProtocolloDefinitivo() {
		Integer res = null;
		for(S si : getListaSubdocumentiIva()) {
			if(si.getNumeroProtocolloDefinitivo() != null
					&& (res == null || res.compareTo(si.getNumeroProtocolloDefinitivo()) < 0)) {
				res = si.getNumeroProtocolloDefinitivo();
			}
		}
		return res;
	}
	
	/**
	 * Ottiene l'ultimo numero di protocollo provvisorio
	 * @return il numero di protocollo
	 */
	protected Integer ottieniUltimoNumeroProtocolloProvvisorio() {
		Integer res = null;
		for(S si : getListaSubdocumentiIva()) {
			if(si.getNumeroProtocolloProvvisorio() != null
					&& (res == null || res.compareTo(si.getNumeroProtocolloProvvisorio()) < 0)) {
				res = si.getNumeroProtocolloProvvisorio();
			}
		}
		return res;
	}
	
	/**
	 * Ottiene l'ultima data di protocollo definitivo
	 * @return la data di protocollo
	 */
	private Date ottieniUltimaDataProtocolloDefinitivo() {
		Date res = null;
		for(S si : getListaSubdocumentiIva()) {
			if(si.getDataProtocolloDefinitivo() != null
					&& (res == null || res.before(si.getDataProtocolloDefinitivo()))) {
				res = si.getDataProtocolloDefinitivo();
			}
		}
		return res;
	}
	
	/**
	 * Ottiene l'ultima data di protocollo provvisorio
	 * @return la data di protocollo
	 */
	protected Date ottieniUltimaDataProtocolloProvvisorio() {
		Date res = null;
		for(S si : getListaSubdocumentiIva()) {
			if(si.getDataProtocolloProvvisorio() != null
					&& (res == null || res.before(si.getDataProtocolloProvvisorio()))) {
				res = si.getDataProtocolloProvvisorio();
			}
		}
		return res;
	}
	
	
	@Override
	protected void elaborationEnd() {
		final String methodName ="elaborationEnd";
		super.elaborationEnd();
		
		boolean endElab = elaborazioniManager.endElaborazione(StampaRegistroIvaService.class, "registroIva.uid:" + registroIva.getUid());
		if(endElab){
			log.info(methodName, "Elaborazione segnata come terminata.");
		} else {
			log.info(methodName, "Elaborazione gia' segnata come terminata precedentemente.");
		}
	}

	/**
	 * @return the annoEsercizio
	 */
	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}

	/**
	 * @param annoEsercizio the annoEsercizio to set
	 */
	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * @return the primaPaginaDaStamparereportHandler.set
	 */
	public Integer getPrimaPaginaDaStampare() {
		return primaPaginaDaStampare;
	}

	/**
	 * @param primaPaginaDaStampare the primaPaginaDaStampare to set
	 */
	public void setPrimaPaginaDaStampare(Integer primaPaginaDaStampare) {
		this.primaPaginaDaStampare = primaPaginaDaStampare;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @param registroIva the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}

	/**
	 * @return the documentiPagati
	 */
	public Boolean getDocumentiPagati() {
		return documentiPagati;
	}

	/**
	 * @param documentiPagati the documentiPagati to set
	 */
	public void setDocumentiPagati(Boolean documentiPagati) {
		this.documentiPagati = documentiPagati;
	}

	/**
	 * @return the gruppoAttivitaIva
	 */
	public GruppoAttivitaIva getGruppoAttivitaIva() {
		return gruppoAttivitaIva;
	}

	/**
	 * @param gruppoAttivitaIva the gruppoAttivitaIva to set
	 */
	public void setGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		this.gruppoAttivitaIva = gruppoAttivitaIva;
	}

	/**
	 * @return the documentiIncassati
	 */
	public Boolean getDocumentiIncassati() {
		return documentiIncassati;
	}

	/**
	 * @param documentiIncassati the documentiIncassati to set
	 */
	public void setDocumentiIncassati(Boolean documentiIncassati) {
		this.documentiIncassati = documentiIncassati;
	}

	/**
	 * @return the tipoStampa
	 */
	public TipoStampa getTipoStampa() {
		return tipoStampa;
	}

	/**
	 * @param tipoStampa the tipoStampa to set
	 */
	public void setTipoStampa(TipoStampa tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

	/**
	 * @return the tipoChiusura
	 */
	public TipoChiusura getTipoChiusura() {
		return tipoChiusura;
	}

	/**
	 * @param tipoChiusura the tipoChiusura to set
	 */
	public void setTipoChiusura(TipoChiusura tipoChiusura) {
		this.tipoChiusura = tipoChiusura;
	}

	/**
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	/**
	 * @return the tipoStampaBozza
	 */
	public boolean isTipoStampaBozza() {
		return tipoStampaBozza;
	}

	/**
	 * @param tipoStampaBozza the tipoStampaBozza to set
	 */
	public void setTipoStampaBozza(boolean tipoStampaBozza) {
		this.tipoStampaBozza = tipoStampaBozza;
	}

	/**
	 * Ottiene la lista dei subdocumenti iva
	 * @return la lista dei subdocumenti iva
	 */
	protected abstract List<S> getListaSubdocumentiIva();
}
