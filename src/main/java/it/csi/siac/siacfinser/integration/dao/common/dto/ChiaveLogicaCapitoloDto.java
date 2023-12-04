/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

public class ChiaveLogicaCapitoloDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer annoCapitolo;
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	private Integer numeroUeb;
	
	
	public Integer getAnnoCapitolo() {
		return annoCapitolo;
	}
	public void setAnnoCapitolo(Integer annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}
	public Integer getNumeroCapitolo() {
		return numeroCapitolo;
	}
	public void setNumeroCapitolo(Integer numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}
	public Integer getNumeroArticolo() {
		return numeroArticolo;
	}
	public void setNumeroArticolo(Integer numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}
	public Integer getNumeroUeb() {
		return numeroUeb;
	}
	public void setNumeroUeb(Integer numeroUeb) {
		this.numeroUeb = numeroUeb;
	}
	
	
}
