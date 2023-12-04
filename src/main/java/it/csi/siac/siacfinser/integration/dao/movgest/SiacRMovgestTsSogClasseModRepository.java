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

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;

public interface SiacRMovgestTsSogClasseModRepository extends JpaRepository<SiacRMovgestTsSogclasseModFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRMovgestTsSogclasseModFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRMovgestTsSogclasseModFin> findListaSiacRMovgestTsSogClasseMod(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	
	@Query("FROM SiacRMovgestTsSogclasseModFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgestT.movgestTsId = :idMovgestTs AND "+condizione)
	public List<SiacRMovgestTsSogclasseModFin> findValidiByMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("dataInput") Timestamp  dataInput,@Param("idMovgestTs") Integer idMovgestTs);
	
	@Query("FROM SiacRMovgestTsSogclasseModFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgestT.movgestTsId IN :idMovgestTsIds AND "+condizione)
	public List<SiacRMovgestTsSogclasseModFin> findValidiByMovgestTsByIds(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("dataInput") Timestamp  dataInput,@Param("idMovgestTsIds") List<Integer>  idMovgestTsIds);
			
}


