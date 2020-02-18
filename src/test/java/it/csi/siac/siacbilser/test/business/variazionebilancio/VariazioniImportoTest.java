/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.variazionebilancio;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.excel.variazionidibilancio.StampaExcelVariazioneDiBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.AggiornaAnagraficaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.AggiornaDettaglioVariazioneImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.DefinisceAnagraficaVariazioneBilancioAsyncService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.DefinisceAnagraficaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.EliminaDettaglioVariazioneImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.GestisciDettaglioVariazioneComponenteImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.InserisceAnagraficaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.InserisceDettaglioResiduoVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.InserisciDettaglioVariazioneImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaDettaglioAnagraficaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaDettaglioVariazioneComponenteImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceDettaglioResiduoVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceDettaglioResiduoVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.ElaborazioniDad;
import it.csi.siac.siacbilser.integration.dad.TipoComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancio;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancioResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.VariabileProcesso;

/**
 * The Class VariazioniImportoDLTest.
 */
public class VariazioniImportoTest extends BaseJunit4TestCase {
	
	@Autowired
	private InserisciDettaglioVariazioneImportoCapitoloService inserisciDettaglioVariazioneImportoCapitoloService;
	@Autowired
	private AggiornaDettaglioVariazioneImportoCapitoloService aggiornaDettaglioVariazioneImportoCapitoloService;
	@Autowired
	private RicercaVariazioneBilancioService ricercaVariazioneBilancioService;
	@Autowired
	private InserisceAnagraficaVariazioneBilancioService inserisceAnagraficaVariazioneBilancioService;
	@Autowired
	private AggiornaAnagraficaVariazioneBilancioService aggiornaAnagraficaVariazioneBilancioService;
	@Autowired
	private RicercaDettagliVariazioneImportoCapitoloNellaVariazioneService ricercaDettagliVariazioneImportoCapitoloNellaVariazioneService;
	@Autowired
	private StampaExcelVariazioneDiBilancioService stampaExcelVariazioneDiBilancioService;
	@Autowired
	private RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneService ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneService;
	@Autowired
	private DefinisceAnagraficaVariazioneBilancioService definisceAnagraficaVariazioneBilancioService;
	@Autowired
	private EliminaDettaglioVariazioneImportoCapitoloService eliminaDettaglioVariazioneImportoCapitoloService;
	@Autowired
	private RicercaDettaglioAnagraficaVariazioneBilancioService ricercaDettaglioAnagraficaVariazioneBilancioService;
	@Autowired
	private GestisciDettaglioVariazioneComponenteImportoCapitoloService gestisciDettaglioVariazioneComponenteImportoCapitoloService;
	@Autowired
	private RicercaDettaglioVariazioneComponenteImportoCapitoloService ricercaDettaglioVariazioneComponenteImportoCapitoloService;
	@Autowired
	private InserisceDettaglioResiduoVariazioneBilancioService inserisceDettaglioResiduoVariazioneBilancioService;
	
	/**
	 * Test ricerca variazione bilancio.
	 */
	@Test
	public void testRicercaVariazioneBilancio() {
		RicercaVariazioneBilancio req = new RicercaVariazioneBilancio();
		
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setParametriPaginazione(getParametriPaginazione(0,100));
		
		//req.setTipiCapitolo(Arrays.asList(TipoCapitolo.CAPITOLO_USCITA_GESTIONE,TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE));
		
		VariazioneImportoCapitolo vic = new VariazioneImportoCapitolo();
		vic.setBilancio(getBilancioTest(16, 2015));
		vic.setEnte(req.getRichiedente().getAccount().getEnte());
		vic.setNumero(56);
		req.setVariazioneImportoCapitolo(vic);
		
		req.setAttoAmministrativo(create(AttoAmministrativo.class, 37649));
		
		RicercaVariazioneBilancioResponse resp = ricercaVariazioneBilancioService.executeService(req);
		
		assertNotNull(resp);
		assertNotNull(resp.getVariazioniDiBilancio());
		
	}
	
	@Test
	public void ricercaDettagliVariazioneImportoCapitoloNellaVariazione() {
		RicercaDettagliVariazioneImportoCapitoloNellaVariazione req = new RicercaDettagliVariazioneImportoCapitoloNellaVariazione();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setUidVariazione(207);
		
		RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse res = ricercaDettagliVariazioneImportoCapitoloNellaVariazioneService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void testRicercaAnagraficaVariazione(){
		RicercaDettaglioAnagraficaVariazioneBilancio request = new RicercaDettaglioAnagraficaVariazioneBilancio();
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setUidVariazione(292);
		
		RicercaDettaglioAnagraficaVariazioneBilancioResponse response = se.executeService(RicercaDettaglioAnagraficaVariazioneBilancioService.class, request);
		assertNotNull(response);
		VariazioneImportoCapitolo varImp = response.getVariazioneImportoCapitolo();
		Date dataInizioValiditaStato = varImp.getDataInizioValiditaStato();
		System.out.println("inizio validita stato: " + dataInizioValiditaStato.toString());
	}
	
	@Test
	public void ricercaVariazioneBilancio() {
		RicercaVariazioneBilancio request = new RicercaVariazioneBilancio();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setParametriPaginazione(getParametriPaginazioneTest());
		VariazioneImportoCapitolo utility = new VariazioneImportoCapitolo();
		
		utility.setBilancio(getBilancioTest());
		utility.setEnte(getEnteTest());
		AttoAmministrativo attoAmministrativoVariazioneDiBilancioDaInjettare = new AttoAmministrativo();
		attoAmministrativoVariazioneDiBilancioDaInjettare.setUid(37649);
		utility.setAttoAmministrativoVariazioneBilancio(attoAmministrativoVariazioneDiBilancioDaInjettare);
		request.setVariazioneImportoCapitolo(utility);
//			request.setUidVariazione(406);
		
		RicercaVariazioneBilancioResponse response = ricercaVariazioneBilancioService.executeService(request);
		assertNotNull(response);
	}
	
	@Test
	public void inserisceAnagraficaVariazioneBilancio() {
		InserisceAnagraficaVariazioneBilancio req = new InserisceAnagraficaVariazioneBilancio();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		
		req.setInvioOrganoAmministrativo(Boolean.FALSE);
		req.setInvioOrganoLegislativo(Boolean.FALSE);
		req.setCaricaResidui(Boolean.FALSE);
		req.setSaltaCheckStanziamentoCassa(false);
		req.setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 0));
		
		req.getVariazioneImportoCapitolo().setApplicazioneVariazione(ApplicazioneVariazione.GESTIONE);
		req.getVariazioneImportoCapitolo().setAttoAmministrativo(create(AttoAmministrativo.class, 34961));
		req.getVariazioneImportoCapitolo().setAttoAmministrativoVariazioneBilancio(create(AttoAmministrativo.class, 34962));
		req.getVariazioneImportoCapitolo().setBilancio(getBilancioByProperties("consip", "regp", 2017));
		req.getVariazioneImportoCapitolo().setData(new Date());
		req.getVariazioneImportoCapitolo().setDescrizione("Test per SIAC-6883. Inserito via JUnit - pronta per residui");
		req.getVariazioneImportoCapitolo().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getVariazioneImportoCapitolo().setNote("");
		// Il numero viene staccato dal servizio
		req.getVariazioneImportoCapitolo().setNumero(null);
		req.getVariazioneImportoCapitolo().setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.BOZZA);
		req.getVariazioneImportoCapitolo().setTipoVariazione(TipoVariazione.VARIAZIONE_IMPORTO);
		
		// Uso quanto gia' caricato
		req.setAnnoBilancio(Integer.valueOf(req.getVariazioneImportoCapitolo().getBilancio().getAnno()));
		
		InserisceAnagraficaVariazioneBilancioResponse res = inserisceAnagraficaVariazioneBilancioService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaAnagraficaVariazioneBilancio() {
		AggiornaAnagraficaVariazioneBilancio req = new AggiornaAnagraficaVariazioneBilancio();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setAnnullaVariazione(Boolean.FALSE);
		req.setEvolviProcesso(Boolean.TRUE);
		req.setIdAttivita("VariazioneDiBilancio--1.0--458--VariazioneDiBilancio_AggiornamentoVariazione--itada3cc46-265d-43b7-a25f-0ae657f0f214--mainActivityInstance--noLoop");
		
		req.setInvioOrganoAmministrativo(Boolean.FALSE);
		req.setInvioOrganoLegislativo(Boolean.FALSE);
		req.setSaltaCheckNecessarioAttoAmministrativoVariazioneDiBilancio(false);
		req.setSaltaCheckStanziamentoCassa(false);
		
		req.setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 207));
		req.getVariazioneImportoCapitolo().setApplicazioneVariazione(ApplicazioneVariazione.PREVISIONE);
		req.getVariazioneImportoCapitolo().setAttoAmministrativo(create(AttoAmministrativo.class, 34961));
		req.getVariazioneImportoCapitolo().setAttoAmministrativoVariazioneBilancio(create(AttoAmministrativo.class, 34962));
		req.getVariazioneImportoCapitolo().setBilancio(getBilancioByProperties("consip", "regp", 2017));
		req.getVariazioneImportoCapitolo().setData(new Date());
		req.getVariazioneImportoCapitolo().setDescrizione("Test per SIAC-6883. Inserito via JUnit e ivi aggiornato");
		req.getVariazioneImportoCapitolo().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getVariazioneImportoCapitolo().setNote("Aggiornamento dei dati");
		req.getVariazioneImportoCapitolo().setNumero(Integer.valueOf(186));
		req.getVariazioneImportoCapitolo().setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.BOZZA);
		req.getVariazioneImportoCapitolo().setTipoVariazione(TipoVariazione.VARIAZIONE_IMPORTO);
		
		Calendar cal = Calendar.getInstance();
		cal.set(2019, Calendar.NOVEMBER, 6, 11, 00, 00);
		cal.set(Calendar.MILLISECOND, 0);
		req.getVariazioneImportoCapitolo().setData(cal.getTime());
		
		AggiornaAnagraficaVariazioneBilancioResponse res = aggiornaAnagraficaVariazioneBilancioService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void definisceAnagraficaVariazioneBilancio() {
		RicercaDettaglioAnagraficaVariazioneBilancio request = new RicercaDettaglioAnagraficaVariazioneBilancio();
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		request.setUidVariazione(207);
		
		RicercaDettaglioAnagraficaVariazioneBilancioResponse response = ricercaDettaglioAnagraficaVariazioneBilancioService.executeService(request);
		assertFalse("Response with errors", response.hasErrori());
		
		DefinisceAnagraficaVariazioneBilancio req = new DefinisceAnagraficaVariazioneBilancio();
		
		req.setDataOra(new Date());
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setIdAttivita("VariazioneDiBilancio--1.0--458--VariazioneDiBilancio_DefinizioneDellaVariazione--it52817ca4-a688-48c0-bf71-cfe433ff3cfa--mainActivityInstance--noLoop");
		req.setSaltaCheckStanziamentoCassa(false);
		req.setVariazioneImportoCapitolo(response.getVariazioneImportoCapitolo());
		
		req.getListaVariabiliProcesso().add(createVariabileProcesso("variazioneDiBilancioDem", "207|186"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("invioGiunta", Boolean.FALSE));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("descrizioneBreve", "186 - Test per SIAC-6883. Inserito via JUnit e ivi aggiornato - Variazione di bilancio"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("siacEnteProprietarioProcesso", "SIAC-ENTE-PROPRIETARIO-2"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("annullaVariazione", Boolean.FALSE));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("quadraturaVariazioneDiBilancio", Boolean.TRUE));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("siacAnnoEsercizioProcesso", "SIAC-ANNO-ESERCIZIO-2017"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("statoVariazioneDiBilancio", "PRE-DEFINITIVA"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("variazioneDiBilancio_execAzioneRichiestaResponse", null));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("annoDiEsercizio", Integer.valueOf(2017)));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("descrizione", "186 - Test per SIAC-6883. Inserito via JUnit e ivi aggiornato - Variazione di bilancio"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("tipoVariazioneBilancio", "Importo"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("invioConsiglio", Boolean.FALSE));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("siacSacProcesso", "strutture"));
		
		req.setVariazioneImportoCapitolo(response.getVariazioneImportoCapitolo());
		DefinisceAnagraficaVariazioneBilancioResponse res = definisceAnagraficaVariazioneBilancioService.executeService(req);
		assertNotNull(res);
	}
	
	@XmlType
	public static class VariabiliProcessoWrapper {
		@XmlElementWrapper(name="variabiliProcesso")
		@XmlElement(name="variabileProcesso")
			public List<VariabileProcesso> variabiliProcesso = new ArrayList<VariabileProcesso>(); 
	}
	
	@Test
	public void inserisciDettaglioVariazioneImportoCapitolo(){
		InserisciDettaglioVariazioneImportoCapitolo req = new InserisciDettaglioVariazioneImportoCapitolo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setSkipLoadCapitolo(false);
		req.setSkipLoadVariazione(false);
		
		req.setDettaglioVariazioneImportoCapitolo(create(DettaglioVariazioneImportoCapitolo.class, 0));
		
		req.getDettaglioVariazioneImportoCapitolo().setCapitolo(create(CapitoloEntrataGestione.class, 87813));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento(new BigDecimal("1.00"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamentoResiduo(new BigDecimal("0.50"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamentoCassa(new BigDecimal("1.50"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento1(new BigDecimal("5.00"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento2(new BigDecimal("25.15"));
		
		req.getDettaglioVariazioneImportoCapitolo().setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 207));
		
		InserisciDettaglioVariazioneImportoCapitoloResponse res = inserisciDettaglioVariazioneImportoCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaDettaglioVariazioneImportoCapitolo() {
		AggiornaDettaglioVariazioneImportoCapitolo req = new AggiornaDettaglioVariazioneImportoCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setSkipLoadCapitolo(false);
		req.setSkipLoadVariazione(false);
		
		req.setDettaglioVariazioneImportoCapitolo(create(DettaglioVariazioneImportoCapitolo.class, 0));
		req.getDettaglioVariazioneImportoCapitolo().setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 207));
		
		req.getDettaglioVariazioneImportoCapitolo().setCapitolo(create(CapitoloEntrataGestione.class, 87813));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento(new BigDecimal("3.00"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamentoResiduo(new BigDecimal("0.00"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamentoCassa(new BigDecimal("1.00"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento1(new BigDecimal("2.00"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamento2(new BigDecimal("0.95"));
		
		AggiornaDettaglioVariazioneImportoCapitoloResponse res = aggiornaDettaglioVariazioneImportoCapitoloService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void stampaExcelVariazioneDiBilancio(){
		final String methodName = "stampaExcelVariazioneDiBilancio";
		StampaExcelVariazioneDiBilancio req = new StampaExcelVariazioneDiBilancio();
		
		req.setDataOra(new Date());
		req.setEnte(getEnteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		// 946 => bilancio 2018, variazione 2018
		// 938 => bilancio 2018, variazione 2019
		req.setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 207));
//		req.setXlsx(Boolean.TRUE);
		
		StampaExcelVariazioneDiBilancioResponse res = stampaExcelVariazioneDiBilancioService.executeService(req);
		assertNotNull(res);
		assertTrue(res.getErrori().isEmpty());
		
		try {
			FileUtils.writeByteArrayToFile(new File("C:/Users/interlogic/Desktop/variazione_" + req.getVariazioneImportoCapitolo().getUid() + "." + res.getExtension()), res.getReport());
		} catch (IOException e) {
			log.error(methodName, "Errore generazione file: " + e.getMessage());
		}
	}
	
	@Test
	public void ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req = new RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", 2017));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setUidVariazione(207);
		
		req.setCapitolo(create(Capitolo.class, 0));
		req.getCapitolo().setNumeroCapitolo(Integer.valueOf(20191104));
		req.getCapitolo().setNumeroArticolo(Integer.valueOf(0));
		req.getCapitolo().setNumeroUEB(Integer.valueOf(1));
		req.getCapitolo().setTipoCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
		
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse res = ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void gestisciDettaglioVariazioneComponenteImportoCapitoloByFile() {
		GestisciDettaglioVariazioneComponenteImportoCapitolo req = getTestFileObject(GestisciDettaglioVariazioneComponenteImportoCapitolo.class,  "variazioni/gestisciDettaglio.xml");
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", 2017));
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		GestisciDettaglioVariazioneComponenteImportoCapitoloResponse res = gestisciDettaglioVariazioneComponenteImportoCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void gestisciDettaglioVariazioneComponenteImportoCapitolo() {
		GestisciDettaglioVariazioneComponenteImportoCapitolo req = new GestisciDettaglioVariazioneComponenteImportoCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", 2017));
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		req.setDettaglioVariazioneImportoCapitolo(new DettaglioVariazioneImportoCapitolo());
		
		req.getDettaglioVariazioneImportoCapitolo().setStanziamentoCassa(new BigDecimal("1.00"));
		req.getDettaglioVariazioneImportoCapitolo().setStanziamentoResiduo(new BigDecimal("0.00"));
		req.getDettaglioVariazioneImportoCapitolo().setCapitolo(create(CapitoloUscitaGestione.class, 87814));
		req.getDettaglioVariazioneImportoCapitolo().setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 207));
		// variazione numero: 223
		
		req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo().add(createDettaglioVariazioneComponenteImportoCapitolo(41, new BigDecimal("2.00"), 0, 3, false));
		req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo().add(createDettaglioVariazioneComponenteImportoCapitolo(0, new BigDecimal("1.00"), 0, 5, false));
		
		req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1().add(createDettaglioVariazioneComponenteImportoCapitolo(43, new BigDecimal("0.50"), 0, 3, false));
		req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1().add(createDettaglioVariazioneComponenteImportoCapitolo(0, new BigDecimal("1.50"), 0, 5, false));
		
		req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2().add(createDettaglioVariazioneComponenteImportoCapitolo(45, new BigDecimal("0.45"), 0, 3, false));
		req.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2().add(createDettaglioVariazioneComponenteImportoCapitolo(0, new BigDecimal("0.50"), 0, 5, false));
		
//		String xml = "<gestisciDettaglioVariazioneComponenteImportoCapitolo><annoBilancio>2017</annoBilancio><dataOra>2019-11-06T17:25:58.362+01:00</dataOra><richiedente><account><stato>VALIDO</stato><uid>52</uid><codice>2 -  ACCOUNT  - AMMINISTRATORE</codice><nome>Demo 22</nome><descrizione>Demo 22 -  ACCOUNT  - AMMINISTRATORE - Regione Piemonte</descrizione><indirizzoMail>email</indirizzoMail><ente><stato>VALIDO</stato><uid>2</uid><gestioneLivelli><entry><key>VARIAZ_ORGANO_AMM</key><value>ORGANO AMMINISTRATIVO</value></entry><entry><key>CARICA_MISSIONE_DA_ESTERNO</key><value>CARICA_MISSIONE_DA_ESTERNO_ATTIVA</value></entry><entry><key>FIRMA_CARTA_2</key><value>Il responsabile della direzione</value></entry><entry><key>VARIAZ_ORGANO_LEG</key><value>ORGANO LEGISLATIVO</value></entry><entry><key>REV_ONERI_DISTINTA_MAN</key><value>FALSE</value></entry><entry><key>GESTIONE_EVASIONE_ORDINI</key><value>SENZA_VERIFICA</value></entry><entry><key>GESTIONE_PNOTA_DA_FIN</key><value>TRUE</value></entry><entry><key>GESTIONE_PARERE_FINANZIARIO</key><value>GESTIONE_PARERE_FINANZIARIO</value></entry><entry><key>GESTIONE_CONVALIDA_AUTOMATICA</key><value>CONVALIDA_MANUALE</value></entry><entry><key>REV_ONERI_CONTO_MAN</key><value>TRUE</value></entry><entry><key>FIRMA_CARTA_1</key><value>Il dirigente del settore ragioneria</value></entry><entry><key>GESTIONE_GSA</key><value>A14</value></entry></gestioneLivelli><nome>Regione Piemonte</nome></ente><casseEconomali><stato>VALIDO</stato><uid>2</uid><codice>01</codice><descrizione>ANTICIPI PER MISSIONI</descrizione></casseEconomali></account><operatore><stato>VALIDO</stato><uid>0</uid><codiceFiscale>AAAAAA00A11C000K</codiceFiscale><cognome>Montuori</cognome><nome>Raffaela</nome></operatore></richiedente><bilancio><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>131</uid><anno>2017</anno><faseEStatoAttualeBilancio><stato>VALIDO</stato><uid>0</uid><faseBilancio>PREVISIONE</faseBilancio></faseEStatoAttualeBilancio></bilancio><dettaglioVariazioneImportoCapitolo><stato>VALIDO</stato><uid>0</uid><capitolo><stato>VALIDO</stato><uid>87815</uid><uidCapitoloEquivalente>0</uidCapitoloEquivalente><uidExCapitolo>0</uidExCapitolo></capitolo><listaDettaglioVariazioneComponenteImportoCapitolo><dettaglioVariazioneComponenteImportoCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>56</uid><componenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>112</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>3</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>componente FRESCO</descrizione><macrotipoComponenteImportiCapitolo>FRESCO</macrotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>1</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo><dettaglioVariazioneComponenteImportoCapitolo><stato>VALIDO</stato><uid>0</uid><componenteImportiCapitolo><stato>VALIDO</stato><uid>115</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>4</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>componente fresco 2</descrizione><macrotipoComponenteImportiCapitolo>FRESCO</macrotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>6</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo><dettaglioVariazioneComponenteImportoCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>57</uid><componenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>106</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>7</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>FPV - PROGRAMMATO</descrizione><macrotipoComponenteImportiCapitolo>FPV</macrotipoComponenteImportiCapitolo><sottotipoComponenteImportiCapitolo>PROGRAMMATO_NON_IMPEGNATO</sottotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>0</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo></listaDettaglioVariazioneComponenteImportoCapitolo><listaDettaglioVariazioneComponenteImportoCapitolo1><dettaglioVariazioneComponenteImportoCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>59</uid><componenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>112</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>3</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>componente FRESCO</descrizione><macrotipoComponenteImportiCapitolo>FRESCO</macrotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>1</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo><dettaglioVariazioneComponenteImportoCapitolo><stato>VALIDO</stato><uid>0</uid><componenteImportiCapitolo><stato>VALIDO</stato><uid>0</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>4</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>componente fresco 2</descrizione><macrotipoComponenteImportiCapitolo>FRESCO</macrotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>5</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo><dettaglioVariazioneComponenteImportoCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>60</uid><componenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>106</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>7</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>FPV - PROGRAMMATO</descrizione><macrotipoComponenteImportiCapitolo>FPV</macrotipoComponenteImportiCapitolo><sottotipoComponenteImportiCapitolo>PROGRAMMATO_NON_IMPEGNATO</sottotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>0</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo></listaDettaglioVariazioneComponenteImportoCapitolo1><listaDettaglioVariazioneComponenteImportoCapitolo2><dettaglioVariazioneComponenteImportoCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>59</uid><componenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>112</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>3</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>componente FRESCO</descrizione><macrotipoComponenteImportiCapitolo>FRESCO</macrotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>1</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo><dettaglioVariazioneComponenteImportoCapitolo><stato>VALIDO</stato><uid>0</uid><componenteImportiCapitolo><stato>VALIDO</stato><uid>0</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>4</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>componente fresco 2</descrizione><macrotipoComponenteImportiCapitolo>FRESCO</macrotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>4</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo><dettaglioVariazioneComponenteImportoCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>60</uid><componenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>106</uid><listaDettaglioComponenteImportiCapitolo/><tipoComponenteImportiCapitolo><loginOperazione>2 -  ACCOUNT  - AMMINISTRATORE</loginOperazione><stato>VALIDO</stato><uid>7</uid><listaDettaglioComponenteImportiCapitolo/><codice>05</codice><descrizione>FPV - PROGRAMMATO</descrizione><macrotipoComponenteImportiCapitolo>FPV</macrotipoComponenteImportiCapitolo><sottotipoComponenteImportiCapitolo>PROGRAMMATO_NON_IMPEGNATO</sottotipoComponenteImportiCapitolo></tipoComponenteImportiCapitolo></componenteImportiCapitolo><flagEliminaComponenteCapitolo>false</flagEliminaComponenteCapitolo><flagNuovaComponenteCapitolo>false</flagNuovaComponenteCapitolo><importo>0</importo><tipoDettaglioComponenteImportiCapitolo>STANZIAMENTO</tipoDettaglioComponenteImportiCapitolo></dettaglioVariazioneComponenteImportoCapitolo></listaDettaglioVariazioneComponenteImportoCapitolo2><flagAnnullaCapitolo>false</flagAnnullaCapitolo><flagNuovoCapitolo>false</flagNuovoCapitolo><fondoPluriennaleVinc>0</fondoPluriennaleVinc><stanziamento>7.00</stanziamento><stanziamento1>6.00</stanziamento1><stanziamento2>5.00</stanziamento2><stanziamentoCassa>0.00</stanziamentoCassa><stanziamentoResiduo>0.00</stanziamentoResiduo><variazioneImportoCapitolo><stato>VALIDO</stato><uid>200</uid></variazioneImportoCapitolo></dettaglioVariazioneImportoCapitolo></gestisciDettaglioVariazioneComponenteImportoCapitolo>";
//		GestisciDettaglioVariazioneComponenteImportoCapitolo req = JAXBUtility.unmarshall(xml, GestisciDettaglioVariazioneComponenteImportoCapitolo.class);
		GestisciDettaglioVariazioneComponenteImportoCapitoloResponse res = gestisciDettaglioVariazioneComponenteImportoCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	private DettaglioVariazioneComponenteImportoCapitolo createDettaglioVariazioneComponenteImportoCapitolo(int uid, BigDecimal importo, int uidComponente, int uidTipoComponente, boolean elimina) {
		DettaglioVariazioneComponenteImportoCapitolo dettaglio = new DettaglioVariazioneComponenteImportoCapitolo();
		dettaglio.setUid(uid);
		dettaglio.setImporto(importo);
		dettaglio.setComponenteImportiCapitolo(create(ComponenteImportiCapitolo.class, uidComponente));
		dettaglio.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dettaglio.setFlagEliminaComponenteCapitolo(elimina);
		if(uidComponente == 0) {
			dettaglio.setFlagNuovaComponenteCapitolo(true);
			dettaglio.getComponenteImportiCapitolo().setTipoComponenteImportiCapitolo(create(TipoComponenteImportiCapitolo.class, uidTipoComponente));
		}
		return dettaglio;
	}
	
	@Test
	public void ricercaDettaglioVariazioneComponenteImportoCapitolo() {
		RicercaDettaglioVariazioneComponenteImportoCapitolo req = new RicercaDettaglioVariazioneComponenteImportoCapitolo();
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setDettaglioVariazioneImportoCapitolo(new DettaglioVariazioneImportoCapitolo());
		req.getDettaglioVariazioneImportoCapitolo().setCapitolo(create(CapitoloUscitaPrevisione.class, 87814));
		req.getDettaglioVariazioneImportoCapitolo().setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 207));
		req.setModelDetails(DettaglioVariazioneComponenteImportoCapitoloModelDetail.ComponenteImportiCapitolo,
				DettaglioVariazioneComponenteImportoCapitoloModelDetail.DettaglioVariazioneImportiCapitolo,
				DettaglioVariazioneComponenteImportoCapitoloModelDetail.Flag,
				ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo,
				ComponenteImportiCapitoloModelDetail.Importo);
		
		RicercaDettaglioVariazioneComponenteImportoCapitoloResponse res = ricercaDettaglioVariazioneComponenteImportoCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	private VariabileProcesso createVariabileProcesso(String nome, Object valore) {
		VariabileProcesso vp = new VariabileProcesso();
		vp.setNome(nome);
		vp.setValore(valore);
		return vp;
	}
	
	@Test
	public void eliminaDettaglio() {
		EliminaDettaglioVariazioneImportoCapitolo req = new EliminaDettaglioVariazioneImportoCapitolo();
		req.setAnnoBilancio(Integer.valueOf(2019));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setDettaglioVariazioneImportoCapitolo(create(DettaglioVariazioneImportoCapitolo.class, 0));
		req.getDettaglioVariazioneImportoCapitolo().setCapitolo(create(Capitolo.class, 201935));
		req.getDettaglioVariazioneImportoCapitolo().setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 977));
		
		EliminaDettaglioVariazioneImportoCapitoloResponse res = eliminaDettaglioVariazioneImportoCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisceDettaglioResiduoVariazioneBilancio() {
		InserisceDettaglioResiduoVariazioneBilancio req = new InserisceDettaglioResiduoVariazioneBilancio();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 208));
		
		InserisceDettaglioResiduoVariazioneBilancioResponse res = inserisceDettaglioResiduoVariazioneBilancioService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaAnagraficaVariazioneBilancioPerComponenti() {
		AggiornaAnagraficaVariazioneBilancio req = new AggiornaAnagraficaVariazioneBilancio();
		
		req.setAnnoBilancio(Integer.valueOf(2019));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setAnnullaVariazione(Boolean.FALSE);
		req.setEvolviProcesso(Boolean.TRUE);
		req.setIdAttivita("VariazioneDiBilancio--1.1--484--VariazioneDiBilancio_AggiornamentoVariazione--itec17ba82-dd70-424f-8657-8757fd981809--mainActivityInstance--noLoop");
//		req.setIdAttivita("VariazioneDiBilancio--1.1--478--VariazioneDiBilancio_AggiornamentoVariazioneGiunta--it45734ff9-97ba-49e6-bf12-4c8f6b3d9c72--mainActivityInstance--noLoop");
//		req.setIdAttivita("VariazioneDiBilancio--1.1--478--VariazioneDiBilancio_AggiornamentoVariazioneConsiglio--itd6aa720e-86cb-4b6f-a0b0-e1a2859af8fc--mainActivityInstance--noLoop");
		
		req.setInvioOrganoAmministrativo(Boolean.FALSE);
		req.setInvioOrganoLegislativo(Boolean.FALSE);
		req.setSaltaCheckNecessarioAttoAmministrativoVariazioneDiBilancio(false);
		req.setSaltaCheckStanziamentoCassa(false);
		
		req.setVariazioneImportoCapitolo(create(VariazioneImportoCapitolo.class, 924));
		req.getVariazioneImportoCapitolo().setNumero(Integer.valueOf(223));
		req.getVariazioneImportoCapitolo().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getVariazioneImportoCapitolo().setBilancio(getBilancioByProperties("consip", "regp", 2019));
		req.getVariazioneImportoCapitolo().setTipoVariazione(TipoVariazione.VARIAZIONE_IMPORTO);
		req.getVariazioneImportoCapitolo().setDescrizione("SIAC-6881 - eliminazione componente");
		req.getVariazioneImportoCapitolo().setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.BOZZA);
		req.getVariazioneImportoCapitolo().setApplicazioneVariazione(ApplicazioneVariazione.GESTIONE);
		req.getVariazioneImportoCapitolo().setAttoAmministrativo(create(AttoAmministrativo.class, 69966));
		req.getVariazioneImportoCapitolo().setAttoAmministrativoVariazioneBilancio(create(AttoAmministrativo.class, 70261));
		
		Calendar cal = Calendar.getInstance();
		cal.set(2019, Calendar.OCTOBER, 16, 8, 00, 00);
		cal.set(Calendar.MILLISECOND, 0);
		req.getVariazioneImportoCapitolo().setData(cal.getTime());
		
		AggiornaAnagraficaVariazioneBilancioResponse res = aggiornaAnagraficaVariazioneBilancioService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void definisceAnagraficaVariazioneBilancioPerComponenti() {
		RicercaDettaglioAnagraficaVariazioneBilancio request = new RicercaDettaglioAnagraficaVariazioneBilancio();
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		request.setUidVariazione(924);
		
		RicercaDettaglioAnagraficaVariazioneBilancioResponse response = ricercaDettaglioAnagraficaVariazioneBilancioService.executeService(request);
		assertFalse("Response with errors", response.hasErrori());
		
		DefinisceAnagraficaVariazioneBilancio req = new DefinisceAnagraficaVariazioneBilancio();
		
		req.setDataOra(new Date());
		req.setAnnoBilancio(Integer.valueOf(2019));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setIdAttivita("VariazioneDiBilancio--1.1--484--VariazioneDiBilancio_DefinizioneDellaVariazione--itbeed4b37-d30f-451a-8fd7-db6b7043d655--mainActivityInstance--noLoop");
		req.setSaltaCheckStanziamentoCassa(false);
		req.setVariazioneImportoCapitolo(response.getVariazioneImportoCapitolo());
		
		req.getListaVariabiliProcesso().add(createVariabileProcesso("variazioneDiBilancioDem", "924|223"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("invioGiunta", Boolean.FALSE));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("descrizioneBreve", "223 - SIAC-6881 - eliminazione componente - Variazione di bilancio"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("siacEnteProprietarioProcesso", "SIAC-ENTE-PROPRIETARIO-2"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("annullaVariazione", Boolean.FALSE));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("quadraturaVariazioneDiBilancio", Boolean.TRUE));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("siacAnnoEsercizioProcesso", "SIAC-ANNO-ESERCIZIO-2019"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("statoVariazioneDiBilancio", "PRE-DEFINITIVA"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("variazioneDiBilancio_execAzioneRichiestaResponse", null));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("annoDiEsercizio", Integer.valueOf(2019)));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("descrizione", "223 - SIAC-6881 - eliminazione componente - Variazione di bilancio"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("tipoVariazioneBilancio", "Importo"));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("invioConsiglio", Boolean.FALSE));
		req.getListaVariabiliProcesso().add(createVariabileProcesso("siacSacProcesso", "strutture"));
		
		req.setVariazioneImportoCapitolo(response.getVariazioneImportoCapitolo());
		DefinisceAnagraficaVariazioneBilancioResponse res = definisceAnagraficaVariazioneBilancioService.executeService(req);
		assertNotNull(res);
	}
	
	@Autowired private TipoComponenteImportiCapitoloDad tipoComponenteImportiCapitoloDad;
	
	@Test
	public void testTipoComponente() {
		Long countTipoComponenteWithMacrotipoDiversoDa = tipoComponenteImportiCapitoloDad.countTipoComponenteWithMacrotipoDiversoDa(Arrays.asList(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5)), MacrotipoComponenteImportiCapitolo.DA_ATTRIBUIRE);
		System.out.println("result : " + countTipoComponenteWithMacrotipoDiversoDa);
	}
	
	@Autowired private VariazioniDad variazioniImportoDad;
	
	@Test
	public void variazioniImportoDadTest () {
		boolean capitoliComuniAdaltreVariazioni = variazioniImportoDad.isCapitoliComuniAdAltreVariazioni(1521, Arrays.asList(1522));
		System.out.println("Presenza di capitoli in comune ad altre variazioni: " + capitoliComuniAdaltreVariazioni);
	}
	
	@Autowired private ElaborazioniDad elaborazioniDad;
	@Autowired private ElaborazioniManager elaborazioniManager;
	
   @Test 
   public void testElaborazioni() {
	   int uidVariazione = 1521;
	   elaborazioniManager.init(getRichiedenteByProperties("consip", "regp").getAccount().getEnte(), "Concorrenza variazine - II ");
	   ElabKeys definisciVariazioneKeySelector = ElabKeys.DEFINISCI_VARIAZIONE;
	   ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(uidVariazione,DefinisceAnagraficaVariazioneBilancioAsyncService.class);
	   String elabService = eakh.creaElabServiceFromPattern(definisciVariazioneKeySelector);
	   List<String> elabKeysElaborazioniAttive = elaborazioniManager.getElabKeysElaborazioniAttive(elabService);
		if(elabKeysElaborazioniAttive == null || elabKeysElaborazioniAttive.isEmpty()) {
			return;
		}
		List<Integer> uids = new ArrayList<Integer>();
		for (String string : elabKeysElaborazioniAttive) {
			Integer uidByElabKey = ElaborazioniAttiveKeyHandler.getUidByElabKey(definisciVariazioneKeySelector, string);
			if(uidByElabKey != null && uidByElabKey.intValue() != uidVariazione) {
				System.out.println("aggiungo l'uid " + uidByElabKey);
				uids.add(uidByElabKey);
			}
		}
		if(uids.isEmpty()) {
			return;
		}
		boolean capitoliComuniAdAltreVariazioni = variazioniImportoDad.isCapitoliComuniAdAltreVariazioni(uidVariazione, uids);
		if(capitoliComuniAdAltreVariazioni) {
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore("Alcuni capitoli afferenti alla variazione sono oggetto di un'altra definizione di variazione attualemnente in corso. Attendere il termine dell'elaborazione."));
		}
	   
	  
   }
	
}

