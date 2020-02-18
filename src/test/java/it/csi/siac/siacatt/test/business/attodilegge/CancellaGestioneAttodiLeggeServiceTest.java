/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.attodilegge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLeggeResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class CancellaGestioneAttodiLeggeServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class CancellaGestioneAttodiLeggeServiceTest extends AttodiLeggeMainTest 
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
		return spreadsheetData("Cancella");	 
	}
	   
	/**
	 * Instantiates a new cancella gestione attodi legge service test.
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
	public CancellaGestioneAttodiLeggeServiceTest(String obiettivoTest, 
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
	 * Test cancella.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCancella() throws Throwable
	{   
		CancellaAttoDiLeggeResponse res = cancellaAttoDiLegge();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO :
		// TESTO ATTO DI LEGGE APPENA CANCELLATO
		assertNotNull("Cancellazione Atto di Legge fallito : Atto di Legge ritornato dal servizio null", res.getAttoDiLeggeCancellato());
		
		//ANNO ATTO
		assertNotNull(res.getAttoDiLeggeCancellato().getAnno());
		assertEquals("Anno Atto di legge cancellato diverso da quello richiesto.", annoAttoLegge, res.getAttoDiLeggeCancellato().getAnno());
		
		//NUMERO ATTO
		assertNotNull(res.getAttoDiLeggeCancellato().getNumero());
		assertEquals("Numero Atto di legge cancellato diverso da quello richiesto.", numeroAttoLegge, res.getAttoDiLeggeCancellato().getNumero());

		//TIPO ATTO
		//assertNotNull(res.getAttoDiLeggeCancellato().getTipoAtto());
		//assertEquals("Elemento Atto di legge cancellato diverso da quello richiesto.", uidTipoAttoLegge.intValue(), res.getAttoDiLeggeCancellato().getTipoAtto().getUid());
		//assertEquals("Descrizione Atto di legge cancellato diversa da quella richiesta.", descrTipoAttoLegge, res.getAttoDiLeggeCancellato().getTipoAtto().getDescrizione());
		
		//ARTICOLO ATTO
		if (articoloAttoLegge != null)
		{
			//assertNotNull(res.getAttoDiLeggeCancellato().getArticolo());
			//assertEquals("Articolo Atto di legge cancellato diverso da quello richiesto.", articoloAttoLegge, res.getAttoDiLeggeCancellato().getArticolo());
		}
		
		//COMMA ATTO
		if (commaAttoLegge != null)
		{
			//assertNotNull(res.getAttoDiLeggeCancellato().getComma());
			//assertEquals("Comma Atto di legge cancellato diverso da quello richiesto.", commaAttoLegge, res.getAttoDiLeggeCancellato().getComma());
		}

		//PUNTO ATTO
		if (puntoAttoLegge != null)
		{
			//assertNotNull(res.getAttoDiLeggeCancellato().getPunto());
			//assertEquals("Punto Atto di legge cancellato diverso da quello richiesto.", puntoAttoLegge, res.getAttoDiLeggeCancellato().getPunto());
		}
	}

	/**
	 * Cancella atto di legge.
	 *
	 * @return the cancella atto di legge response
	 */
	private CancellaAttoDiLeggeResponse cancellaAttoDiLegge() {
		
		CancellaAttoDiLegge req = new CancellaAttoDiLegge();	
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setAttoDiLegge(getAttoDiLegge());
		req.setDataOra(new Date());
		
		CancellaAttoDiLeggeResponse res = attoDiLeggeService.cancellaAttoDiLegge(req);
		return res;
	}
}
