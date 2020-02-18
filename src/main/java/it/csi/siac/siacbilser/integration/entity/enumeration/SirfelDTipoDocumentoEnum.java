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
	Parcella("TD06", TipoDocumentoFEL.PARCELLA);
	
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
