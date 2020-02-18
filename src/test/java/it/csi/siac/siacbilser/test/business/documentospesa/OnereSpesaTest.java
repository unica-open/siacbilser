/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentospesa;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaOnereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaOnereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisciOnereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaOnereByDocumentoSpesaService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoOnere;

// TODO: Auto-generated Javadoc
/**
 * The Class OnereSpesaDLTest.
 */
public class OnereSpesaTest extends BaseJunit4TestCase {
	
	
	/** The inserisci onere spesa service. */
	@Autowired
	private InserisciOnereSpesaService inserisciOnereSpesaService;
	
	/** The aggiorna onere spesa service. */
	@Autowired
	private AggiornaOnereSpesaService aggiornaOnereSpesaService;
	
	/** The ricerca onere by documento spesa service. */
	@Autowired
	private RicercaOnereByDocumentoSpesaService ricercaOnereByDocumentoSpesaService;
	
	@Autowired
	private EliminaOnereSpesaService eliminaOnereSpesaService;
	
	/**
	 * Ricerca onere by documento spesa.
	 */
	@Test
	public void ricercaOnereByDocumentoSpesa() {
		
		RicercaOnereByDocumentoSpesa req = new RicercaOnereByDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(27961);
		req.setDocumentoSpesa(documentoSpesa);
		
		RicercaOnereByDocumentoSpesaResponse response = ricercaOnereByDocumentoSpesaService.executeService(req);
		for (DettaglioOnere de : response.getListaDettagliOnere()) {
			System.out.println("importo a carico soggetto: " + de.getImportoCaricoSoggetto());
			System.out.println("importo imponibile " + de.getImportoImponibile());
		}
	}
	
	/**
	 * Inserisci onere spesa.
	 */
	@Test
	public void inserisciOnereSpesa() {
		InserisciOnereSpesa req = new InserisciOnereSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		
		req.setDettaglioOnere(create(DettaglioOnere.class, 0));
		req.getDettaglioOnere().setCausale770(create(Causale770.class, 270));
		req.getDettaglioOnere().setImportoCaricoEnte(new BigDecimal("0.00"));
		req.getDettaglioOnere().setImportoCaricoSoggetto(new BigDecimal("0.12"));
		req.getDettaglioOnere().setImportoImponibile(new BigDecimal("0.50"));
		req.getDettaglioOnere().setTipoOnere(create(TipoOnere.class, 479));
		req.getDettaglioOnere().getTipoOnere().setNaturaOnere(create(NaturaOnere.class, 3));
		req.getDettaglioOnere().getTipoOnere().setQuadro770("C");
		req.getDettaglioOnere().getTipoOnere().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getDettaglioOnere().setDocumentoSpesa(create(DocumentoSpesa.class, 73985));
		
		InserisciOnereSpesaResponse res = inserisciOnereSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	
	/**
	 * Aggiorna onere spesa.
	 */
	@Test
	public void aggiornaOnereSpesa() {
		AggiornaOnereSpesa req = new AggiornaOnereSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		
		req.setDettaglioOnere(create(DettaglioOnere.class, 8051));
		req.getDettaglioOnere().setCausale770(create(Causale770.class, 270));
		req.getDettaglioOnere().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getDettaglioOnere().setImportoCaricoEnte(new BigDecimal("0.00"));
		req.getDettaglioOnere().setImportoCaricoSoggetto(new BigDecimal("1.15"));
		req.getDettaglioOnere().setImportoImponibile(new BigDecimal("5.00"));
		req.getDettaglioOnere().setTipoOnere(create(TipoOnere.class, 479));
		req.getDettaglioOnere().getTipoOnere().setNaturaOnere(create(NaturaOnere.class, 3));
		req.getDettaglioOnere().getTipoOnere().setQuadro770("C");
		req.getDettaglioOnere().getTipoOnere().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getDettaglioOnere().setDocumentoSpesa(create(DocumentoSpesa.class, 73985));
		
		AggiornaOnereSpesaResponse res = aggiornaOnereSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void eliminaOnereSpesa(){
		EliminaOnereSpesa req = new EliminaOnereSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		DettaglioOnere dettaglioOnere = new DettaglioOnere();
		dettaglioOnere.setUid(161);
		req.setDettaglioOnere(dettaglioOnere );
		EliminaOnereSpesaResponse res = eliminaOnereSpesaService.executeService(req);
		assertNotNull(res);
	}

	

}
