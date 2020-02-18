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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.DocumentoDiEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaMovimentiCapitoloEntrataGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaMovimentiCapitoloEntrataGestioneServiceTest extends CapitoloEntrataGestioneMainTest 
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
    	return spreadsheetData("RicercaMovimenti");
    }

	/**
	 * Instantiates a new ricerca movimenti capitolo entrata gestione service test.
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
	public RicercaMovimentiCapitoloEntrataGestioneServiceTest(String obiettivoTest, 
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
	 * Test ricerca movimenti.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaMovimenti() throws Throwable
	{        	
		
		RicercaMovimentiCapitoloEntrataGestioneResponse res = ricercaMovimentiCapitoloEntrataGestione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		//TESTA GLI OGGETTI DI RITORNO :
		// LISTA VARIAZIONI CODIFICHE
		assertNotNull("Lista Variazioni Codifiche Capitolo Entrata restituita null dalla ricerca movimenti", res.getListaVariazioneCodifiche());
		
		List<VariazioneCodificaCapitolo> LVarCodif = res.getListaVariazioneCodifiche();
		if (LVarCodif != null)
		{
			Iterator<VariazioneCodificaCapitolo> VarCodif = LVarCodif.iterator();
			while (VarCodif.hasNext())
			{
				assertNotNull("Variazione Codifica Capitolo Entrata restituita null dalla ricerca movimenti", VarCodif.next());
			}
		}
				
		// LISTA VARIAZIONE IMPORTI
		assertNotNull("Lista Variazioni Importi Capitolo Entrata restituita null dalla ricerca movimenti", res.getListaVariazioneImporti());
		
		List<VariazioneImportoCapitolo> LVarImpo = res.getListaVariazioneImporti();
		if (LVarImpo != null)
		{
			Iterator<VariazioneImportoCapitolo> VarImpo = LVarImpo.iterator();
			while (VarImpo.hasNext())
			{
				assertNotNull("Variazione Importo Capitolo Entrata restituita null dalla ricerca movimenti", VarImpo.next());
			}
		}
				
		// LISTA VINCOLI
		assertNotNull("Lista Vincoli Capitolo Entrata restituita null dalla ricerca movimenti", res.getListaVincoli());
		
		List<VincoloCapitoli> LVincoliUEGest = res.getListaVincoli();
		if (LVincoliUEGest != null)
		{
			Iterator<VincoloCapitoli> VincoliUEGest = LVincoliUEGest.iterator();
			while (VincoliUEGest.hasNext())
			{
				assertNotNull("Vincolo Capitolo Entrata restituito null dalla ricerca movimenti", VincoliUEGest.next());
			}
		}
				
		// LISTA Accertamenti
		assertNotNull("Lista Accertamenti Capitolo Entrata null dalla ricerca movimenti", res.getListaAccertamenti());
		
		List<Accertamento> LAccert = res.getListaAccertamenti();
		if (LAccert != null)
		{
			Iterator<Accertamento> Accert = LAccert.iterator();
			while (Accert.hasNext())
			{
				assertNotNull("Accertamento Capitolo Entrata restituito null dalla ricerca movimenti", Accert.next());
			}
		}
				
		// LISTA DOCUMENTO SPESA
		assertNotNull("Lista Documenti Capitolo Entrata restituita null dalla ricerca movimenti", res.getDocumentiEntrata());
		
		List<DocumentoDiEntrata> LDocEntrata = res.getDocumentiEntrata();
		if (LDocEntrata != null)
		{
			Iterator<DocumentoDiEntrata> DocEntrata = LDocEntrata.iterator();
			while (DocEntrata.hasNext())
			{
				assertNotNull("Documento Capitolo Entrata restituito null dalla ricerca movimenti", DocEntrata.next());
			}
		}
	}

	/**
	 * Ricerca movimenti capitolo entrata gestione.
	 *
	 * @return the ricerca movimenti capitolo entrata gestione response
	 */
	private RicercaMovimentiCapitoloEntrataGestioneResponse ricercaMovimentiCapitoloEntrataGestione() {

		RicercaMovimentiCapitoloEntrataGestione req = new RicercaMovimentiCapitoloEntrataGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloEntrataGestione capitoloEntrataGestione = getCapitoloEntrataGestione();
		req.setCapitoloEntrataGestione(capitoloEntrataGestione);

		RicercaMovimentiCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.ricercaMovimentiCapitoloEntrataGestione(req);

		return res;		
	}
}
