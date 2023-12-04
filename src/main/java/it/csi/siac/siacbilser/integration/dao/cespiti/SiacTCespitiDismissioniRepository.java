/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacTCespitiDismissioniRepository extends JpaRepository<SiacTCespitiDismissioni, Integer> {
	
	@Query("  SELECT COALESCE(COUNT(tc), 0) " +
		   " FROM SiacTCespiti tc " + 
           " WHERE tc.dataCancellazione IS NULL " +
		   " AND tc.siacTCespitiDismissioni.cesDismissioniId = :cesDismissioniId"
			)
	Long countCespitiCollegatiByDismissioneId(@Param("cesDismissioniId") Integer cesDismissioniId);
	
	@Query("  SELECT COALESCE(COUNT(rcdpn), 0)  " + 
			" FROM SiacRCespitiDismissioniPrimaNota rcdpn " + 
			" WHERE rcdpn.dataCancellazione IS NULL " + 
			" AND rcdpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
			" AND rcdpn.siacTCespitiDismissioni.cesDismissioniId = :cesDismissioniId " + 
			" AND EXISTS ( FROM SiacRPrimaNotaStato rpns, SiacTPrimaNota tp " + 
			"     WHERE rpns.dataCancellazione IS NULL  " + 
			"     AND rpns.siacTPrimaNota = tp " + 
			"     AND rcdpn.siacTPrimaNota = tp " + 
			"     AND rpns.siacDPrimaNotaStato.pnotaStatoCode = :pnotaStatoCode " + 
			" ) "
				)
	Long countPrimeNoteCollegateByDismissioneIdAndCodeStatoPrimaNota(@Param("cesDismissioniId") Integer cesDismissioniId, @Param("pnotaStatoCode") String pnotaStatoCode, @Param("enteProprietarioId") Integer enteProprietarioId);


}
