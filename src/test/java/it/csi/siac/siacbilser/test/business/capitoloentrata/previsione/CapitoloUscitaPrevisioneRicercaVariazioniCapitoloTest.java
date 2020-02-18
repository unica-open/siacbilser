/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;

public class CapitoloUscitaPrevisioneRicercaVariazioniCapitoloTest extends BaseJunit4TestCase{

	@Autowired
	public CapitoloService capitoloService;
	
	@Test
	public void testRicercaVariazioniCapitolo() {
		
		//dichiarazione PARAMETRI
		int uidCapitolo = 202016;
		int anno = 2020;
		int uidBilancio = 136;
		int uidEnte = 2;
		
		CapitoloUscitaPrevisione capUP = new CapitoloUscitaPrevisione();
		capUP.setUid(uidCapitolo);
		capUP.setAnnoCapitolo(anno);
		capUP.setBilancio(getBilancioTest(uidBilancio, anno));
		capUP.setEnte(getEnteTest(uidEnte));

		RicercaVariazioniCapitolo request = new RicercaVariazioniCapitolo();
		request.setAnnoBilancio(2020);
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		request.setDataOra(new Date());
		request.setCapitolo(capUP);
		
		RicercaVariazioniCapitoloResponse response = capitoloService.ricercaVariazioniCapitolo(request);
		
		assertNotNull(response);
		assertTrue(it.csi.siac.siaccorser.model.Esito.SUCCESSO.equals(response.getEsito()));
		
	}
	
}
