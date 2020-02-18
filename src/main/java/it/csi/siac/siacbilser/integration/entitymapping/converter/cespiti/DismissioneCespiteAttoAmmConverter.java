/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.DismissioneCespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 *@author elisa
 * @version 1.0.0 - 09-08-2018
 */
@Component
public class DismissioneCespiteAttoAmmConverter extends ExtendedDozerConverter<DismissioneCespite, SiacTCespitiDismissioni > {
	
	/**
	 * Instantiates a new dismissione cespite atto amm converter.
	 */
	public DismissioneCespiteAttoAmmConverter() {
		super(DismissioneCespite.class, SiacTCespitiDismissioni.class);
	}

	@Override
	public DismissioneCespite convertFrom(SiacTCespitiDismissioni src, DismissioneCespite dest) {
		
		AttoAmministrativo attoAmministrativo = mapNotNull(src.getSiacTAttoAmm(), AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
		dest.setAttoAmministrativo(attoAmministrativo);
		return dest;
		
	}

	@Override
	public SiacTCespitiDismissioni convertTo(DismissioneCespite src, SiacTCespitiDismissioni dest) {
		if(src.getAttoAmministrativo()== null || src.getAttoAmministrativo().getUid() == 0) {
			return dest;
		}
		
		SiacTAttoAmm siacTAttoAmm = new SiacTAttoAmm();
		siacTAttoAmm.setUid(src.getAttoAmministrativo().getUid());
		dest.setSiacTAttoAmm(siacTAttoAmm);
		return dest;
	}


}
