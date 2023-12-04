/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioCapitoloEntrataGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaDettaglioCapitoloEntrataGestioneServiceTest extends CapitoloEntrataGestioneMainTest 
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
    	return spreadsheetData("RicercaDettaglio");
    }
    
	/**
	 * Instantiates a new ricerca dettaglio capitolo entrata gestione service test.
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
	public RicercaDettaglioCapitoloEntrataGestioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String annoEser, String annoCapi, String numeCapi, 
			String numeArti, String numUEB, String StatoCapi, String Esito, String Errore, 
			String Descrizione, String Testout) 
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
		
		RicercaDettaglioCapitoloEntrataGestioneResponse res = ricercaDettaglioCapitoloEntrataGestione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
			
		//TESTA GLI OGGETTI DI RITORNO :
		// ANNO DI BILANCIO
		assertNotNull(res.getBilancio());
		assertEquals("", annoEser.intValue(), res.getBilancio().getAnno());
								
		// CAPITOLO USCITA Gestione TROVATO
		assertNotNull(res.getCapitoloEntrataGestione());
				
		CapitoloEntrataGestione capi = res.getCapitoloEntrataGestione();

		// NUMERO CAPITOLO
		assertNotNull(capi.getNumeroCapitolo());
		assertEquals("", numCapi, capi.getNumeroCapitolo());
					
		// NUMERO ARTICOLO	
   		assertNotNull(capi.getNumeroArticolo());	
		assertEquals("", numArti,  capi.getNumeroArticolo());					
					
		// NUMERO UEB	
   		assertNotNull(capi.getNumeroUEB());
		assertEquals("", numUEB,  capi.getNumeroUEB());

		// STATO (STATO OPERATIVO ELEMENTO DI BILANCIO)
		//assertNotNull(capi.getStatoOperativoElementoDiBilancio());
			
		// DESCRIZIONE CAPITOLO
    	assertNotNull(capi.getDescrizione());
    		
    	// ANNO CREAZIONE CAPITOLO
    	assertNotNull(capi.getAnnoCreazioneCapitolo());
    			
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
	
		RicercaPuntualeCapitoloEntrataGestioneResponse ricercaPuntualeResponse = ricercaPuntuale(capitoloEntrataGestioneService);
		/*String msgCapNonEsiste = "Capitolo da eliminare non trovato! Il capitolo deve esistere per poter testare l'eliminazione!";
		assertNotNull(msgCapNonEsiste, ricercaPuntualeResponse);
		assertEquals(msgCapNonEsiste, Esito.SUCCESSO, ricercaPuntualeResponse.getEsito());*/
		
		if (ricercaPuntualeResponse.getCapitoloEntrataGestione() != null)
		{
			this.uidCapi = ricercaPuntualeResponse.getCapitoloEntrataGestione().getUid();
			log.debug(methodName, "Capitolo da aggiornare trovato con uid: "+this.uidCapi);
		}
		else this.uidCapi = 1;
	}
}
