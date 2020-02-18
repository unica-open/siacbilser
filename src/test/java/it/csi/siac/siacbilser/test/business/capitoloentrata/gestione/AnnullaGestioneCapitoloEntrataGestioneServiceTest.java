/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaGestioneCapitoloEntrataGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class AnnullaGestioneCapitoloEntrataGestioneServiceTest extends CapitoloEntrataGestioneMainTest
{

	/** The capitolo entrata gestione service. */
	@Autowired
	private CapitoloEntrataGestioneService capitoloEntrataGestioneService;

    /**
     * Spreadsheet data.
     *
     * @return the collection
     * @throws Exception the exception
     */
    @Parameters
    public static Collection<Object[]> spreadsheetData() throws Exception {
    	return spreadsheetData("Annulla");
    }

	/**
	 * Instantiates a new annulla gestione capitolo entrata gestione service test.
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
	public AnnullaGestioneCapitoloEntrataGestioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String uidBil, String annoBil, String uidCapi, 
			String annoCapi, String numeCapi, String numeArti, String numUEB, String statoCapi,
			String descCapi, String annoCrea, String Esito, String Errore, String Descrizione, 
			String Testout) 
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
	 * Test annulla.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAnnulla() throws Throwable
	{

		InizializzaUidCapitoloSeValorizzataChiaveRicerca();
		
		AnnullaCapitoloEntrataGestioneResponse res = annullaCapitoloEntrataGestione();
			
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
			
		//TESTA GLI OGGETTI DI RITORNO
		/*FACCIO UNA RICERCA DI DETTAGLIO SUL CAPITOLO APPENA ANNULLATO 
		E TESTO CHE LO STATO OPERATIVO SIA PASSATO DA "VALIDO" AD "ANNULLATO" 
		E TESTO CHE LA DATA ANNULLAMENTO CI SIA E CHE SIA UGUALE ALLA DATA ATTUALE */
		
		RicercaDettaglioCapitoloEntrataGestioneResponse ricercaDettaglioResponse = ricercaDettaglioCapitoloEntrataGestione();
				
		assertEquals("Fallita ricerca dettaglio del capitolo appena annullato.", Esito.SUCCESSO, ricercaDettaglioResponse.getEsito());
					
		// CAPITOLO USCITA PREVISIONE ANNULLATO
		CapitoloEntrataGestione capitoloAnnullato = ricercaDettaglioResponse.getCapitoloEntrataGestione();

		assertNotNull("capitolo restituito null", capitoloAnnullato);
		assertTrue("uid restituito a 0!!", capitoloAnnullato.getUid() != 0);
		
		// STATO OPERATIVO ELEMENTO DI BILANCIO
		assertNotNull("stato elemento di bilancio restituito null", capitoloAnnullato.getStatoOperativoElementoDiBilancio());
		assertTrue("elemento di bilancio non in stato ANNULLATO!!", capitoloAnnullato.getStatoOperativoElementoDiBilancio().toString().equals("ANNULLATO"));
		
		// DATA DI ANNULLAMENTO		
		assertNotNull("Data Annullamento restituita null", capitoloAnnullato.getDataAnnullamento());
				
		String [] vettore = new Date().toString().split(" ");
			
		String dataannulla = vettore[1]+" "+vettore[2];
				
		String annoannulla = vettore[vettore.length-1];
				
		assertTrue("Data Annullamento non corrisponde alla data del giorno!!", capitoloAnnullato.getDataAnnullamento().toString().contains(dataannulla) 
						   && capitoloAnnullato.getDataAnnullamento().toString().contains(annoannulla));
	}

	/**
	 * Ricerca dettaglio capitolo entrata gestione.
	 *
	 * @return the ricerca dettaglio capitolo entrata gestione response
	 */
	private RicercaDettaglioCapitoloEntrataGestioneResponse ricercaDettaglioCapitoloEntrataGestione() {
		RicercaDettaglioCapitoloEGest criteriRicerca = getCriteriRicercaDettaglio(uidCapi);

		RicercaDettaglioCapitoloEntrataGestione req = new RicercaDettaglioCapitoloEntrataGestione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaDettaglioCapitoloEGest(criteriRicerca);

		RicercaDettaglioCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(req);
		return res;
	}

	/**
	 * Annulla capitolo entrata gestione.
	 *
	 * @return the annulla capitolo entrata gestione response
	 */
	private AnnullaCapitoloEntrataGestioneResponse annullaCapitoloEntrataGestione() {
	
		AnnullaCapitoloEntrataGestione req = new AnnullaCapitoloEntrataGestione();
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloEntrataGestione capitoloEntrataGestione = getCapitoloEntrataGestione();		
		req.setCapitoloEntrataGest(capitoloEntrataGestione);

		AnnullaCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.annullaCapitoloEntrataGestione(req);
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
						
		RicercaPuntualeCapitoloEntrataGestioneResponse ricercaPuntualeResponse = ricercaPuntuale(capitoloEntrataGestioneService);
		/*String msgCapNonEsiste = "Capitolo da annullare non trovato! Il capitolo deve esistere per poter testare l'annullamento!";
		assertNotNull(msgCapNonEsiste, ricercaPuntualeResponse);
		assertEquals(msgCapNonEsiste, Esito.SUCCESSO, ricercaPuntualeResponse.getEsito());*/
		
		if (ricercaPuntualeResponse.getCapitoloEntrataGestione() != null)
		{
			this.uidCapi = ricercaPuntualeResponse.getCapitoloEntrataGestione().getUid();
			log.debug("Capitolo da aggiornare trovato con uid: "+this.uidCapi);
		}
		else this.uidCapi = 1;
	}
}
