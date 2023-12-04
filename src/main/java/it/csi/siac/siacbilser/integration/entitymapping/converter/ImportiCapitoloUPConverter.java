/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.dozer.CustomConverter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;

// TODO: Auto-generated Javadoc
/**
 * The Class ImportiCapitoloUPConverter.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ImportiCapitoloUPConverter extends ImportiCapitoloBaseConverter<ImportiCapitoloUP> implements CustomConverter  {

	
	/**
	 * Convert from 
	 * 	List<ImportiCapitoloUP>
	 * to
	 * 	List<SiacTBilElemDet> nested in SiacTBilElem
	 * and viceversa.
	 *
	 * @param dest the dest
	 * @param src the src
	 * @param cdest the cdest
	 * @param csrc the csrc
	 * @return the object
	 */
	@Override
	public Object convert(Object dest /*dest*/, Object src /*source*/, Class<?> cdest, Class<?> csrc) {
		
		if(src == null){
			return null;
		}		
		
		try{
			@SuppressWarnings("unchecked")
			List<ImportiCapitoloUP> listaImporti = (List<ImportiCapitoloUP>)src;
			SiacTBilElem cup = (SiacTBilElem)dest;
			settaListaImportiCapitoloInSiacTBilElem(listaImporti,cup);			
			return cup;
		} catch (ClassCastException cce ){
			SiacTBilElem cup = (SiacTBilElem)src; //dentro avrà getDettaglioElementiBilancio() valorizzato
			 return  getListaImportiCapitoloFromSiacTBilElem(cup);
		}
		
	}
	
	

}
