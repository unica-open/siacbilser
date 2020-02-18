/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCausaleFamTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleTipo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDCausaleTipoRepository.
 */
public interface SiacDCausaleTipoRepository extends JpaRepository<SiacDCausaleTipo, Integer> {

	/**
	 * Find causale tipi by ente e fan.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param causFamTipoCode the caus fam tipo code
	 * @return the list
	 */
	@Query("SELECT c " 
			+ " FROM SiacDCausaleTipo c " 
			+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND c.siacDCausaleFamTipo.causFamTipoCode = :causFamTipoCode " 
			+ " AND c.dataCancellazione IS NULL "
			+ " AND c.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (c.dataFineValidita IS NULL OR c.dataFineValidita > CURRENT_TIMESTAMP) " 
			+ " ORDER BY c.causTipoCode ")
	List<SiacDCausaleTipo> findCausaleTipiByEnteEFan(
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("causFamTipoCode") String causFamTipoCode);
	
	
	@Query("SELECT c " 
			+ " FROM SiacDCausaleTipo c " 
			+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND c.causTipoCode = :causTipoCode " 
			+ " AND c.dataCancellazione IS NULL "
			+ " AND c.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (c.dataFineValidita IS NULL OR c.dataFineValidita > CURRENT_TIMESTAMP) " )
	SiacDCausaleTipo findCausaleTipoByEnteECodice(
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("causTipoCode") String causTipoCode);


	@Query("SELECT ct.siacDCausaleFamTipo " 
			+ " FROM SiacDCausaleTipo ct " 
			+ " WHERE ct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND ct.causTipoId = :causTipoId " 
			+ " AND ct.dataCancellazione IS NULL ")
	SiacDCausaleFamTipo findCausaleFamTipoaByUidCausaleTipo(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("causTipoId") Integer causTipoId);

}
