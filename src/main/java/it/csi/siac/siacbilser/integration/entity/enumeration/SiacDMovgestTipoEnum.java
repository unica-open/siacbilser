/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

public enum SiacDMovgestTipoEnum {
		
	Impegno("I"),
	Accertamento("A"),
	;
	
	private final String codice;
	

	private SiacDMovgestTipoEnum(String codice){
		this.codice = codice;
	}
	
	public String getCodice() {
		return codice;
	}

	public static SiacDMovgestTipoEnum byCodice(String codice){
		for(SiacDMovgestTipoEnum e : SiacDMovgestTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice " + codice + " non ha un mapping corrispondente");
	}
	

}
