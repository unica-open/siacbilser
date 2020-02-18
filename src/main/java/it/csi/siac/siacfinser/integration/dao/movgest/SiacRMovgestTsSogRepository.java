/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;

public interface SiacRMovgestTsSogRepository extends JpaRepository<SiacRMovgestTsSogFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	String condizioneNonValidita = " ( (dataInizioValidita >= :dataInput)  OR (dataFineValidita IS NOT NULL AND :dataInput >= dataFineValidita) OR  dataCancellazione IS NOT NULL ) ";
	
	@Query("FROM SiacRMovgestTsSogFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRMovgestTsSogFin> findListaSiacRMovgestTsSog(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	 @Query("FROM SiacRMovgestTsSogFin sog WHERE sog.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione+" AND sog.siacTMovgestT.movgestTsId = :idMovgestTs")
	 public List<SiacRMovgestTsSogFin> findValidoMovGestTsSogByIdMovGestAndEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp dataInput,@Param("idMovgestTs") Integer idMovgestTs);
	 
	 
	 @Query("FROM SiacRMovgestTsSogFin sog WHERE sog.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizioneNonValidita+" AND sog.siacTMovgestT.movgestTsId = :idMovgestTs ORDER BY dataCreazione ASC")
	 public List<SiacRMovgestTsSogFin> findUltimoInvalidato(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp dataInput,@Param("idMovgestTs") Integer idMovgestTs);
	 
	 
	 
	 @Query("FROM SiacRMovgestTsSogFin sog WHERE sog.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND sog.siacTMovgestT.movgestTsId = :idMovgestTs")
	 public SiacRMovgestTsSogFin findUnicoValidoMovGestTsSogByIdMovGestAndEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("idMovgestTs") Integer idMovgestTs);
}


