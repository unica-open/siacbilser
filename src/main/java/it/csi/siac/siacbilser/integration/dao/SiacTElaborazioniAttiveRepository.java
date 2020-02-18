/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTElaborazioniAttive;

/**
 * Repository per l'entity SiacTSubdocRepository.
 *
 */
public interface SiacTElaborazioniAttiveRepository extends JpaRepository<SiacTElaborazioniAttive, Integer> {
	
	
	/**
	 * Ottiene la lista di SiacTSubdoc legati al documento avente l'id passato come parametro.
	 * 
	 * @param docId id del documento.
	 * 
	 * @return la lista di SiacTSubdoc.
	 */
	@Query("FROM SiacTElaborazioniAttive "
			+ "WHERE dataCancellazione IS NULL "
			+ "AND elabService = :elabService "
			+ "AND elabKey = :elabKey "
			)
	SiacTElaborazioniAttive findByElabServiceAndElabKey(@Param("elabService") String elabService, @Param("elabKey") String elabKey);
	
	@Query("   SELECT ea.elabKey "
			+ " FROM SiacTElaborazioniAttive ea "
			+ " WHERE ea.dataCancellazione IS NULL "
			+ " AND ea.elabService = :elabService "
			+ " AND ea.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			)
	List<String> findByElabKeyByElabService(@Param("elabService") String elabService, @Param("enteProprietarioId") Integer enteProprietarioId);
	

	
}
