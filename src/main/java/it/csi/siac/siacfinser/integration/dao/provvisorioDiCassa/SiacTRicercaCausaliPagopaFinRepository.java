/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTRicercaCausaliPagopaFin;

public interface SiacTRicercaCausaliPagopaFinRepository extends JpaRepository<SiacTRicercaCausaliPagopaFin, Integer> {
	
	@Query("FROM SiacTRicercaCausaliPagopaFin a WHERE a.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTRicercaCausaliPagopaFin> findBySiacTRicercaCausaliPaAll(@Param("enteProprietarioId") Integer enteProprietarioId);
		
}