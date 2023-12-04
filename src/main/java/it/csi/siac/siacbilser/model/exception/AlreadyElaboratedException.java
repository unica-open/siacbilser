/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.model.exception;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;

/**
 * Eccezione rappresentante un
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 12/07/2017
 *
 */
public class AlreadyElaboratedException extends BusinessException {


	/** Per la serializzazione */
	private static final long serialVersionUID = 989127200503864030L;

	public AlreadyElaboratedException(Errore errore, Esito esito, boolean rollbackOnly) {
		super(errore, esito, rollbackOnly);
	}

	public AlreadyElaboratedException(Errore errore, Esito esito) {
		super(errore, esito);
	}

	public AlreadyElaboratedException(Errore errore) {
		super(errore);
	}

	public AlreadyElaboratedException(String msg, Esito esito, boolean rollbackOnly) {
		super(msg, esito, rollbackOnly);
	}

	public AlreadyElaboratedException(String msg, Esito esito) {
		super(msg, esito);
	}

	public AlreadyElaboratedException(String msg) {
		super(msg);
	}

}
