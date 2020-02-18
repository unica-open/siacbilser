/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.test.business.generic;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfinser.business.service.common.ListeService;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

public class GenericTest extends BaseJunit4TestCase {

	@Autowired
	private ListeService listeService;
	
	@Test
	public void liste() {
		Liste req = new Liste();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setTipi(new TipiLista[] {TipiLista.TIPO_SIOPE_SPESA_I});
		
		ListeResponse res = listeService.executeService(req);
		assertNotNull(res);
	}
	
}
