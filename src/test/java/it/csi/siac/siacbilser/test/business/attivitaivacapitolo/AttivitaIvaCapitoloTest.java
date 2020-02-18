/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.attivitaivacapitolo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.attivitaivacapitolo.EliminaRelazioneAttivitaIvaCapitoloService;
import it.csi.siac.siacbilser.business.service.attivitaivacapitolo.InserisceRelazioneAttivitaIvaCapitoloService;
import it.csi.siac.siacbilser.business.service.attivitaivacapitolo.RicercaRelazioneAttivitaIvaCapitoloService;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;

public class AttivitaIvaCapitoloTest extends BaseJunit4TestCase {
	
	
	@Autowired
	private InserisceRelazioneAttivitaIvaCapitoloService inserisceRelazioneAttivitaIvaCapitoloService;
	@Autowired
	private RicercaRelazioneAttivitaIvaCapitoloService ricercaRelazioneAttivitaIvaCapitoloService;
	@Autowired
	private EliminaRelazioneAttivitaIvaCapitoloService eliminaRelazioneAttivitaIvaCapitoloService;
	
	@Test
	public void inserisciRelazione() {
			
		InserisceRelazioneAttivitaIvaCapitolo req = new InserisceRelazioneAttivitaIvaCapitolo();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AttivitaIva attivita = new AttivitaIva();
		attivita.setUid(2);
		attivita.setEnte(getEnteTest());
		Capitolo<?, ?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(1);
		capitolo.setEnte(getEnteTest());
		
		req.setAttivitaIva(attivita);
		req.setCapitolo(capitolo);
		
		InserisceRelazioneAttivitaIvaCapitoloResponse res = inserisceRelazioneAttivitaIvaCapitoloService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void ricercaRelazione() {
		RicercaRelazioneAttivitaIvaCapitolo req = new RicercaRelazioneAttivitaIvaCapitolo();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Capitolo<?, ?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(1);
		capitolo.setEnte(getEnteTest());
		req.setCapitolo(capitolo);
		
		RicercaRelazioneAttivitaIvaCapitoloResponse res = ricercaRelazioneAttivitaIvaCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void eliminaRelazione() {
		EliminaRelazioneAttivitaIvaCapitolo req = new EliminaRelazioneAttivitaIvaCapitolo();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AttivitaIva attivita = new AttivitaIva();
		attivita.setUid(2);
		attivita.setEnte(getEnteTest());
		Capitolo<?, ?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(1);
		capitolo.setEnte(getEnteTest());
		
		req.setAttivitaIva(attivita);
		req.setCapitolo(capitolo);
		
		EliminaRelazioneAttivitaIvaCapitoloResponse res = eliminaRelazioneAttivitaIvaCapitoloService.executeService(req);

		assertNotNull(res);
	}
}	
