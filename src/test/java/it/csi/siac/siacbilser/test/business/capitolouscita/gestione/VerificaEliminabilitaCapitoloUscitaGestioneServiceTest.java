/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.gestione;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class VerificaEliminabilitaCapitoloUscitaGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class VerificaEliminabilitaCapitoloUscitaGestioneServiceTest extends CapitoloUscitaGestioneMainTest
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
    	return spreadsheetData("VerificaElimina");
    }

	/**
	 * Instantiates a new verifica eliminabilita capitolo uscita gestione service test.
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
	 * @param iselimin the iselimin
	 */
	public VerificaEliminabilitaCapitoloUscitaGestioneServiceTest(String obiettivoTest,
			String uidEnte, String CFisc, String uidBil, String annoBil, String annoCapi,
			String numeCapi, String numeArti, String numUEB, String statoCapi, String descCapi, 
			String annoCrea, String Esito, String Errore, String Descrizione, String Testout, 
			String iselimin) 
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
        this.iselimin = false;
        if (iselimin.matches("SI")) this.iselimin = true;	
    }

	/**
	 * Test verifica eliminabilita.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testVerificaEliminabilita() throws Throwable
	{

		VerificaEliminabilitaCapitoloUscitaGestioneResponse res = verificaEliminabilitaCapitoloUscitaGestione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
	
		//TESTA GLI OGGETTI DI RITORNO
		// CAPITOLO ELIMINABILE 0=NO / 1=SI
		assertEquals("Risultato verifica eliminabilita diverso da atteso!!", iselimin, res.isEliminabilitaCapitolo());
	}

	/**
	 * Verifica eliminabilita capitolo uscita gestione.
	 *
	 * @return the verifica eliminabilita capitolo uscita gestione response
	 */
	private VerificaEliminabilitaCapitoloUscitaGestioneResponse verificaEliminabilitaCapitoloUscitaGestione() {

		VerificaEliminabilitaCapitoloUscitaGestione req = new VerificaEliminabilitaCapitoloUscitaGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloUscitaGestione capitoloUscitaGestione = getCapitoloUscitaGestione();
		req.setCapitoloUscitaGest(capitoloUscitaGestione);
		
		VerificaEliminabilitaCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.verificaEliminabilitaCapitoloUscitaGestione(req);

		return res;		
	}

}
