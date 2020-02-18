/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacRDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaTipoIvaSplitReverseConverter.
 */
@Component
public class SubdocumentoSpesaHasSubordinatiConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new subdocumento spesa tipo iva split reverse converter.
 	*/
	public SubdocumentoSpesaHasSubordinatiConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		if(src.getSiacTDoc() == null){
			return dest;
		}
		if(src.getSiacTDoc().getSiacRDocsPadre() == null || src.getSiacTDoc().getSiacRDocsPadre().isEmpty()){
			dest.setHasSubordinati(Boolean.FALSE);
			return dest;
		}
		for(SiacRDoc r : src.getSiacTDoc().getSiacRDocsPadre()){
			if(r.getDataCancellazione() == null && SiacDRelazTipoEnum.Subdocumento.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode())){
				dest.setHasSubordinati(Boolean.TRUE);
				return dest;
			}
		}
		dest.setHasSubordinati(Boolean.FALSE);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		return dest;
	}

}
