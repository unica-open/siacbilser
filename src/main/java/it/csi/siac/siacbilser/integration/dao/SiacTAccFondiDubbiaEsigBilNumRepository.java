/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBilNum;

/**
 * The Interface SiacTAccFondiDubbiaEsigBilNumRepository.
 */
public interface SiacTAccFondiDubbiaEsigBilNumRepository extends JpaRepository<SiacTAccFondiDubbiaEsigBilNum, Integer> {
	
	/**
	 * Find by doc id.
	 *
	 * @param uidDocumento the uid documento
	 * @return the siac t subdoc num
	 */
	@Query("FROM SiacTAccFondiDubbiaEsigBilNum "
			+ " WHERE siacTBil.bilId = :bilId "
			+ " AND siacDAccFondiDubbiaEsigTipo.afdeTipoCode = :afdeTipoCode")
	SiacTAccFondiDubbiaEsigBilNum findByBilIdAndAfdeTipoCode(@Param("bilId") Integer bilId, @Param("afdeTipoCode") String afdeTipoCode);
}
