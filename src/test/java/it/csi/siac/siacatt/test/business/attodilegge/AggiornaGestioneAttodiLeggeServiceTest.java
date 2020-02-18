/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.attodilegge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLeggeResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaGestioneAttodiLeggeServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class AggiornaGestioneAttodiLeggeServiceTest extends AttodiLeggeMainTest
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
		return spreadsheetData("Aggiorna");	 
	}
	   
	/**
	 * Instantiates a new aggiorna gestione attodi legge service test.
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
	public AggiornaGestioneAttodiLeggeServiceTest(String obiettivoTest, 
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
	 * Test aggiorna.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAggiorna() throws Throwable
	{   
		AggiornaAttoDiLeggeResponse res = aggiornaAttoDiLegge();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO :
		// TESTO ATTO DI LEGGE APPENA AGGIORNATO
		assertNotNull("Aggiornamento Atto di Legge fallito : Atto di Legge ritornato dal servizio null", res.getAttoDiLeggeAggiornato());
		
		//ANNO ATTO
		assertNotNull(res.getAttoDiLeggeAggiornato().getAnno());
		assertEquals("Anno Atto di legge aggiornato diverso da quello richiesto.", annoAttoLegge, res.getAttoDiLeggeAggiornato().getAnno());
		
		//NUMERO ATTO
		assertNotNull(res.getAttoDiLeggeAggiornato().getNumero());
		assertEquals("Numero Atto di legge aggiornato diverso da quello richiesto.", numeroAttoLegge, res.getAttoDiLeggeAggiornato().getNumero());

		//TIPO ATTO
		//assertNotNull(res.getAttoDiLeggeAggiornato().getTipoAtto());
		//assertEquals("Elemento Atto di legge aggiornato diverso da quello richiesto.", uidTipoAttoLegge.intValue(), res.getAttoDiLeggeAggiornato().getTipoAtto().getUid());
		//assertEquals("Descrizione Atto di legge aggiornata diversa da quella richiesta.", descrTipoAttoLegge, res.getAttoDiLeggeAggiornato().getTipoAtto().getDescrizione());
		
		//ARTICOLO ATTO
		if (articoloAttoLegge != null)
		{
			assertNotNull(res.getAttoDiLeggeAggiornato().getArticolo());
			assertEquals("Articolo Atto di legge aggiornato diverso da quello richiesto.", articoloAttoLegge, res.getAttoDiLeggeAggiornato().getArticolo());
		}
		
		//COMMA ATTO
		if (commaAttoLegge != null)
		{
			assertNotNull(res.getAttoDiLeggeAggiornato().getComma());
			assertEquals("Comma Atto di legge aggiornato diverso da quello richiesto.", commaAttoLegge, res.getAttoDiLeggeAggiornato().getComma());
		}

		//PUNTO ATTO
		if (puntoAttoLegge != null)
		{
			assertNotNull(res.getAttoDiLeggeAggiornato().getPunto());
			assertEquals("Punto Atto di legge aggiornato diverso da quello richiesto.", puntoAttoLegge, res.getAttoDiLeggeAggiornato().getPunto());
		}
	}

	/**
	 * Aggiorna atto di legge.
	 *
	 * @return the aggiorna atto di legge response
	 */
	private AggiornaAttoDiLeggeResponse aggiornaAttoDiLegge() {
		
		AggiornaAttoDiLegge req = new AggiornaAttoDiLegge();	
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setAttoDiLegge(getAttoDiLegge());

		AggiornaAttoDiLeggeResponse res = attoDiLeggeService.aggiornaAttoDiLegge(req);
		return res;
	}
}
