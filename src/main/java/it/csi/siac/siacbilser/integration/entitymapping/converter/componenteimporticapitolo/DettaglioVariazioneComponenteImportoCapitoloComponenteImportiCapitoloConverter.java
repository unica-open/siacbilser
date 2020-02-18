/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.componenteimporticapitolo;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVarComp;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;

/**
 * The Class DettaglioVariazioneComponenteImportoCapitoloComponenteImportiCapitoloConverter.
 */
@Component
public class DettaglioVariazioneComponenteImportoCapitoloComponenteImportiCapitoloConverter extends ExtendedDozerConverter<DettaglioVariazioneComponenteImportoCapitolo, SiacTBilElemDetVarComp> {
	
	/**
	 * Constructor
	 */
	public DettaglioVariazioneComponenteImportoCapitoloComponenteImportiCapitoloConverter() {
		super(DettaglioVariazioneComponenteImportoCapitolo.class, SiacTBilElemDetVarComp.class);
	}

	@Override
	public DettaglioVariazioneComponenteImportoCapitolo convertFrom(SiacTBilElemDetVarComp src, DettaglioVariazioneComponenteImportoCapitolo dest) {
		if(src.getSiacTBilElemDetComp() != null && src.getSiacTBilElemDetComp().getUid() != null) {
			ComponenteImportiCapitolo componenteImportiCapitolo = mapNotNull(
					src.getSiacTBilElemDetComp(),
					ComponenteImportiCapitolo.class,
					BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail,
					Converters.byModelDetails(Utility.MDTL.byModelDetailClass(ComponenteImportiCapitoloModelDetail.class)));
			dest.setComponenteImportiCapitolo(componenteImportiCapitolo);
			// FIXME [VariazioneComponenteCapitolo]: forzo il tipo a STANZIAMENTO
			dest.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		}
		return dest;
	}

	@Override
	public SiacTBilElemDetVarComp convertTo(DettaglioVariazioneComponenteImportoCapitolo src, SiacTBilElemDetVarComp dest) {
		if(src.getComponenteImportiCapitolo() != null && src.getComponenteImportiCapitolo().getUid() != 0) {
			SiacTBilElemDetComp siacTBilElemDetComp = mapNotNull(src.getComponenteImportiCapitolo(), SiacTBilElemDetComp.class, BilMapId.SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail);
			dest.setSiacTBilElemDetComp(siacTBilElemDetComp);
		}
		return dest;
	}

}
