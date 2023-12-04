/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTAccount;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;

/**
 * The Interface SiacTAccountRepository.
 */
public interface SiacTAccountRepository extends JpaRepository<SiacTAccount, Integer> {
	
	
	@Query("SELECT a.azioneCode "
			+ "FROM SiacTAzione a "
			+ "WHERE a.dataCancellazione IS NULL "
			+ "AND EXISTS (FROM a.siacRRuoloOpAziones ra "
			+ "				 WHERE ra.dataCancellazione IS NULL"
			+ "			     AND EXISTS ( FROM ra.siacDRuoloOp sdro "
			+ "							 WHERE sdro.dataCancellazione IS NULL "
			+ "                          AND ( "
			+ "									EXISTS ( FROM sdro.siacRAccountRuoloOps sraro"
			+ " 									  	WHERE sraro.dataCancellazione IS NULL "	
			+ "										  	AND sraro.siacTAccount.accountId = :accountId "
			+ "											)"
			+ "			     					OR EXISTS ( FROM sdro.siacRGruppoRuoloOps srgro"
			+ "										        WHERE srgro.dataCancellazione IS NULL "
			+ " 									   		AND EXISTS ( FROM srgro.siacTGruppo stg"
			+ "													   		 WHERE stg.dataCancellazione IS NULL"
			+ "															 AND EXISTS ( FROM stg.siacRGruppoAccounts srga"
			+ "																		 WHERE srga.dataCancellazione IS NULL "
			+ "																		 AND srga.siacTAccount.accountId = :accountId "
			+ "																    	)"
			+ "													  		)"	
			+ "											)"	
			+ "								) "
			+ " 						)"
			+ "             )"
			+ "ORDER BY a.azioneCode ")
	List<String> findCodiciAzioniConsentite(@Param("accountId") Integer accountId);

	@Query(" SELECT rac.siacTClass "
			+ " FROM SiacRAccountClass rac "
			+ " WHERE rac.dataCancellazione IS NULL "
			+ " AND rac.siacTAccount.accountId = :accountId "
			+ " AND rac.siacTClass.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) ")
	List<SiacTClass> findSiacTClassByAccountIdAndClassifTipoCodes(@Param("accountId") Integer accountId, @Param("classifTipoCodes") Collection<String> classifTipoCodes);
	
	@Query(" SELECT rac.siacTClass "
			+ " FROM SiacRAccountClass rac "
			+ " WHERE rac.dataCancellazione IS NULL "
			+ " AND rac.siacTAccount.accountId = :accountId "
			+ " AND rac.siacTClass.classifId = :classifId ")
	List<SiacTClass> findSiacTClassByAccountIdAndClassifId(@Param("accountId") Integer accountId, @Param("classifId") Integer classifId);
	
}
