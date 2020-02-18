/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.utility;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class WSO2HandlerResolver implements HandlerResolver {
	private String tokenRenewalUrl;
	private String consumerKey;
	private String consumerSecret;

	public String getTokenRenewalUrl() {
		return tokenRenewalUrl;
	}

	public void setTokenRenewalUrl(String tokenRenewalUrl) {
		this.tokenRenewalUrl = tokenRenewalUrl;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Handler> getHandlerChain(PortInfo portInfo) {
		List<Handler> chain = new ArrayList<Handler>();

		chain.add(new WSO2SOAPHandler(tokenRenewalUrl, consumerKey, consumerSecret));

		return chain;
	}

}
