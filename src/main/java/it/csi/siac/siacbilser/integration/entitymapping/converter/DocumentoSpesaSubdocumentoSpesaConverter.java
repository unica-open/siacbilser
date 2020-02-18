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
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class DocumentoSpesaSubdocumentoIvaConverter.
 */
@Component
public class DocumentoSpesaSubdocumentoSpesaConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc > {

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public DocumentoSpesaSubdocumentoSpesaConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		
		if(src.getSiacTSubdocs() == null){
			return dest;
		}
		List<SubdocumentoSpesa> listaSubdocumenti = new ArrayList<SubdocumentoSpesa>();
		for(SiacTSubdoc s : src.getSiacTSubdocs()){
			if(s.getDataCancellazione() == null){
				SubdocumentoSpesa subdoc = map(s, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
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
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		
		return dest;
	}



	

}
