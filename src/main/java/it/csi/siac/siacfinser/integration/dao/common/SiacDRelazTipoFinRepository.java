/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDRelazTipoFin;

public interface SiacDRelazTipoFinRepository extends JpaRepository<SiacDRelazTipoFin, Integer> {
	@Query("FROM SiacDRelazTipoFin "
			+ " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND relazTipoCode = :code "
			+ " AND " 
			+ " ((dataInizioValidita < CURRENT_TIMESTAMP) "
			+ " AND (dataFineValidita IS NULL OR CURRENT_TIMESTAMP < dataFineValidita)"
			+ " AND dataCancellazione IS NULL) ")
	public SiacDRelazTipoFin findDRelazTipoValidoByEnteAndCode(
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("code") String code
	);	
	
	@Query("from SiacDRelazTipoFin "
			+ " where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND "  
			+ " ((dataInizioValidita < :dataInput) "
			+ " AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita)"
			+ " AND dataCancellazione IS NULL) ")
	public List<SiacDRelazTipoFin> findDRelazTipoValidiByEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                   @Param("dataInput") Timestamp  dataInput);
}