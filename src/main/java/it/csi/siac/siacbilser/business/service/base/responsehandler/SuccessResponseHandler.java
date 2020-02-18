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
 * Controlla che la {@link ServiceResponse} restituisca SUCCESSO.
 * o che, anche se ha restituito {@link Esito.FALLIMENTO}, si si siano verificati solo alcuni codici specifici che si vuole gestire in seguito.
 *
 * @param <ERES> the generic type
 * 
 * @author Domenico
 */
public class SuccessResponseHandler<ERES extends ServiceResponse> extends ResponseHandler<ERES> {

	private String serviceName;
	private String[] codiciErroreDaEscludere;

	/**
	 * Instantiates a new success response handler.
	 *
	 * @param serviceName nome del servizio che chiamante.
	 * @param codiciErroreDaEscludere i codici di errore che sono considerati accettabili in caso di FALLIMENTO {@link ServiceResponse#isFallimento()} del servizio.
	 */
	public SuccessResponseHandler(String serviceName, String... codiciErroreDaEscludere) {
		super();
		this.serviceName = serviceName;
		this.codiciErroreDaEscludere = codiciErroreDaEscludere;
	}



	@Override
	protected void handleResponse(ERES response) {
		checkServiceResponseFallimento(response, this.codiciErroreDaEscludere);
	}
	
	
	
	/**
	 *  
	 * Esegue il check di una risposta di un servizio.
	 * Nel caso esito sia Fallimento e non si sia verificato nessuno degli errori passati nel parametro codiciErroreDaEscludere 
	 * viene sollevata l'eccezione.
	 *
	 * @author Domenico Lisi
	 * @param externalServiceResponse the external service response
	 * @param codiciErroreDaEscludere codici di errori per il quale NON sollevare l'eccezione
	 */
	public void checkServiceResponseFallimento(ERES externalServiceResponse, String... codiciErroreDaEscludere) {		
		if (externalServiceResponse.isFallimento() 
				&& !externalServiceResponse.verificatoErrore(codiciErroreDaEscludere)) {
			//res.addErrori(externalServiceResponse.getErrori());
			String externalServiceName = getServiceName(externalServiceResponse);
			throw new ExecuteExternalServiceException("\nEsecuzione servizio interno " + externalServiceName + " richiamato da "+ getServiceName() + " terminata con esito Fallimento." 
					+ "\nErrori riscontrati da "+externalServiceName+": {"+externalServiceResponse.getDescrizioneErrori().replaceAll("\n", "\n\t")+"}." 
					+ "\nErrori da escludere per "+getServiceName()+": " + ToStringBuilder.reflectionToString(codiciErroreDaEscludere, ToStringStyle.SIMPLE_STYLE)+ ". "
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
