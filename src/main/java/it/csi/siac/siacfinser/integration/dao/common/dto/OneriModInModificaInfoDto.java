/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereModFin;

public class OneriModInModificaInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8197708488282868832L;
	private boolean rimastiUguali=false;
	private ArrayList<SiacRSoggettoOnereModFin> oneriEliminati = new ArrayList<SiacRSoggettoOnereModFin>();
	private ArrayList<String> nuoviOneri = new ArrayList<String>();
	
	public ArrayList<SiacRSoggettoOnereModFin> getOneriEliminati() {
		return oneriEliminati;
	}
	public void setOneriEliminati(ArrayList<SiacRSoggettoOnereModFin> oneriEliminati) {
		this.oneriEliminati = oneriEliminati;
	}
	public ArrayList<String> getNuoviOneri() {
		return nuoviOneri;
	}
	public void setNuoviOneri(ArrayList<String> nuoviOneri) {
		this.nuoviOneri = nuoviOneri;
	}
	public boolean isRimastiUguali() {
		return rimastiUguali;
	}
	public void setRimastiUguali(boolean rimastiUguali) {
		this.rimastiUguali = rimastiUguali;
	}
	
}
