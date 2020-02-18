/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita;

import it.csi.siac.siacbilser.integration.utility.function.jdbc.SQLParam;

/**
 * Parametri per le funzionalit&agrave; delle entit&agave; consultabili
 * @author Marchino Alessandro
 *
 */
public enum FunctionEntitaConsultabileParam {

	UID_PADRE(FunctionEntitaConsultabileParamType.INTEGER),
	ANNO_ESERCIZIO(FunctionEntitaConsultabileParamType.VARCHAR),
	// Filtri generici. Possono essere usati per varie funzionalita'
	FILTRO_GENERICO_0(FunctionEntitaConsultabileParamType.VARCHAR),
	;
	
	private final FunctionEntitaConsultabileParamType type;
	
	private FunctionEntitaConsultabileParam(FunctionEntitaConsultabileParamType type) {
		this.type = type;
	}
	
	/**
	 * @return the type
	 */
	public FunctionEntitaConsultabileParamType getType() {
		return type;
	}
	
	/**
	 * Converte il valore fornito in parametro SQL
	 * @param value il valore fornito
	 * @return il parametro SQL
	 */
	public SQLParam convert(Object value) {
		return this.type.convert(value);
	}
}
