/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoriaCalcoloTipo;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacDCespitiCategoriaCalcoloTipoRepository extends JpaRepository<SiacDCespitiCategoriaCalcoloTipo, Integer> {
	
	@Query(  "SELECT o " +
			" FROM SiacDCespitiCategoriaCalcoloTipo o " +
			" WHERE o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND o.cescatCalcoloTipoCode = :code " +
			" AND o.dataCancellazione IS NULL ")
	SiacDCespitiCategoriaCalcoloTipo findCategoriaCalcoloTipoCespiteByCodiceEEnte(@Param("code") String code, @Param("enteProprietarioId") Integer enteProprietarioId);

}
