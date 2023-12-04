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

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;

public interface SiacRSoggettoClasseRepository extends JpaRepository<SiacRSoggettoClasseFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
			
	@Query("from SiacRSoggettoClasseFin where siacTSoggetto.soggettoId = :idSoggetto AND "+condizione)
	public List<SiacRSoggettoClasseFin>  findValidiByIdSoggetto(@Param("idSoggetto") Integer idSoggetto,@Param("dataInput") Timestamp  dataInput);
	
	@Query("select count(r) from SiacRSoggettoClasseFin r where siacDSoggettoClasse.id = :idSoggettoClasse AND "+condizione)
	public Long  countValidiBySoggettoClasseId(
			@Param("idSoggettoClasse") Integer idSoggettoClasse, 
			@Param("dataInput") Timestamp  dataInput); 
	
	@Query("SELECT DISTINCT siacTSoggetto.soggettoId FROM SiacRSoggettoClasseFin WHERE siacDSoggettoClasse.soggettoClasseId = :idClasse AND "+condizione)
	public List<Integer> findIdSoggettiValidiByIdClasse(@Param("idClasse") Integer idClasse,@Param("dataInput") Timestamp  dataInput);
	
	@Query("SELECT DISTINCT siacTSoggetto.soggettoCode FROM SiacRSoggettoClasseFin WHERE siacDSoggettoClasse.soggettoClasseId = :idClasse AND "+condizione)
	public List<String> findCodiciSoggettiValidiByIdClasse(@Param("idClasse") Integer idClasse,@Param("dataInput") Timestamp  dataInput);
	
	@Query("SELECT DISTINCT siacTSoggetto.soggettoId, siacTSoggetto.soggettoCode FROM SiacRSoggettoClasseFin WHERE siacDSoggettoClasse.soggettoClasseId = :idClasse AND "+condizione)
	public List<Object[]> findIdCodiceSoggettiValidiByIdClasse(@Param("idClasse") Integer idClasse,@Param("dataInput") Timestamp  dataInput);
	
	
}
