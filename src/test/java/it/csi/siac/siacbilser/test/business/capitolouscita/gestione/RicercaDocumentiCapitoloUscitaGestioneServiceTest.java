/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.gestione;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.model.DocumentoDiSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDocumentiCapitoloUscitaGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaDocumentiCapitoloUscitaGestioneServiceTest extends CapitoloUscitaGestioneMainTest
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
    	return spreadsheetData("RicercaDocumenti");
    }

	/**
	 * Instantiates a new ricerca documenti capitolo uscita gestione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidBil the uid bil
	 * @param annoBil the anno bil
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
	public RicercaDocumentiCapitoloUscitaGestioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String uidBil, String annoBil, String numeCapi, 
			String numeArti, String numUEB, String statoCapi, String descCapi, String annoCrea,
			String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.uidBil = toInteger(uidBil);		
        this.annoBil = toInteger(annoBil); this.annoEser = toInteger(annoBil);		
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
	 * Test ricerca documenti.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaDocumenti() throws Throwable
	{
		RicercaDocumentiCapitoloUscitaGestioneResponse res = ricercaDocumentiCapitoloUscitaGestione();
			
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
				return;
		}
				
		//TESTA GLI OGGETTI DI RITORNO 
		
		// LISTA DOCUMENTI SPESA
		assertNotNull("Lista Documenti Spesa Capitolo Uscita restituita null dalla ricerca documenti", res.getListaDocumenti());

		List<DocumentoDiSpesa> LDocSpesa = res.getListaDocumenti();
		if (LDocSpesa != null)
		{
			Iterator<DocumentoDiSpesa> DocSpesa = LDocSpesa.iterator();
			while (DocSpesa.hasNext())
			{
				assertNotNull("Documento Spesa Capitolo Uscita restituito null dalla ricerca documenti", DocSpesa.next());
			}
		}
	}

	/**
	 * Ricerca documenti capitolo uscita gestione.
	 *
	 * @return the ricerca documenti capitolo uscita gestione response
	 */
	private RicercaDocumentiCapitoloUscitaGestioneResponse ricercaDocumentiCapitoloUscitaGestione() {

		RicercaDocumentiCapitoloUscitaGestione req = new RicercaDocumentiCapitoloUscitaGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloUscitaGestione capitoloUscitaGestione = getCapitoloUscitaGestione();
		req.setCapitoloUscitaGestione(capitoloUscitaGestione);

		RicercaDocumentiCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaDocumentiCapitoloUscitaGestione(req);

		return res;		
	}
}
