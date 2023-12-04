/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.model.SubAccertamento;

public class SubAccertamentoPerDoppiaGestioneInfoDto extends OggettoPerDoppiaGestioneInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953900807356949373L;
	
	private SubAccertamento subAccertamento;

	public SubAccertamento getSubAccertamento() {
		return subAccertamento;
	}

	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
	}
	
}