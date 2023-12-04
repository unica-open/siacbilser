/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoFamigliaCausale;


// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDCausaleFamTipoEnum.
 */
@EnumEntity(entityName="SiacDCausaleFamTipo", idPropertyName="causFamTipoId", codePropertyName="causFamTipoCode")
public enum SiacDCausaleFamTipoEnum {
		
	/** The Entrata. */
	Entrata("E", TipoFamigliaCausale.ENTRATA),
	
	/** The Spesa. */
	Spesa("S", TipoFamigliaCausale.SPESA),
	
	Predoc_Entrata("PREDOC_E", TipoFamigliaCausale.PREDOC_ENTRATA),
	
	Predoc_Spesa("PREDOC_S", TipoFamigliaCausale.PREDOC_SPESA);
	
	
	/** The codice. */
	private final String codice;
	
	/** The tipo famiglia causale. */
	private final TipoFamigliaCausale tipoFamigliaCausale;
	
	/**
	 * Instantiates a new siac d causale fam tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoFamigliaCausale the tipo famiglia causale
	 */
	private SiacDCausaleFamTipoEnum(String codice, TipoFamigliaCausale tipoFamigliaCausale) {
		this.codice = codice;
		this.tipoFamigliaCausale = tipoFamigliaCausale;
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
	 * Gets the tipo famiglia causale.
	 *
	 * @return the tipo famiglia causale
	 */
	public TipoFamigliaCausale getTipoFamigliaCausale() {
		return tipoFamigliaCausale;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d causale fam tipo enum
	 */
	public static SiacDCausaleFamTipoEnum byCodice(String codice){
		for(SiacDCausaleFamTipoEnum e : SiacDCausaleFamTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDCausaleFamTipoEnum");
	}
	
	/**
	 * By tipo famiglia causale.
	 *
	 * @param tipoFamigliaCausale the tipo famiglia causale
	 * @return the siac d causale fam tipo enum
	 */
	public static SiacDCausaleFamTipoEnum byTipoFamigliaCausale(TipoFamigliaCausale tipoFamigliaCausale){
		for(SiacDCausaleFamTipoEnum e : SiacDCausaleFamTipoEnum.values()){
			if(e.getTipoFamigliaCausale().equals(tipoFamigliaCausale)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato anagrafico "+ tipoFamigliaCausale + " non ha un mapping corrispondente in SiacDCausaleFamTipoEnum");
	}	
}
