/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.primanota;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.primanota.AggiornaPrimaNotaGENService;
import it.csi.siac.siacbilser.business.service.primanota.AggiornaPrimaNotaIntegrataManualeService;
import it.csi.siac.siacbilser.business.service.primanota.AggiornaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.AnnullaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaIntegrataManualeService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.RegistraPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaDettaglioPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaDettaglioPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaIntegrataManualeService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaIntegrataValidabileService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.ValidaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.ValidazioneMassivaPrimaNotaIntegrataAsyncService;
import it.csi.siac.siacbilser.business.service.primanota.ValidazioneMassivaPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.rateirisconti.OttieniEntitaCollegatePrimaNotaService;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.LiquidazioneModelDetail;
import it.csi.siac.siacfin2ser.model.SubAccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabileResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoCollegamento;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;

public class PrimaNotaTest extends BaseJunit4TestCase {
	
	@Autowired
	private InseriscePrimaNotaService inseriscePrimaNotaService;
	
	@Autowired
	private AggiornaPrimaNotaService aggiornaPrimaNotaService;
	
	@Autowired
	private AnnullaPrimaNotaService annullaPrimaNotaService;
	
	@Autowired
	private RicercaDettaglioPrimaNotaService ricercaDettaglioPrimaNotaService;
	
	@Autowired
	private RicercaDettaglioPrimaNotaIntegrataService ricercaDettaglioPrimaNotaIntegrataService;
	
	@Autowired
	private RicercaSinteticaPrimaNotaService ricercaSinteticaPrimaNotaService;
	
	@Autowired
	private RicercaSinteticaPrimaNotaIntegrataService ricercaSinteticaPrimaNotaIntegrataService;
	
	@Autowired
	private RicercaSinteticaPrimaNotaIntegrataValidabileService ricercaSinteticaPrimaNotaIntegrataValidabileService;
	
	@Autowired
	private ValidaPrimaNotaService validaPrimaNotaService;
	
	@Autowired
	private ValidazioneMassivaPrimaNotaIntegrataService validazioneMassivaPrimaNotaIntegrataService;
	
	@Autowired
	private ValidazioneMassivaPrimaNotaIntegrataAsyncService validazioneMassivaPrimaNotaIntegrataAsyncService;
	
	@Autowired
	private OttieniEntitaCollegatePrimaNotaService ottieniEntitaCollegatePrimaNotaService;
	
	@Autowired
	private RegistraPrimaNotaIntegrataService registraPrimaNotaIntegrataService;
	
	@Autowired
	private InseriscePrimaNotaIntegrataManualeService inseriscePrimaNotaIntegrataManualeService;
	
	@Autowired
	private RicercaSinteticaPrimaNotaIntegrataManualeService ricercaSinteticaPrimaNotaIntegrataManualeService;
	
	@Autowired
	private AggiornaPrimaNotaIntegrataManualeService aggiornaPrimaNotaIntegrataManualeService;
	
	@Autowired
	private AggiornaPrimaNotaGENService aggiornaPrimaNotaGENService;
	
	@Test
	public void inseriscePrimaNota() {
		
		InseriscePrimaNota req = new InseriscePrimaNota();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		PrimaNota primaNota = creaPrimaNotaFigliaFIN(req);
		req.setPrimaNota(primaNota);
		
		InseriscePrimaNotaResponse res = inseriscePrimaNotaService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void inseriscePrimaNotaRelazionata() throws Exception {
		
		RicercaDettaglioPrimaNota reqRicerca = new RicercaDettaglioPrimaNota();
		reqRicerca.setDataOra(new Date());
		reqRicerca.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqRicerca.setPrimaNota(create(PrimaNota.class, 24568));
		reqRicerca.getPrimaNota().setBilancio(getBilancioTest(131, 2017));
		reqRicerca.setAnnoBilancio(reqRicerca.getPrimaNota().getBilancio().getAnno());
		RicercaDettaglioPrimaNotaResponse resRicerca = ricercaDettaglioPrimaNotaService.executeService(reqRicerca);
		assertNotNull(resRicerca.getPrimaNota());
		if(!resRicerca.getPrimaNota().getNumero().equals(Integer.valueOf(24422))) {
			throw new Exception("waaaaaaa");
		}
		
		InseriscePrimaNota req = new InseriscePrimaNota();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		PrimaNota primaNota = creaPrimaNotaFigliaFIN(req);
		TipoRelazionePrimaNota tipoRelazionePrimaNota = new TipoRelazionePrimaNota();
		tipoRelazionePrimaNota.setUid(2);
		tipoRelazionePrimaNota.setCodice("A");
		resRicerca.getPrimaNota().setTipoRelazionePrimaNota(tipoRelazionePrimaNota);
//		tipoRelazionePrimaNota.setDescrizione(Altro);
		primaNota.getListaPrimaNotaPadre().add(resRicerca.getPrimaNota());
		req.setPrimaNota(primaNota);
		
		InseriscePrimaNotaResponse res = inseriscePrimaNotaService.executeService(req);
		assertNotNull(res);

	}


		
	private PrimaNota creaPrimaNotaFigliaFIN(InseriscePrimaNota req) {
		PrimaNota primaNota = new PrimaNota();
		
		primaNota.setAmbito(Ambito.AMBITO_FIN);
		primaNota.setBilancio(getBilancioTest(131, 2017));
		primaNota.setDescrizione(" prima nota figlia");
		primaNota.setEnte(req.getRichiedente().getAccount().getEnte());
		primaNota.setTipoCausale(TipoCausale.Libera);
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.DEFINITIVO);
//		primaNota.setStatoAccettazionePrimaNotaProvvisoria(StatoAccettazionePrimaNotaDefiniti);
		primaNota.setStatoAccettazionePrimaNotaDefinitiva(StatoAccettazionePrimaNotaDefinitiva.DA_ACCETTARE);
		primaNota.setDataRegistrazione(parseDate("25/09/2017"));
//		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
//		primaNota.setStatoAccettazionePrimaNotaProvvisoria(StatoAccettazionePrimaNotaProvvisoria.PROVVISORIO);
		primaNota.setDataRegistrazione(new Date());
		
		MovimentoEP movimentoEP = new MovimentoEP();
		
		movimentoEP.setCausaleEP(create(CausaleEP.class, 539637));
		movimentoEP.setAmbito(Ambito.AMBITO_FIN);
		movimentoEP.setEnte(req.getRichiedente().getAccount().getEnte());
		
		MovimentoDettaglio movimentoDettaglio = new MovimentoDettaglio();
		
		movimentoDettaglio.setAmbito(Ambito.AMBITO_FIN);
		movimentoDettaglio.setConto(create(Conto.class, 38686));
		movimentoDettaglio.setEnte(req.getRichiedente().getAccount().getEnte());
		movimentoDettaglio.setImporto(new BigDecimal("1"));
		movimentoDettaglio.setSegno(OperazioneSegnoConto.DARE);
		movimentoDettaglio.setNumeroRiga(Integer.valueOf(1));
//		movimentoDettaglio.setMissione(create(Missione.class, 118794));
//		movimentoDettaglio.setProgramma(create(Programma.class, 118795));
		
//		movimentoEP.getListaMovimentoDettaglio().add(mov
//		imentoDettaglio);
		// Attivo Patrimoniale, nessuna missione ne' programma
		MovimentoDettaglio movimentoDettaglio2 = new MovimentoDettaglio();		
		movimentoDettaglio2.setAmbito(Ambito.AMBITO_FIN);
		movimentoDettaglio2.setConto(create(Conto.class, 65328));
		movimentoDettaglio2.setEnte(req.getRichiedente().getAccount().getEnte());
		movimentoDettaglio2.setImporto(new BigDecimal("1"));
		movimentoDettaglio2.setSegno(OperazioneSegnoConto.AVERE);
		movimentoDettaglio2.setNumeroRiga(Integer.valueOf(1));
		
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglio);
		movimentoEP.getListaMovimentoDettaglio().add(movimentoDettaglio2);
		primaNota.getListaMovimentiEP().add(movimentoEP);
		return primaNota;
	}
	
	
	@Test
	public void aggiornaPrimaNota() {
		
		AggiornaPrimaNota req = new AggiornaPrimaNota();
		
		Conto conto = new Conto();
		conto.setUid(7);
		
		MovimentoDettaglio det1 = new MovimentoDettaglio();
		det1.setImporto(new BigDecimal(10));
		det1.setSegno(OperazioneSegnoConto.DARE);
		det1.setNumeroRiga(1);
		det1.setConto(conto);
		
		MovimentoDettaglio det2 = new MovimentoDettaglio();
		det2.setImporto(new BigDecimal(20));
		det2.setSegno(OperazioneSegnoConto.AVERE);
		det2.setNumeroRiga(2);
		det2.setConto(conto);
		
		CausaleEP causaleEP = new CausaleEP();
		causaleEP.setUid(1);
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
		listaMovimentoDettaglio.add(det1);
//		listaMovimentoDettaglio.add(det2);
		
		MovimentoEP movimentoEP = new MovimentoEP();
		movimentoEP.setCausaleEP(causaleEP);
		movimentoEP.setListaMovimentoDettaglio(listaMovimentoDettaglio );
		movimentoEP.setNumero(3);
		
		List<MovimentoEP> listaMovimentiEP = new ArrayList<MovimentoEP>();
		listaMovimentiEP.add(movimentoEP);
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setTipoCausale(TipoCausale.Libera);
		primaNota.setBilancio(getBilancio2015Test());
		primaNota.setDataRegistrazione(new Date());
		primaNota.setDescrizione("test prima nota 4 aggiornata");
		primaNota.setListaMovimentiEP(listaMovimentiEP);
		primaNota.setUid(12);
		primaNota.setNumero(4);
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setPrimaNota(primaNota);
		
		AggiornaPrimaNotaResponse res = aggiornaPrimaNotaService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void annullaPrimaNota() {
		AnnullaPrimaNota req = new AnnullaPrimaNota();
		PrimaNota primaNota = new PrimaNota();
		primaNota.setUid(2);
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setPrimaNota(primaNota);
		
		AnnullaPrimaNotaResponse res = annullaPrimaNotaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioPrimaNota() {
		RicercaDettaglioPrimaNota req = new RicercaDettaglioPrimaNota();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setPrimaNota(create(PrimaNota.class, 24145));
		req.getPrimaNota().setBilancio(getBilancioTest(131, 2017));
		req.setAnnoBilancio(req.getPrimaNota().getBilancio().getAnno());
		RicercaDettaglioPrimaNotaResponse res = ricercaDettaglioPrimaNotaService.executeService(req);
		assertNotNull(res);
		assertNotNull(res.getPrimaNota());
		log.logXmlTypeObject(res.getPrimaNota(), "PRIMANOTA");
	}
	
	
	@Test
	public void ricercaDettaglioPrimaNotaIntegrata() {
		RicercaDettaglioPrimaNotaIntegrata req = new RicercaDettaglioPrimaNotaIntegrata();
		PrimaNota primaNota = new PrimaNota();
		primaNota.setUid(23);
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setUid(1);
		Documento<?, ?> documento = new Documento<Subdocumento<?,?>, SubdocumentoIva<?,?,?>>();
		documento.setUid(166);
		//		req.setPrimaNota(primaNota);
//		req.setRegistrazioneMovFin(registrazioneMovFin );
		req.setDocumento(documento );
		
		RicercaDettaglioPrimaNotaIntegrataResponse res = ricercaDettaglioPrimaNotaIntegrataService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaPrimaNota() {
		RicercaSinteticaPrimaNota req = new RicercaSinteticaPrimaNota();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		req.setParametriPaginazione(getParametriPaginazioneTest());
//		req.setMissione(create(Missione.class, 118895));
//		req.setProgramma(create(Programma.class, 118897));
		
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setTipoCausale(TipoCausale.Libera);
		primaNota.setAmbito(Ambito.AMBITO_GSA);
		primaNota.setClassificatoreGSA(create(ClassificatoreGSA.class, 1));
		req.setPrimaNota(primaNota);
		
		RicercaSinteticaPrimaNotaResponse res = ricercaSinteticaPrimaNotaService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaSinteticaPrimaNotaIntegrata() {
		RicercaSinteticaPrimaNotaIntegrata req = new RicercaSinteticaPrimaNotaIntegrata();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(132, 2018));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setParametriPaginazione(getParametriPaginazioneTest());
//		req.setImportoDocumentoA(new BigDecimal("6"));
//		req.setImportoDocumentoDa(new BigDecimal("4"));
//		req.setTipoEvento(create(TipoEvento.class, 121));
		
//		primaNota.setClassificatoreGSA(create(Classificatore.class, 1));
		req.setPrimaNota(create(PrimaNota.class, 0));
		req.getPrimaNota().setAmbito(Ambito.AMBITO_FIN);
		
		req.setTipoEvento(create(TipoEvento.class, 2));
		req.setAnnoMovimento(2018);
		req.setNumeroMovimento("999999");
		req.setTipoElenco("S");
		
		
//		req.setEvento(create(Evento.class,341));
//		
//		req.setAnnoMovimento(2017);
//		req.setNumeroMovimento("6578");
//		req.setMovimentoGestione(movimentoGestione);
//		req.setSubMovimentoGestione(create(SubImpegno.class, 82183));
//		req.setStrutturaAmministrativoContabile(create(StrutturaAmministrativoContabile.class, 1719691));
//		req.setCapitolo(create(CapitoloUscitaGestione.class, 104215));
		RicercaSinteticaPrimaNotaIntegrataResponse res = ricercaSinteticaPrimaNotaIntegrataService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void validaPrimaNota() {
		ValidaPrimaNota req = new ValidaPrimaNota();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setPrimaNota(create(PrimaNota.class, 24147));
		req.getPrimaNota().setAmbito(Ambito.AMBITO_GSA);
		req.getPrimaNota().setClassificatoreGSA(create(ClassificatoreGSA.class, 10));
		
		ValidaPrimaNotaResponse res = validaPrimaNotaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaPrimaNotaIntegrataValidabile() {
		RicercaSinteticaPrimaNotaIntegrataValidabile req = new RicercaSinteticaPrimaNotaIntegrataValidabile();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setDataOra(new Date());
		
		Bilancio bilancio = getBilancioTest(143, 2017);
		req.setBilancio(bilancio);
		
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, 2016);
//		cal.set(Calendar.MONTH, Calendar.JULY);
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		req.setDataRegistrazioneDa(cal.getTime());
//		
//		cal.set(Calendar.MONTH, Calendar.AUGUST);
//		req.setDataRegistrazioneA(cal.getTime());
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setAmbito(Ambito.AMBITO_FIN);
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		req.setPrimaNota(primaNota);

		req.setTipoEvento(create(TipoEvento.class, 77));
		req.setCapitolo(create(CapitoloUscitaGestione.class, 42276));
		
		RicercaSinteticaPrimaNotaIntegrataValidabileResponse res = ricercaSinteticaPrimaNotaIntegrataValidabileService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void validazioneMassivaPrimaNotaIntegrata() {
		ValidazioneMassivaPrimaNotaIntegrata req = new ValidazioneMassivaPrimaNotaIntegrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		
		RicercaSinteticaPrimaNotaIntegrataValidabile reqRSPNIV = new RicercaSinteticaPrimaNotaIntegrataValidabile();
		
		reqRSPNIV.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		reqRSPNIV.setBilancio(getBilancio2015Test());
		reqRSPNIV.setParametriPaginazione(getParametriPaginazioneTest());
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setAmbito(Ambito.AMBITO_FIN);//TODO aggiungere parametri
		reqRSPNIV.setPrimaNota(primaNota);
		TipoEvento tipoEvento = new TipoEvento();
		tipoEvento.setUid(58); //doc spesa dev
//		tipoEvento.setUid(53); //doc spesa FORN2 CRP
		reqRSPNIV.setTipoEvento(tipoEvento);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2016);
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		reqRSPNIV.setDataRegistrazioneDa(cal.getTime());
		
		req.setRicercaSinteticaPrimaNotaIntegrataValidabile(reqRSPNIV);
		
		ValidazioneMassivaPrimaNotaIntegrataResponse res = validazioneMassivaPrimaNotaIntegrataService.executeService(req);
		assertNotNull(res);
		
	}
	
	@Test
	public void validazioneMassivaPrimaNotaIntegrataAsync() {
		ValidazioneMassivaPrimaNotaIntegrata req = new ValidazioneMassivaPrimaNotaIntegrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		
		RicercaSinteticaPrimaNotaIntegrataValidabile reqRSPNIV = new RicercaSinteticaPrimaNotaIntegrataValidabile();
		
		reqRSPNIV.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		reqRSPNIV.setBilancio(getBilancio2015Test());
		reqRSPNIV.setParametriPaginazione(getParametriPaginazioneTest());
		
		PrimaNota primaNota = new PrimaNota();
		reqRSPNIV.setPrimaNota(primaNota);
		req.setRicercaSinteticaPrimaNotaIntegrataValidabile(reqRSPNIV);
		
		AsyncServiceRequestWrapper<ValidazioneMassivaPrimaNotaIntegrata> reqAsync = new AsyncServiceRequestWrapper<ValidazioneMassivaPrimaNotaIntegrata>();
		reqAsync.setDataOra(req.getDataOra());
		reqAsync.setEnte(getEnteTest());
		reqAsync.setRequest(req);
		reqAsync.setRichiedente(req.getRichiedente());
		reqAsync.setAccount(req.getRichiedente().getAccount());
		
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		Azione azione = new Azione();
		azione.setUid(6193);
		azioneRichiesta.setAzione(azione);
		reqAsync.setAzioneRichiesta(azioneRichiesta);
		
		AsyncServiceResponse res = validazioneMassivaPrimaNotaIntegrataAsyncService.executeService(reqAsync);
		assertNotNull(res);
		
		try {
			Thread.sleep(2 * 60 * 1000);
		} catch (InterruptedException e) {
			log.error("validazioneMassivaPrimaNotaIntegrata", e.getMessage(), e);
		}
		
	}
	
	@Test
	public void ottieniEntitaCollegatePrimaNota() {
		OttieniEntitaCollegatePrimaNota req = new OttieniEntitaCollegatePrimaNota();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		PrimaNota primaNota = new PrimaNota();
//		primaNota.setUid(96); //documento entrata
//		req.setTipoCollegamento(TipoCollegamento.SUBDOCUMENTO_ENTRATA);

//		primaNota.setUid(92); //documento entrata
//		req.setTipoCollegamento(TipoCollegamento.SUBDOCUMENTO_ENTRATA);
//		primaNota.setUid(120); //documento entrata
//		req.setTipoCollegamento(TipoCollegamento.SUBDOCUMENTO_SPESA);

		//DOCUMENTO CON 11 quote
		primaNota.setUid(464);
		req.setTipoCollegamento(TipoCollegamento.SUBDOCUMENTO_SPESA);
		
//		primaNota.setUid(61);
//		req.setTipoCollegamento(TipoCollegamento.LIQUIDAZIONE);
		
//		primaNota.setUid(88);//documento spesa
//		req.setTipoCollegamento(TipoCollegamento.SUBDOCUMENTO_SPESA);
		req.setPrimaNota(primaNota);
		//ACCERTAMENTO con pdc:
		
//		primaNota.setUid(82);//
//		req.setTipoCollegamento(TipoCollegamento.ACCERTAMENTO);
//		req.setPrimaNota(primaNota);
		//IMPEGNO con pdc:
//		primaNota.setUid(110);//
//		req.setTipoCollegamento(TipoCollegamento.IMPEGNO);
//		req.setPrimaNota(primaNota);
		
		//req.setTipoCollegamento(TipoCollegamento.SUBDOCUMENTO_ENTRATA);
		//req.setModelDetails(SubdocumentoEntrataModelDetail.TestataDocumento,SubdocumentoEntrataModelDetail.AccertamentoSubaccertamentoPdc, SubdocumentoEntrataModelDetail.Ordinativo);
		
		req.setModelDetails(ottieniModelDetailPerTipoCollegamento(req.getTipoCollegamento()));
//		PrimaNota primaNota = new PrimaNota();
//		primaNota.setUid(59);
//		req.setPrimaNota(primaNota);
//		
//		req.setTipoCollegamento(TipoCollegamento.IMPEGNO);
//		req.setModelDetails(ImpegnoModelDetail.Soggetto, ImpegnoModelDetail.PianoDeiConti);
		
//		PrimaNota primaNota = new PrimaNota();
//		primaNota.setUid(60);
//		req.setPrimaNota(primaNota);
//		
//		req.setTipoCollegamento(TipoCollegamento.ACCERTAMENTO);
//		req.setModelDetails(AccertamentoModelDetail.Soggetto, AccertamentoModelDetail.PianoDeiConti);
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione(1, 10);
		req.setParametriPaginazione(parametriPaginazione);
		
		OttieniEntitaCollegatePrimaNotaResponse res = ottieniEntitaCollegatePrimaNotaService.executeService(req);
		assertNotNull(res);
	}
	
	private ModelDetail[] ottieniModelDetailPerTipoCollegamento(TipoCollegamento tipoCollegamento) {
		Map<TipoCollegamento, ModelDetail[]> details = new HashMap<TipoCollegamento, ModelDetail[]>();
		details.put(TipoCollegamento.IMPEGNO, new ModelDetail[] {ImpegnoModelDetail.Soggetto, ImpegnoModelDetail.PianoDeiConti});
		details.put(TipoCollegamento.SUBIMPEGNO, new ModelDetail[] {SubImpegnoModelDetail.Soggetto, SubImpegnoModelDetail.PianoDeiConti});
		
		details.put(TipoCollegamento.ACCERTAMENTO, new ModelDetail[] {AccertamentoModelDetail.Soggetto, AccertamentoModelDetail.PianoDeiConti});
		details.put(TipoCollegamento.SUBACCERTAMENTO, new ModelDetail[] {SubAccertamentoModelDetail.Soggetto, SubAccertamentoModelDetail.PianoDeiConti});
		
		details.put(TipoCollegamento.LIQUIDAZIONE, new ModelDetail[] {LiquidazioneModelDetail.Impegno, LiquidazioneModelDetail.Soggetto, LiquidazioneModelDetail.PianoDeiConti});
		
		details.put(TipoCollegamento.SUBDOCUMENTO_SPESA, new ModelDetail[] {SubdocumentoSpesaModelDetail.TestataDocumento, SubdocumentoSpesaModelDetail.ImpegnoSubimpegnoPdc, SubdocumentoSpesaModelDetail.Liquidazione, SubdocumentoSpesaModelDetail.Ordinativo});
		details.put(TipoCollegamento.SUBDOCUMENTO_ENTRATA, new ModelDetail[] {SubdocumentoEntrataModelDetail.TestataDocumento,SubdocumentoEntrataModelDetail.AccertamentoSubaccertamentoPdc, SubdocumentoEntrataModelDetail.Ordinativo});
		
		return details.get(tipoCollegamento);
	}
	
	@Test
	public void registraPrimaNotaIntegrata() {
		RegistraPrimaNotaIntegrata req = new RegistraPrimaNotaIntegrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setCheckOnlyElaborazioneAttiva(Boolean.FALSE);
		req.setIsDaValidare(Boolean.TRUE);
		req.setIsAggiornamento(Boolean.TRUE);
		
		req.setPrimaNota(create(PrimaNota.class, 6030));
		req.getPrimaNota().setAmbito(Ambito.AMBITO_FIN);
		req.getPrimaNota().setBilancio(getBilancioTest(131, 2017));
		req.getPrimaNota().setDescrizione("FAT 2017/8017041272 Fattura n. 8017041272 del 02-03-2017");
		req.getPrimaNota().setDataRegistrazione(parseDate("15/03/2017"));
		req.getPrimaNota().setTipoCausale(TipoCausale.Integrata);
		req.getPrimaNota().setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		req.getPrimaNota().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getPrimaNota().setNoteCollegamento(null);
		req.getPrimaNota().setNumero(Integer.valueOf(6030));
		
		MovimentoEP mep = create(MovimentoEP.class, 6052);
		req.getPrimaNota().getListaMovimentiEP().add(mep);
		
		mep.setAmbito(Ambito.AMBITO_FIN);
		mep.setCausaleEP(create(CausaleEP.class, 301572));
		mep.setDataCreazioneMovimentoEP(parseDate("15/03/2017"));
		mep.setDataInizioValiditaMovimentoEP(parseDate("15/03/2017"));
		mep.setDataModificaMovimentoEP(parseDate("15/03/2017"));
		mep.setDescrizione("FAT 2017/8017041272 Fattura n. 8017041272 del 02-03-2017");
		mep.setEnte(req.getRichiedente().getAccount().getEnte());
		mep.setNumero(Integer.valueOf(6052));
		mep.setPrimaNota(create(PrimaNota.class, 6030));
		mep.getPrimaNota().setAmbito(Ambito.AMBITO_FIN);
		mep.getPrimaNota().setEnte(req.getRichiedente().getAccount().getEnte());
		
		mep.setRegistrazioneMovFin(create(RegistrazioneMovFin.class, 12801));
		mep.getRegistrazioneMovFin().setAmbito(Ambito.AMBITO_FIN);
		mep.getRegistrazioneMovFin().setEnte(req.getRichiedente().getAccount().getEnte());
		
		MovimentoDettaglio md1 = create(MovimentoDettaglio.class, 12103);
		MovimentoDettaglio md2 = create(MovimentoDettaglio.class, 12104);
		mep.getListaMovimentoDettaglio().add(md1);
		mep.getListaMovimentoDettaglio().add(md2);
		
		md1.setConto(create(Conto.class, 66023));
		md1.setEnte(req.getRichiedente().getAccount().getEnte());
		md1.setNumeroRiga(Integer.valueOf(0));
		md1.setImporto(new BigDecimal("24667.20"));
		md1.setSegno(OperazioneSegnoConto.AVERE);
		md1.setAmbito(Ambito.AMBITO_FIN);
		md1.setMovimentoEP(create(MovimentoEP.class, 6052));
		md1.getMovimentoEP().setAmbito(Ambito.AMBITO_FIN);
		md1.getMovimentoEP().setEnte(req.getRichiedente().getAccount().getEnte());
		
		md2.setConto(create(Conto.class, 66053));
		md2.setEnte(req.getRichiedente().getAccount().getEnte());
		md2.setNumeroRiga(Integer.valueOf(1));
		md2.setImporto(new BigDecimal("24667.20"));
		md2.setSegno(OperazioneSegnoConto.DARE);
		md2.setAmbito(Ambito.AMBITO_FIN);
		md2.setMovimentoEP(create(MovimentoEP.class, 6052));
		md2.getMovimentoEP().setAmbito(Ambito.AMBITO_FIN);
		md2.getMovimentoEP().setEnte(req.getRichiedente().getAccount().getEnte());
		
		RegistraPrimaNotaIntegrataResponse res = registraPrimaNotaIntegrataService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void inseriscePrimaNotaIntegrataManuale() {
		InseriscePrimaNotaIntegrataManuale req = new InseriscePrimaNotaIntegrataManuale();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setEntita(create(Impegno.class, 51348));
		//req.setEvento(create(Evento.class, 4721));
		req.setEvento(create(Evento.class, 4719));
		
		req.setPrimaNota(create(PrimaNota.class, 0));
		req.getPrimaNota().setTipoCausale(TipoCausale.Integrata);
		req.getPrimaNota().setBilancio(getBilancioTest(131, 2017));
		req.getPrimaNota().setDescrizione("Test via JUnit");
		req.getPrimaNota().setAmbito(Ambito.AMBITO_GSA);
		
		// MovimentiEP
		MovimentoEP mep = new MovimentoEP();
		req.getPrimaNota().getListaMovimentiEP().add(mep);
		mep.setCausaleEP(create(CausaleEP.class, 539623));
		
		MovimentoDettaglio md = new MovimentoDettaglio();
		mep.getListaMovimentoDettaglio().add(md);
		md.setConto(create(Conto.class, 124141));
		md.setNumeroRiga(Integer.valueOf(1));
		md.setImporto(new BigDecimal("100"));
		md.setSegno(OperazioneSegnoConto.DARE);
		
		md = new MovimentoDettaglio();
		mep.getListaMovimentoDettaglio().add(md);
		md.setConto(create(Conto.class, 124673));
		md.setNumeroRiga(Integer.valueOf(2));
		md.setImporto(new BigDecimal("100"));
		md.setSegno(OperazioneSegnoConto.AVERE);
		
		InseriscePrimaNotaIntegrataManualeResponse res = inseriscePrimaNotaIntegrataManualeService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaPrimaNotaIntegrataManuale() {
		RicercaSinteticaPrimaNotaIntegrataManuale req = new RicercaSinteticaPrimaNotaIntegrataManuale();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setBilancio(getBilancioTest(131, 2017));
		req.getEventi().add(create(Evento.class, 4721));
		req.getEventi().add(create(Evento.class, 4720));
		req.getEventi().add(create(Evento.class, 4719));
		
		req.setEntita(create(Impegno.class, 51348));

		req.setPrimaNota(create(PrimaNota.class, 0));
		req.getPrimaNota().setTipoCausale(TipoCausale.Integrata);
		
		RicercaSinteticaPrimaNotaIntegrataManualeResponse res = ricercaSinteticaPrimaNotaIntegrataManualeService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaPrimaNotaIntegrataManuale() {
		Richiedente richiedente = getRichiedenteByProperties("consip", "regp");
		
		PrimaNota pnota;
//		pnota = ottieniPrimaNotaDaServizio(richiedente,create(PrimaNota.class, 24091));
		pnota = getTestFileObject(PrimaNota.class, "coge/primaNota-SIAC-6195.xml");
		
		AggiornaPrimaNotaIntegrataManuale req = new AggiornaPrimaNotaIntegrataManuale();
		
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		
		req.setPrimaNota(pnota);
//		req.getPrimaNota().setDescrizione("Test via JUnit, aggiornato");
		
		AggiornaPrimaNotaIntegrataManualeResponse res = aggiornaPrimaNotaIntegrataManualeService.executeService(req);
		assertNotNull(res);
	}

	
	/**
	 * @param richiedente
	 * @return
	 */
	private PrimaNota ottieniPrimaNotaDaServizio(Richiedente richiedente, PrimaNota pnotaInput) {
		RicercaDettaglioPrimaNota req1 = new RicercaDettaglioPrimaNota();
		req1.setDataOra(new Date());
		req1.setRichiedente(richiedente);
		req1.setPrimaNota(pnotaInput);
		
		RicercaDettaglioPrimaNotaResponse res1 = ricercaDettaglioPrimaNotaService.executeService(req1);
		PrimaNota pnota  = res1.getPrimaNota();
		return pnota;
	}
	
	@Test
	public void aggiornaPrimaNotaGEN() {
		AggiornaPrimaNota req = new AggiornaPrimaNota();
		Bilancio bilancio = getBilancioTest(131, 2017);
		
		req.setAnnoBilancio(bilancio.getAnno());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setPrimaNota(create(PrimaNota.class, 11134));
		req.getPrimaNota().setAmbito(Ambito.AMBITO_FIN);
		req.getPrimaNota().setBilancio(bilancio);
		req.getPrimaNota().setDataRegistrazione(parseDate("03/04/2017"));
		req.getPrimaNota().setDescrizione("Liq 60949 CORSI TRIENNALI DI FORMAZIONE SPECFICA IN MEDICINA GENERALE. D.LVO 17 AGOSTO 1999 N. 368 MODIFICATO ED INTEGRATO DAL D.LVO 277/2003 - ACCERTAMENTO E CONTESTUALE IMPEGNO DI EURO 250.000,00, RISPETTIVAMENTE SUI CAPITOLI 20590 DELL'ENTRATA E 129155 DELLA SPESA DEL BILANCIO PER L'ESERCIZIO FINANZIARIO 2015 - ASSEGNAZIONE N. 100306.");
		req.getPrimaNota().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getPrimaNota().setIsCollegataAMovimentoResiduo(Boolean.FALSE);
		req.getPrimaNota().setNumero(11134);
		req.getPrimaNota().setSoggetto(create(Soggetto.class, 126485));
		req.getPrimaNota().setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		req.getPrimaNota().setTipoCausale(TipoCausale.Integrata);
		
		MovimentoEP mep = create(MovimentoEP.class, 0);
		req.getPrimaNota().getListaMovimentiEP().add(mep);
		
		mep.setAmbito(Ambito.AMBITO_FIN);
		mep.setCausaleEP(create(CausaleEP.class, 305382));
		mep.setRegistrazioneMovFin(create(RegistrazioneMovFin.class, 19928));
		
		MovimentoDettaglio md1 = create(MovimentoDettaglio.class, 22437);
		MovimentoDettaglio md2 = create(MovimentoDettaglio.class, 0);
		mep.getListaMovimentoDettaglio().add(md1);
		mep.getListaMovimentoDettaglio().add(md2);
		
		md1.setAmbito(Ambito.AMBITO_FIN);
		md1.setConto(create(Conto.class, 66032));
		md1.setEnte(req.getRichiedente().getAccount().getEnte());
		md1.setImporto(new BigDecimal("877"));
		md1.setMovimentoEP(create(MovimentoEP.class, 11219));
		md1.setSegno(OperazioneSegnoConto.AVERE);
		md1.setNumeroRiga(0);
		
		md2.setAmbito(Ambito.AMBITO_FIN);
		md2.setConto(create(Conto.class, 66032));
		md2.setEnte(req.getRichiedente().getAccount().getEnte());
		md2.setImporto(new BigDecimal("877.00"));
		//md2.setMovimentoEP(create(MovimentoEP.class, 11219));
		md2.setSegno(OperazioneSegnoConto.DARE);
		md2.setNumeroRiga(1);
		
		AggiornaPrimaNotaResponse res = aggiornaPrimaNotaGENService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaPrimaNotaGENDaServizio() {
		Bilancio bilancio = getBilancioTest(131, 2017);
		Richiedente richiedente = getRichiedenteByProperties("consip", "regp");
		PrimaNota pnota = ottieniPrimaNotaDaServizio(richiedente,create(PrimaNota.class, 24096));
		
		AggiornaPrimaNota req = new AggiornaPrimaNota();
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		
		req.setPrimaNota(pnota);
		req.getPrimaNota().setDescrizione("Test via JUnit, aggiornato");
		
		
		AggiornaPrimaNotaResponse res = aggiornaPrimaNotaGENService.executeService(req);
		assertNotNull(res);
	}
}
