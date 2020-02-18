/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cespite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cespiti.AggiornaImportoCespiteRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaPrimaNotaSuRegistroACespiteAsyncService;
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaPrimaNotaSuRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.CollegaCespiteRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciPrimaNotaSuRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaMovimentoDettaglioRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaMovimentoEPRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaScrittureRegistroAByCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RifiutaPrimaNotaCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RifiutaPrimaNotaSuRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.ScollegaCespiteRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.ValidaPrimaNotaCespiteService;
import it.csi.siac.siacbilser.business.service.primanota.OttieniDatiPrimeNoteFatturaConNotaCreditoService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.ImportiRegistroA;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCredito;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCreditoResponse;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.ContoModelDetail;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.RegistrazioneMovFinModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;

public class PrimaNotaCespiteTest extends BaseJunit4TestCase {

	@Autowired private ValidaPrimaNotaCespiteService validaPrimaNotaCespiteService;
	@Autowired private RifiutaPrimaNotaCespiteService rifiutaPrimaNotaCespiteService;
	@Autowired private InserisciPrimaNotaSuRegistroACespiteService inserisciPrimaNotaSuRegistroACespiteService;
	@Autowired private RifiutaPrimaNotaSuRegistroACespiteService rifiutaPrimaNotaSuRegistroACespiteService;
	@Autowired private RicercaSinteticaMovimentoEPRegistroACespiteService ricercaSinteticaMovimentoEPRegistroACespiteService;
	@Autowired private RicercaSinteticaMovimentoDettaglioRegistroACespiteService ricercaSinteticaMovimentoDettaglioRegistroACespiteService;

	@Autowired private CollegaCespiteRegistroACespiteService collegaCespiteRegistroACespiteService;
	@Autowired private AggiornaImportoCespiteRegistroACespiteService aggiornaImportoCespiteRegistroACespiteService;
	@Autowired private ScollegaCespiteRegistroACespiteService scollegaCespiteRegistroACespiteService;
	
	@Autowired private AggiornaPrimaNotaSuRegistroACespiteService aggiornaPrimaNotaSuRegistroACespiteService;
	@Autowired private AggiornaPrimaNotaSuRegistroACespiteAsyncService aggiornaPrimaNotaSuRegistroACespiteAsyncService;
	@Autowired @Inventario private PrimaNotaInvDad primaNotaInvDad;
	@Autowired private RicercaSinteticaScrittureRegistroAByCespiteService ricercaSinteticaScrittureRegistroAByCespiteService;
	
	@Autowired OttieniDatiPrimeNoteFatturaConNotaCreditoService ottieniDatiPrimeNoteFatturaConNotaCreditoService;

	
	@Test
	public void validaPrimaNotaCespite() {
		ValidaPrimaNotaCespite req = new ValidaPrimaNotaCespite();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setPrimaNota(create(PrimaNota.class,24522));
		
		ValidaPrimaNotaCespiteResponse res = validaPrimaNotaCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void rifiutaPrimaNotaCespite() {
		RifiutaPrimaNotaCespite req = new RifiutaPrimaNotaCespite();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setPrimaNota(create(PrimaNota.class,24507));
		
		RifiutaPrimaNotaCespiteResponse res = rifiutaPrimaNotaCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisciPrimaNotaSuRegistroACespite() {
		InserisciPrimaNotaSuRegistroACespite req = new InserisciPrimaNotaSuRegistroACespite();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		//prima nota libera
		req.setPrimaNota(create(PrimaNota.class,24685));
		//prima nota integrata DS
//		req.setPrimaNota(create(PrimaNota.class,660));
		
		InserisciPrimaNotaSuRegistroACespiteResponse res = inserisciPrimaNotaSuRegistroACespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void rifiutaPrimaNotaSuRegistroACespite() {
		RifiutaPrimaNotaSuRegistroACespite req = new RifiutaPrimaNotaSuRegistroACespite();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setPrimaNota(create(PrimaNota.class,24682));
		
		RifiutaPrimaNotaSuRegistroACespiteResponse res = rifiutaPrimaNotaSuRegistroACespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void testRicercaMovimentoEP() {
		RicercaSinteticaMovimentoEPRegistroACespite req = new RicercaSinteticaMovimentoEPRegistroACespite();
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setPrimaNota(create(PrimaNota.class,677));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setModelDetails(new ModelDetail[] {MovimentoEPModelDetail.RegistrazioneMovFinModelDetail, MovimentoEPModelDetail.PrimaNotaModelDetail, MovimentoEPModelDetail.MovimentoDettaglioModelDetail,
				MovimentoEPModelDetail.CausaleEPModelDetail, CausaleEPModelDetail.Conto,
				MovimentoDettaglioModelDetail.Cespiti, MovimentoDettaglioModelDetail.ContoModelDetail,
				RegistrazioneMovFinModelDetail.EventoMovimento,
				PrimaNotaModelDetail.TipoCausale,
				ContoModelDetail.TipoConto});
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		RicercaSinteticaMovimentoEPRegistroACespiteResponse response = ricercaSinteticaMovimentoEPRegistroACespiteService.executeService(req);
		
	}
	
	@Test
	public void testRicercaMovimentoDettaglio() {
		RicercaSinteticaMovimentoDettaglioRegistroACespite req = new RicercaSinteticaMovimentoDettaglioRegistroACespite();
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setPrimaNota(create(PrimaNota.class,24721));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setModelDetails(new ModelDetail[] {MovimentoEPModelDetail.RegistrazioneMovFinModelDetail, MovimentoEPModelDetail.PrimaNotaModelDetail,
				MovimentoEPModelDetail.CausaleEPModelDetail, CausaleEPModelDetail.Conto,
				MovimentoDettaglioModelDetail.MovimentoEPModelDetail,
				MovimentoDettaglioModelDetail.Cespiti, MovimentoDettaglioModelDetail.ContoModelDetail,
				MovimentoDettaglioModelDetail.ImportoInventariato,
				RegistrazioneMovFinModelDetail.EventoMovimento,
				PrimaNotaModelDetail.TipoCausale,
				ContoModelDetail.TipoConto});
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse response = ricercaSinteticaMovimentoDettaglioRegistroACespiteService.executeService(req);
		
	}

	@Test
	public void testCollegaCespiteRegistroACespite() {
		CollegaCespiteRegistroACespite req = new CollegaCespiteRegistroACespite();
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setMovimentoDettaglio(create(MovimentoDettaglio.class, 49979));
		List<Cespite> listaCespiti = new ArrayList<Cespite>();
		
		
		Cespite cespite1 = new Cespite();
		cespite1.setUid(23);
		listaCespiti.add(cespite1);

//		Cespite cespite2 = new Cespite();
//		cespite2.setUid(129);

		req.setListaCespiti(listaCespiti);
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
				
		CollegaCespiteRegistroACespiteResponse response = collegaCespiteRegistroACespiteService.executeService(req);
		
		
	}
	
	@Test
	public void testAggiornaImportoCespiteRegistroACespiteCespite() {
		AggiornaImportoCespiteRegistroACespite req = new AggiornaImportoCespiteRegistroACespite();
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setMovimentoDettaglio(create(MovimentoDettaglio.class, 49979));
		List<Cespite> listaCespiti = new ArrayList<Cespite>();
		
		
		Cespite cespite1 = new Cespite();
		cespite1.setUid(23);
		listaCespiti.add(cespite1);
		req.setCespite(cespite1);

		req.setImportoSuRegistroA(BigDecimal.ZERO);

		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
				
		AggiornaImportoCespiteRegistroACespiteResponse response = aggiornaImportoCespiteRegistroACespiteService.executeService(req);
		
		
	}

	@Test
	public void testScollegaCespiteRegistroACespite() {
		ScollegaCespiteRegistroACespite req = new ScollegaCespiteRegistroACespite();
		//req.setAnnoBilancio(Integer.valueOf(2018));
		req.setMovimentoDettaglio(create(MovimentoDettaglio.class, 50237));
		Cespite cespite = new Cespite();
		cespite.setUid(64);
		req.setCespite(cespite );
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		//CollegaCespiteRegistroACespiteResponse
		ScollegaCespiteRegistroACespiteResponse response = scollegaCespiteRegistroACespiteService.executeService(req);
	}
	
	
	@Test
	public void aggiornaPrimaNotaSuRegistroACespite() {
		AggiornaPrimaNotaSuRegistroACespite req = new AggiornaPrimaNotaSuRegistroACespite();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		//prima nota libera
		req.setPrimaNota(create(PrimaNota.class,24735));
		//prima nota integrata DS
//		req.setPrimaNota(create(PrimaNota.class,660));
		
		AggiornaPrimaNotaSuRegistroACespiteResponse res = aggiornaPrimaNotaSuRegistroACespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaScrittureRegistroAByCespiteService() {
		RicercaSinteticaScrittureRegistroAByCespite req = new RicercaSinteticaScrittureRegistroAByCespite();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setCespite(create(Cespite.class, 54));
		
		RicercaSinteticaScrittureRegistroAByCespiteResponse res = ricercaSinteticaScrittureRegistroAByCespiteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaPrimaNotaSuRegistroACespiteAsync() {
		AggiornaPrimaNotaSuRegistroACespite req = new AggiornaPrimaNotaSuRegistroACespite();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		//prima nota libera
		req.setPrimaNota(create(PrimaNota.class,24604));
		//prima nota integrata DS
//		req.setPrimaNota(create(PrimaNota.class,660));
		AsyncServiceRequestWrapper<AggiornaPrimaNotaSuRegistroACespite> wrapper = new AsyncServiceRequestWrapper<AggiornaPrimaNotaSuRegistroACespite>();
		wrapper.setAccount(req.getRichiedente().getAccount());
		wrapper.setAnnoBilancio(req.getAnnoBilancio());
		wrapper.setDataOra(req.getDataOra());
		wrapper.setEnte(req.getRichiedente().getAccount().getEnte());
		wrapper.setRequest(req);
		wrapper.setRichiedente(req.getRichiedente());
		
		wrapper.setAzioneRichiesta(create(AzioneRichiesta.class, 0));
		wrapper.getAzioneRichiesta().setAzione(create(Azione.class, 13076));
		
		AsyncServiceResponse res = aggiornaPrimaNotaSuRegistroACespiteAsyncService.executeService(wrapper);
		assertNotNull(res);
		
		try {
			Thread.sleep(10*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			log.info("", ">>>>>>>>>>>>>>>> sleep finito!");
		}
	}
	
	@Test
	public void calcolaImportiRegistroA() {
		List<ImportiRegistroA> calcolaImportiPrimaNotaSuRegistroA = primaNotaInvDad.calcolaImportiPrimaNotaSuRegistroA(create(PrimaNota.class, 116202));
		for (ImportiRegistroA importiRegistroA : calcolaImportiPrimaNotaSuRegistroA) {
			String identificativoConto = new StringBuilder()
					.append(StringUtils.defaultIfBlank(importiRegistroA.getContoCespite().getCodice(), ""))
					.append(" - ")
					.append(StringUtils.defaultIfBlank(importiRegistroA.getContoCespite().getDescrizione(), ""))
					.toString();
			System.out.println("conto: " + identificativoConto + " importo da inventariare:" + importiRegistroA.getImportoDaInventariare().toPlainString() + "importo inventariato: " + importiRegistroA.getImportoInventariato().toPlainString());
		}
	}
	
	@Test
	public void caricaEvento() {
		Integer[] uidIntegrate = new Integer[] {
				Integer.valueOf(54710), Integer.valueOf(24709), Integer.valueOf(24706), Integer.valueOf(24705)
		};
		
		Integer[] uidLibere = new Integer[] {
				Integer.valueOf(24685), Integer.valueOf(24669), Integer.valueOf(24650), Integer.valueOf(24649)
		};
		
		for (Integer uidI : uidIntegrate) {
			 Evento eventoPrimaNotaIntegrata = primaNotaInvDad.caricaEventoPrimaNotaIntegrata(create(PrimaNota.class, uidI));
			 if(eventoPrimaNotaIntegrata == null || eventoPrimaNotaIntegrata.getTipoEvento() == null) {
				 System.out.println("impossibile trovare l'evento per la prima nota: " + uidI);
			 }else {
				 System.out.println("uid Integrata: " + uidI + "evento integrata: " +  eventoPrimaNotaIntegrata.getCodice() + " tipo evento: " + eventoPrimaNotaIntegrata.getTipoEvento().getCodice());
			 }
			 
		}
		for (Integer uidL : uidLibere) {
			Evento eventoPrimaNotaLibera = primaNotaInvDad.caricaEventoPrimaNotaLibera(create(PrimaNota.class, uidL),Utility.ultimoGiornoDellAnno(2017));
			System.out.println("uid Libera: " + uidL + "evento libera: " +  eventoPrimaNotaLibera.getCodice() + " tipo evento: " + eventoPrimaNotaLibera.getTipoEvento().getCodice());
		}
	}
	
	@Test
	public void caricaPrimeNoteNCD() {
		primaNotaInvDad.setEnte(getEnteByProperties("consip", "regp"));
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione(0, Integer.MAX_VALUE); 
		try {
			TipoEvento tipoEvento = create(TipoEvento.class, 50 );
			tipoEvento.setCodice("DS");
			ListaPaginata<PrimaNota> primeNote = primaNotaInvDad.ricercaSinteticaPrimeNoteIntegrateRegistroA(getBilancioByProperties("consip", "regp", "2017"), new PrimaNota(), null, null, null, null, 
					null, null, null, Arrays.asList(StatoOperativoPrimaNota.PROVVISORIO, StatoOperativoPrimaNota.DEFINITIVO), null, "S", Arrays.asList(tipoEvento), null, null, null, null, null, null, null, null, Arrays.asList(Integer.valueOf(32371)), null, null, null, null, parametriPaginazione, PrimaNotaModelDetail.PrimaNotaInventario);
			for (PrimaNota primaNota : primeNote) {
				System.out.println("numero provvisorio: " + primaNota.getNumero() + " numero definitivo " + (primaNota.getNumeroRegistrazioneLibroGiornale() != null ? primaNota.getNumeroRegistrazioneLibroGiornale() : "" ));	
			}
		} catch (DadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void ottieniDatiPrimeNoteFatturaConNotaCredito() {
		OttieniDatiPrimeNoteFatturaConNotaCredito req = new OttieniDatiPrimeNoteFatturaConNotaCredito();
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setPrimaNota(create(PrimaNota.class,24706));
		
		OttieniDatiPrimeNoteFatturaConNotaCreditoResponse res = ottieniDatiPrimeNoteFatturaConNotaCreditoService.executeService(req);
		assertNotNull(res);
	}
	
}
