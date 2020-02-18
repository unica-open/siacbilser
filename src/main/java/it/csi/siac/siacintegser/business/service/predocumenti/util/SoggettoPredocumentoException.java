/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.util;

public class SoggettoPredocumentoException extends Exception
{
	private static final long serialVersionUID = 7167810550207882577L;

	public SoggettoPredocumentoException(Exception exception)
	{
		super(exception);
	}

	public SoggettoPredocumentoException(String message)
	{
		super(message);
	}
}
