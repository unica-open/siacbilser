/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.math.BigDecimal;

/**
 * The Enum SiacDAttrTipoEnum.
 */
@EnumEntity(entityName="SiacDAttrTipo", idPropertyName="attrTipoId", codePropertyName="attrTipoCode")
public enum SiacDAttrTipoEnum {
	
	Boolean("B", Boolean.class),
	Numerico("N", BigDecimal.class),
	Testo("X", String.class),
	Percentuale("P", BigDecimal.class),
	Tabella("T", Integer.class),
	;
	
	/** The codice. */
	private final String codice;
	/** The field type. */
	private final Class<?> fieldType;
	
	/**
	 * Instantiates a new siac d attr tipo enum.
	 *
	 * @param codice the codice
	 * @param fieldType the field type
	 */
	private SiacDAttrTipoEnum(String codice, Class<?> fieldType){
		this.codice = codice;
		this.fieldType = fieldType;
	}
	
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the fieldType
	 */
	public Class<?> getFieldType() {
		return fieldType;
	}

}
