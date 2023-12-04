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
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaDocPadreModelDetailConverter.
 */
@Component
public class SubdocumentoSpesaDocPadreModelDetailConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa doc padre model detail
 	*/
	public SubdocumentoSpesaDocPadreModelDetailConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		if(src.getSiacTDoc() == null){
			return dest;
		}
		// Leggo i model detail per il documento di spesa
		DocumentoSpesaModelDetail[] modelDetails = Utility.MDTL.byModelDetailClass(DocumentoSpesaModelDetail.class);
		// Converto via i model detail richiesti
		DocumentoSpesa documentoSpesa = mapNotNull(src.getSiacTDoc(), DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_ModelDetail, Converters.byModelDetails(modelDetails));
		// Imposto il dato in response
		dest.setDocumento(documentoSpesa);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		// Non devo effetuare alcunche'
		return dest;
	}

}
