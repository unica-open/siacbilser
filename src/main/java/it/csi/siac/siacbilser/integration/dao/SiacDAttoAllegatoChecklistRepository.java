/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDAttoAllegatoChecklist;

public interface SiacDAttoAllegatoChecklistRepository extends JpaRepository<SiacDAttoAllegatoChecklist, Integer> {
	
	@Query("FROM SiacDAttoAllegatoChecklist x "
			+ " WHERE x.siacTEnteProprietario.enteProprietarioId=:idEnte "
			+ " AND x.dataCancellazione IS NULL "
			+ " AND x.dataInizioValidita <= CURRENT_TIMESTAMP "
			+ " AND (x.dataFineValidita IS NULL OR x.dataFineValidita > CURRENT_TIMESTAMP) "
	) 
	List<SiacDAttoAllegatoChecklist> findAllAttoAllegatoChecklist(@Param("idEnte") Integer idEnte);
}
