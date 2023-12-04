/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCausaleEpTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCollegamentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;

/**
 * The Interface SiacDEventoRepository.
 */
public interface SiacDEventoRepository extends JpaRepository<SiacDEvento, Integer> {

	@Query(" SELECT de.siacDCollegamentoTipo "
			+ " FROM SiacDEvento de "
			+ " WHERE de.dataCancellazione IS NULL "
			+ " AND CURRENT_TIMESTAMP > de.dataInizioValidita "
			+ " AND (de.dataFineValidita IS NULL OR CURRENT_TIMESTAMP < de.dataFineValidita) "
			+ " AND de.siacDEventoTipo.eventoTipoId = :eventoTipoId ")
	List<SiacDCollegamentoTipo> findSiacDCollegamentoTipoByEventoTipoId(@Param("eventoTipoId") Integer eventoTipoId);
	
	@Query(" FROM SiacDEvento de "
			+ " WHERE de.dataCancellazione IS NULL "
			+ " AND CURRENT_TIMESTAMP > de.dataInizioValidita "
			+ " AND (de.dataFineValidita IS NULL OR CURRENT_TIMESTAMP < de.dataFineValidita) "
			+ " AND de.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND de.eventoCode IN (:eventoCodes) ")
	List<SiacDEvento> findByEnteProprietarioIdAndEventoCodes(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("eventoCodes") Collection<String> eventoCodes);

	@Query(" FROM SiacDEvento de "
			+ " WHERE de.dataCancellazione IS NULL "
			+ " AND CURRENT_TIMESTAMP > de.dataInizioValidita "
			+ " AND (de.dataFineValidita IS NULL OR CURRENT_TIMESTAMP < de.dataFineValidita) "
			+ " AND de.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND de.siacDCollegamentoTipo.collegamentoTipoCode = :collegamentoTipoCode ")
	List<SiacDEvento> findBySiacDCollegamentoTipoAndEnteProprietario(@Param("collegamentoTipoCode") String collegamentoTipoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query( " SELECT r.siacDCausaleEpTipo "
			+ " FROM SiacRCausaleEpTipoEventoTipo r, SiacDEvento de "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacDEventoTipo = de.siacDEventoTipo "
			+ " AND  de.eventoId = :eventoId " 
			+ " AND r.siacDCausaleEpTipo.causaleEpTipoCode = :causaleEpTipoCode "
		)
	List<SiacDCausaleEpTipo> findSiacDCausaleEpTipoEventoByUidEventoAndCausaleEpTipoCode(@Param("eventoId") Integer eventoId, @Param("causaleEpTipoCode") String causaleEpTipoCode);
	
	

}
