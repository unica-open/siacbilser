/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

public class OrdinativoPagamentoScritturaGenInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OrdinativoPagamento ordinativoPagamento;
	private Liquidazione liquidazione;

	public OrdinativoPagamento getOrdinativoPagamento() {
		return ordinativoPagamento;
	}

	public void setOrdinativoPagamento(OrdinativoPagamento ordinativoPagamento) {
		this.ordinativoPagamento = ordinativoPagamento;
	}

	public Liquidazione getLiquidazione() {
		return liquidazione;
	}

	public void setLiquidazione(Liquidazione liquidazione) {
		this.liquidazione = liquidazione;
	}
	
}
