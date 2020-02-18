/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCollegamentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacDEventoTipo;

/**
 * The Interface SiacDEventoTipoRepository.
 */
public interface SiacDEventoTipoRepository extends JpaRepository<SiacDEventoTipo, Integer> {

	@Query(" FROM SiacDCollegamentoTipo dct" +
			" WHERE EXISTS ( FROM dct.siacDEventos de " +
			"				 WHERE de.dataCancellazione IS NULL " +		
			" 				 AND de.siacDEventoTipo.dataCancellazione IS NULL "	+
			"				 AND de.siacDEventoTipo.eventoTipoId = :eventoTipoId" +
			" 				)"
			)
	List<SiacDCollegamentoTipo> findTipoCollegamentoByTipoEvento(@Param("eventoTipoId") Integer eventoTipoId);

	@Query(" FROM SiacDEvento de" +
			" WHERE de.siacDEventoTipo.eventoTipoId = :eventoTipoId " +
			" AND de.dataCancellazione IS NULL ")
	List<SiacDEvento> findSiacDEventoByEventoTipoId(@Param("eventoTipoId") Integer eventoTipoId);

}
