/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility;

import java.util.ArrayList;
import java.util.List;

import org.dozer.CustomConverter;
import org.dozer.Mapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converter;
import it.csi.siac.siaccommonser.util.dozer.MapId;

public class DozerMapHelper {
	
	/** Prevent instantiation */
	private DozerMapHelper() {
		// Prevent instantiation
	}

	private static String toMapIdString(MapId mapId) {
		return mapId != null ? mapId.name() : null;
	}
	private static CustomConverter instantiateConverter(ApplicationContext appCtx, Class<? extends CustomConverter> converterClass) {
		if(converterClass.getAnnotation(Component.class) != null){
			return appCtx.getBean(Utility.toDefaultBeanName(converterClass), converterClass);
		}

		try {
			return converterClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Impossibile istanziare il Converter "+ converterClass, e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Impossibile accedere al costruttore del Converter "+ converterClass, e);
		}

	}
	private static List<Class<? extends CustomConverter>> toCustomConverterClasses(Converter... converter) {
		List<Class<? extends CustomConverter>> result = new ArrayList<Class<? extends CustomConverter>>();
		for (Converter md : converter) {
			Class<? extends CustomConverter> conv = md.getCustomConverterClass();
			result.add(conv);
		}
		return result;
	}

	// Metodi esposti

	public static <T> T map(Mapper mapper, Object source, Class<T> clazz, MapId mapId) {
		return mapper.map(source, clazz, toMapIdString(mapId));
	}
	public static void map(Mapper mapper, Object source, Object dest, MapId mapId) {
		mapper.map(source, dest, toMapIdString(mapId));
	}
	public static <T> T map(Mapper mapper, ApplicationContext appCtx, Object source, Class<T> clazz, MapId mapId, Class<? extends CustomConverter>... converterClasses) {
		T dest = map(mapper, source, clazz, mapId);
		return (T) applyConverters(appCtx, source, dest, converterClasses);
	}
	public static <T> T map(Mapper mapper, ApplicationContext appCtx, Object source, T dest, MapId mapId, Class<? extends CustomConverter>... converterClasses) {
		map(mapper, source, dest, mapId);
		return applyConverters(appCtx, source, dest, converterClasses);
	}
	public static <T> T map(Mapper mapper, ApplicationContext appCtx, Object source, T dest, MapId mapId, Converter... converter) {
		map(mapper, source, dest, mapId);
		return applyConverters(appCtx, source, dest, converter);
	}
	public static <T> T map(Mapper mapper, ApplicationContext appCtx, Object source, Class<T> clazz, MapId mapId, Converter... converter) {
		T dest = map(mapper, source, clazz, mapId);
		return applyConverters(appCtx, source, dest, converter);
	}
	public static <T> T mapNotNull(Mapper mapper, Object source, Class<T> clazz, MapId mapId) {
		if (source == null) {
			return null;
		}
		return map(mapper, source, clazz, mapId);
	}
	public static void mapNotNull(Mapper mapper, Object source, Object dest, MapId mapId) {
		if (source != null) {
			map(mapper, source, dest, mapId);
		}
	}
	public static <A, DA> List<A> convertiLista(Mapper mapper, List<DA> listDa, Class<A> classA, MapId mapId) {
		if (listDa == null) {
			return null;
		}

		List<A> listA = new ArrayList<A>();
		for (DA tuplaDa : listDa) {
			A mapped = map(mapper, tuplaDa, classA, mapId);
			listA.add(mapped);
		}

		return listA;
	}
	public static <T> T mapNotNull(Mapper mapper, ApplicationContext appCtx, Object source, Class<T> clazz, MapId mapId, Class<? extends CustomConverter>... converterClasses) {
		if(source == null){
			return null;
		}
		return map(mapper, appCtx, source, clazz, mapId, converterClasses);
	}
	public static <T> T mapNotNull(Mapper mapper, ApplicationContext appCtx, Object source, T dest, MapId mapId, Class<? extends CustomConverter>... converterClasses) {
		if(source == null){
			return null;
		}
		return map(mapper, appCtx, source, dest, mapId, converterClasses);
	}
	@SuppressWarnings("unchecked")
	public static <T> T applyConverter(ApplicationContext appCtx, Object source , T dest, Class<? extends CustomConverter> converterClass) {
		CustomConverter converter = instantiateConverter(appCtx, converterClass); 
		return (T) converter.convert(dest, source, dest.getClass(), source.getClass());
	}
	public static <T> T applyConverters(ApplicationContext appCtx, Object source , T dest, Class<? extends CustomConverter>... converterClasses){
		for (Class<? extends CustomConverter> converterClass : converterClasses) {
			dest = applyConverter(appCtx, source, dest, converterClass);
		}
		return dest;
	}
	public static <T> T applyConverters(ApplicationContext appCtx, Object source , T dest, Iterable<Class<? extends CustomConverter>> converterClasses){
		for (Class<? extends CustomConverter> converterClass : converterClasses) {
			dest = applyConverter(appCtx, source, dest, converterClass);
		}
		return dest;
	}
	public static <T> T mapNotNull(Mapper mapper, ApplicationContext appCtx, Object source, T dest, MapId mapId, Converter... converter) {
		if(source == null){
			return null;
		}
		return map(mapper, appCtx, source, dest, mapId, converter);
	}
	public static <T> T mapNotNull(Mapper mapper, ApplicationContext appCtx, Object source, Class<T> clazz, MapId mapId, Converter... converter) {
		if(source == null){
			return null;
		}
		return map(mapper, appCtx, source, clazz, mapId, converter);
	}
	public static <T> T applyConverters(ApplicationContext appCtx, Object a, T b, Converter... converter) {
		List<Class<? extends CustomConverter>> converterClass = toCustomConverterClasses(converter);
		return applyConverters(appCtx, a, b, converterClass);
	}
	public static <A, DA> List<A> convertiLista(Mapper mapper, ApplicationContext appCtx, List<DA> listDa, Class<A> classA, MapId mapId, Converter... converters) {

		List<A> listA = new ArrayList<A>();
		if (listDa == null) {
			return listA;
		}

		for (DA source : listDa) {
			A dest = mapNotNull(mapper, appCtx, source, classA, mapId, converters);
			listA.add(dest);
		}

		return listA;
	}
}
