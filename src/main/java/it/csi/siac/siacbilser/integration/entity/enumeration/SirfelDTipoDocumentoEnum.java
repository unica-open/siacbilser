/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.sirfelser.model.TipoDocumentoFEL;

// TODO: Auto-generated Javadoc
/**
 * The Enum SirfelDTipoDocumentoEnum.
 */
@EnumEntity(entityName="SirfelDTipoDocumento", idPropertyName="doctipoId", codePropertyName="codice")
public enum SirfelDTipoDocumentoEnum {
		
	Fattura("TD01", TipoDocumentoFEL.FATTURA),
	Acconto_Fattura("TD02", TipoDocumentoFEL.ACCONTO_FATTURA),
	Acconto_Parcella("TD03", TipoDocumentoFEL.ACCONTO_PARCELLA),
	Nota_Di_Credito("TD04", TipoDocumentoFEL.NOTA_DI_CREDITO),
	Nota_Di_Debito("TD05", TipoDocumentoFEL.NOTA_DI_DEBITO),
	Parcella("TD06", TipoDocumentoFEL.PARCELLA),
	//SIAC-7557 INIZIO
	integrazione_fattura_reverse_charge_interno("TD16", TipoDocumentoFEL.INTEGRAZIONE_FATTURA_REVERSE_CHARGE_INTERNO),
	integrazione_autofattura_acquisto_servizi_estero("TD17", TipoDocumentoFEL.INTEGRAZIONE_AUTOFATTURA_ACQUISTO_SERVIZI_ESTERO),
	integrazione_acquisto_beni_intracomunitari("TD18", TipoDocumentoFEL.INTEGRAZIONE_ACQUISTO_BENI_INTRACOMUNITARI),
	integrazione_autofattura_acquisto_beni_ex_art_1("TD19", TipoDocumentoFEL.INTEGRAZIONE_AUTOFATTURA_ACQUISTO_BENI_EX_ART_1),
	autofattura_per_splafonamento("TD21", TipoDocumentoFEL.AUTOFATTURA_SPLAFONAMNTO),
	estrazioni_beni_deposito_iva("TD22", TipoDocumentoFEL.ESTRAZIONE_BENI_DEPOSITO_IVA),
	estrazioni_beni_deposito_iva_con_versamento("TD23", TipoDocumentoFEL.ESTRAZIONE_BENI_DEPOSITO_IVA_CON_VERSAMENTO),
	fattura_differita_art_1("TD24", TipoDocumentoFEL.FATTURA_DIFFERITA_ART_1),
	fattura_differita_art_2("TD25", TipoDocumentoFEL.FATTURA_DIFFERITA_ART_2),
	cessione_beni_ammortizzabili("TD26", TipoDocumentoFEL.CESSIONE_BENI_AMMORTIZZABILI),
	fattura_autoconsumo_cessioni_gratuite("TD27", TipoDocumentoFEL.FATTURA_AUTOCONSUMO_CESSIONI_GRATUITE)
	//SIAC-7557 FINE
	;
 
	
	private final String codice;
	private final TipoDocumentoFEL tipoDocumentoFEL;
	
	/**
	 * Instantiates a new siac d subdoc iva stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SirfelDTipoDocumentoEnum(String codice, TipoDocumentoFEL tipoDocumentoFEL){
		this.codice = codice;
		this.tipoDocumentoFEL = tipoDocumentoFEL;
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
	 * @return the tipoDocumentoFEL
	 */
	public TipoDocumentoFEL getTipoDocumentoFEL() {
		return tipoDocumentoFEL;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d subdoc iva stato enum
	 */
	public static SirfelDTipoDocumentoEnum byCodice(String codice){
		for(SirfelDTipoDocumentoEnum e : SirfelDTipoDocumentoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SirfelDTipoDocumentoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d prima nota stato enum
	 */
	public static SirfelDTipoDocumentoEnum byTipoDocumentoFEL(TipoDocumentoFEL tipoDocumento){
		for(SirfelDTipoDocumentoEnum e : SirfelDTipoDocumentoEnum.values()){
			if(e.getTipoDocumentoFEL().equals(tipoDocumento)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo documento "+ tipoDocumento + " non ha un mapping corrispondente in SirfelDTipoDocumentoEnum");
	}
	
	public static SirfelDTipoDocumentoEnum byTipoDocumentoFELEvenNull(TipoDocumentoFEL tipoDocumento){
		if(tipoDocumento==null) {
			return null;
		}
		return byTipoDocumentoFEL(tipoDocumento);
	}
	

}
