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

import it.csi.siac.siacfinser.integration.entity.SiacDIndirizzoTipoFin;

public interface SiacDIndirizzoTipoRepository extends JpaRepository<SiacDIndirizzoTipoFin, Integer>{
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	static final String TIPO_INDIRIZZO_SEDE2 ="FROM SiacDIndirizzoTipoFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            //+"AND ambitoId = :ambitoId " 
            +"AND "+condizione;
	

	
	
	static final String TIPO_INDIRIZZO_VALIDO_BY_CODE2 ="FROM SiacDIndirizzoTipoFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND indirizzoTipoCode = :code " 
            +"AND "+condizione;


	
	@Query(TIPO_INDIRIZZO_SEDE2)
	public List<SiacDIndirizzoTipoFin> findTipoIndirizzoSede(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	
	@Query(TIPO_INDIRIZZO_VALIDO_BY_CODE2)
	public List<SiacDIndirizzoTipoFin> findTipoIndirizzoValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);
	
}
