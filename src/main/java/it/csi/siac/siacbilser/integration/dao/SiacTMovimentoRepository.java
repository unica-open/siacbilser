/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;

/**
 * The Interface SiacTMovimentoRepository.
 */
public interface SiacTMovimentoRepository extends JpaRepository<SiacTMovimento, Integer> {

	
	
	@Query(" FROM SiacTMovimento m "
			+ " WHERE DATE_TRUNC('day', CAST(m.movtData AS date)) = DATE_TRUNC('day', CAST(:movtData AS date)) "
		//	+ " AND m.siacTGiustificativo.siacTRichiestaEcon.siacTCassaEcon.cassaeconId = :cassaeconId"
//			+ " AND ("
//			+ "      m.siacTRichiestaEcon.siacTCassaEcon.cassaeconId = :cassaeconId "
//			+ "      OR m.siacTGiustificativo.siacTRichiestaEcon.siacTCassaEcon.cassaeconId = :cassaeconId "
//			+ "     )"
			+ " AND ("
			+ "     EXISTS ( FROM SiacTCassaEcon ce WHERE ce = m.siacTRichiestaEcon.siacTCassaEcon AND ce.cassaeconId = :cassaeconId AND m.siacTRichiestaEcon.siacTBil.bilId = :bilId)"
			+ "     OR EXISTS ( FROM SiacTCassaEcon ce WHERE ce = m.siacTGiustificativo.siacTRichiestaEcon.siacTCassaEcon AND ce.cassaeconId = :cassaeconId AND m.siacTGiustificativo.siacTRichiestaEcon.siacTBil.bilId = :bilId)"
			+ "     )"
			+ " AND m.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND m.dataCancellazione IS NULL "
			+ " ORDER BY m.movtNumero")
	List<SiacTMovimento> findByDataMovimentoCassaEconId(@Param("movtData") Date movtData, @Param("cassaeconId") Integer cassaeconId, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId);
	
	@Query(" FROM SiacTMovimento m "
			+ " WHERE ("
			+ "		(DATE_TRUNC('day', CAST(m.movtData AS date)) >= DATE_TRUNC('day', CAST(:movtDataInizio AS date)) )"
			+ "		AND  (DATE_TRUNC('day', CAST(m.movtData AS date)) <= DATE_TRUNC('day', CAST(:movtDataFine AS date)) )"
			+ "     )"
			+ " AND ("
			+ "     EXISTS ( FROM SiacTCassaEcon ce WHERE ce = m.siacTRichiestaEcon.siacTCassaEcon AND ce.cassaeconId = :cassaeconId AND m.siacTRichiestaEcon.siacTBil.bilId = :bilId)"
			+ "     OR EXISTS ( FROM SiacTCassaEcon ce WHERE ce = m.siacTGiustificativo.siacTRichiestaEcon.siacTCassaEcon AND ce.cassaeconId = :cassaeconId AND m.siacTGiustificativo.siacTRichiestaEcon.siacTBil.bilId = :bilId)"
			+ "     )"
			+ " AND m.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND m.dataCancellazione IS NULL "
			+ " ORDER BY m.movtNumero")
	List<SiacTMovimento> findByPeriodoMovimentoCassaEconId(@Param("movtDataInizio") Date movtDataInizio,@Param("movtDataFine") Date movtDataFine, @Param("cassaeconId") Integer cassaeconId, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId);
	
	@Query(" FROM SiacTMovimento m "
			+ " WHERE ("
			+ "		(DATE_TRUNC('day', CAST(m.movtData AS date)) >= DATE_TRUNC('day', CAST(:movtDataInizio AS date)) )"
			+ "		AND  (DATE_TRUNC('day', CAST(m.movtData AS date)) <= DATE_TRUNC('day', CAST(:movtDataFine AS date)) )"
			+ "     )"
			+ " AND ("
			+ "     EXISTS ( FROM SiacTCassaEcon ce WHERE ce = m.siacTRichiestaEcon.siacTCassaEcon AND ce.cassaeconId = :cassaeconId AND m.siacTRichiestaEcon.siacTBil.bilId = :bilId)"
			+ "     OR EXISTS ( FROM SiacTCassaEcon ce WHERE ce = m.siacTGiustificativo.siacTRichiestaEcon.siacTCassaEcon AND ce.cassaeconId = :cassaeconId AND m.siacTGiustificativo.siacTRichiestaEcon.siacTBil.bilId = :bilId)"
			+ "     )"
			+ " AND m.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND m.dataCancellazione IS NULL ")
	Page<SiacTMovimento> findByPeriodoMovimentoCassaEconId(@Param("movtDataInizio") Date movtDataInizio,@Param("movtDataFine") Date movtDataFine, @Param("cassaeconId") Integer cassaeconId, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId, Pageable pageable);
	
	@Query(" FROM SiacTMovimento m "
			+ " WHERE  ("
			+ "		(DATE_TRUNC('day', CAST(m.movtData AS date)) >= DATE_TRUNC('day', CAST(:movtDataInizio AS date)) )"
			+ "		AND  (DATE_TRUNC('day', CAST(m.movtData AS date)) <= DATE_TRUNC('day', CAST(:movtDataFine AS date)) )"
			+ "     )"
//			+ " AND ( "
//			+ "     m.siacTGiustificativo.siacTRichiestaEcon.siacTBil.bilId = :bilId "
//			+ "     OR m.siacTRichiestaEcon.siacTBil.bilId = :bilId "
//			+ " ) "
			+ " AND ( "
			+ "     EXISTS ( "
			+ "         FROM SiacTGiustificativo tg "
			+ "         WHERE m.siacTGiustificativo = tg "
			+ "         AND tg.dataCancellazione IS NULL "
			+ "         AND tg.siacTRichiestaEcon.siacTBil.bilId = :bilId "
			+ "     ) "
			+ "     OR EXISTS ( "
			+ "         FROM SiacTRichiestaEcon tre "
			+ "         WHERE m.siacTRichiestaEcon = tre "
			+ "         AND tre.dataCancellazione IS NULL "
			+ "         AND tre.siacTBil.bilId = :bilId "
			+ "     ) "
			+ " ) "
			+ " AND ("
			+ "     EXISTS ( FROM SiacTCassaEcon ce WHERE ce = m.siacTRichiestaEcon.siacTCassaEcon AND ce.cassaeconId = :cassaeconId)"
			+ "     OR EXISTS ( FROM SiacTCassaEcon ce WHERE ce = m.siacTGiustificativo.siacTRichiestaEcon.siacTCassaEcon AND ce.cassaeconId = :cassaeconId)"
			+ "     )"
			+ " AND m.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND m.dataCancellazione IS NULL "
			+ " ORDER BY m.movtNumero")
	List<SiacTMovimento> findAllMovimentiBilancioTillDateCassaEconId(@Param("movtDataInizio") Date movtDataInizio,@Param("movtDataFine") Date movtDataFine, @Param("cassaeconId") Integer cassaeconId, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId);
	

}
