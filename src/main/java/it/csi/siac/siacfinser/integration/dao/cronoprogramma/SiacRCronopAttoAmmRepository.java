/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.cronoprogramma;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRCronopAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttrFin;

public interface SiacRCronopAttoAmmRepository extends JpaRepository<SiacRCronopAttoAmmFin, Integer> {
	
	@Query("FROM SiacRCronopAttoAmmFin rcaa "
			+ " WHERE rcaa.siacTAttoAmm.uid=:idAttoAmm "
			+ " AND rcaa.dataInizioValidita <= CURRENT_TIMESTAMP "
			+ " AND (rcaa.dataFineValidita IS NULL OR CURRENT_TIMESTAMP < rcaa.dataFineValidita) "
			+ " AND rcaa.dataCancellazione IS NULL ")
	public List<SiacRLiquidazioneAttrFin> findValidByAttoAmmId(@Param("idAttoAmm") Integer idAttoAmm);
}