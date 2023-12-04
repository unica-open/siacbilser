/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siacbilser.integration.utility.DozerMapHelper;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccommonser.integration.entitymapping.Converter;
import it.csi.siac.siaccommonser.util.dozer.MapId;

/**
 * Questa classe base rende possibile l'accesso ad un qualunque converter all'interno dell'implementazione di un converter.
 *
 * @author Domenico
 * @param <A> the generic type
 * @param <B> the generic type
 */
public abstract class ExtendedDozerConverter<A, B> extends DozerConverter<A, B> {
	
	private static final Integer INTEGER_ZERO = Integer.valueOf(0);
	/** The mapper. */
	private static Mapper mapper;
	
	protected final LogSrvUtil log = new LogSrvUtil(getClass());

	/** The app ctx. */
	@Autowired
	protected ApplicationContext appCtx;	


	/**
	 * Instantiates a new extended dozer converter.
	 *
	 * @param prototypeA the prototype a
	 * @param prototypeB the prototype b
	 */
	protected ExtendedDozerConverter(Class<A> prototypeA, Class<B> prototypeB) {
		super(prototypeA, prototypeB);
	}
	
	
	
	/**
	 * Gets the mapper.
	 *
	 * @return the mapper
	 */
	protected Mapper getMapper(){
		if(mapper == null){
			mapper = appCtx.getBean(Mapper.class);
		}
		return mapper;
	}

	
	
	/**
	 * Map.
	 *
	 * @param <T> the generic type
	 * @param source the source
	 * @param clazz the clazz
	 * @return the t
	 */
	protected <T> T map(Object source, Class<T> clazz) {
		return getMapper().map(source, clazz);
	}
	
	/**
	 * Map not null.
	 *
	 * @param <T> the generic type
	 * @param source the source
	 * @param clazz the clazz
	 * @return the t
	 */
	protected <T> T mapNotNull(Object source, Class<T> clazz) {
		if(source==null){
			return null;
		}
		return map(source, clazz);
	}
	
	/**
	 * Map.
	 *
	 * @param source the source
	 * @param dest the dest
	 */
	protected void map(Object source, Object dest) {
		getMapper().map(source, dest);
	}
	
	/**
	 * Map not null.
	 *
	 * @param source the source
	 * @param dest the dest
	 */
	protected void mapNotNull(Object source, Object dest) {
		if(source!=null){
			map(source, dest);
		}
		
	}
	
	
	/**
	 * Map.
	 *
	 * @param <T> the generic type
	 * @param source the source
	 * @param clazz the clazz
	 * @param mapId the map id
	 * @return the t
	 */
	protected <T> T map(Object source, Class<T> clazz,  MapId  mapId) {
		String mapIdStr = mapId != null ? mapId.name() : null;
		return getMapper().map(source, clazz, mapIdStr);
	}
	
	/**
	 * Map not null.
	 *
	 * @param <T> the generic type
	 * @param source the source
	 * @param clazz the clazz
	 * @param mapId the map id
	 * @return the t
	 */
	protected <T> T mapNotNull(Object source, Class<T> clazz,  MapId  mapId) {
		if(source == null){
			return null;
		}
		return map(source, clazz, mapId);
	}
	
	/**
	 * Map.
	 *
	 * @param source the source
	 * @param dest the dest
	 * @param mapId the map id
	 */
	protected void  map(Object source, Object dest,  MapId  mapId) {
		String mapIdStr = mapId != null ? mapId.name() : null;
		getMapper().map(source, dest, mapIdStr);
	}
	
	/**
	 * Map not null.
	 *
	 * @param source the source
	 * @param dest the dest
	 * @param mapId the map id
	 */
	protected void  mapNotNull(Object source, Object dest,  MapId  mapId) {
		if(source !=null){
			map(source, dest, mapId);
		}
	}
	
	protected void mapNotNull(Object source, Object dest, MapId mapId, Converter... converters) {
		DozerMapHelper.mapNotNull(getMapper(), appCtx, source, dest, mapId, converters);
	}
	protected <T> T mapNotNull(Object source, Class<T> clazz, MapId mapId, Converter... converters) {
		return DozerMapHelper.mapNotNull(getMapper(), appCtx, source, clazz, mapId, converters);
	}
	
	
	
	
	/**
	 * map to string.
	 * 
	 * @param i
	 * @return
	 */
	protected String mapToString(Integer i) {
		return mapToString(i, null);
	}

	/**
	 * map to string.
	 * 
	 * @param i
	 * @param nullValue
	 * @return
	 */
	protected String mapToString(Integer i, String nullValue) {
		if (i == null) {
			return nullValue;
		}

		return i.toString();
	}

	protected <T extends SiacTBase> T sanifyEntity(T entity) {
		if(INTEGER_ZERO.equals(entity.getUid())) {
			entity.setUid(null);
		}
		return entity;
	}
	
	protected <T> T ifNotNull(T t, T defaultValue) {
		return t == null ? defaultValue : t;
	}
}
