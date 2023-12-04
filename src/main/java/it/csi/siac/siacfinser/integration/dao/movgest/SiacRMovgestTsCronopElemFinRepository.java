/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsCronopElemFin;

public interface SiacRMovgestTsCronopElemFinRepository extends JpaRepository<SiacRMovgestTsCronopElemFin, Integer> {
	
	@Query("SELECT mtce "
			+ " FROM SiacTMovgestFin m "
			+ " JOIN m.siacTMovgestTs mt"
			+ " JOIN mt.siacRMovgestTsCronopElems mtce "
			+ " WHERE m.movgestId=:movgestId "
			+ " AND m.dataCancellazione IS NULL "
			+ " AND mt.dataCancellazione IS NULL "
			+ " AND mtce.dataCancellazione IS NULL ")
	SiacRMovgestTsCronopElemFin findByMovgestId(@Param("movgestId") Integer movgestId);
	
	
	
	@Query("UPDATE SiacRMovgestTsCronopElemFin "
			+ " SET dataCancellazione=CURRENT_TIMESTAMP, dataFineValidita=CURRENT_TIMESTAMP "
		    + " WHERE siacTMovgestT.movgestTsId=:movgestTsId")
	@Modifying
	void cancellaByMovgestTsId(@Param("movgestTsId") Integer movgestTsId);
	
}