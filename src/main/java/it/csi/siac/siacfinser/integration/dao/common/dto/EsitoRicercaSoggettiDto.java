/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class EsitoRicercaSoggettiDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int numeroTotaleSoggetti;
	private int numeroPaginaRestituita;
	private int dimensionePagine;
	private int numeroTotalePagine;
	
	private List<Soggetto> paginaRichiesta;

	public int getNumeroTotaleSoggetti() {
		return numeroTotaleSoggetti;
	}

	public void setNumeroTotaleSoggetti(int numeroTotaleSoggetti) {
		this.numeroTotaleSoggetti = numeroTotaleSoggetti;
	}

	public int getNumeroPaginaRestituita() {
		return numeroPaginaRestituita;
	}

	public void setNumeroPaginaRestituita(int numeroPaginaRestituita) {
		this.numeroPaginaRestituita = numeroPaginaRestituita;
	}

	public int getDimensionePagine() {
		return dimensionePagine;
	}

	public void setDimensionePagine(int dimensionePagine) {
		this.dimensionePagine = dimensionePagine;
	}

	public int getNumeroTotalePagine() {
		return numeroTotalePagine;
	}

	public void setNumeroTotalePagine(int numeroTotalePagine) {
		this.numeroTotalePagine = numeroTotalePagine;
	}

	public List<Soggetto> getPaginaRichiesta() {
		return paginaRichiesta;
	}

	public void setPaginaRichiesta(List<Soggetto> paginaRichiesta) {
		this.paginaRichiesta = paginaRichiesta;
	}
	
	
}
