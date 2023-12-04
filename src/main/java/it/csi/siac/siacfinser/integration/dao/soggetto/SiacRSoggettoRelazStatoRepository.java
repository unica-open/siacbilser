/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazStatoFin;

public interface SiacRSoggettoRelazStatoRepository extends JpaRepository<SiacRSoggettoRelazStatoFin, Integer>{

	@Query("from SiacRSoggettoRelazStatoFin where siacRSoggettoRelaz.soggettoRelazId = :soggRelazId")
	SiacRSoggettoRelazStatoFin findBySoggettoRelazId(@Param("soggRelazId") Integer idSoggetto);
	
	
}
 