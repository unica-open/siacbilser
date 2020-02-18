/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * The Class RichiestaEconomaleCassaEconomaleConverter.
 */
@Component
public class RichiestaEconomaleCassaEconomaleConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon> {

	
	public RichiestaEconomaleCassaEconomaleConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src == null) {
			return dest;
		}
		
		CassaEconomale cassaEconomale = mapNotNull(src.getSiacTCassaEcon(), CassaEconomale.class, getMapIdCassaEconomale());
		dest.setCassaEconomale(cassaEconomale);
		return dest;
	}

	protected CecMapId getMapIdCassaEconomale() {
		return CecMapId.SiacTCassaEcon_CassaEconomale;
	}
	
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		if(src.getCassaEconomale()==null || src.getCassaEconomale().getUid()==0){
			return dest;
		}
		SiacTCassaEcon siacTCassaEcon = new SiacTCassaEcon();
		siacTCassaEcon.setUid(src.getCassaEconomale().getUid());
		dest.setSiacTCassaEcon(siacTCassaEcon);
		return dest;
	}
	
}
