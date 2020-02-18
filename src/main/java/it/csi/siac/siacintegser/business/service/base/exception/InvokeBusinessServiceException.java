/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base.exception;

import java.util.List;

import it.csi.siac.siaccorser.model.Errore;

public class InvokeBusinessServiceException extends RuntimeException
{

	private static final long serialVersionUID = 5734255684495996781L;

	private List<Errore> errori;

	protected InvokeBusinessServiceException()
	{
		super();
	}

	public InvokeBusinessServiceException(List<Errore> errori)
	{
		this();
		this.errori = errori;
	}

	public List<Errore> getErrori()
	{
		return errori;
	}

	public void setErrori(List<Errore> errori)
	{
		this.errori = errori;
	}

}
