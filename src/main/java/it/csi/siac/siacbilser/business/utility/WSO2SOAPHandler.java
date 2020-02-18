/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class WSO2SOAPHandler implements SOAPHandler<SOAPMessageContext> {
	private OauthHelperExt oauthHelperExt;

	public WSO2SOAPHandler(String tokenRenewalUrl, String consumerKey, String consumerSecret) {
		this.oauthHelperExt = new OauthHelperExt(tokenRenewalUrl, consumerKey, consumerSecret);
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean isOutbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (isOutbound) { //se e' una Request..
			@SuppressWarnings("unchecked")
			Map<String, List<String>> headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);

			if (headers == null) {
				headers = new HashMap<String, List<String>>();
			}

			headers.put("Authorization", Collections.singletonList("Bearer " + oauthHelperExt.getToken()));
			context.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		}

		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}

	@Override
	public void close(MessageContext context) {
		// Implementazione vuota
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

}
