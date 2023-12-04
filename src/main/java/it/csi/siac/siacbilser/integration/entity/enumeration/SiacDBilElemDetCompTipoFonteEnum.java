/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.FonteFinanziariaComponenteImportiCapitolo;

/**
 * The Enum SiacDBilElemDetCompTipoFonteFinanziariaEnum.
 */
@EnumEntity(entityName="SiacDBilElemDetCompTipoFonte", idPropertyName="elemDetCompTipoFonteId", codePropertyName="elemDetCompTipoFonteCode")
public enum SiacDBilElemDetCompTipoFonteEnum {

	FRESCO_FPV("01", FonteFinanziariaComponenteImportiCapitolo.FRESCO_FPV),
	AVANZO_FPV("02", FonteFinanziariaComponenteImportiCapitolo.AVANZO_FPV),
	AVANZO_AVANZO("03", FonteFinanziariaComponenteImportiCapitolo.AVANZO_AVANZO),
	REISCRIZIONE_PERENTI_AVANZO("04", FonteFinanziariaComponenteImportiCapitolo.REISCRIZIONE_PERENTI_AVANZO),
	;

	/** The codice. */
	private final String codice;
	/** The fonte finanziaria componente importi capitolo. */
	private final FonteFinanziariaComponenteImportiCapitolo tipoFonteFinanziariaComponenteImportiCapitolo;
	
	/**
	 * Instantiates a new siac d bil elem det comp fonte finanziaria enum.
	 *
	 * @param codice the codice
	 * @param tipoFonteFinanziariaComponenteImportiCapitolo the fonte finanziaria componente importi capitolo
	 */
	private SiacDBilElemDetCompTipoFonteEnum(String codice, FonteFinanziariaComponenteImportiCapitolo tipoFonteFinanziariaComponenteImportiCapitolo){
		this.codice = codice;
		this.tipoFonteFinanziariaComponenteImportiCapitolo = tipoFonteFinanziariaComponenteImportiCapitolo;
	}
	
	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
	/**
	 * @return the tipoFonteFinanziariaComponenteImportiCapitolo
	 */
	public FonteFinanziariaComponenteImportiCapitolo getFonteFinanziariaComponenteImportiCapitolo() {
		return this.tipoFonteFinanziariaComponenteImportiCapitolo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem det comp fonte finanziaria enum
	 */
	public static SiacDBilElemDetCompTipoFonteEnum byCodice(String codice){
		for(SiacDBilElemDetCompTipoFonteEnum e : SiacDBilElemDetCompTipoFonteEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetCompTipoFonteEnum");
	}
	
	

	/**
	 * By fonte finanziaria componente importi capitolo.
	 *
	 * @param tipoFonteFinanziariaComponenteImportiCapitolo the tipoFonteFinanziariaComponenteImportiCapitolo
	 * @return the siac d bil elem det comp fonte finanziaria enum
	 */
	public static SiacDBilElemDetCompTipoFonteEnum byFonteFinanziariaComponenteImportiCapitolo(FonteFinanziariaComponenteImportiCapitolo tipoFonteFinanziariaComponenteImportiCapitolo){
		for(SiacDBilElemDetCompTipoFonteEnum e : SiacDBilElemDetCompTipoFonteEnum.values()){
			if(e.getFonteFinanziariaComponenteImportiCapitolo().equals(tipoFonteFinanziariaComponenteImportiCapitolo)){
				return e;
			}
		}
		throw new IllegalArgumentException("La fonte finanziaria "+ tipoFonteFinanziariaComponenteImportiCapitolo + " non ha un mapping corrispondente in SiacDBilElemDetCompTipoFonteEnum");
	}
	/**
	 * By fonte finanziaria componente importi capitolo, even null.
	 *
	 * @param tipoFonteFinanziariaComponenteImportiCapitolo the tipoFonteFinanziariaComponenteImportiCapitolo
	 * @return the siac d bil elem det comp fonte finanziaria enum
	 */
	public static SiacDBilElemDetCompTipoFonteEnum byFonteFinanziariaComponenteImportiCapitoloEvenNull(FonteFinanziariaComponenteImportiCapitolo macrotipoComponenteImportiCapitolo){
		if(macrotipoComponenteImportiCapitolo == null){
			return null;
		}
		return byFonteFinanziariaComponenteImportiCapitolo(macrotipoComponenteImportiCapitolo);
	}
	
}
