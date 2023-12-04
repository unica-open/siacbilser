/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDNoteTesoriere;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDNoteTesoriereRepository.
 */
public interface SiacDNoteTesoriereRepository extends JpaRepository<SiacTClass, Integer> {
	
	
	/**
	 * Find note tesoriere by ente.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDNoteTesoriere c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY c.notetesCode ")
	List<SiacDNoteTesoriere> findNoteTesoriereByEnte(@Param("enteProprietarioId") Integer enteProprietarioId);
		
}
