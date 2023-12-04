/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegatoStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStampaTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface StampaAllegatoAttoDao extends Dao<SiacTAttoAllegatoStampa, Integer> {
		
	/**
	 * Crea una SiacTAttoAllegatoStampa.
	 * 
	 * @param siacTAttoAllegatoStampa la SiacTAttoAllegatoStampa da inserire
	 * @return la SiacTAttoAllegatoStampa inserita
	 */
	SiacTAttoAllegatoStampa create(SiacTAttoAllegatoStampa siacTAttoAllegatoStampa);
	
	
	/**
	 * Ricerca stampa allegato atto.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param attoalstTipoCode the tipoStampa
	 * @param attoalstAnno the anno
	 * @param attoammid the id of atto amm
	 * @param dataCreazione the data creazione
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTAttoAllegatoStampa> ricercaSinteticaStampaAllegatoAtto(Integer enteProprietarioId, SiacDAttoAllegatoStampaTipoEnum attoalstTipoCode,  Integer bilId, Integer attoammid, Date dataCreazione, Pageable pageable);
}
