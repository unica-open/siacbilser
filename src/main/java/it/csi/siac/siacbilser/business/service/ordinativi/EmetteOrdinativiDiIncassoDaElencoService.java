/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.ordinativi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuoteDaEmettereEntrataService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiIncassoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiIncassoMultiploResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiIncassoSingolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiIncassoSingoloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;


/**
 * Consente di inserire una serie di ordinativi a fronte di un elenco di sub documenti di entrata individuato  in base ai parametri di input.
 * L'elaborazione deve poter essere lanciata da applicativo o schedulata. 
 * Il volume dei dati elaborati pu&ograve; raggiungere l'ordine della decina di migliaia.
 * <br/>
 * Analisi di riferimento: 
 * BIL--SIAC-FIN-SER-017-V01 - COMS003 Servizio Gestione Emissione Ordinativi.docx 
 * &sect;2.4
 * 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmetteOrdinativiDiIncassoDaElencoService extends EmetteOrdinativiDaElencoBaseService<EmetteOrdinativiDiIncassoDaElenco, EmetteOrdinativiDiIncassoDaElencoResponse> {
	
	@Autowired
	private RicercaQuoteDaEmettereEntrataService ricercaQuoteDaEmettereEntrataService;
	@Autowired
	private EmetteOrdinativoDiIncassoSingoloService emetteOrdinativoDiIncassoSingoloService;
	@Autowired
	private EmetteOrdinativoDiIncassoMultiploService emetteOrdinativoDiIncassoMultiploService;
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		
		if(req.getSubdocumenti()==null || req.getSubdocumenti().isEmpty()){
			if(req.getElenchi()==null || req.getElenchi().isEmpty()) {
				//elaborazione tipo "ELABORA QUOTE AUTOMATICHE"
				if(req.getAllegatoAtto() != null && req.getAllegatoAtto().getUid() != 0){
					checkNotNull(req.getAllegatoAtto().getAttoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo allegato atto"));
					AttoAmministrativo aa = req.getAllegatoAtto().getAttoAmministrativo();
					checkCondition(aa.getUid()!=0 || (aa.getAnno()!=0 && aa.getNumero()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid oppure anno e numero atto amministrativo allegato atto"));
				}
			} else {
				//elaborazione tipo "ELABORA ELENCHI MAN".
				for(ElencoDocumentiAllegato eda : req.getElenchi()){
					checkNotNull(eda, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato"));
					checkCondition(eda.getUid()!=0 || (eda.getAnno()!=null && eda.getNumero()!=null), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid oppure anno e numero elenco documenti allegato"));
				}
			}
		} else {
			 //elaborazione tipo: "ELABORA QUOTE MAN"
			for(SubdocumentoEntrata subdoc : req.getSubdocumenti()){
				checkEntita(subdoc, "subdoc");
			}
		}
	}
	
	/**
	 * SIAC-8195
	 * 
	 * @param List<SubdocumentoEntrata> quoteDaEmettere
	 * @throws BusinessException
	 */
	private void controlloBloccoRORAttivoQuote(List<SubdocumentoEntrata> quoteDaEmettere) {
		if(isBloccoRORAttivo()){
			if(CollectionUtils.isNotEmpty(quoteDaEmettere)){
				for(SubdocumentoEntrata s : quoteDaEmettere){
					List<Subdocumento<?,?>> listaSubdocAmmessi = subdocumentoDad.cercaSubdocNonResiduioResiduiConModificheRORM(
							s.getUid(), req.getBilancio().getAnno());
					
					//se non ho risultati validi per l'emissione lancio l'eccezione
					if(CollectionUtils.isEmpty(listaSubdocAmmessi)){
						//TODO restituire le chiavi o solo <anno>/<numero> per un errore piu' parlante?
						throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Accertamento/sub accertamento residuo non utilizzabile"));
					}
				}
			}
		}
	}

	/**
	 * SIAC-8195 
	 * Spostato il controllo sulla parte sincrona dell'emissione con adeguamento dell'azione 
	 * di un probabile refuso da:
	 *  
	 * BLOCCO_SU_LIQ_IMP_RESIDUI => ("OP-COM-insAllegatoAttoNoResImp")
	 * 
	 * a 
	 * 
	 * BLOCCO_SU_INCASSI_RESIDUI => ("OP-COM-insAllegatoAttoNoResAcc")
	 * 
	 * come da analisi, in quanto il servizio e' relativo all'emissione delle quote di incasso
	 * 
	 * @return boolean
	 */
	//SIAC-7470
	protected boolean isBloccoRORAttivo(){
		List<String> codiciAzioniConsentite = accountDad.findCodiciAzioniConsentite(req.getRichiedente().getAccount().getUid());
		return codiciAzioniConsentite.indexOf(AzioneConsentitaEnum.BLOCCO_SU_INCASSI_RESIDUI.getNomeAzione())!=-1;
	}
	
	
	@Override
	protected void init() {
		super.init();
		
		bilancio = req.getBilancio();
		contoTesoreria = req.getContoTesoreria() != null && req.getContoTesoreria().getUid() != 0 ? req.getContoTesoreria() : null;
		distinta = req.getDistinta() != null && req.getDistinta().getUid() != 0 ? req.getDistinta() : null;
		note = req.getNote();
		codiceBollo = req.getCodiceBollo() != null && req.getCodiceBollo().getUid() != 0 ? req.getCodiceBollo() : null;
		dataScadenza = req.getDataScadenza();
		flagNoDataScadenza = req.getFlagNoDataScadenza();
		flagDaTrasmettere = req.getFlagDaTrasmettere();
		classificatoreStipendi = req.getClassificatoreStipendi();
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public EmetteOrdinativiDiIncassoDaElencoResponse executeServiceTxRequiresNew(EmetteOrdinativiDiIncassoDaElenco serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public EmetteOrdinativiDiIncassoDaElencoResponse executeService(EmetteOrdinativiDiIncassoDaElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
		
	@Override
	protected void execute() {
		caricaBilancio();
		caricaCodiceBollo();
		caricaDistinta();
		caricaContoTesoreria();
		
		// Step4: Gestisce stato Elaborazione elenchi
		// TODO: gestione dello Stato Operativo Elaborazioni Asincrone
		
		// Step5: Ricerca documenti da incassare
		List<SubdocumentoEntrata> quoteDaEmettere = caricaQuoteDaEmettere();
		
		//SIAC-8195 si sposta il controllo sulla parte sincrona dell'emissione
		//SIAC-7470: prima di verificare il bloccoROR devo caricare i dati dall'elenco tramite i subdocumenti - vedi "EmetteOrdinativiDiIncassoDaElencoService"
		controlloBloccoRORAttivoQuote(quoteDaEmettere);
		
		startElaborazioneQuote(quoteDaEmettere);
		try {
			// Ordinativi singoli
			List<SubdocumentoEntrata> quoteDaEmettereConOrdinativoSingolo = filtraQuoteConFlagOrdinativoSingolo(quoteDaEmettere, Boolean.TRUE);
			ordinaQuoteDaEmettereOrdinativoSingolo(quoteDaEmettereConOrdinativoSingolo);
			creaEInserisciOrdinativiIncasso(quoteDaEmettereConOrdinativoSingolo);
			
			// Ordinativi multipli
			List<SubdocumentoEntrata> quoteDaEmettereConOrdinativoMultiplo = filtraQuoteConFlagOrdinativoSingolo(quoteDaEmettere, Boolean.FALSE);
			Map<String, List<SubdocumentoEntrata>> gruppiQuoteDaEmettereConOrinativoMultiplo = raggruppaQuoteDaEmettereOrdinativoMultiplo(quoteDaEmettereConOrdinativoMultiplo);
			creaEInserisciOrdinativiIncasso(gruppiQuoteDaEmettereConOrinativoMultiplo);
		} finally {
			endElaborazioneQuote(quoteDaEmettere);
		}
		
	}

	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*-------------------------------------- CONTROLLI E FILTRI QUOTE DA EMETTERE -------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Prepara le quote da emettere a partire dalle varie modalit&agrave; di invocazione del servizio.
	 * 
	 * @return lista delle quote/liquidazioni da cui emettere gli ordinativi.
	 */
	private List<SubdocumentoEntrata> caricaQuoteDaEmettere() {
		final String methodName = "caricaQuoteDaEmettere";
		
		List<SubdocumentoEntrata> quoteDaEmettere = new ArrayList<SubdocumentoEntrata>();
		
		if(req.getSubdocumenti()==null || req.getSubdocumenti().isEmpty()){
			
			if(req.getElenchi()==null || req.getElenchi().isEmpty()) {
				//elaborazione tipo "ELABORA QUOTE AUTOMATICHE"
				if(req.getAllegatoAtto() != null && req.getAllegatoAtto().getUid() != 0){
					log.debug(methodName, "Ottengo le quote a partire dall'allegato atto");
					AttoAmministrativo aa = req.getAllegatoAtto().getAttoAmministrativo();
					quoteDaEmettere = ricercaQuoteDaEmettere(null, null, null, aa.getUid() != 0 ? aa.getUid() : null, aa.getAnno(), aa.getNumero());
				}else{
					log.debug(methodName, "Ottengo tutte le quote che si possono emettere");
					quoteDaEmettere = ricercaQuoteDaEmettere(null, null, null, null, null, null);
				}
				
			} else { 
				//Lista elenchi popolata! elaborazione tipo "ELABORA ELENCHI MAN".
				for(ElencoDocumentiAllegato eda : req.getElenchi()){ //crf. passo 5 in analisi.
					List<SubdocumentoEntrata> subdocumentiEntrataTrovati = ricercaQuoteDaEmettere(eda.getUid() != 0 ? eda.getUid() : null, eda.getAnno(), eda.getNumero(), null, null, null);
					String key = eda.getUid() != 0 ? eda.getUid() + "" : eda.getAnno() + "/" + eda.getNumero();
					log.debug(methodName, "Subdocumenti trovati per elenco " + key + ": " + subdocumentiEntrataTrovati.size());
					quoteDaEmettere.addAll(subdocumentiEntrataTrovati);
				}
			}
			
		}else{ 
			
			for(SubdocumentoEntrata subdoc : req.getSubdocumenti()){
				subdoc = subdocumentoEntrataDad.findSubdocumentoEntrataById(subdoc.getUid());
				quoteDaEmettere.add(subdoc);
			}
		}
		//carico dettagli documento e accertamento per ogni quota
		for(SubdocumentoEntrata s : quoteDaEmettere){
			Accertamento accertamento = caricaAccertamento(s);
			s.setAccertamento(accertamento);
			//SIAC-5712
			s.setSubAccertamento(cercaSubAccertamento(accertamento, s.getSubAccertamento()));
			DocumentoEntrata documento = caricaDocumento(s);
			s.setDocumento(documento);
		}
		log.info(methodName, "totale quote da emettere: "+ quoteDaEmettere.size());
		return quoteDaEmettere;
	}

	/**
	 * Carica i dettagli del documento padre di una quota 
	 * @param subdocumentoEntrata la quota
	 * @return documento il documento padre
	 */
	private DocumentoEntrata caricaDocumento(SubdocumentoEntrata subdocumentoEntrata) {
		DocumentoEntrata documento = documentiEntrataCache.get(subdocumentoEntrata.getDocumento().getUid());
		if(documento == null){
			documento = documentoEntrataDad.findDocumentoEntrataById(subdocumentoEntrata.getDocumento().getUid());
			documentiEntrataCache.put(subdocumentoEntrata.getDocumento().getUid(), documento);
		}
		return documento;
	}

	
	
	/**
     * Ricerca tutte le quote che possono essere emesse a partire da anno  numero elenco.
     * Il tipo convalida e la distinta passate in request vengono utilizzate per filtrare.
     * 
     * @param annoElenco
     * @param numeroElenco
     * @param annoProvvedimento 
     * @param numeroProvvedimento 
     * @return
     */
	private List<SubdocumentoEntrata> ricercaQuoteDaEmettere(Integer uidElenco, Integer annoElenco, Integer numeroElenco, Integer uidProvvedimento, Integer annoProvvedimento, Integer numeroProvvedimento) {
    	ListaPaginata<SubdocumentoEntrata> result = ricercaQuoteDaEmettere(0, uidElenco, annoElenco, numeroElenco, uidProvvedimento, annoProvvedimento, numeroProvvedimento);
    	
    	for(int i = 1; i<result.getTotalePagine(); i++){
    		ListaPaginata<SubdocumentoEntrata> page = ricercaQuoteDaEmettere(i, uidElenco, annoElenco, numeroElenco, uidProvvedimento, annoProvvedimento, numeroProvvedimento);
    		result.addAll(page);
    	}
    	
    	return result;
	}
	
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*----------------------------------------- GESTIONE ORDINATIVI SINGOLI -------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Gestione degli ordinativi emessi con flagOrdinativoSingolo true
	 * 
	 * @param quoteDaEmettereConOrdinativoSingolo
	 * @return la lista degli ordinativi di incasso emessi
	 */
	private List<OrdinativoIncasso> creaEInserisciOrdinativiIncasso(List<SubdocumentoEntrata> quoteDaEmettereConOrdinativoSingolo) {
		final String methodName = "creaOrdinativiIncasso";
		List<OrdinativoIncasso> ordinativi = new ArrayList<OrdinativoIncasso>();
		for(SubdocumentoEntrata subdoc : quoteDaEmettereConOrdinativoSingolo){
			OrdinativoIncasso ordinativo = emettiOrdinativoSingolo(subdoc);
			ordinativi.add(ordinativo);
		}
		log.debug(methodName, "Ordinativi creati: " + ordinativi.size());
		return ordinativi;
	}
	

	/**
	 * Ordina i subdocumenti per:
	 * <ul>
	 *     <li>Soggetto intestatario del Documento </li>
	 *     <li>Anno , tipo e numero Documento </li>
	 *     <li>importoDaIncassare del Subdocumento  discendente</li>
	 * </ul>
	 * 
	 * @param quoteDaEmettereConOrdinativoSingolo
	 */
	private void ordinaQuoteDaEmettereOrdinativoSingolo(List<SubdocumentoEntrata> quoteDaEmettereConOrdinativoSingolo) {
		Collections.sort(quoteDaEmettereConOrdinativoSingolo, new Comparator<SubdocumentoEntrata>(){

			@Override
			public int compare(SubdocumentoEntrata s1, SubdocumentoEntrata s2) {
				return new CompareToBuilder()
						// Soggetto intestatario del Documento
						.append(s1.getDocumento().getSoggetto().getCodiceSoggetto(), s2.getDocumento().getSoggetto().getCodiceSoggetto())
						// Anno, tipo e numero Documento
						.append(s1.getDocumento().getAnno(), s1.getDocumento().getAnno())
						.append(s1.getDocumento().getTipoDocumento().getCodice(), s2.getDocumento().getTipoDocumento().getCodice())
						.append(s1.getDocumento().getNumero(), s2.getDocumento().getNumero())
						// importoDaIncassare del Subdocumento discendente
						.append(s2.getImportoDaIncassare(), s1.getImportoDaIncassare())
						.toComparison();
			}
		});
	}
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*---------------------------------------- GESTIONE ORDINATIVI MULTIPLI -------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Raggruppa i dati per:
	 * <ul>
	 *     <li>Subdocumento:
	 *     <ul>
	 *     	   <li>Atto Amministrativo  => anno, numero, Tipo, Struttura Amministrativa</li>
	 *     	   <li>CapitoloEntrata =>  numeroCapitolo, numeroArticolo (derivato dal MovimentoGestione)</li>
	 *         <li>MovimentoGestione.annoMovimento</li>
	 *         <li>Documento.Soggetto</li>
	 *         <li>Conto di Tesoreria  => se impostato il corrispondente parametro dell'operazione non è significativo </li>
	 *         <li>Distinta   => se impostato il corrispondente parametro dell'operazione non è significativo</li>
	 *         <li>Documento.flagBeneficiarioMultiplo</li>
	 *         <li>Documento.CodiceBollo</li>
	 *         <li>flagAvviso</li>
	 *         <li>NoteTesoriere</li>
	 *         <li>CommissioniDocumento</li>
	 *         <li>flagACopertura</li>
	 *         <li>ClassificazioneFinanziariaGestione.codificaTransazioneElementare</li>
	 *     </ul></li>
	 * </ul>
	 *
	 * @param quoteDaEmettereConOrdinativoMultiplo the quote da emettere con ordinativo multiplo
	 * @return the map
	 */
	private Map<String, List<SubdocumentoEntrata>> raggruppaQuoteDaEmettereOrdinativoMultiplo(List<SubdocumentoEntrata> quoteDaEmettereConOrdinativoMultiplo) {
		Map<String, List<SubdocumentoEntrata>> result = new HashMap<String, List<SubdocumentoEntrata>>();
		
		for (SubdocumentoEntrata s : quoteDaEmettereConOrdinativoMultiplo) {

			String key = computeGroupKeySubdocumento(s);
			log.debug("raggruppaQuoteDaEmettereOrdinativoMultiplo", "uid quota: " + s.getUid() + ", chiave raggruppamento: " + key);

			if (!result.containsKey(key)) {
				List<SubdocumentoEntrata> lista = new ArrayList<SubdocumentoEntrata>();
				result.put(key, lista);
			}
			result.get(key).add(s);
		}
		
		return result;
	}
	
	/**
	 * Crea gli ordinativi di incasso.
	 * 
	 * @param gruppiQuote i gruppi da emettere
	 * 
	 * @return gli ordinativi creati
	 */
	private List<OrdinativoIncasso> creaEInserisciOrdinativiIncasso(Map<String, List<SubdocumentoEntrata>> gruppiQuote) {
		List<OrdinativoIncasso> ordinativi = new ArrayList<OrdinativoIncasso>();
		
		for(Map.Entry<String, List<SubdocumentoEntrata>> entry : gruppiQuote.entrySet()){
			OrdinativoIncasso ordinativo = emettiOrdinativoMultiplo(entry.getValue(), entry.getKey());
			ordinativi.add(ordinativo);
		}
		return ordinativi;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*----------------------------- CALCOLI STRINGHE PER LE CHIAVI DI RAGGRUPPAMENTO ----------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	private String computeGroupKeySubdocumento(SubdocumentoEntrata s) {
 		final String separator = "_";
 		StringBuilder key = new StringBuilder();
 		
 		key.append(computeKeyAttoAmministrativo(s.getAttoAmministrativo()))
 			.append(separator)
 			.append(computeKeyCapitolo(s.getAccertamento()))
 			.append(separator)
 			.append(computeKeyMovimentoGestione(s.getMovimentoGestione()))
 			.append(separator)
 			.append(computeKeySoggetto(s.getDocumento().getSoggetto()))
 			.append(separator)
 			.append(computeKeyContoTesoreria(contoTesoreria != null ? contoTesoreria : s.getContoTesoreria()))
 			.append(separator)
 			.append(computeKeyDistinta(distinta != null ? distinta : s.getDistinta()))
 			.append(separator)
 			.append(computeKeyFlagDebitoreMultiplo(s.getDocumento()))
 			.append(separator)
 			.append(computeKeyCodiceBollo(codiceBollo != null ? codiceBollo : s.getDocumento().getCodiceBollo()))
 			.append(separator)
 			.append(computeKeyFlagAvviso(s.getFlagAvviso()))
 			.append(separator)
 			.append(computeKeyNote(s.getNote()))
 			.append(separator)
 			.append(computeKeyFlagACopertura(s.getFlagACopertura()))
 			.append(separator)
 			.append(computeKeyPianoDeiContiFinanziario(s.getMovimentoGestione()))
 			.append(separator)
 			.append(computeKeyDataScadenza(dataScadenza != null ? dataScadenza : s.getDataScadenza()))
 			.append(separator)
 			.append(computeKeySedeSecondaria(s.getSedeSecondariaSoggetto()));
 		
 		return key.toString();
 	}
 	
 	private String computeKeyCapitolo(Accertamento accertamento) {
 		StringBuilder sb = new StringBuilder();
 		CapitoloEntrataGestione capitoloEntrataGestione = accertamento.getCapitoloEntrataGestione();
 		
 		sb.append("capitolo")
 			.append("<");
 		if(capitoloEntrataGestione == null) {
 			sb.append("null");
 		} else {
 			sb.append(capitoloEntrataGestione.getNumeroCapitolo())
				.append("/")
				.append(capitoloEntrataGestione.getNumeroArticolo());
 		}
 		
 		sb.append(">");
		return sb.toString();
	}


	private String computeKeyFlagDebitoreMultiplo(DocumentoEntrata d) {
 		return "flagBenefMult<" + d.getFlagDebitoreMultiplo() + ">";
 	}


	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*----------------------------------------- RICHIAMO A SERVIZI ESTERNI --------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Ricerca paginata di tutte le quote che possono essere emesse a partire da anno numero elenco.
     * Il tipo convalida e la distinta passate in request vengono utilizzate per filtrare.
     * 
	 * @param numeroPagina
	 * @param annoElenco
	 * @param numeroElenco
	 * @param annoProvvedimento 
	 * @param numeroProvvedimento 
	 * @return
	 */
	private ListaPaginata<SubdocumentoEntrata> ricercaQuoteDaEmettere(int numeroPagina, Integer uidElenco, Integer annoElenco, Integer numeroElenco, Integer uidProvvedimento, Integer annoProvvedimento, Integer numeroProvvedimento) {
		RicercaQuoteDaEmettereEntrata reqRQDE = new RicercaQuoteDaEmettereEntrata();
		reqRQDE.setDataOra(new Date());
		reqRQDE.setRichiedente(req.getRichiedente());
		
		reqRQDE.setUidElenco(uidElenco);
		reqRQDE.setAnnoElenco(annoElenco);
		reqRQDE.setNumeroElenco(numeroElenco);
		reqRQDE.setUidProvvedimento(uidProvvedimento);
		reqRQDE.setAnnoProvvedimento(annoProvvedimento);
		reqRQDE.setNumeroProvvedimento(numeroProvvedimento);
		reqRQDE.setFlagConvalidaManuale(req.getFlagConvalidaManuale());
		reqRQDE.setDistinta(distinta);
		
		ParametriPaginazione pp = new ParametriPaginazione(numeroPagina);
		pp.setElementiPerPagina(100);
		reqRQDE.setParametriPaginazione(pp);
		
		RicercaQuoteDaEmettereEntrataResponse resRQDE = serviceExecutor.executeServiceSuccess(ricercaQuoteDaEmettereEntrataService, reqRQDE);	
		return resRQDE.getListaSubdocumenti();
	}
	
	
	private OrdinativoIncasso emettiOrdinativoSingolo(SubdocumentoEntrata subdoc) {
		EmetteOrdinativoDiIncassoSingolo emetteOrdinativoDiIncassoSingolo = new EmetteOrdinativoDiIncassoSingolo();
		emetteOrdinativoDiIncassoSingolo.setSubdoc(subdoc);
		emetteOrdinativoDiIncassoSingolo.setFlagConvalidaManuale(req.getFlagConvalidaManuale());
		emetteOrdinativoDiIncassoSingolo.setSoggettiCache(soggettiCache);
		emetteOrdinativoDiIncassoSingolo.setDocumentiEntrataCache(documentiEntrataCache);
		emetteOrdinativoDiIncassoSingolo.setAccertamentiCache(accertamentiCache);
		emetteOrdinativoDiIncassoSingolo.setRichiedente(req.getRichiedente());
		emetteOrdinativoDiIncassoSingolo.setBilancio(bilancio);
		//SIAC-5937
		emetteOrdinativoDiIncassoSingolo.setBilancioAnnoSuccessivo(bilancioAnnoSuccessivo);
		emetteOrdinativoDiIncassoSingolo.setBilancioInDoppiaGestione(bilancioInDoppiaGestione);
		emetteOrdinativoDiIncassoSingolo.setDistinta(distinta);
		emetteOrdinativoDiIncassoSingolo.setContoTesoreria(contoTesoreria);
		emetteOrdinativoDiIncassoSingolo.setNote(note);
		emetteOrdinativoDiIncassoSingolo.setCodiceBollo(codiceBollo);
		emetteOrdinativoDiIncassoSingolo.setDataScadenza(dataScadenza);
		emetteOrdinativoDiIncassoSingolo.setFlagNoDataScadenza(flagNoDataScadenza);
		//SIAC-6175
		emetteOrdinativoDiIncassoSingolo.setFlagDaTrasmettere(flagDaTrasmettere);
		//SIAC-6206
		emetteOrdinativoDiIncassoSingolo.setClassificatoreStipendi(classificatoreStipendi);
		
		EmetteOrdinativoDiIncassoSingoloResponse emetteOrdinativoDiIncassoSingoloResponse = emetteOrdinativoDiIncassoSingoloService.executeServiceTxRequiresNew(emetteOrdinativoDiIncassoSingolo);
		
		if(emetteOrdinativoDiIncassoSingoloResponse.getMessaggi() != null && !emetteOrdinativoDiIncassoSingoloResponse.getMessaggi().isEmpty()){
			res.addMessaggi(emetteOrdinativoDiIncassoSingoloResponse.getMessaggi());
		}
		if(emetteOrdinativoDiIncassoSingoloResponse.getSubdocumentoScartato() != null){
			res.getSubdocumentiScartati().add(emetteOrdinativoDiIncassoSingoloResponse.getSubdocumentoScartato());
		}
		if(emetteOrdinativoDiIncassoSingoloResponse.hasErrori()){
			for(Errore errore : emetteOrdinativoDiIncassoSingoloResponse.getErrori()){
				res.addMessaggio(new Messaggio(errore.getCodice(), "Emissione ordinativo per subdocumento " + subdoc.getUid() + ": "+  errore.getDescrizione()));
			}
		}
		if(emetteOrdinativoDiIncassoSingoloResponse.isFallimento()){
			return null;
		}else{
			soggettiCache = emetteOrdinativoDiIncassoSingoloResponse.getSoggettiCache();
			documentiEntrataCache = emetteOrdinativoDiIncassoSingoloResponse.getDocumentiEntrataCache();
			accertamentiCache = emetteOrdinativoDiIncassoSingoloResponse.getAccertamentiCache();
			return emetteOrdinativoDiIncassoSingoloResponse.getOrdinativo();
		}
	}
	

	private OrdinativoIncasso emettiOrdinativoMultiplo(List<SubdocumentoEntrata> lista, String chiave) {
		EmetteOrdinativoDiIncassoMultiplo emetteOrdinativoDiIncassoMultiplo = new EmetteOrdinativoDiIncassoMultiplo();
		emetteOrdinativoDiIncassoMultiplo.setQuote(lista);
		emetteOrdinativoDiIncassoMultiplo.setFlagConvalidaManuale(req.getFlagConvalidaManuale());
		emetteOrdinativoDiIncassoMultiplo.setSoggettiCache(soggettiCache);
		emetteOrdinativoDiIncassoMultiplo.setDocumentiEntrataCache(documentiEntrataCache);
		emetteOrdinativoDiIncassoMultiplo.setAccertamentiCache(accertamentiCache);
		emetteOrdinativoDiIncassoMultiplo.setRichiedente(req.getRichiedente());
		emetteOrdinativoDiIncassoMultiplo.setBilancio(bilancio);
		//SIAC-5937
		emetteOrdinativoDiIncassoMultiplo.setBilancioAnnoSuccessivo(bilancioAnnoSuccessivo);
		emetteOrdinativoDiIncassoMultiplo.setBilancioInDoppiaGestione(bilancioInDoppiaGestione);
		emetteOrdinativoDiIncassoMultiplo.setDistinta(distinta);
		emetteOrdinativoDiIncassoMultiplo.setContoTesoreria(contoTesoreria);
		emetteOrdinativoDiIncassoMultiplo.setNote(note);
		emetteOrdinativoDiIncassoMultiplo.setCodiceBollo(codiceBollo);
		emetteOrdinativoDiIncassoMultiplo.setDataScadenza(dataScadenza);
		emetteOrdinativoDiIncassoMultiplo.setFlagNoDataScadenza(flagNoDataScadenza);
		//SIAC-6175
		emetteOrdinativoDiIncassoMultiplo.setFlagDaTrasmettere(flagDaTrasmettere);
		//SIAC-6206
		emetteOrdinativoDiIncassoMultiplo.setClassificatoreStipendi(classificatoreStipendi);
		
		EmetteOrdinativoDiIncassoMultiploResponse emetteOrdinativoDiIncassoMultiploResponse = emetteOrdinativoDiIncassoMultiploService.executeServiceTxRequiresNew(emetteOrdinativoDiIncassoMultiplo);
		
		if(emetteOrdinativoDiIncassoMultiploResponse.getMessaggi() != null && !emetteOrdinativoDiIncassoMultiploResponse.getMessaggi().isEmpty()){
			for(Messaggio mm : emetteOrdinativoDiIncassoMultiploResponse.getMessaggi()){
				res.addMessaggio(new Messaggio(mm.getCodice(), "Emissione ordinativo per gruppo di quote con chiave " + StringUtils.replace(chiave, "_", " ")+ ": "+  mm.getDescrizione()));
			}
		}
		if(emetteOrdinativoDiIncassoMultiploResponse.getSubdocumentiScartati() != null && !emetteOrdinativoDiIncassoMultiploResponse.getSubdocumentiScartati().isEmpty()){
			res.getSubdocumentiScartati().addAll(emetteOrdinativoDiIncassoMultiploResponse.getSubdocumentiScartati());
		}
		if(emetteOrdinativoDiIncassoMultiploResponse.hasErrori()){
			for(Errore errore : emetteOrdinativoDiIncassoMultiploResponse.getErrori()){
				res.addMessaggio(new Messaggio(errore.getCodice(), "Emissione ordinativo per gruppo di quote con chiave " + chiave + ": "+  errore.getDescrizione()));
			}
		}
		if(emetteOrdinativoDiIncassoMultiploResponse.isFallimento()){
			return null;
		}else{
			soggettiCache = emetteOrdinativoDiIncassoMultiploResponse.getSoggettiCache();
			documentiEntrataCache = emetteOrdinativoDiIncassoMultiploResponse.getDocumentiEntrataCache();
			accertamentiCache = emetteOrdinativoDiIncassoMultiploResponse.getAccertamentiCache();
			return emetteOrdinativoDiIncassoMultiploResponse.getOrdinativo();
		}
		
	}

	@Override
	//SIAC-8017-CMTO
	protected void impostaMessaggiInResponse(List<Messaggio> messaggi) {
		res.addMessaggi(messaggi);
	}

//	//SIAC-8195 si sposta il controllo sulla parte sincrona dell'emissione
//	//SIAC-7470
//	private boolean escludiAccertamentoPerBloccoROR(Accertamento accertamento, Integer annoEsercizio){
//		boolean escludiXBloccoROR = false;
//		if(isBloccoRORAttivo() && (accertamento.getAnnoMovimento() > 0 && annoEsercizio != null && accertamento.getAnnoMovimento() < annoEsercizio)){
//			escludiXBloccoROR = true;
//			if(accertamento.getListaModificheMovimentoGestioneEntrata() != null && !accertamento.getListaModificheMovimentoGestioneEntrata().isEmpty()){
//				for(int j = 0; j < accertamento.getListaModificheMovimentoGestioneEntrata().size(); j++){
//					ModificaMovimentoGestione mmg = accertamento.getListaModificheMovimentoGestioneEntrata().get(j);
//					if(mmg.getTipoModificaMovimentoGestione() != null && mmg.getTipoModificaMovimentoGestione().equalsIgnoreCase(ModificaMovimentoGestione.CODICE_ROR_DA_MANTENERE) &&
//						mmg.getAttoAmministrativo() != null && mmg.getAttoAmministrativo().getStatoOperativo() != null && mmg.getStatoOperativoModificaMovimentoGestione() != null &&
//						mmg.getAttoAmministrativo().getStatoOperativo().equals(StatoOperativoAtti.DEFINITIVO.name()) && 
//						!(mmg.getStatoOperativoModificaMovimentoGestione().name().equals(StatoOperativoModificaMovimentoGestione.ANNULLATO.name()))){
//							escludiXBloccoROR = false;
//							break;
//					}
//				}
//			}
//		}
//		return escludiXBloccoROR;
//	}
//
//	//SIAC-8195 si sposta il controllo sulla parte sincrona dell'emissione
//	//SIAC-7470
//	protected Accertamento ricercaAccertamentoPerChiave(Accertamento accertamento) {
//		final String methodName = "ricercaAccertamentoPerChiave";
//		if(accertamento == null) {
//			log.debug(methodName, "Accertamento null");
//			return null;
//		}
//		final String key = accertamento.getAnnoMovimento() + "/" + accertamento.getNumero();
//		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = popolaCampiComuniRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento);
//		reqRAPCO.setCaricaSub(false);
//		return ottieniAccertamentoDaServizio(key, reqRAPCO);
//	}
//	
//	//SIAC-8195 si sposta il controllo sulla parte sincrona dell'emissione
//	//SIAC-7470
//	private Accertamento ottieniAccertamentoDaServizio(String key, RicercaAccertamentoPerChiaveOttimizzato reqRAPCO) {
//		final String methodName="ottieniAccertamentoDaServizio";
//		RicercaAccertamentoPerChiaveOttimizzatoResponse resRA = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(reqRAPCO);
//		if(!resRA.isFallimento() && resRA.hasErrori()) {
//			log.debug(methodName, "Errori nella response senza impostare il FALLIMENTO per la chiave " + key + ": impostazione del dato a mano");
//			resRA.setEsito(Esito.FALLIMENTO);
//		}
//		if(!resRA.isFallimento() && resRA.getAccertamento() == null) {
//			log.debug(methodName, "Nessun dato trovato per la chiave " + key + ". Aggiungo l'errore nella response");
//			resRA.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", key));
//			resRA.setEsito(Esito.FALLIMENTO);
//		}
//		checkServiceResponseFallimento(resRA);
//		Accertamento accertamentoRes = resRA.getAccertamento();
//		log.debug(methodName, "Data la chiave " + key + " trovata accertamento " + accertamentoRes.getUid());
//		//SIAC-5712
//		if(!reqRAPCO.isCaricaSub() && resRA.getElencoSubAccertamentiTuttiConSoloGliIds() != null) {
//			//non ho richiesto di caricare i subma ho comunque dei dati minimi in piu'
//			accertamentoRes.setSubAccertamenti(resRA.getElencoSubAccertamentiTuttiConSoloGliIds());
//		}
//		return accertamentoRes;
//	}
//	
//	//SIAC-8195 si sposta il controllo sulla parte sincrona dell'emissione
//	//SIAC-7470
//	private RicercaAccertamentoPerChiaveOttimizzato popolaCampiComuniRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento) {
//		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = new RicercaAccertamentoPerChiaveOttimizzato();
//		reqRAPCO.setRichiedente(req.getRichiedente());
//		reqRAPCO.setEnte(req.getRichiedente().getAccount().getEnte());
//		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
//		parametriElencoIds.setEscludiAnnullati(true);
//		//SIAC-5712
//		parametriElencoIds.setCaricaFlagAttivaGsa(true);
//		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
//		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
//		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
//		pRicercaAccertamentoK.setAnnoEsercizio(req.getBilancio().getAnno());
//		pRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
//		pRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumero());
//		reqRAPCO.setpRicercaAccertamentoK(pRicercaAccertamentoK);
//		reqRAPCO.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
//		reqRAPCO.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
//		reqRAPCO.setEscludiSubAnnullati(true);
//		return reqRAPCO;
//	}


}
