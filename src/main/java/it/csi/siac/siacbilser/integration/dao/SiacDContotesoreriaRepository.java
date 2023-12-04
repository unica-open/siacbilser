/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDContotesoreria;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDContotesoreriaRepository.
 */
public interface SiacDContotesoreriaRepository extends JpaRepository<SiacDContotesoreria, Integer> {

	/**
	 * Find contitesoreria by ente.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query("SELECT c " 
			+ " FROM SiacDContotesoreria c " 
			+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataCancellazione IS NULL "
			+ " AND dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " 
			+ " ORDER BY c.contotesCode ")
	List<SiacDContotesoreria> findContitesoreriaByEnte(
			@Param("enteProprietarioId") Integer enteProprietarioId);

	@Query("SELECT c " 
			+ " FROM SiacDContotesoreria c " 
			+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataCancellazione IS NULL "
			+ " AND dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " 
			+ " AND c.contotesCode = :codice ")
	SiacDContotesoreria findContotesoreriaByEnteCodice(
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("codice") String codice
	);

	@Query(value = "SELECT dc.* FROM "
			+ " siac_d_contotesoreria dc, "
			+ " siac_r_saldo_vincolo_sotto_conto rsvsc, "
			+ " siac_r_vincolo_bil_elem rvbe " 
			+ " WHERE rvbe.elem_id=:elemId "
			+ " AND dc.contotes_id=rsvsc.contotes_id "
			+ " and rsvsc.vincolo_id=rvbe.vincolo_id "		
			+ " AND rsvsc.data_cancellazione IS NULL "
			+ " AND rsvsc.validita_inizio < CURRENT_TIMESTAMP "
			+ " AND (rsvsc.validita_fine IS NULL OR rsvsc.validita_fine > CURRENT_TIMESTAMP) "
			+ " AND rvbe.data_cancellazione IS NULL "
			+ " AND rvbe.validita_inizio < CURRENT_TIMESTAMP "
			+ " AND (rvbe.validita_fine IS NULL OR rvbe.validita_fine > CURRENT_TIMESTAMP) ", nativeQuery=true)
	SiacDContotesoreria findContotesoreriaByCapitolo(
			@Param("elemId") Integer elemId
	);

}
