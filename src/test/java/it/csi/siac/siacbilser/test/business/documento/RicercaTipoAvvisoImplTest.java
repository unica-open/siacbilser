/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.RicercaTipoAvvisoService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvvisoResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaTipoAvvisoImplIDLTest.
 */
public class RicercaTipoAvvisoImplTest extends BaseJunit4TestCase
{
	
	/** The ricerca tipo avviso service. */
	@Autowired
	private RicercaTipoAvvisoService ricercaTipoAvvisoService;
	
	
	/**
	 * Test ricerca ok.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaOk() throws Throwable{


		try
		{
			RicercaTipoAvviso req = new RicercaTipoAvviso();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			RicercaTipoAvvisoResponse res = ricercaTipoAvvisoService.executeService(req);
			
			for (ClassificatoreGenerico el : res.getElencoTipiAvviso())
				log.debug("testRicercaOk", "TIPO AVVISO " + el.getUid() + " " + el.getCodice() + " " + el.getDescrizione());
			
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
