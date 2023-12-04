/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaAffidamento;

// TODO: Auto-generated Javadoc
/**
 * Repository JPA per il SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2014
 *
 */
public interface SiacDProgrammaAffidamentoRepository extends JpaRepository<SiacDProgrammaAffidamento, Integer> {
	


	@Query( " FROM SiacDProgrammaAffidamento dpa"
			+ " WHERE dpa.dataCancellazione IS NULL "
			+ " AND dpa.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dpa.programmaAffidamentoCode = :programmaAffidamentoCode "
			)
	public SiacDProgrammaAffidamento findByCode(@Param("programmaAffidamentoCode") String programmaAffidamentoCode, @Param("enteProprietarioId") Integer enteProprietarioId);

}
