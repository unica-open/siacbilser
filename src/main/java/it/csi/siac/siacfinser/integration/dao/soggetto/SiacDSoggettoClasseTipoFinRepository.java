/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoClasseTipoFin;

public interface SiacDSoggettoClasseTipoFinRepository extends JpaRepository<SiacDSoggettoClasseTipoFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	
//	static final String QUERY1 ="FROM SiacDSoggettoClasseTipoFin "
//            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
//            +"AND ambitoId = :ambitoId " 
//            +"AND "+condizione+"";
//	
//	@Query(QUERY1)
//	public SiacDSoggettoClasseTipoFin findByAmbitoAndEnte(@Param("ambitoId") Integer idAmbito, @Param("enteProprietarioId") Integer idEnte);
	
	static final String QUERY2 ="FROM SiacDSoggettoClasseTipoFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +" AND ambitoId = :ambitoId "
            +" AND soggettoClasseTipoCode = :codice"
            +" AND "+condizione+"";
	
	@Query(QUERY2)
	public SiacDSoggettoClasseTipoFin findByCodiceEAmbito(@Param("codice") String codice, @Param("ambitoId") Integer idAmbito, @Param("enteProprietarioId") Integer idEnte, @Param("dataInput") Timestamp dataInput);
	
}
