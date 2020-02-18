/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoFin;
import it.csi.siac.siacfinser.model.soggetto.Contatto;

public class ContattiInModificaInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1492940930796023015L;
	private ArrayList<Contatto> listaContattiNuovi = new ArrayList<Contatto>();
	private ArrayList<Contatto> listaRimastiUguali = new ArrayList<Contatto>();
	private ArrayList<ContattoModificatoDto> listaModificati = new ArrayList<ContattoModificatoDto>();
	private ArrayList<SiacTRecapitoSoggettoFin> listaDaInvalidare = new ArrayList<SiacTRecapitoSoggettoFin>();
//	private ArrayList<Contatto> listaModPiuNuovi = new ArrayList<Contatto>();
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
	public ArrayList<SiacTRecapitoSoggettoFin> getListaDaInvalidare() {
		return listaDaInvalidare;
	}
	public void setListaDaInvalidare(
			ArrayList<SiacTRecapitoSoggettoFin> listaDaInvalidare) {
		this.listaDaInvalidare = listaDaInvalidare;
	}
//	public ArrayList<Contatto> getListaModPiuNuovi() {
//		return listaModPiuNuovi;
//	}
//	public void setListaModPiuNuovi(ArrayList<Contatto> listaModPiuNuovi) {
//		this.listaModPiuNuovi = listaModPiuNuovi;
//	}
	public boolean isRimastiUguali() {
		return rimastiUguali;
	}
	public void setRimastiUguali(boolean rimastiUguali) {
		this.rimastiUguali = rimastiUguali;
	}
	public ArrayList<ContattoModificatoDto> getListaModificati() {
		return listaModificati;
	}
	public void setListaModificati(ArrayList<ContattoModificatoDto> listaModificati) {
		this.listaModificati = listaModificati;
	}
}
