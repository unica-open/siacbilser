/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDContotesoreria;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class SubdocumentoEntrataContoTesoreriaConverter.
 */
@Component
public class SubdocumentoEntrataContoTesoreriaConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc> {
	
	/**
	 * Instantiates a new subdocumento entrata conto tesoreria converter.
	 */
	public SubdocumentoEntrataContoTesoreriaConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		SiacDContotesoreria siacDContotesoreria = src.getSiacDContotesoreria();
		ContoTesoreria contoTesoreria = mapNotNull(siacDContotesoreria, ContoTesoreria.class, BilMapId.SiacDContotesoreria_ContoTesoreria);
		
		dest.setContoTesoreria(contoTesoreria);
		return dest;
	}
	
	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		ContoTesoreria contoTesoreria = src.getContoTesoreria();
		SiacDContotesoreria siacDContotesoreria = mapNotNull(contoTesoreria, SiacDContotesoreria.class, BilMapId.SiacDContotesoreria_ContoTesoreria);
		dest.setSiacDContotesoreria(siacDContotesoreria);
		return dest;
	}



}
