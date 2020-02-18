/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentoentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaNotaCreditoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AnnullaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceDocumentoPerProvvisoriEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceNotaCreditoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaDettaglioDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaModulareDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuoteDaEmettereEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaSinteticaModulareQuoteEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.SpezzaQuotaEntrataService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentoEntrataMVTest.
 */
public class DocumentoEntrataTest extends BaseJunit4TestCase {

	/** The documento entrata service. */
	@Autowired
	@Qualifier("inserisceDocumentoEntrataService")
	private InserisceDocumentoEntrataService documentoEntrataService;
	
	@Autowired
	private InserisceNotaCreditoEntrataService inserisceNotaCreditoEntrataService;
	
	@Autowired
	private AggiornaNotaCreditoEntrataService aggiornaNotaCreditoEntrataService;
	
	@Autowired
	private RicercaQuoteDaEmettereEntrataService ricercaQuoteDaEmettereEntrataService;
	
	@Autowired
	private RicercaDettaglioDocumentoEntrataService ricercaDettaglioDocumentoEntrataService;
	
	@Autowired
	private RicercaModulareDocumentoEntrataService ricercaModulareDocumentoEntrataService;
	@Autowired
	private InserisceDocumentoPerProvvisoriEntrataService inserisceDocumentoPerProvvisoriEntrataService;
	@Autowired
	private AnnullaDocumentoEntrataService annullaDocumentoEntrataService;
	@Autowired
	private SpezzaQuotaEntrataService spezzaQuotaEntrataService;
	@Autowired
	private RicercaSinteticaModulareQuoteEntrataService ricercaSinteticaModulareQuoteEntrataService;

	/**
	 * Inserisci documento entrata.
	 */
	@Test
	public void inserisciDocumentoEntrata() {

		InserisceDocumentoEntrata req = new InserisceDocumentoEntrata();
		
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(6);
		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = new FaseEStatoAttualeBilancio();
		faseEStatoAttualeBilancio.setFaseBilancio(FaseBilancio.GESTIONE);
		bilancio.setFaseEStatoAttualeBilancio(faseEStatoAttualeBilancio );
		req.setBilancio(bilancio);
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoEntrata doc = new DocumentoEntrata();
		doc.setEnte(getEnteTest());
		doc.setAnno(2014);
		doc.setNumero("DOC-ENT-XXX-2");
		doc.setDescrizione("questa è la volta buona");
		doc.setImporto(new BigDecimal("5000"));
		doc.setDataEmissione(new Date());
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);

		
		// attributi
//		doc.setNumeroRepertorio("24");
//		doc.setDataRepertorio(new Date());
//		doc.setNote("mie note documento");
//		doc.setArrotondamento(new BigDecimal("-10"));
//		doc.setTerminePagamento(33);

		CodiceBollo codiceBollo = new CodiceBollo();
		codiceBollo.setUid(1);
		doc.setCodiceBollo(codiceBollo);

		TipoDocumento tipoDoc = new TipoDocumento();
		tipoDoc.setUid(32);//27
		doc.setTipoDocumento(tipoDoc);

		Soggetto soggetto = new Soggetto();
		// soggetto.setUid(1);
		soggetto.setUid(7);
		doc.setSoggetto(soggetto);

		req.setDocumentoEntrata(doc);
	

		InserisceDocumentoEntrataResponse res = documentoEntrataService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void inserisciDocumentoEntrataProvvisori() {

		InserisceDocumentoPerProvvisoriEntrata req = new InserisceDocumentoPerProvvisoriEntrata();
	
		req.setBilancio(getBilancio2015Test());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		DocumentoEntrata doc = new DocumentoEntrata();
		doc.setEnte(getEnteTest());
		doc.setAnno(2015);
		doc.setNumero("DOC-PROVV");
		doc.setDescrizione("doc entrata con provvisori");
		doc.setImporto(new BigDecimal("5000"));
		doc.setDataEmissione(new Date());
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
		TipoDocumento tipoDoc = new TipoDocumento();
		tipoDoc.setUid(32);//27
		doc.setTipoDocumento(tipoDoc);

		Soggetto soggetto = new Soggetto();
		soggetto.setUid(14);
		doc.setSoggetto(soggetto);
		
		ProvvisorioDiCassa provvisorioCassa1 = new ProvvisorioDiCassa();
		provvisorioCassa1.setUid(5959);
		ProvvisorioDiCassa provvisorioCassa2 = new ProvvisorioDiCassa();
		provvisorioCassa2.setUid(5965);
		
		SubdocumentoEntrata sub1 = new SubdocumentoEntrata();
		sub1.setProvvisorioCassa(provvisorioCassa1);
		SubdocumentoEntrata sub2 = new SubdocumentoEntrata();
		sub2.setProvvisorioCassa(provvisorioCassa2);
		
		List<SubdocumentoEntrata> listaQuote = new ArrayList<SubdocumentoEntrata>();
		listaQuote.add(sub1);
		listaQuote.add(sub2);
		req.setListaQuote(listaQuote);

		req.setDocumentoEntrata(doc);
	

		InserisceDocumentoPerProvvisoriEntrataResponse res = inserisceDocumentoPerProvvisoriEntrataService.executeService(req);

		assertNotNull(res);
	}
	
	
	@Test
	public void inserisciNotaCreditoEntrata() {
		InserisceNotaCreditoEntrata request = new InserisceNotaCreditoEntrata();
		request.setBilancio(getBilancio2014Test());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		
		DocumentoEntrata nota = new DocumentoEntrata();
		
		nota.setAnno(2014);
		nota.setNumero("NOTA-DOC-ENT-XXX-2");
		nota.setEnte(getEnteTest());
		nota.setDescrizione("nota credito test per doc entrata uid 310");
		nota.setImporto(new BigDecimal("5000"));
		nota.setDataEmissione(new Date());
		nota.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		nota.setSoggetto(soggetto);
		
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setUid(23);
		nota.setTipoDocumento(tipoDocumento);
		
		List<DocumentoEntrata> listaDocumentiEntrataPadre = new ArrayList<DocumentoEntrata>();
		DocumentoEntrata docEntrata = new DocumentoEntrata();
		docEntrata.setUid(310);
		listaDocumentiEntrataPadre.add(docEntrata);
		nota.setListaDocumentiEntrataPadre(listaDocumentiEntrataPadre);
		
		List<SubdocumentoEntrata> listaSubdocumenti = new ArrayList<SubdocumentoEntrata>();
		SubdocumentoEntrata subdocEntrata = new SubdocumentoEntrata();
		subdocEntrata.setEnte(getEnteTest());
		listaSubdocumenti.add(subdocEntrata );
		nota.setListaSubdocumenti(listaSubdocumenti );
		
		request.setDocumentoEntrata(nota);
		
		InserisceNotaCreditoEntrataResponse response = inserisceNotaCreditoEntrataService.executeService(request);
		assertNotNull(response);
	}
	
	@Test
	public void aggiornaNotaCreditoEntrata() {
		AggiornaNotaCreditoEntrata request = new AggiornaNotaCreditoEntrata();
		
		request.setBilancio(getBilancio2014Test());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		
		DocumentoEntrata nota = new DocumentoEntrata();
		nota.setUid(311);
		nota.setAnno(2014);
		nota.setNumero("NOTA-DOC-ENT-XXX-2");
		nota.setEnte(getEnteTest());
		nota.setDescrizione("nota credito test per doc entrata uid 310");
		nota.setImporto(new BigDecimal("5000"));
		nota.setDataEmissione(new Date());
		nota.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		nota.setSoggetto(soggetto);
		
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setUid(23);
		nota.setTipoDocumento(tipoDocumento);
		
		List<DocumentoEntrata> listaDocumentiEntrataPadre = new ArrayList<DocumentoEntrata>();
		DocumentoEntrata docEntrata = new DocumentoEntrata();
		docEntrata.setUid(310);
		listaDocumentiEntrataPadre.add(docEntrata);
		nota.setListaDocumentiEntrataPadre(listaDocumentiEntrataPadre);
		
		List<SubdocumentoEntrata> listaSubdocumenti = new ArrayList<SubdocumentoEntrata>();
		SubdocumentoEntrata subdocEntrata = new SubdocumentoEntrata();
		subdocEntrata.setEnte(getEnteTest());
		subdocEntrata.setUid(406);
		subdocEntrata.setNumero(1);
		listaSubdocumenti.add(subdocEntrata );
		nota.setListaSubdocumenti(listaSubdocumenti );
		
		request.setDocumentoEntrata(nota);
		
		AggiornaNotaCreditoEntrataResponse response = aggiornaNotaCreditoEntrataService.executeService(request);
		assertNotNull(response);
	}

	@Test
	public void ricercaQuoteDaEmettereEntrata() {
		RicercaQuoteDaEmettereEntrata req = new RicercaQuoteDaEmettereEntrata();
		
		req.setRichiedente(getRichiedenteByProperties("forn2", "edisu"));
		//req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setParametriPaginazione(getParametriPaginazione(0, 10));
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto("100131");
		soggetto.setUid(111040);
		req.setSoggetto(soggetto);
		
		RicercaQuoteDaEmettereEntrataResponse res = ricercaQuoteDaEmettereEntrataService.executeService(req);
		log.logXmlTypeObject(res, "response ");
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioDocEntrata() {
		RicercaDettaglioDocumentoEntrata req = new RicercaDettaglioDocumentoEntrata();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(695);
		req.setDocumentoEntrata(documentoEntrata );
		
		RicercaDettaglioDocumentoEntrataResponse res = ricercaDettaglioDocumentoEntrataService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void ricercaModulareDocumentoEntrata() {
		RicercaModulareDocumentoEntrata request = new RicercaModulareDocumentoEntrata();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(264);
		request.setDocumentoEntrata(documentoEntrata);
		
		request.setDocumentoEntrataModelDetails(
				DocumentoEntrataModelDetail.Attr,
				DocumentoEntrataModelDetail.Sogg,
				DocumentoEntrataModelDetail.Classif,
				//DocumentoEntrataModelDetail.CodiceBollo,
				DocumentoEntrataModelDetail.DataInizioValiditaStato,
				DocumentoEntrataModelDetail.Stato,
				DocumentoEntrataModelDetail.SubdocumentoIva,
				DocumentoEntrataModelDetail.TipoDocumento,
				DocumentoEntrataModelDetail.TotaliImportiQuote,
				DocumentoEntrataModelDetail.TotaleImportoDocumentiEntrataFiglio,
				DocumentoEntrataModelDetail.TotaleImportoDocumentiSpesaFiglio,
				DocumentoEntrataModelDetail.TotaliImportiNoteCredito
				
				);
		
		RicercaModulareDocumentoEntrataResponse response = ricercaModulareDocumentoEntrataService.executeService(request);
		assertNotNull(response);
	}
	
	@Test
	public void annullaDocumentoEntrata() {
		AnnullaDocumentoEntrata req = new AnnullaDocumentoEntrata();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancioTest(16, 2015));
		req.setDocumentoEntrata(create(DocumentoEntrata.class, 1330));
		
		AnnullaDocumentoEntrataResponse res = annullaDocumentoEntrataService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void spezzaQuotaEntrata() {
		SpezzaQuotaEntrata req = new SpezzaQuotaEntrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(133, 2018));
		
		SubdocumentoEntrata subdocumentoEntrata = create(SubdocumentoEntrata.class, 108455);
		subdocumentoEntrata.setDataScadenza(parseDate("30/01/2019"));
		subdocumentoEntrata.setImporto(new BigDecimal("1"));
		subdocumentoEntrata.setProvvisorioCassa(create(ProvvisorioDiCassa.class, 46052));
		req.setSubdocumentoEntrata(subdocumentoEntrata);
		
		SpezzaQuotaEntrataResponse res = spezzaQuotaEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaModulareQuoteEntrata() {
		RicercaSinteticaModulareQuoteEntrata req = new RicercaSinteticaModulareQuoteEntrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setModelDetails(SubdocumentoEntrataModelDetail.DocPadreModelDetail, DocumentoEntrataModelDetail.TotaliImportiQuote, DocumentoEntrataModelDetail.Stato, DocumentoEntrataModelDetail.Sogg);
		req.setSubdocumentoEntrata(create(SubdocumentoEntrata.class, 0));
		req.getSubdocumentoEntrata().setProvvisorioCassa(create(ProvvisorioDiCassa.class, 0));
		req.getSubdocumentoEntrata().getProvvisorioCassa().setAnno(Integer.valueOf(2018));
		req.getSubdocumentoEntrata().getProvvisorioCassa().setNumero(Integer.valueOf(20180406));
		
		RicercaSinteticaModulareQuoteEntrataResponse res = ricercaSinteticaModulareQuoteEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	
}
