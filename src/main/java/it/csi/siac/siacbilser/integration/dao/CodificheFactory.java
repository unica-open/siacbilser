/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.enumeration.CodificheEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Codifica;


/**
 * Componente per il reperimento su database delle codifiche configurate per un Ente.
 *   
 * @author Domenico Lisi
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(propagation=Propagation.SUPPORTS)
public class CodificheFactory extends BaseDadImpl {
	
	/** The log. */
	private static LogUtil log = new LogUtil(CodificheFactory.class);
	
	/** The entity manager. */
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public <C extends Codifica> List<C> ricercaCodifiche(Class<C> codificaClass, Integer enteProprietarioId) {
		CodificheEnum ce = CodificheEnum.byModelClass(codificaClass);
		return ricercaCodifiche(ce, enteProprietarioId);
	}
	
	public <C extends Codifica> List<C> ricercaCodifiche(String codificaName, Integer enteProprietarioId) {
		CodificheEnum ce = CodificheEnum.byCodificaName(codificaName);
		return ricercaCodifiche(ce, enteProprietarioId);
	}
	
	public <C extends Codifica> List<C> ricercaCodifiche(Class<C> codificaClass, String codificaName, Integer enteProprietarioId) {
		CodificheEnum ce = CodificheEnum.byModelClassAndCodificaName(codificaClass, codificaName);
		return ricercaCodifiche(ce, enteProprietarioId);
	}
	
	


	
	/**
	 * Trova tutte le codifiche di un tipo per l'ente passato come parametro.
	 * 
	 * @param codificaClass
	 * @param enteProprietarioId
	 * @return lista delle codifiche
	 */
	@SuppressWarnings("unchecked")
	private <C extends Codifica> List<C> ricercaCodifiche(CodificheEnum ce, Integer enteProprietarioId) {
		final String methodName = "ricercaCodifiche";
		
		String jpql = " FROM "+ce.getEntityClass().getSimpleName()
				+ " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
				+ " AND dataCancellazione IS NULL "
				+ " AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) "
				+ " " + ce.getJpqlConditions()
				+ " " + ce.getJpqlOrderBy();
		
		log.debug(methodName, "jpql generato per "+ce+": "+jpql);

		TypedQuery<SiacTBase> query = entityManager.createQuery(jpql, SiacTBase.class);
		
		query.setParameter("enteProprietarioId", enteProprietarioId);
		
		List<SiacTBase> queryResult = query.getResultList();
		log.debug(methodName, "trovate "+ queryResult.size() +" codifiche per "+ ce.name() + " enteProprietarioId: "+enteProprietarioId);
		
		//Se e' stato definito un MapId lo utilizzo
		if(ce.getMapId()!=null){
			List<? extends Codifica> result = convertiLista(queryResult, ce.getModelClass(), ce.getMapId());
			return (List<C>) result;
		}
		
		//Altrimeni effettuo la conversione di base
		List<C> result = new ArrayList<C>();
		
		for(SiacTBase siacTBase : queryResult) {
			
			C codifica = mapToCodifica(ce, siacTBase);
			result.add(codifica);
		}
		
		return result;
		
	
	}
	
	
	
	public <C extends Codifica> C ricercaCodifica(Class<C> codificaClass, String code, Integer enteProprietarioId) {
		CodificheEnum ce = CodificheEnum.byModelClass(codificaClass);
		return ricercaCodifica(ce, code, enteProprietarioId);
	}
	
	public <C extends Codifica> C ricercaCodifica(String codificaName, String code, Integer enteProprietarioId) {
		CodificheEnum ce = CodificheEnum.byCodificaName(codificaName);
		return ricercaCodifica(ce, code, enteProprietarioId);
	}
	
	public <C extends Codifica> C ricercaCodifica(Class<C> codificaClass, String codificaName, String code, Integer enteProprietarioId) {
		CodificheEnum ce = CodificheEnum.byModelClassAndCodificaName(codificaClass, codificaName);
		return ricercaCodifica(ce, code, enteProprietarioId);
	}
	
	public <C extends Codifica> C ricercaCodifica(Class<C> codificaClass, Integer codificaId) {
		CodificheEnum ce = CodificheEnum.byModelClass(codificaClass);
		return ricercaCodifica(ce, codificaId);
	}
	
	/**
	 * Trova la codifica di un tipo per il codice e l'ente passato come parametro.
	 *
	 * @param <C> the generic type
	 * @param codificaClass the codifica class
	 * @param code the code codice da ricercare.
	 * @param enteProprietarioId the ente proprietario id
	 * 
	 * @return lista delle codifiche
	 * @throws IllegalStateException se la codifica non esiste su DB oppure il codice non &egrave; univoco.
	 * 
	 */
	private <C extends Codifica> C ricercaCodifica(CodificheEnum ce, String code, Integer enteProprietarioId) {
		if(StringUtils.isBlank(ce.getCodeColumnName())) {
			throw new IllegalArgumentException("La codifica CodificheEnum." + ce.name() + " non supporta la ricerca puntuale. E' necessario definire un valore per codeColumnName.");
		}
		
		String jpql = " FROM " + ce.getEntityClass().getSimpleName()
				+ " WHERE dataCancellazione IS NULL "
				+ " AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) "
				+ " AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
				+ " AND " + ce.getCodeColumnName() + " = :code" 
				+ " " + ce.getJpqlConditions();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("enteProprietarioId", enteProprietarioId);
		params.put("code", code);
		
		String debugStr = ce.getCodeColumnName() + "='" + code + "' dell'ente con id: " + enteProprietarioId;
		
		return findCodifica(ce, jpql, params, debugStr);
	}

	private <C extends Codifica> C ricercaCodifica(CodificheEnum ce, Integer codificaId) {
		if(StringUtils.isBlank(ce.getIdColumnName())) {
			throw new IllegalArgumentException("La codifica CodificheEnum." + ce.name() + " non supporta la ricerca per uid. E' necessario definire una colonna di tipo @Id.");
		}
		
		String jpql = " FROM " + ce.getEntityClass().getSimpleName()
				+ " WHERE dataCancellazione IS NULL "
				+ " AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) "
				+ " AND " + ce.getIdColumnName() + " = :id";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", codificaId);
		
		String debugStr = ce.getIdColumnName() + "=" + codificaId;
		
		return findCodifica(ce, jpql, params, debugStr);
	}
	
	@SuppressWarnings("unchecked")
	private <C extends Codifica> C findCodifica(CodificheEnum ce, String jpql, Map<String, Object> params, String debugStr) {
		final String methodName = "findCodifica";
		log.debug(methodName, "jpql generato per " + ce + ": " + jpql);
		
		TypedQuery<SiacTBase> query = entityManager.createQuery(jpql, SiacTBase.class);
		for(Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		SiacTBase queryResult;
		try{
			queryResult = query.getSingleResult();
		} catch (NoResultException nre){
			String msg = "Impossibile trovare un mapping su " + ce.getEntityClass().getSimpleName() + " per " + debugStr; //+ce.getIdColumnName()+"="+codificaId; 
			log.warn(methodName, msg , nre);
			throw new IllegalStateException(msg, nre);
		} catch (NonUniqueResultException nure){
			String msg = "Definito più di un mapping mapping su " + ce.getEntityClass().getSimpleName() + " per " + debugStr; //+ce.getIdColumnName()+"="+codificaId; 
			log.warn(methodName, msg , nure);
			throw new IllegalStateException(msg, nure);
		}
		
		log.debug(methodName, "trovata codifica per "+ce.getEntityClass().getSimpleName() + " per " + debugStr);
		
		//Se e' stato definito un MapId lo utilizzo
		if(ce.getMapId()!=null){
			return (C) map(queryResult, ce.getModelClass(), ce.getMapId());
		}
		
		//Altrimeni effettuo la conversione di base
		C codifica = mapToCodifica(ce, queryResult);
		
		return codifica;
	}


	private <C extends Codifica> C mapToCodifica(CodificheEnum ce, SiacTBase siacTBase) {
		BeanWrapper siacTBaseBeanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(siacTBase) ;
		String codice = (String)siacTBaseBeanWrapper.getPropertyValue(ce.getCodeColumnName());
		String descrizione = (String) siacTBaseBeanWrapper.getPropertyValue(ce.getDescColumnName());
		
		C codifica = ce.newModelInstance();
		
		codifica.setUid(siacTBase.getUid());
		codifica.setCodice(codice);
		codifica.setDescrizione(descrizione);
		
		codifica.setDataCreazione(siacTBase.getDataCreazione());
		codifica.setDataInizioValidita(siacTBase.getDataInizioValidita());
		codifica.setDataModifica(siacTBase.getDataModifica());
		codifica.setLoginOperazione(siacTBase.getLoginOperazione());
		
		return codifica;
		
	}
	
	
	
	
	
	
	
	
	

}
