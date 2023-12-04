/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDContotesoreria;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

/**
 * The Class PreDocumentoSpesaContoTesoreriaConverter.
 */
@Component
public class PreDocumentoSpesaContoTesoreriaConverter extends ExtendedDozerConverter<PreDocumentoSpesa, SiacTPredoc> {
	
	/**
	 * Instantiates a new pre documento spesa conto tesoreria converter.
	 */
	public PreDocumentoSpesaContoTesoreriaConverter() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		ContoTesoreria contoTesoreria = mapNotNull(src.getSiacDContotesoreria(), ContoTesoreria.class, BilMapId.SiacDContotesoreria_ContoTesoreria);
		dest.setContoTesoreria(contoTesoreria);
		return dest;
	}

	@Override
	public SiacTPredoc convertTo(PreDocumentoSpesa src, SiacTPredoc dest) {
		SiacDContotesoreria siacDContotesoreria = mapNotNull(src.getContoTesoreria(), SiacDContotesoreria.class, BilMapId.SiacDContotesoreria_ContoTesoreria);
		dest.setSiacDContotesoreria(siacDContotesoreria);
		return dest;
	}

}
