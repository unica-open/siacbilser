/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dao.MovimentoGestioneDao;
import it.csi.siac.siacbilser.integration.dao.SiacTModificaBilRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestBilRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;


public abstract class MovimentoGestioneBilDad<MG extends MovimentoGestione> extends ExtendedBaseDadImpl  {
	
	/** The siac t movgest t repository. */
	@Autowired
	protected SiacTMovgestTRepository siacTMovgestTRepository;
	
	@Autowired
	protected SiacTMovgestBilRepository siacTMovgestBilRepository;
	
	@Autowired
	protected SiacTModificaBilRepository siacTModificaBilRepository;
	
	
	@Autowired
	protected MovimentoGestioneDao movimentoGestioneDao;
	
	
	
	
	/**
	 * Ottiene la testata a partire dall'uid del movimento
	 * @param uid l'uid del movimento
	 * @return la testata
	 */
	protected SiacTMovgestT findTestataByUidMovimento(Integer uid) {
		List<SiacTMovgestT> siacTMovgestTs = siacTMovgestTRepository.findSiacTMovgestTestataBySiacTMovgestId(uid);
		return siacTMovgestTs != null && !siacTMovgestTs.isEmpty() ? siacTMovgestTs.get(0) : null;
	}
	
	/**
	 * Ottiene l'uid della testata a partire dall'uid del movimento
	 * @param uid
	 * @return
	 */
	public Integer findUidTestataByUidMovimento(int uid) {
		SiacTMovgestT siacTMovgestT = findTestataByUidMovimento(uid);
		return siacTMovgestT != null ? siacTMovgestT.getUid() : null;
	}
	
	/**
	 * Ottiene l'uid del movimento a partire da bilancio, anno e numero
	 */
	public Integer findUidMovgestByAnnoNumeroBilancio(Integer anno, BigDecimal numero, Class<?> baseClass, Integer idBilancio) {
		String tipo = extractMovgestTipo(baseClass);
		List<Integer> uids = siacTMovgestBilRepository.findUidMovgestByAnnoNumeroBilancio(anno, numero, tipo, idBilancio);
		return returnOnlyOne(uids, "Sono presenti piu' movimenti di gestione per chiave " + tipo + "/" + anno + "/" + numero + " per il bilancio " + idBilancio);
	}
	
	/**
	 * Ottiene l'uid del movimento a partire da bilancio, anno e numero
	 */
	public Integer findUidMovgestTsByAnnoNumeroBilancio(Integer anno, BigDecimal numero, String numeroSub, Class<?> baseClass, Integer idBilancio) {
		String tipo = extractMovgestTipo(baseClass);
		List<Integer> uids = siacTMovgestTRepository.findUidMovgestTsByAnnoNumeroBilancio(anno, numero, numeroSub, tipo, idBilancio);
		return returnOnlyOne(uids, "Sono presenti piu' submovimenti di gestione per chiave " + tipo + "/" + anno + "/" + numero + "/" + numeroSub + " per il bilancio " + idBilancio);
	}
	
	/**
	 * Estrazione del tipo di movimento di gestione
	 * @param baseClass la classe di base
	 * @return il tipo di movimento di gestione
	 * @throws IllegalArgumentException nel caso in cui la classe sia null
	 */
	private String extractMovgestTipo(Class<?> baseClass) {
		if(baseClass == null) {
			throw new IllegalArgumentException("La classe base del movimento di gestione deve essere fornita");
		}
		return Impegno.class.isAssignableFrom(baseClass) ? "I" : "A";
	}
	
	/**
	 * Ritorna l'unico elemento della lista se presente
	 * @param list la lista
	 * @param errorMsg l'errore da inviare nel caso vi sia pi&ugrave; di un elemento
	 * @return l'unico elemento della lista
	 * @throws IllegalStateException nel caso in cui la lista abbia pi&ugrave; di un elemento
	 */
	private <T> T returnOnlyOne(List<T> list, String errorMsg) {
		if(list.isEmpty()) {
			return null;
		}
		if(list.size() > 1) {
			throw new IllegalStateException(errorMsg);
		}
		return list.get(0);
	}
	
	/**
	 * Calcola la disponibilit&agrave; richiesta via l'invocazione della function specificata
	 * @param uid l'uid per cui calcolare la function
	 * @param functionName la function
	 */
	protected BigDecimal calcolaDisponibilita(Integer uid, String functionName) {
		return movimentoGestioneDao.calcolaDisponibilita(uid, functionName);
	}
	
	/**
	 * Ottiene una mappa degli importi per il movimento di gestione corrispondente all'uid
	 * @param uid l'uid del movimento
	 * @return gli importi, con chiave <code>A</code> per l'attuale e <code>I</code> per l'iniziale
	 */
	public Map<String, BigDecimal> ottieniImporti(Integer uid) {
		Map<String, BigDecimal> res = new HashMap<String, BigDecimal>();
		BigDecimal importoAttuale = siacTMovgestTRepository.findImportoByMovgestId(uid, SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
		BigDecimal importoIniziale = siacTMovgestTRepository.findImportoByMovgestId(uid, SiacDMovgestTsDetTipoEnum.Iniziale.getCodice());
		
		res.put(SiacDMovgestTsDetTipoEnum.Attuale.getCodice(), importoAttuale);
		res.put(SiacDMovgestTsDetTipoEnum.Iniziale.getCodice(), importoIniziale);
		return res;
	}
	
	/**
	 * Ottiene una mappa degli importi per il submovimento di gestione corrispondente all'uid
	 * @param uid l'uid del submovimento
	 * @return gli importi, con chiave <code>A</code> per l'attuale e <code>I</code> per l'iniziale
	 */
	public Map<String, BigDecimal> ottieniImportiSub(Integer uid) {
		Map<String, BigDecimal> res = new HashMap<String, BigDecimal>();
		BigDecimal importoAttuale = siacTMovgestTRepository.findImportoByMovgestTsId(uid, SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
		BigDecimal importoIniziale = siacTMovgestTRepository.findImportoByMovgestTsId(uid, SiacDMovgestTsDetTipoEnum.Iniziale.getCodice());
		
		res.put(SiacDMovgestTsDetTipoEnum.Attuale.getCodice(), importoAttuale);
		res.put(SiacDMovgestTsDetTipoEnum.Iniziale.getCodice(), importoIniziale);
		return res;
	}
	
	public ElementoPianoDeiConti ottieniPianoDeiContiByMovgestId(Integer uid) {
		final String methodName = "ottieniPianoDeiContiByMovgestId";
		
		List<SiacTClass> siacTClassPdcs = siacTMovgestTRepository.findSiacTClassByMovGestIdECodiciTipo(uid, codiciPdc());
		if(siacTClassPdcs == null || siacTClassPdcs.isEmpty()) {
			log.debug(methodName, "Nessun siacTClassPdc trovato.");
			return null;
		}
		SiacTClass siacTClassPdc = siacTClassPdcs.get(0);
		log.debug(methodName, "siacTClassPdc trovato: " + siacTClassPdc.getUid());
		return toPianoDeiConti(siacTClassPdc);
	}

	public ElementoPianoDeiConti ottieniPianoDeiContiBySubMovgestId(Integer uid) {
		final String methodName = "ottieniPianoDeiContiByMovgestId";
		
		List<SiacTClass> siacTClassPdcs = siacTMovgestTRepository.findSiacTClassByMovgestTsIdECodiciTipo(uid, codiciPdc());
		if(siacTClassPdcs == null || siacTClassPdcs.isEmpty()) {
			log.debug(methodName, "Nessun siacTClassPdc trovato.");
			return null;
		}
		SiacTClass siacTClassPdc = siacTClassPdcs.get(0);
		log.debug(methodName, "siacTClassPdc trovato: " + siacTClassPdc.getUid());
		return toPianoDeiConti(siacTClassPdc);
	}

	private Set<String> codiciPdc() {
		Set<String> pianoDeiConti = new HashSet<String>();
		pianoDeiConti.add(SiacDClassTipoEnum.PrimoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.SecondoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.TerzoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.QuartoLivelloPdc.getCodice());
		pianoDeiConti.add(SiacDClassTipoEnum.QuintoLivelloPdc.getCodice());
		return pianoDeiConti;
	}
	
	private ElementoPianoDeiConti toPianoDeiConti(SiacTClass siacTClassPdc) {
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();
		pdc.setUid(siacTClassPdc.getUid());
		pdc.setCodice(siacTClassPdc.getClassifCode());
		pdc.setDescrizione(siacTClassPdc.getClassifDesc());
		return pdc;
	}
	


}
