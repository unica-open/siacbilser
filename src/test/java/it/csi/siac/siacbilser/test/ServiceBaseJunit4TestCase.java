/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import junit.framework.TestCase;

// TODO: Auto-generated Javadoc
/**
 * Classe di base dei test junit:
 * - aggancia il contesto descritto tramite ContextConfiguration
 * - fa autowiring del contesto stesso
 * 
 * Le sottoclassi devono poi solo ottenere i bean in questo modo:
 * 
 * <pre>
 * 	&#64;Before
 *	public void setUp() {
 *		voceDiBilancioDao = (VoceDiBilancioDao) applicationContext
 *			.getBean("voceDiBilancioDaoBean");
 *	}
 * </pre>	
 * 	Oppure più semplicemente basta dichiarare il proprio bean con annotazione <code>&#64;Autowired</code>
 * 	(Domenico)
 * 
 * @author alagna
 * @version $Id: $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"/spring/bilServiceClientContext-test.xml", 
		})
public abstract class ServiceBaseJunit4TestCase extends TestCase {

	/** The log. */
	protected LogUtil log = new LogUtil(this.getClass());

	/** The application context. */
	@Autowired
	protected ApplicationContext applicationContext;

	/**
	 * Sets the application context.
	 *
	 * @param applicationContext the new application context
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	
	/**
	 * Test del marshall e unmarshall utile per verificare request e response .
	 *
	 * @param <T> the generic type
	 * @param o the o
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T marshallUnmarshall(T o) {
		return marshallUnmarshall(o, (Class<T>)o.getClass());
	}
	

	/**
	 * Test del marshall e unmarshall utile per verificare request e response .
	 *
	 * @param <T> the generic type
	 * @param o the o
	 * @param clazz the clazz
	 * @return the t
	 */
	protected static <T> T marshallUnmarshall(T o, Class<T> clazz) {
		String xml = JAXBUtility.marshall(o);
		T res = JAXBUtility.unmarshall(xml, clazz);
		return res;
	}
	
	
	
	/**
	 * Ottiene dei Parametri Paginazione di test.
	 * 
	 * @return i parametri
	 */
	protected ParametriPaginazione getParametriPaginazioneTest() {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(10);
		parametriPaginazione.setNumeroPagina(0);
		
		return parametriPaginazione;
	}
	
	/**
	 * Ottiene il bilancio con id 1.
	 *
	 * @return the bilancio test
	 */
	protected Bilancio getBilancioTest() {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(1);
		bilancio.setAnno(2013);
		return bilancio;
	}
	
	/**
	 * Ottiene il bilancio 2014 con id 6.
	 *
	 * @return the bilancio test
	 */
	protected Bilancio getBilancio2014Test() {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(6);
		bilancio.setAnno(2014);
		return bilancio;
	}

	/**
	 * Ottiene l'ente con id 1.
	 *
	 * @return the ente test
	 */
	protected Ente getEnteTest()
	{
		Ente ente = new Ente();
		ente.setUid(1);
		//ente.setNome("mio nome di prova");
		return ente;
	}

	/**
	 * Ottiene un richiedente di test.
	 *
	 * @return the richiedente test
	 */
	protected Richiedente getRichiedenteTest()
	{
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale("RMNLSS");
		richiedente.setOperatore(operatore);
		Account account = getAccountTest();
		richiedente.setAccount(account);
		return richiedente;
	}


	/**
	 * Gets the account test.
	 *
	 * @return the account test
	 */
	private Account getAccountTest() {
		Account account = new Account();
		account.setUid(1);
		account.setEnte(getEnteTest());
		return account;
	}
	
	
	

}
