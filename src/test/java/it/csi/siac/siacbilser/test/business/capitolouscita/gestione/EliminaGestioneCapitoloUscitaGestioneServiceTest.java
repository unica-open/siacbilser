/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.gestione;

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

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class EliminaGestioneCapitoloUscitaGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class EliminaGestioneCapitoloUscitaGestioneServiceTest extends CapitoloUscitaGestioneMainTest 
{

	/** The capitolo uscita gestione service. */
	@Autowired
	private CapitoloUscitaGestioneService capitoloUscitaGestioneService;

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
	 * Instantiates a new elimina gestione capitolo uscita gestione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidBil the uid bil
	 * @param annoBil the anno bil
	 * @param uidCapi the uid capi
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
	public EliminaGestioneCapitoloUscitaGestioneServiceTest(String obiettivoTest, String uidEnte, String CFisc,
			String uidBil, String annoBil, String uidCapi, String annoCapi, String numeCapi, 
			String numeArti, String numUEB, String statoCapi, String descCapi, String annoCrea,
			String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.uidBil = toInteger(uidBil);	
        this.annoBil = toInteger(annoBil); this.annoEser = toInteger(annoBil);	
        this.uidCapi = toInteger(uidCapi);	
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

		EliminaCapitoloUscitaGestioneResponse res = eliminaCapitoloUscitaGestione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO
		/*FACCIO UNA RICERCA DI DETTAGLIO SUL CAPITOLO APPENA ELIMINATO 
		 * E MI DEVE RITORNARE IL CAPITOLO RICHIESTO */
				
		RicercaDettaglioCapitoloUscitaGestioneResponse ricercaDettaglioResponse = ricercaDettaglioCapitoloUscitaGestione();
				
		assertEquals("Fallita ricerca dettaglio del capitolo appena eliminato.", Esito.SUCCESSO, ricercaDettaglioResponse.getEsito());
					
		// CAPITOLO USCITA GESTIONE ELIMINATO		
		CapitoloUscitaGestione capitoloEliminato = ricercaDettaglioResponse.getCapitoloUscita();

		assertNotNull("capitolo restituito null", capitoloEliminato);
		assertTrue("uid restituito a 0!!", capitoloEliminato.getUid() != 0);
		assertTrue("uid restituito a 0!!", capitoloEliminato.getUid() == uidCapi);
	}

	/**
	 * Ricerca dettaglio capitolo uscita gestione.
	 *
	 * @return the ricerca dettaglio capitolo uscita gestione response
	 */
	private RicercaDettaglioCapitoloUscitaGestioneResponse ricercaDettaglioCapitoloUscitaGestione() {
		RicercaDettaglioCapitoloUGest criteriRicerca = getCriteriRicercaDettaglio(uidCapi);

		RicercaDettaglioCapitoloUscitaGestione req = new RicercaDettaglioCapitoloUscitaGestione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaDettaglioCapitoloUGest(criteriRicerca);

		RicercaDettaglioCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(req);
		return res;
	}

	/**
	 * Elimina capitolo uscita gestione.
	 *
	 * @return the elimina capitolo uscita gestione response
	 */
	private EliminaCapitoloUscitaGestioneResponse eliminaCapitoloUscitaGestione() {
	
		EliminaCapitoloUscitaGestione req = new EliminaCapitoloUscitaGestione();
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloUscitaGestione capitoloUscitaGestione = getCapitoloUscitaGestione();		
		req.setCapitoloUscitaGest(capitoloUscitaGestione);

		EliminaCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.eliminaCapitoloUscitaGestione(req);
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

		RicercaPuntualeCapitoloUscitaGestioneResponse ricercaPuntualeResponse = ricercaPuntuale(capitoloUscitaGestioneService);
		/*String msgCapNonEsiste = "Capitolo da aggiornare non trovato! Il capitolo deve esistere per poter testare l'aggiornamento!";
		assertNotNull(msgCapNonEsiste, ricercaPuntualeResponse);
		assertEquals(msgCapNonEsiste, Esito.SUCCESSO, ricercaPuntualeResponse.getEsito());*/
		
		if (ricercaPuntualeResponse.getCapitoloUscitaGestione() != null)
		{
			this.uidCapi = ricercaPuntualeResponse.getCapitoloUscitaGestione().getUid();
			log.debug(methodName, "Capitolo da aggiornare trovato con uid: "+this.uidCapi);
		}
		else this.uidCapi = 1;		
	}

}
