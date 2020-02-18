/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;

public class OrdinativoInInserimentoInfoDto extends AbstractOrdinativoInEditingInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1243834148389832754L;
	
	private Bilancio bilancio;
	private Ordinativo ordinativo;
	
	public Bilancio getBilancio() {
		return bilancio;
	}
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}
	public Ordinativo getOrdinativo() {
		return ordinativo;
	}
	public void setOrdinativo(Ordinativo ordinativo) {
		this.ordinativo = ordinativo;
	}
}
