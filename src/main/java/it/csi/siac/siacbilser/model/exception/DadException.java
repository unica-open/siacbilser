/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model.exception;

/**
 * Eccezione nella gestione del DAD
 * @author Marchino Alessandro
 * @version 1.0.0 - 19/03/2018
 */
public class DadException extends Exception {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5582817216139436257L;

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public DadException() {
		super();
	}

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public DadException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public DadException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public DadException(Throwable cause) {
		super(cause);
	}

}
