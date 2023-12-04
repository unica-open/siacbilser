/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDRelazStatoFin;

public interface SiacDRelazStatoRepository extends JpaRepository<SiacDRelazStatoFin, Integer> {

	@Query("from SiacDRelazStatoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND relazStatoCode = :code ")
	public List<SiacDRelazStatoFin> findDRelazStatoValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code);
	
	
	
}
