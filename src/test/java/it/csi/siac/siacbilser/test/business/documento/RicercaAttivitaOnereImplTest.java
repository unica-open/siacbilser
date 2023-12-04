/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.RicercaAttivitaOnereService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.TipoOnere;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaAttivitaOnereImplIDLTest.
 */
public class RicercaAttivitaOnereImplTest extends BaseJunit4TestCase
{
	
	/** The ricerca attivita onere service. */
	@Autowired
	private RicercaAttivitaOnereService ricercaAttivitaOnereService;
	
	
	/**
	 * Test ricerca manca natura onere.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaMancaNaturaOnere() throws Throwable{


		try
		{
			RicercaAttivitaOnere req = new RicercaAttivitaOnere();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			RicercaAttivitaOnereResponse res = ricercaAttivitaOnereService.executeService(req);
			
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
			RicercaAttivitaOnere req = new RicercaAttivitaOnere();
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			TipoOnere tipoOnere = new TipoOnere();
			tipoOnere.setCodice("1040");
			req.setTipoOnere(tipoOnere);
			
			RicercaAttivitaOnereResponse res = ricercaAttivitaOnereService.executeService(req);
			
			assertTrue(res.getEsito().equals(Esito.SUCCESSO));
			
			for (AttivitaOnere el : res.getElencoAttivitaOnere())
				log.debug("testRicercaOk", "ATTIVITA ONERE " + el.getUid() + " " + el.getCodice() + " " + el.getDescrizione());
			
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
