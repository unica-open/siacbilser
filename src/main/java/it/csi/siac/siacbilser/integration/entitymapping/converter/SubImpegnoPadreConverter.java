/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class SubImpegnoPadreConverter.
 */
@Component
public class SubImpegnoPadreConverter extends ExtendedDozerConverter<SubImpegno, SiacTMovgestT> {
	
	/**
	 * Instantiates a new subimpegno padre converter.
	 */
	public SubImpegnoPadreConverter() {
		super(SubImpegno.class, SiacTMovgestT.class);
	}

	@Override
	public SubImpegno convertFrom(SiacTMovgestT src, SubImpegno dest) {
		if(src.getSiacTMovgest() == null) {
			return dest;
		}
		SiacTMovgest siacTMovgest = src.getSiacTMovgest();
		
		dest.setAnnoImpegnoPadre(siacTMovgest.getMovgestAnno());
		dest.setNumeroImpegnoPadre(siacTMovgest.getMovgestNumero());
		
		return dest;
	}

	@Override
	public SiacTMovgestT convertTo(SubImpegno src, SiacTMovgestT dest) {
		return dest;
	}

}
