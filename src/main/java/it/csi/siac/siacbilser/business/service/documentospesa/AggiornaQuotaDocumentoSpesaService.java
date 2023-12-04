/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.appjwebsrv.business.Esito;
import it.csi.appjwebsrv.business.EsitoServizio;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.InserisciRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;

/**
 * Aggiorna una quota documento spesa.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaQuotaDocumentoSpesaService extends CrudDocumentoDiSpesaBaseService<AggiornaQuotaDocumentoSpesa, AggiornaQuotaDocumentoSpesaResponse> {
	
	
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	@Autowired
	private LiquidazioneServiceHelper liquidazioneServiceHelper;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdoc = req.getSubdocumentoSpesa();	
		bilancio = req.getBilancio();
		
		checkNotNull(bilancio, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(bilancio.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento spesa"));
		checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento spesa"));
		
		checkNotNull(subdoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(subdoc.getDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento subdocumento spesa"));
		checkCondition(subdoc.getDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento subdocumento spesa"));
		
		checkNotNull(subdoc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subdocumento"));
		checkNotNull(subdoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento"));
		
		checkCondition( subdoc.getProvvisorioCassa() == null ||
				(subdoc.getProvvisorioCassa().getAnno() != null && subdoc.getProvvisorioCassa().getNumero() != null) ||
				(subdoc.getProvvisorioCassa().getAnno() == null && subdoc.getProvvisorioCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
		
		checkCondition(subdoc.getImpegno()==null || subdoc.getImpegno().getUid()== 0 ||
				(subdoc.getImpegno().getAnnoMovimento()!=0 && subdoc.getImpegno().getNumeroBigDecimal()!=null), 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero impegno")); 
		
		checkCondition(subdoc.getAttoAmministrativo()==null || subdoc.getAttoAmministrativo().getUid() != 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid atto amministrativo")); 
		
		checkCondition(subdoc.getLiquidazione()==null || subdoc.getLiquidazione().getUid() != 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid atto amministrativo"));
		checkSospensione(req.isGestisciSospensioni());
	}	
	
	@Override
	protected void init() {
		super.init();
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setEnte(subdoc.getEnte());
		
		documentoSpesaDad.setEnte(subdoc.getEnte());
		
		registroComunicazioniPCCDad.setEnte(subdoc.getEnte());
	}

	@Override
	@Transactional
	public AggiornaQuotaDocumentoSpesaResponse executeService(AggiornaQuotaDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		checkQuotaAggiornabile();
		
		caricaAttoAmministrativo();
		checkAttoAmministrativo();
		
		caricaBilancio();
		
		caricaDettaglioDocumentoAssociato();
		
		caricaImpegnoESubimpegno();	
		checkImpegnoSubImpegno(req.isForzaRicalcolaDisponibilitaLiquidare());
		//SIAC-5526
		checkSiopeAssenzaMotivazione();
		
		caricaProvvisorioDiCassa();
		checkProvvisorio();

		//SIAC-8153
		caricaStrutturaCompetenteQuota();

		impostaFlagOrdinativo();
		
		gestisciNumeroRegistrazioneIva();
		
		subdocumentoSpesaDad.aggiornaAnagraficaSubdocumentoSpesa(subdoc);
		subdocumentoSpesaDad.flushAndClear();
		log.debug(methodName, "anagrafica del subdocumento aggiornata.");
		
		// SIAC-5115
		gestisciSospensioniSubdocumento(req.isGestisciSospensioni());
		
		gestisciInserimentoAggiornamentoLiquidazione();
		
		if(req.isAggiornaStatoDocumento()){
			DocumentoSpesa statoOperativoDocumento = aggiornaStatoOperativoDocumento(subdoc.getDocumento().getUid()).getDocumentoSpesa();
			subdoc.getDocumento().setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			subdoc.getDocumento().setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}
		
		gestisciComunicazioniPCC();
		
		
		res.setSubdocumentoSpesa(subdoc);
	}
	
	private void gestisciInserimentoAggiornamentoLiquidazione() {
		
		liquidazioneServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), bilancio, false);
		Liquidazione liquidazionePrecedente = subdocumentoSpesaDad.findLiquidazioneAssociataAlSubdocumento(subdoc);
		
		if(liquidazionePrecedente == null && !req.isPreventInserimentoLiquidazione() && isLiquidazioneDaInserire()){
			log.debug("gestisciInserimentoAggiornamentoLiquidazione", "liq da inserire");
			if(subdoc.getAttoAmministrativo().getAllegatoAtto()!= null && subdoc.getAttoAmministrativo().getAllegatoAtto().getUid() != 0){
				log.debug("gestisciInserimentoAggiornamentoLiquidazione", "non inserisco una nuova liquidazione perché il provvedimento e' legato ad un allegato atto");
				//add messaggio?
				return;
			}
			log.debug("gestisciInserimentoAggiornamentoLiquidazione", "inserisco una nuova liquidazione");
			checkNumeroRegistrazioneIva();
			
			checkOrdiniEvasi();
			
			Liquidazione liquidazione = liquidazioneServiceHelper.popolaLiquidazione(subdoc);
			liquidazione = liquidazioneServiceHelper.inserisciLiquidazione(liquidazione);
			
			subdoc.setLiquidazione(liquidazione);
			subdocumentoSpesaDad.associaLiquidazione(subdoc, liquidazione);
			
			//SIAC-3977
			subdoc.setFlagConvalidaManuale(getDefaultFlagConvalidaManuale());
			subdocumentoSpesaDad.aggiornaFlagConvalidaManuale(subdoc);
			subdocumentoSpesaDad.aggiornaLiquidazioneTipoConvalidaAStato(subdoc, liquidazione.getStatoOperativoLiquidazione());
			
			
		}else if (liquidazionePrecedente != null && liquidazionePrecedente.getUid() != 0 
				&& StatoOperativoLiquidazione.PROVVISORIO.equals(liquidazionePrecedente.getStatoOperativoLiquidazione())){
			subdoc.setLiquidazione(liquidazionePrecedente);
			liquidazioneServiceHelper.popolaLiquidazioneAggiornamento(subdoc);
			liquidazioneServiceHelper.aggiornaLiquidazioneModulare(subdoc.getLiquidazione());
		}
	}

	private void checkOrdiniEvasi() {
		String methodName = "checkEvasioneOrdini";
		
		//se ci sono degli ordini e la configurazione dell'ente prevede che siano da verificare, controllo che siano evasi.
		boolean gestioneConVerifica = "CON_VERIFICA".equals(ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_EVASIONE_ORDINI));
		boolean ordineValorizzato = documentoAssociato.getOrdini() != null && !documentoAssociato.getOrdini().isEmpty();
		
		log.debug(methodName, "gestioneConVerifica: " +gestioneConVerifica + " ordineValorizzato: "+ordineValorizzato);
		
		if(!ordineValorizzato || !gestioneConVerifica) {
			log.debug(methodName, "verifica ordiniEvasi saltata.");
			return;
		}
		
		EsitoServizio esitoServizio = liquidazioneServiceHelper.checkOrdiniEvasi(subdoc);
		log.logXmlTypeObject(esitoServizio, "ESITO");
		if(esitoServizio.getErrori() != null && !esitoServizio.getErrori().isEmpty()){
			StringBuilder sb = new StringBuilder();
			sb.append("impossibile inserire la liquidazione, la verifica di evasione degli ordini ha riscontrato i seguenti errori: ");
			for(String s  : esitoServizio.getErrori()){
				sb.append(s);
				sb.append(" ");
			}
			String msg = sb.toString();
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
		}
		//nel caso il servizio restituisse KO ma senza errori
		if(Esito.KO.equals(esitoServizio.getEsito())){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile inserire la liquidazione, la verifica di evasione ha restituito esito negativo."));
		}
	}


	private void checkQuotaAggiornabile() {
		
		//TODO
//		1. DA NON SVILUPPARE IN V1 - La quota non deve essere legata ad un 
//		elenco in elaborazione (entità StatoOperativoElaborazioniAsincrone in stato IE), <FIN_ERR_0246, 
//		Quota non aggiornabile perché in elaborazione asincrona.>.
		if(subdoc.getOrdinativo() != null && subdoc.getOrdinativo().getUid() != 0){
			throw new BusinessException(ErroreFin.QUOTA_NON_AGGIORNABILE_PERCHE_CON_ORDINATIVO.getErrore());
		}
		if(Boolean.TRUE.equals(subdoc.getPagatoInCEC())){
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento", "quota già pagata in Cassa Economale"));
		}
//		if(){
//			throw new BusinessException(ErroreFin.QUOTA_NON_AGGIORNABILE_PERCHE_IN_ELABORAZIONE_ASINCRONA.getErrore());
//		}
	}
	
	/**
	 * Controlla che sia presente almeno una comunicazione PCC per la quota del documento. In caso contrario, nessuna nuova comunicazione dovr&agrave; essere effettuata.
	 * 
	 * @return <code>true</code> se esiste almeno una comunicazione PCC per il subdocumento; <code>false</code> altrimenti
	 */
	private boolean hasComunicazionePCC() {
		final String methodName = "hasComunicazionePCC";
		TipoOperazionePCC.Value tipoOperazioneComScadenza = TipoOperazionePCC.Value.ComunicazioneDataScadenza;
		
		Long operazioniPcc = registroComunicazioniPCCDad.countOperazioniPccBySubdocumentoAndCodiceTipoOperazione(subdoc.getUid(), tipoOperazioneComScadenza);
		boolean result = operazioniPcc != null && operazioniPcc.longValue() > 0L;
		log.debug(methodName, "Numero di operazioni di tipo " + tipoOperazioneComScadenza + ": " + operazioniPcc + " - risultato: " + result);
		return result;
	}
	
	/**
	 * Controlla che la comunicazione avvenuta per PCC sia di tipo Contabilizzazione e non abbia stato del debito pari a <code>LIQ</code>: in caso contrario, ho gi&agrave; comunicato la liquidazione.
	 * <br/>
	 * <strong>SIAC-5087</strong>: si deve controllare anche lo stato debito LIQdaSOSP
	 * @return
	 */
	private boolean hasNotComunicazionePccContabilizzazioneLiq() {
		final String methodName = "hasComunicazionePccContabilizzazioneNonLiq";
		TipoOperazionePCC.Value tipoOperazioneContabilizzazione = TipoOperazionePCC.Value.Contabilizzazione;
		StatoDebito.Value statoDebitoLiq = StatoDebito.Value.ImportoLiquidato;
		Long operazioniPcc = registroComunicazioniPCCDad.countOperazioniPccBySubdocumentoAndCodiceTipoOperazioneAndCodiceStatoDebito(subdoc.getUid(), tipoOperazioneContabilizzazione, statoDebitoLiq);
		log.debug(methodName, "Numero di operazioni di tipo " + tipoOperazioneContabilizzazione + " con stato debito " + statoDebitoLiq + ": " + operazioniPcc);
		
		if(operazioniPcc != null && operazioniPcc.longValue() > 0L) {
			log.debug(methodName, "Ho almeno un'operazione di tipo " + tipoOperazioneContabilizzazione + " con stato debito " + statoDebitoLiq + ": non ne inserisco altre");
			return false;
		}
		
		statoDebitoLiq = StatoDebito.Value.CambioStatoDaSospesoALiquidato;
		operazioniPcc = registroComunicazioniPCCDad.countOperazioniPccBySubdocumentoAndCodiceTipoOperazioneAndCodiceStatoDebito(subdoc.getUid(), tipoOperazioneContabilizzazione, statoDebitoLiq);
		log.debug(methodName, "Numero di operazioni di tipo " + tipoOperazioneContabilizzazione + " con stato debito " + statoDebitoLiq + ": " + operazioniPcc);
		
		return operazioniPcc != null && operazioniPcc.longValue() == 0L;
	}
	
	/**
	 * Controlla che la data di riattivazione sia stata valorizzata sul subdocumento e sia stata modificata dalla precedente
	 * @return
	 */
	private boolean isDataRiattivazioneValorizzataEModificata() {
		final String methodName = "isDataRiattivazioneValorizzataEModificata";
		Date dataScadenzaDopoSospensione = subdoc.getDataScadenzaDopoSospensione();
		if(dataScadenzaDopoSospensione == null) {
			log.debug(methodName, "Data di scadenza dopo sospensione non impostata");
			return false;
		}
		
		// Aggiornamento: controllo la ultima data di scadenza impostata
		TipoOperazionePCC.Value tipoOperazioneComScadenza = TipoOperazionePCC.Value.ComunicazioneDataScadenza;
		
		Date lastDataComunicata = registroComunicazioniPCCDad.getUltimaComunicazioneBySubdocumentoAndCodiceTipo(subdoc.getUid(), tipoOperazioneComScadenza);
		
		// Inserisco la comunicazione se non avevo precedentemente la data di sospensione dopo scadenza o se le date sono distinte
		boolean result = lastDataComunicata == null || !lastDataComunicata.equals(dataScadenzaDopoSospensione);
		log.debug(methodName, "Data sospensione dopo scadenza impostata: " + dataScadenzaDopoSospensione + " - precedente data comunicata: " + lastDataComunicata + " - risultato: " + result);
		return result;
	}
	
	/**
	 * Comunicazione della contabilizzazione della liquidazione
	 */
	private void comunicazionePCCContabilizzazioneLiquidazione() {
		final String methodName = "comunicazionePCCContabilizzazioneLiquidazione";
		InserisciRegistroComunicazioniPCC request = new InserisciRegistroComunicazioniPCC();
		
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		registroComunicazioniPCC.setEnte(ente);
		
		TipoOperazionePCC tipoOperazionePCC = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(TipoOperazionePCC.Value.Contabilizzazione /*"CO"*/);
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);
		
		StatoDebito statoDebito = registroComunicazioniPCCDad.findStatoDebitoByValue(StatoDebito.Value.CambioStatoDaSospesoALiquidato); //Codice("LIQdaSOSP");
		registroComunicazioniPCC.setStatoDebito(statoDebito);
		
		// Imposto il minimo dei dati
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(subdoc.getDocumento().getUid());
		registroComunicazioniPCC.setDocumentoSpesa(documentoSpesa);
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(subdoc.getUid());
		subdocumentoSpesa.setDataScadenza(subdoc.getDataScadenza());
		registroComunicazioniPCC.setSubdocumentoSpesa(subdocumentoSpesa);
		
		request.setRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		InserisciRegistroComunicazioniPCCResponse response = serviceExecutor.executeServiceSuccess(InserisciRegistroComunicazioniPCCService.class, request);
		log.info(methodName, "Inserita comunicazione PCC con uid " 
				+ (response != null && response.getRegistroComunicazioniPCC() != null ? response.getRegistroComunicazioniPCC().getUid() : "null")
				+ " per il subdocumento " + subdoc.getUid() + " e il documento " + subdoc.getDocumento().getUid()
				+ " (tipo operazione " + (tipoOperazionePCC!=null?tipoOperazionePCC.getUid() + " - "+ tipoOperazionePCC.getCodice():"null") +  ")");
	}

	/**
	 * Comunicazione dell'aggiornamento della data di scadenza del subdocumento.
	 */
	private void comunicazioneAggiornamentoDataScadenzaPCC() {
		final String methodName = "comunicazioneAggiornamentoDataScadenzaPCC";
		InserisciRegistroComunicazioniPCC request = new InserisciRegistroComunicazioniPCC();
		
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		registroComunicazioniPCC.setEnte(subdoc.getEnte());
		registroComunicazioniPCC.setDataScadenza(subdoc.getDataScadenzaDopoSospensione());
		
		TipoOperazionePCC tipoOperazionePCC = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(TipoOperazionePCC.Value.ComunicazioneDataScadenza /*"CS"*/);
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);
		
		request.setRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		// Imposto il minimo dei dati
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(subdoc.getDocumento().getUid());
		registroComunicazioniPCC.setDocumentoSpesa(documentoSpesa);
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(subdoc.getUid());
		subdocumentoSpesa.setDataScadenza(subdoc.getDataScadenza());
		registroComunicazioniPCC.setSubdocumentoSpesa(subdocumentoSpesa);
		
		InserisciRegistroComunicazioniPCCResponse response = serviceExecutor.executeServiceSuccess(InserisciRegistroComunicazioniPCCService.class, request);
		log.debug(methodName, "Inserita comunicazione PCC con uid " + response.getRegistroComunicazioniPCC().getUid()
				+ " per il subdocumento " + subdoc.getUid() + " e il documento " + subdoc.getDocumento().getUid()
				+ " (tipo operazione " + tipoOperazionePCC.getUid() + "-" + tipoOperazionePCC.getCodice() + ")");
	}
	
	private void gestisciComunicazioniPCC() {
		final String methodName = "gestisciComunicazioniPCC";
		
		boolean hasComunicazionePcc = hasComunicazionePCC();
		if(hasComunicazionePcc && isLiquidazioneDaInserire() && hasNotComunicazionePccContabilizzazioneLiq()) {
			log.debug(methodName, "Comunicazione PCC liquidazione associata");
			comunicazionePCCContabilizzazioneLiquidazione();
		}
		if(hasComunicazionePcc && isDataRiattivazioneValorizzataEModificata()) {
			log.debug(methodName, "Comunicazione PCC data scadenza dopo riattivazione da inserire");
			comunicazioneAggiornamentoDataScadenzaPCC();
		}
	}

	// SIAC-5204
	@Override
	protected void checkDisponibilitaImpegnoSubimpegno(BigDecimal importoPrecedente, boolean ricalcolaDisponibilitaLiquidare) {
		final String methodName = "checkDisponibilitaImpegnoSubimpegno";
		
		Impegno impegno = subdoc.getImpegnoOSubImpegno(); 
		BigDecimal importoNuovo = subdoc.getImportoDaPagare();

		BigDecimal disponibilitaLiquidare = impegno.getDisponibilitaLiquidare() != null ? impegno.getDisponibilitaLiquidare() : BigDecimal.ZERO;
		importoNuovo = importoNuovo!=null? importoNuovo : BigDecimal.ZERO;
		importoPrecedente = importoPrecedente!=null? importoPrecedente : BigDecimal.ZERO;
		
		// SIAC-5204: se ho la carta contabile (sono in regolarizzazione) devo ripristinare il valore attuale nella disponibilita'
		// FIXME: ricercare la carta contabile, ripristinare il valore del dettaglio
		if(req.getCartaContabile() != null && req.getCartaContabile().getUid() != 0) {
			log.info(methodName, "Regolarizzazione della carta contabile " + req.getCartaContabile().getUid() + ": ripristino l'importo della quota sull'impegno");
			disponibilitaLiquidare = disponibilitaLiquidare.add(importoNuovo);
		}
		
		boolean isDisponibilitaSufficiente = disponibilitaLiquidare.add(importoPrecedente).compareTo(importoNuovo)>=0;
		
		log.info(methodName, "disponibilitaLiquidare: " + disponibilitaLiquidare + " + importoPrecedente: " + importoPrecedente 
				+ " >= importoNuovo: " + importoNuovo + " -> isDisponibilitaSufficiente: " + isDisponibilitaSufficiente);
		
		if(!isDisponibilitaSufficiente){
			String key = calcolaChiaveImpegno(subdoc.getImpegno(), subdoc.getSubImpegno());
			throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Inserimento quota documento spesa", impegno.getClass().getSimpleName() + " " + key));
		}
	}

}
