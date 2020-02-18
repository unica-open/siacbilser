/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.Evento;

/**
 * The Class TipoBeneCespiteEventoAmmortamento.
 *
 * @author Antonino
 */
public class TipoBeneCespiteEventoAmmortamentoConverter extends BaseTipoBeneCespiteEventoConverter {
	
	@Override
	protected SiacDEvento getSiacDEvento(SiacDCespitiBeneTipo src) {
		return src.getSiacDEventoAmmortamento();
	}
	
	@Override
	protected Evento getEvento(TipoBeneCespite src) {
		return src.getEventoAmmortamento();
	}
	
	@Override
	protected void setEvento(TipoBeneCespite dest, int uid, String eventoCode, String eventoDesc) {
		Evento evento = new Evento();
		evento.setUid(uid);
		evento.setCodice(eventoCode);
		evento.setDescrizione(eventoDesc);
		dest.setEventoAmmortamento(evento);
	}
	
	@Override
	protected void setSiacDEvento(SiacDCespitiBeneTipo dest, int uid, String eventoCode, String eventoDesc) {
		SiacDEvento siacDEvento = new SiacDEvento();
		siacDEvento.setUid(uid);
		dest.setSiacDEventoAmmortamento(siacDEvento);
		dest.setEventoAmmortamentoCode(eventoCode);
		dest.setEventoAmmortamentoDesc(eventoDesc);
	}

}
