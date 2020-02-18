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

import it.csi.siac.siacfinser.integration.entity.SiacDSiopeTipoDebitoFin;

public interface SiacDSiopeTipoDebitoFinRepository extends JpaRepository<SiacDSiopeTipoDebitoFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";

	@Query("FROM SiacDSiopeTipoDebitoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siopeTipoDebitoCode = :code AND " + condizione )
	public List<SiacDSiopeTipoDebitoFin> findByCode(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput,@Param("code") String code);
	
	@Query("FROM SiacDSiopeTipoDebitoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione )
	public List<SiacDSiopeTipoDebitoFin> findByEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput);
}