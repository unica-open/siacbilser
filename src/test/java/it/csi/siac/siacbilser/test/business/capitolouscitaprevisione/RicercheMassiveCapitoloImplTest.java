/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscitaprevisione;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaSinteticaMassivaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaDettaglioMassivaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaSinteticaMassivaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercheMassiveCapitoloImplDLTest.
 */
public class RicercheMassiveCapitoloImplTest extends BaseJunit4TestCase
{
	
	/** The ricerca sintetica massiva capitolo uscita previsione service. */
	@Autowired
	private RicercaSinteticaMassivaCapitoloUscitaPrevisioneService ricercaSinteticaMassivaCapitoloUscitaPrevisioneService;
	
	/** The ricerca dettaglio massiva capitolo uscita previsione service. */
	@Autowired
	private RicercaDettaglioMassivaCapitoloUscitaPrevisioneService ricercaDettaglioMassivaCapitoloUscitaPrevisioneService;
	
	/** The ricerca sintetica massiva capitolo uscita gestione service. */
	@Autowired
	private RicercaSinteticaMassivaCapitoloUscitaGestioneService ricercaSinteticaMassivaCapitoloUscitaGestioneService;
	
	/**
	 * Test calcolo pagine.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testCalcoloPagine() throws Throwable{
		
		for (int i = 0; i<1000; i++)
			System.out.println(i / 25);
	}

	
	
	/**
	 * Test ricerca elenco massiva capitolo uscita previsione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaElencoMassivaCapitoloUscitaPrevisione() throws Throwable{


		try
		{
			RicercaSinteticaMassivaCapitoloUscitaPrevisione req = new RicercaSinteticaMassivaCapitoloUscitaPrevisione();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			ParametriPaginazione pp = new ParametriPaginazione();
			pp.setNumeroPagina(0);
			pp.setElementiPerPagina(6);
			
			//RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse res = null;
			
			while (true) {
			
				RicercaSinteticaCapitoloUPrev criteriRicerca = new RicercaSinteticaCapitoloUPrev();
				
//				criteriRicerca.setNumeroCapitolo(9999);
//				criteriRicerca.setDescrizioneCapitolo("pro"); //Trova 83 risultati distinti
				criteriRicerca.setDescrizioneCapitolo("agg"); //Trova 11 risultati, 8 aggregati
	
				criteriRicerca.setAnnoEsercizio(2013);
				
				req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);
				
	
				req.setParametriPaginazione(pp);
				
				RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse res = ricercaSinteticaMassivaCapitoloUscitaPrevisioneService.executeService(req);
				
				if (res.getCapitoli().size() == 0)
					break;
				
				ListaPaginata<CapitoloUscitaPrevisione> list = res.getCapitoli();
				
				log.debug("testRicercaElencoMassivaCapitoloUscitaPrevisione", "testRicercaSinteticaDescrizione - " + "PaginaCorrente: "+list.getPaginaCorrente() + //Numero pagina restituita
						" TotaleElementi: " + list.getTotaleElementi() + //Numero elementi trovati
						" TotalePagine: "+list.getTotalePagine()		 //Numero pagine trovate			
					);
			
				req.setPaginaRemote(res.getPaginaRemote());
				req.setPosizionePaginaRemote(res.getPosizionePaginaRemote());
				
				int numeroPagina = pp.getNumeroPagina();
				
				pp.setNumeroPagina(++numeroPagina);
			}
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	
	}
	
	/**
	 * Test ricerca dettaglio massiva capitolo uscita previsione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaDettaglioMassivaCapitoloUscitaPrevisione() throws Throwable{
		
		RicercaDettaglioMassivaCapitoloUscitaPrevisione req = new RicercaDettaglioMassivaCapitoloUscitaPrevisione();
		
		RicercaSinteticaCapitoloUPrev criteriRicerca = new RicercaSinteticaCapitoloUPrev();
		
		criteriRicerca.setAnnoCapitolo(2013);
		criteriRicerca.setAnnoEsercizio(2013);
		criteriRicerca.setNumeroCapitolo(9999);
		criteriRicerca.setNumeroArticolo(1);
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);
		
		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse res = ricercaDettaglioMassivaCapitoloUscitaPrevisioneService.executeService(req);
		
		System.out.println(res);
	}
	
	/**
	 * Test ricerca uscita gestione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaUscitaGestione() throws Throwable{


		try
		{
			RicercaSinteticaMassivaCapitoloUscitaGestione req = new RicercaSinteticaMassivaCapitoloUscitaGestione();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			ParametriPaginazione pp = new ParametriPaginazione();
			pp.setNumeroPagina(0);
			pp.setElementiPerPagina(3);
			
			//RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse res = null;
			
			while (true) {
			
				RicercaSinteticaCapitoloUGest criteriRicerca = new RicercaSinteticaCapitoloUGest();
				
//				criteriRicerca.setDescrizioneCapitolo("pro"); //Trova 83 risultati distinti
				criteriRicerca.setDescrizioneCapitolo("Fantasioso"); //Trova 11 risultati, 8 aggregati
	
				criteriRicerca.setAnnoEsercizio(2013);
				
				req.setRicercaSinteticaCapitoloUGest(criteriRicerca);
				
	
				req.setParametriPaginazione(pp);
				
				RicercaSinteticaMassivaCapitoloUscitaGestioneResponse res = ricercaSinteticaMassivaCapitoloUscitaGestioneService.executeService(req);
				
				if (res.getCapitoli().size() == 0)
					break;
				
				ListaPaginata<CapitoloUscitaGestione> list = res.getCapitoli();
				
				log.debug("testRicercaUscitaGestione", "testRicercaSinteticaDescrizione - " + "PaginaCorrente: "+list.getPaginaCorrente() + //Numero pagina restituita
						" TotaleElementi: " + list.getTotaleElementi() + //Numero elementi trovati
						" TotalePagine: "+list.getTotalePagine()		 //Numero pagine trovate			
					);
			
				req.setPaginaRemote(res.getPaginaRemote());
				req.setPosizionePaginaRemote(res.getPosizionePaginaRemote());
				
				int numeroPagina = pp.getNumeroPagina();
				
				pp.setNumeroPagina(++numeroPagina);
			}
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.test.BaseJunit4TestCase#getEnteTest()
	 */
	protected Ente getEnteTest()
	{
		Ente ente = new Ente();
		ente.setUid(1);
		//ente.setNome("mio nome di prova");
		return ente;
	}
	
	

//	@Test
//	public void testRicercaSintetica() throws Throwable
//	{
//
//		try
//		{
//			RicercaSinteticaCapitoloUscitaPrevisione req = new RicercaSinteticaCapitoloUscitaPrevisione();
//
//			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//			Ente ente = getEnteTest();
//			req.setEnte(ente);
//
//			RicercaSinteticaCapitoloUPrev criteriRicerca = new RicercaSinteticaCapitoloUPrev();
//
//			criteriRicerca.setAnnoEsercizio(2013);
//			criteriRicerca.setAnnoCapitolo(2013);
//			criteriRicerca.setNumeroCapitolo(17647);
////			criteriRicerca.setNumeroArticolo(1);
//			
//			
//			criteriRicerca.setCodicePianoDeiConti("E.9.02.03.00.000"); //trova E.9.02.03.04.000
//			
//			criteriRicerca.setCodiceStrutturaAmmCont("005"); //troverà "004" CDC
//			criteriRicerca.setCodiceTipoStrutturaAmmCont("CDR");
//			
//			criteriRicerca.setCodiceMissione("01"); //troverà codice programma "11"
//			
//			criteriRicerca.setCodiceTitoloUscita("1"); //troverà codice Macroaggregato "06"
//			
//			req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);
//			ParametriPaginazione pp = new ParametriPaginazione();
//			pp.setNumeroPagina(0);
//			pp.setElementiPerPagina(5);
//
//			req.setParametriPaginazione(pp);
//			/*gestioneCapitoloUscitaPrevisione.ricercaSinteticaCapitoloUscitaPrevisione(richiedente,
//					ente, criteriRicerca, null);*/
//			
//			RicercaSinteticaCapitoloUscitaPrevisioneResponse res = ricercaSinteticaCapitoloUscitaPrevisioneService.executeService(req);
//
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace();
//			throw e;
//		}
//	}
}
