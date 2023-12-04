/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDTrasportoMezzo;

/**
 * The Interface SiacDTrasportoMezzoRepository.
 */
public interface SiacDTrasportoMezzoRepository extends JpaRepository<SiacDTrasportoMezzo, Integer> {
	
	@Query(" FROM SiacDTrasportoMezzo d "
			+ " WHERE d.dataCancellazione IS NULL "
			+ " AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " ORDER BY d.mtraCode ")
	List<SiacDTrasportoMezzo> findByEnteProprietarioId(@Param("enteProprietarioId") Integer enteProprietarioId);
	
}
