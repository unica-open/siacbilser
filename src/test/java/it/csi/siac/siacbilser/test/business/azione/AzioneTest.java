/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.azione;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.azione.RicercaAzionePerChiaveService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiave;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiaveResponse;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Azione;

/**
 * The Class AzioneTest.
 */
public class AzioneTest extends BaseJunit4TestCase {
	
	@Autowired
	private RicercaAzionePerChiaveService ricercaAzionePerChiaveService;
	
	/**
	 * Ricerca azione per chiave
	 */
	@Test
	public void ricercaAzionePerChiave() {
		RicercaAzionePerChiave req = new RicercaAzionePerChiave();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		
		Azione azione = new Azione();
		azione.setNome("OP-GEN-aggiornaPRNotaIntegrataGSA");
		req.setAzione(azione);
		
		RicercaAzionePerChiaveResponse res = ricercaAzionePerChiaveService.executeService(req);
		assertNotNull(res);
	}
	
}
