/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.mutuo;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTMutuoFin;

public interface SiacTMutuoRepository extends JpaRepository<SiacTMutuoFin, Integer> {
	String condizione = " ( (mut.dataInizioValidita < :dataInput)  AND (mut.dataFineValidita IS NULL OR :dataInput < mut.dataFineValidita) AND mut.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTMutuoFin mut WHERE mut.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mut.mutCode = :mutCode ")
	public SiacTMutuoFin findMutuoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			 						  @Param("mutCode") String mutCode);
	
	
	@Query("FROM SiacTMutuoFin mut WHERE mut.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mut.mutCode = :mutCode AND " + condizione)
	public SiacTMutuoFin findMutuoValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			 						  @Param("mutCode") String mutCode,
			 						  @Param("dataInput") Timestamp dataInput);
}