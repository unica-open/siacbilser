/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.predocumento;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriByTipologieClassificatoriService;
import it.csi.siac.siacbilser.business.service.documento.RicercaQuoteDaAssociarePredocumentoService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaPreDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaPreDocumentoEntrataCollegaDocumentoService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AnnullaPreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.CompletaDefiniscePreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.InseriscePreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.LeggiContiCorrenteService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaDettaglioPreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaSinteticaPreDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.RicercaTotaliPreDocumentoEntrataPerStatoService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.ValidaStatoOperativoPreDocumentoEntrataService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatoriResponse;
import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoEntrataCollegaDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoEntrataCollegaDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrenteResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.OrdinamentoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.ContoCorrente;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class PreDocumentoEntrataMVTest.
 */
public class PreDocumentoEntrataTest extends BaseJunit4TestCase {
	
	/** The leggi conti corrente service. */
	@Autowired
	private LeggiContiCorrenteService leggiContiCorrenteService;
	/** The documento entrata service. */
	@Autowired
	private InseriscePreDocumentoEntrataService inseriscePreDocumentoEntrataService;
	/** The aggiorna documento entrata service. */
	@Autowired
	private ValidaStatoOperativoPreDocumentoEntrataService validaStatoOperativoPreDocumentoEntrataService;
	@Autowired
	private LeggiClassificatoriByTipologieClassificatoriService leggiClassificatoriByTipologieClassificatoriService ;
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad ;
	@Autowired
	private RicercaSinteticaPreDocumentoEntrataService ricercaSinteticaPreDocumentoEntrataService;
	@Autowired
	private AggiornaPreDocumentoDiEntrataService aggiornaPreDocumentoDiEntrataService;
	@Autowired
	private RicercaDettaglioPreDocumentoEntrataService ricercaDettaglioPreDocumentoEntrataService;
	@Autowired
	private AnnullaPreDocumentoEntrataService annullaPreDocumentoEntrataService;
	@Autowired
	private CompletaDefiniscePreDocumentoEntrataService completaDefiniscePreDocumentoEntrataService;
	@Autowired
	private RicercaTotaliPreDocumentoEntrataPerStatoService ricercaTotaliPreDocumentoEntrataPerStatoService;
	
	@Autowired
	private RicercaQuoteDaAssociarePredocumentoService ricercaQuoteDaAssociarePredocumentoService;

	@Autowired
	private AccertamentoBilDad accertamentoBilDad;
	
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	
	/**
	 * Leggi conti corrente.
	 */
	@Test
	public void leggiContiCorrente() {
				
		LeggiContiCorrente req = new LeggiContiCorrente();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
						
		LeggiContiCorrenteResponse res = leggiContiCorrenteService.executeService(req);
		
		assertNotNull(res);
	}
	
	/**
	 * Leggi conti corrente.
	 */
	@Test
	public void leggiContiCorrenteFORN2() {
				
		LeggiContiCorrente req = new LeggiContiCorrente();
		req.setRichiedente(getRichiedenteByProperties("forn2", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
						
		LeggiContiCorrenteResponse res = leggiContiCorrenteService.executeService(req);
		List<ContoCorrente> listaInSessione = new ArrayList<ContoCorrente>();
		List<ContoCorrente> listaFiltrata =new ArrayList<ContoCorrente>();
		listaInSessione = res.getContiCorrenti();

		for(ContoCorrente cc : listaInSessione){
			ModalitaAccreditoSoggetto mas = cc.getModalitaAccreditoSoggetto();
			boolean isModalitaPagamentoContoCorrente = mas != null &&
					("CB".equals(mas.getCodice()) /*CCB*/
							|| "CCP".equals(mas.getCodice()))/*CCP*/;
			
			if(isModalitaPagamentoContoCorrente && StatoEntita.VALIDO.name().equals(cc.getCodiceStatoModalitaPagamento())){	
				listaFiltrata.add(cc);
			}		
		
		}
		
		for (ContoCorrente cc : listaFiltrata){
			ModalitaAccreditoSoggetto mas = cc.getModalitaAccreditoSoggetto();
			log.debug("filtraCC", "conto Corrente " + cc.getDescrizioneModalitaAccreditoPagamentoSoggetto()
				+ " e modalita pagamento: " + mas.getCodice() + '-' + mas.getDescrizione()
					+ " con uid: " + cc.getUid());
		}
		
	}
	
	
	/**
	 * Inserisci pre documento entrata.
	 */
	@Test
	public void inserisciPreDocumentoEntrata() {
		InseriscePreDocumentoEntrata req = new InseriscePreDocumentoEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancioTest());
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		preDoc.setEnte(getEnteTest());
		
		preDoc.setNumero("34");
		preDoc.setDescrizione("pre doc di test 4");
		preDoc.setImporto(new BigDecimal("0.01"));
		preDoc.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDoc.setDataDocumento(new Date());
		preDoc.setDataCompetenza(new Date());
		preDoc.setPeriodoCompetenza("mio periodo competenza");
		ProvvisorioDiCassa provvisorioDiCassa=new ProvvisorioDiCassa();
		provvisorioDiCassa.setAnno(2014);
		provvisorioDiCassa.setNumero(123);
		preDoc.setProvvisorioDiCassa(provvisorioDiCassa);
		CausaleEntrata causale = new CausaleEntrata();
		causale.setUid(1);
		preDoc.setCausaleEntrata(causale);
		
		
		preDoc.setNote("mie note predocumento 1");
		
				
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		preDoc.setSoggetto(soggetto);
		
//		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
//		capitoloEntrataGestione.setUid(10);
//		preDoc.setCapitoloEntrataGestione(capitoloEntrataGestione);
		
		
		Accertamento impegno= new Accertamento();
		//impegno.setUid(2);
		impegno.setAnnoMovimento(2013);
		impegno.setNumeroBigDecimal(new BigDecimal("1"));
		preDoc.setAccertamento(null);
		
//		SubAccertamento subImpegno = new SubAccertamento();
//		subImpegno.setUid(4);
//		subImpegno.setAnnoMovimento(2013);
//		subImpegno.setNumero(new BigDecimal("3"));
//		preDoc.setSubAccertamento(subImpegno);
		
		
		req = marshallUnmarshall(req);
		
		req.setPreDocumentoEntrata(preDoc);
		
		InseriscePreDocumentoEntrataResponse res = inseriscePreDocumentoEntrataService.executeService(req);
		
		res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	@Test
	public void inserisciPreDocumentoEntrataFromFile(){
		InseriscePreDocumentoEntrata req = getTestFileObject(InseriscePreDocumentoEntrata.class, "it/csi/siac/siacbilser/test/business/predocumento/inseriscePreDocumentoEntrata.xml");
		InseriscePreDocumentoEntrataResponse res = inseriscePreDocumentoEntrataService.executeService(req);
		System.out.println(JAXBUtility.marshall(res));
		assertNotNull(res);
	}
	
	/**
	 * Aggiorna pre documento entrata.
	 */
	@Test
	public void aggiornaPreDocumentoEntrata() {
		AggiornaPreDocumentoDiEntrata req = new AggiornaPreDocumentoDiEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setBilancio(getBilancioTest(143, 2017));
		req.setDataOra(new Date());
		req.setGestisciModificaImportoAccertamento(true);
		
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		req.setPreDocumentoEntrata(preDoc);
		preDoc.setUid(8);
		preDoc.setEnte(req.getRichiedente().getAccount().getEnte());
		preDoc.setDataCompetenza(parseDate("02/10/2017", "dd/MM/yyyy"));
		preDoc.setPeriodoCompetenza("201710");
		preDoc.setCausaleEntrata(create(CausaleEntrata.class, 834));
		preDoc.setDataDocumento(parseDate("02/10/2017", "dd/MM/yyyy"));
		preDoc.setImporto(new BigDecimal("6"));
		preDoc.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDoc.setNumero(8);
		preDoc.setAccertamento(create(Accertamento.class, 84759));
		preDoc.getAccertamento().setAnnoMovimento(2017);
		preDoc.getAccertamento().setNumeroBigDecimal(new BigDecimal("61"));
		
		AggiornaPreDocumentoDiEntrataResponse res = aggiornaPreDocumentoDiEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void validaStatoOperativoPreDocumentoEntrata(){
		ValidaStatoOperativoPreDocumentoEntrata req= new ValidaStatoOperativoPreDocumentoEntrata();
		req.setBilancio(getBilancioTest());
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.COMPLETO);
		
		PreDocumentoEntrata predoc = new PreDocumentoEntrata();
		predoc.setUid(75);
		predoc.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.ANNULLATO);
		
		req.setPreDocumentoEntrata(predoc);
		
		ValidaStatoOperativoPreDocumentoEntrataResponse res = validaStatoOperativoPreDocumentoEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaPreDocumentoEntrata() {
		RicercaSinteticaPreDocumentoEntrata req = new RicercaSinteticaPreDocumentoEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setAnnoBilancio(Integer.valueOf(2020));
		
		PreDocumentoEntrata preDocumentoEntrata = new PreDocumentoEntrata();
		preDocumentoEntrata.setEnte(req.getRichiedente().getAccount().getEnte());
		preDocumentoEntrata.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		req.setPreDocumentoEntrata(preDocumentoEntrata);
		
		req.setUidPredocumentiDaFiltrare(new ArrayList<Integer>());
		req.getUidPredocumentiDaFiltrare().add(2818);
		
		//req.setOrdinamentoPreDocumentoEntrata(OrdinamentoPreDocumentoEntrata.PERIODO_COMPETENZA);
		req.setOrdinamentoPreDocumentoEntrata(OrdinamentoPreDocumentoEntrata.DATA_COMPETENZA_NOMINATIVO);
		
		//req.setOrdinativoIncasso(create(OrdinativoIncasso.class, 97656));
//		OrdinativoIncasso ordinativoIncasso = new OrdinativoIncasso();
//		ordinativoIncasso.setAnno(Integer.valueOf(2017));
//		ordinativoIncasso.setNumero(Integer.valueOf(7));
//		req.setOrdinativoIncasso(ordinativoIncasso);
		
		RicercaSinteticaPreDocumentoEntrataResponse res = ricercaSinteticaPreDocumentoEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	
	
	@Test
	public void ricercaSinteticaPreDocumentoEntrata2() {
		RicercaDettaglioPreDocumentoEntrata req = new RicercaDettaglioPreDocumentoEntrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setPreDocumentoEntrata(create(PreDocumentoEntrata.class, 5257));
		
		RicercaDettaglioPreDocumentoEntrataResponse res = ricercaDettaglioPreDocumentoEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	
	
	@Test
	public void aggiornaPreDocumentoEntrataTestAhmad() {
		AggiornaPreDocumentoDiEntrata req = new AggiornaPreDocumentoDiEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancioTest());
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		preDoc.setEnte(getEnteTest());
		 
		preDoc.setNumero(30);
		preDoc.setUid(30);
		ContoCorrentePredocumentoEntrata contocorrente = new ContoCorrentePredocumentoEntrata();
		contocorrente.setUid(1);
		preDoc.setContoCorrente(contocorrente);
		
		preDoc.setDescrizione("pre doc di test");
		preDoc.setImporto(new BigDecimal("5555"));
		preDoc.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDoc.setDataDocumento(new Date());
		preDoc.setDataCompetenza(new Date());
		preDoc.setPeriodoCompetenza("mio periodo competenza");
		
		ProvvisorioDiCassa provvisorioDiCassa = new ProvvisorioDiCassa();
		provvisorioDiCassa.setAnno(2014);
		provvisorioDiCassa.setUid(5637);
		provvisorioDiCassa.setNumero(123);
		preDoc.setProvvisorioDiCassa(provvisorioDiCassa);
		
		CausaleEntrata causale = new CausaleEntrata();
		causale.setUid(1);
		preDoc.setCausaleEntrata(causale);
		
		
		preDoc.setNote("mie note predocumento 1 aggiornate");
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
	    req.setPreDocumentoEntrata(preDoc);					
		AggiornaPreDocumentoDiEntrataResponse res = aggiornaPreDocumentoDiEntrataService.executeService(req);
		assertNotNull(res);		
	}

	@Test
	public void ricercaDettaglioPredocumentoEntrataAhmad() {
		RicercaDettaglioPreDocumentoEntrata req = new RicercaDettaglioPreDocumentoEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		preDoc.setEnte(getEnteTest());
		 
		preDoc.setUid(30);

		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
	    req.setPreDocumentoEntrata(preDoc);					
	    RicercaDettaglioPreDocumentoEntrataResponse res = ricercaDettaglioPreDocumentoEntrataService.executeService(req);
		assertNotNull(res);		
	}
	
	@Test
	public void annullaPreDocumentoDiEntrata(){
		AnnullaPreDocumentoEntrata req= new AnnullaPreDocumentoEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		preDoc.setEnte(getEnteTest());
		 
		preDoc.setUid(34);
		req.setPreDocumentoEntrata(preDoc);
		AnnullaPreDocumentoEntrataResponse res = annullaPreDocumentoEntrataService.executeService(req);
		assertNotNull(res);		

	}
	
	@Test
	public void ottieniContoCorrentePredocumentoEntrata(){
		LeggiClassificatoriByTipologieClassificatori req = new LeggiClassificatoriByTipologieClassificatori();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setBilancio(getBilancio2015Test());
		List<TipologiaClassificatore> lista = new ArrayList<TipologiaClassificatore>();
		lista.add(TipologiaClassificatore.CONTO_CORRENTE_PREDISPOSIZIONE_INCASSO);
		req.setListaTipologieClassificatori(lista);
		
		LeggiClassificatoriByTipologieClassificatoriResponse response  = leggiClassificatoriByTipologieClassificatoriService.executeService(req);
		assertNotNull(response);
	}
	
	@Test
	public void ricercaSinteticaPreDocumentoByContoCorrente(){
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		preDoc.setNumero(86);
		preDoc.setEnte(getEnteTest());
		ContoCorrentePredocumentoEntrata ccpe = new ContoCorrentePredocumentoEntrata();
		//ccpe.setUid(455991);
		preDoc.setContoCorrente(ccpe);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(5);
		pp.setNumeroPagina(0);
		
		Boolean contoCorrenteMancante = Boolean.TRUE;
		
		ListaPaginata<PreDocumentoEntrata> listaDocumentoEntrata = preDocumentoEntrataDad.ricercaSinteticaPreDocumento(preDoc, null,
				null,null, null, null,
				Boolean.FALSE,  Boolean.FALSE, Boolean.FALSE, contoCorrenteMancante, null, null, null,null, pp);
		log.debug("ricercaSinteticaPreDocumentoByContoCorrente", "Trovati " + listaDocumentoEntrata.size() + "predocumenti");
		for (PreDocumentoEntrata pde : listaDocumentoEntrata) {
			log.debug("ricercaSinteticaPreDocumentoByContoCorrente", "PreDocumento uid: " + pde.getUid()
					+ " e numero " + pde.getNumero() 
					+ " con CC " + (pde.getContoCorrente() != null? pde.getContoCorrente().getCodice(): "null")
					+ " con importo " + pde.getImportoNotNull()
					);
		}
	}
	
	@Test
	public void testQuery () {
		preDocumentoEntrataDad.setEnte(create(Ente.class, 16));
		preDocumentoEntrataDad.findPreDocumentiByCausaleDataCompetenzaStati(15, new Date(), new Date(), create(ContoCorrentePredocumentoEntrata.class, 13),Arrays.asList(Integer.valueOf(16)),  Arrays.asList(StatoOperativoPreDocumento.INCOMPLETO), PreDocumentoEntrataModelDetail.Accertamento);
	}
	
	@Test
	public void testQuery2 () {
		preDocumentoEntrataDad.setEnte(create(Ente.class, 2));
		boolean hasDocumentiCollegati = preDocumentoEntrataDad.hasDocumentiCollegati(create(PreDocumento.class, 54299));
		System.out.println("hasDocumentiCollegati?" + hasDocumentiCollegati);
	}
	
	@Test
	public void ricercaSinteticaServizioPreDocumentoByContoCorrente(){
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		preDoc.setEnte(getEnteTest());
		ContoCorrentePredocumentoEntrata ccpe = new ContoCorrentePredocumentoEntrata();
		ccpe.setUid(455991);
		preDoc.setContoCorrente(ccpe);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(5);
		pp.setNumeroPagina(0);
		
		RicercaSinteticaPreDocumentoEntrata req = new RicercaSinteticaPreDocumentoEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(pp);
		req.setPreDocumentoEntrata(preDoc);
		Boolean contoCorrenteMancante = null;
		req.setContoCorrenteMancante(contoCorrenteMancante);
		
		RicercaSinteticaPreDocumentoEntrataResponse response = ricercaSinteticaPreDocumentoEntrataService.executeService(req);
		
		for (PreDocumentoEntrata pde : response.getPreDocumenti()) {
			log.debug("ricercaSinteticaPreDocumentoByContoCorrente", "PreDocumento uid: " + pde.getUid()
					+ " e numero " + pde.getNumero() 
					+ " con CC " + (pde.getContoCorrente() != null? pde.getContoCorrente().getCodice(): "null")
					+ " con importo " + pde.getImportoNotNull()
					);
		}
	}
	
	@Test
	public void associaImputazioniContabiliVariate(){
		AssociaImputazioniContabiliVariatePreDocumentoEntrata request = new AssociaImputazioniContabiliVariatePreDocumentoEntrata();
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setDataOra(new Date());
		
		Soggetto sog = new Soggetto();
		sog.setUid(14);
		
		Accertamento acc = new Accertamento();
		acc.setUid(0);
		
		SubAccertamento subacc = new SubAccertamento();
		subacc.setUid(0);
		
		CapitoloEntrataGestione cap = new CapitoloEntrataGestione();
		cap.setUid(0);
		
		AttoAmministrativo aa = new AttoAmministrativo();
		aa.setUid(0);
		
		int[] uids = new int[] {326};
		List<PreDocumentoEntrata> preDocs = new ArrayList<PreDocumentoEntrata>();
		
		for (int i = 0; i < uids.length; i++) {
			PreDocumentoEntrata preDoc = create(PreDocumentoEntrata.class, uids[i]);
			preDocs.add(preDoc);
		}
		
		request.setPreDocumentiEntrata(preDocs);
		request.setBilancio(getBilancioTest(16,2015));
		
//		if(Boolean.TRUE.equals(getInviaTutti())) {
//			request.setRicercaSinteticaPreDocumentoEntrata(ricercaSinteticaPreDocumentoEntrata);
//		}
		
		request.setCapitoloEntrataGestione(cap);
		request.setAccertamento(acc);
		request.setSubAccertamento(subacc);
		request.setSoggetto(sog);
		request.setAttoAmministrativo(aa);
		request.setGestisciModificaImportoAccertamento(true);
		
		
	}

	@Test
	public void completaDefiniscePreDocumentoEntrata() {
		CompletaDefiniscePreDocumentoEntrata req = new CompletaDefiniscePreDocumentoEntrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11C000K", 52, 2));
		
		req.setBilancio(getBilancio2020Test());
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		
		RicercaSinteticaPreDocumentoEntrata reqRicerca = new RicercaSinteticaPreDocumentoEntrata();
		reqRicerca.setPreDocumentoEntrata(new PreDocumentoEntrata());
		reqRicerca.getPreDocumentoEntrata().setEnte(req.getRichiedente().getAccount().getEnte());
//		reqRicerca.getPreDocumentoEntrata().setContoCorrente(create(ContoCorrentePredocumentoEntrata.class,75644534));
		
		List<Integer> listaUid = new ArrayList<Integer>();
		listaUid.add(54491);
		reqRicerca.setUidPredocumentiDaFiltrare(listaUid);
		/*
		Calendar cal = Calendar.getInstance();
		cal.set(2020, Calendar.APRIL, 1, 0, 0, 0);
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2020, Calendar.APRIL, 2, 0, 0, 0);
		 */
		
		String sDate1 = "2020-04-01";  
		Date date1 = null;
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String sDate2 = "2020-04-02";
		Date date2 = null;
	    try {
	    	date2 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate2);
	    } catch (ParseException e) {
	    	e.printStackTrace();
	    }
		
	    req.setDataCompetenzaDa(date1);
		req.setDataCompetenzaA(date2);

		req.setRicercaSinteticaPredocumentoEntrata(reqRicerca);
		// 19827
		// 108297 -> 138171
		Soggetto soggetto = create(Soggetto.class, 138171);
		req.setSoggetto(soggetto);
		
		// 2018 / 424 -> 120914
		// 2020 / 368 -> 165397
		Accertamento accertamento = accertamentoBilDad.findAccertamentoByUid(165397);
		req.setAccertamento(accertamento);
		
//		req.setAttoAmministrativo(create(AttoAmministrativo.class, 70536));
		
		//2020/12 -> 47627
		ProvvisorioDiCassa provvisorio = provvisorioBilDad.findProvvisorioById(47627);

		req.setProvvisorioCassa(provvisorio);
		
		CompletaDefiniscePreDocumentoEntrataResponse res = completaDefiniscePreDocumentoEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaTotaliPreDocumentoEntrataPerStato() {
		RicercaTotaliPreDocumentoEntrataPerStato req = new RicercaTotaliPreDocumentoEntrataPerStato();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", "2020"));
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		
		PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
		preDoc.setEnte(req.getRichiedente().getAccount().getEnte());
		preDoc.setCausaleEntrata(create(CausaleEntrata.class, 240));
//		ContoCorrentePredocumentoEntrata ccpe = new ContoCorrentePredocumentoEntrata();
//		ccpe.setUid(455991);
//		preDoc.setContoCorrente(ccpe);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setElementiPerPagina(50);
		pp.setNumeroPagina(0);
		
		RicercaSinteticaPreDocumentoEntrata reqRicerca = new RicercaSinteticaPreDocumentoEntrata();
		reqRicerca.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRicerca.setParametriPaginazione(pp);
		reqRicerca.setPreDocumentoEntrata(preDoc);
		
		reqRicerca.setTipoCausale(create(TipoCausale.class, 240));
		
		/*Calendar cal = Calendar.getInstance();
		cal.set(2020, Calendar.JANUARY, 1, 0, 0, 0);
		reqRicerca.setDataCompetenzaDa(cal.getTime());
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2020, Calendar.FEBRUARY, 6, 0, 0, 0);
		reqRicerca.setDataCompetenzaA(cal2.getTime());*/
		
		req.setRequestRicerca(reqRicerca);
		
		
		RicercaTotaliPreDocumentoEntrataPerStatoResponse res = ricercaTotaliPreDocumentoEntrataPerStatoService.executeService(req);
		assertNotNull(res);
	}
	
	@Autowired
	AggiornaPreDocumentoEntrataCollegaDocumentoService aggiornaPreDocumentoEntrataCollegaDocumentoService;
	
	@Test
	public void testCollega() {
		AggiornaPreDocumentoEntrataCollegaDocumento req = new AggiornaPreDocumentoEntrataCollegaDocumento();
		req.setBilancio(getBilancioByProperties("consip", "regp", "2020"));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setPreDocumentoEntrata(create(PreDocumentoEntrata.class, 54299));
		req.setSubDocumentoEntrata(create(SubdocumentoEntrata.class, 109619));
		AggiornaPreDocumentoEntrataCollegaDocumentoResponse res = aggiornaPreDocumentoEntrataCollegaDocumentoService.executeService(req);
		
		// Controllo gli errori
		assertFalse(res.getErrori().size() > 0);
		assertTrue(res.getSubDocumentoEntrata() != null);
		assertTrue(res.getPreDocumentoEntrataAggiornato() != null);
		assertTrue(res.getEsito().equals(Esito.SUCCESSO));
	}
	
	@Test
	public void ricercaQuoteDaAssociare() {
		
		RicercaQuoteDaAssociarePredocumento req = new RicercaQuoteDaAssociarePredocumento();
		req.setBilancio(getBilancioByProperties("consip", "regp", "2020"));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setStatiOperativoDocumento(Arrays.asList(StatoOperativoDocumento.INCOMPLETO, StatoOperativoDocumento.ANNULLATO, StatoOperativoDocumento.EMESSO));
		req.setImportoPerRicercaQuote(new BigDecimal("10.00"));
		req.setNumeroDocumento("2020-02");
		req.setAnnoDocumento(2020);
//		req.setPreDocumentoEntrata(create(PreDocumentoEntrata.class, 54299));
//		req.setSubDocumentoEntrata(create(SubdocumentoEntrata.class, 109619));
		RicercaQuoteDaAssociarePredocumentoResponse res = ricercaQuoteDaAssociarePredocumentoService.executeService(req);
	}
}
