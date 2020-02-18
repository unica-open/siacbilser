/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoRuolo;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTSoggettoRepository.
 */
public interface SiacTSoggettoRepository extends JpaRepository<SiacTSoggetto, Integer> {
	
	
	/**
	 * Find soggetto stato code by soggetto id.
	 *
	 * @param soggettoId the soggetto id
	 * @return the string
	 */
	@Query(	   "SELECT rss.siacDSoggettoStato.soggettoStatoCode "
			+ " FROM SiacRSoggettoStato rss "
			+ " WHERE rss.siacTSoggetto.soggettoId = :soggettoId "
			+ "   AND rss.dataCancellazione is NULL "
			+ "   AND rss.dataInizioValidita < CURRENT_TIMESTAMP "
			+ "   AND (rss.dataFineValidita is NULL or rss.dataFineValidita > CURRENT_TIMESTAMP)"
			)
	String findSoggettoStatoCodeBySoggettoId(@Param("soggettoId") Integer soggettoId);
	
	
	/**
	 * Find soggetto by id movgest ts.
	 *
	 * @param movgestTsId the movgest ts id
	 * @return the list
	 */
	@Query(" SELECT r.siacTSoggetto "
			+ "  FROM SiacRMovgestTsSog r"
			+ " WHERE r.siacTMovgestT.movgestTsId = :movgestTsId "
			+ "   AND r.dataCancellazione is NULL "
			+ "   AND r.dataInizioValidita < CURRENT_TIMESTAMP "
			+ "   AND (r.dataFineValidita is NULL or r.dataFineValidita > CURRENT_TIMESTAMP)")
	List<SiacTSoggetto> findSoggettoByIdMovgestTs(@Param("movgestTsId") Integer movgestTsId);
	
	/**
	 * Find soggetto by id movgest.
	 *
	 * @param movgestId the movgest id
	 * @return the list
	 */
	@Query("   SELECT r.siacTSoggetto "
			+ "  FROM SiacRMovgestTsSog r"
			+ "	 WHERE r.siacTMovgestT.siacTMovgest.movgestId = :movgestId "
			+ "   AND r.siacTMovgestT.siacTMovgestIdPadre is NULL "
			+ "   AND r.dataCancellazione is NULL "
			+ "   AND r.dataInizioValidita < CURRENT_TIMESTAMP "
			+ "   AND (r.dataFineValidita is NULL or r.dataFineValidita > CURRENT_TIMESTAMP)")
	List<SiacTSoggetto> findSoggettoByIdMovgest(@Param("movgestId") Integer movgestId);
	
	
	/**
	 * Find soggetto by ente id.
	 *
	 * @param enteId the ente id
	 * @return the list
	 */
	@Query("   SELECT r.siacTSoggetto "
			+ "  FROM SiacRSoggettoEnteProprietario r"
			+ " WHERE r.siacTEnteProprietario.enteProprietarioId = :enteId "
			+ "   AND r.dataCancellazione is NULL "
			+ "   AND r.dataInizioValidita < CURRENT_TIMESTAMP "
			+ "   AND (r.dataFineValidita is NULL or r.dataFineValidita > CURRENT_TIMESTAMP)")
	List<SiacTSoggetto> findSoggettoByEnteId(@Param("enteId") Integer enteId);

	@Query(" FROM SiacDSoggettoClasse s"
			+ " WHERE s.dataCancellazione is NULL "
			+ " AND EXISTS ( "
			+ "     FROM s.siacRSoggettoClasses r "
			+ "     WHERE r.dataCancellazione IS NULL "
			+ "     AND r.siacTSoggetto.soggettoId = :soggettoId "
			+ " ) "
			+ "   AND s.dataInizioValidita < CURRENT_TIMESTAMP "
			+ "   AND (s.dataFineValidita is NULL or s.dataFineValidita > CURRENT_TIMESTAMP)")
	List<SiacDSoggettoClasse> findSiacDSoggettoClasseBySiacTSoggetto(@Param("soggettoId") Integer soggettoId);

	@Query(" FROM SiacDSoggettoClasse s"
			+ " WHERE s.dataCancellazione is NULL "
			+ " AND EXISTS ( "
			+ "     FROM s.siacRMovgestTsSogclasses r "
			+ "     WHERE r.dataCancellazione IS NULL "
			+ "     AND r.siacTMovgestT.movgestTsId = :movgestTsId "
			+ " ) "
			+ "   AND s.dataInizioValidita < CURRENT_TIMESTAMP "
			+ "   AND (s.dataFineValidita is NULL or s.dataFineValidita > CURRENT_TIMESTAMP)")
	SiacDSoggettoClasse findSiacDSoggettoClasseBySiacTMovgestTs(@Param("movgestTsId") Integer movgestTsId);
	
	@Query(" FROM SiacDSoggettoClasse s"
			+ " WHERE s.dataCancellazione is NULL "
			+ " AND EXISTS ( "
			+ "     FROM s.siacRMovgestTsSogclasses r "
			+ "     WHERE r.dataCancellazione IS NULL "
			+ "     AND r.siacTMovgestT.siacTMovgest.movgestId = :movgestId "
			+ " ) "
			+ "   AND s.dataInizioValidita < CURRENT_TIMESTAMP "
			+ "   AND (s.dataFineValidita is NULL or s.dataFineValidita > CURRENT_TIMESTAMP)")
	SiacDSoggettoClasse findSiacDSoggettoClasseBySiacTMovgest(@Param("movgestId") Integer movgestId);

	@Query(" FROM SiacTSoggetto s "
			+ " WHERE s.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM s.siacRDocSogs rds "
			+ "     WHERE rds.dataCancellazione IS NULL "
			+ "     AND rds.siacTDoc.docId = :docId "
			+ " ) ")
	List<SiacTSoggetto> findSoggettoByDocId(@Param("docId") Integer docId);
	
	@Query(   "  FROM  SiacRSoggettoRuolo r "
			+ " WHERE r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "   AND r.dataCancellazione IS NULL "
			+ "   AND r.siacTSoggetto.soggettoId = :soggettoId "
			+ "   AND EXISTS(  FROM r.siacDRuolo dr "
			+ "           	  WHERE dr.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId  "
			+ "                 AND dr.ruoloCode = :ruoloCode "
			+ "           	    AND dr.dataCancellazione IS NULL ) "
			)
	List<SiacRSoggettoRuolo> findSiacSiacRSoggettoRuolo(
			@Param("soggettoId") Integer soggettoId,
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("ruoloCode") String ruoloCode);


	@Query("   SELECT c.siacTSoggetto "
			+ "  FROM SiacTCassaEcon c"
			+ " WHERE c.cassaeconId = :cassaeconId ")
	SiacTSoggetto findSoggettoByCassaEconomale(@Param("cassaeconId") Integer cassaeconId);

	@Query("SELECT stm " + 
			"FROM SiacTSoggetto s, " +
			"     SiacTModpag stm, " +
			"	  SiacRSoggettoRelaz sz, " +
			"	  SiacRSoggrelModpag sr	" +
			"WHERE sz.soggettoRelazId = :soggettoRelazId AND " +	
			"      sz.siacTSoggetto2.soggettoId = s.soggettoId AND "+	
			"	   sr.siacTModpag.modpagId = stm.modpagId AND " +
			"	   sr.siacRSoggettoRelaz.soggettoRelazId = sz.soggettoRelazId"
			)
	SiacTModpag ricercaModalitaPagamentoCessionePerChiaveDef(@Param("soggettoRelazId") Integer cessioneId);
	
	@Query(" FROM SiacTSoggetto ts "
			+ " WHERE ts.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM SiacRDocSog rds, SiacRElencoDocSubdoc reds "
			+ "     WHERE rds.siacTSoggetto = ts "
			+ "     AND rds.siacTDoc = reds.siacTSubdoc.siacTDoc "
			+ "     AND rds.dataCancellazione IS NULL "
			+ "     AND reds.dataCancellazione IS NULL "
			+ "     AND reds.siacTElencoDoc.eldocId = :eldocId "
			+ " ) ")
	List<SiacTSoggetto> findByEldocId(@Param("eldocId") Integer eldocId);
	
	@Query(" FROM SiacTSoggetto ts "
			+ " WHERE ts.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM SiacRDocSog rds, SiacRElencoDocSubdoc reds, SiacRAttoAllegatoElencoDoc raaed "
			+ "     WHERE rds.siacTSoggetto = ts "
			+ "     AND rds.siacTDoc = reds.siacTSubdoc.siacTDoc "
			+ "     AND raaed.siacTElencoDoc = reds.siacTElencoDoc "
			+ "     AND rds.dataCancellazione IS NULL "
			+ "     AND reds.dataCancellazione IS NULL "
			+ "     AND raaed.dataCancellazione IS NULL "
			+ "     AND raaed.siacTAttoAllegato.attoalId = :attoalId "
			+ " ) ")
	List<SiacTSoggetto> findByAttoalId(@Param("attoalId") Integer attoalId);
	
	@Query("SELECT srl.siacTSoggetto2 "
            + " FROM SiacRSoggettoRelaz srl "
			+ " WHERE srl.dataCancellazione IS NULL" 
            + " AND srl.siacTSoggetto1.soggettoId = :soggettoIdDa "
            + " AND srl.siacTSoggetto2.soggettoId = :soggettoIdA "
            + " AND srl.siacDRelazTipo.relazTipoCode = :relazTipoCode " 
            + " ORDER BY srl.dataCreazione")
	List<SiacTSoggetto> findSoggettoIdABySoggettoIdDaAndRelazioneCode(@Param("soggettoIdDa") Integer soggettoIdDa, @Param("soggettoIdA") Integer soggettoIdA, @Param("relazTipoCode") String relazTipoCode);
}
