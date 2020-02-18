/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * class StampeCassaFileBilancioConverter
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/01/2017
 *
 */
@Component
public class StampeCassaFileBilancioConverter  extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa> {
	
	/** Costruttore di default */
	public StampeCassaFileBilancioConverter() {
		super(StampeCassaFile.class, SiacTCassaEconStampa.class);
	}

	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src, StampeCassaFile dest) {
		Bilancio bilancio = mapNotNull(src.getSiacTBil(), Bilancio.class, BilMapId.SiacTBil_Bilancio);
		dest.setBilancio(bilancio);
		return dest;
	}

	@Override
	public SiacTCassaEconStampa convertTo(StampeCassaFile src, SiacTCassaEconStampa dest) {
		if(src.getBilancio() != null && src.getBilancio().getUid() != 0) {
			SiacTBil siacTBil = mapNotNull(src.getBilancio(), SiacTBil.class, BilMapId.SiacTBil_Bilancio);
			dest.setSiacTBil(siacTBil);
		}
		
		return dest;
	}

}
