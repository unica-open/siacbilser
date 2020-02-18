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

import it.csi.siac.siacfinser.integration.entity.SiacDRelazTipoFin;

public interface SiacDRelazTipoRepository extends JpaRepository<SiacDRelazTipoFin, Integer> {

	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("from SiacDRelazTipoFin where relazTipoCode = :code AND "+condizione+" AND siacTEnteProprietario.enteProprietarioId = :idEnte")
	List<SiacDRelazTipoFin> findRelazione(@Param("code") String code, @Param("idEnte") Integer idEnte,@Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacDRelazTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione+" AND relazTipoCode = :code ")
	public List<SiacDRelazTipoFin> findRelazTipoValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);
	
	@Query("from SiacDRelazTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND dataFineValidita IS NULL AND relazTipoCode NOT IN ('SEDE_SECONDARIA','CSI','CSC')")
	public List<SiacDRelazTipoFin> findRelazTipoByEnte(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	@Query("from SiacDRelazTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND dataFineValidita IS NULL "
			+" AND relazTipoCode NOT IN ('SEDE_SECONDARIA','CSI','CSC') AND siacDRelazEntita.relazEntitaCode = :famigliaCode ")
	public List<SiacDRelazTipoFin> findRelazTipoLegameTipizzatoByEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("famigliaCode") String codiceFamiglia);
	
	
//	@Query("SELECT rt.relazTipoCode FROM SiacDRelazTipoFin rt where rt.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND rt.dataFineValidita IS NULL AND rt.relazTipoCode NOT IN ('SEDE_SECONDARIA','CSI','CSC')")
//	List<String> findElencoCodeLegamiSoggetti(@Param("enteProprietarioId") Integer enteProprietarioId);
}
