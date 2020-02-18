/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

public class ModificaMovimentoGestioneSpesaInfoDto implements Serializable {

	private static final long serialVersionUID = -7299069855112178432L;

	private List<ModificaMovimentoGestioneSpesa> modificheDaCreare = new ArrayList<ModificaMovimentoGestioneSpesa>();
	private List<ModificaMovimentoGestioneSpesa> modificheDaAggiornare = new ArrayList<ModificaMovimentoGestioneSpesa>();
	private List<ModificaMovimentoGestioneSpesa> modificheResidue = new ArrayList<ModificaMovimentoGestioneSpesa>();
	
	//Serve tenere traccia degli atti per effettuarne la validazione invocando l'opportuno service:
	private List<AttiAmmModificatiGestioneSpesaInfoDto> attiModificati = new ArrayList<AttiAmmModificatiGestioneSpesaInfoDto>();
	private List<AttiAmmModificatiGestioneSpesaInfoDto> attiInseriti = new ArrayList<AttiAmmModificatiGestioneSpesaInfoDto>();
	private List<AttiAmmModificatiGestioneSpesaInfoDto> attiInseritiEModificati = new ArrayList<AttiAmmModificatiGestioneSpesaInfoDto>();
	
	public List<ModificaMovimentoGestioneSpesa> getModificheDaCreare() {
		return modificheDaCreare;
	}
	public void setModificheDaCreare(
			List<ModificaMovimentoGestioneSpesa> modificheDaCreare) {
		this.modificheDaCreare = modificheDaCreare;
	}
	public List<ModificaMovimentoGestioneSpesa> getModificheDaAggiornare() {
		return modificheDaAggiornare;
	}
	public void setModificheDaAggiornare(
			List<ModificaMovimentoGestioneSpesa> modificheDaAggiornare) {
		this.modificheDaAggiornare = modificheDaAggiornare;
	}
	public List<ModificaMovimentoGestioneSpesa> getModificheResidue() {
		return modificheResidue;
	}
	public void setModificheResidue(
			List<ModificaMovimentoGestioneSpesa> modificheResidue) {
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
		
		return modificheDaCreare!=null && modificheDaCreare.size()>0;
		
	}
	
	public List<AttiAmmModificatiGestioneSpesaInfoDto> getAttiModificati() {
		return attiModificati;
	}
	public void setAttiModificati(
			List<AttiAmmModificatiGestioneSpesaInfoDto> attiModificati) {
		this.attiModificati = attiModificati;
	}
	public List<AttiAmmModificatiGestioneSpesaInfoDto> getAttiInseriti() {
		return attiInseriti;
	}
	public void setAttiInseriti(
			List<AttiAmmModificatiGestioneSpesaInfoDto> attiInseriti) {
		this.attiInseriti = attiInseriti;
	}
	public List<AttiAmmModificatiGestioneSpesaInfoDto> getAttiInseritiEModificati() {
		return attiInseritiEModificati;
	}
	public void setAttiInseritiEModificati(
			List<AttiAmmModificatiGestioneSpesaInfoDto> attiInseritiEModificati) {
		this.attiInseritiEModificati = attiInseritiEModificati;
	}
	
}
