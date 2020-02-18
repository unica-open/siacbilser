/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione;

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

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaGestioneCapitoloEntrataPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class AnnullaGestioneCapitoloEntrataPrevisioneServiceTest extends CapitoloEntrataPrevisioneMainTest
{

	/** The capitolo entrata previsione service. */
	@Autowired
	private CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;

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
	 * Instantiates a new annulla gestione capitolo entrata previsione service test.
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
	public AnnullaGestioneCapitoloEntrataPrevisioneServiceTest(String obiettivoTest,
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
		
		AnnullaCapitoloEntrataPrevisioneResponse res = annullaCapitoloEntrataPrevisione();
			
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		//TESTA GLI OGGETTI DI RITORNO
		/*FACCIO UNA RICERCA DI DETTAGLIO SUL CAPITOLO APPENA ANNULLATO 
		E TESTO CHE LO STATO OPERATIVO SIA PASSATO DA "VALIDO" AD "ANNULLATO" 
		E TESTO CHE LA DATA ANNULLAMENTO CI SIA E CHE SIA UGUALE ALLA DATA ATTUALE */
		
		RicercaDettaglioCapitoloEntrataPrevisioneResponse ricercaDettaglioResponse = ricercaDettaglioCapitoloEntrataPrevisione();
				
		assertEquals("Fallita ricerca dettaglio del capitolo appena annullato.", Esito.SUCCESSO, ricercaDettaglioResponse.getEsito());
					
		// CAPITOLO USCITA PREVISIONE ANNULLATO
		CapitoloEntrataPrevisione capitoloAnnullato = ricercaDettaglioResponse.getCapitoloEntrataPrevisione();

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
	 * Ricerca dettaglio capitolo entrata previsione.
	 *
	 * @return the ricerca dettaglio capitolo entrata previsione response
	 */
	private RicercaDettaglioCapitoloEntrataPrevisioneResponse ricercaDettaglioCapitoloEntrataPrevisione() {
		RicercaDettaglioCapitoloEPrev criteriRicerca = getCriteriRicercaDettaglio(uidCapi);

		RicercaDettaglioCapitoloEntrataPrevisione req = new RicercaDettaglioCapitoloEntrataPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaDettaglioCapitoloEPrev(criteriRicerca);

		RicercaDettaglioCapitoloEntrataPrevisioneResponse res = capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(req);
		return res;
	}


	/**
	 * Annulla capitolo entrata previsione.
	 *
	 * @return the annulla capitolo entrata previsione response
	 */
	private AnnullaCapitoloEntrataPrevisioneResponse annullaCapitoloEntrataPrevisione() {
	
		AnnullaCapitoloEntrataPrevisione req = new AnnullaCapitoloEntrataPrevisione();
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = getCapitoloEntrataPrevisione();		
		req.setCapitoloEntrataPrev(capitoloEntrataPrevisione);

		AnnullaCapitoloEntrataPrevisioneResponse res = capitoloEntrataPrevisioneService.annullaCapitoloEntrataPrevisione(req);
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
						
		RicercaPuntualeCapitoloEntrataPrevisioneResponse ricercaPuntualeResponse = ricercaPuntuale(capitoloEntrataPrevisioneService);
		/*String msgCapNonEsiste = "Capitolo da aggiornare non trovato! Il capitolo deve esistere per poter testare l'aggiornamento!";
		assertNotNull(msgCapNonEsiste, ricercaPuntualeResponse);
		assertEquals(msgCapNonEsiste, Esito.SUCCESSO, ricercaPuntualeResponse.getEsito());*/
		
		if (ricercaPuntualeResponse.getCapitoloEntrataPrevisione() != null)
		{
			this.uidCapi = ricercaPuntualeResponse.getCapitoloEntrataPrevisione().getUid();
			log.debug("Capitolo da aggiornare trovato con uid: "+this.uidCapi);
		}
		else this.uidCapi = 1;
	}

}
