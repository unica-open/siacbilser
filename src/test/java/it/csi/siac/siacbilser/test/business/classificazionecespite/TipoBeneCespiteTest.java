/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.classificazionecespite;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.classificazionecespiti.AggiornaTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.AnnullaTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.EliminaTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.InserisciTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaDettaglioTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaSinteticaTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.VerificaAnnullabilitaTipoBeneCespiteService;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;

public class TipoBeneCespiteTest extends BaseJunit4TestCase {

	@Autowired private InserisciTipoBeneCespiteService inserisciTipoBeneCespiteService;
	@Autowired private AggiornaTipoBeneCespiteService aggiornaTipoBeneCespiteService;
	@Autowired private RicercaDettaglioTipoBeneCespiteService ricercaDettaglioTipoBeneCespiteService;
	@Autowired private RicercaSinteticaTipoBeneCespiteService ricercaSinteticaTipoBeneCespiteService;
	@Autowired private EliminaTipoBeneCespiteService eliminaTipoBeneCespiteService;
	@Autowired private AnnullaTipoBeneCespiteService annullaTipoBeneCespiteService;
	@Autowired private VerificaAnnullabilitaTipoBeneCespiteService verificaAnnullabilitaTipoBeneCespiteService;
	@Autowired private TipoBeneCespiteDad tipoBeneCespiteDad;
	

	
	@Test
	public void inserisciTipoBeneCespite(){
		InserisciTipoBeneCespite req = new InserisciTipoBeneCespite();

		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		
		TipoBeneCespite tipoBeneCespite = new TipoBeneCespite();
		tipoBeneCespite.setCodice("TPB-VAR");
		tipoBeneCespite.setDescrizione("tipo bene con conti per inserimento scritture della variazione cespite");		
		
		CategoriaCespiti categoria = new CategoriaCespiti();
		categoria.setUid(86);
//		categoria.setUid(1020);
		tipoBeneCespite.setCategoriaCespiti(categoria);
		
		Conto contoPatrimoniale = new Conto();
		contoPatrimoniale.setCodice("1.1.2.01.01.03.002");
		
		tipoBeneCespite.setContoPatrimoniale(contoPatrimoniale);
		
		Conto contoIncremento = new Conto();
		contoIncremento.setCodice("2.1.1.01.02.001");
		
		tipoBeneCespite.setContoIncremento(contoIncremento);
		
		Conto contoDecremento = new Conto();
		contoDecremento.setCodice("2.1.1.01.02.003");
		
		tipoBeneCespite.setContoDecremento(contoDecremento);
		
		tipoBeneCespite.setCausaleDecremento(create(CausaleEP.class, 539634));
		
		tipoBeneCespite.setCausaleIncremento(create(CausaleEP.class, 539635));
		
		req.setTipoBeneCespite(tipoBeneCespite);
		
		InserisciTipoBeneCespiteResponse res = inserisciTipoBeneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaTipoBeneCespite(){
		
		RicercaDettaglioTipoBeneCespiteResponse ricercaDettaglio = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		
		AggiornaTipoBeneCespite req = new AggiornaTipoBeneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		
		
		TipoBeneCespite tipoBeneCespite = ricercaDettaglio.getTipoBeneCespite();
		//tipoBeneCespite.setUid(6);		
//		tipoBeneCespite.setDescrizione(tipoBeneCespite.getDescrizione() + " - aggiornata");	
//		tipoBeneCespite.getCategoriaCespiti().setUid(1014);

		req.setTipoBeneCespite(tipoBeneCespite);
		
		AggiornaTipoBeneCespiteResponse res = aggiornaTipoBeneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaTipoBeneCespiteByXml(){
		
		RicercaDettaglioTipoBeneCespiteResponse ricercaDettaglio = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		
		AggiornaTipoBeneCespite req = new AggiornaTipoBeneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2018));
		
		
		TipoBeneCespite tipoBeneCespite = getTestFileObject(TipoBeneCespite.class, "cespiti/tipobene/tipoBene.xml");  ricercaDettaglio.getTipoBeneCespite();

		req.setTipoBeneCespite(tipoBeneCespite);
		
		AggiornaTipoBeneCespiteResponse res = aggiornaTipoBeneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioTipoBeneCespite(){
		RicercaDettaglioTipoBeneCespiteResponse res = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaTipoBeneCespite() {
		RicercaSinteticaTipoBeneCespite req = new RicercaSinteticaTipoBeneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		
//		TipoBeneCespite cat = new TipoBeneCespite();
//		cat.setDescrizione("cancelleria");
//		cat.setCodice("005");
//		cat.setDescrizione("bene cespite");
//		req.setContoPatrimoniale(create(Conto.class, 156));
//		req.setTipoBeneCespite(cat);
		
		req.setContoPatrimoniale(create(Conto.class, 0));
		req.getContoPatrimoniale().setCodice("1.2.1.03.01.01.001");
/*		
		CategoriaCespiti categoria = new CategoriaCespiti();
		categoria.setUid(981);
		categoria.setCodice("006");
		req.setCategoriaCespiti(categoria);
*/
		//req.setContoPatrimoniale(contoPatrimoniale );
//		req.setListaTipoBeneCespiteModelDetail(new TipoBeneCespiteModelDetail[] {TipoBeneCespiteModelDetail.CategoriaCespiti, TipoBeneCespiteModelDetail.ContoPatrimoniale});
		req.setModelDetails(TipoBeneCespiteModelDetail.CategoriaCespiti, TipoBeneCespiteModelDetail.ContoPatrimoniale, CategoriaCespitiModelDetail.AliquotaAnnua);

		
		req.setParametriPaginazione(getParametriPaginazioneTest());
		RicercaSinteticaTipoBeneCespiteResponse response = ricercaSinteticaTipoBeneCespiteService.executeService(req);
		assertNotNull(response);
	}

	/**
	 * @return
	 */
	private RicercaDettaglioTipoBeneCespiteResponse ottieniResponseRicercaDettaglio(Richiedente richiedente) {
		RicercaDettaglioTipoBeneCespite req = new RicercaDettaglioTipoBeneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		req.setAnnoBilancio(new Integer(2018));			
		req.setTipoBeneCespite(create(TipoBeneCespite.class, 40));
//		req.setTipoBeneCespiteModelDetail(new TipoBeneCespiteModelDetail[] {TipoBeneCespiteModelDetail.CategoriaCespiti, TipoBeneCespiteModelDetail.ContoPatrimoniale});
		RicercaDettaglioTipoBeneCespiteResponse res = ricercaDettaglioTipoBeneCespiteService.executeService(req);
		return res;
	}
	
	@Test
	public void eliminaTipoBeneCespiteTest() {
		EliminaTipoBeneCespite req = new EliminaTipoBeneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		TipoBeneCespite cat = new TipoBeneCespite();
//		cat.setDescrizione("cancelleria");
		cat.setUid(32);
		req.setTipoBeneCespite(cat);
		EliminaTipoBeneCespiteResponse res = eliminaTipoBeneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void annullaTipoBeneCespiteTest() {
		AnnullaTipoBeneCespite req = new AnnullaTipoBeneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setTipoBeneCespite(create(TipoBeneCespite.class, 124));
		req.setAnnoAnnullamento(Integer.valueOf(2018));
		//req.setDataAnnullamento(new Date());
		
		AnnullaTipoBeneCespiteResponse res = annullaTipoBeneCespiteService.executeService(req);
		assertNotNull(res);
	}	
	
	/**
	 * Checktipo bene by categoria tipo bene.
	 */
	@Test
	public void checktipoBeneByCategoriaTipoBene() {
		tipoBeneCespiteDad.countTipoBeneByCategoria(create(CategoriaCespiti.class, 982));
	}

	/**
	 * Verifica l'annullabilit&agrave; del tipo bene
	 */
	@Test
	public void verificaAnnullabilitaTipoBeneCespite() {
		VerificaAnnullabilitaTipoBeneCespite req = new VerificaAnnullabilitaTipoBeneCespite();
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
//		req.setTipoBeneCespite(create(TipoBeneCespite.class, 26));
		req.setTipoBeneCespite(create(TipoBeneCespite.class, 12));
		
		VerificaAnnullabilitaTipoBeneCespiteResponse res = verificaAnnullabilitaTipoBeneCespiteService.executeService(req);
		assertNotNull(res);
	}

}
