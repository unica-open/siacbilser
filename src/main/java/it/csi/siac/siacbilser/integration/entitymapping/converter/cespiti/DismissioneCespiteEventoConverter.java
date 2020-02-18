/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siacgenser.model.Evento;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author elisa
 * @version 1.0.0 - 09-08-2018
 */
public class DismissioneCespiteEventoConverter extends ExtendedDozerConverter<DismissioneCespite, SiacTCespitiDismissioni > {
	
	public DismissioneCespiteEventoConverter() {
		super(DismissioneCespite.class, SiacTCespitiDismissioni.class);
	}

	@Override
	public DismissioneCespite convertFrom(SiacTCespitiDismissioni src, DismissioneCespite dest) {
		String methodName = "convertFrom";
		
		Evento evento = mapNotNull(src.getSiacDEvento(), Evento.class, GenMapId.SiacDEvento_Evento);
		dest.setEvento(evento);
		return dest;
		
	}

	@Override
	public SiacTCespitiDismissioni convertTo(DismissioneCespite src, SiacTCespitiDismissioni dest) {
		if(src.getEvento()== null || src.getEvento().getUid() == 0) {
			return dest;
		}
		SiacDEvento siacDEvento = new SiacDEvento();
		siacDEvento.setUid(src.getEvento().getUid());
		dest.setSiacDEvento(siacDEvento);
		
		return dest;
	}


}
