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

import it.csi.siac.siacfinser.integration.entity.SiacDCodicebolloFin;

public interface SiacDCodiceBolloFinRepository extends JpaRepository<SiacDCodicebolloFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("from SiacDCodicebolloFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND codbolloCode = :code AND " + condizione)
	public SiacDCodicebolloFin findDCodiceBolloValidoByEnteAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                    @Param("code") String code,
			                                                    @Param("dataInput") Timestamp  dataInput);	
	
	@Query("from SiacDCodicebolloFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public List<SiacDCodicebolloFin> findDCodiceBolloValidiByEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                   @Param("dataInput") Timestamp  dataInput);
}