/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

public class LiquidazioneScritturaGenInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Liquidazione liquidazione;
	private Impegno impegno;
	
	public Liquidazione getLiquidazione() {
		return liquidazione;
	}
	public void setLiquidazione(Liquidazione liquidazione) {
		this.liquidazione = liquidazione;
	}
	public Impegno getImpegno() {
		return impegno;
	}
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	
}
