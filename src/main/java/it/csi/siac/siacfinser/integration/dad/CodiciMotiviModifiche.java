/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

public enum CodiciMotiviModifiche {

	VALTRI("VALTRI","ROR - Altri vincoli"),
	VTRAS("VTRAS","ROR - Vincoli derivanti da trasferimenti"),
	VPCON("VPCON","ROR - Vincoli derivanti da leggi e dai principi contabili"),
	VCMUT("VCMUT","ROR - Vincoli derivanti dalla contrazione di mutui"),
	VFAEN("VFAEN","ROR - Vincoli formalmente attribuiti dall'ente"),
	CROR("CROR","ROR - Cancellazione"),
	REIMP("REIMP","ROR - Reimputazione"),
	VPN("VPN","ROR - Vincoli per investimento"),
	INESIG("INESIG","Cancellazione per Inesigibilita'"),
	INSUSS("INSUSS","Cancellazione per Insussistenza"),
	INEROR("INEROR","ROR - Cancellazione per Inesigibilita'"),
	INSROR("INSROR","ROR - Cancellazione per Insussistenza"),
	RORM("RORM","ROR - Da Mantenere")
	;
	
	
	private String codice;
	private String descrizione;
	
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	CodiciMotiviModifiche(String codice, String descrizione){
		this.codice=codice;
		this.descrizione=descrizione;
	}
	
	
	
	
	
	
	
}
