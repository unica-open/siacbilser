/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;

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

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaCapitoloUscitaPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class RicercaSinteticaCapitoloUscitaPrevisioneServiceTest extends CapitoloUscitaPrevisioneMainTest 
{

	/** The capitolo uscita previsione service. */
	@Autowired
	private CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;

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
	 * Instantiates a new ricerca sintetica capitolo uscita previsione service test.
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
	public RicercaSinteticaCapitoloUscitaPrevisioneServiceTest(String obiettivoTest, 
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
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse res = ricercaSinteticaCapitoloUscitaPrevisione();
		
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

		ListaPaginata<CapitoloUscitaPrevisione> listacap = res.getCapitoli();
		Iterator<CapitoloUscitaPrevisione> itercapi = listacap.iterator();
					
		while (itercapi.hasNext())
		{
		    // CONTROLLO SU OGNI CAPITOLO IN LISTA :
			// NUMERO CAPITOLO
			CapitoloUscitaPrevisione capitoloRicercaSintetica = itercapi.next();
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
					
			// CODICE MISSIONE
			assertNotNull("Codice Missione restituito null dalla ricerca sintetica", capitoloRicercaSintetica.getMissione());
			//assertEquals("", CodMiss, capi.getMissione().getCodice());
				
			// CODICE MACROAGGREGATO
			assertNotNull("Codice MacroAggregato restituito null dalla ricerca sintetica", capitoloRicercaSintetica.getMacroaggregato());
			//assertEquals("", codMacro, capi.getMacroaggregato().getCodice());
		}
	}


	/**
	 * Ricerca sintetica capitolo uscita previsione.
	 *
	 * @return the ricerca sintetica capitolo uscita previsione response
	 */
	private RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaSinteticaCapitoloUscitaPrevisione() {

		RicercaSinteticaCapitoloUscitaPrevisione req = new RicercaSinteticaCapitoloUscitaPrevisione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		RicercaSinteticaCapitoloUPrev criteriRicerca = 
					getCriteriRicercas(annoEser, annoCapi, numCapi, numArti, numUEB, statoCapi, 
										descCapi, codPdC, codStrutt, codTipStrutt, codMiss, codMacro);
		req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);

		ParametriPaginazione parametriPaginazione = getParametriPaginazione(pagina, eleXPag);
		req.setParametriPaginazione(parametriPaginazione);
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.ricercaSinteticaCapitoloUscitaPrevisione(req);

		return res;		
	}
}
