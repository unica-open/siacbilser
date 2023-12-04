/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.common;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 */
public class EnteTest extends BaseJunit4TestCase {
	
	/** The ente dad. */
	@Autowired
	private EnteDad enteDad;
	
	private LogSrvUtil l = new LogSrvUtil(getClass());
	
	@Test
	public void getEnteByUid() {
		Ente ente = enteDad.getEnteByUid(1);
		assertNotNull("Ente is null", ente);
		l.logXmlTypeObject(ente, "Ente");
	}
	
	@Test
	public void getSoggettoByEnte() {
		Ente ente = enteDad.getEnteByUid(1);
		Soggetto soggetto = enteDad.getSoggettoByEnte(ente);
		l.logXmlTypeObject(soggetto, "Soggetto");
		assertNotNull("Soggetto is null", soggetto);
	}
	
	@Test
	public void getIndirizzoSoggettoPrincipaleIvaByEnte() {
		final String methodName = "getIndirizzoSoggettoPrincipaleIvaByEnte";
		Ente ente = enteDad.getEnteByUid(1);
		IndirizzoSoggetto indirizzoSoggetto = enteDad.getIndirizzoSoggettoPrincipaleIvaByEnte(ente);
		l.logXmlTypeObject(indirizzoSoggetto, "IndirizzoSoggetto");
		l.debug(methodName, "IndirizzoSoggetto.indirizzoFormattato: " +
				(indirizzoSoggetto != null ? indirizzoSoggetto.getIndirizzoFormattato() : "null"));
		assertNotNull("IndirizzoPrincipale is null", indirizzoSoggetto);
	}
}
