/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.registroiva;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.registroiva.RicercaSinteticaStampaIvaService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;

/**
 * The Class StornoImplDLTest.
 */
public class SampaIvaTest extends BaseJunit4TestCase // extends TestCase
{
	
	/** The inserisce storno ueb service. */
	@Autowired
	private RicercaSinteticaStampaIvaService ricercaSinteticaStampaIvaService;
	
	/**
	 * Test inserisce storno uscita.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaSinteticaStampaIvaService() throws Throwable {

		RicercaSinteticaStampaIva req = new RicercaSinteticaStampaIva();
		req.setRichiedente(getRichiedenteByProperties("consip", "coal"));
		StampaIva stampaIva = new StampaIva();
		req.setStampaIva(stampaIva);
		
		stampaIva.setTipoStampaIva(TipoStampaIva.REGISTRO);
		
		List<RegistroIva> listaRegistroIva = new ArrayList<RegistroIva>();
		stampaIva.setListaRegistroIva(listaRegistroIva);
//		Calendar c = new GregorianCalendar();
//		c.set(2015, 0, 16);
//		stampaIva.setDataCreazione(c.getTime());
		
		stampaIva.setAnnoEsercizio(2018);
		
//		stampaIva.setPeriodo(Periodo.GENNAIO);
		
//		RegistroIva registroIva = new RegistroIva();
//		listaRegistroIva.add(registroIva);
//		
//		registroIva.setUid(43);
//		registroIva.setCodice("REG-BEP-AI");

		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.getParametriPaginazione().setElementiPerPagina(1);
		
//		req.setNomeFile("RegistroIvaAcquistiIvaImmediata");
		
//		stampaIva.setFlagIncassati(Boolean.FALSE);
	

		RicercaSinteticaStampaIvaResponse res = ricercaSinteticaStampaIvaService.executeService(req);
		assertNotNull(res);
	}
	
	
	
	
	
	
	
	
}