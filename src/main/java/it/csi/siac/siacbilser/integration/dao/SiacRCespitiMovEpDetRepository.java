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

import it.csi.siac.siacbilser.integration.entity.SiacRCespitiMovEpDet;

/**
 * The Interface SiacTPrimaNotaNumRepository.
 */
public interface SiacRCespitiMovEpDetRepository extends JpaRepository<SiacRCespitiMovEpDet, Integer> {

	@Query("SELECT count(*) "
			+ " FROM "
			+ "  	 SiacRCespitiMovEpDet rcmep, SiacRPnDefAccettazioneStato rpns  "
			+ " WHERE rpns.dataCancellazione IS NULL  "
			+ "      AND rcmep.siacTMovEpDet.siacTMovEp.siacTPrimaNota = rpns.siacTPrimaNota  "
			+ "      AND rpns.siacDPnDefAccettazioneStato.pnStaAccDefCode = :statoCode  "
			+ " 	 AND rcmep.dataCancellazione IS NULL "
			+ " 	 AND rcmep.siacTCespiti.cesId = :cesId " )
	Long countRCespitiMovEpDetByMovEpId(@Param("cesId") Integer cesId, @Param("statoCode") String statoCode);

	@Query("SELECT rcmep "
			+ " FROM "
			+ "  	 SiacRCespitiMovEpDet		rcmep  "
			+ " WHERE "
			+ " 	 rcmep.dataCancellazione IS NULL "
			+ " 	 AND rcmep.siacTCespiti.cesId = :cesId " 
	        + " 	 AND rcmep.siacTMovEpDet.movepDetId = :movepDetId " )	
	List<SiacRCespitiMovEpDet> findSiacRCespitiMovEpDetByCespiteIDAndMovEpDetId(@Param("cesId") Integer cesId , @Param("movepDetId") Integer movepDetId);
	
	@Query("SELECT rcmep "
			+ " FROM "
			+ "  	 SiacRCespitiMovEpDet		rcmep  "
			+ " WHERE "
			+ " 	 rcmep.dataCancellazione IS NULL "
			+ " 	 AND rcmep.siacTCespiti.cesId = :cesId " 
	        + " 	 AND rcmep.siacTMovEpDet.siacTMovEp.movepId = :movepId " )	
	List<SiacRCespitiMovEpDet> findSiacRCespitiMovEpDetByCespiteIDAndMovEpId(@Param("cesId") Integer cesId , @Param("movepId") Integer movepId);

	@Query("SELECT rcmep.cesContestuale "
			+ " FROM SiacRCespitiMovEpDet rcmep  "
			+ " WHERE rcmep.dataCancellazione IS NULL "
			+ " AND rcmep.siacTCespiti.cesId = :cesId " 
	        + " AND rcmep.siacTMovEpDet.movepDetId = :movepDetId " 
			+ " AND rcmep.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			)
	Boolean findInserimentoCespiteContestualeAPrimaNota(@Param("cesId") Integer cesId , @Param("movepDetId") Integer movepDetId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query("SELECT COALESCE(COUNT(rcmep.siacTCespiti.cesId),0) "
			+ " FROM SiacRCespitiMovEpDet  rcmep "
			+ " WHERE rcmep.dataCancellazione IS NULL "
			+ " AND rcmep.siacTCespiti.dataCancellazione IS NULL "
			+ " AND rcmep.siacTMovEpDet.dataCancellazione IS NULL "
			+ " AND rcmep.siacTMovEpDet.siacTMovEp.dataCancellazione IS NULL "
	        + " AND rcmep.siacTMovEpDet.siacTMovEp.siacTPrimaNota.pnotaId = :pnotaId " )	
	Long countCespitiLegatiAPrimaNota(@Param("pnotaId") Integer pnotaId);
	
	@Query("SELECT COALESCE(SUM(rcmep.importoSuPrimaNota), 0) "
			+ " FROM SiacRCespitiMovEpDet rcmep  "
			+ " WHERE rcmep.dataCancellazione IS NULL "
			+ " AND rcmep.siacTCespiti.cesId = :cesId " 
	        + " AND rcmep.siacTMovEpDet.movepDetId = :movEpDetId " 
			)	
	BigDecimal getImportosuRegistroA(@Param("cesId") Integer cesId, @Param("movEpDetId") Integer movEpDetId);

	
}
