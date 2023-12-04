/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;

public class OrdinativoIncassoScritturaGenInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OrdinativoIncasso ordinativoIncasso;
	private Accertamento accertamento;
	
	public OrdinativoIncasso getOrdinativoIncasso() {
		return ordinativoIncasso;
	}
	public void setOrdinativoIncasso(OrdinativoIncasso ordinativoIncasso) {
		this.ordinativoIncasso = ordinativoIncasso;
	}
	public Accertamento getAccertamento() {
		return accertamento;
	}
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}
}
