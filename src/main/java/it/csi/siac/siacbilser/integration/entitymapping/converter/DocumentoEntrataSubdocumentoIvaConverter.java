/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class DocumentoEntrataSubdocumentoIvaConverter extends ExtendedDozerConverter<DocumentoEntrata, SiacTDoc > {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public DocumentoEntrataSubdocumentoIvaConverter() {
		super(DocumentoEntrata.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoEntrata convertFrom(SiacTDoc src, DocumentoEntrata dest) {
		
		if(src.getSiacRDocIvas()!=null){
			for (SiacRDocIva r : src.getSiacRDocIvas()) {
				if(r.getDataCancellazione()==null) {
					for(SiacTSubdocIva si : r.getSiacTSubdocIvas()){
						if(si.getDataCancellazione()==null) {							
							SubdocumentoIvaEntrata subdocumentoIvaEntrata = map(si, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata_Base);
							dest.getListaSubdocumentoIva().add(subdocumentoIvaEntrata);
							return dest;
						}
					}
					
				}
			}
		}
		
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
