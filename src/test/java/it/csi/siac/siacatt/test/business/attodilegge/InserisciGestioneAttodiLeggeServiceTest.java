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
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLeggeResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisciGestioneAttodiLeggeServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class InserisciGestioneAttodiLeggeServiceTest extends AttodiLeggeMainTest 
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
		return spreadsheetData("Inserisci");	 
	}
	   
	/**
	 * Instantiates a new inserisci gestione attodi legge service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
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
	public InserisciGestioneAttodiLeggeServiceTest(String obiettivoTest, 
		String uidEnte, String CFisc, String annoAtto, String numeroAtto, 
		String uidTipoAtto, String DescrTipoAtto, String articoloAtto, String commaAtto, 
		String puntoAtto, String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
		this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
			
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
	 * Test inserisci.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testInserisci() throws Throwable
	{       
		InserisceAttoDiLeggeResponse res = inserisceAttoDiLegge();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO :
		// TESTO ATTO DI LEGGE APPENA INSERITO
		assertNotNull("Inserimento Atto di Legge fallito : Atto di Legge ritornato dal servizio null", res.getAttoDiLeggeInserito());
		
		//ANNO ATTO
		assertNotNull(res.getAttoDiLeggeInserito().getAnno());
		assertEquals("Anno Atto di legge diverso da quello inserito.", annoAttoLegge, res.getAttoDiLeggeInserito().getAnno());
		
		//NUMERO ATTO
		assertNotNull(res.getAttoDiLeggeInserito().getNumero());
		assertEquals("Numero Atto di legge diverso da quello inserito.", numeroAttoLegge, res.getAttoDiLeggeInserito().getNumero());

		//TIPO ATTO
		//assertNotNull(res.getAttoDiLeggeInserito().getTipoAtto());
		//assertEquals("Elemento Atto di legge diverso da quello inserito.", uidTipoAttoLegge.intValue(), res.getAttoDiLeggeInserito().getTipoAtto().getUid());
		//assertEquals("Descrizione Atto di legge diversa da quello inserita.", descrTipoAttoLegge, res.getAttoDiLeggeInserito().getTipoAtto().getDescrizione());
		
		//ARTICOLO ATTO
		if (articoloAttoLegge != null)
		{
			assertNotNull(res.getAttoDiLeggeInserito().getArticolo());
			assertEquals("Articolo Atto di legge diverso da quello inserito.", articoloAttoLegge, res.getAttoDiLeggeInserito().getArticolo());
		}
		
		//COMMA ATTO
		if (commaAttoLegge != null)
		{
			assertNotNull(res.getAttoDiLeggeInserito().getComma());
			assertEquals("Comma Atto di legge diverso da quello inserito.", commaAttoLegge, res.getAttoDiLeggeInserito().getComma());
		}

		//PUNTO ATTO
		if (puntoAttoLegge != null)
		{
			assertNotNull(res.getAttoDiLeggeInserito().getPunto());
			assertEquals("Punto Atto di legge diverso da quello inserito.", puntoAttoLegge, res.getAttoDiLeggeInserito().getPunto());
		}
	}
	
	/**
	 * Inserisce atto di legge.
	 *
	 * @return the inserisce atto di legge response
	 */
	private InserisceAttoDiLeggeResponse inserisceAttoDiLegge() {
		
		InserisceAttoDiLegge req = new InserisceAttoDiLegge();	
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setAttoDiLegge(getAttoDiLegge());

		InserisceAttoDiLeggeResponse res = attoDiLeggeService.inserisceAttoDiLegge(req);
		return res;
	}
}
