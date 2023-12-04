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

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsProgrammaFin;

public interface SiacRMovgestTsProgrammaRepository extends JpaRepository<SiacRMovgestTsProgrammaFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRMovgestTsProgrammaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRMovgestTsProgrammaFin> findListaSiacRMovgestTsProgramma(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacRMovgestTsProgrammaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgestT.movgestTsId = :idMovgestTs AND "+condizione)
	public List<SiacRMovgestTsProgrammaFin> findByMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput
			,@Param("idMovgestTs") Integer idMovgestTs);
	
	
	@Query("FROM SiacRMovgestTsProgrammaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND"
			+ " siacTMovgestT.movgestTsId = :idMovgestTs AND siacTProgramma.programmaId = :idProgramma AND "+condizione)
	public List<SiacRMovgestTsProgrammaFin> findValidoByMovgestTsAndProgramma(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("dataInput") Timestamp  dataInput
			,@Param("idMovgestTs") Integer idMovgestTs,@Param("idProgramma") Integer idProgramma);
	
}


