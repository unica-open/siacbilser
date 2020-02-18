/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPredocNum;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTPredocNumRepository.
 */
public interface SiacTPredocNumRepository extends JpaRepository<SiacTPredocNum, Integer> {

	
	/**
	 * Find by ente proprietario.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac t predoc num
	 */
	@Query(" FROM SiacTPredocNum n  " +	
			" WHERE n.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
	
	)
	SiacTPredocNum findByEnteProprietario(@Param("enteProprietarioId") Integer enteProprietarioId);

	
	@Query("SELECT r.siacTMovgestT "
			+ " FROM SiacRPredocMovgestT r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTPredoc.dataCancellazione IS NULL "
			+ " AND r.siacTPredoc.predocId = :predocId "
			
			)
	SiacTMovgestT findMovgestTSByIdPredoc(@Param("predocId") Integer uid);
	


//	List<SiacRPredocStato> findPredocStatoByPredocIdOrderedyByDataCreazione(@Param("predocId") Integer predocId);
	
	
}
