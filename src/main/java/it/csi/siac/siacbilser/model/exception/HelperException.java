/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model.exception;

public class HelperException extends RuntimeException {

	private static final long serialVersionUID = -4373151268415636488L;

	public HelperException() {
		super();
	}

	public HelperException(String message, Throwable cause) {
		super(message, cause);
	}

	public HelperException(String message) {
		super(message);
	}

	public HelperException(Throwable cause) {
		super(cause);
	}

}
