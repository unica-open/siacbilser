/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDVariazioneTipo;

// TODO: Auto-generated Javadoc
/**
 * Repository per l'entity SiacDVariazioneTipo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/01/2014
 *
 */
public interface SiacDVariazioneTipoRepository extends JpaRepository<SiacDVariazioneTipo, Integer> {
	
	/**
	 * Ottiene la lista di SiacDVariazioneTipo afferenti all'ente di determinato uid.
	 * 
	 * @param enteProprietarioUid l'uid dell'ente
	 * 
	 * @return la lista di SiacDVariazioneTipo
	 */
	@Query("FROM SiacDVariazioneTipo "
			+ "WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioUid "
			+ "AND dataCancellazione IS NULL " 
			+ "AND dataInizioValidita < CURRENT_TIMESTAMP "
			+ "AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) ")
	List<SiacDVariazioneTipo> findByEnteProprietarioUid(@Param("enteProprietarioUid") Integer enteProprietarioUid);
	
}
