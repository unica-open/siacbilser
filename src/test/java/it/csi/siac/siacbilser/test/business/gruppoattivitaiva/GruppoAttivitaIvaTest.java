/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.gruppoattivitaiva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAttivitaIvaLegateAGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.AggiornaGruppoAttivitaIvaEProrataService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.AggiornaGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.AggiornaProrataEChiusuraGruppoIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.EliminaGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.InserisceGruppoAttivitaIvaEProrataService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.InserisceGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.InserisceProrataEChiusuraGruppoIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.RicercaDettaglioAnnualizzataGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.RicercaDettaglioGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.RicercaGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.gruppoattivitaiva.RicercaSinteticaGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.integration.dad.DocumentoIvaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaProrataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaProrataEChiusuraGruppoIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.model.TipoAttivita;
import it.csi.siac.siacfin2ser.model.TipoChiusura;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaVTTest.
 */
public class GruppoAttivitaIvaTest extends BaseJunit4TestCase {
	
	
	/** The inserisce gruppo attivita iva service. */
	@Autowired
	private InserisceGruppoAttivitaIvaService inserisceGruppoAttivitaIvaService;
	
	/** The ricerca gruppo attivita iva service. */
	@Autowired
	private RicercaGruppoAttivitaIvaService ricercaGruppoAttivitaIvaService;
	
	/** The ricerca dettaglio gruppo attivita iva service. */
	@Autowired
	private RicercaDettaglioGruppoAttivitaIvaService ricercaDettaglioGruppoAttivitaIvaService;
	
	/** The aggiorna gruppo attivita iva service. */
	@Autowired
	private AggiornaGruppoAttivitaIvaService aggiornaGruppoAttivitaIvaService;
	
	/** The aggiorna prorata e chiusura gruppo iva service. */
	@Autowired
	private AggiornaProrataEChiusuraGruppoIvaService aggiornaProrataEChiusuraGruppoIvaService;
	
	/** The aggiorna gruppo attivita iva e prorata service. */
	@Autowired
	private AggiornaGruppoAttivitaIvaEProrataService aggiornaGruppoAttivitaIvaEProrataService;
	
	/** The inserisce prorata e chiusura gruppo iva service. */
	@Autowired
	private InserisceProrataEChiusuraGruppoIvaService inserisceProrataEChiusuraGruppoIvaService;
	
	/** The inserisce gruppo attivita iva e prorata service. */
	@Autowired
	private InserisceGruppoAttivitaIvaEProrataService inserisceGruppoAttivitaIvaEProrataService;
	
	/** The ricerca sintetica gruppo attivita iva service. */
	@Autowired
	private RicercaSinteticaGruppoAttivitaIvaService ricercaSinteticaGruppoAttivitaIvaService;
	
	/** The elimina gruppo attivita iva service. */
	@Autowired
	private EliminaGruppoAttivitaIvaService eliminaGruppoAttivitaIvaService;
	
	/** The ricerca attivita iva legate a gruppo attivita iva service. */
	@Autowired
	private RicercaAttivitaIvaLegateAGruppoAttivitaIvaService ricercaAttivitaIvaLegateAGruppoAttivitaIvaService;
	
	/** The ricerca dettaglio annualizzata gruppo attivita iva service. */
	@Autowired
	private RicercaDettaglioAnnualizzataGruppoAttivitaIvaService ricercaDettaglioAnnualizzataGruppoAttivitaIvaService;
	
	/** The documento iva dad. */
	@Autowired
	private DocumentoIvaDad documentoIvaDad;
	
	
	/**
	 * Inserisci gruppo attivita.
	 */
	@Test
	public void inserisciGruppoAttivita() {
			
		InserisceGruppoAttivitaIva req = new InserisceGruppoAttivitaIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		
		gruppo.setEnte(getEnteTest());
		gruppo.setCodice("codice gruppo bb");
		gruppo.setDescrizione("descrizione gruppo 2");
		gruppo.setTipoChiusura(TipoChiusura.TRIMESTRALE);
		
//		ProRataEChiusuraGruppoIva prorata = new ProRataEChiusuraGruppoIva();
//		prorata.setPercentualeProRata(new BigDecimal("12"));
//		prorata.setAnnoEsercizio(2013);
//		List<ProRataEChiusuraGruppoIva> listaProRataEChiusuraGruppoIva = new ArrayList<ProRataEChiusuraGruppoIva>();
//		listaProRataEChiusuraGruppoIva.add(prorata);
//		gruppo.setListaProRataEChiusuraGruppoIva(listaProRataEChiusuraGruppoIva);
		
//		AttivitaIva attivita = new AttivitaIva();
//		attivita.setUid(1);
//		AttivitaIva attivitaDue = new AttivitaIva();
//		attivitaDue.setUid(2);
//		List<AttivitaIva> lista = new ArrayList<AttivitaIva>();
//		lista.add(attivita);
//		lista.add(attivitaDue);
//		gruppo.setListaAttivitaIva(lista);
			
		
		req.setGruppoAttivitaIva(gruppo);
		
		InserisceGruppoAttivitaIvaResponse res = inserisceGruppoAttivitaIvaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Inserisci prorata.
	 */
	@Test
	public void inserisciProrata() {
			
		InserisceProrataEChiusuraGruppoIva req = new InserisceProrataEChiusuraGruppoIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		
		ProRataEChiusuraGruppoIva prorata = new ProRataEChiusuraGruppoIva();
		GruppoAttivitaIva gruppoAttivitaIva = new GruppoAttivitaIva();
		gruppoAttivitaIva.setUid(1);
		
		prorata.setPercentualeProRata(new BigDecimal("12"));
		prorata.setAnnoEsercizio(2013);
		prorata.setEnte(getEnteTest());
		prorata.setGruppoAttivitaIva(gruppoAttivitaIva);
		prorata.setPercentualeProRata(new BigDecimal("24"));
		
		req.setProRataEChiusuraGruppoIva(prorata);
		
		InserisceProrataEChiusuraGruppoIvaResponse res = inserisceProrataEChiusuraGruppoIvaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Inserisci gruppo attivita iva e prorata.
	 */
	@Test
	public void inserisciGruppoAttivitaIvaEProrata() {
			
		InserisceGruppoAttivitaIvaEProrata req = new InserisceGruppoAttivitaIvaEProrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		
		gruppo.setEnte(getEnteTest());
		gruppo.setCodice("GR_TST_ANNO_AI_2");
		gruppo.setDescrizione("Gruppo di test per l'annualizzazione e con le attivita iva, v2");
		gruppo.setTipoChiusura(TipoChiusura.MENSILE);
		gruppo.setAnnualizzazione(2015);
		
		List<ProRataEChiusuraGruppoIva> listaProRataEChiusuraGruppoIva = new ArrayList<ProRataEChiusuraGruppoIva>();
		
		ProRataEChiusuraGruppoIva prorataEChiusuraGruppoIva = new ProRataEChiusuraGruppoIva();
		prorataEChiusuraGruppoIva.setPercentualeProRata(new BigDecimal("1"));
		prorataEChiusuraGruppoIva.setAnnoEsercizio(2015);
		prorataEChiusuraGruppoIva.setEnte(getEnteTest());
		prorataEChiusuraGruppoIva.setIvaPrecedente(new BigDecimal("-1"));

		listaProRataEChiusuraGruppoIva.add(prorataEChiusuraGruppoIva);
		gruppo.setListaProRataEChiusuraGruppoIva(listaProRataEChiusuraGruppoIva);
		
		List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
		
		AttivitaIva attivitaIva1 = new AttivitaIva();
		attivitaIva1.setUid(1);
		
		AttivitaIva attivitaIva2 = new AttivitaIva();
		attivitaIva2.setUid(3);
		
		listaAttivitaIva.add(attivitaIva1);
		listaAttivitaIva.add(attivitaIva2);
		
		gruppo.setListaAttivitaIva(listaAttivitaIva);
		
		req.setGruppoAttivitaIva(gruppo);
		
		InserisceGruppoAttivitaIvaEProrataResponse res = inserisceGruppoAttivitaIvaEProrataService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Ricerca sintetica gruppo attivita.
	 */
	@Test
	public void ricercaSinteticaGruppoAttivita() {
			
		RicercaSinteticaGruppoAttivitaIva req = new RicercaSinteticaGruppoAttivitaIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setAnno(2013);
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		
		gruppo.setEnte(getEnteTest());
		//gruppo.setIvaPrecedente(new BigDecimal("16"));
		//gruppo.setCodice("codice gruppo senza legami");
		//gruppo.setDescrizione("descrizione gruppo 2");
		//gruppo.setTipoAttivita(tipoAttivita); ???
		
				
		req.setGruppoAttivitaIva(gruppo);
		
		RicercaSinteticaGruppoAttivitaIvaResponse res = ricercaSinteticaGruppoAttivitaIvaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Ricerca gruppo attivita.
	 */
	@Test
	public void ricercaGruppoAttivita() {
			
		RicercaGruppoAttivitaIva req = new RicercaGruppoAttivitaIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		gruppo.setEnte(getEnteTest());
		req.setGruppoAttivitaIva(gruppo);
		
		RicercaGruppoAttivitaIvaResponse res = ricercaGruppoAttivitaIvaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Ricerca dettaglio gruppo attivita.
	 */
	@Test
	public void ricercaDettaglioGruppoAttivita() {
			
		RicercaDettaglioGruppoAttivitaIva req = new RicercaDettaglioGruppoAttivitaIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		
		gruppo.setEnte(getEnteTest());
		gruppo.setUid(18);
		req.setGruppoAttivitaIva(gruppo);
		gruppo.setAnnualizzazione(2014);
		
		RicercaDettaglioGruppoAttivitaIvaResponse res = ricercaDettaglioGruppoAttivitaIvaService.executeService(req);

		assertNotNull(res);
	}
	
	
	/**
	 * Aggiorna gruppo attivita.
	 */
	@Test
	public void aggiornaGruppoAttivita() {
			
		AggiornaGruppoAttivitaIva req = new AggiornaGruppoAttivitaIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		
		gruppo.setEnte(getEnteTest());
		gruppo.setUid(16);
		gruppo.setCodice("GR_TST_ANNO_AI");
		gruppo.setDescrizione("Gruppo di test per l'annualizzazione e con le attivita iva - agg");
		gruppo.setTipoChiusura(TipoChiusura.ANNUALE);
		gruppo.setAnnualizzazione(2015);
		
		List<ProRataEChiusuraGruppoIva> listaProRataEChiusuraGruppoIva = new ArrayList<ProRataEChiusuraGruppoIva>();
		
		ProRataEChiusuraGruppoIva prorataEChiusuraGruppoIva = new ProRataEChiusuraGruppoIva();
		prorataEChiusuraGruppoIva.setPercentualeProRata(new BigDecimal("21"));
		prorataEChiusuraGruppoIva.setAnnoEsercizio(2015);
		prorataEChiusuraGruppoIva.setEnte(getEnteTest());
		prorataEChiusuraGruppoIva.setIvaPrecedente(new BigDecimal("-4"));

		listaProRataEChiusuraGruppoIva.add(prorataEChiusuraGruppoIva);
		gruppo.setListaProRataEChiusuraGruppoIva(listaProRataEChiusuraGruppoIva);
		
		List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
		
		AttivitaIva attivitaIva1 = new AttivitaIva();
		attivitaIva1.setUid(1);
		
		AttivitaIva attivitaIva2 = new AttivitaIva();
		attivitaIva2.setUid(3);
		
		listaAttivitaIva.add(attivitaIva1);
		listaAttivitaIva.add(attivitaIva2);
		
		gruppo.setListaAttivitaIva(listaAttivitaIva);
		
		req.setGruppoAttivitaIva(gruppo);
		
		AggiornaGruppoAttivitaIvaResponse res = aggiornaGruppoAttivitaIvaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Aggiorna prorata e chiusura gruppo iva.
	 */
	@Test
	public void aggiornaProrataEChiusuraGruppoIva() {
			
		AggiornaProrataEChiusuraGruppoIva req = new AggiornaProrataEChiusuraGruppoIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ProRataEChiusuraGruppoIva prorata = new ProRataEChiusuraGruppoIva();
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		gruppo.setUid(5);
		
		prorata.setEnte(getEnteTest());
		prorata.setUid(7);
		prorata.setAnnoEsercizio(2014);
		prorata.setGruppoAttivitaIva(gruppo);
		
		
		req.setProRataEChiusuraGruppoIva(prorata);
		
		AggiornaProrataEChiusuraGruppoIvaResponse res = aggiornaProrataEChiusuraGruppoIvaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Aggiorna gruppo attivita iva e prorata.
	 */
	@Test
	public void aggiornaGruppoAttivitaIvaEProrata() {
		
		AggiornaGruppoAttivitaIvaEProrata req = new AggiornaGruppoAttivitaIvaEProrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		
		gruppo.setUid(18);
		gruppo.setEnte(getEnteTest());
		gruppo.setCodice("GR_TST_ANNO_AI_2");
		gruppo.setDescrizione("Gruppo di test per l'annualizzazione e con le attivita iva, v2 -agg");
		gruppo.setTipoChiusura(TipoChiusura.ANNUALE);
		gruppo.setTipoAttivita(TipoAttivita.PROMISCUA);
		gruppo.setAnnualizzazione(2014);
		
		List<ProRataEChiusuraGruppoIva> listaProRataEChiusuraGruppoIva = new ArrayList<ProRataEChiusuraGruppoIva>();
		
		ProRataEChiusuraGruppoIva prorataEChiusuraGruppoIva = new ProRataEChiusuraGruppoIva();
		prorataEChiusuraGruppoIva.setPercentualeProRata(new BigDecimal("10"));
		prorataEChiusuraGruppoIva.setAnnoEsercizio(2014);
		prorataEChiusuraGruppoIva.setEnte(getEnteTest());
		prorataEChiusuraGruppoIva.setIvaPrecedente(new BigDecimal("-20"));
	
		listaProRataEChiusuraGruppoIva.add(prorataEChiusuraGruppoIva);
		gruppo.setListaProRataEChiusuraGruppoIva(listaProRataEChiusuraGruppoIva);
		
		List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
		
		AttivitaIva attivitaIva1 = new AttivitaIva();
		attivitaIva1.setUid(1);
		
		AttivitaIva attivitaIva2 = new AttivitaIva();
		attivitaIva2.setUid(3);
		
		listaAttivitaIva.add(attivitaIva1);
		listaAttivitaIva.add(attivitaIva2);
		
		gruppo.setListaAttivitaIva(listaAttivitaIva);
		
		req.setGruppoAttivitaIva(gruppo);
		
		AggiornaGruppoAttivitaIvaEProrataResponse res = aggiornaGruppoAttivitaIvaEProrataService.executeService(req);
	
		assertNotNull(res);
}
	
	/**
	 * Elimina gruppo attivita.
	 */
	@Test
	public void eliminaGruppoAttivita() {
			
		EliminaGruppoAttivitaIva req = new EliminaGruppoAttivitaIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
	
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		gruppo.setUid(6);
		gruppo.setEnte(getEnteTest());
		req.setGruppoAttivitaIva(gruppo);
		
		EliminaGruppoAttivitaIvaResponse res = eliminaGruppoAttivitaIvaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Ricerca attivita per gruppo attivita.
	 */
	@Test
	public void ricercaAttivitaPerGruppoAttivita() {
		
		RicercaAttivitaIvaLegateAGruppoAttivitaIva req = new RicercaAttivitaIvaLegateAGruppoAttivitaIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		gruppo.setUid(6);
		
		req.setGruppoAttivitaIva(gruppo);
		RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse res = ricercaAttivitaIvaLegateAGruppoAttivitaIvaService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca dettaglio annualizzata gruppo attivita.
	 */
	@Test
	public void ricercaDettaglioAnnualizzataGruppoAttivita() {
			
		RicercaDettaglioAnnualizzataGruppoAttivitaIva req = new RicercaDettaglioAnnualizzataGruppoAttivitaIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		
		gruppo.setEnte(getEnteTest());
		gruppo.setUid(18);
		req.setGruppoAttivitaIva(gruppo);
		
		RicercaDettaglioAnnualizzataGruppoAttivitaIvaResponse res = ricercaDettaglioAnnualizzataGruppoAttivitaIvaService.executeService(req);

		assertNotNull(res);
	}
	
}