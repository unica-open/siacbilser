/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemTipoAttrIdElemCode;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRBilElemTipoAttrIdElemCodeRepository.
 */
public interface SiacRBilElemTipoAttrIdElemCodeRepository extends JpaRepository<SiacRBilElemTipoAttrIdElemCode, Integer> {
	
	
	/**
	 * Find attr id legati.
	 *
	 * @param elemTipoCode the elem tipo code
	 * @param elemCodes the elem codes
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query("SELECT r.siacTAttr FROM SiacRBilElemTipoAttrIdElemCode r " +
			" WHERE  r.siacDBilElemTipo.elemTipoCode = :elemTipoCode " +
			" AND  r.elemCode IN (:elemCodes) " +
			" AND r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			
			)
	List<SiacTAttr> findAttrIdLegati(@Param("elemTipoCode") String elemTipoCode, @Param("elemCodes") List<Integer> elemCodes, @Param("enteProprietarioId") Integer enteProprietarioId);
}
