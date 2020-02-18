/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.stampa.base.AsyncReportBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ContatoreRegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.StampaIvaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaLiquidazioneIvaService extends AsyncReportBaseService<StampaLiquidazioneIva, StampaLiquidazioneIvaResponse, StampaLiquidazioneIvaReportHandler> {

	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private ContatoreRegistroIvaDad contatoreRegistroIvaDad;
	@Autowired
	private StampaIvaDad stampaIvaDad;
	@Autowired
	private EnteDad enteDad;
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	@Autowired
	private RegistroIvaDad registroIvaDad;
	@Autowired
	private SubdocumentoIvaSpesaDad subdocumentoIvaSpesaDad;
	
	private Bilancio bilancio;
	private Ente ente;
	private GruppoAttivitaIva gruppoAttivitaIva;
	
	private Integer ultimoMeseStampatoDefinitivo;
	private List<RegistroIva> listaRegistroIva;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getGruppoAttivitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo attivita' iva"));
		checkCondition(req.getGruppoAttivitaIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid gruppo attivita' iva"));
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		checkNotNull(req.getTipoStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo stampa"));
		checkNotNull(req.getPeriodo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo"));
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public StampaLiquidazioneIvaResponse executeService(StampaLiquidazioneIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void initReportHandler() {
		reportHandler.setAnnoEsercizio(bilancio.getAnno());
		reportHandler.setEnte(ente);
		reportHandler.setGruppoAttivitaIva(gruppoAttivitaIva);
		reportHandler.setListaRegistroIva(listaRegistroIva);
		reportHandler.setPeriodo(req.getPeriodo());
		reportHandler.setTipoChiusura(gruppoAttivitaIva.getTipoChiusura());
		reportHandler.setTipoStampa(req.getTipoStampa());
	}
	
	@Override
	protected void preStartElaboration() {
		caricaDettaglioEnte();
		caricaDettaglioBilancio();
		caricaDettaglioGruppoAttivitaIva();
		caricaListaRegistriIva();
		caricaUltimoMeseStampatoDefinitivo();
		
		checkStampaDefinitiva();
		
//		if(TipoStampa.BOZZA.equals(req.getTipoStampa())) {
//			checkStampaBozza();
//		} else 
		
			
//		}
	}
	
	@Override
	protected void init() {
		ente = req.getEnte();
		
		bilancioDad.setEnteEntity(ente);
		contatoreRegistroIvaDad.setEnte(ente);
		registroIvaDad.setEnte(ente);
		stampaIvaDad.setEnte(ente);
		subdocumentoIvaSpesaDad.setEnte(ente);
	}
	
	/**
	 * Ottiene i dati di dettaglio del bilancio.
	 */
	private void caricaDettaglioBilancio() {
		final String methodName = "caricaDettaglioBilancio";
		final Integer uid = req.getBilancio().getUid();
		log.debug(methodName, "Caricamento dati bilancio per uid " + uid);
		bilancio = bilancioDad.getBilancioByUid(uid);
		if(bilancio == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Bilancio", "uid:" + uid));
		}
		// Controllo sulla fase del bilancio
		checkFaseBilancioCoerenteConOperazione();
	}
	
	/**
	 * Controlla che la fase di bilancio sia coerente con l'operazione di stampa.
	 */
	private void checkFaseBilancioCoerenteConOperazione() {
		final String methodName = "checkFaseBilancioCoerenteConOperazione";
		FaseBilancio faseBilancio = bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio();
		
		log.debug(methodName, "Fase del bilancio: " + faseBilancio);
		// La fase di bilancio non deve essere PLURIENNALE ne' PREVISIONE
		if(FaseBilancio.PLURIENNALE.equals(faseBilancio)
				|| FaseBilancio.PREVISIONE.equals(faseBilancio)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Bilancio", faseBilancio.name()));
		}
	}

	/**
	 * Ottiene i dati di dettaglio del gruppo attivita iva.
	 */
	private void caricaDettaglioGruppoAttivitaIva() {
		final String methodName = "caricaDettaglioGruppoAttivitaIva";
		final Integer uid = req.getGruppoAttivitaIva().getUid();
		log.debug(methodName, "Caricamento dati gruppo attivita iva per uid " + uid);
		
		gruppoAttivitaIva = gruppoAttivitaIvaDad.findGruppoAttivitaIvaByIdAndAnno(uid, bilancio.getAnno());
		if(gruppoAttivitaIva == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Gruppo Attivita Iva", "con uid:" + uid));
		}
		//recupero il valore impostato in maschera di input
		BigDecimal ivaPrecedente = BigDecimal.ZERO;
		for(ProRataEChiusuraGruppoIva precgi : req.getGruppoAttivitaIva().getListaProRataEChiusuraGruppoIva()) {
			if(precgi.getIvaPrecedente() != null) {
				ivaPrecedente = precgi.getIvaPrecedente();
			}
		}
		
		for(ProRataEChiusuraGruppoIva precgi : gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva()) {
			precgi.setIvaPrecedente(ivaPrecedente);
		}
	}
	
	/**
	 * Carica la lista dei registri iva afferenti al gruppo.
	 */
	private void caricaListaRegistriIva() {
		listaRegistroIva = registroIvaDad.findRegistriByGruppoAttivitaIva(gruppoAttivitaIva);
	}
	
	/**
	 * Ottiene i dati di dettaglio dell'ente.
	 */
	private void caricaDettaglioEnte() {
		final String methodName = "caricaDettaglioEnte";
		final Integer uid = req.getEnte().getUid();
		log.debug(methodName, "Caricamento dati ente per uid " + uid);
		
		ente = enteDad.getEnteByUid(uid);
		if(ente == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ente", "con uid " + uid));
		}
	}
	

	@Override
	protected void postStartElaboration() {
		final String methodName = "postStartElaboration";
		log.info(methodName, "post start elaborazione!");
	}
	
	/**
	 * Ottiene l'ultimo mese stampato in definitivo per il gruppo.
	 */
	private void caricaUltimoMeseStampatoDefinitivo() {
		for (ProRataEChiusuraGruppoIva precgi : gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva()) {
			ultimoMeseStampatoDefinitivo = precgi.getUltimoMeseDefinito();
		}
	}

	/**
	 * 1. Nel caso di stampa in &lsquo;bozza&rsquo;: il mese iniziale del periodo selezionato deve essere successivo
	 * all'ultimo mese stampato in &lsquo;definitivo&rsquo;.
	 */
	private void checkStampaBozza() {
		final String methodName = "checkStampaBozza";
		if(ultimoMeseStampatoDefinitivo != null && req.getPeriodo().getOrdinalePrimoMesePeriodo().compareTo(ultimoMeseStampatoDefinitivo) <= 0) {
			log.debug(methodName, "Periodo selezionato gia' stampato per il gruppo " + gruppoAttivitaIva.getUid());
			throw new BusinessException(ErroreFin.NON_E_POSSIBILE_ELABORARE_LA_STAMPA_IN_BOZZA_DELLA_LIQUIDAZIONE_IVA_PER_IL_PERIODO_SELEZIONATO.getErrore());
		}
	}
	
	/**
	 * 2. Nel caso di stampa &lsquo;Definitiva&rsquo; di un certo periodo, e necessario:
	 * <ul>
	 *     <li>aver stampato in &lsquo;definitivo&rsquo; le Liquidazioni Iva dei periodi precedenti</li>
	 *     <li>aver stampato in &lsquo;definitivo&rsquo; tutti i registri IVA relativi al Gruppo Attivit&agrave; Iva e al periodo passati in input al servizio</li>
	 * </ul>
	 */
	private void checkStampaDefinitiva() {
		if(TipoStampa.DEFINITIVA.equals(req.getTipoStampa())) {
			checkStampaInDefinitivoLiquidazioniPeriodiPrecedenti();
			checkStampaInDefinitivoTuttiRegistriRelativiAlGruppo();
		}
	}

	/**
	 * Controllo di aver stampato in definitivo le Liquidazioni Iva dei periodi precedenti.
	 */
	private void checkStampaInDefinitivoLiquidazioniPeriodiPrecedenti() {
		final String methodName = "checkStampaInDefinitivoLiquidazioniPeriodiPrecedenti";
		if(req.getPeriodo().getOrdinalePeriodo() == 1) {
			// Sono nel primo periodo: va tutto bene
			log.debug(methodName, "Primo periodo selezionato: non controllo di aver gia' stampato i periodi precedenti");
			return;
		}
		Periodo periodoPrecedente = req.getPeriodo().precedente();
		log.debug(methodName, "Periodo precedente: " + periodoPrecedente.getCodice());
		if(ultimoMeseStampatoDefinitivo != null && periodoPrecedente.getOrdinaleUltimoMesePeriodo().compareTo(ultimoMeseStampatoDefinitivo) > 0) {
			log.debug(methodName, "Non ho ancora stampato il periodo precedente per il gruppo " + gruppoAttivitaIva.getUid());
			throw new BusinessException(ErroreFin.NON_E_POSSIBILE_ELABORARE_LA_STAMPA_DEFINITIVA_DELLA_LIQUIDAZIONE_IVA_PER_IL_PERIODO_SELEZIONATO_PROCEDERE_STAMPA_LIQUIDAZIONI.getErrore());
		}
	}
	
	/**
	 * Constrollo di aver stampato in definitivo tutti i registri IVA relativi al Gruppo Attivit&agrave; Iva e al periodo passati in input al servizio
	 */
	private void checkStampaInDefinitivoTuttiRegistriRelativiAlGruppo() {
		for(RegistroIva ri : listaRegistroIva) {
			if(TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.equals(ri.getTipoRegistroIva()) || TipoRegistroIva.VENDITE_IVA_DIFFERITA.equals(ri.getTipoRegistroIva())) {
				checkStampaDefinitivaDifferita(ri);
			} else {
				checkStampaDefinitivaImmediata(ri);
			}
		}
	}

	/**
	 * Controlla che il registro iva sia gi&agrave; stato stampato nel caso dell'iva differita.
	 * 
	 * @param registroIva il registro da controllare
	 */
	private void checkStampaDefinitivaDifferita(RegistroIva registroIva) {
		final String methodName = "checkStampaDefinitivaDifferita";
		
//		Integer ultimoNumeroProvvisorio = contatoreRegistroIvaDad.findUltimoNumeroProtocolloProvv(registroIva.getUid(), bilancio.getAnno(), req.getPeriodo());
//		Integer ultimoNumeroDefinitivo = contatoreRegistroIvaDad.findUltimoNumeroProtocolloDef(registroIva.getUid(), bilancio.getAnno(), req.getPeriodo());
//		
//		log.debug(methodName, "UltimoNumeroProvvisorio: " + ultimoNumeroProvvisorio);
//		log.debug(methodName, "UltimoNumeroDefinitivo: " + ultimoNumeroDefinitivo);
		
		Long numSubdocIvaStampatiProv = subdocumentoIvaSpesaDad.countSubdocIvaByRegistroAndBilancioAndPeriodoProv(registroIva.getUid(), bilancio.getAnno(), req.getPeriodo());
		Long numSubdocIvaStampatiDef = subdocumentoIvaSpesaDad.countSubdocIvaByRegistroAndBilancioAndPeriodoDef(registroIva.getUid(), bilancio.getAnno(), req.getPeriodo());
		
		List<StampaIva> listaStampaIva = caricaListaStampeIva(registroIva, req.getPeriodo());
		
		Boolean flagStampaProvvisorio = caricaFlagStampaProvvisorioDaListaStampaIva(listaStampaIva);
		Boolean flagStampaDefinitivo = caricaFlagStampaDefinitivoDaListaStampaIva(listaStampaIva);
		
		log.debug(methodName, "FlagStampaProvvisorio: " + flagStampaProvvisorio);
		log.debug(methodName, "FlagStampaDefinitivo: " + flagStampaDefinitivo);
		
//		if((ultimoNumeroProvvisorio != null && ultimoNumeroProvvisorio.intValue() > 1 && Boolean.FALSE.equals(flagStampaProvvisorio))
//				|| (ultimoNumeroDefinitivo != null && ultimoNumeroDefinitivo.intValue() > 1 && Boolean.FALSE.equals(flagStampaDefinitivo))) {
		if((numSubdocIvaStampatiProv>0 && !Boolean.TRUE.equals(flagStampaProvvisorio))
				|| (numSubdocIvaStampatiDef>0 && !Boolean.TRUE.equals(flagStampaDefinitivo))) {
			throw new BusinessException(ErroreFin.NON_E_POSSIBILE_ELABORARE_LA_STAMPA_DEFINITIVA_DELLA_LIQUIDAZIONE_IVA_PER_IL_PERIODO_SELEZIONATO_PROCEDERE_STAMPA_REGISTRI.getErrore());
		}
	}

	/**
	 * Controlla che il registro iva sia gi&agrave; stato stampato nel caso dell'iva immediata.
	 * 
	 * @param registroIva il registro da controllare
	 */
	private void checkStampaDefinitivaImmediata(RegistroIva registroIva) {
		final String methodName = "checkStampaDefinitivaImmediata";
		List<StampaIva> listaStampaIva = caricaListaStampeIva(registroIva, req.getPeriodo());
		
		Boolean flagStampaDefinitivo = caricaFlagStampaDefinitivoDaListaStampaIva(listaStampaIva);
		
		log.debug(methodName, "FlagStampaDefinitivo: " + flagStampaDefinitivo);
		
		if(Boolean.FALSE.equals(flagStampaDefinitivo)) {
			throw new BusinessException(ErroreFin.NON_E_POSSIBILE_ELABORARE_LA_STAMPA_DEFINITIVA_DELLA_LIQUIDAZIONE_IVA_PER_IL_PERIODO_SELEZIONATO_PROCEDERE_STAMPA_REGISTRI.getErrore());
		}
	}
	
	/**
	 * Ottiene la lista delle stampe iva per il registro relative ad un dato periodo.
	 */
	private List<StampaIva> caricaListaStampeIva(RegistroIva registroIva, Periodo periodo) {
		final String methodName = "caricaListaStampeIva";
		StampaIva si = new StampaIva();
		si.setPeriodo(periodo);
		si.setAnnoEsercizio(bilancio.getAnno());
		si.setTipoStampaIva(TipoStampaIva.REGISTRO);
		
		List<StampaIva> result = stampaIvaDad.ricercaStampaIva(si, registroIva);
		log.debug(methodName, "Stampe trovare per registro " + registroIva.getUid() + ", anno " + bilancio.getAnno() + ", periodo " + periodo.getCodice() 
				+ ": " + result.size());
		return result;
	}
	
	/**
	 * Carica il flag di stampa provvisorio dalla lista delle stampa iva.
	 * 
	 * @param list la lista delle stampe
	 * 
	 * @return il valore del flag per la stampa, se presente; <code>false</code> nel caso in cui il flag non esista
	 */
	private Boolean caricaFlagStampaProvvisorioDaListaStampaIva(List<StampaIva> list) {
		final String methodName = "caricaFlagStampaProvvisorioDaListaStampaIva";
		for(StampaIva si : list) {
			if(TipoStampa.DEFINITIVA.equals(si.getTipoStampa()) && Boolean.TRUE.equals(si.getFlagStampaProvvisorio())) {
				log.debug(methodName, "trovata stampa definitiva con flag provvisorio a true, uid: " + si.getUid());
				return Boolean.TRUE; //si.getFlagStampaProvvisorio();
			}
		}
		log.debug(methodName, "Non ho trovato la stampa provvisoria.");
		return Boolean.FALSE;
	}
	
	/**
	 * Carica il flag di stampa definitivo dalla lista delle stampa iva.
	 * 
	 * @param list la lista delle stampe
	 * 
	 * @return il valore del flag per la stampa, se presente; <code>false</code> nel caso in cui il flag non esista
	 */
	private Boolean caricaFlagStampaDefinitivoDaListaStampaIva(List<StampaIva> list) {
		
		final String methodName = "caricaFlagStampaDefinitivoDaListaStampaIva";
		for(StampaIva si : list) {
			if(TipoStampa.DEFINITIVA.equals(si.getTipoStampa()) && Boolean.TRUE.equals(si.getFlagStampaDefinitivo())) {
				log.debug(methodName, "trovata stampa definitiva con flag definitivo a true, uid: " + si.getUid());
				return Boolean.TRUE; //si.getFlagStampaDefinitivo();
			}
		}
		log.debug(methodName, "Non ho trovato la stampa definitiva.");
		return Boolean.FALSE;
	}
	
	

}
