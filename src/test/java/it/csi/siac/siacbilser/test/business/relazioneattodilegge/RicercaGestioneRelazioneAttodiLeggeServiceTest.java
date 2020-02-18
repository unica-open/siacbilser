/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.relazioneattodilegge;

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

import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaGestioneRelazioneAttodiLeggeServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class RicercaGestioneRelazioneAttodiLeggeServiceTest extends RelazioneAttodiLeggeMainTest 
{
	
	/** The relazione atto di legge capitolo service. */
	@Autowired
	private CapitoloService relazioneAttoDiLeggeCapitoloService;
	
	/**
	 * Spreadsheet data.
	 *
	 * @return the collection
	 * @throws Exception the exception
	 */
	@Parameters
	public static Collection<Object[]> spreadsheetData() throws Exception {
		return spreadsheetData("Ricerca");	 
	}
   
	/**
	 * Instantiates a new ricerca gestione relazione attodi legge service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidBil the uid bil
	 * @param annoBil the anno bil
	 * @param uidAtto the uid atto
	 * @param annoAtto the anno atto
	 * @param numeroAtto the numero atto
	 * @param uidTipoAtto the uid tipo atto
	 * @param tipoAtto the tipo atto
	 * @param uidCapitoloEntrata the uid capitolo entrata
	 * @param uidCapitoloUscita the uid capitolo uscita
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public RicercaGestioneRelazioneAttodiLeggeServiceTest(String obiettivoTest, 
		String uidEnte, String CFisc, String uidBil, String annoBil, String uidAtto, String annoAtto, 
		String numeroAtto, String uidTipoAtto, String tipoAtto,	String uidCapitoloEntrata, 
		String uidCapitoloUscita, String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
		
		this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
		this.uidBil = toInteger(uidBil);
	  	this.annoBil = toInteger(annoBil); this.annoEser = toInteger(annoBil);
		this.uidAttoLegge = toInteger(uidAtto);
		this.annoAttoLegge = toInteger(annoAtto);	
	    this.numeroAttoLegge = toInteger(numeroAtto);	
	    this.uidTipoAttoLegge = toInteger(uidTipoAtto);
	    this.descrTipoAttoLegge = tipoAtto;   
	  	this.uidCapitoloEntrata = toInteger(uidCapitoloEntrata);
		this.uidCapitoloUscita = toInteger(uidCapitoloUscita);	
		
  		this.esito = Esito;
  		this.errore = Errore;
  		this.descrizione = Descrizione;
  		this.testOut = Testout;
	}

	/**
	 * Test ricerca.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicerca() throws Throwable
	{       
		RicercaRelazioneAttoDiLeggeCapitoloResponse res = ricercaRelazioneAttoDiLeggeCapitolo();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
				
		//TESTA GLI OGGETTI DI RITORNO :
		// LISTA RELAZIONI - ATTI DI LEGGE
		assertNotNull("Lista Relazioni Atti di Legge - Capitoli restituita null dalla ricerca", res.getElencoAttiLeggeCapitolo());
		
		List<AttoDiLeggeCapitolo> LAttoCapitolo = res.getElencoAttiLeggeCapitolo();
		if (LAttoCapitolo != null)
		{
			Iterator<AttoDiLeggeCapitolo> AttoCapitolo = LAttoCapitolo.iterator();
			while (AttoCapitolo.hasNext())
			{
				assertNotNull("Atto di Legge - Capitolo restituito null dalla ricerca", AttoCapitolo.next());
				
				/*AttoDiLeggeCapitolo attoc = AttoCapitolo.next();
				assertNotNull("Descrizione Atto di Legge - Capitolo restituita null dalla ricerca", attoc.getDescrizione());
				
				//ATTO DI LEGGE
				assertNotNull("Atto di Legge restituito null dalla ricerca", AttoCapitolo.next().getAttoDiLegge());
								
				//ANNO ATTO DI LEGGE
				assertNotNull("Anno Atto di Legge restituito null dalla ricerca", AttoCapitolo.next().getAttoDiLegge().getAnno());
				
				//NUMERO ATTO DI LEGGE
				assertNotNull("Numero Atto di Legge restituito null dalla ricerca", AttoCapitolo.next().getAttoDiLegge().getNumero());
				
				//TIPO ATTO DI LEGGE
				assertNotNull("Anno Atto di Legge restituito null dalla ricerca", AttoCapitolo.next().getAttoDiLegge().getTipoAtto());*/
			}
		}
		
		// ATTO DI LEGGE LEGATO ALLA LISTA RELAZIONI
		assertNotNull("Atto di Legge legato alla lista Relazioni restituito null dalla ricerca", res.getAttoDiLegge());
		
		//ANNO ATTO DI LEGGE LEGATO ALLA LISTA RELAZIONI
		assertNotNull("Anno Atto di Legge legato alla lista Relazioni restituito null dalla ricerca", res.getAttoDiLegge().getAnno());

		//NUMERO ATTO DI LEGGE LEGATO ALLA LISTA RELAZIONI
		assertNotNull("Numero Atto di Legge alla lista Relazioni restituito null dalla ricerca", res.getAttoDiLegge().getNumero());

		//TIPO ATTO DI LEGGE LEGATO ALLA LISTA RELAZIONI
		//assertNotNull("Anno Atto di Legge alla lista Relazioni restituito null dalla ricerca", res.getAttoDiLegge().getTipoAtto());
	}

	/**
	 * Ricerca relazione atto di legge capitolo.
	 *
	 * @return the ricerca relazione atto di legge capitolo response
	 */
	private RicercaRelazioneAttoDiLeggeCapitoloResponse ricercaRelazioneAttoDiLeggeCapitolo() 
	{

		RicercaRelazioneAttoDiLeggeCapitolo req = new RicercaRelazioneAttoDiLeggeCapitolo();
		req.setEnte(getEnteTest());
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		req.setRicercaAttiDiLeggeCapitolo(getRicercaAttiDiLeggeCapitolo());
				
		RicercaRelazioneAttoDiLeggeCapitoloResponse res = relazioneAttoDiLeggeCapitoloService.ricercaRelazioneAttoDiLeggeCapitolo(req);

		return res;		
	}
}
