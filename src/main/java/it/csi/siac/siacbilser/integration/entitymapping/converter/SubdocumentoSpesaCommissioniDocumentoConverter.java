/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCommissioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCommissioneTipoEnum;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

 /**
 * The Class SubdocumentoSpesaCommissioniDocumentoConverter.
 */
@Component
public class SubdocumentoSpesaCommissioniDocumentoConverter extends DozerConverter<SubdocumentoSpesa, SiacTSubdoc > {
	
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new subdocumento spesa commissioni documento converter.
	 */
	public SubdocumentoSpesaCommissioniDocumentoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		SiacDCommissioneTipo siacDCommissioneTipo = src.getSiacDCommissioneTipo();
		
		if(siacDCommissioneTipo== null){
			return dest;
		}
		CommissioniDocumento commissioniDocumento = SiacDCommissioneTipoEnum.byCodice(siacDCommissioneTipo.getCommTipoCode()).getCommissioniDocumento();
		dest.setCommissioniDocumento(commissioniDocumento);
		return dest;
		
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		if (src == null || src.getCommissioniDocumento() == null || dest == null) {
			return dest;
		}
		
		SiacDCommissioneTipoEnum commissioneTipo =  SiacDCommissioneTipoEnum.byCommissioniDocumento(src.getCommissioniDocumento());
		SiacDCommissioneTipo siacDCommissioneTipo = eef.getEntity(commissioneTipo, dest.getSiacTEnteProprietario().getUid(), SiacDCommissioneTipo.class); 
		dest.setSiacDCommissioneTipo(siacDCommissioneTipo);
		
		return dest;
	}



	

}
