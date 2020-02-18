/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.componenteimporticapitolo;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;

/**
 * The Class ComponenteImportiCapitoloTipoComponenteImportiCapitoloConverter.
 */
@Component
public class ComponenteImportiCapitoloTipoComponenteImportiCapitoloConverter extends ExtendedDozerConverter<ComponenteImportiCapitolo, SiacTBilElemDetComp> {
	
	/**
	 * Constructor
	 */
	public ComponenteImportiCapitoloTipoComponenteImportiCapitoloConverter() {
		super(ComponenteImportiCapitolo.class, SiacTBilElemDetComp.class);
	}

	@Override
	public ComponenteImportiCapitolo convertFrom(SiacTBilElemDetComp src, ComponenteImportiCapitolo dest) {
		if(src.getSiacDBilElemDetCompTipo() != null && src.getSiacDBilElemDetCompTipo().getUid() != null) {
			TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = mapNotNull(
					src.getSiacDBilElemDetCompTipo(),
					TipoComponenteImportiCapitolo.class,
					//rimozione _ModelDetail
					BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo,
					Converters.byModelDetails(Utility.MDTL.byModelDetailClass(TipoComponenteImportiCapitoloModelDetail.class)));
			dest.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		}
		return dest;
	}
	

	@Override
	public SiacTBilElemDetComp convertTo(ComponenteImportiCapitolo src, SiacTBilElemDetComp dest) {
		if(src.getTipoComponenteImportiCapitolo() != null && src.getTipoComponenteImportiCapitolo().getUid() != 0) {
			SiacDBilElemDetCompTipo siacDBilElemDetCompTipo = mapNotNull(src.getTipoComponenteImportiCapitolo(), SiacDBilElemDetCompTipo.class, BilMapId.SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo_ModelDetail);
			dest.setSiacDBilElemDetCompTipo(siacDBilElemDetCompTipo);
		}
		return dest;
	}

}
