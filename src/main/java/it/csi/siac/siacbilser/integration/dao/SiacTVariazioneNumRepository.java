/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTVariazioneNum;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTVariazioneNumRepository.
 */
public interface SiacTVariazioneNumRepository extends JpaRepository<SiacTVariazioneNum, Integer> {

	/**
	 * Ricerca il numero di una nuova variazione da inserire a partire dall'id del bilancio e dell'ente proprietario.
	 *
	 * @param bilId the bil id
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac t variazione num
	 */
	@Query("FROM SiacTVariazioneNum " 
		+ " WHERE siacTBil.bilId = :bilId "
		+ " AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	SiacTVariazioneNum findByBilancioIdAndEnteProprietarioId(@Param("bilId") int bilId, @Param("enteProprietarioId") int enteProprietarioId);
	
}
