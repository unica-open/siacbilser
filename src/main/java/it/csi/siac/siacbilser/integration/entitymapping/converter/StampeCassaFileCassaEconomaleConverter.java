/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampeCassaFile;

/**
 * class StampeCassaFileCassaEconomaleConverter
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/01/2017
 *
 */
@Component
public class StampeCassaFileCassaEconomaleConverter  extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa > {
	
	/** Costruttore di default */
	public StampeCassaFileCassaEconomaleConverter() {
		super(StampeCassaFile.class, SiacTCassaEconStampa.class);
	}

	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src, StampeCassaFile dest) {
		CassaEconomale cassaEconomale = mapNotNull(src.getSiacTCassaEcon(), CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
		dest.setCassaEconomale(cassaEconomale);
		return dest;
	}

	@Override
	public SiacTCassaEconStampa convertTo(StampeCassaFile src, SiacTCassaEconStampa dest) {
		if(src.getCassaEconomale() != null && src.getCassaEconomale().getUid() != 0) {
			SiacTCassaEcon siacTCassaEcon = mapNotNull(src.getCassaEconomale(), SiacTCassaEcon.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
			dest.setSiacTCassaEcon(siacTCassaEcon);
		}
		
		return dest;
	}
	
}
