/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.attodilegge;

import it.csi.siac.siacbilser.test.TestBase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;

// TODO: Auto-generated Javadoc
/**
 * The Class AttodiLeggeMainTestBase.
 */
public class AttodiLeggeMainTestBase extends TestBase
{
	//PARAMETRI SCENARI DI TEST
	/** The uid ente. */
	protected Integer uidEnte;
	
	/** The c fisc. */
	protected String cFisc;
	
	/** The uid atto legge. */
	protected Integer uidAttoLegge;
  	
	  /** The anno atto legge. */
	  protected Integer annoAttoLegge;
   	
	   /** The numero atto legge. */
	   protected Integer numeroAttoLegge;
	
	/** The articolo atto legge. */
	protected String articoloAttoLegge;	
	
	/** The comma atto legge. */
	protected String commaAttoLegge;
	
	/** The punto atto legge. */
	protected String puntoAttoLegge;	
	
	/** The uid tipo atto legge. */
	protected Integer uidTipoAttoLegge;	
	
	/** The descr tipo atto legge. */
	protected String descrTipoAttoLegge;	

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
