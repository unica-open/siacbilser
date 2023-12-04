/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class SubdocumentoEntrataDocPadreModelDetailConverter.
 */
@Component
public class SubdocumentoEntrataDocPadreModelDetailConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa doc padre model detail
 	*/
	public SubdocumentoEntrataDocPadreModelDetailConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		if(src.getSiacTDoc() == null){
			return dest;
		}
		// Leggo i model detail per il documento di spesa
		DocumentoEntrataModelDetail[] modelDetails = Utility.MDTL.byModelDetailClass(DocumentoEntrataModelDetail.class);
		// Converto via i model detail richiesti
		DocumentoEntrata documentoEntrata = mapNotNull(src.getSiacTDoc(), DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_ModelDetail, Converters.byModelDetails(modelDetails));
		// Imposto il dato in response
		dest.setDocumento(documentoEntrata);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		// Non devo effetuare alcunche'
		return dest;
	}

}
