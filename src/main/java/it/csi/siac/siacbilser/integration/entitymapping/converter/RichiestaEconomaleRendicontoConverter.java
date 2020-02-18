/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RichiestaEconomaleRendicontoConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon> {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public RichiestaEconomaleRendicontoConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src.getSiacTGiustificativos() == null || src.getSiacTGiustificativos().isEmpty()){
			return dest;
		}
		for(SiacTGiustificativo siacTGiustificativo: src.getSiacTGiustificativos()){
			RendicontoRichiesta rendicontoRichiesta = new RendicontoRichiesta();
			map(siacTGiustificativo,rendicontoRichiesta,getMapIdRendicontoRichiesta());
			dest.setRendicontoRichiesta(rendicontoRichiesta);
			break;
		}
		return dest;
	}

	protected CecMapId getMapIdRendicontoRichiesta() {
		return CecMapId.SiacTGiustificativo_RendicontoRichiesta_Base;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
	
		return dest;
	}



	

}
