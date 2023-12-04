/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.utility;

import it.csi.wso2.apiman.oauth2.helper.OauthHelper;


/**
 * Helper per ottenere il token per l'accesso ai servizi wso2.
 * 
 * Estende {@link OauthHelper} aggiungendo il rinnovo automatico del token sul metodo
 * {{@link #getToken()}.
 * 
 * @author Domenico
 */
public class OauthHelperExt extends OauthHelper {

	long tokenTakenTime = 0;
	long tokenExpiresIn = 0;
	
	public OauthHelperExt(String oauthURL, String consumerKey, String consumerSecret) {
		super(oauthURL, consumerKey, consumerSecret);
	}
	
	/**
	 * Ottiene il token. In automatico se e' scaduto prima di restituirlo lo rinnova.
	 *
	 * @return il token, ad esempio "5DKvcgrxF40y0_OJlsA7uDbI3UEa";
	 * @see #isTokenValid()
	 */
	@Override
	public String getToken() {
		if(isTokenValid()) {
			return super.getToken();
		}
		
		synchronized (this) {
			if(isTokenValid()) {
				return super.getToken();
			}
			return this.getNewToken();
		}
	}

	/**
	 * Indica se il token non &egrave; ancora scaduto.
	 *
	 * @return true, se NON &egrave; scaduto (quindi valido)
	 */
	public boolean isTokenValid() {
		return tokenExpiresIn > 0 && (getTokenElapsedTime() - tokenExpiresIn) > 100; //tengo 100 ms di scarto.
	}

	private long getTokenElapsedTime() {
		long tokenElapsedTime = System.currentTimeMillis()-tokenTakenTime;
		return tokenElapsedTime;
	}
	
	@Override
	public String getNewToken() {
		String token = super.getNewToken();
		if(token != null){
			tokenTakenTime = System.currentTimeMillis();
			tokenExpiresIn = getExpires();
		} else {
			tokenTakenTime = 0;
			tokenExpiresIn = 0;
		}
		return token;
	}
	
}
