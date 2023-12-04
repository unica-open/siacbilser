/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.provvedimento;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimentoResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class VerificaAnnullabilitaGestioneProvvedimentoServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml"/*, "/spring/datasource-test.xml"*/})
public class VerificaAnnullabilitaGestioneProvvedimentoServiceTest extends ProvvedimentoMainTest  
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
		return spreadsheetData("VerificaAnnulla");	 
	}
   
	/**
	 * Instantiates a new verifica annullabilita gestione provvedimento service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidAtto the uid atto
	 * @param annoAtto the anno atto
	 * @param numeroAtto the numero atto
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 * @param isannull the isannull
	 */
	public VerificaAnnullabilitaGestioneProvvedimentoServiceTest(String obiettivoTest, 
		String uidEnte, String CFisc, String uidAtto, String annoAtto, String numeroAtto,
		String Esito, String Errore, String Descrizione, String Testout, String isannull) 
	{
		this.obiettivoTest = obiettivoTest;
		this.uidEnte = toInteger(uidEnte);
		this.cFisc = CFisc;
		
		this.uidAttoAmministrativo = toInteger(uidAtto);	
		this.annoAttoAmministrativo = toInteger(annoAtto);	
	    this.numeroAttoAmministrativo = toInteger(numeroAtto);	
	    this.uidTipoAttoAmministrativo = 0;   
	    this.tipoAttoAmministrativo = ""; 
	    this.strutturaContabileAttoAmministrativo = "";		
	    this.oggettoAttoAmministrativo = "";	
	    this.statoOperativoAttoAmministrativo = "";
	    this.noteAttoAmministrativo = "";
  	
  		this.esito = Esito;
  		this.errore = Errore;
		
		String new_descr = annoAttoAmministrativo+"/"+numeroAttoAmministrativo;
		Descrizione.replaceFirst("@", new_descr);
		
  		this.descrizione = Descrizione;
  		this.testOut = Testout;
  		
        this.isannull = false;
        if (isannull.matches("SI")) this.isannull = true;	
	}

	/**
	 * Test verifica annulla.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testVerificaAnnulla() throws Throwable
	{     
		VerificaAnnullabilitaProvvedimentoResponse res = verificaAnnullabilitaProvvedimento();
		
		assertServiceResponse(res);

		if (testOut.equals("OUT NO")) {
			return;
		}
		
		//TESTA GLI OGGETTI DI RITORNO :
		// ATTO ANNULLABILE 0=NO / 1=SI
		assertEquals("Risultato verifica annullabilita diverso da atteso!!", isannull, res.getAnnullabilitaAttoAmministrativo());
		
	}

	/**
	 * Verifica annullabilita provvedimento.
	 *
	 * @return the verifica annullabilita provvedimento response
	 */
	private VerificaAnnullabilitaProvvedimentoResponse verificaAnnullabilitaProvvedimento() {

		VerificaAnnullabilitaProvvedimento req = new VerificaAnnullabilitaProvvedimento();
		
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAttoAmministrativo(getAttoAmministrativo());
		
		VerificaAnnullabilitaProvvedimentoResponse res = provvedimentoService.verificaAnnullabilitaProvvedimento(req);

		return res;		
	}

}
