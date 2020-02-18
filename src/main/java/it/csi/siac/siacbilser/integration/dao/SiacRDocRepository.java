/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRDoc;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRDocRepository.
 */
public interface SiacRDocRepository extends JpaRepository<SiacRDoc, Integer> {


	@Query(  "SELECT r " +
			" FROM SiacRDoc r " +
			" WHERE r.siacTDocPadre.docId = :docIdPadre " +
			" AND r.siacTDocFiglio.docId = :docIdFiglio " +
			" AND r.siacDRelazTipo.relazTipoCode = :codiceRelazione " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) ")
	SiacRDoc findRelazione(@Param("docIdFiglio") Integer docIdFiglio, @Param("docIdPadre") Integer docIdPadre,  @Param("codiceRelazione") String codiceRelazione);

	@Query(  "SELECT COUNT(r) " +
			" FROM SiacRDoc r " +
			" WHERE r.siacTDocFiglio.docId = :docIdFiglio " +
			" AND r.siacDRelazTipo.relazTipoCode = :codiceRelazione " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) ")
	Long countDocumentiPadre(@Param("docIdFiglio") Integer docIdFiglio, @Param("codiceRelazione") String codiceRelazione);
	
	
	//OTTIMIZZAZIONE EMETTITORE
	/**
	 * Conta il numero di documenti che sono collegati 
	 * alla notaCredito il cui docId e' passato come parametro. 
	 * 
	 * @param docId della notaCredito
	 * @return numero di documenti collegati.
	 */
	@Query(  "SELECT COUNT(r) " +
			" FROM SiacRDoc r " +
			" WHERE r.siacTDocPadre.docId = :docId " +
			" AND r.dataCancellazione IS NULL " +
			" AND r.dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (r.dataFineValidita IS NULL OR r.dataFineValidita > CURRENT_TIMESTAMP) ")
	Long countDocumentiFigli(@Param("docId") Integer docId);
	
	
	
}
