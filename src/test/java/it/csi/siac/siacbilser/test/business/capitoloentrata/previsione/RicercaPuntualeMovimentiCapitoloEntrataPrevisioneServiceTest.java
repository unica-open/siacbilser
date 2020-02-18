/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione;

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

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeMovimentiCapitoloEntrataPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaPuntualeMovimentiCapitoloEntrataPrevisioneServiceTest  extends 
		CapitoloEntrataPrevisioneMainTest
{

	/** The capitolo entrata previsione service. */
	@Autowired
	private CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	   
    /**
     * Spreadsheet data.
     *
     * @return the collection
     * @throws Exception the exception
     */
    @Parameters
    public static Collection<Object[]> spreadsheetData() throws Exception {
    	return spreadsheetData("RicercaPuntualeMovimenti");    	
    }

	/**
	 * Instantiates a new ricerca puntuale movimenti capitolo entrata previsione service test.
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
	public RicercaPuntualeMovimentiCapitoloEntrataPrevisioneServiceTest(String obiettivoTest,
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
	 * Test ricerca puntuale movimenti.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaPuntualeMovimenti() throws Throwable
	{	
		
		RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse res = ricercaPuntualeMovimentiCapitoloEntrataPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		//TESTA GLI OGGETTI DI RITORNO :
		// LISTA VARIAZIONE CODIFICHE
		assertNotNull("Lista Variazioni Codifiche restituita null dalla ricerca puntuale movimenti", res.getListaVariazioneCodifiche());
				
		List<VariazioneCodificaCapitolo> varcodcapi = res.getListaVariazioneCodifiche();
		if (varcodcapi != null)
		{
			Iterator<VariazioneCodificaCapitolo> varcod = varcodcapi.iterator();
			while (varcod.hasNext())
			{
				assertNotNull("Variazione Codifica restituita null dalla ricerca puntuale movimenti", varcod.next());
			}
		}
			
		// LISTA VARIAZIONE IMPORTI
		assertNotNull("Lista Variazioni Importi restituita null dalla ricerca puntuale movimenti", res.getListaVariazioneImporti());
		
		List<VariazioneImportoCapitolo> varimpcapi = res.getListaVariazioneImporti();
		if (varimpcapi != null)
		{
			Iterator<VariazioneImportoCapitolo> varimp = varimpcapi.iterator();
			while (varimp.hasNext())
			{
				assertNotNull("Variazione Importo restituita null dalla ricerca puntuale movimenti", varimp.next());
			}
		}
			
		// LISTA VINCOLI
		assertNotNull("Lista Vincoli Capitolo restituita null dalla ricerca puntuale movimenti", res.getListaVincoli());
		
		List<VincoloCapitoli> vinccapi = res.getListaVincoli();
		if (vinccapi != null)
		{
			Iterator<VincoloCapitoli> vcapi = vinccapi.iterator();
			while (vcapi.hasNext())
			{
				assertNotNull("Vincolo Capitolo restituito null dalla ricerca puntuale movimenti", vcapi.next());
			}
		}
	}

	/**
	 * Ricerca puntuale movimenti capitolo entrata previsione.
	 *
	 * @return the ricerca puntuale movimenti capitolo entrata previsione response
	 */
	private RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse ricercaPuntualeMovimentiCapitoloEntrataPrevisione() {

		RicercaPuntualeMovimentiCapitoloEntrataPrevisione req = new RicercaPuntualeMovimentiCapitoloEntrataPrevisione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = getCapitoloEntrataPrevisione();
		req.setCapitoloEntrataPrev(capitoloEntrataPrevisione);

		RicercaPuntualeMovimentiCapitoloEntrataPrevisioneResponse res = capitoloEntrataPrevisioneService.ricercaPuntualeMovimentiCapitoloEntrataPrevisione(req);

		return res;		
	}
}
