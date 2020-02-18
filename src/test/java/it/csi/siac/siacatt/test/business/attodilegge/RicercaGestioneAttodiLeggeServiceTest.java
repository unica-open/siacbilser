/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.attodilegge;

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

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaGestioneAttodiLeggeServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class RicercaGestioneAttodiLeggeServiceTest extends AttodiLeggeMainTest
{
	
	/** The atto di legge service. */
	@Autowired
	private AttoDiLeggeService attoDiLeggeService;
 
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
	 * Instantiates a new ricerca gestione attodi legge service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidAtto the uid atto
	 * @param annoAtto the anno atto
	 * @param numeroAtto the numero atto
	 * @param uidTipoAtto the uid tipo atto
	 * @param DescrTipoAtto the descr tipo atto
	 * @param articoloAtto the articolo atto
	 * @param commaAtto the comma atto
	 * @param puntoAtto the punto atto
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public RicercaGestioneAttodiLeggeServiceTest(String obiettivoTest, 
		String uidEnte, String CFisc, String uidAtto, String annoAtto, String numeroAtto, 
		String uidTipoAtto, String DescrTipoAtto, String articoloAtto, String commaAtto, 
		String puntoAtto, String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
		this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
		
		this.uidAttoLegge = toInteger(uidAtto);
		this.annoAttoLegge = toInteger(annoAtto);	
	    this.numeroAttoLegge = toInteger(numeroAtto);	
	    this.uidTipoAttoLegge = toInteger(uidTipoAtto);   
	    this.descrTipoAttoLegge = DescrTipoAtto;   
	    this.articoloAttoLegge = articoloAtto;		
	    this.commaAttoLegge = commaAtto;	
	    this.puntoAttoLegge = puntoAtto;
  	
  		this.esito = Esito;
  		this.errore = Errore;
		
		String new_descr = annoAttoLegge+"/"+numeroAttoLegge;
		Descrizione.replaceFirst("@", new_descr);
		
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
		RicercaAttoDiLeggeResponse res = ricercaAttoDiLegge();
				
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO :
		// LISTA ATTI DI LEGGE
		assertNotNull("Lista Atti di Legge restituita null dalla ricerca atti", res.getAttiTrovati());
		List<AttoDiLegge> LAtto = res.getAttiTrovati();
		if (LAtto != null)
		{
			Iterator<AttoDiLegge> Atto = LAtto.iterator();
			while (Atto.hasNext())
			{
				assertNotNull("Atto restituito null dalla ricerca atti", Atto.next());
				
				//ANNO 
				//assertNotNull(Atto.next().getAnno());
				//assertEquals("Anno Atto di legge diverso da quello ricercato.", annoAttoLegge, Atto.next().getAnno());
				
				//NUMERO 
				//assertNotNull(Atto.next().getNumero());
				//assertEquals("Numero Atto di legge diverso da quello ricercato.", numeroAttoLegge, Atto.next().getNumero());

				//TIPO 
				//assertNotNull(Atto.next().getTipoAtto());
				//assertEquals("Tipo Atto di legge diverso da quello ricercato.", descrTipoAttoLegge, Atto.next().getTipoAtto());
				
				//ARTICOLO 
				if (articoloAttoLegge != null)
				{
					//assertNotNull(Atto.next().getArticolo());
					//assertEquals("Articolo Atto di legge diverso da quello ricercato.", articoloAttoLegge, Atto.next().getArticolo());
				}
				
				//COMMA
				if (commaAttoLegge != null)
				{
					//assertNotNull(Atto.next().getComma());
					//assertEquals("Comma Atto di legge diverso da quello ricercato.", commaAttoLegge, Atto.next().getComma());
				}

				//PUNTO
				if (puntoAttoLegge != null)
				{
					//assertNotNull(Atto.next().getPunto());
					//assertEquals("Punto Atto di legge diverso da quello ricercato.", puntoAttoLegge, Atto.next().getPunto());
				}
			}
		}			
	}

	/**
	 * Ricerca atto di legge.
	 *
	 * @return the ricerca atto di legge response
	 */
	private RicercaAttoDiLeggeResponse ricercaAttoDiLegge() {

		RicercaAttoDiLegge req = new RicercaAttoDiLegge();
		
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));		
		req.setAttoDiLegge(getCriteriRicerca());
		
		RicercaAttoDiLeggeResponse res = attoDiLeggeService.ricercaAttoDiLegge(req);

		return res;		
	}
}
