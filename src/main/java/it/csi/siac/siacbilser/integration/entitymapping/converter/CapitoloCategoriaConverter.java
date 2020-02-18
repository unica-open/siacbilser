/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;


/**
 * The Class CapitoloEntrataGestioneCategoriaConverter
 */
@Component
public class CapitoloCategoriaConverter extends ExtendedDozerConverter<Capitolo<?, ?>, SiacTBilElem > {

	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	@SuppressWarnings("unchecked")
	public CapitoloCategoriaConverter() {
		super((Class<Capitolo<?, ?>>)(Class<?>)Capitolo.class, SiacTBilElem.class);
	}

	@Override
	public Capitolo<?, ?> convertFrom(SiacTBilElem src, Capitolo<?, ?> dest) {
		
		if(src.getSiacRBilElemCategorias()!= null){
			for(SiacRBilElemCategoria r : src.getSiacRBilElemCategorias()){
				if(r.getDataCancellazione()!=null) {
					continue;
				}
				
				CategoriaCapitolo categoriaCapitolo = mapNotNull(r.getSiacDBilElemCategoria(), CategoriaCapitolo.class, BilMapId.SiacDBilElemCategoria_CategoriaCapitolo);
				dest.setCategoriaCapitolo(categoriaCapitolo);
			}
		}
		return dest;
	}


	@Override
	public SiacTBilElem convertTo(Capitolo<?, ?> src, SiacTBilElem dest) {	
		return dest;	
	}
	
}
