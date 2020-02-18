/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;

public class GestioneOrdPagDGInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1335613810979875879L;

	private Impegno impegno = null;
	private SubImpegno subImpegno = null;
	private Liquidazione liquidazione = null;
	private SubOrdinativoPagamento subOrdinativoPagamento = null;
	private BigDecimal disponibilitaAPagareImpegno = BigDecimal.ZERO;
	private BigDecimal disponibilitaAPagareSubImpegno = BigDecimal.ZERO;
	private BigDecimal disponibilitaAPagareLiquidazione = BigDecimal.ZERO;
	private BigDecimal deltaImportoSubOrdinativo = BigDecimal.ZERO;

	public BigDecimal getDisponibilitaAPagareImpegno() {
		return disponibilitaAPagareImpegno;
	}
	public void setDisponibilitaAPagareImpegno(
			BigDecimal disponibilitaAPagareImpegno) {
		this.disponibilitaAPagareImpegno = disponibilitaAPagareImpegno;
	}
	public BigDecimal getDisponibilitaAPagareSubImpegno() {
		return disponibilitaAPagareSubImpegno;
	}
	public void setDisponibilitaAPagareSubImpegno(
			BigDecimal disponibilitaAPagareSubImpegno) {
		this.disponibilitaAPagareSubImpegno = disponibilitaAPagareSubImpegno;
	}
	public BigDecimal getDisponibilitaAPagareLiquidazione() {
		return disponibilitaAPagareLiquidazione;
	}
	public void setDisponibilitaAPagareLiquidazione(
			BigDecimal disponibilitaAPagareLiquidazione) {
		this.disponibilitaAPagareLiquidazione = disponibilitaAPagareLiquidazione;
	}
	public Impegno getImpegno() {
		return impegno;
	}
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}
	public Liquidazione getLiquidazione() {
		return liquidazione;
	}
	public void setLiquidazione(Liquidazione liquidazione) {
		this.liquidazione = liquidazione;
	}
	public SubOrdinativoPagamento getSubOrdinativoPagamento() {
		return subOrdinativoPagamento;
	}
	public void setSubOrdinativoPagamento(
			SubOrdinativoPagamento subOrdinativoPagamento) {
		this.subOrdinativoPagamento = subOrdinativoPagamento;
	}	
	public BigDecimal getDeltaImportoSubOrdinativo() {
		return deltaImportoSubOrdinativo;
	}
	public void setDeltaImportoSubOrdinativo(BigDecimal deltaImportoSubOrdinativo) {
		this.deltaImportoSubOrdinativo = deltaImportoSubOrdinativo;
	}	
}