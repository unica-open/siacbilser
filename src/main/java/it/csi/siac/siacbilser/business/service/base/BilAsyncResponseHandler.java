/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import it.csi.siac.siaccommonser.business.service.base.AsyncResponseHandler;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Gestore della response di un servizio asincrono.
 * 
 * @author Domenico
 * 
 * @param <RES>
 */
public abstract class BilAsyncResponseHandler<RES extends ServiceResponse> extends AsyncResponseHandler<RES> {
	
	@Override
	protected void cleanThread() {
		ServiceExecutor.cleanThreadLocalCache();
	}

}
