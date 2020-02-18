/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRModpagStatoFin;

public interface SiacRModpagStatoRepository extends JpaRepository<SiacRModpagStatoFin, Integer> {

	public String entity = "SiacRModpagStatoFin";
	
	@Query("from SiacRModpagStatoFin where siacTModpag.modpagId = :code ")
	public List<SiacRModpagStatoFin> findStatoValidoByMdpId(@Param("code") Integer mdpId);
	
}
