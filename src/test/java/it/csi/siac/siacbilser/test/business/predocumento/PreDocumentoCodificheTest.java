/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.predocumento;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.predocumentoentrata.LeggiTipiCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.LeggiContiTesoreriaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.LeggiTipiCausaleSpesaService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesaResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoCodificheMVTest.
 */
public class PreDocumentoCodificheTest extends BaseJunit4TestCase {

	/** The leggi tipi causale entrata. */
	@Autowired
	private LeggiTipiCausaleEntrataService leggiTipiCausaleEntrata;

	/** The leggi tipi causale spesa. */
	@Autowired
	private LeggiTipiCausaleSpesaService leggiTipiCausaleSpesa;
	
	/** The leggi conti tesoreria service. */
	@Autowired
	private LeggiContiTesoreriaService leggiContiTesoreriaService;

	
	/**
	 * Leggi tipi causale entrata.
	 */
	@Test
	public void leggiTipiCausaleEntrata() {

		LeggiTipiCausaleEntrata req = new LeggiTipiCausaleEntrata();
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		LeggiTipiCausaleEntrataResponse res = leggiTipiCausaleEntrata.executeService(req);

		assertNotNull(res);
	}

	/**
	 * Leggi tipi causale spesa.
	 */
	@Test
	public void leggiTipiCausaleSpesa() {

		LeggiTipiCausaleSpesa req = new LeggiTipiCausaleSpesa();
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		LeggiTipiCausaleSpesaResponse res = leggiTipiCausaleSpesa.executeService(req);

		assertNotNull(res);
	}

	/**
	 * Leggi conti tesoreria.
	 */
	@Test
	public void leggiContiTesoreria() {

		LeggiContiTesoreria req = new LeggiContiTesoreria();
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		LeggiContiTesoreriaResponse res = leggiContiTesoreriaService.executeService(req);

		assertNotNull(res);
	}

}
