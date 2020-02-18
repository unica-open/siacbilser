/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.liquidazione;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDLiquidazioneStatoFin;

public interface SiacDLiquidazioneStatoRepository extends JpaRepository<SiacDLiquidazioneStatoFin, Integer> {
	
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDLiquidazioneStatoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND liqStatoCode = :codeDMutuo AND " + condizione)
	public SiacDLiquidazioneStatoFin findDLiquidazioneStatoByCodeAndEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                  @Param("codeDMutuo") String codeDMutuo,
			                                                  @Param("dataInput") Timestamp  dataInput);
}