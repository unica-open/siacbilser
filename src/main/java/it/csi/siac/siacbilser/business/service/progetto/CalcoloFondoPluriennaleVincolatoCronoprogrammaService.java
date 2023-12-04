/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioBaseCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class CalcoloFondoPluriennaleVincolatoCronoprogrammaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated
public class CalcoloFondoPluriennaleVincolatoCronoprogrammaService extends CheckedAccountBaseService<CalcoloFondoPluriennaleVincolatoCronoprogramma, CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse> {
	
	
	/** The cronoprogramma. */
	private Cronoprogramma cronoprogramma;

	
	/** The ricerca dettaglio cronoprogramma service. */
	@Autowired
	private RicercaDettaglioCronoprogrammaService ricercaDettaglioCronoprogrammaService;
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//parametri del cronoprogramma
		cronoprogramma  = req.getCronoprogramma();

		checkNotNull(cronoprogramma, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cronoprogramma"));
		checkCondition(cronoprogramma.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid cronoprogramma"));
		
		//aggiunti in data 17_07_2015
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno bilancio"));
	}
	
	@Override
	@Transactional(readOnly=true)
	public CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse executeService(CalcoloFondoPluriennaleVincolatoCronoprogramma serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		// TODO: SIAC-4103, 4153: al momento blocco la generazione dell'FPV se il crono e' da definire
		checkDaDefinire();
		
		popolaDettaglioCronoprogramma();
		
		
		caricaListaAnniComplessiva();
		
		calcolaFondoPluriennaleVincolatoUscita();
		calcolaFondoPluriennaleVincolatoEntrata();
		
		logFPVUscita(methodName, res.getListaFondoPluriennaleVincolatoUscitaCronoprogramma());
		logFPVEntrata(methodName, res.getListaFondoPluriennaleVincolatoEntrataCronoprogramma());
	}

	private void checkDaDefinire() {
		boolean daDefinire = cronoprogrammaDad.isDaDefinire(cronoprogramma);
		if(daDefinire) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il cronoprogramma e' da definire, il calcolo dell'FPV non e' disponibile"));
		}
	}
	
	/**
	 * Calcola fondo pluriennale vincolato entrata.
	 */
	private void calcolaFondoPluriennaleVincolatoEntrata() {
		final String methodName = "calcolaFondoPluriennaleVincolatoEntrata";
		
		for(Integer anno: cronoprogramma.getAnniEntrataEUscita()) {
			
			log.debug(methodName, "anno: "+ anno);
			
			List<FondoPluriennaleVincolatoUscitaCronoprogramma> fpvs = res.filterFPVUscitaCompleteByAnno(anno - 1);
			logFPVUscita(methodName, fpvs);
			
			Set<String> codiciTitoloEntrata = getCodiciTitoloEntrataByAnno(anno);
			
			for (String codiceTitoloEntrata : codiciTitoloEntrata) {
				res.getListaFondoPluriennaleVincolatoEntrataCronoprogramma().add(sommaTitolo(fpvs, anno, codiceTitoloEntrata));
			}

		}
	}
	

	/**
	 * Gets the codici titolo entrata by anno.
	 *
	 * @param anno the anno
	 * @return the codici titolo entrata by anno
	 */
	private Set<String> getCodiciTitoloEntrataByAnno(Integer anno) {
		Set<String> s = new HashSet<String>();
		for(DettaglioEntrataCronoprogramma dec : cronoprogramma.getCapitoliEntrata()){
			if(anno.equals(dec.getAnnoCompetenza()) && dec.getTitoloEntrata() != null && dec.getTitoloEntrata().getCodice() != null) {
				s.add(dec.getTitoloEntrata().getCodice());
			}
		}
		
		return s;
	}


	/**
	 * Somma titolo.
	 *
	 * @param fpvs the fpvs
	 * @param anno the anno
	 * @param codiceTitolo the codice titolo
	 * @return the fondo pluriennale vincolato entrata cronoprogramma
	 */
	private FondoPluriennaleVincolatoEntrataCronoprogramma sommaTitolo(List<FondoPluriennaleVincolatoUscitaCronoprogramma> fpvs, Integer anno, String codiceTitolo) {
		final String methodName = "sommaTitolo";
		FondoPluriennaleVincolatoEntrataCronoprogramma res = new FondoPluriennaleVincolatoEntrataCronoprogramma();
		res.setAnno(anno);
		TitoloSpesa titoloSpesa = new TitoloSpesa();
		titoloSpesa.setCodice(codiceTitolo);
		res.setTitoloSpesa(titoloSpesa);
		
		BigDecimal sum1 = BigDecimal.ZERO;
		
		//if("1".equals(codiceTitolo) || "2".equals(codiceTitolo)) { //La somma per l'FPV entrata viene effettuata solo se codiceTitolo è 1 o 2;
			for(FondoPluriennaleVincolatoUscitaCronoprogramma fpv : fpvs) {
				if(codiceTitolo.equals(fpv.getTitoloSpesa().getCodice())){
					sum1 = sum1.add(fpv.getImportoFPV());
				}
			}
		//}
		
		res.setImportoFPV(sum1);
		log.debug(methodName, "anno: " + anno + " codiceTitolo: " + codiceTitolo + " importo fpv: " + sum1);
		
		BigDecimal sum2 = BigDecimal.ZERO;
		for(DettaglioEntrataCronoprogramma dec : cronoprogramma.getCapitoliEntrata()) {
			if(dec.getTitoloEntrata() != null && codiceTitolo.equals(dec.getTitoloEntrata().getCodice()) && anno.equals(dec.getAnnoCompetenza())){
				sum2 = sum2.add(dec.getStanziamento());
			}
		}
		
		res.setImporto(sum2);
		log.debug(methodName, "anno: " + anno + " codiceTitolo: " + codiceTitolo + " importo: " + sum2);
		
		return res;
	}


	/**
	 * Merge by anno.
	 *
	 * @param anno the anno
	 * @param capitoliUscitaAggregatiPerFPV the capitoli uscita aggregati per fpv
	 * @param capitoliUscitaAggregatiPerAnno the capitoli uscita aggregati per anno
	 * @return the list
	 */
	private List<FondoPluriennaleVincolatoUscitaCronoprogramma> mergeByAnno(Integer anno,
			Iterable<DettaglioUscitaCronoprogramma> capitoliUscitaAggregatiPerFPV, Iterable<DettaglioUscitaCronoprogramma> capitoliUscitaAggregatiPerAnno) {
		
		List<FondoPluriennaleVincolatoUscitaCronoprogramma> list = new ArrayList<FondoPluriennaleVincolatoUscitaCronoprogramma>();
		
		for(DettaglioUscitaCronoprogramma duc : capitoliUscitaAggregatiPerFPV){
			FondoPluriennaleVincolatoUscitaCronoprogramma fpvu = new FondoPluriennaleVincolatoUscitaCronoprogramma(anno, duc);
			fpvu.setImportoFPV(duc.getStanziamento());
			list.add(fpvu);
		}
		
		for(DettaglioUscitaCronoprogramma duc : capitoliUscitaAggregatiPerAnno){
			FondoPluriennaleVincolatoUscitaCronoprogramma fpvu = findByAnnoMissioneProgrammaTitolo(list,anno, duc.getMissione(), duc.getProgramma(), duc.getTitoloSpesa());
			
			if(fpvu == null) {
				fpvu = new FondoPluriennaleVincolatoUscitaCronoprogramma(anno, duc);
				list.add(fpvu);
			}
			
			fpvu.setImporto(duc.getStanziamento());
		}

		return list;
	}

	/**
	 * Find by anno missione programma titolo.
	 *
	 * @param list the list
	 * @param anno the anno
	 * @param missione the missione
	 * @param programma the programma
	 * @param titoloSpesa the titolo spesa
	 * @return the fondo pluriennale vincolato uscita cronoprogramma
	 */
	private FondoPluriennaleVincolatoUscitaCronoprogramma findByAnnoMissioneProgrammaTitolo(Iterable<FondoPluriennaleVincolatoUscitaCronoprogramma> list, Integer anno,
			Missione missione, Programma programma, TitoloSpesa titoloSpesa) {
		for (FondoPluriennaleVincolatoUscitaCronoprogramma fpv : list) {
			if (anno.equals(fpv.getAnno()) && areEquals(fpv.getMissione(), missione) && areEquals(fpv.getProgramma(), programma) && areEquals(fpv.getTitoloSpesa(), titoloSpesa)) {
				return fpv;
			}
		}
		return null;
	}
	
	/**
	 * Controlla se le due entit&agrave; sono identiche.
	 * <br/>
	 * Due entit&agrave; sono identiche se sono entrambe null o hanno pari uid
	 * @param entita1 la prima entit&agrave;
	 * @param entita2 la seconda entit&agrave;
	 * @return <code>true</code> se le due entit&agrave; sono identiche; <code>false</code> altrimenti
	 */
	private boolean areEquals(Entita entita1, Entita entita2) {
		return (entita1 == null && entita2 == null) || (entita1 != null && entita2 != null && entita1.getUid() == entita2.getUid());
	}


	/**
	 * Group by missione programma titolo.
	 *
	 * @param dettagliUscita the dettagli uscita
	 * @return the list
	 */
	private Collection<DettaglioUscitaCronoprogramma> groupByMissioneProgrammaTitolo(List<DettaglioUscitaCronoprogramma> dettagliUscita) {
		Map<String, DettaglioUscitaCronoprogramma> m = new HashMap<String, DettaglioUscitaCronoprogramma>();
		for (DettaglioUscitaCronoprogramma duc : dettagliUscita){
			String key = buildKey(duc);
			
			DettaglioUscitaCronoprogramma ducPre  = m.get(key);
			if(ducPre == null){
				ducPre = new DettaglioUscitaCronoprogramma();
				ducPre.setMissione(duc.getMissione());
				ducPre.setProgramma(duc.getProgramma());
				ducPre.setTitoloSpesa(duc.getTitoloSpesa());
				ducPre.setAnnoCompetenza(duc.getAnnoCompetenza());
				ducPre.setAnnoEntrata(duc.getAnnoEntrata());
			} 
			
			ducPre.setStanziamento(ducPre.getStanziamento().add(duc.getStanziamento()));
			m.put(key, ducPre);
		}
		
		log.debug("groupByMissioneProgrammaTitolo", "grouped map: " + m.keySet());
		
		return m.values();
	}
	
	/**
	 * Costruisce la chiave per il dettaglio di uscita
	 * @param duc il dettaglio per cui calcolare la chiave
	 * @return la chiave
	 */
	private String buildKey(DettaglioUscitaCronoprogramma duc) {
		return new StringBuilder()
				.append(duc.getMissione() != null ? Integer.toString(duc.getMissione().getUid()) : "null")
				.append("/")
				.append(duc.getProgramma() != null ? Integer.toString(duc.getProgramma().getUid()) : "null")
				.append("/")
				.append(duc.getTitoloSpesa() != null ? Integer.toString(duc.getTitoloSpesa().getUid()) : "null")
				.toString();
	}
	
	/**
	 * Filter by anno for fpv uscita.
	 *
	 * @param dettagliUscita the dettagli uscita
	 * @param anno the anno
	 * @return the list
	 */
	private List<DettaglioUscitaCronoprogramma> filterByAnnoForFPVUscita(List<DettaglioUscitaCronoprogramma> dettagliUscita, Integer anno) {
		final String methodName = "filterByAnnoForFPVUscita";
		List<DettaglioUscitaCronoprogramma> result = new ArrayList<DettaglioUscitaCronoprogramma>();
		for (DettaglioUscitaCronoprogramma dett : dettagliUscita) {
			if(dett.getAnnoCompetenza() != null && dett.getAnnoEntrata() != null && dett.getAnnoCompetenza().compareTo(anno) > 0 && dett.getAnnoEntrata().compareTo(anno) <= 0){
				result.add(dett);
			}
		}
		log.debug(methodName, "returnig "+result.size()+" results for anno: "+anno);
		logDettaglioUscitaCronoprogramma(methodName,result);
		return result;
	}
	
	/**
	 * Filter dettagli uscita by anno.
	 *
	 * @param dettagli the dettagli
	 * @param anno the anno
	 * @return the list
	 */
	private List<DettaglioUscitaCronoprogramma> filterDettagliUscitaByAnno(Iterable<DettaglioUscitaCronoprogramma> dettagli, Integer anno) {
		String methodName = "filterDettagliUscitaByAnno";
		List<DettaglioUscitaCronoprogramma> filteredList = filterByAnno(dettagli, anno);
		log.debug(methodName, "returning " + filteredList.size() + " results for anno: " + anno);
		logDettaglioUscitaCronoprogramma(methodName, filteredList);
		return filteredList;
	}
	
	/**
	 * Filter by anno.
	 *
	 * @param <T> the generic type
	 * @param dettagli the dettagli
	 * @param anno the anno
	 * @return the list
	 */
	private <T extends DettaglioBaseCronoprogramma> List<T> filterByAnno(Iterable<T> dettagli, Integer anno) {
		List<T> result = new ArrayList<T>();
		for (T dett : dettagli) {
			if(dett.getAnnoCompetenza() != null && dett.getAnnoCompetenza().compareTo(anno) == 0){
				result.add(dett);
			}
		}
		
		return result;
	}


	/**
	 * Popola dettaglio cronoprogramma.
	 */
	private void popolaDettaglioCronoprogramma() {
		final String methodName = "popolaDettaglioCronoprogramma";
		RicercaDettaglioCronoprogramma reqd = new RicercaDettaglioCronoprogramma();
		reqd.setRichiedente(req.getRichiedente());
		reqd.setCronoprogramma(cronoprogramma);
		RicercaDettaglioCronoprogrammaResponse respd = executeExternalServiceSuccess(ricercaDettaglioCronoprogrammaService, reqd);
		cronoprogramma = respd.getCronoprogramma();
		
		logDettaglioUscitaCronoprogramma(methodName,cronoprogramma.getCapitoliUscita());
		logDettaglioEntrataCronoprogramma(methodName, cronoprogramma.getCapitoliEntrata());
	}
	
	
	//TODO sarebbe bello metterlo nel LogSrvUtil.java!!
	/**
	 * Log list.
	 *
	 * @param <T> the generic type
	 * @param methodName the method name
	 * @param message the message
	 * @param list the list
	 */
	private <T> void  logList(String methodName, String message, List<T> list) {
		if(log.isDebugEnabled()) {	
			StringBuilder sb = new StringBuilder();
			sb.append(message).append(": ");
			
			if(list!=null){
				for(T o : list) {
					sb.append("\n\t").append(o);
				}
			}
			
			log.debug(methodName, sb.toString());
		}
	}

	
	/**
	 * Log fpv uscita.
	 *
	 * @param methodName the method name
	 * @param detts the detts
	 */
	private void logFPVUscita(String methodName, List<FondoPluriennaleVincolatoUscitaCronoprogramma> detts) {
		logList(methodName, "FpvUscita", detts);
	}
	
	/**
	 * Log fpv entrata.
	 *
	 * @param methodName the method name
	 * @param detts the detts
	 */
	private void logFPVEntrata(String methodName, List<FondoPluriennaleVincolatoEntrataCronoprogramma> detts) {
		logList(methodName, "FpvEntrata", detts);
	}	

	/**
	 * Log dettaglio uscita cronoprogramma.
	 *
	 * @param methodName the method name
	 * @param detts the detts
	 */
	private void logDettaglioUscitaCronoprogramma(String methodName, List<DettaglioUscitaCronoprogramma> detts) {
		logList(methodName, "Dettagli Uscita", detts);
	}
	
	/**
	 * Log dettaglio entrata cronoprogramma.
	 *
	 * @param methodName the method name
	 * @param detts the detts
	 */
	private void logDettaglioEntrataCronoprogramma(String methodName,List<DettaglioEntrataCronoprogramma> detts) {
		logList(methodName, "Dettagli Entrata", detts);
	}
	
	/**
	 * CARICA LA LISTA DEGLI ANNI COMPLESSIVI dall'anno piu piccolo a ll'anno piu grande
	 * tra entrata e spesa ed include anche l'anno del bilancio 
	 */
	private List<Integer> caricaListaAnniComplessiva(){
		
		List<Integer> listaAnniEntrataEUscita = cronoprogramma.getAnniEntrataEUscita();
 		Integer annoBilancio = req.getBilancio().getAnno();

 		if(listaAnniEntrataEUscita.isEmpty()){
 			listaAnniEntrataEUscita.add(annoBilancio);
 			res.setListaAnniEntrataUscitaComplessiva(listaAnniEntrataEUscita);
 			return listaAnniEntrataEUscita;
 		}
 		
		if (!listaAnniEntrataEUscita.contains(annoBilancio)) {
			listaAnniEntrataEUscita.add(annoBilancio);
		}
		Collections.sort(listaAnniEntrataEUscita);

		res.setListaAnniEntrataUscitaComplessiva(listaAnniEntrataEUscita);
		
		return listaAnniEntrataEUscita;
		
	}
	/**
	 * Calcola fondo pluriennale vincolato uscita.
	 */
	private void calcolaFondoPluriennaleVincolatoUscita() {
		String methodName = "calcolaFondoPluriennaleVincolatoUscita";
		for (Integer anno : res.getListaAnniEntrataUscitaComplessiva()) {

			log.debug(methodName, "Anno : " + anno + " con indice : " + (cronoprogramma.getAnniUscita().indexOf(anno) + 1));

			List<DettaglioUscitaCronoprogramma> capitoliUscitaFiltratiPerFPV = filterByAnnoForFPVUscita(cronoprogramma.getCapitoliUscita(), anno);
			Collection<DettaglioUscitaCronoprogramma> capitoliUscitaAggregatiPerFPV = groupByMissioneProgrammaTitolo(capitoliUscitaFiltratiPerFPV);

			List<DettaglioUscitaCronoprogramma> capitoliUscitaFiltratiPerAnno = filterDettagliUscitaByAnno(cronoprogramma.getCapitoliUscita(), anno);
			Collection<DettaglioUscitaCronoprogramma> capitoliUscitaAggregatiPerAnno = groupByMissioneProgrammaTitolo(capitoliUscitaFiltratiPerAnno);

			res.getListaFondoPluriennaleVincolatoUscitaCronoprogramma().addAll(mergeByAnno(anno, capitoliUscitaAggregatiPerFPV, capitoliUscitaAggregatiPerAnno));

		}
	}


}
