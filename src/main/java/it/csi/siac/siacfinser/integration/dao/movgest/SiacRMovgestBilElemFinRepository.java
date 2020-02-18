/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;

public interface SiacRMovgestBilElemFinRepository extends JpaRepository<SiacRMovgestBilElemFin, Integer> {
	
	String condizione =  " ( (srmbe.dataInizioValidita < :dataInput)  AND (srmbe.dataFineValidita IS NULL OR :dataInput < srmbe.dataFineValidita) AND srmbe.dataCancellazione IS NULL ) ";
	
	@Query("SELECT srmbe FROM SiacRMovgestBilElemFin srmbe WHERE " +
			" srmbe.siacTMovgest.movgestId = :movgestId "+
			" AND "+condizione)
	public List<SiacRMovgestBilElemFin> getValidoByMovgestId(@Param("movgestId") Integer movgestId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("SELECT srmbe FROM SiacRMovgestBilElemFin srmbe "
			+ " WHERE srmbe.siacTMovgest.movgestId = :movgestId " 
			+ " AND srmbe.dataInizioValidita <= CURRENT_TIMESTAMP "
			+ " AND (srmbe.dataFineValidita IS NULL OR CURRENT_TIMESTAMP < srmbe.dataFineValidita) "
			+ " AND srmbe.dataCancellazione IS NULL ")
	public List<SiacRMovgestBilElemFin> getValidByMovgestId(@Param("movgestId") Integer movgestId);
}