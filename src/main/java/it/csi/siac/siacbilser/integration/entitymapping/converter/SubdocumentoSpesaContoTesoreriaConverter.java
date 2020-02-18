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
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaContoTesoreriaConverter
 */
@Component
public class SubdocumentoSpesaContoTesoreriaConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc > {

	/**
	 * Instantiates a new subdocumento spesa conto tesoreria converter.
	 */
	public SubdocumentoSpesaContoTesoreriaConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		ContoTesoreria contoTesoreria = mapNotNull(src.getSiacDContotesoreria(), ContoTesoreria.class, BilMapId.SiacDContotesoreria_ContoTesoreria_Fin2);
		dest.setContoTesoreria(contoTesoreria);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		SiacDContotesoreria siacDContotesoreria = mapNotNull(src.getContoTesoreria(), SiacDContotesoreria.class, BilMapId.SiacDContotesoreria_ContoTesoreria_Fin2);
		dest.setSiacDContotesoreria(siacDContotesoreria);
		return dest;
	}

}
