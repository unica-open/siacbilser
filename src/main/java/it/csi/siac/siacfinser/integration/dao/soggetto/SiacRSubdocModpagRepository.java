/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRSubdocModpagFin;

public interface SiacRSubdocModpagRepository extends JpaRepository<SiacRSubdocModpagFin, Integer> {

	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	
	@Query("FROM SiacRSubdocModpagFin WHERE siacTModpag.modpagId = :idModpag AND "+condizione+" AND siacTEnteProprietario.enteProprietarioId = :idEnte")
	List<SiacRSubdocModpagFin> findSiacRSubdocModpagByIdModPag(@Param("idModpag") Integer idModPag, @Param("dataInput") Timestamp  dataInput, @Param("idEnte") Integer idEnte);
}
