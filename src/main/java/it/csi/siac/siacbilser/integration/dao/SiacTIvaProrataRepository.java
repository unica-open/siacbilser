/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaProrata;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTIvaProrataRepository.
 */
public interface SiacTIvaProrataRepository extends JpaRepository<SiacTIvaProrata, Integer> {
	
	/**
	 * Find by anno e gruppo.
	 *
	 * @param ivagruproAnno the ivagrupro anno
	 * @param ivagruId the ivagru id
	 * @return the siac t iva prorata
	 */
	@Query( " SELECT p " +
			" FROM SiacTIvaProrata p " +
			" WHERE p.dataCancellazione IS NULL " +
			" AND EXISTS (" +
			" 	FROM p.siacRIvaGruppoProratas igp "  +
			" 	WHERE igp.ivagruproAnno = :ivagruproAnno"  +
			" 	AND igp.siacTIvaGruppo.ivagruId = :ivagruId "  +
			" 	AND igp.dataCancellazione IS NULL "  +
			" ) " )
	SiacTIvaProrata findByAnnoEGruppo(@Param("ivagruproAnno") Integer ivagruproAnno, @Param("ivagruId") Integer ivagruId);
	


}
