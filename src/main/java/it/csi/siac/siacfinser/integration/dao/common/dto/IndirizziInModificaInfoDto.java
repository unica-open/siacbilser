/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;

public class IndirizziInModificaInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 868477552616390597L;
	private ArrayList<IndirizzoSoggetto> indirizziDaInserire = null;
	private ArrayList<IndirizzoSoggetto> indirizziDaModificare = null;
	private List<SiacTIndirizzoSoggettoFin> indirizziOld = null;
	private ArrayList<SiacTIndirizzoSoggettoFin> indirizziDaEliminare = null;
	private ArrayList<Integer> listaIdRicevutiDalFrontEnd=null;
	
	private boolean rimastiUguali=false;
	
	public ArrayList<IndirizzoSoggetto> getIndirizziDaInserire() {
		return indirizziDaInserire;
	}
	public void setIndirizziDaInserire(
			ArrayList<IndirizzoSoggetto> indirizziDaInserire) {
		this.indirizziDaInserire = indirizziDaInserire;
	}
	public ArrayList<IndirizzoSoggetto> getIndirizziDaModificare() {
		return indirizziDaModificare;
	}
	public void setIndirizziDaModificare(
			ArrayList<IndirizzoSoggetto> indirizziDaModificare) {
		this.indirizziDaModificare = indirizziDaModificare;
	}
	public List<SiacTIndirizzoSoggettoFin> getIndirizziOld() {
		return indirizziOld;
	}
	public void setIndirizziOld(List<SiacTIndirizzoSoggettoFin> indirizziOld) {
		this.indirizziOld = indirizziOld;
	}
	public ArrayList<SiacTIndirizzoSoggettoFin> getIndirizziDaEliminare() {
		return indirizziDaEliminare;
	}
	public void setIndirizziDaEliminare(
			ArrayList<SiacTIndirizzoSoggettoFin> indirizziDaEliminare) {
		this.indirizziDaEliminare = indirizziDaEliminare;
	}
	public boolean isRimastiUguali() {
		return rimastiUguali;
	}
	public void setRimastiUguali(boolean rimastiUguali) {
		this.rimastiUguali = rimastiUguali;
	}
	public ArrayList<Integer> getListaIdRicevutiDalFrontEnd() {
		return listaIdRicevutiDalFrontEnd;
	}
	public void setListaIdRicevutiDalFrontEnd(
			ArrayList<Integer> listaIdRicevutiDalFrontEnd) {
		this.listaIdRicevutiDalFrontEnd = listaIdRicevutiDalFrontEnd;
	}
	
	
}
