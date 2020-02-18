/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;

/**
 * The Interface SiacRAttoAllegatoStatoRepository.
 */
public interface SiacRAttoAllegatoStatoRepository extends JpaRepository<SiacRAttoAllegatoStato, Integer> {
	

	
	@Query(" FROM SiacRAttoAllegatoStato r  " +	
			"WHERE r.siacTAttoAllegato.attoalId = :attoalId " +
			"ORDER BY r.dataInizioValidita "
			)
	List<SiacRAttoAllegatoStato> findAttoStatoByAttoalIdOrderedyByDataInizioValidita(@Param("attoalId") Integer attoalId);
	
	
}
