/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

public class AttiAmmModificatiGestioneSpesaInfoDto implements Serializable {

	private static final long serialVersionUID = -6144610062572678270L;
	
	private ModificaMovimentoGestioneSpesa modificaAssociata;
	
	//ricevuto in input dal front end:
	private AttoAmministrativo attoRicevutoInInput;
	
	//ri-caricato a partire da quanto ricevuto dal front end:
	private SiacTAttoAmmFin nuovoAttoIndividuato;
	
	//atto attuale persistente a db:
	private SiacTAttoAmmFin oldAttoSuDb;
	
	
	public AttiAmmModificatiGestioneSpesaInfoDto(AttoAmministrativo attoRicevutoInInput,SiacTAttoAmmFin nuovoAttoIndividuato,
			SiacTAttoAmmFin oldAttoSuDb,ModificaMovimentoGestioneSpesa modificaAssociata){
		setAttoRicevutoInInput(attoRicevutoInInput);
		setModificaAssociata(modificaAssociata);
		setNuovoAttoIndividuato(nuovoAttoIndividuato);
		setOldAttoSuDb(oldAttoSuDb);
	}

	public ModificaMovimentoGestioneSpesa getModificaAssociata() {
		return modificaAssociata;
	}

	public void setModificaAssociata(
			ModificaMovimentoGestioneSpesa modificaAssociata) {
		this.modificaAssociata = modificaAssociata;
	}

	public AttoAmministrativo getAttoRicevutoInInput() {
		return attoRicevutoInInput;
	}

	public void setAttoRicevutoInInput(AttoAmministrativo attoRicevutoInInput) {
		this.attoRicevutoInInput = attoRicevutoInInput;
	}

	public SiacTAttoAmmFin getNuovoAttoIndividuato() {
		return nuovoAttoIndividuato;
	}

	public void setNuovoAttoIndividuato(SiacTAttoAmmFin nuovoAttoIndividuato) {
		this.nuovoAttoIndividuato = nuovoAttoIndividuato;
	}

	public SiacTAttoAmmFin getOldAttoSuDb() {
		return oldAttoSuDb;
	}

	public void setOldAttoSuDb(SiacTAttoAmmFin oldAttoSuDb) {
		this.oldAttoSuDb = oldAttoSuDb;
	}
	
}
