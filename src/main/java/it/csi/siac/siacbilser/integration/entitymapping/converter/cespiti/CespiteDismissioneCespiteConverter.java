/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteDismissioneCespiteConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
	public CespiteDismissioneCespiteConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacTCespitiDismissioni()== null){
			return dest;
		}

		DismissioneCespite dismissione = mapNotNull(src.getSiacTCespitiDismissioni(),DismissioneCespite.class,CespMapId.SiacTCespitiDismissioni_DismissioneCespite_ModelDetail );
		
		dest.setDismissioneCespite(dismissione);
		return dest;
		
	}

	@Override
	public SiacTCespiti convertTo(Cespite src, SiacTCespiti dest) {
		if(src.getDismissioneCespite() == null || src.getDismissioneCespite().getUid() == 0) {
			return dest;
		}
		SiacTCespitiDismissioni siacDCespitiBeneTipo = new SiacTCespitiDismissioni();
		siacDCespitiBeneTipo.setUid(src.getDismissioneCespite().getUid());
		dest.setSiacTCespitiDismissioni(siacDCespitiBeneTipo);
		return dest;
	}


}
