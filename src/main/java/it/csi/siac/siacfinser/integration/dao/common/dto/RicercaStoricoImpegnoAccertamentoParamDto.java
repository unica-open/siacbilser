/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siaccorser.model.Bilancio;

/**
 * @author elisa
 * @version 1.0.0 - 03-07-2019
 *
 */
public class RicercaStoricoImpegnoAccertamentoParamDto implements Serializable{

	/**Serializzazione	 */
	private static final long serialVersionUID = 7673938695648007920L;

	private Integer annoImpegno;
	private BigDecimal numeroImpegno;
	private BigDecimal numeroSubImpegno;
	

	private Integer annoAccertamento;
	private BigDecimal numeroAccertamento;
	private BigDecimal numeroSubAccertamento;
	
	private Bilancio bilancio;
	
	private Boolean escludiSubImpegni;
	
	/**
	 * @return the annoImpegno
	 */
	public Integer getAnnoImpegno() {
		return annoImpegno;
	}
	/**
	 * @return the numeroImpegno
	 */
	public BigDecimal getNumeroImpegno() {
		return numeroImpegno;
	}
	/**
	 * @return the numeroSubImpegno
	 */
	public BigDecimal getNumeroSubImpegno() {
		return numeroSubImpegno;
	}
	/**
	 * @return the annoAccertamento
	 */
	public Integer getAnnoAccertamento() {
		return annoAccertamento;
	}
	/**
	 * @return the numeroAccertamento
	 */
	public BigDecimal getNumeroAccertamento() {
		return numeroAccertamento;
	}
	/**
	 * @return the numeroSubAccertamento
	 */
	public BigDecimal getNumeroSubAccertamento() {
		return numeroSubAccertamento;
	}
	/**
	 * @param annoImpegno the annoImpegno to set
	 */
	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}
	/**
	 * @param numeroImpegno the numeroImpegno to set
	 */
	public void setNumeroImpegno(BigDecimal numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
	/**
	 * @param numeroSubImpegno the numeroSubImpegno to set
	 */
	public void setNumeroSubImpegno(BigDecimal numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
	}
	/**
	 * @param annoAccertamento the annoAccertamento to set
	 */
	public void setAnnoAccertamento(Integer annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	/**
	 * @param numeroAccertamento the numeroAccertamento to set
	 */
	public void setNumeroAccertamento(BigDecimal numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	/**
	 * @param numeroSubAccertamento the numeroSubAccertamento to set
	 */
	public void setNumeroSubAccertamento(BigDecimal numeroSubAccertamento) {
		this.numeroSubAccertamento = numeroSubAccertamento;
	}
	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}
	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}
	
	/**
	 * @return the escludiSubImpegni
	 */
	public Boolean getEscludiSubImpegni() {
		return escludiSubImpegni;
	}
	/**
	 * @param escludiSubImpegni the escludiSubImpegni to set
	 */
	public void setEscludiSubImpegni(Boolean escludiSubImpegni) {
		this.escludiSubImpegni = escludiSubImpegni;
	}
	
	
}
