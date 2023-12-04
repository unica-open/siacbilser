/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;

public class EsitoRicercaMovimentoPkDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int numeroTotaleSubMovimenti;
	private boolean restituitiSubMovimentiPaginati;
	private int numeroPaginaSubMovimentiRestituita;
	private int dimensionePaginaSubMovimenti;
	private int numeroTotalePagineSubMovimenti;
	
	private boolean movimentoConLiquidazioni = false; // vale per l'impegno
	private boolean movimentoConOrdinativi = false; // vale per l'accertamento
	
	
	
	
	/**
	 * @return the movimentoConOrdinativi
	 */
	public boolean isMovimentoConOrdinativi() {
		return movimentoConOrdinativi;
	}

	/**
	 * @param movimentoConOrdinativi the movimentoConOrdinativi to set
	 */
	public void setMovimentoConOrdinativi(boolean movimentoConOrdinativi) {
		this.movimentoConOrdinativi = movimentoConOrdinativi;
	}

	/**
	 * @return the movimentoConLiquidazioni
	 */
	public boolean isMovimentoConLiquidazioni() {
		return movimentoConLiquidazioni;
	}

	/**
	 * @param movimentoConLiquidazioni the movimentoConLiquidazioni to set
	 */
	public void setMovimentoConLiquidazioni(boolean movimentoConLiquidazioni) {
		this.movimentoConLiquidazioni = movimentoConLiquidazioni;
	}

	private List<SubImpegno> elencoSubImpegniTuttiConSoloGliIds;
	private List<SubAccertamento> elencoSubAccertamentiTuttiConSoloGliIds;
	
	private MovimentoGestione movimentoGestione;

	public int getNumeroTotaleSubMovimenti() {
		return numeroTotaleSubMovimenti;
	}

	public void setNumeroTotaleSubMovimenti(int numeroTotaleSubMovimenti) {
		this.numeroTotaleSubMovimenti = numeroTotaleSubMovimenti;
	}

	public boolean isRestituitiSubMovimentiPaginati() {
		return restituitiSubMovimentiPaginati;
	}

	public void setRestituitiSubMovimentiPaginati(
			boolean restituitiSubMovimentiPaginati) {
		this.restituitiSubMovimentiPaginati = restituitiSubMovimentiPaginati;
	}

	public int getNumeroPaginaSubMovimentiRestituita() {
		return numeroPaginaSubMovimentiRestituita;
	}

	public void setNumeroPaginaSubMovimentiRestituita(
			int numeroPaginaSubMovimentiRestituita) {
		this.numeroPaginaSubMovimentiRestituita = numeroPaginaSubMovimentiRestituita;
	}

	public int getDimensionePaginaSubMovimenti() {
		return dimensionePaginaSubMovimenti;
	}

	public void setDimensionePaginaSubMovimenti(int dimensionePaginaSubMovimenti) {
		this.dimensionePaginaSubMovimenti = dimensionePaginaSubMovimenti;
	}

	public MovimentoGestione getMovimentoGestione() {
		return movimentoGestione;
	}

	public void setMovimentoGestione(MovimentoGestione movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	public int getNumeroTotalePagineSubMovimenti() {
		return numeroTotalePagineSubMovimenti;
	}

	public void setNumeroTotalePagineSubMovimenti(int numeroTotalePagineSubMovimenti) {
		this.numeroTotalePagineSubMovimenti = numeroTotalePagineSubMovimenti;
	}

	public List<SubImpegno> getElencoSubImpegniTuttiConSoloGliIds() {
		return elencoSubImpegniTuttiConSoloGliIds;
	}

	public void setElencoSubImpegniTuttiConSoloGliIds(
			List<SubImpegno> elencoSubImpegniTuttiConSoloGliIds) {
		this.elencoSubImpegniTuttiConSoloGliIds = elencoSubImpegniTuttiConSoloGliIds;
	}

	public List<SubAccertamento> getElencoSubAccertamentiTuttiConSoloGliIds() {
		return elencoSubAccertamentiTuttiConSoloGliIds;
	}

	public void setElencoSubAccertamentiTuttiConSoloGliIds(
			List<SubAccertamento> elencoSubAccertamentiTuttiConSoloGliIds) {
		this.elencoSubAccertamentiTuttiConSoloGliIds = elencoSubAccertamentiTuttiConSoloGliIds;
	}
	
}
