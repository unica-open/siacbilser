/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscitagestione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDocumentiCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaImpegniCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaSinteticaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.VerificaAnnullabilitaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.VerificaEliminabilitaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.utility.CompareOperator;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

// TODO: Auto-generated Javadoc
/**
 * The Class GestioneCapitoloUscitaGestioneImplTest.
 */
public class GestioneCapitoloUscitaGestioneImplTest extends BaseJunit4TestCase
{
	
	
	/** The ricerca documenti capitolo uscita gestione service. */
	@Autowired
	private RicercaDocumentiCapitoloUscitaGestioneService ricercaDocumentiCapitoloUscitaGestioneService;
	
	/** The ricerca impegni capitolo uscita gestione service. */
	@Autowired
	private RicercaImpegniCapitoloUscitaGestioneService ricercaImpegniCapitoloUscitaGestioneService;
	
	@Autowired
	private RicercaSinteticaCapitoloUscitaGestioneService ricercaSinteticaCapitoloUscitaGestioneService; 
	
	
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;

	
	/**
	 * Gets the capitolo uscita gestione.
	 *
	 * @return the capitolo uscita gestione
	 */
	protected CapitoloUscitaGestione getcapitoloUscitaGestione() {
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setAnnoCapitolo(2021);
		capitoloUscitaGestione.setNumeroCapitolo(100010);
		capitoloUscitaGestione.setNumeroArticolo(0);
		return capitoloUscitaGestione;
	}

	
	/**
	 * Test ricerca documenti capitolo uscita gestione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaDocumentiCapitoloUscitaGestione() throws Throwable
	{

		try
		{
			RicercaDocumentiCapitoloUscitaGestione req = new RicercaDocumentiCapitoloUscitaGestione();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			req.setEnte(getEnteTest());
			req.setBilancio(getBilancioTest());
			req.setCapitoloUscitaGestione(getcapitoloUscitaGestione());
			req.setDataOra(new Date());
					
			RicercaDocumentiCapitoloUscitaGestioneResponse res = ricercaDocumentiCapitoloUscitaGestioneService.executeService(req);

			assertFalse("Esito Negativo", res.isFallimento());
			assertNotNull("Lista NULLA! ",res.getListaDocumenti());
			assertTrue("La lista non è vuota",res.getListaDocumenti().isEmpty());
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test ricerca impegni capitolo uscita gestione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaImpegniCapitoloUscitaGestione() throws Throwable
	{

		try
		{
			RicercaImpegniCapitoloUscitaGestione req = new RicercaImpegniCapitoloUscitaGestione();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			req.setEnte(getEnteTest());
			req.setBilancio(getBilancioTest());
			req.setCapitoloUscitaGestione(getcapitoloUscitaGestione());
			req.setDataOra(new Date());
					
			RicercaImpegniCapitoloUscitaGestioneResponse res = ricercaImpegniCapitoloUscitaGestioneService.executeService(req);
			
			assertFalse("Esito Negativo", res.isFallimento());
			assertNotNull("Lista NULLA! ",res.getListaImpegni());
			assertTrue("La lista non è vuota",res.getListaImpegni().isEmpty());
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	
	@Test 
	public void testRicercaSintetica() {

		RicercaSinteticaCapitoloUscitaGestione req = new RicercaSinteticaCapitoloUscitaGestione();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		req.setCalcolaTotaleImporti(Boolean.TRUE);
		req.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.PROGRAMMA, TipologiaClassificatore.MACROAGGREGATO,
				TipologiaClassificatore.CDC, TipologiaClassificatore.CDR, TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_V,
				TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_I));
		
		RicercaSinteticaCapitoloUGest rs = new RicercaSinteticaCapitoloUGest();
		rs.setAnnoEsercizio(Integer.valueOf(2021));
//		rs.setCodiceMissione("19");
//		rs.setCodiceRicorrenteSpesa("4");
		req.setRicercaSinteticaCapitoloUGest(rs);
		rs.setNumeroArticolo(0);
		rs.setNumeroCapitolo(100011);
		rs.setCapitoliIndicatiPerPrevisioneImpegnato(Boolean.TRUE);
	
		RicercaSinteticaCapitoloUscitaGestioneResponse res = ricercaSinteticaCapitoloUscitaGestioneService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Autowired
	private VerificaAnnullabilitaCapitoloUscitaGestioneService verificaAnnullabilitaCapitoloUscitaGestioneService;
	@Test
	public void verificaAnnullabilitaCapitoloUscitaGestione() {
		
		
		VerificaAnnullabilitaCapitoloUscitaGestione req = new VerificaAnnullabilitaCapitoloUscitaGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteTest("RMNLSS", 1, 1);
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest(); //getBilancioTest(1, 2015);
		req.setBilancio(bilancio);
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		//capitoloUscitaGestione.setUid(31036);
		capitoloUscitaGestione.setAnnoCapitolo(2015);
		capitoloUscitaGestione.setNumeroCapitolo(1807);
		capitoloUscitaGestione.setNumeroArticolo(2559);
		capitoloUscitaGestione.setNumeroUEB(1);
		capitoloUscitaGestione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		//capitoloUscitaGestione.setUid(95536);
		req.setCapitolo(capitoloUscitaGestione);
		
		VerificaAnnullabilitaCapitoloUscitaGestioneResponse res = verificaAnnullabilitaCapitoloUscitaGestioneService.executeService(req);
		assertNotNull(res);
		//log.logXmlTypeObject(res, "RESPONSE");//log.logXmlTypeObject();
		
	}
	
	@Autowired
	private VerificaEliminabilitaCapitoloUscitaGestioneService verificaEliminabilitaCapitoloUscitaGestioneService;
	@Test
	public void verificaEliminabilitaCapitoloUscitaGestione() {
		
		
		VerificaEliminabilitaCapitoloUscitaGestione req = new VerificaEliminabilitaCapitoloUscitaGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteTest("RMNLSS", 1, 1);
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest(); //getBilancioTest(1, 2015);
		req.setBilancio(bilancio);
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		//capitoloUscitaGestione.setUid(31037);
		capitoloUscitaGestione.setAnnoCapitolo(2015);
		capitoloUscitaGestione.setNumeroCapitolo(1807);
		capitoloUscitaGestione.setNumeroArticolo(2559);
		capitoloUscitaGestione.setNumeroUEB(1);
		capitoloUscitaGestione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		//capitoloUscitaGestione.setUid(95536);
		req.setCapitoloUscitaGest(capitoloUscitaGestione);
		
		VerificaEliminabilitaCapitoloUscitaGestioneResponse res = verificaEliminabilitaCapitoloUscitaGestioneService.executeService(req);
		assertNotNull(res);
		//log.logXmlTypeObject(res, "RESPONSE");//log.logXmlTypeObject();
		
	}

	@Test
	public void TestQuery(){
		List<Integer> test = new ArrayList<Integer>();
		test.add(new Integer(45283));
//		BigDecimal liquidatoDaPrenotazione = capitoloUscitaGestioneDad.computeTotaleImportiDaPrenotazioneLiquidazioniNonAnnullateCapitoloByAnno(test, new Integer(2015), CompareOperator.EQUALS);
		BigDecimal impegnatoAnnoDaEserciziPrec = capitoloUscitaGestioneDad.computeTotaleImportiDaEserciziPrecMovimentiNonAnnullatiCapitoloByAnno(test, new Integer(2017), CompareOperator.EQUALS,new Integer(2017));
		assertNotNull(impegnatoAnnoDaEserciziPrec);
	}
	
}
