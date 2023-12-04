/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * The Class RegistrazioneMovFinMovimentoConverter.
 * 
 * @author Valentina
 */
@Component
public class EventoTipoCollegamentoConverter extends ExtendedDozerConverter<Evento, SiacDEvento > {
	
	
	
	/**
	 * Instantiates a new causale ep stato converter.
	 */
	public EventoTipoCollegamentoConverter() {
		super(Evento.class, SiacDEvento.class);
	}

	@Override
	public Evento convertFrom(SiacDEvento src, Evento dest) {
		if(src.getSiacDCollegamentoTipo() == null){
			return dest;
		}
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(src.getSiacDCollegamentoTipo().getCollegamentoTipoCode());
		TipoCollegamento tipoCollegamento = siacDCollegamentoTipoEnum.getTipoCollegamento();
		dest.setTipoCollegamento(tipoCollegamento);
		return dest;
	}

	@Override
	public SiacDEvento convertTo(Evento src, SiacDEvento dest) {
		return dest;
	}



	

}
