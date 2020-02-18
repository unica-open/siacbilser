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

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriByTipologieClassificatoriService;
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaPreDocumentoDiEntrataService;
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
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataResponse;
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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.ContoCorrente;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
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
		impegno.setNumero(new BigDecimal("1"));
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
		preDoc.getAccertamento().setNumero(new BigDecimal("61"));
		
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
		req.setAnnoBilancio(Integer.valueOf(2018));
		
		PreDocumentoEntrata preDocumentoEntrata = new PreDocumentoEntrata();
		preDocumentoEntrata.setEnte(req.getRichiedente().getAccount().getEnte());
		preDocumentoEntrata.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		req.setPreDocumentoEntrata(preDocumentoEntrata);
		
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
				Boolean.FALSE,  Boolean.FALSE, Boolean.FALSE, contoCorrenteMancante, null, null, null, pp);
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
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		
		Calendar cal = Calendar.getInstance();
		cal.set(2017, Calendar.JULY, 4, 0, 0, 0);
		req.setDataCompetenzaDa(cal.getTime());
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2017, Calendar.JULY, 7, 0, 0, 0);
		req.setDataCompetenzaA(cal2.getTime());
		
		// 2018 / 424
		req.setAccertamento(create(Accertamento.class, 64624));
		// 19827
		req.setSoggetto(create(Soggetto.class, 130002));
		
		//
		req.setAttoAmministrativo(create(AttoAmministrativo.class, 35076));
		
		CompletaDefiniscePreDocumentoEntrataResponse res = completaDefiniscePreDocumentoEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaTotaliPreDocumentoEntrataPerStato() {
		RicercaTotaliPreDocumentoEntrataPerStato req = new RicercaTotaliPreDocumentoEntrataPerStato();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(133, 2018));
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		
		Calendar cal = Calendar.getInstance();
		cal.set(2018, Calendar.JANUARY, 1, 0, 0, 0);
		req.setDataCompetenzaDa(cal.getTime());
		
		req.setCausaleEntrata(create(CausaleEntrata.class, 1));
		
		RicercaTotaliPreDocumentoEntrataPerStatoResponse res = ricercaTotaliPreDocumentoEntrataPerStatoService.executeService(req);
		assertNotNull(res);
	}
}
