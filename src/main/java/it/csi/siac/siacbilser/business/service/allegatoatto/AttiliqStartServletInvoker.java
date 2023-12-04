/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.business.utility.OauthHelperExt;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.wso2.apiman.oauth2.helper.OauthHelper;

/**
 * Componente per gestire l'invio degli allegati atti al Sistema Atti di Liquidazione.
 * 
 * @author Domenico Lisi
 */
public class AttiliqStartServletInvoker {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private String host;// = "tst-api-ent.ecosis.csi.it"; // "tst-wls9-01.csi.it";
	private int port;// = 80; //9130
	private String serviceUri;// = "/api/AttiliqStartWorkflow/1.0"; //"/attiliqbs/1.1/AttiliqStartServlet";
	
	private OauthHelperExt oauthHelperExt;
	
	private int timeoutSocket = 60000;
	private int timeoutConnection = 30000;
	
	
	/**
	 * Instantiates a new attiliq start servlet invoker.
	 *
	 * @param host the host
	 * @param port the port
	 * @param serviceUri the service uri
	 * @param tokenRenewalUri the token renewal uri
	 * @param consumerKey the consumer key
	 * @param consumerSecret the consumer secret
	 */
	public AttiliqStartServletInvoker(String host, int port, String serviceUri, String tokenRenewalUri, String consumerKey, String consumerSecret){
		this.host = host;
		this.port = port;
		this.serviceUri = serviceUri;
		
		this.oauthHelperExt = new OauthHelperExt(tokenRenewalUri, consumerKey, consumerSecret);
		
	}
	
	/**
	 * Instantiates a new attiliq start servlet invoker.
	 *
	 * @param host the host
	 * @param port the port
	 * @param serviceUri the service uri
	 * @param tokenRenewalUri the token renewal uri
	 * @param consumerKey the consumer key
	 * @param consumerSecret the consumer secret
	 * @param timeoutConnection the timeout connection
	 * @param timeoutSocket the timeout socket
	 */
	public AttiliqStartServletInvoker(String host, int port, String serviceUri, String tokenRenewalUri, String consumerKey, String consumerSecret, int timeoutConnection, int timeoutSocket){
		this(host,port, serviceUri,tokenRenewalUri,consumerKey,consumerSecret);
		this.timeoutConnection = timeoutConnection;
		this.timeoutSocket = timeoutSocket;
		
	}
	
	
	/**
	 * Invoca la Servlet di Attiliq.
	 *
	 * @param formData the form data
	 * @param report the report
	 * 
	 * @throws BusinessException in caso di errore.
	 */
	public void invoke(Map<String, String> formData, File report) {
		String methodName = "invokeAttiliqStartServlet";
		
		PostMethod method = new PostMethod("http://"+host+":"+port+"/"+serviceUri);
		method.getParams().setParameter(
				HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		
		method.setRequestHeader("Authorization", "Bearer "+getTokenOAuth());
		
		List<Part> parts = new ArrayList<Part>();
		for(Entry<String, String> e : formData.entrySet()){
			String key = e.getKey();
			String value = e.getValue();
			if(StringUtils.isNotBlank(value)){
				StringPart sp = new StringPart(key, value);
				parts.add(sp);
			} else {
				log.warn(methodName, "Il campo " + key + " non e' valorizzato. Viene scartato.");
			}
		}
		
		parts.add(new FilePart("documento1", new ByteArrayPartSource(report.getNome(), report.getContenuto())));
		
		log.warn(methodName, "ATTILIQ: dati passati al servizio");
		
		log.warn(methodName, report.getNome());
		
		log.warn(methodName, report.getContenuto());
		
		method.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), method.getParams()));
		
		
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setSoTimeout(timeoutSocket);
		httpClientParams.setConnectionManagerTimeout(timeoutConnection);
		HttpClient httpClien = new HttpClient(httpClientParams);
		
		int statusCode = -1;
		String result = null;
		try {
			statusCode = httpClien.executeMethod(method);

			result = new String(method.getResponseBody());
			log.info(methodName, "Result: [" +result+"]");
		} catch (HttpException e) {
			String msg = "HttpException nella trasmissione del file ad Atti Di Liquidazione. "+ e.getMessage();
			log.error(methodName, msg, e);
			throw new BusinessException(msg);
		} catch (IOException e) {
			String msg = "IOException nella trasmissione del file ad Atti Di Liquidazione. "+ (e!=null?e.getMessage():"");
			log.error(methodName, msg, e);
			throw new BusinessException(msg);
		} finally {
			method.releaseConnection();
		}
		
		
		if(statusCode != HttpStatus.SC_OK || result.startsWith("KO") || !"OK".equals(result)){
			String msg = "Errore nella trasmissione del file ad Atti Di Liquidazione. " + result.replaceFirst("KO", "").trim();
			log.error(methodName, "statusCode:" + statusCode + ". " + msg);
			log.error(methodName, "Risposta dalla Servlet: "+ result);
			throw new BusinessException(msg);
		}
		
		log.info(methodName, "Trasmissione andata a buon fine. Risposta dalla Servlet:"+ result);

	}
	

	/**
	 * Ottiene il token, se il token &egrave; scaduto lo rinnova.
	 * 
	 * @return the tokenOAuth
	 */
	private synchronized String getTokenOAuth() {
		String methodName = "getTokenOAuth";
		
		String token = oauthHelperExt.getToken();
		
		if(token==null) {
//			log.warn(methodName, "OauthHelper: ottenuto token null! Faccio un secondo tentativo... "); //sotto utilizza già executeWithRetry che di default fa 3 tentativi!
//			token = oauthHelperExt.getToken();
//			if(token == null) {
				String msg = "OauthHelper: impossibile ottenere il token.";
				log.error(methodName, msg);
				throw new IllegalStateException(msg);
//			}
		}
		
		log.debug(methodName, "Returning token: " + token + " [version: "+ OauthHelper.getVersion()+"]");
		
		return token;
	}
	
	
	
}



