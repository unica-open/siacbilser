/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Entita;

/**
 * Estende RegistrazioneMovFinEventoMovimentoConverter ma mappa solo l'uid del movimento.
 * Il Movimento ottenuto ha comunque l'istanza di Model corretta: ad esempio SubdocumentoSpesa per il tipo collegamento
 * SiacDCollegamentoTipoEnum.SubdocumentoSpesa
 * 
 * @author Domenico
 */
@Component
public class RegistrazioneMovFinEventoMovimentoLightConverter extends RegistrazioneMovFinEventoMovimentoConverter {
	
	@Override
	protected void mapMovimento(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, SiacTBase src, Entita dest) {
		
		String methodName = "mapMovimento"; 
		log.debug(methodName, "Mapping "+siacDCollegamentoTipoEnum.getModelClass().getSimpleName() 
				+ " with uid only: "+src.getUid());
		
		dest.setUid(src.getUid());
	}
}
