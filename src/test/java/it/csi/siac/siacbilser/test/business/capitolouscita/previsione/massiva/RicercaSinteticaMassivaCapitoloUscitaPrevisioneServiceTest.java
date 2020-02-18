/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione.massiva;

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

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaMassivaCapitoloUscitaPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaSinteticaMassivaCapitoloUscitaPrevisioneServiceTest extends CapitoloUscitaPrevisioneMassivaMainTest 
{

	/** The capitolo uscita previsione service. */
	@Autowired
	private CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;

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
	 * Instantiates a new ricerca sintetica massiva capitolo uscita previsione service test.
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
	public RicercaSinteticaMassivaCapitoloUscitaPrevisioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String annoEser, String descCapi, String Esito,
			String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.annoEser = toInteger(annoEser);
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

		RicercaSinteticaMassivaCapitoloUscitaPrevisione req = new RicercaSinteticaMassivaCapitoloUscitaPrevisione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setNumeroPagina(0);
		pp.setElementiPerPagina(20);
		
		while (true)
		{
			RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse res = ricercaSinteticaMassivaCapitoloUscitaPrevisione(req, pp);
			
			assertServiceResponse(res);
			
			if (res.getCapitoli().size() == 0)
				break;

			//TESTA GLI OGGETTI DI RITORNO :
			// ANNO DI BILANCIO
			assertNotNull(res.getBilancio());
			assertEquals("Anno di bilancio diverso da quello ricercato.", annoBil.intValue(), res.getBilancio().getAnno());
					
			// LISTA CAPITOLI DI USCITA PREVISIONE
			assertNotNull("Lista capitoli restituita null dalla ricerca sintetica", res.getCapitoli());

			ListaPaginata<CapitoloUscitaPrevisione> listacap = res.getCapitoli();
			
			assertNotNull("Numero pagina corrente restituito null dalla ricerca", listacap.getPaginaCorrente());
			
			assertNotNull("Numero elementi trovati restituito null dalla ricerca", listacap.getTotaleElementi());
			
			assertNotNull("Numero pagine trovate restituito null dalla ricerca", listacap.getTotalePagine());

			if (testOut.equals("OUT NO")) {
				return;
			}
			
			Iterator<CapitoloUscitaPrevisione> itercapi = listacap.iterator();
						
			while (itercapi.hasNext())
			{
				CapitoloUscitaPrevisione capitoloRicercaSintetica = itercapi.next();
				
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
	 * Ricerca sintetica massiva capitolo uscita previsione.
	 *
	 * @param req the req
	 * @param pp the pp
	 * @return the ricerca sintetica massiva capitolo uscita previsione response
	 */
	private RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse ricercaSinteticaMassivaCapitoloUscitaPrevisione(RicercaSinteticaMassivaCapitoloUscitaPrevisione req,
			ParametriPaginazione pp) 
	{
		RicercaSinteticaCapitoloUPrev criteriRicerca = getCriteriRicercas(annoEser, descCapi);
	
		req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);

		req.setParametriPaginazione(pp);
	
		RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.ricercaSinteticaMassivaCapitoloUscitaPrevisione(req);

		return res;		
	}
}
