/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

public class ModalitaPagamentoInModificaInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1599947774443408283L;
	private ArrayList<ModalitaPagamentoSoggetto> modalitaDaInserire = null;
	private ArrayList<ModalitaPagamentoSoggetto> modalitaDaModificare = null;
	private List<SiacTModpagFin> modalitaOld = null;
	private ArrayList<SiacTModpagFin> modalitaDaEliminare = null;
	private ArrayList<Integer> listaIdModificati=null;
	private ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoCessioneDaEliminare = null;
	private ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoInModifica = null;
	private boolean rimasteUguali=false;
	
	
	//GENERATED GETTER & SETTER
	
	public ArrayList<ModalitaPagamentoSoggetto> getModalitaDaInserire() {
		return modalitaDaInserire;
	}
	public void setModalitaDaInserire(
			ArrayList<ModalitaPagamentoSoggetto> modalitaDaInserire) {
		this.modalitaDaInserire = modalitaDaInserire;
	}
	public ArrayList<ModalitaPagamentoSoggetto> getModalitaDaModificare() {
		return modalitaDaModificare;
	}
	public void setModalitaDaModificare(
			ArrayList<ModalitaPagamentoSoggetto> modalitaDaModificare) {
		this.modalitaDaModificare = modalitaDaModificare;
	}
	public List<SiacTModpagFin> getModalitaOld() {
		return modalitaOld;
	}
	public void setModalitaOld(List<SiacTModpagFin> modalitaOld) {
		this.modalitaOld = modalitaOld;
	}
	public ArrayList<SiacTModpagFin> getModalitaDaEliminare() {
		return modalitaDaEliminare;
	}
	public void setModalitaDaEliminare(ArrayList<SiacTModpagFin> modalitaDaEliminare) {
		this.modalitaDaEliminare = modalitaDaEliminare;
	}
	public ArrayList<Integer> getListaIdModificati() {
		return listaIdModificati;
	}
	public void setListaIdModificati(ArrayList<Integer> listaIdModificati) {
		this.listaIdModificati = listaIdModificati;
	}
	public ArrayList<ModalitaPagamentoSoggetto> getModalitaPagamentoSoggettoCessioneDaEliminare() {
		return modalitaPagamentoSoggettoCessioneDaEliminare;
	}
	public void setModalitaPagamentoSoggettoCessioneDaEliminare(
			ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoCessioneDaEliminare) {
		this.modalitaPagamentoSoggettoCessioneDaEliminare = modalitaPagamentoSoggettoCessioneDaEliminare;
	}
	public boolean isRimasteUguali() {
		return rimasteUguali;
	}
	public void setRimasteUguali(boolean rimasteUguali) {
		this.rimasteUguali = rimasteUguali;
	}
	public ArrayList<ModalitaPagamentoSoggetto> getModalitaPagamentoSoggettoInModifica() {
		return modalitaPagamentoSoggettoInModifica;
	}
	public void setModalitaPagamentoSoggettoInModifica(
			ArrayList<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoInModifica) {
		this.modalitaPagamentoSoggettoInModifica = modalitaPagamentoSoggettoInModifica;
	}
	
}
