/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNotaNum;

/**
 * The Interface SiacTPrimaNotaNumRepository.
 */
public interface SiacTPrimaNotaNumRepository extends JpaRepository<SiacTPrimaNotaNum, Integer> {

	
	@Query(" FROM SiacTPrimaNotaNum p  "
			+ " WHERE p.dataCancellazione IS NULL "
			+ " AND p.pnotaAnno = :pnotaAnno "
			+ " AND p.siacDAmbito.ambitoCode = :ambitoCode "
			+ " AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "              )")
	SiacTPrimaNotaNum findByAnnoAndAmbito(@Param("pnotaAnno") String pnotaAnno, @Param("ambitoCode") String ambitoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	
	
}
