/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import it.csi.siac.siacbilser.integration.entity.SiacDCespitiVariazioneStato;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiVariazioneStatoDao.
 *
 * @author Antonino
 */
public interface CespitiVariazioneStatoDao extends Dao<SiacDCespitiVariazioneStato, Integer> {
	
	SiacDCespitiVariazioneStato create(SiacDCespitiVariazioneStato r);

	SiacDCespitiVariazioneStato update(SiacDCespitiVariazioneStato r);

}
