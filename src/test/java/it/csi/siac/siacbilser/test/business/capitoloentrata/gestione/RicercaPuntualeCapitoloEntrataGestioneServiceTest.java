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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeCapitoloEntrataGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaPuntualeCapitoloEntrataGestioneServiceTest  extends 
		CapitoloEntrataGestioneMainTest
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
		return spreadsheetData("RicercaPuntuale");	 
	}

	/**
	 * Instantiates a new ricerca puntuale capitolo entrata gestione service test.
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
	public RicercaPuntualeCapitoloEntrataGestioneServiceTest(String obiettivoTest, 
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
	 * Test ricerca puntuale.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaPuntuale() throws Throwable
	{       
		RicercaPuntualeCapitoloEntrataGestioneResponse res = ricercaPuntuale(capitoloEntrataGestioneService);
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		//TESTA GLI OGGETTI DI RITORNO :
		// ANNO DI BILANCIO
		assertNotNull(res.getCapitoloEntrataGestione().getBilancio());
		assertEquals("Anno di bilancio diverso da quello ricercato.", annoEser.intValue(), res.getCapitoloEntrataGestione().getBilancio().getAnno());
				
		// CAPITOLO USCITA Gestione 
		assertNotNull("Capitolo restituito null dalla ricerca puntuale", res.getCapitoloEntrataGestione());
				
		CapitoloEntrataGestione capi = res.getCapitoloEntrataGestione();

		// NUMERO CAPITOLO
		assertNotNull(capi.getNumeroCapitolo());
		assertEquals("Numero di capitolo diverso da quello ricercato", numCapi, capi.getNumeroCapitolo());
					
		// NUMERO ARTICOLO	
		assertNotNull(capi.getNumeroArticolo());							
		assertEquals("Numero articolo diverso da quello ricercato", numArti,  capi.getNumeroArticolo());					
					
		// NUMERO UEB	
		assertNotNull(capi.getNumeroUEB());
		assertEquals("Numero ueb diverso da quello ricercato", numUEB,  capi.getNumeroUEB());
								
		/* IMPORTI CAPITOLO			
		assertNotNull(capi.getImportiCapitoloUP());
		assertEquals("", annoEser+1, capi.getImportiCapitoloUP().getAnnoCompetenza());

		assertEquals("", (long)(importo + 0.3), capi.getImportiCapitoloUP().getStanziamento().longValue());
		assertEquals("", (long)(importo + 20000.5), capi.getImportiCapitoloUP().getStanziamentoCassa().longValue());
		assertEquals("", (long)(importo + 10000.7), capi.getImportiCapitoloUP().getStanziamentoResiduo().longValue());*/
				
		/* STATO (STATO OPERATIVO ELEMENTO DI BILANCIO)
		assertNotNull(capi.getStatoOperativoElementoDiBilancio());
		assertEquals("", StatoCapi, capi.getStatoOperativoElementoDiBilancio().toString());*/					
					
		/* ELEMENTO PIANO dei CONTI
		assertNotNull(capi.getElementoPianoDeiConti());
		assertEquals("", uidPiano, capi.getElementoPianoDeiConti().getUid());*/

		/* ELEMENTO STRUTTURA AMMIN. CONTABILE
		assertNotNull(capi.getStrutturaAmministrativoContabile());
		assertEquals("", uidStrutt, capi.getStrutturaAmministrativoContabile().getUid());*/
	}
}
