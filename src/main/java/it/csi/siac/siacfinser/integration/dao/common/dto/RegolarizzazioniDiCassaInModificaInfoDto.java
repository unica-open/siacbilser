/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoProvCassaFin;
import it.csi.siac.siacfinser.model.ordinativo.RegolarizzazioneProvvisorio;

public class RegolarizzazioniDiCassaInModificaInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7325793529784011291L;
	
	private ArrayList<RegolarizzazioneProvvisorio> regolarizzazioniDiCassaDaInserire = null;
	private ArrayList<RegolarizzazioneProvvisorio> regolarizzazioniDiCassaDaModificare = null;
	private ArrayList<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaDaEliminare = null;
	private ArrayList<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaInvariate = null;
	
	private List<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaOld = null;
	private ArrayList<Integer> listaIdModificati=null;

	public ArrayList<Integer> getListaIdModificati() {
		return listaIdModificati;
	}
	public void setListaIdModificati(ArrayList<Integer> listaIdModificati) {
		this.listaIdModificati = listaIdModificati;
	}
	public ArrayList<RegolarizzazioneProvvisorio> getRegolarizzazioniDiCassaDaInserire() {
		return regolarizzazioniDiCassaDaInserire;
	}
	public void setRegolarizzazioniDiCassaDaInserire(
			ArrayList<RegolarizzazioneProvvisorio> regolarizzazioniDiCassaDaInserire) {
		this.regolarizzazioniDiCassaDaInserire = regolarizzazioniDiCassaDaInserire;
	}
	public ArrayList<RegolarizzazioneProvvisorio> getRegolarizzazioniDiCassaDaModificare() {
		return regolarizzazioniDiCassaDaModificare;
	}
	public void setRegolarizzazioniDiCassaDaModificare(
			ArrayList<RegolarizzazioneProvvisorio> regolarizzazioniDiCassaDaModificare) {
		this.regolarizzazioniDiCassaDaModificare = regolarizzazioniDiCassaDaModificare;
	}
	public ArrayList<SiacROrdinativoProvCassaFin> getRegolarizzazioniDiCassaDaEliminare() {
		return regolarizzazioniDiCassaDaEliminare;
	}
	public void setRegolarizzazioniDiCassaDaEliminare(
			ArrayList<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaDaEliminare) {
		this.regolarizzazioniDiCassaDaEliminare = regolarizzazioniDiCassaDaEliminare;
	}
	public List<SiacROrdinativoProvCassaFin> getRegolarizzazioniDiCassaOld() {
		return regolarizzazioniDiCassaOld;
	}
	public void setRegolarizzazioniDiCassaOld(
			List<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaOld) {
		this.regolarizzazioniDiCassaOld = regolarizzazioniDiCassaOld;
	}
	public ArrayList<SiacROrdinativoProvCassaFin> getRegolarizzazioniDiCassaInvariate() {
		return regolarizzazioniDiCassaInvariate;
	}
	public void setRegolarizzazioniDiCassaInvariate(
			ArrayList<SiacROrdinativoProvCassaFin> regolarizzazioniDiCassaInvariate) {
		this.regolarizzazioniDiCassaInvariate = regolarizzazioniDiCassaInvariate;
	}
	
}
