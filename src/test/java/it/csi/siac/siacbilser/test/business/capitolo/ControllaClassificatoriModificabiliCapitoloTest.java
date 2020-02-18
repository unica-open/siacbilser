/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolo;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitolo.ControllaAttributiModificabiliCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.ControllaClassificatoriModificabiliCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

/**
 * The Class ControllaClassificatoriModificabiliCapitoloDLTest.
 */
public class ControllaClassificatoriModificabiliCapitoloTest extends BaseJunit4TestCase
{

	
	/** The controlla classificatori modificabili capitolo service. */
	@Autowired
	private ControllaClassificatoriModificabiliCapitoloService controllaClassificatoriModificabiliCapitoloService;
	
	/** The controlla attributi modificabili capitolo service. */
	@Autowired
	private ControllaAttributiModificabiliCapitoloService controllaAttributiModificabiliCapitoloService;
	
	
	
	/**
	 * Test controllo attributi.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testControlloAttributi() throws Throwable {
		final String methodName = "testControlloAttributi";

		ControllaAttributiModificabiliCapitolo req = new ControllaAttributiModificabiliCapitolo();
		req.setBilancio(getBilancioTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setNumeroCapitolo(1); //4444
		req.setNumeroArticolo(1); //1
		req.setNumeroUEB(1); //3
		req.setTipoCapitolo(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE);
		
		
		ControllaAttributiModificabiliCapitoloResponse res = controllaAttributiModificabiliCapitoloService.executeService(req);
		assertNotNull(res);
		
		log.info(methodName, "esito: "+res.getEsito());
		log.info(methodName, "errori: "+res.getErrori());
		
		assertNotNull(res.getAttributiNonModificabili());
		log.info(methodName, " Classificatori Non Modificabili: "+ res.getAttributiNonModificabili());

	}
	

	/**
	 * Test controllo classificatori.
	 *
	 * @throws Throwable the throwable
	 */
	
	@Test
	public void testControlloClassificatori() throws Throwable {
		final String methodName = "testControlloClassificatori";

		ControllaClassificatoriModificabiliCapitolo req = new ControllaClassificatoriModificabiliCapitolo();
		
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());

		req.setBilancio(getBilancioTest(132, 2018));

		req.setEnte(getEnteTest());
		req.setNumeroCapitolo(108662); 
		req.setNumeroArticolo(88); 
		req.setNumeroUEB(1); 
		req.setTipoCapitolo(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
		
		req.setModalitaAggiornamento(true);
		
		
		ControllaClassificatoriModificabiliCapitoloResponse res = controllaClassificatoriModificabiliCapitoloService.executeService(req);
		assertNotNull(res);
		
		log.info(methodName, "esito: "+res.getEsito());
		log.info(methodName, "errori: "+res.getErrori());
		
		assertNotNull(res.getClassificatoriNonModificabili());
		log.info(methodName, " Classificatori Non Modificabili: "+ res.getClassificatoriNonModificabili());
		
		log.info(methodName, " Classificatori Non Modificabili MG: "+ res.getClassificatoriNonModificabiliPerMovimentoGestione());

	}
	
	
	
	
	
	
	
	
	
	/**
	 * Test aggiorna.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAggiorna() throws Throwable {


		try
		{

			

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	
	}

	
	
	/**
	 * Test ricerca sintetica descrizione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test 
	public void testRicercaSinteticaDescrizione() throws Throwable{


		try
		{
			

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	
	}
	
	
	
	
	
	
}
