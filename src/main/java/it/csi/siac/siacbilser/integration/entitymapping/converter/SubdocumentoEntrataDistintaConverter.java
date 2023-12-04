/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDDistinta;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Distinta;

/**
 * The Class SubdocumentoEntrataDistintaConverter.
 */
@Component
public class SubdocumentoEntrataDistintaConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc> {
	
	/**
	 * Instantiates a new subdocumento entrata distinta converter.
	 */
	public SubdocumentoEntrataDistintaConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		SiacDDistinta siacDDistinta = src.getSiacDDistinta();
		Distinta distinta = mapNotNull(siacDDistinta, Distinta.class, BilMapId.SiacDDistinta_Distinta);
		dest.setDistinta(distinta);
		return dest;
	}
	
	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		Distinta contoTesoreria = src.getDistinta();
		SiacDDistinta siacDDistinta = mapNotNull(contoTesoreria, SiacDDistinta.class, BilMapId.SiacDDistinta_Distinta);
		dest.setSiacDDistinta(siacDDistinta);
		return dest;
	}



}
