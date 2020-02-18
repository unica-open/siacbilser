/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class SubdocumentoSpesaLiquidazioneConverter
 */
@Component
public class SubdocumentoEntrataPredocumentoConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc > {

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public SubdocumentoEntrataPredocumentoConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		if(src.getSiacRPredocSubdocs() != null){
			for (SiacRPredocSubdoc  siacRPredocSubdoc : src.getSiacRPredocSubdocs()) {
				if(siacRPredocSubdoc.getDataCancellazione() == null){
					PreDocumentoEntrata preDocEntrata = map(siacRPredocSubdoc.getSiacTPredoc(), PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata);
					dest.setPreDocumentoEntrata(preDocEntrata);
					break;
				}
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		
		return dest;
	}



	

}
