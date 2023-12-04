/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacfinser.model.Accertamento;

public class SubAccertamentiModAutomaticaPerReintroitoInfoDto extends AccertamentoPerReintroitoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<AccertamentoModAutomaticaPerReintroitoInfoDto> modificheSub;
	
	private Accertamento accertamento;
	private BigDecimal modificaAccertamento;
	
	public List<AccertamentoModAutomaticaPerReintroitoInfoDto> getModificheSub() {
		return modificheSub;
	}
	public void setModificheSub(List<AccertamentoModAutomaticaPerReintroitoInfoDto> modificheSub) {
		this.modificheSub = modificheSub;
	}
	public Accertamento getAccertamento() {
		return accertamento;
	}
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}
	public BigDecimal getModificaAccertamento() {
		return modificaAccertamento;
	}
	public void setModificaAccertamento(BigDecimal modificaAccertamento) {
		this.modificaAccertamento = modificaAccertamento;
	}
	
}
