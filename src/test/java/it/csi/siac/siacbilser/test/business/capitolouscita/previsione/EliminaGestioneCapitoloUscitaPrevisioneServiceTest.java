/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class EliminaGestioneCapitoloUscitaPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class EliminaGestioneCapitoloUscitaPrevisioneServiceTest extends CapitoloUscitaPrevisioneMainTest 
{

	/** The capitolo uscita previsione service. */
	@Autowired
	private CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;

    /**
     * Spreadsheet data.
     *
     * @return the collection
     * @throws Exception the exception
     */
    @Parameters
    public static Collection<Object[]> spreadsheetData() throws Exception {
    	return spreadsheetData("Elimina");
    }

	/**
	 * Instantiates a new elimina gestione capitolo uscita previsione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidBil the uid bil
	 * @param annoBil the anno bil
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @param numUEB the num ueb
	 * @param statoCapi the stato capi
	 * @param descCapi the desc capi
	 * @param annoCrea the anno crea
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public EliminaGestioneCapitoloUscitaPrevisioneServiceTest(String obiettivoTest,
			String uidEnte, String CFisc, String uidBil, String annoBil, String annoCapi, 
			String numeCapi, String numeArti, String numUEB, String statoCapi, String descCapi,
			String annoCrea, String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.uidBil = toInteger(uidBil);	
        this.annoBil = toInteger(annoBil); this.annoEser = toInteger(annoBil);	
        this.annoCapi = toInteger(annoCapi);
        this.numCapi = toInteger(numeCapi);	
        this.numArti = toInteger(numeArti);
        this.numUEB = toInteger(numUEB);
        this.statoCapi = statoCapi;		
        this.descCapi = descCapi;		
        this.annoCrea = toInteger(annoCrea);		
        this.esito = Esito;
        this.errore = Errore;
        this.descrizione = Descrizione;
        this.testOut = Testout;
    }

	/**
	 * Test elimina.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testElimina() throws Throwable
	{
		
		InizializzaUidCapitoloSeValorizzataChiaveRicerca();

		EliminaCapitoloUscitaPrevisioneResponse res = eliminaCapitoloUscitaPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO
		/*FACCIO UNA RICERCA DI DETTAGLIO SUL CAPITOLO APPENA ELIMINATO 
		 * E MI DEVE RITORNARE IL CAPITOLO RICHIESTO */
				
		RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaDettaglioResponse = ricercaDettaglioCapitoloUscitaPrevisione();
				
		assertEquals("Fallita ricerca dettaglio del capitolo appena eliminato.", Esito.SUCCESSO, ricercaDettaglioResponse.getEsito());
					
		// CAPITOLO USCITA PREVISIONE ELIMINATO		
		CapitoloUscitaPrevisione capitoloEliminato = ricercaDettaglioResponse.getCapitoloUscitaPrevisione();

		assertNotNull("capitolo restituito null", capitoloEliminato);
		assertTrue("uid restituito a 0!!", capitoloEliminato.getUid() != 0);
		assertTrue("uid restituito a 0!!", capitoloEliminato.getUid() == uidCapi);
	}

	/**
	 * Ricerca dettaglio capitolo uscita previsione.
	 *
	 * @return the ricerca dettaglio capitolo uscita previsione response
	 */
	private RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaDettaglioCapitoloUscitaPrevisione() {
		RicercaDettaglioCapitoloUPrev criteriRicerca = getCriteriRicercaDettaglio(uidCapi);

		RicercaDettaglioCapitoloUscitaPrevisione req = new RicercaDettaglioCapitoloUscitaPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaDettaglioCapitoloUPrev(criteriRicerca);

		RicercaDettaglioCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(req);
		return res;
	}

	/**
	 * Elimina capitolo uscita previsione.
	 *
	 * @return the elimina capitolo uscita previsione response
	 */
	private EliminaCapitoloUscitaPrevisioneResponse eliminaCapitoloUscitaPrevisione() {
	
		EliminaCapitoloUscitaPrevisione req = new EliminaCapitoloUscitaPrevisione();
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloUscitaPrevisione capitoloUscitaPrevisione = getCapitoloUscitaPrevisione();		
		req.setCapitoloUscitaPrev(capitoloUscitaPrevisione);

		EliminaCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.eliminaCapitoloUscitaPrevisione(req);
		return res;		
	}
	
	/**
	 * Inizializza uid capitolo se valorizzata chiave ricerca.
	 */
	private void InizializzaUidCapitoloSeValorizzataChiaveRicerca(){
		if(!valorizzatiParametriRicercaPuntuale()){
			return;
		}
		
		inizializzaUidCapitolo();
	}
	
	/**
	 * Inizializza uid capitolo.
	 */
	private void inizializzaUidCapitolo() {
		final String methodName = "inizializzaUidCapitolo";
	
		RicercaPuntualeCapitoloUscitaPrevisioneResponse ricercaPuntualeResponse = ricercaPuntuale(capitoloUscitaPrevisioneService);
		/*String msgCapNonEsiste = "Capitolo da eliminare non trovato! Il capitolo deve esistere per poter testare l'eliminazione!";
		assertNotNull(msgCapNonEsiste, ricercaPuntualeResponse);
		assertEquals(msgCapNonEsiste, Esito.SUCCESSO, ricercaPuntualeResponse.getEsito());*/
		
		if (ricercaPuntualeResponse.getCapitoloUscitaPrevisione() != null)
		{
			this.uidCapi = ricercaPuntualeResponse.getCapitoloUscitaPrevisione().getUid();
			log.debug(methodName, "Capitolo da aggiornare trovato con uid: "+this.uidCapi);
		}
		else this.uidCapi = 1;	
	}
}
