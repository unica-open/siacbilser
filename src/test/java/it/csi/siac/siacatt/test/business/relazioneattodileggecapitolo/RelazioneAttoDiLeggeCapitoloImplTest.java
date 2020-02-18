/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.relazioneattodileggecapitolo;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.AggiornaRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.CancellaRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.InserisceRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.RicercaPuntualeRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.RicercaRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ric.RicercaAttiDiLeggeCapitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;

// TODO: Auto-generated Javadoc
/**
 * The Class RelazioneAttoDiLeggeCapitoloImplDLTest.
 */
public class RelazioneAttoDiLeggeCapitoloImplTest extends BaseJunit4TestCase
{
	
	
	/** The inserisce atto di legge service. */
	@Autowired
	private InserisceRelazioneAttoDiLeggeCapitoloService inserisceAttoDiLeggeService;
	
	/** The ricerca atto di legge service. */
	@Autowired
	private RicercaRelazioneAttoDiLeggeCapitoloService ricercaAttoDiLeggeService;
	
	/** The ricerca puntuale atto di legge service. */
	@Autowired
	private RicercaPuntualeRelazioneAttoDiLeggeCapitoloService ricercaPuntualeAttoDiLeggeService;
	
	/** The aggiorna atto di legge service. */
	@Autowired
	private AggiornaRelazioneAttoDiLeggeCapitoloService aggiornaAttoDiLeggeService;
	
	
	/** The cancella atto di legge service. */
	@Autowired
	private CancellaRelazioneAttoDiLeggeCapitoloService cancellaAttoDiLeggeService;
	

	/**
	 * Test create ok.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCreateOk() throws Throwable
	{
		final String methodName = "testCreateOk";
		try
		{
			
			InserisceRelazioneAttoDiLeggeCapitolo req = new InserisceRelazioneAttoDiLeggeCapitolo();
			
			req.setEnte(getEnteTest());
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			
			req.setAttoDiLegge(getAttoDiLegge());
			
			Capitolo<?, ?> capitolo = new CapitoloEntrataGestione();
			capitolo.setUid(12657);
			req.setCapitolo(capitolo);
			
			AttoDiLeggeCapitolo relazione = new AttoDiLeggeCapitolo();
			relazione.setDataFineFinanz(new Date());
			relazione.setDataInizioFinanz(new Date());
			relazione.setDescrizione("descrizione");
			relazione.setGerarchia("gerarchia");
			req.setAttoDiLeggeCapitolo(relazione);
			
			InserisceRelazioneAttoDiLeggeCapitoloResponse res = inserisceAttoDiLeggeService.executeService(req);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "esito: "+res.getAttoDiLeggeCapitolo());
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
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

			RicercaRelazioneAttoDiLeggeCapitolo req = new RicercaRelazioneAttoDiLeggeCapitolo();
			req.setEnte(getEnteTest());
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			RicercaAttiDiLeggeCapitolo ricercaAttiDiLeggeCapitolo = new RicercaAttiDiLeggeCapitolo();
//			ricercaAttiDiLeggeCapitolo.setAttoDiLegge(getAttoDiLegge());
			Capitolo<?, ?> capitolo = new CapitoloEntrataGestione();
			capitolo.setUid(1);
			Bilancio bilancio = new Bilancio();
			bilancio.setUid(1);
			ricercaAttiDiLeggeCapitolo.setBilancio(bilancio);
			ricercaAttiDiLeggeCapitolo.setCapitolo(capitolo);
			req.setRicercaAttiDiLeggeCapitolo(ricercaAttiDiLeggeCapitolo);
			
			RicercaRelazioneAttoDiLeggeCapitoloResponse res = ricercaAttoDiLeggeService.executeService(req);
			
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			log.info(methodName, "errori: "+res.getElencoAttiLeggeCapitolo());
			
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
			
			RicercaRelazioneAttoDiLeggeCapitolo req = new RicercaRelazioneAttoDiLeggeCapitolo();
			req.setEnte(getEnteTest());
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			RicercaAttiDiLeggeCapitolo ricercaAttiDiLeggeCapitolo = new RicercaAttiDiLeggeCapitolo();
			ricercaAttiDiLeggeCapitolo.setUid(16);
			req.setRicercaAttiDiLeggeCapitolo(ricercaAttiDiLeggeCapitolo);
			
			RicercaRelazioneAttoDiLeggeCapitoloResponse res = ricercaPuntualeAttoDiLeggeService.executeService(req);
			
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			log.info(methodName, "errori: "+res.getElencoAttiLeggeCapitolo());
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test aggiorna.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAggiorna() throws Throwable
	{
		final String methodName = "testAggiorna";
		AggiornaRelazioneAttoDiLeggeCapitolo req = new AggiornaRelazioneAttoDiLeggeCapitolo();
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAttoDiLegge(getAttoDiLegge());
		Capitolo<?, ?> capitolo = new CapitoloEntrataGestione();
		capitolo.setUid(1);
		req.setCapitolo(capitolo);
		AttoDiLeggeCapitolo relazione = new AttoDiLeggeCapitolo();
		relazione.setUid(3);
		relazione.setDataFineFinanz(new Date());
		relazione.setDataInizioFinanz(new Date());
		relazione.setDescrizione("descrizione MODIFICATA");
		relazione.setGerarchia("gerarchia MODIFICATA");
		req.setAttoDiLeggeCapitolo(relazione);
		
		AggiornaRelazioneAttoDiLeggeCapitoloResponse res = aggiornaAttoDiLeggeService.executeService(req);
		
		log.info(methodName, "AGGIORNA: " + res.getAttoDiLeggeCapitolo());
		
	}
	
	/**
	 * Test cancella.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCancella() throws Throwable
	{
		final String methodName = "testCancella";
		CancellaRelazioneAttoDiLeggeCapitolo req = new CancellaRelazioneAttoDiLeggeCapitolo();
		req.getAttoDiLeggeCapitolo();

		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AttoDiLeggeCapitolo relazione = new AttoDiLeggeCapitolo();
		relazione.setUid(3);
		relazione.setDataFineFinanz(new Date());
		relazione.setDataInizioFinanz(new Date());
		relazione.setDescrizione("descrizione MODIFICATA");
		relazione.setGerarchia("gerarchia MODIFICATA");
		req.setAttoDiLeggeCapitolo(relazione);
		
		CancellaRelazioneAttoDiLeggeCapitoloResponse res = cancellaAttoDiLeggeService.executeService(req);
		
		log.info(methodName, "CANCELLAZIONE: " + res.getAttoDiLeggeCapitolo());
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
		attoDiLegge.setTipoAtto(getTipoAtto());
		attoDiLegge.setUid(3);
		
		return attoDiLegge;
	}


	/**
	 * Gets the tipo atto.
	 *
	 * @return the tipo atto
	 */
	private TipoAtto getTipoAtto() {
		
		TipoAtto tipoAtto = new TipoAtto();
		
		tipoAtto.setCodice("codice");
		
		return tipoAtto;
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

}
