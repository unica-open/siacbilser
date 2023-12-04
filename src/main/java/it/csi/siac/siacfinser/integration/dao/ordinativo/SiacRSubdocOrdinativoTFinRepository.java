/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRSubdocOrdinativoTFin;

public interface SiacRSubdocOrdinativoTFinRepository extends JpaRepository<SiacRSubdocOrdinativoTFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	
	@Query("FROM SiacRSubdocOrdinativoTFin WHERE subdocLiqId = :subOrdinativoIdRSubDocId AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public SiacRSubdocOrdinativoTFin findSiacRSubdocOrdinativoTFinValidoById(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                               @Param("subOrdinativoIdRSubDocId") Integer subOrdinativoIdRSubDocId,
			                                                               @Param("dataInput") Timestamp  dataInput);
}