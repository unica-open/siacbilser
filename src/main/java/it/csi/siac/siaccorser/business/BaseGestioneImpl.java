/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccorser.business;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;

/**
 * The Class BaseGestioneImpl.
 */
public abstract class BaseGestioneImpl implements BaseGestione{
	
	/** The dozer bean mapper. */
	@Autowired
	protected Mapper dozerBeanMapper;

	/** The log. */
	protected static final LogSrvUtil log = new LogSrvUtil(BaseGestioneImpl.class);

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccorser.business.BaseGestione#getDozerBeanMapper()
	 */
	public Mapper getDozerBeanMapper(){
		return dozerBeanMapper;
	}

	/**
	 * Sets the dozer bean mapper.
	 *
	 * @param dozerBeanMapper the new dozer bean mapper
	 */
	public void setDozerBeanMapper(Mapper dozerBeanMapper){
		this.dozerBeanMapper = dozerBeanMapper;
	}

	/**
	 * Converti lista.
	 *
	 * @param <T> the generic type
	 * @param <DTO> the generic type
	 * @param dtoList the dto list
	 * @param cls the cls
	 * @return the list
	 */
	protected <T, DTO> List<T> convertiLista(List<DTO> dtoList, Class<T> cls){
		List<T> list = new ArrayList<T>();

		for (DTO dto : dtoList){
			list.add(dozerBeanMapper.map(dto, cls));
		}

		return list;
	}

	/**
	 * Converti lista paginata.
	 *
	 * @param <T> the generic type
	 * @param <DTO> the generic type
	 * @param dtoList the dto list
	 * @param cls the cls
	 * @return the lista paginata
	 */
	protected <T, DTO> ListaPaginata<T> convertiListaPaginata(ListaPaginata<DTO> dtoList, Class<T> cls){
		ListaPaginata<T> list = new ListaPaginataImpl<T>();

		for (DTO dto : dtoList){
			list.add(dozerBeanMapper.map(dto, cls));
		}
			
		return list;
	}

	

}
