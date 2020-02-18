/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.test.business.ordinativo;

import java.math.BigInteger;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfinser.business.service.classificatorefin.LeggiClassificatoriGenericiByTipoOrdinativoGestService;
import it.csi.siac.siacfinser.business.service.ordinativo.AggiornaOrdinativoPagamentoService;
import it.csi.siac.siacfinser.business.service.ordinativo.AnnullaOrdinativoPagamentoService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoPagamentoPerChiaveService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoPagamentoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGestResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;

public class OrdinativoPagamentoTest extends BaseJunit4TestCase {
	
	@Autowired
	private RicercaOrdinativoPagamentoService ricercaOrdinativoPagamentoService;
	
	@Autowired
	private RicercaOrdinativoPagamentoPerChiaveService ricercaOrdinativoPagamentoPerChiaveService;
	
	@Autowired
	private AggiornaOrdinativoPagamentoService aggiornaOrdinativoPagamentoService;
	
	@Autowired 
	private LeggiClassificatoriGenericiByTipoOrdinativoGestService leggiClassificatoriGenericiByTipoOrdinativoGestService;
	@Autowired
	private AnnullaOrdinativoPagamentoService annullaOrdinativoPagamentoService;
	
	
	@Test
	public void ricercaOrdinativoPagamento() {
		RicercaOrdinativo req = new RicercaOrdinativo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setBilancio(getBilancioTest(143, 2017));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(10);
		
		ParametroRicercaOrdinativoPagamento prop = new ParametroRicercaOrdinativoPagamento();
		req.setParametroRicercaOrdinativoPagamento(prop);
		
		prop.setAnnoEsercizio(Integer.valueOf(2017));
		prop.setAnnoLiquidazione(Integer.valueOf(2017));
		prop.setNumeroLiquidazione(BigInteger.valueOf(712L));
		
		RicercaOrdinativoResponse res = ricercaOrdinativoPagamentoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void testLeggiClassificatoriGenericiByTipoOrdinativoGest() {
		LeggiClassificatoriGenericiByTipoOrdinativoGest req = new LeggiClassificatoriGenericiByTipoOrdinativoGest();
		req.setDataOra(new Date());
		req.setAnnoBilancio(2017);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setIdEnteProprietario(req.getRichiedente().getAccount().getEnte().getUid());
//		req.setCodiceTipoOrdinativoGestione("P");
		req.setCodiceTipoOrdinativoGestione("I");
		req.setAnno(req.getAnnoBilancio());
		LeggiClassificatoriGenericiByTipoOrdinativoGestResponse res = leggiClassificatoriGenericiByTipoOrdinativoGestService.executeService(req);
		
	}
	
	@Test
	public void aggiornaOrdinativoPagamento() {
		RicercaOrdinativoPagamentoPerChiave req = new RicercaOrdinativoPagamentoPerChiave();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(2019);
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		RicercaOrdinativoPagamentoK pRicercaOrdinativoPagamentoK = new RicercaOrdinativoPagamentoK();
		pRicercaOrdinativoPagamentoK.setBilancio(getBilancioTest(131,2019));
		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		ordinativoPagamento.setAnno(Integer.valueOf(2019));
		ordinativoPagamento.setNumero(Integer.valueOf(147));
		ordinativoPagamento.setAnnoBilancio(Integer.valueOf(2019));
		pRicercaOrdinativoPagamentoK.setOrdinativoPagamento(ordinativoPagamento);
		req.setpRicercaOrdinativoPagamentoK(pRicercaOrdinativoPagamentoK);
		
		RicercaOrdinativoPagamentoPerChiaveResponse res = ricercaOrdinativoPagamentoPerChiaveService.executeService(req);
		
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		//System.out.println(res.getOrdinativoPagamento().getClassificatoreStipendi() != null? res.getOrdinativoPagamento().getClassificatoreStipendi().getUid() : "null");
		AggiornaOrdinativoPagamento reqA = new AggiornaOrdinativoPagamento();
		reqA.setDataOra(new Date());
		reqA.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqA.setAnnoBilancio(2019);
		reqA.setEnte(req.getRichiedente().getAccount().getEnte());
		reqA.setBilancio(req.getpRicercaOrdinativoPagamentoK().getBilancio());
		reqA.setOrdinativoPagamento(res.getOrdinativoPagamento());
		reqA.getOrdinativoPagamento().setClassificatoreStipendi(create(ClassificatoreStipendi.class, 75644594));
//		reqA.getOrdinativoPagamento().setClassificatoreStipendi(null);
		
//		AggiornaOrdinativoPagamentoResponse resA = aggiornaOrdinativoPagamentoService.executeService(reqA);
		
	}
	
	@Test
	public void testRicercaOrdinativoPagamentoPerChiaveService() {
		RicercaOrdinativoPagamentoPerChiave req = new RicercaOrdinativoPagamentoPerChiave();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(2019);
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		RicercaOrdinativoPagamentoK pRicercaOrdinativoPagamentoK = new RicercaOrdinativoPagamentoK();
		pRicercaOrdinativoPagamentoK.setBilancio(getBilancioByProperties("consip", "regp", "2019"));
		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		ordinativoPagamento.setAnno(Integer.valueOf(2019));
		ordinativoPagamento.setNumero(Integer.valueOf(147));
		ordinativoPagamento.setAnnoBilancio(Integer.valueOf(2019));
		pRicercaOrdinativoPagamentoK.setOrdinativoPagamento(ordinativoPagamento);
		req.setpRicercaOrdinativoPagamentoK(pRicercaOrdinativoPagamentoK);
		
		RicercaOrdinativoPagamentoPerChiaveResponse res = ricercaOrdinativoPagamentoPerChiaveService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void annullaOrdinativo() {
		AnnullaOrdinativoPagamento req = new AnnullaOrdinativoPagamento();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(2017);
		req.setBilancio(getBilancioTest(131,2017));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setOrdinativoPagamentoDaAnnullare(create(OrdinativoPagamento.class, 20045));
		req.getOrdinativoPagamentoDaAnnullare().setAnno(Integer.valueOf(2017));
		req.getOrdinativoPagamentoDaAnnullare().setNumero(Integer.valueOf(13509));
		
		AnnullaOrdinativoPagamentoResponse res = annullaOrdinativoPagamentoService.executeService(req);
		assertNotNull(res);
	}

}
