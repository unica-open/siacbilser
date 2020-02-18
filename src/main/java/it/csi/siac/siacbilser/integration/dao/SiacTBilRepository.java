/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDFaseOperativa;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTBilRepository.
 */
public interface SiacTBilRepository extends JpaRepository<SiacTBil, Integer> {
	
	/**
	 * Cerca Fase di bilincioCerca il periodo annuale valido attualmente per l'ente selezionato.
	 *
	 * @param periodo the periodo
	 * @param ente the ente
	 * @return the fase
	 */
	@Query( "SELECT fo.siacDFaseOperativa" +
			" FROM SiacTBil b" +
			" INNER JOIN b.siacRBilFaseOperativas fo" +
			" WHERE b.siacTPeriodo = :periodo" +
			" and b.siacTEnteProprietario = :ente" +
			" and b.dataCancellazione is null " +
			" and b.dataInizioValidita < CURRENT_DATE " +
			" and ( b.dataFineValidita is null or b.dataFineValidita > CURRENT_DATE)" +
	        " and fo.dataCancellazione is null " +
			" and fo.dataInizioValidita < CURRENT_DATE " +
			" and ( fo.dataFineValidita is null or fo.dataFineValidita > CURRENT_DATE)")
	SiacDFaseOperativa getFase(
			@Param("periodo")SiacTPeriodo periodo,
			@Param("ente")SiacTEnteProprietario ente);
	
	
	
	@Query( "SELECT b " +
			" FROM SiacTBil b" +
			" WHERE b.siacTPeriodo.anno = :anno" +
			" AND b.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId" +
			" AND b.dataCancellazione is null " +
			" AND EXISTS (FROM b.siacRBilFaseOperativas r " +
			"             WHERE r.dataCancellazione IS NULL" +
			"             AND r.dataInizioValidita < CURRENT_DATE " +
			"             AND ( r.dataFineValidita is null OR r.dataFineValidita > CURRENT_DATE) " +
			"             AND r.siacDFaseOperativa.faseOperativaCode = :faseOperativaCode" +
			"            )" +
			" AND b.dataInizioValidita < CURRENT_DATE " +
			" AND ( b.dataFineValidita is null or b.dataFineValidita > CURRENT_DATE)"+
			" AND b.siacTPeriodo.siacDPeriodoTipo.periodoTipoCode= :periodoTipoCode"+
			" AND b.siacTPeriodo.siacDPeriodoTipo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			)
	SiacTBil getSiacTBilByAnnoAndFase(
			@Param("anno") String anno,
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("faseOperativaCode") String faseOperativaCode,
	        @Param("periodoTipoCode") String periodoTipoCode);
	
	
	@Query( " FROM SiacTBil b" +
			" WHERE b.siacTPeriodo.anno = :anno" +
			" AND b.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND b.dataCancellazione IS null " +
			" AND b.siacTPeriodo.siacDPeriodoTipo.periodoTipoCode= :periodoTipoCode "
			)
	SiacTBil getSiacTBilByAnno(
			@Param("anno") String anno,
			@Param("enteProprietarioId") Integer enteProprietarioId,
	        @Param("periodoTipoCode") String periodoTipoCode);
	
	
	@Query( " FROM SiacTBil b" +
			" WHERE b.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND b.dataCancellazione IS null " +
			" AND b.siacTPeriodo.anno = ( SELECT MAX(t.siacTPeriodo.anno) "+ 
			" 								FROM SiacTBil t " +
			"								WHERE t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" 								AND t.dataCancellazione IS NULL " +
			"								AND t.dataInizioValidita < CURRENT_DATE " +
			" 								AND (t.dataFineValidita IS NULL OR t.dataFineValidita > CURRENT_DATE) " +
			" 								AND EXISTS (FROM t.siacRBilFaseOperativas r " +
			"     								        WHERE r.dataCancellazione IS NULL" +
			"           							    AND r.dataInizioValidita < CURRENT_DATE " +
			"          								    AND ( r.dataFineValidita is null OR r.dataFineValidita > CURRENT_DATE) " +
			"           							    AND r.siacDFaseOperativa.faseOperativaCode <> :faseOperativaCode " +
			"           								)"+
			"							)"+
			" ORDER BY b.dataCreazione DESC"
			)
	List<SiacTBil> getSiacTBilByEnteAndNotFaseOperativaCode(
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("faseOperativaCode") String faseOperativaCode);


	@Query(" SELECT MAX(b.siacTPeriodo.anno) "+ 
			" FROM SiacTBil b " +
			" WHERE b.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND b.dataCancellazione IS NULL " +
			" AND b.dataInizioValidita < CURRENT_DATE " +
			" AND (b.dataFineValidita IS NULL OR b.dataFineValidita > CURRENT_DATE) " +
			" AND EXISTS (FROM b.siacRBilFaseOperativas r " +
			"             WHERE r.dataCancellazione IS NULL" +
			"             AND r.dataInizioValidita < CURRENT_DATE " +
			"             AND ( r.dataFineValidita is null OR r.dataFineValidita > CURRENT_DATE) " +
			"             AND r.siacDFaseOperativa.faseOperativaCode <> :faseOperativaCode " +
			"            )" 
		    )
	String getMaxAnnoSiacTBilByEnteAndNotFaseOperativaCode(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("faseOperativaCode") String faseOperativaCode);


	@Query(" FROM SiacTBil tb "
			+ " WHERE tb.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM SiacTBil tb2 "
			+ "     WHERE tb2.bilId = :bilId "
			+ "     AND tb2.dataCancellazione IS NULL "
			+ "     AND tb2.siacTEnteProprietario = tb.siacTEnteProprietario "
			+ "     AND CAST(tb2.siacTPeriodo.anno AS int) = CAST(tb.siacTPeriodo.anno AS int) - :delta"
			+ " ) ")
	List<SiacTBil> findByBilIdAndDeltaAnno(@Param("bilId") Integer bilId, @Param("delta") Integer delta);
	
	
	//SIAC-5387
	@Query( "SELECT fo.siacDFaseOperativa" +
			" FROM SiacTBil b" +
			" INNER JOIN b.siacRBilFaseOperativas fo" +
			" WHERE b.bilId = :bilId" +
			" and b.dataCancellazione is null " +
			" and b.dataInizioValidita < CURRENT_DATE " +
			" and ( b.dataFineValidita is null or b.dataFineValidita > CURRENT_DATE)" +
	        " and fo.dataCancellazione is null " +
			" and fo.dataInizioValidita < CURRENT_DATE " +
			" and ( fo.dataFineValidita is null or fo.dataFineValidita > CURRENT_DATE)")
	SiacDFaseOperativa getFaseById(@Param("bilId") Integer bilId);
}