/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;

public class SubOrdinativoImportoVariatoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	private SubOrdinativo subOrdinativo = null;
	private BigDecimal delta = null;
	
	public SubOrdinativoImportoVariatoDto(SubOrdinativo subOrdinativo, BigDecimal delta){
		this.subOrdinativo = subOrdinativo;
		this.delta = delta;
	}
	
	public SubOrdinativo getSubOrdinativo() {
		return subOrdinativo;
	}
	public void setSubOrdinativo(SubOrdinativo subOrdinativo) {
		this.subOrdinativo = subOrdinativo;
	}
	public BigDecimal getDelta() {
		return delta;
	}
	public void setDelta(BigDecimal delta) {
		this.delta = delta;
	}
	
}
