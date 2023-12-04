/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaTipo;

// TODO: Auto-generated Javadoc
/**
 * Repository JPA per il SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2014
 *
 */
public interface SiacDProgrammaTipoRepository extends JpaRepository<SiacDProgrammaTipo, Integer> {
	


	@Query( " FROM SiacDProgrammaTipo dpa"
			+ " WHERE dpa.dataCancellazione IS NULL "
			+ " AND dpa.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dpa.programmaTipoCode = :programmaTipoCode "
			)
	public SiacDProgrammaTipo findByCode(@Param("programmaTipoCode") String programmaTipoCode, @Param("enteProprietarioId") Integer enteProprietarioId);

}
