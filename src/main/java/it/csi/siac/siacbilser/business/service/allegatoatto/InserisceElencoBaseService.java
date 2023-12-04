/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.AccertamentoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public abstract class InserisceElencoBaseService extends CheckedAccountBaseService<InserisceElenco,InserisceElencoResponse> {
	
	@Autowired
	protected ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	protected AllegatoAttoDad allegatoAttoDad;
	
	@Autowired
	protected SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	protected SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private CodificaDad codificaDad;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	
	@Autowired
	private SoggettoService soggettoService;
	@Autowired
	private LiquidazioneService liquidazioneService;
	
	//SIAC-7470
	@Autowired
	private ImpegnoOttimizzatoDad impegnoOttimizzatoDad;
	@Autowired
	private AccertamentoOttimizzatoDad accertamentoOttimizzatoDad;
	
	protected ElencoDocumentiAllegato elencoDocumentiAllegato;
	protected Bilancio bilancio;
	
	// XXX: utilizzo di una mappa in quanto l'equals del documento spesa e' l'equals nativo (identita' del riferimento). Devo avere un'unica istanza per uid
	private Map<Integer, DocumentoSpesa> documentiPerCuiAggiornareStato = new HashMap<Integer, DocumentoSpesa>();

	@Override
	protected void init() {
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
		elencoDocumentiAllegatoDad.setEnte(elencoDocumentiAllegato.getEnte());
		
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		allegatoAttoDad.setEnte(elencoDocumentiAllegato.getEnte());
		
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setEnte(elencoDocumentiAllegato.getEnte());
		
		soggettoDad.setEnte(ente);
	}
	
	/**
	 * Ottiene i dati dell bilancio il cui uid e' passato come parametro.
	 *
	 * @param uid the uid
	 * @return the bilancio
	 */
	protected Bilancio caricaBilancio(Integer uid) {
		final String methodName = "caricaBilancio";
		
		Bilancio bil = bilancioDad.getBilancioByUid(uid);
		
		if(bil == null) {
			log.error(methodName, "Nessun bilancio con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("bilancio", "uid: "+ uid));
		}
		return bil;
	}
	
	protected AllegatoAtto caricaAllegatoAtto(Integer uid) {
		final String methodName = "caricaAllegatoAtto";
		
		AllegatoAtto allegatoAtto = allegatoAttoDad.findAllegatoAttoById(uid);
		
		if(allegatoAtto == null) {
			log.error(methodName, "Nessun allegato atto con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("allegato atto", "uid: "+ uid));
		}
		return allegatoAtto;
	}
	
	/**
	 * 
	 */
	protected void staccaNumeroElenco() {
		Integer numeroElenco = elencoDocumentiAllegatoDad.staccaNumeroElenco(bilancio.getUid());
		elencoDocumentiAllegato.setNumero(numeroElenco);
	}

	/**
	 * Se l'atto &eacute; in stato completato o parzialmente convalidato, per ogni quota di documento di spesa passata in input ancora da associare all'atto,
	 * sia essa gi&agrave; presente in archivio o sia essa una quota da inserire come indicato nei passi precedenti, purch&eacute; non ancora
	 * collegata a liquidazione e con importoDaPagare > 0 (si escludono con questa seconda condizione le quote collegate a note di credito)
	 * &eacute; necessario inserire una liquidazione con questi attributi:
	 * <ul>
	 *     <li>in stato 'PROVVISORIO',</li>
	 *     <li>con la descrizione uguale al campo causale 'ordinativo di pagamento' di ogni singola quota di spesa</li>
	 *     <li>l'importo pari all'importoDaPagare della quota stessa,</li>
	 *     <li>gli altri dati della liquidazione (ad es. l'impegno o cup e cig) sono ricavati dalla quota documento di spesa a cui &eacute; legata.</li>
	 * </ul>
	 * 
	 * @param subdocumentiSpesa
	 * @param allegato 
	 */
	protected void valutaInserimentoLiquidazioni(List<SubdocumentoSpesa> subdocumentiSpesa, AllegatoAtto allegato) {
		for(SubdocumentoSpesa ss : subdocumentiSpesa) {
			SubdocumentoSpesa subdocumentoSpesa = subdocumentoSpesaDad.findSubdocumentoSpesaById(ss.getUid());
			if(isLiquidazioneDaInserire(subdocumentoSpesa)) {
				Liquidazione liquidazione = popolaLiquidazione(subdocumentoSpesa, allegato);
				liquidazione = inserisciLiquidazione(liquidazione);
				subdocumentoSpesaDad.associaLiquidazione(subdocumentoSpesa, liquidazione);
				ss.setLiquidazione(liquidazione);
				documentiPerCuiAggiornareStato.put(subdocumentoSpesa.getDocumento().getUid(), subdocumentoSpesa.getDocumento());
			}
		}
	}

	private boolean isLiquidazioneDaInserire(SubdocumentoSpesa ss) {
		final String methodName = "isLiquidazioneDaInserire";
		Liquidazione liquidazione = subdocumentoSpesaDad.findLiquidazioneAssociataAlSubdocumento(ss);
		BigDecimal importoDaPagare = ss.getImportoDaPagare();
		Boolean isCollegataANotaCredito = subdocumentoSpesaDad.isCollegataANotaCredito(ss);
		log.debug(methodName, "SubdocumentoSpesa " + ss.getUid() + " - Liquidazione presente? " + (liquidazione == null)
				+ " - Importo da pagare > 0? " + (importoDaPagare.signum() > 0)
				+ " - Collegato a nota credito? " + isCollegataANotaCredito.booleanValue());
		return liquidazione == null && (isCollegataANotaCredito.booleanValue() || importoDaPagare.signum() > 0);
	}

	private Liquidazione inserisciLiquidazione(Liquidazione liquidazione) {
		InserisceLiquidazione request = new InserisceLiquidazione();
		request.setAnnoEsercizio(bilancio.getAnno() + "");
		request.setBilancio(bilancio);
		request.setDataOra(new Date());
		request.setEnte(ente);
		request.setLiquidazione(liquidazione);
		request.setRichiedente(req.getRichiedente());
		
		log.logXmlTypeObject(request, "Request per InserisceLiquidazione.");
		InserisceLiquidazioneResponse response = liquidazioneService.inserisceLiquidazione(request);
		log.logXmlTypeObject(response, "Risposta ottenuta dal servizio InserisceLiquidazione.");
		checkServiceResponseFallimento(response);
		return response.getLiquidazione();
	}

	private Liquidazione popolaLiquidazione(SubdocumentoSpesa subdoc, AllegatoAtto allegato) {
		Liquidazione l = new Liquidazione();
		l.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
		l.setDescrizioneLiquidazione(StringUtils.isNotBlank(subdoc.getCausaleOrdinativo())?subdoc.getCausaleOrdinativo():"");
		l.setImportoAttualeLiquidazione(subdoc.getImportoDaPagare());
		//TODO controllare
		l.setImportoLiquidazione(subdoc.getImportoDaPagare());
		l.setAttoAmministrativoLiquidazione(allegato.getAttoAmministrativo());
		
		//TODO SE ModalitaPagamentoSoggetto.tipoAccredito = CSC (cessione di credito) ricercare la Modalita' 
		//di Pagamento e il Soggetto collegati attraverso la relazione "Cessione Credito ad altro Soggetto"
		//ALTRIMENTi si ricopia dal subdoc
		Soggetto soggettoLiquidazione = creaSoggettoLiquidazione(subdoc);
		l.setSoggettoLiquidazione(soggettoLiquidazione);
		gestisciCessioneDelCredito(l);
		
		//TODO controllare
		Impegno impegno = subdoc.getImpegno();
		SubImpegno subImpegno = subdoc.getSubImpegno();
		impegnoBilDad.aggiungiCodiciClassificatoriAImpegnoEOSubImpegno(impegno,subImpegno);
		
		if(subImpegno != null){
			impostaCodiciNellaLiquidazione(subImpegno, l);
		}else{
			impostaCodiciNellaLiquidazione(impegno, l);
		}
		
		l.setImpegno(impegno);
		l.setSubImpegno(subImpegno);
		l.setCup(subdoc.getCup());
		l.setCig(subdoc.getCig());
		
		if(subdoc.getContoTesoreria() != null){
			ContoTesoreria ct = new ContoTesoreria();
			ct.setUid(subdoc.getContoTesoreria().getUid());
			ct.setCodice(subdoc.getContoTesoreria().getCodice());
			ct.setDescrizione(subdoc.getContoTesoreria().getDescrizione());
			l.setContoTesoreria(ct);
		}
		
		l.setForza(true);
		
		
		//popolo la quota con dati minimi per il documento padre
		SubdocumentoSpesa quota = new SubdocumentoSpesa();
		quota.setUid(subdoc.getUid());
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(subdoc.getDocumento().getUid());
		doc.setTipoDocumento(subdoc.getDocumento().getTipoDocumento());
		doc.setContabilizzaGenPcc(subdoc.getDocumento().getContabilizzaGenPcc());
		quota.setDocumento(doc);
		l.setSubdocumentoSpesa(quota);
		
		// SIAC-5391
		l.setSiopeAssenzaMotivazione(estraiSiopeAssenzaMotivazione(subdoc));
		SiopeTipoDebito siopeTipoDebito = impegnoBilDad.findSiopeTipoDebito(impegno, subImpegno);
		l.setSiopeTipoDebito(siopeTipoDebito);
		
		return l;
	}
	
	/**
	 * @param subdoc
	 * @return
	 */
	private SiopeAssenzaMotivazione estraiSiopeAssenzaMotivazione(SubdocumentoSpesa subdoc) {
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = subdoc.getSiopeAssenzaMotivazione();
		if(siopeAssenzaMotivazione == null || siopeAssenzaMotivazione.getUid()==0) {
			return null;
		}
		if(StringUtils.isEmpty(siopeAssenzaMotivazione.getCodice())) {
			siopeAssenzaMotivazione = codificaDad.ricercaCodifica(SiopeAssenzaMotivazione.class, siopeAssenzaMotivazione.getUid());
		}
		return siopeAssenzaMotivazione;
	}

	private Soggetto creaSoggettoLiquidazione(SubdocumentoSpesa subdoc) {
		
		Soggetto soggettoLiquidazione = new Soggetto();
		
		RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiave(subdoc.getDocumento().getSoggetto().getCodiceSoggetto());
		Soggetto soggettoDocumento = resRSPC.getSoggetto();
		if(soggettoDocumento == null || soggettoDocumento.getUid() == 0){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + subdoc.getDocumento().getSoggetto().getCodiceSoggetto()));
		}
		
		soggettoLiquidazione.setUid(soggettoDocumento.getUid());
		soggettoLiquidazione.setCodiceSoggetto(soggettoDocumento.getCodiceSoggetto());
		soggettoLiquidazione.setStatoOperativo(soggettoDocumento.getStatoOperativo());
		
		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
		if(resRSPC.getListaModalitaPagamentoSoggetto() == null) {
			// TODO
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("ListaModalitaPagamentoSoggetto", "Soggetto codice " + subdoc.getDocumento().getSoggetto().getCodiceSoggetto()));
		}
		int uidSubdoc = subdoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2() != null && subdoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2().getUid() != 0
				? subdoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2().getUid()
				: subdoc.getModalitaPagamentoSoggetto().getUid();
		for(ModalitaPagamentoSoggetto mps : resRSPC.getListaModalitaPagamentoSoggetto()){
			int uid = mps.getModalitaPagamentoSoggettoCessione2() != null && mps.getModalitaPagamentoSoggettoCessione2().getUid() != 0
					? mps.getModalitaPagamentoSoggettoCessione2().getUid()
					: mps.getUid();
			if(uid == uidSubdoc){
				mps = ricercaModalitaPagamento(mps, soggettoDocumento);
				modalitaPagamentoList.add(mps);
				break;
			}
		}
		
		if(modalitaPagamentoList.isEmpty()){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("ModalitaPagamentoSoggetto", "uid "+subdoc.getModalitaPagamentoSoggetto().getUid() +" modalita pagamento per Soggetto codice " + subdoc.getDocumento().getSoggetto().getCodiceSoggetto()));
		}
		soggettoLiquidazione.setModalitaPagamentoList(modalitaPagamentoList);
		
		if(subdoc.getSedeSecondariaSoggetto() != null){
			List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
			SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
			sedeSecondariaSoggetto.setUid(subdoc.getSedeSecondariaSoggetto().getUid());
			sediSecondarie.add(sedeSecondariaSoggetto);
			soggettoLiquidazione.setSediSecondarie(sediSecondarie);
		}
		
		return soggettoLiquidazione;
	}
	
	/**
	 * Nel caso in cui la quota da liquidare è associata ad una modalità di pagamento 
	 * di tipo cessione del credito (Tipo.Accredito=CSC) oltre alle verifiche ai punti precedenti,  
	 * si inserisce la liquidazione da abbinare alla quota tramite il metodo Inserisce liquidazione  
	 * del servizio Gestione liquidazione passando in input il 
	 * 
	 * soggetto e 
	 * 
	 * modalità di pagamento (con eventuale sede) 
	 * 
	 * che sono collegati alla modalità di pagamento della quota tramite la relazione 'Cessione del Credito ad altro soggetto'.
	 */
	private void gestisciCessioneDelCredito(Liquidazione liquidazione) {
		
		Soggetto soggettoLiquidazione = liquidazione.getSoggettoLiquidazione();
		ModalitaPagamentoSoggetto mps = null;
		if(soggettoLiquidazione.getModalitaPagamentoList()!=null && !soggettoLiquidazione.getModalitaPagamentoList().isEmpty()){
			//riutilizzo ModalitaPagamentoSoggetto caricata dal servizio RicercaModalitaPagamentoPerChiave in precedenza (nel metodo #creaSoggettoLiquidazione)
			mps = soggettoLiquidazione.getModalitaPagamentoList().get(0); 
		}
		if(mps == null) {
			return;
		}
		
		String codiceGruppoAccredito = getCodiceGruppoAccredito(mps);
		
		// TODO: configurazione per FA e PI
		if(isTipoAccreditoCSCLike(codiceGruppoAccredito)) {
			Soggetto soggettoCessione = ottieniSoggettoCessione(mps);
			ModalitaPagamentoSoggetto mpsCessione = mps.getModalitaPagamentoSoggettoCessione2();
			// ModalitaPagamentoSoggetto mpsCessione = ricercaModalitaPagamento(mps.getModalitaPagamentoSoggettoCessione2(), soggettoCessione);
			popolaModalitaPagamentoCessione(liquidazione, mpsCessione, soggettoCessione);
			return;
		}
		
		//jira 2793
//		if(isTipoAccreditoCSILike(codiceGruppoAccredito)) {
//			Soggetto soggettoCessione = soggettoLiquidazione;
//			ModalitaPagamentoSoggetto mpsCessione = mps.getModalitaPagamentoSoggettoCessione2();
//			//ModalitaPagamentoSoggetto mpsCessione = ricercaModalitaPagamento(mps.getModalitaPagamentoSoggettoCessione2(), soggettoCessione);
//			popolaModalitaPagamentoCessione(liquidazione, mpsCessione, soggettoCessione);
//			return;
//		}
	}
	
	private String getCodiceGruppoAccredito(ModalitaPagamentoSoggetto mps) {
		return soggettoDad.ottieniCodiceGruppoAccreditoByTipoAccredito(mps.getTipoAccredito());
	}

	private boolean isTipoAccreditoCSCLike(String codiceGruppoAccredito) {
		return TipoAccredito.CSC.name().equalsIgnoreCase(codiceGruppoAccredito);
	}
	
//	private boolean isTipoAccreditoCSILike(String codiceGruppoAccredito) {
//		return TipoAccredito.CSI.name().equalsIgnoreCase(codiceGruppoAccredito);
//	}
	
	private void popolaModalitaPagamentoCessione(Liquidazione liquidazione, ModalitaPagamentoSoggetto mpsCessione, Soggetto soggetto) {
		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
		modalitaPagamentoList.add(mpsCessione);
		soggetto.setModalitaPagamentoList(modalitaPagamentoList);
		liquidazione.setSoggettoLiquidazione(soggetto);
		liquidazione.setModalitaPagamentoSoggetto(mpsCessione);
	}

	private Soggetto ottieniSoggettoCessione(ModalitaPagamentoSoggetto mps) {
		RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiave(mps.getCessioneCodSoggetto());
		Soggetto soggettoCessione = resRSPC.getSoggetto();
		if(soggettoCessione == null || soggettoCessione.getUid() == 0){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + mps.getCessioneCodSoggetto()));
		}
		return soggettoCessione;
	}
	
	private ModalitaPagamentoSoggetto ricercaModalitaPagamento(ModalitaPagamentoSoggetto mps, Soggetto soggetto) {
		RicercaModalitaPagamentoPerChiave reqRMPC = new RicercaModalitaPagamentoPerChiave();
		reqRMPC.setEnte(ente);
		reqRMPC.setRichiedente(req.getRichiedente());
		reqRMPC.setModalitaPagamentoSoggetto(mps);
		reqRMPC.setSoggetto(soggetto);
		RicercaModalitaPagamentoPerChiaveResponse resRMPS = soggettoService.ricercaModalitaPagamentoPerChiave(reqRMPC);
		log.logXmlTypeObject(resRMPS, "response ricerca modalità pagamento per chiave: ");
		return resRMPS.getModalitaPagamentoSoggetto();
	}
	
	/**
	 * Integerizzazione di una stringa.
	 * 
	 * @param str la stringa di partenza
	 * @return l'Integer corrispondente, se valido; <code>null</code> altrimenti
	 */
	private Integer integerize(String str) {
		final String methodName = "integerize";
		try {
			return Integer.valueOf(str);
		} catch(NumberFormatException nfe) {
			log.debug(methodName, "NumberFormatException per la stringa " + str + ": " + nfe.getMessage());
		} catch(NullPointerException npe) {
			log.debug(methodName, "NullPointerException per la stringa " + str + ": " + npe.getMessage());
		}
		log.debug(methodName, "Fallimento dell'integerizzazione della stringa " + str + ". Restituisco null");
		return null;
	}
	
	private void impostaCodiciNellaLiquidazione(Impegno impegno, Liquidazione l) {
		l.setCodPdc(impegno.getCodPdc() != null ? impegno.getCodPdc() : "");
		l.setCodTransazioneEuropeaSpesa(impegno.getCodTransazioneEuropeaSpesa() != null ? impegno.getCodTransazioneEuropeaSpesa() : "");
		l.setCodRicorrenteSpesa(impegno.getCodRicorrenteSpesa() != null ? impegno.getCodRicorrenteSpesa() : "");
		l.setCodContoEconomico(impegno.getCodContoEconomico() != null ? impegno.getCodContoEconomico() : "" );
		l.setCodSiope(impegno.getCodSiope() != null ? impegno.getCodSiope() : "" );
		l.setCodCofog(impegno.getCodCofog() != null ? impegno.getCodCofog() : "");
		// SIAC-3655
		l.setCodCapitoloSanitarioSpesa(impegno.getCodCapitoloSanitarioSpesa() != null ? impegno.getCodCapitoloSanitarioSpesa() : "");
		l.setCodPrgPolReg(impegno.getCodPrgPolReg() != null ? impegno.getCodPrgPolReg() : "");
	}

	private RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiave(String codiceSoggetto) {
		RicercaSoggettoPerChiave reqRSPC = new RicercaSoggettoPerChiave();
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(codiceSoggetto /*documentoAssociato.getSoggetto().getCodiceSoggetto()*/);
		reqRSPC.setParametroSoggettoK(parametroSoggettoK);
		reqRSPC.setRichiedente(req.getRichiedente());
		reqRSPC.setEnte(ente /*documentoAssociato.getEnte()*/);
		RicercaSoggettoPerChiaveResponse resRSPC = soggettoService.ricercaSoggettoPerChiave(reqRSPC);
		log.logXmlTypeObject(resRSPC, "response ricerca soggetto per chiave");
		return resRSPC;
	}
	
	protected String getDescrizone(MovimentoGestione movimentoGestione) {
		return movimentoGestione.getAnnoMovimento()+"/"+movimentoGestione.getNumeroBigDecimal();
	}
	
	protected boolean isResiduo(MovimentoGestione movimentoGestione) {
		int annoMovimento = movimentoGestione.getAnnoMovimento();
		boolean result = annoMovimento < bilancio.getAnno();
		return result;
	}

	protected boolean isAccountConPermessoElenchiResidui() {
		return !super.isAzioneConsentita("OP-COM-insAllegatoAttoNoRes");
	}
	
	//SIAC-7470
	protected boolean isBloccoRORAttivo(){
		return super.isAzioneConsentita(AzioneConsentitaEnum.BLOCCO_SU_LIQ_IMP_RESIDUI.getNomeAzione()) || super.isAzioneConsentita(AzioneConsentitaEnum.BLOCCO_SU_INCASSI_RESIDUI.getNomeAzione()); 
	}
	
	//SIAC-7470
	protected void checkBloccoRor(){
		boolean bloccoRorErrorImp = false;
		boolean bloccoRorErrorAcc = false;
		//impegni
		for(SubdocumentoSpesa ss : elencoDocumentiAllegato.getSubdocumentiSpesa()) {
			Impegno impegnoOSubImpegno = caricaDatiImpegno(ss);
			//SIAC-7470
			Impegno impegnoROR = this.ricaricaImpegno(impegnoOSubImpegno);
			if(impegnoROR != null){
				bloccoRorErrorImp = this.escludiImpegnoPerBloccoROR(impegnoROR, bilancio.getAnno());
				//se l'impegno ha superato i test, verifico gli eventuali subImpegni
				if(!bloccoRorErrorImp && impegnoROR.getElencoSubImpegni() != null && !impegnoROR.getElencoSubImpegni().isEmpty()){
					for(int k = 0; k < impegnoROR.getElencoSubImpegni().size(); k++){
						bloccoRorErrorImp = this.escludiImpegnoPerBloccoROR(impegnoROR.getElencoSubImpegni().get(k), bilancio.getAnno());
					}
				}
			}
		}
		//accertamenti
		for(SubdocumentoEntrata se : elencoDocumentiAllegato.getSubdocumentiEntrata()) {
			Accertamento accertamentoOSubAccertamento = caricaDatiAccertamento(se);
			//SIAC-7470
			Accertamento accertamentoROR = this.ricaricaAccertamento(accertamentoOSubAccertamento);
			if(accertamentoROR != null){
				bloccoRorErrorAcc = this.escludiAccertamentoPerBloccoROR(accertamentoROR, bilancio.getAnno());
				//se l'impegno ha superato i test, verifico gli eventuali subImpegni
				if(!bloccoRorErrorAcc && accertamentoROR.getElencoSubAccertamenti() != null && !accertamentoROR.getElencoSubAccertamenti().isEmpty()){
					for(int k = 0; k < accertamentoROR.getElencoSubAccertamenti().size(); k++){
						bloccoRorErrorAcc = this.escludiAccertamentoPerBloccoROR(accertamentoROR.getElencoSubAccertamenti().get(k), bilancio.getAnno());
					}
				}
			}
		}
		//errori
		if(bloccoRorErrorAcc || bloccoRorErrorImp){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore((bloccoRorErrorImp ? "Impegno/sub impegno" : "Accertamento/sub accertamento") + " residuo non utilizzabile"));
		}
	}
	
	//SIAC-7470
	private Impegno ricaricaImpegno(Impegno param){
		Impegno result = null;
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		caricaDatiOpzionaliDto.setEscludiAnnullati(true);
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		paginazioneSubMovimentiDto.setNoSub(false);
		EsitoRicercaMovimentoPkDto esitoRicerca = impegnoOttimizzatoDad.ricercaMovimentoPk(req.getRichiedente(), ente, 
				String.valueOf(bilancio.getAnno()), param.getAnnoMovimento(), param.getNumeroBigDecimal(),
				paginazioneSubMovimentiDto, caricaDatiOpzionaliDto, CostantiFin.MOVGEST_TIPO_IMPEGNO, true, false);
		if(esitoRicerca != null && esitoRicerca.getMovimentoGestione() != null){
			result = (Impegno) esitoRicerca.getMovimentoGestione();
		}
		return result;
	}
	
	//SIAC-7470
	private Accertamento ricaricaAccertamento(Accertamento param){
		Accertamento result = null;
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		caricaDatiOpzionaliDto.setEscludiAnnullati(true);
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		paginazioneSubMovimentiDto.setNoSub(false);
		EsitoRicercaMovimentoPkDto esitoRicerca = accertamentoOttimizzatoDad.ricercaMovimentoPk(req.getRichiedente(), ente,
				String.valueOf(bilancio.getAnno()), param.getAnnoMovimento(), param.getNumeroBigDecimal(), 
				paginazioneSubMovimentiDto, caricaDatiOpzionaliDto, CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, true, true);
		if(esitoRicerca!=null && esitoRicerca.getMovimentoGestione()!=null){
			result = (Accertamento) esitoRicerca.getMovimentoGestione();
		}
		return result;
	}
	
	//SIAC-7470
	private boolean escludiAccertamentoPerBloccoROR(Accertamento accertamento, Integer annoEsercizio){
		boolean escludiXBloccoROR = false;
		if(super.isAzioneConsentita(AzioneConsentitaEnum.BLOCCO_SU_INCASSI_RESIDUI.getNomeAzione()) && 
			(accertamento.getAnnoMovimento() > 0 && annoEsercizio != null && accertamento.getAnnoMovimento() < annoEsercizio)){
			escludiXBloccoROR = true;
			if(accertamento.getListaModificheMovimentoGestioneEntrata() != null && !accertamento.getListaModificheMovimentoGestioneEntrata().isEmpty()){
				for(int j = 0; j < accertamento.getListaModificheMovimentoGestioneEntrata().size(); j++){
					ModificaMovimentoGestione mmg = accertamento.getListaModificheMovimentoGestioneEntrata().get(j);
					if(mmg.getTipoModificaMovimentoGestione() != null && mmg.getTipoModificaMovimentoGestione().equalsIgnoreCase(ModificaMovimentoGestione.CODICE_ROR_DA_MANTENERE) &&
						mmg.getAttoAmministrativo() != null && mmg.getAttoAmministrativo().getStatoOperativo() != null && mmg.getStatoOperativoModificaMovimentoGestione() != null &&
						mmg.getAttoAmministrativo().getStatoOperativo().equals(StatoOperativoAtti.DEFINITIVO.name()) && 
						!(mmg.getStatoOperativoModificaMovimentoGestione().name().equals(StatoOperativoModificaMovimentoGestione.ANNULLATO.name()))){
							escludiXBloccoROR = false;
							break;
					}
				}
			}
		}
		return escludiXBloccoROR;
	}
	
	//SIAC-7470
	private boolean escludiImpegnoPerBloccoROR(Impegno impegno, Integer annoEsercizio){
		boolean escludiXBloccoROR = false;
		if(super.isAzioneConsentita(AzioneConsentitaEnum.BLOCCO_SU_LIQ_IMP_RESIDUI.getNomeAzione()) && 
			(impegno.getAnnoMovimento() > 0 && annoEsercizio != null && impegno.getAnnoMovimento() < annoEsercizio)){
			escludiXBloccoROR = true;
			if(impegno.getListaModificheMovimentoGestioneSpesa() != null && !impegno.getListaModificheMovimentoGestioneSpesa().isEmpty()){
				for(int j = 0; j < impegno.getListaModificheMovimentoGestioneSpesa().size(); j++){
					ModificaMovimentoGestione mmg = impegno.getListaModificheMovimentoGestioneSpesa().get(j);
					if(mmg.getTipoModificaMovimentoGestione() != null && mmg.getTipoModificaMovimentoGestione().equalsIgnoreCase(ModificaMovimentoGestione.CODICE_ROR_DA_MANTENERE) &&
						mmg.getAttoAmministrativo() != null && mmg.getAttoAmministrativo().getStatoOperativo() != null && mmg.getStatoOperativoModificaMovimentoGestione() != null &&
						mmg.getAttoAmministrativo().getStatoOperativo().equals(StatoOperativoAtti.DEFINITIVO.name()) && 
						!(mmg.getStatoOperativoModificaMovimentoGestione().name().equals(StatoOperativoModificaMovimentoGestione.ANNULLATO.name()))){
							escludiXBloccoROR = false;
							break;
					}
				}
			}
		}
		return escludiXBloccoROR;
	}
	
	/**
	 * Controlla che non ci siano movimenti residui nel caso l'utente non sia abilitato a
	 */
	protected void checkMovimentiResidui() {
		String methodName = "checkMovimentiResidui";
		
		if(isAccountConPermessoElenchiResidui()){
			//L'utente e' abilitato ad inserire movimenti residui. Pertanto esco dal check
			log.debug(methodName, "L'utente e' abilitato ai residui. NON effettuo i controlli.");
			return;
		}
		log.debug(methodName, "L'utente NON e' abilitato ai residui. Effettuo i controlli.");
		
		for(SubdocumentoSpesa ss : elencoDocumentiAllegato.getSubdocumentiSpesa()) {
			Impegno impegnoOSubImpegno = caricaDatiImpegno(ss);
			if(isResiduo(impegnoOSubImpegno)){
				String msg = "L'impegno "+getDescrizone(impegnoOSubImpegno) +" non puo' essere associato in quanto residuo.";
				log.error(methodName, msg);
				res.addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
			}
			
			
		}
		for(SubdocumentoEntrata se : elencoDocumentiAllegato.getSubdocumentiEntrata()) {
			Accertamento accertamentoOSubAccertamento = caricaDatiAccertamento(se);
			if(isResiduo(accertamentoOSubAccertamento)){
				String msg = "L'accertamento "+getDescrizone(accertamentoOSubAccertamento) +" non puo' essere associato in quanto residuo.";
				log.error(methodName, msg);
				res.addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
			}
			
		}
		
		if(res.hasErrori()){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Account utente non abilitato alla gestione dei Residui negli Elenchi."));
		}
		
		
	}
	
	/**
	 * Aggiornamento dello stato del documento associato
	 */
	protected void aggiornaStatoDocumenti() {
		final String methodName = "aggiornaStatoDocumenti";
		// Aggiornamento dello stato dei documenti referenziati
		for(DocumentoSpesa ds : documentiPerCuiAggiornareStato.values()) {
			AggiornaStatoDocumentoDiSpesa reqASDDS = new AggiornaStatoDocumentoDiSpesa();
			
			reqASDDS.setDataOra(new Date());
			reqASDDS.setBilancio(bilancio);
			reqASDDS.setRichiedente(req.getRichiedente());
			reqASDDS.setDocumentoSpesa(ds);
			
			AggiornaStatoDocumentoDiSpesaResponse resASDDS = serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiSpesaService.class, reqASDDS);
			log.debug(methodName, "Aggiornato stato del documento " + ds.getUid() + " => stato finale " + resASDDS.getDocumentoSpesa().getStatoOperativoDocumento());
		}
	}

	protected abstract Accertamento caricaDatiAccertamento(SubdocumentoEntrata se);

	protected abstract Impegno caricaDatiImpegno(SubdocumentoSpesa ss);
}
