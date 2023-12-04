/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaginazioneSubMovimentiDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal numeroSubMovimentoRichiesto;
	
	private boolean paginazione=false;
	private int numeroPagina;
	private int dimensionePagina;
	
	private boolean paginazioneSuDatiMinimi=false;
	
	//ESCLUDE IL CARICAMENTO DEI SUB ANNULLATI:
	private boolean escludiSubAnnullati=false; 
	
	//ESCLUDE DEL TUTTO IL CARICAMENTO DEI SUB:
	private boolean noSub=false;
	
	//CARICO I SOLI SUB NELLO STATO RICHIESTO:
	private String filtroSubSoloInQuestoStato;
	
	public BigDecimal getNumeroSubMovimentoRichiesto() {
		return numeroSubMovimentoRichiesto;
	}
	public void setNumeroSubMovimentoRichiesto(
			BigDecimal numeroSubMovimentoRichiesto) {
		this.numeroSubMovimentoRichiesto = numeroSubMovimentoRichiesto;
	}
	public boolean isPaginazione() {
		return paginazione;
	}
	public void setPaginazione(boolean paginazione) {
		this.paginazione = paginazione;
	}
	public int getNumeroPagina() {
		return numeroPagina;
	}
	public void setNumeroPagina(int numeroPagina) {
		this.numeroPagina = numeroPagina;
	}
	public int getDimensionePagina() {
		return dimensionePagina;
	}
	public void setDimensionePagina(int dimensionePagina) {
		this.dimensionePagina = dimensionePagina;
	}
	public boolean isNoSub() {
		return noSub;
	}
	public void setNoSub(boolean noSub) {
		this.noSub = noSub;
	}
	public boolean isEscludiSubAnnullati() {
		return escludiSubAnnullati;
	}
	public void setEscludiSubAnnullati(boolean escludiSubAnnullati) {
		this.escludiSubAnnullati = escludiSubAnnullati;
	}
	public boolean isPaginazioneSuDatiMinimi() {
		return paginazioneSuDatiMinimi;
	}
	public void setPaginazioneSuDatiMinimi(boolean paginazioneSuDatiMinimi) {
		this.paginazioneSuDatiMinimi = paginazioneSuDatiMinimi;
	}
	public String getFiltroSubSoloInQuestoStato() {
		return filtroSubSoloInQuestoStato;
	}
	public void setFiltroSubSoloInQuestoStato(String filtroSubSoloInQuestoStato) {
		this.filtroSubSoloInQuestoStato = filtroSubSoloInQuestoStato;
	}
	 
}
