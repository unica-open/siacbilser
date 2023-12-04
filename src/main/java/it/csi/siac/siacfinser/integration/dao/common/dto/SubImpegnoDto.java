/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;

public class SubImpegnoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4197432130623595949L;
	
	private int numermoSubImpegno;
	private SiacTMovgestTsFin siacTMovgestTs;
	private SubImpegno subImpegno;
	private SubAccertamento subAccertamento;
	
	public SiacTMovgestTsFin getSiacTMovgestTs() {
		return siacTMovgestTs;
	}
	public void setSiacTMovgestTs(SiacTMovgestTsFin siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}
	public int getNumermoSubImpegno() {
		return numermoSubImpegno;
	}
	public void setNumermoSubImpegno(int numermoSubImpegno) {
		this.numermoSubImpegno = numermoSubImpegno;
	}
	public SubAccertamento getSubAccertamento() {
		return subAccertamento;
	}
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
	}

}
