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
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;

/**
 * The Class PreDocumentoSpesaSubDocumentoConverter.
 */
@Component
public class PreDocumentoSpesaSubDocumentoConverter extends ExtendedDozerConverter<PreDocumentoSpesa, SiacTPredoc > {

	/**
	 * Instantiates a new pre documento spesa sub documento converter.
 	*/
	public PreDocumentoSpesaSubDocumentoConverter() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		
		if(src.getSiacRPredocSubdocs()!=null) {
			for(SiacRPredocSubdoc siacRPredocSubdoc : src.getSiacRPredocSubdocs()){
				if(siacRPredocSubdoc.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTSubdoc siacTSubdoc = siacRPredocSubdoc.getSiacTSubdoc();
				
				
				if(siacTSubdoc!=null) {
					//SIAC-6428
					SubdocumentoSpesaModelDetail[] ssmm = new SubdocumentoSpesaModelDetail[]{SubdocumentoSpesaModelDetail.Ordinativo,SubdocumentoSpesaModelDetail.TestataDocumento, SubdocumentoSpesaModelDetail.ImpegnoSubimpegno};					
					SubdocumentoSpesa subdocumento = mapNotNull(siacTSubdoc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_ModelDetail, Converters.byModelDetails(ssmm));
					/*
					SubdocumentoSpesa subdocumento = new SubdocumentoSpesa();
					
					subdocumento.setUid(siacTSubdoc.getUid());			
					subdocumento.setNumero(siacTSubdoc.getSubdocNumero());
					
					SiacTDoc siacTDoc = siacTSubdoc.getSiacTDoc();
					
					DocumentoSpesa documento = map(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Base);
					
					subdocumento.setDocumento(documento);
					*/
					
					dest.setSubDocumento(subdocumento);
				}
				
			}	
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoSpesa src, SiacTPredoc dest) {
				
		return dest;
	}



	

}
