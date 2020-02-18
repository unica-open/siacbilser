/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDCommissioniesteroFin;

public interface SiacDCommissioniEsteroRepository extends JpaRepository<SiacDCommissioniesteroFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDCommissioniesteroFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND commestTipoCode = :commestTipoCode AND " + condizione)
	public SiacDCommissioniesteroFin findDCommissioneEsteroValidoByEnteAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                @Param("commestTipoCode") String commestTipoCode,
			                                                                @Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacDCommissioniesteroFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public List<SiacDCommissioniesteroFin> findDCommissioniEsteroValideByEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                               @Param("dataInput") Timestamp  dataInput);
}