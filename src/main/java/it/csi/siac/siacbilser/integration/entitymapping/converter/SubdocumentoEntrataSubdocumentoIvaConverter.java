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
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

 /**
 * The Class SubdocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class SubdocumentoEntrataSubdocumentoIvaConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc > {
	
	
	@Autowired
	private SiacTSubdocIvaRepository siacTSubdocIvaRepository;

	/**
	 * Instantiates a new subdocumento entrata subdocumento iva converter.
	 */
	public SubdocumentoEntrataSubdocumentoIvaConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		
		if(src.getSiacRSubdocSubdocIvas()!=null){
			for (SiacRSubdocSubdocIva siacRSubdocSubdocIva : src.getSiacRSubdocSubdocIvas()) {
				if(siacRSubdocSubdocIva.getDataCancellazione()==null) {			
					SiacTSubdocIva siacTSubdocIva = siacTSubdocIvaRepository.findOne(siacRSubdocSubdocIva.getSiacTSubdocIva().getUid());
					SubdocumentoIvaEntrata subdocumentoIvaEntrata = map(siacTSubdocIva, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata_Base);
					dest.setSubdocumentoIva(subdocumentoIvaEntrata);
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
		
		if (src == null || dest == null) {
			return dest;
		}		
		
		return dest;
	}
	

}
