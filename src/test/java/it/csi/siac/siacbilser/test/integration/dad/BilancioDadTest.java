/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.integration.dad;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.bilancio.RicercaDettaglioBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Ente;

// TODO: Auto-generated Javadoc
/**
 * The Class BilancioDadTest.
 */
public class BilancioDadTest 
		extends	BaseJunit4TestCase {

	/** The bilancio dad. */
	@Autowired
	BilancioDad bilancioDad;
	
	/**
	 * Test is fase esercizio provvisiorio.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testIsFaseEsercizioProvvisiorio()
			throws Throwable {
		final String methodName = "testIsFaseEsercizioProvvisiorio";
		try
		{
			Ente ente = new Ente();
			ente.setUid(1);
			bilancioDad.setEnteEntity(ente);
			boolean bool = bilancioDad.isFaseEsercizioProvvisiorio(2013);
			if (bool)
				log.info(methodName, "Il bilancio è in fase esercizio provvisorio");
			else
				log.info(methodName, "Il bilancio non è in fase esercizio provvisorio");
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	@Autowired
	private RicercaDettaglioBilancioService ricercaDettaglioBilancioService;
	
	@Test
	public void ricercaDettaglioBilancio() {
		
		StringBuilder sb = new StringBuilder();

		sb.append("<ricercaDettaglioBilancio>");
		sb.append("    <dataOra>2015-01-23T16:14:35.271+01:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <nome>Demo 21</nome>");
		sb.append("            <descrizione>Demo 21 - Città di Torino</descrizione>");
		sb.append("            <indirizzoMail>email</indirizzoMail>");
		sb.append("            <ente>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <gestioneLivelli>");
		sb.append("                    <entry>");
		sb.append("                        <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                        <value>GESTIONE_UEB</value>");
		sb.append("                    </entry>");
		sb.append("                </gestioneLivelli>");
		sb.append("                <nome>Città di Torino</nome>");
		sb.append("            </ente>");
		sb.append("        </account>");
		sb.append("        <operatore>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
		sb.append("            <cognome>AAAAAA00A11B000J</cognome>");
		sb.append("            <nome>Demo</nome>");
		sb.append("        </operatore>");
		sb.append("    </richiedente>");
		sb.append("    <chiaveBilancio>6</chiaveBilancio>");
		sb.append("</ricercaDettaglioBilancio>");
		
		RicercaDettaglioBilancio req = JAXBUtility.unmarshall(sb.toString(), RicercaDettaglioBilancio.class);
		RicercaDettaglioBilancioResponse res = ricercaDettaglioBilancioService.executeService(req);
		assertNotNull(res);
	}
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Test
	public void funcTest(){
		final String methodName = "funcTest";
		String sql = "SELECT * FROM fnc_siac_atto_amm_aggiorna_stato_movgest(:attoAmmId,:attoammStatCode,:isEsecutivo,:loginOperazione)";
		
		/*select * From siac.fnc_siac_atto_amm_aggiorna_stato_movgest (
				  36708,
				  'DEFINITIVO',
				'admin20170228'
				)
		*/
		
		Query query = entityManager.createNativeQuery(sql);
		
		
		query.setParameter("attoAmmId", 36708);
		query.setParameter("attoammStatCode", "DEFINITIVO");
		query.setParameter("isEsecutivo", true);
		query.setParameter("loginOperazione", "admin20170228jt");
		
		@SuppressWarnings("unchecked")
		List<Integer> result = query.getResultList();
		log.info(methodName, "returning result: "+result);
	}
	
	@Test
	public void testDoppiaGestione() {
		boolean bilancioDoppiaGestione = bilancioDad.isBilancioDoppiaGestione(getBilancioTest(105, 2015));
		log.debug("testDoppiaGestione", "bilancioDoppiaGestione? " + bilancioDoppiaGestione);	
		
	}
}
