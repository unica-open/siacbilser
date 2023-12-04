/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNotaProgressivogiornaleNum;

/**
 * The Interface SiacTPrimaNotaNumRepository.
 */
public interface SiacTPrimaNotaProgressivogiornaleNumRepository extends JpaRepository<SiacTPrimaNotaProgressivogiornaleNum, Integer> {

	
	@Query(" FROM SiacTPrimaNotaProgressivogiornaleNum p  "
			+ " WHERE p.dataCancellazione IS NULL "
			+ " AND p.pnotaAnno = :pnotaAnno "
			+ " AND p.siacDAmbito.ambitoCode = :ambitoCode "
			+ " AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "              )")
	SiacTPrimaNotaProgressivogiornaleNum findByAnnoAndAmbito(@Param("pnotaAnno") String pnotaAnno, @Param("ambitoCode") String ambitoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	
	
}
