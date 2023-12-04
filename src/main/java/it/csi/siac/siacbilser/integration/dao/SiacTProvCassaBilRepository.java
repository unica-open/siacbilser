/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;

/**
 * Repository per l'entity SiacTSubdocRepository.
 *
 */
public interface SiacTProvCassaBilRepository extends JpaRepository<SiacTProvCassa, Integer> {

	@Query("SELECT pc.provcCausale " +
			" FROM SiacTProvCassa pc " +
			" WHERE pc.provcId = :provcId ")
	String findCausaleByUidProvCassa(@Param("provcId")Integer provcId);

	@Query("SELECT r.siacTProvCassa " +
			" FROM SiacRSubdocProvCassa r " +
			" WHERE r.siacTSubdoc.subdocId = :subdocId " +
			" AND r.dataCancellazione IS NULL")
	SiacTProvCassa findSiacTProvCassaBySubdocId(@Param("subdocId")Integer subdocId);
	
	
	//SIAC-7556
	@Query("SELECT pc " +
			" FROM SiacTProvCassa pc " +
			" WHERE pc.provcNumero = :numero "
			+ " AND pc.dataCancellazione IS NULL "
			+ " AND pc.provcAnno = :anno "
			+ " AND pc.siacDProvCassaTipo.provcTipoCode = :tipo "
			+ " AND pc.siacTEnteProprietario.enteProprietarioId = :enteId")
	List<SiacTProvCassa> findProvCassaByAnnoNumTipo(@Param("numero")BigDecimal numero, @Param("anno")Integer anno, @Param("tipo")String tipo
			, @Param("enteId")Integer enteId);
	
	
}
