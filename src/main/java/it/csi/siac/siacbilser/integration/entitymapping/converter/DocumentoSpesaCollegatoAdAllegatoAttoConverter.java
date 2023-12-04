/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * The Class DocumentoSpesaSubdocumentoIvaConverter.
 */
@Component
public class DocumentoSpesaCollegatoAdAllegatoAttoConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc > {

	@Autowired
	private SiacTDocRepository siacTDocRepository;
	
	
	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public DocumentoSpesaCollegatoAdAllegatoAttoConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		
		
		SiacTDoc siacTDoc = siacTDocRepository.findDocCollegatoAdAllegatoAtto(src.getDocId());
		
		dest.setCollegatoAdAllegatoAtto(siacTDoc!=null);
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
