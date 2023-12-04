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

import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRDocOnereRepository.
 */
public interface SiacRDocOnereRepository extends JpaRepository<SiacRDocOnere, Integer> {

	
	/**
	 * Find siac r doc onere by doc id.
	 *
	 * @param docId the doc id
	 * @return the list
	 */
	@Query(  "SELECT r " +
			" FROM SiacRDocOnere r " +
			" WHERE r.siacTDoc.docId = :docId " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY r.siacTDoc.docNumero")
	List<SiacRDocOnere> findSiacRDocOnereByDocId(@Param("docId") Integer docId);
	
	@Query(  "SELECT COALESCE ( SUM ( rdo.importoCaricoSoggetto ), 0 ) " +
			" FROM SiacRDocOnere rdo " +
			" WHERE rdo.dataCancellazione IS NULL " +
			" AND rdo.siacTDoc.docId = :docId " +
			" AND EXISTS ( " +
			"     FROM rdo.siacDOnere.siacROnereSplitreverseIvaTipos r " +
			"     WHERE r.dataCancellazione IS NULL " +
			"     AND r.siacDSplitreverseIvaTipo.srivaTipoCode = :srivaTipoCode " +
			" ) ")
	BigDecimal sommaImportiOneriStessoTipoSR(@Param("docId") Integer docId, @Param("srivaTipoCode") String code);
	
	@Query(  "SELECT COALESCE ( SUM ( rdo.importoImponibile ), 0 ) " +
			" FROM SiacRDocOnere rdo " +
			" WHERE rdo.dataCancellazione IS NULL " +
			" AND rdo.siacTDoc.docId = :docId ")
	BigDecimal sommaImportiImponibileOnereByDocId(@Param("docId") Integer docId);
	
	@Query(  "SELECT COALESCE ( MAX ( rdo.importoImponibile ), 0 ) " +
			" FROM SiacRDocOnere rdo " +
			" WHERE rdo.dataCancellazione IS NULL " +
			" AND rdo.siacTDoc.docId = :docId ")
	BigDecimal massimoImportiImponibileOnereByDocId(@Param("docId") Integer docId);
	
	@Query(  "SELECT COALESCE ( SUM ( rdo.importoImponibile ), 0 ) " +
			" FROM SiacRDocOnere rdo " +
			" WHERE rdo.dataCancellazione IS NULL " +
			" AND rdo.siacTDoc.docId = :docId " +
			" AND rdo.docOnereId <> :docOnereId ")
	BigDecimal sommaImportiImponibileOnereByDocIdExcludingDocOnereId(@Param("docId") Integer docId, @Param("docOnereId") Integer docOnereId);

	
}
