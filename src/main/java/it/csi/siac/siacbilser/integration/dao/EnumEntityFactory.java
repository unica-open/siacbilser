/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.enumeration.EnumEntity;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;


/**
 * Componente per il reperimento su database dell'id di tutte le entity <code>SiacD*</code> che rimappano un codice con una descrizione per ogni ente.
 * A partire dall'id dell'ente e dal codice riportato nell'enum annotato con &#64;EnumEntity viene resituito l'id corrispondente.
 *   
 * @author Domenico Lisi
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(propagation=Propagation.SUPPORTS)
public class EnumEntityFactory {
	
	/** Lock for synchronization */
	private static final Object LOCK = new Object();
	/** The log. */
	private static LogSrvUtil log = new LogSrvUtil(EnumEntityFactory.class);
	
	/** The entity manager. */
	@PersistenceContext
	protected EntityManager entityManager;
	
	/**
	 * Cache incrementale degli id.
	 * Ogni volta che viene richiesto un id viene messo in cache per evitare di doverlo reperire di nuovo dal database.
	 */
	private Map<String,Integer> idsCache =  new ConcurrentHashMap<String,Integer>(); //non necessario static in quanto è un Component singleton
	
	
	
	/**
	 * Restituisce l'id associato ad un entity annotato con EnumEntity.
	 *
	 * @param enumEntity the enum entity
	 * @param enteProprietarioId the ente proprietario id
	 * @return the id
	 */
	public Integer getId(Enum<?> enumEntity, Integer enteProprietarioId){	
		EnumEntity annotation = getEnumEntityAnnotation(enumEntity);						
		String codice = invokeGetCodiceMethod(enumEntity);		
		return getIdCached(annotation.entityName(), annotation.idPropertyName(), annotation.codePropertyName(), codice, enteProprietarioId);	
	}


	/**
	 * Gets the enum entity annotation.
	 *
	 * @param enumEntity the enum entity
	 * @return the enum entity annotation
	 */
	private EnumEntity getEnumEntityAnnotation(Enum<?> enumEntity) {
		EnumEntity annotation = enumEntity.getClass().getAnnotation(EnumEntity.class);
		if(annotation==null){
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ " must be annotated with @EnumEntity!");
		}
		return annotation;
	}

	/**
	 * Invoca il metodo <code>getCodice</code> dell'enum passato come parametro e restituisce il suo risultato.
	 *
	 * @param enumEntity the enum entity
	 * @return the string
	 */
	private String invokeGetCodiceMethod(Enum<?> enumEntity) {
		
		Method getCodice = getGetCodiceMethod(enumEntity);
		
		String codice;
		try {
			codice = (String) getCodice.invoke(enumEntity);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ ": IllegalArgumentException invoking " +getCodice.getName()+" method.  It must be with no argument and return type String.",e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ ": IllegalArgumentException invoking " +getCodice.getName()+" method.  It must be with no argument and return type String.",e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ ": InvocationTargetException invoking " +getCodice.getName()+" method.",e.getTargetException());
		}
		return codice;
	}

	/**
	 * Ottiene il Method <code>getCodice</code> dell'enum passato come parametro.
	 *
	 * @param enumEntity the enum entity
	 * @return the gets the codice method
	 */
	private Method getGetCodiceMethod(Enum<?> enumEntity) {
		Method getCodice;
		try {
			getCodice = enumEntity.getClass().getMethod("getCodice");
		} catch (SecurityException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ " must define an accessible getCodice() method with no argument and return type String.",e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ " must define a getCodice() method with no argument and return type String.",e);
		}
		return getCodice;
	}
	
	
	/**
	 * Restituisce una istanza di entityClass valorizzata con l'id ed il codice.
	 *
	 * @param <T> the generic type
	 * @param enumEntity the enum entity
	 * @param enteProprietarioId the ente proprietario id
	 * @param entityObj the entity obj
	 * @return the entity
	 */
	public<T> T getEntity(Enum<?> enumEntity, Integer enteProprietarioId, T entityObj) {
		EnumEntity annotation = getEnumEntityAnnotation(enumEntity);
		try {	
			
			//reperisco e setto il codice
			String codice = invokeGetCodiceMethod(enumEntity);			
			entityObj.getClass().getMethod("set"+StringUtils.capitalize(annotation.codePropertyName()), String.class).invoke(entityObj, codice);
			
			//reperisco e setto l'id
			Integer id = getIdCached(annotation.entityName(), annotation.idPropertyName(), codePropertyNameJpql(annotation), codice, enteProprietarioId);			
			entityObj.getClass().getMethod("set"+StringUtils.capitalize(annotation.idPropertyName()), Integer.class).invoke(entityObj, id);
		
			return entityObj;	

		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ "",e);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ "",e);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ "",e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ " "+ e.getMessage(), e.getTargetException());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ "",e);
		}

	}


	private String codePropertyNameJpql(EnumEntity annotation) {
		return "".equals(annotation.codePropertyNameJpql())? annotation.codePropertyName() : annotation.codePropertyNameJpql();
	}


	
	
	/**
	 * Gets the entity.
	 *
	 * @param <T> the generic type
	 * @param enumEntity the enum entity
	 * @param enteProprietarioId the ente proprietario id
	 * @param entityClass the entity class
	 * @return the entity
	 */
	public<T> T getEntity(Enum<?> enumEntity, Integer enteProprietarioId, Class<T> entityClass) {		
		T entityObj;
		try {	
			entityObj = entityClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ ": Entity class "+entityClass.getSimpleName()+" must declare an empty constructor.",e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ ": Entity class "+entityClass.getSimpleName()+" constructor must be accessible.",e);
		}
		return  getEntity(enumEntity, enteProprietarioId, entityObj);
	}
	
	/**
	 * Restituisce una istanza di entityClass valorizzata con l'id ed il codice.
	 *
	 * @param <T> the generic type
	 * @param enumEntity the enum entity
	 * @param enteProprietarioId the ente proprietario id
	 * @return the entity
	 */
	@SuppressWarnings("unchecked")
	public <T> T getEntity(Enum<?> enumEntity, Integer enteProprietarioId) {
		Class<?> entityClass = getEntityClass(enumEntity);		
		return (T)getEntity(enumEntity, enteProprietarioId, entityClass);

	}


	/**
	 * Restituisce una istanza di entityClass valorizzata con l'id ed il codice.
	 *
	 * @param enumEntity the enum entity
	 * @return the entity class
	 */
	private Class<?> getEntityClass(Enum<?> enumEntity) {
		EnumEntity annotation = getEnumEntityAnnotation(enumEntity);
		try {
			return Class.forName("it.csi.siac.siacbilser.integration.entity."+annotation.entityName());			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(enumEntity.getClass().getSimpleName()+ ": Entity class "+annotation.entityName()+" not found under package: it.csi.siac.siacbilser.integration.entity",e);		
		}
	}
	
	
	
	/**
	 * Ottiene l'id dalla cache se presente. Altrimenti richiama getId() per reperirlo da Database.
	 *
	 * @param entityName the entity name
	 * @param idPropertyName the id property name
	 * @param codePropertyName the code property name
	 * @param code the code
	 * @param enteProprietarioId the ente proprietario id
	 * @return the id cached
	 */
	private Integer getIdCached(String entityName, String idPropertyName, String codePropertyName, String code, Integer enteProprietarioId) {
		final String methodName = "getIdCached";
		boolean logIsCached = true;
		String key = entityName+"_"+idPropertyName+"_"+codePropertyName+"_"+code+"_"+enteProprietarioId;		
		Integer value = idsCache.get(key);		
		if(value==null) {
			synchronized(LOCK) {
				value = idsCache.get(key);
				if(value == null) {
					value = getId(entityName, idPropertyName, codePropertyName, code, enteProprietarioId);
					idsCache.put(key,value);
					logIsCached = false;
				}
			}
		}
		log.debug(methodName, "returning "+(logIsCached?"cached ":"")+"value: "+ value + " for "+entityName+"."+code + " (enteProprietarioId: "+enteProprietarioId+")");
		return value;		
		
	}
	
	/**
	 * Reperisce l'id dal database filtrando sull'entity entityName la property con codice codePropertyName ed ente uguale ad enteProprietarioId.
	 *
	 * @param entityName the entity name
	 * @param idPropertyName the id property name
	 * @param codePropertyName the code property name
	 * @param code the code
	 * @param enteProprietarioId the ente proprietario id
	 * @return null se l'entita non è mappata su db
	 */
	private Integer getId(String entityName, String idPropertyName, String codePropertyName, String code, Integer enteProprietarioId) {
		final String methodName = "getId";
		TypedQuery<Integer> createNamedQuery = entityManager.createQuery("SELECT "+idPropertyName+" FROM "+entityName+" WHERE "+codePropertyName+" = :code AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ", Integer.class);
				
		createNamedQuery.setParameter("code", code);
		createNamedQuery.setParameter("enteProprietarioId", enteProprietarioId);		
		
		try{
			return createNamedQuery.getSingleResult();
		} catch (NoResultException nre){
			String msg = "Impossibile trovare un mapping su "+entityName+" per "+codePropertyName+"='"+code + "' dell'ente con id: "+enteProprietarioId+""; 
			log.warn(methodName, msg , nre);
			throw new IllegalStateException(msg, nre);
		} catch (NonUniqueResultException nure){
			String msg = "Definito più di un mapping mapping su "+entityName+" per "+codePropertyName+"='"+code + "' dell'ente con id: "+enteProprietarioId+""; 
			log.warn(methodName, msg , nure);
			throw new IllegalStateException(msg, nure);
		}
	}
	
	
	
	
	
	
	
	
	

}
