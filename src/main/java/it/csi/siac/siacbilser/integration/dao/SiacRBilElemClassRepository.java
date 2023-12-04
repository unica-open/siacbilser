/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;

/**
 * The Interface SiacRBilElemClassRepository.
 */
public interface SiacRBilElemClassRepository extends JpaRepository<SiacRBilElemClass, Integer>{

	@Query(" SELECT COALESCE(COUNT(DISTINCT rbec.siacTClass.classifId), 0) "
			+ " FROM SiacRBilElemClass rbec "
			+ " WHERE rbec.dataCancellazione IS NULL "
			+ " AND rbec.siacTClass.siacDClassTipo.classifTipoCode = :classifTipoCode "
			+ " AND EXISTS ( "
			+ "     FROM SiacRVariazioneStato rvs, SiacTBilElemDetVar tbedv "
			+ "     WHERE tbedv.siacTBilElem = rbec.siacTBilElem "
			+ "     AND tbedv.siacRVariazioneStato = rvs "
			+ "     AND rvs.siacTVariazione.variazioneId = :variazioneId "
			+ "     AND tbedv.dataCancellazione IS NULL "
			+ "     AND rvs.dataCancellazione IS NULL "
			+ " ) ")
	Long countDistinctClassifByVariazioneIdAndClassifTipoCode(@Param("variazioneId") Integer variazioneId, @Param("classifTipoCode") String classifTipoCode);
	
}
