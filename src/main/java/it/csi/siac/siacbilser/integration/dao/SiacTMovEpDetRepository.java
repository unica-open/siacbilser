/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;

/**
 * The Interface SiacTPrimaNotaNumRepository.
 */
public interface SiacTMovEpDetRepository extends JpaRepository<SiacTMovEpDet, Integer> {

	
	@Query(" FROM SiacTMovEpNum m  "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND m.movepAnno = :movepAnno "
			+ " AND m.siacDAmbito.ambitoCode = :ambitoCode "
			+ " AND m.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "              )")
	SiacTMovEpDet findMovimentoDettaglioCespiteByMovEpId(@Param("movepAnno") Integer movepAnno, @Param("ambitoCode") String ambitoCode, @Param("enteProprietarioId") Integer enteProprietarioId);

	@Query("SELECT movepdet "
			+ " FROM "
			+ "  	 SiacTMovEpDet		movepdet  "
			+ "  	,SiacTPdceConto		pdcec "
			+ "  	,SiacDPdceContoTipo pdcectipo "
			+ " WHERE "
			+ " 	    movepdet.siacTPdceConto.pdceContoId = pdcec.pdceContoId "
			+ "		AND pdcec.siacDPdceContoTipo.pdceCtTipoId  = pdcectipo.pdceCtTipoId "
			+ " 	AND pdcectipo.pdceCtTipoCode = 'CES' "
			+ " 	AND movepdet.siacTMovEp.movepId = :movepId " )
	SiacTMovEpDet findMovEpCespiteById(@Param("movepId") Integer movepId);

	
	
}
