/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione.massiva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaMassivaCapitoloUscitaPrevisioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class AggiornaMassivaCapitoloUscitaPrevisioneServiceTest extends CapitoloUscitaPrevisioneMassivaMainTest 
{

	/** The capitolo uscita previsione service. */
	@Autowired
	private CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;

    /**
     * Spreadsheet data massiva.
     *
     * @return the collection
     * @throws Exception the exception
     */
    @Parameters
    public static Collection<Object[]> spreadsheetDataMassiva() throws Exception {
    	return spreadsheetDataMassiva("Aggiorna");    	
    }

	/**
	 * Instantiates a new aggiorna massiva capitolo uscita previsione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidBil the uid bil
	 * @param annoBil the anno bil
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @param statoCapi the stato capi
	 * @param descCapi the desc capi
	 * @param annoCrea the anno crea
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
	public AggiornaMassivaCapitoloUscitaPrevisioneServiceTest(String obiettivoTest, 
			String uidEnte, String CFisc, String uidBil, String annoBil, String annoCapi, 
			String numeCapi, String numeArti, String statoCapi, String descCapi, String annoCrea,
			String uidPiano, String uidStrutt, String impStanz, String impCassa, String impResiduo, 
			String impStanz1, String impCassa1,	String impResiduo1, String impStanz2, String impCassa2, 
			String impResiduo2, String Esito, String Errore, String Descrizione, String Testout)  
	{
				
		this.obiettivoTest = obiettivoTest;
		this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
		this.uidBil = toInteger(uidBil);
		this.annoBil = toInteger(annoBil); this.annoEser = toInteger(annoBil);
		this.annoCapi = toInteger(annoCapi);
		this.numCapi = toInteger(numeCapi);
		this.numArti = toInteger(numeArti);
		this.statoCapi = statoCapi;		
		this.descCapi = descCapi;
		this.annoCrea = toInteger(annoCrea);	
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
	 * Test aggiorna massiva.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAggiornaMassiva() throws Throwable
	{

		AggiornaMassivoCapitoloDiUscitaPrevisioneResponse res = aggiornaMassivoCapitoloDiUscitaPrevisione();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}

		// TESTA GLI OGGETTI DI RITORNO
		// CAPITOLO USCITA PREVISIONE AGGIORNATO

		CapitoloUscitaPrevisione capitoloAggiornato = res.getCapitoloUscitaPrevisione();

		assertNotNull("capitolo restituito null", capitoloAggiornato);

		/*
		 * FACCIO UNA RICERCA DI DETTAGLIO SUL CAPITOLO APPENA AGGIORNATO E
		 * TESTO CHE I VALORI RITORNATI SIANO EGUALI A QUELLI AGGIORNATI
		 */

		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse ricercaDettaglioResponse = ricercaDettaglioMassivaCapitoloUscitaPrevisione();

		assertEquals("Fallita ricerca dettaglio del capitolo appena aggiornato.", Esito.SUCCESSO, ricercaDettaglioResponse.getEsito());
		
		// CAPITOLO ENTRATA PREVISIONE AGGIORNATO
		assertNotNull(ricercaDettaglioResponse.getCapitoloMassivaUscitaPrevisione());

		CapitoloMassivaUscitaPrevisione capi = ricercaDettaglioResponse.getCapitoloMassivaUscitaPrevisione();
		
		// ANNO DI BILANCIO
		assertNotNull(capi.getBilancio());
		assertEquals("Anno di bilancio diverso da quello aggiornato.", annoBil.intValue(), capi.getBilancio().getAnno());
		
		// ANNO CAPITOLO
		assertNotNull(capi.getAnnoCapitolo());
		assertEquals("Anno Capitolo diverso da quello aggiornato", annoCapi, capi.getAnnoCapitolo());	
		
		// NUMERO CAPITOLO
		assertNotNull(capi.getNumeroCapitolo());
		assertEquals("Numero Capitolo diverso da quello aggiornato", numCapi, capi.getNumeroCapitolo());

		// NUMERO ARTICOLO	
		assertNotNull(capi.getNumeroArticolo());	
		assertEquals("Numero Articolo diverso da quello aggiornato", numArti,  capi.getNumeroArticolo());					

		// DESCRIZIONE CAPITOLO
		assertNotNull(capi.getDescrizione());
		assertEquals("Descrizione Capitolo diversa da quella aggiornata", descCapi,  capi.getDescrizione());					

		// ANNO CREAZIONE CAPITOLO
		assertNotNull(capi.getAnnoCreazioneCapitolo());
		assertEquals("Anno Creazione Capitolo diversa da quella aggiornata", annoCrea,  capi.getAnnoCreazioneCapitolo());		
	}

	/**
	 * Ricerca dettaglio massiva capitolo uscita previsione.
	 *
	 * @return the ricerca dettaglio massiva capitolo uscita previsione response
	 */
	private RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse ricercaDettaglioMassivaCapitoloUscitaPrevisione() 
	{
		RicercaSinteticaCapitoloUPrev criteriRicerca = getCriteriRicercas(annoEser, annoCapi, numCapi, numArti);

		RicercaDettaglioMassivaCapitoloUscitaPrevisione req = new RicercaDettaglioMassivaCapitoloUscitaPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);

		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.ricercaDettaglioMassivaCapitoloUscitaPrevisione(req);
		return res;
	}

	/**
	 * Aggiorna massivo capitolo di uscita previsione.
	 *
	 * @return the aggiorna massivo capitolo di uscita previsione response
	 */
	private AggiornaMassivoCapitoloDiUscitaPrevisioneResponse aggiornaMassivoCapitoloDiUscitaPrevisione() {
		
		AggiornaMassivoCapitoloDiUscitaPrevisione req = new AggiornaMassivoCapitoloDiUscitaPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setBilancio(getBilancioTest());	

		CapitoloUscitaPrevisione capitoloUscitaPrevisione = getCapitoloUPrev();
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
		
		//Imposto gli importi 
		List<ImportiCapitoloUP> listaImporti = new ArrayList<ImportiCapitoloUP>();
		
		listaImporti.add(getImportiCapitolo(0, impStanz, impCassa, impResiduo));    // IMPORTI STANZIAMENTO ANNO DI RIFERIMENTO
		listaImporti.add(getImportiCapitolo(1, impStanz1, impCassa1, impResiduo1)); // IMPORTI STANZIAMENTO ANNO DI RIFERIMENTO+1
		listaImporti.add(getImportiCapitolo(2, impStanz2, impCassa2, impResiduo2)); // IMPORTI STANZIAMENTO ANNO DI RIFERIMENTO+2
		
		req.setImportiCapitoloUP(listaImporti);
					
		AggiornaMassivoCapitoloDiUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService.aggiornaMassivoCapitoloDiUscitaPrevisione(req);
		return res;
	}
}
