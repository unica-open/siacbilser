/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.saldo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTCbpiSaldoAddebitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCbpiSaldoFin;

public interface SiacTCbpiSaldoAddebitoRepository extends JpaRepository<SiacTCbpiSaldoAddebitoFin, Integer>
{
	@Query(" FROM SiacTCbpiSaldoAddebitoFin a "
			+ " WHERE a.saldo=:saldo "
			+ " AND a.data>=:dataInizio AND a.data<:dataFine "
			+ " AND a.dataCancellazione IS NULL "
			+ " ORDER BY a.data DESC ")
	public List<SiacTCbpiSaldoAddebitoFin> readAddebitiSaldo(
			@Param("saldo") SiacTCbpiSaldoFin saldo,
			@Param("dataInizio") Date dataInizio,
			@Param("dataFine") Date dataFine
		);
}
