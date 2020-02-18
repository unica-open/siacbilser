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
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class VerificaAnnullabilitaCapitoloUscitaGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class VerificaAnnullabilitaCapitoloUscitaGestioneServiceTest extends CapitoloUscitaGestioneMainTest 
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
    	return spreadsheetData("VerificaAnnulla");    	
    }

	/**
	 * Instantiates a new verifica annullabilita capitolo uscita gestione service test.
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
	 * @param isannull the isannull
	 */
	public VerificaAnnullabilitaCapitoloUscitaGestioneServiceTest(String obiettivoTest,
			String uidEnte, String CFisc, String uidBil, String annoBil, String annoCapi, 
			String numeCapi, String numeArti, String numUEB, String statoCapi, String descCapi, 
			String annoCrea, String Esito, String Errore, String Descrizione, String Testout,
			String isannull) 
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
        this.isannull = false;
        if (isannull.matches("SI")) this.isannull = true;		
	}

	/**
	 * Test verifica annullabilta.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testVerificaAnnullabilta() throws Throwable
	{
		
		VerificaAnnullabilitaCapitoloUscitaGestioneResponse res = verificaAnnullabilitaCapitoloUscitaGestione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
			
		//TESTA GLI OGGETTI DI RITORNO
		// CAPITOLO ANNULLABILE 0=NO / 1=SI
		assertEquals("Risultato verifica annullabilita diverso da atteso!!", isannull, res.isAnnullabilitaCapitolo());
	}

	/**
	 * Verifica annullabilita capitolo uscita gestione.
	 *
	 * @return the verifica annullabilita capitolo uscita gestione response
	 */
	private VerificaAnnullabilitaCapitoloUscitaGestioneResponse verificaAnnullabilitaCapitoloUscitaGestione() {

		VerificaAnnullabilitaCapitoloUscitaGestione req = new VerificaAnnullabilitaCapitoloUscitaGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloUscitaGestione capitoloUscitaGestione = getCapitoloUscitaGestione();
		req.setCapitolo(capitoloUscitaGestione);
		
		VerificaAnnullabilitaCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.verificaAnnullabilitaCapitoloUscitaGestione(req);

		return res;		
	}
}
