/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.provvedimento;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimentoResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaGestioneProvvedimentoServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class AggiornaGestioneProvvedimentoServiceTest extends ProvvedimentoMainTest 
{
	
	/** The provvedimento service. */
	@Autowired
	private ProvvedimentoService provvedimentoService;
 
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
	 * Instantiates a new aggiorna gestione provvedimento service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidAtto the uid atto
	 * @param annoAtto the anno atto
	 * @param numeroAtto the numero atto
	 * @param uidTipoAtto the uid tipo atto
	 * @param DescrTipoAtto the descr tipo atto
	 * @param strutturaContabileAtto the struttura contabile atto
	 * @param oggettoAtto the oggetto atto
	 * @param statoOperativoAtto the stato operativo atto
	 * @param noteAtto the note atto
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public AggiornaGestioneProvvedimentoServiceTest(String obiettivoTest, 
		String uidEnte, String CFisc, String uidAtto, String annoAtto, String numeroAtto, 
		String uidTipoAtto, String DescrTipoAtto, String strutturaContabileAtto, String oggettoAtto,
		String statoOperativoAtto, String noteAtto,	String Esito, String Errore, String Descrizione, 
		String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
		this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
		
		this.uidAttoAmministrativo = toInteger(uidAtto);
		this.annoAttoAmministrativo = toInteger(annoAtto);	
		this.numeroAttoAmministrativo = toInteger(numeroAtto);
	    this.uidTipoAttoAmministrativo = toInteger(uidTipoAtto);   
	    this.tipoAttoAmministrativo = DescrTipoAtto; 
	    this.strutturaContabileAttoAmministrativo = strutturaContabileAtto;		
	    this.oggettoAttoAmministrativo = oggettoAtto;	
	    this.statoOperativoAttoAmministrativo = statoOperativoAtto;
	    this.noteAttoAmministrativo = noteAtto;
  	
  		this.esito = Esito;
  		this.errore = Errore;
		
		String new_descr = annoAttoAmministrativo+"/"+numeroAttoAmministrativo;
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
		AggiornaProvvedimentoResponse res = aggiornaProvvedimento();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO :
		// ATTI AMMINISTRATIVO AGGIORNATO
		assertNotNull("Aggiornamento Provvedimento fallito : Atti Amministrativo restituito a null", res.getAttoAmministrativoAggiornato());
		
		//ANNO 
		assertNotNull(res.getAttoAmministrativoAggiornato().getAnno());
		assertEquals("Anno Atto Amministrativo diverso da quello da aggiornare.", annoAttoAmministrativo.intValue(), res.getAttoAmministrativoAggiornato().getAnno());
		
		//NUMERO 
		assertNotNull(res.getAttoAmministrativoAggiornato().getNumero());
		assertEquals("Numero Atto Amministrativo diverso da quello da aggiornare.", numeroAttoAmministrativo.intValue(), res.getAttoAmministrativoAggiornato().getNumero());

		//TIPO 
		assertNotNull(res.getAttoAmministrativoAggiornato().getTipoAtto());
		assertEquals("Tipo Atto Amministrativo diverso da quello da aggiornare.", tipoAttoAmministrativo, res.getAttoAmministrativoAggiornato().getTipoAtto());
		
		//STRUTTURA CONTABILE
		if (strutturaContabileAttoAmministrativo != null)
		{
			assertNotNull(res.getAttoAmministrativoAggiornato().getStrutturaAmmContabile());
			//assertEquals("Struttura Contabile Atto diversa da quella da aggiornare.", strutturaContabileAttoAmministrativo, res.getAttoAmministrativoAggiornato().getStrutturaAmmContabile());
		}
		
		//OGGETTO ATTO
		if (oggettoAttoAmministrativo != null)
		{
			assertNotNull(res.getAttoAmministrativoAggiornato().getOggetto());
			assertEquals("Oggetto Atto diverso da quello da aggiornare.", oggettoAttoAmministrativo, res.getAttoAmministrativoAggiornato().getOggetto());
		}

		//STATO OPERATIVO ATTO
		if (statoOperativoAttoAmministrativo != null)
		{
			assertNotNull(res.getAttoAmministrativoAggiornato().getStatoOperativo());
			assertEquals("Stato Operativo Atto diverso da quello da aggiornare.", statoOperativoAttoAmministrativo, res.getAttoAmministrativoAggiornato().getStatoOperativo());
		}

		//NOTE
		if (noteAttoAmministrativo != null)
		{
			assertNotNull(res.getAttoAmministrativoAggiornato().getNote());
			assertEquals("Note Atto diverse da quello da aggiornare.", noteAttoAmministrativo, res.getAttoAmministrativoAggiornato().getNote());
		}

	}

	/**
	 * Aggiorna provvedimento.
	 *
	 * @return the aggiorna provvedimento response
	 */
	private AggiornaProvvedimentoResponse aggiornaProvvedimento() {

		AggiornaProvvedimento req = new AggiornaProvvedimento();
		
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAttoAmministrativo(getAttoAmministrativo());
		req.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		req.setTipoAtto(getTipoAtto());
		
		AggiornaProvvedimentoResponse res = provvedimentoService.aggiornaProvvedimento(req);

		return res;		
	}
}
