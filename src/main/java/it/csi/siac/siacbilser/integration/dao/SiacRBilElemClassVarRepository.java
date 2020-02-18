/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClassVar;

/**
 * The Interface SiacRBilElemClassVarRepository.
 * 
 * @author Domenico Lisi
 */
public interface SiacRBilElemClassVarRepository extends JpaRepository<SiacRBilElemClassVar, Integer>{
	

	/**
	 * ricerca le variazioni di classificatore di un capitolo specifico.
	 * 
	 * @param elemId
	 * @param integer 
	 * @return varaizioni di classificatore
	 */
	@Query(" FROM SiacRBilElemClassVar r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTBilElem.elemId = :elemId "
			+ " AND r.siacRVariazioneStato.variazioneStatoId = :variazioneStatoId "
			+ "  ")
	List<SiacRBilElemClassVar> findSiacRBilElemClassVarsBySiacBilElemId(@Param("elemId") Integer elemId, @Param("variazioneStatoId") Integer variazioneStatoId);
		
	
}
