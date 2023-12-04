/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDAccreditoGruppoFin;

public interface SiacDAccreditoGruppoRepository extends JpaRepository<SiacDAccreditoGruppoFin, Integer>{
	
	public static final String RICERCA_GRUPPO_PER_TIPO_ACCREDITO = "SELECT grup FROM SiacDAccreditoGruppoFin grup , SiacDAccreditoTipoFin tip WHERE " +
			"grup.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "
						+ " tip.accreditoTipoId = :tipoId AND tip.siacDAccreditoGruppo.accreditoGruppoId = grup.accreditoGruppoId";
	

	@Query(RICERCA_GRUPPO_PER_TIPO_ACCREDITO)
	public SiacDAccreditoGruppoFin findGruppoByTipoId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("tipoId")Integer tipoId);
	
}
