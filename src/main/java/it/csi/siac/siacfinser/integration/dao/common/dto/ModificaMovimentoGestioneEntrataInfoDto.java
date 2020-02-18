/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

public class ModificaMovimentoGestioneEntrataInfoDto implements Serializable {

	private static final long serialVersionUID = -6144610062572678270L;

	private List<ModificaMovimentoGestioneEntrata> modificheDaCreare = new ArrayList<ModificaMovimentoGestioneEntrata>();
	private List<ModificaMovimentoGestioneEntrata> modificheDaAggiornare = new ArrayList<ModificaMovimentoGestioneEntrata>();
	private List<ModificaMovimentoGestioneEntrata> modificheResidue = new ArrayList<ModificaMovimentoGestioneEntrata>();
	
	//Serve tenere traccia degli atti per effettuarne la validazione invocando l'opportuno service:
	private List<AttiAmmModificatiGestioneEntrataInfoDto> attiModificati = new ArrayList<AttiAmmModificatiGestioneEntrataInfoDto>();
	private List<AttiAmmModificatiGestioneEntrataInfoDto> attiInseriti = new ArrayList<AttiAmmModificatiGestioneEntrataInfoDto>();
	private List<AttiAmmModificatiGestioneEntrataInfoDto> attiInseritiEModificati = new ArrayList<AttiAmmModificatiGestioneEntrataInfoDto>();
	
	public List<ModificaMovimentoGestioneEntrata> getModificheDaCreare() {
		return modificheDaCreare;
	}
	public void setModificheDaCreare(
			List<ModificaMovimentoGestioneEntrata> modificheDaCreare) {
		this.modificheDaCreare = modificheDaCreare;
	}
	public List<ModificaMovimentoGestioneEntrata> getModificheDaAggiornare() {
		return modificheDaAggiornare;
	}
	public void setModificheDaAggiornare(
			List<ModificaMovimentoGestioneEntrata> modificheDaAggiornare) {
		this.modificheDaAggiornare = modificheDaAggiornare;
	}
	public List<ModificaMovimentoGestioneEntrata> getModificheResidue() {
		return modificheResidue;
	}
	public void setModificheResidue(
			List<ModificaMovimentoGestioneEntrata> modificheResidue) {
		this.modificheResidue = modificheResidue;
	}
	
	/**
	 * Da lanciare dopo aver valorizzato il dto con il metodo che valuta le modifiche
	 * Serve per checkare al volo se c'e' qualcosa da modificare riguardo le modifiche gestione
	 * @return
	 */
	public boolean modifichePresenti(){
		if(modificheDaCreare!=null && modificheDaCreare.size()>0){
			return true;
		}
		if(modificheDaAggiornare!=null && modificheDaAggiornare.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean isModificheDaCrearePresenti(){

		return (modificheDaCreare!=null && modificheDaCreare.size()>0);
	}
	
	
	public List<AttiAmmModificatiGestioneEntrataInfoDto> getAttiModificati() {
		return attiModificati;
	}
	public void setAttiModificati(
			List<AttiAmmModificatiGestioneEntrataInfoDto> attiModificati) {
		this.attiModificati = attiModificati;
	}
	public List<AttiAmmModificatiGestioneEntrataInfoDto> getAttiInseriti() {
		return attiInseriti;
	}
	public void setAttiInseriti(
			List<AttiAmmModificatiGestioneEntrataInfoDto> attiInseriti) {
		this.attiInseriti = attiInseriti;
	}
	public List<AttiAmmModificatiGestioneEntrataInfoDto> getAttiInseritiEModificati() {
		return attiInseritiEModificati;
	}
	public void setAttiInseritiEModificati(
			List<AttiAmmModificatiGestioneEntrataInfoDto> attiInseritiEModificati) {
		this.attiInseritiEModificati = attiInseritiEModificati;
	}
	
}
