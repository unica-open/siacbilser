/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolobilancio;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisioneResponse;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class CalcolaTotaliStanziamentiPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class CalcolaTotaliStanziamentiPrevisioneServiceTest extends CapitoloBilancioMainTest 
{

	/** The capitolo service. */
	@Autowired
	private CapitoloService capitoloService;

    /**
     * Spreadsheet data.
     *
     * @return the collection
     * @throws Exception the exception
     */
    @Parameters
    public static Collection<Object[]> spreadsheetData() throws Exception {
    	return spreadsheetData("CalcolaTotaliStanziamenti");    	
    }

	/**
	 * Instantiates a new calcola totali stanziamenti previsione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param annoEser the anno eser
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public CalcolaTotaliStanziamentiPrevisioneServiceTest(String obiettivoTest, String uidEnte,
			String CFisc, String annoEser, String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
        this.annoEser = toInteger(annoEser);
        this.esito = Esito;
        this.errore = Errore;
        this.descrizione = Descrizione;
        this.testOut = Testout;
    }

	/**
	 * Test calcola totali stanziamenti previsione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCalcolaTotaliStanziamentiPrevisione() throws Throwable
	{

		CalcolaTotaliStanziamentiDiPrevisioneResponse res = calcolaTotaliStanziamentiDiPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
				
		//TESTA GLI OGGETTI DI RITORNO 
		assertNotNull("totali anno esercizio attuale restituiti null", res.getTotaliAnnoEsercizioAttuale());
		assertNotNull("totali anno esercizio passato restituiti null", res.getTotaliAnnoEsercizioPassato());
		assertNotNull("totali anno esercizio successivo restituiti null", res.getTotaliAnnoEsercizioSuccessivo());
	}

	/**
	 * Calcola totali stanziamenti di previsione.
	 *
	 * @return the calcola totali stanziamenti di previsione response
	 */
	private CalcolaTotaliStanziamentiDiPrevisioneResponse calcolaTotaliStanziamentiDiPrevisione() {
		
		CalcolaTotaliStanziamentiDiPrevisione req = new CalcolaTotaliStanziamentiDiPrevisione();		
			
		Account acc = new Account();
		acc.setEnte(getEnteTest());
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		richiedente.setAccount(acc);

		req.setRichiedente(richiedente);
		req.setAnnoEsercizio(annoEser);
		
		CalcolaTotaliStanziamentiDiPrevisioneResponse res = capitoloService.calcolaTotaliStanziamentiDiPrevisione(req);
		return res;
	}
}
