/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.test.business.classificatore;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfinser.business.service.classificatorefin.LeggiClassificatoriGenericiByTipoMovimentoGestService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoMovimentoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoMovimentoGestResponse;

public class ClassificatoreFinTest extends BaseJunit4TestCase {

	@Autowired private LeggiClassificatoriGenericiByTipoMovimentoGestService leggiClassificatoriGenericiByTipoMovimentoGestService;
	
	@Test
	public void leggiClassificatoriGenericiByTipoMovimentoGest() {
		LeggiClassificatoriGenericiByTipoMovimentoGest req = new LeggiClassificatoriGenericiByTipoMovimentoGest();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnno(2018);
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setCodiceTipoMovimentoGestione("I");
		req.setDataOra(new Date());
		req.setIdEnteProprietario(2);
		
		LeggiClassificatoriGenericiByTipoMovimentoGestResponse res = leggiClassificatoriGenericiByTipoMovimentoGestService.executeService(req);
		assertNotNull(res);
	}
	
}


/*
 * LeggiClassificatoriGenericiByTipoMovimentoGestService
 */
