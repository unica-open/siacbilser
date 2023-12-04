/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconModpagTipo;

public interface SiacDCassaEconModpagTipoRepository extends JpaRepository<SiacDCassaEconModpagTipo, Integer> {


	@Query( " FROM SiacDCassaEconModpagTipo mpt " +
			" WHERE mpt.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND mpt.dataCancellazione IS NULL " +
			" AND (mpt.dataFineValidita IS NULL OR mpt.dataFineValidita > CURRENT_TIMESTAMP) ")
	List<SiacDCassaEconModpagTipo> findValideByEnte(@Param("enteProprietarioId")Integer enteProprietarioId);
	
	@Query( " FROM SiacDCassaEconModpagTipo mpt " +
			" WHERE mpt.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND mpt.cassamodpagTipoCode = :cassamodpagTipoCode " +
			" AND mpt.dataCancellazione IS NULL " +
			" AND (mpt.dataFineValidita IS NULL OR mpt.dataFineValidita > CURRENT_TIMESTAMP) ")
	SiacDCassaEconModpagTipo findValideByEnteECodice(@Param("enteProprietarioId")Integer enteProprietarioId, @Param("cassamodpagTipoCode") String cassamodpagTipoCode);
	
}
