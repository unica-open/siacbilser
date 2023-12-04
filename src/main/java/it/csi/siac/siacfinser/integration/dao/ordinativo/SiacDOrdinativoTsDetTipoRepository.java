/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoTsDetTipoFin;

public interface SiacDOrdinativoTsDetTipoRepository extends JpaRepository<SiacDOrdinativoTsDetTipoFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDOrdinativoTsDetTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND ordTsDetTipoCode = :code AND " + condizione)
	public SiacDOrdinativoTsDetTipoFin findDOrdinativoTsDetTipoValidoByEnteAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                  @Param("code") String code,
			                                                  @Param("dataInput") Timestamp  dataInput);
}