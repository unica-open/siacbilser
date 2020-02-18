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
import java.util.Map.Entry;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteDaEmettereSpesaService;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.OnereSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiPagamentoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiPagamentoMultiploResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiPagamentoSingolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiPagamentoSingoloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesaResponse;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;


/**
 * Consente di inserire una serie di ordinativi a fronte di un elenco di liquidazioni individuato in base ai parametri di input.
 * L'elaborazione deve poter essere lanciata da applicativo o schedulata. 
 * Il volume dei dati elaborati pu&ograve; raggiungere l'ordine della decina di migliaia.
 * <br/>
 * Analisi di riferimento: 
 * BIL--SIAC-FIN-SER-017-V01 - COMS003 Servizio Gestione Emissione Ordinativi.docx 
 * &sect;2.4
 * 
 * @author Domenico
 * @author Marchino Alessandro
 * @author Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmetteOrdinativiDiPagamentoDaElencoService extends EmetteOrdinativiDaElencoBaseService<EmetteOrdinativiDiPagamentoDaElenco, EmetteOrdinativiDiPagamentoDaElencoResponse> {

	@Autowired
	private RicercaQuoteDaEmettereSpesaService ricercaQuoteDaEmettereSpesaService;
	@Autowired
	private EmetteOrdinativoDiPagamentoSingoloService emetteOrdinativoDiPagamentoSingoloService;
	@Autowired
	private EmetteOrdinativoDiPagamentoMultiploService emetteOrdinativoDiPagamentoMultiploService;
	
	@Autowired
	private OnereSpesaDad onereSpesaDad;
	@Autowired
	private DocumentoDad documentoDad;
	
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
			for(SubdocumentoSpesa subdoc : req.getSubdocumenti()){
				checkEntita(subdoc, "subdoc/liquidazione");
			}
		}
	}

	@Override
	protected void init() {
		super.init();
		
		bilancio = req.getBilancio();
		contoTesoreria = req.getContoTesoreria() != null && req.getContoTesoreria().getUid() != 0 ? req.getContoTesoreria() : null;
		distinta = req.getDistinta() != null && req.getDistinta().getUid() != 0 ? req.getDistinta() : null;
		note = req.getNote();
		commissioniDocumento = req.getCommissioniDocumento();
		codiceBollo = req.getCodiceBollo() != null && req.getCodiceBollo().getUid() != 0 ? req.getCodiceBollo() : null;
		dataScadenza = req.getDataScadenza();
		flagNoDataScadenza = req.getFlagNoDataScadenza();
		//SIAC-6175
		flagDaTrasmettere = req.getFlagDaTrasmettere();
		//SIAC-6206
		classificatoreStipendi = req.getClassificatoreStipendi();
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 8)
	public EmetteOrdinativiDiPagamentoDaElencoResponse executeServiceTxRequiresNew(EmetteOrdinativiDiPagamentoDaElenco serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, timeout=AsyncBaseService.TIMEOUT * 8)
	public EmetteOrdinativiDiPagamentoDaElencoResponse executeService(EmetteOrdinativiDiPagamentoDaElenco serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
		
	}
	
	@Override
	protected void execute() {
		caricaBilancio();
		caricaCodiceBollo();
		caricaDistinta();
		caricaContoTesoreria();
		
		// Step4: Gestisce stato Elaborazione elenchi
		// TODO: gestione dello Stato Operativo Elaborazioni Asincrone
		
		// Step5: Ricerca documenti da incassare (non fa controlli adesso, carica solo quote da emettere da req o da servizio)
		List<SubdocumentoSpesa> quoteDaEmettere = caricaQuoteDaEmettere();
		
		startElaborazioneQuote(quoteDaEmettere);
		try {
			// Ordinativi singoli
			caricamentoOrdinativiSingoli(quoteDaEmettere);
			// Ordinativi multipli
			caricamentoOrdinativiMultipli(quoteDaEmettere);
		} finally {
			endElaborazioneQuote(quoteDaEmettere);
		}
		
		// Step9: Libera stato elaborazione elenchi
		// TODO: gestione dello Stato Operativo Elaborazioni Asincrone
	}

	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*-------------------------------------- CONTROLLI E FILTRI QUOTE DA EMETTERE -------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Prepara le quote da emettere a partire dalle varie modalit&agrave; di invocazione del servizio.
	 * 
	 * @return lista delle quote/liquidazioni da cui emettere gli ordinativi.
	 */
	private List<SubdocumentoSpesa> caricaQuoteDaEmettere() {
		final String methodName = "caricaQuoteDaEmettere";
		
		List<SubdocumentoSpesa> quoteDaEmettere = new ArrayList<SubdocumentoSpesa>();
		
		if(req.getSubdocumenti()==null || req.getSubdocumenti().isEmpty()){
			
			if(req.getElenchi()==null || req.getElenchi().isEmpty()) {
				//elaborazione tipo "ELABORA QUOTE AUTOMATICHE"
				if(req.getAllegatoAtto() != null && req.getAllegatoAtto().getUid() != 0){
					log.debug(methodName, "Ottengo le quote a partire dall'allegato atto");
					AttoAmministrativo aa = (req.getAllegatoAtto() != null && req.getAllegatoAtto().getUid() != 0) ? req.getAllegatoAtto().getAttoAmministrativo() : null;
					//quoteDaEmettere = ricercaQuoteDaEmettere(null, null, null, (aa!=null && aa.getUid() != 0) ? aa.getUid() : null,	aa != null ? aa.getAnno():null,	aa != null ? aa.getNumero():null);
					quoteDaEmettere = subdocumentoSpesaDad.ricercaSubdocumentiSpesaDaEmettereByAttoAmministrativo(
							(aa != null && aa.getUid() != 0) ? aa.getUid() : null,
							(aa != null && aa.getAnno() != 0) ? aa.getAnno() : null,
							(aa != null && aa.getNumero() != 0) ? aa.getNumero() : null);
				}else{
					//se non ho specificato un elenco o un allegato, voglio tutte le quote dell'ente
					log.debug(methodName, "Ottengo tutte le quote che si possono emettere");
					quoteDaEmettere = subdocumentoSpesaDad.ricercaSubdocumentiSpesaDaEmettereByEnte(Boolean.FALSE);
				}
				
			} else {
				//Lista elenchi popolata! elaborazione tipo "ELABORA ELENCHI MAN".//crf. passo 5 in analisi.
				log.debug(methodName, "Ottengo le quote a partire dall'elenco");
				List<SubdocumentoSpesa> subdocumentiSpesaTrovati = subdocumentoSpesaDad.ricercaSubdocumentiSpesaDaEmettereByElenco(req.getElenchi(),Boolean.TRUE);
				quoteDaEmettere.addAll(subdocumentiSpesaTrovati);
			}
			
		} else { //Lista liquidazioni popolata! elaborazione tipo: "ELABORA QUOTE MAN"
			for(SubdocumentoSpesa s : req.getSubdocumenti()){
				//se le quote arrivano dalla request non ho tutti i dati necessari, carico i dettagli
				s = subdocumentoSpesaDad.findSubdocumentoSpesaPerEmissione(s.getUid());
				quoteDaEmettere.add(s);
			}
		}
		//carico i dettagli delle liquidazioni e creo una mappa. Se più quote dovessero avere la stessa liquidazione evito di ricaricare i dettagli.
		//da aggiornare quando aggiorno emetto un ordinativo, potrebbe cambiare la diponibilità a liquidare
		//idem per documento padre
		for(SubdocumentoSpesa subdocumentoSpesa : quoteDaEmettere){
			Liquidazione liquidazione = caricaLiquidazione(subdocumentoSpesa);
			subdocumentoSpesa.setLiquidazione(liquidazione);
			DocumentoSpesa doc = caricaDocumento(subdocumentoSpesa);
			subdocumentoSpesa.setDocumento(doc);
		}
		
		log.info(methodName, "totale quote da emettere: "+ quoteDaEmettere.size());
		return quoteDaEmettere;
	}
	
	/**
	 * Carica i dettagli di una quota di spesa da emettere
	 * 
	 * @param s la quota
	 * @return la quota trovata
	 */
	private SubdocumentoSpesa caricaDatiQuota(SubdocumentoSpesa s) {
		//Pre-ottimizzazione
		//return subdocumentoSpesaDad.findSubdocumentoSpesaById(s.getUid());
		//ottimizzato
		return subdocumentoSpesaDad.findSubdocumentoSpesaPerEmissione(s.getUid());
	}

	/**
	 * Ricerca tutte le quote che possono essere emesse a partire da anno numero elenco.
	 * Il tipo convalida e la distinta passate in request vengono utilizzate per filtrare.
	 * 
	 * @param annoElenco
	 * @param numeroElenco
	 * @param annoProvvedimento
	 * @param numeroProvvedimento
	 * @return
	 */
	private List<SubdocumentoSpesa> ricercaQuoteDaEmettere(Integer uidElenco, Integer annoElenco, Integer numeroElenco, Integer uidProvvedimento,
			Integer annoProvvedimento, Integer numeroProvvedimento) {
		//TODO chiamare il DAD
		ListaPaginata<SubdocumentoSpesa> result = ricercaQuoteDaEmettere(0, uidElenco, annoElenco, numeroElenco, uidProvvedimento, annoProvvedimento,
				numeroProvvedimento);

		for (int i = 1; i < result.getTotalePagine(); i++) {
			ListaPaginata<SubdocumentoSpesa> page = ricercaQuoteDaEmettere(i, uidElenco, annoElenco, numeroElenco, uidProvvedimento, annoProvvedimento,
					numeroProvvedimento);
			result.addAll(page);
		}

		return result;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*----------------------------------------- GESTIONE ORDINATIVI SINGOLI -------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Caricamento degli ordinativi singoli.
	 * 
	 * @param quote      le quote da cui creare gli ordinativi singoli
	 */
	private void caricamentoOrdinativiSingoli(List<SubdocumentoSpesa> quote) {
		String methodName = "caricamentoOrdinativiSingoli";
		List<SubdocumentoSpesa> quoteDaEmettereConOrdinativoSingolo = filtraQuoteConFlagOrdinativoSingolo(quote, Boolean.TRUE);
		log.debug(methodName, "quote da emettere per ordinativo singolo = " + quoteDaEmettereConOrdinativoSingolo.size());
		ordinaQuoteDaEmettereOrdinativoSingolo(quoteDaEmettereConOrdinativoSingolo);
		log.debug(methodName, "quote da emettere per ordinativo singolo = " + quoteDaEmettereConOrdinativoSingolo.size());
		creaOrdinativiPagamento(quoteDaEmettereConOrdinativoSingolo);
	}
	
	/**
	 * Ordina i subdocumenti per:
	 * <ul>
	 *     <li>Soggetto intestatario della Liquidazione (nei casi di Cessione del Credito non coincide con quello del Documento)</li>
	 *     <li>Anno , tipo e numero Documento</li>
	 *     <li>importoDaPagare del Subdocumento discendente</li>
	 * </ul>
	 * 
	 * @param quoteDaEmettereConOrdinativoSingolo
	 */
	private void ordinaQuoteDaEmettereOrdinativoSingolo(List<SubdocumentoSpesa> quoteDaEmettereConOrdinativoSingolo) {
		Collections.sort(quoteDaEmettereConOrdinativoSingolo, new Comparator<SubdocumentoSpesa>(){

			@Override
			public int compare(SubdocumentoSpesa s1, SubdocumentoSpesa s2) {
				return new CompareToBuilder()
						// Soggetto intestatario della Liquidazione (nei casi di Cessione del Credito non coincide con quello del Documento)
						.append(s1.getLiquidazione().getSoggettoLiquidazione().getCodiceSoggetto(), s2.getLiquidazione().getSoggettoLiquidazione().getCodiceSoggetto())
						// Anno, tipo e numero Documento
						.append(s1.getDocumento().getAnno(), s1.getDocumento().getAnno())
						.append(s1.getDocumento().getTipoDocumento().getCodice(), s2.getDocumento().getTipoDocumento().getCodice())
						.append(s1.getDocumento().getNumero(), s2.getDocumento().getNumero())
						// importoDaPagare del Subdocumento discendente
						.append(s2.getImportoDaPagare(), s1.getImportoDaPagare())
						.toComparison();
			}
		});
	}
	
	/**
	 * Creazione degli ordinativi di pagamento a partire dalle quote.
	 * 
	 * @param quote le quote da emettere
	 * @return gli ordinativi di pagamento
	 */
	protected List<OrdinativoPagamento> creaOrdinativiPagamento(List<SubdocumentoSpesa> quote) {
		final String methodName = "creaOrdinativiPagamento";
		List<OrdinativoPagamento> ordinativi = new ArrayList<OrdinativoPagamento>();
		for(SubdocumentoSpesa subdoc : quote){
			
			OrdinativoPagamento ordinativo = emettiOrdinativoSingolo(subdoc);
			ordinativi.add(ordinativo);
		}
		log.debug(methodName, "Ordinativi creati: " + ordinativi.size());
		return ordinativi;
	}
		
	

	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*---------------------------------------- GESTIONE ORDINATIVI MULTIPLI -------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Caricamento degli ordinativi multipli.
	 * 
	 * @param quote      le quote da cui creare gli ordinativi multipli
	 * @param ordinativi gli ordinativi creati
	 */
	private void caricamentoOrdinativiMultipli(List<SubdocumentoSpesa> quote) {
		List<SubdocumentoSpesa> quoteDaEmettereConOrdinativoMultiplo = filtraQuoteConFlagOrdinativoSingolo(quote, Boolean.FALSE);
		log.debug("caricamentoOrdinativiMultipli", "quote da emettere per ordinativo multipli = " + quoteDaEmettereConOrdinativoMultiplo.size());
		Map<String, List<SubdocumentoSpesa>> gruppiQuoteDaEmettereConOrinativoMultiplo = raggruppaQuoteDaEmettereOrdinativoMultiplo(quoteDaEmettereConOrdinativoMultiplo);
		creaOrdinativiPagamento(gruppiQuoteDaEmettereConOrinativoMultiplo);
	}
	
	/**
	 * Raggruppa i dati per:
	 * <ul>
	 *     <li>Liquidazione:
	 *     <ul>
	 *         <li>Atto Amministrativo  => anno, numero, Tipo, Struttura Amministrativa</li>
	 *         <li>CapitoloUscita =>  numeroCapitolo, numeroArticolo,  Tipo Finanziamento (derivato dal MovimentoGestione)</li>
	 *         <li>MovimentoGestione.annoMovimento</li>
	 *         <li>Soggetto</li>
	 *         <li>ModalitaPagamentoSoggetto (da cui discende in modo indiretto anche la rottura per SedeSecondaria)</li>
	 *         <li>Conto di Tesoreria  => se impostato il corrispondente parametro dell'operazione non è significativo </li>
	 *         <li>Distinta   => se impostato il corrispondente parametro dell'operazione non è significativo</li>
	 *         <li>Siope tipo debito</li>
	 *     </ul></li>
	 *     <li>Subdocumento:
	 *     <ul>
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
	private Map<String, List<SubdocumentoSpesa>> raggruppaQuoteDaEmettereOrdinativoMultiplo(List<SubdocumentoSpesa> quoteDaEmettereConOrdinativoMultiplo) {
		final String methodName = "raggruppaQuoteDaEmettereOrdinativoMultiplo";
		Map<String, List<SubdocumentoSpesa>> result = new HashMap<String, List<SubdocumentoSpesa>>();
		
		for (SubdocumentoSpesa s : quoteDaEmettereConOrdinativoMultiplo) {
			Liquidazione l = s.getLiquidazione();

			String keyLiq = computeGroupKeyLiquidazione(l);
			String keySubdoc = computeGroupKeySubdocumento(s);

			String key = keyLiq + "_" + keySubdoc;
			
			if (!result.containsKey(key)) {
				List<SubdocumentoSpesa> lista = new ArrayList<SubdocumentoSpesa>();
				result.put(key, lista);
			}
			result.get(key).add(s);
			log.debug(methodName, "Aggiunta quota " + s.getUid() + " alla chiave " + key);
		}
		
		//Ordinemento dei gruppi individuati per anno e numero liquidazione.
		for(List<SubdocumentoSpesa> subdocs : result.values()){
			Collections.sort(subdocs, new Comparator<SubdocumentoSpesa>(){
				@Override
				public int compare(SubdocumentoSpesa s1, SubdocumentoSpesa s2) {
					return new CompareToBuilder()
							.append(s1.getLiquidazione().getAnnoLiquidazione(), s2.getLiquidazione().getAnnoLiquidazione())
							.append(s1.getLiquidazione().getNumeroLiquidazione(), s2.getLiquidazione().getNumeroLiquidazione())
							.toComparison();
				}
			});
		}
		
		return splitValueList(result, 30);
	}
	
	/**
	 * Suddivide una mappa di liste in una mappa di liste, in cui ciascuna lista &eacute; composta
	 * da al pi&ugrave; <code>size</code> elementi.
	 * @param original la mappa originale
	 * @param size la lunghezza massima delle liste sottostanti
	 * @return la mappa di liste, con ogni lista lunga al pi&ugrave; <code>size</code>
	 */
	private <T> Map<String, List<T>> splitValueList(Map<String, List<T>> original, int size) {
		Map<String, List<T>> res = new HashMap<String, List<T>>();
		
		for(Entry<String, List<T>> entry : original.entrySet()) {
			int entrySize = entry.getValue().size();
			int maxChunks = (entrySize + size - 1) / size;
			
			for(int chunkId = 0; chunkId < maxChunks; chunkId++) {
				List<T> newList = new ArrayList<T>();
				int limit = Math.min((chunkId + 1) * size, entrySize);
				for(int i = chunkId * size; i < limit; i++) {
					newList.add(entry.getValue().get(i));
				}
				res.put(entry.getKey() + "_chunk_" + chunkId, newList);
			}
		}
		
		return res;
	}
	
	/**
	 * Crea gli ordinativi di pagamento.
	 * 
	 * @param gruppiQuote i gruppi da emettere
	 * 
	 * @return gli ordinativi creati
	 */
	protected List<OrdinativoPagamento> creaOrdinativiPagamento(Map<String, List<SubdocumentoSpesa>> gruppiQuote) {
		String methodName = "creaOrdinativiPagamento";
		List<OrdinativoPagamento> ordinativi = new ArrayList<OrdinativoPagamento>();
		for(Map.Entry<String, List<SubdocumentoSpesa>> entry : gruppiQuote.entrySet()){
			log.debug(methodName, "Creo ordinativo per gruppo con chiave " + entry.getKey());
			OrdinativoPagamento ordinativo = emettiOrdinativoMultiploDaGruppoQuote(entry.getValue(), entry.getKey());
			ordinativi.add(ordinativo);
		}
		log.debug(methodName, "Ordinativi creati: " + ordinativi.size());
		return ordinativi;
	}
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*----------------------------- CALCOLI STRINGHE PER LE CHIAVI DI RAGGRUPPAMENTO ----------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Calcola la chiave delle liquidazioni.
	 * 
	 * @param liquidazione la liquidazione
	 * 
	 * @return la chiave
	 */
	private String computeGroupKeyLiquidazione(Liquidazione liquidazione) {
		final String separator = "_";
		StringBuilder key = new StringBuilder();
		
		// Compongo la stringa
		key.append(computeKeyAttoAmministrativo(liquidazione.getAttoAmministrativoLiquidazione()))
			.append(separator)
			.append(computeKeyCapitolo(liquidazione.getCapitoloUscitaGestione()))
			.append(separator)
			.append(computeKeyMovimentoGestione(liquidazione.getImpegno()))
			.append(separator)
			.append(computeKeySoggetto(liquidazione.getSoggettoLiquidazione()))
			.append(separator)
			.append(computeKeyModalitaPagamento(liquidazione.getModalitaPagamentoSoggetto()))
			.append(separator)
			.append(computeKeyPianoDeiContiFinanziario(liquidazione.getImpegno()))
			.append(separator)
			.append(computeKeyContoTesoreria(contoTesoreria != null ? contoTesoreria : contoTesoreriaBilFromContoTesoreriaFin(liquidazione.getContoTesoreria())))
			.append(separator)
			.append(computeKeyDistinta(distinta != null ? distinta :liquidazione.getDistinta()))
			.append(separator)
			.append(computeKeyTipoDebitoSiope(liquidazione.getSiopeTipoDebito()))
			.append(separator)
			.append(computeKeyCig(liquidazione.getCig()))
			.append(separator)
			.append(computeKeySiopeAssenzaMotivazione(liquidazione.getSiopeAssenzaMotivazione()))
			;
		
		return key.toString();
	}
	
	/**
	 * Calcola la chiave del subdocumento.
	 * 
	 * @param subdocumento il subdocumento
	 * 
	 * @return la chiave
	 */
	private String computeGroupKeySubdocumento(SubdocumentoSpesa subdocumento) {
		final String separator = "_";
		StringBuilder key = new StringBuilder();
		
		key.append(computeKeyFlagBeneficiarioMultiplo(subdocumento.getDocumento()))
			.append(separator)
			.append(computeKeyCodiceBollo(codiceBollo != null ? codiceBollo : subdocumento.getDocumento().getCodiceBollo()))
			.append(separator)
			.append(computeKeyFlagAvviso(subdocumento.getFlagAvviso()))
			.append(separator)
			.append(computeKeyNote(subdocumento.getNote()))
			.append(separator)
			.append(computeKeyCommissioniDocumento(commissioniDocumento != null ? commissioniDocumento : subdocumento.getCommissioniDocumento()))
			.append(separator)
			.append(computeKeyFlagACopertura(subdocumento.getFlagACopertura()))
			.append(computeKeyDataScadenza(dataScadenza != null ? dataScadenza : subdocumento.getDataScadenza()));
		
		return key.toString();
	}
	
	/**
	 * Calcola la chiave del flag beneficiario multiplo.
	 * 
	 * @param d il documento
	 * @return la chiave
	 */
	private String computeKeyFlagBeneficiarioMultiplo(DocumentoSpesa d) {
		final StringBuilder sb = new StringBuilder();
		// Documento.flagBeneficiarioMultiplo
		sb.append("flagBenefMult")
			.append("<")
			.append(d != null ? d.getFlagBeneficiarioMultiplo() : "null")
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave delle commissioni documento
	 * 
	 * @param cd le commissioni
	 * @return la chiave
	 */
	private String computeKeyCommissioniDocumento(CommissioniDocumento cd) {
		final StringBuilder sb = new StringBuilder();
		// Commissioni documento
		sb.append("commissDoc")
			.append("<")
			.append(cd != null ? cd.name() : "null")
			.append(">");
		return sb.toString();
	}
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*----------------------------------------------- GESTIONE RITENUTE -----------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	
	@Override
	protected <D extends Documento<?, ?>> boolean hasRitenute(D d) {
		final String methodName = "hasRitenute";
		DocumentoSpesa doc = (DocumentoSpesa) d;		
		List<DettaglioOnere> oneriCollegati = onereSpesaDad.findOneryByIdDocumento(doc.getUid());
		boolean hasRitenuteOneri =  oneriCollegati!= null && !oneriCollegati.isEmpty();
		log.debug(methodName, "Documento con uid: " + doc.getUid() + ". Ha ritenute? " + hasRitenuteOneri);
		return hasRitenuteOneri;
	}
	
	@Override
	protected <D extends Documento<?, ?>> boolean hasSubordinati(D documento) {
		final String methodName = "hasSubordinati";
		Long numeroDocumentiFigli =  documentoDad.countDocumentiFigli(documento.getUid());
		log.debug(methodName, "IL numero di documenti collegati e' pari a: " + numeroDocumentiFigli);
		return Long.valueOf(0L).compareTo(numeroDocumentiFigli)  < 0;
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
	private ListaPaginata<SubdocumentoSpesa> ricercaQuoteDaEmettere(int numeroPagina, Integer uidElenco, Integer annoElenco, Integer numeroElenco, Integer uidProvvedimento, Integer annoProvvedimento, Integer numeroProvvedimento) {
		RicercaQuoteDaEmettereSpesa reqRQDE = new RicercaQuoteDaEmettereSpesa();
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
		
		RicercaQuoteDaEmettereSpesaResponse resRQDE = serviceExecutor.executeServiceSuccess(ricercaQuoteDaEmettereSpesaService, reqRQDE);	
		return resRQDE.getListaSubdocumenti();
	}
	
	
	private OrdinativoPagamento emettiOrdinativoSingolo(SubdocumentoSpesa subdoc) {
		
		EmetteOrdinativoDiPagamentoSingolo eodps = new EmetteOrdinativoDiPagamentoSingolo();
		eodps.setSubdoc(subdoc);
		eodps.setFlagConvalidaManuale(req.getFlagConvalidaManuale());
		eodps.setSoggettiCache(soggettiCache);
		eodps.setDocumentiCache(documentiSpesaCache);
		eodps.setLiquidazioniCache(liquidazioniCache);
		eodps.setRichiedente(req.getRichiedente());
		eodps.setBilancio(bilancio);
		eodps.setDistinta(distinta);
		eodps.setContoTesoreria(contoTesoreria);
		eodps.setNote(note);
		eodps.setCodiceBollo(codiceBollo);
		eodps.setCommissioniDocumento(commissioniDocumento);
		eodps.setDataScadenza(dataScadenza);
		eodps.setFlagNoDataScadenza(flagNoDataScadenza);
		//SIAC-6175
		eodps.setFlagDaTrasmettere(flagDaTrasmettere);
		//SIAC-5937
		eodps.setBilancioAnnoSuccessivo(bilancioAnnoSuccessivo);
		eodps.setBilancioInDoppiaGestione(bilancioInDoppiaGestione);
		
		//SIAC-6206
		eodps.setClassificatoreStipendi(classificatoreStipendi);
		EmetteOrdinativoDiPagamentoSingoloResponse eodpsRes = emetteOrdinativoDiPagamentoSingoloService.executeServiceTxRequiresNew(eodps);
		
		if(eodpsRes.getMessaggio() != null){
			res.addMessaggio(eodpsRes.getMessaggio());
		}
		if(eodpsRes.getSubdocumentoScartato() != null){
			res.getSubdocumentiScartati().add(eodpsRes.getSubdocumentoScartato());
		}
		if(eodpsRes.hasErrori()){
			for(Errore errore : eodpsRes.getErrori()){
				res.addMessaggio(new Messaggio(errore.getCodice(), "Emissione ordinativo per subdocumento " + subdoc.getUid() + ": "+  errore.getDescrizione()));
			}
		}
		if(eodpsRes.isFallimento()){
			return null;
		}else{
			soggettiCache = eodpsRes.getSoggettiCache();
			documentiSpesaCache = eodpsRes.getDocumentiCache();
			liquidazioniCache = eodpsRes.getLiquidazioniCache();
			return eodpsRes.getOrdinativo();
		}
		
		
	}
	
	
	private OrdinativoPagamento emettiOrdinativoMultiploDaGruppoQuote(List<SubdocumentoSpesa> list, String chiave) {

		EmetteOrdinativoDiPagamentoMultiplo eodpm = new EmetteOrdinativoDiPagamentoMultiplo();
		eodpm.setQuote(list);
		eodpm.setFlagConvalidaManuale(req.getFlagConvalidaManuale());
		eodpm.setSoggettiCache(soggettiCache);
		eodpm.setDocumentiCache(documentiSpesaCache);
		eodpm.setLiquidazioniCache(liquidazioniCache);
		eodpm.setRichiedente(req.getRichiedente());
		eodpm.setBilancio(bilancio);
		eodpm.setDistinta(distinta);
		eodpm.setContoTesoreria(contoTesoreria);
		eodpm.setNote(note);
		eodpm.setCommissioniDocumento(commissioniDocumento);
		eodpm.setCodiceBollo(codiceBollo);
		eodpm.setDataScadenza(dataScadenza);
		eodpm.setFlagNoDataScadenza(flagNoDataScadenza);
		//SIAC-6175
		eodpm.setFlagDaTrasmettere(flagDaTrasmettere);
		//SIAC-5937
		eodpm.setBilancioAnnoSuccessivo(bilancioAnnoSuccessivo);
		eodpm.setBilancioInDoppiaGestione(bilancioInDoppiaGestione);
		//SIAC-6206
		eodpm.setClassificatoreStipendi(classificatoreStipendi);
		
		EmetteOrdinativoDiPagamentoMultiploResponse eodpmRes = emetteOrdinativoDiPagamentoMultiploService.executeServiceTxRequiresNew(eodpm);
		
		if(eodpmRes.getMessaggi() != null && !eodpmRes.getMessaggi().isEmpty()){
			res.addMessaggi(eodpmRes.getMessaggi());
		}
		if(eodpmRes.getSubdocumentiScartati() != null && !eodpmRes.getSubdocumentiScartati().isEmpty()){
			res.getSubdocumentiScartati().addAll(eodpmRes.getSubdocumentiScartati());
		}
		if(eodpmRes.hasErrori()){
			for(Errore errore : eodpmRes.getErrori()){
				res.addMessaggio(new Messaggio(errore.getCodice(), "Emissione ordinativo per gruppo di quote con chiave " + chiave + ": "+  errore.getDescrizione()));
			}
		}
		if(eodpmRes.isFallimento()){
			return null;
		}else{
			soggettiCache = eodpmRes.getSoggettiCache();
			documentiSpesaCache = eodpmRes.getDocumentiCache();
			liquidazioniCache = eodpmRes.getLiquidazioniCache();
			return eodpmRes.getOrdinativo();
		}
	}
	
	
}
