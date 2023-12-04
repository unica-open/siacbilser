/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.registroiva.InserisceStampaIvaService;
import it.csi.siac.siacbilser.business.service.stampa.base.JAXBBaseReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler.StampaLiquidazioneIvaAcquistiIvaDifferitaPagatiDataCompiler;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler.StampaLiquidazioneIvaAcquistiIvaImmediataDataCompiler;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler.StampaLiquidazioneIvaCorrispettiviDataCompiler;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler.StampaLiquidazioneIvaVenditeIvaDifferitaIncassatiDataCompiler;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler.StampaLiquidazioneIvaVenditeIvaImmediataDataCompiler;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaIntestazione;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaReportModel;
import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaRiepilogoGlobale;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siacbilser.integration.dad.ProrataEChiusuraGruppoIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaLiquidazioneIvaReportHandler extends JAXBBaseReportHandler<StampaLiquidazioneIvaReportModel> {
	
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	@Autowired
	private ProrataEChiusuraGruppoIvaDad prorataEChiusuraGruppoIvaDad;
	@Autowired
	private EnteDad enteDad;
	
	@Autowired
	private InserisceStampaIvaService inserisceStampaIvaService;
	@Autowired
	private StampaLiquidazioneIvaAcquistiIvaImmediataDataCompiler stampaLiquidazioneIvaAcquistiIvaImmediataDataCompiler;
	@Autowired
	private StampaLiquidazioneIvaAcquistiIvaDifferitaPagatiDataCompiler stampaLiquidazioneIvaAcquistiIvaDifferitaPagatiDataCompiler;
	@Autowired
	private StampaLiquidazioneIvaVenditeIvaImmediataDataCompiler stampaLiquidazioneIvaVenditeIvaImmediataDataCompiler;
	@Autowired
	private StampaLiquidazioneIvaVenditeIvaDifferitaIncassatiDataCompiler stampaLiquidazioneIvaVenditeIvaDifferitaIncassataDataCompiler;
	@Autowired
	private StampaLiquidazioneIvaCorrispettiviDataCompiler stampaLiquidazioneIvaCorrispettiviDataCompiler;
	
	private Integer annoEsercizio;
	private GruppoAttivitaIva gruppoAttivitaIva;
	private Periodo periodo;
	private TipoChiusura tipoChiusura;
	private TipoStampa tipoStampa;
	private List<RegistroIva> listaRegistroIva;

	
	private final Date now = new Date();
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public void elaborate() {	
		super.elaborate();
	}
	
	@Override
	public String getCodiceTemplate() {
		return "LiquidazioneIva";
	}
	
	@Override
	protected void elaborateData() {
		final String methodName = "elaborateData";
		
		// Popolo i campi comuni
		popolaDatiBaseAPartireDaiDatiDiInput();
		
		log.debug(methodName, "Compilazione intestazione");
		compilaIntestazione();
		
		log.debug(methodName, "Stampa pagina 1: " + TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA.getDescrizione());
		stampaLiquidazioneIvaAcquistiIvaImmediataDataCompiler.compileData(this, result, listaRegistroIva);
		
		log.debug(methodName, "Stampa pagina 2: " + TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.getDescrizione() + " pagata");
		stampaLiquidazioneIvaAcquistiIvaDifferitaPagatiDataCompiler.compileData(this, result, listaRegistroIva);
		
		log.debug(methodName, "Stampa pagina 3: " + TipoRegistroIva.VENDITE_IVA_IMMEDIATA.getDescrizione());
		stampaLiquidazioneIvaVenditeIvaImmediataDataCompiler.compileData(this, result, listaRegistroIva);
		
		log.debug(methodName, "Stampa pagina 4: " + TipoRegistroIva.VENDITE_IVA_DIFFERITA.getDescrizione() + " incassata");
		stampaLiquidazioneIvaVenditeIvaDifferitaIncassataDataCompiler.compileData(this, result, listaRegistroIva);
		
		log.debug(methodName, "Stampa pagina 5: " + TipoRegistroIva.CORRISPETTIVI.getDescrizione());
		stampaLiquidazioneIvaCorrispettiviDataCompiler.compileData(this, result, listaRegistroIva);
		
		log.debug(methodName, "Compilazione riepilogo della stampa");
		compilaRiepilogo();
		
		// Aggiorno il gruppo
		if(TipoStampa.DEFINITIVA.equals(tipoStampa)) {
			aggiornaGruppoAttivitaIvaEProRata();
		}
	}
	
	/**
	 * Popola i dati di base a partire dall'input.
	 */
	private void popolaDatiBaseAPartireDaiDatiDiInput() {
		// Empty
	}
	
	/**
	 * Compila l'intestazione della stampa
	 */
	protected void compilaIntestazione() {
		final String methodName = "compilaIntestazione";
		StampaLiquidazioneIvaIntestazione stampaLiquidazioneIvaIntestazione = new StampaLiquidazioneIvaIntestazione();
		
		// Ottengo i dati dell'ente da db
		// Ottengo soggetto associato all'ente e indirizzo
		Soggetto soggetto = enteDad.getSoggettoByEnte(getEnte());
		IndirizzoSoggetto indirizzoSoggetto = enteDad.getIndirizzoSoggettoPrincipaleIvaByEnte(getEnte());
		
		stampaLiquidazioneIvaIntestazione.setEnte(getEnte());
		log.debug(methodName, "Impostazione ente con uid " + (getEnte() != null ? getEnte().getUid() : "null"));
		stampaLiquidazioneIvaIntestazione.setSoggetto(soggetto);
		log.debug(methodName, "Impostazione soggetto con uid " + (soggetto != null ? soggetto.getUid() : "null"));
		stampaLiquidazioneIvaIntestazione.setIndirizzoSoggetto(indirizzoSoggetto);
		log.debug(methodName, "Impostazione indirizzo soggetto con denominazione " + (indirizzoSoggetto != null ? indirizzoSoggetto.getDenominazione() : "null"));
		
		stampaLiquidazioneIvaIntestazione.setPeriodo(getPeriodo());
		log.debug(methodName, "Impostazione periodo " + getPeriodo());
		stampaLiquidazioneIvaIntestazione.setGruppoAttivitaIva(getGruppoAttivitaIva());
		log.debug(methodName, "Impostazione gruppo attivita iva con uid " + (getGruppoAttivitaIva() != null ? getGruppoAttivitaIva().getUid() : "null"));
		
		// Ottengo il tipoStampa direttamente dall'handler
		stampaLiquidazioneIvaIntestazione.setTipoStampa(getTipoStampa());
		log.debug(methodName, "Impostazione tipo stampa " + getTipoStampa());
		
		// L'anno di riferimento è presente all'interno del bilancio fornito in input
		stampaLiquidazioneIvaIntestazione.setAnnoDiRiferimentoContabile(getAnnoEsercizio());
		log.debug(methodName, "Impostazione anno di riferimento contabile " + getAnnoEsercizio());
		
		// Il numero di pagina comincia sempre da 1
		final Integer numeroDiPagina = Integer.valueOf(1);
		stampaLiquidazioneIvaIntestazione.setNumeroDiPagina(numeroDiPagina);
		log.debug(methodName, "Impostazione numero di pagina " + numeroDiPagina);
		
		result.setIntestazione(stampaLiquidazioneIvaIntestazione);
	}
	
	private void compilaRiepilogo() {
		final String methodName = "compilaRiepilogo";
		BigDecimal percentualeProRata = ottieniPercentualeProRata();
		
		// A - <<TOTALE iva a debito>>
		// È la sommatoria delle colonne H2 delle pagine 3,4 e 5 relative ai Registri di tipo Vendite Iva immediata,
		// Vendite iva esigibilità differita (incassate) e Corrispettivi.
		BigDecimal totaleIvaADebito = stampaLiquidazioneIvaVenditeIvaImmediataDataCompiler.getTotaleIva()
				.add(stampaLiquidazioneIvaVenditeIvaDifferitaIncassataDataCompiler.getTotaleIva())
				.add(stampaLiquidazioneIvaCorrispettiviDataCompiler.getTotaleIva());
		log.debug(methodName, "totaleIvaADebito: " + totaleIvaADebito);
		
		// B - <<TOTALE iva a credito>>
		// È la sommatoria delle colonne H2 delle pagine 1 e 2 relative ai Registri di tipo Acquisti Iva immediata
		// e Acquisti iva esigibilità differita (pagati).
		BigDecimal totaleIvaACredito = stampaLiquidazioneIvaAcquistiIvaImmediataDataCompiler.getTotaleIva()
				.add(stampaLiquidazioneIvaAcquistiIvaDifferitaPagatiDataCompiler.getTotaleIva());
		log.debug(methodName, "totaleIvaACredito: " + totaleIvaACredito);
		
		/*
		 * SIAC-xxxx: il calcolo dell'indetraibile e' invertito. Originariamente era
		 *     C - <<IVA indetraibile causa proRata>>: Il calcolo é il seguente: <<IVA A CREDITO>> * <<percProrata>>%
		 *     D - <<TOTALE iva in detrazione>>: Il calcolo é il seguente: <<TOTALE iva a credito>> - <<IVA indetraibile causa proRata>>
		 * Diviene:
		 *     C - <<IVA indetraibile causa proRata>>: Il calcolo é il seguente: <<IVA A CREDITO>> * (100 - <<percProrata>>%)
		 *     D - <<TOTALE iva in detrazione>>: Il calcolo é il seguente: <<TOTALE iva a credito>> - <<IVA indetraibile causa proRata>>
		 */
		// C1 - <<IVA credito indetraibile causa proRata>>
		// Il calcolo e' il seguente: <<TOTALE iva a credito>> * (1 - <<percProrata>>)%
		BigDecimal ivaCreditoIndetraibileCausaProRata = totaleIvaACredito.multiply(BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(percentualeProRata))
				.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
		log.debug(methodName, "ivaCreditoIndetraibileCausaProRata: " + ivaCreditoIndetraibileCausaProRata);
		
		// C2 - <<IVA a debito indetraibile causa proRata>>
		// Il calcolo e' il seguente: <<TOTALE iva a debito>> * (1 - <<percProrata>>)%
		BigDecimal ivaDebitoIndetraibileCausaProRata = totaleIvaADebito.multiply(BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(percentualeProRata))
				.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
		log.debug(methodName, "ivaDebitoIndetraibileCausaProRata: " + ivaDebitoIndetraibileCausaProRata);
		
		// D1 - <<TOTALE iva credito in detrazione>>
		// Il calcolo e' il seguente: <<TOTALE iva a credito>> - <<IVA credito indetraibile causa proRata>>
		BigDecimal totaleIvaCreditoInDetrazione = totaleIvaACredito.subtract(ivaCreditoIndetraibileCausaProRata);
		log.debug(methodName, "totaleIvaCreditoInDetrazione: " + totaleIvaCreditoInDetrazione);
		
		// D2 - <<TOTALE iva in detrazione>>
		// Il calcolo e' il seguente: <<TOTALE iva a debito>> - <<IVA debito indetraibile causa proRata>>
		BigDecimal totaleIvaDebitoInDetrazione = totaleIvaADebito.subtract(ivaDebitoIndetraibileCausaProRata);
		log.debug(methodName, "totaleIvaDebitoInDetrazione: " + totaleIvaDebitoInDetrazione);
//		
		// E - <<IVA A DEBITO – IVA A CREDITO>>
		// Il calcolo è il seguente: <<TOTALE iva a debito>> - <<TOTALE iva in detrazione>>
		BigDecimal ivaADebitoMenoIvaACredito = totaleIvaADebito.subtract(totaleIvaCreditoInDetrazione);
		log.debug(methodName, "ivaADebitoMenoIvaACredito: " + ivaADebitoMenoIvaACredito);
		
		// F - <<iva a credito precedente>>
		// È il valore del campo “ivaPrecedente” legato al Gruppo Attività Iva passato in input al servizio. E’ un valore sempre negativo.
		BigDecimal ivaACreditoPrecedente = gruppoAttivitaIva.getIvaPrecedente();
		log.debug(methodName, "ivaACreditoPrecedente: " + ivaACreditoPrecedente);
		
		// G - <<IVA DA VERSARE / IVA A CREDITO>>
		// Il calcolo è il seguente (somma algebrica): <<IVA A DEBITO – IVA A CREDITO>> + <<iva a credito precedente>>
		// Se l'importo è positivo si tratta di IVA DA VERSARE, se è negativo si tratta di IVA A CREDITO da utilizzare nel periodo di liquidazione iva successivo.
		BigDecimal ivaDaVersareIvaACredito = totaleIvaADebito.subtract(totaleIvaCreditoInDetrazione)
				.add(ivaACreditoPrecedente);
		log.debug(methodName, "ivaDaVersareIvaACredito: " + ivaDaVersareIvaACredito);
		
		StampaLiquidazioneIvaRiepilogoGlobale riepilogo = new StampaLiquidazioneIvaRiepilogoGlobale();
		result.setRiepilogoGlobale(riepilogo);
		
		// Popolamento riepilogo
		riepilogo.setIvaADebito(totaleIvaADebito);
		riepilogo.setIvaACredito(totaleIvaACredito);
		riepilogo.setPercentualeProRata(percentualeProRata);
		riepilogo.setIvaIndetraibileCausaProRata(ivaCreditoIndetraibileCausaProRata);
		riepilogo.setIvaDebitoIndetraibileCausaProRata(ivaDebitoIndetraibileCausaProRata);
		riepilogo.setTotaleIvaAmmessaInDetrazione(totaleIvaCreditoInDetrazione);
		riepilogo.setTotaleIvaDebitoAmmessaInDetrazione(totaleIvaDebitoInDetrazione);
		riepilogo.setIvaADebitoMenoIvaACredito(ivaADebitoMenoIvaACredito);
		riepilogo.setIvaACreditoPeriodoPrecedente(ivaACreditoPrecedente);
		riepilogo.setIvaDaVersareIvaACredito(ivaDaVersareIvaACredito);
	}
	
	@Override
	protected void handleResponse(GeneraReportResponse res) {
		final String methodName = "handleResponse";
		log.debug(methodName, "numero di pagine generata: " + res.getNumeroPagineGenerate());
		
		persistiStampaIva(res);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Aggiorna il GruppoAttivitaIva e il ProRata associato.
	 */
	private void aggiornaGruppoAttivitaIvaEProRata() {
		aggiornaUltimoMeseDefinitivo();
		aggiornaIvaPrecedente();
		gruppoAttivitaIvaDad.setLoginOperazione(getRichiedente().getOperatore().getCodiceFiscale());
		
		gruppoAttivitaIva.setAnnualizzazione(annoEsercizio);
		gruppoAttivitaIvaDad.aggiornaGruppoAttivitaIva(gruppoAttivitaIva);
		
		for(ProRataEChiusuraGruppoIva precgi : gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva()) {
			GruppoAttivitaIva gai = new GruppoAttivitaIva();
			gai.setUid(gruppoAttivitaIva.getUid());
			precgi.setGruppoAttivitaIva(gai);
			
			prorataEChiusuraGruppoIvaDad.aggiornaProrataEChiusuraGruppoIva(precgi);
		}
	}
	
	/**
	 * Aggiorna l'ultimo mese definitivo
	 */
	private void aggiornaUltimoMeseDefinitivo() {
		// ultimoMeseDef: corrisponde al mese finale del periodo di cui si vuole stampare la liquidazione iva.
		for (ProRataEChiusuraGruppoIva precgi : gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva()) {
			precgi.setUltimoMeseDefinito(periodo.getOrdinaleUltimoMesePeriodo());
		}
	}

	/**
	 * Aggiorna l'iva a credito precedente
	 */
	private void aggiornaIvaPrecedente() {
		final String methodName = "aggiornaIvaPrecedente";
		// se l’importo <<IVA DA VERSARE / IVA A CREDITO>> e' negativo, allora il valore (comprensivo del segno) viene utilizzato per aggiornare il campo
		// dell’entità 'Gruppo Attività Iva'; se è positivo, il campo 'ivaPrecedente' dell'entità 'Gruppo Attivita' Iva' viene settato a zero.
		BigDecimal ivaACredito = result.getRiepilogoGlobale().getIvaDaVersareIvaACredito();
		log.debug(methodName, "Iva a credito da riepilogo: " + ivaACredito);
		BigDecimal ivaACreditoDaImpostare = ivaACredito.signum() < 0 ? ivaACredito : BigDecimal.ZERO;
		log.debug(methodName, "Iva a credito da impostare: " + ivaACreditoDaImpostare);
//		gruppoAttivitaIva.setIvaPrecedente(ivaACreditoDaImpostare);
		
		for(ProRataEChiusuraGruppoIva precgi : gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva()) {
			precgi.setIvaPrecedente(ivaACreditoDaImpostare);
		}
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

	private StampaIva creaStampaIva(GeneraReportResponse res) {
		StampaIva result = new StampaIva();
		
		result.setListaRegistroIva(listaRegistroIva);
		result.setPeriodo(periodo);
		result.setAnnoEsercizio(annoEsercizio);
		
		File file = res.getReport();
		
		result.setCodice(file.getCodice());
		
		result.setEnte(ente);
		result.setTipoStampa(tipoStampa);
		result.setTipoStampaIva(TipoStampaIva.LIQUIDAZIONE_IVA);
		
		List<File> listaFile = new ArrayList<File>();
		listaFile.add(res.getReport());
		result.setFiles(listaFile);
		
		// Dati di bozza e definitiva
		boolean stampaBozza = TipoStampa.BOZZA.equals(tipoStampa);
		boolean stampaDefinitiva = TipoStampa.DEFINITIVA.equals(tipoStampa);
		result.setFlagStampaDefinitivo(Boolean.valueOf(stampaDefinitiva));
		result.setFlagStampaProvvisorio(Boolean.valueOf(stampaBozza));
		
		return result;
	}



	/**
	 * Ottiene la percentuale del pro-rata.
	 * 
	 * @return il pro-rata
	 */
	private BigDecimal ottieniPercentualeProRata() {
		List<ProRataEChiusuraGruppoIva> listaProRata = gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva();
		
		BigDecimal proRata = BigDecimal.ZERO;
		for(ProRataEChiusuraGruppoIva precgi : listaProRata) {
			if(precgi.getDataFineValidita() == null || precgi.getDataFineValidita().after(now)) {
				proRata = precgi.getPercentualeProRata();
			}
		}
		return proRata;
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
	 * @return the listaRegistroIva
	 */
	public List<RegistroIva> getListaRegistroIva() {
		return listaRegistroIva;
	}

	/**
	 * @param listaRegistroIva the listaRegistroIva to set
	 */
	public void setListaRegistroIva(List<RegistroIva> listaRegistroIva) {
		this.listaRegistroIva = listaRegistroIva;
	}
	
}
