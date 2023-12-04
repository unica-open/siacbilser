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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.model.DocumentoDiSpesa;
import it.csi.siac.siacfinser.model.Impegno;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaMovimentiCapitoloUscitaGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaMovimentiCapitoloUscitaGestioneServiceTest extends CapitoloUscitaGestioneMainTest 
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
    	return spreadsheetData("RicercaMovimenti");
    }

	/**
	 * Instantiates a new ricerca movimenti capitolo uscita gestione service test.
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
	public RicercaMovimentiCapitoloUscitaGestioneServiceTest(String obiettivoTest,
			String uidEnte, String CFisc, String uidBil, String annoBil, String annoCapi,
			String numeCapi, String numeArti, String numUEB, String statoCapi, 
			String descCapi, String annoCrea, String Esito,
			String Errore, String Descrizione, String Testout) 
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
	 * Test ricerca movimenti.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaMovimenti() throws Throwable
	{        	
		
		RicercaMovimentiCapitoloUscitaGestioneResponse res = ricercaMovimentiCapitoloUscitaGestione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		//TESTA GLI OGGETTI DI RITORNO :
		// LISTA VARIAZIONI CODIFICHE
		assertNotNull("Lista Variazioni Codifiche Capitolo Uscita restituita null dalla ricerca movimenti", res.getListaVariazioneCodifiche());
		
		List<VariazioneCodificaCapitolo> LVarCodif = res.getListaVariazioneCodifiche();
		if (LVarCodif != null)
		{
			Iterator<VariazioneCodificaCapitolo> VarCodif = LVarCodif.iterator();
			while (VarCodif.hasNext())
			{
				assertNotNull("Variazione Codifica Capitolo Uscita restituita null dalla ricerca movimenti", VarCodif.next());
			}
		}
				
		// LISTA VARIAZIONE IMPORTI
		assertNotNull("Lista Variazioni Importi Capitolo Uscita restituita null dalla ricerca movimenti", res.getListaVariazioneImporti());
		
		List<VariazioneImportoCapitolo> LVarImpo = res.getListaVariazioneImporti();
		if (LVarImpo != null)
		{
			Iterator<VariazioneImportoCapitolo> VarImpo = LVarImpo.iterator();
			while (VarImpo.hasNext())
			{
				assertNotNull("Variazione Importo Capitolo Uscita restituita null dalla ricerca movimenti", VarImpo.next());
			}
		}
				
		// LISTA VINCOLI
		assertNotNull("Lista Vincoli Capitolo Uscita restituita null dalla ricerca movimenti", res.getListaVincoli());
		
		List<VincoloCapitoli> LVincoliUEGest = res.getListaVincoli();
		if (LVincoliUEGest != null)
		{
			Iterator<VincoloCapitoli> VincoliUEGest = LVincoliUEGest.iterator();
			while (VincoliUEGest.hasNext())
			{
				assertNotNull("Vincolo Capitolo Uscita restituito null dalla ricerca movimenti", VincoliUEGest.next());
			}
		}
				
		// LISTA IMPEGNI
		assertNotNull("Lista Impegni Capitolo Uscita restituita null dalla ricerca movimenti", res.getListaImpegni());
		
		List<Impegno> LImpegni = res.getListaImpegni();
		if (LImpegni != null)
		{
			Iterator<Impegno> Impegni = LImpegni.iterator();
			while (Impegni.hasNext())
			{
				assertNotNull("Impegno Capitolo Uscita restituita null dalla ricerca movimenti", Impegni.next());
			}
		}
				
		// LISTA DOCUMENTO SPESA
		assertNotNull("Lista Documenti Spesa Capitolo Uscita restituita null dalla ricerca movimenti", res.getListaDocumentoSpesa());
		
		List<DocumentoDiSpesa> LDocSpesa = res.getListaDocumentoSpesa();
		if (LDocSpesa != null)
		{
			Iterator<DocumentoDiSpesa> DocSpesa = LDocSpesa.iterator();
			while (DocSpesa.hasNext())
			{
				assertNotNull("Documento Spesa Capitolo Uscita restituito null dalla ricerca movimenti", DocSpesa.next());
			}
		}
	}

	/**
	 * Ricerca movimenti capitolo uscita gestione.
	 *
	 * @return the ricerca movimenti capitolo uscita gestione response
	 */
	private RicercaMovimentiCapitoloUscitaGestioneResponse ricercaMovimentiCapitoloUscitaGestione() {

		RicercaMovimentiCapitoloUscitaGestione req = new RicercaMovimentiCapitoloUscitaGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloUscitaGestione capitoloUscitaGestione = getCapitoloUscitaGestione();
		req.setCapitoloUscitaGestione(capitoloUscitaGestione);

		RicercaMovimentiCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaMovimentiCapitoloUscitaGestione(req);

		return res;		
	}
}
