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

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;

public interface SiacRMovgestTsAttoAmmRepository extends JpaRepository<SiacRMovgestTsAttoAmmFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRMovgestTsAttoAmmFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacRMovgestTsAttoAmmFin> findListaSiacRMovgestTsAttoAmm(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacRMovgestTsAttoAmmFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgestT.movgestTsId = :idMovgestTs AND "+condizione)
	public List<SiacRMovgestTsAttoAmmFin> findValidoByMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput
			,@Param("idMovgestTs") Integer idMovgestTs);
	
	@Query("FROM SiacRMovgestTsAttoAmmFin maa "
			+ " WHERE siacTMovgestT.movgestTsId=:idMovgestTs "
			+ " AND maa.dataInizioValidita <= CURRENT_TIMESTAMP "
			+ " AND (maa.dataFineValidita IS NULL OR CURRENT_TIMESTAMP < maa.dataFineValidita) "
			+ " AND maa.dataCancellazione IS NULL ")
	public List<SiacRMovgestTsAttoAmmFin> findValidByMovgestTs(@Param("idMovgestTs") Integer idMovgestTs);
	
	
	@Query("FROM SiacRMovgestTsAttoAmmFin WHERE siacTMovgestT.movgestTsId = :uid AND dataCancellazione IS NOT NULL")
	public List<SiacRMovgestTsAttoAmmFin> findStoricoSiacRMovgestTsAttoAmm(@Param("uid") Integer uid);
	
}


