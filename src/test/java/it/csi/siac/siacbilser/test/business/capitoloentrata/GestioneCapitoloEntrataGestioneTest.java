/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitoloentratagestione.AggiornamentoMassivoCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaSinteticaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.VerificaAnnullabilitaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.VerificaEliminabilitaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class GestioneCapitoloEntrataGestioneAMTest.
 */
public class GestioneCapitoloEntrataGestioneTest extends BaseJunit4TestCase {
	
	/** The ricerca sintetica capitolo entrata gestione service. */
	@Autowired
	private RicercaSinteticaCapitoloEntrataGestioneService ricercaSinteticaCapitoloEntrataGestioneService;
	@Autowired
	private AggiornamentoMassivoCapitoloEntrataGestioneService aggiornamentoMassivoCapitoloEntrataGestioneService;
	
	@Autowired
	private SiacTClassDao siacTClassDao;
	
	@Test
	public void ricercaSintetica() {
		RicercaSinteticaCapitoloEntrataGestione request = new RicercaSinteticaCapitoloEntrataGestione();
		
		request.setEnte(getEnteTest());
		request.setParametriPaginazione(new ParametriPaginazione(0, 100));
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setDataOra(new Date());
		
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloEntrata = new RicercaSinteticaCapitoloEGest();
		ricercaSinteticaCapitoloEntrata.setCodiceTitoloEntrata("1");
		ricercaSinteticaCapitoloEntrata.setCodiceTipologia("1010100");
		//ricercaSinteticaCapitoloEntrata.setCodiceCategoria("03");
		ricercaSinteticaCapitoloEntrata.setAnnoEsercizio(2013);
		request.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloEntrata);
		
		RicercaSinteticaCapitoloEntrataGestioneResponse response = ricercaSinteticaCapitoloEntrataGestioneService.executeService(request);
		
		assertNotNull(response);
		assertTrue("Not empty error list", response.getErrori().isEmpty());
		assertTrue("Empty list", !response.getCapitoli().isEmpty());
	}
	
	@Test
	public void findFigliClassificatore() {
		List<SiacTClass> classes = siacTClassDao.findFigliClassificatore("2014", SiacDClassTipoEnum.TitoloEntrata, 2, "1", "1010100"/*, "1010117"*/);
		assertNotNull(classes);
		assertEquals(classes.isEmpty(), false);
		for(SiacTClass stc : classes) {
			log.debug("findFigliClassificatore", "SiacTClass - uid: " + stc.getUid() + " | codice: " + stc.getClassifCode() + " | descrizione: "
					+ stc.getClassifDesc() + " | tipo: " + stc.getSiacDClassTipo().getClassifTipoCode());
		}
	}
	
	@Test
	public void aggiornamentoMassivoCapitoloEntrataGestione() {
		AggiornaMassivoCapitoloDiEntrataGestione req = getTestFileObject(AggiornaMassivoCapitoloDiEntrataGestione.class, "it/csi/siac/siacbilser/test/business/capitoloentrata/aggiornaMassivoCapitoloDiEntrataGestione.xml");
		
		AggiornaMassivoCapitoloDiEntrataGestioneResponse res = aggiornamentoMassivoCapitoloEntrataGestioneService.executeService(req);
		assertNotNull(res);
	}
	
	@Autowired
	private VerificaAnnullabilitaCapitoloEntrataGestioneService verificaAnnullabilitaCapitoloEntrataGestioneService;
	@Test
	public void verificaAnnullabilitaCapitoloEntrataGestione() {
		
		
		VerificaAnnullabilitaCapitoloEntrataGestione req = new VerificaAnnullabilitaCapitoloEntrataGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteTest("RMNLSS", 1, 1);
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest(); //getBilancioTest(1, 2015);
		req.setBilancio(bilancio);
		
		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setUid(31036);
		capitoloEntrataGestione.setAnnoCapitolo(2015);
		capitoloEntrataGestione.setNumeroCapitolo(3355);
		capitoloEntrataGestione.setNumeroArticolo(1);
		capitoloEntrataGestione.setNumeroUEB(1);
		capitoloEntrataGestione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		//capitoloEntrataGestione.setUid(95536);
		req.setCapitolo(capitoloEntrataGestione);
		
		VerificaAnnullabilitaCapitoloEntrataGestioneResponse res = verificaAnnullabilitaCapitoloEntrataGestioneService.executeService(req);
		assertNotNull(res);
		//log.logXmlTypeObject(res, "RESPONSE");//log.logXmlTypeObject();
		
	}
	
	@Autowired
	private VerificaEliminabilitaCapitoloEntrataGestioneService verificaEliminabilitaCapitoloEntrataGestioneService;
	@Test
	public void verificaEliminabilitaCapitoloEntrataGestione() {
		
		
		VerificaEliminabilitaCapitoloEntrataGestione req = new VerificaEliminabilitaCapitoloEntrataGestione();
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		
		Richiedente richiedente = getRichiedenteTest("RMNLSS", 1, 1);
		req.setRichiedente(richiedente);
		
		Bilancio bilancio = getBilancioTest(); //getBilancioTest(1, 2015);
		req.setBilancio(bilancio);
		
		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setUid(31037);
		capitoloEntrataGestione.setAnnoCapitolo(2015);
		capitoloEntrataGestione.setNumeroCapitolo(3355);
		capitoloEntrataGestione.setNumeroArticolo(2);
		capitoloEntrataGestione.setNumeroUEB(1);
		capitoloEntrataGestione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		//capitoloEntrataGestione.setUid(95536);
		req.setCapitoloEntrataGest(capitoloEntrataGestione);
		
		VerificaEliminabilitaCapitoloEntrataGestioneResponse res = verificaEliminabilitaCapitoloEntrataGestioneService.executeService(req);
		assertNotNull(res);
		//log.logXmlTypeObject(res, "RESPONSE");//log.logXmlTypeObject();
		
	}

}
