/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetSubdocFin;

public interface SiacRCartacontDetSubdocRepository extends JpaRepository<SiacRCartacontDetSubdocFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("FROM SiacRCartacontDetSubdocFin WHERE siacTSubdoc.subdocId = :subdocId AND "+ condizione)
	public List<SiacRCartacontDetSoggettoFin> findValidoBySubdocId(@Param("subdocId") Integer subdocId, @Param("dataInput") Timestamp  dataInput);
	
	
}