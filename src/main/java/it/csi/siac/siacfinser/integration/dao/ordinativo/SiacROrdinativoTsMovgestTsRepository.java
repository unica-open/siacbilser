/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoTsMovgestTFin;

public interface SiacROrdinativoTsMovgestTsRepository extends JpaRepository<SiacROrdinativoTsMovgestTFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	@Query("FROM SiacROrdinativoTsMovgestTFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
		       " AND siacTOrdinativoT.ordTsId = :idOrdinativoTs AND " + condizione)
	public List<SiacROrdinativoTsMovgestTFin> findValidoByIdOrdinativoTs(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                     @Param("dataInput") Timestamp  dataInput,
			                                                     @Param("idOrdinativoTs") Integer idOrdinativoTs);

	@Query("FROM SiacROrdinativoTsMovgestTFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
		       " AND siacTMovgestT.movgestTsId = :idMovGestTs AND " + condizione)
	public List<SiacROrdinativoTsMovgestTFin> findValidoByIdMovGestTs(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                     @Param("dataInput") Timestamp  dataInput,
			                                                     @Param("idMovGestTs") Integer idMovGestTs);
	
}