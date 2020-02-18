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

import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;

public interface SiacTIndirizzoSoggettoRepository extends JpaRepository<SiacTIndirizzoSoggettoFin, Integer> {

	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	@Query("from SiacTIndirizzoSoggettoFin where siacTSoggetto.soggettoId = :idSoggetto AND "+condizione)
	public List<SiacTIndirizzoSoggettoFin>  findValidiByIdSoggetto(@Param("idSoggetto") Integer idSoggetto,@Param("dataInput") Timestamp  dataInput);
	
	
}
