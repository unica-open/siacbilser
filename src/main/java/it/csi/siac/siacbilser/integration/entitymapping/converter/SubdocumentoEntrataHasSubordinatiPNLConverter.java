/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class SubdocumentoEntrataHasSubordinatiPNLConverter.
 */
@Component
public class SubdocumentoEntrataHasSubordinatiPNLConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento entrata has subordinati PNL converter.
 	*/
	public SubdocumentoEntrataHasSubordinatiPNLConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		
		if(src.getSiacTDoc() == null){
			return dest;
		}
		if(src.getSiacTDoc().getSiacRDocsPadre() == null || src.getSiacTDoc().getSiacRDocsPadre().isEmpty()){
			dest.setHasSubordinatiPNL(Boolean.FALSE);
			return dest;
		}
		for(SiacRDoc r : src.getSiacTDoc().getSiacRDocsPadre()){
			if(r.getDataCancellazione() == null && SiacDRelazTipoEnum.Subdocumento.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode()) && isPNL(r.getSiacTDocFiglio())){
				dest.setHasSubordinatiPNL(Boolean.TRUE);
				return dest;
			}
		}
		dest.setHasSubordinatiPNL(Boolean.FALSE);
		return dest;
	}

	private boolean isPNL(SiacTDoc siacTDoc) {
		return siacTDoc != null && siacTDoc.getSiacDDocTipo() != null && "PNL".equals(siacTDoc.getSiacDDocTipo().getDocTipoCode());
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		return dest;
	}

}
