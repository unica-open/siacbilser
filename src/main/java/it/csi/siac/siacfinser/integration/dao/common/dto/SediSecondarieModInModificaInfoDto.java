/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazModFin;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class SediSecondarieModInModificaInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2989179001265477978L;
	ArrayList<SedeSecondariaSoggetto> listaDaAggiornare = new ArrayList<SedeSecondariaSoggetto>();
	ArrayList<SedeSecondariaSoggetto> listaDaInserire = new ArrayList<SedeSecondariaSoggetto>();
	ArrayList<SiacRSoggettoRelazModFin> listaDaEliminare = new ArrayList<SiacRSoggettoRelazModFin>();
	
	public ArrayList<SedeSecondariaSoggetto> getListaDaAggiornare() {
		return listaDaAggiornare;
	}
	public void setListaDaAggiornare(
			ArrayList<SedeSecondariaSoggetto> listaDaAggiornare) {
		this.listaDaAggiornare = listaDaAggiornare;
	}
	public ArrayList<SedeSecondariaSoggetto> getListaDaInserire() {
		return listaDaInserire;
	}
	public void setListaDaInserire(ArrayList<SedeSecondariaSoggetto> listaDaInserire) {
		this.listaDaInserire = listaDaInserire;
	}
	public ArrayList<SiacRSoggettoRelazModFin> getListaDaEliminare() {
		return listaDaEliminare;
	}
	public void setListaDaEliminare(ArrayList<SiacRSoggettoRelazModFin> listaDaEliminare) {
		this.listaDaEliminare = listaDaEliminare;
	}
	
	
}
