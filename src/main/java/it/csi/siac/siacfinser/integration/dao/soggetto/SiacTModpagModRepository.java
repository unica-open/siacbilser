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

import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;

public interface SiacTModpagModRepository extends JpaRepository<SiacTModpagModFin, Integer> {
	
	public String entity = "SiacTModpagModFin";
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";		
	
	//condizione -> condizione_scad per eliminare le mod scadute
	String condizioneScad = " ( (dataInizioValidita < :dataInput)  AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTModpagModFin WHERE siacTSoggetto.soggettoId = :idSoggetto AND "+condizione)
	public List<SiacTModpagModFin> findValidoBySoggettoId(@Param("idSoggetto") Integer idSoggetto,@Param("dataInput") Timestamp  dataInput);
	
	@Query("DELETE FROM " + entity + " WHERE siacTSoggettoMod.sogModId = :idSoggMod ")
	public void deleteAllBySoggModId(@Param("idSoggMod") Integer idSoggMod);
	
	@Modifying
	@Query("DELETE FROM " + entity + " WHERE siacTSoggetto.soggettoId = :idSoggetto ")
	public void deleteAllBySoggettoId(@Param("idSoggetto") Integer idSoggetto);
	
	@Query("FROM SiacTModpagModFin WHERE siacTModpag.modpagId = :idModpag AND "+condizioneScad)	//prima condizione
	public SiacTModpagModFin findValidoByModpagId(@Param("idModpag") Integer idModpag,@Param("dataInput") Timestamp  dataInput);

	@Query("FROM SiacTModpagModFin WHERE siacTModpag.modpagId = :idModpag ")
	public SiacTModpagModFin findValidoByMdpId(@Param("idModpag") Integer idModpag);
}
