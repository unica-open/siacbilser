/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.responsehandler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Controlla che la ServieResponse NON abbia errore (vedi {@link ServiceResponse#hasErrori()}) e che 
 * NON si sia verificato un errrore specifico.
 * 
 * Questo tipo di check evita problemi per tutti i servizi che ritornano {@link Esito#SUCCESSO} pur avendo riscontrato degli errori.
 *
 * @param <ERES> the generic type
 * @author Domenico
 */
public class NotErroreResponseHandler<ERES extends ServiceResponse> extends ResponseHandler<ERES> {

	private String serviceName;
	private String[] codiciErrore;

	/**
	 * Instantiates a new success response handler.
	 *
	 * @param serviceName nome del servizio che chiamante.
	 * @param codiciErrore codici di errore che se verificati sono bloccanti
	 */
	public NotErroreResponseHandler(String serviceName, String... codiciErrore) {
		super();
		this.serviceName = serviceName;
		this.codiciErrore = codiciErrore;
	}



	@Override
	protected void handleResponse(ERES response) {
		checkServiceResponseErrore(response, codiciErrore);
	}
	
	
	
	/**
	 *  
	 * Esegue il check di una risposta di un servizio.
	 * Nel caso venga invocato un servizio di un altro modulo rispetto al chiamante è possibile passare la serviceResponse ottenuta a
	 * questo metodo di check che in caso si sia verificato un errore con il codice passato in input solleva l'eccezione al chiamante.
	 *
	 * @author Domenico Lisi
	 * @param externalServiceResponse the external service response
	 * @param codiciErrore the codici errore
	 */
	public void checkServiceResponseErrore(ERES externalServiceResponse, String... codiciErrore) {
		if((externalServiceResponse.hasErrori() && codiciErrore.length == 0)
				|| externalServiceResponse.verificatoErrore(codiciErrore)) {
			//res.addErrori(externalServiceResponse.getErrori());
			String externalServiceName = getServiceName(externalServiceResponse);			
			throw new ExecuteExternalServiceException("\nEsecuzione servizio interno " + externalServiceName + " richiamato da "+ getServiceName() + " terminata con errori." 
					+ "\nErrori riscontrati da "+externalServiceName+": {"+externalServiceResponse.getDescrizioneErrori().replaceAll("\n", "\n\t")+"}."
					+ "\nErrori da sollevare per "+getServiceName()+": " + ToStringBuilder.reflectionToString(codiciErrore, ToStringStyle.SIMPLE_STYLE)+ ". "
					, externalServiceResponse.getErrori());
		}
	}
	
	
	
	/**
	 * Gets the service name.
	 *
	 * @param externalServiceResponse the external service response
	 * @return the service name
	 */
	protected String getServiceName(ERES externalServiceResponse) {
		return externalServiceResponse.getClass().getSimpleName().replaceAll("(Response)$","")+"Service";
	}
	
	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName; //this.getClass().getSimpleName();
	}
	

}
