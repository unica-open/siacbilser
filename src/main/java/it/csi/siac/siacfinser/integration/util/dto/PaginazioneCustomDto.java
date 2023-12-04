/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siaccorser.model.Entita;

public class PaginazioneCustomDto <T extends Entita>  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int paginaCorrente;
	private int totaleElementi;
	private int totalePagine;
	
	private List<T> paginaRichiesta;
	
	public int getPaginaCorrente() {
		return paginaCorrente;
	}
	public void setPaginaCorrente(int paginaCorrente) {
		this.paginaCorrente = paginaCorrente;
	}
	public int getTotaleElementi() {
		return totaleElementi;
	}
	public void setTotaleElementi(int totaleElementi) {
		this.totaleElementi = totaleElementi;
	}
	public int getTotalePagine() {
		return totalePagine;
	}
	public void setTotalePagine(int totalePagine) {
		this.totalePagine = totalePagine;
	}
	public List<T> getPaginaRichiesta() {
		return paginaRichiesta;
	}
	public void setPaginaRichiesta(List<T> paginaRichiesta) {
		this.paginaRichiesta = paginaRichiesta;
	}
	
}
