/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVarComp;

/**
 * The Interface SiacTBilElemDetVarCompRepository.
 */
public interface SiacTBilElemDetVarCompRepository extends JpaRepository<SiacTBilElemDetVarComp, Integer> {

	@Query(" FROM SiacTBilElemDetVarComp tbedvc "
			+ " WHERE tbedvc.dataCancellazione IS NULL "
			+ " AND tbedvc.siacTBilElemDetVar.dataCancellazione IS NULL "
			+ " AND tbedvc.siacTBilElemDetVar.siacTBilElem.dataCancellazione IS NULL "
			+ " AND tbedvc.siacTBilElemDetVar.siacTBilElem.elemId = :elemId "
			+ " AND tbedvc.siacTBilElemDetVar.siacRVariazioneStato.dataCancellazione IS NULL "
			+ " AND tbedvc.siacTBilElemDetVar.siacRVariazioneStato.siacTVariazione.variazioneId = :variazioneId ")
	List<SiacTBilElemDetVarComp> findByElemIdAndVariazioneId(@Param("elemId") Integer elemId, @Param("variazioneId") Integer variazioneId);
	
	
}
