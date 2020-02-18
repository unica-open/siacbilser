/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.ahmad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDettaglioAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDettaglioElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaSinteticaQuoteElencoService;
import it.csi.siac.siacbilser.business.service.capitolo.ContaMovimentiAssociatiACapitoloService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiTreePianoDeiContiService;
import it.csi.siac.siacbilser.business.service.consultazioneentita.RicercaSinteticaEntitaConsultabileService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaDettaglioDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaSinteticaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AggiornaPreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaDettaglioPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaIntegrataValidabileService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoComplessivoService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoEntrataService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloFondoPluriennaleVincolatoSpesaService;
import it.csi.siac.siacbilser.business.service.progetto.CalcoloProspettoRiassuntivoCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.CambiaFlagUsatoPerFpvCronoprogrammaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaMovimentiAssociatiACapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaMovimentiAssociatiACapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaCapitoloSpesaConsultabile;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabileResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * 
 */
public class AhmadTest extends BaseJunit4TestCase {
	
	@Autowired
	private CalcoloFondoPluriennaleVincolatoSpesaService calcoloFondoPluriennaleVincolatoSpesaService;
	@Autowired
	private CalcoloFondoPluriennaleVincolatoEntrataService calcoloFondoPluriennaleVincolatoEntrataService;
	@Autowired
	private CalcoloFondoPluriennaleVincolatoComplessivoService calcoloFondoPluriennaleVincolatoComplessivoService;
	@Autowired
	private CalcoloProspettoRiassuntivoCronoprogrammaService calcoloProspettoRiassuntivoCronoprogrammaService;
	@Autowired
	private CambiaFlagUsatoPerFpvCronoprogrammaService cambiaFlagUsatoPerFpvCronoprogrammaService;
	@Autowired
	private RicercaDettaglioRichiestaEconomaleService ricercaDettaglioRichiestaEconomaleService;
	@Autowired 
	private ContaMovimentiAssociatiACapitoloService contaMovimentiAssociatiACapitoloService;
	@Autowired
	private MovimentoGestioneService movimentoGestioneService;
	@Autowired
	private RicercaDettaglioDocumentoSpesaService ricercaDettaglioDocumentoSpesaService;
	@Autowired
	protected SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	@Autowired
	private PrimaNotaDad primanotaDad;
	@Autowired
	private LeggiTreePianoDeiContiService leggiTreePianoDeiContiService;
	@Autowired
	private RicercaSinteticaEntitaConsultabileService  ricercaSinteticaEntitaConsultabileService;
	@Autowired
	private RicercaDettaglioAllegatoAttoService ricercaDettaglioAllegatoAttoService;
	@Autowired
	private RicercaDettaglioDocumentoEntrataService ricercaDettaglioDocumentoEntrataService;
	@Autowired
	private RicercaSinteticaDocumentoEntrataService ricercaSinteticaDocumentoEntrataService;
//	@Autowired
//	private ContatoreRegistroIvaDad contatoreRegistroIvaDad;
	@Autowired
	private InserisceElencoService inserisceService;
	@Autowired
	private RicercaDettaglioElencoService ricercaDettaglioElencoService;
	@Autowired
	private RicercaSinteticaQuoteElencoService ricercaSinteticaQuoteElencoService;
	@Autowired
	private RicercaSinteticaPrimaNotaIntegrataValidabileService ricercaSinteticaPrimaNotaIntegrataValidabileService;
	@Autowired
	private RicercaDettaglioPreDocumentoSpesaService ricercaDettaglioPreDocumentoSpesaService;
	@Autowired
	private AggiornaPreDocumentoDiSpesaService aggiornaPreDocumentoDiSpesaService;
	@Autowired
	private SoggettoService soggettoService;
	
	@Test
	public void calcoloFondoPluriennaleVincolatoSpesa(){
		
		CalcoloFondoPluriennaleVincolatoSpesa req = new CalcoloFondoPluriennaleVincolatoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto = new Progetto();
		progetto.setUid(1);
		req.setProgetto(progetto);

		req.setAnno(2015);
		
		CalcoloFondoPluriennaleVincolatoSpesaResponse res = calcoloFondoPluriennaleVincolatoSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void calcoloFondoPluriennaleVincolatoEntrata(){
		
		CalcoloFondoPluriennaleVincolatoEntrata req = new CalcoloFondoPluriennaleVincolatoEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto = new Progetto();
		progetto.setUid(1);
		req.setProgetto(progetto);

		req.setAnno(2015);
		
		CalcoloFondoPluriennaleVincolatoEntrataResponse res = calcoloFondoPluriennaleVincolatoEntrataService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	@Test
	public void calcoloFondoPluriennaleVincolatoTotale(){
		
		CalcoloFondoPluriennaleVincolatoComplessivo req = new CalcoloFondoPluriennaleVincolatoComplessivo();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto = new Progetto();
		progetto.setUid(1);
		req.setProgetto(progetto);

		req.setAnno(2015);
		
		CalcoloFondoPluriennaleVincolatoComplessivoResponse res = calcoloFondoPluriennaleVincolatoComplessivoService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void calcoloProspettoRiassuntivoCronoprogrammaDiGestione(){
		
		CalcoloProspettoRiassuntivoCronoprogramma req = new CalcoloProspettoRiassuntivoCronoprogramma();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto=new Progetto();
		progetto.setUid(3);

		req.setProgetto(progetto);
		req.setAnno(2014);
		
		CalcoloProspettoRiassuntivoCronoprogrammaResponse res = calcoloProspettoRiassuntivoCronoprogrammaService.executeService(req);
		
		assertNotNull(res);
	}
	
	
	/**
	 * cambia il flag usato per fpv cronoprogramma
	 */
	@Test
	public void cambiaFlagUsatoPerFPVCronoprogramma() {
			
		CambiaFlagUsatoPerFpvCronoprogramma req = new CambiaFlagUsatoPerFpvCronoprogramma();
		req.setRichiedente(getRichiedenteByProperties("consip", "rp"));
		Cronoprogramma c = new Cronoprogramma();
		c.setBilancio(getBilancioTest());
		c.setUsatoPerFpv(true);
		c.setUid(17);
        req.setCronoprogramma(c);
		CambiaFlagUsatoPerFpvCronoprogrammaResponse res=cambiaFlagUsatoPerFpvCronoprogrammaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Test
	 */
	@Test
	public void testAggiornamentoQuotaAhmad() {
		RicercaDettaglioDocumentoSpesa request = new RicercaDettaglioDocumentoSpesa();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(968);
		request.setDocumentoSpesa(documentoSpesa);
		
		RicercaDettaglioDocumentoSpesaResponse response = ricercaDettaglioDocumentoSpesaService.executeService(request);
		
		log.logXmlTypeObject(response, "response ricerca dettaglio documento");
		
//		documentoSpesa = response.getDocumento();
//		
//		
//		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
//		subdocumentoSpesa.setUid(542);
//		SubImpegno si = new SubImpegno();
//		si.setUid(62381);
//		subdocumentoSpesa.setEnte(getEnteTest());
//		Impegno impegno = new Impegno();
//		impegno.setAnnoMovimento(2015);
//		impegno.setNumero(new BigDecimal("2227"));
//		subdocumentoSpesa.setImpegno(impegno);
//		//subdocumentoSpesa.setSubImpegno(si);
//		subdocumentoSpesa.setDocumento(documentoSpesa);
//		subdocumentoSpesa.setNumero(2);
//		subdocumentoSpesa.setImporto(new BigDecimal("21"));
//		VoceMutuo voceMutuo = new VoceMutuo();
//		voceMutuo.setNumeroMutuo("1");
//		subdocumentoSpesa.setVoceMutuo(voceMutuo);
//		AggiornaQuotaDocumentoSpesa req = new AggiornaQuotaDocumentoSpesa();
//		req.setDataOra(new Date());
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setBilancio(getBilancio2015Test());
//        req.setSubdocumentoSpesa(subdocumentoSpesa);		
//		
//	//	log.logXmlTypeObject(req, "REQUEST");
//
//		AggiornaQuotaDocumentoSpesaResponse res = aggiornaQuotaDocumentoSpesaService.executeService(req);
//		assertNotNull(res);
	}
	
	
	/**
	 * Test
	 */
	@Test
	public void testRicercaDettaglioRichiestaEconomale() {
		RicercaDettaglioRichiestaEconomale request = new RicercaDettaglioRichiestaEconomale();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		CassaEconomale cassaEcnomale = new CassaEconomale();
		cassaEcnomale.setUid(2);
		richiestaEconomale.setNumeroRichiesta(44);
		richiestaEconomale.setBilancio(getBilancio2015Test());
		richiestaEconomale.setCassaEconomale(cassaEcnomale);
		request.setRichiestaEconomale(richiestaEconomale);
		log.logXmlTypeObject(request, "REQUEST");

		RicercaDettaglioRichiestaEconomaleResponse response = ricercaDettaglioRichiestaEconomaleService.executeService(request);
		log.logXmlTypeObject(request, "REQUEST");
		assertNotNull(response);
	}
	
	@Test
	public void testContaMovimentiValidiAssociatiACapitolo(){
		ContaMovimentiAssociatiACapitolo req =  new ContaMovimentiAssociatiACapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));		
		req.setIdCapitolo(18429);
		log.logXmlTypeObject(req, "REQUEST");

		ContaMovimentiAssociatiACapitoloResponse res =  contaMovimentiAssociatiACapitoloService.executeService(req);
		log.logXmlTypeObject(res, "RESPONSE");
		assertNotNull(res);
		
	}

	@Test
	public void testSoggettoSospeso(){
		SubdocumentoSpesa sub = new SubdocumentoSpesa();
		sub.setUid(1141);
		Soggetto s =  new Soggetto();
		s.setUid(14);
		DocumentoSpesa doc =  new DocumentoSpesa();
		doc.setUid(894);
		doc.setSoggetto(s);
		sub.setDocumento(doc);
		boolean pagamentoSospeso =  subdocumentoSpesaDad.isSoggettoAttoSubdocSospeso(sub, 14);
	    log.debug("test", pagamentoSospeso);
	    
//		SubdocumentoSpesa sub = new SubdocumentoSpesa();
//		sub.setUid(130);
//		Soggetto s =  new Soggetto();
//		s.setUid(14);
//		DocumentoSpesa doc =  new DocumentoSpesa();
//		doc.setUid(894);
//		doc.setSoggetto(s);
//		sub.setDocumento(doc);
//		boolean pagamentoSospeso =  subdocumentoSpesaDad.isSoggettoAttoSubdocSospeso(sub, 14);
//	    log.debug("test", pagamentoSospeso);
	}
	
	@Test
	public void testFindNotaCreditoIvaAssociataUid(){
		final String methodName = "findNotaCreditoIvaAssociata";
		SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
		subdoc.setUid(210);
		SiacTSubdocIva siacTSubdocIva = siacTSubdocRepository.findSiacTSubdocIvaNotaCreditoIvaAssociata(subdoc.getUid());
		if(siacTSubdocIva==null){
			log.debug(methodName, "returning null");
		}
		log.logXmlTypeObject(siacTSubdocIva,"subdoc");
		
	}

	@Test
	public void testCercaPrimaNotaIntegrata() {

		PrimaNota primaNota = primanotaDad.findPrimaNotaByUid(382);
		log.logXmlTypeObject(primaNota, "PRIMA NOTA ");
	}

//	@Test
//	public void findDataProtocolloProvv(){
//		
//			Date ultimaDataProtocolloProvv = contatoreRegistroIvaDad.findDataProtocolloProvv(6, uidPeriodo); 
//			
//			if(ultimaDataProtocolloProvv!= null && subdocIva.getDataProtocolloProvvisorio().compareTo(ultimaDataProtocolloProvv)<0){
//				throw new BusinessException(ErroreFin.DATA_DI_REGISTRAZIONE_ANTECEDENTE_ALLA_DATA_DI_REGISTRAZIONE_DELL_ULTIMO_DOCUMENTO_REGISTRATO_NEL_SISTEMA_IN_QUELLO_STESSO_REGISTRO.getErrore());
//			 }
//			
//	}
	
	@Test
	public void testLeggiTreePianoDeiContiFULLService(){
		LeggiTreePianoDeiConti req =  new LeggiTreePianoDeiConti();
		req.setIdCodificaPadre(0);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setIdEnteProprietario(1);
		req.setAnno(2015);
		req.setDataOra(new Date());
		LeggiTreePianoDeiContiResponse res = leggiTreePianoDeiContiService.executeService(req);
		log.logXmlTypeObject(res, "responseSeervizio leggiTreePianoDeiConti");
		assertNotNull(res);
	}
	
	@Test
	public void testRicercaSinteticaEntitaConsultabileService(){
		/****************ENTITA CONSULTABILE**************************/
		ParametriRicercaCapitoloSpesaConsultabile prcsc = new ParametriRicercaCapitoloSpesaConsultabile();		
		prcsc.setTipoCapitolo("Spesa");
		prcsc.setFaseBilancio(FaseBilancio.GESTIONE);
		prcsc.setAnnoCapitolo(2015);

		/***************Ricerca*****************************/
		RicercaSinteticaEntitaConsultabile request = new RicercaSinteticaEntitaConsultabile();
		request.setParametriRicercaEntitaConsultabile(prcsc);
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setParametriPaginazione(getParametriPaginazioneTest());
		
		/***************RESPONSE*****************************/
		RicercaSinteticaEntitaConsultabileResponse response = ricercaSinteticaEntitaConsultabileService.executeService(request);
		log.logXmlTypeObject(request , "request  del servizio ricercaSinteticaEntitaConsultabileService ");

		log.logXmlTypeObject(response, "response del servizio ricercaSinteticaEntitaConsultabileService ");
	}
	
	@Test
	public void ricercaDettaglioAllegatoAtto() {
		RicercaDettaglioAllegatoAtto req = new RicercaDettaglioAllegatoAtto();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(33);
		req.setAllegatoAtto(allegatoAtto);
		RicercaDettaglioAllegatoAttoResponse res = ricercaDettaglioAllegatoAttoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void testRicercaDettaglioDocumentoSpesaFORN2() {
		RicercaSinteticaDocumentoEntrata rse = new RicercaSinteticaDocumentoEntrata();
		DocumentoEntrata documentoEntrata  = new DocumentoEntrata();
		documentoEntrata.setNumero("334");
		documentoEntrata.setAnno(2016);
		documentoEntrata.setEnte(getEnteForn2());

		rse.setDocumentoEntrata(documentoEntrata);
		rse.setRichiedente(getRichiedenteFORN2());
		rse.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaDocumentoEntrataResponse responseRSDE = new RicercaSinteticaDocumentoEntrataResponse();

		responseRSDE= ricercaSinteticaDocumentoEntrataService.executeService(rse);
		log.logXmlTypeObject(responseRSDE, "RESPONSE RICERCA SINTETICA ");
		
		RicercaDettaglioDocumentoEntrata request = new RicercaDettaglioDocumentoEntrata();
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteFORN2());
		DocumentoEntrata documentoEntrataa = new DocumentoEntrata();
		documentoEntrataa.setUid(responseRSDE.getDocumenti().get(0).getUid());
		request.setDocumentoEntrata(documentoEntrataa);
		
		RicercaDettaglioDocumentoEntrataResponse response = ricercaDettaglioDocumentoEntrataService.executeService(request);
		
		log.logXmlTypeObject(response, "response ricerca dettaglio documento");
//		
	}
	
	@Test
	public void inserisceElencoService() {

		
		InserisceElenco req2 = new InserisceElenco();
	
		req2.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req2.setBilancio(getBilancio2015Test());
		
		ElencoDocumentiAllegato e = new ElencoDocumentiAllegato();
		
		
//		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setEnte(getEnteTest());
		allegatoAtto.setUid(211);
		e.setAllegatoAtto(allegatoAtto);
		e.setSysEsterno("Sono uno da sistema esterno!!");
		e.setAnnoSysEsterno(2015);
		
		e.setAnno(2015);
		e.setNumero(1);
		e.setDataTrasmissione(new Date());
		e.setEnte(getEnteTest());
		
		
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?, ?>>();
		
		int subdocIDPartenza = 1450;
		
		for(int i=0;i<30;i++){
			SubdocumentoEntrata s = new SubdocumentoEntrata();
			s.setUid(subdocIDPartenza+i);
			subdocumenti.add(s);
		}
		
		e.setSubdocumenti(subdocumenti);
		e.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		
		
		req2.setElencoDocumentiAllegato(e);
		
		InserisceElencoResponse res = inserisceService.executeService(req2);
		log.logXmlTypeObject(res, "ss");
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioElencoDocService(){
		
		RicercaDettaglioElenco req = new RicercaDettaglioElenco();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		ElencoDocumentiAllegato elenco = new ElencoDocumentiAllegato();
		elenco.setUid(261);
		req.setElencoDocumentiAllegato(elenco);
		RicercaDettaglioElencoResponse res = ricercaDettaglioElencoService.executeService(req);

		log.logXmlTypeObject(res, "RESPONSE RICERCA DETTAGLIO");
	}
	@Test
	public void ricercaDettaglioQuoteElencoDocService(){
		
		RicercaSinteticaQuoteElenco req = new RicercaSinteticaQuoteElenco();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		ElencoDocumentiAllegato elenco = new ElencoDocumentiAllegato();
		elenco.setUid(263);
		req.setElencoDocumentiAllegato(elenco);
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(10);
		parametriPaginazione.setNumeroPagina(2);
		req.setParametriPaginazione(parametriPaginazione);
		RicercaSinteticaQuoteElencoResponse res = ricercaSinteticaQuoteElencoService.executeService(req);

		log.logXmlTypeObject(res, "RESPONSE RICERCA DETTAGLIO");
	}
	
	@Test
	public void ricercaSinteticaPrimaNotaIntegrataValidabile() {
		RicercaSinteticaPrimaNotaIntegrataValidabile req = new RicercaSinteticaPrimaNotaIntegrataValidabile();
		
		req.setRichiedente(getRichiedenteFORN2());
		req.setBilancio(getBilancioForn2());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		String dataA="2016-07-25 00:00:00";
		String dataDA="2016-07-01 00:00:00";
		String format ="yyyy-MM-dd HH:mm:ss";
		Date dataRegistrazioneA = parseDate(dataA, format);
		Date dataRegistrazioneDa = parseDate(dataDA, format);
		
		req.setDataRegistrazioneA(dataRegistrazioneA);
		req.setDataRegistrazioneDa(dataRegistrazioneDa);

		PrimaNota primaNota = new PrimaNota();
		primaNota.setAmbito(Ambito.AMBITO_FIN);
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		req.setPrimaNota(primaNota);
		
		RicercaSinteticaPrimaNotaIntegrataValidabileResponse res = ricercaSinteticaPrimaNotaIntegrataValidabileService.executeService(req);
		log.logXmlTypeObject(req,"REQUEST DEL SERVIZIO RICERCA RicercaSinteticaPrimaNotaIntegrataValidabile");
		log.logXmlTypeObject(res,"RESPONSE DEL SERVIZIO RICERCA RicercaSinteticaPrimaNotaIntegrataValidabile");
	}
	
	
	
	/*************************** RICERCA IMPEGNO PER CHIAVE **************************************************************/
	
	
	/**
	 * Test per RicercaImpegnoPerChiave
	 */
	@Test
	public void testRicercaImpegnoPerChiave() {
		RicercaImpegnoPerChiave req = new RicercaImpegnoPerChiave();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		
		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		req.setpRicercaImpegnoK(pRicercaImpegnoK);
		
		pRicercaImpegnoK.setAnnoEsercizio(2015);
		pRicercaImpegnoK.setAnnoImpegno(2015);
		pRicercaImpegnoK.setNumeroImpegno(new BigDecimal("496"));//2300
//		pRicercaImpegnoK.setNumeroSubDaCercare(new BigDecimal("14"));
		RicercaImpegnoPerChiaveResponse response = movimentoGestioneService.ricercaImpegnoPerChiave(req);
		
		log.logXmlTypeObject(req, "REQUEST DEL SERVIZIO RicercaImpegnoPerChiave");
		log.logXmlTypeObject(response, "RESPONSE DEL SERVIZIO RicercaImpegnoPerChiave");
	}
	
	/**
	 * Test per RicercaImpegnoPerChiaveOttimizzato
	 */
	@Test
	public void testRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato req = new RicercaImpegnoPerChiaveOttimizzato();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		
		req.setEscludiSubAnnullati(true);
		req.setSubPaginati(true);
		req.setCaricaSub(true);
		
		
		//req.setNumPagina(1);
		//req.setNumRisultatiPerPagina(13);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
//		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCig(true);
//		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCup(true);
		//
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaMutui(true);
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaVociMutuo(true);
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		//tutto il resto e' false se non lo setto
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
	
		
		
		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		
		req.setpRicercaImpegnoK(pRicercaImpegnoK);
		
		
		pRicercaImpegnoK.setAnnoEsercizio(2015);
		// ************* IMPEGNO CON 13 SUB  DI CUI UNO ANNULLATO(N.2) E UN SUB CON MUTUO (N.14)
		pRicercaImpegnoK.setAnnoImpegno(2015);
		pRicercaImpegnoK.setNumeroImpegno(new BigDecimal("2300"));
		pRicercaImpegnoK.setNumeroSubDaCercare(new BigDecimal("14"));
		//pRicercaImpegnoK.setNumeroSubDaCercare(BigDecimal.ZERO);
		
		// ************** IMPEGNO CON VOCE MUTUO
		//pRicercaImpegnoK.setAnnoImpegno(2014);
		//pRicercaImpegnoK.setNumeroImpegno(new BigDecimal("2227"));
		
		// ************* IMPEGNO CON CIG E CUP
		//pRicercaImpegnoK.setAnnoImpegno(2015);
		//pRicercaImpegnoK.setNumeroImpegno(new BigDecimal("166"));
		
		//pRicercaImpegnoK.setCaricaDatiUlteriori(Boolean.FALSE);
		//pRicercaImpegnoK.setCaricaSediEModalitaPagamento(Boolean.FALSE);
//		RicercaAttributiMovimentoGestioneOttimizzato attributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();

		
		
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
	
		log.logXmlTypeObject(req, "REQUEST DEL SERVIZIO RicercaImpegnoPerChiaveOttimizzato");
		log.logXmlTypeObject(response, "RESPONSE DEL SERVIZIO RicercaImpegnoPerChiaveOttimizzato");

	}
	
	/**
	 * Test per RicercaImpegnoPerChiaveOttimizzato
	 */
	@Test
	public void testRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato req = new RicercaAccertamentoPerChiaveOttimizzato();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		
		req.setEscludiSubAnnullati(true);
		req.setSubPaginati(true);
		//req.setCaricaSub(true);
		
		
		//req.setNumPagina(1);
		//req.setNumRisultatiPerPagina(13);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
//		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCig(true);
//		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCup(true);
		//
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaMutui(true);
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaVociMutuo(true);
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		//tutto il resto e' false se non lo setto
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
	
		
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		
		req.setpRicercaAccertamentoK(pRicercaAccertamentoK);;
		
		
		pRicercaAccertamentoK.setAnnoEsercizio(2015);
		// ************* IMPEGNO CON 13 SUB  DI CUI UNO ANNULLATO(N.2) E UN SUB CON MUTUO (N.14)
		pRicercaAccertamentoK.setAnnoAccertamento(2015);
		pRicercaAccertamentoK.setNumeroAccertamento(new BigDecimal("2300"));
		//pRicercaAccertamentoK.setNumeroSubDaCercare(new BigDecimal("14"));
		pRicercaAccertamentoK.setNumeroSubDaCercare(BigDecimal.ZERO);
		
		// ************** IMPEGNO CON VOCE MUTUO
		//pRicercaAccertamentoK.setAnnoImpegno(2014);
		//pRicercaAccertamentoK.setNumeroImpegno(new BigDecimal("2227"));
		
		// ************* IMPEGNO CON CIG E CUP
		//pRicercaAccertamentoK.setAnnoImpegno(2015);
		//pRicercaAccertamentoK.setNumeroImpegno(new BigDecimal("166"));
		
		//pRicercaAccertamentoK.setCaricaDatiUlteriori(Boolean.FALSE);
		//pRicercaAccertamentoK.setCaricaSediEModalitaPagamento(Boolean.FALSE);
				
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		assertNotNull(response);
	}
	/**
	 * Test per RicercaImpegnoPerChiaveOttimizzato
	 */
	@Test
	public void testRicercaAccertamentoPerChiaveOttimizzatoSimulazioneApp() {
		RicercaAccertamentoPerChiaveOttimizzato req = new RicercaAccertamentoPerChiaveOttimizzato();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		
		req.setEscludiSubAnnullati(true);
		req.setSubPaginati(true);
		//req.setCaricaSub(true);
		
		
		//req.setNumPagina(1);
		//req.setNumRisultatiPerPagina(13);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
//		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCig(true);
//		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCup(true);
		//
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaMutui(true);
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaVociMutuo(true);
		//datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		//tutto il resto e' false se non lo setto
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
	
		
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		
		req.setpRicercaAccertamentoK(pRicercaAccertamentoK);;
		
		
		pRicercaAccertamentoK.setAnnoEsercizio(2015);
		// ************* IMPEGNO CON 13 SUB  DI CUI UNO ANNULLATO(N.2) E UN SUB CON MUTUO (N.14)
		pRicercaAccertamentoK.setAnnoAccertamento(2015);
		pRicercaAccertamentoK.setNumeroAccertamento(new BigDecimal("2300"));
		//pRicercaAccertamentoK.setNumeroSubDaCercare(new BigDecimal("14"));
		pRicercaAccertamentoK.setNumeroSubDaCercare(BigDecimal.ZERO);
		
		// ************** IMPEGNO CON VOCE MUTUO
		//pRicercaAccertamentoK.setAnnoImpegno(2014);
		//pRicercaAccertamentoK.setNumeroImpegno(new BigDecimal("2227"));
		
		// ************* IMPEGNO CON CIG E CUP
		//pRicercaAccertamentoK.setAnnoImpegno(2015);
		//pRicercaAccertamentoK.setNumeroImpegno(new BigDecimal("166"));
		
		//pRicercaAccertamentoK.setCaricaDatiUlteriori(Boolean.FALSE);
		//pRicercaAccertamentoK.setCaricaSediEModalitaPagamento(Boolean.FALSE);
				
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		assertNotNull(response);
	}
	
	@Test
	public void testAggiornaPreDocumentoDiSpesa(){
		AggiornaPreDocumentoDiSpesa request =  new AggiornaPreDocumentoDiSpesa();
		
		PreDocumentoSpesa predocumentoDiSpesa = new PreDocumentoSpesa();
		predocumentoDiSpesa.setUid(13);
		RicercaDettaglioPreDocumentoSpesa reqRicerca = new RicercaDettaglioPreDocumentoSpesa();
		reqRicerca.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRicerca.setPreDocumentoSpesa(predocumentoDiSpesa);
		RicercaDettaglioPreDocumentoSpesaResponse response = ricercaDettaglioPreDocumentoSpesaService.executeService(reqRicerca);
		log.logXmlTypeObject(response, "response della ricerca dettaglio");
		
		Impegno impegno =  new Impegno();
		impegno.setUid(41028);
		impegno.setAnnoMovimento(2015);
		impegno.setNumero(new BigDecimal(496));
		response.getPreDocumentoSpesa().setImpegno(impegno);
		request.setPreDocumentoSpesa(response.getPreDocumentoSpesa());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setBilancio(getBilancio2015Test());
		AggiornaPreDocumentoDiSpesaResponse response2 =  aggiornaPreDocumentoDiSpesaService.executeService(request);
		log.logXmlTypeObject(request, "request  del servizio aggiorna predocumento spesa service");

		log.logXmlTypeObject(response2, "response del servizio aggiorna predocumento spesa service");
		
	}

	@Test
	public void  testRicercaModalitaPagamentoPerchiave(){
	RicercaModalitaPagamentoPerChiave request =  new RicercaModalitaPagamentoPerChiave();
	request.setRichiedente(getRichiedenteByProperties("consip","regp"));
	request.setEnte(getEnteTest());
	ModalitaPagamentoSoggetto mp =  new ModalitaPagamentoSoggetto();
	mp.setCodiceModalitaPagamento("1");
	Soggetto soggetto = new Soggetto();
	soggetto.setCodiceSoggetto("29");
	
	request.setModalitaPagamentoSoggetto(mp);
	request.setSoggetto(soggetto);
	RicercaModalitaPagamentoPerChiaveResponse response = soggettoService.ricercaModalitaPagamentoPerChiave(request);
	log.logXmlTypeObject(response, "response ricercaModalitaPagamentoPerChiaveCached ");
	}
}
