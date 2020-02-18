/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDPccCausale;

/**
 * The Interface SiacDPccCausaleRepository.
 */
public interface SiacDPccCausaleRepository extends JpaRepository<SiacDPccCausale, Integer> {
	
	@Query(" FROM SiacDPccCausale dppc "
			+ " WHERE dppc.dataCancellazione IS NULL "
			+ " AND dppc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dppc.pcccauCode = :pcccauCode ")
	SiacDPccCausale findByPcccauCodeAndEnteProprietarioId(@Param("pcccauCode") String pcccauCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}
