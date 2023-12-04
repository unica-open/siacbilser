/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDDistinta;

public interface SiacDDistintaBilRepository extends JpaRepository<SiacDDistinta, Integer> {
	final String AND_DATA_VALIDITA_E_CANCELLAZIONE = " AND ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	
	@Query("FROM SiacDDistinta "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND siacDDistintaTipo.distTipoCode = :distTipoCode "
            +"AND distCode = :distCode "
            + AND_DATA_VALIDITA_E_CANCELLAZIONE
            + "order by distCode ASC"
			)
	SiacDDistinta findDDistintaValidaByEnteAndTipoAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,
			 @Param("distTipoCode") String distTipoCode,
			 @Param("distCode") String distCode,
			 @Param("dataInput") Date  dataInput);	
}
