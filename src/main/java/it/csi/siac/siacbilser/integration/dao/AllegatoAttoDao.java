/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface AllegatoAttoDao.
 */
public interface AllegatoAttoDao extends Dao<SiacTAttoAllegato, Integer> {
	
	/**
	 * Crea una SiacTAttoAllegato.
	 *
	 * @param c la SiacTAttoAllegato da inserire
	 * 
	 * @return la SiacTAttoAllegato inserita
	 */
	SiacTAttoAllegato create(SiacTAttoAllegato c);

	/**
	 * Aggiorna una SiacTAttoAllegato.
	 *
	 * @param c la SiacTAttoAllegato da aggiornare
	 * 
	 * @return la SiacTAttoAllegato aggiornata
	 */
	SiacTAttoAllegato update(SiacTAttoAllegato c);

	/**
	 * Effettua la ricerca sintetica paginata con i filtri passati come parametro.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param causale the causale
	 * @param siacDAttoAllegatoStatoEnum the siac D atto allegato stato enum
	 * @param scadenzaDa the scadenza da
	 * @param scadenzaA the scadenza A
	 * @param attoAmministrativoId the atto amministrativo id
	 * @param soggettoId the soggetto id
	 * @param movgestId the movgest id
	 * @param movgestTsId the movgest ts id
	 * @param elencoId the elenco id
	 * @param attoalFlagRitenute the attoal flag ritenute
	 * @param siacDAttoAllegatoStatoEnums the siac D atto allegato stato enums
	 * @param bilAnno the bil anno
	 * @param hasImpegnoConfermaDurc 
	 * @param pageable the pageable
	 * @return la lista paginata di SiacTAttoAllegato
	 */
	Page<SiacTAttoAllegato> ricercaSinteticaAllegatoAtto(
			Integer enteProprietarioId,
			String causale,
			SiacDAttoAllegatoStatoEnum siacDAttoAllegatoStatoEnum,
			Date scadenzaDa,
			Date scadenzaA,
			List<Integer> attoAmministrativoId,
			Integer soggettoId,
			Integer movgestId,
			Integer movgestTsId,
			Integer elencoId,
			Boolean attoalFlagRitenute,
			List<SiacDAttoAllegatoStatoEnum> siacDAttoAllegatoStatoEnums,
			Integer bilAnno,
			Boolean hasImpegnoConfermaDurc, 
			Pageable pageable);
	
}
