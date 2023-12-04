/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache.CigCacheInitializer;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache.CupCacheInitializer;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache.NoteCreditoCacheInitializer;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache.PenaliCacheInitializer;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache.RitenuteCacheInitializer;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache.SamCacheInitializer;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.cache.SoggettoCacheInitializer;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model.StampaAllegatoAttoElenco;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model.StampaAllegatoAttoImpegno;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model.StampaAllegatoAttoReportModel;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model.StampaAllegatoAttoSubdocumento;
import it.csi.siac.siacbilser.business.service.stampa.base.JAXBBaseReportHandler;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.collections.filter.NotNullFilter;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AggiornaOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.AggiornaOperazioneAsincResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciDettaglioOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciDettaglioOperazioneAsincResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StatoOperazioneAsincronaEnum;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.Checklist;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Report handler per la stampa dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 11/11/2015 - CR-2484 - modifica gestione SAC e reperimento importi
 * @author Elisa Chiari
 * @version 1.2.0 - 13/01/2016 - CR-2705 - dati per ricerca stampa
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaAllegatoAttoReportHandler extends JAXBBaseReportHandler<StampaAllegatoAttoReportModel> {
	
	
	private AllegatoAtto allegatoAtto;
	private Bilancio bilancio;
	private Integer annoEsercizio;
	private TipoStampaAllegatoAtto tipoStampa;
	
	// SIAC-8804
	private Checklist allegatoAttoChecklist;

	
	//DADs
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private ClassificatoriDad classificatoriDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private SubImpegnoBilDad subImpegnoBilDad;
	@Autowired
	protected OperazioneAsincronaService operazioneAsincronaService;
	@Autowired
	private CapitoloDad capitoloDad;
	
	//Services
	@Autowired
	private InserisceStampaAllegatoAttoService inserisceStampaAllegatoAttoService;
	
	// Cache
	private final Cache<Integer, String> noteCreditoCache = new MapCache<Integer, String>();
	private final Cache<Integer, String> penaliCache      = new MapCache<Integer, String>();
	private final Cache<Integer, String> ritenuteCache    = new MapCache<Integer, String>();
	private final Cache<Integer, Soggetto> soggettoCache  = new MapCache<Integer, Soggetto>();
	private final Cache<String, Set<String>> cigCache     = new MapCache<String, Set<String>>();
	private final Cache<String, Set<String>> cupCache     = new MapCache<String, Set<String>>();
	private final Cache<String, Set<String>> samCache     = new MapCache<String, Set<String>>(); // SIAC-6269
	
	private CacheElementInitializer<Integer, String> noteCreditoCacheInitializer;
	private CacheElementInitializer<Integer, String> penaliCacheInitializer;
	private CacheElementInitializer<Integer, String> ritenuteCacheInitializer;
	private CacheElementInitializer<Integer, Soggetto> soggettoCacheInitializer;
	private CacheElementInitializer<String, Set<String>> cigCacheInitializer;
	private CacheElementInitializer<String, Set<String>> cupCacheInitializer;
	private CacheElementInitializer<String, Set<String>> siopeAssenzaMotivazioneCacheInitializer; // SIAC-6269
	
	List<Errore> errori = new ArrayList<Errore>();
	Integer idOperazioneAsincrona ;
	
	
	
	/**
	 * @return the idOperazioneAsincrona
	 */
	public Integer getIdOperazioneAsincrona() {
		return idOperazioneAsincrona;
	}

	/**
	 * @param idOperazioneAsincrona the idOperazioneAsincrona to set
	 */
	public void setIdOperazioneAsincrona(Integer idOperazioneAsincrona) {
		this.idOperazioneAsincrona = idOperazioneAsincrona;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,timeout=AsyncBaseService.TIMEOUT)
	public void elaborate() {
		super.elaborate();
	}
	
	private void init() {
		elencoDocumentiAllegatoDad.setEnte(ente);
		soggettoDad.setEnte(ente);
		subdocumentoSpesaDad.setEnte(ente);
		
		noteCreditoCacheInitializer = new NoteCreditoCacheInitializer(documentoSpesaDad);
		penaliCacheInitializer = new PenaliCacheInitializer(documentoSpesaDad);
		ritenuteCacheInitializer = new RitenuteCacheInitializer(documentoSpesaDad);
		soggettoCacheInitializer = new SoggettoCacheInitializer(soggettoDad);
		cigCacheInitializer = new CigCacheInitializer();
		cupCacheInitializer = new CupCacheInitializer();
		siopeAssenzaMotivazioneCacheInitializer = new SamCacheInitializer(); // SIAC-6269
	}
	
	@Override
	public String getCodiceTemplate() {
		return "AllegatoAtto";
	}
	
	@Override
	protected void elaborateData() {
		result.setAllegatoAtto(getAllegatoAtto());
		result.setAllegatoAttoChecklist(getAllegatoAttoChecklist());
		
		// Inizializzazione dei dati
		init();
		
		elaboraStruttureAmministrativoContabili();
		elaboraSoggettoProponente();
		// SIAC-4800
		elaboraDataCompletamento();
		// SIAC-5446
		elaboraDataScadenza();
		elaboraDataInserimento();
		elaboraOneri();

		// Per ogni elenco (analogo a raggruppare per elenco)
		// SIAC-4201: raggruppamento per Soggetto
		// Per ogni soggetto
		int soggettoIndex = 1;
		List<Soggetto> soggetti = allegatoAttoDad.findSoggettiByAllegatoAtto(allegatoAtto);
		for(Soggetto soggetto : soggetti) {
			elaboraSoggetto(soggetto, soggettoIndex++);
		}
	}

	/**
	 * <ul>
	 *     <li><strong>direzione</strong>: CDR Struttura Amministrativa Contabile dell'utente che ha inserito l'atto CDR</li>
	 *     <li><strong>settore</strong>: CDC come sopra</li>
	 * </ul>
	 */
	private void elaboraStruttureAmministrativoContabili() {
		// CR-2484
		final String methodName = "elaboraStruttureAmministrativoContabili";
		if(allegatoAtto.getAttoAmministrativo() == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Allegato atto senza provvedimento"));
		}
		if(allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile() == null || allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile().getUid() == 0) {
			log.info(methodName, "Provvedimento [" + allegatoAtto.getAttoAmministrativo().getUid() + "] senza struttura amministrativo contabile collegata");
			return;
		}
		
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = classificatoriDad.ricercaStrutturaAmministrativoContabile(allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile().getUid());
		if(strutturaAmministrativoContabile == null || strutturaAmministrativoContabile.getTipoClassificatore() == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore di caricamento dati per la struttura amministrativo contabile del provvedimento"));
		}
		log.debug(methodName, "Struttura amministrativo contabile collegata al provvedimento: " + strutturaAmministrativoContabile.getUid());
		// Ne controllo il tipo: se CDR => non ho CDC; se CDC => devo ricercare il padre CDR
		if(TipologiaClassificatore.CDR.name().equals(strutturaAmministrativoContabile.getTipoClassificatore().getCodice())) {
			log.debug(methodName, "SAC di tipo CDR");
			// CDR => non ho il CDC
			result.setStrutturaAmministrativoContabileCDR(strutturaAmministrativoContabile);
			return;
		}
		if(TipologiaClassificatore.CDC.name().equals(strutturaAmministrativoContabile.getTipoClassificatore().getCodice())) {
			log.debug(methodName, "SAC di tipo CDC: ricerco il padre CDR");
			// CDC => devo ancora ricavare il CDR
			result.setStrutturaAmministrativoContabileCDC(strutturaAmministrativoContabile);
			
			// Ricerco il CDR
			// SIAC-5163: correzione SAC con data scadenza
			StrutturaAmministrativoContabile cdr = classificatoriDad.ricercaPadreStrutturaAmministrativoContabile(strutturaAmministrativoContabile, annoEsercizio);
			log.debug(methodName, "CDR per SAC con uid " + strutturaAmministrativoContabile.getUid() + ": " + (cdr != null ? cdr.getUid() : "null"));
			result.setStrutturaAmministrativoContabileCDR(cdr);
			return;
		}
		// Non ho ne' un CDR ne' un CDC: lancio un'eccezione
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Struttura amministrativo contabile collegata al provvedimento non di tipo CDC ne' di tipo CDR"));
	}
	
	/**
	 * Soggetto collegato all’ente proponente
	 */
	private void elaboraSoggettoProponente() {
		final String methodName = "elaboraSoggettoProponente";
		List<Soggetto> soggetti = soggettoDad.findSoggettoByEnte(ente);
		if(soggetti == null || soggetti.isEmpty()) {
			log.warn(methodName, "Nessun soggetto collegato all'ente " + ente.getUid());
			return;
		}
		log.debug(methodName, "Trovati " + soggetti.size() + " soggetti collegati all'ente " + ente.getUid() + ". Prendo il primo");
		result.setSoggettoProponente(soggetti.get(0));
	}
	
	/**
	 * Data dello stato &ldquo;completato&rdquo; pi&ugrave; recente
	 */
	private void elaboraDataCompletamento() {
		Date dataCompletamento = allegatoAttoDad.findDataUltimoInizioValiditaStato(allegatoAtto, StatoOperativoAllegatoAtto.COMPLETATO);
		result.setDataCompletamento(dataCompletamento);
	}
	
	/**
	 * Se la data scadenza non &egrave; stata inserita dall'utente, la si popola con la data scadenza della fattura pi&ugrave;
	 * prossima alla scadenza o, se gi&agrave; scaduta, quella pi&ugrave; vecchia.
	 * <br/>
	 * Se invece l'atto non contiene fatture la data pu&ograve; non essere valorizzata.
	 * <br/>
	 * Se vuota non presentare neanche l'etichetta
	 */
	private void elaboraDataScadenza() {
		final String methodName = "elaboraDataScadenza";
		if(allegatoAtto.getDataScadenza() != null) {
			log.debug(methodName, "Data inserita dall'utente [" + allegatoAtto.getDataScadenza() + "]");
			result.setDataScadenza(allegatoAtto.getDataScadenza());
			return;
		}
		// Recupero tutte le date
		// SIAC-5446: utilizzo i subdoc
		List<SubdocumentoSpesa> quoteFatture = subdocumentoSpesaDad.findSubdocWithDateScadenzaFattureByAllegatoAtto(allegatoAtto);
		
		Date now = new Date();
		Date dataScadenzaPiuProssimaAScadenza = null;
		Date dataScadenzaPiuVecchia = null;
		
		for(SubdocumentoSpesa ss : quoteFatture) {
			Date dsf = ss.getDataScadenzaDopoSospensione() != null ? ss.getDataScadenzaDopoSospensione() : ss.getDataScadenza();
			if(now.before(dsf) && (dataScadenzaPiuProssimaAScadenza == null || dataScadenzaPiuProssimaAScadenza.after(dsf))) {
				// Se la data e' successiva ad ora ed e' prima della data a scadenza piu' prossima gia' registrata...
				log.debug(methodName, "Impostazione della data piu' prossima a scadenza per allegato atto [" + allegatoAtto.getUid() + "] [" + dsf + "]");
				dataScadenzaPiuProssimaAScadenza = dsf;
			} else if(dataScadenzaPiuVecchia == null || dsf.before(dataScadenzaPiuVecchia)) {
				// Se la data di scadenza e' dopo la piu' vecchia gia' registrata
				log.debug(methodName, "Impostazione della data di scadenza piu' vecchia per allegato atto [" + allegatoAtto.getUid() + "] [" + dsf + "]");
				dataScadenzaPiuVecchia = dsf;
			}
		}
		
		Date dataScadenza = dataScadenzaPiuProssimaAScadenza != null ? dataScadenzaPiuProssimaAScadenza : dataScadenzaPiuVecchia;
		log.debug(methodName, "Allegato atto [" + allegatoAtto.getUid() + "], data scadenza piu' prossima a scadenza [" + dataScadenzaPiuProssimaAScadenza
				+ "], data scadenza piu' vecchia [" + dataScadenzaPiuVecchia + "] => data scadenza [" + dataScadenza + "]");
		result.setDataScadenza(dataScadenza);
	}

	/**
	 * La data di inserimento &eacute; la data di creazione dell'allegato atto
	 */
	private void elaboraDataInserimento() {
		result.setDataInserimento(allegatoAtto.getDataCreazione());
	}
	
	/**
	 * Elaborazione del soggetto
	 * @param soggetto il soggetto da elaborare
	 * @param index    l'indice
	 */
	private void elaboraSoggetto(Soggetto s, int index) {
		final String methodName = "elaboraSoggetto";
		if(s == null || s.getUid() == 0) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nel caricamento del soggetto " + index + " per l'allegato atto: soggetto non caricato correttamente"));
		}
		Soggetto soggetto = soggettoDad.findSoggettoByIdWithIndirizzi(s.getUid());
		if(soggetto == null || soggetto.getUid() == 0) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nel caricamento del soggetto " + index + " (uid: " + s.getUid() + ") per l'allegato atto: soggetto non caricato correttamente"));
		}
		log.debug(methodName, "Elaborazione soggetto " + index + " (uid: " + soggetto.getUid() + ")");
		
		// Caricamento lista quote per soggetto e allegato atto
		List<SubdocumentoSpesa> subdocs = subdocumentoSpesaDad.findSubdocumentoSpesaByAllegatoAttoIdAndSoggettoId(allegatoAtto, soggetto);
		log.debug(methodName, "Soggetto " + index + " (uid " + soggetto.getUid() + ") numero subdocumenti spesa: " + subdocs.size());
		
		// Creo la mappa per raggruppare i dati
		Map<String, StampaAllegatoAttoElenco> mapStampaAllegatoAttoElenco = new HashMap<String, StampaAllegatoAttoElenco>();
		Map<String, StampaAllegatoAttoImpegno> mapStampaAllegatoAttoImpegno = new HashMap<String, StampaAllegatoAttoImpegno>();
		
		// Per ogni quota (di spesa, come da analisi)
		int indexSubdocumento = 1;
		for(SubdocumentoSpesa subdocumentoSpesa : subdocs) {
			elaboraSubdocumentoSpesa(soggetto, index, subdocumentoSpesa, indexSubdocumento++, mapStampaAllegatoAttoElenco, mapStampaAllegatoAttoImpegno);
		}
		
		// Aggiungo i valori nella response
		for(StampaAllegatoAttoElenco saae : mapStampaAllegatoAttoElenco.values()) {
			// SIAC-5272
			for(StampaAllegatoAttoImpegno saai : saae.getListaStampaAllegatoAttoImpegno()) {
				sovrascriviCigCupConDatiQuoteDaNonEsporre(saai);
			}
			
			// SIAC-8835
			handleListaCapitoliSottoConto(saae);
			
			// Ordino la lista dei subdocumenti
			Collections.sort(saae.getListaStampaAllegatoAttoSubdocumento(), ComparatorStampaAllegatoAttoSubdocumento.INSTANCE);
			
			result.getListaStampaAllegatoAttoElenco().add(saae);
		}
	}

	private void handleListaCapitoliSottoConto(StampaAllegatoAttoElenco saae) {
		if (saae == null) {
			return;
		}
		
		saae.setListaCapitoliSottoConto(new ArrayList<CapitoloUscitaGestione>(
			CollectionUtil.distinct(	
				CollectionUtil.filter(
					CollectionUtil.map(saae.getListaStampaAllegatoAttoImpegno(), new Function<StampaAllegatoAttoImpegno, CapitoloUscitaGestione>() {
						@Override
						public CapitoloUscitaGestione map(StampaAllegatoAttoImpegno source) {
							return source == null || source.getImpegno() == null ? null : source.getImpegno().getCapitoloUscitaGestione();
					}}),
					new NotNullFilter<CapitoloUscitaGestione>()
				),
				new Function<CapitoloUscitaGestione, String>() {
					@Override
					public String map(CapitoloUscitaGestione source) {
						return String.valueOf(source.getUid());
					}
				}
			)
		));
		
		saae.setHasCapitoliSottoConto(CollectionUtil.isNotEmpty(saae.getListaCapitoliSottoConto()));
	}

	/**
	 * Se nella sezione IMPEGNO sono presenti quote di tipo ALG, DSP o CCN (quindi non da esporre in stampa) i CIG e CUP presenti nelle quote escluse dalla stampa
	 * devono essere riportati in questo tag al posto dell'informazione presente sull'Impegno.
	 * Se nessuna quota ha CIG e/o CUP prevale il dato sull'impegno 
	 * @param saai la stampa dell'impegno
	 */
	private void sovrascriviCigCupConDatiQuoteDaNonEsporre(StampaAllegatoAttoImpegno saai) {
		if(!saai.isHasQuoteDaNonEsporre()) {
			return;
		}
		String cacheKey = createCacheKeyImpegnoSubimpegno(saai.getImpegno(), saai.getSubImpegno());
		Set<String> cigs = cigCache.get(cacheKey);
		Set<String> cups = cupCache.get(cacheKey);
		// SIAC-6269
		Set<String> sams = samCache.get(cacheKey);
		boolean esistonoQuoteConCigValorizzato = cigs != null && !cigs.isEmpty();
		boolean esistonoQuoteConMotivoAssenzaCigValorizzato = sams != null && !sams.isEmpty();
		
		if(esistonoQuoteConCigValorizzato) {
			saai.setCig(StringUtils.join(cigs, ", "));
			if(!esistonoQuoteConMotivoAssenzaCigValorizzato) {
				saai.setSiopeAssenzaMotivazioneDescr("");
			}
		}
		// SIAC-6269
		if(esistonoQuoteConMotivoAssenzaCigValorizzato) {
			saai.setSiopeAssenzaMotivazioneDescr(StringUtils.join(sams, ", "));
			if(!esistonoQuoteConCigValorizzato) {
				saai.setCig("");
			}
		}
		if(cups != null && !cups.isEmpty()) {
			saai.setCup(StringUtils.join(cups, ", "));
		}
		
	}
	
	private void elaboraSubdocumentoSpesa(Soggetto soggetto, int indexSoggetto, SubdocumentoSpesa ss, int indexSubdocumento,
			Map<String, StampaAllegatoAttoElenco> mapStampaAllegatoAttoElenco, Map<String, StampaAllegatoAttoImpegno> mapStampaAllegatoAttoImpegno) {
		
		final String methodName = "elaboraSubdocumentoSpesa";
		if(ss == null || ss.getUid() == 0) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nel caricamento del subdocumento " + indexSubdocumento + " per il soggetto " + indexSoggetto
					+ " dell'allegato atto: subdocumento non caricato correttamente"));
		}
		log.debug(methodName, "Elaborazione subdocumento " + indexSubdocumento + " (uid: " + ss.getUid() + ") per soggetto " + indexSoggetto + " (uid: " + soggetto.getUid() + ")");
		
		// Carico il dettaglio del subdocumento
		SubdocumentoSpesa subdocumentoSpesa = subdocumentoSpesaDad.findSubdocumentoSpesaById(ss.getUid());
		
		if(subdocumentoSpesa == null || subdocumentoSpesa.getUid() == 0) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nel caricamento del subdocumento " + indexSubdocumento + " per l'elenco " + indexSoggetto
					+ " dell'allegato atto: subdocumento non caricato correttamente"));
		}
		if(subdocumentoSpesa.getDocumento() == null || subdocumentoSpesa.getDocumento().getUid() == 0) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nel caricamento del documento per il subdocumento " + indexSubdocumento + " per l'elenco " + indexSoggetto
					+ " dell'allegato atto: subdocumento non caricato correttamente"));
		}
		if(subdocumentoSpesa.getDocumento().getSoggetto() == null || subdocumentoSpesa.getDocumento().getSoggetto().getUid() == 0) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nel caricamento del soggetto per il documento per il subdocumento " + indexSubdocumento
					+ " per l'elenco " + indexSoggetto + " dell'allegato atto: subdocumento non caricato correttamente"));
		}
		
		// Applico l'importo split/reverse
		BigDecimal importoIvaSplitReverse = subdocumentoDad.getImportoSplitReverseSubdocumento(subdocumentoSpesa);
		log.debug(methodName, "Elaborazione subdocumento, sto per settare l'importo " + (importoIvaSplitReverse!=null? importoIvaSplitReverse : "null" ) + " per il subdocumento con uid " + subdocumentoSpesa.getUid());
		
		subdocumentoSpesa.setImportoSplitReverse(importoIvaSplitReverse);
		
		// Carico il dettaglio del soggetto
		// SIAC-4632
		Soggetto soggettoModpag = soggettoCache.get(subdocumentoSpesa.getModalitaPagamentoSoggetto().getUid(), soggettoCacheInitializer);
		
		String keyElenco = buildKeyElenco(soggetto, subdocumentoSpesa.getModalitaPagamentoSoggetto());
		log.debug(methodName, "Chiave per iterazione " + indexSoggetto + "_" + indexSubdocumento + ": " + keyElenco);
		
		StampaAllegatoAttoElenco stampaAllegatoAttoElenco = mapStampaAllegatoAttoElenco.get(keyElenco);
		if(stampaAllegatoAttoElenco == null) {
			// Stampa non ancora popolata: la inizializzo
			log.debug(methodName, "Elemento non creato per chiave " + keyElenco + ": inizializzazione in corso");
			stampaAllegatoAttoElenco = new StampaAllegatoAttoElenco();
			stampaAllegatoAttoElenco.setSoggetto(soggetto);
			// SIAC-4632
			impostaIndirizzo(stampaAllegatoAttoElenco, soggettoModpag, soggetto);
			
			stampaAllegatoAttoElenco.setModalitaPagamentoSoggetto(subdocumentoSpesa.getModalitaPagamentoSoggetto());
			stampaAllegatoAttoElenco.setSoggettoCessione(ottieniSoggettoCessione(subdocumentoSpesa.getModalitaPagamentoSoggetto()));
			mapStampaAllegatoAttoElenco.put(keyElenco, stampaAllegatoAttoElenco);
		}
		
		//SIAC-6190
		caricaSedeSecondariaSoggetto(subdocumentoSpesa.getSedeSecondariaSoggetto(), soggetto, soggettoModpag, stampaAllegatoAttoElenco);
		
		// Aggiungo alla lista se e solo se il tipo di documento e' corretto
		StampaAllegatoAttoSubdocumento saas = inizializzaStampaAllegatoAttoSubdocumento(subdocumentoSpesa);
		if(isTipoDocumentoCorretto(subdocumentoSpesa)) {
			stampaAllegatoAttoElenco.getListaStampaAllegatoAttoSubdocumento().add(saas);
		}
		
		
		Impegno impegno = subdocumentoSpesa.getImpegno();
		SubImpegno subImpegno = subdocumentoSpesa.getSubImpegno();
		
		log.debug(methodName, "Impegno: " + (impegno != null ? impegno.getUid() : "null") + " - Subimpegno: " + (subImpegno != null ? subImpegno.getUid() : "null"));
		String keyImpegno = buildKeyImpegno(soggetto, subdocumentoSpesa.getModalitaPagamentoSoggetto(), impegno, subImpegno);
		
		
		
		//Tra i dati da estrarre per la stampa dell'allegato atto devono esserci anche, a livello di IMPEGNO (o SUBIMPEGNO) CIG e CUP
		StampaAllegatoAttoImpegno stampaAllegatoAttoImpegno = mapStampaAllegatoAttoImpegno.get(keyImpegno);
		if(stampaAllegatoAttoImpegno == null) {
			// Impegno non ancora popolato: inizializzazione
			stampaAllegatoAttoImpegno = new StampaAllegatoAttoImpegno();
			stampaAllegatoAttoImpegno.setImpegno(impegno);
			stampaAllegatoAttoImpegno.setSubImpegno(subImpegno);
			//stampaAllegatoAttoImpegno.setCigImpegno(impegno != null && StringU impegno.getCig());
			
			// CR SIAC-2948
			popolaCigECupImpegno(impegno);
			log.debug(methodName, "Impegno: cup =  " + (impegno != null ? impegno.getCup() : "null") + " - cig = " + (impegno != null ? impegno.getCig() : "null"));
			// CR SIAC-2948
			popolaCigECupSubImpegno(subImpegno);
			log.debug(methodName, "SUBimpegno xxx: cup =  " + (subImpegno != null && subImpegno.getCup() != null? subImpegno.getCup() : "null") + " - cig = " + (subImpegno != null && subImpegno.getCup() != null? subImpegno.getCig() : "null"));
			
			// SIAC-4865: precedenza al cig/cup del subimpegno
			setCigIfExists(stampaAllegatoAttoImpegno, impegno);
			setCupIfExists(stampaAllegatoAttoImpegno, impegno);
			setSiopeAssenzaMotivo(stampaAllegatoAttoImpegno, impegno);
			
			setCupIfExists(stampaAllegatoAttoImpegno, subImpegno);
			setCupIfExists(stampaAllegatoAttoImpegno, subImpegno);
			
			mapStampaAllegatoAttoImpegno.put(keyImpegno, stampaAllegatoAttoImpegno);
			// Injetto il riferimento nell'elenco
			stampaAllegatoAttoElenco.getListaStampaAllegatoAttoImpegno().add(stampaAllegatoAttoImpegno);
		}
		
		// CR-2484
		BigDecimal importo = subdocumentoSpesa.getImporto();
		BigDecimal importoDaDedurre = subdocumentoSpesa.getImportoDaDedurre();
		stampaAllegatoAttoElenco.addTotaleImpegni(importo);
		stampaAllegatoAttoElenco.addTotaleImportoDaDedurre(importoDaDedurre);
		stampaAllegatoAttoImpegno.addImporto(importo);
		stampaAllegatoAttoImpegno.addTotaleImportoDaDedurre(importoDaDedurre);
		
		// SIAC-4865 se il tipo documento e' di tipo ALG CNN o DSP
		if(!isTipoDocumentoCorretto(subdocumentoSpesa) ){
			// SIAC-5272 aggiungo i dati nella lista dei cig
			String cacheKey = createCacheKeyImpegnoSubimpegno(impegno, subImpegno);
			Set<String> cigs = cigCache.get(cacheKey, cigCacheInitializer);
			Set<String> cups = cupCache.get(cacheKey, cupCacheInitializer);
			Set<String> siopeAssenzaMotivaziones = samCache.get(cacheKey, siopeAssenzaMotivazioneCacheInitializer); // SIAC-6269
			
			addIfNotBlank(cigs, subdocumentoSpesa.getCig());
			addIfNotBlank(cups, subdocumentoSpesa.getCup());
			// SIAC-6269
			if(subdocumentoSpesa.getSiopeAssenzaMotivazione() != null) {
				addIfNotBlank(siopeAssenzaMotivaziones, subdocumentoSpesa.getSiopeAssenzaMotivazione().getDescrizione());
			}
			stampaAllegatoAttoImpegno.setHasQuoteDaNonEsporre(true);
		}
		
		// SIAC-5271: Nel caso che una fattura sia stata sospesa e quindi riattivata, la stampa deve riportare, come data di scadenza, quella dopo la sospensione
		Date dataScadenza = subdocumentoSpesa.getDataScadenzaDopoSospensione() != null ? subdocumentoSpesa.getDataScadenzaDopoSospensione() : subdocumentoSpesa.getDataScadenza();
		saas.setDataScadenza(dataScadenza);
		
		// SIAC-8835
		if (impegno.getCapitoloUscitaGestione() != null) {
			impegno.getCapitoloUscitaGestione().setSottoConto(capitoloDad.getContoTesoreriaCapitolo(impegno.getCapitoloUscitaGestione()));
		}
				
		// Aggiungo i totali
		BigDecimal importoImponibile = subdocumentoSpesa.getImportoNotNull().subtract(subdocumentoSpesa.getImportoDaDedurreNotNull());
		result.addTotaleAtto(importoImponibile);
		result.addTotaleSplitIstituzionale(TipoIvaSplitReverse.SPLIT_ISTITUZIONALE.equals(subdocumentoSpesa.getTipoIvaSplitReverse()), subdocumentoSpesa.getImportoSplitReverse());
		result.addTotaleSplitCommerciale(TipoIvaSplitReverse.SPLIT_COMMERCIALE.equals(subdocumentoSpesa.getTipoIvaSplitReverse()), subdocumentoSpesa.getImportoSplitReverse());
		result.addTotaleReverseChange(TipoIvaSplitReverse.REVERSE_CHANGE.equals(subdocumentoSpesa.getTipoIvaSplitReverse()), subdocumentoSpesa.getImportoSplitReverse());
	}

	/**
	 * @param sedeSecondariaSoggetto
	 * @param soggettoModpag 
	 * @param soggetto 
	 * @param stampaAllegatoAttoElenco
	 */
	private void caricaSedeSecondariaSoggetto(SedeSecondariaSoggetto sedeSecondariaSoggetto, Soggetto soggetto, Soggetto soggettoModpag, StampaAllegatoAttoElenco stampaAllegatoAttoElenco) {
		if(sedeSecondariaSoggetto != null && sedeSecondariaSoggetto.getUid() != 0) {
			//se il subdocumento e' legato ad una sede secondaria, la modalita' pagamento e' per forza di quelle sede: evito di chiamare il db e caricos
			stampaAllegatoAttoElenco.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
			return;
		}
		//la sede secondaria ha lo stesso codice della sede principale, uid diverso e relazione (su db) di tipo sede secondaria
		if(soggettoModpag != null && soggetto.getUid() != soggettoModpag.getUid() && soggetto.getCodiceSoggetto().equals(soggettoModpag.getCodiceSoggetto()) && isSoggettoModPagSedeSecondaria(soggetto, soggettoModpag)) {
			SedeSecondariaSoggetto sede = new SedeSecondariaSoggetto();
			sede.setDenominazione(soggettoModpag.getDenominazione());
			sede.setUid(soggettoModpag.getUid());
			stampaAllegatoAttoElenco.setSedeSecondariaSoggetto(sede);
		}
		
	}

	/**
	 * @param soggetto
	 * @param soggettoModpag
	 */
	private boolean isSoggettoModPagSedeSecondaria(Soggetto soggetto, Soggetto soggettoModpag) {
		return soggettoDad.checkSoggettoSedeSecondaria(soggetto, soggettoModpag);
	}
	
	/**
	 * Aggiunge la stringa nella lista se non vuota
	 * @param container il contenitore da popolare
	 * @param str la stringa da apporre
	 */
	private void addIfNotBlank(Collection<String> container, String str) {
		if(StringUtils.isNotBlank(str)) {
			container.add(str);
		}
	}
	
	/**
	 * Crea la chiave di cache per impegno e subimpegno
	 * @param impegno l'impegno
	 * @param subImpegno il subimpegno
	 * @return la chiave di cache
	 */
	private String createCacheKeyImpegnoSubimpegno(Impegno impegno, SubImpegno subImpegno) {
		return (impegno != null ? Integer.toString(impegno.getUid()) : "null")
				+ "_"
				+ (subImpegno != null ? Integer.toString(subImpegno.getUid()) : "null");
	}

	/**
	 * Imposta il cup nel caso sia presente nell'impegno
	 * @param stampaAllegatoAttoImpegno la stampa da popolare
	 * @param impegno l'impegno da cui prelevare il cup
	 */
	private void setCupIfExists(StampaAllegatoAttoImpegno stampaAllegatoAttoImpegno, Impegno impegno) {
		if(impegno != null && StringUtils.isNotBlank(impegno.getCup())) {
			stampaAllegatoAttoImpegno.setCup(impegno.getCup());
		}
	}
	
	/**
	 * SIAC-6269  &  SIAC-6315
	 * @param stampaAllegatoAttoImpegno la stampa da popolare
	 * @param impegno l'impegno da cui prelevare la siope
	 */
	private void setSiopeAssenzaMotivo(StampaAllegatoAttoImpegno stampaAllegatoAttoImpegno, Impegno impegno) {
		if(impegno != null && impegno.getSiopeAssenzaMotivazione() != null && StringUtils.isNotBlank(impegno.getSiopeAssenzaMotivazione().getDescrizione())) {
			stampaAllegatoAttoImpegno.setSiopeAssenzaMotivazioneDescr(impegno.getSiopeAssenzaMotivazione().getDescrizione());
		}
	}

	/**
	 * Imposta il cig nel caso sia presente nell'impegno
	 * @param stampaAllegatoAttoImpegno la stampa da popolare
	 * @param impegno l'impegno da cui prelevare il cig
	 */
	private void setCigIfExists(StampaAllegatoAttoImpegno stampaAllegatoAttoImpegno, Impegno impegno) {
		if(impegno != null && StringUtils.isNotBlank(impegno.getCig())) {
			stampaAllegatoAttoImpegno.setCig(impegno.getCig());
		}
	}

	private Soggetto ottieniSoggettoCessione(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		String gruppoAccredito = soggettoDad.ottieniCodiceGruppoAccreditoByModalitaAccredito(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto());
		if((!TipoAccredito.CSC.name().equalsIgnoreCase(gruppoAccredito) && !TipoAccredito.CSI.name().equalsIgnoreCase(gruppoAccredito)) || modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2() == null) {
			// Non sono una cessione
			return null;
		}
		// Sono una cessione: devo determinare il soggetto di cessione
		return soggettoCache.get(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getUid(), soggettoCacheInitializer);
	}

	/**
	 * Inizializzazione del wrapper per la stampa dell'allegato atto, subdocumento
	 * @param subdocumentoSpesa il subdoc da wrappare
	 * @return il wrapper
	 */
	private StampaAllegatoAttoSubdocumento inizializzaStampaAllegatoAttoSubdocumento(SubdocumentoSpesa subdocumentoSpesa) {
		final String methodName = "inizializzaStampaAllegatoAttoSubdocumento";
		StampaAllegatoAttoSubdocumento saas = new StampaAllegatoAttoSubdocumento();
		saas.setSubdocumentoSpesa(subdocumentoSpesa);
		
		String estremiNoteCredito = noteCreditoCache.get(subdocumentoSpesa.getDocumento().getUid(), noteCreditoCacheInitializer);
		String estremiPenali = penaliCache.get(subdocumentoSpesa.getDocumento().getUid(), penaliCacheInitializer);
		String estremiRitenute = ritenuteCache.get(subdocumentoSpesa.getDocumento().getUid(), ritenuteCacheInitializer);
		
		String estremiCartaContabile = ottieniEstremiCassaContabile(subdocumentoSpesa);
		String estremiProvvisorioCassa = ottieniEstremiProvvisorioCassa(subdocumentoSpesa);
		String estremiImpegno = ottieniEstremiImpegno(subdocumentoSpesa);
		String estremiLiquidazione = ottieniEstremiLiquidazione(subdocumentoSpesa);
		
		log.debug(methodName, "Subdocumento [uid " + subdocumentoSpesa.getUid() + "] - estremi note credito: " + estremiNoteCredito);
		log.debug(methodName, "Subdocumento [uid " + subdocumentoSpesa.getUid() + "] - estremi penali: " + estremiPenali);
		log.debug(methodName, "Subdocumento [uid " + subdocumentoSpesa.getUid() + "] - estremi ritenute: " + estremiRitenute);
		
		log.debug(methodName, "Subdocumento [uid " + subdocumentoSpesa.getUid() + "] - estremi carta contabile: " + estremiCartaContabile);
		log.debug(methodName, "Subdocumento [uid " + subdocumentoSpesa.getUid() + "] - estremi provvisorio cassa: " + estremiProvvisorioCassa);
		log.debug(methodName, "Subdocumento [uid " + subdocumentoSpesa.getUid() + "] - estremi impegno: " + estremiImpegno);
		log.debug(methodName, "Subdocumento [uid " + subdocumentoSpesa.getUid() + "] - estremi liquidazione: " + estremiLiquidazione);
		
		saas.setEstremiNoteCredito(estremiNoteCredito);
		saas.setEstremiPenali(estremiPenali);
		saas.setEstremiRitenute(estremiRitenute);
		
		saas.setEstremiCartaContabile(estremiCartaContabile);
		saas.setEstremiProvvisorioCassa(estremiProvvisorioCassa);
		saas.setEstremiImpegno(estremiImpegno);
		saas.setEstremiLiquidazione(estremiLiquidazione);
		return saas;
	}

	/**
	 * Calcolo degli estremi della cassa contabile
	 * @param subdocumentoSpesa il subdoc da cui ottenere i dati della carta
	 * @return i dati della carta contabile
	 */
	private String ottieniEstremiCassaContabile(SubdocumentoSpesa subdocumentoSpesa) {
		List<CartaContabile> carte = subdocumentoSpesaDad.findCartaContabileBySubdocId(subdocumentoSpesa.getUid());
		if(carte == null || carte.isEmpty()) {
			return null;
		}
		
		List<String> chunks = new ArrayList<String>();
		for(CartaContabile cc : carte) {
			chunks.add(cc.getBilancio().getAnno() + "/" + cc.getNumero());
		}
		return StringUtils.join(chunks, ", ");
	}

	/**
	 * Calcolo degli estremi del provvisorio di cassa
	 * @param subdocumentoSpesa il subdoc da cui ottenere i dati della carta
	 * @return i dati della carta contabile
	 */
	private String ottieniEstremiProvvisorioCassa(SubdocumentoSpesa subdocumentoSpesa) {
		if(subdocumentoSpesa.getProvvisorioCassa() == null) {
			return null;
		}
		ProvvisorioDiCassa pc = subdocumentoSpesa.getProvvisorioCassa();
		return pc.getAnno() + "/" + pc.getNumero();
	}
	
	private String ottieniEstremiImpegno(SubdocumentoSpesa subdocumentoSpesa) {
		if(subdocumentoSpesa.getImpegno() == null || subdocumentoSpesa.getImpegno().getNumeroBigDecimal() == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder()
				.append(subdocumentoSpesa.getImpegno().getAnnoMovimento())
				.append("/")
				.append(subdocumentoSpesa.getImpegno().getNumeroBigDecimal().toPlainString());
		if(subdocumentoSpesa.getSubImpegno() != null && subdocumentoSpesa.getSubImpegno().getNumeroBigDecimal() != null) {
			sb.append("-")
				.append(subdocumentoSpesa.getSubImpegno().getNumeroBigDecimal().toPlainString());
		}
		return sb.toString();
	}

	private String ottieniEstremiLiquidazione(SubdocumentoSpesa subdocumentoSpesa) {
		if(subdocumentoSpesa.getLiquidazione() == null || subdocumentoSpesa.getLiquidazione().getNumeroLiquidazione() == null) {
			return null;
		}
		return subdocumentoSpesa.getLiquidazione().getAnnoLiquidazione() + "/" + subdocumentoSpesa.getLiquidazione().getNumeroLiquidazione().toPlainString();
	}
	
	/**
	 * Impostazione dell'indirizzo
	 * @param stampaAllegatoAttoElenco
	 * @param soggettoModpag
	 * @param soggetto
	 */
	private void impostaIndirizzo(StampaAllegatoAttoElenco stampaAllegatoAttoElenco, Soggetto soggettoModpag, Soggetto soggetto) {
		impostaIndirizzo(stampaAllegatoAttoElenco, soggettoModpag, true);
		if(stampaAllegatoAttoElenco.getIndirizzoSoggetto() == null) {
			// Fallback: uso il soggetto del subdoc
			impostaIndirizzo(stampaAllegatoAttoElenco, soggetto, false);
		}
	}
	
	private void impostaIndirizzo(StampaAllegatoAttoElenco stampaAllegatoAttoElenco, Soggetto soggetto, boolean mayUseFirst) {
		final String methodName = "impostaIndirizzo";
		boolean firstUsed = false;
		if(soggetto == null || soggetto.getIndirizzi() == null) {
			return;
		}
		for(IndirizzoSoggetto is : soggetto.getIndirizzi()) {
			if((is != null && is.isCheckPrincipale()) || (mayUseFirst && !firstUsed)) {
				log.debug(methodName, "Impostato indirizzo " + is.getUid());
				stampaAllegatoAttoElenco.setIndirizzoSoggetto(is);
				if(!mayUseFirst || firstUsed) {
					break;
				}
				log.debug(methodName, "Preso il primo indirizzo");
				firstUsed = true;
			}
		}
		log.debug(methodName, "Impostato indirizzo " + (stampaAllegatoAttoElenco.getIndirizzoSoggetto() != null ? Integer.toString(stampaAllegatoAttoElenco.getIndirizzoSoggetto().getUid()) : "null"));
	}

	/**
	 * Carica i valori di cig e cup a livello dell'impegno
	 * 
	 * @param impegno l'impegno
	 * 
	 */
	private void popolaCigECupImpegno(Impegno impegno) {
		if (impegno == null){
			return;
		}
		String cig = impegnoBilDad.getCIG(impegno.getUid());
		impegno.setCig(cig);
		String cup = impegnoBilDad.getCUP(impegno.getUid());
		impegno.setCup(cup);
		// SIAC-6036
		SiopeAssenzaMotivazione sam = impegnoBilDad.getSiopeAssenzaMotivazione(impegno.getUid());
		impegno.setSiopeAssenzaMotivazione(sam);
		
	}

	
	/**
	 * Carica i valori di cig e cup del subimpegno
	 * 
	 * @param subImpegno the SubImpegno
	 * 
	 */
	private void popolaCigECupSubImpegno(SubImpegno subimpegno) {
		if (subimpegno== null){
			return;
		}
		String cig = subImpegnoBilDad.getCIG(subimpegno.getUid());
		subimpegno.setCig(cig);
		String cup = subImpegnoBilDad.getCUP(subimpegno.getUid());
		subimpegno.setCup(cup);
		// SIAC-6036
		SiopeAssenzaMotivazione sam = subImpegnoBilDad.getSiopeAssenzaMotivazione(subimpegno.getUid());
		subimpegno.setSiopeAssenzaMotivazione(sam);
	}
	
	/**
	 * Per la tabella relativa alle fatture prendere in considerazione solo i documenti escludendo i tipi <code>ALG</code>, <code>CNN</code>, <code>DSP</code>.
	 * 
	 * @param subdocumentoSpesa il subdocumento
	 * 
	 * @return se il tipo di documento sia corretto per l'injezione nella lista
	 */
	private boolean isTipoDocumentoCorretto(SubdocumentoSpesa subdocumentoSpesa) {
		return subdocumentoSpesa.getDocumento().getTipoDocumento() != null
				&& !subdocumentoSpesa.getDocumento().getTipoDocumento().isAllegatoAtto()
				&& !subdocumentoSpesa.getDocumento().getTipoDocumento().isCartaContabile()
				&& !subdocumentoSpesa.getDocumento().getTipoDocumento().isDisposizioneDiPagamento();
	}

	/**
	 * Costruzione della chiave per soggetto + modalita di pagamento.
	 * <br/>
	 * Codice: <code>S:(Soggetto.uid|null)_MPS:(ModalitaPagamentoSoggetto.uid|null)</code>
	 * 
	 * @param soggetto
	 * @param modalitaPagamentoSoggetto
	 * @return la chiave
	 */
	private String buildKeyElenco(Soggetto soggetto, ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		StringBuilder sb = new StringBuilder();
		sb.append("S:");
		sb.append(soggetto != null ? soggetto.getUid() : "null");
		sb.append("_MPS:");
		sb.append(modalitaPagamentoSoggetto != null ? modalitaPagamentoSoggetto.getUid() : "null");
		
		return sb.toString();
	}
	
	/**
	 * Costruzione della chiave per soggetto + modalita di pagamento.
	 * <br/>
	 * Codice: <code>S:(Soggetto.uid|null)_MPS:(ModalitaPagamentoSoggetto.uid|null)_I:(Impegno.uid|null)_SI:(SubImpegno.uid|null)</code>
	 * 
	 * @param soggetto
	 * @param modalitaPagamentoSoggetto
	 * @param impegno
	 * @param subImpegno
	 * @return la chiave
	 */
	private String buildKeyImpegno(Soggetto soggetto,ModalitaPagamentoSoggetto modalitaPagamentoSoggetto, Impegno impegno, SubImpegno subImpegno) {
		StringBuilder sb = new StringBuilder();
		sb.append(buildKeyElenco(soggetto, modalitaPagamentoSoggetto));
		sb.append("_I:");
		sb.append(impegno != null ? impegno.getUid() : "null");
		sb.append("_SI:");
		sb.append(subImpegno != null ? subImpegno.getUid() : "null");
		
		return sb.toString();
	}
	
	/**
	 * Controlla se vi sia almeno un onere collegato all'atto
	 */
	private void elaboraOneri() {
		Long oneri = allegatoAttoDad.countOneriByAllegatoAtto(allegatoAtto);
		result.setHasRitenute(Boolean.valueOf(oneri.longValue() > 0));
	}

	@Override
	protected void handleResponse(GeneraReportResponse res) {
		final String methodName = "handleResponse";		
		persistiStampaAllegatoAtto(res);
		log.debug(methodName, "numero di pagine generata: " + res.getNumeroPagineGenerate());
	}
	
	private void persistiStampaAllegatoAtto(GeneraReportResponse res) {
		final String methodName = "persistiStampaAllegatoAtto";
		log.debug(methodName, "Persistenza della stampa");
		InserisceStampaAllegatoAtto reqISAA = new InserisceStampaAllegatoAtto();
		reqISAA.setDataOra(new Date());
		reqISAA.setRichiedente(richiedente);
		reqISAA.setAllegatoAttoStampa(creaAllegatoAttoStampa(res));
//		log.
		InserisceStampaAllegatoAttoResponse resISAA = inserisceStampaAllegatoAttoService.executeService(reqISAA);
		if(resISAA == null) {
			log.error(methodName, "Response del servizio InserisceStampaAllegatoAttoService null");
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il servizio InserisceStampaIva ha risposto null"));
		}
		if(resISAA.hasErrori()) {
			log.error(methodName, "Errori nell'invocazione del servizio InserisceStampaAllegatoAttoService: " + resISAA.getDescrizioneErrori());
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il servizio InserisceStampaAllegatoAtto e' terminato con errori") + " Errori riscontrati: "+ resISAA.getDescrizioneErrori());
		}
		log.info(methodName, "Stampa terminata con successo. Uid AllegatoAttoStampa inserito su database: " + resISAA.getAllegatoAttoStampa().getUid());
		
	}

	private AllegatoAttoStampa creaAllegatoAttoStampa(GeneraReportResponse res) {
		AllegatoAttoStampa result = new AllegatoAttoStampa();
//		
		result.setAnnoEsercizio(annoEsercizio);
		result.setBilancio(bilancio);
		result.setTipoStampa(TipoStampaAllegatoAtto.ALLEGATO);
		result.setAllegatoAtto(allegatoAtto);
		result.setVersioneInvioFirma(allegatoAtto.getVersioneInvioFirma()!=null? allegatoAtto.getVersioneInvioFirma() : 0);
		result.setEnte(ente);
		
		File file = res.getReport();
		List<File> listaFile = new ArrayList<File>();
		
		listaFile.add(file);
		result.setFiles(listaFile);
		result.setCodice(file.getCodice());
		
		return result;
	}

	/**
	 * @return the allegatoAtto
	 */
	public AllegatoAtto getAllegatoAtto() {
		return allegatoAtto;
	}

	/**
	 * @param allegatoAtto the allegatoAtto to set
	 */
	public void setAllegatoAtto(AllegatoAtto allegatoAtto) {
		this.allegatoAtto = allegatoAtto;
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
	 * @return the tipoStampa
	 */
	public TipoStampaAllegatoAtto getTipoStampa() {
		return tipoStampa;
	}

	/**
	 * @param tipoStampa the tipoStampa to set
	 */
	public void setTipoStampa(TipoStampaAllegatoAtto tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

	
	protected void elaborationEnd(){
		if(idOperazioneAsincrona == null){
			log.warn("elaborationEnd", "idOperazioneAsincrona == null");
			return;
		}
		//ciclo su errori e messagi e inserisco dettaglio operazione asincrona
		
		//inserisco gli eventuali messaggi della response su base dati, in modo tale che sia poi possibile mostrarli da cruscotto
		for(Errore errore : errori) {
			//log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - "+ messaggio.getDescrizione());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		if(errori.size() == 0){
			//non si sono verificati errori, inserisco l'informazione di successo su base dati in modo tale che venga visualizzata
			inserisciDettaglioSuccesso();
			aggiornaOperazioneAsinc(StatoOperazioneAsincronaEnum.STATO_OPASINC_CONCLUSA);
			return;
		}
		aggiornaOperazioneAsinc(StatoOperazioneAsincronaEnum.STATO_OPASINC_ERRORE);
	}

	/**
	 * 
	 * @param codice
	 * @param descrizione
	 * @param esito
	 */
	protected void inserisciDettaglioOperazioneAsinc(String codice, String descrizione, Esito esito) {
		InserisciDettaglioOperazioneAsinc reqdett = new InserisciDettaglioOperazioneAsinc();

		reqdett.setIdOperazioneAsincrona(this.idOperazioneAsincrona);
		reqdett.setCodice(codice);
		reqdett.setDescrizione(descrizione);
		reqdett.setRichiedente(richiedente);
		reqdett.setIdEnte(ente.getUid());
		reqdett.setEsito(esito.name());

		InserisciDettaglioOperazioneAsincResponse resIDOA = operazioneAsincronaService.inserisciDettaglioOperazioneAsinc(reqdett);
		//checkServiceResponseFallimento(resIDOA);
		if (resIDOA.isFallimento()) {

			throw new ExecuteExternalServiceException("\nEsecuzione servizio interno  InserisciDettaglioOperazioneAsincService  terminata con esito Fallimento."
					+ "\nErrori riscontrati da  InserisciDettaglioOperazioneAsincService : {" + resIDOA.getDescrizioneErrori().replaceAll("\n", "\n\t") + "}.",
					resIDOA.getErrori());
		}
	}
	/**
	 * 
	 */
	protected void inserisciDettaglioSuccesso() {
		inserisciDettaglioOperazioneAsinc("CRU_CON_2001", "L'operazione e' stata completata con successo", Esito.SUCCESSO);
	}
	
	/**
	 * 
	 */
	protected void gestisciErroreElaborazione(Exception e) {
		Errore s = ErroreCore.ERRORE_DI_SISTEMA.getErrore(e.getMessage());
		errori.add(s);
	}
	
	/**
	 * Aggiorna lo stato dell'operazione asincrona
	 * 
	 * @param stato
	 */
	protected void aggiornaOperazioneAsinc(StatoOperazioneAsincronaEnum stato) {
		AggiornaOperazioneAsinc reqAgg = new AggiornaOperazioneAsinc();
		reqAgg.setRichiedente(richiedente);
		reqAgg.setIdOperazioneAsinc(this.idOperazioneAsincrona);
		reqAgg.setIdEnte(ente.getUid());

		reqAgg.setStato(stato);

		AggiornaOperazioneAsincResponse resAgg = operazioneAsincronaService.aggiornaOperazioneAsinc(reqAgg);
		if (resAgg.isFallimento()) {

			throw new ExecuteExternalServiceException("\nEsecuzione servizio interno  InserisciDettaglioOperazioneAsincService  terminata con esito Fallimento."
					+ "\nErrori riscontrati da  InserisciDettaglioOperazioneAsincService : {" + resAgg.getDescrizioneErrori().replaceAll("\n", "\n\t") + "}.",
					resAgg.getErrori());
		}
	}

	public Checklist getAllegatoAttoChecklist() {
		return allegatoAttoChecklist;
	}

	public void setAllegatoAttoChecklist(Checklist allegatoAttoChecklist) {
		this.allegatoAttoChecklist = allegatoAttoChecklist;
	}
	

}
