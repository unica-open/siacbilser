/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTPredocNumRepository.
 */
public interface SiacTPredocRepository extends JpaRepository<SiacTPredoc, Integer> {

	
	/**
	 * Find by ente proprietario.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac t predoc num
	 */
	@Query(" FROM SiacTSubdoc n  "	
			+ " WHERE n.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND n.dataCancellazione IS NULL " 
			+ " AND EXISTS ( "
			+ "   FROM SiacRPredocSubdoc rps "
			+ "   WHERE rps.dataCancellazione IS NULL "
			+ "   and rps.siacTSubdoc = n "
			+ "   AND rps.siacTPredoc.dataCancellazione IS NULL "
			+ "   AND rps.siacTPredoc.predocId = :predocId "
			+ "   AND rps.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " ) "
	)
	List<SiacTSubdoc> findSubdocCollegatiAPredoc(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("predocId") Integer uidPredoc );
	
	
	@Query( "SELECT COALESCE(SUM(d.predocImporto),0) "
			+ " FROM SiacTPredoc d  "	
			+ " WHERE d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND d.dataCancellazione IS NULL " 
			+ " AND d.predocId IN  ( :uidsPredoc ) "
	)
	BigDecimal findImportoPredocByUids(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("uidsPredoc") List<Integer> predocIds );
	
	@Query( "SELECT COALESCE(SUM(d.predocImporto),0) "
			+ " FROM SiacTPredoc d  "	
			+ " WHERE d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND d.dataCancellazione IS NULL " 
			+ " AND d.predocId IN  ( :uidsPredoc ) "
			+ " AND NOT EXISTS ( "
			+ "     FROM  SiacRPredocProvCassa rpc "
			+ "     WHERE rpc.dataCancellazione IS NULL "
			+ "     AND rpc.siacTPredoc = d "
			+ "     AND rpc.siacTProvCassa.provcId = :provcId "
			+ " ) "
	)
	BigDecimal findImportoPredocByUidsNotWithProvc(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("uidsPredoc") List<Integer> predocIds, @Param("provcId") Integer provcId);
	
}
