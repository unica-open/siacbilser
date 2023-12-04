/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class OggettoPerDoppiaGestioneInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953900807356949373L;
	
	private BigDecimal deltaImportoRibaltamento;
	
	public OggettoPerDoppiaGestioneInfoDto(){
		this.deltaImportoRibaltamento = null;
	}

	public BigDecimal getDeltaImportoRibaltamento() {
		return deltaImportoRibaltamento;
	}
	
	public void addDelta(BigDecimal delta){
		if(this.deltaImportoRibaltamento == null){
			this.deltaImportoRibaltamento = new BigDecimal(delta.toString());
		} else {
			this.deltaImportoRibaltamento.add(delta);
		}
	}
	
}