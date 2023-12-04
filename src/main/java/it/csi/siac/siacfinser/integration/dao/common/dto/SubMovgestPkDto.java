/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubMovgestPkDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2686617489718026468L;
	
	private MovgestPkDto chiaveMovimento;
	private BigDecimal numeroSubMovimento;
	
	public SubMovgestPkDto(MovgestPkDto chiaveMovimento, BigDecimal numeroSubMovimento){
		setChiaveMovimento(chiaveMovimento);
		setNumeroSubMovimento(numeroSubMovimento);
	}

	public MovgestPkDto getChiaveMovimento() {
		return chiaveMovimento;
	}

	public void setChiaveMovimento(MovgestPkDto chiaveMovimento) {
		this.chiaveMovimento = chiaveMovimento;
	}

	public BigDecimal getNumeroSubMovimento() {
		return numeroSubMovimento;
	}

	public void setNumeroSubMovimento(BigDecimal numeroSubMovimento) {
		this.numeroSubMovimento = numeroSubMovimento;
	}

}
