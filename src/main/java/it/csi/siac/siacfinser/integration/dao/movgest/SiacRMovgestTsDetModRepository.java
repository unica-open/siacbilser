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

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsDetModFin;

public interface SiacRMovgestTsDetModRepository extends JpaRepository<SiacRMovgestTsDetModFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	String condizioneNonValidita = " ( (dataInizioValidita >= :dataInput)  OR (dataFineValidita IS NOT NULL AND :dataInput >= dataFineValidita) OR  dataCancellazione IS NOT NULL ) ";
	
	@Query("FROM SiacRMovgestTsDetModFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRMovgestTsDetModFin> findListaSiacRMovgestTsDetMod(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	
}


