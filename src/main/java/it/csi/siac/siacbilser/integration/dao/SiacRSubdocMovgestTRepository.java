/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;

/**
 * Repository per l'entity SiacRSubdocMovgestT.
 *
 */
public interface SiacRSubdocMovgestTRepository extends JpaRepository<SiacRSubdocMovgestT, Integer> {
	
	String condizioneRecordValido = " ( (rsmt.dataInizioValidita < CURRENT_TIMESTAMP) AND (rsmt.dataFineValidita IS NULL OR CURRENT_TIMESTAMP < rsmt.dataFineValidita) AND rsmt.dataCancellazione IS NULL ) ";
	
	@Query(" FROM SiacRSubdocMovgestT r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.subdocId = :subdocId ")
	public List<SiacRSubdocMovgestT> findBySubdocId(@Param("subdocId") Integer subdocId);
	
	@Query(" FROM SiacRSubdocMovgestT r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.subdocId = :subdocId "
			+ " AND r.siacTMovgestT.siacTMovgest.movgestId = :movgestId ")
	public List<SiacRSubdocMovgestT> findBySubdocIdAndMovgestId(@Param("subdocId") Integer subdocId, @Param("movgestId") Integer movgestId);
	
	@Query(" FROM SiacRSubdocMovgestT r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.subdocId = :subdocId "
			+ " AND r.siacTMovgestT.movgestTsId = :movgestTsId ")
	public List<SiacRSubdocMovgestT> findBySubdocIdAndMovgestTsId(@Param("subdocId") Integer subdocId, @Param("movgestTsId") Integer movgestTsId);
	
	
	@Query(" FROM SiacRSubdocMovgestT rsmt "
			+ " WHERE rsmt.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId "
			+ " AND rsmt.siacTSubdoc.subdocId = :subdocId "
			+ " AND rsmt.siacTMovgestT.siacTMovgest.movgestAnno = :movgestAnno "
			+ " AND rsmt.siacTMovgestT.siacTMovgest.movgestNumero = :movgestNumero "
			+ " AND rsmt.siacTMovgestT.movgestTsCode = :movgestTsCode "
			+ " AND rsmt.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = :movgestTsTipoCode "	
			+ " AND rsmt.dataFineValidita IS NOT NULL "
			+ " ORDER BY rsmt.dataFineValidita DESC "
			)
	public  List<SiacRSubdocMovgestT> findRelazioneCancellataBySubdocIdAndBilIdAndChiaveMovgestOrderByValiditaFineDesc(@Param("subdocId") Integer subdocId, @Param("movgestAnno") Integer movgestAnno,
			@Param("movgestNumero") BigDecimal movgestNumero, @Param("movgestTsCode") String movgestTsCode, @Param("movgestTsTipoCode") String movgestTsTipoCode, @Param("bilId") Integer bilId);
	
	
	@Query(" FROM SiacRSubdocMovgestT rsmt "
			+ " WHERE rsmt.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId "
			+ " AND rsmt.siacTSubdoc.subdocId = :subdocId "
			+ " AND rsmt.siacTMovgestT.siacTMovgest.movgestAnno = :movgestAnno "
			+ " AND rsmt.siacTMovgestT.siacTMovgest.movgestNumero = :movgestNumero "
			+ " AND rsmt.siacTMovgestT.movgestTsCode = :movgestTsCode "
			+ " AND rsmt.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = :movgestTsTipoCode "			
			+ " AND rsmt.dataFineValidita IS NULL "
			+ " AND rsmt.dataCancellazione IS NULL "			
			)
	public List<SiacRSubdocMovgestT> findUltimaRelazioneValidaBySubdocIdAndBilIdAndChiaveMovgest(@Param("subdocId") Integer subdocId, @Param("movgestAnno") Integer movgestAnno,
			@Param("movgestNumero") BigDecimal movgestNumero, @Param("movgestTsCode") String movgestTsCode, @Param("movgestTsTipoCode") String movgestTsTipoCode, @Param("bilId") Integer bilId);
	
	
	@Query(  " SELECT tm.movgestAnno, tm.movgestNumero, tmt.movgestTsCode, tmt.siacDMovgestTsTipo.movgestTsTipoCode "
			+ " FROM SiacRSubdocMovgestT rsmt, SiacTMovgestT tmt, SiacTMovgest tm "
			+ " WHERE rsmt.siacTMovgestT = tmt "
			+ " AND tmt.siacTMovgest = tm "
			+ " AND rsmt.siacTSubdoc.subdocId = :subdocId "
			+ " AND rsmt.dataFineValidita IS NULL "
			+ " AND rsmt.dataCancellazione IS NULL "
			+ " AND tm.dataFineValidita IS NULL "
			+ " AND tm.dataCancellazione IS NULL "
			+ " AND tmt.dataFineValidita IS NULL "
			+ " AND tmt.dataCancellazione IS NULL "
			)
	public List<Object[]> findChiaveMovimentoGestioneAssociatoBySubdocId(@Param("subdocId") Integer subdocId);
	
//	@Query(" FROM SiacRSubdocMovgestT rsmt "
//			+ " WHERE rsmt.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId "
//			+ " AND rsmt.siacTMovgestT.siacTMovgest.movgestAnno = :movgestAnno "
//			+ " AND rsmt.siacTMovgestT.siacTMovgest.movgestNumero = :movgestNumero "
//			+ " AND rsmt.siacTMovgestT.movgestTsCode = :movgestTsCode "
//			+ " AND rsmt.siacTSubdoc.subdocId = :subdocId "
//			+ " AND rsmt.dataFineValidita IS NOT NULL "
//			+ " ORDER BY rsmt.dataFineValidita DESC "
//			)
//	public  List<SiacRSubdocMovgestT> findRelazioniCancellataBySubdocIdAndBilIdAndChiaveMovgestTsOrderByValiditaFineDesc(@Param("subdocId") Integer subdocId, @Param("movgestAnno") Integer movgestAnno,
//			@Param("movgestNumero") BigDecimal movgestNumero, @Param("movgestTsCode") String movgestTsCode, @Param("bilId") Integer bilId);
//	
//	@Query(" FROM SiacRSubdocMovgestT rsmt "
//			+ " WHERE rsmt.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId "
//			+ " AND rsmt.siacTSubdoc.subdocId = :subdocId "
//			+ " AND rsmt.dataFineValidita IS NOT NULL "
//			+ " ORDER BY rsmt.dataFineValidita DESC "
//			)
//	public  List<SiacRSubdocMovgestT> findRelazioniCancellataBySubdocIdAndBilIdOrderByValiditaFineDesc(@Param("subdocId") Integer subdocId, @Param("bilId") Integer bilId);
//	
//	@Query(" FROM SiacRSubdocMovgestT rsmt "
//			+ " WHERE rsmt.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId "
//			+ " AND rsmt.siacTSubdoc.subdocId = :subdocId "
//			+ " AND rsmt.dataFineValidita IS NULL "
//			+ " AND rsmt.dataCancellazione IS NULL "			
//			)
//	public List<SiacRSubdocMovgestT> findUltimaRelazioneValidaBySubdocIdAndBilId(@Param("subdocId") Integer subdocId, @Param("bilId") Integer bilId);
	
}