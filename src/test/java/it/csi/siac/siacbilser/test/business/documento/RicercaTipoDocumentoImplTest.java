/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.RicercaTipoDocumentoService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaTipoDocumentoImplIDLTest.
 */
public class RicercaTipoDocumentoImplTest extends BaseJunit4TestCase
{
	
	/** The ricerca tipo documento service. */
	@Autowired
	private RicercaTipoDocumentoService ricercaTipoDocumentoService;
	
	
	/**
	 * Test ricerca manca natura onere.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaMancaNaturaOnere() throws Throwable{


		try
		{
			RicercaTipoDocumento req = new RicercaTipoDocumento();

			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			RicercaTipoDocumentoResponse res = ricercaTipoDocumentoService.executeService(req);
			
			assertTrue(res.getEsito().equals(Esito.FALLIMENTO));
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	
	}
	
	/**
	 * Test ricerca entrata.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaEntrata() throws Throwable{
		
		
		try
		{
			RicercaTipoDocumento req = new RicercaTipoDocumento();
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			req.setTipoFamDoc(TipoFamigliaDocumento.ENTRATA);
			
			RicercaTipoDocumentoResponse res = ricercaTipoDocumentoService.executeService(req);
			
			assertTrue(res.getEsito().equals(Esito.SUCCESSO));
			
			for (TipoDocumento el : res.getElencoTipiDocumento())
				log.debug("testRicercaEntrata", "TIPO DOCUMENTO " + el.getUid() + " " + el.getCodice() + " " + el.getDescrizione());
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * Test ricerca spesa.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaSpesa() throws Throwable{
		
		
		try
		{
			RicercaTipoDocumento req = new RicercaTipoDocumento();
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			Ente ente = getEnteTest();
			req.setEnte(ente);
			
			req.setTipoFamDoc(TipoFamigliaDocumento.SPESA);
			
			RicercaTipoDocumentoResponse res = ricercaTipoDocumentoService.executeService(req);
			
			assertTrue(res.getEsito().equals(Esito.SUCCESSO));
			
			for (TipoDocumento el : res.getElencoTipiDocumento())
				log.debug("testRicercaSpesa", "TIPO DOCUMENTO " + el.getUid() + " " + el.getCodice() + " " + el.getDescrizione());
			
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
