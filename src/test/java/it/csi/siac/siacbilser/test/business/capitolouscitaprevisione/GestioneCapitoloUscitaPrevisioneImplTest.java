/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscitaprevisione;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.GestioneCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AggiornaCapitoloDiUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.InserisceCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaDettaglioCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaPuntualeCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

// TODO: Auto-generated Javadoc
/**
 * The Class GestioneCapitoloUscitaPrevisioneImplTest.
 */
public class GestioneCapitoloUscitaPrevisioneImplTest extends BaseJunit4TestCase
{
	
	
	/** The inserisce gestione capitolo uscita previsione service. */
	@Autowired
	private InserisceCapitoloUscitaPrevisioneService inserisceGestioneCapitoloUscitaPrevisioneService;

	/** The inserisce gestione capitolo uscita previsione service. */
	@Autowired
	private AggiornaCapitoloDiUscitaPrevisioneService aggiornaCapitoloDiUscitaPrevisioneService;

	/** The ricerca puntuale capitolo uscita previsione service. */
	@Autowired
	private RicercaPuntualeCapitoloUscitaPrevisioneService ricercaPuntualeCapitoloUscitaPrevisioneService;
	
	@Autowired
	private RicercaDettaglioCapitoloUscitaPrevisioneService ricercaDettaglioCapitoloUscitaPrevisioneService;
	
	
	/** The gestione capitolo uscita previsione. */
	private GestioneCapitoloUscitaPrevisione gestioneCapitoloUscitaPrevisione; //Non più necessario
	
	

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

		try
		{

			Bilancio bilancio = getBilancioTest();

			CapitoloUscitaPrevisione capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();

			capitoloUscitaPrevisione.setNumeroCapitolo(17643);
			capitoloUscitaPrevisione.setNumeroArticolo(0);
			capitoloUscitaPrevisione.setAnnoCapitolo(bilancio.getAnno());
			capitoloUscitaPrevisione
					.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);

			capitoloUscitaPrevisione.setDescrizione("TEST CUP " + new Date().toString());
			capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(2011);

			Ente ente = getEnteTest();

			Richiedente richiedente = getRichiedenteByProperties("consip","regp");

			ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
			elementoPianoDeiConti.setUid(5255);

			Macroaggregato macroaggregato = new Macroaggregato();
			macroaggregato.setUid(119);

			Programma programma = new Programma();
			programma.setUid(24);

			//StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
			//strutturaAmministrativoContabile.setUid(5256);

			ClassificazioneCofogProgramma classificazioneCofogProgramma = new ClassificazioneCofogProgramma();
			classificazioneCofogProgramma.setUid(5257);

			TipoFondo tipoFondo = null;
			TipoFinanziamento tipoFinanziamento = null;
			List<ClassificatoreGenerico> listaClassificatori = null;

			List<ImportiCapitoloUP> listaImporti = new ArrayList<ImportiCapitoloUP>();

			listaImporti.add(getImportiCapitolo(2013));
			listaImporti.add(getImportiCapitolo(2014));
			listaImporti.add(getImportiCapitolo(2015));
			
			
			
			
			InserisceCapitoloDiUscitaPrevisione req = new InserisceCapitoloDiUscitaPrevisione();
			req.setRichiedente(richiedente);
			req.setEnte(ente);
			req.setBilancio(bilancio);
			req.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);
			req.setTipoFondo(tipoFondo);
			req.setTipoFinanziamento(tipoFinanziamento);
			req.setClassificatoriGenerici(listaClassificatori);
			req.setElementoPianoDeiConti(elementoPianoDeiConti);
			//req.setStruttAmmContabile(strutturaAmministrativoContabile);
			req.setClassificazioneCofogProgramma(classificazioneCofogProgramma);
			req.setImportiCapitoloUP(listaImporti);
			req.setMacroaggregato(macroaggregato);
			req.setProgramma(programma);
			
			
			InserisceCapitoloDiUscitaPrevisioneResponse res = inserisceGestioneCapitoloUscitaPrevisioneService.executeService(req);

			/*InserisceCapitoloDiUscitaPrevisioneResponse res = gestioneCapitoloUscitaPrevisione
					.inserisceCapitoloUscitaPrevisione(richiedente, ente, bilancio,
							capitoloUscitaPrevisione, tipoFondo, tipoFinanziamento,
							listaClassificatori, elementoPianoDeiConti,
							strutturaAmministrativoContabile, classificazioneCofogProgramma,
							listaImporti, macroaggregato, programma);

			*/
			
			System.out.println("esito: "+res.getEsito());
			System.out.println("errori: "+res.getErrori());
			
			
			assertNotNull(res);
			
			CapitoloUscitaPrevisione ins = res.getCapitoloUPrevInserito();
			
			assertNotNull(ins);			

			System.out.println("inserito id = " + ins.getUid());

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.test.BaseJunit4TestCase#getBilancioTest()
	 */
//	protected Bilancio getBilancioTest()
//	{
//		Bilancio bilancio = new Bilancio();
//		bilancio.setUid(1);
//		bilancio.setAnno(2013);
//		return bilancio;
//	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.test.BaseJunit4TestCase#getEnteTest()
	 */
//	protected Ente getEnteTest()
//	{
//		Ente ente = new Ente();
//		ente.setUid(1);
//		//ente.setNome("mio nome di prova");
//		return ente;
//	}

//	/* (non-Javadoc)
//	 * @see it.csi.siac.siacbilser.test.BaseJunit4TestCase#getRichiedenteByProperties("consip","regp")
//	 */
//	protected Richiedente getRichiedenteByProperties("consip","regp")
//	{
//		Richiedente richiedente = new Richiedente();
//		Operatore operatore = new Operatore();
//		operatore.setCodiceFiscale("RMNLSS");
//		richiedente.setOperatore(operatore);
//		return richiedente;
//	}

	/**
	 * Gets the importi capitolo.
	 *
	 * @param i the i
	 * @return the importi capitolo
	 */
	protected ImportiCapitoloUP getImportiCapitolo(int i)
	{
		ImportiCapitoloUP importiCapitoloUP = new ImportiCapitoloUP();

		importiCapitoloUP.setAnnoCompetenza(i);
		//importiCapitoloUP.setStanziamento(i + 0.3);  ???
		//importiCapitoloUP.setStanziamentoCassa(i + 20000.5);  ???
		//importiCapitoloUP.setStanziamentoResiduo(i + 10000.7);  ???

		return importiCapitoloUP;
	}

	/**
	 * Test update.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testUpdate() throws Throwable
	{

		try
		{
			RicercaPuntualeCapitoloUPrev criteriRicerca = new RicercaPuntualeCapitoloUPrev();

			criteriRicerca.setNumeroCapitolo(17643);
			criteriRicerca.setAnnoEsercizio(2013);
			criteriRicerca.setAnnoCapitolo(2013);
			criteriRicerca.setNumeroArticolo(0);
			criteriRicerca.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);

			RicercaPuntualeCapitoloUscitaPrevisioneResponse res = gestioneCapitoloUscitaPrevisione
					.ricercaPuntualeCapitoloUscitaPrevisione(getRichiedenteByProperties("consip","regp"), getEnteTest(),
							criteriRicerca);

			CapitoloUscitaPrevisione capitoloUscitaPrevisione = res.getCapitoloUscitaPrevisione();

			capitoloUscitaPrevisione.setDescrizione(StringUtils.reverse(capitoloUscitaPrevisione
					.getDescrizione()));

			Ente ente = getEnteTest();

			Richiedente richiedente = getRichiedenteByProperties("consip","regp");

			Bilancio bilancio = getBilancioTest();

			// List<ImportiCapitoloUP> listaImporti =
			// capitoloUscitaPrevisione.getImportiCapitoloUP();

			// Bilancio bilancio = new Bilancio();
			// bilancio.setUid(1);
			//
			//
			// capitoloUscitaPrevisione.setUid(817950);
			//
			// capitoloUscitaPrevisione.setDescrizione("TEST CUP UPDATE "
			// + new Date().toString());
			// capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(2011);
			//
			// Ente ente = getEnteTest();
			//
			// ElementoPianoDeiConti elementoPianoDeiConti = new
			// ElementoPianoDeiConti();
			//
			// Richiedente richiedente = new Richiedente();
			//
			// TipoFondo tipoFondo = new TipoFondo();
			// tipoFondo.setUid(88);
			//
			// Macroaggregato macroaggregato = new Macroaggregato();
			// Programma programma = new Programma();
			// TipoFinanziamento tipoFinanziamento = null;
			// List<ClassificatoreGenerico> listaClassificatori = null;
			// StrutturaAmministrativoContabile strutturaAmministrativoContabile
			// = new StrutturaAmministrativoContabile();
			// ClassificazioneCofogProgramma classificazioneCofogProgramma = new
			// ClassificazioneCofogProgramma();
			// ImportiCapitoloUP importiCapitoloUP = new ImportiCapitoloUP();
			//
			// importiCapitoloUP.setAnnoCompetenza(2012);
			// importiCapitoloUP.setStanziamento(693.3);
			// importiCapitoloUP.setStanziamentoCassa(685.5);
			// importiCapitoloUP.setStanziamentoResiduo(617.7);
			//
			// List<ImportiCapitoloUP> listaImporti = new
			// ArrayList<ImportiCapitoloUP>();
			//
			// listaImporti.add(importiCapitoloUP);

			// gestioneCapitoloUscitaPrevisione.aggiornaCapitoloUscitaPrevisione(
			// richiedente, ente, bilancio, capitoloUscitaPrevisione,
			// tipoFondo, tipoFinanziamento, listaClassificatori,
			// elementoPianoDeiConti,
			// strutturaAmministrativoContabile,
			// classificazioneCofogProgramma, listaImporti, macroaggregato,
			// programma);

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Test ricerca sintetica.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaSintetica() throws Throwable
	{

		try
		{

			Richiedente richiedente = new Richiedente();
			Ente ente = getEnteTest();

			RicercaSinteticaCapitoloUPrev criteriRicerca = new RicercaSinteticaCapitoloUPrev();

			criteriRicerca.setAnnoEsercizio(2013);
			criteriRicerca.setAnnoCapitolo(2012);
			criteriRicerca.setNumeroCapitolo(1234567);
			criteriRicerca.setNumeroArticolo(1234567);
			criteriRicerca.setDescrizioneCapitolo("CUP");

			gestioneCapitoloUscitaPrevisione.ricercaSinteticaCapitoloUscitaPrevisione(richiedente,
					ente, criteriRicerca, null);

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

		try
		{
			RicercaPuntualeCapitoloUscitaPrevisione req = new RicercaPuntualeCapitoloUscitaPrevisione();
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			
//			Richiedente richiedente = new Richiedente();
//			Operatore operatore = new Operatore();
//			operatore.setCodiceFiscale("LSIDNC32242");
//			richiedente.setOperatore(operatore);
//			req.setRichiedente(richiedente);
			Ente ente = getEnteTest();
			req.setEnte(ente);

			RicercaPuntualeCapitoloUPrev criteriRicerca = new RicercaPuntualeCapitoloUPrev();

			criteriRicerca.setAnnoEsercizio(2015);
			criteriRicerca.setAnnoCapitolo(2015);
			criteriRicerca.setNumeroCapitolo(7320);
			criteriRicerca.setNumeroArticolo(4);
			criteriRicerca.setNumeroUEB(101020000);

			criteriRicerca.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
			req.setRicercaPuntualeCapitoloUPrev(criteriRicerca);
			

			req.setImportiDerivatiRichiesti(EnumSet.of(ImportiCapitoloEnum.diCuiImpegnatoAnno1));
			/*gestioneCapitoloUscitaPrevisione.ricercaPuntualeCapitoloUscitaPrevisione(richiedente,
					ente, criteriRicerca);*/
					
					
			RicercaPuntualeCapitoloUscitaPrevisioneResponse res = ricercaPuntualeCapitoloUscitaPrevisioneService.executeService(req);
			
			System.out.println("test result: "+res.getCapitoloUscitaPrevisione());

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Test ricerca dettaglio.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaDettaglio() throws Throwable
	{

		try
		{

			Richiedente richiedente = new Richiedente();
			Ente ente = getEnteTest();

			RicercaDettaglioCapitoloUPrev criteriRicerca = new RicercaDettaglioCapitoloUPrev();

			criteriRicerca.setChiaveCapitolo(817950);

			gestioneCapitoloUscitaPrevisione.ricercaDettaglioCapitoloUscitaPrevisione(richiedente,
					ente, criteriRicerca);

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Test ricerca dettaglio.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testcheckCapitoloUscitaPrevisioneSeFPV() throws Throwable
	{

		try
		{

			log.info("testcheckCapitoloUscitaPrevisioneSeFPV", "START");			
			
			
			InserisceCapitoloDiUscitaPrevisione req = new InserisceCapitoloDiUscitaPrevisione();
			req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
			
			//req.setEnte(req.getRichiedente().getAccount().getEnte());
			req.setEnte(getEnteTest());
			
			req.setBilancio(getBilancioTest(16,2015));
			
		
			CapitoloUscitaPrevisione a = new CapitoloUscitaPrevisione();
			a.setAnnoCapitolo(2015);
			a.setNumeroCapitolo(234234);
			a.setNumeroArticolo(23423424);
			a.setNumeroUEB(1);
			CategoriaCapitolo categoriaCapitolo = new CategoriaCapitolo();
			categoriaCapitolo.setCodice("FPV");
			categoriaCapitolo.setUid(8);
			a.setCategoriaCapitolo(categoriaCapitolo);
			
		    req.setCapitoloUscitaPrevisione(a);
			StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
			sac.setUid(509);
			req.setStruttAmmContabile(sac);
			log.info("testcheckCapitoloUscitaPrevisioneSeFPV", "FINE INIZIALIZAZIONE");			
			
			inserisceGestioneCapitoloUscitaPrevisioneService.executeService(req);
			log.info("testcheckCapitoloUscitaPrevisioneSeFPV", "STOP");			

			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Test ricerca dettaglio.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAggiornaCapitoloDiUscitaPrevisioneService() throws Throwable
	{

		try
		{

			log.info("testAggiornaCapitoloDiUscitaPrevisioneService", "START");			
			
			
			AggiornaCapitoloDiUscitaPrevisione req = new AggiornaCapitoloDiUscitaPrevisione();
			req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
			req.setEnte(req.getRichiedente().getAccount().getEnte());
			req.setEnte(getEnteTest());
			req.setBilancio(getBilancioTest(16,2015));
			
			
			log.info("testAggiornaCapitoloDiUscitaPrevisioneService", "1");		
			
			CapitoloUscitaPrevisione a = new CapitoloUscitaPrevisione();
			a.setUid(30991);
			a.setAnnoCapitolo(2015);
			a.setNumeroCapitolo(2102);
			a.setNumeroArticolo(2);
			a.setNumeroUEB(1);			
			a.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.PROVVISORIO );
			
			log.info("testAggiornaCapitoloDiUscitaPrevisioneService", "2");	
			
			CategoriaCapitolo categoriaCapitolo = new CategoriaCapitolo();
			categoriaCapitolo.setCodice("FPV");
			categoriaCapitolo.setUid(8);
			a.setCategoriaCapitolo(categoriaCapitolo);
			
			log.info("testAggiornaCapitoloDiUscitaPrevisioneService", "3");
			
		    req.setCapitoloUscitaPrevisione(a);
			StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
			sac.setUid(509);
			req.setStruttAmmContabile(sac);
			
			log.info("testAggiornaCapitoloDiUscitaPrevisioneService", "FINE INIZIALIZAZIONE");			
			
			aggiornaCapitoloDiUscitaPrevisioneService.executeService(req);
			
			log.info("testAggiornaCapitoloDiUscitaPrevisioneService", "STOP");			

			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	
	@Test
	public void testRicercaDettaglioUscitaPrevisione() {
		RicercaDettaglioCapitoloUscitaPrevisione request = new RicercaDettaglioCapitoloUscitaPrevisione();
		request.setDataOra(new Date());
		request.setEnte(getEnteByProperties("consip", "regp"));
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		utility.setChiaveCapitolo(122094);
		request.setRicercaDettaglioCapitoloUPrev(
				utility);
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));
		RicercaDettaglioCapitoloUscitaPrevisioneResponse res = ricercaDettaglioCapitoloUscitaPrevisioneService.executeService(request);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
}
