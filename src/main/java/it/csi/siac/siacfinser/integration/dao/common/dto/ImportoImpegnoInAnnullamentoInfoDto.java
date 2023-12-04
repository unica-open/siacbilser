/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;

public class ImportoImpegnoInAnnullamentoInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	
	private BigDecimal totaleModifiche;
	private SiacTMovgestTsDetFin siacTMovgestTsDet;
	
	public BigDecimal getTotaleModifiche() {
		return totaleModifiche;
	}
	public void setTotaleModifiche(BigDecimal totaleModifiche) {
		this.totaleModifiche = totaleModifiche;
	}
	public SiacTMovgestTsDetFin getSiacTMovgestTsDet() {
		return siacTMovgestTsDet;
	}
	public void setSiacTMovgestTsDet(SiacTMovgestTsDetFin siacTMovgestTsDet) {
		this.siacTMovgestTsDet = siacTMovgestTsDet;
	}
	
	
}
