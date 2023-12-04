/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * The Class SubAccertamentoPadreConverter.
 */
@Component
public class SubAccertamentoPadreConverter extends ExtendedDozerConverter<SubAccertamento, SiacTMovgestT> {
	
	/**
	 * Instantiates a new sub accertamento padre converter.
	 */
	public SubAccertamentoPadreConverter() {
		super(SubAccertamento.class, SiacTMovgestT.class);
	}

	@Override
	public SubAccertamento convertFrom(SiacTMovgestT src, SubAccertamento dest) {
		if(src.getSiacTMovgest() == null) {
			return dest;
		}
		SiacTMovgest siacTMovgest = src.getSiacTMovgest();
		
		dest.setAnnoAccertamentoPadre(siacTMovgest.getMovgestAnno());
		dest.setNumeroAccertamentoPadre(siacTMovgest.getMovgestNumero());
		
		return dest;
	}

	@Override
	public SiacTMovgestT convertTo(SubAccertamento src, SiacTMovgestT dest) {
		return dest;
	}

}
