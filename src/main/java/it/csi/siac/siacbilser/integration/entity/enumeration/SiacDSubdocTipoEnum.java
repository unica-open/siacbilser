/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;



// TODO: Auto-generated Javadoc
/**
 * select replace(initcap(elem_det_tipo_desc),' ','') || '("' || elem_det_tipo_code || '"),' from siac_d_bil_elem_det_tipo.
 */
@EnumEntity(entityName="SiacDSubdocTipo", idPropertyName="subdocTipoId", codePropertyName="subdocTipoCode")
public enum SiacDSubdocTipoEnum {
	
	/** The Entrata. */
	Entrata("SE", "Subdocumetno Entrata"),
	
	/** The Spesa. */
	Spesa("SS", "Subdocumento Spesa"),
	;

	/** The codice. */
	private final String codice;
	
	/** The descrizione. */
	private final String descrizione;

	/**
	 * Instantiates a new siac d subdoc tipo enum.
	 *
	 * @param codice the codice
	 * @param descrizione - nome del Field in nella classe di Model <? extends ImportiCapitolo>
	 */
	SiacDSubdocTipoEnum(String codice, String descrizione){
		this.codice = codice;
		this.descrizione = descrizione;
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
 * @return the siac d subdoc tipo enum
 */
public static SiacDSubdocTipoEnum byCodice(String codice){
		for(SiacDSubdocTipoEnum e : SiacDSubdocTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("importo capitolo con codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetTipoEnum");
	}

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
		

//	public Integer getId() {
//		return id;
//	}
//	
//	public void setId(Integer id) {
//		this.id = id;
//	}
	
	

	/**
 * Gets the descrizione.
 *
 * @return the descrizione
 */
public String getDescrizione() {
		return descrizione;
	}
	
}