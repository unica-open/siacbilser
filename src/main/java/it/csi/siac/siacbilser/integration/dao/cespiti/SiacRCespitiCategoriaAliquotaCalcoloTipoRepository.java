/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRCespitiCategoriaAliquotaCalcoloTipo;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacRCespitiCategoriaAliquotaCalcoloTipoRepository extends JpaRepository<SiacRCespitiCategoriaAliquotaCalcoloTipo, Integer> {
	//AND (  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL )
	
	@Query( " FROM SiacRCespitiCategoriaAliquotaCalcoloTipo r " +
			" WHERE r.siacDCespitiCategoria.cescatId = :cescatId " +
			" AND r.dataCancellazione IS NULL " + 
			" AND (r.dataFineValidita IS NULL OR :dataInput <= r.dataFineValidita) " + 
			" AND (r.dataInizioValidita <= :dataInput) "
			)
	List<SiacRCespitiCategoriaAliquotaCalcoloTipo> findRelazioneValidaByIdCategoriaEData(@Param("cescatId") Integer cescatId, @Param("dataInput") Date dataInput);

}


