/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconTipoEnum;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;


@Component
public class TipoCassaEconomaleConverter extends ExtendedDozerConverter<CassaEconomale, SiacTCassaEcon> {
	
	@Autowired
	private EnumEntityFactory eef;
	
	
	public TipoCassaEconomaleConverter() {
		super(CassaEconomale.class, SiacTCassaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CassaEconomale convertFrom(SiacTCassaEcon src, CassaEconomale dest) {
		
		if(src.getSiacDCassaEconTipo() == null){
			return dest;
		}
		if(src.getSiacDCassaEconTipo().getCassaeconTipoCode() != null){
			TipoDiCassa tipoDiCassa =  SiacDCassaEconTipoEnum.byCodice(src.getSiacDCassaEconTipo().getCassaeconTipoCode()).getTipoDiCassa();
			dest.setTipoDiCassa(tipoDiCassa);
		}
		return dest;
		
	}

	@Override
	public SiacTCassaEcon convertTo(CassaEconomale src, SiacTCassaEcon dest) {
		
		if(src.getTipoDiCassa() == null) {
			return dest;
		}
		
		SiacDCassaEconTipoEnum tipo =  SiacDCassaEconTipoEnum.byTipoDiCassa(src.getTipoDiCassa());
		SiacDCassaEconTipo siacDCassaEconTipo = eef.getEntity(tipo, dest.getSiacTEnteProprietario().getUid(), SiacDCassaEconTipo.class); 
		dest.setSiacDCassaEconTipo(siacDCassaEconTipo);
	
		return dest;
		 
	}
	
}
