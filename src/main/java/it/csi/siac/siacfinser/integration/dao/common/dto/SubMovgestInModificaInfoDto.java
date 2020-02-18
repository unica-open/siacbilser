/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public class SubMovgestInModificaInfoDto <I extends MovimentoGestione> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	private ArrayList<I> subImpegniDaInserire = null;
	private ArrayList<I> subImpegniDaModificare = null;
	private List<SiacTMovgestTsFin> subImpegniOld = null;
	private ArrayList<SiacTMovgestTsFin> subImpegniDaEliminare = null;
	private ArrayList<I> subImpegniInvariati = null;
	
	private ArrayList<Integer> listaIdModificati=null;
	
	public ArrayList<I> getSubImpegniDaInserire() {
		return subImpegniDaInserire;
	}
	public void setSubImpegniDaInserire(ArrayList<I> subImpegniDaInserire) {
		this.subImpegniDaInserire = subImpegniDaInserire;
	}
	public ArrayList<I> getSubImpegniDaModificare() {
		return subImpegniDaModificare;
	}
	public void setSubImpegniDaModificare(ArrayList<I> subImpegniDaModificare) {
		this.subImpegniDaModificare = subImpegniDaModificare;
	}
	public List<SiacTMovgestTsFin> getSubImpegniOld() {
		return subImpegniOld;
	}
	public void setSubImpegniOld(List<SiacTMovgestTsFin> subImpegniOld) {
		this.subImpegniOld = subImpegniOld;
	}
	public ArrayList<SiacTMovgestTsFin> getSubImpegniDaEliminare() {
		return subImpegniDaEliminare;
	}
	public void setSubImpegniDaEliminare(
			ArrayList<SiacTMovgestTsFin> subImpegniDaEliminare) {
		this.subImpegniDaEliminare = subImpegniDaEliminare;
	}
	public ArrayList<Integer> getListaIdModificati() {
		return listaIdModificati;
	}
	public void setListaIdModificati(ArrayList<Integer> listaIdModificati) {
		this.listaIdModificati = listaIdModificati;
	}
	public ArrayList<I> getSubImpegniInvariati() {
		return subImpegniInvariati;
	}
	public void setSubImpegniInvariati(ArrayList<I> subImpegniInvariati) {
		this.subImpegniInvariati = subImpegniInvariati;
	}
	
	/**
	 * restituisce MODIFICATI + INVARIATI + INSERITI
	 * @return
	 */
	public ArrayList<I> getRimanentiDopoModifiche(){
		ArrayList<I> l = new ArrayList<I>();
		if(subImpegniDaModificare!=null){
			l.addAll(subImpegniDaModificare);
		}
		if(subImpegniDaInserire!=null){
			l.addAll(subImpegniDaInserire);
		}
		if(subImpegniInvariati!=null){
			l.addAll(subImpegniInvariati);
		}
		return l;
	}
	
	/**
	 * restituisce quelli INSERITI + MODIFICATI
	 * @return
	 */
	public ArrayList<I> getNuoviEModificati(){
		ArrayList<I> l = new ArrayList<I>();
		if(subImpegniDaModificare!=null){
			l.addAll(subImpegniDaModificare);
		}
		if(subImpegniDaInserire!=null){
			l.addAll(subImpegniDaInserire);
		}
		return l;
	}
	
}
