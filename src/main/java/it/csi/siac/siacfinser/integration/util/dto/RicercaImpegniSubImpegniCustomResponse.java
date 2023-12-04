/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

public class RicercaImpegniSubImpegniCustomResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Impegno> listaImpegnos = new ArrayList<Impegno>();
	List<SubImpegno> listaSubImpegnos = new ArrayList<SubImpegno>();
	
	private int numRisultati;
	private int numPagina;

	public List<Impegno> getListaImpegnos() {
		return listaImpegnos;
	}
	public void setListaImpegnos(List<Impegno> listaImpegnos) {
		this.listaImpegnos = listaImpegnos;
	}
	public List<SubImpegno> getListaSubImpegnos() {
		return listaSubImpegnos;
	}
	public void setListaSubImpegnos(List<SubImpegno> listaSubImpegnos) {
		this.listaSubImpegnos = listaSubImpegnos;
	}
	public int getNumRisultati() {
		return numRisultati;
	}
	public void setNumRisultati(int numRisultati) {
		this.numRisultati = numRisultati;
	}
	public int getNumPagina() {
		return numPagina;
	}
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

}
