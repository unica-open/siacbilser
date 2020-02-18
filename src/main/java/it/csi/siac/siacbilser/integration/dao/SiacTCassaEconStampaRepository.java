/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampaValore;

public interface SiacTCassaEconStampaRepository extends JpaRepository<SiacTCassaEconStampa, Integer> {

	@Query(" FROM SiacTCassaEconStampa ces "
			+ " WHERE ces.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND ces.siacDCassaEconStampaTipo.cestTipoCode = :cestTipoCode "
			+ " AND ces.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND ces.dataCancellazione IS NULL "
			+ " ORDER BY ces.dataModifica DESC ")
	List<SiacTCassaEconStampa> findAllStampeByTipoDocumentoOrderByDataModifica(@Param("cassaeconId") Integer cassaeconId, @Param("cestTipoCode") String cestTipoCode,@Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" SELECT DISTINCT ces "
			+ " FROM SiacTCassaEconStampa ces, SiacRCassaEconStampaStato rcess "
			+ " WHERE rcess.siacTCassaEconStampa = ces "
			+ " AND ces.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND ces.siacDCassaEconStampaTipo.cestTipoCode = :cestTipoCode "
			+ " AND ces.siacTBil.bilId = :bilId "
			+ " AND ces.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND rcess.siacDCassaEconStampaStato.cestStatoCode = :cestStatoCode "
			+ " AND rcess.dataCancellazione IS NULL "
			+ " AND ces.dataCancellazione IS NULL "
			+ " ORDER BY ces.dataModifica DESC ")
	List<SiacTCassaEconStampa> findAllStampeByCassaEconomaleStampaStatoAndCassaEconStampaStatoOrderByDataModifica(@Param("cassaeconId") Integer cassaeconId,
			@Param("cestTipoCode") String cestTipoCode, @Param("cestStatoCode") String cestStatoCode, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId);
	
	
	@Query(" FROM SiacTCassaEconStampa ces "
			+ " WHERE ces.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND ces.siacDCassaEconStampaTipo.cestTipoCode = :cestTipoCode "
			+ " AND ces.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND ces.dataCancellazione IS NULL "
			+ " AND ces.siacDCassaEconStampaTipo.cestTipoCode = :cestTipoCode "
			+ " AND EXISTS ( "
			+ "     FROM ces.siacTCassaEconStampaValores cesv "
			+ "     WHERE cesv.dataCancellazione IS NULL"
			+ "     AND ( "
			+ "         ( "
			+ "	            (DATE_TRUNC('day', CAST(cesv.renPeriodoinizio AS date)) = DATE_TRUNC('day', CAST(CAST(:renPeriodoinizio AS string) AS date)))"
			+ "             AND (DATE_TRUNC('day', CAST(cesv.renPeriodofine AS date)) = DATE_TRUNC('day', CAST(CAST(:renPeriodofine AS string) AS date)))"
			+ "	        ) "
			+ "	        OR ( "
			+ "	            (DATE_TRUNC('day', CAST(cesv.renPeriodoinizio AS date)) <= DATE_TRUNC('day', CAST(CAST(:renPeriodoinizio AS string) AS date)))"
			+ "             AND (DATE_TRUNC('day', CAST(cesv.renPeriodofine AS date)) >= DATE_TRUNC('day', CAST(CAST(:renPeriodoinizio AS string) AS date)))"
			+ "	        ) "
			+ "	    ) "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM ces.siacRCassaEconStampaStatos rcess "
			+ "     WHERE rcess.dataCancellazione IS NULL "
			+ "     AND rcess.siacDCassaEconStampaStato.cestStatoCode = :cestStatoCode "
			+ " ) "
			+ " ORDER BY ces.dataModifica DESC ")
	List<SiacTCassaEconStampa> findAllStampeCorrispondentiPeriodoRendiconto(@Param("renPeriodoinizio") Date renPeriodoinizio,
			@Param("renPeriodofine") Date renPeriodofine,
			@Param("cassaeconId") Integer cassaeconId,
			@Param("cestTipoCode") String cestTipoCode,
			@Param("cestStatoCode") String cestStatoCode,
			@Param("enteProprietarioId") Integer enteProprietarioId);

	
	
	@Query(" SELECT cesv "
			+ " FROM SiacTCassaEconStampaValore cesv, SiacTCassaEconStampa ces "
			+ " WHERE cesv.siacTCassaEconStampa = ces "
			+ " AND cesv.dataCancellazione IS NULL "
			+ " AND cesv.renNum IS NOT NULL "
			+ " AND cesv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND ces.siacTCassaEcon.cassaeconId = :cassaeconId  "
			+ " AND ces.siacDCassaEconStampaTipo.cestTipoCode = :cestTipoCode "
			+ " AND ces.siacTBil.bilId = :bilId "
			+ " AND EXISTS ( "
			+ "     FROM ces.siacRCassaEconStampaStatos rcess "
			+ "     WHERE rcess.dataCancellazione IS NULL "
			+ "     AND rcess.siacDCassaEconStampaStato.cestStatoCode IN (:cestStatoCodes)"
			+ " ) "
			+ " ORDER BY cesv.renNum DESC ")
	List<SiacTCassaEconStampaValore> findStampaUltimoNumeroRendiconto(@Param("cassaeconId") Integer cassaeconId, @Param("cestTipoCode") String cestTipoCode,@Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId, @Param("cestStatoCodes") Collection<String> cestStatoCodes);
	
	
	@Query("SELECT cesv FROM SiacTCassaEconStampaValore cesv, SiacTCassaEconStampa ces "
			+ " WHERE cesv.dataCancellazione IS NULL "
			+ " AND cesv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND cesv.siacTCassaEconStampa.siacTCassaEcon.cassaeconId = :cassaeconId  "
			+ " AND cesv.siacTCassaEconStampa.siacDCassaEconStampaTipo.cestTipoCode = :cestTipoCode "
			+ " AND cesv.siacTCassaEconStampa = ces "
			+ " AND ces.siacTBil.bilId = :bilId "
			+ " AND EXISTS (FROM ces.siacRCassaEconStampaStatos rstato "
			+ "             WHERE rstato.dataCancellazione IS NULL "
			+ "             AND rstato.siacDCassaEconStampaStato.cestStatoCode = :cestStatoCode "
			+ "            )"
			+ " ORDER BY cesv.gioUltimadatastampadef DESC "
			
			)
	List<SiacTCassaEconStampaValore> findUltimaStampaDefinitivaGiornaleCassa(@Param("cassaeconId") Integer cassaeconId, @Param("cestTipoCode") String cestTipoCode, @Param("cestStatoCode") String cestStatoCode, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId);
	
	
	@Query("SELECT cesv FROM SiacTCassaEconStampaValore cesv, SiacTCassaEconStampa ces "
			+ " WHERE cesv.dataCancellazione IS NULL "
			+ " AND cesv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND cesv.siacTCassaEconStampa.siacTCassaEcon.cassaeconId = :cassaeconId  "
			+ " AND cesv.siacTCassaEconStampa.siacDCassaEconStampaTipo.cestTipoCode = :cestTipoCode "
			+ " AND cesv.siacTCassaEconStampa = ces "
			+ " AND ces.siacTBil.bilId = :bilId "
			+ " AND EXISTS (FROM ces.siacRCassaEconStampaStatos rstato "
			+ "             WHERE rstato.dataCancellazione IS NULL "
			+ "             AND rstato.siacDCassaEconStampaStato.cestStatoCode = :cestStatoCode "
			+ "            )"
			+ " AND (DATE_TRUNC('day', CAST(cesv.gioUltimadatastampadef AS date)) < DATE_TRUNC('day', CAST(CAST(:gioDataBozza AS string) AS date))) "
			
			+ " ORDER BY cesv.gioUltimadatastampadef DESC "
			
			)
	List<SiacTCassaEconStampaValore> findUltimaStampaDefinitivaPerBozzaGiornaleCassa(@Param("cassaeconId") Integer cassaeconId, @Param("cestTipoCode") String cestTipoCode, @Param("cestStatoCode") String cestStatoCode, @Param("gioDataBozza") Date gioDataBozza, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId);
	
	
	
	
	
	
	
	@Query(" SELECT tcesv "
			+ " FROM SiacTCassaEconStampaValore tcesv, SiacTCassaEconStampa tces "
			+ " WHERE tcesv.dataCancellazione IS NULL "
			+ " AND tcesv.siacTCassaEconStampa = tces "
			+ " AND tcesv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tces.dataCancellazione IS NULL "
			+ " AND tcesv.renNum IS NOT NULL "
			+ " AND EXISTS ( "
			+ "     FROM tces.siacRMovimentoStampas rms "
			+ "     WHERE rms.dataCancellazione IS NULL "
			+ "     AND rms.siacTMovimento.siacTRichiestaEcon.riceconId = :riceconId "
			+ " ) "
			+ " ORDER BY tcesv.renNum DESC ")
	List<SiacTCassaEconStampaValore> findSiacTCassaEconStampaValoreByRiceconIdAndEnteProprietarioId(@Param("riceconId") Integer riceconId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" SELECT tcesv "
			+ " FROM SiacTCassaEconStampaValore tcesv, SiacTCassaEconStampa tces "
			+ " WHERE tcesv.dataCancellazione IS NULL "
			+ " AND tcesv.siacTCassaEconStampa = tces "
			+ " AND tcesv.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tces.dataCancellazione IS NULL "
			+ " AND tcesv.renNum IS NOT NULL "
			+ " AND EXISTS ( "
			+ "     FROM tces.siacRMovimentoStampas rms "
			+ "     WHERE rms.dataCancellazione IS NULL "
			+ "     AND rms.siacTMovimento.siacTGiustificativo.gstId = :gstId "
			+ "     AND rms.siacTMovimento.siacTRichiestaEcon.riceconId = :riceconId "
			+ " ) "
			+ " ORDER BY tcesv.renNum DESC ")
	List<SiacTCassaEconStampaValore> findSiacTCassaEconStampaValoreByGstIdAndRiceconIdAndEnteProprietarioId(@Param("gstId") Integer gstId, @Param("riceconId") Integer riceconId,
			@Param("enteProprietarioId") Integer enteProprietarioId);
}
