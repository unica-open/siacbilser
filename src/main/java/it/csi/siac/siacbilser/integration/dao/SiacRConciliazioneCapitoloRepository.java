/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneCapitolo;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;

/**
 * The Interface SiacRConciliazioneCapitoloRepository.
 */
@Component
public interface SiacRConciliazioneCapitoloRepository extends JpaRepository<SiacRConciliazioneCapitolo, Integer> {

	@Query("SELECT COALESCE(COUNT(rcc), 0) "
			+ " FROM SiacRConciliazioneCapitolo rcc "
			+ " WHERE rcc.dataCancellazione IS NULL "
			+ " AND rcc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND rcc.siacTPdceConto.pdceContoId = :pdceContoId "
			+ " AND rcc.siacTBilElem.elemId = :elemId "
			+ " AND rcc.conccapId <> :conccapId ")
	Long countByEnteProprietarioIdAndPdceContoIdAndElemIdAndNotConccapId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("pdceContoId") Integer pdceContoId,
			@Param("elemId") Integer elemId, @Param("conccapId") Integer conccapId);

	
	@Query(" SELECT rcc.siacTPdceConto "
			+ " FROM SiacRConciliazioneCapitolo rcc "
			+ " WHERE rcc.dataCancellazione IS NULL "
			+ " AND rcc.siacTPdceConto.dataCancellazione IS NULL "
			+ " AND rcc.siacTBilElem.elemId = :elemId ")
	List<SiacTPdceConto> findSiacTPdceContoByBilElem(@Param("elemId") Integer elemId);
	
	@Query(" SELECT rcc.siacTPdceConto "
			+ " FROM SiacRConciliazioneCapitolo rcc "
			+ " WHERE rcc.dataCancellazione IS NULL "
			+ " AND rcc.siacTPdceConto.dataCancellazione IS NULL "
			+ " AND rcc.siacTBilElem.elemId = :elemId "
			+ " AND rcc.siacDConciliazioneClasse.concclaCode = :concclaCode ")
	List<SiacTPdceConto> findSiacTPdceContoByBilElemAndClasseConciliazione(@Param("elemId") Integer elemId, @Param("concclaCode") String concclaCode);
	
	
}
