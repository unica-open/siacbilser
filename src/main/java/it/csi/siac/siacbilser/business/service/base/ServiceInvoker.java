/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;


import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Interfaccia di esecuzione di un servizio generico.
 * 
 * @author Domenico
 *
 * @param <REQ> the request type
 * @param <RES> the response type
 */
public interface ServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse> {
	
	/**
	 * Invoke service.
	 *
	 * @param req the request for the service
	 * @return the response from the service
	 */
	public abstract RES invokeService(REQ req);

}
