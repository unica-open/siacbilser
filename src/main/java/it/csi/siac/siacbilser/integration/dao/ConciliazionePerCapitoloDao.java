/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneCapitolo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface ConciliazionePerCapitoloDao.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2015
 */
public interface ConciliazionePerCapitoloDao extends Dao<SiacRConciliazioneCapitolo, Integer> {
	
	SiacRConciliazioneCapitolo create(SiacRConciliazioneCapitolo r);

	SiacRConciliazioneCapitolo update(SiacRConciliazioneCapitolo r);

	void elimina(SiacRConciliazioneCapitolo siacRConciliazioneCapitolo);
	
	Page<SiacRConciliazioneCapitolo> ricercaSinteticaConciliazioniPerCapitolo(
			int enteProprietarioId, 
			Integer bilElemId, 
			Pageable pageable);
	
}
