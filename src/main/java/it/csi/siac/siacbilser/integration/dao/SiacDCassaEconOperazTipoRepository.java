/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;

public interface SiacDCassaEconOperazTipoRepository extends JpaRepository<SiacDCassaEconOperazTipo, Integer> {

	@Query( " FROM SiacDCassaEconOperazTipo toc " +
			" WHERE toc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND EXISTS ( " +
			"     FROM toc.siacRCassaEconOperazTipoCassas r " +
			"     WHERE r.dataCancellazione IS NULL " +
			"     AND r.siacTCassaEcon.cassaeconId = :cassaeconId " +
			" ) " +
			" AND toc.dataCancellazione IS NULL " +
			" AND (toc.dataFineValidita IS NULL OR toc.dataFineValidita > CURRENT_TIMESTAMP) ")
	List<SiacDCassaEconOperazTipo> findValideByEnteProprietarioIdAndCassaeconId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("cassaeconId") Integer cassaeconId);

	

}
