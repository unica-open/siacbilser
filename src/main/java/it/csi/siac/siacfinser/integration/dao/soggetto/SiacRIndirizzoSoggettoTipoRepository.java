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

import it.csi.siac.siacfinser.integration.entity.SiacRIndirizzoSoggettoTipoFin;

public interface SiacRIndirizzoSoggettoTipoRepository extends JpaRepository<SiacRIndirizzoSoggettoTipoFin, Integer> {

	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	
	@Query("from SiacRIndirizzoSoggettoTipoFin where siacTIndirizzoSoggetto.indirizzoId = :idIndirizzoSogg AND "+condizione)
	public List<SiacRIndirizzoSoggettoTipoFin>  findValidoByIdIndirizzoSoggetto(@Param("idIndirizzoSogg") Integer idIndirizzoSogg,@Param("dataInput") Timestamp  dataInput);
	
//	@Query("from SiacRIndirizzoSoggettoTipoFin where siacTIndirizzoSoggetto.indirizzoId = :idIndirizzoSogg AND dataFineValidita IS NULL")
//	public List<SiacRIndirizzoSoggettoTipoFin>  findValidoByIdIndirizzoSoggettoEasy(@Param("idIndirizzoSogg") Integer idIndirizzoSogg);
	
}
