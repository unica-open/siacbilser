/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.variazionicodifica;

import it.csi.siac.siacbilser.test.TestBase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class VariazioneCodificheMainTestBase.
 */
public class VariazioneCodificheMainTestBase extends TestBase
{
	//PARAMETRI SCENARI DI TEST
	/** The uid ente. */
	protected Integer uidEnte;
	
	/** The c fisc. */
	protected String cFisc;
  	
	  /** The uid bil. */
	  protected Integer uidBil;
   	
	   /** The anno bil. */
	   protected Integer annoBil;
	
	/** The uid capitolo entrata. */
	protected Integer uidCapitoloEntrata;	
	
	/** The uid capitolo uscita. */
	protected Integer uidCapitoloUscita;
   	
	   /** The anno eser. */
	   protected Integer annoEser;	
	
	/** The uid relazione atto capitolo. */
	protected Integer uidRelazioneAttoCapitolo;
	
	/** The uid atto legge. */
	protected Integer uidAttoLegge;
  	
	  /** The anno atto legge. */
	  protected Integer annoAttoLegge;
   	
	   /** The numero atto legge. */
	   protected Integer numeroAttoLegge;
   	
	   /** The uid tipo atto legge. */
	   protected Integer uidTipoAttoLegge;
	
	/** The descr tipo atto legge. */
	protected String descrTipoAttoLegge;	
	
	/** The atto capi gerarchia. */
	protected String attoCapiGerarchia;
	
	/** The atto capi descrizione. */
	protected String attoCapiDescrizione;

	/**
	 * Gets the ente test.
	 *
	 * @return the ente test
	 */
	protected Ente getEnteTest()
	{		
		try{
			Ente ente = new Ente();
			ente.setUid(uidEnte);
			return  ente;
		} catch(NullPointerException npe){
			return null;
		}		
	}

	/**
	 * Gets the richiedente test.
	 *
	 * @return the richiedente test
	 */
	protected Richiedente getRichiedenteTest()
	{
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale(cFisc);
		richiedente.setOperatore(operatore);

		return richiedente;
	}

    
    /**
     * Gets the bilancio test.
     *
     * @return the bilancio test
     */
    protected Bilancio getBilancioTest(/*int uidBil, int annoBil*/)
	{
    	try {
	    	Bilancio bilancio = new Bilancio();
			bilancio.setUid(uidBil);
			bilancio.setAnno(annoBil);   	
			return bilancio;
    	}catch(NullPointerException npe){
    		return null;
    	}
	}
}
