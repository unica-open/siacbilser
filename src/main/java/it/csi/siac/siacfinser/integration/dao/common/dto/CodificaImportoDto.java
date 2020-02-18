/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CodificaImportoDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;

	
	private BigDecimal importo;
	private Integer idOggetto;
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Integer getIdOggetto() {
		return idOggetto;
	}
	public void setIdOggetto(Integer idOggetto) {
		this.idOggetto = idOggetto;
	}
	
}
