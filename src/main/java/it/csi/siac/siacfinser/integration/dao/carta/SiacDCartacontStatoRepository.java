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

import it.csi.siac.siacfinser.integration.entity.SiacDCartacontStatoFin;

public interface SiacDCartacontStatoRepository extends JpaRepository<SiacDCartacontStatoFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDCartacontStatoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND cartacStatoCode = :codeDCartaCont AND " + condizione)
	public SiacDCartacontStatoFin findDCartaContStatoValidoByEnteAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                          @Param("codeDCartaCont") String codeDCartaCont,
			                                                          @Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacDCartacontStatoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public List<SiacDCartacontStatoFin> findDCartaContStatoValidiByEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                               @Param("dataInput") Timestamp  dataInput);
}