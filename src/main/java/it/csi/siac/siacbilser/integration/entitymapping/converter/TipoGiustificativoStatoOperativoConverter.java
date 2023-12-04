/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativo;
import it.csi.siac.siaccecser.model.StatoOperativoTipoGiustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;


@Component
public class TipoGiustificativoStatoOperativoConverter extends ExtendedDozerConverter<TipoGiustificativo, SiacDGiustificativo> {
	
	@Autowired
	private EnumEntityFactory eef;
	
	
	public TipoGiustificativoStatoOperativoConverter() {
		super(TipoGiustificativo.class, SiacDGiustificativo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoGiustificativo convertFrom(SiacDGiustificativo src, TipoGiustificativo dest) {
		final Date now = new Date();
		if(src.getDataFineValidita() == null || src.getDataFineValidita().after(now)){
			dest.setStatoOperativoTipoGiustificativo(StatoOperativoTipoGiustificativo.VALIDO);
		}else{
			dest.setStatoOperativoTipoGiustificativo(StatoOperativoTipoGiustificativo.ANNULLATO);
		}
		return dest;
		
	}

	@Override
	public SiacDGiustificativo convertTo(TipoGiustificativo src, SiacDGiustificativo dest) {
		
		return dest;
		 
	}
	
}
