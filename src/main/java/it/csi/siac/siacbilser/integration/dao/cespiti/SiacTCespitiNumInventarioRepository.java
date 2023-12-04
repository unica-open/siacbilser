/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiNumInventario;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacTCespitiNumInventarioRepository extends JpaRepository<SiacTCespitiNumInventario, Integer> {
	
	@Query(  "SELECT o " +
			" FROM SiacTCespitiNumInventario o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.numInventarioPrefisso = :code " +
			" AND o.dataCancellazione IS NULL ")
	SiacTCespitiNumInventario findCespitiNumInventarioByCodiceEEnte(@Param("code") String code, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(  "SELECT o " +
			" FROM SiacTCespitiNumInventario o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.dataCancellazione IS NULL ")
	SiacTCespitiNumInventario findCespitiNumInventarioByEnte(@Param("enteProprietarioId") Integer enteProprietarioId);

}
