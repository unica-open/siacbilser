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

import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;

public interface SiacTModpagFinRepository extends JpaRepository<SiacTModpagFin, Integer> {
	
	public String entity = "SiacTModpagFin";
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacTModpagFin where siacTSoggetto.soggettoId = :idSoggetto AND "+condizione)
	public List<SiacTModpagFin>  findValidiByIdSoggetto(@Param("idSoggetto") Integer idSoggetto,@Param("dataInput") Timestamp  dataInput);
	
	
//	//TO-DO DA ERRORI NELL'INVOCAZIONE!!!!
//	@Query("DELETE FROM " + entity + " WHERE siacTSoggettoMod.sogModId = :idSoggMod ")
//	public void deleteAllBySoggModId(@Param("idSoggMod") Integer idSoggMod);
//	
//	@Query("DELETE FROM " + entity + " WHERE siacTSoggetto.soggettoId = :idSoggetto ")
//	public void deleteAllBySoggettoId(@Param("idSoggetto") Integer idSoggetto);
}
