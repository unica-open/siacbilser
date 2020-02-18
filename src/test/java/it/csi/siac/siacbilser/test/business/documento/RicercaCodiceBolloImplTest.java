/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.RicercaCodiceBolloService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaCodiceBolloImplIDLTest.
 */
public class RicercaCodiceBolloImplTest extends BaseJunit4TestCase
{
	
	/** The ricerca codice bollo service. */
	@Autowired
	private RicercaCodiceBolloService ricercaCodiceBolloService;
	
	
	/**
	 * Test ricerca ok.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaOk() throws Throwable{


		try
		{
			RicercaCodiceBollo req = new RicercaCodiceBollo();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			RicercaCodiceBolloResponse res = ricercaCodiceBolloService.executeService(req);
			
			for (CodiceBollo el : res.getElencoCodiciBollo())
				log.debug("testRicercaOk", "CODICE BOLLO " + el.getUid() + " " + el.getCodice() + " " + el.getDescrizione());
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.test.BaseJunit4TestCase#getEnteTest()
	 */
	protected Ente getEnteTest()
	{
		Ente ente = new Ente();
		ente.setUid(1);
		//ente.setNome("mio nome di prova");
		return ente;
	}
	
	
}
