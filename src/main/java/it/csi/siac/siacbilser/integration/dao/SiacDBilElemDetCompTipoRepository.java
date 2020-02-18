/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;


public interface SiacDBilElemDetCompTipoRepository extends JpaRepository<SiacDBilElemDetCompTipo, Integer> {

	@Query("FROM SiacDBilElemDetCompTipo "
			+ " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND dataCancellazione IS NULL ")
	List<SiacDBilElemDetCompTipo> findAllByEnteProprietarioId(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" SELECT COALESCE(COUNT(*), 0) "
			+ "FROM SiacDBilElemDetCompTipo sde "
			+ " WHERE sde.siacDBilElemDetCompMacroTipo.elemDetCompMacroTipoCode <> :elemDetCompMacroTipoCode "
			+ " AND dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (dataFineValidita IS NULL ) " // OR dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND sde.elemDetCompTipoId IN (:elemDetCompTipoIds)" 
			+ " AND dataCancellazione IS NULL ")
	Long countTipoComponenteWithMacrotipoDiversoDa(@Param("elemDetCompTipoIds") List<Integer> elemDetCompTipoIds, @Param("elemDetCompMacroTipoCode") String elemDetCompMacroTipoCode);
}
