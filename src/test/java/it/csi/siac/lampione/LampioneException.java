/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.lampione;

/**
 * Eccezione per il Lampione
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/05/2018
 *
 */
public class LampioneException extends Exception {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1077198902494572711L;

	/**
	 * @param message il messaggio di errore
	 * @param cause la causa dell'errore
	 * @see Exception#Exception(String, Throwable)
	 */
	public LampioneException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message il messaggio di errore
	 * @see Exception#Exception(String)
	 */
	public LampioneException(String message) {
		super(message);
	}

}
