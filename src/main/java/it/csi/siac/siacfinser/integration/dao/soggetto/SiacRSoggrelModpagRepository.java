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

import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;

public interface SiacRSoggrelModpagRepository extends JpaRepository<SiacRSoggrelModpagFin, Integer> {
	
	public String entity = "SiacRSoggrelModpagFin";
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRSoggrelModpagFin WHERE siacRSoggettoRelaz.soggettoRelazId = :idSoggettoRelaz")
	public SiacRSoggrelModpagFin findValidiBySoggettoRelaz(@Param("idSoggettoRelaz") Integer idSoggettoRelaz);
	
	//ERRORE in INVOCAZIONE AUTOWIRED!!!!!!!!!
//	@Query("DELETE FROM " + entity + " WHERE siacTSoggettoMod.sogModId = :idSoggMod ")
//	public void deleteAllBySoggModId(@Param("idSoggMod") Integer idSoggMod);
//	
//	@Query("DELETE FROM " + entity + " WHERE siacTSoggetto.soggettoId = :idSoggetto ")
//	public void deleteAllBySoggettoId(@Param("idSoggetto") Integer idSoggetto);
	
	@Query("FROM SiacRSoggrelModpagFin WHERE siacTModpag.modpagId = :idModpag AND "+condizione+" AND siacTEnteProprietario.enteProprietarioId = :idEnte")
	List<SiacRSoggrelModpagFin> findCessioniByIdModPag(@Param("idModpag") Integer idModPag, @Param("dataInput") Timestamp  dataInput, @Param("idEnte") Integer idEnte);

}
