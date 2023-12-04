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

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;

public interface SiacROrdinativoStatoRepository extends JpaRepository<SiacROrdinativoStatoFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) order by dataCreazione DESC ";

	@Query("FROM SiacROrdinativoStatoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public List<SiacROrdinativoStatoFin> findListaSiacROrdinativoStatoValidi(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacROrdinativoStatoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTOrdinativo.ordId = :idOrdinativo AND " + condizione)
	public List<SiacROrdinativoStatoFin> findSiacROrdinativoStatoValidoByIdOrdinativo(@Param("enteProprietarioId") Integer enteProprietarioId,
									  										       @Param("idOrdinativo") Integer idOrdinativo,
			                                                                       @Param("dataInput") Timestamp  dataInput);
}