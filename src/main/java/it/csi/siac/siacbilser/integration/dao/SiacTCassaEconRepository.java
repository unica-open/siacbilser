/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;

/**
 * The Interface SiacTCassaEconRepository.
 */
public interface SiacTCassaEconRepository extends JpaRepository<SiacTCassaEcon, Integer> {



	@Query( " SELECT mpt.cassamodpagTipoId " + 
			" FROM SiacDCassaEconModpagTipo mpt " +
			" WHERE mpt.cassamodpagTipoCode = :cassamodpagTipoCode " +
			" AND mpt.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND mpt.dataCancellazione IS NULL ")
	Integer findUidModpagTipoByCodice(@Param("cassamodpagTipoCode") String cassamodpagTipoCode, @Param("enteProprietarioId")Integer enteProprietarioId);

	
	@Query( " FROM SiacTCassaEcon ce " +
			" WHERE ce.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND ( ce.dataFineValidita IS NULL or ce.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND ce.dataCancellazione IS NULL " +
			" AND EXISTS (FROM ce.siacRCassaEconBils rceb " +
			"			  WHERE rceb.siacTBil.bilId = :bilId " +
			"			  AND rceb.dataCancellazione IS NULL) " + 
			" AND EXISTS (FROM ce.siacRAccountCassaEcons cea " +
			"			  WHERE cea.dataCancellazione IS NULL " +
			"             AND cea.siacTAccount.accountId = :accountId) "
			)
	List<SiacTCassaEcon> findByEnteDataFineValiditaAccountEBilancio(@Param("enteProprietarioId")Integer enteProprietarioId, @Param("accountId")Integer accountId, @Param("bilId")Integer bilId);
	
	@Query( " SELECT COUNT(*) "+
			" FROM SiacTCassaEcon ce " +
			" WHERE ce.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND ( ce.dataFineValidita IS NULL or ce.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND ce.dataCancellazione IS NULL " +
			" AND EXISTS (FROM ce.siacTCassaEconStanzs ces " +
			"			  WHERE ces.siacTBil.bilId = :bilId " +
			"			  AND ces.dataCancellazione IS NULL) " 
			)
	Long countByEnteDataFineValiditaEBilancio(@Param("enteProprietarioId")Integer enteProprietarioId,  @Param("bilId")Integer bilId);
	
}
