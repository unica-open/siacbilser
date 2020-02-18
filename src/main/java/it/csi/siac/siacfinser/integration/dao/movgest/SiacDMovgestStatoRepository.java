/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDMovgestStatoFin;

public interface SiacDMovgestStatoRepository extends JpaRepository<SiacDMovgestStatoFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacDMovgestStatoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacDMovgestStatoFin> findListaMovgestStatoOperativo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacDMovgestStatoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione+" AND movgestStatoCode = :code ")
	public List<SiacDMovgestStatoFin> findValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);

}
