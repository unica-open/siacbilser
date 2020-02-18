/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.InserisciRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * Inserimento di una quota di un Documento di Spesa.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceQuotaDocumentoSpesaService extends CrudDocumentoDiSpesaBaseService<InserisceQuotaDocumentoSpesa, InserisceQuotaDocumentoSpesaResponse> {
	
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	@Autowired
	private LiquidazioneServiceHelper liquidazioneServiceHelper;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdoc = req.getSubdocumentoSpesa();
		bilancio = req.getBilancio();
		
		checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento spesa"));
		
		checkNotNull(bilancio, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(bilancio.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		checkNotNull(subdoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(subdoc.getDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento subdocumento spesa"));
		checkCondition(subdoc.getDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento subdocumento spesa"));
		
		//checkNotNull(subdoc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subdocumento spesa")); //staccato in automatico
		checkNotNull(subdoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento spesa"));
		
		checkCondition(subdoc.getProvvisorioCassa() == null ||  
				(subdoc.getProvvisorioCassa().getAnno() != null && subdoc.getProvvisorioCassa().getNumero() != null) ||
				(subdoc.getProvvisorioCassa().getAnno() == null && subdoc.getProvvisorioCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
		
		checkCondition(subdoc.getImpegno()==null || subdoc.getImpegno().getUid()== 0 ||
				(subdoc.getImpegno().getAnnoMovimento()!=0 && subdoc.getImpegno().getNumero()!=null), 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero impegno")); 
		
		checkCondition(subdoc.getAttoAmministrativo()==null || subdoc.getAttoAmministrativo().getUid() != 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid atto amministrativo")); 
		
		if(!req.isQuotaContestuale() && !req.isSenzaModalitaPagamento()){
			checkNotNull(subdoc.getModalitaPagamentoSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modalita' pagamento subdocumento spesa"));
			checkCondition(subdoc.getModalitaPagamentoSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid modalita' pagamento subdocumento spesa"));
		}
		
		if(subdoc.getNoteTesoriere()!=null) {
			checkEntita(subdoc.getNoteTesoriere(), "note tesoriere");
		}
		checkSospensione(req.isGestisciSospensioni());
	}	
	
	@Override
	protected void init() {
		super.init();
		subdocumentoSpesaDad.setEnte(subdoc.getEnte());
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
		documentoSpesaDad.setEnte(subdoc.getEnte());
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		
		registroComunicazioniPCCDad.setEnte(subdoc.getEnte());
	}
	
	@Override
	@Transactional
	public InserisceQuotaDocumentoSpesaResponse executeService(InserisceQuotaDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		
		if(!req.isQuotaContestuale()){
			caricaAttoAmministrativo();
			checkAttoAmministrativo();
			
			caricaBilancio();
			
			caricaDettaglioDocumentoAssociato();
			
			popolaDefaultCausaleOrdinativo();
			
			caricaImpegnoESubimpegno();	
			checkImpegnoSubImpegno(req.isForzaRicalcolaDisponibilitaLiquidare());
			checkSiopeAssenzaMotivazione();
			
			caricaProvvisorioDiCassa();
			checkProvvisorio();
			
			impostaFlagOrdinativo();
		}
		
		Integer numeroSubdocumento = subdocumentoSpesaDad.staccaNumeroSubdocumento(subdoc.getDocumento().getUid());
		subdoc.setNumero(numeroSubdocumento);
		gestisciNumeroRegistrazioneIva();
		
		subdocumentoSpesaDad.inserisciAnagraficaSubdocumentoSpesa(subdoc);
		
		// SIAC-5115
		gestisciSospensioniSubdocumento(req.isGestisciSospensioni());
		
		
		// Mantengo il valore della eventuale liquidazione presente
		boolean hasLiquidazioneValorizzata = subdoc.getLiquidazione() != null && subdoc.getLiquidazione().getUid() != 0;
		if(!req.isQuotaContestuale()) {
			gestisciInserimentoLiquidazioneAutomatica();
		}
		
		if(!req.isQuotaContestuale() && req.isAggiornaStatoDocumento()) {
			AggiornaStatoDocumentoDiSpesaResponse resASDDS = aggiornaStatoOperativoDocumento(subdoc.getDocumento().getUid());
			DocumentoSpesa documentoConStatoAggiornato = resASDDS.getDocumentoSpesa();
			
			subdoc.getDocumento().setStatoOperativoDocumento(documentoConStatoAggiornato.getStatoOperativoDocumento());
			subdoc.getDocumento().setDataInizioValiditaStato(documentoConStatoAggiornato.getDataInizioValiditaStato());
		}
		
		boolean hasComunicazionePcc = hasComunicazionePCC();
		
		// I dati della liquidazione possono essere inseriti solo con quota non contestuale e solo se la liquidazione non e' gia' stata precedentemente inserita
		if(hasComunicazionePcc && !req.isQuotaContestuale() && !hasLiquidazioneValorizzata && isLiquidazioneDaInserire() && hasNotComunicazionePccContabilizzazioneLiq()) {
			log.debug(methodName, "Comunicazione PCC liquidazione associata");
			comunicazionePCCContabilizzazioneLiquidazione();
		}
		if(hasComunicazionePcc && isDataRiattivazioneValorizzataEModificata()) {
			log.debug(methodName, "Comunicazione PCC data scadenza dopo riattivazione da inserire");
			comunicazionePCCAggiornamentoDataScadenzaPCC();
		}
		
		res.setSubdocumentoSpesa(subdoc);
		
	}

	private void popolaDefaultCausaleOrdinativo() {
		String methodName = "popolaDefaultCausaleOrdinativo";
		if(StringUtils.isBlank(subdoc.getCausaleOrdinativo())){
			String causaleOrdinativo = new StringBuilder().append("DOCUMENTO N. ")
			   .append(documentoAssociato.getNumero())
			   .append(" DEL ")
			   .append(new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY).format(documentoAssociato.getDataEmissione()))
			   .toString();
			log.debug(methodName, "causale di default:" + causaleOrdinativo);
			subdoc.setCausaleOrdinativo(causaleOrdinativo);
		}
	}
	
	/**
	 * Controlla che la comunicazione avvenuta per PCC sia di tipo Contabilizzazione e non abbia stato del debito pari a <code>LIQ</code>: in caso contrario, ho gi&agrave; comunicato la liquidazione.
	 * @return
	 */
	private boolean hasNotComunicazionePccContabilizzazioneLiq() {
		final String methodName = "hasComunicazionePccContabilizzazioneNonLiq";
		TipoOperazionePCC.Value tipoOperazioneContabilizzazione = TipoOperazionePCC.Value.Contabilizzazione;
		StatoDebito.Value statoDebitoLiq = StatoDebito.Value.ImportoLiquidato;
		Long operazioniPcc = registroComunicazioniPCCDad.countOperazioniPccBySubdocumentoAndCodiceTipoOperazioneAndCodiceStatoDebito(subdoc.getUid(), tipoOperazioneContabilizzazione, statoDebitoLiq);
		boolean result = operazioniPcc != null && operazioniPcc.longValue() == 0L;
		log.debug(methodName, "Numero di operazioni di tipo " + tipoOperazioneContabilizzazione + " con stato debito " + statoDebitoLiq + ": " + operazioniPcc + " - risultato: " + result);
		return result;
	}

	/**
	 * Controlla che sia presente almeno una comunicazione PCC per la quota del documento. In caso contrario, nessuna nuova comunicazione dovr&agrave; essere effettuata.
	 * 
	 * @return <code>true</code> se esiste almeno una comunicazione PCC per il subdocumento; <code>false</code> altrimenti
	 */
	private boolean hasComunicazionePCC() {
		final String methodName = "hasComunicazionePCC";
		TipoOperazionePCC.Value tipoOperazioneDataScadenza = TipoOperazionePCC.Value.ComunicazioneDataScadenza;
		Long operazioniPcc = registroComunicazioniPCCDad.countOperazioniPccBySubdocumentoAndCodiceTipoOperazione(subdoc.getUid(), tipoOperazioneDataScadenza);
		boolean result = operazioniPcc != null && operazioniPcc.longValue() > 0L;
		log.debug(methodName, "Numero di operazioni di tipo " + tipoOperazioneDataScadenza + ": " + operazioniPcc + " - risultato: " + result);
		return result;
	}

	/**
	 * Controlla che la data di riattivazione sia stata valorizzata sul subdocumento e sia stata modificata dalla precedente
	 * @return
	 */
	private boolean isDataRiattivazioneValorizzataEModificata() {
		final String methodName = "isDataRiattivazioneValorizzataEModificata";
		Date dataRiattivazione = subdoc.getDataScadenzaDopoSospensione();
		// Inserimento: non ho altre date da controllare
		boolean result = dataRiattivazione != null;
		log.debug(methodName, "Data di riattivazione: " + dataRiattivazione + " - risultato: " + result);
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
		
		StatoDebito statoDebito = registroComunicazioniPCCDad.findStatoDebitoByValue(StatoDebito.Value.CambioStatoDaSospesoALiquidato);//Codice("LIQdaSOSP");
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
	private void comunicazionePCCAggiornamentoDataScadenzaPCC() {
		final String methodName = "comunicazioneAggiornamentoDataScadenzaPCC";
		InserisciRegistroComunicazioniPCC request = new InserisciRegistroComunicazioniPCC();
		
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		registroComunicazioniPCC.setEnte(ente);
		registroComunicazioniPCC.setDataScadenza(subdoc.getDataScadenzaDopoSospensione());
		
		TipoOperazionePCC tipoOperazionePCC = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(TipoOperazionePCC.Value.ComunicazioneDataScadenza /*"CS"*/);
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);
		
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
		log.debug(methodName, "Inserita comunicazione PCC con uid " + response.getRegistroComunicazioniPCC().getUid()
				+ " per il subdocumento " + subdoc.getUid() + " e il documento " + subdoc.getDocumento().getUid()
				+ " (tipo operazione " + tipoOperazionePCC.getUid() + "-" + tipoOperazionePCC.getCodice() + ")");
	}

	private void gestisciInserimentoLiquidazioneAutomatica() {
		if(!req.isPreventInserimentoLiquidazione() && isLiquidazioneDaInserire() && subdoc.getLiquidazione() == null) {
			liquidazioneServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), bilancio, false);
			checkNumeroRegistrazioneIva();
			Liquidazione liquidazione = liquidazioneServiceHelper.popolaLiquidazione(subdoc);
			liquidazione = liquidazioneServiceHelper.inserisciLiquidazione(liquidazione);
			subdoc.setLiquidazione(liquidazione);
			subdocumentoSpesaDad.associaLiquidazione(subdoc, liquidazione);
		}
	}

	
}
