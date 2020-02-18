/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereFin;

public class OneriInModificaInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1638719588043711656L;
	private ArrayList<SiacRSoggettoOnereFin> oneriEliminati = new ArrayList<SiacRSoggettoOnereFin>();
	private ArrayList<String> nuoviOneri = new ArrayList<String>();
	private boolean rimastiUguali=false;
	
	public ArrayList<SiacRSoggettoOnereFin> getOneriEliminati() {
		return oneriEliminati;
	}
	public void setOneriEliminati(ArrayList<SiacRSoggettoOnereFin> oneriEliminati) {
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
