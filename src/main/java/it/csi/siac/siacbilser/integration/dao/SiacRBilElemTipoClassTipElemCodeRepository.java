/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemTipoClassTipElemCode;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRBilElemTipoClassTipElemCodeRepository.
 */
public interface SiacRBilElemTipoClassTipElemCodeRepository extends JpaRepository<SiacRBilElemTipoClassTipElemCode, Integer> {
	
	
	/**
	 * Find class tipo legati.
	 *
	 * @param elemTipoCode the elem tipo code
	 * @param elemCodes the elem codes
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query("SELECT r.siacDClassTipo FROM SiacRBilElemTipoClassTipElemCode r " +
			" WHERE  r.siacDBilElemTipo.elemTipoCode = :elemTipoCode " +
			" AND  r.elemCode IN (:elemCodes) " +
			" AND r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			
			)
	List<SiacDClassTipo> findClassTipoLegati(@Param("elemTipoCode") String elemTipoCode, @Param("elemCodes") List<Integer> elemCodes, @Param("enteProprietarioId") Integer enteProprietarioId);
}
