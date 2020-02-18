/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;



import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClassVar;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemVar;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTVariazioneRepository.
 */
public interface SiacTVariazioneRepository extends JpaRepository<SiacTVariazione, Integer> {

	
	/**
	 * Find by numero variazione.
	 *
	 * @param variazioneNum the variazione num
	 * @return the siac t variazione
	 */
	@Query("FROM SiacTVariazione "
			+ "WHERE variazioneNum = :variazioneNum ")
	SiacTVariazione findByNumeroVariazione(@Param("variazioneNum") Integer variazioneNum);
	
	@Query(" SELECT t.variazioneNum "
			+ " FROM SiacTVariazione t "
			+ " WHERE t.dataCancellazione IS NULL "
			+ " AND t.siacDVariazioneTipo.variazioneTipoCode IN (:variazioneTipoCodes) "
			+ " AND EXISTS ( "
			+ "     FROM t.siacRVariazioneStatos rvs "
			+ "     WHERE rvs.dataCancellazione IS NULL "
			+ "     AND rvs.siacDVariazioneStato.variazioneStatoTipoCode NOT IN (:variazioneStatoTipoCodes) "
			+ "     AND EXISTS ( "
			+ "         FROM rvs.siacTBilElemDetVars rbedv "
			+ "         WHERE rbedv.dataCancellazione IS NULL "
			+ "         AND rbedv.siacTBilElem.elemId = :elemId "
			+ "     ) "
			+ " ) "
			+ " ORDER BY t.variazioneNum ")
	List<Integer> getVariazioneNumeroByBilElemIdAndVariazioneTipoCodesInAndVariazioneStatoTipoCodesNotIn(@Param("elemId") Integer uid, @Param("variazioneTipoCodes") Collection<String> variazioneTipoCodes,
			@Param("variazioneStatoTipoCodes") Collection<String> variazioneStatoTipoCodes);
	
	@Query(" SELECT t.variazioneNum "
			+ " FROM SiacTVariazione t "
			+ " WHERE t.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM t.siacRVariazioneStatos rvs "
			+ "     WHERE rvs.dataCancellazione IS NULL "
			+ "     AND rvs.siacDVariazioneStato.variazioneStatoTipoCode NOT IN (:variazioneStatoTipoCodes) "
			+ "     AND EXISTS ( "
			+ "         FROM rvs.siacRBilElemClassVars rbecv "
			+ "         WHERE rbecv.dataCancellazione IS NULL "
			+ "         AND rbecv.siacTBilElem.elemId = :elemId "
			+ "     ) "
			+ " ) "
			+ " ORDER BY t.variazioneNum ")
	List<Integer> getVariazioneCodificheNumeroByBilElemIdAndCodesInAndVariazioneStatoTipoCodesNotIn(@Param("elemId") Integer uid, @Param("variazioneStatoTipoCodes") Collection<String> variazioneStatoTipoCodes);
	
	
	@Query( " FROM SiacTBilElem c"
			+ " WHERE c.dataCancellazione IS NULL"
			+ " AND c.elemId = :elemId"
			+ " AND EXISTS ( "
			+ "     FROM c.siacTBilElemDetVars dv"
			+ "     WHERE dv.dataCancellazione IS NULL"
			+ "     AND EXISTS ("
			+ "    		FROM dv.siacRVariazioneStato rvs "
			+ "    		WHERE rvs.dataCancellazione IS NULL "
			+ "    		AND rvs.siacTVariazione.variazioneId = :variazioneId"
			+ "         ) "
			+ "     ) "
			)
	SiacTBilElem findCapitoloIfInVariazione(@Param("elemId") Integer uidElem, @Param("variazioneId") Integer variazioneId);
	
	@Query(" SELECT dv.siacTBilElem.elemId, dv.siacTPeriodo.anno, dv.siacDBilElemDetTipo.elemDetTipoCode, dv.elemDetImporto "
			+ " FROM SiacTBilElemDetVar dv"
			+ " WHERE dv.dataCancellazione IS NULL"
			+ " AND dv.siacTBilElem.elemId IN (:elemIds)"
			+ " AND dv.siacDBilElemDetTipo.elemDetTipoCode IN ('STA', 'SCA')"
			+ " AND EXISTS ( "
			+ "   FROM dv.siacRVariazioneStato rvs "
			+ "   WHERE rvs.dataCancellazione IS NULL "
			+ "   AND rvs.siacTVariazione.variazioneId = :variazioneId"
			+ " ) ")
	List<Object[]> findStanziamentoCapitoloInVariazione(@Param("elemIds") Collection<Integer> uidElems, @Param("variazioneId") Integer variazioneId);

	
	@Query( " FROM SiacTBilElemDetVar tbedv "
			+ " WHERE tbedv.dataCancellazione IS NULL "
			+ " AND tbedv.siacTBilElem.elemId = :elemId "
			+ " AND tbedv.siacRVariazioneStato.variazioneStatoId = :variazioneStatoId "
			)	
	List<SiacTBilElemDetVar> findDettagliVariazioneImportiByRVarStatoAndCapitolo(@Param("variazioneStatoId") Integer variazioneStatoId, @Param("elemId") Integer elemId);

	@Query( "SELECT tbedv.siacTPeriodo.anno, tbedv.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode, tbedv.siacDBilElemDetTipo.elemDetTipoCode, SUM(COALESCE(tbedv.elemDetImporto, 0)) "
			+ " FROM SiacTBilElemDetVar tbedv "
			+ " WHERE tbedv.dataCancellazione IS NULL "
			+ " AND tbedv.siacTBilElem.elemId = :elemId "
			+ " AND tbedv.siacRVariazioneStato.siacTVariazione.siacTBil.bilId = :bilId "
			+ " AND tbedv.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " AND tbedv.siacRVariazioneStato.siacTVariazione.dataCancellazione IS NULL "
			+ " AND tbedv.elemDetImporto > 0 "
			+ " GROUP BY tbedv.siacTPeriodo.anno, tbedv.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode, tbedv.siacDBilElemDetTipo.elemDetTipoCode ")
	List<Object[]> findTotalePositiveGroupedByImportoCapitolo(@Param("elemId") Integer elemId, @Param("bilId") Integer bilId);
	
	@Query( "SELECT tbedv.siacTPeriodo.anno, tbedv.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode, COALESCE(COUNT(DISTINCT tbedv.siacRVariazioneStato.siacTVariazione), 0) "
			+ " FROM SiacTBilElemDetVar tbedv "
			+ " WHERE tbedv.dataCancellazione IS NULL "
			+ " AND tbedv.siacTBilElem.elemId = :elemId "
			+ " AND tbedv.siacRVariazioneStato.siacTVariazione.siacTBil.bilId = :bilId "
			+ " AND tbedv.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " AND tbedv.siacRVariazioneStato.siacTVariazione.dataCancellazione IS NULL "
			+ " AND tbedv.elemDetImporto > 0 "
			+ " GROUP BY tbedv.siacTPeriodo.anno, tbedv.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode ")
	List<Object[]> findCountPositiveGroupedByImportoCapitolo(@Param("elemId") Integer elemId, @Param("bilId") Integer bilId);
	
	@Query( "SELECT tbedv.siacTPeriodo.anno, tbedv.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode, tbedv.siacDBilElemDetTipo.elemDetTipoCode, SUM(COALESCE(tbedv.elemDetImporto, 0)) "
			+ " FROM SiacTBilElemDetVar tbedv "
			+ " WHERE tbedv.dataCancellazione IS NULL "
			+ " AND tbedv.siacTBilElem.elemId = :elemId "
			+ " AND tbedv.siacRVariazioneStato.siacTVariazione.siacTBil.bilId = :bilId "
			+ " AND tbedv.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " AND tbedv.siacRVariazioneStato.siacTVariazione.dataCancellazione IS NULL "
			+ " AND tbedv.elemDetImporto < 0 "
			+ " GROUP BY tbedv.siacTPeriodo.anno, tbedv.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode, tbedv.siacDBilElemDetTipo.elemDetTipoCode ")
	List<Object[]> findTotaleNegativeGroupedByImportoCapitolo(@Param("elemId") Integer elemId, @Param("bilId") Integer bilId);
	
	@Query( "SELECT tbedv.siacTPeriodo.anno, tbedv.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode, COALESCE(COUNT(DISTINCT tbedv.siacRVariazioneStato.siacTVariazione), 0) "
			+ " FROM SiacTBilElemDetVar tbedv "
			+ " WHERE tbedv.dataCancellazione IS NULL "
			+ " AND tbedv.siacTBilElem.elemId = :elemId "
			+ " AND tbedv.siacRVariazioneStato.siacTVariazione.siacTBil.bilId = :bilId "
			+ " AND tbedv.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " AND tbedv.siacRVariazioneStato.siacTVariazione.dataCancellazione IS NULL "
			+ " AND tbedv.elemDetImporto < 0 "
			+ " GROUP BY tbedv.siacTPeriodo.anno, tbedv.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode ")
	List<Object[]> findCountNegativeGroupedByImportoCapitolo(@Param("elemId") Integer elemId, @Param("bilId") Integer bilId);

	@Query(" FROM SiacTBilElemVar tbev "
			+ " WHERE tbev.dataCancellazione IS NULL "
			+ " AND tbev.siacTBilElem.elemId = :elemId "
			+ " AND tbev.siacRVariazioneStato.siacDVariazioneStato.variazioneStatoTipoCode = '" + SiacDVariazioneStatoEnum.CODICE_DEFINITIVA + "' "
			+ " AND tbev.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " GROUP BY tbev, tbev.siacRVariazioneStato.dataModifica "
			+ " ORDER BY tbev.siacRVariazioneStato.dataModifica "
			)
	Page<SiacTBilElemVar> findStoricoTBilelemVarsStatoDefinivo(@Param("elemId") Integer elemId, Pageable pageable);
	
	 
	@Query(" FROM SiacRBilElemClassVar "
			+ " WHERE siacRVariazioneStato.variazioneStatoId = :variazioneStatoId "
			+ " AND dataCancellazione IS NULL ")
	List<SiacRBilElemClassVar> findSiacRBilElemClassVarByVariazioneStatoId(@Param("variazioneStatoId") Integer variazioneId);
}
