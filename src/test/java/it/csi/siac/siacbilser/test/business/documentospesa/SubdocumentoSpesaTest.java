/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentospesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaNoteCreditoIvaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioQuotaSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuotaSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteDaEmettereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaModulareQuoteByDocumentoSpesaService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dao.SiacTModpagRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoRelaz;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SospensioneSubdocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoSpesaDLTest.
 */
public class SubdocumentoSpesaTest extends BaseJunit4TestCase {
	
	@Autowired
	private LiquidazioneService liquidazioneService;
	/** The inserisce quota documento spesa service. */
	@Autowired
	private InserisceQuotaDocumentoSpesaService inserisceQuotaDocumentoSpesaService;
	
	/** The ricerca quote by documento spesa service. */
	@Autowired
	private RicercaQuoteByDocumentoSpesaService ricercaQuoteByDocumentoSpesaService;
	
	/** The elimina quota documento spesa service. */
	@Autowired
	private EliminaQuotaDocumentoSpesaService eliminaQuotaDocumentoSpesaService;
	
	@Autowired
	private RicercaQuotaSpesaService ricercaQuotaSpesaService;
	
	@Autowired
	private RicercaQuoteDaEmettereSpesaService ricercaQuoteDaEmettereSpesaService;
	
	@Autowired
	private RicercaDettaglioQuotaSpesaService ricercaDettaglioQuotaSpesaService;
	
	@Autowired
	private AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	
	@Autowired
	private AggiornaQuotaDocumentoSpesaService aggiornaQuotaDocumentoSpesaService;
	
	@Autowired
	private MovimentoGestioneService movimentoGestioneService;
	
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	@Autowired
	private RicercaSinteticaModulareQuoteByDocumentoSpesaService ricercaSinteticaModulareQuoteByDocumentoSpesaService;
	
	/**
	 * Ricerca quote by documento spesa.
	 */
	@Test
	public void ricercaQuoteByDocumentoSpesa() {
//		RicercaQuoteByDocumentoSpesa req = new RicercaQuoteByDocumentoSpesa();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		DocumentoSpesa doc = new DocumentoSpesa();
//		doc.setUid(3);
//		req.setDocumentoSpesa(doc);	
//		
//		RicercaQuoteByDocumentoSpesaResponse res = ricercaQuoteByDocumentoSpesaService.executeService(req);
//		
//		res = marshallUnmarshall(res);
//		
//		LogUtil logg = new LogUtil(this.getClass());
//		logg.logXmlTypeObject(res, "ciao");
	}

	
	/**
	 * 
	 * Inserisce quota documento spesa service.
	 */
	@Test
	public void inserisceQuotaDocumentoSpesaService() {
			
		InserisceQuotaDocumentoSpesa req = new InserisceQuotaDocumentoSpesa();
		

		req.setBilancio(getBilancioTest(131,2017));
		
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = new ModalitaPagamentoSoggetto();
		modalitaPagamentoSoggetto.setUid(142088);
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		SubdocumentoSpesa sd = new SubdocumentoSpesa();
		
		sd.setEnte(req.getRichiedente().getAccount().getEnte());
		
		DocumentoSpesa doc = new DocumentoSpesa();
		
		doc.setUid(32095);
		sd.setDocumento(doc);
		
		sd.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		
		sd.setImporto(new BigDecimal("0.01"));
//		sd.setCommissioniDocumento(CommissioniDocumento.ESENTE);
//		sd.setCig("mio cig 3");
//		sd.setCup("mio cup 3");
//		sd.setNumeroMutuo("mio numero mutuo 3");
		
//		TipoAvviso tipoAvviso = new TipoAvviso(); //41158,41163
//		tipoAvviso.setUid(41158);
//		sd.setTipoAvviso(tipoAvviso);
//		
//		DatiCertificazioneCrediti datiCertificazioneCrediti = new DatiCertificazioneCrediti();
//		datiCertificazioneCrediti.setFlagCertificazione(Boolean.TRUE);
//		datiCertificazioneCrediti.setNoteCertificazione("Mie note certificazione!");
//		datiCertificazioneCrediti.setAnnotazione("Mia annotazione!");
//		datiCertificazioneCrediti.setNumeroCertificazione("Mio numero certificazione!");
//		sd.setDatiCertificazioneCrediti(datiCertificazioneCrediti);
		
//		sd.setCausaleOrdinativo("causale ordinativo");
//		
//		SubImpegno subImpegno = new SubImpegno();
//		subImpegno.setUid(5);		
//		sd.setSubImpegno(subImpegno);
		
		Impegno impegno = new Impegno();
		impegno.setAnnoMovimento(2017);
		impegno.setNumero(new BigDecimal(2426));
		sd.setImpegno(impegno);
		
//		AttoAmministrativo atto = new AttoAmministrativo();
//		atto.setUid(51);
////		atto.setAnno(2013);
////		atto.setNumero(25);
//		sd.setAttoAmministrativo(atto);
		
//		ProvvisorioDiCassa provvisorio = new ProvvisorioDiCassa();
//		provvisorio.setAnno(2015);
//		provvisorio.setNumero(38);
//		sd.setProvvisorioCassa(provvisorio);
//		
		req.setSubdocumentoSpesa(sd);
		
		InserisceQuotaDocumentoSpesaResponse res = inserisceQuotaDocumentoSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Elimina quota documento sesa.
	 */
	@Test
	public void eliminaQuotaDocumentoSpesa (){
		EliminaQuotaDocumentoSpesa req = new EliminaQuotaDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setBilancio(getBilancioTest(132, 2018));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 39682));
		
		EliminaQuotaDocumentoSpesaResponse res = eliminaQuotaDocumentoSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaQuotaDocumentoSpesa (){
		RicercaQuotaSpesa req = new RicercaQuotaSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		
//		req.setAnnoMovimento(2013);
//		req.setAnnoProvvedimento(2013);
//		req.setAnnoProvvisorio(2013);
//		req.setDataEmissioneDocumento(dataEmissioneDocumento);
//		req.setDataProvvisorio(dataProvvisorio);
		
		req.setAnnoDocumento(2015);
		req.setNumeroDocumento("2015/50041/ALG/016/124/01");
		
//		req.setNumeroMovimento(new BigDecimal(1));
//		req.setNumeroProvvedimento(50000);
//		req.setNumeroQuota(1);
//		
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(9);
//		req.setSoggetto(soggetto);
//		
//		TipoAtto tipoAtto = new TipoAtto();
//		tipoAtto.setUid(3);
//		req.setTipoAtto(tipoAtto);
//		
//		TipoDocumento tipoDocumento = new TipoDocumento();
//		tipoDocumento.setUid(6);
//		req.setTipoDocumento(tipoDocumento);
//		
//		
//		StrutturaAmministrativoContabile strutt = new StrutturaAmministrativoContabile();
//		strutt.setUid(1);
//		req.setStruttAmmContabile(strutt);
		
		
		RicercaQuotaSpesaResponse res = ricercaQuotaSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void ricercaQuoteDaEmettereSpesa (){
		RicercaQuoteDaEmettereSpesa req = new RicercaQuoteDaEmettereSpesa();
		req.setRichiedente(getRichiedenteByProperties("forn2", "crp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
//		req.setAnnoCapitolo(2017);
//		req.setNumeroArticolo(1);
//		req.setNumeroCapitolo(1);
//		req.setNumeroUEB(1);
	
		
		//req.setAnnoDocumento(2014);
		//req.setNumeroDocumento("001_PROVVISORIO");
		
//		
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(9);
//		req.setSoggetto(soggetto);
//		
//		TipoAtto tipoAtto = new TipoAtto();
//		tipoAtto.setUid(3);
//		req.setTipoAtto(tipoAtto);
//		
//		
//		StrutturaAmministrativoContabile strutt = new StrutturaAmministrativoContabile();
//		strutt.setUid(1);
//		req.setStruttAmmContabile(strutt);
		
		
		RicercaQuoteDaEmettereSpesaResponse res = ricercaQuoteDaEmettereSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaDettaglioQuotaDocumentoSpesa (){
		RicercaDettaglioQuotaSpesa req = new RicercaDettaglioQuotaSpesa();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setDataOra(new Date());
		req.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 72037));
		
		
		RicercaDettaglioQuotaSpesaResponse res = ricercaDettaglioQuotaSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	
	
	@Test
	public void inserisciLiquidazione(){
		
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(1);
		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = new FaseEStatoAttualeBilancio();
		faseEStatoAttualeBilancio.setFaseBilancio(FaseBilancio.GESTIONE);
		bilancio.setFaseEStatoAttualeBilancio(faseEStatoAttualeBilancio );
		
		Impegno impegno = new Impegno();
		impegno.setUid(1);
		
		AttoAmministrativo atto = new AttoAmministrativo();
		atto.setUid(3);
		
		Soggetto soggetto= new Soggetto();
		soggetto.setUid(7);
		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = new ModalitaPagamentoSoggetto();
		modalitaPagamentoSoggetto.setUid(2);
		modalitaPagamentoList.add(modalitaPagamentoSoggetto);
		soggetto.setModalitaPagamentoList(modalitaPagamentoList );
		
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setImportoAttualeLiquidazione(new BigDecimal("100"));
		liquidazione.setDescrizioneLiquidazione("causale liquidazione");
		liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
		liquidazione.setImpegno(impegno);
		liquidazione.setAttoAmministrativoLiquidazione(atto);
		liquidazione.setSoggettoLiquidazione(soggetto);
		
		InserisceLiquidazione inserisceLiquidazione = new InserisceLiquidazione();
		inserisceLiquidazione.setRichiedente(getRichiedenteByProperties("consip","regp"));
		inserisceLiquidazione.setBilancio(bilancio);
		inserisceLiquidazione.setEnte(getEnteTest());
		inserisceLiquidazione.setLiquidazione(liquidazione);
		
		InserisceLiquidazioneResponse inserisceLiquidazioneResponse = liquidazioneService.inserisceLiquidazione(inserisceLiquidazione);
		log.logXmlTypeObject(inserisceLiquidazioneResponse, "Risposta ottenuta dal servizio InserisceLiquidazione.");
	}
	
	@Test
	public void aggiornaStatoDocumento(){
		AggiornaStatoDocumentoDiSpesa req = new AggiornaStatoDocumentoDiSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoSpesa docSpesa = new DocumentoSpesa();
		docSpesa.setUid(2);
		req.setDocumentoSpesa(docSpesa);
		
		AggiornaStatoDocumentoDiSpesaResponse res = aggiornaStatoDocumentoDiSpesaService.executeService(req);
	}
	
	
//	public static void main(String[] args) {
//		Method[] ms = InserisceLiquidazione.class.getDeclaredMethods();
//		for(Method m : ms) {
//			Class<?>[] pt = m.getParameterTypes();
//			System.out.println(m.getName() + "(" + Arrays.toString(pt) + ")");
//		}
//	}
	

	/**
	 * 
	 * Inserisce quota documento spesa service.
	 */
	@Test
	public void aggiornaQuotaDocumentoSpesaService() {
			
		AggiornaQuotaDocumentoSpesa req = new AggiornaQuotaDocumentoSpesa();
		
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(1);
		FaseEStatoAttualeBilancio faseEStatoAttualeBilancio = new FaseEStatoAttualeBilancio();
		faseEStatoAttualeBilancio.setFaseBilancio(FaseBilancio.GESTIONE);
		bilancio.setFaseEStatoAttualeBilancio(faseEStatoAttualeBilancio );
		req.setBilancio(bilancio);
		
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = new ModalitaPagamentoSoggetto();
		modalitaPagamentoSoggetto.setUid(2);
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoSpesa sd = new SubdocumentoSpesa();
		
		sd.setEnte(getEnteTest());
		
		sd.setNumero(1);
		sd.setUid(352);
		
//		Liquidazione liquidazione = new Liquidazione();
//		liquidazione.setUid(15);
//		liquidazione.setAnnoLiquidazione(2013);
//		liquidazione.setNumeroLiquidazione(new BigDecimal(14));
//		
//		sd.setLiquidazione(liquidazione);
		
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(262);
		sd.setDocumento(doc);
		
		sd.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		
		sd.setImporto(new BigDecimal("500"));
		sd.setCommissioniDocumento(CommissioniDocumento.ESENTE);
//		sd.setCig("mio cig 3");
//		sd.setCup("mio cup 3");
//		sd.setNumeroMutuo("mio numero mutuo 3");
		
//		TipoAvviso tipoAvviso = new TipoAvviso(); //41158,41163
//		tipoAvviso.setUid(41158);
//		sd.setTipoAvviso(tipoAvviso);
//		
//		DatiCertificazioneCrediti datiCertificazioneCrediti = new DatiCertificazioneCrediti();
//		datiCertificazioneCrediti.setFlagCertificazione(Boolean.TRUE);
//		datiCertificazioneCrediti.setNoteCertificazione("Mie note certificazione!");
//		datiCertificazioneCrediti.setAnnotazione("Mia annotazione!");
//		datiCertificazioneCrediti.setNumeroCertificazione("Mio numero certificazione!");
//		sd.setDatiCertificazioneCrediti(datiCertificazioneCrediti);
//		
//		sd.setCausaleOrdinativo("causale ordinativo");
//		
//		SubImpegno subImpegno = new SubImpegno();
//		subImpegno.setUid(5);		
//		sd.setSubImpegno(subImpegno);
		
//		Impegno impegno = new Impegno();
//		impegno.setAnnoMovimento(2013);
//		impegno.setNumero(new BigDecimal(1));
//		sd.setImpegno(impegno);
//		
//		AttoAmministrativo atto = new AttoAmministrativo();
//		atto.setUid(3);
////		atto.setAnno(2013);
////		atto.setNumero(25);
//		sd.setAttoAmministrativo(atto);
		
//		ProvvisorioDiCassa provvisorio = new ProvvisorioDiCassa();
//		provvisorio.setAnno(2013);
//		provvisorio.setNumero(16);
//		sd.setProvvisorioCassa(provvisorio);
		
		req.setSubdocumentoSpesa(sd);
		
		AggiornaQuotaDocumentoSpesaResponse res = aggiornaQuotaDocumentoSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void aggiornaQuotaDocumentoSpesa2() {
		RicercaDettaglioQuotaSpesa rq1 = new RicercaDettaglioQuotaSpesa();
		rq1.setDataOra(new Date());
		rq1.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		rq1.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 0));
		
		RicercaDettaglioQuotaSpesaResponse rs1 = ricercaDettaglioQuotaSpesaService.executeService(rq1);
		
		AggiornaQuotaDocumentoSpesa req = new AggiornaQuotaDocumentoSpesa();
		req.setAggiornaStatoDocumento(false);
		req.setBilancio(getBilancioTest(143, 2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setSubdocumentoSpesa(rs1.getSubdocumentoSpesa());
		
		AggiornaQuotaDocumentoSpesaResponse res = aggiornaQuotaDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaImpegnoPerChiave(){
		RicercaImpegnoPerChiave req = new RicercaImpegnoPerChiave();
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		pRicercaImpegnoK.setAnnoEsercizio(2014);
		pRicercaImpegnoK.setAnnoImpegno(2014);
		pRicercaImpegnoK.setNumeroImpegno(new BigDecimal(93));
		req.setpRicercaImpegnoK(pRicercaImpegnoK );
		
		RicercaImpegnoPerChiaveResponse res = movimentoGestioneService.ricercaImpegnoPerChiave(req);
		log.logXmlTypeObject(res.getImpegno(), "impegno trovato");
	}
	
	 @Test
	 public void uidImpegno(){
//		 Integer uid = subdocumentoSpesaDad.findUidImpegnoOSubImpegnoSubdocumentoSpesaById(149);
//		 log.debug("uidImpegno", "UID: " + uid);
	 }
	
	/**
	 * 
	 * Inserisce quota documento spesa service.
	 */
	@Test
	public void aggiornaQuotaDocumentoSpesaConMutuo() {
		Date now = new Date();
		RicercaDettaglioQuotaSpesa reqRD = new RicercaDettaglioQuotaSpesa();
		reqRD.setDataOra(now);
		reqRD.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(542);
		reqRD.setSubdocumentoSpesa(subdocumentoSpesa);
		
		RicercaDettaglioQuotaSpesaResponse resRD = ricercaDettaglioQuotaSpesaService.executeService(reqRD);
		assertNotNull(resRD);
		assertNotNull(resRD.getSubdocumentoSpesa());
		
		// Imposto il mutuo
		subdocumentoSpesa = resRD.getSubdocumentoSpesa();
		VoceMutuo voceMutuo = new VoceMutuo();
		voceMutuo.setUid(1);
		subdocumentoSpesa.setVoceMutuo(voceMutuo);
		
		AggiornaQuotaDocumentoSpesa req = new AggiornaQuotaDocumentoSpesa();
		
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(now);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setSubdocumentoSpesa(subdocumentoSpesa);
		
		AggiornaQuotaDocumentoSpesaResponse res = aggiornaQuotaDocumentoSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * 
	 * Inserisce quota documento spesa service.
	 */
	@Test
	public void aggiornaQuotaDocumentoSpesaConTipoIvaSplitReverse() {
		Date now = new Date();
		RicercaDettaglioQuotaSpesa reqRD = new RicercaDettaglioQuotaSpesa();
		reqRD.setDataOra(now);
		reqRD.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(568);
		reqRD.setSubdocumentoSpesa(subdocumentoSpesa);
		
		RicercaDettaglioQuotaSpesaResponse resRD = ricercaDettaglioQuotaSpesaService.executeService(reqRD);
		assertNotNull(resRD);
		assertNotNull(resRD.getSubdocumentoSpesa());
		
		// Imposto il tipo iva split reverse
		subdocumentoSpesa = resRD.getSubdocumentoSpesa();
		subdocumentoSpesa.setTipoIvaSplitReverse(TipoIvaSplitReverse.SPLIT_ISTITUZIONALE);
		
		subdocumentoSpesa.setImportoSplitReverse(new BigDecimal("0.50"));
		
		AggiornaQuotaDocumentoSpesa req = new AggiornaQuotaDocumentoSpesa();
		
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(now);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setSubdocumentoSpesa(subdocumentoSpesa);
		
		AggiornaQuotaDocumentoSpesaResponse res = aggiornaQuotaDocumentoSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	@Autowired
	private ServiceExecutor se; 
	
	@Test
	public void ricercaNoteCreditoIva(){
		RicercaNoteCreditoIvaDocumentoSpesa req = new RicercaNoteCreditoIvaDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoSpesa ds = new DocumentoSpesa();
		ds.setUid(1);
		req.setDocumentoSpesa(ds);
		
		se.executeServiceSuccess(RicercaNoteCreditoIvaDocumentoSpesaService.class, req);
	}
	
	@Test
	public void aggiornaQuotaDocumentoSpesaByRicerca() {
		RicercaDettaglioQuotaSpesa reqRDQS = new RicercaDettaglioQuotaSpesa();
		reqRDQS.setDataOra(new Date());
		reqRDQS.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		reqRDQS.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 72037));
		RicercaDettaglioQuotaSpesaResponse resRDQS = ricercaDettaglioQuotaSpesaService.executeService(reqRDQS);
		
		SubdocumentoSpesa ss = resRDQS.getSubdocumentoSpesa();
		
		List<SospensioneSubdocumento> sospensioni = new ArrayList<SospensioneSubdocumento>();
		ss.setSospensioni(sospensioni);
		
		SospensioneSubdocumento sospensione = new SospensioneSubdocumento();
		sospensione.setDataSospensione(parseDate("13/10/2017"));
		sospensione.setCausaleSospensione("Test 1");
		sospensione.setDataRiattivazione(parseDate("15/10/2017"));
		sospensione.setUid(4);
		sospensioni.add(sospensione);
		
//		sospensione = new SospensioneSubdocumento();
//		sospensione.setDataSospensione(parseDate("17/10/2017"));
//		sospensione.setCausaleSospensione("Test 2, aggiornato");
//		sospensione.setDataRiattivazione(parseDate("19/10/2017"));
//		sospensione.setUid(5);
//		sospensioni.add(sospensione);
		
		sospensione = new SospensioneSubdocumento();
		sospensione.setDataSospensione(parseDate("25/10/2017"));
		sospensione.setCausaleSospensione("Test 5");
		sospensione.setDataRiattivazione(parseDate("30/10/2017"));
		sospensioni.add(sospensione);
		
		AggiornaQuotaDocumentoSpesa req = new AggiornaQuotaDocumentoSpesa();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setAggiornaStatoDocumento(false);
		req.setBilancio(getBilancioTest(143, 2017));
		req.setSubdocumentoSpesa(ss);
		req.setGestisciSospensioni(true);
		
		AggiornaQuotaDocumentoSpesaResponse res = aggiornaQuotaDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaModulareQuoteByDocumentoSpesa() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa req = new RicercaSinteticaModulareQuoteByDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setSubdocumentoSpesaModelDetails(SubdocumentoSpesaModelDetail.ModPag);
//		req.setDocumentoSpesa(create(DocumentoSpesa.class, 78005));
		req.setDocumentoSpesa(create(DocumentoSpesa.class, 77983));
		
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse res = ricercaSinteticaModulareQuoteByDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSubdocumentiPerProvvisorio() {
		final String methodName="ricercaSubdocumentiPerProvvisorio";
		Soggetto soggetto = create(Soggetto.class, 130002);
		soggetto.setCodiceSoggetto("10508");
		subdocumentoSpesaDad.setEnte(create(Ente.class, 2));
		ListaPaginata<SubdocumentoSpesa> ricercaSubdocumentiSpesaPerProvvisorio = subdocumentoSpesaDad.ricercaSubdocumentiSpesaPerProvvisorio(create(TipoDocumento.class,39), 2017, "6048-II", null, null, null, null, soggetto, null, null, getParametriPaginazione(0, 2));
		
		for (SubdocumentoSpesa ss : ricercaSubdocumentiSpesaPerProvvisorio) {
			DocumentoSpesa doc = ss.getDocumento();
			StringBuilder sb = new StringBuilder()
						.append(doc.getAnno())
						.append("/")
						.append(doc.getNumero())
						.append(" quota: ")
						.append(ss.getNumero());
			log.debug(methodName, "trovato: " + sb.toString());
		}
	}
	
	@Autowired
	private SiacTModpagRepository siacTModpagRepository;
	
	@Test
	public void testModpag() {
		List<SiacRSoggettoRelaz> siacRSoggettoRelazs = siacTModpagRepository.findSiacRSoggettoRelazByModpagIdAndSoggettoIdDaSedeSecondaria(13, 16);
	}
	
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	
	@Test
	public void testQuoteAssociare() {
		ListaPaginata<Subdocumento<?,?>> listaSubdocumenti = subdocumentoDad.ricercaSubdocumentiDaAssociare(
				getRichiedenteByProperties("consip", "regp").getAccount().getEnte(),
				getBilancioByProperties("consip", "regp", "2019"),
				TipoFamigliaDocumento.SPESA, //req.getTipoFamigliaDocumento()
				null, //uid elenco
				null, //anno elenco
				null, //numero elenco
				null, //anno provvisorio
				null, //numero provvisorio
				null, //data provvisorio
				create(TipoDocumento.class, 16), //tipoDocumento
				Integer.valueOf(2019), //anno documento
				"521",
				null, // data emissione documento
				null,//numero quota
				null, //numero mobimento
				null, //anno  movimento
				null, //soggetto
				null, //ui provvedimento
				null, //anno provv
				null, // numero provv
				null, //tipo atto
				null, //SAC
				Arrays.asList(StatoOperativoDocumento.VALIDO, StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO,
						StatoOperativoDocumento.PARZIALMENTE_EMESSO),
				Boolean.TRUE, //collegato stesso bilancio
				Boolean.FALSE, //associato provvedimento
				Boolean.FALSE, //importo pagare zero
				Boolean.TRUE, //iva				
				getParametriPaginazioneTest()
				);
	}

}
