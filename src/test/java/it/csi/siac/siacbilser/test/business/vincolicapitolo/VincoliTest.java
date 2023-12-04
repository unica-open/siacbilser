/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.vincolicapitolo;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.vincolicapitolo.AggiornaVincoloCapitoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.AssociaCapitoloAlVincoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.InserisceAnagraficaVincoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaDettaglioVincoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaVincoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.ScollegaCapitoloAlVincoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.GenereVincolo;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.TipoVincoloCapitoli;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class VincoliDLTest.
 */
public class VincoliTest extends BaseJunit4TestCase {
	
	/** The ricerca vincolo service. */
	@Autowired
	private RicercaVincoloService ricercaVincoloService;
		
	/** The ricerca dettaglio vincolo service. */
	@Autowired
	private RicercaDettaglioVincoloService ricercaDettaglioVincoloService;
	
	/** The inserisce anagrafica vincolo service. */
	@Autowired
	private InserisceAnagraficaVincoloService inserisceAnagraficaVincoloService;
	
	/** The associa capitolo al vincolo service. */
	@Autowired
	private AssociaCapitoloAlVincoloService associaCapitoloAlVincoloService;
	

	/** The scollega capitolo al vincolo service. */
	@Autowired
	private ScollegaCapitoloAlVincoloService scollegaCapitoloAlVincoloService;
	
	/** The ricerca vincolo service. */
	@Autowired
	private AggiornaVincoloCapitoloService aggiornaVincoloCapitoloService;
	
	
	/**
	 * Associa vincolo.
	 */
	@Test
	public void associaVincolo() {
		AssociaCapitoloAlVincolo req = new AssociaCapitoloAlVincolo();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		VincoloCapitoli vincolo = new VincoloCapitoli();
		vincolo.setUid(9); //9:tipo Gestione
		vincolo.setEnte(getEnteTest());
		req.setVincolo(vincolo);
		
		Capitolo<?, ?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(40908820);
		req.setCapitolo(capitolo);
		
		AssociaCapitoloAlVincoloResponse res = associaCapitoloAlVincoloService.executeService(req);
		
		assertNotNull(res);
		//assertNotNull(res.getVincolo());
		
	}
	
	/**
	 * Scollega vincolo.
	 */
	@Test
	public void scollegaVincolo() {
		ScollegaCapitoloAlVincolo req = new ScollegaCapitoloAlVincolo();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		VincoloCapitoli vincolo = new VincoloCapitoli();
		vincolo.setUid(9); //9:tipo Gestione
		vincolo.setEnte(getEnteTest());
		req.setVincolo(vincolo);
		
		Capitolo<?, ?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(40908820);
		req.setCapitolo(capitolo);
		
		ScollegaCapitoloAlVincoloResponse res = scollegaCapitoloAlVincoloService.executeService(req);
		
		assertNotNull(res);
		//assertNotNull(res.getVincolo());
		
	}
		
	
	/**
	 * Inserisce anagrafica vincolo.
	 */
	@Test
	public void inserisceAnagraficaVincolo(){
		InserisceAnagraficaVincolo req = new InserisceAnagraficaVincolo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		VincoloCapitoli vincolo = new VincoloCapitoli();
		vincolo.setBilancio(getBilancioTest(16, 2015));
		vincolo.setEnte(req.getRichiedente().getAccount().getEnte());
		vincolo.setCodice("SIAC-5076");
		vincolo.setDescrizione("Test numero 1 SIAC-5076");
		vincolo.setFlagTrasferimentiVincolati(Boolean.TRUE);
		vincolo.setNote("Test");
		vincolo.setStatoOperativo(StatoOperativo.VALIDO);
		vincolo.setTipoVincoloCapitoli(TipoVincoloCapitoli.GESTIONE);
		vincolo.setGenereVincolo(create(GenereVincolo.class, 8));
		
		req.setVincolo(vincolo);
		
		InserisceAnagraficaVincoloResponse res = inserisceAnagraficaVincoloService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	
	/**
	 * Test ricerca vincolo.
	 */
	@Test
	public void testRicercaVincolo() {
		RicercaVincolo req = new RicercaVincolo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setBilancio(getBilancioTest(131, 2017));
		
		Vincolo vincolo = new Vincolo();
		vincolo.setEnte(req.getRichiedente().getAccount().getEnte());
//		vincolo.setTipoVincoloCapitoli(TipoVincoloCapitoli.PREVISIONE);
		//vincolo.setCodice("mio codice");
		
		
		
//		vincolo.setFlagTrasferimentiVincolati(Boolean.TRUE);
		
//		req.setTipiCapitolo(Arrays.asList(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE, TipoCapitolo.CAPITOLO_USCITA_PREVISIONE));
//		Capitolo c = new Capitolo(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
//		c.setUid(40908817);
//		req.setCapitolo(c);
		
		req.setVincolo(vincolo);
				
		RicercaVincoloResponse res = ricercaVincoloService.executeService(req);
		
		assertNotNull(res);
		assertNotNull(res.getVincoloCapitoli());
		

		
	}
	
	
	/**
	 * Test ricerca dettaglio vincolo.
	 */
	@Test
	public void testRicercaDettaglioVincolo() {
		RicercaDettaglioVincolo req = new RicercaDettaglioVincolo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setChiaveVincolo(3540);
		req.setBilancio(getBilancioTest(16, 2015));
		
		RicercaDettaglioVincoloResponse res = ricercaDettaglioVincoloService.executeService(req);
		
		assertNotNull(res);
	}

	@Test
	public void aggiornaVincoloCapitolo() {
		AggiornaVincoloCapitolo req = new AggiornaVincoloCapitolo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		Vincolo vincolo = create(Vincolo.class, 3540);
		req.setVincolo(vincolo);
		vincolo.setBilancio(getBilancioTest(16, 2015));
		vincolo.setCodice("SIAC-5076");
		vincolo.setDescrizione("Test numero 1 SIAC-5076");
		vincolo.setEnte(req.getRichiedente().getAccount().getEnte());
		vincolo.setFlagTrasferimentiVincolati(Boolean.TRUE);
		vincolo.setGenereVincolo(create(GenereVincolo.class, 2));
		vincolo.setNote("Test, aggiornato");
		vincolo.setStatoOperativo(StatoOperativo.VALIDO);
		vincolo.setTipoVincoloCapitoli(TipoVincoloCapitoli.GESTIONE);
		
		AggiornaVincoloCapitoloResponse res = aggiornaVincoloCapitoloService.executeService(req);
		assertNotNull(res);
	}

}
