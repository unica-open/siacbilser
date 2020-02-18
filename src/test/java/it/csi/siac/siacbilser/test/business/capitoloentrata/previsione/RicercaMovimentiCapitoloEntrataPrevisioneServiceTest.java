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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
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
 * The Class RicercaMovimentiCapitoloEntrataPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaMovimentiCapitoloEntrataPrevisioneServiceTest extends CapitoloEntrataPrevisioneMainTest 
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
    	return spreadsheetData("RicercaMovimenti");    	
    }

	/**
	 * Instantiates a new ricerca movimenti capitolo entrata previsione service test.
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
	public RicercaMovimentiCapitoloEntrataPrevisioneServiceTest(String obiettivoTest,
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
	 * Instantiates a new ricerca movimenti capitolo entrata previsione service test.
	 */
	public RicercaMovimentiCapitoloEntrataPrevisioneServiceTest() {}

	/**
	 * Test ricerca movimenti.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaMovimenti() throws Throwable
	{        	
		
		RicercaMovimentiCapitoloEntrataPrevisioneResponse res = ricercaMovimentiCapitoloEntrataPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		//TESTA GLI OGGETTI DI RITORNO :
		// LISTA VARIAZIONI CODIFICHE
		assertNotNull("Lista Variazioni Codifiche Capitolo Entrata Previsione restituita null dalla ricerca movimenti", res.getListaVariazioneCodifiche());
		
		List<VariazioneCodificaCapitolo> LVarCodif = res.getListaVariazioneCodifiche();
		if (LVarCodif != null)
		{
			Iterator<VariazioneCodificaCapitolo> VarCodif = LVarCodif.iterator();
			while (VarCodif.hasNext())
			{
				assertNotNull("Variazione Codifica Capitolo Entrata Previsione restituita null dalla ricerca movimenti", VarCodif.next());
			}
		}
				
		// LISTA VARIAZIONE IMPORTI
		assertNotNull("Lista Variazioni Importi Capitolo Entrata Previsione restituita null dalla ricerca movimenti", res.getListaVariazioneImporti());
		
		List<VariazioneImportoCapitolo> LVarImpo = res.getListaVariazioneImporti();
		if (LVarImpo != null)
		{
			Iterator<VariazioneImportoCapitolo> VarImpo = LVarImpo.iterator();
			while (VarImpo.hasNext())
			{
				assertNotNull("Variazione Importo Capitolo Entrata Previsione restituita null dalla ricerca movimenti", VarImpo.next());
			}
		}
				
		// LISTA VINCOLI
		assertNotNull("Lista Vincoli Capitolo Entrata Previsione restituita null dalla ricerca movimenti", res.getListaVincoli());
		
		List<VincoloCapitoli> LVincoliUEPrev = res.getListaVincoli();
		if (LVincoliUEPrev != null)
		{
			Iterator<VincoloCapitoli> VincoliUEPrev = LVincoliUEPrev.iterator();
			while (VincoliUEPrev.hasNext())
			{
				assertNotNull("Vincolo Capitolo Entrata Previsione restituito null dalla ricerca movimenti", VincoliUEPrev.next());
			}
		}
				
		// LISTA IMPEGNI
		assertNotNull("Lista Accertamenti Capitolo Entrata Previsione restituita null dalla ricerca movimenti", res.getListaAccertamenti());
		
		List<Accertamento> LAccert = res.getListaAccertamenti();
		if (LAccert != null)
		{
			Iterator<Accertamento> Accert = LAccert.iterator();
			while (Accert.hasNext())
			{
				assertNotNull("Accertamento Capitolo Entrata Previsione restituita null dalla ricerca movimenti", Accert.next());
			}
		}
				
		// LISTA DOCUMENTO SPESA
		assertNotNull("Lista Documenti Capitolo Entrata Previsione restituita null dalla ricerca movimenti", res.getDocumentiEntrata());
		
		List<DocumentoDiEntrata> LDocEntrata = res.getDocumentiEntrata();
		if (LDocEntrata != null)
		{
			Iterator<DocumentoDiEntrata> DocEntrata = LDocEntrata.iterator();
			while (DocEntrata.hasNext())
			{
				assertNotNull("Documento Capitolo Entrata Previsione restituito null dalla ricerca movimenti", DocEntrata.next());
			}
		}
				
		// LISTA VARIAZIONE CODIFICHE CAPITOLO ENTRATA GESTIONE
		assertNotNull("Lista Variazioni Codifiche Capitolo Entrata Gestione restituita null dalla ricerca movimenti", res.getListaVariazioneCodificheCapEGest());
		
		List<VariazioneCodificaCapitolo> LVarCodifUG = res.getListaVariazioneCodificheCapEGest();
		if (LVarCodifUG != null)
		{
			Iterator<VariazioneCodificaCapitolo> VarCodifUG = LVarCodifUG.iterator();
			while (VarCodifUG.hasNext())
			{
				assertNotNull("Variazione Codifica Capitolo Entrata Gestione restituita null dalla ricerca movimenti", VarCodifUG.next());
			}
		}
			
		// LISTA VARIAZIONE IMPORTI CAPITOLO ENTRATA GESTIONE
		assertNotNull("Lista Variazioni Importi Capitolo Entrata Gestione restituita null dalla ricerca movimenti", res.getListaVariazioneImportiCapEGest());
		
		List<VariazioneImportoCapitolo> LVarImpoUG = res.getListaVariazioneImportiCapEGest();
		if (LVarImpoUG != null)
		{
			Iterator<VariazioneImportoCapitolo> VarImpoUG = LVarImpoUG.iterator();
			while (VarImpoUG.hasNext())
			{
				assertNotNull("Variazione Importo Capitolo Entrata Gestione restituita null dalla ricerca movimenti", VarImpoUG.next());
			}
		}
		
		// LISTA VINCOLI CAPITOLO ENTRATA GESTIONE
		assertNotNull("Lista Vincoli Capitolo Entrata Gestione restituita null dalla ricerca movimenti", res.getListaVincoliCapEGest());

		List<VincoloCapitoli> LVincoliUEGest = res.getListaVincoliCapEGest();
		if (LVincoliUEGest != null)
		{
			Iterator<VincoloCapitoli> VincoliUEGest = LVincoliUEGest.iterator();
			while (VincoliUEGest.hasNext())
			{
				assertNotNull("Vincolo Capitolo Entrata Gestione restituito null dalla ricerca movimenti", VincoliUEGest.next());
			}
		}
	}

	/**
	 * Ricerca movimenti capitolo entrata previsione.
	 *
	 * @return the ricerca movimenti capitolo entrata previsione response
	 */
	private RicercaMovimentiCapitoloEntrataPrevisioneResponse ricercaMovimentiCapitoloEntrataPrevisione() {

		RicercaMovimentiCapitoloEntrataPrevisione req = new RicercaMovimentiCapitoloEntrataPrevisione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest();
		req.setBilancio(bilancio);
		
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = getCapitoloEntrataPrevisione();
		req.setCapitoloEPrev(capitoloEntrataPrevisione);

		RicercaMovimentiCapitoloEntrataPrevisioneResponse res = capitoloEntrataPrevisioneService.ricercaMovimentiCapitoloEntrataPrevisione(req);

		return res;		
	}
}
