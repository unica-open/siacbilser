/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneTitolo;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;

/**
 * The Interface SiacRConciliazioneTitoloRepository.
 */

public interface SiacRConciliazioneTitoloRepository extends JpaRepository<SiacRConciliazioneTitolo, Integer> {
	
	@Query("SELECT COALESCE(COUNT(rct), 0) "
			+ " FROM SiacRConciliazioneTitolo rct "
			+ " WHERE rct.dataCancellazione IS NULL "
			+ " AND rct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND rct.siacTPdceConto.pdceContoId = :pdceContoId "
			+ " AND rct.siacTClass.classifId = :classifId "
			+ " AND rct.siacDConciliazioneClasse.concclaCode = :concclaCode "
			+ " AND rct.conctitId <> :conctitId ")
	Long countByEnteProprietarioIdAndPdceContoIdAndClassifIdAndConcclaCodeAndNotConctitId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("pdceContoId") Integer pdceContoId,
			@Param("classifId") Integer classifId, @Param("concclaCode") String concclaCode, @Param("conctitId") Integer conctitId);
	
	@Query(" FROM SiacRConciliazioneTitolo rct "
			+ " WHERE rct.dataCancellazione IS NULL "
			+ " AND rct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND rct.siacTPdceConto.pdceContoId = :pdceContoId "
			+ " AND EXISTS ( "
			+ "     FROM SiacTClass tc "
			+ "     WHERE tc = rct.siacTClass "
			+ "     AND EXISTS ( "
			+ "         FROM tc.siacRBilElemClasses rbec "
			+ "         WHERE rbec.dataCancellazione IS NULL "
			+ "         AND rbec.siacTBilElem.elemId = :elemId "
			+ "     ) "
			+ " ) ")
	List<SiacRConciliazioneTitolo> findByEnteProprietarioIdAndContoIdAndClassifId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("pdceContoId") Integer pdceContoId,
			@Param("elemId") Integer elemId);

//	@Query(" SELECT rct.siacTPdceConto "
//			+ " FROM SiacRConciliazioneTitolo rct "
//			+ " WHERE rct.dataCancellazione IS NULL "
//			+ " AND rct.siacTPdceConto.dataCancellazione IS NULL "
//			+ " AND rct.siacTClass.classifId = :classifId "
//			+ " AND rct.siacDConciliazioneClasse.concclaCode = :concclaCode " )
//	List<SiacTPdceConto> findContiByTitolo(@Param("concclaCode") String concclaCode, @Param("classifId") Integer classifId);
	
	@Query(" SELECT rct.siacTPdceConto "
			+ " FROM SiacRConciliazioneTitolo rct "
			+ " WHERE rct.dataCancellazione IS NULL "
			+ " AND rct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND rct.siacTClass.classifId = :classifId "
			+ " AND rct.siacDConciliazioneClasse.concclaCode = :concclaCode ")
	List<SiacTPdceConto> findSiacTPdceContoByEnteProprietarioIdAndClassifIdAndClasseConciliazione(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("classifId") Integer classifId,
			@Param("concclaCode") String concclaCode);
}
