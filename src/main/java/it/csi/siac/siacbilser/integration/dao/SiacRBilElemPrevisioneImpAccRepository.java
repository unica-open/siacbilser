/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemPrevisioneImpacc;

/**
 * The Interface SiacRBilElemClassRepository.
 */
public interface SiacRBilElemPrevisioneImpAccRepository extends JpaRepository<SiacRBilElemPrevisioneImpacc, Integer>{

	@Query( " FROM SiacRBilElemPrevisioneImpacc erre "
			+ " WHERE erre.dataCancellazione IS NULL "
			+ " AND erre.siacTEnteProprietario.enteProprietarioId = :enteId "
			+ " AND erre.siacTBilElem.elemId = :elemId "
			+ " AND erre.siacTBilElem.dataCancellazione IS NULL "			
			)
	public List<SiacRBilElemPrevisioneImpacc> findByCapitolo(@Param("elemId") Integer uidCapitolo, @Param("enteId") Integer enteId);
	
	@Query( " FROM SiacRBilElemPrevisioneImpacc erre "
			+ " WHERE erre.dataCancellazione IS NULL "
			+ " AND erre.siacTEnteProprietario.enteProprietarioId = :enteId "
			+ " AND erre.siacTBilElem.siacTBil.siacTPeriodo.anno = :anno "
			+ " AND erre.siacTBilElem.dataCancellazione IS NULL "			
			)
	public List<SiacRBilElemPrevisioneImpacc> findByAnno(@Param("anno") String anno, @Param("enteId") Integer enteId);

}
