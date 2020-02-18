/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.rateirisconti;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.rateirisconti.AggiornaRateoService;
import it.csi.siac.siacbilser.business.service.rateirisconti.AggiornaRiscontoService;
import it.csi.siac.siacbilser.business.service.rateirisconti.InserisciRateoService;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRateoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRiscontoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateoResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.Risconto;

/**
 * The Class RateiRiscontiTest.
 */
public class RateiRiscontiTest extends BaseJunit4TestCase {
	
	
	@Autowired private PrimaNotaDad primaNotaDad;
	
	/**
	 * Inserisci rateo
	 */
	@Test
	public void inserisciRateo() {
			
		InserisciRateo req = new InserisciRateo();
		
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		
		Rateo rateo = new Rateo();
		req.setRateo(rateo);
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setUid(592 /*397*/);
		//220 no Imp o Acc test
//59

		rateo.setPrimaNota(primaNota);
		
		rateo.setImporto(new BigDecimal(1.00));
		rateo.setAnno(2014);
		
		InserisciRateoResponse res = se.executeService(InserisciRateoService.class, req);
		
		assertNotNull(res);
	}
	
	/**
	 * Aggiorna rateo
	 */
	@Test
	public void aggiornaRateo() {
			
		AggiornaRateo req = new AggiornaRateo();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		
		Rateo rateo = new Rateo();
		rateo.setUid(17);
		req.setRateo(rateo);
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setUid(59 /*397*/);
		//220 no Imp o Acc test
//59

		rateo.setPrimaNota(primaNota);
		
		rateo.setImporto(new BigDecimal(12.1));
		rateo.setAnno(2015);
		
		AggiornaRateoResponse res = se.executeService(AggiornaRateoService.class, req);
		
		assertNotNull(res);
	}
	
	@Test
	public void ottieniPrimaNotaRateo() {
		Risconto rs = new Risconto();
		rs.setUid(14);
		Integer ottieniIdPrimaNotaRiscontoByRisconto = primaNotaDad.ottieniIdPrimaNotaRiscontoByRisconto(rs);
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		System.out.println("uid: " +(ottieniIdPrimaNotaRiscontoByRisconto!= null? ottieniIdPrimaNotaRiscontoByRisconto.toString() : "null"));
	}
	
	@Test
	public void aggiornaRisconto() {
			
		AggiornaRisconto req = new AggiornaRisconto();
		
		req.setRichiedente(getRichiedenteByProperties("dev", "sacriMonti"));
		
		
		Risconto risconto = new Risconto();
		risconto.setUid(3);
		risconto.setAnno(2018);
		risconto.setImporto(new BigDecimal(2));
		req.setRisconto(risconto);
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setUid(151475);
	

		risconto.setPrimaNota(primaNota);
				
		AggiornaRiscontoResponse res = se.executeService(AggiornaRiscontoService.class, req);
		
		assertNotNull(res);
	}
	
		
}