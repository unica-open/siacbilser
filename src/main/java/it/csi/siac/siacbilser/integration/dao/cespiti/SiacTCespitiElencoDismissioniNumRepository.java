/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElencoDismissioniNum;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacTCespitiElencoDismissioniNumRepository extends JpaRepository<SiacTCespitiElencoDismissioniNum, Integer> {
	
	@Query( " SELECT d " + 
			" FROM SiacTCespitiElencoDismissioniNum d " + 
			" WHERE d.dataCancellazione IS NULL " + 
			" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
			" AND d.elencoDismissioniAnno = :elencoDismissioniAnno "
			)
	SiacTCespitiElencoDismissioniNum findSiacTCespitiElencoDismissioniNumByAnnoAndEnte(@Param("elencoDismissioniAnno") Integer elencoDismissioniAnno, @Param("enteProprietarioId") Integer enteProprietarioId);
}
