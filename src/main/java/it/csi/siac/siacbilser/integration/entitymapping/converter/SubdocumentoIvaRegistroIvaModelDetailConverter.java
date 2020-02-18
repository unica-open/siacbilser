/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;


/**
 * The Class SubdocumentoIvaRegistroIvaConverter.
 */
@Component
public class SubdocumentoIvaRegistroIvaModelDetailConverter extends ExtendedDozerConverter<SubdocumentoIva<?, ?, ?>, SiacTSubdocIva > {
	
	/**
	 * Instantiates a new subdocumento iva registro iva converter.
	 */
	@SuppressWarnings("unchecked")
	public SubdocumentoIvaRegistroIvaModelDetailConverter() {
		super((Class<SubdocumentoIva<?, ?, ?>>)(Class<?>)SubdocumentoIva.class, SiacTSubdocIva.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoIva<?, ?, ?> convertFrom(SiacTSubdocIva siacTSubdocIva, SubdocumentoIva<?, ?, ?> dest) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "dest.uid: "+ dest.getUid());
		
		SiacTIvaRegistro siacTIvaRegistro = siacTSubdocIva.getSiacTIvaRegistro();
		if(siacTIvaRegistro.getDataCancellazione()!=null){
			return dest;
		}
		
		RegistroIva registroIva = map(siacTIvaRegistro, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva_Minimal);
		
		dest.setRegistroIva(registroIva);	
		
		return dest;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdocIva convertTo(SubdocumentoIva<?, ?, ?> subdoc, SiacTSubdocIva dest) {	
		
		if(subdoc.getRegistroIva()!=null && subdoc.getRegistroIva().getUid()!=0) {
			
			SiacTIvaRegistro siacTIvaRegistro = new SiacTIvaRegistro();
			siacTIvaRegistro.setUid(subdoc.getRegistroIva().getUid());
			dest.setSiacTIvaRegistro(siacTIvaRegistro);			
				
		}		
		
		return dest;	
	}

}
	

	
	
	



	


