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

import it.csi.siac.siacfinser.integration.entity.SiacDOnereFin;

public interface SiacDOnereFinRepository extends JpaRepository<SiacDOnereFin, Integer>{
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	
	@Query("from SiacDOnereFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione+" ORDER BY onereCode ASC ")
	public List<SiacDOnereFin> findOneri(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	


	@Query("from SiacDOnereFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione+" AND onereCode = :code ")
	public List<SiacDOnereFin> findOnereValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);

}
