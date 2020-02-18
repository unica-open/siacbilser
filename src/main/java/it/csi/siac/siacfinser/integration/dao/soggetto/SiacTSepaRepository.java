/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTSepaFin;

public interface SiacTSepaRepository extends JpaRepository<SiacTSepaFin, Integer>
{

	@Query("SELECT s FROM SiacTSepaFin s WHERE s.codiceIsoNazione=:codiceIsoNazione " +
			" AND s.siacTEnteProprietario.enteProprietarioId=:idEnte")
	public SiacTSepaFin find(
			@Param("codiceIsoNazione") String codiceIsoNazione,
			@Param("idEnte") Integer idEnte);


}
