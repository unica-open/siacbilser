/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;

public class SubOrdinativoInModificaInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	private ArrayList<SubOrdinativo> subOrdinativiDaInserire = null;
	//subordinativi per i quali sia stato variato l'importo oppure la data di scadenza oppure la descrizione
	private ArrayList<SubOrdinativo> subOrdinativiDaModificare = null;
	
	private ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiModificatiConImportoVariato = null;
	private ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiNuoviConImportoVariato = new ArrayList<SubOrdinativoImportoVariatoDto>();
	private ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiEliminatiConImportoVariato = new ArrayList<SubOrdinativoImportoVariatoDto>();
	
	private ArrayList<SubOrdinativo> subOrdinativiDaModificarePerImportoAumentato = null;
	private ArrayList<SubOrdinativo> subOrdinativiDaModificarePerImportoDiminuito = null;
	
	private List<SiacTOrdinativoTFin> subOrdinativiOld = null;
	private ArrayList<SiacTOrdinativoTFin> subOrdinativiDaEliminare = null;
	private ArrayList<Integer> listaIdModificati=null;
	private int maxCodeOld;
	
	private ArrayList<SiacTOrdinativoTFin> subOrdinativiInvariati;
	
	
	public List<SiacTOrdinativoTFin> getSubOrdinativiOld() {
		return subOrdinativiOld;
	}
	public void setSubOrdinativiOld(List<SiacTOrdinativoTFin> subOrdinativiOld) {
		this.subOrdinativiOld = subOrdinativiOld;
	}
	public ArrayList<SiacTOrdinativoTFin> getSubOrdinativiDaEliminare() {
		return subOrdinativiDaEliminare;
	}
	public void setSubOrdinativiDaEliminare(
			ArrayList<SiacTOrdinativoTFin> subOrdinativiDaEliminare) {
		this.subOrdinativiDaEliminare = subOrdinativiDaEliminare;
	}
	public ArrayList<Integer> getListaIdModificati() {
		return listaIdModificati;
	}
	public void setListaIdModificati(ArrayList<Integer> listaIdModificati) {
		this.listaIdModificati = listaIdModificati;
	}
	public int getMaxCodeOld() {
		return maxCodeOld;
	}
	public void setMaxCodeOld(int maxCodeOld) {
		this.maxCodeOld = maxCodeOld;
	}
	public ArrayList<SubOrdinativo> getSubOrdinativiDaInserire() {
		return subOrdinativiDaInserire;
	}
	public void setSubOrdinativiDaInserire(
			ArrayList<SubOrdinativo> subOrdinativiDaInserire) {
		this.subOrdinativiDaInserire = subOrdinativiDaInserire;
	}
	public ArrayList<SubOrdinativo> getSubOrdinativiDaModificare() {
		return subOrdinativiDaModificare;
	}
	public void setSubOrdinativiDaModificare(
			ArrayList<SubOrdinativo> subOrdinativiDaModificare) {
		this.subOrdinativiDaModificare = subOrdinativiDaModificare;
	}
	public ArrayList<SubOrdinativo> getSubOrdinativiDaModificarePerImportoAumentato() {
		return subOrdinativiDaModificarePerImportoAumentato;
	}
	public void setSubOrdinativiDaModificarePerImportoAumentato(
			ArrayList<SubOrdinativo> subOrdinativiDaModificarePerImportoAumentato) {
		this.subOrdinativiDaModificarePerImportoAumentato = subOrdinativiDaModificarePerImportoAumentato;
	}
	public ArrayList<SubOrdinativo> getSubOrdinativiDaModificarePerImportoDiminuito() {
		return subOrdinativiDaModificarePerImportoDiminuito;
	}
	public void setSubOrdinativiDaModificarePerImportoDiminuito(
			ArrayList<SubOrdinativo> subOrdinativiDaModificarePerImportoDiminuito) {
		this.subOrdinativiDaModificarePerImportoDiminuito = subOrdinativiDaModificarePerImportoDiminuito;
	}
	public ArrayList<SubOrdinativoImportoVariatoDto> getSubOrdinativiModificatiConImportoVariato() {
		return subOrdinativiModificatiConImportoVariato;
	}
	public ArrayList<SubOrdinativoImportoVariatoDto> getSubOrdinativiNuoviOModificatoImporto() {
		ArrayList<SubOrdinativoImportoVariatoDto> list = new ArrayList<SubOrdinativoImportoVariatoDto>();
		if(subOrdinativiModificatiConImportoVariato!=null){
			list.addAll(subOrdinativiModificatiConImportoVariato);
		}
		if(subOrdinativiNuoviConImportoVariato!=null){
			list.addAll(subOrdinativiNuoviConImportoVariato);
		}
		return list;
	}
	public ArrayList<SubOrdinativoImportoVariatoDto> getAllsPerDoppiaGestione() {
		ArrayList<SubOrdinativoImportoVariatoDto> list = new ArrayList<SubOrdinativoImportoVariatoDto>();
		if(subOrdinativiModificatiConImportoVariato!=null){
			list.addAll(subOrdinativiModificatiConImportoVariato);
		}
		if(subOrdinativiNuoviConImportoVariato!=null){
			list.addAll(subOrdinativiNuoviConImportoVariato);
		}
		if(subOrdinativiEliminatiConImportoVariato!=null){
			list.addAll(subOrdinativiEliminatiConImportoVariato);
		}
		return list;
	}
	public void setSubOrdinativiModificatiConImportoVariato(
			ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiModificatiConImportoVariato) {
		this.subOrdinativiModificatiConImportoVariato = subOrdinativiModificatiConImportoVariato;
	}
	public ArrayList<SubOrdinativoImportoVariatoDto> getSubOrdinativiNuoviConImportoVariato() {
		return subOrdinativiNuoviConImportoVariato;
	}
	public void setSubOrdinativiNuoviConImportoVariato(
			ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiNuoviConImportoVariato) {
		this.subOrdinativiNuoviConImportoVariato = subOrdinativiNuoviConImportoVariato;
	}
	public ArrayList<SubOrdinativoImportoVariatoDto> getSubOrdinativiEliminatiConImportoVariato() {
		return subOrdinativiEliminatiConImportoVariato;
	}
	public void setSubOrdinativiEliminatiConImportoVariato(
			ArrayList<SubOrdinativoImportoVariatoDto> subOrdinativiEliminatiConImportoVariato) {
		this.subOrdinativiEliminatiConImportoVariato = subOrdinativiEliminatiConImportoVariato;
	}
	public ArrayList<SiacTOrdinativoTFin> getSubOrdinativiInvariati() {
		return subOrdinativiInvariati;
	}
	public void setSubOrdinativiInvariati(
			ArrayList<SiacTOrdinativoTFin> subOrdinativiInvariati) {
		this.subOrdinativiInvariati = subOrdinativiInvariati;
	}
	
}
