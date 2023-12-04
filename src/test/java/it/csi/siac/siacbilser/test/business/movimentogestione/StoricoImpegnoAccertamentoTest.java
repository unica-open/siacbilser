/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.movimentogestione;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfinser.business.service.movgest.AggiornaStoricoImpegnoAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.EliminaStoricoImpegnoAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.InserisceStoricoImpegnoAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaStoricoImpegnoAccertamentoPerChiaveService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaStoricoImpegnoAccertamentoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dad.StoricoImpegnoAccertamentoDad;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsStoricoImpAccRepository;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStoricoImpAccFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaStoricoImpegnoAccertamento;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaVTTest.
 */
public class StoricoImpegnoAccertamentoTest extends BaseJunit4TestCase {
	
	@Autowired private InserisceStoricoImpegnoAccertamentoService inserisceStoricoImpegnoAccertamentoService;
	/**
	 * Inserisci gruppo attivita.*/
	@Test
	public void testInserisci() {
		InserisceStoricoImpegnoAccertamento req = new InserisceStoricoImpegnoAccertamento();
		req.setDataOra(new Date());
		req.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		StoricoImpegnoAccertamento storico = new StoricoImpegnoAccertamento();
		storico.setEnte(req.getEnte());
		
		Impegno imp = new Impegno();
		imp.setAnnoMovimento(2017);
		imp.setNumeroBigDecimal(new BigDecimal("2490"));
		storico.setImpegno(imp);
		
		SubImpegno subi = new SubImpegno();
		subi.setNumeroBigDecimal(new BigDecimal("1"));
		storico.setSubImpegno(subi);
		
		Accertamento acc = new Accertamento();
		acc.setAnnoMovimento(1492);
		acc.setNumeroBigDecimal(new BigDecimal("11"));
		storico.setAccertamento(acc);
		
		SubAccertamento suba = new SubAccertamento();
		suba.setNumeroBigDecimal(new BigDecimal("13"));
//		storico.setSubAccertamento(suba);

		req.setStoricoImpegnoAccertamento(storico);
		
		InserisceStoricoImpegnoAccertamentoResponse res = inserisceStoricoImpegnoAccertamentoService.executeService(req);
	}
	
	@Autowired private RicercaStoricoImpegnoAccertamentoPerChiaveService ricercaDettaglioStoricoImpegnoAccertamento;
	/**
	 * Inserisci gruppo attivita.*/
	@Test
	public void testRicerca() {
		RicercaStoricoImpegnoAccertamentoPerChiave req = new RicercaStoricoImpegnoAccertamentoPerChiave();
		req.setDataOra(new Date());
		req.setStoricoImpegnoAccertamento(create(StoricoImpegnoAccertamento.class,2));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		RicercaStoricoImpegnoAccertamentoPerChiaveResponse res = ricercaDettaglioStoricoImpegnoAccertamento.executeService(req);
	}
	
	@Autowired private EliminaStoricoImpegnoAccertamentoService eliminaDettaglioStoricoImpegnoAccertamento;
	/**
	 * Inserisci gruppo attivita.*/
	@Test
	public void testElimina() {
		EliminaStoricoImpegnoAccertamento req = new EliminaStoricoImpegnoAccertamento();
		req.setDataOra(new Date());
		req.setStoricoImpegnoAccertamento(create(StoricoImpegnoAccertamento.class,21));
		req.setBilancio(getBilancioByProperties("consip", "regp", "2016"));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		EliminaStoricoImpegnoAccertamentoResponse res = eliminaDettaglioStoricoImpegnoAccertamento.executeService(req);
	}
	
	@Autowired private RicercaStoricoImpegnoAccertamentoService ricercaStoricoImpegnoAccertamento;
	/**
	 * Inserisci gruppo attivita.*/
	@Test
	public void testRicercaPaginata() {
		RicercaStoricoImpegnoAccertamento req = new RicercaStoricoImpegnoAccertamento();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		ParametroRicercaStoricoImpegnoAccertamento par = new ParametroRicercaStoricoImpegnoAccertamento();
		par.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		StoricoImpegnoAccertamento storico = new StoricoImpegnoAccertamento();
		storico.setEnte(req.getEnte());
		Impegno imp = new Impegno();
		imp.setAnnoMovimento(2017);
		imp.setNumeroBigDecimal(new BigDecimal("2490"));
//		storico.setImpegno(imp);
		
		SubImpegno subi = new SubImpegno();
		subi.setNumeroBigDecimal(new BigDecimal("1"));
//		storico.setSubImpegno(subi);
		
		Accertamento acc = new Accertamento();
		acc.setAnnoMovimento(2017);
		acc.setNumeroBigDecimal(new BigDecimal("5"));
		storico.setAccertamento(acc);
		
		SubAccertamento suba = new SubAccertamento();
		suba.setNumeroBigDecimal(new BigDecimal("13"));
//		storico.setSubAccertamento(suba);
		
		par.setStoricoImpegnoAccertamento(storico);
		
		req.setParametroRicercaStoricoImpegnoAccertamento(par);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		RicercaStoricoImpegnoAccertamentoResponse res = ricercaStoricoImpegnoAccertamento.executeService(req);
		if(res.getElencoStoricoImpegnoAccertamento() != null) {
			for (StoricoImpegnoAccertamento i : res.getElencoStoricoImpegnoAccertamento()) {
				System.out.println("storico su impegno: " + i.getImpegno().getAnnoMovimento() + " / " + i.getImpegno().getNumeroBigDecimal());
			}
			
		}
	}
	
	
	@Autowired private AggiornaStoricoImpegnoAccertamentoService aggiornaStoricoImpegnoAccertamentoService;
	/**
	 * Inserisci gruppo attivita.*/
	@Test
	public void testAggiorna() {
		AggiornaStoricoImpegnoAccertamento req = new AggiornaStoricoImpegnoAccertamento();
		req.setDataOra(new Date());
		req.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		StoricoImpegnoAccertamento storico = new StoricoImpegnoAccertamento();
		storico.setUid(2);
		storico.setEnte(req.getEnte());
		
		Impegno imp = new Impegno();
		imp.setAnnoMovimento(2017);
		imp.setNumeroBigDecimal(new BigDecimal("1"));
		storico.setImpegno(imp);
		
		SubImpegno subi = new SubImpegno();
		subi.setNumeroBigDecimal(new BigDecimal("13"));
//		storico.setSubImpegno(subi);
		
		Accertamento acc = new Accertamento();
		acc.setAnnoMovimento(1974);
		acc.setNumeroBigDecimal(new BigDecimal("5"));
		storico.setAccertamento(acc);
		
		SubAccertamento suba = new SubAccertamento();
		suba.setNumeroBigDecimal(new BigDecimal("13"));
//		storico.setSubAccertamento(suba);

		req.setStoricoImpegnoAccertamento(storico);
		
		AggiornaStoricoImpegnoAccertamentoResponse res = aggiornaStoricoImpegnoAccertamentoService.executeService(req);
	}
	
	@Test
	public void testAggiornaDaRicerca() {
		
		RicercaStoricoImpegnoAccertamentoPerChiave reqRic = new RicercaStoricoImpegnoAccertamentoPerChiave();
		reqRic.setDataOra(new Date());
		reqRic.setStoricoImpegnoAccertamento(create(StoricoImpegnoAccertamento.class,21));
		reqRic.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		RicercaStoricoImpegnoAccertamentoPerChiaveResponse resRic = ricercaDettaglioStoricoImpegnoAccertamento.executeService(reqRic);
		
		AggiornaStoricoImpegnoAccertamento req = new AggiornaStoricoImpegnoAccertamento();
		req.setDataOra(new Date());
		req.setBilancio(getBilancioByProperties("consip", "regp", "2016"));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		StoricoImpegnoAccertamento storico = resRic.getStoricoImpegnoAccertamento();
		
		Accertamento acc = new Accertamento();
		acc.setAnnoMovimento(1974);
		acc.setNumeroBigDecimal(new BigDecimal("5"));
		storico.setAccertamento(acc);
		
		SubAccertamento suba = new SubAccertamento();
		suba.setNumeroBigDecimal(new BigDecimal("13"));
//		storico.setSubAccertamento(suba);

		req.setStoricoImpegnoAccertamento(storico);
		
		AggiornaStoricoImpegnoAccertamentoResponse res = aggiornaStoricoImpegnoAccertamentoService.executeService(req);
	}
	
	@Autowired StoricoImpegnoAccertamentoDad siad;
	
	@Test
	public void testDad() {
		StoricoImpegnoAccertamento ricerca = siad.ricercaRecordCorrispondenteInAnnoBilancio(create(StoricoImpegnoAccertamento.class,21), "2017");
		log.logXmlTypeObject(ricerca, "rrrrrrrrrrrrr");
	}
	
	@Autowired
	private SiacRMovgestTsStoricoImpAccRepository siacRMovgestTsStoricoImpAccRepository;
	
	@Test
	public void testRepository() {
//		List<SiacRMovgestTsStoricoImpAccFin> storicos = siacRMovgestTsStoricoImpAccRepository.findAllSiacRMovgestTsStoricoImpAccFinByMovgestId(64800, Arrays.asList("T", "S"));
//		List<SiacRMovgestTsStoricoImpAccFin> siacRMovgestTsStoricoImpAcc = siacRMovgestTsStoricoImpAccRepository.findSiacRMovgestTsStoricoImpAccFinByMovgestTsIdImpAndMovgestTsIdAndBilId(67377, 67376, 48);
		List<SiacRMovgestTsStoricoImpAccFin> siacRMovgestTsStoricoImpAcc = siacRMovgestTsStoricoImpAccRepository.findSiacRMovgestTsStoricoImpAccFinInBilSuccessivoByMovgestTsIdImpAndMovgestTsIdAccBilPrecedente(67384, 67376, 131);
		System.out.println("trovati : " + siacRMovgestTsStoricoImpAcc.size() + " record");
	}
}