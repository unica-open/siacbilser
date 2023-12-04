/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTMutuoRata;

// TODO: Auto-generated Javadoc
/**
 * Repository JPA per il SiacTMutuo.
 * 
 * @author DT
 * @version 1.0.0 - 13/03/2023
 *
 */
public interface SiacTMutuoRataRepository extends JpaRepository<SiacTMutuoRata, Integer> {

	@Query("SELECT s FROM SiacTMutuoRata s " 
			+ " WHERE s.siacTMutuo.mutuoId=:mutuoId "
			+ " AND s.mutuoRataNumRataPiano=:mutuoRataNumRataPiano "
			+ " AND s.siacTEnteProprietario.enteProprietarioId=:enteId ")
	SiacTMutuoRata findMutuoRataByLogicKey(@Param("mutuoId") Integer mutuoId, @Param("mutuoRataNumRataPiano") Integer mutuoRataNumRataPiano, @Param("enteId") Integer enteId);
	
	@Query("SELECT s FROM SiacTMutuoRata s " 
			+ " WHERE s.siacTMutuo.mutuoId=:mutuoId "
			+ " AND s.mutuoRataNumRataPiano=:mutuoRataNumRataPiano "
			+ " AND s.siacTEnteProprietario.enteProprietarioId=:enteId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND s.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (s.dataFineValidita IS NULL OR s.dataFineValidita > CURRENT_TIMESTAMP) ")
	SiacTMutuoRata findValidMutuoRataByLogicKey(@Param("mutuoId") Integer mutuoId, @Param("mutuoRataNumRataPiano") Integer mutuoRataNumRataPiano, @Param("enteId") Integer enteId);
	
	@Query("SELECT s FROM SiacTMutuoRata s " 
			+ " WHERE s.siacTMutuo.mutuoId=:mutuoId "
			+ " AND s.mutuoRataAnno=:mutuoRataAnno "
			+ " AND s.mutuoRataNumRataAnno=:mutuoRataNumRataAnno "
			+ " AND s.siacTEnteProprietario.enteProprietarioId=:enteId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND s.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (s.dataFineValidita IS NULL OR s.dataFineValidita > CURRENT_TIMESTAMP) ")
	SiacTMutuoRata findValidMutuoRataByLogicKey(@Param("mutuoId") Integer mutuoId, @Param("mutuoRataAnno") Integer mutuoRataAnno, @Param("mutuoRataNumRataAnno") Integer mutuoRataNumRataAnno, @Param("enteId") Integer enteId);	
}
