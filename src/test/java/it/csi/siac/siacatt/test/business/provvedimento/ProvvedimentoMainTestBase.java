/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.provvedimento;

import it.csi.siac.siacbilser.test.TestBase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class ProvvedimentoMainTestBase.
 */
public class ProvvedimentoMainTestBase extends TestBase
{
	//PARAMETRI SCENARI DI TEST
	/** The uid ente. */
	protected Integer uidEnte;
	
	/** The c fisc. */
	protected String cFisc;
  	
	  /** The uid atto amministrativo. */
	  protected Integer uidAttoAmministrativo;
  	
	  /** The anno atto amministrativo. */
	  protected Integer annoAttoAmministrativo;
   	
	   /** The numero atto amministrativo. */
	   protected Integer numeroAttoAmministrativo;   	
   	
	   /** The uid tipo atto amministrativo. */
	   protected Integer uidTipoAttoAmministrativo;
	
	/** The tipo atto amministrativo. */
	protected String tipoAttoAmministrativo;	
   	
	   /** The uid struttura contabile atto. */
	   protected Integer uidStrutturaContabileAtto;
	
	/** The struttura contabile atto amministrativo. */
	protected String strutturaContabileAttoAmministrativo;	
	
	/** The oggetto atto amministrativo. */
	protected String oggettoAttoAmministrativo;	
	
	/** The stato operativo atto amministrativo. */
	protected String statoOperativoAttoAmministrativo;	
	
	/** The note atto amministrativo. */
	protected String noteAttoAmministrativo;	
	
	/** The isannull. */
	protected boolean isannull;

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
}
