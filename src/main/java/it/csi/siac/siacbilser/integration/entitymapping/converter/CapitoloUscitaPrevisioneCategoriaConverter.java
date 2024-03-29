/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloUscitaPrevisioneCategoriaConverter extends ExtendedDozerConverter<CapitoloUscitaPrevisione, SiacTBilElem > {

	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloUscitaPrevisioneCategoriaConverter() {
		super(CapitoloUscitaPrevisione.class, SiacTBilElem.class);
	}

	@Override
	public CapitoloUscitaPrevisione convertFrom(SiacTBilElem src, CapitoloUscitaPrevisione dest) {
		
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
	public SiacTBilElem convertTo(CapitoloUscitaPrevisione src, SiacTBilElem dest) {	
		return dest;	
	}
	
}
