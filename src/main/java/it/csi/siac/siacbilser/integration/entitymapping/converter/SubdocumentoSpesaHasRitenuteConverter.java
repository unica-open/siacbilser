/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaTipoIvaSplitReverseConverter.
 */
@Component
public class SubdocumentoSpesaHasRitenuteConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new subdocumento spesa tipo iva split reverse converter.
 	*/
	public SubdocumentoSpesaHasRitenuteConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		if(src.getSiacTDoc() == null){
			return dest;
		}
		if(src.getSiacTDoc().getSiacRDocOneres() == null || src.getSiacTDoc().getSiacRDocOneres().isEmpty()){
			dest.setHasRitenute(Boolean.FALSE);
			return dest;
		}
		for (SiacRDocOnere r : src.getSiacTDoc().getSiacRDocOneres()) {
			if(r.getDataCancellazione()==null) {
				dest.setHasRitenute(Boolean.TRUE);
				return dest;
			}
		}
		dest.setHasRitenute(Boolean.FALSE);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		return dest;
	}

}
