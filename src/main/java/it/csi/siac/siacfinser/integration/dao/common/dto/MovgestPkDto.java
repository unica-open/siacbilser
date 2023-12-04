/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MovgestPkDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2686617489718026468L;
	
	private String annoEsercizio;
	private Integer annoMovimento;
	private BigDecimal numeroMovimento;
	private String tipoTMovGest;
	
	public MovgestPkDto(String annoEsercizio,Integer annoMovimento,BigDecimal numeroMovimento,String tipoTMovGest){
		setAnnoEsercizio(annoEsercizio);
		setAnnoMovimento(annoMovimento);
		setNumeroMovimento(numeroMovimento);
		setTipoTMovGest(tipoTMovGest);
	}
	
	public String getAnnoEsercizio() {
		return annoEsercizio;
	}
	public void setAnnoEsercizio(String annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}
	public BigDecimal getNumeroMovimento() {
		return numeroMovimento;
	}
	public void setNumeroMovimento(BigDecimal numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}
	public String getTipoTMovGest() {
		return tipoTMovGest;
	}
	public void setTipoTMovGest(String tipoTMovGest) {
		this.tipoTMovGest = tipoTMovGest;
	}

}
