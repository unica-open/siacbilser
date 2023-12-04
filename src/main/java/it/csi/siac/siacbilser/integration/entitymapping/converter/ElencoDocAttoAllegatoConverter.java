/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

@Component
public class ElencoDocAttoAllegatoConverter extends ElencoDocAttoAllegatoBaseConverter {
	
	/**
	 * Ottengo l'allegato dall'entity. A seconda della necessit&agrave; posso tirare su diversi livelli dell'allegato atto.
	 * 
	 * @param siacTAttoAllegato l'entity
	 * @return l'allegato atto
	 */
	protected AllegatoAtto mapAllegatoAtto(SiacTAttoAllegato siacTAttoAllegato) {
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(siacTAttoAllegato.getUid());
		return allegatoAtto;
	}

}
