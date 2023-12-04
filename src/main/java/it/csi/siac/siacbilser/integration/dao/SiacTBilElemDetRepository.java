/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTBilElemDetRepository.
 */
public interface SiacTBilElemDetRepository extends JpaRepository<SiacTBilElemDet, Integer> {
	
	
	/**
	 * Totale by elem tipo and elem det tipo and periodo.
	 *
	 * @param periodo the periodo
	 * @param enteProprietario the ente proprietario
	 * @param elemTipoCode the elem tipo code
	 * @param elemDetTipoCode the elem det tipo code
	 * @return the big decimal
	 */
	@Query( "SELECT COALESCE(SUM(s.elemDetImporto), 0) " +
			" FROM SiacTBilElem c " +
			" JOIN c.siacTBilElemDets s " +
			/* CAPITOLO */
			" WHERE c.siacDBilElemTipo.elemTipoCode = :elemTipoCode " +
			" AND c.siacTEnteProprietario = :enteProprietario " +
			" AND c.siacTBil.bilId = :bilId " +
			" AND EXISTS ( " +
			"     SELECT sb " + 
			"     FROM c.siacRBilElemStatos sb " + 
			"     WHERE sb.siacDBilElemStato.elemStatoCode = 'VA' " +
			" ) " +
			" AND c.dataCancellazione IS NULL " +
			" AND c.dataInizioValidita < CURRENT_TIMESTAMP " +
			" AND (c.dataFineValidita IS NULL OR c.dataFineValidita > CURRENT_TIMESTAMP) " +
			/* STANZIAMENTO*/
			" AND s.siacDBilElemDetTipo.elemDetTipoCode = :elemDetTipoCode " +
			" AND s.siacTEnteProprietario = :enteProprietario " +
			" AND s.siacTPeriodo = :periodo " +
			" AND s.dataCancellazione IS NULL " +
			" AND s.dataInizioValidita < CURRENT_TIMESTAMP" +
			" AND (s.dataFineValidita IS NULL OR s.dataFineValidita > CURRENT_TIMESTAMP)")
	BigDecimal totaleByElemTipoAndElemDetTipoAndPeriodo(
			@Param("bilId") Integer bilId,
			@Param("periodo") SiacTPeriodo periodo,
			@Param("enteProprietario") SiacTEnteProprietario enteProprietario,
			@Param("elemTipoCode") String elemTipoCode,
			@Param("elemDetTipoCode") String elemDetTipoCode);

	
	

	@Query(" FROM SiacTBilElemDet s " +
			" WHERE s.siacTBilElem.elemId = :bilElemId " +
			" AND s.siacTPeriodo.anno = :annoCompetenza " +
			" AND s.siacDBilElemDetTipo.elemDetTipoCode = :elemDetTipoCode " +
			" AND s.dataCancellazione IS NULL" +
			" AND s.dataInizioValidita < CURRENT_TIMESTAMP" +
			" AND (s.dataFineValidita IS NULL OR s.dataFineValidita > CURRENT_TIMESTAMP) "
			)
	List<SiacTBilElemDet> findBilElemDetsByBilElemIdAndAnnoAndTipo(@Param("bilElemId")Integer bilElemId, @Param("annoCompetenza") String annoCompetenza, @Param("elemDetTipoCode") String elemDetTipoCode);
	
	
	/**
	 * Find bil elem dets by bil elem id and anno.
	 *
	 * @param bilElemId the bil elem id
	 * @param annoCompetenza the anno competenza
	 * @return the list
	 */
	@Query(" FROM SiacTBilElemDet s " +
			" WHERE s.siacTBilElem.elemId = :bilElemId " +
			" AND s.siacTPeriodo.anno = :annoCompetenza " +
			" AND s.dataCancellazione IS NULL " +
			" AND s.dataInizioValidita < CURRENT_TIMESTAMP " +
			" AND (s.dataFineValidita IS NULL OR s.dataFineValidita > CURRENT_TIMESTAMP) "
			)
	List<SiacTBilElemDet> findBilElemDetsByBilElemIdAndAnno(@Param("bilElemId") Integer bilElemId,@Param("annoCompetenza") String annoCompetenza);
	
	/**
	 * Count bil elem dets by bil elem id and anno.
	 *
	 * @param bilElemId the bil elem id
	 * @param annoCompetenza the anno competenza
	 * @return the count
	 */
	@Query(" SELECT COUNT(s) " +
			" FROM SiacTBilElemDet s " +
			" WHERE s.siacTBilElem.elemId = :bilElemId " +
			" AND s.siacTPeriodo.anno = :annoCompetenza " +
			" AND s.dataCancellazione IS NULL " +
			" AND s.dataInizioValidita < CURRENT_TIMESTAMP " +
			" AND (s.dataFineValidita IS NULL OR s.dataFineValidita > CURRENT_TIMESTAMP) "
			)
	Long countBilElemDetsByBilElemIdAndAnno(@Param("bilElemId") Integer bilElemId,@Param("annoCompetenza") String annoCompetenza);
	
	@Query( " SELECT s.elemDetImporto" + 
			" FROM SiacTBilElemDet s " +
			" WHERE s.siacTBilElem.elemId = :bilElemId " +
			" AND s.siacTPeriodo.anno = :annoCompetenza " +
			" AND s.siacDBilElemDetTipo.elemDetTipoCode = :elemDetTipoCode " +
			" AND s.dataCancellazione IS NULL " +
			" AND s.dataInizioValidita < CURRENT_TIMESTAMP " +
			" AND (s.dataFineValidita IS NULL OR s.dataFineValidita > CURRENT_TIMESTAMP) "			
			)
	BigDecimal findElemDetImportoByBilElemIdAndAnno(@Param("bilElemId") Integer bilElemId,@Param("annoCompetenza") String annoCompetenza, @Param("elemDetTipoCode") String elemDetTipoCode);
	
}
