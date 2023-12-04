/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDPccDebitoStato;

/**
 * The Interface SiacDPccDebitoStatoRepository.
 */
public interface SiacDPccDebitoStatoRepository extends JpaRepository<SiacDPccDebitoStato, Integer> {
	
	@Query(" FROM SiacDPccDebitoStato dpds "
			+ " WHERE dpds.dataCancellazione IS NULL "
			+ " AND dpds.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dpds.pccdebStatoCode = :pccdebStatoCode ")
	SiacDPccDebitoStato findByPccdebStatoCodeAndEnteProprietarioId(@Param("pccdebStatoCode") String pccdebStatoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}
