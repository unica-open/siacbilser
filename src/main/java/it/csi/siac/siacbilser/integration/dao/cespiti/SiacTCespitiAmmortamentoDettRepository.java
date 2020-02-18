/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacTCespitiAmmortamentoDettRepository extends JpaRepository<SiacTCespitiAmmortamentoDett, Integer> {

	@Query( " FROM SiacTCespitiAmmortamentoDett tced " + 
			" WHERE tced.dataCancellazione IS NULL " + 
			" AND tced.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
			" AND tced.siacTCespitiAmmortamento.cesAmmId = :cesAmmId " + 
			" AND tced.cesAmmDettAnno = :cesAmmDettAnno " + 
			" AND tced.cesAmmDettId <> :cesAmmDettId "
			)
	List<SiacTCespitiAmmortamentoDett> findDettagliByAmmortamentoAndAnnoDettaglio(@Param("cesAmmId") Integer cesAmmId, @Param("cesAmmDettAnno") Integer cesAmmDettAnno, @Param("cesAmmDettId") Integer cesAmmDettIdDaEscludere, @Param("enteProprietarioId")Integer enteProprietarioId);

	@Query( " FROM SiacTCespitiAmmortamentoDett tced " + 
			" WHERE tced.dataCancellazione IS NULL " + 
			" AND tced.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " + 
			" AND tced.siacTPrimaNota.pnotaId = :pnotaId "
			)
	List<SiacTCespitiAmmortamentoDett> findDettagliByPrimaNota(@Param("pnotaId") Integer pnotaId, @Param("enteProprietarioId")Integer enteProprietarioId);
	
}
