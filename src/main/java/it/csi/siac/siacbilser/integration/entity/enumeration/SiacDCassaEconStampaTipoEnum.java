/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siaccecser.model.TipoDocumento;

/**
 * Enum per l'entity SiacDCassaEconStampaTipo
 *
 */
@EnumEntity(entityName="SiacDCassaEconStampaTipo", idPropertyName="cestTipoId", codePropertyName="cestTipoCode")
public enum SiacDCassaEconStampaTipoEnum {
	
	GiornaleCassa    ("GIO", TipoDocumento.GIORNALE_CASSA),
	
	Rendiconto    ("REN", TipoDocumento.RENDICONTO),
	
	Ricevuta    ("RIC", TipoDocumento.RICEVUTA),

	;
	
	/** The codice. */
	private final String codice;
	
	/** The tipo documento. */
	private final TipoDocumento tipoDocumento;
	
	
	/**
	 * Instantiates a new siac d iva registro tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoRegistroIva the tipo registro iva
	 */
	private SiacDCassaEconStampaTipoEnum(String codice, TipoDocumento tipoDocumento){
		this.codice = codice;
		this.tipoDocumento = tipoDocumento;
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
	 * Gets the tipo registro iva.
	 *
	 * @return the tipo registro iva
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDCassaEconStampaTipoEnum byCodice(String codice){
		for(SiacDCassaEconStampaTipoEnum e : SiacDCassaEconStampaTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDCassaEconStampaTipoEnum");
	}
	
	/**
	 * By tipo registro iva.
	 *
	 * @param tipoStampaIva the tipo
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDCassaEconStampaTipoEnum byTipoStampaIva(TipoDocumento tipoDocumento){
		for(SiacDCassaEconStampaTipoEnum e : SiacDCassaEconStampaTipoEnum.values()){
			if(e.getTipoDocumento().equals(tipoDocumento)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo stampa cassa economale "+ tipoDocumento + " non ha un mapping corrispondente in SiacDIvaStampaTipoEnum");
	}
	
}
