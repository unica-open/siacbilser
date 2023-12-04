/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.dismissionecespite;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaAnagraficaDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.CollegaCespiteDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.EliminaDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciAnagraficaDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciPrimeNoteDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaDettaglioDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.ScollegaCespiteDismissioneCespiteService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespiteModelDetail;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class DismissioneCespiteTest.
 */
public class DismissioneCespiteTest extends BaseJunit4TestCase {

	@Autowired private InserisciAnagraficaDismissioneCespiteService inserisciAnagraficaDismissioneCespiteService;
	@Autowired private AggiornaAnagraficaDismissioneCespiteService aggiornaAnagraficaDismissioneCespiteService;
	@Autowired private RicercaDettaglioDismissioneCespiteService ricercaDettaglioDismissioneCespiteService;
	@Autowired private RicercaSinteticaDismissioneCespiteService ricercaSinteticaDismissioneCespiteService;
	@Autowired private EliminaDismissioneCespiteService eliminaDismissioneCespiteService;
	@Autowired private CollegaCespiteDismissioneCespiteService collegaCespiteDismissioneCespiteService;
	
	@Autowired private InserisciPrimeNoteDismissioneCespiteService inserisciPrimeNoteDismissioneCespiteService;

	@Autowired private DismissioneCespiteDad dismissioneCespiteDad;
	@Autowired private CespiteDad cespiteDad;
	@Autowired private ScollegaCespiteDismissioneCespiteService scollegaCespiteDismissioneCespiteService;
	
	/**
	 * Inserisce un record sulla siac_t_cespiti_dismissioni
	 */
	@Test
	public void inserisciDismissioneCespiti(){
		InserisciAnagraficaDismissioneCespite req = new InserisciAnagraficaDismissioneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));

		DismissioneCespite dismissioneCespite = new DismissioneCespite();
		dismissioneCespite.setDescrizione("la prima dismissione inserita con uno stato");
		dismissioneCespite.setDescrizioneStatoCessazione("ma a cosa serve questa descrizione ulteriore?");
		
		dismissioneCespite.setAttoAmministrativo(create(AttoAmministrativo.class,35076));
		dismissioneCespite.setCausaleEP(create(CausaleEP.class,539634));
		dismissioneCespite.setDataCessazione(new Date());
		
		req.setDismissioneCespite(dismissioneCespite);

		InserisciAnagraficaDismissioneCespiteResponse res = inserisciAnagraficaDismissioneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Aggiorna dismissione cespiti.
	 */
	@Test
	public void aggiornaDismissioneCespiti(){
		
		RicercaDettaglioDismissioneCespiteResponse ricercaDettaglio = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		AggiornaAnagraficaDismissioneCespite req = new AggiornaAnagraficaDismissioneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));		
		DismissioneCespite dism = ricercaDettaglio.getDismissioneCespite();
		dism.setDescrizione("descrizione aggiornata");
		req.setDismissioneCespite(dism);
		AggiornaAnagraficaDismissioneCespiteResponse res = aggiornaAnagraficaDismissioneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioDismissioneCespiti(){
		RicercaDettaglioDismissioneCespiteResponse res = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaDismissioneCespiti() {
		RicercaSinteticaDismissioneCespite req = new RicercaSinteticaDismissioneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2017));
		DismissioneCespite dces = new DismissioneCespite();
//		dces.setAnnoElenco(2017);
//		dces.setNumeroElenco(3);
		req.setDismissioneCespite(dces);
		req.setCespite(create(Cespite.class, 18));
		req.setModelDetails(DismissioneCespiteModelDetail.values());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		RicercaSinteticaDismissioneCespiteResponse response = ricercaSinteticaDismissioneCespiteService.executeService(req);
		assertNotNull(response);
		for (DismissioneCespite dc : response.getListaDismissioneCespite()) {
//			System.out.println("uid dismissione_trovata: " + dc.getUid() + " stato: "  + dc.getStatoDismissioneCespite().name());
//			System.out.println("uid dismissione_trovata: " + dc.getUid() + "numero cespiti collegati: "  + dc.getNumeroCespitiCollegati());
		}
	}

	/**
	 * @return
	 */
	private RicercaDettaglioDismissioneCespiteResponse ottieniResponseRicercaDettaglio(Richiedente richiedente) {
		RicercaDettaglioDismissioneCespite req = new RicercaDettaglioDismissioneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		req.setAnnoBilancio(new Integer(2017));
		req.setDismissioneCespite(create(DismissioneCespite.class, 15));
		RicercaDettaglioDismissioneCespiteResponse res = ricercaDettaglioDismissioneCespiteService.executeService(req);
		return res;
	}
	
	@Test
	public void eliminaDismissioneCespitiTest() {
		EliminaDismissioneCespite req = new EliminaDismissioneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		DismissioneCespite cat = new DismissioneCespite();
		cat.setUid(16);
		req.setDismissioneCespite(cat);
		EliminaDismissioneCespiteResponse res = eliminaDismissioneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void eliminaSinteticaDismissioneCespiti() {
		EliminaDismissioneCespite reqEl = new EliminaDismissioneCespite();
		reqEl.setDataOra(new Date());
		reqEl.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqEl.setAnnoBilancio(new Integer(2017));
		DismissioneCespite cat = new DismissioneCespite();
		cat.setUid(16);
		reqEl.setDismissioneCespite(cat);
		EliminaDismissioneCespiteResponse res = eliminaDismissioneCespiteService.executeService(reqEl);
		assertNotNull(res);
	}
	
	@Test
	public void collegaCespiteADismissioneCespite(){
		CollegaCespiteDismissioneCespite req = new CollegaCespiteDismissioneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));

		req.setDismissioneCespite(create(DismissioneCespite.class, 18));
		Integer[] uidsCespiti = new Integer[] {
				25				
		};
		req.setUidsCespiti(Arrays.asList(uidsCespiti));
		CollegaCespiteDismissioneCespiteResponse res = collegaCespiteDismissioneCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test 
	public void testCaricaDismissioneDaPrimaNota() {
		dismissioneCespiteDad.setEnte(create(Ente.class, 2));
		DismissioneCespite dismissione = dismissioneCespiteDad.ottieniDismissioneCespitePrimaNota(create(PrimaNota.class, 24561), new DismissioneCespiteModelDetail[] {});
		log.debug("", "uidDismissione: " + dismissione.getUid());
	}
	@Test
	public void caricaCespitiCollegati() {
		cespiteDad.setEnte(create(Ente.class, 2));
		Utility.MDTL.addModelDetails(AmmortamentoAnnuoCespiteModelDetail.DettaglioAmmortamentoAnnuoCespite);
		List<Cespite> caricaCespitiCollegatiDismissioneByStato = cespiteDad.caricaCespitiCollegatiDismissioneByStato(create(DismissioneCespite.class, 15), Boolean.TRUE, CespiteModelDetail.AmmortamentoAnnuoCespiteModelDetail);
		for (Cespite cespite : caricaCespitiCollegatiDismissioneByStato) {
			log.logXmlTypeObject(cespite, "cespite");
		}
	}
	
	@Test
	public void testEffettuaScritture() {
		InserisciPrimeNoteDismissioneCespite req = new InserisciPrimeNoteDismissioneCespite();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setAnnoBilancio(2017);
		req.setDismissioneCespite(create(DismissioneCespite.class, 23));
		InserisciPrimeNoteDismissioneCespiteResponse res = inserisciPrimeNoteDismissioneCespiteService.executeService(req);
	}
	
	@Test
	public void scollegaCespite() {
		ScollegaCespiteDismissioneCespite req = new ScollegaCespiteDismissioneCespite();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setAnnoBilancio(2017);
		req.setDismissioneCespite(create(DismissioneCespite.class, 19));
		req.setCespite(create(Cespite.class, 26));
		ScollegaCespiteDismissioneCespiteResponse response = scollegaCespiteDismissioneCespiteService.executeService(req);
	}

}
