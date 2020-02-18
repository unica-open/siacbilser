/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.mutuo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRMutuoStatoFin;

public interface SiacRMutuoStatoRepository extends JpaRepository<SiacRMutuoStatoFin, Integer> {

	@Query("FROM SiacRMutuoStatoFin rms WHERE rms.siacTMutuo.mutId = :idMutuo ")
	public List<SiacRMutuoStatoFin> findMutuoValidoByCode(@Param("idMutuo") Integer idMutuo );
	
}