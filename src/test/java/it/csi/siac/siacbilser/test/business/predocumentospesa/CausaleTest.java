/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.predocumentospesa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.predocumentoentrata.DettaglioStoricoCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.InserisceCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaDettaglioCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaSinteticaCausaleEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AggiornaCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AnnullaCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DettaglioStoricoCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.InserisceCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaDettaglioCausaleSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaSinteticaCausaleSpesaService;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class CausaleDLTest.
 */
public class CausaleTest extends BaseJunit4TestCase {
	
	/** The inserisce causale spesa service. */
	@Autowired
	private InserisceCausaleSpesaService inserisceCausaleSpesaService;
	
	/** The inserisce causale entrata service. */
	@Autowired
	private InserisceCausaleEntrataService inserisceCausaleEntrataService;
	
	/** The ricerca dettaglio causale spesa service. */
	@Autowired
	private RicercaDettaglioCausaleSpesaService ricercaDettaglioCausaleSpesaService;
	
	/** The ricerca dettaglio causale entrta service. */
	@Autowired
	private RicercaDettaglioCausaleEntrataService ricercaDettaglioCausaleEntrtaService;
	
	/** The ricerca sintetica causale spesa service. */
	@Autowired
	private RicercaSinteticaCausaleSpesaService ricercaSinteticaCausaleSpesaService;
	
	/** The ricerca sintetica causale entrata service. */
	@Autowired
	private RicercaSinteticaCausaleEntrataService ricercaSinteticaCausaleEntrataService;
	
	/** The dettaglio storico causale spesa service. */
	@Autowired
	private DettaglioStoricoCausaleSpesaService dettaglioStoricoCausaleSpesaService;
	
	/** The aggiorna causale spesa service. */
	@Autowired
	private AggiornaCausaleSpesaService aggiornaCausaleSpesaService;
	
	/** The annulla causale spesa service. */
	@Autowired
	private AnnullaCausaleSpesaService annullaCausaleSpesaService;

	/** The dettaglio storico causale entrata service. */
	@Autowired
	private DettaglioStoricoCausaleEntrataService dettaglioStoricoCausaleEntrataService;
		
	
	/**
	 * Inserisci causale spesa.
	 */
	@Test
	public void inserisciCausaleSpesa() {
		InserisceCausaleSpesa req = new InserisceCausaleSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		CausaleSpesa causale = new CausaleSpesa();	
		
		causale.setEnte(getEnteTest());
		
		TipoCausale tipoCausale = new TipoCausale();
		tipoCausale.setUid(1);
		causale.setTipoCausale(tipoCausale);
		
		causale.setCodice("prova PROVA");
		causale.setDescrizione("DESCRIZIONE CAUSALE PROVA");
				
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		causale.setSoggetto(soggetto);	
		
		SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
		sedeSecondariaSoggetto.setUid(24);
		causale.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(9);
		causale.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		
//		Impegno impegno = new Impegno();
//		impegno.setUid(3);
//		impegno.setAnnoMovimento(2013);
//		impegno.setNumero(new BigDecimal("3"));
//		causale.setImpegno(impegno);
		
		SubImpegno subImpegno = new SubImpegno();
		subImpegno.setUid(4);
		causale.setSubImpegno(subImpegno);
		
		
		req = marshallUnmarshall(req);
		
		req.setCausaleSpesa(causale);
		
		InserisceCausaleSpesaResponse res = inserisceCausaleSpesaService.executeService(req);
		
		res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	/**
	 * Inserisci causale entrata.
	 */
	@Test
	public void inserisciCausaleEntrata() {
		InserisceCausaleEntrata req = new InserisceCausaleEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		CausaleEntrata causale = new CausaleEntrata();	
		
		causale.setEnte(getEnteTest());
		
		TipoCausale tipoCausale = new TipoCausale();
		tipoCausale.setUid(5);
		causale.setTipoCausale(tipoCausale);
		
		causale.setCodice("prova PROVA");
		causale.setDescrizione("DESCRIZIONE CAUSALE PROVA");
				
		
		
//		SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
//		SedeSecondariaSoggetto.setUid(24);
//		soggetto.setS
		
//		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
//		capitoloEntrataGestione.setUid(9);
//		causale.setCapitoloEntrataGestione(capitoloEntrataGestione);
		
		
//		Impegno impegno = new Impegno();
//		impegno.setUid(3);
//		impegno.setAnnoMovimento(2013);
//		impegno.setNumero(new BigDecimal("3"));
//		causale.setImpegno(impegno);
		
//		SubImpegno subImpegno = new SubImpegno();
//		subImpegno.setUid(4);
//		causale.setSubImpegno(subImpegno);
		
		
		req = marshallUnmarshall(req);
		
		req.setCausaleEntrata(causale);
		
		InserisceCausaleEntrataResponse res = inserisceCausaleEntrataService.executeService(req);
		
		res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	
	/**
	 * Aggiorna causale spesa.
	 */
	@Test
	public void aggiornaCausaleSpesa() {
		AggiornaCausaleSpesa req = new AggiornaCausaleSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		CausaleSpesa causale = new CausaleSpesa();	
		
		causale.setUid(37);
		causale.setEnte(getEnteTest());
		
		TipoCausale tipoCausale = new TipoCausale();
		tipoCausale.setUid(5);
		causale.setTipoCausale(tipoCausale);
		
		causale.setCodice("MIO CODICE CAUSALE aggiornato");
		causale.setDescrizione("MIO DESCRIZIONE CAUSALE aggiornata");
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(17);
		causale.setSoggetto(soggetto);	
		
		SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
		sedeSecondariaSoggetto.setUid(24);
		causale.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		
//		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
//		capitoloUscitaGestione.setUid(9);
//		causale.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		SubImpegno subImpegno = new SubImpegno();
		subImpegno.setUid(4);
		causale.setSubImpegno(subImpegno);
		
		req.setCausaleSpesa(causale);
		
		AggiornaCausaleSpesaResponse res = aggiornaCausaleSpesaService.executeService(req);
		
		res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	
	

	/**
	 * Ricerca dettaglio causale spesa.
	 */
	@Test
	public void ricercaDettaglioCausaleSpesa() {
		RicercaDettaglioCausaleSpesa req = new RicercaDettaglioCausaleSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		CausaleSpesa causale = new CausaleSpesa();	
		causale.setUid(37);
		
		
		req = marshallUnmarshall(req);
		
		req.setCausaleSpesa(causale);
		
		RicercaDettaglioCausaleSpesaResponse res = ricercaDettaglioCausaleSpesaService.executeService(req);
		
		res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	/**
	 * Ricerca dettaglio causale entrata.
	 */
	@Test
	public void ricercaDettaglioCausaleEntrata() {
		RicercaDettaglioCausaleEntrata req = new RicercaDettaglioCausaleEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		CausaleEntrata causale = new CausaleEntrata();	
		causale.setUid(38);
		
		
		req = marshallUnmarshall(req);
		
		req.setCausaleEntrata(causale);
		
		RicercaDettaglioCausaleEntrataResponse res = ricercaDettaglioCausaleEntrtaService.executeService(req);
		
		res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	
	/**
	 * Ricerca sintetica causale spesa.
	 */
	@Test
	public void ricercaSinteticaCausaleSpesa() {
		RicercaSinteticaCausaleSpesa req = new RicercaSinteticaCausaleSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		CausaleSpesa causale = new CausaleSpesa();	
		
		causale.setEnte(getEnteTest());
		
		//causale.setUid(1);
		
//		TipoCausale tipoCausale = new TipoCausale();
//		tipoCausale.setUid(5);
//		causale.setTipoCausale(tipoCausale);
		
		//causale.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		//causale.setStatoOperativoCausale(StatoOperativoCausale.ANNULLATA);
//				
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(7);
//		causale.setSoggetto(soggetto);
////		
//		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
//		capitoloUscitaGestione.setUid(9);
//		causale.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
//		
//		Impegno impegno = new Impegno();
//		impegno.setUid(3);
//		causale.setImpegno(impegno);
		

//		StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
//		struttura.setUid(502);
//		causale.setStrutturaAmministrativoContabile(struttura);
////		
//		AttoAmministrativo atto = new AttoAmministrativo();
//		atto.setUid(1);
//		causale.setAttoAmministrativo(atto);
////		
//		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = new ModalitaPagamentoSoggetto();
//		modalitaPagamentoSoggetto.setUid(4);
//		causale.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		
//		
//		SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
//		sedeSecondariaSoggetto.setUid(24);
//		causale.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(9);
		causale.setSoggetto(soggetto);
		
		//req = marshallUnmarshall(req);
		
		req.setCausaleSpesa(causale);
		
		RicercaSinteticaCausaleSpesaResponse res = ricercaSinteticaCausaleSpesaService.executeService(req);
		
		//res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	
	/**
	 * Ricerca sintetica causale entrata.
	 */
	@Test
	public void ricercaSinteticaCausaleEntrata() {
		RicercaSinteticaCausaleEntrata req = new RicercaSinteticaCausaleEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		CausaleEntrata causale = new CausaleEntrata();	
		
		causale.setEnte(getEnteTest());
		
//		causale.setUid(1);
//		
//		TipoCausale tipoCausale = new TipoCausale();
//		tipoCausale.setUid(1);
//		causale.setTipoCausale(tipoCausale);
//		
//		causale.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
//				
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(17);
		causale.setSoggetto(soggetto);
		
//		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
//		capitoloEntrataGestione.setUid(9);
//		causale.setCapitoloEntrataGestione(capitoloEntrataGestione);
//		
////		
//		Accertamento accertamento = new Accertamento();
//		accertamento.setUid(3);
//		accertamento.setAnnoMovimento(2013);
//		accertamento.setNumero(new BigDecimal("3"));
//		causale.setAccertamento(accertamento);
//		
//		SubAccertamento subAccertamento = new SubAccertamento();
//		subAccertamento.setUid(4);
//		causale.setSubAccertamento(subAccertamento);
//		
//		StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
//		struttura.setUid(1);
//		causale.setStrutturaAmministrativoContabile(struttura);
////		
//		AttoAmministrativo atto = new AttoAmministrativo();
//		atto.setUid(1);
//		causale.setAttoAmministrativo(atto);
//		
//		
//		req = marshallUnmarshall(req);
		
		req.setCausaleEntrata(causale);
		
		RicercaSinteticaCausaleEntrataResponse res = ricercaSinteticaCausaleEntrataService.executeService(req);
		
//		res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	/**
	 * Dettaglio storico causale spesa.
	 */
	@Test
	public void dettaglioStoricoCausaleSpesa(){
		
		DettaglioStoricoCausaleSpesa req = new DettaglioStoricoCausaleSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		CausaleSpesa causale = new CausaleSpesa();
		causale.setEnte(getEnteTest());
		causale.setUid(24);
		
		req.setCausale(causale);
		
		DettaglioStoricoCausaleSpesaResponse res = dettaglioStoricoCausaleSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	/**
	 * Dettaglio storico causale entrata.
	 */
	@Test
	public void dettaglioStoricoCausaleEntrata(){
		
		DettaglioStoricoCausaleEntrata req = new DettaglioStoricoCausaleEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		CausaleEntrata causale = new CausaleEntrata();
		causale.setEnte(getEnteTest());
		causale.setUid(38);
		
		req.setCausale(causale);
		
		DettaglioStoricoCausaleEntrataResponse res = dettaglioStoricoCausaleEntrataService.executeService(req);
		
		assertNotNull(res);
	}
	
	/**
	 * Annulla causale spesa.
	 */
	@Test
	public void annullaCausaleSpesa(){
		
		AnnullaCausaleSpesa req = new AnnullaCausaleSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		CausaleSpesa causale = new CausaleSpesa();
		causale.setEnte(getEnteTest());
		causale.setUid(35);
		
		req.setCausaleSpesa(causale);
		
		AnnullaCausaleSpesaResponse res = annullaCausaleSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	
}
