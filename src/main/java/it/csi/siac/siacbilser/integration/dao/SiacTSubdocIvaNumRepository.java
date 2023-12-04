/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaNum;

// TODO: Auto-generated Javadoc
/**
 * Repository per l'entity SiacTSubdocNum.
 *
 */
public interface SiacTSubdocIvaNumRepository extends JpaRepository<SiacTSubdocIvaNum, Integer> {
	
	
	/**
	 * Find by anno and ente.
	 *
	 * @param subdocivaAnno the subdociva anno
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac t subdoc iva num
	 */
	@Query("FROM SiacTSubdocIvaNum n WHERE n.subdocivaAnno = :subdocivaAnno "
			+ "AND n.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "AND n.dataCancellazione IS NULL")
	SiacTSubdocIvaNum findByAnnoAndEnte(@Param("subdocivaAnno") Integer subdocivaAnno, @Param("enteProprietarioId") Integer enteProprietarioId);
	

	
}
