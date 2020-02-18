/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.conciliazione;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.conciliazione.AggiornaConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.EliminaConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazionePerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazionePerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazioniPerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaContiConciliazionePerClasseService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaDettaglioConciliazionePerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaDettaglioConciliazionePerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaDettaglioConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaSinteticaConciliazionePerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaSinteticaConciliazionePerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaSinteticaConciliazionePerTitoloService;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasseResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;
import it.csi.siac.siacgenser.model.Conto;

public class ConciliazioneTest extends BaseJunit4TestCase {
	
	@Autowired
	private InserisceConciliazionePerTitoloService inserisceConciliazionePerTitoloService;
	@Autowired
	private AggiornaConciliazionePerTitoloService aggiornaConciliazionePerTitoloService;
	@Autowired
	private EliminaConciliazionePerTitoloService eliminaConciliazionePerTitoloService;
	@Autowired
	private RicercaDettaglioConciliazionePerTitoloService ricercaDettaglioConciliazionePerTitoloService;
	@Autowired
	private RicercaSinteticaConciliazionePerTitoloService ricercaSinteticaConciliazionePerTitoloService;
	
	@Autowired
	private InserisceConciliazionePerCapitoloService inserisceConciliazionePerCapitoloService;
	@Autowired
	private InserisceConciliazioniPerCapitoloService inserisceConciliazioniPerCapitoloService;
	@Autowired
	private RicercaDettaglioConciliazionePerCapitoloService ricercaDettaglioConciliazionePerCapitoloService;
	@Autowired
	private RicercaSinteticaConciliazionePerCapitoloService ricercaSinteticaConciliazionePerCapitoloService;
	
	@Autowired
	private InserisceConciliazionePerBeneficiarioService inserisceConciliazionePerBeneficiarioService;
	@Autowired
	private RicercaDettaglioConciliazionePerBeneficiarioService ricercaDettaglioConciliazionePerBeneficiarioService;
	@Autowired
	private RicercaSinteticaConciliazionePerBeneficiarioService ricercaSinteticaConciliazionePerBeneficiarioService;
	
	@Autowired
	private RicercaContiConciliazionePerClasseService ricercaContiConciliazionePerClasseService;
	
	@Autowired
	CapitoloDad capitoloDad;
	
	@Test
	public void inserisceConciliazionePerTitolo() {
		InserisceConciliazionePerTitolo req = new InserisceConciliazionePerTitolo();
		
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerTitolo conciliazionePerTitolo = new ConciliazionePerTitolo();
		conciliazionePerTitolo.setEnte(getEnteTest());
		conciliazionePerTitolo.setClasseDiConciliazione(ClasseDiConciliazione.RICAVI);
		
		Conto conto = new Conto();
		conto.setUid(137);
		conciliazionePerTitolo.setConto(conto);
		
//		Macroaggregato classificatoreGerarchico = new Macroaggregato();
//		classificatoreGerarchico.setUid(118917);
		CategoriaTipologiaTitolo classificatoreGerarchico = new CategoriaTipologiaTitolo();
		classificatoreGerarchico.setUid(119436);
		
		conciliazionePerTitolo.setClassificatoreGerarchico(classificatoreGerarchico);
		
		req.setConciliazionePerTitolo(conciliazionePerTitolo);
		
		InserisceConciliazionePerTitoloResponse res = inserisceConciliazionePerTitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaConciliazionePerTitolo() {
		AggiornaConciliazionePerTitolo req = new AggiornaConciliazionePerTitolo();
		
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerTitolo conciliazionePerTitolo = new ConciliazionePerTitolo();
		conciliazionePerTitolo.setUid(2);
		conciliazionePerTitolo.setEnte(getEnteTest());
		conciliazionePerTitolo.setClasseDiConciliazione(ClasseDiConciliazione.RICAVI);
		
		Conto conto = new Conto();
		conto.setUid(137);
		conciliazionePerTitolo.setConto(conto);
		
		Macroaggregato classificatoreGerarchico = new Macroaggregato();
		classificatoreGerarchico.setUid(455536);
		conciliazionePerTitolo.setClassificatoreGerarchico(classificatoreGerarchico);
		
		req.setConciliazionePerTitolo(conciliazionePerTitolo);
		
		AggiornaConciliazionePerTitoloResponse res = aggiornaConciliazionePerTitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void eliminaConciliazionePerTitolo() {
		EliminaConciliazionePerTitolo req = new EliminaConciliazionePerTitolo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerTitolo conciliazionePerTitolo = new ConciliazionePerTitolo();
		conciliazionePerTitolo.setUid(2);
		req.setConciliazionePerTitolo(conciliazionePerTitolo);
		
		EliminaConciliazionePerTitoloResponse res = eliminaConciliazionePerTitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioConciliazionePerTitolo() {
		RicercaDettaglioConciliazionePerTitolo req = new RicercaDettaglioConciliazionePerTitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerTitolo conciliazionePerTitolo = new ConciliazionePerTitolo();
		conciliazionePerTitolo.setUid(4);
		req.setConciliazionePerTitolo(conciliazionePerTitolo);
		
		RicercaDettaglioConciliazionePerTitoloResponse res = ricercaDettaglioConciliazionePerTitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaConciliazionePerTitolo() {
		RicercaSinteticaConciliazionePerTitolo req = new RicercaSinteticaConciliazionePerTitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		ConciliazionePerTitolo conciliazionePerTitolo = new ConciliazionePerTitolo();
		conciliazionePerTitolo.setClasseDiConciliazione(ClasseDiConciliazione.COSTI);
		
		ClassificatoreGerarchico classificatoreGerarchico = new ClassificatoreGerarchico();
		classificatoreGerarchico.setUid(118917);
		conciliazionePerTitolo.setClassificatoreGerarchico(classificatoreGerarchico);
		ClassificatoreGerarchico titolo = new ClassificatoreGerarchico();
		titolo.setUid(118916);
		
//		ClassificatoreGerarchico classificatoreGerarchico = new ClassificatoreGerarchico();
//		classificatoreGerarchico.setUid(119436);
//		ClassificatoreGerarchico titolo = new ClassificatoreGerarchico();
//		titolo.setUid(119434);
//		ClassificatoreGerarchico tipologia = new ClassificatoreGerarchico();
//		tipologia.setUid(119435);
		
//		conciliazionePerTitolo.setClassificatoreGerarchico(classificatoreGerarchico);
//		req.setTipologia(tipologia);
		req.setTitolo(titolo);
		
		req.setConciliazionePerTitolo(conciliazionePerTitolo);
		
		RicercaSinteticaConciliazionePerTitoloResponse res = ricercaSinteticaConciliazionePerTitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisceConciliazionePerCapitolo() {
		InserisceConciliazionePerCapitolo req = new InserisceConciliazionePerCapitolo();
		
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerCapitolo conciliazionePerCapitolo = new ConciliazionePerCapitolo();
		conciliazionePerCapitolo.setEnte(getEnteTest());
		conciliazionePerCapitolo.setClasseDiConciliazione(ClasseDiConciliazione.COSTI);
		
		Conto conto = new Conto();
		conto.setUid(137);
		conciliazionePerCapitolo.setConto(conto);
		
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		cug.setUid(18447);
		conciliazionePerCapitolo.setCapitolo(cug);
		
		req.setConciliazionePerCapitolo(conciliazionePerCapitolo);
		
		InserisceConciliazionePerCapitoloResponse res = inserisceConciliazionePerCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisceConciliazioniPerCapitolo() {
		InserisceConciliazioniPerCapitolo req = new InserisceConciliazioniPerCapitolo();
		
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Conto conto = new Conto();
		conto.setUid(137);
		
		// 1
		ConciliazionePerCapitolo conciliazionePerCapitolo1 = new ConciliazionePerCapitolo();
		conciliazionePerCapitolo1.setEnte(getEnteTest());
		conciliazionePerCapitolo1.setClasseDiConciliazione(ClasseDiConciliazione.RICAVI);
		conciliazionePerCapitolo1.setConto(conto);
		
		CapitoloEntrataGestione ceg = new CapitoloEntrataGestione();
		ceg.setUid(18446);
		conciliazionePerCapitolo1.setCapitolo(ceg);
		
		req.getConciliazioniPerCapitolo().add(conciliazionePerCapitolo1);
		
		// 2
		ConciliazionePerCapitolo conciliazionePerCapitolo2 = new ConciliazionePerCapitolo();
		conciliazionePerCapitolo2.setEnte(getEnteTest());
		conciliazionePerCapitolo2.setClasseDiConciliazione(ClasseDiConciliazione.RICAVI);
		conciliazionePerCapitolo2.setConto(conto);
		
		CapitoloEntrataGestione ceg2 = new CapitoloEntrataGestione();
		ceg2.setUid(30968);
		conciliazionePerCapitolo2.setCapitolo(ceg2);
		
		req.getConciliazioniPerCapitolo().add(conciliazionePerCapitolo2);
		
		InserisceConciliazioniPerCapitoloResponse res = inserisceConciliazioniPerCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioConciliazionePerCapitolo() {
		RicercaDettaglioConciliazionePerCapitolo req = new RicercaDettaglioConciliazionePerCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerCapitolo conciliazionePerCapitolo = new ConciliazionePerCapitolo();
		conciliazionePerCapitolo.setUid(3);
		req.setConciliazionePerCapitolo(conciliazionePerCapitolo);
		
		RicercaDettaglioConciliazionePerCapitoloResponse res = ricercaDettaglioConciliazionePerCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSintetciaConciliazionePerCapitolo() {
		RicercaSinteticaConciliazionePerCapitolo req = new RicercaSinteticaConciliazionePerCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerCapitolo conciliazionePerCapitolo = new ConciliazionePerCapitolo();
		Capitolo<?, ?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(18447);
		conciliazionePerCapitolo.setCapitolo(capitolo);
		req.setConciliazionePerCapitolo(conciliazionePerCapitolo);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaConciliazionePerCapitoloResponse res = ricercaSinteticaConciliazionePerCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisceConciliazionePerBeneficiario() {
		InserisceConciliazionePerBeneficiario req = new InserisceConciliazionePerBeneficiario();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancio2015Test());
		
		ConciliazionePerBeneficiario conciliazionePerBeneficiario = new ConciliazionePerBeneficiario();
		conciliazionePerBeneficiario.setEnte(getEnteTest());
		conciliazionePerBeneficiario.setClasseDiConciliazione(ClasseDiConciliazione.COSTI);
		
		Conto conto = new Conto();
		conto.setUid(137);
		conciliazionePerBeneficiario.setConto(conto);
		
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		cug.setUid(18447);
		conciliazionePerBeneficiario.setCapitolo(cug);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(66);
		conciliazionePerBeneficiario.setSoggetto(soggetto);
		
		req.setConciliazionePerBeneficiario(conciliazionePerBeneficiario);
		
		InserisceConciliazionePerBeneficiarioResponse res = inserisceConciliazionePerBeneficiarioService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioConciliazionePerBeneficiario() {
		RicercaDettaglioConciliazionePerBeneficiario req = new RicercaDettaglioConciliazionePerBeneficiario();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerBeneficiario conciliazionePerBeneficiario = new ConciliazionePerBeneficiario();
		conciliazionePerBeneficiario.setUid(1);
		req.setConciliazionePerBeneficiario(conciliazionePerBeneficiario);
		
		RicercaDettaglioConciliazionePerBeneficiarioResponse res = ricercaDettaglioConciliazionePerBeneficiarioService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaConciliazionePerBeneficiario() {
		RicercaSinteticaConciliazionePerBeneficiario req = new RicercaSinteticaConciliazionePerBeneficiario();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ConciliazionePerBeneficiario conciliazionePerBeneficiario = new ConciliazionePerBeneficiario();
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(1);
		conciliazionePerBeneficiario.setSoggetto(soggetto);
		req.setConciliazionePerBeneficiario(conciliazionePerBeneficiario);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		Capitolo<?, ?> capitolo = new Capitolo<ImportiCapitolo, ImportiCapitolo>();
		capitolo.setTipoCapitolo(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
		capitolo.setAnnoCapitolo(2015);
		capitolo.setNumeroCapitolo(101);
		capitolo.setNumeroArticolo(1);
		capitolo.setNumeroUEB(1);
		
		conciliazionePerBeneficiario.setCapitolo(capitolo);
		
		RicercaSinteticaConciliazionePerBeneficiarioResponse res = ricercaSinteticaConciliazionePerBeneficiarioService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void testRicercaConticonciliazionePerClasse(){
		Capitolo<?,?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(55094);
		Soggetto soggetto = new Soggetto();
		//soggetto.setUid(37584);
		RicercaContiConciliazionePerClasse req = new RicercaContiConciliazionePerClasse();
		req.setSoggetto(soggetto);
		req.setCapitolo(capitolo);
		req.setClasseDiConciliazione(ClasseDiConciliazione.COSTI);
		
		req.setRichiedente(getRichiedenteByProperties("forn2", "regp"));
		
		RicercaContiConciliazionePerClasseResponse res = ricercaContiConciliazionePerClasseService.executeService(req);
	}
	
	@Test
	public void caricaClassificatore() {
		
		Capitolo<?,?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(55116);
		TipoCapitolo tipoCapitolo = capitoloDad.findTipoCapitolo(capitolo.getUid());
		
		TipologiaClassificatore tc;
		if(TipoCapitolo.isTipoCapitoloEntrata(tipoCapitolo)){
			tc = TipologiaClassificatore.CATEGORIA;
		}else{
			tc = TipologiaClassificatore.MACROAGGREGATO;
		}
		
		ClassificatoreGerarchico ricercaClassificatoreGerarchico = capitoloDad.ricercaClassificatoreGerarchico(capitolo.getUid(), tc, null);
		System.out.println("uid: " + ricercaClassificatoreGerarchico.getUid());
	}
}
