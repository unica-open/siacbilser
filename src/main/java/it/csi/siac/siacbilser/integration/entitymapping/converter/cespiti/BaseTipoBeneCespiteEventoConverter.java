/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.Evento;

/**
 * The Class TipoBeneCespiteEventoAmmortamento.
 *
 * @author Antonino
 */
public abstract class BaseTipoBeneCespiteEventoConverter extends ExtendedDozerConverter<TipoBeneCespite, SiacDCespitiBeneTipo> {
	
	/**
	 * Instantiates a new base tipo bene cespite evento converter.
	 */
	public BaseTipoBeneCespiteEventoConverter() {
		super(TipoBeneCespite.class, SiacDCespitiBeneTipo.class);
	}

	@Override
	public TipoBeneCespite convertFrom(SiacDCespitiBeneTipo src, TipoBeneCespite dest) {
		SiacDEvento siacDEvento = getSiacDEvento(src);
		if(siacDEvento == null){
			return dest;
		}
		Evento eventoAmmortamento = new Evento();
		eventoAmmortamento.setUid(siacDEvento.getUid());
		eventoAmmortamento.setCodice(siacDEvento.getEventoCode());
		eventoAmmortamento.setDescrizione(siacDEvento.getEventoDesc());
		
		setEvento(dest, siacDEvento.getUid(), siacDEvento.getEventoCode(), siacDEvento.getEventoDesc() );
		return dest;
		
	}

	@Override
	public SiacDCespitiBeneTipo convertTo(TipoBeneCespite src, SiacDCespitiBeneTipo dest) {
		Evento evento = getEvento(src);
		if(evento == null || evento.getUid() == 0) {
			return dest;
		}
		
		setSiacDEvento(dest, evento.getUid(),evento.getCodice(), evento.getDescrizione());
		
		return dest;
	}

	protected abstract SiacDEvento getSiacDEvento(SiacDCespitiBeneTipo src);
	protected abstract void setEvento(TipoBeneCespite dest, int uid, String eventoCode, String eventoDesc);
	
	protected abstract Evento getEvento(TipoBeneCespite src);
	protected abstract void setSiacDEvento(SiacDCespitiBeneTipo dest, int uid, String eventoCode, String eventoDesc);

}
