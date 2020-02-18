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

import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoStatoFin;

public interface SiacDSoggettoStatoRepository extends JpaRepository<SiacDSoggettoStatoFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	String condizione2 = " ( (soggstat.dataInizioValidita < :dataInput)  AND (soggstat.dataFineValidita IS NULL OR :dataInput < soggstat.dataFineValidita) AND soggstat.dataCancellazione IS NULL ) ";


	
	
	@Query("Select soggstat FROM SiacDSoggettoStatoFin soggstat WHERE soggstat.soggettoStatoCode = :code " +
			" and soggstat.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId and "+condizione2)
    public List<SiacDSoggettoStatoFin> findValidoByEnteAndByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("code") String code);


	
	@Query("FROM SiacDSoggettoStatoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacDSoggettoStatoFin> findListaSoggettoStatoOperativo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);

	
}
