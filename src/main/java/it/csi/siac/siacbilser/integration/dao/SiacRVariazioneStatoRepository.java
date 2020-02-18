/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;

/**
 * The Interface SiacRAttoAllegatoStatoRepository.
 */
public interface SiacRVariazioneStatoRepository extends JpaRepository<SiacRVariazioneStato, Integer> {
	

	
	@Query(
			" FROM SiacRVariazioneStato r " +	
			" WHERE r.siacTVariazione.variazioneId = :variazioneId " +
			" AND r.dataCancellazione is null " +
			" ORDER BY r.dataInizioValidita "
			)
	List<SiacRVariazioneStato> findRStatoByVariazioneIdOrderedyByDataInizioValidita(@Param("variazioneId") Integer variazioneId);
	
	
}
