/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTAzione;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTAzioneRepository.
 */
public interface SiacTAzioneRepository extends JpaRepository<SiacTAzione, Integer> {
	
	@Query(" FROM SiacTAzione ta "
			+ " WHERE ta.dataCancellazione IS NULL "
			+ " AND ta.azioneCode = :azioneCode "
			+ " AND ta.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	SiacTAzione findByAzioneCodeAndEnteProprietarioId(@Param("azioneCode") String azioneCode, @Param("enteProprietarioId") Integer enteProprietarioId);


}
