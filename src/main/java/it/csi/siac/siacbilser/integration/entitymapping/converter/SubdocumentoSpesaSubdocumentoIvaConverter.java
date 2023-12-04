/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSubdocIvaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaSubdocumentoIvaConverter.
 */
@Component
public class SubdocumentoSpesaSubdocumentoIvaConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc > {
	
	
	@Autowired
	private SiacTSubdocIvaRepository siacTSubdocIvaRepository;

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public SubdocumentoSpesaSubdocumentoIvaConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		if(src.getSiacRSubdocSubdocIvas()!=null){
			for (SiacRSubdocSubdocIva siacRSubdocSubdocIva : src.getSiacRSubdocSubdocIvas()) {
				if(siacRSubdocSubdocIva.getDataCancellazione()==null) {			
					SiacTSubdocIva siacTSubdocIva = siacTSubdocIvaRepository.findOne(siacRSubdocSubdocIva.getSiacTSubdocIva().getUid());
					SubdocumentoIvaSpesa subdocumentoIvaSpesa = map(siacTSubdocIva, SubdocumentoIvaSpesa.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaSpesa_Base);
					dest.setSubdocumentoIva(subdocumentoIvaSpesa);
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
		
		if (src == null || dest == null) {
			return dest;
		}		
		
		return dest;
	}



	

}
