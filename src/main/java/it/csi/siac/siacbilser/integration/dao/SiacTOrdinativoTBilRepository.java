/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoT;

public interface SiacTOrdinativoTBilRepository extends JpaRepository<SiacTOrdinativoT, Integer> {
	
	
//	@Query(" SELECT ot.ordTsId FROM SiacTOrdinativoT ot"
//			+ " WHERE ot.dataCancellazione IS NULL "
//			+ " AND ot.siacTOrdinativo.ordId = :ordId "
//			+ " AND ot.ordTsDesc = :ordTsDesc ")
//	Integer findUidSubOrdinativoByOrdinativoAndDesc(@Param("ordId") Integer ordId, @Param("ordTsDesc") String ordTsDesc);
	
	
	
	
}