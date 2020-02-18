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

import it.csi.siac.siacfinser.integration.entity.SiacDMovgestTsDetTipoFin;

public interface SiacDMovgestTsDetTipoRepository extends JpaRepository<SiacDMovgestTsDetTipoFin, Integer>  {
	
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacDMovgestTsDetTipoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacDMovgestTsDetTipoFin> findListaSiacDMovgestTsDetTipo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacDMovgestTsDetTipoFin WHERE movgestTsDetTipoCode = :code AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacDMovgestTsDetTipoFin> findValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput,@Param("code") String code);
}
