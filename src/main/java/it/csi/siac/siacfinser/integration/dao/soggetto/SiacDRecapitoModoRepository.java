/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDRecapitoModoFin;

public interface SiacDRecapitoModoRepository extends JpaRepository<SiacDRecapitoModoFin, Integer>{

	public String entity = "SiacDRecapitoModoFin";
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	@Query("from SiacDRecapitoModoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacDRecapitoModoFin> findRecapitoModo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacDRecapitoModoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione + " and UPPER(recapitoModoCode) = UPPER(:code)")
	public List<SiacDRecapitoModoFin> findValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                        @Param("dataInput") Timestamp  dataInput,
			                                        @Param("code") String code);
}