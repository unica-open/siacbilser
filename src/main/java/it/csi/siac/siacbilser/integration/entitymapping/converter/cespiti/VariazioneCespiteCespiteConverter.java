/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiVariazione;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;

/**
 * The Class VariazioneCespiteCespiteConverter.
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/08/2018
 */
@Component
public class VariazioneCespiteCespiteConverter extends ExtendedDozerConverter<VariazioneCespite, SiacTCespitiVariazione> {
	
	public VariazioneCespiteCespiteConverter() {
		super(VariazioneCespite.class, SiacTCespitiVariazione.class);
	}

	@Override
	public VariazioneCespite convertFrom(SiacTCespitiVariazione src, VariazioneCespite dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacTCespiti() == null){
			log.warn(methodName, "Variazione cespite [uid: " + src.getUid() + "] priva del Cespite! Controllare su DB. Entita associata: " + src.getClass().getSimpleName());
			return dest;
		}

		Cespite cespite = mapNotNull(src.getSiacTCespiti(), Cespite.class, CespMapId.SiacTCespiti_Cespite);
		
		dest.setCespite(cespite);
		return dest;
	}

	@Override
	public SiacTCespitiVariazione convertTo(VariazioneCespite src, SiacTCespitiVariazione dest) {
		if(src.getCespite() == null || src.getCespite().getUid() == 0) {
			return dest;
		}
		SiacTCespiti siacTCespiti = new SiacTCespiti();
		siacTCespiti.setUid(src.getCespite().getUid());
		dest.setSiacTCespiti(siacTCespiti);
		return dest;
	}


}
