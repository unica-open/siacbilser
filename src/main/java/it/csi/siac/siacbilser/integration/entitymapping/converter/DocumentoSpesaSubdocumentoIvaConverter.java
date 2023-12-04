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
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;

/**
 * The Class DocumentoSpesaSubdocumentoIvaConverter.
 */
@Component
public class DocumentoSpesaSubdocumentoIvaConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc > {

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public DocumentoSpesaSubdocumentoIvaConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		
		if(src.getSiacRDocIvas()!=null){
			for (SiacRDocIva r : src.getSiacRDocIvas()) {
				if(r.getDataCancellazione()==null) {
					for(SiacTSubdocIva si : r.getSiacTSubdocIvas()){
						if(si.getDataCancellazione()==null) {							
							SubdocumentoIvaSpesa subdocumentoIvaSpesa = map(si, SubdocumentoIvaSpesa.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaSpesa_Base);
							dest.getListaSubdocumentoIva().add(subdocumentoIvaSpesa);
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
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		
		return dest;
	}



	

}
