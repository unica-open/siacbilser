/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model.exception;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;

/**
 * Eccezione rappresentante una mancata mappatura dell'ente sulla base dati.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/07/2015
 *
 */
public class UnmappedEnteException extends BusinessException {

	private static final long serialVersionUID = -1084978525059426422L;

	public UnmappedEnteException(Errore errore, Esito esito, boolean rollbackOnly) {
		super(errore, esito, rollbackOnly);
	}

	public UnmappedEnteException(Errore errore, Esito esito) {
		super(errore, esito);
	}

	public UnmappedEnteException(Errore errore) {
		super(errore);
	}

	public UnmappedEnteException(String msg, Esito esito, boolean rollbackOnly) {
		super(msg, esito, rollbackOnly);
	}

	public UnmappedEnteException(String msg, Esito esito) {
		super(msg, esito);
	}

	public UnmappedEnteException(String msg) {
		super(msg);
	}

}
