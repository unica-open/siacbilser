/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;

public class ClassificazioniInModificaInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4931108753290880292L;

	private boolean rimasteUguali=false;
	
	private ArrayList<SiacRSoggettoClasseFin> classificazioniEliminate = new ArrayList<SiacRSoggettoClasseFin>();
	private ArrayList<String> nuoveClassi = new ArrayList<String>();
	
	public ArrayList<SiacRSoggettoClasseFin> getClassificazioniEliminate() {
		return classificazioniEliminate;
	}
	public void setClassificazioniEliminate(
			ArrayList<SiacRSoggettoClasseFin> classificazioniEliminate) {
		this.classificazioniEliminate = classificazioniEliminate;
	}
	public ArrayList<String> getNuoveClassi() {
		return nuoveClassi;
	}
	public void setNuoveClassi(ArrayList<String> nuoveClassi) {
		this.nuoveClassi = nuoveClassi;
	}
	public boolean isRimasteUguali() {
		return rimasteUguali;
	}
	public void setRimasteUguali(boolean rimasteUguali) {
		this.rimasteUguali = rimasteUguali;
	}
	
}
