/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione.massiva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaMassivaCapitoloEntrataPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaSinteticaMassivaCapitoloEntrataPrevisioneServiceTest extends CapitoloEntrataPrevisioneMassivaMainTest 
{

	/** The capitolo entrata previsione service. */
	@Autowired
	private CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;

    /**
     * Spreadsheet data massiva.
     *
     * @return the collection
     * @throws Exception the exception
     */
    @Parameters
    public static Collection<Object[]> spreadsheetDataMassiva() throws Exception {
    	return spreadsheetDataMassiva("RicercaSintetica");
    }

	/**
	 * Instantiates a new ricerca sintetica massiva capitolo entrata previsione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param annoEser the anno eser
	 * @param descCapi the desc capi
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public RicercaSinteticaMassivaCapitoloEntrataPrevisioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String annoEser, String descCapi, String Esito,
			String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.annoEser = toInteger(annoEser); this.annoBil = toInteger(annoEser);
      	this.descCapi = descCapi;   
        this.esito = Esito;
        this.errore = Errore;
        this.descrizione = Descrizione;
        this.testOut = Testout;
    }

	/**
	 * Test ricerca sintetica massiva.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaSinteticaMassiva() throws Throwable
	{        	

		RicercaSinteticaMassivaCapitoloEntrataPrevisione req = new RicercaSinteticaMassivaCapitoloEntrataPrevisione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setNumeroPagina(0);
		pp.setElementiPerPagina(20);
		
		while (true)
		{
			RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse res = ricercaSinteticaMassivaCapitoloEntrataPrevisione(req, pp);
			
			assertServiceResponse(res);
			
			if (res.getCapitoli().size() == 0)
				break;

			//TESTA GLI OGGETTI DI RITORNO :
			// ANNO DI BILANCIO
			assertNotNull(res.getBilancio());
			assertEquals("Anno di bilancio diverso da quello ricercato.", annoBil.intValue(), res.getBilancio().getAnno());
					
			// LISTA CAPITOLI DI USCITA PREVISIONE
			assertNotNull("Lista capitoli restituita null dalla ricerca sintetica", res.getCapitoli());

			ListaPaginata<CapitoloEntrataPrevisione> listacap = res.getCapitoli();
			
			assertNotNull("Numero pagina corrente restituito null dalla ricerca", listacap.getPaginaCorrente());
			
			assertNotNull("Numero elementi trovati restituito null dalla ricerca", listacap.getTotaleElementi());
			
			assertNotNull("Numero pagine trovate restituito null dalla ricerca", listacap.getTotalePagine());

			if (testOut.equals("OUT NO")) {
				return;
			}
			
			Iterator<CapitoloEntrataPrevisione> itercapi = listacap.iterator();
						
			while (itercapi.hasNext())
			{
				CapitoloEntrataPrevisione capitoloRicercaSintetica = itercapi.next();
				
				// CONTROLLO SU OGNI CAPITOLO IN LISTA :
				// ANNO CAPITOLO
				assertNotNull(capitoloRicercaSintetica.getAnnoCapitolo());
				
				// NUMERO CAPITOLO
				assertNotNull(capitoloRicercaSintetica.getNumeroCapitolo());

				// NUMERO ARTICOLO
				assertNotNull(capitoloRicercaSintetica.getNumeroArticolo());

				// NUMERO UEB
				assertNotNull(capitoloRicercaSintetica.getNumeroUEB());
				
				// DESCRIZIONE CAPITOLO
				assertNotNull("Descrizione restituita null dalla ricerca sintetica", capitoloRicercaSintetica.getDescrizione());

				// ELEMENTO PIANO DEI CONTI
				assertNotNull("Piano dei Conti restituito null dalla ricerca sintetica", capitoloRicercaSintetica.getElementoPianoDeiConti());

				// ELEMENTO STRUTTURA AMMINISTRATIVO CONTABILE
				assertNotNull("Struttura Amministrativo Contabile restituita null dalla ricerca sintetica", capitoloRicercaSintetica.getStrutturaAmministrativoContabile());
			}

			req.setPaginaRemote(res.getPaginaRemote());
			req.setPosizionePaginaRemote(res.getPosizionePaginaRemote());
			
			int numeroPagina = pp.getNumeroPagina();
			
			pp.setNumeroPagina(++numeroPagina);

		}
	}

	/**
	 * Ricerca sintetica massiva capitolo entrata previsione.
	 *
	 * @param req the req
	 * @param pp the pp
	 * @return the ricerca sintetica massiva capitolo entrata previsione response
	 */
	private RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse ricercaSinteticaMassivaCapitoloEntrataPrevisione(RicercaSinteticaMassivaCapitoloEntrataPrevisione req,
			ParametriPaginazione pp) 
	{
		RicercaSinteticaCapitoloEPrev criteriRicerca = getCriteriRicercas(annoEser, descCapi);
	
		req.setRicercaSinteticaCapitoloEPrev(criteriRicerca);

		req.setParametriPaginazione(pp);
	
		RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse res = capitoloEntrataPrevisioneService.ricercaSinteticaMassivaCapitoloEntrataPrevisione(req);

		return res;		
	}
}
