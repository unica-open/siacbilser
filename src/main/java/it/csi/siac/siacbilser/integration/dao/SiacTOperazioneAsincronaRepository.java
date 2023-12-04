/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTOperazioneAsincrona;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTPredocNumRepository.
 */
public interface SiacTOperazioneAsincronaRepository extends JpaRepository<SiacTOperazioneAsincrona, Integer> {

	
	/**
	 * Find by ente proprietario.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac t predoc num
	 */
	@Query(" SELECT stoa " +
			" FROM SiacTOperazioneAsincrona stoa " + 
			" WHERE stoa.dataCancellazione IS NULL " +
			" AND stoa.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"+
			" AND stoa.siacTVariazione.variazioneId = :variazioneId " + 
			" AND stoa.siacTAzione.dataCancellazione IS NULL " + 
			" AND stoa.siacTAzione.azioneCode = :azioneCode " +
			" AND NOT EXISTS ( "+ 
			"    FROM stoa.siacTOperazioneAsincronaDets dets " +
			"    WHERE dets.dataCancellazione IS NULL " +
			"    AND dets.opasDetCode IN (:opasDetCodes) " +
			")" 
		)
	Page<SiacTOperazioneAsincrona> ottieniLaPiuRecenteOperazioneAsincronaByAzioneEUidVariazione(@Param("variazioneId") Integer variazioneId, @Param("azioneCode") String azioneCode, @Param("opasDetCodes") List<String> opasDetCodes, @Param("enteProprietarioId") Integer enteProprietarioId, Pageable pageable);
	
}
