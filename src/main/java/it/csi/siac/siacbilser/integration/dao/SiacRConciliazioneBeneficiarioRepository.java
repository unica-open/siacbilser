/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneBeneficiario;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;

/**
 * The Interface SiacRConciliazioneBeneficiario.
 */

public interface SiacRConciliazioneBeneficiarioRepository extends JpaRepository<SiacRConciliazioneBeneficiario, Integer> {

	@Query("SELECT COALESCE(COUNT(rcb), 0) "
			+ " FROM SiacRConciliazioneBeneficiario rcb "
			+ " WHERE rcb.dataCancellazione IS NULL "
			+ " AND rcb.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND rcb.siacTPdceConto.pdceContoId = :pdceContoId "
			+ " AND rcb.siacTBilElem.elemId = :elemId "
			+ " AND rcb.siacTSoggetto.soggettoId = :soggettoId "
			+ " AND rcb.concbenId <> :concbenId ")
	Long countByEnteProprietarioIdAndPdceContoIdAndElemIdAndSoggettoIdAndNotConcbenId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("pdceContoId") Integer pdceContoId,
			@Param("elemId") Integer elemId, @Param("soggettoId") Integer soggettoId, @Param("concbenId") Integer concbenId);

	@Query("SELECT rcb.siacTPdceConto "
			+ " FROM SiacRConciliazioneBeneficiario rcb "
			+ " WHERE rcb.dataCancellazione IS NULL "
			+ " AND rcb.siacTPdceConto.dataCancellazione IS NULL "
			+ " AND rcb.siacTBilElem.elemId = :elemId "
			+ " AND rcb.siacTSoggetto.soggettoId = :soggettoId ")
	List<SiacTPdceConto> findSiacTPdceContoByBilElemESoggetto(@Param("soggettoId") Integer soggettoId, @Param("elemId") Integer elemId);
	
	@Query("SELECT rcb.siacTPdceConto "
			+ " FROM SiacRConciliazioneBeneficiario rcb "
			+ " WHERE rcb.dataCancellazione IS NULL "
			+ " AND rcb.siacTPdceConto.dataCancellazione IS NULL "
			+ " AND rcb.siacTBilElem.elemId = :elemId "
			+ " AND rcb.siacTSoggetto.soggettoId = :soggettoId "
			+ " AND rcb.siacDConciliazioneClasse.concclaCode = :concclaCode ")
	List<SiacTPdceConto> findSiacTPdceContoByBilElemESoggettoEClasseConciliazione(@Param("soggettoId") Integer soggettoId, @Param("elemId") Integer elemId, @Param("concclaCode") String concclaCode);
	
}
