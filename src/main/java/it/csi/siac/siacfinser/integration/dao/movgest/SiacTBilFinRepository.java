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

import it.csi.siac.siacfinser.integration.entity.SiacTBilFin;

public interface SiacTBilFinRepository extends JpaRepository<SiacTBilFin, Integer> {
	
	String condizione =  " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTBilFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacTBilFin> findListaSiacTBil(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTBilFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTPeriodo.anno = :anno AND "+condizione)
	public List<SiacTBilFin> getValidoByAnno(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("anno") String  anno,@Param("dataInput") Timestamp  dataInput);
}