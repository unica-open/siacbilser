/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import it.csi.siac.siacfinser.integration.dao.EnumEntityFinFactory;

/**
 * Stabilisce il legame tra una Enum e la sua Entity corrispondente su database.
 * 
 * @author Domenico Lisi
 * @see EnumEntityFinFactory
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumEntityFin {
	
	/**
	 * Nome dell'entity
	 * @return
	 */
	String entityName();
	
	
	/**
	 * Nome della property contenente l'id da restituire.
	 * @return
	 */
	String idPropertyName();

	/**
	 * Nome della property contenente il codice da cercare.
	 * @return
	 */
	String codePropertyName();

}
