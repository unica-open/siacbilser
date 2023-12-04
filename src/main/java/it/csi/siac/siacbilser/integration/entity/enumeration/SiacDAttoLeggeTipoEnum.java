/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;



// TODO: Auto-generated Javadoc
/**
 * select replace(initcap(elem_det_tipo_desc),' ','') || '("' || elem_det_tipo_code || '"),' from siac_d_bil_elem_det_tipo.
 */
@EnumEntity(entityName="SiacDAttoLeggeTipo", idPropertyName="attoleggeTipoId", codePropertyName="attoleggeTipoCode")
public enum SiacDAttoLeggeTipoEnum {
	
	/** The Legge regionale. */
	LeggeRegionale("1", "Legge Regionale"),
	
	/** The Legge statale. */
	LeggeStatale("2", "Legge Statale");
	
	
	/** The codice. */
	private final String codice;
	
	/** The descrizione. */
	private final String descrizione;

	/**
	 * Instantiates a new siac d atto legge tipo enum.
	 *
	 * @param codice the codice
	 * @param importiCapitoloFieldName - nome del Field in nella classe di Model <? extends ImportiCapitolo>
	 */
	SiacDAttoLeggeTipoEnum(String codice, String importiCapitoloFieldName){
		
		this.codice = codice;
		this.descrizione = importiCapitoloFieldName;
	}
	
//	public SiacDBilElemDetTipo getEntity(){
//		SiacDBilElemDetTipo result = new SiacDBilElemDetTipo();
//		result.setElemDetTipoId(getId());
//		result.setElemDetTipoCode(getCodice());
//		return result;
//	}
	
	/**
 * By codice.
 *
 * @param codice the codice
 * @return the siac d atto legge tipo enum
 */
public static SiacDAttoLeggeTipoEnum byCodice(String codice){
		for(SiacDAttoLeggeTipoEnum e : SiacDAttoLeggeTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Codice "+ codice + " non ha un mapping corrispondente in SiacDAttoLeggeTipoEnum");
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
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}
	
}