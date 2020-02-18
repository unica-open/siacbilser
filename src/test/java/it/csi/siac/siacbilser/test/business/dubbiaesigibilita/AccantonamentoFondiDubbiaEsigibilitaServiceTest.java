/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.dubbiaesigibilita;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.AggiornaFondoDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.ControllaFondiDubbiaEsigibilitaAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.InserisceFondiDubbiaEsigibilitaRendicontoService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.InserisceFondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteService;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.RicercaSinteticaFondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class VerificaEliminabilitaCapitoloEntrataPrevisioneServiceTest.
 */

public class AccantonamentoFondiDubbiaEsigibilitaServiceTest extends BaseJunit4TestCase {

	/** The fondiDubbiaEsigibilitaService service. */
	@Autowired
	private InserisceFondiDubbiaEsigibilitaService inserisceFondiDubbiaEsigibilitaService;
	/** The fondiDubbiaEsigibilitaService service. */
	@Autowired
	private AggiornaFondoDubbiaEsigibilitaService aggiornaFondoDubbiaEsigibilitaService;
	@Autowired
	private RicercaSinteticaFondiDubbiaEsigibilitaService ricercaSinteticaFondiDubbiaEsigibilitaService;
	@Autowired
	private InserisceFondiDubbiaEsigibilitaRendicontoService inserisceFondiDubbiaEsigibilitaRendicontoService;
	@Autowired
	private ControllaFondiDubbiaEsigibilitaAnnoPrecedenteService controllaFondiDubbiaEsigibilitaAnnoPrecedenteService;
	@Autowired
	private PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService popolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService;
	@Autowired
	private PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteService popolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteService;
	@Autowired
	private PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteService popolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteService;
	
	/**
	 * Verifica eliminabilita capitolo entrata previsione.
	 *
	 * @return the verifica eliminabilita capitolo entrata previsione response
	 */
	@Test
	public void inserisciFondiAccantonamentoDubbiaEsigibilita() {
		
		InserisceFondiDubbiaEsigibilita request = new InserisceFondiDubbiaEsigibilita();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		List<AccantonamentoFondiDubbiaEsigibilita> lista = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
		AccantonamentoFondiDubbiaEsigibilita accantonamento = new AccantonamentoFondiDubbiaEsigibilita();
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		capitoloEntrataPrevisione.setUid(11778);
		accantonamento.setCapitolo(capitoloEntrataPrevisione);
		accantonamento.setPercentualeAccantonamentoFondi(BigDecimal.TEN);
		accantonamento.setPercentualeAccantonamentoFondi1(BigDecimal.TEN);
		accantonamento.setPercentualeAccantonamentoFondi2(BigDecimal.TEN);
		accantonamento.setPercentualeAccantonamentoFondi3(BigDecimal.TEN);
		accantonamento.setPercentualeAccantonamentoFondi4(BigDecimal.TEN);
		accantonamento.setPercentualeMediaAccantonamento(BigDecimal.TEN);
		accantonamento.setPercentualeDelta(BigDecimal.TEN);
		lista.add(accantonamento);
		request.setAccantonamentiFondiDubbiaEsigibilita(lista);
	
		
		InserisceFondiDubbiaEsigibilitaResponse response = inserisceFondiDubbiaEsigibilitaService.executeService(request);
		assertNotNull(response);
		ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Test
	public void aggiornaFondiAccantonamentoDubbiaEsigibilita() {
		
		AggiornaFondoDubbiaEsigibilita request = new AggiornaFondoDubbiaEsigibilita();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AccantonamentoFondiDubbiaEsigibilita accantonamento = new AccantonamentoFondiDubbiaEsigibilita();
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		capitoloEntrataPrevisione.setUid(11778);
		accantonamento.setUid(60);
		accantonamento.setCapitolo(capitoloEntrataPrevisione);
		accantonamento.setPercentualeAccantonamentoFondi(BigDecimal.ONE);
		accantonamento.setPercentualeAccantonamentoFondi1(BigDecimal.ONE);
		accantonamento.setPercentualeAccantonamentoFondi2(BigDecimal.ONE);
		accantonamento.setPercentualeAccantonamentoFondi3(BigDecimal.ONE);
		accantonamento.setPercentualeAccantonamentoFondi4(BigDecimal.ONE);
		accantonamento.setPercentualeMediaAccantonamento(BigDecimal.ONE);
		accantonamento.setPercentualeDelta(BigDecimal.ONE);
		request.setAccantonamentoFondiDubbiaEsigibilita(accantonamento);
		
		AggiornaFondoDubbiaEsigibilitaResponse response = aggiornaFondoDubbiaEsigibilitaService.executeService(request);
		assertNotNull(response);
		//ToStringBuilder.reflectionToString(response, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	@Test
	public void ricercaSinteticaFondiAccantonamentoDubbiaEsigibilita() {
		
		RicercaSinteticaFondiDubbiaEsigibilita request = new RicercaSinteticaFondiDubbiaEsigibilita();
		
		request.setAccantonamentoFondiDubbiaEsigibilitaModelDetails(AccantonamentoFondiDubbiaEsigibilitaModelDetail.CapitoloEntrataPrevisione);
		request.setBilancio(getBilancio2015Test());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione(0);
		parametriPaginazione.setElementiPerPagina(10);
		request.setParametriPaginazione(parametriPaginazione);
		
		RicercaSinteticaFondiDubbiaEsigibilitaResponse response = ricercaSinteticaFondiDubbiaEsigibilitaService.executeService(request);
		assertNotNull(response);
	}
	
	@Test
	public void inserisceFondiDubbiaEsigibilitaRendiconto() {
		InserisceFondiDubbiaEsigibilitaRendiconto req = new InserisceFondiDubbiaEsigibilitaRendiconto();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		AccantonamentoFondiDubbiaEsigibilitaRendiconto afder = new AccantonamentoFondiDubbiaEsigibilitaRendiconto();
		afder.setPercentualeAccantonamentoFondi(new BigDecimal("12"));
		afder.setPercentualeAccantonamentoFondi1(new BigDecimal("7"));
		afder.setPercentualeAccantonamentoFondi2(new BigDecimal("5"));
		afder.setPercentualeAccantonamentoFondi3(new BigDecimal("30"));
		afder.setPercentualeAccantonamentoFondi4(new BigDecimal("25"));
		afder.setPercentualeMediaAccantonamento(new BigDecimal("15.8"));
		
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		capitolo.setUid(31045);
		afder.setCapitolo(capitolo);
		
		req.getAccantonamentiFondiDubbiaEsigibilitaRendiconto().add(afder);
		
		InserisceFondiDubbiaEsigibilitaRendicontoResponse res = inserisceFondiDubbiaEsigibilitaRendicontoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void controllaFondiDubbiaEsigibilitaAnnoPrecedente() {
		ControllaFondiDubbiaEsigibilitaAnnoPrecedente req = new ControllaFondiDubbiaEsigibilitaAnnoPrecedente();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		req.setBilancio(getBilancio2014Test());
		
		ControllaFondiDubbiaEsigibilitaAnnoPrecedenteResponse res = controllaFondiDubbiaEsigibilitaAnnoPrecedenteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void popolaFondiDubbiaEsigibilitaDaAnnoPrecedente() {
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente req = new PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(132, 2018));
		
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse res = popolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void popolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente() {
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente req = new PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancio2015Test());
		
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse res = popolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void popolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente() {
		PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente req = new PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setBilancio(getBilancioTest(143, 2017));
		
		PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse res = popolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteService.executeService(req);
		assertNotNull(res);
	}
}
