/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.RicercaTipoOnereService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoOnere;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaTipoOnereImplIDLTest.
 */
public class RicercaTipoOnereImplTest extends BaseJunit4TestCase
{
	
	/** The ricerca tipo onere service. */
	@Autowired
	private RicercaTipoOnereService ricercaTipoOnereService;
	
	
	/**
	 * Test ricerca manca natura onere.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaMancaNaturaOnere() throws Throwable{


		try
		{
			RicercaTipoOnere req = new RicercaTipoOnere();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			RicercaTipoOnereResponse res = ricercaTipoOnereService.executeService(req);
			
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
			RicercaTipoOnere req = new RicercaTipoOnere();
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			NaturaOnere nat = new NaturaOnere();
			nat.setUid(1);
			req.setNaturaOnere(nat);
			
			RicercaTipoOnereResponse res = ricercaTipoOnereService.executeService(req);
			
			assertTrue(res.getEsito().equals(Esito.SUCCESSO));
			
			for (TipoOnere el : res.getElencoTipiOnere())
				log.debug("testRicercaOk", "TIPO ONERE " + el.getUid() + " " + el.getCodice() + " " + el.getDescrizione());
			
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
