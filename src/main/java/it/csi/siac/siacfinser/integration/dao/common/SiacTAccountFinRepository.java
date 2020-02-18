/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTAccountFin;

public interface SiacTAccountFinRepository extends JpaRepository<SiacTAccountFin, Integer>{
	
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";
	
	@Query("FROM SiacTAccountFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND accountId = :accountId AND " + condizione)
	public SiacTAccountFin findSiacTAccountValidoByAccountId(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                              @Param("dataInput") Timestamp  dataInput,
			                                              @Param("accountId") Integer accountId);
	
	@Query("FROM SiacTAccountFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND accountCode = :accountCode AND " + condizione)
	public SiacTAccountFin findSiacTAccountValidoByAccountCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                @Param("dataInput") Timestamp  dataInput,
			                                                @Param("accountCode") String accountCode);
}
