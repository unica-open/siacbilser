/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDTipoDocumentoRepository.
 */
public interface SiacDTipoDocumentoRepository extends JpaRepository<SiacTClass, Integer> {
	
	
	
	/**
	 * Find tipo doc by ente famiglia.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param famiglia the famiglia
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDDocTipo c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND c.siacDDocFamTipo.docFamTipoCode = :famigliaCod "+			
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY c.docTipoCode ")
	List<SiacDDocTipo> findTipoDocByEnteFamiglia(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("famigliaCod")String famiglia);
	
	
	
	
	
	/**
	 * Find tipo doc by ente famiglia flag.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param famiglia the famiglia
	 * @param flagSubordinato the flag subordinato
	 * @param flagRegolarizzazione the flag regolarizzazione
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDDocTipo c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND (:famigliaCod = '' OR :famigliaCod is null OR c.siacDDocFamTipo.docFamTipoCode = :famigliaCod ) "+
			" AND (:flagSubordinato = '' OR :flagSubordinato is null OR EXISTS ( " +
			" 	SELECT r " + 
			"	FROM c.siacRDocTipoAttrs r " +
			"	WHERE r.siacTAttr.attrCode = :flagSubordinato )) " +
			" AND (:flagRegolarizzazione = '' OR :flagRegolarizzazione is null OR EXISTS ( " +
			" 	SELECT r " + 
			"	FROM c.siacRDocTipoAttrs r " +
			"	WHERE r.siacTAttr.attrCode = :flagRegolarizzazione )) " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY c.docTipoCode ")
	List<SiacDDocTipo> findTipoDocByEnteFamigliaFlag(@Param("enteProprietarioId") Integer enteProprietarioId, 
			@Param("famigliaCod")String famiglia, 
			@Param("flagSubordinato") String flagSubordinato,
			@Param("flagRegolarizzazione") String flagRegolarizzazione);
		
}
