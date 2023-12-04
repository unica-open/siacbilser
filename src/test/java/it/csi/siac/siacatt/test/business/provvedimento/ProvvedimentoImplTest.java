/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.provvedimento;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.service.provvedimento.AggiornaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.InserisceProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.RicercaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.TipiProvvedimentoService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * The Class ProvvedimentoImplDLTest.
 */
public class ProvvedimentoImplTest extends BaseJunit4TestCase {
	private static final LogSrvUtil log = new LogSrvUtil(ProvvedimentoImplTest.class);
	
	/** The inserisce service. */
	@Autowired
	private InserisceProvvedimentoService inserisceService;
	
	/** The ricerca service. */
	@Autowired
	private RicercaProvvedimentoService ricercaService;
	
	/** The aggiorna service. */
	@Autowired
	private AggiornaProvvedimentoService aggiornaService;
	
	/** The tipi provvedimento service. */
	@Autowired
	private TipiProvvedimentoService tipiProvvedimentoService;

	
	//private GestioneCapitoloUscitaPrevisione gestioneCapitoloUscitaPrevisione; //Non più necessario
	
	

	/*@Before //Non più necessario
	public void setUp()
	{
		gestioneCapitoloUscitaPrevisioneImpl = (GestioneCapitoloUscitaPrevisione) applicationContext
				.getBean("gestioneCapitoloUscitaPrevisioneImpl");
	}*/

	/**
	 * Test create.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCreate() {
		InserisceProvvedimento req = new InserisceProvvedimento();
		req.setDataOra(new Date());
		Ente ente = getEnteTest();
		req.setEnte(ente);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		
		
		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
		attoAmministrativo.setAnno(2015);
		attoAmministrativo.setNumero(4005);
		//attoAmministrativo.setNote("note test "+attoAmministrativo.getNumero());
		attoAmministrativo.setOggetto("Test per SIAC-4005, senza SAC");
		attoAmministrativo.setStato(StatoEntita.VALIDO);
		attoAmministrativo.setStatoOperativo(StatoOperativoAtti.PROVVISORIO.getDescrizione());
		
		req.setAttoAmministrativo(attoAmministrativo);
	
		
//		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
//		strutturaAmministrativoContabile.setUid(502); //direzione generale
//		req.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile );
		
		TipoAtto tipoAtto = new TipoAtto();
		//tipoAtto.setUid(3); //determina con Movimento Interno
		tipoAtto.setUid(32);
		req.setTipoAtto(tipoAtto);
		
		InserisceProvvedimentoResponse res = inserisceService.executeService(req);
		assertNotNull(res);
	}
	
	
	/**
	 * Test create movimento interno.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCreateMovimentoInterno() {
		InserisceProvvedimento req = new InserisceProvvedimento();
		
		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
		attoAmministrativo.setAnno(2013);
//			attoAmministrativo.setNumero(4);
		attoAmministrativo.setNote("movimento interno 3");
		attoAmministrativo.setOggetto("oggetto movimento interno 3");
		attoAmministrativo.setStato(StatoEntita.VALIDO);
		attoAmministrativo.setStatoOperativo(StatoOperativoAtti.DEFINITIVO.getDescrizione());
		
		req.setAttoAmministrativo(attoAmministrativo);
		req.setDataOra(new Date());
		Ente ente = getEnteTest();
		req.setEnte(ente);
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile("codice", "descrizione");
		strutturaAmministrativoContabile.setUid(1);
		req.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile );
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setUid(3);
		req.setTipoAtto(tipoAtto);
		
		inserisceService.executeService(req);
	}
	
	/**
	 * Test create esistente.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCreateEsistente() {
		InserisceProvvedimento req = new InserisceProvvedimento();
		
		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
		attoAmministrativo.setAnno(2013);
		attoAmministrativo.setNumero(4);
		attoAmministrativo.setNote("note");
		attoAmministrativo.setOggetto("oggetto");
		attoAmministrativo.setStato(StatoEntita.VALIDO);
		attoAmministrativo.setStatoOperativo("stato1");
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setCodice("tipo1");
		attoAmministrativo.setTipoAtto(tipoAtto);
		
		req.setAttoAmministrativo(attoAmministrativo);
		req.setDataOra(new Date());
		Ente ente = getEnteTest();
		req.setEnte(ente);
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile("codice", "descrizione");
		strutturaAmministrativoContabile.setUid(1);
		req.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile );
//			TipoAtto tipoAtto = new TipoAtto("tipo1", "");
		tipoAtto.setUid(1);
		req.setTipoAtto(tipoAtto);
		
		inserisceService.executeService(req);
	}
	
	/**
	 * Test update.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testUpdate() {
		AggiornaProvvedimento req = new AggiornaProvvedimento();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setTipoAtto(create(TipoAtto.class, 306));
		
		req.setAttoAmministrativo(create(AttoAmministrativo.class, 43988));
		req.getAttoAmministrativo().setAnno(2017);
		req.getAttoAmministrativo().setNote("dsa");
		req.getAttoAmministrativo().setNumero(1);
		req.getAttoAmministrativo().setOggetto("das");
		req.getAttoAmministrativo().setParereRegolaritaContabile(Boolean.FALSE);
		req.getAttoAmministrativo().setStrutturaAmmContabile(create(StrutturaAmministrativoContabile.class, 0));
		req.getAttoAmministrativo().setTipoAtto(create(TipoAtto.class, 306));
		
		AggiornaProvvedimentoResponse res = aggiornaService.executeService(req);
		assertNotNull(res);
	}
	
	
	/**
	 * Test update movimento interno.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testUpdateMovimentoInterno() {
		AggiornaProvvedimento req = new AggiornaProvvedimento();
		
		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
		attoAmministrativo.setNote("Movimento interno 1 modificato");
		attoAmministrativo.setOggetto("oggetto Movimento interno 1 modificato");
		attoAmministrativo.setStato(StatoEntita.VALIDO);
		attoAmministrativo.setStatoOperativo("DEFINITIVO");
		attoAmministrativo.setUid(37);
		StrutturaAmministrativoContabile strutturaAmmContabile = new StrutturaAmministrativoContabile();
		strutturaAmmContabile.setUid(358);
		req.setStrutturaAmministrativoContabile(strutturaAmmContabile);
		TipoAtto tipoAtto = new TipoAtto("", "");
		tipoAtto.setUid(3);
		req.setTipoAtto(tipoAtto);
		
		req.setAttoAmministrativo(attoAmministrativo);
		req.setDataOra(new Date());
		Ente ente = getEnteTest();
		req.setEnte(ente);
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		aggiornaService.executeService(req);
	}
	
	/**
	 * Test ricerca.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicerca() {
		RicercaProvvedimento req = new RicercaProvvedimento();
		
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setRicercaAtti(new RicercaAtti());
		req.getRicercaAtti().setNumeroAtto(Integer.valueOf(388));
		
		//ricercaAtti.setUid(2);
//		ricercaAtti.setStatoOperativo(StatoOperativoAtti.DEFINITIVO.name());
//		attoAmministrativo.setUid(-37);
//		ricercaAtti.setAnnoAtto(2015);
//		ricercaAtti.setNumeroAtto(4);
//		TipoAtto tipoAtto = new TipoAtto();
//		tipoAtto.setUid(1);
//		tipoAtto.setCodice("1");
//		ricercaAtti.setTipoAtto(tipoAtto);
//		attoAmministrativo.setOggetto("oggetto");
//		attoAmministrativo.setNote("note");
		
//		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
//		strutturaAmministrativoContabile.setUid(342);
		
//		ricercaAtti.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		
//		Ente ente = getEnteTest();
//		req.setEnte(ente);
//		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
//		req.setRichiedente(richiedente);
//		
//		ricercaAtti.setConDocumentoAssociato(false);
//		
//		req.setRicercaAtti(ricercaAtti);
		
		
		RicercaProvvedimentoResponse res = ricercaService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void testRicercaAggiorna() {
		final String methodName = "testRicercaAggiorna";
		int uidAtto = 161165;
		RicercaProvvedimento req = new RicercaProvvedimento();
		
		req.setAnnoBilancio(Integer.valueOf(2022));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setRicercaAtti(new RicercaAtti());
		req.getRicercaAtti().setUid(uidAtto);
		
		RicercaProvvedimentoResponse res = ricercaService.executeService(req);
		
		if(res.hasErrori() || res.getListaAttiAmministrativi() == null || res.getListaAttiAmministrativi().isEmpty()) {
			log.error(methodName, "atto non trovato");
			return;
		}
		
		AggiornaProvvedimento reqAgg = new AggiornaProvvedimento();
		
		AttoAmministrativo attoAmministrativo = res.getListaAttiAmministrativi().get(0);
//		attoAmministrativo.setStatoOperativo(StatoOperativoAtti.DEFINITIVO);
//        reqAgg.setIsEsecutivo(Boolean.TRUE);
		reqAgg.setAttoAmministrativo(attoAmministrativo);
		reqAgg.setTipoAtto(attoAmministrativo.getTipoAtto());
		reqAgg.setDataOra(new Date());
		
		reqAgg.setAnnoBilancio(Integer.valueOf(2022));
		reqAgg.setDataOra(new Date());
		reqAgg.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqAgg.setEnte(req.getRichiedente().getAccount().getEnte());

		
		aggiornaService.executeService(reqAgg);
		
		
	}
	
	/**
	 * Test ricerca strutt amm cont.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testRicercaStruttAmmCont() {
		final String methodName = "testRicercaStruttAmmCont";
		RicercaProvvedimento req = new RicercaProvvedimento();
		
		RicercaAtti attoAmministrativo = new RicercaAtti();
//		attoAmministrativo.setUid(-37);
		attoAmministrativo.setAnnoAtto(2013);
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setUid(1);
		attoAmministrativo.setTipoAtto(tipoAtto);
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile.setUid(358);
		attoAmministrativo.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		Ente ente = getEnteTest();
		req.setEnte(ente);
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		req.setRicercaAtti(attoAmministrativo);
		
		
		RicercaProvvedimentoResponse res = ricercaService.executeService(req);
		
		log.info(methodName, res.getListaAttiAmministrativi());
	}
	
	/**
	 * Test tipi.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testTipi() {
		TipiProvvedimento req = new TipiProvvedimento();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		req.setRichiedente(richiedente);
		
		tipiProvvedimentoService.executeService(req);
	}
	
	@Test
	public void aggiornaProvvedimento() {
		RicercaProvvedimento reqRicerca = new RicercaProvvedimento();
		
		reqRicerca.setAnnoBilancio(Integer.valueOf(2017));
		reqRicerca.setDataOra(new Date());
		reqRicerca.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqRicerca.setEnte(reqRicerca.getRichiedente().getAccount().getEnte());
		
		reqRicerca.setRicercaAtti(new RicercaAtti());
		reqRicerca.getRicercaAtti().setNumeroAtto(Integer.valueOf(67543));
		reqRicerca.getRicercaAtti().setAnnoAtto(2017);
		RicercaProvvedimentoResponse responseRicerca = ricercaService.executeService(reqRicerca);
		assertNotNull(responseRicerca.getListaAttiAmministrativi());
		assertTrue("ricerca fallita", responseRicerca.getListaAttiAmministrativi().size()!= 0);
		
		AttoAmministrativo attoAmministrativoDaAggiornare = null;
		
		for (AttoAmministrativo attoAmministrativo : responseRicerca.getListaAttiAmministrativi()) {
			if(attoAmministrativo.getUid() == 44127) {
				attoAmministrativoDaAggiornare = attoAmministrativo;
				break;
			}
		}
		assertNotNull(attoAmministrativoDaAggiornare);
		
		AggiornaProvvedimento req = new AggiornaProvvedimento();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		attoAmministrativoDaAggiornare.setStatoOperativo(StatoOperativoAtti.DEFINITIVO.getDescrizione());
		attoAmministrativoDaAggiornare.setStatoOperativo(StatoOperativoAtti.DEFINITIVO);
		
		req.setAttoAmministrativo(attoAmministrativoDaAggiornare);
		req.setTipoAtto(attoAmministrativoDaAggiornare.getTipoAtto());
		
		AggiornaProvvedimentoResponse res = aggiornaService.executeService(req);
		assertNotNull(res);
		
	}
	
}
