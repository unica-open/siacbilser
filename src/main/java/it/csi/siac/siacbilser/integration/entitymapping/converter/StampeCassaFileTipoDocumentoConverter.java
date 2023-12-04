/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconStampaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconStampaTipoEnum;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;

/**
 * class StampeCassaFileTipoDocumentoConverter
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 11/03/2015
 *
 */
@Component
public class StampeCassaFileTipoDocumentoConverter  extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa > {
	@Autowired
	private EnumEntityFactory eef;
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public StampeCassaFileTipoDocumentoConverter() {
		super(StampeCassaFile.class, SiacTCassaEconStampa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src, StampeCassaFile dest) {
		if(src.getSiacDCassaEconStampaTipo()!=null){
			TipoDocumento tipoDocumento = TipoDocumento.fromCodice(src.getSiacDCassaEconStampaTipo().getCestTipoCode());
			dest.setTipoDocumento(tipoDocumento);
		}
        return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCassaEconStampa convertTo(StampeCassaFile src, SiacTCassaEconStampa dest) {
		
	
		SiacDCassaEconStampaTipoEnum siacDCassaEconStampaTipoEnum = SiacDCassaEconStampaTipoEnum.byCodice(src.getTipoDocumento().getCodice());
		SiacDCassaEconStampaTipo siacDCassaEconStampaTipo = eef.getEntity(siacDCassaEconStampaTipoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDCassaEconStampaTipo.class);
		dest.setSiacDCassaEconStampaTipo(siacDCassaEconStampaTipo);
		
		return dest;
	}

}
