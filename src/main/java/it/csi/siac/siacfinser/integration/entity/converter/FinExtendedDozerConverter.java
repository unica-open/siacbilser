/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;


import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siaccommonser.util.dozer.MapId;


/**
 * Questa classe base rende possibile l'accesso ad un qualunque converter all'interno dell'implementazione di un converter.
 * 
 * @author 
 *
 * @param <A>
 * @param <B>
 */
public abstract class FinExtendedDozerConverter<A, B> extends DozerConverter<A, B> {
	
	
	private static Mapper mapper;

	@Autowired
	protected ApplicationContext appCtx;	


	protected FinExtendedDozerConverter(Class<A> prototypeA, Class<B> prototypeB) {
		super(prototypeA, prototypeB);
	}
	
	
	
	protected Mapper getMapper(){
		if(mapper == null){
			mapper = appCtx.getBean(Mapper.class);
		}
		return mapper;
	}

	
	
	protected <T> T map(Object source, Class<T> clazz) {
		return getMapper().map(source, clazz);
	}
	
	protected <T> T mapNotNull(Object source, Class<T> clazz) {
		if(source==null){
			return null;
		}
		return map(source, clazz);
	}
	
	protected void map(Object source, Object dest) {
		getMapper().map(source, dest);
	}
	
	protected void mapNotNull(Object source, Object dest) {
		if(source!=null){
			mapNotNull(source, dest);
		}
		
	}
	
	
	protected <T> T map(Object source, Class<T> clazz,  MapId  mapId) {
		String mapIdStr = mapId != null ? mapId.name() : null;
		return getMapper().map(source, clazz, mapIdStr);
	}
	
	protected <T> T mapNotNull(Object source, Class<T> clazz,  MapId  mapId) {
		if(source == null){
			return null;
		}
		return map(source, clazz, mapId);
	}
	
	protected void  map(Object source, Object dest,  MapId  mapId) {
		String mapIdStr = mapId != null ? mapId.name() : null;
		getMapper().map(source, dest, mapIdStr);
	}
	
	protected void  mapNotNull(Object source, Object dest,  MapId  mapId) {
		if(source !=null){
			map(source, dest, mapId);
		}
	}

}
