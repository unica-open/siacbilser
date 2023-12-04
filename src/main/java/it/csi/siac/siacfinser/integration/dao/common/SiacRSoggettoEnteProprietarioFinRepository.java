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

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoEnteProprietarioFin;

public interface SiacRSoggettoEnteProprietarioFinRepository extends JpaRepository<SiacRSoggettoEnteProprietarioFin, Integer>{
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacRSoggettoEnteProprietarioFin where siacTEnteProprietario.enteProprietarioId = :enteUid AND "+condizione)
	List<SiacRSoggettoEnteProprietarioFin> findValidoByEnte(@Param("enteUid") Integer enteUid,@Param("dataInput") Timestamp  dataInput);
	
}
