/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDConciliazioneClasse;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;

/**
 * The Interface SiacTCausaleEpRepository.
 */
public interface SiacTCausaleEpRepository extends JpaRepository<SiacTCausaleEp, Integer> {

	
	@Query(" FROM SiacDEvento e "
			+ " WHERE e.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND e.dataCancellazione IS NULL "
			+ " AND (e.dataFineValidita IS NULL OR e.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND e.siacDEventoTipo.eventoTipoId = :eventoTipoId "
			)
	List<SiacDEvento> findSiacDEventoBySiacDEventoTipo(@Param("eventoTipoId") Integer eventoTipoId, @Param("enteProprietarioId") Integer enteProprietarioId);

	
	@Query(" FROM SiacTCausaleEp e "
			+ " WHERE e.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND e.dataCancellazione IS NULL "
			+ " AND (e.dataFineValidita IS NULL OR e.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND e.causaleEpCode = :causaleEpCode "
			// SIAC-5354
			+ " AND e.siacDAmbito.ambitoCode = :ambitoCode "
			)
	SiacTCausaleEp findCausaleEPByCodiceAndEnteAndAmbitoCode(@Param("causaleEpCode") String causaleEpCode,
			@Param("enteProprietarioId") Integer enteProprietarioId, @Param("ambitoCode") String ambitoCode);


	@Query("SELECT COUNT(*)"
			+ " FROM SiacTCausaleEp tce "
			+ " WHERE tce.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND tce.dataCancellazione IS NULL "
			+ " AND (tce.dataFineValidita IS NULL OR tce.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND EXISTS ( "
			+ "     FROM tce.siacRCausaleEpPdceContos rcepc "
			+ " 	WHERE rcepc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "		AND rcepc.dataCancellazione IS NULL "
			+ " 	AND (rcepc.dataFineValidita IS NULL OR rcepc.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND rcepc.siacTPdceConto.pdceContoId IN (:contiIds) "
			+ " ) "
			
			+ " AND EXISTS ( SELECT rcepc.siacTCausaleEp.causaleEpId , count(rcepc.siacTPdceConto.pdceContoId)  "
			+ "     FROM tce.siacRCausaleEpPdceContos rcepc "
			+ " 	WHERE rcepc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " 	AND (rcepc.dataFineValidita IS NULL OR rcepc.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND rcepc.dataCancellazione IS NULL  "
			+ "     GROUP BY rcepc.siacTCausaleEp.causaleEpId "
			+ "     HAVING COUNT(rcepc.siacTPdceConto.pdceContoId) = :numContiEp "
			+ " ) "

			+ " AND EXISTS ( "
			+ "     FROM tce.siacRCausaleEpClasses rcec "
			+ " 	WHERE rcec.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ "		AND rcec.dataCancellazione IS NULL "
			+ " 	AND (rcec.dataFineValidita IS NULL OR rcec.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND rcec.siacTClass.classifId = :contoVLivId "
			+ " ) "
			
			)
	Long countCausaleEpByContiEPAndContiFIN(@Param("contiIds") Collection<Integer> contiIds, @Param("numContiEp") Long numContiEp, @Param("contoVLivId") Integer contoVLivId,  @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query( " FROM SiacTCausaleEp tce "
			+ " WHERE tce.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND tce.dataCancellazione IS NULL "
			+ " AND (tce.dataFineValidita IS NULL OR tce.dataFineValidita > CURRENT_TIMESTAMP) "
			
			+ " AND tce.siacDAmbito.ambitoCode = :ambitoCode "
			
			+ " AND EXISTS ( "
			+ "     FROM tce.siacRCausaleEpPdceContos rcepc "
			+ " 	WHERE rcepc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "		AND rcepc.dataCancellazione IS NULL "
			+ " 	AND (rcepc.dataFineValidita IS NULL OR rcepc.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND rcepc.siacTPdceConto.pdceContoId IN (:contiIds) "
			+ " ) "
			
			+ " AND EXISTS ( SELECT rcepc.siacTCausaleEp.causaleEpId , count(rcepc.siacTPdceConto.pdceContoId)  "
			+ "     FROM tce.siacRCausaleEpPdceContos rcepc "
			+ " 	WHERE rcepc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "     AND rcepc.dataCancellazione IS NULL "
			+ " 	AND (rcepc.dataFineValidita IS NULL OR rcepc.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     GROUP BY rcepc.siacTCausaleEp.causaleEpId "
			+ "     HAVING COUNT(rcepc.siacTPdceConto.pdceContoId) = :numContiEp "
			+ " ) "

			+ " AND EXISTS ( "
			+ "     FROM tce.siacRCausaleEpClasses rcec "
			+ " 	WHERE rcec.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ "		AND rcec.dataCancellazione IS NULL "
			+ " 	AND (rcec.dataFineValidita IS NULL OR rcec.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND rcec.siacTClass.classifId = :contoVLivId "
			+ " ) "
			
			+ " AND EXISTS ( "
			+ "     FROM tce.siacREventoCausales rec "
			+ " 	WHERE rec.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ "		AND rec.dataCancellazione IS NULL "
			+ " 	AND (rec.dataFineValidita IS NULL OR rec.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND rec.siacDEvento.eventoId IN (:eventoIds) "
			+ " ) "
			
			) //TODO: riscrivere query togliendo EXISTS per migliorare le performance!! attualmente ci impiega circa 10 secondi.
	List<SiacTCausaleEp> findCausaleEpByContiEPAndContiFIN(@Param("contiIds") Collection<Integer> contiIds, @Param("numContiEp") Long numContiEp, @Param("contoVLivId") Integer contoVLivId, @Param("eventoIds") Collection<Integer> eventoIds,  @Param("ambitoCode") String ambitoCode, @Param("enteProprietarioId") Integer enteProprietarioId);


	@Query( " FROM SiacTCausaleEp cep " +
			" WHERE cep.dataCancellazione IS NULL " +
			" AND (cep.dataFineValidita IS NULL OR cep.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND EXISTS ( FROM cep.siacREventoCausales rce " +
			" 			   WHERE rce.dataCancellazione IS NULL" +
			"              AND (rce.dataFineValidita IS NULL OR rce.dataFineValidita > CURRENT_TIMESTAMP) " +
			" 			   AND rce.siacDEvento.siacDEventoTipo.eventoTipoCode = :eventoTipoCode " +
			" 			) " +
			" AND EXISTS ( FROM cep.siacRCausaleEpClasses rcc" +
			"  			   WHERE rcc.dataCancellazione IS NULL " +
			"              AND (rcc.dataFineValidita IS NULL OR rcc.dataFineValidita > CURRENT_TIMESTAMP) " +
			"              AND rcc.siacTClass.classifId = :classifId " +
			" 				) " 
			)
	List<SiacTCausaleEp> findCausaleEPByTipoEventoEClassificatore(@Param("eventoTipoCode") String eventoTipoCode, @Param("classifId") Integer classifId);

	@Query(" FROM SiacTCausaleEp tce "
			+ " WHERE tce.dataCancellazione IS NULL "
			+ " AND tce.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			// Tolto per performance, ma valutare se utilizzare getSiacTClassDataValiditaSql
//			+ " AND ( "
//			+ "     DATE_TRUNC('day', TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') )"
//			+ "     BETWEEN DATE_TRUNC('day', tce.dataInizioValidita)"
//			+ "     AND COALESCE(tce.dataFineValidita, DATE_TRUNC('day',TO_TIMESTAMP(CONCAT(:anno, ' 12 31'), 'YYYY MM DD') ) ) "
//			+ " ) "
			+ " AND tce.siacDAmbito.ambitoCode = :ambitoCode "
			+ " AND EXISTS ( "
			+ "     FROM tce.siacRCausaleEpStatos rces "
			+ "     WHERE rces.dataCancellazione IS NULL "
			+ "     AND rces.siacDCausaleEpStato.causaleEpStatoCode = :causaleEpStatoCode "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tce.siacREventoCausales rec "
			+ "     WHERE rec.dataCancellazione IS NULL "
			+ "     AND rec.siacDEvento.eventoId = :eventoId "
			+ " ) "
			+ " AND tce.siacDCausaleEpTipo.causaleEpTipoCode = :causaleEpTipoCode "
			+ " ORDER BY tce.siacDCausaleEpTipo.causaleEpTipoCode, tce.causaleEpCode ")
	List<SiacTCausaleEp> findCausaleEPByEnteProprietarioIdAndAnnoAndAmbitoCodeAndCausaleEpStatoCodeAndCausaleEpTipoCode(@Param("enteProprietarioId") Integer enteProprietarioId, /*@Param("anno") Integer anno,*/
			@Param("ambitoCode") String ambitoCode, @Param("causaleEpStatoCode") String causaleEpStatoCode, @Param("causaleEpTipoCode") String causaleEpTipoCode, @Param("eventoId") Integer eventoId);


	@Query(" FROM SiacDConciliazioneClasse cc "
			+ " WHERE cc.dataCancellazione IS NULL "
			+ " AND EXISTS ( FROM cc.siacRConciliazioneClasseCausaleEps r "
			+ "              WHERE r.dataCancellazione IS NULL " 
			+ "              AND r.siacRCausaleEpPdceConto.siacTCausaleEp.causaleEpId = :causaleEpId "
			+ " ) ")
	List<SiacDConciliazioneClasse> findClasseDiConciliazioneByCausale(@Param("causaleEpId") Integer causaleEpId);

}
