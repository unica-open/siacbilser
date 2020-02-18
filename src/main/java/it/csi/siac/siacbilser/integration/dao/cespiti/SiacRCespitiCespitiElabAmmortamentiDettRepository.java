/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRCespitiCespitiElabAmmortamentiDett;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamentiDett;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacRCespitiCespitiElabAmmortamentiDettRepository extends JpaRepository<SiacRCespitiCespitiElabAmmortamentiDett, Integer> {
	
	@Query( " SELECT DISTINCT rdet.siacTCespitiElabAmmortamentiDettAvere " + 
			" FROM SiacRCespitiCespitiElabAmmortamentiDett rdet " + 
			" WHERE rdet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId" + 
			" AND rdet.siacTCespitiElabAmmortamentiDettDare.elabDettId = :elabDettIdDare " + 
			" AND rdet.dataCancellazione IS NULL "
			)
	SiacTCespitiElabAmmortamentiDett findElabDettCollegatoAvere(@Param("elabDettIdDare") Integer elabDettIdDare, @Param("enteProprietarioId") Integer enteProprietarioId);

	@Query(	" FROM SiacRCespitiCespitiElabAmmortamentiDett rdet " + 
			" WHERE rdet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId" + 
			" AND rdet.siacTCespitiElabAmmortamentiDettDare.elabDettId = :elabDettIdDare " + 
			" AND rdet.siacTCespitiElabAmmortamentiDettAvere.elabDettId = :elabDettIdAvere " + 
			" AND rdet.siacTCespitiElabAmmortamentiDettDare.dataCancellazione IS NULL " + 
			" AND rdet.siacTCespitiElabAmmortamentiDettAvere.dataCancellazione IS NULL " + 
			" AND rdet.dataCancellazione IS NULL "
			)
	List<SiacRCespitiCespitiElabAmmortamentiDett> findByDettagli(@Param("elabDettIdDare") Integer elabDettIdDare, @Param("elabDettIdAvere") Integer elabDettIdAvere, @Param("enteProprietarioId") Integer enteProprietarioId);

}
