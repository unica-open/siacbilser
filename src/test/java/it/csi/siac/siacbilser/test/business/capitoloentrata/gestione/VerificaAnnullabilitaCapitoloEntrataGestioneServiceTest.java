/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class VerificaAnnullabilitaCapitoloEntrataGestioneServiceTest.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"/spring/bilServiceClientContext-test.xml", "/spring/datasource-test.xml"})
public class VerificaAnnullabilitaCapitoloEntrataGestioneServiceTest extends CapitoloEntrataGestioneMainTest 
{

	/** The capitolo entrata gestione service. */
	@Autowired
	private CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
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
	 * Instantiates a new verifica annullabilita capitolo entrata gestione service test.
	 *
	 * @param obiettivoTest the obiettivo test
	 * @param uidEnte the uid ente
	 * @param CFisc the c fisc
	 * @param uidBil the uid bil
	 * @param annoBil the anno bil
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @param numUEB the num ueb
	 * @param statoCapi the stato capi
	 * @param descCapi the desc capi
	 * @param annoCrea the anno crea
	 * @param Esito the esito
	 * @param Errore the errore
	 * @param Descrizione the descrizione
	 * @param Testout the testout
	 * @param isannull the isannull
	 */
	public VerificaAnnullabilitaCapitoloEntrataGestioneServiceTest(String obiettivoTest,
			String uidEnte, String CFisc, String uidBil, String annoBil, String annoCapi, 
			String numeCapi, String numeArti, String numUEB, String statoCapi, String descCapi, 
			String annoCrea, String Esito, String Errore, String Descrizione, String Testout, 
			String isannull) 
	{
		this.obiettivoTest = obiettivoTest;
        this.uidEnte = toInteger(uidEnte);
        this.cFisc = CFisc;
        this.uidBil = toInteger(uidBil);	
        this.annoBil = toInteger(annoBil); this.annoEser = toInteger(annoBil);	
        this.annoCapi = toInteger(annoCapi);		
        this.numCapi = toInteger(numeCapi);	
        this.numArti = toInteger(numeArti);
        this.numUEB = toInteger(numUEB);
        this.statoCapi = statoCapi;		
        this.descCapi = descCapi;
        this.annoCrea = toInteger(annoCrea);
        this.esito = Esito;
        this.errore = Errore;
        this.descrizione = Descrizione;
        this.testOut = Testout;
        this.isannull = false;
        if (isannull.matches("SI")) this.isannull = true;		
    }

	/**
	 * Test verifica annullabilta.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testVerificaAnnullabilta() throws Throwable
	{
		
		VerificaAnnullabilitaCapitoloEntrataGestioneResponse res = verificaAnnullabilitaCapitoloEntrataGestione();
		
		assertNotNull(res);

//		if (testOut.equals("OUT NO")) {
//			return;
//		}
			
		//TESTA GLI OGGETTI DI RITORNO
		// CAPITOLO ANNULLABILE 0=NO / 1=SI
		//assertEquals("Risultato verifica annullabilita diverso da atteso!!", isannull, res.isAnnullabilitaCapitolo());
	}

	
	/**
	 * Verifica annullabilita capitolo entrata gestione.
	 *
	 * @return the verifica annullabilita capitolo entrata gestione response
	 */
	private VerificaAnnullabilitaCapitoloEntrataGestioneResponse verificaAnnullabilitaCapitoloEntrataGestione() {
		LogSrvUtil log = new LogSrvUtil(this.getClass());
		
		VerificaAnnullabilitaCapitoloEntrataGestione req = new VerificaAnnullabilitaCapitoloEntrataGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedente("RMNLSS", 1, 1);
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest(); //getBilancioTest(1, 2015);
		req.setBilancio(bilancio);
		
		CapitoloEntrataGestione capitoloEntrataGestione = getCapitoloEntrataGestione();
		//capitoloEntrataGestione.setUid(95536);
		req.setCapitolo(capitoloEntrataGestione);
		
		VerificaAnnullabilitaCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.verificaAnnullabilitaCapitoloEntrataGestione(req);
		log.logXmlTypeObject(res, "RESPONSE");//log.logXmlTypeObject();
		return res;		
	}
	
	
	protected Richiedente getRichiedente(String codiceFiscaleOperatore, int uidAccount, int uidEnte) {
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale(codiceFiscaleOperatore);
		richiedente.setOperatore(operatore);
		Account account = getAccount(uidAccount, uidEnte);
		richiedente.setAccount(account);
		return richiedente;
	}

	
	protected Bilancio getBilancioTest(Integer uid,Integer anno) {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(uid);
		bilancio.setAnno(anno);
		return bilancio;
	}
	
	private Account getAccount(int uidAccount, int uidEnte) {
		Account account = new Account();
		account.setUid(uidAccount);
		Ente ente = new Ente();
		ente.setUid(uidEnte);
		account.setEnte(ente);
		account.setNome("MioNomeFinto");
		return account;
	}
	
}
