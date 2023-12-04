/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
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
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class StampaRegistroIvaService extends AsyncReportBaseService<StampaRegistroIva, StampaRegistroIvaResponse, StampaRegistroIvaReportHandler> {
	
	//DADs..
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private ContatoreRegistroIvaDad contatoreRegistroIvaDad;
	@Autowired
	protected EnteDad enteDad;
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	@Autowired
	private RegistroIvaDad registroIvaDad;
	@Autowired
	private StampaIvaDad stampaIvaDad;
	
	//Components...
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	//Fields..T
	private Bilancio bilancio;
	private Ente ente;
	private Integer primaPaginaDaStampare;
	private RegistroIva registroIva;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRegistroIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva"));
		checkCondition(req.getRegistroIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid registro iva"));
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente registro iva"));
		checkCondition(req.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente registro iva"));
		
		checkNotNull(req.getPeriodo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo"));
		
		checkNotNull(req.getTipoStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo stampa"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		checkNotNull(req.getRegistroIva().getGruppoAttivitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo attivita iva registro iva"));
		checkCondition(req.getRegistroIva().getGruppoAttivitaIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid gruppo attivita iva registro iva"));
		checkNotNull(req.getRegistroIva().getGruppoAttivitaIva().getTipoChiusura(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo chiusura gruppo attivita iva registro iva"));
		
		checkNotNull(req.getDocumentiPagati(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag documenti pagati"));
		checkNotNull(req.getDocumentiIncassati(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag documenti incassati"));
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public StampaRegistroIvaResponse executeService(StampaRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected Class<StampaRegistroIvaReportHandler> getReportHandlerClass() {	
		StampaRegistroIvaReportHandlers srirh = StampaRegistroIvaReportHandlers.fromTipoRegistroIvaAndPagatoAndIncassato(registroIva.getTipoRegistroIva(),
				req.getDocumentiPagati(), req.getDocumentiIncassati());
		return srirh.getReportHandlerClass();
	}	
	
	@Override
	protected void initReportHandler() {
		reportHandler.setBilancio(bilancio);
		reportHandler.setDocumentiIncassati(req.getDocumentiIncassati());
		reportHandler.setDocumentiPagati(req.getDocumentiPagati());
		reportHandler.setEnte(ente);
		reportHandler.setGruppoAttivitaIva(registroIva.getGruppoAttivitaIva());
		reportHandler.setPeriodo(req.getPeriodo());
		reportHandler.setPrimaPaginaDaStampare(primaPaginaDaStampare);
		reportHandler.setRegistroIva(registroIva);
		reportHandler.setTipoChiusura(registroIva.getGruppoAttivitaIva().getTipoChiusura());
		reportHandler.setTipoStampa(req.getTipoStampa());
	}

	@Override
	protected void preStartElaboration() {
		// Caricamento dati ente
		caricaDettaglioEnte();
	
		// Controllo se il caso d'uso sia applicabile
		caricaDettaglioBilancio();
		
		// Ottengo i dati del registro
		caricaDettaglioRegistro();
		
		// Caricamento dati gruppo
		
		// Controlli logici sulla stampa
		checkStampaDefinitivaGiaEffettuata();
		checkStampaDefinitivaPeriodiPrecedentiEffetuata();
		
		// Controlli logici sul periodo
		checkPeriodoCoerenteConTipoChiusura();
	}
	
		
	@Override
	protected void startElaboration() {
		String methodName = "startElaboration";
		try{
			elaborazioniManager.startElaborazione(StampaRegistroIvaService.class, "registroIva.uid:"+req.getRegistroIva().getUid());
		} catch (ElaborazioneAttivaException eae){
			String msg = "Esista gia' una stampa in corso per lo stesso registro iva [uid: "+ req.getRegistroIva().getUid()+"]. Attendere il termine di tale stampa";
			log.error(methodName, msg, eae);
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
		
		super.startElaboration();
	}
	
	@Override
	protected void init() {
		ente = req.getEnte();
		
		bilancioDad.setEnteEntity(ente);
		contatoreRegistroIvaDad.setEnte(ente);
		registroIvaDad.setEnte(ente);
		stampaIvaDad.setEnte(ente);
		
		elaborazioniManager.init(ente, req.getRichiedente().getOperatore().getCodiceFiscale());
	}


	private void checkPeriodoCoerenteConTipoChiusura() {
		final String methodName = "checkPeriodoCoerenteConTipoChiusura";
		log.debug(methodName, "Tipo chiusura del GruppoAttivitaIva: " + registroIva.getGruppoAttivitaIva().getTipoChiusura());
		log.debug(methodName, "Periodo selezionato: " + req.getPeriodo());
		
		if(registroIva.getGruppoAttivitaIva().getTipoChiusura() == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.
					getErrore(String.format("Nessun tipo di chiusura collegato al gruppo attivita' iva %s - %s (registro iva: %s)", 
							registroIva.getGruppoAttivitaIva().getCodice(), 
							registroIva.getGruppoAttivitaIva().getDescrizione(),
							registroIva.getCodice())));
		}
		
		if(!registroIva.getGruppoAttivitaIva().getTipoChiusura().getPeriodi().contains(req.getPeriodo())){
			throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("periodo", "per il tipo di chiusura selezionato"));
		}
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
	 * Ottiene i dati di dettaglio del registro.
	 */
	private void caricaDettaglioRegistro() {
		final String methodName = "caricaDettaglioRegistro";
		final Integer uid = req.getRegistroIva().getUid();
		log.debug(methodName, "Caricamento dati registro per uid " + uid);
		registroIva = registroIvaDad.findRegistroIvaByIdGruppoBase(uid);
		if(registroIva == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Registro Iva", "con uid:" + uid));
		}
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

	/**
	 * Non &egrave; possibile stampare in modalit&agrave; &ldquo;Definitiva&rdquo; un registro gi&agrave; stampato in definitivo per il periodo selezionato.
	 * <br>
	 * Per fare questa verifica viene richiamato il metodo Ricerca Contatore Registro Iva 
	 * del Servizio Gestione Registro Iva.
	 * 
	 */
	private void checkStampaDefinitivaGiaEffettuata() {
		final String methodName = "checkStampaDefinitivaGiaEffettuata";
		if(TipoStampa.BOZZA.equals(req.getTipoStampa())) {
			// Se la stampa e' in bozza, allora va tutto bene
			log.debug(methodName, "Tipo stampa pari a BOZZA: posso ristampare");
			return;
		}
		
		List<StampaIva> listaStampaIva = caricaListaStampeIva(req.getPeriodo());
		
		Boolean flagStampaProvvisorio = stampaIvaDefinitivaConFlagProvvGiaPresente(listaStampaIva);
		Boolean flagStampaDefinitivo = stampaIvaDefinitivaConFlagDefGiaPresente(listaStampaIva);
		
		log.debug(methodName, "FlagStampaProvvisorio: " + flagStampaProvvisorio);
		log.debug(methodName, "FlagStampaDefinitivo: " + flagStampaDefinitivo);
		
		boolean provvisorio = isControlloDaEffettuareSuiProvvisorii();
		log.debug(methodName, "provvisorio: " + provvisorio);
		
		// A. Se Tipo Registro Iva = “ACQUISTI IVA ESIGIBILITA’ DIFFERITA” e il parametro “documenti pagati” = 0
		// oppure se Tipo Registro Iva = “VENDITE IVA ESIGIBILITA’ DIFFERITA” e il parametro “documenti incassati” = 0,
		// allora si controlla solo il “flagStampaProvv”
		// B. In tutti gli altri casi si controlla solo il “flagStampaDef”
		if((provvisorio && Boolean.TRUE.equals(flagStampaProvvisorio)) || (!provvisorio && Boolean.TRUE.equals(flagStampaDefinitivo))) {
			log.debug(methodName, "Stampa in definitivo gia' effettuata per il registro " + registroIva.getUid());
			throw new BusinessException(ErroreFin.NON_E_POSSIBILE_STAMPARE_UN_REGISTRO_GIA_STAMPATO_IN_DEFINITIVO_PER_IL_PERIODO_SELEZIONATO.getErrore(registroIva.getCodice()));
		}
	}

	/**
	 * Per effettuare la stampa di tipo &ldquo;Definitiva&rdquo; di un registro di un certo periodo
	 * &egrave; necessario che tutti i periodi antecedenti a quello che si vuole stampare siano gi&agrave; stati stampati tutti in definitivo.
	 */
	private void checkStampaDefinitivaPeriodiPrecedentiEffetuata() {
		final String methodName = "checkStampaDefinitivaPeriodiPrecedentiEffetuata";
		if(TipoStampa.BOZZA.equals(req.getTipoStampa()) || Integer.valueOf(1).equals(req.getPeriodo().getOrdinalePeriodo())) {
			// Se la stampa e' in bozza, allora va tutto bene
			// Se il periodo e' 1 (ANNUALE, GENNAIO, GENNAIO-MARZO, GENNAIO-APRILE, GENNAIO-GIUGNO), va tutto bene
			log.debug(methodName, "Tipo stampa pari a BOZZA o primo periodo: non controllo i periodi precedenti");
			
			// Imposto la prima pagina da stampare
			primaPaginaDaStampare = Integer.valueOf(1);
			log.debug(methodName, "Prima pagina per la stampa: " + primaPaginaDaStampare);
			return;
		}
		
		List<StampaIva> listaStampaIva = caricaListaStampeIva(req.getPeriodo().precedente());
		log.debug(methodName, "codice periodo precedente: " + req.getPeriodo().precedente().getCodice());
		for(StampaIva s : listaStampaIva){
			log.debug(methodName, "uid stampa: " + s.getUid());
		}
		
		Boolean flagStampaProvvisorio = stampaIvaDefinitivaConFlagProvvGiaPresente(listaStampaIva);
		Boolean flagStampaDefinitivo = stampaIvaDefinitivaConFlagDefGiaPresente(listaStampaIva);
		
		log.debug(methodName, "FlagStampaProvvisorio: " + flagStampaProvvisorio);
		log.debug(methodName, "FlagStampaDefinitivo: " + flagStampaDefinitivo);
		
		boolean provvisorio = isControlloDaEffettuareSuiProvvisorii();
		log.debug(methodName, "provvisorio: " + provvisorio);
		
		if((provvisorio && Boolean.FALSE.equals(flagStampaProvvisorio)) || (!provvisorio && Boolean.FALSE.equals(flagStampaDefinitivo))) {
			log.debug(methodName, "Periodo precedente ad " + req.getPeriodo().getCodice() + " / " + bilancio.getAnno() +
					" non ancora stampato per il registro " + registroIva.getUid());
			throw new BusinessException(ErroreFin.NON_E_POSSIBILE_STAMPARE_IL_REGISTRO_IN_DEFINITIVO_PER_IL_PERIODO_SELEZIONATO.getErrore(registroIva.getCodice()));
		}
		
		Integer ultimaPaginaStampata = isControlloDaEffettuareSuiProvvisorii()
				? caricaUltimaPaginaStampataProvvisorioDaListaStampaIva(listaStampaIva)
				: caricaUltimaPaginaStampataDefinitivoDaListaStampaIva(listaStampaIva);
		// Imposto la prima pagina da stampare
		primaPaginaDaStampare = Integer.valueOf(ultimaPaginaStampata.intValue() + 1);
		log.debug(methodName, "Prima pagina per la stampa: " + primaPaginaDaStampare);
	}
	
	/**
	 * Descrive se il controllo sia da effettuare sui dati provvisor&icirc; rispetto ai dati definitivi.
	 * 
	 * @return <code>true</code> se siano da controllare i provvisor&icirc;; <code>false</code> in caso contrario
	 */
	private boolean isControlloDaEffettuareSuiProvvisorii() {
		return (TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.equals(registroIva.getTipoRegistroIva()) && Boolean.FALSE.equals(req.getDocumentiPagati()))
				|| (TipoRegistroIva.VENDITE_IVA_DIFFERITA.equals(registroIva.getTipoRegistroIva()) && Boolean.FALSE.equals(req.getDocumentiIncassati()));
	}
	
	/**
	 * Ottiene la lista delle stampe iva per il registro relative ad un dato periodo.
	 */
	private List<StampaIva> caricaListaStampeIva(Periodo periodo) {
		final String methodName = "caricaListaStampeIva";
		StampaIva si = new StampaIva();
		si.setPeriodo(periodo);
		si.setAnnoEsercizio(bilancio.getAnno());
		//si.setListaRegistroIva(Arrays.asList(registroIva));
		si.setTipoStampaIva(TipoStampaIva.REGISTRO);
		
		List<StampaIva> result = stampaIvaDad.ricercaStampaIva(si, registroIva);
		log.debug(methodName, "Stampe trovare per registro " + registroIva.getUid() + ", anno " + bilancio.getAnno() + ", periodo " + periodo.getCodice() 
				+ ": " + result.size());
		return result;
	}
	
	/**
	 * Controlla se nella lista delle stampa iva ne esista una definitiva con flagStampaPrvvisorio con valore true.
	 * 
	 * @param list la lista delle stampe
	 * 
	 * @return  <code>true</code> se esiste una stampa iva di tipo definitivo con flagStampaPrvvisorio true
	 * 			<code>false</code> altrimenti
	 */
	private Boolean stampaIvaDefinitivaConFlagProvvGiaPresente(List<StampaIva> list) {
		final String methodName = "stampaIvaDefinitivaConFlagProvvGiaPresente";
		Boolean flag = Boolean.FALSE;
		for(StampaIva si : list) {
			if( TipoStampa.DEFINITIVA.equals(si.getTipoStampa()) && Boolean.TRUE.equals(si.getFlagStampaProvvisorio())) {
				log.debug(methodName, "trovata stampa definitiva con flag provvisorio a true, uid: " + si.getUid());
				flag = Boolean.TRUE;
			}
		}
		log.debug(methodName, "returning: " + flag);
		return flag;
	}
	
	/**
	 * Controlla se nella lista delle stampa iva ne esista una definitiva con flagStampaDefinitivo con valore true.
	 * 
	 * @param list la lista delle stampe
	 * 
	 * @return  <code>true</code> se esiste una stampa iva di tipo definitivo con flagStampaDefinitivo true
	 * 			<code>false</code> altrimenti
	 */
	private Boolean stampaIvaDefinitivaConFlagDefGiaPresente(List<StampaIva> list) {
		final String methodName = "caricaFlagStampaDefinitivoDaListaStampaIva";
		Boolean flag = Boolean.FALSE;
		for(StampaIva si : list) {
			if(TipoStampa.DEFINITIVA.equals(si.getTipoStampa()) && Boolean.TRUE.equals(si.getFlagStampaDefinitivo()) ) {
				log.debug(methodName, "trovata stampa definitiva con flag definitivo a true, uid: " + si.getUid());
				flag = Boolean.TRUE;
			}
		}
		log.debug(methodName, "returning: " + flag);
		return flag;
	}
	
	/**
	 * Carica l'ultima pagina stampata in provvisorio dalla lista della stampa iva.
	 * 
	 * @param list la lista delle stampe
	 * 
	 * @return il valore della pagina , se presente; <code>0</code> nel caso in cui la pagina non esista
	 */
	private Integer caricaUltimaPaginaStampataProvvisorioDaListaStampaIva(List<StampaIva> listaStampaIva) {
		Integer numPaginaProvv = Integer.valueOf(0);
		for(StampaIva si : listaStampaIva) {
			if(TipoStampa.DEFINITIVA.equals(si.getTipoStampa()) && Boolean.TRUE.equals(si.getFlagStampaProvvisorio())) {
				numPaginaProvv = si.getUltimaPaginaStampaProvvisoria();
			}
		}
		log.debug("caricaUltimaPaginaStampataProvvisorioDaListaStampaIva", "ultima pagina prvvisorio: " + numPaginaProvv);
		return numPaginaProvv;
	}
	
	/**
	 * Carica l'ultima pagina stampata in definitivo dalla lista della stampa iva.
	 * 
	 * @param list la lista delle stampe
	 * 
	 * @return il valore della pagina , se presente; <code>0</code> nel caso in cui la pagina non esista
	 */
	private Integer caricaUltimaPaginaStampataDefinitivoDaListaStampaIva(List<StampaIva> listaStampaIva) {
		Integer numPaginaDef = Integer.valueOf(0);
		for(StampaIva si : listaStampaIva) {
			if(TipoStampa.DEFINITIVA.equals(si.getTipoStampa()) && Boolean.TRUE.equals(si.getFlagStampaDefinitivo())) {
				numPaginaDef = si.getUltimaPaginaStampaDefinitiva();
			}
		}
		log.debug("caricaUltimaPaginaStampataDefinitivoDaListaStampaIva", "ultima pagina definitivo: " + numPaginaDef);
		return numPaginaDef;
	}

	@Override
	protected void postStartElaboration() {
		final String methodName = "postStartElaboration";
		log.info(methodName, "post start elaborazione!");
	}

}
