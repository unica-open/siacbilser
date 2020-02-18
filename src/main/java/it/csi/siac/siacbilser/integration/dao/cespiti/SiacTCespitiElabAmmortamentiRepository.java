/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamenti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamentiDett;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacTCespitiElabAmmortamentiRepository extends JpaRepository<SiacTCespitiElabAmmortamenti, Integer> {
	
	@Query( " FROM SiacTCespitiElabAmmortamentiDett tdet " + 
			" WHERE tdet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId" + 
			" AND tdet.dataCancellazione IS NULL " + 
			" AND tdet.siacTCespitiElabAmmortamenti.elabId = :elabId " + 
			" AND tdet.elabDetSegno = :elabDetSegno "
			)
	List<SiacTCespitiElabAmmortamentiDett> findDettagliByIdAnteprimaAndSegno(@Param("elabId") Integer elabId, @Param("elabDetSegno") String elabDetSegno, @Param("enteProprietarioId") Integer enteProprietarioId);

	@Query( " FROM SiacTCespitiElabAmmortamenti tde " + 
			" WHERE tde.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId" + 
			" AND tde.dataCancellazione IS NULL " + 
			" AND tde.anno = :anno "
			)
	SiacTCespitiElabAmmortamenti findAnteprimaByAnno(@Param("anno") Integer anno, @Param("enteProprietarioId") Integer enteProprietarioId);

}
