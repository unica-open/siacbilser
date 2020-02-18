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

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;

/**
 * Repository per l'entity SiacRSubdocLiquidazione.
 *
 */
public interface SiacRSubdocLiquidazioneRepository extends JpaRepository<SiacRSubdocLiquidazione, Integer> {
	
	@Query(" FROM SiacRSubdocLiquidazione rsl "
			+ " WHERE rsl.siacTLiquidazione.liqId = :liqId "
			+ " AND rsl.dataCancellazione IS NULL ")
	List<SiacRSubdocLiquidazione> findByLiqId(@Param("liqId") Integer liqId);
	
	@Query(" FROM SiacRSubdocLiquidazione rsl "
			+ " WHERE rsl.siacTLiquidazione.liqId = :liqId "
			+ " AND rsl.siacTSubdoc.subdocId = :subdocId "
			+ " AND rsl.dataCancellazione IS NULL ")
	List<SiacRSubdocLiquidazione> findByLiqIdAndSubdocId(@Param("liqId") Integer liqId, @Param("subdocId") Integer subdocId);
	
	@Query(" FROM SiacRSubdocLiquidazione rsl "
			+ " WHERE rsl.siacTLiquidazione.siacTBil.bilId = :bilId "
			+ " AND rsl.siacTSubdoc.subdocId = :subdocId "
			+ " AND rsl.siacTLiquidazione.liqAnno = :liqAnno "
			+ " AND rsl.siacTLiquidazione.liqNumero = :liqNumero "
			+ " AND rsl.dataFineValidita IS NOT NULL "
			+ " ORDER BY rsl.dataFineValidita DESC "
			)
	List<SiacRSubdocLiquidazione> findRelazioniCancellataBySubdocIdAndBilIdOrderByValiditaFineDesc(@Param("subdocId") Integer subdocId, @Param("liqAnno") Integer liqAnno,  @Param("liqNumero") BigDecimal liqNumero, @Param("bilId") Integer bilId);
	
	@Query(" FROM SiacRSubdocLiquidazione rsl "
			+ " WHERE rsl.siacTLiquidazione.siacTBil.bilId = :bilId "
			+ " AND rsl.siacTSubdoc.subdocId = :subdocId "
			+ " AND rsl.siacTLiquidazione.liqAnno = :liqAnno "
			+ " AND rsl.siacTLiquidazione.liqNumero = :liqNumero "
			+ " AND rsl.dataFineValidita IS NULL "
			+ " AND rsl.dataCancellazione IS NULL "			
			)
	List<SiacRSubdocLiquidazione> findUltimaRelazioneValidaBySubdocIdAndBilId(@Param("subdocId") Integer subdocId, @Param("liqAnno") Integer liqAnno,  @Param("liqNumero") BigDecimal liqNumero, @Param("bilId") Integer bilId);
	
}