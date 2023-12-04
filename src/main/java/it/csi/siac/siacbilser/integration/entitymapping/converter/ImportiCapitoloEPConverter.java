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
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;

// TODO: Auto-generated Javadoc
/**
 * The Class ImportiCapitoloEPConverter.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ImportiCapitoloEPConverter extends ImportiCapitoloBaseConverter<ImportiCapitoloEP> implements CustomConverter  {

		
	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(Object dest /*dest*/, Object src /*source*/, Class<?> cdest, Class<?> csrc) {
		
		if(src == null){
			return null;
		}		
		
		try{
			@SuppressWarnings("unchecked")
			List<ImportiCapitoloEP> listaImporti = (List<ImportiCapitoloEP>)src;
			SiacTBilElem cup = (SiacTBilElem)dest;
			settaListaImportiCapitoloInSiacTBilElem(listaImporti,cup);			
			return cup;
		} catch (ClassCastException cce ){
			SiacTBilElem cup = (SiacTBilElem)src; //dentro avrà getDettaglioElementiBilancio() valorizzato
			 return  getListaImportiCapitoloFromSiacTBilElem(cup);
		}
		
	}
	
	

}
