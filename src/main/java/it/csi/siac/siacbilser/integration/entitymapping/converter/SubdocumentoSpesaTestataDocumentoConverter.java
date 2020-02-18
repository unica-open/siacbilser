/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaTipoIvaSplitReverseConverter.
 */
@Component
public class SubdocumentoSpesaTestataDocumentoConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	
	/**
	 * Instantiates a new subdocumento spesa tipo iva split reverse converter.
 	*/
	public SubdocumentoSpesaTestataDocumentoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		SiacTDoc siacTDoc = src.getSiacTDoc();
	
		DocumentoSpesa docSpesa = map(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Minimal);
		
		dest.setDocumento(docSpesa);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		return dest;
	}

}
