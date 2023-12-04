/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;


public interface SiacTNazioneRepository extends JpaRepository<SiacTNazioneFin, Integer> {
	
	
	
	@Query("FROM SiacTNazioneFin s WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId  ORDER by nazioneDesc ")
	public List<SiacTNazioneFin> findNazioni(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query("FROM SiacTNazioneFin s WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId  AND nazioneCode=:nazioneCode ")
	public SiacTNazioneFin findByCodice(
			@Param("nazioneCode") String nazioneCode,
			@Param("enteProprietarioId") Integer enteProprietarioId
			);
	
}
