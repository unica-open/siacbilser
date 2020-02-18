/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.saldo;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.entity.SiacTCbpiSaldoFin;

public interface SaldoDao extends Dao<SiacTCbpiSaldoFin, Integer>
{
	public BigDecimal leggiSommaImportiPredocumentiEntrataDataCompetenza(Integer idClassifConto, Date data, Integer anno, Integer idEnte);

	public BigDecimal leggiSaldoSpesePrelievi(Integer idClassifConto, Date data, Integer anno, Integer idEnte);
}
