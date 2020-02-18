/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDRichiestaEconStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;

/**
 * The Interface SiacTRichiestaEconRepository.
 */
public interface SiacTRichiestaEconRepository extends JpaRepository<SiacTRichiestaEcon, Integer> {

	

	@Query(" SELECT r.siacDRichiestaEconStato "
			+ " FROM SiacRRichiestaEconStato r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTRichiestaEcon.riceconId = :riceconId ")
	SiacDRichiestaEconStato findStatoByIdRichiesta( @Param("riceconId") Integer riceconId);

	@Query(" SELECT t "
			+ " FROM SiacTRichiestaEcon t "
			+ " WHERE t.riceconNumero = :riceconNumero "
			+ " AND t.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND t.siacTBil.bilId = :bilId "
			+ " AND t.dataCancellazione IS NULL ")
	SiacTRichiestaEcon findByRiceconNumeroAndCassaeconId(@Param("riceconNumero") Integer riceconNumero, @Param("cassaeconId") Integer cassaeconId, @Param("bilId") Integer bilId);
	
	@Query(" SELECT COALESCE(COUNT(tre), 0) "
			+ " FROM SiacTRichiestaEcon tre "
			+ " WHERE tre.dataCancellazione IS NULL "
			+ " AND tre.siacTBil.bilId = :bilId "
			+ " AND EXISTS ( "
			+ "     FROM tre.siacTGiustificativoDets tgd "
			+ "     WHERE tgd.dataCancellazione IS NULL "
			+ "     AND tgd.siacDGiustificativo.giustId = :giustId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tre.siacRRichiestaEconStatos rres "
			+ "     WHERE rres.dataCancellazione IS NULL "
			+ "     AND rres.siacDRichiestaEconStato.riceconStatoCode IN (:riceconStatoCodes) "
			+ " ) "
			)
	Long countBySiacDGiustificativoAndSiacTBilAndSiacDRichiestaEconStatoIn(@Param("giustId") Integer giustId, @Param("bilId") Integer bilId,
			@Param("riceconStatoCodes") Collection<String> riceconStatoCodes);
	
	@Query(" SELECT tre.siacTCassaEcon "
			+ " FROM SiacTRichiestaEcon tre "
			+ " WHERE tre.riceconId = :riceconId ")
	SiacTCassaEcon findSiacTCassaEconByRiceoncId(@Param("riceconId") Integer riceconId);
	
	@Query(" SELECT tg.siacTRichiestaEcon "
			+ " FROM SiacTGiustificativo tg "
			+ " WHERE tg.gstId = :gstId ")
	SiacTRichiestaEcon findBySiacTGiustificativo(@Param("gstId") Integer gstId);

	@Query(" FROM SiacTRichiestaEcon re "
			+ " WHERE re.dataCancellazione IS NULL "
			+ " AND re.siacDRichiestaEconTipo.riceconTipoCode = :riceconTipoCode"
			+ " AND EXISTS ( FROM re.siacRRichiestaEconSubdocs s "
			+ " 			 WHERE s.dataCancellazione IS NULL "
			+ "				 AND s.siacTSubdoc.subdocId = :subdocId "
			+ " 			) " )
	SiacTRichiestaEcon findRichiestePagamentoBySubdocId(@Param("subdocId") Integer subdocId, @Param("riceconTipoCode") String riceconTipoCode);

}
