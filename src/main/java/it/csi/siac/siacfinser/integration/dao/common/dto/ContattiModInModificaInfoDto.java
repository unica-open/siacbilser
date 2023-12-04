/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoModFin;
import it.csi.siac.siacfinser.model.soggetto.Contatto;

public class ContattiModInModificaInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -541704184894844890L;
	private ArrayList<Contatto> listaContattiNuovi = new ArrayList<Contatto>();
	private ArrayList<Contatto> listaRimastiUguali = new ArrayList<Contatto>();
	private ArrayList<ContattoModModificatoDto> listaModificati = new ArrayList<ContattoModModificatoDto>();
	private ArrayList<SiacTRecapitoSoggettoModFin> listaDaInvalidare = new ArrayList<SiacTRecapitoSoggettoModFin>();
	private boolean rimastiUguali=false;
	
	public ArrayList<Contatto> getListaContattiNuovi() {
		return listaContattiNuovi;
	}
	public void setListaContattiNuovi(ArrayList<Contatto> listaContattiNuovi) {
		this.listaContattiNuovi = listaContattiNuovi;
	}
	public ArrayList<Contatto> getListaRimastiUguali() {
		return listaRimastiUguali;
	}
	public void setListaRimastiUguali(ArrayList<Contatto> listaRimastiUguali) {
		this.listaRimastiUguali = listaRimastiUguali;
	}
	public ArrayList<SiacTRecapitoSoggettoModFin> getListaDaInvalidare() {
		return listaDaInvalidare;
	}
	public void setListaDaInvalidare(
			ArrayList<SiacTRecapitoSoggettoModFin> listaDaInvalidare) {
		this.listaDaInvalidare = listaDaInvalidare;
	}
	public boolean isRimastiUguali() {
		return rimastiUguali;
	}
	public void setRimastiUguali(boolean rimastiUguali) {
		this.rimastiUguali = rimastiUguali;
	}
	public ArrayList<ContattoModModificatoDto> getListaModificati() {
		return listaModificati;
	}
	public void setListaModificati(
			ArrayList<ContattoModModificatoDto> listaModificati) {
		this.listaModificati = listaModificati;
	}
	
}
