/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.relazioneattodilegge;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;

// TODO: Auto-generated Javadoc
/**
 * The Class CancellaGestioneRelazioneAttodiLeggeServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class CancellaGestioneRelazioneAttodiLeggeServiceTest extends RelazioneAttodiLeggeMainTest 
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
		return spreadsheetData("Cancella");	 
	}
   
	/**
	 * Instantiates a new cancella gestione relazione attodi legge service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidRelazione the uid relazione
	 * @param uidAtto the uid atto
	 * @param annoAtto the anno atto
	 * @param numeroAtto the numero atto
	 * @param uidTipoAtto the uid tipo atto
	 * @param tipoAtto the tipo atto
	 * @param uidCapitoloEntrata the uid capitolo entrata
	 * @param uidCapitoloUscita the uid capitolo uscita
	 * @param attoCapiGerarchia the atto capi gerarchia
	 * @param attoCapiDescrizione the atto capi descrizione
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 */
	public CancellaGestioneRelazioneAttodiLeggeServiceTest(String obiettivoTest, 
		String uidEnte, String CFisc, String uidRelazione, String uidAtto, String annoAtto, String numeroAtto, 
		String uidTipoAtto, String tipoAtto, String uidCapitoloEntrata, String uidCapitoloUscita, 
		String attoCapiGerarchia, String attoCapiDescrizione, String Esito, String Errore, 
		String Descrizione, String Testout) 
	{
		this.obiettivoTest = obiettivoTest;
		
		this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
		
		this.uidRelazioneAttoCapitolo = toInteger(uidRelazione);
		this.uidAttoLegge = toInteger(uidAtto);
		this.annoAttoLegge = toInteger(annoAtto);	
	    this.numeroAttoLegge = toInteger(numeroAtto);		
	    this.uidTipoAttoLegge = toInteger(uidTipoAtto);
	    this.descrTipoAttoLegge = tipoAtto;   
	  	this.uidCapitoloEntrata = toInteger(uidCapitoloEntrata);
		this.uidCapitoloUscita = toInteger(uidCapitoloUscita);	
		this.attoCapiGerarchia = attoCapiGerarchia;	
		this.attoCapiDescrizione = attoCapiDescrizione;	
		
  		this.esito = Esito;
  		this.errore = Errore;
  		
  		if (uidRelazione != null) Descrizione.replaceFirst("@", uidRelazione);

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
		CancellaRelazioneAttoDiLeggeCapitoloResponse res = cancellaRelazioneAttoDiLeggeCapitolo();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
				
		//TESTA GLI OGGETTI DI RITORNO :
		// ATTI DI LEGGE - CAPITOLO -> RELAZIONE INSERITA
		assertNotNull("Cancellazione fallita : Relazione Atto di Legge - Capitolo restituita null", res.getAttoDiLeggeCapitolo());
			
		//ATTO DI LEGGE
		assertNotNull("Atto di Legge restituito null da cancellazione", res.getAttoDiLeggeCapitolo().getAttoDiLegge());
								
		//ANNO ATTO DI LEGGE
		assertNotNull("Anno Atto di Legge restituito null da cancellazione", res.getAttoDiLeggeCapitolo().getAttoDiLegge().getAnno());
				
		//NUMERO ATTO DI LEGGE
		assertNotNull("Numero Atto di Legge restituito null da cancellazione", res.getAttoDiLeggeCapitolo().getAttoDiLegge().getNumero());
			
		//TIPO ATTO DI LEGGE
		//assertNotNull("Tipo Atto di Legge restituito null da cancellazione", res.getAttoDiLeggeCapitolo().getAttoDiLegge().getTipoAtto());
		
		//
	}

	/**
	 * Cancella relazione atto di legge capitolo.
	 *
	 * @return the cancella relazione atto di legge capitolo response
	 */
	private CancellaRelazioneAttoDiLeggeCapitoloResponse cancellaRelazioneAttoDiLeggeCapitolo() 
	{

		CancellaRelazioneAttoDiLeggeCapitolo req = new CancellaRelazioneAttoDiLeggeCapitolo();
		req.setEnte(getEnteTest());
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		if (uidRelazioneAttoCapitolo != null)
		{
			AttoDiLeggeCapitolo attoleggecapi = getAttoDiLeggeCapitolo();
			attoleggecapi.setUid(uidRelazioneAttoCapitolo);
			attoleggecapi.setAttoDiLegge(getAttoDiLegge());
			req.setAttoDiLeggeCapitolo(attoleggecapi);
		}
						
		CancellaRelazioneAttoDiLeggeCapitoloResponse res = relazioneAttoDiLeggeCapitoloService.cancellaRelazioneAttoDiLeggeCapitolo(req);

		return res;		
	}
}
