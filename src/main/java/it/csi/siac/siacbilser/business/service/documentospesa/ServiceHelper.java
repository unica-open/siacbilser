/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

/**
 * Base per i IntegServiceHelper.
 * 
 * @author Domenico
 */
public abstract class ServiceHelper {
	

	protected ServiceExecutor serviceExecutor;
	protected Ente ente;
	protected Richiedente richiedente;

	public void init(ServiceExecutor serviceExecutor, Ente ente, Richiedente richiedente) {
		this.serviceExecutor = serviceExecutor;
		this.ente = ente;
		this.richiedente = richiedente;
	}
	
	
}