/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTCabFin;

public interface SiacTCabRepository extends JpaRepository<SiacTCabFin, Integer>
{

	@Query("SELECT c FROM SiacTCabFin c WHERE c.codice=:cab " +
			" AND c.abi.codice=:abi " +
			" AND c.siacTEnteProprietario.enteProprietarioId=:idEnte")
	public SiacTCabFin findAbiCab(
			@Param("abi") String abi,
			@Param("cab") String cab,
			@Param("idEnte") Integer idEnte);


}
