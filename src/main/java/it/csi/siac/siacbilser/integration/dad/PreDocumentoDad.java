/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.PreDocumentoDao;
import it.csi.siac.siacbilser.integration.dao.SiacTPredocNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPredocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDPredocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTPredocAnagr;
import it.csi.siac.siacbilser.integration.entity.SiacTPredocNum;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPredocStatoEnum;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Data access delegate di un PreDocumento.
 *
 * @author Domenico
 */
public abstract class PreDocumentoDad<P extends PreDocumento<?, ?>, MD extends ModelDetailEnum> extends ExtendedBaseDadImpl {
	
	/** The pre documento dao. */
	@Autowired
	protected PreDocumentoDao preDocumentoDao;
	
	/** The eef. */
	@Autowired
	protected EnumEntityFactory eef;
	
	/** The siac t predoc num repository. */
	@Autowired
	protected SiacTPredocNumRepository siacTPredocNumRepository;
	
	@Autowired
	protected SiacTPredocRepository siacTPredocRepository;
	
	/**
	 * Find pre documento by id.
	 *
	 * @param uid the uid
	 * @return the pre documento entrata
	 */
	public abstract P findPreDocumentoById(Integer uid);

	/**
	 * Find pre documento by id, with model details.
	 *
	 * @param uid the uid
	 * @param modelDetails the model details
	 * @return the pre documento entrata
	 */
	public abstract P findPreDocumentoByIdModelDetail(Integer uid, MD... modelDetails);
	
	/**
	 * Inserisci anagrafica pre documento.
	 *
	 * @param predocumento the documento
	 */
	public void inserisciAnagraficaPreDocumento(P predocumento) {
		SiacTPredoc siacTPredoc = buildSiacTPredoc(predocumento);
		siacTPredoc.setLoginCreazione(loginOperazione);
		siacTPredoc.setLoginModifica(loginOperazione);
		if(siacTPredoc.getSiacTPredocAnagrs()!=null){
			for(SiacTPredocAnagr t : siacTPredoc.getSiacTPredocAnagrs()){
				t.setLoginCreazione(loginOperazione);
				siacTPredoc.setLoginModifica(loginOperazione);
			}
		}
		preDocumentoDao.create(siacTPredoc);
		predocumento.setUid(siacTPredoc.getUid());
	}

	/**
	 * Aggiorna anagrafica pre documento.
	 *
	 * @param predocumento the documento
	 */
	public void aggiornaAnagraficaPreDocumento(P predocumento) {
		SiacTPredoc siacTPredoc = buildSiacTPredoc(predocumento);
		siacTPredoc.setLoginModifica(loginOperazione);
		if(siacTPredoc.getSiacTPredocAnagrs()!=null){
			for(SiacTPredocAnagr t : siacTPredoc.getSiacTPredocAnagrs()){
				t.setLoginCreazione(loginOperazione);
				t.setLoginModifica(loginOperazione);
			}
		}
		
		preDocumentoDao.update(siacTPredoc);
		predocumento.setUid(siacTPredoc.getUid());
	}
	
	/**
	 * Builds the siac t predoc.
	 *
	 * @param documento the documento
	 * @return the siac t predoc
	 */
	protected abstract SiacTPredoc buildSiacTPredoc(P documento);
	
	/**
	 * Ricerca puntuale pre documento entrata.
	 *
	 * @param preDoc the pre doc
	 * @return the pre documento entrata
	 */
	public abstract P ricercaPuntualePreDocumento(P preDoc);
	
	/**
	 * Converte il numero in bigdecimal
	 * @param number il numero da convertire
	 * @return il BigDecimal corrispondente
	 */
	protected BigDecimal toBigDecimal(Integer number) {
		return number != null ? new BigDecimal(number.intValue()) : null;
	}
	
	/**
	 * Definisci pre documento.
	 *
	 * @param uidPreDocumento the uid pre documento
	 * @param uidSubdocumento the uid subdocumento
	 */
	public void definisciPreDocumento(Integer uidPreDocumento, Integer uidSubdocumento) {
		if(uidSubdocumento == null || uidSubdocumento.intValue() == 0) {
			// Non ho il predoc: esco
			return;
		}
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uidPreDocumento);
		
		aggiornaStatoPreDocumento(siacTPredoc, StatoOperativoPreDocumento.DEFINITO);
		
		Date dataCancellazione = new Date();
		if(siacTPredoc.getSiacRPredocSubdocs()==null){
			siacTPredoc.setSiacRPredocSubdocs(new ArrayList<SiacRPredocSubdoc>());
		}		
		for(SiacRPredocSubdoc r : siacTPredoc.getSiacRPredocSubdocs()){
			r.setDataCancellazioneIfNotSet(dataCancellazione);
		}
		Date now = new Date();
		SiacRPredocSubdoc siacRPredocSubdoc = new SiacRPredocSubdoc();
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setUid(uidSubdocumento);
		siacRPredocSubdoc.setSiacTSubdoc(siacTSubdoc);
		siacRPredocSubdoc.setSiacTPredoc(siacTPredoc);
		siacRPredocSubdoc.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
		siacRPredocSubdoc.setDataInizioValidita(now);
		siacRPredocSubdoc.setDataCreazione(now);
		siacRPredocSubdoc.setDataModifica(now);
		siacRPredocSubdoc.setLoginOperazione(loginOperazione);
		
		siacTPredoc.addSiacRPredocSubdoc(siacRPredocSubdoc);
	}
	
	/**
	 * Colllega pre documento.
	 *
	 * @param uidPreDocumento the uid pre documento
	 * @param uidSubdocumento the uid subdocumento
	 */
	public void collegaPreDocumento(Integer uidPreDocumento, Integer uidSubdocumento) {
		if(uidSubdocumento == null || uidSubdocumento.intValue() == 0) {
			// Non ho il predoc: esco
			return;
		}
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uidPreDocumento);
		
		Date dataCancellazione = new Date();
		if(siacTPredoc.getSiacRPredocSubdocs()==null){
			siacTPredoc.setSiacRPredocSubdocs(new ArrayList<SiacRPredocSubdoc>());
		}		
		for(SiacRPredocSubdoc r : siacTPredoc.getSiacRPredocSubdocs()){
			r.setDataCancellazioneIfNotSet(dataCancellazione);
		}
		Date now = new Date();
		SiacRPredocSubdoc siacRPredocSubdoc = new SiacRPredocSubdoc();
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setUid(uidSubdocumento);
		siacRPredocSubdoc.setSiacTSubdoc(siacTSubdoc);
		siacRPredocSubdoc.setSiacTPredoc(siacTPredoc);
		siacRPredocSubdoc.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
		siacRPredocSubdoc.setDataInizioValidita(now);
		siacRPredocSubdoc.setDataCreazione(now);
		siacRPredocSubdoc.setDataModifica(now);
		siacRPredocSubdoc.setLoginOperazione(loginOperazione);
		
		siacTPredoc.addSiacRPredocSubdoc(siacRPredocSubdoc);
	}
	
	/**
	 * Aggiorna stato pre documento.
	 *
	 * @param uidPreDocumento the uid pre documento
	 * @param statoOperativoPreDocumento the stato operativo pre documento
	 */
	public void aggiornaStatoPreDocumento(Integer uidPreDocumento, StatoOperativoPreDocumento statoOperativoPreDocumento) {
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uidPreDocumento);
		Date now = new Date();
		//SIAC-YYYY
		/*if(siacTPredoc.getSiacRPredocProvCassas()!=null){
			for(SiacRPredocProvCassa r : siacTPredoc.getSiacRPredocProvCassas()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}*/
		aggiornaStatoPreDocumento(siacTPredoc, statoOperativoPreDocumento);
	}
	
	/**
	 * Aggiorna stato pre documento by subdoc.
	 *
	 * @param uidPreDocumento the uid pre documento
	 * @param statoOperativoPreDocumento the stato operativo pre documento
	 */
	public void aggiornaStatoPreDocumentiAssociatiAlSubdocumento(Integer uidSubdoc, StatoOperativoPreDocumento statoOperativoPreDocumento) {
		List<SiacTPredoc> siacTPredocs = preDocumentoDao.findBySubdocId(uidSubdoc);
		for(SiacTPredoc siacTPredoc : siacTPredocs) {
			aggiornaStatoPreDocumento(siacTPredoc.getUid(), statoOperativoPreDocumento);
		}
	}

	/**
	 * Aggiorna stato pre documento entrata.
	 *
	 * @param siacTPredoc the siac t predoc
	 * @param statoOperativoPreDocumento the stato operativo pre documento
	 */
	private void aggiornaStatoPreDocumento(SiacTPredoc siacTPredoc, StatoOperativoPreDocumento statoOperativoPreDocumento) {
		Date dataCancellazione = new Date();
		if(siacTPredoc.getSiacRPredocStatos()==null){
			siacTPredoc.setSiacRPredocStatos(new ArrayList<SiacRPredocStato>());
		}
		for(SiacRPredocStato r : siacTPredoc.getSiacRPredocStatos()){
			r.setDataCancellazioneIfNotSet(dataCancellazione);
		}
		Date now = new Date();
		SiacRPredocStato siacRPredocStato = new SiacRPredocStato();
		SiacDPredocStato siacDPredocStato = eef.getEntity(SiacDPredocStatoEnum.byStatoOperativo(statoOperativoPreDocumento), siacTPredoc.getSiacTEnteProprietario().getUid());
		siacRPredocStato.setSiacDPredocStato(siacDPredocStato);
		siacRPredocStato.setSiacTPredoc(siacTPredoc);
		siacRPredocStato.setSiacTEnteProprietario(siacTPredoc.getSiacTEnteProprietario());
		siacRPredocStato.setDataInizioValidita(now);
		siacRPredocStato.setDataCreazione(now);
		siacRPredocStato.setDataModifica(now);
		siacRPredocStato.setLoginOperazione(loginOperazione);
		
		siacTPredoc.addSiacRPredocStato(siacRPredocStato);
		//siacRPredocStatoRepository.save(siacRPredocStato);
	}
	
	/**
	 * Find documenti collegati by id pre documento.
	 *
	 * @param uid the uid
	 * @return the pre documento entrata
	 */
	public abstract P findDocumentiCollegatiByIdPreDocumento(Integer uid);
	
	/**
	 * Ottiene il numero di un nuovo predocumento.preDoc.getStatoOperativoPreDocumento().getCodice()
	 * 
	 * @return numero predocumento
	 */
	// SIAC-5267: il numero del predocumento non e' piu' richiesto come progressivo senza buchi
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Integer staccaNumeroPreDocumento() {
		final String methodName = "staccaNumeroPreDocumento";
		
		SiacTPredocNum siacTPredocNum = siacTPredocNumRepository.findByEnteProprietario(ente.getUid());
		
		Date now = new Date();
		if(siacTPredocNum == null) {
			siacTPredocNum = new SiacTPredocNum();
//			SiacTPredoc siacTPredoc = new SiacTPredoc();
//			siacTPredoc.setPredocId(0);			
//			siacTPredocNum.setSiacTPredoc(siacTPredoc);
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTPredocNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTPredocNum.setDataCreazione(now);
			siacTPredocNum.setDataInizioValidita(now);
			siacTPredocNum.setLoginOperazione(loginOperazione);
			siacTPredocNum.setPredocNumero(1); //La numerazione parte da 1
		}
		siacTPredocNum.setDataModifica(now);
		
		siacTPredocNumRepository.saveAndFlush(siacTPredocNum);
		
		Integer numeroSubdocumento = siacTPredocNum.getPredocNumero();
		log.info(methodName, "returning numeroPreDocumento: "+ numeroSubdocumento);
		return numeroSubdocumento;
	}

	public BigDecimal findImportoPreDocumentoById(Integer uid) {
		final String methodName = "findImportoPreDocumentoEntrataById";	
		log.debug(methodName, "uid: "+ uid);
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uid);
		if(siacTPredoc == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il PreDocumentoEntrata con id: " + uid);
			return null;
		}
		
		log.debug(methodName, "Returning "+siacTPredoc.getPredocImporto()+". siacTPredoc trovato con uid: " + siacTPredoc.getUid());
		return siacTPredoc.getPredocImporto();
	}
	
	public BigDecimal findImportoPreDocumentoByElencoIdAndStatiOperativi(Integer uidElenco, Integer uidImpegno, Integer uidSubImpegno, StatoOperativoPreDocumento... statiOperativi) {
		final String methodName = "findImportoPreDocumentoByElencoIdAndStatiOperativi";	
		log.debug(methodName, "uid elenco: " + uidElenco + " stati operativi: " + Arrays.toString(statiOperativi));
		
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		Map<String, BigDecimal> importi = preDocumentoDao.findImportiByEldocIdAndPredocStato(uidElenco, uidImpegno, uidSubImpegno, predocStatoCodes);
		
		BigDecimal result = BigDecimal.ZERO;
		for(BigDecimal importo : importi.values()) {
			result = result.add(importo);
		}
		return result;
	}
	
	public Map<StatoOperativoPreDocumento, BigDecimal> findImportoPreDocumentoByElencoIdAndStatiOperativiGroupByStatoOperativo(Integer uidElenco, Integer uidImpegno, Integer uidSubImpegno, StatoOperativoPreDocumento... statiOperativi) {
		final String methodName = "findImportoPreDocumentoByElencoIdAndStatiOperativiGroupByStatoOperativo";
		log.debug(methodName, "uid elenco: " + uidElenco + " stati operativi: " + Arrays.toString(statiOperativi));
		
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		Map<String, BigDecimal> importi = preDocumentoDao.findImportiByEldocIdAndPredocStato(uidElenco, uidImpegno, uidSubImpegno, predocStatoCodes);
		
		Map<StatoOperativoPreDocumento, BigDecimal> result = new HashMap<StatoOperativoPreDocumento, BigDecimal>();
		for(StatoOperativoPreDocumento sopd : StatoOperativoPreDocumento.values()) {
			BigDecimal importo = importi.get(sopd.getCodice());
			result.put(sopd, importo != null ? importo : BigDecimal.ZERO);
		}
		return result;
	}
	
	public Long countPreDocumentoByElencoIdAndStatiOperativi(Integer uidElenco, Integer uidImpegno, Integer uidSubImpegno, StatoOperativoPreDocumento... statiOperativi) {
		final String methodName = "countPreDocumentoByElencoIdAndStatiOperativi";
		log.debug(methodName, "uid elenco: " + uidElenco + " stati operativi: " + Arrays.toString(statiOperativi));
		
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		Map<String, Long> importi = preDocumentoDao.countByEldocIdAndPredocStato(uidElenco, uidImpegno, uidSubImpegno, predocStatoCodes);
		
		long result = Long.valueOf(0);
		for(Long importo : importi.values()) {
			if(importo != null) {
				result += importo.longValue();
			}
		}
		return Long.valueOf(result);
	}
	
	public Map<StatoOperativoPreDocumento, Long> countPreDocumentoByElencoIdAndStatiOperativiGroupByStatoOperativo(Integer uidElenco, Integer uidImpegno, Integer uidSubImpegno, StatoOperativoPreDocumento... statiOperativi) {
		final String methodName = "findImportoPreDocumentoByElencoIdAndStatiOperativi";	
		log.debug(methodName, "uid elenco: " + uidElenco + " stati operativi: " + Arrays.toString(statiOperativi));
		
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		Map<String, Long> importi = preDocumentoDao.countByEldocIdAndPredocStato(uidElenco, uidImpegno, uidSubImpegno, predocStatoCodes);
		
		Map<StatoOperativoPreDocumento, Long> result = new HashMap<StatoOperativoPreDocumento, Long>();
		for(StatoOperativoPreDocumento sopd : StatoOperativoPreDocumento.values()) {
			Long importo = importi.get(sopd.getCodice());
			result.put(sopd, importo != null ? importo : Long.valueOf(0));
		}
		return result;
	}

	public void aggiornaDataTrasmissione(P preDoc, Date dataTrasmissione) {
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(preDoc.getUid());
		siacTPredoc.setPredocDataTrasmissione(dataTrasmissione);
	}

	protected List<String> toPredocStatoCodes(Iterable<StatoOperativoPreDocumento> statiOperativi) {
		List<String> predocStatoCodes = new ArrayList<String>();
		for(StatoOperativoPreDocumento sopd : statiOperativi) {
			predocStatoCodes.add(sopd.getCodice());
		}
		return predocStatoCodes;
	}
	
	protected List<String> toPredocStatoCodes(StatoOperativoPreDocumento[] statiOperativi) {
		List<String> predocStatoCodes = new ArrayList<String>();
		for(StatoOperativoPreDocumento sopd : statiOperativi) {
			predocStatoCodes.add(sopd.getCodice());
		}
		return predocStatoCodes;
	}
	
	//SIAC-6780
	/**
	 * Checks for documenti collegati.
	 *
	 * @param predocumento the predocumento
	 * @return true, if successful
	 */
	public boolean hasDocumentiCollegati(PreDocumento predocumento) {
		if(predocumento == null) {
			return false;
		}
		
		List<SiacTSubdoc> findSubdocCollegatiAPredoc = siacTPredocRepository.findSubdocCollegatiAPredoc(ente.getUid(), predocumento.getUid());
		return findSubdocCollegatiAPredoc != null && !findSubdocCollegatiAPredoc.isEmpty();
	}
	
	
}
