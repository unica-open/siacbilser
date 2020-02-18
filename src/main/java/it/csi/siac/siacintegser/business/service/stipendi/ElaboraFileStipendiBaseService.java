/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaPuntualeCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaPuntualeCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.documento.DocumentoServiceCallGroup;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.business.service.documento.SoggettoServiceCallGroup;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmTipoEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siaccommon.util.threadlocal.ThreadLocalUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.utility.threadlocal.UseClockTimeThreadLocal;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaAccSubAcc;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.business.service.stipendi.cache.ModalitaPagamentoSoggettoCacheInitializer;
import it.csi.siac.siacintegser.business.service.stipendi.comparator.ComparatorStipendioPrimoLivello;
import it.csi.siac.siacintegser.business.service.stipendi.comparator.ComparatorStipendioSecondoLivello;
import it.csi.siac.siacintegser.business.service.stipendi.factory.StipendioFactory;
import it.csi.siac.siacintegser.business.service.stipendi.model.Stipendio;
import it.csi.siac.siacintegser.business.service.stipendi.model.StipendioParams;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

/**
 * @author Nazha Ahmad
 *
 * Descrizione breve del servizio :
 * 1-Il servizio legge i record  da un file di input ,ci sono 4 tipi di record ritenute,stipendi lordi,recuperi e oneri
 * 2-In base al tipo di elaborazione filtra i record da elaborare
 * 3-Effettua i controlli su bilancio,movimenti,capitolo e soggetto  presenti nei record
 * 4-Ragruppa gli stipendi
 * 5-Inserisce o aggiorna un documento spesa/entrata con eventuali quote
 * 6-Inserisce degli elenchi documento allegato per tipologia Entrate,Spese,Oneri/Ritenute
 *
 * @version 1.0.0
 * @Version 1.0.1 luglio 2015
 * @Version 1.1.0 luglio 2016 (CR-3838)
 */
public abstract class ElaboraFileStipendiBaseService extends ElaboraFileBaseService {

	private static final Pattern PATTERN_RITENUTE = Pattern.compile(".*?Rit\\. (\\d+).*?");

	@Autowired
	protected MovimentoGestioneService movimentoGestioneService;
	@Autowired
	protected ProvvedimentoService provvedimentoService;
	@Autowired
	private RicercaPuntualeCapitoloUscitaGestioneService ricercaPuntualeCapitoloUscitaGestioneService;
	@Autowired
	private RicercaPuntualeCapitoloEntrataGestioneService ricercaPuntualeCapitoloEntrataGestioneService;
	private DocumentoServiceCallGroup dscg;
	private SoggettoServiceCallGroup sscg;
	private MovimentoGestioneServiceCallGroup mgscg;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private StipendioFactory stipendioFactory;
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	@Autowired
	private DocumentoDad documentoDad;

	private final Map<String, BigDecimal> mappaMovimentiDisponibilita = new HashMap<String, BigDecimal>();
	private final Map<Integer, Bilancio> mappaBilancio = new HashMap<Integer, Bilancio>();
	private final Map<String, List<Subdocumento<?, ?>>> mappaListaSubDocumenti = new HashMap<String, List<Subdocumento<?, ?>>>();
	private final Map<String, Long> mappaProgressiviRitenute = new HashMap<String, Long>();
	private final List<Stipendio> stipendi = new ArrayList<Stipendio>();

	// Cache
	private final Cache<String, ModalitaPagamentoSoggetto> cacheModalitaPagamentoSoggetto = new MapCache<String, ModalitaPagamentoSoggetto>();
	private ModalitaPagamentoSoggettoCacheInitializer modalitaPagamentoSoggettoCacheInitializer;

	private AttoAmministrativo attoAmministrativo;
	private String nomeFile;
	private int numero;
	private Counter counter;
	private static final int ACCERTAMENTO_SUB_VALIDO= 1;
	private static final int ACCERTAMENTO_SENZA_DISPONIBILITA = 2;
	private static final int SUBACCERTAMENTO_SENZA_DISPONIBILITA = 3;
	private static final int CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO= 4;

	private static final UseClockTimeThreadLocal USE_CLOCK_TIME = (UseClockTimeThreadLocal) ThreadLocalUtil.registerThreadLocal(UseClockTimeThreadLocal.class);

	@Override
	public void checkServiceParam() throws ServiceParamError {
		// Espongo il servizio all'esterno
		super.checkServiceParam();
	}

	@Override
	protected void init() {
		super.init();
		USE_CLOCK_TIME.set(Boolean.TRUE);
		soggettoDad.setEnte(ente);
		documentoDad.setEnte(ente);
		documentoDad.setLoginOperazione(loginOperazione);
		dscg = new DocumentoServiceCallGroup(serviceExecutor, req.getRichiedente(), req.getEnte(), req.getBilancio());
		sscg = new SoggettoServiceCallGroup(serviceExecutor, req.getRichiedente(), req.getEnte());
		mgscg = new MovimentoGestioneServiceCallGroup(serviceExecutor, req.getRichiedente(), req.getEnte(), req.getBilancio());

		// Cache
		modalitaPagamentoSoggettoCacheInitializer = new ModalitaPagamentoSoggettoCacheInitializer(soggettoDad);
		// SIAC-4956
		nomeFile = req.getFile().getNome();

		counter = new Counter(nomeFile);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 3)
	public ElaboraFileResponse executeServiceTxRequiresNew(ElaboraFile serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}

	@Override
	protected void initFileData() {
		final String methodName = "initFileData";

		log.debug(methodName, "(nome file: " + nomeFile + ") lunghezza del file: " + fileBytes.length);

		LineIterator it;
		try {
			it = IOUtils.lineIterator(new ByteArrayInputStream(fileBytes), "UTF-8");
		} catch (IOException e) {
			log.error("(nome file: " + nomeFile + ") Impossibile leggere il file", e);
			throw new BusinessException("Impossibile leggere il file (nome file: " + nomeFile + "): " + e.getMessage());
		}

		try {
			int lineNumber = 0;
			while (it.hasNext()) {
				String line = it.nextLine();
				lineNumber++;
				/*
				 * ndA: ho aggiunto il trim. In caso contrario, avrei la seguente eccezione:
				 * Errore di runtime nell'esecuzione del Servizio.
				 * java.lang.StringIndexOutOfBoundsException: String index out of range: 21
				 *     at java.lang.String.substring(String.java:1934)
				 *     at it.csi.siac.siacintegser.business.service.stipendi.factory.StipendioFactory.newInstanceFromFlussoStipendi(StipendioFactory.java:46)
				 *     at it.csi.siac.siacintegser.business.service.stipendi.ElaboraFileStipendiBaseService.initFileData(ElaboraFileStipendiBaseService.java:165)
				 *     [...]
				 */
				if (StringUtils.isBlank(line)) {
					continue;
				}
				try{
					Stipendio stipendio = stipendioFactory.newInstanceFromFlussoStipendi(line);
					stipendio.setFileLineNumber(lineNumber);
					stipendi.add(stipendio);
				} catch (RuntimeException re){
					String msg = "Scartata la linea  " + (counter.righe + 1) + " (nome file: " + nomeFile + "), " + re.getMessage();
					log.warn(methodName, msg + line, re);
					res.addMessaggio("STIPENDIO_SCARTATO", msg);
					counter.addRigaScartata();
				}

				counter.addRiga();
			}
		} finally {
			it.close();
			log.info(methodName, "(nome file: " + nomeFile + ") LineIterator successfully closed.");
		}

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService#elaborateData()
	 */
	@Override
	protected void elaborateData() {
		try {
			elaborateDataStipendi();
		} finally {
			aggiungiMessaggioDiRiepilogo();
		}
	}

	private void elaborateDataStipendi() {
		String methodName = "elaborateData";
		List<Stipendio> stipendiDaEliminare = new ArrayList<Stipendio>();

		log.info(methodName, "numero stipendi da elaborare: " + stipendi.size());

		for (Stipendio stipendio : stipendi) {
			// Forza l'Ente dello Stipendio con quello del Richiedente del
			// servizio supponendo che ogni flusso sia relativo ad un solo Ente.
			stipendio.setEnte(ente);
			// verifico se i campi sono valorizzati
			checkAnnoAndMeseElaborazione(stipendio);
			checkVoceContabile(stipendio);
			checkImportoEntrataOSpesaValorizzato(stipendio);
		}

		// inserimento
		for (Stipendio stipendio : stipendi) {
			numero = (stipendi.indexOf(stipendio) + 1);
			//log.info(methodName, "Elaborazione Stipendio: " + numero);
			log.info(methodName, "Elaborazione Stipendio: " + numero);
			boolean elaborato = elaborateStipendio(stipendio);
			if (!elaborato) {
				stipendiDaEliminare.add(stipendio);
				counter.addRigaScartata();

				String msg = "\n(nome file: " + nomeFile + ") Elaborazione Flusso Stipendi. Scartato Stipendio: " + numero
						+ ". Dati Stipendio:" + " anno/mese elaborazione: " + stipendio.getAnnoElaborazione() + "/"
						+ stipendio.getMeseElaborazione() + " importoSpesa: " + stipendio.getImportoSpesa()
						+ " importoEntrata: " + stipendio.getImportoEntrata() + " soggetto: "
						+ stipendio.getSoggetto().getCodiceSoggetto();

				log.info(methodName, msg);
				Messaggio m = new Messaggio();
				m.setCodice(StipendioParams.CODICE_STIPENDIO_SCARTATO);
				m.setDescrizione(msg);
				res.addMessaggio(m);
			}
		}
		// elimina dalla lista i record da scartare
		cleanStipendi(stipendiDaEliminare);
		log.debug(methodName, "Numero stipendi da elaborare dopo l'eliminazione di quelli non validi : " + stipendi.size());

		//PER I TEST
//		Wrapper wrapper = new Wrapper();
//		wrapper.stipendi = stipendi;
//		log.logXmlTypeObject(wrapper, "STIPENDI!! WAWA!!");
//		wrapper = null;
		
		//Ordinamento di primo e secondo livello
		ordinaStipendi();
		
		//ragruppamento degli stipendi
		Map<String, List<Stipendio>> stipendiRaggruppati = raggruppaStipendi();

		//stampo gli stipendi ----
		stampaStipendiRagruppati(stipendiRaggruppati);

		int stipendiRaggruppatiSize = stipendiRaggruppati.size();
		int i = 1;
		for (List<Stipendio> gruppoStipendi : stipendiRaggruppati.values()) {

			//log.info(methodName, computaChiaveInserimentoDati(gruppoStipendi, i, stipendiRaggruppatiSize));
			log.info(methodName, computaChiaveInserimentoDati(gruppoStipendi, i, stipendiRaggruppatiSize));
			inserisciDocumentoConQuote(gruppoStipendi);
			i++;
		}

		//Inserisce gli elenchi al Max saranno presenti 3 elenchi 1-SPESE,2-ENTRATE,3-ONERI/RITENUTE
		inserisciElenchiDocumentiAllegato();
		//aggiunge un messaggio alla response con delle info relative alla elaborazione
		aggiungiMessaggioDiRiepilogo();
	}
	
	//PER I TEST: lasciare commentato in modo da poter effettuare un marshal dei dati
//	@XmlType
//	public static class Wrapper {
//		@XmlElementWrapper(name="stipendi")
//		@XmlElement(name="stipendio")
//		public List<Stipendio> stipendi = new ArrayList<Stipendio>(); 
//	}
	
	private String computaChiaveInserimentoDati(List<Stipendio> gruppoStipendi, int i, int stipendiRaggruppatiSize) {
		List<String> lines = new ArrayList<String>();
		for(Stipendio s : gruppoStipendi) {
			lines.add(Integer.toString(s.getFileLineNumber()));
		}

		return new StringBuilder()
			.append("Inserimento dati per stipendio ")
			.append(i)
			.append(" su ")
			.append(stipendiRaggruppatiSize)
			.append(" (stipendi corrispondenti alle righe ")
			.append(StringUtils.join(lines, ", "))
			.append(")")
			.toString();
	}

    /**
     * stampa
     * @param stipendi gli stipendi
     */
	private void stampaListaStipendio(List<Stipendio> stipendi) {
		String methodName = "stampaListaStipendio";
		if(log.isDebugEnabled()){
			String intestazione = " tipo voce "+" spesa/entrata "+" capitolo(numero,articolo,UEB) "+" SAC "+" soggetto "+
								" tipo record "+" accertamento anno numero sub "+" impegno anno numero  ";
			log.debug(methodName, intestazione);
			for(Stipendio s : stipendi){
				StringBuilder sb= new StringBuilder();
				sb.append("    "+s.getTipoVoce().getCodice()+"    ");
				sb.append(" - ");
				sb.append(s.isSpesa()?"  SPESA    ":"  ENTRATA  ");
				sb.append(" - ");
				sb.append(s.getCapitolo()!=null ? "     "+s.getCapitolo().getAnnoCapitolo():" anno");
				sb.append(s.getCapitolo()!=null ? "     "+s.getCapitolo().getNumeroCapitolo():" numero  ");
				sb.append(s.getCapitolo()!=null ? "     "+s.getCapitolo().getNumeroArticolo():"  numero  ");
				sb.append(s.getCapitolo()!=null ? "     "+s.getCapitolo().getNumeroUEB():"  UEB  ");
				sb.append("  -  ");
				sb.append(s.getStrutturaAmministrativoContabile() !=null ? "   "+s.getStrutturaAmministrativoContabile().getCodice():"   SAC   ");
				sb.append("  -  ");
				sb.append(s.getSoggetto()!=null ? "    "+s.getSoggetto().getCodiceSoggetto():"  Soggetto  ");
				sb.append("  -  ");
				sb.append(s.getTipoRecord().getCodice());
				sb.append("  -  ");
				//ACCERTAMENTO SUBACCERTAMENTO
				sb.append(s.getAccertamento()!=null ?"   "+s.getAccertamento().getAnnoMovimento():"  anno  ");
				sb.append(s.getAccertamento()!=null ?" "+s.getAccertamento().getNumero():"  numero  ");
				sb.append(s.getSubAccertamento()!=null ?" "+ s.getSubAccertamento().getNumero():"  sub  ");

				//IMPEGNO  SUBIMPEGNO
				sb.append("    -    ");

				sb.append(s.getImpegno()!=null ?"   "+s.getImpegno().getAnnoMovimento():"  anno  ");
				sb.append(s.getImpegno()!=null ?"  "+s.getImpegno().getNumero():"  numero  ");
				sb.append(s.getSubImpegno()!=null ?"  "+ s.getSubImpegno().getNumero():"  sub  ");
				log.debug(methodName, sb.toString());

			}
		}
	}

	/**
	 * stampa gli stipendi presenti in mappa e ragruppati
	 * @param stipendiRaggruppati
	 */
	private void stampaStipendiRagruppati(Map<String, List<Stipendio>> stipendiRaggruppati) {
		if(!log.isDebugEnabled()){
			return;
		}
		for (Entry<String, List<Stipendio>> entry : stipendiRaggruppati.entrySet()) {
			String key = entry.getKey();
			List<Stipendio> value = entry.getValue();
			log.debug("","#######################STIPENDI RAGGRUPPATI MAPPA ###################################################");
			log.debug("KeyStipendio", "key: "+key);
			stampaListaStipendio(value);
		}
	}

	/**
     * <font color='blue'><b>Inserisce un elenco dei documenti allegato per ogni tipologia: ENTRATE. SPESE, ONERI/RITENUTE: </b></font><br />
	 */
	private void inserisciElenchiDocumentiAllegato() {
		String methodName = "inserisciElenchiDocumentiAllegato";
		log.debug(methodName, "inserisciElenchiDocumentiAllegato INIZIO....");

		for (Entry<String, List<Subdocumento<?, ?>>> entry : mappaListaSubDocumenti.entrySet()) {
			inserisciElencoDocumentiAllegato(entry);
		}

		log.debug(methodName, "inserisciElenchiDocumentiAllegato FINE....");
	}

	/**
     * <font color='blue'><b>Inserisce un elenco dei documenti allegato per ogni tipologia: ENTRATE. SPESE, ONERI/RITENUTE: </b></font><br />
	 * @param entry
	 */
	private void inserisciElencoDocumentiAllegato(Entry<String, List<Subdocumento<?, ?>>> entry) {
		String methodName = "inserisciElencoDocumentiAllegato";

		String key = entry.getKey();
		List<Subdocumento<?, ?>> value = entry.getValue();
		log.debug(methodName, "Inserisco i l'elenco con i subdocumenti del tipo :"+key);
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setSubdocumenti(value);
		elencoDocumentiAllegato.setEnte(ente);
		// anno del bilancio......
		elencoDocumentiAllegato.setAnno(req.getBilancio().getAnno());
		dscg.setBilancio(req.getBilancio());
		dscg.setEnte(ente);
		InserisceElencoResponse response = dscg.inserisceElencoDocumentiAllegatoAtto(elencoDocumentiAllegato);
		checkServiceResponseErrore(response);
		addCounterNumeroElencoDocumentiAllegatoPerTipologia(response,key);

		log.logXmlTypeObject(response, "Risposta ottenuta dal servizio InserisceElencoService");
	}

	/**
	 * utilizzata per
	 * @param response
	 * @param key
	 */
	private void addCounterNumeroElencoDocumentiAllegatoPerTipologia(InserisceElencoResponse response, String key) {
		if(response == null || response.getElencoDocumentiAllegato() == null || response.getElencoDocumentiAllegato().getUid() == 0){
			return;
		}
		//serve per individuare il tipo di elenco
		switch (key.charAt(0)) {
		case 'S'://spese
			counter.setNumeroElencoSpese(response.getElencoDocumentiAllegato().getNumero());
			break;
		case 'E'://entrate
			counter.setNumeroElencoEntrate(response.getElencoDocumentiAllegato().getNumero());
			break;
		case 'O'://oneri/ritenute
			counter.setNumeroElencoOneriRitenute(response.getElencoDocumentiAllegato().getNumero());
			break;
		default:
			break;
		}

	}

	/**
     * <font color='blue'><b>Controlla se l' anno e il mese di elaborazione sono valorizzati </b></font><br />
	 * @param stipendio
	 */
	private void checkAnnoAndMeseElaborazione(Stipendio stipendio) {

		checkCondition(stipendio.getAnnoElaborazione() > 0, "Anno Di Elaborazione",stipendio);
		checkCondition(stipendio.getMeseElaborazione() > 0, "Mese Di Elaborazione",stipendio);

	}

	/**
     * <font color='blue'><b>Controlla se la condizione passata non &egrave valida lancia un errore  </b></font><br />
	 * @param nomeParametro
	 */
	protected void checkCondition(boolean condition, String nomeParametro,Stipendio stipendio) {
		if (!condition) {
			sendMessaggioConNumeroStipendioInElaborazione("",stipendio,false);
			throw new IllegalArgumentException("Il parametro " + nomeParametro + " e' obbligatorio , e deve essere diverso da zero");

		}

	}

	/**
     * <font color='blue'><b>Controlla che sia valorizzato il campo voce contabile </b></font><br />
	 * @param stipendio
	 */
	protected void checkVoceContabile(Stipendio stipendio) {
		if ("0".equals(stipendio.getVoceContabile())) {
			sendMessaggioConNumeroStipendioInElaborazione("",stipendio,false);
			throw new IllegalArgumentException("Il parametro VoceContabile e' obbligatorio");
		}

	}


	/**
	 * <font color='blue'><b>Controlla gli importi di spesa e di entrata </b></font><br />
	 * deve essere valorizzato uno dei 2
	 *
	 * @param stipendio
	 */
	protected void checkImportoEntrataOSpesaValorizzato(Stipendio stipendio) {
		if (stipendio.getImportoSpesa().signum() == 0 && stipendio.getImportoEntrata().signum() == 0) {
			sendMessaggioConNumeroStipendioInElaborazione("",stipendio,false);
			throw new IllegalArgumentException("Per ogni record deve essere valorizzato (diverso da zero) l'importo di spesa o l'importo di entrata.");
		}
	}

	/**
     * <font color='blue'><b>Inserisce un gruppo di stipendi </b></font><br />
     *  <ul>
     *      <li>richiama il methodo {@link #inserisciDocumentoDiSpesaConQuote} Se lo stipendio &egrave di Spesa</li>
     *  </ul>
     *  <ul>
     *      <li>richiama il methodo {@link #inserisciDocumentoDiEntrataConQuote} Se lo stipendio &egrave di Entrata</li>
     *  </ul>
	 * @param gruppoStipendi
	 *            --->corrisponde ad un documento con un insieme di quote
	 */
	private void inserisciDocumentoConQuote(List<Stipendio> gruppoStipendi) {
		if(gruppoStipendi.isEmpty()) {
			return;
		}

		if (gruppoStipendi.get(0).isSpesa() && !gruppoStipendi.get(0).isTipoRecordRitenute()) {
			inserisciDocumentoDiSpesaConQuote(gruppoStipendi);
		} else if(gruppoStipendi.get(0).isEntrata() && !gruppoStipendi.get(0).isTipoRecordRitenute()) {
			inserisciDocumentoDiEntrataConQuote(gruppoStipendi);
		}else if(gruppoStipendi.get(0).isTipoRecordRitenute()){
			inserisciDocumentiPerTipoRitenute(gruppoStipendi);
		}

	}
	/**
	 * gestione dei record di tipo ritenute
	 * @param gruppoStipendi
	 */
	private void inserisciDocumentiPerTipoRitenute(List<Stipendio> gruppoStipendi) {
		//elaboro sia spesa che entrata
		if (isTipoElaborazioneTUTTO()) {
			inserisciCollegaDocumentiSpesaEntrataMonoQuota(gruppoStipendi,true,true);
			return;
		}

		//elaboro solo la parte entrata delle ritenute
		if (isTipoElaborazioneSTIPE()) {
			inserisciCollegaDocumentiSpesaEntrataMonoQuota(gruppoStipendi,true,false);
			return;
		}

		if (isTipoElaborazioneONERI()) {
			// Elaboro solo la parte spesa delle ritenute
			inserisciCollegaDocumentiSpesaEntrataMonoQuota(gruppoStipendi,false,true);
			return;
		}
	}

	/**
	 * Inserisce gli stipendi di entrata o spesa in base ai flag
	 * il collegamento tra spesa ed entrata viene effettuato solo se si effettua l'elaborazione di tutti i due i record
	 * ovvero solo nel caso di una elaborazione di tutto il flusso
	 * @param gruppoStipendi
	 */
	private void inserisciCollegaDocumentiSpesaEntrataMonoQuota(List<Stipendio> gruppoStipendi,boolean elaboraEntrata,boolean elaboraSpesa) {
		String methodName ="inserisciCollegaDocumentiSpesaEntrataMonoQuota";
		//Per ogni record di tipo ritenuta
		for(Stipendio s : gruppoStipendi){
			DocumentoEntrata documentoEntrataInseritoAggiornato = elaboraEntrata ? inserisciAggiornaDocumentoEntrataMonoQuota(s):null;
			DocumentoSpesa documentoSpesaInseritoAggiornato = elaboraSpesa ? inserisciAggiornaDocumentoSpesaMonoQuota(s):null;
			if(documentoEntrataInseritoAggiornato !=null && documentoSpesaInseritoAggiornato!=null && documentoEntrataInseritoAggiornato.getUid() != 0 && documentoSpesaInseritoAggiornato.getUid() != 0){
				log.info(methodName, "Collego il documento di spesa "+documentoSpesaInseritoAggiornato.getUid()+ " al documento di entrata "+documentoEntrataInseritoAggiornato.getUid()+ " Con una relazione del tipo SUBORDINATO ");
				documentoDad.collegaDocumenti(documentoSpesaInseritoAggiornato, documentoEntrataInseritoAggiornato, TipoRelazione.SUBORDINATO);
			}
		}

	}

	/**
	 * inserisce un documento di spesa con una sola quota
	 * @param stipendioSpesa lo stipendio
	 * @return il documento
	 */
	private DocumentoSpesa inserisciAggiornaDocumentoSpesaMonoQuota(Stipendio stipendioSpesa) {
		String methodName = "inserisciAggiornaDocumentoSpesaMonoQuota";
		log.debug(methodName, "inserisciAggiornaDocumentoSpesaMonoQuota INIZIO....");

		DocumentoSpesa doc = creaDocumentoDiSpesa(stipendioSpesa);

		doc.setImporto(calcolaImportoSpesa(stipendioSpesa));

		DocumentoSpesa documentoSpesa = inserisceOAggiornaDocumentoSpesaService(doc);

		log.info(methodName, "Inserimento quota corrispondente alla linea " + stipendioSpesa.getFileLineNumber() + ": \"" + stipendioSpesa.getFileLineContent() + "\"");
		SubdocumentoSpesa subdocumentoSpesa = inserisceQuotaDocumentoSpesa(documentoSpesa, stipendioSpesa,true);
		counter.addSubdocumentoSpesa();
		inserisciSubDocumentoNellaMappaSubdocumenti(stipendioSpesa,subdocumentoSpesa);
		log.debug(methodName, "inserisciAggiornaDocumentoSpesaMonoQuota FINE....");
		return documentoSpesa;
	}

	private BigDecimal calcolaImportoSpesa(Stipendio stipendio) {
		if(stipendio.isTipoRecordRitenute()) {
			return stipendio.getImportoEntrata();
		}
		return stipendio.getImportoSpesa();
	}

	/**
	 * computeKeySubdocumentiPerRecord
	 * @param stipendio lo stipendio
	 * @return la chiave
	 */
	private String computeKeySubdocumentiPerRecord(Stipendio stipendio) {

		if(stipendio.isTipoRecordOneri() || stipendio.isTipoRecordRitenute()){
			return "ONERI_RITENUTE";
		}
		if(stipendio.isTipoRecordRecuperi()){
			return "ENTRATE";
		}
		return "SPESE";
	}

	/**
	 * inserisce o aggiorna un documento di entrata con una sola quota
	 * @param stipendioEntrata lo stipendio
	 * @return il documento
	 */
	private DocumentoEntrata inserisciAggiornaDocumentoEntrataMonoQuota(Stipendio stipendioEntrata) {

		String methodName = "inserisciAggiornaDocumentoEntrataMonoQuota";
		log.debug(methodName, "inserisciAggiornaDocumentoEntrataMonoQuota INIZIO....");


		DocumentoEntrata doc = creaDocumentoEntrata(stipendioEntrata);
		doc.setImporto(stipendioEntrata.getImportoEntrata());

		DocumentoEntrata documentoEntrata = inserisceOAggiornaDocumentoEntrataService(doc);
		log.info(methodName, "Inserimento quota corrispondente alla linea " + stipendioEntrata.getFileLineNumber() + ": \"" + stipendioEntrata.getFileLineContent() + "\"");
	    SubdocumentoEntrata subdocEntrata = inserisceQuotaDocumentoEntrata(documentoEntrata, stipendioEntrata,true);
	    counter.addSubdocumentoEntrata();
	    inserisciSubDocumentoNellaMappaSubdocumenti(stipendioEntrata,subdocEntrata);
		log.debug(methodName, "inserisciAggiornaDocumentoEntrataMonoQuota FINE....");

		return documentoEntrata;
	}

	/**
	 * <font color='blue'><b>Inserisce un documento di Entrata </b></font><br />
	 *  Popola il documento di Entrata necessario <br />
	 *  Cerca il tipo di documento per il codice di famiglia
     *  <ul>
     *      <li>richiama il methodo {@link #inserisciQuoteDocumentoDiEntrata} </li>
     *  </ul>
     *  <ul>
     *      <li>richiama il methodo {@link #inserisceOAggiornaDocumentoEntrataService} </li>
     *  </ul>
	 * @param gruppoStipendi
	 */
	private void inserisciDocumentoDiEntrataConQuote(List<Stipendio> gruppoStipendi) {
		String methodName = "inserisciDocumentoDiEntrataConQuote";
		log.debug(methodName, "inserisciDocumentoDiEntrataConQuote INIZIO....");

		Stipendio stipendioEntrata = gruppoStipendi.get(0);

		DocumentoEntrata doc = creaDocumentoEntrata(stipendioEntrata);
		doc.setImporto(calcolaTotaleImportoDocumentoDiEntrata(gruppoStipendi));

		DocumentoEntrata documentoEntrata = inserisceOAggiornaDocumentoEntrataService(doc);
		inserisciQuoteDocumentoDiEntrata(documentoEntrata, gruppoStipendi);

		log.debug(methodName, "inserisciDocumentoDiEntrataConQuote FINE....");

	}

	/**
	 * crea un documento di entrata a partire dallo stipendio
	 * @param stipendioEntrata
	 * @return il documento
	 */
	private DocumentoEntrata creaDocumentoEntrata(Stipendio stipendioEntrata) {
		DocumentoEntrata doc = new DocumentoEntrata();
		doc.setEnte(stipendioEntrata.getEnte());
		doc.setAnno(stipendioEntrata.getAnnoElaborazione());
		doc.setNumero(computeNumeroDocumentoFromStipendio(stipendioEntrata, Boolean.FALSE));
		doc.setSoggetto(stipendioEntrata.getSoggetto());
		doc.setDataEmissione(computeDataEmissioneRepertorioDocumento(stipendioEntrata));
		doc.setDataRepertorio(computeDataEmissioneRepertorioDocumento(stipendioEntrata));
		doc.setNumeroRepertorio(stipendioEntrata.getVoceContabile());
		doc.setDescrizione(computeDescrizioneDocumentoEntrata(stipendioEntrata));
		// TIPO DOCUMENTO
		documentoEntrataDad.setEnte(stipendioEntrata.getEnte());
		TipoDocumento tipoDocumento = documentoEntrataDad.findTipoDocumentoByCodiceEFamiglia(getCodiceDocumento(), TipoFamigliaDocumento.ENTRATA);
		doc.setTipoDocumento(tipoDocumento);
		doc.setCodiceBollo(findCodiceBollo(stipendioEntrata));

		return doc;
	}

	/**
	 * <font color='blue'><b>Inserisce un Elenco di quote per un documento di Entrata </b></font><br />
     *  <ul>
     *      <li>richiama il methodo {@link #inserisceQuotaDocumentoEntrata} </li>
     *  </ul>
	 * @param doc
	 * @param gruppoStipendi
	 */
	private void inserisciQuoteDocumentoDiEntrata(DocumentoEntrata doc, List<Stipendio> gruppoStipendi) {
		String methodName = "inserisciQuoteDocumentoDiEntrata";
		log.info(methodName, "inserisciQuoteDocumentoDiEntrata Inizio....");
		for (Stipendio s : gruppoStipendi) {
			log.info(methodName, "Inserimento quota corrispondente alla linea " + s.getFileLineNumber() + ": \"" + s.getFileLineContent() + "\"");
			SubdocumentoEntrata subdocumentoEntrata = inserisceQuotaDocumentoEntrata(doc, s,false);
			counter.addSubdocumentoEntrata();
			inserisciSubDocumentoNellaMappaSubdocumenti(s,subdocumentoEntrata);
		}
		dscg.aggiornaStatoDocumentoDiEntrata(doc);
		log.info(methodName, "inserisciQuoteDocumentoDiEntrata Fine....");

	}

	/**
	 * <font color='blue'><b>Inserisce un Elenco di quote per un documento di Spesa </b></font><br />
     *  <ul>
     *      <li>richiama il methodo {@link #inserisceQuotaDocumentoSpesa} </li>
     *  </ul>
	 * @param documentoSpesa
	 * @param gruppoStipendi
	 */
	private void inserisciQuoteDocumentoDiSpesa(DocumentoSpesa documentoSpesa, List<Stipendio> gruppoStipendi) {
		String methodName = "inserisciQuoteDocumentoDiSpesa";
		log.info(methodName, "inserisciQuoteDocumentoDiSpesa Inizio....");
		for (Stipendio s : gruppoStipendi) {
			log.info(methodName, "Inserimento quota corrispondente alla linea " + s.getFileLineNumber() + ": \"" + s.getFileLineContent() + "\"");
			SubdocumentoSpesa subdocumentoSpesa = inserisceQuotaDocumentoSpesa(documentoSpesa, s,false);
			counter.addSubdocumentoSpesa();
			inserisciSubDocumentoNellaMappaSubdocumenti(s,subdocumentoSpesa);
		}
		dscg.aggiornaStatoDocumentoDiSpesa(documentoSpesa);
		log.info(methodName, "inserisciQuoteDocumentoDiSpesa Fine....");
	}

	/**
	 * inserisce in base al tipo di record un subdocumento alla mappa
	 * la mappa contiene tre tipi di liste subdoc ENTRATE,SPESE,ONERI/RITENUTE
	 * @param s lo stipendio
	 * @param subdocumento il subdoc
	 */
	private void inserisciSubDocumentoNellaMappaSubdocumenti(Stipendio s, Subdocumento<?,?> subdocumento) {
		if( subdocumento == null || subdocumento.getUid() == 0){
			return ;
		}

		String key = computeKeySubdocumentiPerRecord(s);
		if (!mappaListaSubDocumenti.containsKey(key)) {
			List<Subdocumento<?, ?>> lista =  new ArrayList<Subdocumento<?, ?>>();
			mappaListaSubDocumenti.put(key, lista);
		}
		mappaListaSubDocumenti.get(key).add(subdocumento);
	}

	/**
	 * <font color='blue'><b>Calcola l'importo del documento di entrata </b></font><br />
	 * Totale = Totale delle quote del documento
	 * @param gruppoStipendi
	 * @return il totale
	 */
	private BigDecimal calcolaTotaleImportoDocumentoDiEntrata(List<Stipendio> gruppoStipendi) {
		BigDecimal importo = BigDecimal.ZERO;
		for (Stipendio s : gruppoStipendi) {
			importo = importo.add(s.getImportoEntrata());
		}
		return importo;
	}

	/**
	 * <font color='blue'><b>Calcola l'importo del documento di Spesa </b></font><br />
	 * Totale = Totale delle quote del documento
	 * @param gruppoStipendi
	 * @return il totale
	 */
	private BigDecimal calcolaTotaleImportoDocumentoDiSpesa(List<Stipendio> gruppoStipendi) {
		BigDecimal importo = BigDecimal.ZERO;
		for (Stipendio s : gruppoStipendi) {
			importo = importo.add(calcolaImportoSpesa(s));
		}
		return importo;
	}

	/**
	 * <font color='blue'><b>Inserisce un documento di Spesa </b></font><br />
	 *  Popola il documento di Spesa necessario <br />
	 *  Cerca il tipo di documento per il codice di famiglia  e lo setta al Documento
     *  <ul>
     *      <li>richiama il methodo {@link #inserisceOAggiornaDocumentoSpesaService} </li>
     *  </ul>
     *  <ul>
     *      <li>richiama il methodo {@link #inserisciQuoteDocumentoDiSpesa} </li>
     *  </ul>
	 * @param gruppoStipendi
	 */
	private void inserisciDocumentoDiSpesaConQuote(List<Stipendio> gruppoStipendi) {
		String methodName = "inserisciDocumentoDiSpesaConQuote";
		log.debug(methodName, "inserisciDocumentoDiSpesaConQuote INIZIO....");

		Stipendio stipendioSpesa = gruppoStipendi.get(0);

		DocumentoSpesa doc = creaDocumentoDiSpesa(stipendioSpesa);

		doc.setImporto(calcolaTotaleImportoDocumentoDiSpesa(gruppoStipendi));

		DocumentoSpesa documentoSpesa = inserisceOAggiornaDocumentoSpesaService(doc);

		inserisciQuoteDocumentoDiSpesa(documentoSpesa, gruppoStipendi);
		log.debug(methodName, "inserisciDocumentoDiSpesaConQuote FINE....");

	}
	/**
	 * crea e popola un documento di spesa a partire dallo stipendio
	 * @param stipendioSpesa
	 * @return il documento
	 */
	private DocumentoSpesa creaDocumentoDiSpesa(Stipendio stipendioSpesa) {
		String methodName = "creaDocumentoDiSpesa";
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setEnte(stipendioSpesa.getEnte());
		doc.setAnno(stipendioSpesa.getAnnoElaborazione());
		doc.setNumero(computeNumeroDocumentoFromStipendio(stipendioSpesa, Boolean.TRUE));
		doc.setSoggetto(stipendioSpesa.getSoggetto());
		doc.setDataEmissione(computeDataEmissioneRepertorioDocumento(stipendioSpesa));
		doc.setDataRepertorio(computeDataEmissioneRepertorioDocumento(stipendioSpesa));
		doc.setNumeroRepertorio(stipendioSpesa.getVoceContabile());
		doc.setDescrizione(computeDescrizioneDocumentoSpesa(stipendioSpesa));
		doc.setCodiceBollo(findCodiceBollo(stipendioSpesa));
		// TIPO DOCUMENTO
		documentoSpesaDad.setEnte(stipendioSpesa.getEnte());
		TipoDocumento tipoDocumento = documentoSpesaDad.findTipoDocumentoByCodiceEFamiglia(getCodiceDocumento(), TipoFamigliaDocumento.SPESA);
		log.debug(methodName, "Tipo documento di spesa : " + tipoDocumento.getCodice());
		doc.setTipoDocumento(tipoDocumento);
		return doc;
	}

	/**
	 * <font color='blue'><b>Cerca il codice bollo da settare al documento </b></font><br />
	 * cerca il codice bollo con codice '99' In base all'ente  <br />
	 * se non e' presente prendo il primo della lista altrimenti return null
	 * @param stipendio
	 * @return il codice bollo
	 */
	public CodiceBollo findCodiceBollo(Stipendio stipendio) {

		CodiceBollo codiceBolloFromRicerca = null;
		dscg.setBilancio(stipendio.getBilancio());
		dscg.setEnte(stipendio.getEnte());
		RicercaCodiceBolloResponse resRCBR = dscg.ricercaCodiceBolloCached();

		if (!resRCBR.getElencoCodiciBollo().isEmpty()) {
			codiceBolloFromRicerca = new CodiceBollo();
			// prendo il primo e poi se trovo quello con codice 99 prendo quello
			codiceBolloFromRicerca = resRCBR.getElencoCodiciBollo().get(0);
			for (CodiceBollo cb : resRCBR.getElencoCodiciBollo()) {
				if ("99".equals(cb.getCodice())) {
					codiceBolloFromRicerca = cb;
				}
			}
		}
		return codiceBolloFromRicerca;
	}

	/**
	 * <font color='blue'><b>Inserisce o Aggiorna un documento di Spesa </b></font><br />
	 * <li>permette di inserire o aggiornare un documento di spesa<br /></li>
	 * <li>richiama l'inserimento e poi l'aggiornamento se il documento e' gia presente in archivio</li>
	 *
	 * @param documentoSpesa
	 * @return il documento
	 */
	private DocumentoSpesa inserisceOAggiornaDocumentoSpesaService(DocumentoSpesa documentoSpesa) {
		final String methodName = "inserisceOAggiornaDocumentoSpesaService";
		DocumentoSpesa documentoRicercaPuntuale = documentoSpesaDad.ricercaPuntualeDocumentoSpesa(documentoSpesa, StatoOperativoDocumento.ANNULLATO,
				DocumentoSpesaModelDetail.Stato);

		if(documentoRicercaPuntuale == null) {
			InserisceDocumentoSpesaResponse resIDSS = dscg.inserisceDocumentoSpesa(documentoSpesa, false);

			log.info(methodName, "Inserito documento di spesa? " + (resIDSS.getDocumentoSpesa() == null ? "No" : "Si, con uid " + resIDSS.getDocumentoSpesa().getUid()));
			counter.addDocumentoSpesaInserito();
			return resIDSS.getDocumentoSpesa();
		}

		log.info(methodName, "Documento gia' presente con uid " + documentoRicercaPuntuale.getUid());
		BigDecimal importoDocumentoDaAggiornare = BigDecimal.ZERO;
		importoDocumentoDaAggiornare = importoDocumentoDaAggiornare.add(documentoSpesa.getImporto());

		importoDocumentoDaAggiornare = importoDocumentoDaAggiornare.add(documentoRicercaPuntuale.getImporto());
		documentoSpesa.setImporto(importoDocumentoDaAggiornare);
		documentoSpesa.setUid(documentoRicercaPuntuale.getUid());

		StatoOperativoDocumento statoDoc = documentoRicercaPuntuale.getStatoOperativoDocumento();
		if(statoDoc == null){
			statoDoc = documentoDad.findStatoOperativoDocumento(documentoRicercaPuntuale.getUid());
		}
		documentoSpesa.setStatoOperativoDocumento(statoDoc);

		//documentoSpesa.setRegistroUnico(resIDSS.getDocumentoSpesa().getRegistroUnico());
		AggiornaDocumentoDiSpesaResponse resADDS = dscg.aggiornaDocumentoDiSpesa(documentoSpesa, false, ErroreCore.ENTITA_PRESENTE.getCodice());
		counter.addDocumentoSpesaAggiornato();

		return resADDS.getDocumentoSpesa();

	}

	/**
	 * <font color='blue'><b>Inserisce o Aggiorna un documento di Entrata </b></font><br />
	 * <li>permette di inserire o aggiornare un documento di Entrata<br /></li>
	 * <li>richiama l'inserimento e poi l'aggiornamento se il documento e' gia presente in archivio</li>
	 *
	 * @param documentoEntrata
	 * @return il documento
	 */
	private DocumentoEntrata inserisceOAggiornaDocumentoEntrataService(DocumentoEntrata documentoEntrata) {
		final String methodName = "inserisceOAggiornaDocumentoEntrataService";
		DocumentoEntrata documentoRicercaPuntuale = documentoEntrataDad.ricercaPuntualeDocumentoEntrata(documentoEntrata, StatoOperativoDocumento.ANNULLATO,
				DocumentoEntrataModelDetail.Stato);

		if(documentoRicercaPuntuale == null) {
			InserisceDocumentoEntrataResponse resIDSS = dscg.inserisceDocumentoEntrata(documentoEntrata, false);

			log.info(methodName, "Inserito documento di entrata? " + (resIDSS.getDocumentoEntrata() == null ? "No" : "Si, con uid " + resIDSS.getDocumentoEntrata().getUid()));
			counter.addDocumentoEntrataInserito();
			return resIDSS.getDocumentoEntrata();
		}

		log.info(methodName, "Documento gia' presente con uid " + documentoRicercaPuntuale.getUid());
		BigDecimal importoDocumentoDaAggiornare = BigDecimal.ZERO;
		importoDocumentoDaAggiornare = importoDocumentoDaAggiornare.add(documentoEntrata.getImporto());

		importoDocumentoDaAggiornare = importoDocumentoDaAggiornare.add(documentoRicercaPuntuale.getImporto());
		documentoEntrata.setImporto(importoDocumentoDaAggiornare);
		documentoEntrata.setUid(documentoRicercaPuntuale.getUid());

		StatoOperativoDocumento statoDoc = documentoRicercaPuntuale.getStatoOperativoDocumento();
		if(statoDoc == null){
			statoDoc = documentoDad.findStatoOperativoDocumento(documentoRicercaPuntuale.getUid());
		}
		documentoEntrata.setStatoOperativoDocumento(statoDoc);

		//documentoSpesa.setRegistroUnico(resIDSS.getDocumentoSpesa().getRegistroUnico());
		AggiornaDocumentoDiEntrataResponse resADDS = dscg.aggiornaDocumentoDiEntrata(documentoEntrata, false, ErroreCore.ENTITA_PRESENTE.getCodice());
		counter.addDocumentoEntrataAggiornato();

		return resADDS.getDocumentoEntrata();
	}

	/**
	 * <font color='blue'><b>Ragruppa la lista degli stipendi </b></font><br />
	 * <li>raggruppamento in base alla key dello stipendio che alla sua volta viene calcolata richiamando il methodo {@link #computeGroupKeyStipendio} </li>
	 * @return la mappa degli stipendi raggruppati
	 */
	private Map<String, List<Stipendio>> raggruppaStipendi() {
		Map<String, List<Stipendio>> result = new HashMap<String, List<Stipendio>>();

		for (Stipendio s : stipendi) {
			addCounterNumeroPerTipoRecord(s);
			String key = computeGroupKeyStipendio(s);
			if (!result.containsKey(key)) {
				List<Stipendio> lista = new ArrayList<Stipendio>();
				result.put(key, lista);
			}
			result.get(key).add(s);
		}

		return result;
	}

	/**
	 * conta le occorrenze dei vari tipi di record da elaborare(dopo il filtraggio )
	 * @param stipendio
	 */
	private void addCounterNumeroPerTipoRecord(Stipendio stipendio) {

		if(stipendio.isTipoRecordStipendioLordo()){
			counter.addNumeroStipendiLordi();
		}else if(stipendio.isTipoRecordOneri()){
			counter.addNumeroOneri();
		}else if(stipendio.isTipoRecordRecuperi()){
			counter.addNumeroRecuperi();
		}else if(stipendio.isTipoRecordRitenute()){
			counter.addNumeroRitenute();
		}

	}

	/**
	 * <font color='blue'><b>computeNumeroDocumentoFromStipendio </b></font><br />
	 *Numero documento =<br />
	 * {<br />
	 *  0                   <br />
	 *  mese elaborazione   <br />
	 *  anno di riferimento <br />
	 *  capitolo 		    <br />
	 *  articolo 			<br />
	 *  ueb                 <br />
	 *  }<br />
	 *  presenti nel record se presenti
	 * o relativi al capitolo o alla UEB reperita
	 *
	 * @param stipendio
	 * @param isSpesa
	 * @return il numero documento
	 */
	private String computeNumeroDocumentoFromStipendio(Stipendio stipendio, Boolean isSpesa) {
		StringBuilder sb = new StringBuilder();
		sb.append("0");
		sb.append(stipendio.getMeseElaborazione());
		sb.append(stipendio.getBilancio().getAnno());
		sb.append(stipendio.getCapitolo().getNumeroCapitolo());
		sb.append(stipendio.getCapitolo().getNumeroArticolo());
		if (stipendio.getCapitolo().getNumeroUEB() != null) {
			sb.append(stipendio.getCapitolo().getNumeroUEB());
		}
		if (stipendio.getStrutturaAmministrativoContabile().getCodice() != null) {
			sb.append(stipendio.getStrutturaAmministrativoContabile().getCodice());
		}
		if(isSpesa != null && stipendio.isTipoRecordRitenute()) {
			computeSuffixForRitenute(stipendio, isSpesa.booleanValue(), sb);
		}
		return sb.toString().trim();

	}

	private void computeSuffixForRitenute(Stipendio stipendio, boolean isSpesa, StringBuilder sb) {
		String temp = sb.toString();
		String key = (isSpesa ? "S-" : "E-" ) + temp;
		Long prog = mappaProgressiviRitenute.get(key);
		if(prog == null) {
			// Devo ricalcolarmi il massimo gia' inserito
			prog = computaMax(stipendio, isSpesa, temp);
		}
		prog = Long.valueOf(prog.longValue() + 1);
		mappaProgressiviRitenute.put(key, prog);
		sb.append(" Rit. ")
			.append(prog);
	}

	private Long computaMax(Stipendio stipendio,  boolean isSpesa, String temp) {
		long maxValue = 0L;
		List<String> numeriDoc = findNumeriDoc(stipendio, isSpesa, temp);
		for(String num : numeriDoc) {
			Matcher matcher = PATTERN_RITENUTE.matcher(num);
			if(matcher.matches()) {
				String group = matcher.group(1);
				long tmp = Long.parseLong(group);
				maxValue = Math.max(maxValue, tmp);
			}
		}
		return Long.valueOf(maxValue);
	}

	private List<String> findNumeriDoc(Stipendio stipendio,  boolean isSpesa, String temp) {
		Documento<?, ?> documento = new DocumentoSpesa();
		documento.setEnte(stipendio.getEnte());
		documento.setAnno(stipendio.getAnnoElaborazione());
		documento.setNumero(temp);
		documento.setSoggetto(stipendio.getSoggetto());

		// TipoDocumento
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setCodice(getCodiceDocumento());
		tipoDocumento.setTipoFamigliaDocumento(isSpesa ? TipoFamigliaDocumento.SPESA : TipoFamigliaDocumento.ENTRATA);
		documento.setTipoDocumento(tipoDocumento);

		return documentoDad.findNumeroDocumentoLikeNumero(documento);
	}

	/**
	 * <font color='blue'><b>computeCausaleOrdinativoFromDocumento </b></font><br />
	 * CausaleOrdinativo = ‘DOCUMENTO N.'+numero documento+'DEL'+data documento
	 *
	 * @param documentoDiSpesa il documento
	 * @return la causale
	 */
	private String computeCausaleOrdinativoFromDocumento(DocumentoSpesa documentoDiSpesa) {
		StringBuilder sb = new StringBuilder();
		sb.append("DOCUMENTO N.");
		sb.append(documentoDiSpesa.getNumero());
		sb.append("DEL");
		sb.append(Utility.formatDate(documentoDiSpesa.getDataEmissione()));

		return sb.toString();
	}

	/**
	 * <font color='blue'><b>computeDescrizioneDocumentoSpesa </b></font><br />
//	 * @param stipendio
	 * @return la descrizione
	 */
	private String computeDescrizioneDocumentoSpesa(Stipendio stipendio) {
		StringBuilder sb = new StringBuilder();
		if(stipendio.isTipoRecordStipendioLordo()){
			sb.append(StipendioParams.CODICE_DESC_STIPENDIO_LORDO + stipendio.getMeseElaborazione());
		}else if(stipendio.isTipoRecordOneri() || stipendio.isTipoRecordRitenute()){
			//Oneri Ritenute
			sb.append(StipendioParams.CODICE_DESC_ONERI_RITENUTE + stipendio.getMeseElaborazione());
		}
		return sb.toString();
	}

	/**
	 * <font color='blue'><b>computeDescrizioneDocumentoEntrata </b></font><br />
	 * @param stipendio
	 * @return la descrizione
	 */
	private String computeDescrizioneDocumentoEntrata(Stipendio stipendio) {
		StringBuilder sb = new StringBuilder();
		if (stipendio.isTipoRecordRitenute()) {//ritenute
			sb.append(StipendioParams.CODICE_DESC_RITENUTE + stipendio.getMeseElaborazione());
		} else {//recuperi
			sb.append(StipendioParams.CODICE_DESC_STIPENDIO_LORDO + stipendio.getMeseElaborazione());
		}

		return sb.toString();
	}

	/**
	 * <font color='blue'><b>computeDataEmissioneRepertorioDocumento </b></font><br />
	 * @param stipendio
	 * @return la data di emissione
	 */
	private Date computeDataEmissioneRepertorioDocumento(Stipendio stipendio) {
		String methodName = "computeDataEmissioneRepertorioDocumento";

		// CR-3971: ultimo giorno del mese di elaborazione, per tutte le elaborazioni
//		Date dataEmissione = new Date();
//		if (isTipoElaborazioneONERI()) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, stipendio.getAnnoElaborazione());

			// I mesi vanno da 0 a 11 (il 12 fa parte del calendario lunare)
			int mese = stipendio.getMeseElaborazione() - 1;
			cal.set(Calendar.MONTH, mese);

			// Ultimo giorno del mese (devo prima settare il giorno pari a 1: in caso contrario potrei aver problemi nel caso stia elaborando il mese di febbraio in data 31/03)
			cal.set(Calendar.DAY_OF_MONTH, 1);
			int giorno = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, giorno);
			Date dataEmissione = cal.getTime();
//		}
		log.debug(methodName, "data emissione documento :" + dataEmissione.toString());

		// JIRA-2263: tronco la data di emissione del documento
		//return dataEmissione;
		return Utility.truncateToStartOfDay(dataEmissione);
	}

	/**
	 * <font color='blue'><b>computeGroupKeyStipendio </b></font><br />
	 * costruisce una key a partire dallo stipendio
	 * questa key serve per ragruppare gli stipendi
	 * @param s
	 * @return la chiave di raggruppamento
	 */
	private String computeGroupKeyStipendio(Stipendio s) {
		StringBuilder sb = new StringBuilder();
		sb.append(s.getTipoVoce().getCodice());
		sb.append("_");
		sb.append(s.isSpesa() ? "S" : "E");
		sb.append("_");

		Capitolo<?, ?> capitolo = s.getCapitolo();
		sb.append(capitolo.getAnnoCapitolo());
		sb.append("_");
		sb.append(capitolo.getNumeroCapitolo());
		sb.append("_");
		sb.append(capitolo.getNumeroArticolo());
		sb.append("_");
		if (capitolo.getNumeroUEB() != null && capitolo.getNumeroUEB() != 0) {
			sb.append(capitolo.getNumeroUEB());
			sb.append("_");
		}

		if(s.getSoggetto()!=null && s.getSoggetto().getCodiceSoggetto() !=null){
			sb.append(s.getSoggetto().getCodiceSoggetto());
			sb.append("_");
		}

		Accertamento accertamento = s.getAccertamento();
		SubAccertamento subAccertamento= s.getSubAccertamento();

		Impegno impegno =  s.getImpegno();
		SubImpegno subImpegno = s.getSubImpegno();


		sb.append("Accertamento");
		sb.append(accertamento != null ? accertamento.getAnnoMovimento() : "null");
		sb.append("_");
		sb.append(accertamento != null ? accertamento.getNumero() : "null");
		sb.append("_");
		sb.append(subAccertamento != null ? subAccertamento.getNumero() : "null");
		sb.append("_");
		sb.append("Impegno");
		sb.append(impegno != null ? impegno.getAnnoMovimento() : "null");
		sb.append("_");
		sb.append(impegno != null ? impegno.getNumero() : "null");
		sb.append("_");
		sb.append(subImpegno != null ? subImpegno.getNumero() : "null");
		sb.append("_");

		return sb.toString();
	}

	/**
	 * <font color='blue'><b>checkEnteAndBilancio </b></font><br />
	 * controlla e setta il bilancio<br />
	 * Modificato il 09/07/2015 (Lotto L) <br />
	 * l'anno di Bilancio da considerare &egrave; quello derivato dal cruscotto da cui viene lanciata l'elaborazione. <br />
	 * setta un messaggio nella response del servizio di elabora file stipendi quando il bilancio non viene trovato
	 *  richiamando {@link #sendMessaggioConNumeroStipendioInElaborazione}
	 * @param stipendio
	 */
	private void checkEnteAndBilancio(Stipendio stipendio) {
		String methodName = "checkEnteAndBilancio";
		if (req.getBilancio() == null || req.getBilancio().getUid() == 0) {
			sendMessaggioConNumeroStipendioInElaborazione("",stipendio,false);
			throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Bilancio,Uid Bilancio "), Esito.FALLIMENTO);
		}

		Bilancio bilancio = mappaBilancio.get(req.getBilancio().getUid());
		if(bilancio == null) {
			bilancioDad.setEnteEntity(stipendio.getEnte());
			bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
			mappaBilancio.put(req.getBilancio().getUid(), bilancio);

			if (bilancio == null || bilancio.getUid() == 0) {
				sendMessaggioConNumeroStipendioInElaborazione("",stipendio,false);
				throw new BusinessException(ErroreFin.ENTITA_NON_VALIDA.getErrore("Bilancio"), Esito.FALLIMENTO);
			}
			log.info(methodName, "Info Bilancio  (Anno/Fase/Uid):" + bilancio.getAnno() + "/" + bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio().toString() + "/" + bilancio.getUid());
		}
		stipendio.setBilancio(bilancio);
	}

	/**
	 * <font color='blue'><b>cleanStipendi </b></font><br />
	 * Elimina gli stipendi non validi dalla lista stipendi
	 *
	 * @param stipendiDaEliminare
	 */
	private void cleanStipendi(List<Stipendio> stipendiDaEliminare) {
		for(Iterator<Stipendio> it = stipendi.iterator(); it.hasNext();) {
			Stipendio stipendio = it.next();
			if (stipendiDaEliminare.contains(stipendio)) {
				it.remove();
			}
		}
	}

	/**
	 * <font color='blue'><b>caricaSoggetto </b></font><br />
	 * carica il soggetto richiamando il {@link #ricercaSoggettoPerChiave}
	 *
	 * @param stipendio
	 */
	private void caricaSoggetto(Stipendio stipendio) {
		String methodName = "caricaSoggetto";
		log.info(methodName, "codice soggetto from input :" + stipendio.getSoggetto().getCodiceSoggetto());
		RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiave(stipendio.getSoggetto());
		if(resRSPC.getSoggetto() !=null && resRSPC.getSoggetto().getUid() !=0){
			log.debug(methodName, "Soggetto trovato (codice/uid) : "+resRSPC.getSoggetto().getCodiceSoggetto()+"/"+resRSPC.getSoggetto().getUid());
		    stipendio.setSoggetto(resRSPC.getSoggetto());
		}

	}

	/**
	 * <font color='blue'><b>Ordina gli stipendi </b></font><br />
	 * <li>Ordinamento primo livello {@link #sortStipendiPrimoLivello} </li>
	 * <li>Ordinamento secondo livello {@link #sortStipendiSecondoLivello}</li>
	 */
	private void ordinaStipendi() {

		sortStipendiSecondoLivello(stipendi);
		sortStipendiPrimoLivello(stipendi);
	}

	/**
	 * <font color='blue'><b>ELABORAZIONE GENERICA STIPENDIO </b></font><br />
	 * <li>SE LO STIPENDIO &Egrave; DI SPESA RICHIAMA {@link #elaborateStipendioSpesa} </li>
	 * <li>SE LO STIPENDIO &Egrave; DI ENTRATA RICHIAMA {@link #elaborateStipendioEntrata}</li>
     * @param stipendio
	 * @return se l'elaborazione &eacute; andata bene
	 */
	private boolean elaborateStipendio(Stipendio stipendio) {
		String methodName = "elaborateStipendio";
		log.info(methodName, "Elaborazione Stipendio");

		checkEnteAndBilancio(stipendio);

		/** soggetto **/
		caricaSoggetto(stipendio);
		controllaValiditaSoggetto(stipendio);

		// controllo tipo di elaborazione e scarto il record se ci sono errori
		if (isStipendioDaScartare(stipendio)) {
			log.debug(methodName, "isStipendioDaScartare == TRUE");
			return false;
		}

		if (stipendio.isSpesa() && !stipendio.isTipoRecordRitenute()) {
			 elaborateStipendioSpesa(stipendio);
		} else if(stipendio.isEntrata() && !stipendio.isTipoRecordRitenute()) {
			 elaborateStipendioEntrata(stipendio);
		}else if( stipendio.isTipoRecordRitenute()){
			// NOTA BENE: per le ritenute sono presenti sia l'accertamento che l'impegno con l'importo nella colonna entrate.
			// Per entrambi ricercare il capitolo/UEB ma nell'ordinamento prevale l'entrata, la UEB di spesa servira' solo per la numerazione
			// del documento di spesa subordinato
			// questa parte e' comune
			log.debug(methodName, "Elaboro la parte spesa relativa al record ritenute");
			elaborateStipendioSpesa(stipendio);

			log.debug(methodName, "Elaboro la parte Entrata relativa al record ritenute");
			stipendio.resetCapitoloFromInput();
			elaborateStipendioEntrata(stipendio);
		}

		return true;
	}

	/**
	 * <font color='blue'><b>ELABORAZIONE  STIPENDIO Entrata </b></font><br />
	 * @param stipendio
	 * @return se l'elaborazione &eacute; andata bene
	 */
	private void elaborateStipendioEntrata(Stipendio stipendio) {
		String methodName = "elaborateStipendioEntrata";

		boolean accertamentoValorizzatoFromInput = stipendio.getAccertamento() != null
				&& stipendio.getAccertamento().getAnnoMovimento() > 0
				&& !stipendio.getAccertamento().getNumero().equals(BigDecimal.ZERO);
		boolean subAccertamentoValorizzatoFromInput = accertamentoValorizzatoFromInput
				&& stipendio.getSubAccertamento() !=null
				&& !stipendio.getSubAccertamento().getNumero().equals(BigDecimal.ZERO);
		int comportamento = ACCERTAMENTO_SUB_VALIDO;
		log.info(methodName, "anno accertamento da input : " + (stipendio.getAccertamento() != null ? stipendio.getAccertamento().getAnnoMovimento() : "null"));
		log.info(methodName, "numero accertamento da input : " + (stipendio.getAccertamento() != null ? stipendio.getAccertamento().getNumero() : "null"));
		log.info(methodName, "importo di Entrata  da input: " + stipendio.getImportoEntrata());
		Accertamento accertamentoInput = stipendio.getAccertamento();
		SubAccertamento subAccertamentoInput =  stipendio.getSubAccertamento();
		if (subAccertamentoValorizzatoFromInput) {
			comportamento = checkAndSetAccertamentoSubAccertamento(stipendio);
		}

		if (accertamentoValorizzatoFromInput && !subAccertamentoValorizzatoFromInput) {
			comportamento = checkAndSetAccertamento(stipendio);
		}


//		AHMAD NAZHA 05/02/2018 SIAC-5638
//		In caso di mancata disponibilita degli accertamenti
//		il sistema non deve caricare un nuovo accertamento ma la modifica a copertura (come gi previsto per documenti e ordinativi).
//		L'inserimento deve essere previsto SOLO in assenza dell'accertamento.
// 		ci saranno varie casi
//		1- accertamento non presente -----> bisogna inserire nuovo accertamento
//		2- accertamento presente e non ha disponibilita ----> aggiorna importo accertamento
//		3- accertamento e sub presenti ma sub non ha disponibilita .---> aggiorno importo del sub

		switch (comportamento) {
			case ACCERTAMENTO_SUB_VALIDO:
				break;
			case CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO:
				stipendio.getAccertamento().setUid(0);
				stipendio.setCapitolo(elaboraCapitoloEntrataGestioneDalFlussoDati(stipendio));
				caricaAccertamentoSubAccertamentoFromElementoDiBilancio(stipendio);
				break;
			case SUBACCERTAMENTO_SENZA_DISPONIBILITA:
				aggiornaImportoAccertamentoSubPerMancataDisponibilita(stipendio,accertamentoInput,subAccertamentoInput);
				break;
			case ACCERTAMENTO_SENZA_DISPONIBILITA:
				aggiornaImportoAccertamentoPerMancataDisponibilita(stipendio,accertamentoInput);
				break;
			default:
				break;
		}

	}


		/**
		 * aggiorna importo subaccertamento e poi se e' necessario quello dell'accertamento
		 * @author NAZHA AHMAD
		 * SIAC-5638 NEL CASO di mancata disponibilita aggiorno l'accertamento
		 * @param stipendio
		 * @param subAccertamentoInput
		 * @param accertamentoInput
		 */
	private void aggiornaImportoAccertamentoSubPerMancataDisponibilita(Stipendio stipendio, Accertamento accertamentoInput, SubAccertamento subAccertamentoInput) {
		String methodName ="aggiornaImportoAccertamentoSubPerMancataDisponibilita";

		mgscg.setBilancio(stipendio.getBilancio());
		mgscg.setEnte(stipendio.getEnte());

		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		attributiMovimentoGestioneOttimizzato.setCaricaSub(true);
		attributiMovimentoGestioneOttimizzato.setEscludiSubAnnullati(true);

		RicercaAccertamentoPerChiaveOttimizzatoResponse response = mgscg.ricercaAccertamentoPerChiaveOttimizzatoNoSuccess(accertamentoInput,attributiMovimentoGestioneOttimizzato, new DatiOpzionaliCapitoli(),subAccertamentoInput);
		log.logXmlTypeObject(response, "Response del servizio ricercaAccertamentoPerChiaveOttimizzata");

		if(response.getAccertamento() == null || response.getAccertamento().getUid() == 0 || response.getAccertamento().getElencoSubAccertamenti() == null || response.getAccertamento().getElencoSubAccertamenti().isEmpty()){
			log.warn(methodName, "SubAccertamento Non presente sulla base dati  ");
			return;
		}

		Accertamento accertamentoR = response.getAccertamento();
		SubAccertamento subAccertamentoR = accertamentoR.getElencoSubAccertamenti().get(0);
		for(SubAccertamento sub : accertamentoR.getElencoSubAccertamenti()){
			if(sub.getUid() == subAccertamentoR.getUid()){
				subAccertamentoR =sub;
				continue;
			}
		}
		log.debug(methodName, "Info disponibilita SubAccertamento ..................................................");
		BigDecimal importoQuota = !(stipendio.getImportoEntrata().equals(BigDecimal.ZERO)) ? stipendio.getImportoEntrata() : BigDecimal.ZERO;
		String keySubAccertamento = computekeyAccertamentoSubAccertamento(subAccertamentoR, true);
		BigDecimal importoQuoteSub = BigDecimal.ZERO;

		BigDecimal importoMancanteSubAccertamento = BigDecimal.ZERO;
		if (mappaMovimentiDisponibilita.containsKey(keySubAccertamento)) {
			importoQuoteSub = mappaMovimentiDisponibilita.get(keySubAccertamento);
			log.debug(methodName, "Importo quote iniziale :" + importoQuoteSub);
		}

		BigDecimal sumSub = importoQuoteSub.add(importoQuota);
		importoMancanteSubAccertamento = sumSub.subtract(subAccertamentoR.getDisponibilitaIncassare()).abs();

		String keyAccertamento = computekeyAccertamentoSubAccertamento(accertamentoInput, false);

		//log.debug(methodName, "Disponibilita SubAccertamento iniziale :" + importoSubAccertamentoSenzaQuota);
		log.debug(methodName, "Importo quota da aggiungere :" + importoQuota);
		log.debug(methodName, "Importo totale quote relative al keySubAccertamento con key " + keySubAccertamento + " e' di " + sumSub);
		log.debug(methodName, "Importo mancante subAaccertamento " + importoMancanteSubAccertamento);
		//log.debug(methodName, "Importo accertamento con key "+keyAccertamento +" e' di " +importoAccertamento);

		//ci sono due strade da seguire secondo me ...(AHMAD)
		//1- accertamento ha disponibilita sufficiente per coprire l'importo mancante del sub
		//   in questo caso aggiorno solo sub
		//2- accertamento non ha disponibilita sufficiente per coprire ...
		//   in questo caso bisogna rifare il calcolo per l'importo mancante all'accertamento ed al sub

		//attualmente il comportamento e' il seguente
		// aggiorno sia sub che accertamento con l'importo mancante al sub

		//chiamo il servizio che aggiorna gli importi

		List<ModificaMovimentoGestioneEntrata> modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();

		//invoco il servizio:
        ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();

        BigDecimal importoCambiatoSub = subAccertamentoR.getImportoAttuale().add(importoMancanteSubAccertamento);    
        BigDecimal importoCambiatoAcc = accertamentoR.getImportoAttuale().add(importoMancanteSubAccertamento);

        spesa.setUid(0); //Lo setto a zero cosi da riconoscere che e nuovo
        spesa.setImportoOld(importoMancanteSubAccertamento);
        spesa.setImportoNew(importoCambiatoAcc);
        //provvedimento
        if(accertamentoR.getAttoAmministrativo() !=null ){
            spesa.setAttoAmministrativoAnno(String.valueOf(accertamentoR.getAttoAmministrativo().getAnno()));
            spesa.setAttoAmministrativoNumero(accertamentoR.getAttoAmministrativo().getNumero());
            spesa.setAttoAmministrativoTipoCode(accertamentoR.getAttoAmministrativo().getTipoAtto().getCodice());
            //TIPO ATTO
            if(accertamentoR.getAttoAmministrativo().getTipoAtto() !=null ){
                spesa.setAttoAmministrativoTipoCode( accertamentoR.getAttoAmministrativo().getTipoAtto().getCodice());
                spesa.setAttoAmmTipoAtto(accertamentoR.getAttoAmministrativo().getTipoAtto());
            }
            //ATTO
            spesa.setAttoAmministrativo(accertamentoR.getAttoAmministrativo());

        }
        //causa errori .... o provvedimento
//        if(accertamentoR.getCapitoloEntrataGestione() !=null && accertamentoR.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile() !=null){
//            spesa.setIdStrutturaAmministrativa(accertamentoR.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile().getUid());
//        }
//		IMP, SIM, RIAC, ECON, RIU sembrano queste le codifiche ma quale utilizzare ??
        spesa.setTipoModificaMovimentoGestione("ALT");
		spesa.setDescrizione("");

        spesa.setTipoMovimento("ACC");
        //reimputazione non so cosa sia setto a false
        spesa.setReimputazione(false);
        spesa.setAnnoReimputazione(null);
        spesa.setValidato(true);
        //Inserisco nell impegno che andra nel servizio aggiorna impegno
        modificheList.add(spesa);

		accertamentoR.setListaModificheMovimentoGestioneEntrata(modificheList);

		/************************** i sub *****************************************/
		//Subimpegno
		List<ModificaMovimentoGestioneEntrata> modificheSubList = new ArrayList<ModificaMovimentoGestioneEntrata>();


		ModificaMovimentoGestioneEntrata spesaSub = new ModificaMovimentoGestioneEntrata();



		spesaSub.setUid(0); //Lo setto a zero cosi da riconoscere che e nuovo
		spesaSub.setImportoOld(importoMancanteSubAccertamento);
		spesaSub.setImportoNew(importoCambiatoSub);
		spesaSub.setUidSubAccertamento(subAccertamentoR.getUid());


		//provvedimento
        if(subAccertamentoR.getAttoAmministrativo() !=null ){
            spesaSub.setAttoAmministrativoAnno(String.valueOf(subAccertamentoR.getAttoAmministrativo().getAnno()));
            spesaSub.setAttoAmministrativoNumero(subAccertamentoR.getAttoAmministrativo().getNumero());
            spesaSub.setAttoAmministrativoTipoCode(subAccertamentoR.getAttoAmministrativo().getTipoAtto().getCodice());
            //TIPO ATTO
            if(subAccertamentoR.getAttoAmministrativo().getTipoAtto() !=null ){
                spesaSub.setAttoAmministrativoTipoCode( subAccertamentoR.getAttoAmministrativo().getTipoAtto().getCodice());
                spesaSub.setAttoAmmTipoAtto(subAccertamentoR.getAttoAmministrativo().getTipoAtto());

            }
            //ATTO
            spesaSub.setAttoAmministrativo(subAccertamentoR.getAttoAmministrativo());

        }
        //causa errore su provvedimento
//        if(subAccertamentoR.getCapitoloEntrataGestione() !=null && subAccertamentoR.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile() !=null){
//            spesaSub.setIdStrutturaAmministrativa(subAccertamentoR.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile().getUid());
//        }
//		IMP, SIM, RIAC, ECON, RIU sembrano queste le codifiche ma quale utilizzare ??
        spesaSub.setTipoModificaMovimentoGestione("ALT");
        spesaSub.setDescrizione("");

        spesaSub.setTipoMovimento("SAC");
        //reimputazione non so cosa sia setto a false
        spesaSub.setReimputazione(false);
        spesaSub.setAnnoReimputazione(null);
        spesaSub.setValidato(true);


		modificheSubList.add(spesaSub);
		subAccertamentoR.setListaModificheMovimentoGestioneEntrata(modificheSubList);
		List<SubAccertamento> listaSub= response.getElencoSubAccertamentiTuttiConSoloGliIds();
		for(SubAccertamento sub : listaSub){
			if(sub.getUid() == subAccertamentoR.getUid()){
				sub.setListaModificheMovimentoGestioneEntrata(modificheSubList);
				continue;
			}
		}
		accertamentoR.setElencoSubAccertamenti(listaSub);
		accertamentoR.setSubAccertamenti(listaSub);
		AggiornaAccertamento aggiornaAccertamentoSubReq = new AggiornaAccertamento();

		//compongo la request:

		//compongo la request:
		//modificata manualmente
		accertamentoR.setCodSiope(null);
		accertamentoR.setCodicePdc(null);
		accertamentoR.setDescCodSiope(null);
		accertamentoR.setDescPdc(null);
		accertamentoR.setDescTransazioneEuropeaSpesa(null);
		accertamentoR.setIdPdc(null);
		accertamentoR.setIdSiope(null);
		accertamentoR.setAttoAmmAnno(null);
		accertamentoR.setAttoAmmNumero(null);
		accertamentoR.setAttoAmmTipoAtto(null);
		CapitoloEntrataGestione capitolo =  new CapitoloEntrataGestione();
		if(accertamentoR.getCapitoloEntrataGestione() !=null){
			capitolo.setStato(accertamentoR.getCapitoloEntrataGestione().getStato());
			capitolo.setUid(accertamentoR.getCapitoloEntrataGestione().getUid());
			capitolo.setAnnoCapitolo(accertamentoR.getCapitoloEntrataGestione().getAnnoCapitolo());
			capitolo.setNumeroArticolo(accertamentoR.getCapitoloEntrataGestione().getNumeroArticolo());
			capitolo.setNumeroCapitolo(accertamentoR.getCapitoloEntrataGestione().getNumeroCapitolo());
			capitolo.setNumeroUEB(accertamentoR.getCapitoloEntrataGestione().getNumeroUEB());
			capitolo.setTipoCapitolo(accertamentoR.getCapitoloEntrataGestione().getTipoCapitolo());

		}
		accertamentoR.setCapitoloEntrataGestione(capitolo);
		accertamentoR.setChiaveCapitoloEntrataGestione(0);

		aggiornaAccertamentoSubReq.setAccertamento(accertamentoR);
		aggiornaAccertamentoSubReq.setEnte(stipendio.getEnte());
		//mantengo il soggetto dell'accertamento ma volendo lo si puo modificare(niente analisi) .... con quello dello stipendio
		aggiornaAccertamentoSubReq.getAccertamento().setSoggetto(accertamentoR.getSoggetto());

		aggiornaAccertamentoSubReq.setRichiedente(req.getRichiedente());
		aggiornaAccertamentoSubReq.setUnitaElementareGestioneE(null);
		aggiornaAccertamentoSubReq.setBilancio(stipendio.getBilancio());
		//invoco il servizio:
		AggiornaAccertamentoResponse responseAA = mgscg.aggiornaAccertamento(aggiornaAccertamentoSubReq);

		if(responseAA.isFallimento() || (responseAA.getErrori() != null && !responseAA.getErrori().isEmpty())){
			//presenza errori

			for (Errore err : responseAA.getErrori()) {
				log.warn(methodName, String.format("Riga %d - %s - %s", numero, err.getCodice(), err.getDescrizione()));
				res.addMessaggio(err.getCodice(), String.format("Riga %d - %s", numero, err.getDescrizione()));
			}

			return;
		}

		counter.addnumeroAccertamentiConModificaImporti();

		
		subAccertamentoR.setDisponibilitaIncassare(
				getSubAccertamento(subAccertamentoR.getUid(), responseAA.getAccertamento().getSubAccertamenti())
				.getDisponibilitaIncassare());
		
		stipendio.setSubAccertamento(subAccertamentoR);   
		
		
		log.debug(methodName, "Importo accertamento dopo l'aggiornamento da servizio " + response.getAccertamento().getDisponibilitaIncassare());

		BigDecimal importoQuote = BigDecimal.ZERO;
		if (mappaMovimentiDisponibilita.containsKey(keyAccertamento)) {
			importoQuote = mappaMovimentiDisponibilita.get(keyAccertamento);
			log.debug(methodName, "Importo quote iniziale :" + importoQuote);
		}

		BigDecimal sum = importoQuote.add(importoQuota);
		//se servizio modifica importo andato a buon fine aggiorno importo nella mappa
		mappaMovimentiDisponibilita.put(keyAccertamento, sum);
		mappaMovimentiDisponibilita.put(keySubAccertamento, sumSub);


	}
	
	private SubAccertamento getSubAccertamento(Integer uid, List<SubAccertamento> list) {
		for(SubAccertamento sub : list) {
			if(sub.getUid() == uid) {
				return sub;
			}
		}
		return null;
	}
	
	/**
	 * aggiorna importo accertamento
	 * @author NAZHA AHMAD
	 * SIAC-5638 NEL CASO di mancata disponibilita aggiorno l'accertamento
	 * @param stipendio
	 * @param accertamentoInput
	 */
	private void aggiornaImportoAccertamentoPerMancataDisponibilita(Stipendio stipendio, Accertamento accertamentoInput) {
		String methodName ="aggiornaImportoAccertamentoPerMancataDisponibilita";

		mgscg.setBilancio(stipendio.getBilancio());
		mgscg.setEnte(stipendio.getEnte());
		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		attributiMovimentoGestioneOttimizzato.setCaricaSub(false);
		attributiMovimentoGestioneOttimizzato.setEscludiSubAnnullati(true);

		RicercaAccertamentoPerChiaveOttimizzatoResponse response = mgscg.ricercaAccertamentoPerChiaveOttimizzatoNoSuccess(accertamentoInput,attributiMovimentoGestioneOttimizzato, new DatiOpzionaliCapitoli(), null);
		log.logXmlTypeObject(response, "Response del servizio ricercaAccertamentoPerChiaveOttimizzata");
		if(response.getAccertamento() == null || response.getAccertamento().getUid() == 0 ){
			log.warn(methodName, "Accertamento Non presente sulla base dati  ");
			return;
		}
		Accertamento accertamentoR = response.getAccertamento();

//		response
//
		//importi
		BigDecimal importoAccertamentoSenzaQuota = accertamentoR.getDisponibilitaIncassare();

		BigDecimal importoMancante = BigDecimal.ZERO;
		log.debug(methodName, "Info Importi Accertamento ..................................................");
		BigDecimal importoQuota = !(stipendio.getImportoEntrata().equals(BigDecimal.ZERO)) ? stipendio.getImportoEntrata() : BigDecimal.ZERO;
		String keyAccertamento = computekeyAccertamentoSubAccertamento(accertamentoR, false);
		BigDecimal importoQuote = BigDecimal.ZERO;
		if (mappaMovimentiDisponibilita.containsKey(keyAccertamento)) {
			importoQuote = mappaMovimentiDisponibilita.get(keyAccertamento);
			log.debug(methodName, "Importo quote iniziale :" + importoQuote);
		}

		BigDecimal sum = importoQuote.add(importoQuota);
		importoMancante = sum.subtract(importoAccertamentoSenzaQuota).abs();


		log.debug(methodName, "Disponibilita Accertamento iniziale :" + importoAccertamentoSenzaQuota);
		log.debug(methodName, "Importo quota da aggiungere :" + importoQuota);
		log.debug(methodName, "Importo totale quote relative al keyAccertamento con key " + keyAccertamento + " e' di " + sum);
		log.debug(methodName, "Importo mancante per l'accertamento " + importoMancante);


		log.debug(methodName, "Importo accertamento dopo la modifica dell'importo " + importoMancante);

		//chiamo il servizio per modificare l'importo dell'accertamento

		//compongo la request:
		AggiornaAccertamento aggiornaAccertamentoReq = new AggiornaAccertamento();
		accertamentoR.getListaModificheMovimentoGestioneEntrata();
		aggiornaAccertamentoReq.setAccertamento(accertamentoR);
		List<ModificaMovimentoGestioneEntrata> modificheList = new ArrayList<ModificaMovimentoGestioneEntrata>();

		//invoco il servizio:
		ModificaMovimentoGestioneEntrata spesa = new ModificaMovimentoGestioneEntrata();

		BigDecimal importoCambiato = accertamentoR.getImportoAttuale().add(importoMancante);

		spesa.setUid(0); //Lo setto a zero cosi da riconoscere che e nuovo
		spesa.setImportoOld(importoMancante);
		spesa.setImportoNew(importoCambiato);
		//provvedimento
		if(accertamentoR.getAttoAmministrativo() !=null ){
			spesa.setAttoAmministrativoAnno(String.valueOf(accertamentoR.getAttoAmministrativo().getAnno()));
			spesa.setAttoAmministrativoNumero(accertamentoR.getAttoAmministrativo().getNumero());
			spesa.setAttoAmministrativoTipoCode(accertamentoR.getAttoAmministrativo().getTipoAtto().getCodice());
			//TIPO ATTO
			if(accertamentoR.getAttoAmministrativo().getTipoAtto() !=null ){
				spesa.setAttoAmministrativoTipoCode( accertamentoR.getAttoAmministrativo().getTipoAtto().getCodice());
				spesa.setAttoAmmTipoAtto(accertamentoR.getAttoAmministrativo().getTipoAtto());
			}
			//ATTO
			spesa.setAttoAmministrativo(accertamentoR.getAttoAmministrativo());
		}

//		if(accertamentoR.getCapitoloEntrataGestione() !=null && accertamentoR.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile() !=null){
//			spesa.setIdStrutturaAmministrativa(accertamentoR.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile().getUid());
//		}
//		IMP, SIM, RIAC, ECON, RIU sembrano queste le codifiche ma quale utilizzare ??
		spesa.setTipoModificaMovimentoGestione("ALT");
		spesa.setDescrizione("");

		spesa.setTipoMovimento("ACC");
		//reimputazione non so cosa sia setto a false
		spesa.setReimputazione(false);
		spesa.setAnnoReimputazione(null);
		spesa.setValidato(true);
		//Inserisco nell impegno che andra nel servizio aggiorna impegno
		modificheList.add(spesa);

		accertamentoR.setListaModificheMovimentoGestioneEntrata(modificheList);

		//compongo la request:
		//modificata manualmente
		accertamentoR.setCodSiope(null);
		accertamentoR.setCodicePdc(null);
		accertamentoR.setDescCodSiope(null);
		accertamentoR.setDescPdc(null);
		accertamentoR.setDescTransazioneEuropeaSpesa(null);
		accertamentoR.setIdPdc(null);
		accertamentoR.setIdSiope(null);
		accertamentoR.setAttoAmmAnno(null);
		accertamentoR.setAttoAmmNumero(null);
		accertamentoR.setAttoAmmTipoAtto(null);

		CapitoloEntrataGestione capitolo =  new CapitoloEntrataGestione();
		if(accertamentoR.getCapitoloEntrataGestione() !=null){
			capitolo.setStato(accertamentoR.getCapitoloEntrataGestione().getStato());
			capitolo.setUid(accertamentoR.getCapitoloEntrataGestione().getUid());
			capitolo.setAnnoCapitolo(accertamentoR.getCapitoloEntrataGestione().getAnnoCapitolo());
			capitolo.setNumeroArticolo(accertamentoR.getCapitoloEntrataGestione().getNumeroArticolo());
			capitolo.setNumeroCapitolo(accertamentoR.getCapitoloEntrataGestione().getNumeroCapitolo());
			capitolo.setNumeroUEB(accertamentoR.getCapitoloEntrataGestione().getNumeroUEB());
			capitolo.setTipoCapitolo(accertamentoR.getCapitoloEntrataGestione().getTipoCapitolo());
		}
		accertamentoR.setCapitoloEntrataGestione(capitolo);
		accertamentoR.setChiaveCapitoloEntrataGestione(0);
		accertamentoR.setIdStrutturaAmministrativa(null);
		//setIdsTRUTTURAaMM A NULL NON fungeva bene
		accertamentoR.setIdStrutturaAmministrativa(Integer.valueOf(0));


		aggiornaAccertamentoReq.setAccertamento(accertamentoR);
		aggiornaAccertamentoReq.setEnte(stipendio.getEnte());
		//mantengo il soggetto dell'accertamento ma volendo lo si puo modificare(niente analisi) .... con quello dello stipendio
		aggiornaAccertamentoReq.getAccertamento().setSoggetto(accertamentoR.getSoggetto());
		aggiornaAccertamentoReq.setRichiedente(req.getRichiedente());
		aggiornaAccertamentoReq.setUnitaElementareGestioneE(null);
		aggiornaAccertamentoReq.setBilancio(stipendio.getBilancio());
		//invoco il servizio:
		AggiornaAccertamentoResponse responseAA = mgscg.aggiornaAccertamento(aggiornaAccertamentoReq); // FIXME ottimizzabile chiamando servizio inserisciModificaImportoMovimentoGestioneEntrataService

		if(responseAA.isFallimento() || (responseAA.getErrori() != null && !responseAA.getErrori().isEmpty())){
			//presenza errori

			for (Errore err : responseAA.getErrori()) {
				log.warn(methodName, String.format("Riga %d - %s - %s", numero, err.getCodice(), err.getDescrizione()));
				res.addMessaggio(err.getCodice(), String.format("Riga %d - %s", numero, err.getDescrizione()));
			}

			return;
		}


		counter.addnumeroAccertamentiConModificaImporti();

		stipendio.setAccertamento(response.getAccertamento());
		log.debug(methodName, "Importo accertamento dopo l'aggiornamento da servizio " + response.getAccertamento().getDisponibilitaIncassare());

		//se servizio modifica importo andato a buon fine aggiorno importo nella mappa
		mappaMovimentiDisponibilita.put(keyAccertamento, sum);


	}

	/**
	 * <font color='blue'><b>Controlla se l'accertamento &egrave in stato DEFINITIVO </b></font><br />
	 * @param accertamento
	 * @return se lo stato dell'accertamento sia coerente
	 */
	private boolean checkStatoAccertmento(Accertamento accertamento) {

		return StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata());
	}

	/**
	 * <font color='blue'><b>ELABORAZIONE  STIPENDIO Entrata </b></font><br />
	 * @param stipendio
	 * @return se l'elaborazione &eacute; andata bene
	 */
	private boolean elaborateStipendioSpesa(Stipendio stipendio) {
		final String methodName = "elaborateStipendioSpesa";

		boolean impegnoPresenteEvalido = false;
		boolean impegnoValorizzatoFromInput = stipendio.getImpegno() != null
				&& stipendio.getImpegno().getAnnoMovimento() > 0
				&& !stipendio.getImpegno().getNumero().equals(BigDecimal.ZERO);
		boolean subImpegnoValorizzatoFromInput = impegnoValorizzatoFromInput
				&& stipendio.getSubImpegno()!=null
				&& !stipendio.getSubImpegno().getNumero().equals(BigDecimal.ZERO);

		log.info(methodName, "anno impegno da input : " + stipendio.getImpegno().getAnnoMovimento());
		log.info(methodName, "numero impegno da input : " + stipendio.getImpegno().getNumero().intValue());
		log.info(methodName, "valore di spesa da input : " + stipendio.getImportoSpesa());
		/** impegno **/
		stipendio.setDaAssociareAlDocumento(true);
		if (subImpegnoValorizzatoFromInput) {
			impegnoPresenteEvalido = checkAndSetImpegnoSubimpegnoFromSubImpegno(stipendio);
		}

		if (impegnoValorizzatoFromInput && !subImpegnoValorizzatoFromInput) {
			impegnoPresenteEvalido = checkAndSetImpegnoSubimpegnoFromImpegno(stipendio);
		}

		if(!stipendio.isDaAssociareAlDocumento()){
			return false;
		}

		if (!impegnoPresenteEvalido && stipendio.isDaAssociareAlDocumento()) {
			log.debug(methodName, "Impegno non valido cerco un'altro");
			stipendio.setCapitolo(elaboraCapitoloUscitaGestioneDalFlussoDati(stipendio));
			log.debug(methodName, "Capitolo settato");
			caricaImpegnoSubImpegnoFromElementoDiBilancio(stipendio);
		}

		return impegnoPresenteEvalido;
	}

	/**
	 * <font color='blue'><b>Controlla se L'IMPEGNO &egrave in stato DEFINITIVO </b></font><br />
	 * @param impegno
	 * @return se lo stato dell'impegno sia coerente
	 */
	private boolean checkStatoImpegno(Impegno impegno) {

		return StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa());

	}

	/**
	 * <font color='blue'><b> </b></font><br />
	 * ricercare il capitolo di entrata a partire dal flusso dati
	 * @param stipendio
	 * @return il capitolo
	 */
	private CapitoloEntrataGestione elaboraCapitoloEntrataGestioneDalFlussoDati(Stipendio stipendio) {
		String methodName = "elaboraCapitoloEntrataGestioneDalFlussoDati";
		log.debug(methodName, "Inizio");
		// deve gestire 2 situazioni
		if (stipendio.getCapitolo() != null && stipendio.getCapitolo().getUid() != 0) {
			return (CapitoloEntrataGestione) stipendio.getCapitolo();
		}
		CapitoloEntrataGestione capitoloEntrataGestione = (CapitoloEntrataGestione) stipendio.getCapitolo();
		capitoloEntrataGestione.setEnte(stipendio.getEnte());
		capitoloEntrataGestione.setBilancio(stipendio.getBilancio());

		// annoCapitolo non viene passato dal file  setto l'anno di elaborazione come anno del capitolo
		capitoloEntrataGestione.setAnnoCapitolo(stipendio.getAnnoElaborazione());

		CapitoloEntrataGestione capitoloEntrataGestioneFromRisultatiDiRicerca = capitoloEntrataGestioneDad.findCapitoloEntrataGestioneFromCodiciStipendi(capitoloEntrataGestione, stipendio.getVoceContabile());
		// log.debug(methodName,"capitoloEntrataGestioneFromFindCapitoloEntrataGestione :"+capitoloEntrataGestioneFromRisultatiDiRicerca != null ? capitoloEntrataGestioneFromRisultatiDiRicerca.getUid() : "null");
		if (capitoloEntrataGestioneFromRisultatiDiRicerca == null) {
			capitoloEntrataGestione.setBilancio(stipendio.getBilancio());
			capitoloEntrataGestione.setNumeroUEB(1);
			RicercaPuntualeCapitoloEntrataGestioneResponse resCUG = ricercaPuntualeCapitoloEntrataGestione(capitoloEntrataGestione);
			capitoloEntrataGestioneFromRisultatiDiRicerca = resCUG.getCapitoloEntrataGestione();
		}
		log.debug(methodName, "Fine");
		return capitoloEntrataGestioneFromRisultatiDiRicerca;
	}

	/**
	 * ricercare il capitolo  di uscita dal flusso dati
	 * @param stipendio
	 * @return il capitolo
	 */
	private CapitoloUscitaGestione elaboraCapitoloUscitaGestioneDalFlussoDati(Stipendio stipendio) {
		if (stipendio.getCapitolo() != null && stipendio.getCapitolo().getUid() != 0) {
			return (CapitoloUscitaGestione) stipendio.getCapitolo();

		}
		// deve gestire 2 situazioni
		CapitoloUscitaGestione capitoloUscitaGestione = (CapitoloUscitaGestione) stipendio.getCapitolo();
		capitoloUscitaGestione.setEnte(stipendio.getEnte());
		capitoloUscitaGestione.setBilancio(stipendio.getBilancio());
		// annoCapitolo non viene passato da input setto l'anno di
		// elaborazione come anno del capitolo
		capitoloUscitaGestione.setAnnoCapitolo(stipendio.getAnnoElaborazione());
		CapitoloUscitaGestione capitoloUscitaGestioneFromRisultatiDiRicerca = capitoloUscitaGestioneDad.findCapitoloUscitaGestioneFromCodiciStipendi(capitoloUscitaGestione, stipendio.getVoceContabile());

		if (capitoloUscitaGestioneFromRisultatiDiRicerca == null) {
			capitoloUscitaGestione.setBilancio(stipendio.getBilancio());
			capitoloUscitaGestione.setNumeroUEB(1);
			RicercaPuntualeCapitoloUscitaGestioneResponse resCUG = ricercaPuntualeCapitoloUscitaGestione(capitoloUscitaGestione);
			capitoloUscitaGestioneFromRisultatiDiRicerca = resCUG.getCapitoloUscitaGestione();
		}

		return capitoloUscitaGestioneFromRisultatiDiRicerca;
	}

	/**
	 * effettua una ricerca degli accertamenti collegati all'elemento di
	 * bilancio se ne trova uno valido ---->lo setta
	 */
	private void caricaAccertamentoSubAccertamentoFromElementoDiBilancio(Stipendio stipendio) {
		String methodName = "caricaAccertamentoSubAccertamentoFromElementoDiBilancio";

		Accertamento accertamento = ricercaAccertamentoSubAccertamento(stipendio);

		if (accertamento == null || accertamento.getUid() == 0 ) {
			log.info(methodName, "Non e' stato trovato nessun accertamento e' necessario inserire uno automaticamente ");
			inserisciProvvedimento(stipendio);
			InserisceAccertamentiResponse response = inserisciAccertamentoAutomatico(stipendio);
			checkServiceResponseErrore(response);
			log.logXmlTypeObject(response, "Response del servizio inserisceAccertamentiResponse");
			accertamento = response.getElencoAccertamentiInseriti().get(0);
			stipendio.setAccertamentoAutomatico(true);
		}
		stipendio.setAccertamento(accertamento);

	}

	/**
	 * inserisco solo una volta l'atto amministrativo
	 * @param stipendio
	 */
	private void inserisciProvvedimento(Stipendio stipendio) {
		if (attoAmministrativo != null && attoAmministrativo.getUid() != 0) {
			return;
		}
		popolaAttoAmministrativoFromStipendio(stipendio);
		InserisceProvvedimento reqAA = new InserisceProvvedimento();
		reqAA.setEnte(ente);
		reqAA.setRichiedente(req.getRichiedente());
		reqAA.setAttoAmministrativo(attoAmministrativo);
		reqAA.setTipoAtto(attoAmministrativo.getTipoAtto());
		log.logXmlTypeObject(reqAA, "Request del servizio InserisceProvvedimento");
		InserisceProvvedimentoResponse response = provvedimentoService.inserisceProvvedimento(reqAA);
		checkServiceResponseFallimento(response);
		log.logXmlTypeObject(response, "Response  del servizio InserisceProvvedimento");

		attoAmministrativo = response.getAttoAmministrativoInserito();
	}

	/**
	 * VERSIONE V03 e' stato introdotto il provvedimento per gli accertamenti e'
	 * uguale per tutto il flusso
	 */
	private void popolaAttoAmministrativoFromStipendio(Stipendio stipendio) {
		String methodName = "popolaAttoAmministrativoFromStipendio";
		provvedimentoDad.setEnte(ente);
		List<TipoAtto> listaTipoAtto = provvedimentoDad.getElencoTipi();
		if (listaTipoAtto == null || listaTipoAtto.isEmpty()) {
			log.debug(methodName, "lista tipo atto vuota ");
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo Atto","codice MIN"), Esito.FALLIMENTO);
		}
		this.attoAmministrativo = new AttoAmministrativo();
		this.attoAmministrativo.setAnno(stipendio.getAnnoElaborazione());

		TipoAtto tipoAtto = ottieniMovimentoInterno(listaTipoAtto);
		this.attoAmministrativo.setTipoAtto(tipoAtto);
		this.attoAmministrativo.setOggetto("Atto amministrativo relativo al flusso stipendi (accertamenti automatici)");
		this.attoAmministrativo.setStatoOperativo(StatoOperativoAtti.DEFINITIVO);
	}

	/**
	 *
	 * @param listaTipoAtto
	 * @return il tipo di atto
	 */
	private TipoAtto ottieniMovimentoInterno(List<TipoAtto> listaTipoAtto) {
		String methodName = "ottieniMovimentoInterno";
		TipoAtto tipoAtto = null;
		for (TipoAtto t : listaTipoAtto) {
			if (SiacDAttoAmmTipoEnum.MovimentoInterno.getCodice().equals(t.getCodice())) {
				log.debug(methodName, "Trovato tipo atto con uid "+t.getUid());
				tipoAtto = t;
				continue;
			}
		}

		if (tipoAtto == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo Atto","codice MIN"), Esito.FALLIMENTO);
		}
		return tipoAtto;
	}

	/**
	 * se l'impegno non e' presente o non e' valido o non ha disponibilita
	 * sufficiente...eccc cerco un'altro impegno dall'elemento di bilancio
	 */
	private void caricaImpegnoSubImpegnoFromElementoDiBilancio(Stipendio stipendio) {
		String methodName = "caricaImpegnoSubImpegnoFromElementoDiBilancio";
		log.debug(methodName, "caricamento impegno e sub da elemento di bilancio ");

		Impegno impegno = ricercaImpegniSubimpegni(stipendio);

		if (impegno == null) {
			// non ci sono impegni nel db per i criteri di ricerca effettuata
			log.info(methodName, "Nessun impegno e' stato trovato nell'archivio ");
			String msg = "Movimento Non trovato per :"
			            + " Anno di riferimento "+stipendio.getAnnoDiRiferimento()
			            + " Capitolo "+stipendio.getCapitolo().getNumeroCapitolo()
			            + " Articolo "+stipendio.getCapitolo().getNumeroArticolo()
			            + " VOCE CONTABILE "+stipendio.getVoceContabile();

			log.info(methodName, msg);
			stipendio.setImpegno(null);
			stipendio.setSubImpegno(null);
			return;
		}
		stipendio.setImpegno(impegno);

	}

	/**
	 * ricerca soggettoPerChiave
	 *
	 * @param soggetto
	 * @return la response della ricerca
	 */
	private RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiave(Soggetto soggetto) {
		return sscg.ricercaSoggettoPerChiaveCachedSuccess(soggetto.getCodiceSoggetto());
	}

	/**
	 * controlla se il soggetto e' in stato valido
	 *
	 * @param stipendio
	 */
	private void controllaValiditaSoggetto(Stipendio stipendio) {
		Soggetto soggetto = stipendio.getSoggetto();
		String memethodName = "controllaValiditaSoggetto";
		log.info(memethodName, "controllo validita soggetto");
		if (soggetto != null && soggetto.getUid() > 0) {
			// se c'e un soggetto con il codice passato da input verifico se il
			// soggetto e' valido
			StatoOperativoAnagrafica statoOperativoAnagrafica = soggettoDad.findStatoOperativoAnagraficaSoggetto(soggetto);
			log.debug(memethodName, "Stato soggetto from find stato operativo anagrafica soggetto :"+statoOperativoAnagrafica);
			if (!StatoOperativoAnagrafica.VALIDO.equals(statoOperativoAnagrafica)) {
				stipendio.setDaAssociareAlDocumento(Boolean.FALSE);
				String msgSoggetto = "Soggetto con codiceSoggetto : " + soggetto.getCodiceSoggetto() + " NON VALIDO";
				log.info(memethodName,msgSoggetto);
				sendMessaggioConNumeroStipendioInElaborazione(msgSoggetto,stipendio,true);
				//throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO.getErrore(""), Esito.FALLIMENTO);
			}
		} else {
			stipendio.setDaAssociareAlDocumento(Boolean.FALSE);
			String descrizioneMSG = ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto ","codice "+ (soggetto != null ? soggetto.getCodiceSoggetto() : "null")).getDescrizione();
			log.info(memethodName,descrizioneMSG );
			sendMessaggioConNumeroStipendioInElaborazione(descrizioneMSG,stipendio,true);
		}
	}

	/*********************************************************************************************************************/

	/**
	* Guardare (par. 2.2.1) del tracciato
	* ‘TUTTO' tutti i record del file
	* ‘STIPE  elabora i tipiStipendio Lordo, Recuperi, e le sole entrate delle Ritenute(senza elaborarne la spesa)
	* ‘ONERI' elabora Oneri e la parte spesa delleRitenute
	 */
	private boolean isStipendioDaScartare(Stipendio stipendio) {
		String methodName="isStipendioDaScartare";
		log.debug(methodName, "isTipoElaborazioneSTIPE " + isTipoElaborazioneSTIPE());
		log.debug(methodName, "isTipoElaborazioneONERI " + isTipoElaborazioneONERI());
		log.debug(methodName, "isTipoElaborazioneTUTTO " + isTipoElaborazioneTUTTO());

		log.debug(methodName, "isTipoRecordRecuperi " + stipendio.isTipoRecordRecuperi());
		log.debug(methodName, "isTipoRecordOneri " + stipendio.isTipoRecordOneri());
		log.debug(methodName, "isTipoRecordRitenute " + stipendio.isTipoRecordRitenute());
		log.debug(methodName, "isTipoRecordStipendioLordo " + stipendio.isTipoRecordStipendioLordo());

		//Tipo Elaborazione E' TUTTO ----> Elaboro tutto il flusso senza scarti
		if(isTipoElaborazioneTUTTO()){
			return false;
		}
		//se Tipo Elaborazione E' STIPE
		//elabora i tipiStipendio Lordo, Recuperi, e le sole entrate delle Ritenute(senza elaborarne la spesa)
		if(isTipoElaborazioneSTIPE() && (stipendio.isTipoRecordStipendioLordo() || stipendio.isTipoRecordRecuperi()) || stipendio.isTipoRecordRitenute()){
			return false;
		}
		//se Tipo Elaborazione E' ONERI
		//elabora Oneri e la parte spesa delleRitenute
		return !(isTipoElaborazioneONERI() && (stipendio.isTipoRecordOneri() || stipendio.isTipoRecordRitenute()));
	}

	/**
	 * 1-effettua la ricercaImpegnoPerChiaveOttimizzata per subimpegno
	 * 2-controlla e verifica validità e disponibilita del sumbimpegno
	 * 3-verifica che il soggetto del subimpegno sia uguale a quello del documento
	 * 4-effettua la verifica sulla classe soggetto
	 * @param stipendio
	 * @return subimpegno valido ??
	 */
	private boolean checkAndSetImpegnoSubimpegnoFromSubImpegno(Stipendio stipendio) {
		String methodName = "checkAndSetImpegnoSubimpegnoFromSubImpegno";
		mgscg.setBilancio(stipendio.getBilancio());
		mgscg.setEnte(stipendio.getEnte());
		Impegno impegno = stipendio.getImpegno();
		String keySoggetto = " (codice soggetto " + stipendio.getSoggetto().getCodiceSoggetto() + ")";
		String keySoggettoDocumento = " (codice soggetto " + stipendio.getSoggetto().getCodiceSoggetto() + ")";
		
		StringBuilder keyMovimento = new StringBuilder()
			.append(impegno.getAnnoMovimento())
			.append("/")
			.append(impegno.getNumero().toPlainString());
		
		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		attributiMovimentoGestioneOttimizzato.setCaricaSub(true);
		attributiMovimentoGestioneOttimizzato.setEscludiSubAnnullati(true);
		attributiMovimentoGestioneOttimizzato.setSubPaginati(true);

		RicercaImpegnoPerChiaveOttimizzatoResponse response = mgscg.ricercaImpegnoPerChiaveOttimizzatoCached(impegno,attributiMovimentoGestioneOttimizzato, new DatiOpzionaliCapitoli(), stipendio.getSubImpegno());
		log.logXmlTypeObject(response, "Response del servizio RicercaImpegnoPerChiaveResponse");

		if(response.getImpegno() == null || response.getImpegno().getUid() == 0 || response.getImpegno().getElencoSubImpegni() == null  || response.getImpegno().getElencoSubImpegni().isEmpty() ){
			log.warn(methodName, "subimpegno Non presente sulla base dati  ");
			return false;
		}

		impegno = response.getImpegno();
		stipendio.setImpegno(impegno);
		SubImpegno subImpegno = impegno.getElencoSubImpegni().get(0);
		log.info(methodName, "Trovato subimpegno con uid  " + (subImpegno != null ? subImpegno.getUid() : "null") +" dell'impegno "+impegno.getUid());
		log.info(methodName, "UID soggetto del subImpegno :  " + (subImpegno != null && subImpegno.getSoggetto() != null ? subImpegno.getSoggetto().getUid() : "null"));

		boolean subimpegnoValidoDef = Boolean.TRUE;

		if ((subImpegno != null && subImpegno.getUid() != 0) && !StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(subImpegno.getStatoOperativoMovimentoGestioneSpesa())) {
			String keySubImpegno = subImpegno.getAnnoMovimento() + "/" + subImpegno.getNumero();
			sendMessaggioConNumeroStipendioInElaborazione(ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore(keySubImpegno).getDescrizione(), stipendio, true);
			stipendio.setDaAssociareAlDocumento(false);
			log.info(methodName, ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore(keySubImpegno).getDescrizione());
			return false;

		}

		if (isSubImpegnoDisponibile(stipendio, subImpegno)) {
			log.info(methodName, "Subimpegno ha disponibilita sufficiente");
			stipendio.setSubImpegno(subImpegno);
			keySoggetto = " (codice soggetto " + stipendio.getSubImpegno().getSoggetto().getCodiceSoggetto() + ")";
			subimpegnoValidoDef = Boolean.TRUE;
		} else {
			log.info(methodName, "SubImpegno non ha disponibilita sufficiente ");
			String key = "Movimento : " + stipendio.getImpegno().getAnnoMovimento() + "/" + stipendio.getImpegno().getNumero() + "/" + stipendio.getSubImpegno().getNumero();
			String msg = "(nome file: " + nomeFile + ") " + ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO.getErrore(key).getTesto();

			log.info(methodName, msg);
			Messaggio m = new Messaggio();
			m.setCodice("DISPONIBILITA_MOVIMENTO");
			m.setDescrizione(msg);
			res.addMessaggio(m);
			subimpegnoValidoDef = Boolean.FALSE;
		}

		keyMovimento.append("-")
			.append(subImpegno.getNumero().toPlainString());

		Soggetto soggettoCollegatoAlDocumento = stipendio.getSoggetto();
		Soggetto soggettoCollegatoAlSubImpegno = subImpegno.getSoggetto();

		if (soggettoCollegatoAlSubImpegno != null && soggettoCollegatoAlSubImpegno.getUid()!=0 && (soggettoCollegatoAlSubImpegno.getUid() != soggettoCollegatoAlDocumento.getUid())) {
			StringBuilder sb = new StringBuilder();
			sb.append(" Operazione non possibile: il creditore del movimento-");
			sb.append("SubImpegno");
			sb.append(" ");
			sb.append(keyMovimento.toString());
			sb.append(keySoggetto);
			sb.append(" e' diverso da quello del documento");
			sb.append(" ");
			sb.append(keySoggettoDocumento);

			sendMessaggioConNumeroStipendioInElaborazione(sb.toString(), stipendio, true);
			stipendio.setDaAssociareAlDocumento(false);
			log.info(methodName, sb.toString());
			stipendio.setImpegno(null);
			stipendio.setSubImpegno(null);

			return false;
		}

		// JIRA 2725: l'importante e' che una classe soggetto ci sia
		// JIRA 3258 : il controllo sulla classe soggetto bisogna farlo solo nel
		// caso in cui l'impegno o sub non hanno un soggetto collegato

		// La classe soggetto NON ha l'uid popolato
		if ((soggettoCollegatoAlSubImpegno == null  || soggettoCollegatoAlSubImpegno.getUid() == 0 ) && (subImpegno.getClasseSoggetto() == null || StringUtils.isBlank(subImpegno.getClasseSoggetto().getCodice()))) {
			log.info(methodName, "Impegno " + subImpegno.getUid() + " senza classe soggetto");
			StringBuilder sb = new StringBuilder()
				.append(" Operazione non possibile: il movimento ")
				.append("SubImpegno")
				.append(" ")
				.append(keyMovimento.toString())
				.append(" non ha una classe soggetto associata");
			sendMessaggioConNumeroStipendioInElaborazione(sb.toString(), stipendio, true);
			stipendio.setDaAssociareAlDocumento(false);
			stipendio.setImpegno(null);
			stipendio.setSubImpegno(null);
			log.info(methodName, sb.toString());
			return false;
		}
		// prendo il capitolo dell'impegno
		CapitoloUscitaGestione capitoloFromImpegno = response.getCapitoloUscitaGestione();
		stipendio.setCapitolo(capitoloFromImpegno);
		return subimpegnoValidoDef;
	}


	/**
	 * 1-effettua la ricercaImpegnoPerChiaveOttimizzata per impegno
	 * 2-controlla e verifica validità e disponibilita dell'impegno
	 * 3-verifica che il soggetto dell'impegno sia uguale a quello del documento
	 * 4-effettua la verifica sulla classe soggetto
	 * @param stipendio
	 * @return impegno valido ??
	 */
	private boolean checkAndSetImpegnoSubimpegnoFromImpegno(Stipendio stipendio) {
		String methodName = "checkAndSetImpegnoSubimpegnoFromImpegno";
		mgscg.setBilancio(stipendio.getBilancio());
		mgscg.setEnte(stipendio.getEnte());
		Impegno impegno = stipendio.getImpegno();
		String keyMovimento = impegno.getAnnoMovimento() + "/" + impegno.getNumero().toPlainString();
		String keySoggetto = " (codice soggetto " + stipendio.getSoggetto().getCodiceSoggetto() + ")";
		String keySoggettoDocumento = " (codice soggetto " + stipendio.getSoggetto().getCodiceSoggetto() + ")";
		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		attributiMovimentoGestioneOttimizzato.setCaricaSub(false);
		attributiMovimentoGestioneOttimizzato.setEscludiSubAnnullati(true);
		attributiMovimentoGestioneOttimizzato.setSubPaginati(true);

		RicercaImpegnoPerChiaveOttimizzatoResponse response = mgscg.ricercaImpegnoPerChiaveOttimizzatoCached(impegno,attributiMovimentoGestioneOttimizzato, new DatiOpzionaliCapitoli());
		log.logXmlTypeObject(response, "Response del servizio RicercaImpegnoPerChiaveOttimizzatoResponse");

		if(response.getImpegno() == null || response.getImpegno().getUid() == 0){
			log.warn(methodName, "impegno Non presente sulla base dati  ");
			return false;
		}

		impegno = response.getImpegno();
		stipendio.setImpegno(impegno);
		log.info(methodName, "Trovato impegno con uid  " + impegno.getUid());

		boolean impegnoValidoDef = Boolean.TRUE;

		log.info(methodName, "stato operativo impegno " + impegno.getStatoOperativoMovimentoGestioneSpesa());

		// impegno valorizzato ma non subimpegno o impegno non ha subimpegni
		if (!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())) {
			String keyImpegno = impegno.getAnnoMovimento() + "/" + impegno.getNumero();
			sendMessaggioConNumeroStipendioInElaborazione(ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore(keyImpegno).getDescrizione(), stipendio, true);
			stipendio.setDaAssociareAlDocumento(false);
			log.info(methodName, ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore(keyImpegno).getDescrizione());
			return false;
		}

		if (isImpegnoDisponibile(stipendio, impegno)) {
			stipendio.setImpegno(impegno);
			keySoggetto = " (codice soggetto " + stipendio.getImpegno().getSoggetto().getCodiceSoggetto() + ")";
			impegnoValidoDef = Boolean.TRUE;
		} else {
			String key = "Movimento : " + impegno.getAnnoMovimento() + "/" + impegno.getNumero();
			String msg = "(nome file: " + nomeFile + ") " + ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO.getErrore(key).getTesto();

			log.info(methodName, msg);
			Messaggio m = new Messaggio();
			m.setCodice("DISPONIBILITA_MOVIMENTO");
			m.setDescrizione(msg);
			res.addMessaggio(m);
			impegnoValidoDef = Boolean.FALSE;
		}

		Soggetto soggettoCollegatoAlDocumento = stipendio.getSoggetto();
		Soggetto soggettoCollegatoAllImpegno = impegno.getSoggetto();

		if (soggettoCollegatoAllImpegno != null && soggettoCollegatoAllImpegno.getUid()!=0 && (soggettoCollegatoAllImpegno.getUid() != soggettoCollegatoAlDocumento.getUid())) {
			StringBuilder sb = new StringBuilder();
			sb.append(" Operazione non possibile: il creditore del movimento-");
			sb.append("Impegno");
			sb.append(" ");
			sb.append(keyMovimento);
			sb.append(keySoggetto);
			sb.append(" e' diverso da quello del documento");
			sb.append(" ");
			sb.append(keySoggettoDocumento);

			sendMessaggioConNumeroStipendioInElaborazione(sb.toString(), stipendio, true);
			stipendio.setDaAssociareAlDocumento(false);
			log.info(methodName, sb.toString());
			stipendio.setImpegno(null);
			stipendio.setSubImpegno(null);

			return false;
		}

		// JIRA 2725: l'importante e' che una classe soggetto ci sia
		// JIRA 3258 : il controllo sulla classe soggetto bisogna farlo solo nel
		// caso in cui l'impegno o sub non hanno un soggetto collegato

		// La classe soggetto NON ha l'uid popolato
		if ((soggettoCollegatoAllImpegno == null  || soggettoCollegatoAllImpegno.getUid() == 0 ) && (impegno.getClasseSoggetto() == null || StringUtils.isBlank(impegno.getClasseSoggetto().getCodice()))) {
			log.info(methodName, "Impegno " + impegno.getUid() + " senza classe soggetto");
			StringBuilder sb = new StringBuilder()
				.append(" Operazione non possibile: il movimento ")
				.append("Impegno")
				.append(" ")
				.append(keyMovimento)
				.append(" non ha una classe soggetto associata");
			sendMessaggioConNumeroStipendioInElaborazione(sb.toString(), stipendio, true);
			stipendio.setDaAssociareAlDocumento(false);
			stipendio.setImpegno(null);
			stipendio.setSubImpegno(null);
			log.info(methodName, sb.toString());
			return false;
		}
		// prendo il capitolo dell'impegno
		CapitoloUscitaGestione capitoloFromImpegno = response.getCapitoloUscitaGestione();
		stipendio.setCapitolo(capitoloFromImpegno);
		return impegnoValidoDef;
	}

	/**
	 * 1-effettua la ricercaAccertamentoPerChiaveOttimizzata per subaccertamento
	 * 2-controlla e verifica validità e disponibilita del subaccertamento
	 * 3-verifica che il soggetto del subaccertamento sia uguale a quello del documento
	 * 4-effettua la verifica sulla classe soggetto
	 * @param stipendio
	 * @return subaccertamento valido
	 */
	private int checkAndSetAccertamentoSubAccertamento(Stipendio stipendio) {
		String methodName = "checkAndSetAccertamentoSubAccertamento";
		log.info(methodName, "Verifico accertamento subAccertamento");
		Accertamento accertamento = stipendio.getAccertamento();
		int returnType = ACCERTAMENTO_SUB_VALIDO;
		String keySoggetto = " (codice soggetto " + stipendio.getSoggetto().getCodiceSoggetto() + ")";
		String keySoggettoDocumento = " (codice soggetto " + stipendio.getSoggetto().getCodiceSoggetto() + ")";
		StringBuilder keyMovimento = new StringBuilder()
				.append(accertamento.getAnnoMovimento())
				.append("/")
				.append(accertamento.getNumero().toPlainString());

		mgscg.setBilancio(stipendio.getBilancio());
		mgscg.setEnte(stipendio.getEnte());

		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		attributiMovimentoGestioneOttimizzato.setCaricaSub(true);
		attributiMovimentoGestioneOttimizzato.setEscludiSubAnnullati(true);

		RicercaAccertamentoPerChiaveOttimizzatoResponse response = mgscg.ricercaAccertamentoPerChiaveOttimizzatoNoSuccess(accertamento,attributiMovimentoGestioneOttimizzato, new DatiOpzionaliCapitoli(),stipendio.getSubAccertamento());
		log.logXmlTypeObject(response, "Response del servizio ricercaAccertamentoPerChiaveOttimizzata");

		if(response.getAccertamento() == null || response.getAccertamento().getUid() == 0 || response.getAccertamento().getElencoSubAccertamenti() == null || response.getAccertamento().getElencoSubAccertamenti().isEmpty()){
			log.warn(methodName, "SubAccertamento Non presente sulla base dati  ");
			return CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO;
		}

		accertamento = response.getAccertamento();
		stipendio.setAccertamento(accertamento);
		SubAccertamento subAccertamento = response.getAccertamento().getElencoSubAccertamenti().get(0);
		log.debug(methodName, "trovato Accertamento con uid " +accertamento.getUid()+" e  subaccertamento con uid "+(subAccertamento != null ? subAccertamento.getUid() : "null"));
		
		// Check del subAccertamento nella lista degli accertamenti
		if ((subAccertamento != null && subAccertamento.getUid() != 0)
				&& !StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(subAccertamento.getStatoOperativoMovimentoGestioneEntrata())) {

			String keySubAccertamento = subAccertamento.getAnnoMovimento() + "/" + subAccertamento.getNumero();
			sendMessaggioConNumeroStipendioInElaborazione(ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(keySubAccertamento).getDescrizione(), stipendio, true);
			log.debug(methodName, ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(keySubAccertamento).getDescrizione());
			stipendio.setDaAssociareAlDocumento(Boolean.FALSE);
			return CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO;
		}
		
		Soggetto soggettoCollegatoAlsubAccertamento = subAccertamento.getSoggetto();
		Soggetto soggettoCollegatoAlDocumento = stipendio.getSoggetto();

		log.debug(methodName, "SubAcccertamento UID Soggetto : " + soggettoCollegatoAlsubAccertamento.getUid());
		log.debug(methodName, "soggettoDelloStipendio UID Soggetto : " + (soggettoCollegatoAlDocumento != null ? soggettoCollegatoAlDocumento.getUid() : "null"));

		keyMovimento.append("-")
			.append(subAccertamento.getNumero().toPlainString());

		if (soggettoCollegatoAlDocumento !=null && soggettoCollegatoAlDocumento.getUid() != 0 && (soggettoCollegatoAlDocumento.getUid() != soggettoCollegatoAlDocumento.getUid())) {
			StringBuilder sb = new StringBuilder();
			sb.append(" Operazione non possibile: il creditore del movimento-");
			sb.append("SubAccertamento");
			sb.append(" ");
			sb.append(keyMovimento.toString());
			sb.append(keySoggetto);
			sb.append(" e' diverso da quello del documento");
			sb.append(" ");
			sb.append(keySoggettoDocumento);
			sendMessaggioConNumeroStipendioInElaborazione(sb.toString(),stipendio,true);
			log.debug(methodName, sb.toString());
			stipendio.setDaAssociareAlDocumento(false);
			stipendio.setAccertamento(null);
			stipendio.setSubAccertamento(null);
			return CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO;

		}
		// JIRA 2725: l'importante e' che una classe soggetto ci sia
		// L'accertamento NON ha una classe soggetto con uid popolato
		if(!(soggettoCollegatoAlDocumento !=null && soggettoCollegatoAlDocumento.getUid() != 0 ) && (subAccertamento.getClasseSoggetto() == null || StringUtils.isBlank(subAccertamento.getClasseSoggetto().getCodice()))) {
			log.info(methodName, "subaccertamento  " + subAccertamento.getUid() + " non ha una classe soggetto");
			StringBuilder sb = new StringBuilder()
				.append(" Operazione non possibile: il movimento ")
				.append("SubAccertamento")
				.append(" ")
				.append(keyMovimento.toString())
				.append(" non ha una classe soggetto associata");
			sendMessaggioConNumeroStipendioInElaborazione(sb.toString(), stipendio, true);
			stipendio.setDaAssociareAlDocumento(false);
			stipendio.setAccertamento(null);
			stipendio.setSubAccertamento(null);
			log.info(methodName, sb.toString());
			return CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO;
		}

		if (isSubAccertamentoDisponibile(stipendio, subAccertamento)) {
			stipendio.setSubAccertamento(subAccertamento);
			log.info(methodName, "SubAccertamento ha disponibilita sufficiente");
			returnType = ACCERTAMENTO_SUB_VALIDO;

		} else {
			log.info(methodName, "SubAccertamento non ha disponibilita sufficiente");
			//AHMAD NAZHA 05/02/2018 SIAC-5638
			returnType = SUBACCERTAMENTO_SENZA_DISPONIBILITA;

		}
		// prendo il capitolo dell'accertamento
		CapitoloEntrataGestione capitoloFromAccertamento = response.getCapitoloEntrataGestione();
		stipendio.setCapitolo(capitoloFromAccertamento);
		return returnType;
	}


	/**
	 * 1-effettua la ricercaAccertamentoPerChiaveOttimizzata per accertamento
	 * 2-controlla e verifica validità e disponibilita del accertamento
	 * 3-verifica che il soggetto del accertamento sia uguale a quello del documento
	 * 4-effettua la verifica sulla classe soggetto
	 * @param stipendio
	 * @return accertamento valido
	 */
	private int checkAndSetAccertamento(Stipendio stipendio) {
		String methodName = "checkAndSetAccertamento";
		log.info(methodName, "Verifico accertamento");
		Accertamento accertamento = stipendio.getAccertamento();
		int returnType = ACCERTAMENTO_SUB_VALIDO;
		String keyMovimento = accertamento.getAnnoMovimento() + "/" + accertamento.getNumero().toPlainString();
		String keySoggetto = " (codice soggetto " + stipendio.getSoggetto().getCodiceSoggetto() + ")";
		String keySoggettoDocumento = " (codice soggetto " + stipendio.getSoggetto().getCodiceSoggetto() + ")";

		mgscg.setBilancio(stipendio.getBilancio());
		mgscg.setEnte(stipendio.getEnte());
		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		attributiMovimentoGestioneOttimizzato.setCaricaSub(false);
		attributiMovimentoGestioneOttimizzato.setEscludiSubAnnullati(true);

		RicercaAccertamentoPerChiaveOttimizzatoResponse response = mgscg.ricercaAccertamentoPerChiaveOttimizzatoNoSuccess(accertamento,attributiMovimentoGestioneOttimizzato, new DatiOpzionaliCapitoli(), null);
		log.logXmlTypeObject(response, "Response del servizio ricercaAccertamentoPerChiaveOttimizzata");
		if(response.getAccertamento() == null || response.getAccertamento().getUid() == 0 ){
			log.warn(methodName, "Accertamento Non presente sulla base dati  ");
			return CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO;
		}
		accertamento = response.getAccertamento();
		Soggetto soggettoCollegatoAllaccertamento = accertamento.getSoggetto();
		Soggetto soggettoCollegatoAlDocumento = stipendio.getSoggetto();
		// non ha subaccertamenti
		log.info(methodName, "codice stato operativo accertamento : " + accertamento.getStatoOperativoMovimentoGestioneEntrata());
		if (!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())) {

			String keyAccertamento = accertamento.getAnnoMovimento() + "/" + accertamento.getNumero();
			sendMessaggioConNumeroStipendioInElaborazione(ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(keyAccertamento).getDescrizione(), stipendio, true);
			log.debug(methodName, ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(keyAccertamento).getDescrizione());
			stipendio.setDaAssociareAlDocumento(false);
			return CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO;
		}


		log.debug(methodName, "Acccertamento UID Soggetto : " + (soggettoCollegatoAllaccertamento != null ? soggettoCollegatoAllaccertamento.getUid() : "null"));
		log.debug(methodName, "soggettoDelloStipendio UID Soggetto : " + soggettoCollegatoAlDocumento.getUid());

		if (soggettoCollegatoAllaccertamento !=null && soggettoCollegatoAllaccertamento.getUid() !=0 && (soggettoCollegatoAllaccertamento.getUid() != soggettoCollegatoAlDocumento.getUid())) {
			StringBuilder sb = new StringBuilder();
			sb.append(" Operazione non possibile: il creditore del movimento-");
			sb.append("Accertamento");
			sb.append(" ");
			sb.append(keyMovimento);
			sb.append(keySoggetto);
			sb.append(" e' diverso da quello del documento");
			sb.append(" ");
			sb.append(keySoggettoDocumento);
			sendMessaggioConNumeroStipendioInElaborazione(sb.toString(),stipendio,true);
			log.debug(methodName, sb.toString());
			stipendio.setDaAssociareAlDocumento(false);
			stipendio.setAccertamento(null);
			stipendio.setSubAccertamento(null);
			returnType = CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO;
		}
		// JIRA 2725: l'importante e' che una classe soggetto ci sia
		// L'accertamento NON ha una classe soggetto con uid popolato
		if(!(soggettoCollegatoAllaccertamento !=null && soggettoCollegatoAllaccertamento.getUid() !=0 ) && (accertamento.getClasseSoggetto() == null || StringUtils.isBlank(accertamento.getClasseSoggetto().getCodice()))) {
			log.info(methodName,"Accertamento  " + accertamento.getUid() + " senza classe soggetto");
			StringBuilder sb = new StringBuilder()
				.append(" Operazione non possibile: il movimento ")
				.append("Accertamento")
				.append(" ")
				.append(keyMovimento)
				.append(" non ha una classe soggetto associata");
			sendMessaggioConNumeroStipendioInElaborazione(sb.toString(), stipendio, true);
			stipendio.setDaAssociareAlDocumento(false);
			stipendio.setAccertamento(null);
			stipendio.setSubAccertamento(null);
			log.info(methodName, sb.toString());
			return CARICA_ACCERTAMENTO_SUB_DA_ELEMENTO_BILANCIO;
		}

		if (isAccertamentoDisponibile(stipendio, accertamento)) {
			stipendio.setAccertamento(accertamento);
			returnType = ACCERTAMENTO_SUB_VALIDO;

		} else {
			log.info(methodName, "Accertamento non ha disponibilita sufficiente");
			//AHMAD NAZHA 05/02/2018 SIAC-5638
			returnType = ACCERTAMENTO_SENZA_DISPONIBILITA;
		}

		// prendo il capitolo dell'accertamento
		CapitoloEntrataGestione capitoloFromAccertamento = response.getCapitoloEntrataGestione();
		stipendio.setCapitolo(capitoloFromAccertamento);
		return returnType;
	}

	/**
	 * richiama il servizio di ricerca accertamento subAccertamento recuperare
	 * l'elemento di bilancio da utilizzare solo quando l'accerrtamento o non e'
	 * valorizzato O quando l'accertamento non ha disponibilita
	 */
	private Accertamento ricercaAccertamentoSubAccertamento(Stipendio stipendio) {
		String methodName = "ricercaAccertamentoSubAccertamento" ;
		RicercaAccertamentiSubAccertamenti reqRAS = creaRequestRicercaAccertamentiSubAccertamenti(stipendio);
		reqRAS.setNumPagina(1);
		reqRAS.setNumRisultatiPerPagina(StipendioParams.NUM_RISULTATI_PER_PAGINA);
		RicercaAccertamentiSubAccertamentiResponse response = movimentoGestioneService.ricercaAccertamentiSubAccertamenti(reqRAS);
		checkServiceResponseErrore(response);
		Accertamento accertamentoDisponibile = new Accertamento();

		// verifico la response
		boolean isResponseHasAccertamenti = response != null && response.getListaAccertamenti() != null && response.getNumPagina() != 0 && response.getNumRisultati() != 0;
		double floatResult = response.getNumRisultati() * 1.0 / StipendioParams.NUM_RISULTATI_PER_PAGINA;
		int numeroIterazionePerOttenereLaListaCompletaDegliIAccertamenti = (int) Math.ceil(floatResult);

		if (isResponseHasAccertamenti) {
			List<Accertamento> listaAccertamenti = response.getListaAccertamenti();
			accertamentoDisponibile = cercaAccetamentoDisponibileValidoFromListaAccertamenti(listaAccertamenti, stipendio);
			if (numeroIterazionePerOttenereLaListaCompletaDegliIAccertamenti > 1 && (accertamentoDisponibile == null || accertamentoDisponibile.getUid() == 0)) {
				for (int i = numeroIterazionePerOttenereLaListaCompletaDegliIAccertamenti; i > 1; i--) {
					log.debug("FOR", " ITERAZIONE NUMERO : " + i);
					if (i == 1 || (accertamentoDisponibile != null && accertamentoDisponibile.getUid() > 0)) {
						break;
					}
					reqRAS.setNumPagina(i);
					response = movimentoGestioneService.ricercaAccertamentiSubAccertamenti(reqRAS);
					checkServiceResponseErrore(response);
					accertamentoDisponibile = cercaAccetamentoDisponibileValidoFromListaAccertamenti(response.getListaAccertamenti(), stipendio);
				}
			}
			log.debug(methodName, accertamentoDisponibile != null ? "trovato accertamento con uid " + accertamentoDisponibile.getUid() : "nessun accertamento ha disponibilita sufficiente ");
		}
		return accertamentoDisponibile;
	}

	private Accertamento cercaAccetamentoDisponibileValidoFromListaAccertamenti(List<Accertamento> listaAccertamenti, Stipendio stipendio) {
		String methodName = "cercaAccetamentoDisponibileValidoFromListaAccertamenti" ;
		for (Accertamento accertamento : listaAccertamenti) {
			// prendo il primo che
			if (isAccertamentoValido(accertamento, stipendio)) {
				log.info(methodName, "Accertamento trovato con numero : " + accertamento.getNumero());
				stipendio.setSubAccertamento(null);
				return accertamento;
			}
		}
		return null;
	}

	private RicercaAccertamentiSubAccertamenti creaRequestRicercaAccertamentiSubAccertamenti(Stipendio stipendio) {
		RicercaAccertamentiSubAccertamenti reqRAS = new RicercaAccertamentiSubAccertamenti();

		reqRAS.setDataOra(new Date());
		reqRAS.setEnte(stipendio.getEnte());
		reqRAS.setRichiedente(req.getRichiedente());
		ParametroRicercaAccSubAcc parametroRicercaAccSubAcc = new ParametroRicercaAccSubAcc();
		parametroRicercaAccSubAcc.setAnnoEsercizio(stipendio.getBilancio().getAnno());
		parametroRicercaAccSubAcc.setDisponibilitaAdIncassare(true);
		parametroRicercaAccSubAcc.setAnnoAccertamento(stipendio.getAnnoDiRiferimento());
		// passo il capitolo
		if (stipendio.getCapitolo() != null &&  stipendio.getCapitolo().getUid() !=0 && stipendio.getCapitolo().getNumeroArticolo() != null && stipendio.getCapitolo().getNumeroCapitolo() != null ) {
			parametroRicercaAccSubAcc.setNumeroArticolo(stipendio.getCapitolo().getNumeroArticolo());
			parametroRicercaAccSubAcc.setNumeroCapitolo(stipendio.getCapitolo().getNumeroCapitolo());
			parametroRicercaAccSubAcc.setUidCapitolo(stipendio.getCapitolo().getUid() );

		}
		reqRAS.setParametroRicercaAccSubAcc(parametroRicercaAccSubAcc);
		return reqRAS;
	}

	/**
	 * richiama il servizio ricercaImpegniSubimpegniDocSpesa per recuperare
	 * l'elemento di bilancio da utilizzare solo quando l'IMPEGNO non e'
	 * valorizzato O quando L'IMPEGNO non ha disponibilita
	 *
	 * @param stipendio
	 * @return l'impegno
	 */
	private Impegno ricercaImpegniSubimpegni(Stipendio stipendio) {
		String methodName = "ricercaImpegniSubimpegni";
		log.info(methodName, "RicercaImpegniSubImpegni");
		Impegno impegnoDisponibile = new Impegno();
		RicercaImpegniSubImpegni reqRIS = creaRequestRicercaImpegniSubImpegni(stipendio);
		reqRIS.setNumPagina(1);
		reqRIS.setNumRisultatiPerPagina(StipendioParams.NUM_RISULTATI_PER_PAGINA);

		log.logXmlTypeObject(reqRIS, "Request del servizio ricercaImpegniSubimpegni");
		RicercaImpegniSubimpegniResponse response = movimentoGestioneService.ricercaImpegniSubimpegni(reqRIS);
		checkServiceResponseErrore(response);
		log.logXmlTypeObject(response, "Risposta ottenuta dal servizio ricercaImpegniSubimpegni");

		// verifico la response
		boolean isResponseHasImpegni = response != null && response.getListaImpegni() != null && response.getNumPagina() != 0 && response.getNumRisultati() != 0;
		double floatResult = response.getNumRisultati() * 1.0 / StipendioParams.NUM_RISULTATI_PER_PAGINA;
		int numeroIterazionePerOttenereLaListaCompletaDegliImpegni = (int) Math.ceil(floatResult);

		if (isResponseHasImpegni) {
			List<Impegno> listaImpegni = response.getListaImpegni();
			impegnoDisponibile = cercaImpegnoDisponibileValidoFromListaImpegni(listaImpegni, stipendio);
			if (numeroIterazionePerOttenereLaListaCompletaDegliImpegni > 1  && (impegnoDisponibile == null || impegnoDisponibile.getUid() == 0)) {
				for (int i = numeroIterazionePerOttenereLaListaCompletaDegliImpegni ; i > 1 ; i--) {
					log.debug("FOR", " ITERAZIONE NUMERO : " + i);
					if (i == 1 || (impegnoDisponibile != null && impegnoDisponibile.getUid() >0)) {
						break;
					}

					reqRIS.setNumPagina(i);
					response = movimentoGestioneService.ricercaImpegniSubimpegni(reqRIS);
					checkServiceResponseErrore(response);
					impegnoDisponibile = cercaImpegnoDisponibileValidoFromListaImpegni(response.getListaImpegni(), stipendio);
				}
				log.debug(methodName, impegnoDisponibile != null ? "trovato impegno con uid " +impegnoDisponibile.getUid() : "nessun impegno ha disponibilita sufficiente ");
			}
		}
		return impegnoDisponibile;
	}

	/**
	 * trova un impegno valido con disponibilita a partire listaImpegni
	 * @param listaImpegni
	 * @param stipendio
	 * @return l'impegno
	 */
	private Impegno cercaImpegnoDisponibileValidoFromListaImpegni(List<Impegno> listaImpegni, Stipendio stipendio) {
		String methodName = "cercaECaricaImpegnoDisponibileValidoFromListaImpegni";
		for (Impegno impegno : listaImpegni) {
			log.debug(methodName, "TIPO IMPEGNO :" +impegno.getTipoImpegno().getCodice());
			boolean impegnoValido = isImpegnoValido(impegno, stipendio);
			log.debug(methodName, "isImpegno valido :" +impegnoValido);

			// prendo il primo che
			if (impegnoValido && impegno.getTipoImpegno() != null && !"MUT".equals(impegno.getTipoImpegno().getCodice())) {
				log.info(methodName, "impegno trovato con numero : " + impegno.getNumero());
				stipendio.setSubImpegno(null);
				return impegno;
			}
		}
		return null;
	}

	/**
	 * creaRequestRicercaImpegniSubImpegni
	 * @param stipendio
	 * @return la request di ricerca
	 */
	private RicercaImpegniSubImpegni creaRequestRicercaImpegniSubImpegni(Stipendio stipendio) {
		String  methodName = "creaRequestRicercaImpegniSubImpegni" ;
		log.info(methodName, "creaRequestRicercaImpegniSubImpegni");
		RicercaImpegniSubImpegni reqRIS = new RicercaImpegniSubImpegni();
		reqRIS.setDataOra(new Date());
		reqRIS.setEnte(stipendio.getEnte());
		reqRIS.setRichiedente(req.getRichiedente());
		ParametroRicercaImpSub parametroRicercaImpSub = new ParametroRicercaImpSub();
		parametroRicercaImpSub.setAnnoEsercizio(stipendio.getBilancio().getAnno());
		parametroRicercaImpSub.setAnnoImpegno(stipendio.getAnnoDiRiferimento());
		parametroRicercaImpSub.setIsRicercaDaImpegno(Boolean.TRUE);
		// passo il capitolo
		if (stipendio.getCapitolo() != null &&  stipendio.getCapitolo().getUid() !=0 && stipendio.getCapitolo().getNumeroArticolo() != null && stipendio.getCapitolo().getNumeroCapitolo() != null ) {
			parametroRicercaImpSub.setNumeroArticolo(stipendio.getCapitolo().getNumeroArticolo());
			parametroRicercaImpSub.setNumeroCapitolo(stipendio.getCapitolo().getNumeroCapitolo());
			parametroRicercaImpSub.setUidCapitolo(stipendio.getCapitolo().getUid() );
		}

		reqRIS.setParametroRicercaImpSub(parametroRicercaImpSub);

		return reqRIS;
	}

	/**
	 * quando non si trova l'accertamento con disponibilita sufficiente o uno
	 * presente sul db che abbia disponibilita sufficiente bisogna inserire un
	 * accertamento nuovo
	 *
	 * @param stipendio
	 * @return la response di inserimento
	 */
	protected InserisceAccertamentiResponse inserisciAccertamentoAutomatico(Stipendio stipendio) {
		final String methodName = "inserisciAccertamentoAutomatico";
		CapitoloEntrataGestione capitolo = (CapitoloEntrataGestione) stipendio.getCapitolo();
		if (capitolo == null || capitolo.getUid() == 0) {
			log.debug(methodName, "non ci sono dati sufficienti per inserire l'accertamento automatico");
			return null;
		}
		log.debug(methodName, "inserisco l'accertamento");
		Accertamento accertamento = new Accertamento();
		accertamento.setAnnoMovimento(stipendio.getBilancio().getAnno());
		accertamento.setImportoAttuale(stipendio.getImportoEntrata());
		// SIAC-3969
		accertamento.setImportoIniziale(stipendio.getImportoEntrata());
		accertamento.setImportoUtilizzabile(stipendio.getImportoEntrata());

		accertamento.setSoggetto(stipendio.getSoggetto());
		accertamento.setCapitoloEntrataGestione(capitolo);
		accertamento.setAttoAmministrativo(attoAmministrativo);
		accertamento.setStatoOperativoMovimentoGestioneEntrata(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice());
		CapitoloEntrataGestione capitoloEntrata = (CapitoloEntrataGestione)stipendio.getCapitolo();
		log.logXmlTypeObject(capitoloEntrata, "Capitolo entrata");

		// imposto i tre campi successivi in quanto non lo fa il servizio di FIN
		if(capitolo.getElementoPianoDeiConti() != null){
			accertamento.setIdPdc(capitolo.getElementoPianoDeiConti().getUid());
			accertamento.setCodicePdc(capitolo.getElementoPianoDeiConti().getCodice());
			accertamento.setDescPdc(capitolo.getElementoPianoDeiConti().getDescrizione());

		}

 		accertamento.setCapitoloEntrataGestione(capitoloEntrata);
		mgscg.setBilancio(stipendio.getBilancio());
		mgscg.setEnte(stipendio.getEnte());
		log.logXmlTypeObject(accertamento, "Accertamento inserisciAccertamenti");

		InserisceAccertamentiResponse resInsAcc = mgscg.inserisceAccertamenti(accertamento);
		counter.addAccertamentoAutomatico();

		return resInsAcc;

	}


	/**
	 * Ordinamento di secondo livello per gli stipendi in base al movimento
	 *
	 * @param stipendiDaOrdinare lista da ordinare
	 */
	public void sortStipendiSecondoLivello(List<Stipendio> stipendiDaOrdinare) {
		if (stipendiDaOrdinare != null && !stipendi.isEmpty()) {
			log.debug("sortStipendiSecondoLivello", "Ordinamento secondo livello in corso ...");
			//nel caso si verifichi un errore Comparison method violates its general contract!, modificare il codice segnalato all'interno di ComparatorStipendioSecondoLivello.java 
			Collections.sort(stipendiDaOrdinare, ComparatorStipendioSecondoLivello.INSTANCE);
			log.debug("","############################## ORDINAMENTO SECONDO LIVELLO###############################################################################");
			stampaListaStipendio(stipendiDaOrdinare);
			return;
		}
		log.debug("sortStipendiSecondoLivello", "La lista fornita in input e' null");

	}

	/**
	 * Ordinamento di primo livello per gli stipendi ---_>in base al capitolo
	 *
	 * @param stipendiDaOrdinare la lista da ordinare
	 */
	public void sortStipendiPrimoLivello(List<Stipendio> stipendiDaOrdinare) {
		if (stipendiDaOrdinare != null && !stipendiDaOrdinare.isEmpty()) {
			log.debug("sortStipendiPrimoLivello", "Ordinamento primo livello in corso ...");
			Collections.sort(stipendiDaOrdinare, ComparatorStipendioPrimoLivello.INSTANCE);
			log.debug("","############################## ORDINAMENTO PRIMO LIVELLO###############################################################################");
			stampaListaStipendio(stipendiDaOrdinare);

			return;
		}
		log.debug("sortStipendiPrimoLivello", "La lista fornita in input e' null");

	}

	/**
	 * inserisceQuotaDocumentoEntrata inserisce una quota per il documento di
	 * entrata
	 *
	 * @param doc
	 * @param stipendio
	 * @param aggiornaStato
	 * @return il subdoc
	 */
	public SubdocumentoEntrata inserisceQuotaDocumentoEntrata(DocumentoEntrata doc, Stipendio stipendio,boolean aggiornaStato) {
		String methodName = "inserisceQuotaDocumentoEntrata";
		log.debug(methodName, "inserisco una nuova quota per doc Entrata");
		SubdocumentoEntrata se = new SubdocumentoEntrata();
		se.setEnte(stipendio.getEnte());
		se.setDocumento(doc);
		// SIAC-4956: il flag convalida manuale deve essere a NULL
		se.setFlagConvalidaManuale(null);
		se.setFlagOrdinativoManuale(Boolean.TRUE);
		se.setFlagEsproprio(Boolean.FALSE);
		se.setFlagAvviso(Boolean.FALSE);
		se.setFlagOrdinativoSingolo(Boolean.valueOf(stipendio.isTipoRecordRitenute()));
		se.setImporto(stipendio.getImportoEntrata());
		se.setDescrizione(doc.getDescrizione());
		//inserisce
		//Controllo lo stato dell'accertamento prima per evitare che si schianti il servizio di inserimento
		se.setAccertamento(checkStatoAccertmento(stipendio.getAccertamento()) ? stipendio.getAccertamento() :null);
		se.setSubAccertamento(checkStatoAccertmento(stipendio.getSubAccertamento()) ? stipendio.getSubAccertamento() :null);

		if(stipendio.isAccertamentoAutomatico()){
			se.setAttoAmministrativo(attoAmministrativo);
		}
		se.setDataScadenza(calcolaDataScadenzaPerTipoStipendio(stipendio));
		dscg.setBilancio(stipendio.getBilancio());
		dscg.setEnte(stipendio.getEnte());

		return dscg.inserisceQuotaDocumentoEntrata(se, aggiornaStato, false);
	}

	/**
	 * inserisce una quota per il documento di spesa
	 *
	 * @param doc
	 * @param stipendio
	 * @param aggiornaStato
	 * @return il subdoc
	 */
	public SubdocumentoSpesa inserisceQuotaDocumentoSpesa(DocumentoSpesa doc, Stipendio stipendio,boolean aggiornaStato) {
		String methodName = "inserisceQuotaDocumentoSpesa";
		log.debug(methodName, "inserisco una nuova quota per doc Spesa");
		SubdocumentoSpesa se = new SubdocumentoSpesa();
		se.setEnte(stipendio.getEnte());
		se.setDocumento(doc);
		// - flagOrdinativoManuale = ‘No';
		// - flagEsproprio = ‘No';
		// - flagAvviso = ‘No';
		// - movimento di spesa associato al record o selezionato in base al capitolo o alla UEB reperita;
		// - CausaleOrdinativo = ‘DOCUMENTO N.'+numero documento+'DEL'+data documento
		// - Importo quota = importo spesa del record trattato;
		// - ModalitaPagamentoSoggetto.numero ricavata in base all'algoritmo 2.4.1.8.1 Calcolo modalita' di pagamento
		// - Sede secondaria associata alla modalita' di pagamento indicata se presente: Sede in stato VALIDO.
		se.setFlagOrdinativoManuale(Boolean.FALSE);
		se.setFlagEsproprio(Boolean.FALSE);
		se.setFlagAvviso(Boolean.FALSE);
		se.setImporto(calcolaImportoSpesa(stipendio));
		se.setImpegno(stipendio.getImpegno());

		// SIAC-5311 (se ci sono i dati SIOPE+ li clono)
		if(se.getImpegno() != null) {
			se.setCig(se.getImpegno().getCig());
			se.setSiopeAssenzaMotivazione(se.getImpegno().getSiopeAssenzaMotivazione());
			se.setCup(se.getImpegno().getCup());
		}

		se.setCausaleOrdinativo(computeCausaleOrdinativoFromDocumento(doc));
		ModalitaPagamentoSoggetto mds = calcolaModalitaPagamentoSoggetto(stipendio);
		se.setDataScadenza(calcolaDataScadenzaPerTipoStipendio(stipendio));
		se.setModalitaPagamentoSoggetto(mds);
		se.setDescrizione(doc.getDescrizione());
		// Verificare come metterea posto la sede secondaria
		// se.setSedeSecondariaSoggetto(stipendio.getSoggetto().getSediSecondarie());

		dscg.setBilancio(stipendio.getBilancio());
		dscg.setEnte(stipendio.getEnte());
		return dscg.inserisceQuotaDocumentoSpesa(se, aggiornaStato);

	}

	/**
	 * in base al tipo di record calcolo la modalita di pagamento del soggetto
	 * @param stipendio
	 * @return la modalit&agrave; di pagamento
	 */
	private ModalitaPagamentoSoggetto calcolaModalitaPagamentoSoggetto(Stipendio stipendio) {
		String key = Boolean.toString(stipendio.isTipoRecordOneri() ||stipendio.isTipoRecordRitenute()) + "|" + Integer.toString(stipendio.getSoggetto().getUid());
		return cacheModalitaPagamentoSoggetto.get(key, modalitaPagamentoSoggettoCacheInitializer);
	}

	/**
	 * calcola la data di scadenza in base al Tipo di record stipendio
	 * Data Scadenza =  fine mese (mese del flusso) per Stipendio Lordo, il 15 del mese successivo per Oneri Ritenute
	 * @param stipendio
	 * @return la data di scadenza
	 */
	private Date calcolaDataScadenzaPerTipoStipendio(Stipendio stipendio) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		//CALENDAR CON I MESI PARTE DA 0... faccio -1
		calendar.set(Calendar.MONTH, stipendio.getMeseElaborazione() - 1);
		calendar.set(Calendar.YEAR, stipendio.getAnnoElaborazione());
		calendar.set(Calendar.DATE,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date dataFineMese = calendar.getTime();

		if(!stipendio.isTipoStipendioLordo()){
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DATE, StipendioParams.GIORNO_MESE_DATA_SCADENZA);
			return calendar.getTime();
		}

		return dataFineMese;
	}

	/**
	 * permette di ricercare un capitolo di spesa a partire dal numero
	 * capitolo,numero articolo ed anno bilancio
	 *
	 * @param capitoloUscitaGestione
	 * @return la response di ricerca
	 */
	private RicercaPuntualeCapitoloUscitaGestioneResponse ricercaPuntualeCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione) {
		RicercaPuntualeCapitoloUGest criteriRicerca = new RicercaPuntualeCapitoloUGest();
		criteriRicerca.setAnnoEsercizio(capitoloUscitaGestione.getBilancio().getAnno());
		criteriRicerca.setAnnoCapitolo(capitoloUscitaGestione.getAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(capitoloUscitaGestione.getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(capitoloUscitaGestione.getNumeroArticolo());
		criteriRicerca.setNumeroUEB(capitoloUscitaGestione.getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);

		RicercaPuntualeCapitoloUscitaGestione rpcug = new RicercaPuntualeCapitoloUscitaGestione();
		rpcug.setRichiedente(req.getRichiedente());
		rpcug.setEnte(req.getEnte());
		rpcug.setRicercaPuntualeCapitoloUGest(criteriRicerca);

		log.logXmlTypeObject(rpcug, "request del servizio ricercaPuntualeCapitoloUscitaGestione");
		RicercaPuntualeCapitoloUscitaGestioneResponse response = executeExternalServiceSuccess(ricercaPuntualeCapitoloUscitaGestioneService, rpcug);
		log.logXmlTypeObject(response, "response ricercaPuntualeCapitoloUscitaGestione");

		checkServiceResponseErrore(response);

		return response;

	}

	/**
	 * permette di ricercare un capitolo di entrata a partire dal numero
	 * capitolo,numero articolo ed anno bilancio
	 *
	 * @param capitoloEntrataGestione
	 * @return la resopnse di ricerca
	 */
	private RicercaPuntualeCapitoloEntrataGestioneResponse ricercaPuntualeCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione) {
		RicercaPuntualeCapitoloEGest criteriRicerca = new RicercaPuntualeCapitoloEGest();
		criteriRicerca.setAnnoEsercizio(capitoloEntrataGestione.getBilancio().getAnno());
		criteriRicerca.setAnnoCapitolo(capitoloEntrataGestione.getAnnoCapitolo());
		criteriRicerca.setNumeroCapitolo(capitoloEntrataGestione.getNumeroCapitolo());
		criteriRicerca.setNumeroArticolo(capitoloEntrataGestione.getNumeroArticolo());
		criteriRicerca.setNumeroUEB(capitoloEntrataGestione.getNumeroUEB());
		criteriRicerca.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);

		RicercaPuntualeCapitoloEntrataGestione rpcug = new RicercaPuntualeCapitoloEntrataGestione();
		rpcug.setRichiedente(req.getRichiedente());
		rpcug.setEnte(req.getEnte());
		rpcug.setRicercaPuntualeCapitoloEGest(criteriRicerca);

		RicercaPuntualeCapitoloEntrataGestioneResponse response = executeExternalServiceSuccess(ricercaPuntualeCapitoloEntrataGestioneService, rpcug);

		checkServiceResponseErrore(response);

		return response;

	}

	/**
	 * @param accertamento
	 * @param stipendio
	 * @return se l'accertamento sia valido
	 */
	public boolean isAccertamentoValido(Accertamento accertamento, Stipendio stipendio) {
		String methodName = "isAccertamentoValido";
		// controllo stato,disponibilita solo accertamento no Sub
		boolean isAccertamentoHasSoggetto = accertamento.getSoggetto() != null && accertamento.getSoggetto().getUid() != 0;
		log.info(methodName, "isAccertamentoHasSoggetto : "+(isAccertamentoHasSoggetto ? " true uid soggetto collegato all'accertamento :"+accertamento.getSoggetto().getUid() :" false" ));
		log.info(methodName, "checkStatoAccertmento : "+checkStatoAccertmento(accertamento));
		return checkStatoAccertmento(accertamento)
				&& isMovimentoGestioneConSoggettoCoerente(stipendio.getSoggetto(), accertamento)
				&& isAccertamentoDisponibile(stipendio, accertamento);
	}

	/**
	 *
	 * @param impegno
	 * @param stipendio
	 * @return se l'impegno sia valido
	 */
	public boolean isImpegnoValido(Impegno impegno, Stipendio stipendio) {
		String methodName = "isImpegnoValido";
		// controllo stato,disponibilita solo impegno no Sub
		boolean isImpegnoHasSoggetto = impegno.getSoggetto() != null && impegno.getSoggetto().getUid() != 0;
		boolean statoImpegnoCongruente = checkStatoImpegno(impegno);
		log.info(methodName, "isImpegnoHasSoggetto : "+(isImpegnoHasSoggetto ? " true uid soggetto collegato all'impegno :"+impegno.getSoggetto().getUid() :" false" ));
		log.info(methodName, "checkStatoImpegno : "+statoImpegnoCongruente);
		// TODO: controllare anche i sub?
		return statoImpegnoCongruente
				&& isMovimentoGestioneConSoggettoCoerente(stipendio.getSoggetto(), impegno)
				&& isImpegnoDisponibile(stipendio, impegno);
	}

	private boolean isMovimentoGestioneConSoggettoCoerente(Soggetto soggetto, MovimentoGestione movimentoGestione) {
		final String methodName = "isMovimentoGestioneConSoggettoCoerente";
		if(soggetto == null || movimentoGestione == null) {
			log.debug(methodName, "Dati non presenti per effettuare i controlli");
			return false;
		}
		if(movimentoGestione.getSoggetto() != null) {
			boolean result = movimentoGestione.getSoggetto().getUid() == soggetto.getUid();
			log.debug(methodName, "Movimento di gestione con soggetto impostato - controllo sugli id dei soggetti con risultato " + result);
			return result;
		}
		// JIRA -2725 il movimento di gestione deve avere una classe
		boolean result = movimentoGestione.getClasseSoggetto() != null && StringUtils.isNotBlank(movimentoGestione.getClasseSoggetto().getCodice());
		log.debug(methodName, "Controllo sulla classe soggetto del movimento di gestione. La classe soggetto e' presente e valorizzata? " + result);

		return result;

	}

	/**
	 * Aggiunge un messaggio alla response del servizio
	 * settare il flag a true se l'elaborazione e' stata parziale
	 * @param stipendio
	 */
	protected void sendMessaggioConNumeroStipendioInElaborazione(String messaggio, Stipendio stipendio,boolean flagInserimentoParziale){
		String methodName = "sendMessaggioConNumeroStipendioInElaborazione";

		StringBuilder msgsb = new StringBuilder();
		msgsb.append("\n(nome file: " + nomeFile + ") L'elaborazione dello Stipendio ");
		msgsb.append(numero);
		if (flagInserimentoParziale) {
			msgsb.append(" (documento creato numero ");
			msgsb.append(computeNumeroDocumentoFromStipendio(stipendio, null));
			msgsb.append(")");
		}
		if (stipendio.getAnnoElaborazione() != null && stipendio.getMeseElaborazione() != null) {
			msgsb.append(" con anno/mese elaborazione: ");
			msgsb.append(stipendio.getAnnoElaborazione());
			msgsb.append("/");
			msgsb.append(stipendio.getMeseElaborazione());
		}
		msgsb.append(flagInserimentoParziale ? " e' stata parziale." : " e' stata interrotta ");
		if (StringUtils.isNotBlank(messaggio)) {
			msgsb.append(" \n");
			msgsb.append(messaggio);
			msgsb.append("\n");
		}

		String msg = msgsb.toString();
		log.info(methodName, msg);
		Messaggio m = new Messaggio();
		m.setCodice(flagInserimentoParziale ? StipendioParams.CODICE_ELABORAZIONE_PARZIALE : StipendioParams.CODICE_ELABORAZIONE_INTERROTTA);
		m.setDescrizione(msg);
		res.addMessaggio(m);
	}

	//aggiunti il 16/09/2015 per migliorare il controllo sulla disponibilita dell'impegno e dell'accertamento
	private boolean isImpegnoDisponibile(Stipendio stipendio, Impegno impegno){
		String methodName = "isImpegnoDisponibile";
		boolean isImpegnoDisponibile = false;
		boolean isImpegnoPopolatoValido;
		isImpegnoPopolatoValido = impegno != null && impegno.getUid() > 0;

		if (!isImpegnoPopolatoValido) {
			return false;
		}
		// SIAC-5388: la disponibilita' e' gia' ottenuta dal servizio
		BigDecimal importoImpegnoSenzaQuota = impegno.getDisponibilitaLiquidare();
//		BigDecimal importoImpegnoSenzaQuota = impegnoBilDad.ottieniDisponibilitaLiquidare(impegno, null);

		log.debug(methodName, "Info disponibilita Impegno ......................................................");
		BigDecimal importoQuota = calcolaImportoSpesa(stipendio);
		String keyImpegno = computeKeyImpegnoSubImpegno(impegno, false);
		BigDecimal importoQuote = BigDecimal.ZERO;
		if (mappaMovimentiDisponibilita.containsKey(keyImpegno)) {
			importoQuote = mappaMovimentiDisponibilita.get(keyImpegno);
			log.debug(methodName, "Importo quote iniziale :" + importoQuote);
		}

		BigDecimal sum = importoQuote.add(importoQuota);
		isImpegnoDisponibile = importoImpegnoSenzaQuota.compareTo(sum) >= 0;

		if (isImpegnoDisponibile) {
			mappaMovimentiDisponibilita.put(keyImpegno, sum);
		}

		log.debug(methodName, "Disponibilita impegno iniziale :" + importoImpegnoSenzaQuota);
		log.debug(methodName, "Importo quota da aggiungere :" + importoQuota);
		log.debug(methodName, "Importo totale quote relative all'impegno con key " + keyImpegno + " e' di " + sum);

		log.info(methodName, isImpegnoDisponibile ? "Impegno ha disponibilita sufficiente " : "Impegno non ha disponibilita sufficiente");

		return isImpegnoDisponibile;

	}

	/**
	 * isSubImpegnoDisponibile
	 * @param stipendio
	 * @param subImpegno
	 * @return se il subimpegno sia disponibile
	 */
	private boolean isSubImpegnoDisponibile(Stipendio stipendio, SubImpegno subImpegno){
		String methodName = "isSubImpegnoDisponibile";
		boolean isSubImpegnoDisponibile = false;
		boolean isSubImpegnoPopolatoValido;
		isSubImpegnoPopolatoValido = subImpegno != null && subImpegno.getUid() > 0;

		if (!isSubImpegnoPopolatoValido) {
			return false;
		}
		// SIAC-5388: la disponibilita' e' gia' ottenuta dal servizio
		BigDecimal importoSubImpegnoSenzaQuota = subImpegno.getDisponibilitaLiquidare();
//		BigDecimal importoSubImpegnoSenzaQuota = impegnoBilDad.ottieniDisponibilitaLiquidare(null, subImpegno);

		log.debug(methodName, "Info disponibilita SubImpegno ............................................");
		BigDecimal importoQuota = calcolaImportoSpesa(stipendio);
		String keySubImpegno = computeKeyImpegnoSubImpegno(subImpegno, true);
		BigDecimal importoQuote = BigDecimal.ZERO;
		if (mappaMovimentiDisponibilita.containsKey(keySubImpegno)) {
			importoQuote = mappaMovimentiDisponibilita.get(keySubImpegno);
			log.debug(methodName, "Importo quote iniziale :" + importoQuote);
		}

		BigDecimal sum = importoQuote.add(importoQuota);
		isSubImpegnoDisponibile = importoSubImpegnoSenzaQuota.compareTo(sum) >= 0;

		if (isSubImpegnoDisponibile) {
			mappaMovimentiDisponibilita.put(keySubImpegno, sum);
		}

		log.debug(methodName, "Disponibilita SubImpegno iniziale :" + importoSubImpegnoSenzaQuota);
		log.debug(methodName, "Importo quota da aggiungere :" + importoQuota);
		log.debug(methodName, "Importo totale quote relative al keySubImpegno con key " + keySubImpegno + " e' di " + sum);

		log.info(methodName, isSubImpegnoDisponibile ? "SubImpegno ha disponibilita sufficiente " : "SubImpegno non ha disponibilita sufficiente");

		return isSubImpegnoDisponibile;

	}

	/**
	 * isAccertamentoDisponibile
	 * @param stipendio
	 * @param accertamento
	 * @return se l'accertamento sia disponibile
	 */
	private boolean isAccertamentoDisponibile(Stipendio stipendio, Accertamento accertamento){
		String methodName = "isAccertamentoDisponibile";
		boolean isAccertamentoDisponibile = false;
		boolean isAccertamentoPopolatoValido;
		isAccertamentoPopolatoValido = accertamento != null && accertamento.getUid() > 0;

		if(!isAccertamentoPopolatoValido){
			return false;
		}
		// SIAC-5388: la disponibilita' e' gia' ottenuta dal servizio
		BigDecimal importoAccertamentoSenzaQuota = accertamento.getDisponibilitaIncassare();
//		BigDecimal importoAccertamentoSenzaQuota = accertamentoBilDad.ottieniDisponibilitaLiquidare(accertamento,null);
		log.debug(methodName, "Info disponibilita Accertamento ..................................................");
		BigDecimal importoQuota = !(stipendio.getImportoEntrata().equals(BigDecimal.ZERO)) ? stipendio.getImportoEntrata() : BigDecimal.ZERO;
		String keyAccertamento = computekeyAccertamentoSubAccertamento(accertamento, false);
		BigDecimal importoQuote = BigDecimal.ZERO;
		if (mappaMovimentiDisponibilita.containsKey(keyAccertamento)) {
			importoQuote = mappaMovimentiDisponibilita.get(keyAccertamento);
			log.debug(methodName, "Importo quote iniziale :" + importoQuote);
		}

		BigDecimal sum = importoQuote.add(importoQuota);
		isAccertamentoDisponibile = importoAccertamentoSenzaQuota.compareTo(sum) >= 0;

		if (isAccertamentoDisponibile) {
			mappaMovimentiDisponibilita.put(keyAccertamento, sum);
		}

		log.debug(methodName, "Disponibilita Accertamento iniziale :" + importoAccertamentoSenzaQuota);
		log.debug(methodName, "Importo quota da aggiungere :" + importoQuota);
		log.debug(methodName, "Importo totale quote relative al keyAccertamento con key " + keyAccertamento + " e' di " + sum);

		log.info(methodName, isAccertamentoDisponibile ? "Accertamento ha disponibilita sufficiente " : "Accertamento non ha disponibilita sufficiente");

		return isAccertamentoDisponibile;
	}

	/**
	 * isSubAccertamentoDisponibile
	 * @param stipendio
	 * @param subAccertamento
	 * @return se il subaccertamento sia disponibile
	 */
	private boolean isSubAccertamentoDisponibile(Stipendio stipendio, SubAccertamento subAccertamento){
		String methodName = "isSubAccertamentoDisponibile";
		boolean isSubAccertamentoDisponibile = false;
		boolean isSubAccertamentoPopolatoValido;
		isSubAccertamentoPopolatoValido = subAccertamento != null && subAccertamento.getUid() > 0;

		if(!isSubAccertamentoPopolatoValido){
			return false;
		}
		// SIAC-5388: la disponibilita' e' gia' ottenuta dal servizio
		BigDecimal importoSubAccertamentoSenzaQuota = subAccertamento.getDisponibilitaIncassare();
//		BigDecimal importoSubAccertamentoSenzaQuota = accertamentoBilDad.ottieniDisponibilitaLiquidare(null,subAccertamento);

		log.debug(methodName, "Info disponibilita SubAccertamento ..................................................");
		BigDecimal importoQuota = !(stipendio.getImportoEntrata().equals(BigDecimal.ZERO)) ? stipendio.getImportoEntrata() : BigDecimal.ZERO;
		String keySubAccertamento = computekeyAccertamentoSubAccertamento(subAccertamento, true);
		BigDecimal importoQuote = BigDecimal.ZERO;
		if (mappaMovimentiDisponibilita.containsKey(keySubAccertamento)) {
			importoQuote = mappaMovimentiDisponibilita.get(keySubAccertamento);
			log.debug(methodName, "Importo quote iniziale :" + importoQuote);
		}

		BigDecimal sum = importoQuote.add(importoQuota);
		isSubAccertamentoDisponibile = importoSubAccertamentoSenzaQuota.compareTo(sum) >= 0;

		if (isSubAccertamentoDisponibile) {
			mappaMovimentiDisponibilita.put(keySubAccertamento, sum);
		}

		log.debug(methodName, "Disponibilita SubAccertamento iniziale :" + importoSubAccertamentoSenzaQuota);
		log.debug(methodName, "Importo quota da aggiungere :" + importoQuota);
		log.debug(methodName, "Importo totale quote relative al keyAccertamento con key " + keySubAccertamento + " e' di " + sum);

		log.info(methodName, isSubAccertamentoDisponibile ? "Accertamento ha disponibilita sufficiente " : "Accertamento non ha disponibilita sufficiente");

		return isSubAccertamentoDisponibile;

	}

	/**
	 * computeKeyImpegnoSubImpegno
	 * @param impegno
	 * @param isSubImpegno
	 * @return la chiave dell'impegno
	 */
	private String computeKeyImpegnoSubImpegno(Impegno impegno,boolean isSubImpegno) {
		StringBuilder sb = new StringBuilder();
		sb.append(impegno.getAnnoMovimento());
		sb.append("_");
		sb.append(impegno.getNumero());
		sb.append("_");
		sb.append(impegno.getUid());
		sb.append("_");
		sb.append(isSubImpegno ? "SubImpegno" : "Impegno");

		return sb.toString().trim();
	}

	/**
	 * computekeyAccertamentoSubAccertamento
	 * @param accertamento
	 * @param isSubAccertamento
	 * @return la chiave dell'accertamento
	 */
	private String computekeyAccertamentoSubAccertamento(Accertamento accertamento,boolean isSubAccertamento) {
		StringBuilder sb = new StringBuilder();
		sb.append(accertamento.getAnnoMovimento());
		sb.append("_");
		sb.append(accertamento.getNumero());
		sb.append("_");
		sb.append(accertamento.getUid());
		sb.append("_");
		sb.append(isSubAccertamento ? "SubAccertamento" : "Accertamento");

		return sb.toString().trim();

	}

	/**
	 * aggiunge un messaggie con il riepilogo alla response del servizio
	 */
	private void aggiungiMessaggioDiRiepilogo() {
		log.debug("RIEPILOGO_STIPENDI", counter.toString());
		res.addMessaggio("RIEP", counter.toString());
	}

	//aggiunti il 02/07/2015
	protected abstract boolean isTipoElaborazioneONERI();

	protected abstract boolean isTipoElaborazioneSTIPE();

	protected abstract boolean isTipoElaborazioneTUTTO();

	protected abstract String getCodiceDocumento();

	/**
	 * Contatore per  l'elaborazione degli stipendi.
	 *
	 * @author Marchino Alessandro,Nazha Ahmad
	 * @version 1.0.0 - 22/10/2015
	 *
	 */
	private static class Counter {

		private final String nomeFile;

		private int righe;
		private int righeScartate;

		private int documentiSpesaInseriti;
		private int documentiSpesaAggiornati;
		private int subdocumentiSpesa;
		private int documentiEntrataInseriti;
		private int documentiEntrataAggiornati;
		private int subdocumentiEntrata;
		private int accertamentiAutomatici;

		private int numeroOneri;
		private int numeroRitenute;
		private int numeroStipendiLordi;
		private int numeroRecuperi;
		private int numeroElencoEntrate;
		private int numeroElencoSpese;
		private int numeroElencoOneriRitenute;
		private int numeroAccertamentiConModificaImporti;

		Counter(String nomeFile) {
			this.nomeFile = nomeFile;
		}

		int addRiga() {
			return ++righe;
		}

		int addRigaScartata() {
			return ++righeScartate;
		}

		int addDocumentoSpesaInserito() {
			return ++documentiSpesaInseriti;
		}

		int addDocumentoSpesaAggiornato() {
			return ++documentiSpesaAggiornati;
		}

		int addSubdocumentoSpesa() {
			return ++subdocumentiSpesa;
		}

		int addDocumentoEntrataInserito() {
			return ++documentiEntrataInseriti;
		}

		int addDocumentoEntrataAggiornato() {
			return ++documentiEntrataAggiornati;
		}

		int addSubdocumentoEntrata() {
			return ++subdocumentiEntrata;
		}

		int addAccertamentoAutomatico() {
			return ++accertamentiAutomatici;
		}

		int addNumeroOneri (){
			return ++numeroOneri;
		}

		int addNumeroRitenute (){
			return ++numeroRitenute;
		}

		int addNumeroRecuperi (){
			return ++numeroRecuperi;
		}

		int addNumeroStipendiLordi (){
			return ++numeroStipendiLordi;
		}
		int addnumeroAccertamentiConModificaImporti(){
			return ++numeroAccertamentiConModificaImporti;
		}

		void setNumeroElencoSpese(Integer numero){
			numeroElencoSpese = numero;
		}

		void setNumeroElencoEntrate(Integer numero){
			numeroElencoEntrate = numero;
		}

		void setNumeroElencoOneriRitenute(Integer numero){
			numeroElencoOneriRitenute = numero;
		}


		String getNumeriElenchi(){
			StringBuilder sb = new StringBuilder();
			sb.append(numeroElencoEntrate > 0 ? "Numero elenco di tipologia Entrate inserito "+numeroElencoEntrate : "Nessun Elenco di tipologia Entrate inserito");
			sb.append(", ");
			sb.append(numeroElencoSpese > 0 ? "Numero elenco di tipologia Spese inserito "+numeroElencoSpese : "Nessun Elenco di tipologia Spese inserito");
			sb.append(", ");
			sb.append(numeroElencoOneriRitenute > 0 ? "Numero elenco di tipologia Oneri/Ritenute inserito "+numeroElencoOneriRitenute : "Nessun Elenco di tipologia Oneri/Ritenute inserito");
			return sb.toString();
		}


		@Override
		public String toString() {
			final String template = "Elaborazione (nome file: {13}): {0,choice,0#nessuna riga|1#una riga|1<{0, number} righe}"
					+ " ({1,choice,0#nessuna ritenuta|1#una ritenuta|1<{1, number} ritenute}, "
					+ " {2,choice,0#nessun stipendio lordo|1#uno stipendio lordo|1<{2, number} stipendi lordi}, "
					+ " {3,choice,0#nessun onere|1#un onere|1<{3, number} oneri}, "
					+ " {4,choice,0#nessun recupero|1#un recupero|1<{4, number} recuperi} ) , "
					+ " di cui {5,choice,0#nessuna scartata|1#una scartata|1<{5, number} scartate}"
					+ " - Inseriti: {6,choice,0#nessun documento di spesa|1#un documento di spesa|1<{6, number} documenti di spesa}, "
					+ "{7,choice,0#nessun documento di entrata|1#un documento di entrata|1<{7, number} documenti di entrata}"
					+ " - Aggiornati: {8,choice,0#nessun documento di spesa|1#un documento di spesa|1<{8, number} documenti di spesa}, "
					+ "{9,choice,0#nessun documento di entrata|1#un documento di entrata|1<{9, number} documenti di entrata}"
					+ " - {10,choice,0#nessuna quota|1#una quota|1<{10, number} quote} di spesa, "
					+ "{11,choice,0#nessuna quota|1#una quota|1<{11, number} quote} di entrata"
					+ " - {12,choice,0#nessun accertamento automatico|1#un accertamento automatico|1<{12, number} accertamenti automatici}"
					+ " - {13,choice,0#nessun accertamento con importo modificato|1#un accertamento con importo modificato|1<{12, number} accertamenti con importi modificati}"
					+ " - "+getNumeriElenchi();

			return MessageFormat.format(template, righe, numeroRitenute, numeroStipendiLordi, numeroOneri , numeroRecuperi, righeScartate, documentiSpesaInseriti, documentiEntrataInseriti, documentiSpesaAggiornati, documentiEntrataAggiornati,
					subdocumentiSpesa, subdocumentiEntrata, accertamentiAutomatici, numeroAccertamentiConModificaImporti, nomeFile);
		}

	}
	
}
