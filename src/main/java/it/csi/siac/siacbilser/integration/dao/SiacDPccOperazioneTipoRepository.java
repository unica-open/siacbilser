/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDPccOperazioneTipo;

/**
 * The Interface SiacDPccOperazioneTipoRepository.
 */
public interface SiacDPccOperazioneTipoRepository extends JpaRepository<SiacDPccOperazioneTipo, Integer> {
	
	@Query(" FROM SiacDPccOperazioneTipo dpot "
			+ " WHERE dpot.dataCancellazione IS NULL "
			+ " AND dpot.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dpot.pccopTipoCode = :pccopTipoCode ")
	SiacDPccOperazioneTipo findByCodiceAndEnteProprietarioId(@Param("pccopTipoCode") String pccopTipoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}
