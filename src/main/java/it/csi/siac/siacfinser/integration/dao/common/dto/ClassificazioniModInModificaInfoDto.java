/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseModFin;

public class ClassificazioniModInModificaInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8268972416067069208L;
	private boolean rimasteUguali=false;
	private ArrayList<SiacRSoggettoClasseModFin> classificazioniEliminate = new ArrayList<SiacRSoggettoClasseModFin>();
	private ArrayList<String> nuoveClassi = new ArrayList<String>();
	
	public ArrayList<SiacRSoggettoClasseModFin> getClassificazioniEliminate() {
		return classificazioniEliminate;
	}
	public void setClassificazioniEliminate(
			ArrayList<SiacRSoggettoClasseModFin> classificazioniEliminate) {
		this.classificazioniEliminate = classificazioniEliminate;
	}
	public boolean isRimasteUguali() {
		return rimasteUguali;
	}
	public void setRimasteUguali(boolean rimasteUguali) {
		this.rimasteUguali = rimasteUguali;
	}
	public ArrayList<String> getNuoveClassi() {
		return nuoveClassi;
	}
	public void setNuoveClassi(ArrayList<String> nuoveClassi) {
		this.nuoveClassi = nuoveClassi;
	}
	
}
