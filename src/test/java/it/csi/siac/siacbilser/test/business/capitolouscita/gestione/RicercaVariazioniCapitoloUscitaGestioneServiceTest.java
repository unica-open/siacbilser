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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaVariazioniCapitoloUscitaGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaVariazioniCapitoloUscitaGestioneServiceTest extends CapitoloUscitaGestioneMainTest
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
    	return spreadsheetData("RicercaVariazioni");
    }

	/**
	 * Instantiates a new ricerca variazioni capitolo uscita gestione service test.
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
	 */
	public RicercaVariazioniCapitoloUscitaGestioneServiceTest(String obiettivoTest,
			String uidEnte, String CFisc, String uidBil, String annoBil, String annoCapi, 
			String numeCapi, String numeArti,  String numUEB, String statoCapi, String descCapi, 
			String annoCrea, String Esito, String Errore, String Descrizione, String Testout) 
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
    }

	/**
	 * Test ricerca variazioni.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaVariazioni() throws Throwable
	{        	

		RicercaVariazioniCapitoloUscitaGestioneResponse res = ricercaVariazioniCapitoloUscitaGestione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		//TESTA GLI OGGETTI DI RITORNO :
		// LISTA VARIAZIONI CODIFICHE
		assertNotNull("Lista Variazioni Codifiche restituita null dalla ricerca variazioni", res.getListaVariazioniCodifiche());
		List<VariazioneCodificaCapitolo> LVarCodif = res.getListaVariazioniCodifiche();
		if (LVarCodif != null)
		{
			Iterator<VariazioneCodificaCapitolo> VarCodif = LVarCodif.iterator();
			while (VarCodif.hasNext())
			{
				assertNotNull("Variazione Codifica restituita null dalla ricerca variazioni", VarCodif.next());
			}
		}				

		// LISTA VARIAZIONI IMPORTI
		assertNotNull("Lista Variazioni Importi restituita null dalla ricerca variazioni", res.getListaVariazioniImporti());
		List<VariazioneImportoCapitolo> LVarImpo = res.getListaVariazioniImporti();
		if (LVarImpo != null)
		{
			Iterator<VariazioneImportoCapitolo> VarImpo = LVarImpo.iterator();
			while (VarImpo.hasNext())
			{
				assertNotNull("Variazione Importo restituita null dalla ricerca variazioni", VarImpo.next());
			}
		}
	}

	/**
	 * Ricerca variazioni capitolo uscita gestione.
	 *
	 * @return the ricerca variazioni capitolo uscita gestione response
	 */
	private RicercaVariazioniCapitoloUscitaGestioneResponse ricercaVariazioniCapitoloUscitaGestione() {

		RicercaVariazioniCapitoloUscitaGestione req = new RicercaVariazioniCapitoloUscitaGestione();
				
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		CapitoloUscitaGestione capitoloUscitaGestione = getCapitoloUscitaGestione();
		capitoloUscitaGestione.setEnte(getEnteTest());
		capitoloUscitaGestione.setBilancio(getBilancioTest());
		req.setCapitoloUscitaGest(capitoloUscitaGestione);
		
		RicercaVariazioniCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaVariazioniCapitoloUscitaGestione(req);

		return res;		
	}
}
