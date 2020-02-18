/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class SubdocumentoSpesaTipoIvaSplitReverseConverter.
 */
@Component
public class SubdocumentoEntrataIsSubordinatoConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc> {

	
	/**
	 * Instantiates a new subdocumento spesa tipo iva split reverse converter.
 	*/
	public SubdocumentoEntrataIsSubordinatoConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		
		if(src.getSiacTDoc() == null){
			return dest;
		}
		if(src.getSiacTDoc().getSiacRDocsFiglio() == null || src.getSiacTDoc().getSiacRDocsFiglio().isEmpty()){
			dest.setIsSubordinato(Boolean.FALSE);
			return dest;
		}
		for(SiacRDoc r : src.getSiacTDoc().getSiacRDocsFiglio()){
			if(r.getDataCancellazione() == null && SiacDRelazTipoEnum.Subdocumento.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode())){
				dest.setIsSubordinato(Boolean.TRUE);
				return dest;
			}
		}
		dest.setIsSubordinato(Boolean.FALSE);
		return dest;
		
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		return dest;
	}

}
