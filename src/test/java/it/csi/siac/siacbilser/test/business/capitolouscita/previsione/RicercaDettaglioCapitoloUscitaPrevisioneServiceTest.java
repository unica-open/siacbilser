/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioCapitoloUscitaPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaDettaglioCapitoloUscitaPrevisioneServiceTest extends CapitoloUscitaPrevisioneMainTest 
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
    	return spreadsheetData("RicercaDettaglio");
    }

	/**
	 * Instantiates a new ricerca dettaglio capitolo uscita previsione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param annoEser the anno eser
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @param numUEB the num ueb
	 * @param StatoCapi the stato capi
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public RicercaDettaglioCapitoloUscitaPrevisioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String annoEser, String annoCapi, String numeCapi, 
			String numeArti, String numUEB, String StatoCapi, String Esito, 
			String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
		this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.annoEser = toInteger(annoEser);	
		this.annoCapi = toInteger(annoCapi);		
   	    this.numCapi = toInteger(numeCapi);		
   	    this.numArti = toInteger(numeArti);	
        this.numUEB = toInteger(numUEB);
      	this.statoCapi = StatoCapi;   
        this.esito = Esito;
        this.errore = Errore;
        this.descrizione = Descrizione;
        this.testOut = Testout;
    }

	/**
	 * Test ricerca dettaglio.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaDettaglio() throws Throwable
	{

		InizializzaUidCapitoloSeValorizzataChiaveRicerca();
			
		RicercaDettaglioCapitoloUscitaPrevisioneResponse res = ricercaDettaglioCapitoloUscitaPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
	
		//TESTA GLI OGGETTI DI RITORNO :
		
		// ANNO DI BILANCIO
		assertNotNull(res.getBilancio());
		assertEquals("Anno di bilancio diverso da quello ricercato.", annoBil.intValue(), res.getBilancio().getAnno());
		
		// CAPITOLO USCITA PREVISIONE 
		assertNotNull("Capitolo restituito null dalla ricerca dettaglio",res.getCapitoloUscitaPrevisione());

		CapitoloUscitaPrevisione capitoloRicercaDettaglio = res.getCapitoloUscitaPrevisione();
		
		// NUMERO CAPITOLO
		assertNotNull(capitoloRicercaDettaglio.getNumeroCapitolo());
		assertEquals("Numero di capitolo diverso da quello ricercato", numCapi, capitoloRicercaDettaglio.getNumeroCapitolo());
		
		// NUMERO ARTICOLO
		assertNotNull(capitoloRicercaDettaglio.getNumeroArticolo());
		assertEquals("Numero articolo diverso da quello ricercato", numArti,capitoloRicercaDettaglio.getNumeroArticolo());
		
		// NUMERO UEB
		assertNotNull(capitoloRicercaDettaglio.getNumeroUEB());
		assertEquals("Numero ueb diverso da quello ricercato", numUEB,capitoloRicercaDettaglio.getNumeroUEB());

		// STATO (STATO OPERATIVO ELEMENTO DI BILANCIO)
		/*
		 * assertNotNull(capi.getStatoOperativoElementoDiBilancio());
		 * assertEquals("", "VALIDO",
		 * capi.getStatoOperativoElementoDiBilancio().toString());
		 */
			
		// DESCRIZIONE CAPITOLO
    	assertNotNull("Descrizione restituita null dalla ricerca dettaglio", capitoloRicercaDettaglio.getDescrizione());
    		
    	// ANNO CREAZIONE CAPITOLO
    	assertNotNull("Anno Creazione Capitolo restituito null dalla ricerca dettaglio", capitoloRicercaDettaglio.getAnnoCreazioneCapitolo());
    			
		//assertNotNull(capi.getElementoPianoDeiConti());
   		//assertNotNull(capi.getStrutturaAmministrativoContabile());
   		//assertNotNull(capi.getImportiCapitoloUP());
   		//assertNotNull(capi.getMacroaggregato());
   		//assertNotNull(capi.getProgramma());

   		/*assertNotNull(capi.getDescrizioneArticolo());
		assertNotNull(capi.getDisponibilitaVariare());
		assertNotNull(capi.getElementoPianoDeiConti());
		assertNotNull(capi.getExAnnoCapitolo());
		assertNotNull(capi.getExArticolo());
		assertNotNull(capi.getExCapitolo());
		assertNotNull(capi.getExUEB());
		assertNotNull(capi.getFlagRilevanteIva());
		assertNotNull(capi.getFondoPluriennaleVinc());
		assertNotNull(capi.getFondoPluriennaleVincPrec());
		assertNotNull(capi.getMissione());
		assertNotNull(capi.getNote());
		assertNotNull(capi.getProgramma());
		assertNotNull(capi.getTitoloSpesa());*/
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
