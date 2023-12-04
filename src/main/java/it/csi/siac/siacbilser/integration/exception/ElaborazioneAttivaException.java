/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.exception;

/**
 * Indica che una elaborazione &egrave; gi&agrave; attiva.
 * 
 * @author Domenico
 *
 */
public class ElaborazioneAttivaException extends Exception {

	private static final long serialVersionUID = -7583194300388941078L;

	public ElaborazioneAttivaException() {
		super();
	}

	public ElaborazioneAttivaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElaborazioneAttivaException(String message) {
		super(message);
	}

	public ElaborazioneAttivaException(Throwable cause) {
		super(cause);
	}

	
	
}
