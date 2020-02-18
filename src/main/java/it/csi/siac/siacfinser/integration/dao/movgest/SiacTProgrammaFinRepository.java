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

import it.csi.siac.siacfinser.integration.entity.SiacTProgrammaFin;

public interface SiacTProgrammaFinRepository extends JpaRepository<SiacTProgrammaFin, Integer>  {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTProgrammaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacTProgrammaFin> findListaSiacTProgramma(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTProgrammaFin WHERE siacTEnteProprietario.enteProprietarioId = :idEnte AND UPPER(programmaCode) = UPPER(:code) AND "+condizione)
	public List<SiacTProgrammaFin> findByCodeAndEnte(@Param("code") String code, @Param("idEnte") Integer idEnte,@Param("dataInput") Timestamp  dataInput);

	@Query("SELECT COUNT(*) FROM SiacTProgrammaFin WHERE siacTEnteProprietario.enteProprietarioId = :idEnte AND UPPER(programmaCode) = UPPER(:programmaCode) AND "+condizione)
	public Long verificaCodiceProgramma(@Param("programmaCode") String programmaCode, @Param("idEnte") Integer idEnte,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTProgrammaFin WHERE siacTEnteProprietario.enteProprietarioId = :idEnte AND UPPER(programmaCode) = UPPER(:programmaCode) AND "+condizione)
	public SiacTProgrammaFin getProgrammaByCodice(@Param("programmaCode") String programmaCode, @Param("idEnte") Integer idEnte,@Param("dataInput") Timestamp  dataInput);
	
	
	@Query( "SELECT p " +
			" FROM SiacTProgrammaFin p " +
			" WHERE UPPER(p.programmaCode) = UPPER(:programmaCode) " +
			" AND p.dataCancellazione IS NULL " +
			" AND p.dataInizioValidita < CURRENT_TIMESTAMP " +
			" AND ( " +
			"    p.dataFineValidita IS NULL " +
			"    OR p.dataFineValidita > CURRENT_TIMESTAMP " +
			" ) " +
			" AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND p.siacDProgrammaTipo.programmaTipoCode = :tipoProgrammaCode " +
			" AND p.siacTBil.bilId = :bilancioId " +
			" AND EXISTS ( " +
			" 	SELECT ps " +
			" 	FROM p.siacRProgrammaStatos ps " +
			"	WHERE ps.siacDProgrammaStato.programmaStatoCode = :programmaStatoCode " +
			"	AND ps.dataCancellazione IS NULL " +
			" )")
	public SiacTProgrammaFin findProgrammaByCodiceAndStatoOperativoAndEnteProprietarioId(@Param("programmaCode") String progettoCodice, 
			@Param("programmaStatoCode") String progettoStatoOperativoCodice,
			@Param("tipoProgrammaCode") String tipoProgrammaCode,
			@Param("bilancioId") Integer bilancioId,
			@Param("enteProprietarioId") Integer enteProprietarioId
		);
}
