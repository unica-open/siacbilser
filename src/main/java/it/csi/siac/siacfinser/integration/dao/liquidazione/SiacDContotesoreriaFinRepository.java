/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.liquidazione;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;

public interface SiacDContotesoreriaFinRepository extends JpaRepository<SiacDContotesoreriaFin, Integer> {
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) order by contotesCode ASC ";
	
	static final String FIND_CONTOTESORERIA_BY_CODE ="FROM SiacDContotesoreriaFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND contotesCode = :code " 
            +"AND "+condizione;
	
	static final String FIND_CONTOTESORERIA_BY_ENTE ="FROM SiacDContotesoreriaFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND "+condizione;
	
	@Query(FIND_CONTOTESORERIA_BY_CODE)
	public SiacDContotesoreriaFin findContotesoreriaByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);

	@Query(FIND_CONTOTESORERIA_BY_ENTE)
	public List<SiacDContotesoreriaFin> findContotesoreriaByEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput);
}