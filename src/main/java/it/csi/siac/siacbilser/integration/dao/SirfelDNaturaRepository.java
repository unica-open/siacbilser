/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SirfelDNatura;
import it.csi.siac.siacbilser.integration.entity.SirfelDNaturaPK;

/**
 * The Interface SirfelTFatturaRepository.
 */
public interface SirfelDNaturaRepository extends  JpaRepository<SirfelDNatura, SirfelDNaturaPK> { 
	
	
	
	@Query(" FROM SirfelDNatura tbe "
			+ " WHERE  tbe.id.enteProprietarioId = :enteProprietarioId "
			+ " AND UPPER(tbe.id.codice) = UPPER(:codice) "
			+ " ) ")
	SirfelDNatura findByEnteECodice(@Param("enteProprietarioId")Integer enteProprietarioId, @Param("codice") String codice);


}

