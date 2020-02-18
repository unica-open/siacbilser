/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.classificatoreGsa;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.classifgsa.AggiornaClassificatoreGSAService;
import it.csi.siac.siacbilser.business.service.classifgsa.AnnullaClassificatoreGSAService;
import it.csi.siac.siacbilser.business.service.classifgsa.InserisceClassificatoreGSAService;
import it.csi.siac.siacbilser.business.service.classifgsa.RicercaClassificatoreGSAValidoService;
import it.csi.siac.siacbilser.business.service.classifgsa.RicercaDettaglioClassificatoreGSAService;
import it.csi.siac.siacbilser.business.service.classifgsa.RicercaSinteticaClassificatoreGSAService;
import it.csi.siac.siacbilser.integration.dad.ClassificatoreGSADad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValido;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValidoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.ClassificatoreGSAModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoClassificatoreGSA;

public class ClassificatoreGsaTest extends BaseJunit4TestCase {
	
	@Autowired
	private InserisceClassificatoreGSAService inserisceClassificatoreGSAService;
	@Autowired
	private AggiornaClassificatoreGSAService aggiornaClassificatoreGSAService;
	@Autowired
	private RicercaDettaglioClassificatoreGSAService ricercaDettaglioClassificatoreGSAService;
	@Autowired
	private RicercaSinteticaClassificatoreGSAService ricercaSinteticaClassificatoreGSAService;
	@Autowired
	private AnnullaClassificatoreGSAService annullaClassificatoreGSAService;
	@Autowired
	private RicercaClassificatoreGSAValidoService ricercaClassificatoreGSAValidoService;
	
	@Autowired
	private ClassificatoreGSADad classificatoreGsaDad;
	
	@Test
	public void testRicercaContoBycodice() {
		Ente ente = create(Ente.class, 2);
		classificatoreGsaDad.setEnte(ente);
		ClassificatoreGSA classificatoreGSA = new ClassificatoreGSA();
		classificatoreGSA.setCodice("codiceACaso");
		ClassificatoreGSA cgsa = classificatoreGsaDad.findClassificatoreGSAValidoByCodice(classificatoreGSA);
		System.out.println(cgsa != null? "trovato un classificatore" : "nessun classificatore trovato");
	}
	
	
	
	@Test
	public void inserisceClassificatoreGSA() {
		
		InserisceClassificatoreGSA req = new InserisceClassificatoreGSA();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		Bilancio bilancio = create(Bilancio.class, 131);
		bilancio.setAnno(2017);
		req.setBilancio(bilancio);
		
		
		ClassificatoreGSA conto = new ClassificatoreGSA();
		conto.setCodice("WAWAWA");
		conto.setDescrizione("non lo devo inserire");
		
//		ClassificatoreGSA classificatoreGSAPadre = new ClassificatoreGSA();
//		classificatoreGSAPadre.setUid(8);
//		conto.setClassificatoreGSAPadre(classificatoreGSAPadre);
		
//		conto.setLivello(0);
		conto.setStatoOperativoClassificatoreGSA(StatoOperativoClassificatoreGSA.VALIDO);
		
		conto.setAmbito(Ambito.AMBITO_GSA);
		
//		ClassificatoreGSA contoPadre = new ClassificatoreGSA();
//		contoPadre.setUid(11);
//		conto.setClassificatoreGSAPadre(contoPadre);

		req.setClassificatoreGSA(conto);
		
		InserisceClassificatoreGSAResponse res = inserisceClassificatoreGSAService.executeService(req);
		assertNotNull(res);

	}
	
	
	@Test
	public void aggiornaClassificatoreGSA() {
		
		RicercaDettaglioClassificatoreGSAResponse res = caricaDettaglioClassificatoreGSA();
		assertNotNull(res);
		ClassificatoreGSA classificatoreGSADaAggiornare = res.getClassificatoreGSA();
		
		AggiornaClassificatoreGSA req = new AggiornaClassificatoreGSA();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		Bilancio bilancio = create(Bilancio.class, 131);
		bilancio.setAnno(2017);
		req.setBilancio(bilancio);
		
		
		classificatoreGSADaAggiornare.setCodice("WAWAWA");
		classificatoreGSADaAggiornare.setDescrizione("WAWADESC3");
		req.setClassificatoreGSA(classificatoreGSADaAggiornare);
		
		AggiornaClassificatoreGSAResponse res2 = aggiornaClassificatoreGSAService.executeService(req);
		assertNotNull(res2);

	}
	
	
	@Test
	public void ricercaDettaglioClassificatoreGSA() {
		
		RicercaDettaglioClassificatoreGSAResponse res = caricaDettaglioClassificatoreGSA();
		assertNotNull(res);

	}



	/**
	 * @return
	 */
	private RicercaDettaglioClassificatoreGSAResponse caricaDettaglioClassificatoreGSA() {
		RicercaDettaglioClassificatoreGSA req = new RicercaDettaglioClassificatoreGSA();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		Bilancio bilancio = create(Bilancio.class, 131);
		bilancio.setAnno(2017);
		req.setBilancio(bilancio);
		
		ClassificatoreGSAModelDetail[] classificatoreGSAModelDetails = new ClassificatoreGSAModelDetail[] {
				ClassificatoreGSAModelDetail.ClassificatoreGSAPadre,
				ClassificatoreGSAModelDetail.ClassificatoreGSAFigli,
				ClassificatoreGSAModelDetail.Ambito,
				ClassificatoreGSAModelDetail.Stato
		};
		
		req.setClassificatoreGSAModelDetails(classificatoreGSAModelDetails);
		
		//req.setClassificatoreGSAModelDetails(ClassificatoreGSAModelDetail.Soggetto);
		
		ClassificatoreGSA conto = new ClassificatoreGSA();
		conto.setUid(7); //6 //5, //113
		
		req.setClassificatoreGSA(conto);
		
		RicercaDettaglioClassificatoreGSAResponse res = ricercaDettaglioClassificatoreGSAService.executeService(req);
		return res;
	}
	
	@Test
	public void ricercaSinteticaClassificatoreGSA() {
		RicercaSinteticaClassificatoreGSA req = new RicercaSinteticaClassificatoreGSA();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		ClassificatoreGSA conto = new ClassificatoreGSA();
		conto.setAmbito(Ambito.AMBITO_GSA);
		conto.setStatoOperativoClassificatoreGSA(StatoOperativoClassificatoreGSA.VALIDO);
//		conto.setCodice("FIRSTFIGLIO");
//		conto.setDescrizione("descrizione");
		
		req.setClassificatoreGSA(conto);
		
		RicercaSinteticaClassificatoreGSAResponse res = ricercaSinteticaClassificatoreGSAService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void annullaClassificatoreGSA() {
		
		AnnullaClassificatoreGSA req = new AnnullaClassificatoreGSA();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		Bilancio bilancio = create(Bilancio.class, 131);
		bilancio.setAnno(2017);
		req.setBilancio(bilancio);
		
		
		ClassificatoreGSA classificatoreGSA = new ClassificatoreGSA();
		classificatoreGSA.setUid(8); //108, //113
		
		req.setClassificatoreGSA(classificatoreGSA);
		
		AnnullaClassificatoreGSAResponse res = annullaClassificatoreGSAService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void ricercaSinteticaClassificatoreGSAFigli() {
		
//		RicercaSinteticaClassificatoreGSAFigli req = new RicercaSinteticaClassificatoreGSAFigli();
//		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
//		req.setParametriPaginazione(new ParametriPaginazione(1, 2));
//		req.setBilancio(getBilancioTest(143, 2017));
//		
//		ClassificatoreGSA conto = new ClassificatoreGSA();
//		conto.setAmbito(Ambito.AMBITO_FIN);
//		
////		conto.setPianoDeiConti(new PianoDeiConti());
////		conto.getPianoDeiConti().setClassePiano(new ClassePiano());
////		conto.getPianoDeiConti().getClassePiano().setUid(30);;
////		
////		conto.setCodiceInterno("NON FILTRATO PER ORA ;(");
//		conto.setCodice("2" ); //"RE001002"
//		conto.setCodiceInterno("");
//		conto.setPianoDeiConti(create(PianoDeiConti.class, 0));
//		conto.getPianoDeiConti().setClassePiano(create(ClassePiano.class, 209));
//		
//		req.setClassificatoreGSA(conto);
//		
//		RicercaSinteticaClassificatoreGSAFigliResponse res = ricercaSinteticaClassificatoreGSAFigliService.executeService(req);
//		assertNotNull(res);

	}
	
	@Test
	public void ricercaClassificatoreGSAValido() {
		RicercaClassificatoreGSAValido req = new RicercaClassificatoreGSAValido();

		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setAmbito(Ambito.AMBITO_GSA);
		req.setBilancio(getBilancioTest(131, 2017));
		
		RicercaClassificatoreGSAValidoResponse res = ricercaClassificatoreGSAValidoService.executeService(req);
		assertNotNull(res);
	}

}
