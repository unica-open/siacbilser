/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public class MovGestInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -402509439372203527L;
	
	private int movGestId;
	
	private SiacTMovgestFin siacTMovgest;
	private SiacTMovgestTsFin siacTMovgestTs;
	
	private SiacTMovgestFin siacTMovgestResiduo;
	private SiacTMovgestTsFin siacTMovgestTsResiduo;
	
	private MovimentoGestione movGestCompleto;
	
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
	public SiacTMovgestFin getSiacTMovgestResiduo() {
		return siacTMovgestResiduo;
	}
	public void setSiacTMovgestResiduo(SiacTMovgestFin siacTMovgestResiduo) {
		this.siacTMovgestResiduo = siacTMovgestResiduo;
	}
	public SiacTMovgestTsFin getSiacTMovgestTsResiduo() {
		return siacTMovgestTsResiduo;
	}
	public void setSiacTMovgestTsResiduo(SiacTMovgestTsFin siacTMovgestTsResiduo) {
		this.siacTMovgestTsResiduo = siacTMovgestTsResiduo;
	}
	public MovimentoGestione getMovGestCompleto() {
		return movGestCompleto;
	}
	public void setMovGestCompleto(MovimentoGestione movGestCompleto) {
		this.movGestCompleto = movGestCompleto;
	}
	public int getMovGestId() {
		return movGestId;
	}
	public void setMovGestId(int movGestId) {
		this.movGestId = movGestId;
	}
}
