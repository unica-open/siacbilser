/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDValuta;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDValutaRepository.
 */
public interface SiacDValutaRepository extends JpaRepository<SiacDValuta, Integer> {
	
	/**
	 * Find by ente proprietario.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query( " SELECT v " +
			" FROM SiacDValuta v " +
			" WHERE v.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND (v.dataFineValidita IS NULL OR v.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND v.dataCancellazione IS NULL " +
			" ORDER BY v.valutaDesc ")
	List<SiacDValuta> findByEnteProprietario(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	/**
	 * Find by ente proprietario id and valuta code.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param valutaCode the valuta code
	 * @return the list
	 */
	@Query(" FROM SiacDValuta v "
			+ " WHERE v.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND v.valutaCode = :valutaCode "
			+ " AND (v.dataFineValidita IS NULL OR v.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND v.dataCancellazione IS NULL ")
	List<SiacDValuta> findByEnteProprietarioIdAndValutaCode(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("valutaCode") String valutaCode);
	
}
