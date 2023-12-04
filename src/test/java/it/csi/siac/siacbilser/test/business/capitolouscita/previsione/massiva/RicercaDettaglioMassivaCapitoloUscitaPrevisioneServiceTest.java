/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione.massiva;

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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioMassivaCapitoloUscitaPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaDettaglioMassivaCapitoloUscitaPrevisioneServiceTest extends CapitoloUscitaPrevisioneMassivaMainTest 
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
    	return spreadsheetDataMassiva("RicercaDettaglio");
    }
    
	/**
	 * Instantiates a new ricerca dettaglio massiva capitolo uscita previsione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param annoEser the anno eser
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public RicercaDettaglioMassivaCapitoloUscitaPrevisioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String annoEser, String annoCapi, String numeCapi, 
			String numeArti, String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
		this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.annoEser = toInteger(annoEser);	
		this.annoCapi = toInteger(annoCapi);		
   	    this.numCapi = toInteger(numeCapi);		
   	    this.numArti = toInteger(numeArti);	
        this.esito = Esito;
        this.errore = Errore;
        this.descrizione = Descrizione;
        this.testOut = Testout;
    }

	/**
	 * Test ricerca dettaglio massiva.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaDettaglioMassiva() throws Throwable
	{
		
		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse res = ricercaDettaglioMassivaCapitoloUscitaPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
			
		//TESTA GLI OGGETTI DI RITORNO :
		// CAPITOLO USCITA Previsione TROVATO
		assertNotNull(res.getCapitoloMassivaUscitaPrevisione());
				
		CapitoloMassivaUscitaPrevisione capi = res.getCapitoloMassivaUscitaPrevisione();
		
		// ANNO CAPITOLO
		assertNotNull(capi.getAnnoCapitolo());
		assertEquals("Anno Capitolo diverso da quello ricercato", annoCapi, capi.getAnnoCapitolo());	

		// NUMERO CAPITOLO
		assertNotNull(capi.getNumeroCapitolo());
		assertEquals("Numero Capitolo diverso da quello ricercato", numCapi, capi.getNumeroCapitolo());
					
		// NUMERO ARTICOLO	
   		assertNotNull(capi.getNumeroArticolo());	
		assertEquals("Numero Articolo diverso da quello ricercato", numArti,  capi.getNumeroArticolo());					
					
		// NUMERO UEB	
   		assertNotNull(capi.getNumeroUEB());
			
		// DESCRIZIONE CAPITOLO
    	assertNotNull(capi.getDescrizione());
    		
    	// ANNO CREAZIONE CAPITOLO
    	assertNotNull(capi.getAnnoCreazioneCapitolo());
	}

	/**
	 * Ricerca dettaglio massiva capitolo uscita previsione.
	 *
	 * @return the ricerca dettaglio massiva capitolo uscita previsione response
	 */
	private RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse ricercaDettaglioMassivaCapitoloUscitaPrevisione() 
	{
		RicercaSinteticaCapitoloUPrev criteriRicerca = getCriteriRicercas(annoEser, annoCapi, numCapi, numArti);

		RicercaDettaglioMassivaCapitoloUscitaPrevisione req = new RicercaDettaglioMassivaCapitoloUscitaPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);

		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.ricercaDettaglioMassivaCapitoloUscitaPrevisione(req);
		return res;
	}
}
