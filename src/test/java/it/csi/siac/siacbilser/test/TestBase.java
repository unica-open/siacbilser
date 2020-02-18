/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.test.context.TestContextManager;

import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class TestBase.
 */
public class TestBase {
	
	
	/** The log report. */
	protected Logger logReport = Logger.getLogger("it.csi.siac.siacbilser.test.report");
	
	/** The log. */
	protected Logger log = Logger.getLogger(this.getClass());
	   	   
	/** The fileparam. */
	protected String fileparam;
	
	/** The testlaunch. */
	protected String testlaunch;
    
    /** The maxrows. */
    protected static int maxrows;
    
    //PARAMETRI GENERALI SCENARI DI TEST
    /** The obiettivo test. */
    protected String obiettivoTest; //breve descrizione obiettivo del test
   
	/** The errore. */
	protected String errore; 
	
	/** The descrizione. */
	protected String descrizione; 
	
	/** The esito. */
	protected String esito;
	
	/** The test out. */
	protected String testOut; //"OUT NO" oppure "NOT NULL"
	
	private Properties accountProperties;
	
	@PostConstruct
	private void init() {
		accountProperties = new Properties();
		
		InputStream is = getClass().getClassLoader().getResourceAsStream("./spring/account.properties");
		if(is != null) {
			try {
				accountProperties.load(is);
			} catch (IOException e) {
				// Non fare nulla
				log.error("Errore nella lettura delle properties", e);
			}
		} else {
			log.error("Properties non lette");
		}
	}
	
		
   	/**
	    * Sets the up.
	    *
	    * @throws Exception the exception
	    */
	   @Before
    public void setUp() throws Exception {
   		TestContextManager testContextManager = new TestContextManager(getClass());		
        testContextManager.prepareTestInstance(this);   
	}  	
   	
   	/**
	    * Spreadsheet data.
	    *
	    * @param spreadSheetFileNamePath the spread sheet file name path
	    * @param sheetName the sheet name
	    * @return the collection
	    * @throws Exception the exception
	    */
	   public static Collection<Object[]> spreadsheetData(String spreadSheetFileNamePath, String sheetName) throws Exception {
    	InputStream spreadsheet = null;
    	//
        try{
    	
            spreadsheet = new FileInputStream(spreadSheetFileNamePath);
        } catch(IOException e) {
        	System.out.println("FILE PARAMETRI NON PRESENTE: " + spreadSheetFileNamePath);
        	fail(e.getMessage());
        }

        SpreadsheetData data = null;
        try{
        	data = new SpreadsheetData(spreadsheet, sheetName);
        } catch(IllegalArgumentException e) {
        	System.out.println("PAGINA NON PRESENTE: " + sheetName);
        	fail(e.getMessage());
        }
        //maxrows=data.getMaxrows();
        return data.getData();
    }
	
    
	/** The regola. */
	@Rule
	public TestRule regola=new TestWatcher() {
		
		private String testcount;
		private String className;
		private String row;
		
		@Override
		protected void starting(Description description) {			
			this.testcount=description.getMethodName().substring(description.getMethodName().indexOf('[')+1,description.getMethodName().length()-1);
			this.testcount=""+(Integer.parseInt(testcount)+1);
			this.row=StringUtils.leftPad(""+(Integer.parseInt(testcount)+1),2,"0");
			this.testcount = StringUtils.leftPad(testcount, 4,"0");
			this.className = description.getClassName().substring(description.getClassName().lastIndexOf(".")+1);	
		}
		
		@Override
		protected void failed(Throwable e, Description description) {			
			logReport.error(className + " RUN"+ testcount + " (excel row:"+row+") FAILED   - Obiettivo test:  " + obiettivoTest + ". Errore:"+ e.getMessage());
		}
		
		  @Override
		protected void succeeded(Description description) {			
			logReport.info( className + " RUN"+ testcount + " (excel row:"+row+") SUCCEDED - Obiettivo test:  " + obiettivoTest);
			
		}
		@Override
		protected void finished(Description description) {
		}
		
		
	};
	
	/**
	 * Gets the service name.
	 *
	 * @param <ERES> the generic type
	 * @param externalServiceResponse the external service response
	 * @return the service name
	 */
	protected <ERES extends ServiceResponse> String getServiceName(ERES externalServiceResponse) {
		return externalServiceResponse.getClass().getSimpleName().replaceAll("(Response)$","")+"Service";
	}
	
	/**
	 * Controlli comuni per tutte le service response.
	 * Controlla che Esito sia quello atteso e se Esito = "FALLIMENTO" controlla che tra gli errori
	 * restituiti ci sia quello atteso.
	 *
	 * @param <RES> the generic type
	 * @param res the res
	 */
	protected <RES extends ServiceResponse> void assertServiceResponse(RES res) {
		String serviceName = getServiceName(res);
		assertNotNull("Risposta "+serviceName+" null!",res);
		assertNotNull("Esito "+serviceName+" null!",res.getEsito());
		assertNotNull("Errori "+serviceName+" null! ", res.getErrori());				
		assertEquals("Esito diverso da quello atteso per il servizio."
		+ ". Elenco risultati verificati: " + res.getErrori()  , esito, res.getEsito().name());		
		
		if("FALLIMENTO".equals(esito)){
			//log.debug("Errori verificati: "+res.getErrori());
			assertTrue( "Non si è verificato il risultato atteso: " 
						+ errore + " - " + descrizione 
						+ ". Elenco risultati verificati: " + res.getErrori(),
					res.verificatoErrore(errore));
		}
	}
	
	/**
	 * To integer.
	 *
	 * @param intValue the int value
	 * @return the integer
	 */
	protected Integer toInteger(String intValue) {
		if(intValue==null) {
			return null;
		}
		return new Integer(intValue);
	}
	
	/**
	 * To big decimal.
	 *
	 * @param intValue the int value
	 * @return the big decimal
	 */
	protected BigDecimal toBigDecimal(String intValue) {
		if(intValue==null) {
			return null;
		}
		return new BigDecimal(intValue);
	}
	
	/**
	 * Ottiene un richiedente di test.
	 *
	 * @param codiceFiscaleOperatore il codice fiscale dell'operatore
	 * @param uidAccount l'uid dell'account
	 * @param uidEnte l'uid dell'ente
	 * 
	 * @return the richiedente test
	 */
	protected Richiedente getRichiedenteTest(String codiceFiscaleOperatore, int uidAccount, int uidEnte) {
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale(codiceFiscaleOperatore);
		richiedente.setOperatore(operatore);
		Account account = getAccountTest(uidAccount, uidEnte);
		richiedente.setAccount(account);
		return richiedente;
	}
	
	/**
	 * Ottiene un richiedente di test dal file di properties.
	 *
	 * @param ambiente l'ambiente da usare (forn2, coll, ...)
	 * @param codiceEnte il codice dell'ente (coto, regp, crp, edisu...)
	 * 
	 * @return the richiedente test
	 */
	protected Richiedente getRichiedenteByProperties(String ambiente, String codiceEnte) {
		String codiceFiscale = accountProperties.getProperty(ambiente + "." + codiceEnte + ".codicefiscale");
		int uidAccount = Integer.parseInt(accountProperties.getProperty(ambiente + "." + codiceEnte + ".accountid"));
		int uidEnte = Integer.parseInt(accountProperties.getProperty(ambiente + "." + codiceEnte + ".enteproprietarioid"));
		
		return getRichiedenteTest(codiceFiscale, uidAccount, uidEnte);
	}
	
	/**
	 * Gets the account test.
	 *
	 * @param uidAccount l'uid dell'account
	 * @param uidEnte l'uid dell'ente
	 * 
	 * @return the account test
	 */
	private Account getAccountTest(int uidAccount, int uidEnte) {
		Account account = new Account();
		account.setUid(uidAccount);
		account.setEnte(getEnteTest(uidEnte));
		account.setNome("MioNomeFinto");
		return account;
	}
	
	/**
	 * Ottiene l'ente con id selezionato.
	 *
	 * @param uid l'uid dell'ente
	 * @return the ente test
	 */
	protected Ente getEnteTest(int uid) {
		Ente ente = new Ente();
		ente.setUid(uid);
		return ente;
	}
}
