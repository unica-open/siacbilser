/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
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
public class RegistrazioneMovFinEventoMovimentoModelDetailConverter extends RegistrazioneMovFinEventoMovimentoConverter {
	
	@Override
	protected void mapMovimento(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, SiacTBase src, Entita dest) {
		
		String methodName = "mapMovimento"; 
		log.debug(methodName, "Mapping "+siacDCollegamentoTipoEnum.getModelClass().getSimpleName() 
				+ " with MapId: " +siacDCollegamentoTipoEnum.getMapId().name() + "[uid: "+src.getUid()+"]");
		
		mapNotNull(src, dest, siacDCollegamentoTipoEnum.getMapIdModelDetail(),
				Converters.byModelDetails(Utility.MDTL.byModelDetailClass(siacDCollegamentoTipoEnum.getModelDetailClass())));
	}
}
