/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cespite;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cespiti.InserisciAmmortamentoMassivoCespiteAsyncService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciAmmortamentoMassivoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaDettaglioCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.utility.AmmortamentoAnnuoCespiteFactory;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaDettaglioCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaDettaglioTipoBeneCespiteService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.integration.dad.DettaglioAmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.CategoriaCalcoloTipoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacgenser.model.PrimaNota;

public class AmmortamentoAnnuoCespiteTest extends BaseJunit4TestCase {

	@Autowired private RicercaDettaglioCespiteService ricercaDettaglioCespiteService;
	@Autowired private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;
	@Autowired private DettaglioAmmortamentoAnnuoCespiteDad dettaglioAmmortamentoAnnuoCespiteDad;
	@Autowired private RicercaDettaglioTipoBeneCespiteService ricercaDettaglioTipoBeneCespiteService;
	@Autowired private RicercaDettaglioCategoriaCespitiService ricercaDettaglioCategoriaCespitiService;
	@Autowired private InserisciAmmortamentoMassivoCespiteService inserisciAmmortamentoMassivoCespiteService;
	@Autowired private InserisciAmmortamentoMassivoCespiteAsyncService inserisciAmmortamentoMassivoCespiteAsynService;
	@Autowired private RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteService ricercaSinteticaDettaglioAmmortamentoAnnuoCespiteService;
	
	private Cespite ottieniCespiteDaAmmortare(Cespite cespite) {
		RicercaDettaglioCespite req = new RicercaDettaglioCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		req.setCespite(cespite);
		RicercaDettaglioCespiteResponse res = ricercaDettaglioCespiteService.executeService(req);
		assertNotNull(res);
		return res.getCespite();
	}
	
	private CategoriaCespiti ottieniCategoria(TipoBeneCespite tipoBene) {
		RicercaDettaglioTipoBeneCespite req = new RicercaDettaglioTipoBeneCespite();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));			
		req.setTipoBeneCespite(tipoBene);
		req.setTipoBeneCespiteModelDetail(new TipoBeneCespiteModelDetail[] {TipoBeneCespiteModelDetail.CategoriaCespiti});
		RicercaDettaglioTipoBeneCespiteResponse res = ricercaDettaglioTipoBeneCespiteService.executeService(req);
		assertNotNull(res);
		RicercaDettaglioCategoriaCespiti reqCat = new RicercaDettaglioCategoriaCespiti();
		reqCat.setDataOra(new Date());
		reqCat.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqCat.setAnnoBilancio(new Integer(2017));			
		reqCat.setCategoriaCespiti(create(CategoriaCespiti.class, 1023));	
		RicercaDettaglioCategoriaCespitiResponse responseCat = ricercaDettaglioCategoriaCespitiService.executeService(reqCat);
		return responseCat.getCategoriaCespiti();
	}
	
	@Test
	public void inserisciAmmortamentoAnnuo() {
//		Cespite cespiteDaAmmortare = ottieniCespiteDaAmmortare(create(Cespite.class, 24));
		Cespite cespiteDaAmmortare = create(Cespite.class, 24);
		cespiteDaAmmortare.setValoreAttuale(new BigDecimal("200"));
		cespiteDaAmmortare.setDataAccessoInventario(parseDate("15/09/2017"));
//		CategoriaCespiti categoria = ottieniCategoria(cespiteDaAmmortare.getTipoBeneCespite());
		CategoriaCespiti categoria = new CategoriaCespiti();
		categoria.setAliquotaAnnua(new BigDecimal("35"));
		CategoriaCalcoloTipoCespite cc = new CategoriaCalcoloTipoCespite();
		cc.setUid(2);
		cc.setCodice("50");
		categoria.setCategoriaCalcoloTipoCespite(cc);
		Date dataAmmortamento = parseDate("08/09/2017");
		Date dataVariazione = parseDate("08/09/2018");
		Integer ultimoAnnoDaAmmortare = Integer.valueOf(2020);
		
		
		ammortamentoAnnuoCespiteDad.setEnte(create(Ente.class, 2));
		ammortamentoAnnuoCespiteDad.setLoginOperazione("test junit");
		dettaglioAmmortamentoAnnuoCespiteDad.setEnte(create(Ente.class, 2));
		dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione("test junit");
		
		Utility.MDTL.addModelDetails(new DettaglioAmmortamentoAnnuoCespiteModelDetail[] { DettaglioAmmortamentoAnnuoCespiteModelDetail.PrimaNota});
		
		AmmortamentoAnnuoCespite ammortamentoCespite = ammortamentoAnnuoCespiteDad.cancellaDettagliAmmortamentiSenzaPrimaNotaDefinitiva(cespiteDaAmmortare);
		
		
		if(ammortamentoCespite == null) {
			System.out.println("non esiste un ammortamento per il cespite.");
		}
		/*else {
			System.out.println("Trovato un ammortamento precedente con prima nota uid ammortamento: " + ammortamentoPrecedente.getUid());
		}*/
		//cespite, categoria, annoFine ammortamento, data ammortamento, ammortamentoAnnuoPrecedente
		AmmortamentoAnnuoCespiteFactory factory = new AmmortamentoAnnuoCespiteFactory(cespiteDaAmmortare, categoria, ultimoAnnoDaAmmortare, dataAmmortamento 
				,dataVariazione
				);
		factory.elaboraAmmortamento(ammortamentoCespite);
		if(ammortamentoCespite == null || ammortamentoCespite.getUid() == 0) {
			ammortamentoCespite = ammortamentoAnnuoCespiteDad.inserisciTestataAmmortamentoAnnuoCespite(factory.getTestataAmmortamento());
		}
		for (DettaglioAmmortamentoAnnuoCespite dettaglio : factory.getDettagliAmmortamentoElaborati()) { 
			System.out.println("sto per inserire il dettaglio per l'anno:" + dettaglio.getAnno());
			dettaglioAmmortamentoAnnuoCespiteDad.inserisciDettaglioAmmortamentoAnnuoCespite(dettaglio, ammortamentoCespite.getUid());
		}
		
//		dettaglioAmmortamentoAnnuoCespiteDad.inserisciDettagliAmmortamentoAnnuoCespite(nuovoAmmortamento);
	}
	
	private void loggaAmmortamentoCreato(AmmortamentoAnnuoCespite ammortamentoCreato) {
		StringBuilder sb = new StringBuilder();
		if(ammortamentoCreato == null) {
			sb.append("Nessun ammortamento creato.");
			System.out.println(sb.toString());
			return;
		}
		sb.append("Ammortamento effettuato con successo.\n\tData ammortamento:")
		.append("\n\timporto totale ammortato: ")
		.append(ammortamentoCreato.getImportoTotaleAmmortato())
		.append("\n\tultimo anno: ")
		.append(ammortamentoCreato.getUltimoAnnoAmmortato())
		.append("\n\tdettaglio ammortamento: \n");
		
		sb.append("\t\t")
		.append("Anno")
		.append("\t")
		.append("quota")
		.append("\t")
		.append("registrazione")
		.append("\t")
		.append(" data cancellazione")
		.append("\n");
		
		for (DettaglioAmmortamentoAnnuoCespite dett : ammortamentoCreato.getDettagliAmmortamentoAnnuoCespite()) {
			sb.append("\t\t")
			.append(dett.getAnno().toString())
			.append("\t")
			.append(dett.getQuotaAnnuale().toString())
			.append("\t\t")
			.append(dett.getPrimaNota() != null? "PRIMA NOTA X" : "null")
			.append("\t\t")
			.append(dett.getDataCancellazione() != null? formattaData(dett.getDataAmmortamento()) : "null")
			.append("\n");
		}
		
		System.out.println(sb.toString());
		
	}
	
	private String  formattaData(Date data) {
		return new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY).format(data);
	}
	
	@Test
	public void inserisciAmmortamentoMassivo() {
		
		InserisciAmmortamentoMassivoCespite req = new InserisciAmmortamentoMassivoCespite();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setAnnoBilancio(2017);
		req.setUltimoAnnoAmmortamento(Integer.valueOf(2030));
		Integer[] uids = new Integer[] {
				//17,13, 15,
				340
		};

		List<Integer> uidList = new ArrayList<Integer>();
		for (Integer uid : uids) {
			uidList.add(uid);
		}
		req.setUidsCespiti(uidList);
//		RicercaSinteticaCespite reqSint = new RicercaSinteticaCespite();
//		reqSint.setRichiedente(getRichiedenteByProperties("consip", "regp"));
//		reqSint.setDataOra(new Date());
//		reqSint.setAnnoBilancio(2017);
//		reqSint.setTipoBeneCespite(create(TipoBeneCespite.class, 30));
//		req.setRequestRicerca(reqSint);
		InserisciAmmortamentoMassivoCespiteResponse res= inserisciAmmortamentoMassivoCespiteService.executeService(req);
	}
	
	@Test 
	public void ricercaDettagliByCespite() {
		RicercaSinteticaDettaglioAmmortamentoAnnuoCespite req = new RicercaSinteticaDettaglioAmmortamentoAnnuoCespite();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setAnnoBilancio(2017);
		req.setModelDetails(new DettaglioAmmortamentoAnnuoCespiteModelDetail[] {DettaglioAmmortamentoAnnuoCespiteModelDetail.PrimaNota});
		req.setCespite(create(Cespite.class, 24));
		req.setParametriPaginazione(getParametriPaginazione(0, 0));
		RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse res = ricercaSinteticaDettaglioAmmortamentoAnnuoCespiteService.executeService(req);
	}
	
	@Test
	public void ammortamentoMassivoAsync() {
		final String methodName ="ammortamentoMassivoAsync";
		InserisciAmmortamentoMassivoCespite req = new InserisciAmmortamentoMassivoCespite();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(getBilancioTest(131, 2017).getAnno());
		req.setUltimoAnnoAmmortamento(Integer.valueOf(2030));
		Integer[] uids = new Integer[] {
				Integer.valueOf(20)
				};
//		req.setUidsCespiti(Arrays.asList(uids));
		RicercaSinteticaCespite reqSint = new RicercaSinteticaCespite();
		reqSint.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqSint.setDataOra(new Date());
		reqSint.setAnnoBilancio(2017);
		reqSint.setTipoBeneCespite(create(TipoBeneCespite.class, 30));
		reqSint.setMassimoAnnoAmmortato(req.getUltimoAnnoAmmortamento());
		reqSint.setConPianoAmmortamentoCompleto(Boolean.FALSE);
		req.setRequestRicerca(reqSint);
		
		
		AsyncServiceRequestWrapper<InserisciAmmortamentoMassivoCespite> wrapper = new AsyncServiceRequestWrapper<InserisciAmmortamentoMassivoCespite>();
		wrapper.setAccount(req.getRichiedente().getAccount());
		wrapper.setAnnoBilancio(req.getAnnoBilancio());
		wrapper.setDataOra(req.getDataOra());
		wrapper.setEnte(req.getRichiedente().getAccount().getEnte());
		wrapper.setRequest(req);
		wrapper.setRichiedente(req.getRichiedente());
		
		wrapper.setAzioneRichiesta(create(AzioneRichiesta.class, 0));
		wrapper.getAzioneRichiesta().setAzione(create(Azione.class, 13076));
		
		AsyncServiceResponse res = inserisciAmmortamentoMassivoCespiteAsynService.executeService(wrapper);
		assertNotNull(res);
		
		try {
			Thread.sleep(10*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			log.info(methodName, ">>>>>>>>>>>>>>>> sleep finito!");
		}
	}
	
	@Test
	public void testInserimentoNuovoDettaglio() {
		DettaglioAmmortamentoAnnuoCespite nuovoDettaglio = new DettaglioAmmortamentoAnnuoCespite();
		nuovoDettaglio.setQuotaAnnuale(new BigDecimal("3.47"));
		nuovoDettaglio.setAnno(2018);
		nuovoDettaglio.setDataAmmortamento(new Date());
		nuovoDettaglio.setPrimaNota(create(PrimaNota.class, 24554));
		dettaglioAmmortamentoAnnuoCespiteDad.setEnte(create(Ente.class, 2));
		dettaglioAmmortamentoAnnuoCespiteDad.setLoginOperazione("test junit");
		dettaglioAmmortamentoAnnuoCespiteDad.inserisciDettaglioAmmortamentoAnnuoCespite(nuovoDettaglio, 13);
	}
	
}
