/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.AggiornaTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.DettaglioStoricoTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.InserisceTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaDettaglioTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaSinteticaTipoOnereService;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ModelloCausale;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfin2ser.model.TipoOnereModelDetail;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class OnereSpesaDLTest.
 */
public class TipoOnereTest extends BaseJunit4TestCase {
	
	
	/** The inserisci onere spesa service. */
	@Autowired
	private InserisceTipoOnereService inserisceTipoOnereService;

	@Autowired
	private RicercaSinteticaTipoOnereService ricercaSinteticaTipoOnereService;
	
	@Autowired
	private AggiornaTipoOnereService aggiornaTipoOnereService;
	
	@Autowired
	private RicercaDettaglioTipoOnereService ricercaDettaglioTipoOnereService;
	
	@Autowired
	private DettaglioStoricoTipoOnereService dettaglioStoricoTipoOnereService;
	
	@Autowired
	private SoggettoDad soggettoDad;
	
	@Autowired
	private MovimentoGestioneService movimentoGestioneService;
	
	
	/**
	 * Inserisci tipo onere
	 */
	@Test
	public void inserisciTipoOnere() {
			
		InserisceTipoOnere req = new InserisceTipoOnere();
		
		TipoOnere tipoOnere = new TipoOnere();
		
//		tipoOnere.setAliquotaCaricoEnte(new BigDecimal("20"));
//		tipoOnere.setAliquotaCaricoSoggetto(new BigDecimal("10"));
		tipoOnere.setCodice("SI");
		tipoOnere.setDescrizione("SPLIT ISTITUZIONALE");
		tipoOnere.setEnte(getEnteTest());
		tipoOnere.setQuadro770("Quadro");
		tipoOnere.setTipoIvaSplitReverse(TipoIvaSplitReverse.SPLIT_ISTITUZIONALE);
		
		NaturaOnere naturaOnere = new NaturaOnere();
		naturaOnere.setUid(29);
		tipoOnere.setNaturaOnere(naturaOnere);
		
		List<Causale> causali = new ArrayList<Causale>();
		Causale770 causale1 = new Causale770();
		causale1.setUid(1);
		causali.add(causale1);
		Causale770 causale2 = new Causale770();
		causale2.setUid(6);
		causali.add(causale2);
		tipoOnere.setCausali(causali);
		
		List<AttivitaOnere> attivitaOnere = new ArrayList<AttivitaOnere>();
		AttivitaOnere attivita1 = new AttivitaOnere();
		attivita1.setUid(2);
		attivitaOnere.add(attivita1);
		AttivitaOnere attivita2 = new AttivitaOnere();
		attivita2.setUid(8);
		attivitaOnere.add(attivita2);
		tipoOnere.setAttivitaOnere(attivitaOnere);
		
		req.setTipoOnere(tipoOnere);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		InserisceTipoOnereResponse res = inserisceTipoOnereService.executeService(req);

		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaSinteticaTipoOnere() {
		
		RicercaSinteticaTipoOnere req = new RicercaSinteticaTipoOnere();
		
		TipoOnere tipoOnere = new TipoOnere();
		NaturaOnere naturaOnere = new NaturaOnere();
		naturaOnere.setUid(29);
		tipoOnere.setNaturaOnere(naturaOnere);
		tipoOnere.setEnte(getEnteTest());
		
		req.setCorsoDiValidita(Boolean.TRUE);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoOnere(tipoOnere);
		
		RicercaSinteticaTipoOnereResponse res = ricercaSinteticaTipoOnereService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaDettaglioTipoOnere() {
		
		RicercaDettaglioTipoOnere req = new RicercaDettaglioTipoOnere();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(166);
		req.setTipoOnere(tipoOnere);
		
		req.setTipoOnereModelDetails(TipoOnereModelDetail.values());
		
		RicercaDettaglioTipoOnereResponse res = ricercaDettaglioTipoOnereService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void aggiornaTipoOnere() {
			
		AggiornaTipoOnere req = new AggiornaTipoOnere();
		req.setDataOra(new Date());
		req.setAnnoEsercizio(2015);
		
		TipoOnere tipoOnere = new TipoOnere();
		
		tipoOnere.setUid(159);
		
		tipoOnere.setAliquotaCaricoEnte(new BigDecimal("10.00"));
		//tipoOnere.setAliquotaCaricoSoggetto(new BigDecimal("40"));
		tipoOnere.setCodice("E-Acc");
		tipoOnere.setDescrizione("onere collegato con un accertamento");
		tipoOnere.setEnte(getEnteTest());
		tipoOnere.setQuadro770("Quadro");
		
		NaturaOnere naturaOnere = new NaturaOnere();
		naturaOnere.setUid(2);
		tipoOnere.setNaturaOnere(naturaOnere);
		
		List<Causale> causali = new ArrayList<Causale>();
		Causale770 causale1 = new Causale770();
		causale1.setUid(1);
		causale1.setCodice("01");
		causale1.setDescrizione("CAUSALE 1");
		causali.add(causale1);
		
		
	
		CausaleEntrata caus = new CausaleEntrata();
		caus.setUid(277);
		caus.setCodice("CAUS_E_E-Acc_1");
		caus.setDescrizione("Causale entrata per accertamento 2015/42");
		Calendar instance = Calendar.getInstance();
		instance.set(2017, Calendar.JULY, 2512, 17,35);
		caus.setDataCreazione(instance.getTime());
		caus.setDataInizioValiditaCausale(instance.getTime());
		caus.setDataModificaCausale(instance.getTime());
		caus.setModelloCausale(ModelloCausale.ONERI);
		caus.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		caus.setEnte(getEnteTest());
		
		TipoCausale tc = new TipoCausale();
		tc.setUid(21);
		tc.setCodice("RIT");
		caus.setTipoCausale(tc);
		
		Accertamento acc = new Accertamento();
		acc.setUid(95425);
		acc.setAnnoMovimento(2015);
		acc.setNumeroBigDecimal(BigDecimal.valueOf(42));
		caus.setAccertamento(acc);
		
		Distinta distinta = new Distinta();
		distinta.setUid(2);
		caus.setDistinta(distinta);
		
		CapitoloEntrataGestione ceg = new CapitoloEntrataGestione();
		ceg.setUid(18085);
		acc.setCapitoloEntrataGestione(ceg);
		
		tipoOnere.addCausale(caus);
		
		List<AttivitaOnere> attivitaOnere = new ArrayList<AttivitaOnere>();
		AttivitaOnere attivita1 = new AttivitaOnere();
		attivita1.setUid(2);
		attivitaOnere.add(attivita1);
		AttivitaOnere attivita2 = new AttivitaOnere();
		attivita2.setUid(8);
		attivitaOnere.add(attivita2);
		tipoOnere.setAttivitaOnere(attivitaOnere);
		
		req.setTipoOnere(tipoOnere);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		AggiornaTipoOnereResponse res = aggiornaTipoOnereService.executeService(req);

		assertNotNull(res);
	}
	
	
	@Test
	public void dettaglioStoricoTipoOnere(){
		final String methodName="AAAAAA";
		DettaglioStoricoTipoOnere req = new DettaglioStoricoTipoOnere();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		TipoOnere tipoOnere = new TipoOnere();		
		tipoOnere.setUid(168);
		
		req.setTipoOnere(tipoOnere);
		
		DettaglioStoricoTipoOnereResponse res = dettaglioStoricoTipoOnereService.executeService(req);
		assertNotNull(res);
		List<TipoOnere> tipiOnere = res.getTipiOnere();
		List<CausaleEntrata> causaliEntrata = res.getCausaliEntrata();
		List<CausaleSpesa> causaliSpesa = res.getCausaliSpesa();
		List<Soggetto> soggetti = res.getSoggetti();
		TipoOnere tipoOnereTrovato = new TipoOnere();
		
		// Injetto il primo tipo onere
		for(TipoOnere tOn : res.getTipiOnereStorico()) {
			if(tOn.getDataFineValidita() == null) {
				tipoOnereTrovato = tipoOnere;
				break;
			}
		}
		
		for (CausaleEntrata caus : causaliEntrata) {
			String ss = "uidCausale " + caus.getUid() + 
					" con validita inizio causale: " + caus.getDataInizioValiditaCausale()+ 
					" con validita fine causale: " +caus.getDataFineValiditaCausale() + 
					" e validita inizio: " + caus.getDataInizioValidita() + 
					" con validita fine; " +caus.getDataFineValidita();
			log.debug(methodName, ss);
		}
		
		
	}
	
	
	@Test
	public void findClassiSoggetto(){
		List<ClasseSoggetto> classi = soggettoDad.findClasseSoggetto(9);
		for(ClasseSoggetto classe : classi){
			log.debug("findClassiSoggetto", "uid classe: " + classe.getUid());
		}
		
	}
	
	@Test
	public void ricercaImpegnoPerChiave(){
		RicercaImpegnoPerChiave reqRIPC = new RicercaImpegnoPerChiave();
		reqRIPC.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRIPC.setEnte(getEnteTest());
		
		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		pRicercaImpegnoK.setAnnoEsercizio(2014);
		pRicercaImpegnoK.setAnnoImpegno(2014);
		pRicercaImpegnoK.setNumeroImpegno(new BigDecimal(1));
		
		reqRIPC.setpRicercaImpegnoK(pRicercaImpegnoK);
		log.logXmlTypeObject(reqRIPC, "Request del servizio RicercaImpegnoPerChiave.");
		RicercaImpegnoPerChiaveResponse resRIPC = movimentoGestioneService.ricercaImpegnoPerChiave(reqRIPC);
		log.logXmlTypeObject(resRIPC, "Risposta ottenuta dal servizio RicercaImpegnoPerChiave.");
		
	}
	

	
	


}
