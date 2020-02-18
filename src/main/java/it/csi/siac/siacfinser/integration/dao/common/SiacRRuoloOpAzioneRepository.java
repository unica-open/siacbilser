/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRRuoloOpAzioneFin;

public interface SiacRRuoloOpAzioneRepository extends JpaRepository<SiacRRuoloOpAzioneFin, Integer>{

	@Query("from SiacRRuoloOpAzioneFin where siacDRuoloOp.ruoloOpId = :ruoloOpId AND siacTAzione.azioneCode = :azione")
	List<SiacRRuoloOpAzioneFin> findByAzioneERuolo(@Param("ruoloOpId") Integer ruoloOpId, @Param("azione") String azione);
}
