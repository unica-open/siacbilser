/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cespite;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cespiti.AggiornaCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.EliminaCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaCespiteDaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaCespitePerChiaveService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaDettaglioCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaScrittureInventarioByEntitaCollegataService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaRegistroACespiteService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.utility.cespite.CespiteInventarioWrapper;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNota;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNotaResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiave;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiaveResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegata;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegataResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

public class CespitiTest extends BaseJunit4TestCase {

	@Autowired private InserisciCespiteService inserisciCespiteService;
	@Autowired private AggiornaCespiteService aggiornaCespiteService;
	@Autowired private RicercaDettaglioCespiteService ricercaDettaglioCespiteService;
	@Autowired private RicercaSinteticaCespiteService ricercaSinteticaCespiteService;
	@Autowired private EliminaCespiteService eliminaCespiteService;
	@Autowired private RicercaCespitePerChiaveService ricercaCespitePerChiaveService;
	@Autowired private RicercaCespiteDaPrimaNotaService ricercaCespiteDaPrimaNotaService;
	
	@Autowired private RicercaScrittureInventarioByEntitaCollegataService ricercaSinteticaScrittureCespiteService;
	@Autowired private RicercaSinteticaRegistroACespiteService ricercaSinteticaRegistroACespiteService;

	@Autowired private CespiteDad cespiteDad;
	
	@Test
	public void inserisciCespiti(){
		InserisciCespite req = new InserisciCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));

		Cespite cespite = new Cespite();

		cespite.setCodice("11M");
		cespite.setDescrizione("lavandino");

		cespite.setValoreIniziale(new BigDecimal(300));
		cespite.setDataAccessoInventario(new Date());
		cespite.setFlagStatoBene(Boolean.TRUE);

		TipoBeneCespite tb = create(TipoBeneCespite.class,44);
		cespite.setTipoBeneCespite(tb);
		cespite.setClassificazioneGiuridicaCespite(ClassificazioneGiuridicaCespite.CES_BENE_DISPONIBILE);
		cespite.setFlgDonazioneRinvenimento(Boolean.TRUE);
		req.setCespite(cespite );

		InserisciCespiteResponse res = inserisciCespiteService.executeService(req);
		assertNotNull(res);
		System.out.println("uid:" + 
		res.getCespite() .getUid());
	}
	
	@Test
	public void aggiornaCespiti(){
		
		RicercaDettaglioCespiteResponse ricercaDettaglio = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		AggiornaCespite req = new AggiornaCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));		
		Cespite cespite = ricercaDettaglio.getCespite();
		cespite.setValoreIniziale(new BigDecimal("30"));
//		cespite.setCodice("nuovo codice aggiornato 2");
//		cespite.setTipoBeneCespite(create(TipoBeneCespite.class,21));
		req.setCespite(cespite);
		AggiornaCespiteResponse res = aggiornaCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioCespiti(){
		RicercaDettaglioCespiteResponse res = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaCespiti() {
		RicercaSinteticaCespite req = creaRequestManuale();
//		RicercaSinteticaCespite req = creaRequestByXml();
		
//		
		RicercaSinteticaCespiteResponse response = ricercaSinteticaCespiteService.executeService(req);
		assertNotNull(response);
	}

	private RicercaSinteticaCespite creaRequestByXml() {
		return getTestFileObject(RicercaSinteticaCespite.class, "cespiti/cespite/ricercaSinteticaCespite.xml");	
	}

	/**
	 * @return
	 */
	private RicercaSinteticaCespite creaRequestManuale() {
		RicercaSinteticaCespite req = new RicercaSinteticaCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2019));
		//cespite,req.getTipoBeneCespite(), req.getClassificazioneGiuridicaCespite()
		Cespite ces = new Cespite();
		ces.setCodice("1009");
//		ces.setCodice("002");
//		ces.setFlagSoggettoTutelaBeniCulturali(true);
//		ces.setFlgDonazioneRinvenimento(null);
//		ces.setNumeroInventario("001");
//		ces.setDataAccessoInventario(new Date());
//		ces.setUbicazione("otto");
//		ces.setFlagStatoBene(Boolean.TRUE);
//		ces.setDataCessazione(parseDate("23/08/2018"));
//		req.setCespite(ces);
//		req.setDismissioneCespite(create(DismissioneCespite.class,15));
//		TipoBeneCespite tbc = new TipoBeneCespite();
//		Conto cp = new Conto();
//		cp.setCodice("1.1.2.01.01.03.001");
//		tbc.setContoPatrimoniale(cp);
//		req.setTipoBeneCespite(tbc);
//		req.setEscludiCespitiCollegatiADismissione(Boolean.TRUE);
//		req.setConPianoAmmortamentoCompleto(Boolean.FALSE);
//		req.setMassimoAnnoAmmortato(2019);
//		req.setCategoriaCespiti(create(CategoriaCespiti.class, 1013));
//		AnteprimaAmmortamentoAnnuoCespite aa = new AnteprimaAmmortamentoAnnuoCespite();
//		aa.setAnno(2019);
//		req.setAnteprimaAmmortamentoAnnuoCespite(aa);
//		req.setAnnoDettaglioAmmortamentoFiltro(Integer.valueOf(2045));
//		req.setDettaglioAnteprimaAmmortamentoAnnuoCespite(create(DettaglioAnteprimaAmmortamentoAnnuoCespite.class, 43));
//		req.getDettaglioAnteprimaAmmortamentoAnnuoCespite().setSegno(OperazioneSegnoConto.DARE);		
//		req.setMovimentoDettaglio(create(MovimentoDettaglio.class, 50302));
		req.setModelDetails(CespiteModelDetail.IsInserimentoContestualeRegistroA, CespiteModelDetail.ImportoSuRegistroA);
//		req.setModelDetails(CespiteModelDetail.ClassificazioneGiuridicaCespite, CespiteModelDetail.TipoBeneCespite);
		// tiro su solo la categoria
//		req.setModelDetails(CespiteModelDetail.AmmortamentoAnnuoCespiteModelDetail,
//				AmmortamentoAnnuoCespiteModelDetail.DettaglioAmmortamentoAnnuoCespiteAnnoSpecificoModelDetail
//								
//				CespiteModelDetail.ResiduoAmmortamento
//				);

		req.setCespite(ces);
		
		TipoBeneCespite tb = create(TipoBeneCespite.class, 91) ;
		req.setTipoBeneCespite(tb);
		req.getCespite().setTipoBeneCespite(tb);
//		req.setClassificazioneGiuridicaCespite(ClassificazioneGiuridicaCespite.CES_BENE_DISPONIBILE);

//		DismissioniCespite dismissioneCespite = new DismissioniCespite();
//		dismissioneCespite.setDataCessazione(new Date());
//		req.setDismissioniCespite(dismissioneCespite );
		
//		req.setNumeroInventarioDa(Integer.valueOf(7));
//		req.setNumeroInventarioA(Integer.valueOf(11));
		
		req.setMassimoAnnoAmmortato(2019);
		req.setConPianoAmmortamentoCompleto(false);
		
//		req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail,
//				CespiteModelDetail.ClassificazioneGiuridicaCespite,
//				TipoBeneCespiteModelDetail.Annullato);

		req.setParametriPaginazione(getParametriPaginazioneTest());
		return req;
	}

	/**
	 * @return
	 */
	private RicercaDettaglioCespiteResponse ottieniResponseRicercaDettaglio(Richiedente richiedente) {
		RicercaDettaglioCespite req = new RicercaDettaglioCespite();
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		req.setAnnoBilancio(new Integer(2017));
		req.setCespite(create(Cespite.class, 37));
		RicercaDettaglioCespiteResponse res = ricercaDettaglioCespiteService.executeService(req);
		return res;
	}
	
	@Test
	public void eliminaCespitiTest() {
		EliminaCespite req = new EliminaCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		Cespite cat = new Cespite();
		cat.setUid(37);
		req.setCespite(cat);
		EliminaCespiteResponse res = eliminaCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void staccaNumeroACaso() {
		cespiteDad.setEnte(create(Ente.class, 15));
		cespiteDad.setLoginOperazione("admin_junit");
		CespiteInventarioWrapper staccaNumeroInventario = cespiteDad.staccaNumeroInventario();
		System.out.println("staccato numero inventario: " + staccaNumeroInventario.getNumeroInventario());
	}
	
	@Test
	public void testCespite() {
		Long numeroCollegamentiDelCespiteAPrimeNote = cespiteDad.contaCespiticollegatiAPrimeNoteCogeInvDaAccettare(45);
		System.out.println("numeroCollegamentiDelCespiteAPrimeNote "  + numeroCollegamentiDelCespiteAPrimeNote);
		
	}
	
	@Test
	public void testImportosuRegistroA() {
		BigDecimal numeroCollegamentiDelCespiteAPrimeNote = cespiteDad.getImportosuRegistroA(create(Cespite.class,  71), create(MovimentoDettaglio.class, 50302));
		System.out.println("numeroCollegamentiDelCespiteAPrimeNote "  + numeroCollegamentiDelCespiteAPrimeNote);
		
	}
	
	@Test
	public void collegaPrimaNotaAlienazione() {
		cespiteDad.setEnte(create(Ente.class, 15));
		cespiteDad.setLoginOperazione("admin_junit");
		cespiteDad.associaPrimaNotaAlienzazioneACespite(create(MovimentoDettaglio.class, 24984), create(Cespite.class, 16),create(PrimaNota.class, 24636));
	}
	
	@Test
	public void ricercaCespitePerChiave() {
		RicercaCespitePerChiave req = new RicercaCespitePerChiave();
		
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setCespite(new Cespite());
		req.getCespite().setNumeroInventario("11");
		
		req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail,
				CespiteModelDetail.ClassificazioneGiuridicaCespite);
				//CespiteModelDetail.DismissioniCespite);
		
		RicercaCespitePerChiaveResponse res = ricercaCespitePerChiaveService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * 
	 */
	@Test
	public void ricercaScrittureCespite() {
		RicercaScrittureInventarioByEntitaCollegata req = new RicercaScrittureInventarioByEntitaCollegata();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2017));

//		req.setEntitaGeneranteScritture(create(Cespite.class, 36));
//		req.setEntitaGeneranteScritture(create(DettaglioAmmortamentoAnnuoCespite.class, 36));
		
//		req.setEntitaGeneranteScritture(create(VariazioneCespite.class, 15));
//		req.setEntitaGeneranteScritture(create(PrimaNota.class, 24640));
		
//		req.setCespiteCollegatoAdEntitaGenerante(create(Cespite.class,54));
		req.setEntitaGeneranteScritture(create(PrimaNota.class, 24640));
		req.setEntitaGeneranteScritture(create(DismissioneCespite.class, 19));
//		req.setEntitaGeneranteScritture(create(DettaglioAmmortamentoAnnuoCespite.class,83));
		req.setModelDetails(new ModelDetail[] {
//				PrimaNotaModelDetail.MovimentiEpModelDetail, 
				PrimaNotaModelDetail.StatoOperativo, 
				PrimaNotaModelDetail.StatoAccettazionePrimaNotaProvvisoria,
//				PrimaNotaModelDetail.MovimentiEpModelDetail,
//				MovimentoEPModelDetail.MovimentoDettaglioModelDetail,
//				MovimentoDettaglioModelDetail.Conto, 
//				MovimentoDettaglioModelDetail.Segno				
		});
//		PrimaNotaModelDetail[]  a = new PrimaNotaModelDetail[] {};
		
		RicercaScrittureInventarioByEntitaCollegataResponse response = ricercaSinteticaScrittureCespiteService.executeService(req);
		assertNotNull(response);
	}

	@Test
	public void ricercaSinteticaRegistroACespite() {
		RicercaSinteticaRegistroACespite req = new RicercaSinteticaRegistroACespite();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		
		req.setPrimaNota(create(PrimaNota.class, 0));
		req.getPrimaNota().setTipoCausale(TipoCausale.Libera);
		req.getPrimaNota().setStatoOperativoPrimaNota(StatoOperativoPrimaNota.DEFINITIVO);
//		req.setTipoElenco("S");
//		req.setTipoEvento(create(TipoEvento.class,38));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setModelDetails(
				// Conto INV
//				PrimaNotaModelDetail.ContoInventario,
				// Stato CoGe
//				PrimaNotaModelDetail.StatoOperativo,
				// Stato INV
				PrimaNotaModelDetail.PrimaNotaInventario
				);
		
		RicercaSinteticaRegistroACespiteResponse res = ricercaSinteticaRegistroACespiteService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void ricercaCespiteDaPrimaNota() {
		RicercaCespiteDaPrimaNota req = new RicercaCespiteDaPrimaNota();
		
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		PrimaNota primaNota = new PrimaNota();
		primaNota.setUid(115619);
		req.setPrimaNota(primaNota );
		
		//req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail,CespiteModelDetail.ClassificazioneGiuridicaCespite);
				//CespiteModelDetail.DismissioniCespite);
		
		RicercaCespiteDaPrimaNotaResponse res = ricercaCespiteDaPrimaNotaService.executeService(req);
		assertNotNull(res);
	}
	
}
