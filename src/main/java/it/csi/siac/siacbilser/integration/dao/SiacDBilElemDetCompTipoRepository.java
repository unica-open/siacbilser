/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Date;
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
	
	
	//SIAC-7349
	@Query("FROM SiacDBilElemDetCompTipo "
			+ " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND (dataFineValidita IS NULL OR dataFineValidita > :dataFineValidita) "
			+ " ")
	List<SiacDBilElemDetCompTipo> findAllByEnteProprietarioIdAndFineValidita(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataFineValidita") Date dataFineValidita);
	
	
	//SIAC-7349
	@Query("FROM SiacDBilElemDetCompTipo "
			+ " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ "")
	List<SiacDBilElemDetCompTipo> findAllByOnlyEnteProprietarioId(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	//SIAC-7349
	@Query("FROM SiacDBilElemDetCompTipo "
			+ " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND siacDBilElemDetCompTipoImp.elemDetCompTipoImpCode = '01' "
			+ " AND siacDBilElemDetCompTipoStato.elemDetCompTipoStatoCode = 'V' "
			//SIAC-7868
			+ " AND dataCancellazione IS NULL "
			//FINE SIAC-7868
			+ "")
	List<SiacDBilElemDetCompTipo> findAllByEnteProprietarioIdImpegnabili(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	/**
	 * SIAC-8012
	 *
	 * Calcolo della somma degli importi dei movimenti associati al capitolo
	 * per ogni componente associata ad esso. 
	 * 
	 */
	
}
