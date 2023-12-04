/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoEntrataCommissioniDocumentoConverter.
 */
@Component
public class SubdocumentoEntrataCommissioniDocumentoConverter extends DozerConverter<SubdocumentoEntrata, SiacTSubdoc > {

	/**
	 * Instantiates a new subdocumento entrata commissioni documento converter.
	 */
	public SubdocumentoEntrataCommissioniDocumentoConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
//		
//		SiacDCommissioneTipo siacDCommissioneTipo = src.getSiacDCommissioneTipo();
//		
//		if(siacDCommissioneTipo== null){
//			return null;
//		}
//		
//		CommissioniDocumento commissioniDocumento = SiacDCommissioneTipoEnum.byCodice(siacDCommissioneTipo.getCommTipoCode()).getCommissioniDocumento();
		return dest;
		
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		//final String methodName = "convertTo";
		
//		if (src == null || dest == null) {
//			return dest;
//		}
//		
//		SiacDCommissioneTipoEnum commissioneTipo =  SiacDCommissioneTipoEnum.byCommissioniDocumento(src);
//		SiacDCommissioneTipo siacDCommissioneTipo = eef.getEntity(commissioneTipo, dest.getSiacTEnteProprietario().getUid(), SiacDCommissioneTipo.class); 
//		dest.setSiacDCommissioneTipo(siacDCommissioneTipo);
//		
		return dest;
	}



	

}
