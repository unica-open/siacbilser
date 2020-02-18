/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.RicercaCausale770Service;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.TipoOnere;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaCausale770ImplIDLTest.
 */
public class RicercaCausale770ImplTest extends BaseJunit4TestCase
{
	
	/** The ricerca causale770 service. */
	@Autowired
	private RicercaCausale770Service ricercaCausale770Service;
	
	
	/**
	 * Test ricerca manca natura onere.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaMancaNaturaOnere() throws Throwable{


		try
		{
			RicercaCausale770 req = new RicercaCausale770();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			RicercaCausale770Response res = ricercaCausale770Service.executeService(req);
			
			assertTrue(res.getEsito().equals(Esito.FALLIMENTO));
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	
	}
	
	/**
	 * Test ricerca ok.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaOk() throws Throwable{
		
		
		try
		{
			RicercaCausale770 req = new RicercaCausale770();
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			TipoOnere tipoOnere = new TipoOnere();
			tipoOnere.setCodice("1040");
			req.setTipoOnere(tipoOnere);
			
			RicercaCausale770Response res = ricercaCausale770Service.executeService(req);
			
			assertTrue(res.getEsito().equals(Esito.SUCCESSO));
			
			for (Causale770 el : res.getElencoCausali())
				log.debug("testRicercaOk", "CAUSALE 770 " + el.getUid() + " " + el.getCodice() + " " + el.getDescrizione());
			
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
