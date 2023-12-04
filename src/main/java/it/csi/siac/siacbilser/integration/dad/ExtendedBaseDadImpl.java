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

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.base.JpaManagementDao;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.entitymapping.mapper.ModelDetailMapperDecorator;
import it.csi.siac.siacbilser.model.mutuo.MutuoAttoAmministrativoComposedModelDetail;
import it.csi.siac.siaccommon.model.ComposedModelDetail;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siaccommon.util.mapper.MapperDecorator;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccommonser.integration.entitymapping.Converter;
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
		setEnte(ente, true);
	}
	/**
	 * @param ente the ente to set
	 * @param mapEntity whether to map to the entity
	 */
	public void setEnte(Ente ente, boolean mapEntity) {
		this.ente = ente;
		if(mapEntity) {
			this.siacTEnteProprietario = map(ente, SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		}
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
	
	protected <T, E> ListaPaginata<T> toListaPaginata(Page<E> pagedList, Class<T> classDest, MapId mapId, ModelDetailEnum... modelDetails) {
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
	protected <T extends Serializable, E> ListaPaginata<T> toListaPaginata(Page<E> pagedList, T destSample, MapId mapId, ModelDetailEnum... modelDetails) {
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

	public void detach(SiacTBase siacTBase) {
		jpaManagementDao.detach(siacTBase);
	}
	
	public void flush() {
		jpaManagementDao.flush();
	}

	public void flushAndClear() {
		jpaManagementDao.flushAndClear();
	}
	
	@Override
	protected Converter[] getConverterByModelDetail(ModelDetailEnum... modelDetails) {
		return Converters.byModelDetails(modelDetails);
	}
	
	@Override
	protected CustomConverter getConverterFromComponent(Class<? extends CustomConverter> converterClass) {
		return appCtx.getBean(Utility.toDefaultBeanName(converterClass), converterClass);
	}
	
	protected Integer getAnnoBilancio ( ) {
		return Integer.valueOf(Utility.BTL.get().getAnno());
	}
}
