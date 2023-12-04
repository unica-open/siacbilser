/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;

/**
 * The Class PreDocumentoEntrataSubDocumentoConverter.
 */
@Component
public class PreDocumentoEntrataSubDocumentoConverter extends ExtendedDozerConverter<PreDocumentoEntrata, SiacTPredoc > {
	
	/**
 * Instantiates a new pre documento entrata sub documento converter.
 */
public PreDocumentoEntrataSubDocumentoConverter() {
		super(PreDocumentoEntrata.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoEntrata convertFrom(SiacTPredoc src, PreDocumentoEntrata dest) {
		
		if(src.getSiacRPredocSubdocs()!=null) {
			for(SiacRPredocSubdoc siacRPredocSubdoc : src.getSiacRPredocSubdocs()){
				if(siacRPredocSubdoc.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTSubdoc siacTSubdoc = siacRPredocSubdoc.getSiacTSubdoc();
				
				if(siacTSubdoc!=null) {
					//SIAC-6428
					SubdocumentoEntrataModelDetail[] ssmm = new SubdocumentoEntrataModelDetail[]{SubdocumentoEntrataModelDetail.Ordinativo,SubdocumentoEntrataModelDetail.TestataDocumento, SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento};					
					SubdocumentoEntrata subdocumento = mapNotNull(siacTSubdoc, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_ModelDetail, Converters.byModelDetails(ssmm));
					dest.setSubDocumento(subdocumento);
					
					/*
					SubdocumentoEntrata subdocumento = new SubdocumentoEntrata();
					
					subdocumento.setUid(siacTSubdoc.getUid());			
					subdocumento.setNumero(siacTSubdoc.getSubdocNumero());
					
					SiacTDoc siacTDoc = siacTSubdoc.getSiacTDoc();
					
					DocumentoEntrata documento = map(siacTDoc, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Base);
					
					subdocumento.setDocumento(documento);

					dest.setSubDocumento(subdocumento);
					*/
					
				}				
							
			}	
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoEntrata src, SiacTPredoc dest) {
				
		return dest;
	}



	

}
