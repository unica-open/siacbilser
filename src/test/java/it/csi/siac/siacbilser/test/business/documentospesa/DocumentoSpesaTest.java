/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentospesa;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaAttributiQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaImportoQuoteDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaNotaCreditoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaTestataDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AnnullaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceNotaCreditoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.ProporzionaImportiSplitReverseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioQuotaSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDocumentiCollegatiByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaModulareDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteDaEmettereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaModulareQuoteByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaModulareQuoteSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.SpezzaQuotaSpesaService;
import it.csi.siac.siacbilser.integration.dad.CodifichePCCDad;
import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverseResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoImpresa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class DocumentoSpesaDLTest.
 */
public class DocumentoSpesaTest extends BaseJunit4TestCase {
	
	
	/** The documento spesa service. */
	@Autowired
	@Qualifier("inserisceDocumentoSpesaService")
	private InserisceDocumentoSpesaService inserisceDocumentoSpesaService;
	
	/** The inserisce nota credito spesa service. */
	@Autowired
	private InserisceNotaCreditoSpesaService inserisceNotaCreditoSpesaService;
	
	/** The aggiorna nota credito spesa service. */
	@Autowired
	private AggiornaNotaCreditoSpesaService aggiornaNotaCreditoSpesaService;
	
	/** The aggiorna documento di spesa service. */
	@Autowired
	private AggiornaDocumentoDiSpesaService aggiornaDocumentoDiSpesaService;
	
	/** The ricerca dettaglio documento spesa service. */
	@Autowired
	private RicercaDettaglioDocumentoSpesaService ricercaDettaglioDocumentoSpesaService;
	
	/** The annulla documento spesa service. */
	@Autowired
	private AnnullaDocumentoSpesaService annullaDocumentoSpesaService;
	
	/** The ricerca sintetica documento spesa service. */
	@Autowired
	private RicercaSinteticaDocumentoSpesaService ricercaSinteticaDocumentoSpesaService;
	
	/** The ricerca documenti collegati by documento spesa service. */
	@Autowired
	private RicercaDocumentiCollegatiByDocumentoSpesaService ricercaDocumentiCollegatiByDocumentoSpesaService;
	
	@Autowired
	private RicercaQuoteDaEmettereSpesaService ricercaQuoteDaEmettereSpesaService;
	
	@Autowired
	private AggiornaAttributiQuotaDocumentoSpesaService aggiornaAttributiQuotaDocumentoSpesaService;
	
	@Autowired
	private RicercaDettaglioQuotaSpesaService ricercaDettaglioQuotaSpesaService;
	
	@Autowired
	private RicercaQuoteByDocumentoSpesaService ricercaQuoteByDocumentoSpesaService;
	
	@Autowired
	private AggiornaQuotaDocumentoSpesaService aggiornaQuotaDocumentoSpesaService;
	
	@Autowired
	private ProporzionaImportiSplitReverseService proporzionaImportiSplitReverseService;
	
	@Autowired
	private RicercaSinteticaModulareQuoteByDocumentoSpesaService ricercaSinteticaModulareQuoteByDocumentoSpesaService;
	
	@Autowired
	private RicercaModulareDocumentoSpesaService ricercaModulareDocumentoSpesaService;
	
	@Autowired
	private AggiornaTestataDocumentoDiSpesaService aggiornaTestataDocumentoDiSpesaService;
	
	@Autowired
	private SpezzaQuotaSpesaService spezzaQuotaSpesaService;
	
	@Autowired
	private AggiornaImportoQuoteDocumentoSpesaService aggiornaImportoQuoteDocumentoSpesaService;
	
	@Autowired
	private RicercaSinteticaModulareQuoteSpesaService ricercaSinteticaModulareQuoteSpesaService;
	
	@Autowired
	private SiacTDocRepository siacTDocRepository;
	
	@Autowired
	private CodifichePCCDad codifichePCCDad;
	
	@Test
	public void testCodicePcc() {
		codifichePCCDad.setEnte(create(Ente.class, 2));
		codifichePCCDad.findCodiciPCCByEnteAndStrutturaAmministrativoContabile(Arrays.asList(create(StrutturaAmministrativoContabile.class, 5)), create(CodiceUfficioDestinatarioPCC.class, 13));
	}
	
	/**
	 * Inserisci documento spesa.
	 */
	@Test
	public void inserisciDocumentoSpesa() {
		
		InserisceDocumentoSpesa req = new InserisceDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setBilancio(getBilancioTest(143, 2017));
		
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setAnno(Integer.valueOf(2017));
		doc.setNumero("2017_003_FAT_SIOPE+");
		doc.setImporto(new BigDecimal("1.00"));
		doc.setArrotondamento(new BigDecimal("0.00"));
		doc.setDescrizione("Test inserimento con dati del SIOPE+");
		doc.setEnte(req.getRichiedente().getAccount().getEnte());
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		doc.setTerminePagamento(Integer.valueOf(30));
		doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
		
		// FAT
		doc.setTipoDocumento(create(TipoDocumento.class, 138));
		// CMTO
		doc.setSoggetto(create(Soggetto.class, 1102));
		// IVA ASSOLTA
		doc.setCodiceBollo(create(CodiceBollo.class, 10));
		// 00-1JB
		doc.setCodicePCC(create(CodicePCC.class, 146));
		
		// SIOPE+
		// A
		doc.setSiopeDocumentoTipo(create(SiopeDocumentoTipo.class, 62));
		// FA
//		doc.setSiopeDocumentoTipoAnalogico(create(SiopeDocumentoTipoAnalogico.class, 62));
//		doc.setSiopeIdentificativoLottoSdi("125266630");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		doc.setDataEmissione(cal.getTime());
		cal.set(Calendar.DAY_OF_MONTH, 31);
		doc.setDataScadenza(cal.getTime());
//		doc.setDataRicezionePortale(cal.getTime());
//		cal.set(Calendar.MONTH, Calendar.JANUARY);
//		cal.set(Calendar.DAY_OF_MONTH, 28);
//		doc.setDataRepertorio(cal.getTime());
//		doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
		
		//attributi
//		doc.setNumeroRepertorio("24");
//		doc.setDataRepertorio(new Date());
//		doc.setNote("mie note documento 5");
//		doc.setArrotondamento(new BigDecimal("-13.1"));
//		doc.setTerminePagamento(33);
		
//		CodiceUfficioDestinatarioPCC codiceUfficioDestinatarioPCC = new CodiceUfficioDestinatarioPCC();
//		codiceUfficioDestinatarioPCC.setUid(7);
//		doc.setCodiceUfficioDestinatario(codiceUfficioDestinatarioPCC);
		
//		FatturaFEL fatturaFEL = new FatturaFEL();
//		fatturaFEL.setIdFattura(556);
//		doc.setFatturaFEL(fatturaFEL);
		
//		dettaglioOnere.setOrdinativi(ordinativi);
		
//		ritenuteDocumento.setListaOnere(listaOnere);
//		doc.setRitenuteDocumento(ritenuteDocumento );
//		
		req.setDocumentoSpesa(doc);
		
		InserisceDocumentoSpesaResponse res = inserisceDocumentoSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	
	/**
	 * Inserisci nota credito.
	 */
	@Test
	public void inserisciNotaCredito() {
		InserisceNotaCreditoSpesa req = new InserisceNotaCreditoSpesa();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setDataOra(new Date());
		req.setBilancio(getBilancioTest(143, 2017));
		
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setEnte(req.getRichiedente().getAccount().getEnte());
		doc.setAnno(Integer.valueOf(2017));
		doc.setContabilizzaGenPcc(Boolean.FALSE);
		doc.setCollegatoCEC(Boolean.FALSE);
		doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
		doc.setDataEmissione(parseDate("13/09/2017"));
		doc.setDescrizione("Nota credito per SIAC-5233-test (numero 3)");
		doc.setImporto(new BigDecimal("0.30"));
		// Gia' presente
		doc.setNumero("SIAC-5233-ncd");
		doc.setSoggetto(create(Soggetto.class, 1001));
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		doc.setTipoDocumento(create(TipoDocumento.class, 145));
		
		doc.getListaDocumentiSpesaPadre().add(create(DocumentoSpesa.class, 61534));
		
		doc.getListaSubdocumenti().add(create(SubdocumentoSpesa.class, 0));
		
		req.setDocumentoSpesa(doc);
		
		InserisceNotaCreditoSpesaResponse res = inserisceNotaCreditoSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	
	/**
	 * Aggiorna nota credito.
	 */
	@Test
	public void aggiornaNotaCredito() {
			
		AggiornaNotaCreditoSpesa req = new AggiornaNotaCreditoSpesa();
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancio2014Test());
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setEnte(getEnteTest());
		doc.setAnno(2014);		
		doc.setNumero("1");
		doc.setDescrizione("doc di test 1 aggiornato");
		doc.setImporto(new BigDecimal("106.01"));
		doc.setDataEmissione(new Date());
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		doc.setUid(290);
		
		doc.setFlagBeneficiarioMultiplo(Boolean.TRUE);
		
		//attributi
		doc.setNumeroRepertorio("24");
		doc.setDataRepertorio(new Date());
		doc.setNote("mie note documento 5");
		doc.setArrotondamento(new BigDecimal("13.1"));
		doc.setTerminePagamento(33);
	
		
		TipoDocumento tipoDoc = new TipoDocumento();
		tipoDoc.setUid(13);		//1 = CONGUAGLIO
		doc.setTipoDocumento(tipoDoc);
		
		Soggetto soggetto = new Soggetto();
		//soggetto.setUid(1);		
		soggetto.setUid(7);
		doc.setSoggetto(soggetto);
		
		
		
		DocumentoSpesa dsf = new DocumentoSpesa();
		dsf.setUid(25);
		doc.getListaDocumentiSpesaPadre().add(dsf);
		
		SubdocumentoSpesa subdoc = new SubdocumentoSpesa();	
		subdoc.setUid(385);
		subdoc.setNumero(1);
		doc.getListaSubdocumenti().add(subdoc);
		
		req.setDocumentoSpesa(doc);
		
		AggiornaNotaCreditoSpesaResponse res = aggiornaNotaCreditoSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	
	/**
	 * Annulla documento spesa.
	 */
	@Test
	public void annullaDocumentoSpesa(){	
		
		AnnullaDocumentoSpesa req = new AnnullaDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(65);
		req.setDocumentoSpesa(documentoSpesa);
		
		AnnullaDocumentoSpesaResponse res = annullaDocumentoSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	/**
	 * Aggiorna documento spesa.
	 */
	@Test
	public void aggiornaDocumentoSpesa() {
			
		AggiornaDocumentoDiSpesa req = new AggiornaDocumentoDiSpesa();
		
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(2);
		doc.setEnte(getEnteTest());
		doc.setAnno(2013);		
		doc.setNumero("333");
		doc.setDescrizione("doc spesa 333 aggiornato");
		doc.setImporto(new BigDecimal("10000"));
		doc.setDataEmissione(new Date());
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
//		Bilancio bilancio = new Bilancio();
//		bilancio.setUid(1);
//		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = new FaseEStatoAttualeBilancio();
//		faseEStatoAttualeBilancio.setFaseBilancio(FaseBilancio.GESTIONE);
//		bilancio.setFaseEStatoAttualeBilancio(faseEStatoAttualeBilancio );
//		req.setBilancio(bilancio);
		
		
		doc.setFlagBeneficiarioMultiplo(Boolean.TRUE);
		
		//attributi
//		doc.setNumeroRepertorio("24");
//		doc.setDataRepertorio(new Date());
//		doc.setNote("mie note documento 5");
//		doc.setArrotondamento(new BigDecimal("-13.1"));
//		doc.setTerminePagamento(33);
		BigDecimal importo = new BigDecimal("10");
		
		RitenuteDocumento ritenuteDocumento = new RitenuteDocumento();
		ritenuteDocumento.setImportoCassaPensioni(importo);
		ritenuteDocumento.setImportoEsente(importo);
		ritenuteDocumento.setImportoIVA(importo);
		ritenuteDocumento.setImportoRivalsa(importo);
		doc.setRitenuteDocumento(ritenuteDocumento );
		
		CodiceBollo codiceBollo = new CodiceBollo();
		codiceBollo.setUid(1);
		doc.setCodiceBollo(codiceBollo);
		
		TipoImpresa tipoImpresa = new TipoImpresa();
		tipoImpresa.setUid(41168);//41168//41173
		doc.setTipoImpresa(tipoImpresa);
		
		TipoDocumento tipoDoc = new TipoDocumento();
		tipoDoc.setUid(6);		//1 = CONGUAGLIO //22 AGG con flagRegolarizzazione
		doc.setTipoDocumento(tipoDoc);
		
		Soggetto soggetto = new Soggetto();
		//soggetto.setUid(1);		
		soggetto.setUid(7);
		doc.setSoggetto(soggetto);
		
		
//		dettaglioOnere.setOrdinativi(ordinativi);
		
//		ritenuteDocumento.setListaOnere(listaOnere);
//		doc.setRitenuteDocumento(ritenuteDocumento );
//		
		req.setDocumentoSpesa(doc);
		
		AggiornaDocumentoDiSpesaResponse res = aggiornaDocumentoDiSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void aggiornaDocumentoSpesa2() {
		Richiedente richiedente = getRichiedenteByProperties("consip", "crp");
		Date now = new Date();
		
		RicercaDettaglioDocumentoSpesa reqRD = new RicercaDettaglioDocumentoSpesa();
		reqRD.setDataOra(now);
		reqRD.setRichiedente(richiedente);
		reqRD.setDocumentoSpesa(create(DocumentoSpesa.class, 61563));
		
		RicercaDettaglioDocumentoSpesaResponse resRD = ricercaDettaglioDocumentoSpesaService.executeService(reqRD);
		DocumentoSpesa ds = resRD.getDocumento();
		
		ds.setSiopeIdentificativoLottoSdi("88894852");
		
		AggiornaDocumentoDiSpesa req = new AggiornaDocumentoDiSpesa();
		req.setBilancio(getBilancioTest(143, 2017));
		req.setDataOra(now);
		req.setRichiedente(richiedente);
		req.setDocumentoSpesa(ds);
		
		AggiornaDocumentoDiSpesaResponse res = aggiornaDocumentoDiSpesaService.executeService(req);
		assertNotNull(res);
	}
	

	
	/**
	 * Ricerca dettaglio documento spesa.
	 */
	@Test
	public void ricercaDettaglioDocumentoSpesa(){
		RicercaDettaglioDocumentoSpesa req = new RicercaDettaglioDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
//		req.setRichiedente(getRichiedenteTest("2 -  ACCOUNT  - AMMINISTRATORE", 52, 2));

		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(77508);
		
		req.setDocumentoSpesa(doc);
		
		RicercaDettaglioDocumentoSpesaResponse res = ricercaDettaglioDocumentoSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	/**
	 * Ricerca sintetica documento spesa.
	 */
	@Test
	public void ricercaSinteticaDocumentoSpesa(){
		
		
		RicercaSinteticaDocumentoSpesa req = new RicercaSinteticaDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setParametriPaginazione(getParametriPaginazioneTest());

		DocumentoSpesa doc = new DocumentoSpesa();
		Ente ente = new Ente();
		ente.setUid(1);
		doc.setEnte(ente);
		doc.setAnno(2015);
		
//		Liquidazione liquidazione = new Liquidazione();
//		liquidazione.setAnnoLiquidazione(2013);
//		liquidazione.setNumeroLiquidazione(new BigDecimal(14));
//		
//		req.setLiquidazione(liquidazione);
		
		Bilancio bilancio= new Bilancio();
		bilancio.setAnno(2015);
		//bilancio.setUid(1);
//		req.setBilancioLiquidazione(bilancio);
		
		//doc.setNumero("7");
	
		//Soggetto soggetto = new Soggetto();
		//soggetto.setUid(1);
		//doc.setSoggetto(soggetto);
		
		//req.setRilevanteIva(Boolean.TRUE);
		
//		Impegno impegno = new Impegno();
//		impegno.setUid(1);
//		req.setImpegno(impegno);
//		
//		AttoAmministrativo attoAmministrativo  = new AttoAmministrativo();
//		attoAmministrativo.setUid(1);
//		req.setAttoAmministrativo(attoAmministrativo);
//		
//		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		
		req.setDocumentoSpesa(doc);
		req.setContabilizzaGenPcc(true);
		RicercaSinteticaDocumentoSpesaResponse res = ricercaSinteticaDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca documenti collegati by documento spesa service.
	 */
	@Test
	public void ricercaDocumentiCollegatiByDocumentoSpesaService() {
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(25);
		
		RicercaDocumentiCollegatiByDocumentoSpesa req = new RicercaDocumentiCollegatiByDocumentoSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setDocumentoSpesa(documentoSpesa);
		
		RicercaDocumentiCollegatiByDocumentoSpesaResponse res = ricercaDocumentiCollegatiByDocumentoSpesaService.executeService(req);
		assertNotNull(res);
		
		log.info("ricercaDocumentiCollegatiByDocumentoSpesaService", "#Documenti entrata padre collegati: " + res.getDocumentiEntrataPadre().size());
		log.info("ricercaDocumentiCollegatiByDocumentoSpesaService", "#Documenti entrata figlio collegati: " + res.getDocumentiEntrataFiglio().size());
		log.info("ricercaDocumentiCollegatiByDocumentoSpesaService", "#Documenti spesa padre collegati: " + res.getDocumentiSpesaPadre().size());
		log.info("ricercaDocumentiCollegatiByDocumentoSpesaService", "#Documenti spesa figlio collegati: " + res.getDocumentiSpesaFiglio().size());
	}
	
	@Test
	public void aggiornaAttributiQuotaDocumentoSpesa() {
		AggiornaAttributiQuotaDocumentoSpesa req = new AggiornaAttributiQuotaDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		
		req.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 72043));
		req.getSubdocumentoSpesa().setEnte(req.getRichiedente().getAccount().getEnte());
		
		Map<TipologiaAttributo, Object> attributi = new EnumMap<TipologiaAttributo, Object>(TipologiaAttributo.class);
		attributi.put(TipologiaAttributo.CIG, "");
		attributi.put(TipologiaAttributo.CUP, "A01A01234567890");
		attributi.put(TipologiaAttributo.NUMERO_MUTUO, "");
		req.setAttributi(attributi);
		
		req.setSiopeAssenzaMotivazione(create(SiopeAssenzaMotivazione.class, 277));
		
		AggiornaAttributiQuotaDocumentoSpesaResponse res = aggiornaAttributiQuotaDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaQuoteDaEmettereSpesa() {
		RicercaQuoteDaEmettereSpesa req = new RicercaQuoteDaEmettereSpesa();
		Soggetto s =  new Soggetto();
		s.setUid(181386);
		s.setCodiceSoggetto("72");
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
//		req.setAnnoElenco(2015);
		req.setSoggetto(s);
		req.setFlagConvalidaManuale(Boolean.TRUE);
//		req.setNumeroElencoDa(1);
//		req.setNumeroElencoA(100);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaQuoteDaEmettereSpesaResponse res = ricercaQuoteDaEmettereSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioQuotaSpesa() {
		RicercaDettaglioQuotaSpesa req = new RicercaDettaglioQuotaSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn1", "cmto"));
		// Metto l'ente a 15
//		req.getRichiedente().getOperatore().setCodiceFiscale("AAAAAA00A11E000M");
//		req.getRichiedente().getAccount().getEnte().setUid(15);
//		req.getRichiedente().getAccount().setUid(71);
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(54707);
		req.setSubdocumentoSpesa(subdocumentoSpesa);
		
		RicercaDettaglioQuotaSpesaResponse res = ricercaDettaglioQuotaSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaQuoteByDocumentoSpesa() {
		RicercaQuoteByDocumentoSpesa req = new RicercaQuoteByDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		// Metto l'ente a 15
//		req.getRichiedente().getOperatore().setCodiceFiscale("AAAAAA00A11E000M");
//		req.getRichiedente().getAccount().getEnte().setUid(15);
//		req.getRichiedente().getAccount().setUid(71);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(73970);
		req.setDocumentoSpesa(documentoSpesa);
		
		RicercaQuoteByDocumentoSpesaResponse res = ricercaQuoteByDocumentoSpesaService.executeService(req);
		assertNotNull(res);
		assertNotNull(res.getSubdocumentiSpesa());
	}
	
	@Test
	public void aggiornaQuotaDocumentoSpesa() {
		// Doc 463, subdoc 547
		RicercaDettaglioQuotaSpesa reqRDQS = new RicercaDettaglioQuotaSpesa();
		reqRDQS.setDataOra(new Date());
		reqRDQS.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		reqRDQS.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 71994));
		
		RicercaDettaglioQuotaSpesaResponse resRDQS = ricercaDettaglioQuotaSpesaService.executeService(reqRDQS);
		assertNotNull(resRDQS);
		assertTrue(resRDQS.getErrori().isEmpty());
		assertNotNull(resRDQS.getSubdocumentoSpesa());
		
		SubdocumentoSpesa subdocumentoSpesa = resRDQS.getSubdocumentoSpesa();
		subdocumentoSpesa.setImporto(new BigDecimal("11"));
		AggiornaQuotaDocumentoSpesa reqAQDS = new AggiornaQuotaDocumentoSpesa();
		
		reqAQDS.setAggiornaStatoDocumento(true);
		reqAQDS.setBilancio(getBilancioTest(143, 2017));
		reqAQDS.setDataOra(new Date());
		reqAQDS.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		reqAQDS.setSubdocumentoSpesa(subdocumentoSpesa);
		
		AggiornaQuotaDocumentoSpesaResponse resAQDS = aggiornaQuotaDocumentoSpesaService.executeService(reqAQDS);
		assertNotNull(resAQDS);
	}
	
	@Test
	public void aggiornaQuotaSpesaPCC() {
		// Doc 460, subdoc 544
		RicercaDettaglioQuotaSpesa reqRDQS = new RicercaDettaglioQuotaSpesa();
		reqRDQS.setDataOra(new Date());
		reqRDQS.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		SubdocumentoSpesa ssRQDS = new SubdocumentoSpesa();
		ssRQDS.setUid(544);
		reqRDQS.setSubdocumentoSpesa(ssRQDS);
		
		RicercaDettaglioQuotaSpesaResponse resRDQS = ricercaDettaglioQuotaSpesaService.executeService(reqRDQS);
		assertNotNull(resRDQS);
		assertTrue(resRDQS.getErrori().isEmpty());
		assertNotNull(resRDQS.getSubdocumentoSpesa());
		
		SubdocumentoSpesa subdocumentoSpesa = resRDQS.getSubdocumentoSpesa();
		
		Calendar cal = Calendar.getInstance();
		cal.set(2015, Calendar.JUNE, 30, 0, 0, 0);
		subdocumentoSpesa.setDataScadenzaDopoSospensione(cal.getTime());
		
		Impegno impegno = new Impegno();
		impegno.setUid(41028);
		impegno.setAnnoMovimento(2015);
		impegno.setNumero(new BigDecimal("496"));
		subdocumentoSpesa.setImpegno(impegno);
		
		AggiornaQuotaDocumentoSpesa reqAQDS = new AggiornaQuotaDocumentoSpesa();
		
		reqAQDS.setAggiornaStatoDocumento(true);
		reqAQDS.setBilancio(getBilancio2015Test());
		reqAQDS.setDataOra(new Date());
		reqAQDS.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		reqAQDS.setSubdocumentoSpesa(subdocumentoSpesa);
		
		AggiornaQuotaDocumentoSpesaResponse resAQDS = aggiornaQuotaDocumentoSpesaService.executeService(reqAQDS);
		assertNotNull(resAQDS);
	}
	
	
	/**
	 * Annulla documento spesa.
	 */
	@Test
	public void proporzionaImportiSplitReverseService(){	
		
		ProporzionaImportiSplitReverse req = new ProporzionaImportiSplitReverse();
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(723);
		req.setDocumentoSpesa(documentoSpesa);
		
		ProporzionaImportiSplitReverseResponse res = proporzionaImportiSplitReverseService.executeService(req);
		
		assertNotNull(res);
	}

	@Test
	public void ricercaSinteticaModulareQuoteByDocumentoSpesa() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa req = new RicercaSinteticaModulareQuoteByDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(5);
		parametriPaginazione.setNumeroPagina(0);
		req.setParametriPaginazione(parametriPaginazione);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(2222);
		req.setDocumentoSpesa(documentoSpesa);
		
		req.setRilevanteIva(Boolean.FALSE);
		req.setSubdocumentoSpesaModelDetails(SubdocumentoSpesaModelDetail.Liquidazione);
		
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse res = ricercaSinteticaModulareQuoteByDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaModulareDocumentoSpesa() {
		RicercaModulareDocumentoSpesa request = new RicercaModulareDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(32850);
		request.setDocumentoSpesa(documentoSpesa);
		
		request.setDocumentoSpesaModelDetails(
				//DocumentoSpesaModelDetail.Attr,
				DocumentoSpesaModelDetail.Soggetto,
				//DocumentoSpesaModelDetail.Classif,
				//DocumentoSpesaModelDetail.CodiceBollo,
//				DocumentoSpesaModelDetail.CodicePCC,
//				DocumentoSpesaModelDetail.CodiceUfficioDestinatarioPCC,
//				DocumentoSpesaModelDetail.CollegatoAdAllegatoAtto,
//				DocumentoSpesaModelDetail.DataInizioValiditaStato,
//				DocumentoSpesaModelDetail.FatturaFEL,
//				DocumentoSpesaModelDetail.ImportoDaPagareNonPagatoInCassaEconomale,
//				DocumentoSpesaModelDetail.RegistroComunicazioniPCC,
//				DocumentoSpesaModelDetail.RegistroUnico,
//				DocumentoSpesaModelDetail.Stato,
				DocumentoSpesaModelDetail.DocumentiCollegati,
				DocumentoSpesaModelDetail.SubdocumentoIva,
				DocumentoSpesaModelDetail.TipoDocumento,
				DocumentoSpesaModelDetail.SubdocumentoSpesa
//				DocumentoSpesaModelDetail.TotaliImportiQuote,
//				DocumentoSpesaModelDetail.TotaleImportoDocumentiEntrataFiglio,
//				DocumentoSpesaModelDetail.TotaleImportoDocumentiSpesaFiglio,
//				DocumentoSpesaModelDetail.TotaliImportiNoteCredito,
//				DocumentoSpesaModelDetail.TotaliImportiRilevantiIvaQuote,
//				DocumentoSpesaModelDetail.EsisteQuotaRilevanteIva
				);
		
		RicercaModulareDocumentoSpesaResponse response = ricercaModulareDocumentoSpesaService.executeService(request);
		assertNotNull(response);
	}
	
	@Test
	public void testSumImportoDaDedurreSuFatturaNoteCreditoColegateByDocId(){
		BigDecimal sum = siacTDocRepository.sumImportoDaDedurreSuFattureByDocFiglioUid(1242);
		System.out.println(">>>>>>>>>> sum: "+sum);
	}
	
	@Test
	public void aggiornaTestataDocumentoDiSpesa() {
		AggiornaDocumentoDiSpesa req = new AggiornaDocumentoDiSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancio2015Test());
		
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(184);
		doc.setEnte(req.getRichiedente().getAccount().getEnte());
		doc.setAnno(2014);
		doc.setNumero("01docfit");
		doc.setDescrizione("descr");
		doc.setImporto(new BigDecimal("1500"));
		
		doc.setDataEmissione(parseDate("04/02/2014"));
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(64);
		doc.setSoggetto(soggetto);
		
		req.setDocumentoSpesa(doc);
		
		AggiornaDocumentoDiSpesaResponse res = aggiornaTestataDocumentoDiSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void spezzaQuotaSpesa() {
		SpezzaQuotaSpesa req = new SpezzaQuotaSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", "2019"));
		
		// L'importo e' pari a 1, lo split reverse e' null
		req.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 110206));
		
//		subdocumentoSpesa.setCig("0123456789");
//		subdocumentoSpesa.setCup("A12A01234567890");
		req.getSubdocumentoSpesa().setModalitaPagamentoSoggetto(create(ModalitaPagamentoSoggetto.class, 138167));
//		req.getSubdocumentoSpesa().setModalitaPagamentoSoggetto(create(ModalitaPagamentoSoggetto.class, 204562));
//		subdocumentoSpesa.setProvvisorioCassa(create(ProvvisorioDiCassa.class, 5838));
		req.getSubdocumentoSpesa().setImporto(new BigDecimal("20.00"));
		req.getSubdocumentoSpesa().setImportoDaDedurre(BigDecimal.ZERO);
//		req.getSubdocumentoSpesa().setImportoSplitReverse(BigDecimal.ZERO);
		req.getSubdocumentoSpesa().setDataScadenza(parseDate("28/06/2020"));
		req.getSubdocumentoSpesa().setElencoDocumenti(create(ElencoDocumentiAllegato.class, 28280));
//		req.getSubdocumentoSpesa().setProvvisorioCassa(create(ProvvisorioDiCassa.class, 47117));
//		req.getSubdocumentoSpesa().getProvvisorioCassa().setAnno(Integer.valueOf(2018));
//		req.getSubdocumentoSpesa().getProvvisorioCassa().setNumero(Integer.valueOf(15523005));
		
		SpezzaQuotaSpesaResponse res = spezzaQuotaSpesaService.executeService(req);
		assertNotNull(res);
		
	}
	
	@Test
	public void aggiornaImportoQuoteDocumentoSpesa() {
		AggiornaImportiQuoteDocumentoSpesa req = new AggiornaImportiQuoteDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		
		SubdocumentoSpesa ss = create(SubdocumentoSpesa.class, 35686);
		ss.setDocumento(create(DocumentoSpesa.class, 31015));
		ss.setEnte(req.getRichiedente().getAccount().getEnte());
		
		ss.setImporto(new BigDecimal("0.34"));
		ss.setImportoDaDedurre(BigDecimal.ZERO);
		ss.setProvvisorioCassa(create(ProvvisorioDiCassa.class, 8464));
		ss.setModalitaPagamentoSoggetto(create(ModalitaPagamentoSoggetto.class, 134746));
		req.getSubdocumentiSpesa().add(ss);
		
		AggiornaImportiQuoteDocumentoSpesaResponse res = aggiornaImportoQuoteDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaModulareQuoteSpesa() {
		RicercaSinteticaModulareQuoteSpesa req = new RicercaSinteticaModulareQuoteSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setModelDetails(SubdocumentoSpesaModelDetail.DocPadreModelDetail, DocumentoSpesaModelDetail.Soggetto);
		req.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 0));
		req.getSubdocumentoSpesa().setProvvisorioCassa(create(ProvvisorioDiCassa.class, 0));
		req.getSubdocumentoSpesa().getProvvisorioCassa().setAnno(Integer.valueOf(2017));
		req.getSubdocumentoSpesa().getProvvisorioCassa().setNumero(Integer.valueOf(25602));
		
		RicercaSinteticaModulareQuoteSpesaResponse res = ricercaSinteticaModulareQuoteSpesaService.executeService(req);
		assertNotNull(res);
	}
}
