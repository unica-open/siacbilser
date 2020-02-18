/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

public class RicercaAccertamentiSubAccertamentiCustomResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Accertamento> listaAccertamentos = new ArrayList<Accertamento>();
	List<SubAccertamento> listaSubAccertamentos = new ArrayList<SubAccertamento>();
	
	private int numRisultati;
	private int numPagina;

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
	public List<Accertamento> getListaAccertamentos() {
		return listaAccertamentos;
	}
	public void setListaAccertamentos(List<Accertamento> listaAccertamentos) {
		this.listaAccertamentos = listaAccertamentos;
	}
	public List<SubAccertamento> getListaSubAccertamentos() {
		return listaSubAccertamentos;
	}
	public void setListaSubAccertamentos(List<SubAccertamento> listaSubAccertamentos) {
		this.listaSubAccertamentos = listaSubAccertamentos;
	}
}
