/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;

/**
 * Stabilisce il legame tra una Enum e la sua Entity corrispondente su database.
 * 
 * @author Domenico Lisi
 * @see EnumEntityFactory
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnumEntity {
	
	/**
	 * Nome dell'entity.
	 *
	 * @return the string
	 */
	String entityName();
	
	
	/**
	 * Nome della property contenente l'id da restituire.
	 *
	 * @return the string
	 */
	String idPropertyName();

	/**
	 * Nome della property contenente il codice da cercare.
	 *
	 * @return the string
	 */
	String codePropertyName();
	
	
	/**
	 * Nome della property utilizzato per la composizione delle query jpql. 
	 * Se non specificato verra' utilizzato codePropertyName
	 *
	 * @return the string
	 */
	String codePropertyNameJpql() default "";
	
	
}
