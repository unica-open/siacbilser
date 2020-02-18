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
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitoloResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class CalcoloDisponibilitaCapitoloServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class CalcoloDisponibilitaCapitoloServiceTest extends CapitoloBilancioMainTest
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
    	return spreadsheetData("CalcolaDisponibilita");    	
    }

	/**
	 * Instantiates a new calcolo disponibilita capitolo service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidBil the uid bil
	 * @param annoBil the anno bil
	 * @param annocapi the annocapi
	 * @param numecapi the numecapi
	 * @param tipodisp the tipodisp
	 * @param faseBil the fase bil
	 * @param statoBil the stato bil
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public CalcoloDisponibilitaCapitoloServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String uidBil, String annoBil, 
			String annocapi, String numecapi, String tipodisp, String faseBil,
			String statoBil, String Esito,	String Errore, String Descrizione, 
			String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.uidBil = toInteger(uidBil);		
        this.annoBil = toInteger(annoBil);		
        this.annoCapi = toInteger(annocapi);		
        this.numCapi = toInteger(numecapi);		
        this.tipodisp = tipodisp;		
        this.faseBil = faseBil;		
        this.statoBil = statoBil;		
        this.esito = Esito;
        this.errore = Errore;
        this.descrizione = Descrizione;
        this.testOut = Testout;
    }
	
	/**
	 * Test calcolo disponibilita.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCalcoloDisponibilita() throws Throwable
	{
			
		CalcoloDisponibilitaDiUnCapitoloResponse res = calcoloDisponibilitaDiUnCapitolo();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
				
		//TESTA GLI OGGETTI DI RITORNO 
		assertNotNull("calcolo disponibilita richiesta restituito null", res.getDisponibilitaRichiesta());
	}

	/**
	 * Calcolo disponibilita di un capitolo.
	 *
	 * @return the calcolo disponibilita di un capitolo response
	 */
	private CalcoloDisponibilitaDiUnCapitoloResponse calcoloDisponibilitaDiUnCapitolo() {
		
		CalcoloDisponibilitaDiUnCapitolo req = new CalcoloDisponibilitaDiUnCapitolo();		
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setBilancio(getBilancioTest());
		req.setFase(getFaseStato(faseBil, statoBil));
		req.setAnnoCapitolo(annoCapi);
		req.setNumroCapitolo(numCapi);
		req.setTipoDisponibilitaRichiesta(tipodisp);
		
		CalcoloDisponibilitaDiUnCapitoloResponse res = capitoloService.calcoloDisponibilitaDiUnCapitolo(req);
		return res;
	}
}
