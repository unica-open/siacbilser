/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;

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

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisioneResponse;
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
 * The Class AnnullaGestioneCapitoloUscitaPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class AnnullaGestioneCapitoloUscitaPrevisioneServiceTest extends CapitoloUscitaPrevisioneMainTest
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
    	return spreadsheetData("Annulla");
    }

	/**
	 * Instantiates a new annulla gestione capitolo uscita previsione service test.
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
	public AnnullaGestioneCapitoloUscitaPrevisioneServiceTest(String obiettivoTest, String uidEnte, 
			String CFisc, String uidBil, String annoBil, String annoCapi, String numeCapi, 
			String numeArti, String numUEB, String statoCapi, String descCapi, 
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
	 * Test annulla.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAnnulla() throws Throwable
	{

		InizializzaUidCapitoloSeValorizzataChiaveRicerca();
		
		AnnullaCapitoloUscitaPrevisioneResponse res = annullaCapitoloUscitaPrevisione();
			
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
	
		//TESTA GLI OGGETTI DI RITORNO
		/*FACCIO UNA RICERCA DI DETTAGLIO SUL CAPITOLO APPENA ANNULLATO
		E TESTO CHE LO STATO OPERATIVO SIA PASSATO DA "VALIDO" AD "ANNULLATO" 
		E TESTO CHE LA DATA ANNULLAMENTO CI SIA E CHE SIA UGUALE ALLA DATA ATTUALE */
		
		RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaDettaglioResponse = ricercaDettaglioCapitoloUscitaPrevisione();
				
		assertEquals("Fallita ricerca dettaglio del capitolo appena annullato.", Esito.SUCCESSO, ricercaDettaglioResponse.getEsito());
					
		// CAPITOLO USCITA PREVISIONE ANNULLATO
		CapitoloUscitaPrevisione capitoloAnnullato = ricercaDettaglioResponse.getCapitoloUscitaPrevisione();

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
	 * Annulla capitolo uscita previsione.
	 *
	 * @return the annulla capitolo uscita previsione response
	 */
	private AnnullaCapitoloUscitaPrevisioneResponse annullaCapitoloUscitaPrevisione() {
	
		AnnullaCapitoloUscitaPrevisione req = new AnnullaCapitoloUscitaPrevisione();
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloUscitaPrevisione capitoloUscitaPrevisione = getCapitoloUscitaPrevisione();		
		req.setCapitoloUscitaPrev(capitoloUscitaPrevisione);

		AnnullaCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.annullaCapitoloUscitaPrevisione(req);
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
						
		RicercaPuntualeCapitoloUscitaPrevisioneResponse ricercaPuntualeResponse = ricercaPuntuale(capitoloUscitaPrevisioneService);
		/*String msgCapNonEsiste = "Capitolo da annullare non trovato! Il capitolo deve esistere per poter testare l'annullamento!";
		assertNotNull(msgCapNonEsiste, ricercaPuntualeResponse);
		assertEquals(msgCapNonEsiste, Esito.SUCCESSO, ricercaPuntualeResponse.getEsito());*/
		
		if (ricercaPuntualeResponse.getCapitoloUscitaPrevisione() != null)
		{
			this.uidCapi = ricercaPuntualeResponse.getCapitoloUscitaPrevisione().getUid();
			log.debug("Capitolo da aggiornare trovato con uid: "+this.uidCapi);
		}
		else this.uidCapi = 1;
	}
}
