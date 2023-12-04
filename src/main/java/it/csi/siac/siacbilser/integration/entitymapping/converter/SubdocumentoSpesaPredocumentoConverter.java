/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaLiquidazioneConverter
 */
@Component
public class SubdocumentoSpesaPredocumentoConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc > {

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public SubdocumentoSpesaPredocumentoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		if(src.getSiacRPredocSubdocs() != null){
			for (SiacRPredocSubdoc  siacRPredocSubdoc : src.getSiacRPredocSubdocs()) {
				if(siacRPredocSubdoc.getDataCancellazione() == null){
					PreDocumentoSpesa preDocSpesa = map(siacRPredocSubdoc.getSiacTPredoc(), PreDocumentoSpesa.class, BilMapId.SiacTPredoc_PreDocumentoSpesa);
					dest.setPreDocumentoSpesa(preDocSpesa);
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
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		return dest;
	}



	

}
