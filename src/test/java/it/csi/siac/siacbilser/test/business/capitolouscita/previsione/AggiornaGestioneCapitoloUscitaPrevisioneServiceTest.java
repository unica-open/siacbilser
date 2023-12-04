/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;


// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaGestioneCapitoloUscitaPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml" /*, "/spring/datasource-test.xml"*/})
public class AggiornaGestioneCapitoloUscitaPrevisioneServiceTest extends CapitoloUscitaPrevisioneMainTest
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
    	return spreadsheetData("Aggiorna");    	
    }

	/**
	 * Instantiates a new aggiorna gestione capitolo uscita previsione service test.
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
	 * @param MacroAggr the macro aggr
	 * @param Progr the progr
	 * @param uidPiano the uid piano
	 * @param uidStrutt the uid strutt
	 * @param impStanz the imp stanz
	 * @param impCassa the imp cassa
	 * @param impResiduo the imp residuo
	 * @param impStanz1 the imp stanz1
	 * @param impCassa1 the imp cassa1
	 * @param impResiduo1 the imp residuo1
	 * @param impStanz2 the imp stanz2
	 * @param impCassa2 the imp cassa2
	 * @param impResiduo2 the imp residuo2
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public AggiornaGestioneCapitoloUscitaPrevisioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String uidBil, String annoBil,  String annoCapi, 
			String numeCapi, String numeArti, String numUEB, String statoCapi, String descCapi, 
			String annoCrea, String MacroAggr, String Progr, String uidPiano, String uidStrutt, 
			String impStanz, String impCassa, String impResiduo, String impStanz1, String impCassa1,
			String impResiduo1, String impStanz2, String impCassa2, String impResiduo2, String Esito,
			String Errore, String Descrizione, String Testout)  {
		
		
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
		this.MacroAggr = toInteger(MacroAggr);
		this.Progr = toInteger(Progr);
		this.uidPiano = toInteger(uidPiano);	
		this.uidStrutt = toInteger(uidStrutt);	        
		this.impStanz = toBigDecimal(impStanz);		  // STANZIAMENTO ANNO DI RIFERIMENTO
        this.impCassa = toBigDecimal(impCassa);		  // STANZIAMENTO CASSA ANNO DI RIFERIMENTO
        this.impResiduo = toBigDecimal(impResiduo);	  // STANZIAMENTO RESIDUO ANNO DI RIFERIMENTO
        this.impStanz1 = toBigDecimal(impStanz1);	  // STANZIAMENTO ANNO DI RIFERIMENTO+1	
        this.impCassa1 = toBigDecimal(impCassa1);	  // STANZIAMENTO CASSA ANNO DI RIFERIMENTO+1
        this.impResiduo1 = toBigDecimal(impResiduo1); // STANZIAMENTO RESIDUO ANNO DI RIFERIMENTO+1	
        this.impStanz2 = toBigDecimal(impStanz2);	  // STANZIAMENTO ANNO DI RIFERIMENTO+2	
        this.impCassa2 = toBigDecimal(impCassa2);	  // STANZIAMENTO CASSA ANNO DI RIFERIMENTO+2
        this.impResiduo2 = toBigDecimal(impResiduo2); // STANZIAMENTO RESIDUO ANNO DI RIFERIMENTO+2	
		this.esito = Esito;
		this.errore = Errore;
		this.descrizione = Descrizione;
		this.testOut = Testout;
    }
	
	/**
	 * Test update.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testUpdate() throws Throwable
	{

		InizializzaUidCapitoloSeValorizzataChiaveRicerca();

		AggiornaCapitoloDiUscitaPrevisioneResponse res = aggiornaCapitoloDiUscitaPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		// TESTA GLI OGGETTI DI RITORNO
		// CAPITOLO USCITA PREVISIONE AGGIORNATO

		CapitoloUscitaPrevisione capitoloAggiornato = res.getCapitoloUscitaPrevisione();

		assertNotNull("capitolo restituito null", capitoloAggiornato);
		assertTrue("uid restituito a 0!!", capitoloAggiornato.getUid() != 0);

		/*
		 * FACCIO UNA RICERCA DI DETTAGLIO SUL CAPITOLO APPENA AGGIORNATO E
		 * TESTO CHE I VALORI RITORNATI SIANO UGUALI A QUELLI INSERITI
		 */

		RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaDettaglioResponse = ricercaDettaglioCapitoloUscitaPrevisione();

		assertEquals("Fallita ricerca dettaglio del capitolo appena aggiornato.", Esito.SUCCESSO, ricercaDettaglioResponse.getEsito());
		

		/*
		 * List<Errore> err1 = res1.getErrori(); Iterator<Errore> itererr1 =
		 * err1.iterator();
		 * 
		 * while(itererr1.hasNext()) { Errore errelem=itererr1.next();
		 * assertEquals("", Errore, errelem.getCodice()); assertEquals("",
		 * Descrizione, errelem.getDescrizione()); }
		 */

		// ANNO DI BILANCIO
		assertNotNull(ricercaDettaglioResponse.getBilancio());
		assertEquals("Anno di bilancio diverso da quello aggiornato.", annoBil.intValue(), ricercaDettaglioResponse.getBilancio().getAnno());

		// CAPITOLO USCITA PREVISIONE AGGIORNATO
		assertNotNull("Capitolo aggiornato retituito null dalla ricerca sintetica",ricercaDettaglioResponse.getCapitoloUscitaPrevisione());

		CapitoloUscitaPrevisione capitoloRicercaDettaglio = ricercaDettaglioResponse.getCapitoloUscitaPrevisione();

		// NUMERO CAPITOLO
		assertNotNull(capitoloRicercaDettaglio.getNumeroCapitolo());
		assertEquals("Numero di capitolo diverso da quello aggiornato", numCapi, capitoloRicercaDettaglio.getNumeroCapitolo());

		// NUMERO ARTICOLO
		assertNotNull(capitoloRicercaDettaglio.getNumeroArticolo());
		assertEquals("Numero articolo diverso da quello aggiornato", numArti,capitoloRicercaDettaglio.getNumeroArticolo());

		// NUMERO UEB
		assertNotNull(capitoloRicercaDettaglio.getNumeroUEB());
		assertEquals("Numero ueb diverso da qullo aggiornato", numUEB,capitoloRicercaDettaglio.getNumeroUEB());

		// STATO (STATO OPERATIVO ELEMENTO DI BILANCIO)
		/*
		 * assertNotNull(capi.getStatoOperativoElementoDiBilancio());
		 * assertEquals("", "VALIDO",
		 * capi.getStatoOperativoElementoDiBilancio().toString());
		 */

		// DESCRIZIONE CAPITOLO
		assertNotNull(capitoloRicercaDettaglio.getDescrizione());
		assertTrue(capitoloRicercaDettaglio.getDescrizione().startsWith(descCapi));
		// assertEquals("", testdesc, capi.getDescrizione());

		// ANNO CREAZIONE CAPITOLO
		assertNotNull(capitoloRicercaDettaglio.getAnnoCreazioneCapitolo());
		assertEquals("Anno creazione capitolo diverso da quello aggiornato", annoCrea, capitoloRicercaDettaglio.getAnnoCreazioneCapitolo());

		// ELEMENTO PIANO DEI CONTI
		/*
		 * assertNotNull(capi.getElementoPianoDeiConti()); assertEquals("",
		 * uidPiano, (int) capi.getElementoPianoDeiConti().getUid());
		 */

		// ELEMENTO STRUTTURA AMMINISTRATIVO CONTABILE
		/*
		 * assertNotNull(capi.getStrutturaAmministrativoContabile());
		 * assertEquals("", uidStrutt, (int)
		 * capi.getStrutturaAmministrativoContabile().getUid());
		 */

		// IMPORTI CAPITOLO AGGIORNATO
		/*
		 * assertNotNull(capi.getImportiCapitoloUP()); assertEquals("",
		 * annoBil+1, (int) capi.getImportiCapitoloUP().getAnnoCompetenza());
		 * assertEquals("", (long)(importo + 0.3),
		 * capi.getImportiCapitoloUP().getStanziamento().longValue());
		 * assertEquals("", (long)(importo + 20000.5),
		 * capi.getImportiCapitoloUP().getStanziamentoCassa().longValue());
		 * assertEquals("", (long)(importo + 10000.7),
		 * capi.getImportiCapitoloUP().getStanziamentoResiduo().longValue());
		 */

		// ELEMENTO MACROAGGREGATO
		assertNotNull(capitoloRicercaDettaglio.getMacroaggregato());
		assertEquals("Macroaggregato diverso da quello aggiornato", MacroAggr.intValue(), (int) capitoloRicercaDettaglio.getMacroaggregato().getUid());

		// ELEMENTO PROGRAMMA
		assertNotNull(capitoloRicercaDettaglio.getProgramma());
		assertEquals("Programma diverso da quello aggiornato", Progr.intValue(), (int) capitoloRicercaDettaglio.getProgramma().getUid());

		/*
		 * assertNotNull(capi.getDescrizioneArticolo());
		 * assertNotNull(capi.getDisponibilitaVariare());
		 * assertNotNull(capi.getElementoPianoDeiConti());
		 * assertNotNull(capi.getExAnnoCapitolo());
		 * assertNotNull(capi.getExArticolo());
		 * assertNotNull(capi.getExCapitolo()); assertNotNull(capi.getExUEB());
		 * assertNotNull(capi.getFlagRilevanteIva());
		 * assertNotNull(capi.getFondoPluriennaleVinc());
		 * assertNotNull(capi.getFondoPluriennaleVincPrec());
		 * assertNotNull(capi.getMissione()); assertNotNull(capi.getNote());
		 * assertNotNull(capi.getProgramma());
		 * assertNotNull(capi.getTitoloSpesa());
		 */

	}

	/**
	 * Ricerca dettaglio capitolo uscita previsione.
	 *
	 * @return the ricerca dettaglio capitolo uscita previsione response
	 */
	private RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaDettaglioCapitoloUscitaPrevisione() {
		RicercaDettaglioCapitoloUPrev criteriRicerca = getCriteriRicercaDettaglio(uidCapi);

		RicercaDettaglioCapitoloUscitaPrevisione req = new RicercaDettaglioCapitoloUscitaPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaDettaglioCapitoloUPrev(criteriRicerca);

		RicercaDettaglioCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(req);
		return res;
	}

	/**
	 * Aggiorna capitolo di uscita previsione.
	 *
	 * @return the aggiorna capitolo di uscita previsione response
	 */
	private AggiornaCapitoloDiUscitaPrevisioneResponse aggiornaCapitoloDiUscitaPrevisione() {
		AggiornaCapitoloDiUscitaPrevisione req = new AggiornaCapitoloDiUscitaPrevisione();		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setBilancio(getBilancioTest());	

		CapitoloUscitaPrevisione capitoloUscitaPrevisione = getCapitoloUscitaPrevisione();
		if(uidCapi!=null) {
			capitoloUscitaPrevisione.setUid(uidCapi);
		}
		
		req.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);
		
		
		//Imposto i classificatori
		ElementoPianoDeiConti elementoPianoDeiConti = getElementoPianoDeiConti(uidPiano);	
		req.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = getStrutturaAmministrativoContabile(uidStrutt);	
		req.setStruttAmmContabile(strutturaAmministrativoContabile);
		
		TipoFondo tipoFondo = null;
		req.setTipoFondo(tipoFondo);		
		
		TipoFinanziamento tipoFinanziamento = null;
		req.setTipoFinanziamento(tipoFinanziamento);
		
		
		List<ClassificatoreGenerico> listaClassificatori = null;	
		req.setClassificatoriGenerici(listaClassificatori);
		
		ClassificazioneCofogProgramma clcofog = null; //getClassificazioneCofogProgramma(clCofog);	
		req.setClassificazioneCofogProgramma(clcofog);
		
		Macroaggregato macroaggr = getMacroaggregato(MacroAggr);	
		req.setMacroaggregato(macroaggr);
		
		Programma progr = getProgramma(Progr);
		req.setProgramma(progr);
		
		//Imposto gli importi 
		List<ImportiCapitoloUP> listaImporti = new ArrayList<ImportiCapitoloUP>();

		listaImporti.add(getImportiCapitolo(0, impStanz, impCassa, impResiduo));    // IMPORTI STANZIAMENTO ANNO DI RIFERIMENTO
		listaImporti.add(getImportiCapitolo(1, impStanz1, impCassa1, impResiduo1)); // IMPORTI STANZIAMENTO ANNO DI RIFERIMENTO+1
		listaImporti.add(getImportiCapitolo(2, impStanz2, impCassa2, impResiduo2)); // IMPORTI STANZIAMENTO ANNO DI RIFERIMENTO+2
		
		req.setImportiCapitoloUP(listaImporti);
	
				
		AggiornaCapitoloDiUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.aggiornaCapitoloDiUscitaPrevisione(req);
		return res;
	}
	
	/**
	 * Inizializza uid capitolo se valorizzata chiave ricerca.
	 */
	private void InizializzaUidCapitoloSeValorizzataChiaveRicerca(){
		if(!valorizzatiParametriRicercaPuntuale()){
			return;
		}
		
		inizializzaUidCapitolo();
	}
	
	/**
	 * Inizializza uid capitolo.
	 */
	private void inizializzaUidCapitolo() {
		final String methodName = "inizializzaUidCapitolo";
		
		RicercaPuntualeCapitoloUscitaPrevisioneResponse ricercaPuntualeResponse = ricercaPuntuale(capitoloUscitaPrevisioneService);
		/*String msgCapNonEsiste = "Capitolo da aggiornare non trovato! Il capitolo deve esistere per poter testare l'aggiornamento!";
		assertNotNull(msgCapNonEsiste, ricercaPuntualeResponse);
		assertEquals(msgCapNonEsiste, Esito.SUCCESSO, ricercaPuntualeResponse.getEsito());*/
		
		if (ricercaPuntualeResponse.getCapitoloUscitaPrevisione() != null)
		{
			this.uidCapi = ricercaPuntualeResponse.getCapitoloUscitaPrevisione().getUid();
			log.debug(methodName, "Capitolo da aggiornare trovato con uid: "+this.uidCapi);
		}
		else this.uidCapi = 1;
	}
}