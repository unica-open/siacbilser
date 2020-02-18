/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.mutuo;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDMutuoStatoFin;

public interface SiacDMutuoStatoRepository extends JpaRepository<SiacDMutuoStatoFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDMutuoStatoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mutStatoCode = :codeDMutuo AND " + condizione)
	public SiacDMutuoStatoFin findDMutuoStatoValidoByEnteAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                  @Param("codeDMutuo") String codeDMutuo,
			                                                  @Param("dataInput") Timestamp  dataInput);
}