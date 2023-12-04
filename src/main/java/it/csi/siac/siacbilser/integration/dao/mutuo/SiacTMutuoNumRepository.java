/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTMutuoNum;

public interface SiacTMutuoNumRepository extends JpaRepository<SiacTMutuoNum, Integer> {
	
	@Query("FROM SiacTMutuoNum WHERE siacTEnteProprietario.enteProprietarioId=:enteProprietarioId")
	SiacTMutuoNum findByEnte(@Param("enteProprietarioId") Integer enteProprietarioId);	
}
