/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneTitolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface ConciliazionePerTitoloDao.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/10/2015
 */
public interface ConciliazionePerTitoloDao extends Dao<SiacRConciliazioneTitolo, Integer> {
	
	SiacRConciliazioneTitolo create(SiacRConciliazioneTitolo r);

	SiacRConciliazioneTitolo update(SiacRConciliazioneTitolo r);

	void elimina(SiacRConciliazioneTitolo siacRConciliazioneTitolo);

	Page<SiacRConciliazioneTitolo> ricercaSinteticaConciliazioniPerTitolo(
			int enteProprietarioId,
			SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum,
			Integer classifId,
			Integer titoloId,
			Integer tipologiaId,
			Pageable pageable);

	
}
