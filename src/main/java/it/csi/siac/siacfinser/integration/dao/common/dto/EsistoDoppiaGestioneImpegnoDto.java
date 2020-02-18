/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

public class EsistoDoppiaGestioneImpegnoDto  implements Serializable{
	
	

	private static final long serialVersionUID = 1L;
	
	private SiacTMovgestFin siacTMovgestImpegnoRibaltato;
	private SiacTMovgestTsFin siacTMovgestTsImpegnoRibaltato;
	
	private boolean presenzaErrori;
	
	
	public SiacTMovgestFin getSiacTMovgestImpegnoRibaltato() {
		return siacTMovgestImpegnoRibaltato;
	}
	public void setSiacTMovgestImpegnoRibaltato(
			SiacTMovgestFin siacTMovgestImpegnoRibaltato) {
		this.siacTMovgestImpegnoRibaltato = siacTMovgestImpegnoRibaltato;
	}
	public SiacTMovgestTsFin getSiacTMovgestTsImpegnoRibaltato() {
		return siacTMovgestTsImpegnoRibaltato;
	}
	public void setSiacTMovgestTsImpegnoRibaltato(
			SiacTMovgestTsFin siacTMovgestTsImpegnoRibaltato) {
		this.siacTMovgestTsImpegnoRibaltato = siacTMovgestTsImpegnoRibaltato;
	}
	public boolean isPresenzaErrori() {
		return presenzaErrori;
	}
	public void setPresenzaErrori(boolean presenzaErrori) {
		this.presenzaErrori = presenzaErrori;
	}
	

}
