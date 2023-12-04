/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagModFin;

public interface SiacRSoggrelModpagModRepository extends JpaRepository<SiacRSoggrelModpagModFin, Integer> {
	
	public String entity = "SiacRSoggrelModpagModFin";
	
//	@Query("FROM SiacRSoggrelModpagModFin WHERE siacTSoggetto.soggettoId = :idSoggetto AND dataFineValidita IS NULL")
//	public List<SiacRSoggrelModpagModFin> findValidoBySoggettoId(@Param("idSoggetto") Integer idSoggetto);
	
	@Modifying
	@Query("DELETE FROM " + entity + " WHERE siacTSoggettoMod.sogModId = :idSoggMod ")
	public void deleteAllBySoggModId(@Param("idSoggMod") Integer idSoggMod);
	
	@Query("FROM SiacRSoggrelModpagModFin WHERE siacRSoggettoRelazMod.soggettoRelazModId = :idSoggRelazMod")
	public SiacRSoggrelModpagModFin findValidoBySoggRelazModId(@Param("idSoggRelazMod") Integer idSoggRelazMod);
	
//	@Query("FROM " + entity + " WHERE siacTSoggetto.soggettoId = :idSoggetto ")
//	public List<SiacRSoggettoClasseModFin> deleteAllBySoggettoId(@Param("idSoggetto") Integer idSoggetto);
}
