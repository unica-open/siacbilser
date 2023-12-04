/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccertamentoModAutomaticaPerReintroitoInfoDto extends AccertamentoPerReintroitoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public AccertamentoModAutomaticaPerReintroitoInfoDto(AccertamentoPerReintroitoInfoDto accInfo, BigDecimal modifica){
		setEsitoRicercaMovPkDto(accInfo.getEsitoRicercaMovPkDto());
		setAccertamento(accInfo.getAccertamento());
		setSubAccertamento(accInfo.getSubAccertamento());
		setKey(accInfo.getKey());
		setModificaDaApportare(modifica);
		setMovGestInfoAccertamento(accInfo.getMovGestInfoAccertamento());
		setMovGestInfoSubAccertamento(accInfo.getMovGestInfoSubAccertamento());
	}
	
	private BigDecimal modificaDaApportare;

	public BigDecimal getModificaDaApportare() {
		return modificaDaApportare;
	}

	public void setModificaDaApportare(BigDecimal modificaDaApportare) {
		this.modificaDaApportare = modificaDaApportare;
	}
	
	
}
