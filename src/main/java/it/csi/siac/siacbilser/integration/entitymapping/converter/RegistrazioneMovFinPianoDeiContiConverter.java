/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

@Component
public class RegistrazioneMovFinPianoDeiContiConverter extends ExtendedDozerConverter<RegistrazioneMovFin, SiacTRegMovfin> {
	
	/**
	 * Instantiates a new causale ep stato converter.
	 */
	public RegistrazioneMovFinPianoDeiContiConverter() {
		super(RegistrazioneMovFin.class, SiacTRegMovfin.class);
	}
	
	@Override
	public RegistrazioneMovFin convertFrom(SiacTRegMovfin src, RegistrazioneMovFin dest) {
		String methodName = "convertFrom";

		
		dest.setElementoPianoDeiContiIniziale(mapNotNull(src.getSiacTClass2(), ElementoPianoDeiConti.class, BilMapId.SiacTClass_ElementoPianoDeiConti));
		dest.setElementoPianoDeiContiAggiornato(mapNotNull(src.getSiacTClass1(), ElementoPianoDeiConti.class, BilMapId.SiacTClass_ElementoPianoDeiConti));
		
		return dest;
	}

	
	@Override
	public SiacTRegMovfin convertTo(RegistrazioneMovFin src, SiacTRegMovfin dest) {

		dest.setSiacTClass1(mapNotNull(src.getElementoPianoDeiContiIniziale(), SiacTClass.class, BilMapId.SiacTClass_ElementoPianoDeiConti));
		dest.setSiacTClass2(mapNotNull(src.getElementoPianoDeiContiAggiornato(), SiacTClass.class, BilMapId.SiacTClass_ElementoPianoDeiConti));
		
		return dest;
	}



	

}
