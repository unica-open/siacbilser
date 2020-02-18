/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

@Component
public class ElencoDocAttoAllegatoExtendedConverter extends ElencoDocAttoAllegatoBaseConverter {

	@Override
	protected AllegatoAtto mapAllegatoAtto(SiacTAttoAllegato siacTAttoAllegato) {
		return map(siacTAttoAllegato, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base);
	}
}
