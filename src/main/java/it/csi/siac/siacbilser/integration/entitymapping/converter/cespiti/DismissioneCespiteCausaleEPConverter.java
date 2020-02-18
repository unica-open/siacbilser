/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author elisa
 * @version 1.0.0 - 09-08-2018
 */
@Component
public class DismissioneCespiteCausaleEPConverter extends ExtendedDozerConverter<DismissioneCespite, SiacTCespitiDismissioni > {
	
	/**
	 * Instantiates a new dismissione cespite causale EP converter.
	 */
	public DismissioneCespiteCausaleEPConverter() {
		super(DismissioneCespite.class, SiacTCespitiDismissioni.class);
	}

	@Override
	public DismissioneCespite convertFrom(SiacTCespitiDismissioni src, DismissioneCespite dest) {
		
		CausaleEP causaleEp = mapNotNull(src.getSiacTCausaleEp(), CausaleEP.class, GenMapId.SiacTCausaleEp_CausaleEP_Minimal);
		dest.setCausaleEP(causaleEp);
		return dest;
		
	}

	@Override
	public SiacTCespitiDismissioni convertTo(DismissioneCespite src, SiacTCespitiDismissioni dest) {
		if(src.getCausaleEP()== null || src.getCausaleEP().getUid() == 0) {
			return dest;
		}
		
		SiacTCausaleEp siacTCausaleEp = new SiacTCausaleEp();
		siacTCausaleEp.setUid(src.getCausaleEP().getUid());
		dest.setSiacTCausaleEp(siacTCausaleEp);
		return dest;
	}


}
