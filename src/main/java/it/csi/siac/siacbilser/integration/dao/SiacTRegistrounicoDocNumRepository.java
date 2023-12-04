/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTRegistrounicoDocNum;

/**
 * The Interface SiacTRegistrounicoDocNumRepository.
 */
public interface SiacTRegistrounicoDocNumRepository extends JpaRepository<SiacTRegistrounicoDocNum, Integer> {
	
	
	/**
	 * Find by anno and ente proprietario id.
	 *
	 * @param anno the anno
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac t subdoc num
	 */
	@Query("FROM SiacTRegistrounicoDocNum WHERE rudocRegistrazioneAnno = :rudocRegistrazioneAnno AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	SiacTRegistrounicoDocNum findByAnnoAndEnteProprietarioId(@Param("rudocRegistrazioneAnno") Integer anno, @Param("enteProprietarioId") Integer enteProprietarioId);
	

	
}
