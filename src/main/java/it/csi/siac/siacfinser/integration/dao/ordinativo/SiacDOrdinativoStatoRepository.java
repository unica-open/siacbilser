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

import it.csi.siac.siacfinser.integration.entity.SiacDMovgestStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoStatoFin;

public interface SiacDOrdinativoStatoRepository extends JpaRepository<SiacDOrdinativoStatoFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDOrdinativoStatoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND ordStatoCode = :code AND " + condizione)
	public SiacDOrdinativoStatoFin findDOrdinativoStatoValidoByEnteAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                  @Param("code") String code,
			                                                  @Param("dataInput") Timestamp  dataInput);

	@Query("FROM SiacDOrdinativoStatoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacDMovgestStatoFin> findListaOrdinativoStatoOperativo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
}