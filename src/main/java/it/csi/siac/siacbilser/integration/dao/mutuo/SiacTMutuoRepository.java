/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDMutuoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;

// TODO: Auto-generated Javadoc
/**
 * Repository JPA per il SiacTMutuo.
 * 
 * @author DT
 * @version 1.0.0 - 13/03/2023
 *
 */
public interface SiacTMutuoRepository extends JpaRepository<SiacTMutuo, Integer> {

	@Query(" SELECT m.siacDMutuoStato FROM SiacTMutuo m "
			+ " WHERE m.mutuoId = :mutuoId "
			+ " AND m.dataCancellazione IS NULL ")
	SiacDMutuoStato findSiacDMutuoStatoByMutuoId(@Param("mutuoId") Integer mutuoId);
	
	@Query("SELECT s FROM SiacTMutuo s " 
			+ " WHERE s.mutuoNumero=:mutuoNumero "
			+ " AND s.siacTEnteProprietario.enteProprietarioId=:enteId ")
	SiacTMutuo findMutuoByLogicKey(@Param("mutuoNumero") Integer mutuoNumero, @Param("enteId") Integer enteId);
	
	@Query("SELECT s FROM SiacTMutuo s " 
			+ " WHERE s.mutuoNumero=:mutuoNumero "
			+ " AND s.siacTEnteProprietario.enteProprietarioId=:enteId "
			+ " AND s.dataCancellazione IS NULL "
			+ " AND s.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (s.dataFineValidita IS NULL OR s.dataFineValidita > CURRENT_TIMESTAMP) ")
	SiacTMutuo findValidMutuoByLogicKey(@Param("mutuoNumero") Integer mutuoNumero, @Param("enteId") Integer enteId);	
}
