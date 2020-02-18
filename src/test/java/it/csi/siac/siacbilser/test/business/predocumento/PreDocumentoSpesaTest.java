/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.predocumento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.predocumentospesa.AggiornaPreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AggiornaStatoPreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.InseriscePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaDettaglioPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaSinteticaPreDocumentoSpesaService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoSpesaMVTest.
 */
public class PreDocumentoSpesaTest extends BaseJunit4TestCase {
	
	/** The aggiorna stato pre documento di spesa service. */
	@Autowired
	private AggiornaStatoPreDocumentoDiSpesaService aggiornaStatoPreDocumentoDiSpesaService;
	@Autowired
	private RicercaSinteticaPreDocumentoSpesaService ricercaSinteticaPreDocumentoSpesaService;
	@Autowired
	private InseriscePreDocumentoSpesaService inseriscePreDocumentoSpesaService;
	@Autowired
	private DefiniscePreDocumentoDiSpesaService definiscePreDocumentoDiSpesaService;
	@Autowired
	private RicercaDettaglioPreDocumentoSpesaService ricercaDettaglioPreDocumentoSpesaService;
	@Autowired
	private AggiornaPreDocumentoDiSpesaService aggiornaPreDocumentoDiSpesaService;
	
	
	/**
	 * Aggiorna stato pre documento di spesa.
	 */
	@Test
	public void aggiornaStatoPreDocumentoDiSpesa() {
		
		PreDocumentoSpesa preDocumentoSpesa = new PreDocumentoSpesa();
		preDocumentoSpesa.setEnte(getEnteTest());
		preDocumentoSpesa.setUid(131);
				
		AggiornaStatoPreDocumentoDiSpesa req = new AggiornaStatoPreDocumentoDiSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setPreDocumentoSpesa(preDocumentoSpesa);
						
		AggiornaStatoPreDocumentoDiSpesaResponse res = aggiornaStatoPreDocumentoDiSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaPreDocumentoSpesa() {
		RicercaSinteticaPreDocumentoSpesa req = new RicercaSinteticaPreDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setAnnoBilancio(Integer.valueOf(2018));
		
		PreDocumentoSpesa preDocumentoSpesa = new PreDocumentoSpesa();
		preDocumentoSpesa.setEnte(req.getRichiedente().getAccount().getEnte());
		preDocumentoSpesa.setNumero(Integer.valueOf(47311));
		req.setPreDocumentoSpesa(preDocumentoSpesa);
		
//		req.setNonAnnullati(Boolean.TRUE);

		RicercaSinteticaPreDocumentoSpesaResponse res = ricercaSinteticaPreDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inseriscePredocumentoSpesa(){
		InseriscePreDocumentoSpesa req = new InseriscePreDocumentoSpesa();
		
		Calendar cal = Calendar.getInstance();
		cal.set(2015, 01, 01, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancioTest(16, 2015));
		//req.setInserisciElenco(true);
		
		PreDocumentoSpesa preDocumentoSpesa = new PreDocumentoSpesa();
		req.setPreDocumentoSpesa(preDocumentoSpesa);
		
		preDocumentoSpesa.setEnte(req.getRichiedente().getAccount().getEnte());
		preDocumentoSpesa.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDocumentoSpesa.setDataDocumento(cal.getTime());
		preDocumentoSpesa.setDataCompetenza(cal.getTime());
		preDocumentoSpesa.setPeriodoCompetenza("201501");
		preDocumentoSpesa.setDescrizione("Test inserimento elenco contestuale");
		preDocumentoSpesa.setCausaleSpesa(create(CausaleSpesa.class, 68));
		preDocumentoSpesa.setContoTesoreria(create(ContoTesoreria.class, 5));
		preDocumentoSpesa.setImporto(new BigDecimal("1.01"));
		preDocumentoSpesa.setElencoDocumentiAllegato(create(ElencoDocumentiAllegato.class, 417));
		
		InseriscePreDocumentoSpesaResponse res = inseriscePreDocumentoSpesaService.executeService(req); 
		assertNotNull(res);

	}
	
	@Test
	public void definiscePreDocumentoDiSpesa() {
		DefiniscePreDocumentoDiSpesa req = new DefiniscePreDocumentoDiSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancio2015Test());
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setIdOperazioneAsincrona(Integer.valueOf(925));
		
		List<PreDocumentoSpesa> preDocumentiSpesa = new ArrayList<PreDocumentoSpesa>();
		req.setPreDocumentiSpesa(preDocumentiSpesa);
		
		PreDocumentoSpesa pds = new PreDocumentoSpesa();
		pds.setUid(132);
		preDocumentiSpesa.add(pds);
		
		definiscePreDocumentoDiSpesaService.executeService(req);
	}
	
	
	//ANTO
	@Test
	public void ricercaDettaglioPreDocumentoSpesa() {
		RicercaDettaglioPreDocumentoSpesa req = new RicercaDettaglioPreDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setPreDocumentoSpesa(create(PreDocumentoSpesa.class, 5257));
		
		
		
		
		RicercaDettaglioPreDocumentoSpesaResponse res = ricercaDettaglioPreDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	
	
	@Test
	public void aggiornaPredocumentoDiSpesa(){
		AggiornaPreDocumentoDiSpesa req = new AggiornaPreDocumentoDiSpesa();
		
		Calendar cal = Calendar.getInstance();
		cal.set(2015, 01, 01, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancioTest(16, 2015));
		//req.setInserisciElenco(true);
		
		PreDocumentoSpesa preDocumentoSpesa = create(PreDocumentoSpesa.class, 569);
		// 570
		req.setPreDocumentoSpesa(preDocumentoSpesa);
		
		preDocumentoSpesa.setNumero(Integer.valueOf(560));
		preDocumentoSpesa.setEnte(req.getRichiedente().getAccount().getEnte());
		preDocumentoSpesa.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDocumentoSpesa.setDataDocumento(cal.getTime());
		preDocumentoSpesa.setDataCompetenza(cal.getTime());
		preDocumentoSpesa.setPeriodoCompetenza("201501");
		preDocumentoSpesa.setDescrizione("Test inserimento elenco contestuale");
		preDocumentoSpesa.setCausaleSpesa(create(CausaleSpesa.class, 68));
		preDocumentoSpesa.setContoTesoreria(create(ContoTesoreria.class, 5));
		preDocumentoSpesa.setImporto(new BigDecimal("1.01"));
//		preDocumentoSpesa.setElencoDocumentiAllegato(create(ElencoDocumentiAllegato.class, 417));
		
		AggiornaPreDocumentoDiSpesaResponse res = aggiornaPreDocumentoDiSpesaService.executeService(req); 
		assertNotNull(res);

	}
}
