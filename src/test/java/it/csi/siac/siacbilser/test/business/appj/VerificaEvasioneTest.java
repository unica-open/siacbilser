/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.appj;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.appjwebsrv.business.EsitoServizio;
import it.csi.appjwebsrv.business.Evasioni;
import it.csi.appjwebsrv.business.WSInterface;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class OnereSpesaDLTest.
 */
public class VerificaEvasioneTest extends BaseJunit4TestCase {
	
	
	@Autowired
	private WSInterface wWSInterface;
	
	
	@Test
	public void verificaEvasione() {
		final String methodName = "verificaEvasione";
		Evasioni evasioni = new Evasioni();
		evasioni.setAnnoDocumento("2016");
		evasioni.setCodiceFornitore(123);
		EsitoServizio esitoServizio = wWSInterface.verificaEvasione(evasioni);
		log.info(methodName, "ESITO>"+esitoServizio.getEsito());
		log.info(methodName, ToStringBuilder.reflectionToString(esitoServizio,
				ToStringStyle.MULTI_LINE_STYLE));
	}
	
	

}
