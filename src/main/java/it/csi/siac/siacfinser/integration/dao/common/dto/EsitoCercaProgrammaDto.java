/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * usato nel common service per il cercaProgramma
 * 
 * @author 
 *
 */
public class EsitoCercaProgrammaDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean esitenzaProgramma;
	private boolean flagRilevabileFPV;
	private BigDecimal valoreComplessivo;
	
	public boolean isEsitenzaProgramma() {
		return esitenzaProgramma;
	}
	public void setEsitenzaProgramma(boolean esitenzaProgramma) {
		this.esitenzaProgramma = esitenzaProgramma;
	}
	public boolean isFlagRilevabileFPV() {
		return flagRilevabileFPV;
	}
	public void setFlagRilevabileFPV(boolean flagRilevabileFPV) {
		this.flagRilevabileFPV = flagRilevabileFPV;
	}
	public BigDecimal getValoreComplessivo() {
		return valoreComplessivo;
	}
	public void setValoreComplessivo(BigDecimal valoreComplessivo) {
		this.valoreComplessivo = valoreComplessivo;
	}
}