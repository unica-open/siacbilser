/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

public class ScritturaGenRitenuteStessoImpegnoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LiquidazioneScritturaGenInfoDto liquidazioneInserita;
	private OrdinativoPagamentoScritturaGenInfoDto ordinativoInserito;
	
	public LiquidazioneScritturaGenInfoDto getLiquidazioneInserita() {
		return liquidazioneInserita;
	}
	public void setLiquidazioneInserita(
			LiquidazioneScritturaGenInfoDto liquidazioneInserita) {
		this.liquidazioneInserita = liquidazioneInserita;
	}
	public OrdinativoPagamentoScritturaGenInfoDto getOrdinativoInserito() {
		return ordinativoInserito;
	}
	public void setOrdinativoInserito(
			OrdinativoPagamentoScritturaGenInfoDto ordinativoInserito) {
		this.ordinativoInserito = ordinativoInserito;
	}
	
}
