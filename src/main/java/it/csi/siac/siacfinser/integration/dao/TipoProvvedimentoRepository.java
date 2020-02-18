/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmTipoFin;

public interface TipoProvvedimentoRepository extends JpaRepository<SiacDAttoAmmTipoFin, Integer> {
	
	String condizione = " AND ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacDAttoAmmTipoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND attoammTipoCode = :codice " + condizione)
	public List<SiacDAttoAmmTipoFin> findTipoProvvedimentoByCodiceAndIdEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                     @Param("codice") String codice,
			                                                                     @Param("dataInput") Timestamp  dataInput);
}