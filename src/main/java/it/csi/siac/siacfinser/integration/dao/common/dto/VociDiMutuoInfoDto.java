/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceFin;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;

public class VociDiMutuoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2664415329705174144L;
	
	ArrayList<VoceMutuo> listaDaAggiornare = new ArrayList<VoceMutuo>();
	ArrayList<VoceMutuo> listaDaInserire = new ArrayList<VoceMutuo>();
	ArrayList<SiacTMutuoVoceFin> listaDaEliminare = new ArrayList<SiacTMutuoVoceFin>();
	
	public ArrayList<VoceMutuo> getListaDaAggiornare() {
		return listaDaAggiornare;
	}
	public void setListaDaAggiornare(
			ArrayList<VoceMutuo> listaDaAggiornare) {
		this.listaDaAggiornare = listaDaAggiornare;
	}
	public ArrayList<VoceMutuo> getListaDaInserire() {
		return listaDaInserire;
	}
	public void setListaDaInserire(ArrayList<VoceMutuo> listaDaInserire) {
		this.listaDaInserire = listaDaInserire;
	}
	public ArrayList<SiacTMutuoVoceFin> getListaDaEliminare() {
		return listaDaEliminare;
	}
	public void setListaDaEliminare(ArrayList<SiacTMutuoVoceFin> listaDaEliminare) {
		this.listaDaEliminare = listaDaEliminare;
	}	
}
