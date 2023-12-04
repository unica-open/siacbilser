/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.componenteimpcap;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.componenteimpcap.AggiornaComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.AggiornaImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.AnnullaComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.InserisceComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.RicercaComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

public class ComponenteImportiCapitoloTest extends BaseJunit4TestCase {
	
	@Autowired private InserisceComponenteImportiCapitoloService inserisceComponenteImportiCapitoloService;
	@Autowired private RicercaComponenteImportiCapitoloService ricercaComponenteImportiCapitoloService;
	@Autowired private AnnullaComponenteImportiCapitoloService annullaComponenteImportiCapitoloService;
	@Autowired private AggiornaComponenteImportiCapitoloService aggiornaComponenteImportiCapitoloService;
	@Autowired private AggiornaImportiCapitoloService aggiornaImportiCapitoloService;
	
	@Test
	public void ricercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo req = new RicercaComponenteImportiCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setCapitolo(create(CapitoloUscitaGestione.class, 180589));
		
		RicercaComponenteImportiCapitoloResponse res = ricercaComponenteImportiCapitoloService.executeService(req);
		assertNotNull(res);
	}
	@Test
	public void inserisceComponenteImportiCapitolo() {
		int uidTipoComponenteImportiCapitolo = 79;
		
		InserisceComponenteImportiCapitolo req = new InserisceComponenteImportiCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2019));
		req.setCapitolo(create(CapitoloUscitaGestione.class, 201900));
		
		creaComponente(req, uidTipoComponenteImportiCapitolo, 2019, new BigDecimal("2.33"));
		creaComponente(req, uidTipoComponenteImportiCapitolo, 2020, new BigDecimal("7"));
		creaComponente(req, uidTipoComponenteImportiCapitolo, 2021, new BigDecimal("5.8"));
		
		InserisceComponenteImportiCapitoloResponse res = inserisceComponenteImportiCapitoloService.executeService(req);
		assertNotNull(res);
	}
	private void creaComponente(InserisceComponenteImportiCapitolo req, int tipo, int anno, BigDecimal importo) {
		ComponenteImportiCapitolo cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(req.getCapitolo().getTipoCapitolo().newImportiCapitoloInstance());
		cic.getImportiCapitolo().setAnnoCompetenza(Integer.valueOf(anno));
		cic.setTipoComponenteImportiCapitolo(create(TipoComponenteImportiCapitolo.class, tipo));
		
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		dcic.setImporto(new BigDecimal("250.01"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(importo);
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);
	}
	
	@Test
	public void annullaComponenteImportiCapitolo() {
		AnnullaComponenteImportiCapitolo req = new AnnullaComponenteImportiCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setCapitolo(create(CapitoloUscitaGestione.class, 201854));
		req.setTipoComponenteImportiCapitolo(create(TipoComponenteImportiCapitolo.class, 15));
		
		AnnullaComponenteImportiCapitoloResponse res = annullaComponenteImportiCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaComponenteImportiCapitolo() {
		AggiornaComponenteImportiCapitolo req = new AggiornaComponenteImportiCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setCapitolo(create(CapitoloUscitaPrevisione.class, 201854));
		
		// 2020
		ComponenteImportiCapitolo cic = create(ComponenteImportiCapitolo.class, 15);
		
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		dcic.setImporto(new BigDecimal("250.01"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(new BigDecimal("0.01"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);
		
		// 2021
		cic = create(ComponenteImportiCapitolo.class, 16);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		dcic.setImporto(new BigDecimal("250.01"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(new BigDecimal("1250.02"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);

		// 2022
		cic = create(ComponenteImportiCapitolo.class, 17);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		dcic.setImporto(new BigDecimal("250.01"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(new BigDecimal("5.8"));
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);
		
		AggiornaComponenteImportiCapitoloResponse res = aggiornaComponenteImportiCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaImportiCapitolo() {
		AggiornaImportiCapitolo req = new AggiornaImportiCapitolo();
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		CapitoloUscitaPrevisione cup = create(CapitoloUscitaPrevisione.class, 201854);
		req.setCapitolo(cup);
		
		ImportiCapitoloUP ic = new ImportiCapitoloUP();
		ic.setAnnoCompetenza(Integer.valueOf(2020));
		ic.setStanziamentoResiduo(new BigDecimal("1.50"));
		ic.setStanziamentoCassa(new BigDecimal("16.16"));
		cup.getListaImportiCapitolo().add(ic);
		
		ic = new ImportiCapitoloUP();
		ic.setAnnoCompetenza(Integer.valueOf(2021));
		ic.setStanziamentoResiduo(new BigDecimal("7.50"));
		ic.setStanziamentoCassa(new BigDecimal("31.50"));
		cup.getListaImportiCapitolo().add(ic);
		
		ic = new ImportiCapitoloUP();
		ic.setAnnoCompetenza(Integer.valueOf(2022));
		ic.setStanziamentoResiduo(new BigDecimal("150.00"));
		ic.setStanziamentoCassa(new BigDecimal("171.60"));
		cup.getListaImportiCapitolo().add(ic);
		
		AggiornaImportiCapitoloResponse res = aggiornaImportiCapitoloService.executeService(req);
		assertNotNull(res);
		
	}
	
	@Test
	public void findImpegniAssociati() {
		Integer uidCapitolo = 159720;
		
		RicercaComponenteImportiCapitolo requestRicerca = new RicercaComponenteImportiCapitolo();
		requestRicerca.setDataOra(new Date());
		requestRicerca.setAnnoBilancio(Integer.valueOf(2020));
		requestRicerca.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		requestRicerca.setCapitolo(create(CapitoloUscitaPrevisione.class, uidCapitolo));
		
		RicercaComponenteImportiCapitoloResponse response = ricercaComponenteImportiCapitoloService.executeService(requestRicerca);
		
		assertNotNull(response);
		assertTrue(response.getErrori().isEmpty());
		assertNotNull(response.getListaImportiCapitolo());
		assertTrue(!response.getListaImportiCapitolo().isEmpty());
		
	}
	
}
