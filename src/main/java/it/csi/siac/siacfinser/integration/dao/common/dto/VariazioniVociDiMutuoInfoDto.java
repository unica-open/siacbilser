/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;

import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceVarFin;
import it.csi.siac.siacfinser.model.mutuo.VariazioneImportoVoceMutuo;

public class VariazioniVociDiMutuoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2664415329705174144L;
	
	ArrayList<VariazioneImportoVoceMutuo> listaDaAggiornare = new ArrayList<VariazioneImportoVoceMutuo>();
	ArrayList<VariazioneImportoVoceMutuo> listaDaInserire = new ArrayList<VariazioneImportoVoceMutuo>();
	ArrayList<SiacTMutuoVoceVarFin> listaDaEliminare = new ArrayList<SiacTMutuoVoceVarFin>();
	
	public ArrayList<VariazioneImportoVoceMutuo> getListaDaAggiornare() {
		return listaDaAggiornare;
	}
	public void setListaDaAggiornare(
			ArrayList<VariazioneImportoVoceMutuo> listaDaAggiornare) {
		this.listaDaAggiornare = listaDaAggiornare;
	}
	public ArrayList<VariazioneImportoVoceMutuo> getListaDaInserire() {
		return listaDaInserire;
	}
	public void setListaDaInserire(ArrayList<VariazioneImportoVoceMutuo> listaDaInserire) {
		this.listaDaInserire = listaDaInserire;
	}
	public ArrayList<SiacTMutuoVoceVarFin> getListaDaEliminare() {
		return listaDaEliminare;
	}
	public void setListaDaEliminare(ArrayList<SiacTMutuoVoceVarFin> listaDaEliminare) {
		this.listaDaEliminare = listaDaEliminare;
	}	
}
