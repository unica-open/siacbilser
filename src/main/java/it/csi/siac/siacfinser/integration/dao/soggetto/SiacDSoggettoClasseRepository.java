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

import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoClasseFin;

public interface SiacDSoggettoClasseRepository extends JpaRepository<SiacDSoggettoClasseFin, Integer> {

	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";

	
	static final String SOGGETTI_CLASSE2 ="FROM SiacDSoggettoClasseFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND ambitoId = :ambitoId " 
            +"AND "+condizione+" ORDER BY soggettoClasseCode ASC ";
	

	
	static final String VALIDO_BY_CODE2 ="FROM SiacDSoggettoClasseFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND ambitoId = :ambitoId " 
            +"AND "+condizione+" AND soggettoClasseCode =:codice ";
	
	
	
	
	@Query(SOGGETTI_CLASSE2)
	public List<SiacDSoggettoClasseFin> findSoggettiClasse(@Param("enteProprietarioId") Integer enteProprietarioId,
														@Param("ambitoId") Integer ambitoId,@Param("dataInput") Timestamp  dataInput);
	
	
	
	@Query(VALIDO_BY_CODE2)
	public List<SiacDSoggettoClasseFin> findValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("ambitoId") Integer ambitoId,
														@Param("codice") String codice,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacDSoggettoClasseFin sogg WHERE siacTEnteProprietario.enteProprietarioId = :idEnte " +
			" and ambitoId = :ambito and soggettoClasseCode = :code and "+condizione)
	public List<SiacDSoggettoClasseFin> findByCodeAndAmbitoAndEnte(@Param("code") String code, @Param("idEnte") Integer idEnte,@Param("dataInput") Timestamp  dataInput,@Param("ambito") Integer ambito);
	
	@Query("FROM SiacDSoggettoClasseFin classSogg WHERE classSogg.siacTEnteProprietario.enteProprietarioId = :idEnte " +
			" and classSogg.ambitoId = :ambito and classSogg.soggettoClasseCode = :code and "+condizione)
	public List<SiacDSoggettoClasseFin> findSoggettoClasseByCodeAndAmbitoAndEnte(@Param("code") String code, @Param("idEnte") Integer idEnte,@Param("dataInput") Timestamp  dataInput,@Param("ambito") Integer ambito);
	

}
