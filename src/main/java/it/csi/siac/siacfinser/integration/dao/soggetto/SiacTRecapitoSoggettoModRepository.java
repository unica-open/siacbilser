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

import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoModFin;

public interface SiacTRecapitoSoggettoModRepository extends JpaRepository<SiacTRecapitoSoggettoModFin, Integer> {

	public String entity = "SiacTRecapitoSoggettoModFin";
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";		
	
	@Query("FROM SiacTRecapitoSoggettoModFin WHERE siacTSoggettoMod.sogModId = :idSoggMod AND "+condizione)
	public List<SiacTRecapitoSoggettoModFin> findValidoBySoggModId(@Param("idSoggMod") Integer idSoggMod,@Param("dataInput") Timestamp  dataInput);
	
	@Query("DELETE FROM " + entity + " WHERE siacTSoggettoMod.sogModId = :idSoggMod ")
	public void deleteAllBySoggModId(@Param("idSoggMod") Integer idSoggMod);
	
	@Modifying
	@Query("DELETE FROM " + entity + " WHERE siacTSoggetto.soggettoId = :idSoggetto ")
	public void deleteAllBySoggettoId(@Param("idSoggetto") Integer idSoggetto);
	
	
}
