/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

public class EsitoDoppiaGestioneSubImpegnoDto implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2551234310932912466L;
	private boolean presenzaErrori;
	private SiacTMovgestTsFin subImpegnoRibaltato;
	
	private EsistoDoppiaGestioneImpegnoDto riepilogoImpegno;

	public boolean isPresenzaErrori() {
		return presenzaErrori;
	}

	public void setPresenzaErrori(boolean presenzaErrori) {
		this.presenzaErrori = presenzaErrori;
	}

	public SiacTMovgestTsFin getSubImpegnoRibaltato() {
		return subImpegnoRibaltato;
	}

	public void setSubImpegnoRibaltato(SiacTMovgestTsFin subImpegnoRibaltato) {
		this.subImpegnoRibaltato = subImpegnoRibaltato;
	}

	public EsistoDoppiaGestioneImpegnoDto getRiepilogoImpegno() {
		return riepilogoImpegno;
	}

	public void setRiepilogoImpegno(EsistoDoppiaGestioneImpegnoDto riepilogoImpegno) {
		this.riepilogoImpegno = riepilogoImpegno;
	}
	
}
