/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.responsehandler;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Controlla soltanto che la response non sia null non effettuando nessun altro tipo di controllo.
 * Viene utilizzato come default qualora non venga specificato un ResponseHandler 
 * nei vari metodi {@link ServiceExecutor#executeService(ServiceInvoker, ServiceRequest, ResponseHandler)}
 * 
 * @param <ERES> the generic type
 * 
 * @author Domenico
 */
public class NotNullResponseHandler<ERES extends ServiceResponse> extends ResponseHandler<ERES> {

	@Override
	protected void handleResponse(ERES response) {
		// TODO: Nessuna elaborazione?
	}

}
