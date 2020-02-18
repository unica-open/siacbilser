/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

public class AccertamentoDaVincolareInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	
	private SiacTMovgestFin siacTMovgest;
	private SiacTMovgestTsFin siacTMovgestTs;
	
	private Integer chiaveCapitolo = null;
	private Integer chiaveCapitoloResiduo = null;
	
	public SiacTMovgestFin getSiacTMovgest() {
		return siacTMovgest;
	}
	public void setSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		this.siacTMovgest = siacTMovgest;
	}
	public SiacTMovgestTsFin getSiacTMovgestTs() {
		return siacTMovgestTs;
	}
	public void setSiacTMovgestTs(SiacTMovgestTsFin siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}
	public Integer getChiaveCapitolo() {
		return chiaveCapitolo;
	}
	public void setChiaveCapitolo(Integer chiaveCapitolo) {
		this.chiaveCapitolo = chiaveCapitolo;
	}
	public Integer getChiaveCapitoloResiduo() {
		return chiaveCapitoloResiduo;
	}
	public void setChiaveCapitoloResiduo(Integer chiaveCapitoloResiduo) {
		this.chiaveCapitoloResiduo = chiaveCapitoloResiduo;
	}
	
}
