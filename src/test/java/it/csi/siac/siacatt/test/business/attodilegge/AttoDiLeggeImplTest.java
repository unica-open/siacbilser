/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.attodilegge;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaLeggi;
import it.csi.siac.siacbilser.business.service.attodilegge.AggiornaAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.CancellaAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.InserisceAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.RicercaAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.RicercaPuntualeAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.RicercaTipiAttoDiLeggeService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class AttoDiLeggeImplDLTest.
 */
public class AttoDiLeggeImplTest extends BaseJunit4TestCase
{
	
	
	/** The inserisce atto di legge service. */
	@Autowired
	private InserisceAttoDiLeggeService inserisceAttoDiLeggeService;
	
	/** The ricerca atto di legge service. */
	@Autowired
	private RicercaAttoDiLeggeService ricercaAttoDiLeggeService;
	
	/** The aggiorna atto di legge service. */
	@Autowired
	private AggiornaAttoDiLeggeService aggiornaAttoDiLeggeService;
	
	
	/** The cancella atto di legge service. */
	@Autowired
	private CancellaAttoDiLeggeService cancellaAttoDiLeggeService;
	
	/** The ricerca tipi atto di legge service. */
	@Autowired
	private RicercaTipiAttoDiLeggeService ricercaTipiAttoDiLeggeService;
	
	/** The ricerca puntuale atto di legge service. */
	@Autowired
	private RicercaPuntualeAttoDiLeggeService ricercaPuntualeAttoDiLeggeService;
	
	//private GestioneCapitoloUscitaPrevisione gestioneCapitoloUscitaPrevisione; //Non più necessario
	
	

	/*@Before //Non più necessario
	public void setUp()
	{
		gestioneCapitoloUscitaPrevisioneImpl = (GestioneCapitoloUscitaPrevisione) applicationContext
				.getBean("gestioneCapitoloUscitaPrevisioneImpl");
	}*/

	/**
	 * Test create.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCreate() throws Throwable
	{
		final String methodName = "testCreate";

		try
		{

			//Modificare ogni volta i parametri!
			AttoDiLegge attoDiLegge = getAttoDiLegge();
			attoDiLegge.setNumero(100);
			attoDiLegge.setTipoAtto(getTipoAtto3());
			Ente ente = getEnteTest();

			Richiedente richiedente = getRichiedenteByProperties("consip","regp");
			
			InserisceAttoDiLegge req = new InserisceAttoDiLegge();
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			req.setAttoDiLegge(attoDiLegge);
			req.setDataOra(new Date());
			req.setEnte(ente);
			req.setRichiedente(richiedente);
			
			InserisceAttoDiLeggeResponse res = inserisceAttoDiLeggeService.executeService(req);
			
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			log.info(methodName, "inserito id = " + res.getAttoDiLeggeInserito().getUid());

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test create esistente.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCreateEsistente() throws Throwable
	{
		final String methodName = "testCreateEsistente";
		
		try
		{
			
			//Modificare ogni volta i parametri!
			AttoDiLegge attoDiLegge = getAttoDiLegge();
			attoDiLegge.setComma("inesistente");
			attoDiLegge.setAnno(2013);
			attoDiLegge.setNumero(100);
			attoDiLegge.setTipoAtto(getTipoAtto1());
			Ente ente = getEnteTest();
			
			Richiedente richiedente = getRichiedenteByProperties("consip","regp");
			
			InserisceAttoDiLegge req = new InserisceAttoDiLegge();
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			req.setAttoDiLegge(attoDiLegge);
			req.setDataOra(new Date());
			req.setEnte(ente);
			req.setRichiedente(richiedente);
			
			InserisceAttoDiLeggeResponse res = inserisceAttoDiLeggeService.executeService(req);
			
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			log.info(methodName, "inserito id = " + res.getAttoDiLeggeInserito().getUid());
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test create movimento interno.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCreateMovimentoInterno() throws Throwable
	{
		final String methodName = "testCreateMovimentoInterno";
		try
		{
			
			//Modificare ogni volta i parametri!
			AttoDiLegge attoDiLegge = getAttoDiLegge();
//			attoDiLegge.setNumero(100);
			attoDiLegge.setTipoAtto(getTipoAtto3());
			Ente ente = getEnteTest();
			
			Richiedente richiedente = getRichiedenteByProperties("consip","regp");
			
			InserisceAttoDiLegge req = new InserisceAttoDiLegge();
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			req.setAttoDiLegge(attoDiLegge);
			req.setDataOra(new Date());
			req.setEnte(ente);
			req.setRichiedente(richiedente);
			
			InserisceAttoDiLeggeResponse res = inserisceAttoDiLeggeService.executeService(req);
			
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			log.info(methodName, "inserito id = " + res.getAttoDiLeggeInserito().getUid());
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test update semplice.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testUpdateSemplice() throws Throwable
	{
		final String methodName = "testUpdateSemplice";
		try
		{
			
			
			AttoDiLegge attoDiLegge = getAttoDiLegge();
			Ente ente = getEnteTest();
			
			
			AggiornaAttoDiLegge req = new AggiornaAttoDiLegge();
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			attoDiLegge.setUid(22);
//			attoDiLegge.setNumero(1);
			attoDiLegge.setArticolo("uno Art.Mod");
			attoDiLegge.setComma("uno Comma.Mod");
			attoDiLegge.setPunto("uno Punto.Mod");
			attoDiLegge.setTipoAtto(getTipoAtto1());
			req.setAttoDiLegge(attoDiLegge);
			req.setDataOra(new Date());
			req.setEnte(ente);
			
			
			
			AggiornaAttoDiLeggeResponse res = aggiornaAttoDiLeggeService.executeService(req);
			
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			log.info(methodName, "inserito id = " + res.getAttoDiLeggeAggiornato().getUid());
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test update con tipo atto2.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testUpdateConTipoAtto2() throws Throwable
	{
		final String methodName = "testUpdateConTipoAtto2";
		try
		{
			
			
			AttoDiLegge attoDiLegge = getAttoDiLegge();
			Ente ente = getEnteTest();
			
			Richiedente richiedente = getRichiedenteByProperties("consip","regp");
			
			AggiornaAttoDiLegge req = new AggiornaAttoDiLegge();
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			attoDiLegge.setUid(2);
			attoDiLegge.setNumero(1);
			attoDiLegge.setArticolo("Ar:.Mod");
			attoDiLegge.setComma("Comma:.Mod");
			attoDiLegge.setPunto("Punto:.Mod");
			attoDiLegge.setTipoAtto(getTipoAtto2());
			req.setAttoDiLegge(attoDiLegge);
			req.setDataOra(new Date());
			req.setEnte(ente);
			req.setRichiedente(richiedente);
			
			AggiornaAttoDiLeggeResponse res = aggiornaAttoDiLeggeService.executeService(req);
			
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			log.info(methodName, "inserito id = " + res.getAttoDiLeggeAggiornato().getUid());
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	

	/**
	 * Test cancella.
	 */
	@Test
	public void testCancella(){
		final String methodName = "testCancella";
		
		AttoDiLegge attoDiLegge = getAttoDiLegge();
		Ente ente = getEnteTest();
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		
		CancellaAttoDiLegge req = new CancellaAttoDiLegge();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		attoDiLegge.setUid(2);
		req.setAttoDiLegge(attoDiLegge);
		req.setDataOra(new Date());
		req.setEnte(ente);
		req.setRichiedente(richiedente);
		
		CancellaAttoDiLeggeResponse res = cancellaAttoDiLeggeService.executeService(req);
		
		assertEquals("COR_ERR_0024", res.getErrori().get(0).getCodice());
		
		log.info(methodName, "esito: "+res.getEsito());
		log.info(methodName, "errori: "+res.getErrori());
	}
	
	
	/**
	 * Test ricerca trovato.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaTrovato() throws Throwable
	{
		final String methodName = "testRicercaTrovato";

		try
		{

			
			RicercaAttoDiLegge ricerca = new RicercaAttoDiLegge();
			RicercaLeggi ricercaLeggi = new RicercaLeggi();
			ricercaLeggi.setAnno(2013);
//			ricercaLeggi.setArticolo("articolo modE");
//			ricercaLeggi.setComma("comma MODE");
			ricercaLeggi.setNumero(2);
			TipoAtto tipoAtto = new TipoAtto();
			tipoAtto.setUid(1);
			ricercaLeggi.setTipoAtto(tipoAtto);
//			ricercaLeggi.setPunto("PUNTO moDEEE");
			ricerca.setAttoDiLegge(ricercaLeggi);
//			ricercaLeggi.setTipoAtto("codice");
			Ente ente = getEnteTest();

			Richiedente richiedente = getRichiedenteByProperties("consip","regp");
			
			RicercaAttoDiLegge req = new RicercaAttoDiLegge();
			req.setAttoDiLegge(ricercaLeggi);
			req.setEnte(ente);
			req.setRichiedente(richiedente);
			
			RicercaAttoDiLeggeResponse res = ricercaAttoDiLeggeService.executeService(req);
			
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			assertNotNull(res.getAttiTrovati());
			assertTrue(res.getAttiTrovati().size()>0);
			log.info(methodName, "inserito id = " + res.getAttiTrovati().get(0));

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test ricerca puntuale.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaPuntuale() throws Throwable
	{
		final String methodName = "testRicercaPuntuale";
		
		try
		{
			RicercaAttoDiLegge ricerca = new RicercaAttoDiLegge();
			RicercaLeggi ricercaLeggi = new RicercaLeggi();
			ricercaLeggi.setUid(28);
			ricerca.setAttoDiLegge(ricercaLeggi);
			
			RicercaAttoDiLegge req = new RicercaAttoDiLegge();
			req.setAttoDiLegge(ricercaLeggi);
			req.setEnte(getEnteTest());
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			
			RicercaAttoDiLeggeResponse res = ricercaPuntualeAttoDiLeggeService.executeService(req);
			
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			log.info(methodName, "inserito id = " + res.getAttiTrovati().get(0));
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * Test tipi atto.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testTipiAtto() throws Throwable
	{
		final String methodName = "testTipiAtto";
		try
		{
			
			
			RicercaTipiAttoDiLegge req = new RicercaTipiAttoDiLegge();
			req.setEnte(getEnteTest());
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			
			RicercaTipiAttoDiLeggeResponse res = ricercaTipiAttoDiLeggeService.executeService(req);
			
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			for (TipoAtto ta : res.getElencoTipi())
				log.info(methodName, "trovato tipo atto legge = " + ta);
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Gets the atto di legge.
	 *
	 * @return the atto di legge
	 */
	protected AttoDiLegge getAttoDiLegge() {
		AttoDiLegge attoDiLegge = new AttoDiLegge();
		attoDiLegge.setAnno(2013);
		attoDiLegge.setArticolo("articolo");
		attoDiLegge.setComma("comma");
		//attoDiLegge.setDataCreazione(new Date());
		attoDiLegge.setNumero(1);
		attoDiLegge.setPunto("punto");
		attoDiLegge.setStato(StatoEntita.IN_LAVORAZIONE);
		
		getTipoAtto1();
		
		return attoDiLegge;
	}


	/**
	 * Gets the tipo atto1.
	 *
	 * @return the tipo atto1
	 */
	private TipoAtto getTipoAtto1() {
		
		TipoAtto tipoAtto = new TipoAtto();
		
		tipoAtto.setUid(1);
		
		return tipoAtto;
	}
	
	
	/**
	 * Gets the tipo atto2.
	 *
	 * @return the tipo atto2
	 */
	private TipoAtto getTipoAtto2() {
		
		TipoAtto tipoAtto = new TipoAtto();
		
		tipoAtto.setUid(2);
		
		return tipoAtto;
	}
	
	
	/**
	 * Gets the tipo atto3.
	 *
	 * @return the tipo atto3
	 */
	private TipoAtto getTipoAtto3() {
		
		TipoAtto tipoAtto = new TipoAtto();
		
		tipoAtto.setUid(3);
		
		return tipoAtto;
	}



}
