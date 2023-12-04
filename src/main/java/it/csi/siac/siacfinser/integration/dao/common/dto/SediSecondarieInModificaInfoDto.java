/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class SediSecondarieInModificaInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2766314308775457947L;
	ArrayList<SedeSecondariaSoggetto> listaDaAggiornare = new ArrayList<SedeSecondariaSoggetto>();
	ArrayList<SedeSecondariaSoggetto> listaDaInserire = new ArrayList<SedeSecondariaSoggetto>();
	ArrayList<SiacRSoggettoRelazFin> listaDaEliminare = new ArrayList<SiacRSoggettoRelazFin>();
	ArrayList<SiacRSoggettoRelazFin> listaInvariati = new ArrayList<SiacRSoggettoRelazFin>();
	
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
	public ArrayList<SiacRSoggettoRelazFin> getListaDaEliminare() {
		return listaDaEliminare;
	}
	public void setListaDaEliminare(ArrayList<SiacRSoggettoRelazFin> listaDaEliminare) {
		this.listaDaEliminare = listaDaEliminare;
	}
	public ArrayList<SiacRSoggettoRelazFin> getListaInvariati() {
		return listaInvariati;
	}
	public void setListaInvariati(ArrayList<SiacRSoggettoRelazFin> listaInvariati) {
		this.listaInvariati = listaInvariati;
	}
	
}
