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
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisciGestioneProvvedimentoServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class InserisciGestioneProvvedimentoServiceTest extends ProvvedimentoMainTest 
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
		return spreadsheetData("Inserisci");	 
	}
   
	/**
	 * Instantiates a new inserisci gestione provvedimento service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
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
	public InserisciGestioneProvvedimentoServiceTest(String obiettivoTest, 
		String uidEnte, String CFisc, String annoAtto, String numeroAtto, 
		String uidTipoAtto, String DescrTipoAtto, String strutturaContabileAtto, 
		String oggettoAtto,	String statoOperativoAtto, String noteAtto,	
		String Esito, String Errore, String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
		this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
		
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
	 * Test inserisci.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testInserisci() throws Throwable
	{     
		InserisceProvvedimentoResponse res = inserisceProvvedimento();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO :
		// ATTI AMMINISTRATIVO INSERITO
		assertNotNull("Inserimento Provvedimento fallito : Atti Amministrativo restituito a null", res.getAttoAmministrativoInserito());
		
		//ANNO 
		assertNotNull(res.getAttoAmministrativoInserito().getAnno());
		assertEquals("Anno Atto Amministrativo diverso da quello ricercato.", annoAttoAmministrativo.intValue(), res.getAttoAmministrativoInserito().getAnno());
		
		//NUMERO 
		assertNotNull(res.getAttoAmministrativoInserito().getNumero());
		assertEquals("Numero Atto Amministrativo diverso da quello ricercato.", numeroAttoAmministrativo.intValue(), res.getAttoAmministrativoInserito().getNumero());

		//TIPO 
		//assertNotNull(res.getAttoAmministrativoInserito().getTipoAtto());
		//assertEquals("Tipo Atto Amministrativo diverso da quello ricercato.", tipoAttoAmministrativo, res.getAttoAmministrativoInserito().getTipoAtto());
		
		//STRUTTURA CONTABILE
		if (strutturaContabileAttoAmministrativo != null)
		{
			//assertNotNull(res.getAttoAmministrativoInserito().getStrutturaAmmContabile());
			//assertEquals("Struttura Contabile Atto diversa da quello ricercata.", strutturaContabileAttoAmministrativo, res.getAttoAmministrativoInserito().getStrutturaAmmContabile());
		}
		
		//OGGETTO ATTO
		if (oggettoAttoAmministrativo != null)
		{
			assertNotNull(res.getAttoAmministrativoInserito().getOggetto());
			assertEquals("Oggetto Atto diverso da quello ricercato.", oggettoAttoAmministrativo, res.getAttoAmministrativoInserito().getOggetto());
		}

		//STATO OPERATIVO ATTO
		if (statoOperativoAttoAmministrativo != null)
		{
			//assertNotNull(res.getAttoAmministrativoInserito().getStatoOperativo());
			//assertEquals("Stato Operativo Atto diverso da quello ricercato.", statoOperativoAttoAmministrativo, res.getAttoAmministrativoInserito().getStatoOperativo());
		}

		//NOTE
		if (noteAttoAmministrativo != null)
		{
			assertNotNull(res.getAttoAmministrativoInserito().getNote());
			assertEquals("Note Atto diverse da quello ricercate.", noteAttoAmministrativo, res.getAttoAmministrativoInserito().getNote());
		}

	}

	/**
	 * Inserisce provvedimento.
	 *
	 * @return the inserisce provvedimento response
	 */
	private InserisceProvvedimentoResponse inserisceProvvedimento() {

		InserisceProvvedimento req = new InserisceProvvedimento();
		
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAttoAmministrativo(getAttoAmministrativo());
		req.setStrutturaAmministrativoContabile(getStrutturaAmministrativoContabile());
		req.setTipoAtto(getTipoAtto());
		
		InserisceProvvedimentoResponse res = provvedimentoService.inserisceProvvedimento(req);

		return res;		
	}

}
