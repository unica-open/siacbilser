/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaCapitoloEntrataPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaSinteticaCapitoloEntrataPrevisioneServiceTest  extends 
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
    	return spreadsheetData("RicercaSintetica");
    }

	/**
	 * Instantiates a new ricerca sintetica capitolo entrata previsione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param annoEser the anno eser
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @param numUEB the num ueb
	 * @param statoCapi the stato capi
	 * @param descCapi the desc capi
	 * @param CodPdC the cod pd c
	 * @param CodStrutt the cod strutt
	 * @param CodTipStrutt the cod tip strutt
	 * @param CodMiss the cod miss
	 * @param CodMacro the cod macro
	 * @param Pagina the pagina
	 * @param EleXPag the ele x pag
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public RicercaSinteticaCapitoloEntrataPrevisioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String annoEser, String annoCapi, String numeCapi, 
			String numeArti, String numUEB, String statoCapi, String descCapi, 
			String CodPdC, String CodStrutt, String CodTipStrutt, String CodMiss, 
			String CodMacro, String Pagina, String EleXPag, String Esito,
			String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.annoEser = toInteger(annoEser);	
   	    this.annoCapi = toInteger(annoCapi);		
   	    this.numCapi = toInteger(numeCapi);		
   	    this.numArti = toInteger(numeArti);
   	    this.numUEB = toInteger(numUEB);
        this.statoCapi = statoCapi;
      	this.descCapi = descCapi;   
      	this.codPdC = CodPdC; 
      	this.codStrutt = CodStrutt;
      	this.codTipStrutt = CodTipStrutt;
      	this.codMiss = CodMiss;
      	this.codMacro = CodMacro;
      	this.pagina = toInteger(Pagina);
      	this.eleXPag = toInteger(EleXPag);
        this.esito = Esito;
        this.errore = Errore;
        this.descrizione = Descrizione;
        this.testOut = Testout;
    }

	/**
	 * Test ricerca sintetica.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaSintetica() throws Throwable
	{        	
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse res = ricercaSinteticaCapitoloEntrataPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		//TESTA GLI OGGETTI DI RITORNO :
		// ANNO DI BILANCIO
		assertNotNull(res.getBilancio());
		assertEquals("Anno di bilancio diverso da quello ricercato.", annoBil.intValue(), res.getBilancio().getAnno());
				
		// LISTA CAPITOLI DI USCITA PREVISIONE
		assertNotNull("Lista capitoli restituita null dalla ricerca sintetica", res.getCapitoli());

		ListaPaginata<CapitoloEntrataPrevisione> listacap = res.getCapitoli();
		Iterator<CapitoloEntrataPrevisione> itercapi = listacap.iterator();
					
		while (itercapi.hasNext())
		{
		    // CONTROLLO SU OGNI CAPITOLO IN LISTA :
			// NUMERO CAPITOLO
			CapitoloEntrataPrevisione capitoloRicercaSintetica = itercapi.next();
			assertNotNull(capitoloRicercaSintetica.getNumeroCapitolo());
			assertEquals("Numero di capitolo diverso da quello ricercato", numCapi, capitoloRicercaSintetica.getNumeroCapitolo());

			// NUMERO ARTICOLO
			assertNotNull(capitoloRicercaSintetica.getNumeroArticolo());							
			assertEquals("Numero articolo diverso da quello ricercato", numArti, capitoloRicercaSintetica.getNumeroArticolo());
					
			// NUMERO UEB
			assertNotNull(capitoloRicercaSintetica.getNumeroUEB());
			assertEquals("Numero ueb diverso da quello ricercato", numUEB, capitoloRicercaSintetica.getNumeroUEB());
					
			// STATO (STATO OPERATIVO ELEMENTO DI BILANCIO)
			/*assertNotNull(capi.getStatoOperativoElementoDiBilancio());
			assertEquals("", statocapi, capi.getStatoOperativoElementoDiBilancio().toString());*/
					
			// DESCRIZIONE CAPITOLO
			assertNotNull("Descrizione restituita null dalla ricerca sintetica", capitoloRicercaSintetica.getDescrizione());
			//assertEquals("", descCapi, capi.getDescrizione().toString());
					
			// ELEMENTO PIANO DEI CONTI
			assertNotNull("Piano dei Conti restituito null dalla ricerca sintetica", capitoloRicercaSintetica.getElementoPianoDeiConti());
			//assertEquals("", codPdC, capi.getElementoPianoDeiConti().getCodice());

			// ELEMENTO STRUTTURA AMMINISTRATIVO CONTABILE
			assertNotNull("Struttura Amministrativo Contabile restituita null dalla ricerca sintetica", capitoloRicercaSintetica.getStrutturaAmministrativoContabile());
			//assertEquals("", CodStrutt, capi.getStrutturaAmministrativoContabile().getCodice());
		}
	}


	/**
	 * Ricerca sintetica capitolo entrata previsione.
	 *
	 * @return the ricerca sintetica capitolo entrata previsione response
	 */
	private RicercaSinteticaCapitoloEntrataPrevisioneResponse ricercaSinteticaCapitoloEntrataPrevisione() {

		RicercaSinteticaCapitoloEntrataPrevisione req = new RicercaSinteticaCapitoloEntrataPrevisione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		RicercaSinteticaCapitoloEPrev criteriRicerca = 
					getCriteriRicercas(annoEser, annoCapi, numCapi, numArti, numUEB, statoCapi, 
										descCapi, codPdC, codStrutt, codTipStrutt, codMiss, codMacro);
		req.setRicercaSinteticaCapitoloEPrev(criteriRicerca);

		ParametriPaginazione parametriPaginazione = getParametriPaginazione(pagina, eleXPag);
		req.setParametriPaginazione(parametriPaginazione);
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse res = capitoloEntrataPrevisioneService.ricercaSinteticaCapitoloEntrataPrevisione(req);

		return res;		
	}
}
