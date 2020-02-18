/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTMovEpNum;

/**
 * The Interface SiacTPrimaNotaNumRepository.
 */
public interface SiacTMovEpNumRepository extends JpaRepository<SiacTMovEpNum, Integer> {

	
	@Query(" FROM SiacTMovEpNum m  "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND m.movepAnno = :movepAnno "
			+ " AND m.siacDAmbito.ambitoCode = :ambitoCode "
			+ " AND m.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "              )")
	SiacTMovEpNum findByAnnoAndAmbito(@Param("movepAnno") Integer movepAnno, @Param("ambitoCode") String ambitoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	
	
}
