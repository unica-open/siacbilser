/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;

/**
 * The Interface SiacTBilElemDetCompRepository.
 */
public interface SiacTBilElemDetCompRepository extends JpaRepository<SiacTBilElemDetComp, Integer> {

	@Query(" FROM SiacTBilElemDetComp stbedc "
			+ " WHERE stbedc.siacTBilElemDet.siacTBilElem.elemId = :elemId "
			+ " AND stbedc.siacTBilElemDet.siacTPeriodo.anno = :anno "
			+ " AND stbedc.dataCancellazione IS NULL "
			+ " AND NOT EXISTS ("
			+ "   FROM SiacTBilElemDetVarComp stbedvc "
			+ "   WHERE stbedvc.siacTBilElemDetComp = stbedc "
			+ "   AND stbedvc.elemDetFlag = 'N' "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode <> 'D' "
			+ "   AND stbedvc.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " ) "
			+ " ORDER BY stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId, stbedc.elemDetCompId ")
	List<SiacTBilElemDetComp> findByElemIdAndAnno(@Param("elemId") Integer elemId, @Param("anno") String anno);

	@Query(" FROM SiacTBilElemDetComp stbedc "
			+ " WHERE stbedc.siacTBilElemDet.siacTBilElem.elemId = :elemId "
			+ " AND stbedc.dataCancellazione IS NULL "
			+ " AND NOT EXISTS ("
			+ "   FROM SiacTBilElemDetVarComp stbedvc "
			+ "   WHERE stbedvc.siacTBilElemDetComp = stbedc "
			+ "   AND stbedvc.elemDetFlag = 'N' "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode <> 'D' "
			+ "   AND stbedvc.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " ) "
			+ " ORDER BY stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId, stbedc.elemDetCompId ")
	List<SiacTBilElemDetComp> findByElemId(@Param("elemId") Integer elemId);
	
	@Query(" FROM SiacTBilElemDetComp stbedc "
			+ " WHERE stbedc.siacTBilElemDet.siacTBilElem.elemId = :elemId "
			+ " AND stbedc.dataCancellazione IS NULL "
			+ " AND NOT EXISTS ("
			+ "   FROM SiacTBilElemDetVarComp stbedvc "
			+ "   WHERE stbedvc.siacTBilElemDetComp = stbedc "
			+ "   AND stbedvc.elemDetFlag = 'N' "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode <> 'D' "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacTVariazione.variazioneId <> :variazioneId "
			+ "   AND stbedvc.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " ) "
			+ " ORDER BY stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId, stbedc.elemDetCompId ")
	List<SiacTBilElemDetComp> findByElemIdAndVariazioneId(@Param("elemId") Integer elemId, @Param("variazioneId") Integer variazioneId);
	
	@Query(" FROM SiacTBilElemDetComp stbedc "
			+ " WHERE stbedc.siacTBilElemDet.siacTBilElem.elemId = :elemId "
			+ " AND stbedc.siacTBilElemDet.siacTPeriodo.anno = :anno "
			+ " AND stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId = :elemDetCompTipoId "
			+ " AND stbedc.dataCancellazione IS NULL "
			+ " AND NOT EXISTS ("
			+ "   FROM SiacTBilElemDetVarComp stbedvc "
			+ "   WHERE stbedvc.siacTBilElemDetComp = stbedc "
			+ "   AND stbedvc.elemDetFlag = 'N' "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode <> 'D' "
			+ "   AND stbedvc.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " ) "
			+ " ORDER BY stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId, stbedc.elemDetCompId ")
	List<SiacTBilElemDetComp> findByElemIdAndAnnoAndElemDetCompTipoId(@Param("elemId") Integer elemId, @Param("anno") String anno, @Param("elemDetCompTipoId") Integer elemDetCompTipoId);
	
	@Query(" SELECT stbedc "
			+ " FROM SiacTBilElemDetComp stbedc, SiacTBilElemDetComp stbedc2 "
			+ " WHERE stbedc.siacTBilElemDet.siacTBilElem.elemId = stbedc2.siacTBilElemDet.siacTBilElem.elemId "
			+ " AND stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId = stbedc2.siacDBilElemDetCompTipo.elemDetCompTipoId "
			+ " AND stbedc2.elemDetCompId = :elemDetCompId"
			+ " AND stbedc.dataCancellazione IS NULL "
			+ " AND stbedc2.dataCancellazione IS NULL "
			+ " AND NOT EXISTS ("
			+ "   FROM SiacTBilElemDetVarComp stbedvc "
			+ "   WHERE stbedvc.siacTBilElemDetComp = stbedc "
			+ "   AND stbedvc.elemDetFlag = 'N' "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode <> 'D' "
			+ "   AND stbedvc.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " ) "
			+ " ORDER BY stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId, stbedc.elemDetCompId ")
	List<SiacTBilElemDetComp> findRowByElemDetCompId(@Param("elemDetCompId") Integer elemDetCompId);
	
	@Query(" FROM SiacTBilElemDetComp stbedc "
			+ " WHERE stbedc.siacTBilElemDet.elemDetId = :elemDetId "
			+ " AND stbedc.dataCancellazione IS NULL "
			+ " ORDER BY stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId, stbedc.elemDetCompId ")
	List<SiacTBilElemDetComp> findByElemDetId(@Param("elemDetId") Integer elemDetId);
	
	@Query(" SELECT COALESCE(COUNT(stbedc), 0) "
			+ " FROM SiacTBilElemDetComp stbedc "
			+ " WHERE stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId = :elemDetCompTipoId "
			+ " AND stbedc.siacTBilElemDet.siacTBilElem.elemId = :elemId "
			+ " AND NOT EXISTS ("
			+ "   FROM SiacTBilElemDetVarComp stbedvc "
			+ "   WHERE stbedvc.siacTBilElemDetComp = stbedc "
			+ "   AND stbedvc.elemDetFlag = 'N' "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode <> 'D' "
			+ "   AND stbedvc.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " ) "
			+ " AND stbedc.dataCancellazione IS NULL ")
	Long countByElemIdAndElemDetCompTipoId(@Param("elemId") Integer elemId, @Param("elemDetCompTipoId") Integer elemDetCompTipoId);
	
	@Query(" SELECT COALESCE(COUNT(tbedvc), 0) "
			+ " FROM SiacTBilElemDetVarComp tbedvc "
			+ " WHERE tbedvc.siacTBilElemDetComp.elemDetCompId = :elemDetCompId "
			+ " AND tbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode NOT IN ('D', 'A') "
			+ " AND tbedvc.dataCancellazione IS NULL "
			+ " AND tbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ " AND tbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " AND tbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacTVariazione.dataCancellazione IS NULL ")
	Long countVariazioniByElemDetCompId(@Param("elemDetCompId") Integer elemDetCompId);
	
	@Query(" SELECT COALESCE(COUNT(stbedc), 0) "
			+ " FROM SiacTBilElemDetComp stbedc "
			+ " WHERE stbedc.siacDBilElemDetCompTipo.elemDetCompTipoId = :elemDetCompTipoId "
			+ " AND stbedc.dataCancellazione IS NULL "
			+ " AND stbedc.siacTBilElemDet.dataCancellazione IS NULL "
			+ " AND stbedc.siacTBilElemDet.siacTBilElem.dataCancellazione IS NULL "
			+ " AND stbedc.siacDBilElemDetCompTipo.dataCancellazione IS NULL ")
	Long countByElemDetCompTipoId(@Param("elemDetCompTipoId") Integer elemDetCompTipoId);
	
	@Query(" SELECT dbedct, COALESCE(SUM(tbedc.elemDetImporto), 0) "
			+ " FROM SiacTBilElemDetComp tbedc "
			+ " JOIN tbedc.siacDBilElemDetCompTipo dbedct "
			+ " WHERE tbedc.dataCancellazione IS NULL "
			+ " AND tbedc.siacTBilElemDet.siacTBilElem.siacTBil.bilId = :bilId "
			+ " AND tbedc.siacTBilElemDet.siacTPeriodo.anno = :anno "
			+ " AND NOT EXISTS ("
			+ "   FROM SiacTBilElemDetVarComp stbedvc "
			+ "   WHERE stbedvc.siacTBilElemDetComp = tbedc "
			+ "   AND stbedvc.elemDetFlag = 'N' "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode <> 'D' "
			+ "   AND stbedvc.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ "   AND stbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " ) "
			+ " GROUP BY dbedct ")
	List<Object[]> sumBySiacDBilElemDetCompTipo(@Param("bilId") Integer bilId, @Param("anno") String anno);
}
