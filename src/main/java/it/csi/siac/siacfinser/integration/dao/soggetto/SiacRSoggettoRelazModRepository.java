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

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazModFin;

public interface SiacRSoggettoRelazModRepository extends JpaRepository<SiacRSoggettoRelazModFin, Integer> {
	
	public String entity = "SiacRSoggettoRelazModFin";
	
//	@Query("FROM SiacRSoggettoRelazModFin WHERE siacTSoggetto.soggettoId = :idSoggetto AND dataFineValidita IS NULL")
//	public List<SiacRSoggettoRelazModFin> findValidoBySoggettoId(@Param("idSoggetto") Integer idSoggetto);
	
	@Modifying
	@Query("DELETE FROM " + entity + " WHERE siacTSoggettoMod.sogModId = :idSoggMod ")
	public void deleteAllBySoggModId(@Param("idSoggMod") Integer idSoggMod);
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacRSoggettoRelazModFin where siacTSoggetto1.soggettoId = :idSoggetto AND "+condizione+" AND siacDRelazTipo.relazTipoCode = :tipoRelazione")
	List<SiacRSoggettoRelazModFin> findBySoggettoETipo(@Param("idSoggetto") Integer idSoggetto, @Param("tipoRelazione") String tipoRelazione,@Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacRSoggettoRelazModFin where siacTSoggetto1.soggettoId = :idSoggettoDa AND siacTSoggetto2.soggettoId = :idSoggettoA AND dataFineValidita IS NULL AND siacDRelazTipo.relazTipoCode = :tipoRelazione AND siacTEnteProprietario.enteProprietarioId = :idEnte")
	SiacRSoggettoRelazModFin findValidaBySoggettiETipo(@Param("idSoggettoDa") Integer idSoggettoDa, @Param("idSoggettoA") Integer idSoggettoA, @Param("tipoRelazione") String tipoRelazione, @Param("idEnte") Integer idEnte);
	
	@Query("from SiacRSoggettoRelazModFin where siacRSoggettoRelaz.soggettoRelazId = :soggettoRelazId")
	SiacRSoggettoRelazModFin findValidaBySoggettoRelazId(@Param("soggettoRelazId") Integer soggettoRelazId);
}
