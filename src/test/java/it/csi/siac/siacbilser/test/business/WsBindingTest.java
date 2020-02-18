/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import junit.framework.TestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class WsBindingTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"})
public class WsBindingTest  extends TestCase  {
	
	private final LogUtil log = new LogUtil(getClass());
	
	 //@Resource(name="capitoloUscitaPrevisioneService")
	 /** The capitolo uscita previsione service. */
 	@Autowired
	 private CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;	 
	 
 	/** The core service. */
 	@Autowired
	 private CoreService coreService;
	 
 	/** The classificatore bil service. */
 	@Autowired
	 private ClassificatoreBilService classificatoreBilService;
	 
 	/** The classificatore service. */
 	@Autowired
	 private ClassificatoreService classificatoreService;
	 
	 
	 /**
 	 * Test service binding.
 	 */
 	@Test
	public void testServiceBinding() {
 		final String methodName = "testServiceBinding";
		assertNotNull(coreService);
		log.info(methodName, "coreService: " + ToStringBuilder.reflectionToString(coreService));

		assertNotNull(classificatoreService);
		log.info(methodName, "classificatoreService: " + ToStringBuilder.reflectionToString(classificatoreService));

		assertNotNull(capitoloUscitaPrevisioneService);
		log.info(methodName, "capitoloUscitaPrevisioneService: " + ToStringBuilder.reflectionToString(capitoloUscitaPrevisioneService));

		assertNotNull(classificatoreBilService);
		log.info(methodName, "classificatoreBilService: " + ToStringBuilder.reflectionToString(classificatoreBilService));

	}
	 
	 
	 
	 
	 
	 /**
 	 * Test ricerca dettaglio.
 	 *
 	 * @throws Throwable the throwable
 	 */
 	@Test
	public void testRicercaDettaglio() throws Throwable
	{

 		final String methodName = "testRicercaDettaglio";
			try
			{
				RicercaDettaglioCapitoloUscitaPrevisione req = new RicercaDettaglioCapitoloUscitaPrevisione();

				req.setRichiedente(getRichiedenteTest());
				req.setEnte(getEnteTest());

				RicercaDettaglioCapitoloUPrev criteriRicerca = new RicercaDettaglioCapitoloUPrev();

				//criteriRicerca.setChiaveCapitolo(817950);
				//criteriRicerca.setChiaveCapitolo(2045426850);
				
				//criteriRicerca.setChiaveCapitolo(2045425050);
				
				criteriRicerca.setChiaveCapitolo(40908566);

				req.setRicercaDettaglioCapitoloUPrev(criteriRicerca);
				
				
				RicercaDettaglioCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(req);
				
				log.info(methodName, "lista classificatori: "+res.getListaClassificatori());
				
				log.info(methodName, "res.getListaClassificatori()!=null? " +(res.getListaClassificatori()!=null));

			}
			catch (Throwable e)
			{
				e.printStackTrace();
				throw e;
			}
		}
	 
	 
	 
	 
	 /**
 	 * Test ricerca sintetica capitolo di uscita previsione.
 	 *
 	 * @throws Throwable the throwable
 	 */
 	@Test
	 public void testRicercaSinteticaCapitoloDiUscitaPrevisione() throws Throwable {

 		final String methodName = "testRicercaSinteticaCapitoloDiUscitaPrevisione";

			try
			{
				RicercaSinteticaCapitoloUscitaPrevisione req = new RicercaSinteticaCapitoloUscitaPrevisione();

				req.setRichiedente(getRichiedenteTest());
				Ente ente = getEnteTest();
				req.setEnte(ente);

				RicercaSinteticaCapitoloUPrev criteriRicerca = new RicercaSinteticaCapitoloUPrev();
				
				criteriRicerca.setDescrizioneCapitolo("TEST");

				criteriRicerca.setAnnoEsercizio(2013);
//				criteriRicerca.setAnnoCapitolo(2013);
//				criteriRicerca.setNumeroCapitolo(17647);
//				criteriRicerca.setNumeroArticolo(1);
				
				
//				criteriRicerca.setCodicePianoDeiConti("E.9.02.03.00.000"); //trova E.9.02.03.04.000
				
//				criteriRicerca.setCodiceStrutturaAmmCont("005"); //troverà "004" CDC
//				criteriRicerca.setCodiceTipoStrutturaAmmCont("CDR");
				
//				criteriRicerca.setCodiceMissione("01"); //troverà codice programma "11"
				
				//criteriRicerca.setCodiceTitoloUscita("1"); //troverà codice Macroaggregato "06"
				
				req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);
				ParametriPaginazione pp = new ParametriPaginazione();
				pp.setNumeroPagina(0);
				pp.setElementiPerPagina(100);

				req.setParametriPaginazione(pp);
				/*gestioneCapitoloUscitaPrevisione.ricercaSinteticaCapitoloUscitaPrevisione(richiedente,
						ente, criteriRicerca, null);*/
				
				RicercaSinteticaCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.ricercaSinteticaCapitoloUscitaPrevisione(req);
				
				log.info(methodName, res.getEsito());
				ListaPaginata<CapitoloUscitaPrevisione> listaCapitoli = res.getCapitoli();
				if(listaCapitoli!=null){
					log.info(methodName, "totaleElementi: "+listaCapitoli.getTotaleElementi());
					log.info(methodName, "totalePagine: "+listaCapitoli.getTotalePagine());
					log.info(methodName, "paginaCorrente: "+listaCapitoli.getPaginaCorrente());
					
					log.info(methodName, "list: "+ToStringBuilder.reflectionToString(listaCapitoli));
					
					for (CapitoloUscitaPrevisione capitoloUscitaPrevisione : listaCapitoli) {
						log.info(methodName, "capitoloUscitaPrevisione: "+ToStringBuilder.reflectionToString(capitoloUscitaPrevisione));
						log.info(methodName, "capitoloUscitaPrevisione.Missione: "+ToStringBuilder.reflectionToString(capitoloUscitaPrevisione.getMissione()));
						log.info(methodName, "capitoloUscitaPrevisione.TitoloSpesa: "+ToStringBuilder.reflectionToString(capitoloUscitaPrevisione.getTitoloSpesa()));
					}
				}
				

			}
			catch (Throwable e)
			{
				e.printStackTrace();
				throw e;
			}
		
		
	 }
	 
	 /**
 	 * Test inserisce capitolo di uscita previsione.
 	 *
 	 * @throws Throwable the throwable
 	 */
 	@Test
	 public void testInserisceCapitoloDiUscitaPrevisione() throws Throwable {
 		final String methodName = "testInserisceCapitoloDiUscitaPrevisione";
			try
			{
				
				log.info(methodName, "capitoloUscitaPrevisioneService: "+ToStringBuilder.reflectionToString(capitoloUscitaPrevisioneService));

				Bilancio bilancio = getBilancioTest();

				CapitoloUscitaPrevisione capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();

				capitoloUscitaPrevisione.setNumeroCapitolo(17643);
				capitoloUscitaPrevisione.setNumeroArticolo(0);
				capitoloUscitaPrevisione.setAnnoCapitolo(bilancio.getAnno());
				capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
				capitoloUscitaPrevisione.setDescrizione("TEST CUP " + new Date().toString());
				capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(2011);

				Ente ente = getEnteTest();

				Richiedente richiedente = getRichiedenteTest();

				ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
				elementoPianoDeiConti.setUid(5251);

				Macroaggregato macroaggregato = new Macroaggregato();
				macroaggregato.setUid(119);

				Programma programma = new Programma();
				programma.setUid(24);

				StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
				strutturaAmministrativoContabile.setUid(5244);

				ClassificazioneCofogProgramma classificazioneCofogProgramma = new ClassificazioneCofogProgramma();
				classificazioneCofogProgramma.setUid(5245);

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
				req.setStruttAmmContabile(strutturaAmministrativoContabile);
				req.setClassificazioneCofogProgramma(classificazioneCofogProgramma);
				req.setImportiCapitoloUP(listaImporti);
				req.setMacroaggregato(macroaggregato);
				req.setProgramma(programma);
				
				
				
				/*InserisceCapitoloDiUscitaPrevisioneResponse res = gestioneCapitoloUscitaPrevisione
						.inserisceCapitoloUscitaPrevisione(richiedente, ente, bilancio,
								capitoloUscitaPrevisione, tipoFondo, tipoFinanziamento,
								listaClassificatori, elementoPianoDeiConti,
								strutturaAmministrativoContabile, classificazioneCofogProgramma,
								listaImporti, macroaggregato, programma);

				*/
				
				//InserisceCapitoloDiUscitaPrevisioneResponse res = inserisceGestioneCapitoloUscitaPrevisioneService.executeService(req);

				
				InserisceCapitoloDiUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.inserisceCapitoloDiUscitaPrevisione(req);
				
				
				log.info(methodName, "esito: "+res.getEsito());
				log.info(methodName, "errori: "+res.getErrori());
				
				
				assertNotNull(res);
				
				CapitoloUscitaPrevisione ins = res.getCapitoloUPrevInserito();
				
				assertNotNull(ins);			

				log.info(methodName, "inserito id = " + ins.getUid());

			}
			catch (Throwable e)
			{
				e.printStackTrace();
				throw e;
			}
		

	}
	 
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
 	 * Gets the bilancio test.
 	 *
 	 * @return the bilancio test
 	 */
 	protected Bilancio getBilancioTest()
		{
			Bilancio bilancio = new Bilancio();
			bilancio.setUid(1);
			bilancio.setAnno(2013);
			return bilancio;
		}

		/**
		 * Gets the ente test.
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
		 * Gets the richiedente test.
		 *
		 * @return the richiedente test
		 */
		protected Richiedente getRichiedenteTest()
		{
			Richiedente richiedente = new Richiedente();
			Operatore operatore = new Operatore();
			operatore.setCodiceFiscale("RMNLSS");
			richiedente.setOperatore(operatore);
			return richiedente;
		}
	 

}
