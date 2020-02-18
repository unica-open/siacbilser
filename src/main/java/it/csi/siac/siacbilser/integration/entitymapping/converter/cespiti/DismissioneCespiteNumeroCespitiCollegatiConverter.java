/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiDismissioniRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.DismissioneCespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 *@author elisa
 * @version 1.0.0 - 09-08-2018
 */
@Component
public class DismissioneCespiteNumeroCespitiCollegatiConverter extends ExtendedDozerConverter<DismissioneCespite, SiacTCespitiDismissioni > {
	
	@Autowired
	SiacTCespitiDismissioniRepository siacTCespitiDismissioniRepository;
	/**
	 * Instantiates a new dismissione cespite atto amm converter.
	 */
	public DismissioneCespiteNumeroCespitiCollegatiConverter() {
		super(DismissioneCespite.class, SiacTCespitiDismissioni.class);
	}

	@Override
	public DismissioneCespite convertFrom(SiacTCespitiDismissioni src, DismissioneCespite dest) {
		Long cespitiCollegati = siacTCespitiDismissioniRepository.countCespitiCollegatiByDismissioneId(src.getUid());
		dest.setNumeroCespitiCollegati(cespitiCollegati);
		return dest;
		
	}

	@Override
	public SiacTCespitiDismissioni convertTo(DismissioneCespite src, SiacTCespitiDismissioni dest) {
		return dest;
	}


}
