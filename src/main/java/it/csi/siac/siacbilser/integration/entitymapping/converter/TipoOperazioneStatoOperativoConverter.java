/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siaccecser.model.StatoOperativoTipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;


@Component
public class TipoOperazioneStatoOperativoConverter extends ExtendedDozerConverter<TipoOperazioneCassa, SiacDCassaEconOperazTipo> {
	
	@Autowired
	private EnumEntityFactory eef;
	
	
	public TipoOperazioneStatoOperativoConverter() {
		super(TipoOperazioneCassa.class, SiacDCassaEconOperazTipo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoOperazioneCassa convertFrom(SiacDCassaEconOperazTipo src, TipoOperazioneCassa dest) {
		final Date now = new Date();
		
		if(src.getDataFineValidita() == null || src.getDataFineValidita().after(now)){
			dest.setStatoOperativoTipoOperazioneCassa(StatoOperativoTipoOperazioneCassa.VALIDO);
		}else{
			dest.setStatoOperativoTipoOperazioneCassa(StatoOperativoTipoOperazioneCassa.ANNULLATO);
		}
		return dest;
		
	}

	@Override
	public SiacDCassaEconOperazTipo convertTo(TipoOperazioneCassa src, SiacDCassaEconOperazTipo dest) {
		
		return dest;
		 
	}
	
}
