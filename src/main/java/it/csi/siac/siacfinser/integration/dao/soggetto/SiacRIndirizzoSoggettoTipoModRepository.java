/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRIndirizzoSoggettoTipoModFin;

public interface SiacRIndirizzoSoggettoTipoModRepository extends JpaRepository<SiacRIndirizzoSoggettoTipoModFin, Integer> {

	String entity = "SiacRIndirizzoSoggettoTipoModFin";
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	@Query("from SiacRIndirizzoSoggettoTipoModFin where siacTIndirizzoSoggettoMod.indirizzoModId = :idIndirizzoSoggMod AND " + condizione)
	public List<SiacRIndirizzoSoggettoTipoModFin>  findValidoByIdIndirizzoSoggettoMod(@Param("idIndirizzoSoggMod") Integer idIndirizzoSoggMod, @Param("dataInput") Timestamp  dataInput);
	
	@Modifying
	@Query("DELETE FROM " + entity + " WHERE siacTIndirizzoSoggettoMod.indirizzoModId = :indirizzoModId ")
	public void deleteAllByIndirizzoModId(@Param("indirizzoModId") Integer indirizzoModId);

}
