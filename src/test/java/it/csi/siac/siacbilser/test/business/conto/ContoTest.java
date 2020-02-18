/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.conto;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.common.RicercaCodificheService;
import it.csi.siac.siacbilser.business.service.conto.AggiornaContoService;
import it.csi.siac.siacbilser.business.service.conto.AnnullaContoService;
import it.csi.siac.siacbilser.business.service.conto.InserisceContoService;
import it.csi.siac.siacbilser.business.service.conto.RicercaDettaglioContoService;
import it.csi.siac.siacbilser.business.service.conto.RicercaSinteticaContoFigliService;
import it.csi.siac.siacbilser.business.service.conto.RicercaSinteticaContoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siacbilser.integration.dao.ContoDao;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigli;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigliResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.CodiceBilancio;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.PianoDeiConti;
import it.csi.siac.siacgenser.model.TipoConto;

public class ContoTest extends BaseJunit4TestCase {
	
	@Autowired
	private InserisceContoService inserisceContoService;
	@Autowired
	private AggiornaContoService aggiornaContoService;
	@Autowired
	private RicercaDettaglioContoService ricercaDettaglioContoService;
	@Autowired
	private RicercaSinteticaContoService ricercaSinteticaContoService;
	@Autowired
	private RicercaSinteticaContoFigliService ricercaSinteticaContoFigliService;
	@Autowired
	private RicercaCodificheService ricercaCodificheService;
	@Autowired
	private AnnullaContoService annullaContoService;
	
	@Autowired
	private ContoDao contoDao;
	
	@Autowired ContoDad contoDad;
	
	
	@Test
	public void inserisceConto() {
		
		InserisceConto req = new InserisceConto();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
//		req.setBilancio(getBilancioTest(131,2017));		
		req.setBilancio(getBilancioTest(48,2016));
		
		Conto conto = new Conto();
		conto.setCodice("1.1.2.01.01.01.009");
		conto.setDescrizione("come da Jira: inserisco nel " + req.getBilancio().getAnno());
		
		conto.setLivello(7);
		
		PianoDeiConti pianoDeiConti = new PianoDeiConti();
		pianoDeiConti.setUid(27);
		conto.setPianoDeiConti(pianoDeiConti);
		
//		List<TipoConto> tipiConto = ricercaCodifiche(TipoConto.class);
//		conto.setTipoConto(tipiConto.get(1));
		conto.setTipoConto(create(TipoConto.class, 50));
		
		conto.setContoAPartite(Boolean.FALSE);
		//Attributi sulla siac_r_pdce_conto_attr
		conto.setAttivo(Boolean.TRUE); //Impostato dal servizio
		conto.setContoDiLegge(Boolean.TRUE);
		conto.setCodiceInterno("codice interno in fase di inserimento");
		conto.setContoFoglia(Boolean.TRUE);
		
//		if(conto.getContoFoglia()){
//			CodiceBilancio codiceBilancio = new CodiceBilancio();
//			codiceBilancio.setUid(75606745);
//			conto.setCodiceBilancio(codiceBilancio);
//			
//			ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
//			elementoPianoDeiConti.setUid(129010);
//			conto.setElementoPianoDeiConti(elementoPianoDeiConti);
//		}
		
		Conto contoPadre = new Conto();
		contoPadre.setUid(38328);
		conto.setContoPadre(contoPadre);

		req.setConto(conto);
		
		InserisceContoResponse res = inserisceContoService.executeService(req);
		assertNotNull(res);

	}
	
	
	/**
	 * Aggiorna conto.
	 */
	@Test
	public void aggiornaConto() {
		
		Bilancio bilancio ;
//      bilancio = getBilancioTest(105,2015);
//		bilancio = getBilancioTest(131,2017);
        bilancio = getBilancioTest(48,2016);
		
		RicercaDettaglioConto req = new RicercaDettaglioConto();
		req.setBilancio(bilancio);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		
		//req.setContoModelDetails(ContoModelDetail.Soggetto);
		
		Conto conto = new Conto();
		conto.setUid(1833853); //118 //108, //113
		req.setConto(conto);
		
		
		
		RicercaDettaglioContoResponse res = ricercaDettaglioContoService.executeService(req);
		assertNotNull(res);
		assertNotNull(res.getConto());
		
		Conto contoDaAggiornare = res.getConto();
		
		//WORKAROUND NEL CASO DDI BILANCIO vecchio
		if(bilancio.getAnno() < 2016) {
			conto.setAttivo(Boolean.TRUE); //Impostato dal servizio
			conto.setContoDiLegge(Boolean.TRUE);
			conto.setCodiceInterno("codice interno in fase di inserimento");
			conto.setContoFoglia(Boolean.TRUE);
		}
		
		
		contoDaAggiornare.setDescrizione(contoDaAggiornare.getDescrizione() + " -  aggiorno nel " + bilancio.getAnno());
		
		//aggiungo la codifica di bilancio
		CodiceBilancio codiceBilancio = new CodiceBilancio();
		//2017
//		codiceBilancio.setUid(75606745);
		//2016
		codiceBilancio.setUid(75606774);
		//2015
//		codiceBilancio.setUid(75606785);
//		codiceBilancio.setDescrizione("CE - B - 9 - Acquisto di materie prime e/o beni di consumo");		
		contoDaAggiornare.setCodiceBilancio(codiceBilancio);
		
		AggiornaConto reqAC = new AggiornaConto();
		reqAC.setConto(conto);
		reqAC.setBilancio(bilancio);
		reqAC.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		//reqAC.setDataOra(new Date());
		
		AggiornaContoResponse resAgg = aggiornaContoService.executeService(reqAC);
		assertNotNull(resAgg);
		Conto contoAggiornato = resAgg.getConto();
		log.logXmlTypeObject(contoAggiornato, "conto aggiornato");
	}
	
	@Test
	public void aggiornaContoByXml() {
		
	}
	
	
	
	@Test
	public void ricercaDettaglioConto() {
		
		RicercaDettaglioConto req = new RicercaDettaglioConto();
		req.setBilancio(getBilancioTest(132,2018));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		//req.setContoModelDetails(ContoModelDetail.Soggetto);
		
		Conto conto = new Conto();
		conto.setUid(123889); //118 //108, //113
		
		req.setConto(conto);
		
		RicercaDettaglioContoResponse res = ricercaDettaglioContoService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void ricercaSinteticaConto() {
		
		RicercaSinteticaConto req = new RicercaSinteticaConto();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		Conto conto = new Conto();
		conto.setAmbito(Ambito.AMBITO_FIN);
		conto.setAmmortamento(null);
		conto.setContoFoglia(Boolean.TRUE);
		conto.setAttivo(Boolean.TRUE);
		TipoConto tc = new TipoConto();
		tc.setCodice("CES");
//		conto.setTipoConto(tc);
		conto.setPianoDeiConti(new PianoDeiConti());
		conto.getPianoDeiConti().setClassePiano(new ClassePiano());
		conto.getPianoDeiConti().getClassePiano().setUid(189);
		
		
//		conto.setCodice("AP");
		
		req.setConto(conto);
		req.setBilancio(getBilancioTest(131,2017));
		
		RicercaSinteticaContoResponse res = ricercaSinteticaContoService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void annullaConto() {
		
		AnnullaConto req = new AnnullaConto();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		req.setBilancio(getBilancio2015Test());
		
		//req.setContoModelDetails(ContoModelDetail.Soggetto);
		
		Conto conto = new Conto();
		conto.setUid(12); //108, //113
		
		req.setConto(conto);
		
		AnnullaContoResponse res = annullaContoService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void ricercaSinteticaContoFigli() {
		
		RicercaSinteticaContoFigli req = new RicercaSinteticaContoFigli();
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setParametriPaginazione(new ParametriPaginazione(1, 2));
		req.setBilancio(getBilancioTest(143, 2017));
		
		Conto conto = new Conto();
		conto.setAmbito(Ambito.AMBITO_FIN);
		
//		conto.setPianoDeiConti(new PianoDeiConti());
//		conto.getPianoDeiConti().setClassePiano(new ClassePiano());
//		conto.getPianoDeiConti().getClassePiano().setUid(30);;
//		
//		conto.setCodiceInterno("NON FILTRATO PER ORA ;(");
		conto.setCodice("2" ); //"RE001002"
		conto.setCodiceInterno("");
		conto.setPianoDeiConti(create(PianoDeiConti.class, 0));
		conto.getPianoDeiConti().setClassePiano(create(ClassePiano.class, 209));
		
		req.setConto(conto);
		
		RicercaSinteticaContoFigliResponse res = ricercaSinteticaContoFigliService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void ricercaFigliRicorsiva(){
		String methodName = "ricercaFigliRicorsiva";
		List<SiacTPdceConto> siacTPdceContos = contoDao.ricercaFigliRicorsiva(12);
		
		StringBuilder sb = new StringBuilder();
		for(SiacTPdceConto siacTPdceConto : siacTPdceContos){
			sb.append("uid: ").append(siacTPdceConto.getUid()).append(" [").append(siacTPdceConto.getPdceContoCode()).append("], ");
		}
		
		log.debug(methodName, sb.toString());
		
	}
	
	
	@SuppressWarnings("unchecked")
	private <T extends Codifica> List<T> ricercaCodifiche(Class<T> codificaClass) {
		final String methodName = "ricercaCodifiche";
		RicercaCodifiche req = new RicercaCodifiche();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
				
		req.addTipiCodifica(codificaClass);
		
		RicercaCodificheResponse resp = ricercaCodificheService.executeService(req);
		
		assertNotNull(resp);
		
		List<T> codifiche = resp.getCodifiche(codificaClass);
		
		assertFalse("Nessuna codifica "+codificaClass.getSimpleName()+" trovata.", codifiche.isEmpty());
		
		for (T codifica : codifiche) {
			log.debug(methodName, codifica.getCodice() + " - "+ codifica.getDescrizione());
		}
		
		return codifiche;
	}
	
	@Test
	public void testCheckCodiceConto() {
		contoDad.setEnte(getRichiedenteByProperties("consip", "regp").getAccount().getEnte());
		contoDad.setLoginOperazione("admin_junit");
		Conto contoTrovato = contoDad.ricercaContoByCodice("2.1.2.02.01", 2016, Ambito.AMBITO_FIN);
	}
	
	

}
