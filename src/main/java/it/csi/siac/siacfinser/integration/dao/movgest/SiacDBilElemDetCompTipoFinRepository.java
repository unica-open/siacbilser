/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDBilElemDetCompTipoFin;

/**
 * The Interface SiacTBilElemDetCompRepository.
 */
public interface SiacDBilElemDetCompTipoFinRepository extends JpaRepository<SiacDBilElemDetCompTipoFin, Integer> {
	
	@Query(
			" SELECT detCompTipo "
			+ " FROM SiacDBilElemDetCompTipoFin detCompTipo "
			+ " JOIN detCompTipo.siacRMovgestBilElems rMovgestBilElems "
			+ " JOIN rMovgestBilElems.siacTMovgest movgest "
			+ " JOIN movgest.siacDMovgestTipo tipo "
			+ " JOIN movgest.siacTBil.siacTPeriodo periodo "
			+ " WHERE periodo.anno < :annoBil "
			+ " AND movgest.movgestNumero = :movgestNumero "
			+ " AND tipo.movgestTipoCode = :tipoCode "
			+ " AND rMovgestBilElems.dataCancellazione IS NULL "
			)
	public SiacDBilElemDetCompTipoFin getSiacDBilElemDetCompTipoFinPerAdeguamentoComponenteResiduo(
			@Param("movgestNumero") BigDecimal movgestNumero,
			@Param("tipoCode") String movgestTipoCode,
			@Param("annoBil") String annoBil);
	
}
