/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.util.List;

import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

public class EsitoGestioneModificheMovimentiDto {

	private static final long serialVersionUID = 1L;

	private List<ModificaMovimentoGestioneSpesa> movimentiImpegno = null;
	private List<ModificaMovimentoGestioneEntrata> movimentiAccertamenti = null;
	private List<SubImpegno> subImpegnoList = null;
	private List<SubAccertamento> subAccertamentoList = null;
	
	public List<ModificaMovimentoGestioneSpesa> getMovimentiImpegno() {
		return movimentiImpegno;
	}
	public void setMovimentiImpegno(
			List<ModificaMovimentoGestioneSpesa> movimentiImpegno) {
		this.movimentiImpegno = movimentiImpegno;
	}
	public List<ModificaMovimentoGestioneEntrata> getMovimentiAccertamenti() {
		return movimentiAccertamenti;
	}
	public void setMovimentiAccertamenti(
			List<ModificaMovimentoGestioneEntrata> movimentiAccertamenti) {
		this.movimentiAccertamenti = movimentiAccertamenti;
	}
	public List<SubImpegno> getSubImpegnoList() {
		return subImpegnoList;
	}
	public void setSubImpegnoList(List<SubImpegno> subImpegnoList) {
		this.subImpegnoList = subImpegnoList;
	}
	public List<SubAccertamento> getSubAccertamentoList() {
		return subAccertamentoList;
	}
	public void setSubAccertamentoList(List<SubAccertamento> subAccertamentoList) {
		this.subAccertamentoList = subAccertamentoList;
	}
}
