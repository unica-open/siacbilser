/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTProgrammaNum;

/**
 * The Interface SiacTProgrammaNumRepository.
 */
public interface SiacTProgrammaNumRepository extends JpaRepository<SiacTProgrammaNum, Integer> {
	
	@Query(" FROM SiacTProgrammaNum tpn "
			+ " WHERE tpn.programmaAnno = :programmaAnno "
			+ " AND tpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	SiacTProgrammaNum findByAnno(@Param("programmaAnno") Integer programmaAnno, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}
