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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaImpegniCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.model.Impegno;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaImpegniCapitoloUscitaGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaImpegniCapitoloUscitaGestioneServiceTest extends CapitoloUscitaGestioneMainTest 
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
    	return spreadsheetData("RicercaImpegni");
    }

	/**
	 * Instantiates a new ricerca impegni capitolo uscita gestione service test.
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
	public RicercaImpegniCapitoloUscitaGestioneServiceTest(String obiettivoTest, 
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
	 * Test ricerca impegni.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaImpegni() throws Throwable
	{
			
		RicercaImpegniCapitoloUscitaGestioneResponse res = ricercaImpegniCapitoloUscitaGestione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
				return;
		}
				
		//TESTA GLI OGGETTI DI RITORNO 
		
		// LISTA IMPEGNI
		assertNotNull("Lista Impegni Capitolo Uscita restituita null dalla ricerca impegni", res.getListaImpegni());

		List<Impegno> LImpegni = res.getListaImpegni();
		if (LImpegni != null)
		{
			Iterator<Impegno> Impegni = LImpegni.iterator();
			while (Impegni.hasNext())
			{
				assertNotNull("Impegno Capitolo Uscita restituita null dalla ricerca impegni", Impegni.next());
			}
		}
	}

	/**
	 * Ricerca impegni capitolo uscita gestione.
	 *
	 * @return the ricerca impegni capitolo uscita gestione response
	 */
	private RicercaImpegniCapitoloUscitaGestioneResponse ricercaImpegniCapitoloUscitaGestione() {

		RicercaImpegniCapitoloUscitaGestione req = new RicercaImpegniCapitoloUscitaGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloUscitaGestione capitoloUscitaGestione = getCapitoloUscitaGestione();
		req.setCapitoloUscitaGestione(capitoloUscitaGestione);

		RicercaImpegniCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaImpegniCapitoloUscitaGestione(req);

		return res;		
	}
}
