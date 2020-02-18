/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.provvedimento;

import static org.junit.Assert.assertEquals;
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

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaGestioneProvvedimentoServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class RicercaGestioneProvvedimentoServiceTest extends ProvvedimentoMainTest 
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
		return spreadsheetData("Ricerca");	 
	}
   
	/**
	 * Instantiates a new ricerca gestione provvedimento service test.
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
	public RicercaGestioneProvvedimentoServiceTest(String obiettivoTest, 
		String uidEnte, String CFisc, String uidAtto, String annoAtto, String numeroAtto, 
		String uidTipoAtto, String DescrTipoAtto, String strutturaContabileAtto, 
		String oggettoAtto,	String statoOperativoAtto, String noteAtto,	
		String Esito, String Errore, String Descrizione, String Testout) 
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
	 * Test ricerca.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicerca() throws Throwable
	{       
		RicercaProvvedimentoResponse res = ricercaProvvedimento();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO :
		// LISTA ATTI AMMINISTRATIVI
		assertNotNull("Lista Atti Amministrativi restituita null dalla ricerca", res.getListaAttiAmministrativi());
		List<AttoAmministrativo> LAtto = res.getListaAttiAmministrativi();
		if (LAtto != null)
		{
			Iterator<AttoAmministrativo> Atto = LAtto.iterator();
			while (Atto.hasNext())
			{
				assertNotNull("Atto Amministrativo restituito null dalla ricerca", Atto.next());
				
				//ANNO 
				assertNotNull(Atto.next().getAnno());
				assertEquals("Anno Atto Amministrativo diverso da quello ricercato.", annoAttoAmministrativo.intValue(), Atto.next().getAnno());
				
				//NUMERO 
				assertNotNull(Atto.next().getNumero());
				assertEquals("Numero Atto Amministrativo diverso da quello ricercato.", numeroAttoAmministrativo.intValue(), Atto.next().getNumero());

				//TIPO 
				assertNotNull(Atto.next().getTipoAtto());
				assertEquals("Tipo Atto Amministrativo diverso da quello ricercato.", tipoAttoAmministrativo, Atto.next().getTipoAtto());
				
				//STRUTTURA CONTABILE
				if (strutturaContabileAttoAmministrativo != null)
				{
					assertNotNull(Atto.next().getStrutturaAmmContabile());
					assertEquals("Struttura Contabile Atto diversa da quella ricercata.", strutturaContabileAttoAmministrativo, Atto.next().getStrutturaAmmContabile());
				}
				
				//OGGETTO ATTO
				if (oggettoAttoAmministrativo != null)
				{
					assertNotNull(Atto.next().getOggetto());
					assertEquals("Oggetto Atto diverso da quello ricercato.", oggettoAttoAmministrativo, Atto.next().getOggetto());
				}

				//STATO OPERATIVO ATTO
				if (statoOperativoAttoAmministrativo != null)
				{
					assertNotNull(Atto.next().getStatoOperativo());
					assertEquals("Stato Operativo Atto diverso da quello ricercato.", statoOperativoAttoAmministrativo, Atto.next().getStatoOperativo());
				}

				//NOTE
				if (noteAttoAmministrativo != null)
				{
					assertNotNull(Atto.next().getNote());
					assertEquals("Note Atto diverse da quello ricercato.", noteAttoAmministrativo, Atto.next().getNote());
				}
			}
		}			
		
	}

	/**
	 * Ricerca provvedimento.
	 *
	 * @return the ricerca provvedimento response
	 */
	private RicercaProvvedimentoResponse ricercaProvvedimento() {

		RicercaProvvedimento req = new RicercaProvvedimento();
		
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setRicercaAtti(getCriteriRicerca());
		
		RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimento(req);

		return res;		
	}
}
