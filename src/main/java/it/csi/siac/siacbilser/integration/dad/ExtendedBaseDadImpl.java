/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.dozer.CustomConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.base.JpaManagementDao;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccommonser.util.dozer.MapId;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siacfin2ser.model.DecodificaEnum;

/**
 * Estende le funzionalit&agrave; del Data Access Delegate di base per
 * consentire consentire di specificare programmaticamente i converter che si desidera utilizzare.
 * 
 * @author Domenico
 *
 */
public class ExtendedBaseDadImpl extends BaseDadImpl {
	
	/** The app ctx. */
	@Autowired protected ApplicationContext appCtx;
	@Autowired protected JpaManagementDao jpaManagementDao;
	
	protected Ente ente;
	protected SiacTEnteProprietario siacTEnteProprietario;
	protected String loginOperazione;
	
	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
		this.siacTEnteProprietario = map(ente, SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
	}
	/**
	 * @param siacTEnteProprietario the siacTEnteProprietario to set
	 * @deprecated Non dovrebbe essere usato: l'uso potrebbe significare un erroneo uso degli strati applicativi, in quanto
	 * il DAD dovrebbe essere <strong>@Autowired</strong> solo nelle classi di servizio, che non dovrebbero avere accesso
	 * alle entities.
	 * <br/>
	 * Usare {@link #setEnte(Ente)}
	 */
	@Deprecated
	public void setSiacTEnteProprietario(SiacTEnteProprietario siacTEnteProprietario) {
		this.siacTEnteProprietario = siacTEnteProprietario;
		this.ente = map(siacTEnteProprietario, Ente.class, BilMapId.SiacTEnteProprietario_Ente_Base);
	}
	/**
	 * @param loginOperazione the loginOperazione to set
	 */
	public void setLoginOperazione(String loginOperazione) {
		this.loginOperazione = loginOperazione;
	}
	
	/**
	 * To lista paginata.
	 * 
	 * A differenza di toListaPaginata in cui si specifica la classe di destinazione, con questo metodo e' possibile fornire un oggetto di destinazione
	 * di partenza. Questo oggetto verra' clonato mediante serializzazione per ogni elemento della pagina.
	 * Questa soluzione è più lenta ed è da utilizzare solo se è necessario fornire una base valorizzata per costruire l'oggetto di destinazione. 
	 *
	 * @param <T> the generic type
	 * @param <E> the element type
	 * @param pagedList the paged list
	 * @param destSample the dest sample
	 * @param mapId the map id
	 * @return the lista paginata
	 */
	protected <T extends Serializable, E> ListaPaginata<T> toListaPaginata(Page<E> pagedList, T destSample, MapId mapId) {
		final String methodName = "toListaPaginata";
		ListaPaginataImpl<T> list = new ListaPaginataImpl<T>();

		if (!pagedList.hasContent()) {
			return list;
		}

		int elementsPerPage = 1 + (int) (pagedList.getTotalElements() / pagedList.getTotalPages());

		list.setPaginaCorrente(pagedList.getNumber());
		list.setTotaleElementi((int) pagedList.getTotalElements());
		list.setTotalePagine(pagedList.getTotalPages());
		list.setHasPaginaPrecedente(pagedList.hasPreviousPage());
		list.setHasPaginaSuccessiva(pagedList.hasNextPage());
		list.setNumeroElementoInizio(1 + pagedList.getNumber() * elementsPerPage);
		list.setNumeroElementoFine(pagedList.getNumber() * elementsPerPage
				+ pagedList.getNumberOfElements());

		for (E dto : pagedList.getContent()){
			@SuppressWarnings("unchecked")
			T dest = (T) SerializationUtils.clone(destSample);
			map(dto, dest, mapId);
			list.add(dest);
		}

		log.debug(methodName, "PaginaCorrente: " + list.getPaginaCorrente()
				+ " TotaleElementi: " + list.getTotaleElementi() + " TotalePagine: "
				+ list.getTotalePagine());

		return list;

	}
	
	protected <T, E> ListaPaginata<T> toListaPaginata(Page<E> pagedList, Class<T> classDest, MapId mapId, ModelDetail... modelDetails) {
		final String methodName = "toListaPaginata";
		ListaPaginataImpl<T> list = new ListaPaginataImpl<T>();

		if (!pagedList.hasContent()){
			return list;
		}

		int elementsPerPage = 1 + (int) (pagedList.getTotalElements() / pagedList
				.getTotalPages());

		list.setPaginaCorrente(pagedList.getNumber());
		list.setTotaleElementi((int) pagedList.getTotalElements());
		list.setTotalePagine(pagedList.getTotalPages());
		list.setHasPaginaPrecedente(pagedList.hasPreviousPage());
		list.setHasPaginaSuccessiva(pagedList.hasNextPage());
		list.setNumeroElementoInizio(1 + pagedList.getNumber()
				* elementsPerPage);
		list.setNumeroElementoFine(pagedList.getNumber() * elementsPerPage
				+ pagedList.getNumberOfElements());

		Converters[] converters = Converters.byModelDetails(modelDetails);
		for (E dto : pagedList.getContent()){
			T o = map(dto, classDest, mapId, converters);
			list.add(o);
		}

		log.debug(methodName, "PaginaCorrente: "
				+ list.getPaginaCorrente() + " TotaleElementi: "
				+ list.getTotaleElementi() + " TotalePagine: "
				+ list.getTotalePagine());

		return list;

	}
	
	/**
	 * To lista paginata.
	 * 
	 * A differenza di toListaPaginata in cui si specifica la classe di destinazione, con questo metodo e' possibile fornire un oggetto di destinazione
	 * di partenza. Questo oggetto verra' clonato mediante serializzazione per ogni elemento della pagina.
	 * Questa soluzione è più lenta ed è da utilizzare solo se è necessario fornire una base valorizzata per costruire l'oggetto di destinazione. 
	 *
	 * @param <T> the generic type
	 * @param <E> the element type
	 * @param pagedList the paged list
	 * @param destSample the dest sample
	 * @param mapId the map id
	 * @param modelDetails the model details
	 * @return the lista paginata
	 */
	protected <T extends Serializable, E> ListaPaginata<T> toListaPaginata(Page<E> pagedList, T destSample, MapId mapId, ModelDetail... modelDetails) {
		final String methodName = "toListaPaginata";
		ListaPaginataImpl<T> list = new ListaPaginataImpl<T>();

		if (!pagedList.hasContent()) {
			return list;
		}

		int elementsPerPage = 1 + (int) (pagedList.getTotalElements() / pagedList.getTotalPages());

		list.setPaginaCorrente(pagedList.getNumber());
		list.setTotaleElementi((int) pagedList.getTotalElements());
		list.setTotalePagine(pagedList.getTotalPages());
		list.setHasPaginaPrecedente(pagedList.hasPreviousPage());
		list.setHasPaginaSuccessiva(pagedList.hasNextPage());
		list.setNumeroElementoInizio(1 + pagedList.getNumber() * elementsPerPage);
		list.setNumeroElementoFine(pagedList.getNumber() * elementsPerPage
				+ pagedList.getNumberOfElements());

		Converters[] converters = Converters.byModelDetails(modelDetails);
		for (E dto : pagedList.getContent()){
			@SuppressWarnings("unchecked")
			T dest = (T) SerializationUtils.clone(destSample);
			map(dto, dest, mapId, converters);
			list.add(dest);
		}

		log.debug(methodName, "PaginaCorrente: " + list.getPaginaCorrente()
				+ " TotaleElementi: " + list.getTotaleElementi() + " TotalePagine: "
				+ list.getTotalePagine());

		return list;

	}
	
	
	
	/**
	 * Map to anno.
	 *
	 * @param date the date
	 * @return the integer
	 */
	protected static Integer mapToAnno(Date date){
		if(date == null) {
			return null;
		}
		GregorianCalendar cg =   new GregorianCalendar();
		cg.setTime(date);
		int anno = cg.get(Calendar.YEAR);
		return anno;
	 }
	
	
	//---------------------------------------
	
	protected <T> T map(Object source, Class<T> clazz, MapId mapId, Class<? extends CustomConverter>... converterClasses) {
		T dest = super.map(source, clazz, mapId);
		return (T) applyConverters(source, dest, converterClasses);
	}
	
	protected <T> T mapNotNull(Object source, Class<T> clazz, MapId mapId, Class<? extends CustomConverter>... converterClasses) {
		if(source==null){
			return null;
		}
		return map(source, clazz, mapId, converterClasses);
	}
	
	protected <T> T map(Object source, T dest, MapId mapId, Class<? extends CustomConverter>... converterClasses) {
		super.map(source, dest, mapId);
		return applyConverters(source, dest, converterClasses);
	}
	
	protected <T> T mapNotNull(Object source, T dest, MapId mapId, Class<? extends CustomConverter>... converterClasses) {
		if(source==null){
			return null;
		}
		return map(source, dest, mapId, converterClasses);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T applyConverter(Object source , T dest, Class<? extends CustomConverter> converterClass) {		
		CustomConverter converter = instantiateConverter(converterClass); 
		dest = (T) converter.convert(dest, source, dest.getClass(), source.getClass());
		return dest;
	}
	
	protected  <T> T applyConverters(Object source , T dest, Class<? extends CustomConverter>... converterClasses){
		for (Class<? extends CustomConverter> converterClass : converterClasses) {
			dest = applyConverter(source, dest, converterClass);			
		}		
		return dest;		
	}
	
	protected  <T> T applyConverters(Object source , T dest, List<Class<? extends CustomConverter>> converterClasses){
		for (Class<? extends CustomConverter> converterClass : converterClasses) {
			dest = applyConverter(source, dest, converterClass);			
		}		
		return dest;		
	}
	
	protected  <T> T map(Object source, T dest, MapId mapId, Converter... converter) {	
		super.map(source, dest, mapId);
		return applyConverters(source, dest, converter);
	}
	
	protected  <T> T mapNotNull(Object source, T dest, MapId mapId, Converter... converter) {
		if(source==null){
			return null;
		}
		return map(source, dest, mapId, converter);
	}
	
	protected  <T> T map(Object source, Class<T> clazz, MapId mapId, Converter... converter) {	
		T dest = super.map(source, clazz, mapId);
		return applyConverters(source, dest, converter);
	}
	
	protected  <T> T mapNotNull(Object source, Class<T> clazz, MapId mapId, Converter... converter) {	
		if(source==null){
			return null;
		}
		return map(source, clazz, mapId, converter);
	}
	
	protected <T> T  applyConverters(Object a , T b, Converter... converter) {	
		List<Class<? extends CustomConverter>> converterClass = toCustomConverterClasses(converter);
		return applyConverters(a, b, converterClass);
	}
	
	protected CustomConverter instantiateConverter(Converter converter) {
		return instantiateConverter(converter.getCustomConverterClass());
	}
	
	protected CustomConverter instantiateConverter(Class<? extends CustomConverter> converterClass) {
//		try{
//			return appCtx.getBean(converterClass);
//		}catch(NoSuchBeanDefinitionException nsbde){
//			try {
//				return converterClass.newInstance();
//			} catch (InstantiationException e) {
//				throw new IllegalArgumentException("Impossibile istanziare il Converter "+ converterClass);
//			} catch (IllegalAccessException e) {
//				throw new IllegalArgumentException("Impossibile accedere al costruttore del Converter "+ converterClass);
//			}
//		}
		
		
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
	
	/**
	 * Ottiene l'elenco dei {@link CustomConverter} a partire da un elenco di
	 * {@link Converter}
	 * 
	 * @param converter
	 * @return elenco dei {@link CustomConverter} associati ai
	 *         {@link Converter} passati come parametro.
	 */
	private static List<Class<? extends CustomConverter>> toCustomConverterClasses(Converter... converter) {
		List<Class<? extends CustomConverter>> result = new ArrayList<Class<? extends CustomConverter>>();
		for (Converter md : converter) {
			Class<? extends CustomConverter> conv = md.getCustomConverterClass();
			result.add(conv);
		}

		return result;
	}
	
	/**
	 * Converter un intera lista (senza paginazione) specificando un mapId di base i Converter aggiuntivi da applicare.
	 * 
	 * @param listDa
	 * @param classA
	 * @param mapId
	 * @param converters
	 * @return lista convertita
	 */
	protected <A, DA> List<A> convertiLista(List<DA> listDa, Class<A> classA, MapId mapId, Converter... converters) {

		List<A> listA = new ArrayList<A>();
		if (listDa == null) {
			return listA;
		}

		for (DA source : listDa) {
			A dest = mapNotNull(source, classA, mapId, converters);
			listA.add(dest);
		}

		return listA;
	}
	
	/**
	 * Converti lista senza paginazione, richiamanso il metodo {@link #convertiLista(List, Class, MapId, Converter...)}
	 *
	 * @param <A> the generic type
	 * @param <DA> the generic type
	 * @param listDa the list da
	 * @param classA the class A
	 * @param mapId the map id
	 * @param modelDetails the model details
	 * @return the list
	 */
	protected <A, DA> List<A> convertiLista(List<DA> listDa, Class<A> classA, MapId mapId, ModelDetail... modelDetails) {
		return convertiLista( listDa, classA, mapId, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Controlla se l'entit&agrave; fornita abbia l'uid valorizzato
	 * @param e l'entit&agrave; da controllare
	 * @return true se l'entit&agrave; &eacute; valorizzata con uid non zero
	 */
	protected boolean entitaHasUid(Entita e) {
		return e != null && e.getUid() != 0;
	}
	
	/**
	 * Controlla se almeno una delle entit&agrave; fornita abbia l'uid valorizzato
	 * @param e le entit&agrave; da controllare
	 * @return true se almeno una delle entit&agrave; &eacute; valorizzata con uid non zero
	 */
	protected boolean entitaHasUid(Collection<? extends Entita> e) {
		if(e == null || e.isEmpty()) {
			return false;
		}
		for(Entita entita : e) {
			if(entita != null && entita.getUid() != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Projetta le entit&agrave; sugli uid
	 * @param entitas le entit&agrave; da projettare
	 * @return gli uid delle entit&agrave;
	 */
	protected <E extends Entita> Set<Integer> projectToUid(Iterable<E> entitas) {
		Set<Integer> res = new HashSet<Integer>();
		if(entitas == null) {
			return res;
		}
		for(E e : entitas) {
			if(e != null && e.getUid() != 0) {
				res.add(e.getUid());
			}
		}
		return res;
	}
	
	protected <E extends Entita> Set<Integer> projectToUid(E... entitas) {
		return projectToUid(Arrays.asList(entitas));
	}
	
	/**
	 * Projetta le entit&agrave; sugli uid
	 * @param entitas le entit&agrave; da projettare
	 * @return gli uid delle entit&agrave;
	 */
	protected <E extends Entita> List<Integer> projectToUidList(Iterable<E> entitas) {
		List<Integer> res = new ArrayList<Integer>();
		if(entitas == null) {
			return res;
		}
		for(E e : entitas) {
			if(e != null && e.getUid() != 0) {
				res.add(e.getUid());
			}
		}
		return res;
	}
	
	protected <E extends Entita> List<Integer> projectToUidList(E... entitas) {
		return projectToUidList(Arrays.asList(entitas));
	}
	
	protected <E extends DecodificaEnum> List<String> projectToCode(Iterable<E> decodifiche) {
		List<String> res = new ArrayList<String>();
		if(decodifiche != null) {
			for(E de : decodifiche) {
				res.add(de.getCodice());
			}
		}
		return res;
	}
	
	protected <E extends DecodificaEnum> List<String> projectToCode(E... decodifiche) {
		if(decodifiche == null) {
			return new ArrayList<String>();
		}
		return projectToCode(Arrays.asList(decodifiche));
	}
	
	public void flush() {
		jpaManagementDao.flush();
	}
	public void flushAndClear() {
		jpaManagementDao.flushAndClear();
	}
}
