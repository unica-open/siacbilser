/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.bilancio;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.bilancio.AggiornaAttributiBilancioService;
import it.csi.siac.siacbilser.business.service.bilancio.RicercaAttributiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancioResponse;
import it.csi.siac.siacbilser.model.AttributiBilancio;
import it.csi.siac.siacbilser.model.TipoMediaPrescelta;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

/**
 * The Class BilancioTest.
 */
public class BilancioTest extends BaseJunit4TestCase {
	
	@Autowired
	private AggiornaAttributiBilancioService aggiornaAttributiBilancioService;
	@Autowired
	private RicercaAttributiBilancioService ricercaAttributiBilancioService;
	
	/**
	 * Aggiornamento degli attributi del bilancio
	 */
	@Test
	public void aggiornaAttributiBilancio() {
		AggiornaAttributiBilancio req = new AggiornaAttributiBilancio();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancio2015Test());
		
		AttributiBilancio attributiBilancio = new AttributiBilancio();
		req.setAttributiBilancio(attributiBilancio);
		attributiBilancio.setAccantonamentoGraduale(Boolean.FALSE);
		attributiBilancio.setMediaApplicata(TipoMediaPrescelta.SEMPLICE);
		attributiBilancio.setPercentualeAccantonamentoAnno(new BigDecimal(10));
		attributiBilancio.setPercentualeAccantonamentoAnno1(new BigDecimal(20));
		attributiBilancio.setPercentualeAccantonamentoAnno2(new BigDecimal(30));
		attributiBilancio.setRiscossioneVirtuosa(Boolean.FALSE);
		attributiBilancio.setUltimoAnnoApprovato(2014);
		
		AggiornaAttributiBilancioResponse res = aggiornaAttributiBilancioService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca degli attributi del bilancio
	 */
	@Test
	public void ricercaAttributiBilancio() {
		RicercaAttributiBilancio req = new RicercaAttributiBilancio();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		
		RicercaAttributiBilancioResponse res = ricercaAttributiBilancioService.executeService(req);
		assertNotNull(res);
	}
}
