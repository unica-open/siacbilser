/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.responsehandler;

import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Controlla che la {@link ServiceResponse} restituisca SUCCESSO oppure {@link ErroreCore#ENTITA_NON_TROVATA}.
 *
 * @param <ERES> the generic type
 * 
 * @author Domenico
 */
public class RicercaSuccessResponseHandler<ERES extends ServiceResponse> extends SuccessResponseHandler<ERES> {


	/**
	 * Instantiates a new success response handler.
	 *
	 * @param serviceName nome del servizio che chiamante.
	 */
	public RicercaSuccessResponseHandler(String serviceName) {
		super(serviceName, ErroreCore.ENTITA_NON_TROVATA.getCodice());
	}




	

}
