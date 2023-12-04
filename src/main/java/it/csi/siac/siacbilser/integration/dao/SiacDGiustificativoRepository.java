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
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativo;

public interface SiacDGiustificativoRepository extends JpaRepository<SiacDGiustificativo, Integer> {


	@Query( " FROM SiacDCassaEconOperazTipo toc " +
			" WHERE toc.cassaeconopTipoCode = :cassaeconopTipoCode " +
			" AND toc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND toc.dataCancellazione IS NULL " +
			" AND (toc.dataFineValidita IS NULL OR toc.dataFineValidita > CURRENT_TIMESTAMP) ")
	List<SiacDCassaEconOperazTipo> findByCodiceEEnte(
			@Param("cassaeconopTipoCode") String cassaeconopTipoCode, 
			@Param("enteProprietarioId")Integer enteProprietarioId);

	@Query( " FROM SiacDGiustificativo g " +
			" WHERE g.giustCode = :giustCode " +
			" AND g.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND g.dataCancellazione IS NULL " +
			" AND (g.dataFineValidita IS NULL OR g.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND g.siacDGiustificativoTipo.giustTipoCode = :giustTipoCode " +
			// Lotto M
			" AND EXISTS ( " +
			"     FROM g.siacRCassaEconGiustificativos r " +
			"     WHERE r.dataCancellazione IS NULL " +
			"     AND r.siacTCassaEcon.cassaeconId = :cassaeconId " +
			" ) "
			)
	List<SiacDGiustificativo> findByEnteECodiceETipoECassa(
			@Param("enteProprietarioId")Integer enteProprietarioId, 
			@Param("giustCode") String giustCode,
			@Param("giustTipoCode") String giustTipoCode, @Param("cassaeconId") Integer cassaeconId);
	
	@Query( " FROM SiacDGiustificativo g " +
			" WHERE g.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND g.dataCancellazione IS NULL " +
			" AND (g.dataFineValidita IS NULL OR g.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND g.siacDGiustificativoTipo.giustTipoCode = :giustTipoCode " +
			// Lotto M
			" AND EXISTS ( " +
			"     FROM g.siacRCassaEconGiustificativos r " +
			"     WHERE r.dataCancellazione IS NULL " +
			"     AND r.siacTCassaEcon.cassaeconId = :cassaeconId " +
			" ) " +
			" ORDER BY g.giustCode ")
	List<SiacDGiustificativo> findByEnteETipoECassa(
			@Param("enteProprietarioId")Integer enteProprietarioId,
			@Param("giustTipoCode") String giustTipoCode,
			@Param("cassaeconId") Integer cassaeconId);


	
}
