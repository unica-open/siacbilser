/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class DocumentoEntrataSubdocumentoEntrataConverter extends ExtendedDozerConverter<DocumentoEntrata, SiacTDoc > {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public DocumentoEntrataSubdocumentoEntrataConverter() {
		super(DocumentoEntrata.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoEntrata convertFrom(SiacTDoc src, DocumentoEntrata dest) {
		
		if(src.getSiacTSubdocs() == null){
			return dest;
		}
		List<SubdocumentoEntrata> listaSubdocumenti = new ArrayList<SubdocumentoEntrata>();
		for(SiacTSubdoc s : src.getSiacTSubdocs()){
			if(s.getDataCancellazione() == null){
				SubdocumentoEntrata subdoc = map(s, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base);
				listaSubdocumenti.add(subdoc);
			}
		}
		dest.setListaSubdocumenti(listaSubdocumenti);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoEntrata src, SiacTDoc dest) {
		return dest;
	}



	

}
