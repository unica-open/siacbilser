/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione;

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

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.model.DocumentoDiEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDocumentiCapitoloEntrataGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaDocumentiCapitoloEntrataGestioneServiceTest extends CapitoloEntrataGestioneMainTest
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
    	return spreadsheetData("RicercaDocumenti");
    }

	/**
	 * Instantiates a new ricerca documenti capitolo entrata gestione service test.
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
	public RicercaDocumentiCapitoloEntrataGestioneServiceTest(String obiettivoTest, 
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
	 * Test ricerca documenti.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaDocumenti() throws Throwable
	{
			
		RicercaDocumentiCapitoloEntrataGestioneResponse res = ricercaDocumentiCapitoloEntrataGestione();

		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
				
		//TESTA GLI OGGETTI DI RITORNO 
		
		// LISTA DOCUMENTI ENTRATA
		assertNotNull("Lista Documenti Capitolo Entrata restituita null dalla ricerca documenti", res.getListaDocumentiDiEntrata());

		List<DocumentoDiEntrata> LDocEntrata = res.getListaDocumentiDiEntrata();
		if (LDocEntrata != null)
		{
			Iterator<DocumentoDiEntrata> DocEntrata = LDocEntrata.iterator();
			while (DocEntrata.hasNext())
			{
				assertNotNull("Documento Capitolo Entrata restituito null dalla ricerca documenti", DocEntrata.next());
			}
		}
	}

	/**
	 * Ricerca documenti capitolo entrata gestione.
	 *
	 * @return the ricerca documenti capitolo entrata gestione response
	 */
	private RicercaDocumentiCapitoloEntrataGestioneResponse ricercaDocumentiCapitoloEntrataGestione() {

		RicercaDocumentiCapitoloEntrataGestione req = new RicercaDocumentiCapitoloEntrataGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloEntrataGestione capitoloEntrataGestione = getCapitoloEntrataGestione();
		req.setCapitoloEntrataGestione(capitoloEntrataGestione);

		RicercaDocumentiCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.ricercaDocumentiCapitoloEntrataGestione(req);

		return res;		
	}
}
