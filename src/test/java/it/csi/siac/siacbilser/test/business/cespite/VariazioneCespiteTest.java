/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cespite;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cespiti.AggiornaVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.EliminaVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaDettaglioVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaVariazioneCespiteService;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;

public class VariazioneCespiteTest extends BaseJunit4TestCase {

	@Autowired private InserisciVariazioneCespiteService inserisciVariazioneCespiteService;
	@Autowired private AggiornaVariazioneCespiteService aggiornaVariazioneCespiteService;
	@Autowired private EliminaVariazioneCespiteService eliminaVariazioneCespiteService;
	@Autowired private RicercaDettaglioVariazioneCespiteService ricercaDettaglioVariazioneCespiteService;
	@Autowired private RicercaSinteticaVariazioneCespiteService ricercaSinteticaVariazioneCespiteService;
	@Autowired @Inventario private PrimaNotaInvDad primaNotaInvDad;
	
	@Test
	public void inserisciVariazioneCespite() {
		InserisciVariazioneCespite req = new InserisciVariazioneCespite();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setVariazioneCespite(new VariazioneCespite());
		req.getVariazioneCespite().setCespite(create(Cespite.class, 22));
//		req.getVariazioneCespite().getCespite().setCodice("003");
		req.getVariazioneCespite().setAnnoVariazione("2017");
		req.getVariazioneCespite().setDescrizione("Variazione di test");
		req.getVariazioneCespite().setFlagTipoVariazioneIncremento(Boolean.TRUE);
//		req.getVariazioneCespite().setFlagTipoVariazioneIncremento(Boolean.FALSE);
		req.getVariazioneCespite().setDataVariazione(parseDate("09/08/2018"));
		req.getVariazioneCespite().setImporto(new BigDecimal("15"));
		
		InserisciVariazioneCespiteResponse res = inserisciVariazioneCespiteService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void aggiornaVariazioneCespite() {
		
		RicercaDettaglioVariazioneCespiteResponse caricaVariazione = caricaVariazione(92);
		AggiornaVariazioneCespite req = new AggiornaVariazioneCespite();
		
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setVariazioneCespite(caricaVariazione.getVariazioneCespite());
		
		AggiornaVariazioneCespiteResponse res = aggiornaVariazioneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void eliminaVariazioneCespite() {
		EliminaVariazioneCespite req = new EliminaVariazioneCespite();
		
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setVariazioneCespite(create(VariazioneCespite.class, 15));
		
		EliminaVariazioneCespiteResponse res = eliminaVariazioneCespiteService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void ricercaDettaglioVariazioneCespite() {
		caricaVariazione(Integer.valueOf(92));
	}

	/**
	 * @param integer 
	 * @return 
	 * 
	 */
	private RicercaDettaglioVariazioneCespiteResponse caricaVariazione(Integer uidVariazione) {
		RicercaDettaglioVariazioneCespite req = new RicercaDettaglioVariazioneCespite();
		
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setVariazioneCespite(create(VariazioneCespite.class, uidVariazione));
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite, VariazioneCespiteModelDetail.CespiteModelDetail);
//		req.setModelDetails(VariazioneCespiteModelDetail.values()
////				CespiteModelDetail.TipoBeneCespiteModelDetail,
////				TipoBeneCespiteModelDetail.CategoriaCespitiModelDetail,
////				CategoriaCespitiModelDetail.AliquotaAnnua
//				);
		
		RicercaDettaglioVariazioneCespiteResponse res = ricercaDettaglioVariazioneCespiteService.executeService(req);
		assertNotNull(res);
		return res;
	}
	
	@Test
	public void ricercaSinteticaVariazioneCespite() {
		RicercaSinteticaVariazioneCespite req = new RicercaSinteticaVariazioneCespite();
		
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setVariazioneCespite(create(VariazioneCespite.class, 0));
//		req.getVariazioneCespite().setAnnoVariazione("2018");
//		req.getVariazioneCespite().setDataVariazione(parseDate("09/08/2018"));
//		req.getVariazioneCespite().setDescrizione("Variazione");
		req.getVariazioneCespite().setFlagTipoVariazioneIncremento(Boolean.TRUE);
		
		req.getVariazioneCespite().setCespite(create(Cespite.class, 0));
		req.getVariazioneCespite().getCespite().setCodice("003");
//		req.getVariazioneCespite().getCespite().setDescrizione("a");
//		req.getVariazioneCespite().getCespite().setFlagSoggettoTutelaBeniCulturali(Boolean.FALSE);
//		req.getVariazioneCespite().getCespite().setFlgDonazioneRinvenimento(Boolean.FALSE);
//		req.getVariazioneCespite().getCespite().setNumeroInventario("23");
//		req.getVariazioneCespite().getCespite().setDataAccessoInventario(parseDate("28/08/2018"));
		
//		req.getVariazioneCespite().getCespite().setTipoBeneCespite(create(TipoBeneCespite.class, 55));
//		req.getVariazioneCespite().getCespite().setClassificazioneGiuridicaCespite(ClassificazioneGiuridicaCespite.CES_BENE_DEMANIALE);
		
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite,
				VariazioneCespiteModelDetail.CespiteModelDetail,
				CespiteModelDetail.TipoBeneCespiteModelDetail,
				TipoBeneCespiteModelDetail.CategoriaCespitiModelDetail,
				CategoriaCespitiModelDetail.AliquotaAnnua);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		
		RicercaSinteticaVariazioneCespiteResponse res = ricercaSinteticaVariazioneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
}
