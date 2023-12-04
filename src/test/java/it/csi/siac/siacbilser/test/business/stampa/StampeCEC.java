/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.stampa;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaUltimaStampaDefinitivaGiornaleCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaUltimoRendicontoCassaStampatoService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.StampaGiornaleCassaService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.RicercaSinteticaRendicontoCassaDaStampareService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.StampaRendicontoCassaService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.VerificaStampaRendicontoCassaService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.ricevuta.StampaRicevutaRendicontoRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.ricevuta.StampaRicevutaRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.MovimentoDad;
import it.csi.siac.siacbilser.integration.dad.StampeCassaFileDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampare;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampareResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampato;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampatoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRendicontoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRendicontoCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

public class StampeCEC extends BaseJunit4TestCase {
	@Autowired
	private StampaRicevutaRichiestaEconomaleService stampaRicevutaRichiestaEconomaleService;
	@Autowired
	private StampaRicevutaRendicontoRichiestaEconomaleService stampaRicevutaRendicontoRichiestaEconomale;
	@Autowired
    private StampaGiornaleCassaService stampaGiornaleCassaService;
	@Autowired
	private StampaRendicontoCassaService stampaRendicontoCassaService;
	@Autowired
	private RicercaUltimoRendicontoCassaStampatoService ricercaUltimoRendicontoCassaStampatoService;
	@Autowired
	private RicercaUltimaStampaDefinitivaGiornaleCassaService ricercaUltimaStampaDefinitivaGiornaleCassaService;
	@Autowired
	private StampeCassaFileDad stampeCassaFileDad;
	@Autowired
	private MovimentoDad movimentoDad;
	
	@Autowired
	private VerificaStampaRendicontoCassaService verificaStampaRendicontoCassaService;
	@Autowired
	private RicercaSinteticaRendicontoCassaDaStampareService ricercaSinteticaRendicontoCassaDaStampareService;
	
	/**
	 * Ottiene il bilancio 2014 con id 6.
	 *
	 * @return the bilancio test
	 */
	protected Bilancio getBilancio2014Test() {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(6);
		bilancio.setAnno(2014);
		return bilancio;
	}
	
	/**
	 * Ottiene il bilancio 2015 con id 16.
	 *
	 * @return the bilancio test
	 */
	protected Bilancio getBilancio2015Test() {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(16);
		bilancio.setAnno(2015);
		return bilancio;
	}
	
	private Richiedente getRichiedenteByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getRichiedenteByProperties("consip","regp") : getRichiedenteTest("AAAAAA00A11E000M",71,15);
	}
	
	private Bilancio getBilancioByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getBilancio2015Test() : getBilancioTest(46, 2015);
	}
	
	private Ente getEnteByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getEnteTest() : getEnteTest(15);
	}
	
	protected Ente getEnteForn2() {
		return   getEnteTest(5);
	}
	
	protected Bilancio getBilancioForn2(){
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(51);
		bilancio.setAnno(2016);
		return bilancio;
	}
	
	@Test
	public void stampaRicevutaRichiestaEconomale() {
		
		StampaRicevutaRichiestaEconomale req = new StampaRicevutaRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByEnvironment(true));
		
		req.setBilancio(getBilancioByEnvironment(true));
		
		req.setEnte(getEnteByEnvironment(true));
		RichiestaEconomale re = new RichiestaEconomale();
		/* dev
		 * ANTICIPO SPESE UID: 20,21
		 * RIMBORSO SPESE UID: 19, 22, 23
		 * PAGAMENTO FATTURE UID: 24
		 * ANTICIPO SPESE TRASF DIP UID: 25
		 * ANTICIPO SPESE MISSIONE UID: 26
		 * PAGAMENTO: 228,220(auto)
		 * */
		re.setUid(228);
		
		req.setRichiestaEconomale(re);
		StampaRicevutaRichiestaEconomaleResponse res = stampaRicevutaRichiestaEconomaleService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void stampaRicevutaRendicontoRichiestaEconomale() {
		
		StampaRicevutaRendicontoRichiestaEconomale req = new StampaRicevutaRendicontoRichiestaEconomale();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioTest(133, 2018));
		
		req.setRendicontoRichiesta(create(RendicontoRichiesta.class, 32));
		StampaRicevutaRendicontoRichiestaEconomaleResponse res = stampaRicevutaRendicontoRichiestaEconomale.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void stampaGiornaleCassa() {
		
		StampaGiornaleCassa req = new StampaGiornaleCassa();
		req.setBilancio(getBilancio2015Test());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setEnte(getEnteTest());
//		req.setBilancio(getBilancioByEnvironment(true));
//		req.setEnte(getEnteByEnvironment(true));
//		req.setRichiedente(getRichiedenteForn2());
//		req.setBilancio(getBilancioForn2());
//		req.setEnte(getEnteForn2());

		CassaEconomale ce = new CassaEconomale();
		ce.setUid(2);
		
		req.setCassaEconomale(ce);
//		req.setCassaEconomale(getCassaEconomaleFORN2());
		req.setProseguiCEC_INF_0017(true);
		
		String string = "15/02/2017";
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
		req.setDataStampaGiornale(format.parse(string));
		
		req.setTipoStampa(TipoStampa.BOZZA);

		StampaGiornaleCassaResponse res = stampaGiornaleCassaService.executeService(req);
		assertNotNull(res);
		} catch (ParseException e1) {

			e1.printStackTrace();
		}
		try {
			Thread.sleep(20* 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void stampaGiornaleCassaForn2() {
		final String methodName = "stampaGiornaleCassaForn2";
		
		StampaGiornaleCassa req = new StampaGiornaleCassa();
		//demo24
		req.setProseguiCEC_INF_0017(true);
		req.setRichiedente(getRichiedenteByProperties("forn2", "crp"));
		req.setBilancio(getBilancioTest(51, 2016));
		req.setEnte(req.getRichiedente().getAccount().getEnte());

		CassaEconomale ce = new CassaEconomale();
		ce.setUid(36);
		req.setCassaEconomale(ce);


		String string = "15/12/2016";
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			req.setDataStampaGiornale(format.parse(string));
		
		} catch (ParseException e1) {
			log.error(methodName, e1.getMessage(), e1);
		}
		
		//req.setTipoStampa(TipoStampa.BOZZA);
		req.setTipoStampa(TipoStampa.DEFINITIVA);
		StampaGiornaleCassaResponse res = stampaGiornaleCassaService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * Ottiene la lista delle stampe iva per il registro relative ad un dato periodo.
//	 */
//	@Test
//	public void caricaListaStampeGiornaleDefinitiveFORN2() {
//		final String methodName = "caricaListaStampe";
//		CassaEconomale ce = new CassaEconomale();
//		ce.setUid(36);
//		StampeCassaFile scf1 = new StampeCassaFile();
//		Richiedente richiedente = getRichiedenteByProperties("forn2", "crp");
//		//si.setListaRegistroIva(Arrays.asList(registroIva));
//		scf1.setCassaEconomale(ce);
//		scf1.setEnte(richiedente.getAccount().getEnte());
//		scf1.setTipoDocumento(TipoDocumento.GIORNALE_CASSA);
//		scf1.setTipoStampa(TipoStampa.DEFINITIVA);
//		scf1.setBilancio(getBilancioTest(51, 2016));
//		List<StampeCassaFile> listaStampeDef = stampeCassaFileDad.findAllStampeByTipoDocumentoAndStatoOrderByDataModificaModelDetail(scf1, StampeCassaFileModelDetail.TipoDocumento, StampeCassaFileModelDetail.StatoStampa, StampeCassaFileModelDetail.Valore);
//
//		log.debug(methodName, "Numero stampe trovate per cassa economale " + ce.getUid() + " : " + listaStampeDef.size());
//		
//		for(StampeCassaFile scf : listaStampeDef) {
//			//controllo solo le definitive
//			if(TipoStampa.DEFINITIVA.equals(scf.getTipoStampa()) && scf.getStampaGiornale() != null) {
//				if (ultimaStampaGiornaleDefinitiva != null 
//						&& ultimaStampaGiornaleDefinitiva.getStampaGiornale() != null
//						&& ultimaStampaGiornaleDefinitiva.getStampaGiornale().getDataUltimaStampa()  != null) {
//					
//					Date dataUltimaStampaLista = scf.getStampaGiornale().getDataUltimaStampa();
//					
//					long resultcompare = DateUtils.truncate(ultimaStampaGiornaleDefinitiva.getStampaGiornale().getDataUltimaStampa(),Calendar.DAY_OF_MONTH).compareTo(
//							DateUtils.truncate(dataUltimaStampaLista,Calendar.DAY_OF_MONTH));
//					log.debug(methodName, "resultcompare : " +resultcompare);
//					if (resultcompare<0) {
//						uidUltimaStampa = Integer.valueOf(scf.getUid());
//					}
//				} else {
//					uidUltimaStampa = Integer.valueOf(scf.getUid());
//				}
//			}
//		}
//		for (StampeCassaFile stampeCassaFile : listaStampeDef) {
//			log.debug(methodName, "Trovata stampa con uid  in data: " + ((stampeCassaFile.getStampaGiornale() != null && stampeCassaFile.getStampaGiornale().getDataUltimaStampa() != null) ?
//					stampeCassaFile.getStampaGiornale().getDataUltimaStampa() : ""));
//			log.debug(methodName, "Trovata stampa in data: " + ((stampeCassaFile.getStampaGiornale() != null && stampeCassaFile.getStampaGiornale().getDataUltimaStampa() != null) ?
//					stampeCassaFile.getStampaGiornale().getDataUltimaStampa() : ""));
//			
//		}
//		
//	}
	
	
	@Test
	public void stampaRendicontoCassa() {
		StampaRendicontoCassa req = new StampaRendicontoCassa();
		
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setDataOra(new Date());
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancio2014Test());
		
		CassaEconomale cassaEconomale = new CassaEconomale();
		cassaEconomale.setUid(2);
		req.setCassaEconomale(cassaEconomale);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DAY_OF_MONTH, 17);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		req.setDataRendiconto(cal.getTime());
		
		cal.set(Calendar.YEAR, 2016);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.DAY_OF_MONTH, 21);
		req.setPeriodoDaRendicontareDataInizio(cal.getTime());
		
		cal.set(Calendar.DAY_OF_MONTH, 23);
		req.setPeriodoDaRendicontareDataFine(cal.getTime());
		
		req.setTipoStampa(TipoStampa.BOZZA);
		
		StampaRendicontoCassaResponse res = stampaRendicontoCassaService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getUltimoRendicontoCassa() {
		final String methodName = "getUltimoRendicontoCassa";
		CassaEconomale ce = new CassaEconomale();
		ce.setUid(1);
		StampeCassaFile scfForReq = new StampeCassaFile();
		scfForReq.setBilancio(getBilancio2015Test());
		scfForReq.setTipoDocumento(TipoDocumento.RENDICONTO);
		scfForReq.setCassaEconomale(ce);
		scfForReq.setTipoStampa(TipoStampa.DEFINITIVA);
		
		RicercaUltimoRendicontoCassaStampato reqRicercaUltimo = new RicercaUltimoRendicontoCassaStampato();
		reqRicercaUltimo.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRicercaUltimo.setStampeCassaFile(scfForReq);
		
		RicercaUltimoRendicontoCassaStampatoResponse responseUltimo = ricercaUltimoRendicontoCassaStampatoService.executeService(reqRicercaUltimo);
		log.debug(methodName, "responseUltimo: " + responseUltimo);
		
		assertNotNull(responseUltimo);
			
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getUltimaStampaDEfGiornaleCassa() {
		final String methodName = "getUltimaStampaDEfGiornaleCassa";
		CassaEconomale ce = new CassaEconomale();
		ce.setUid(1);
		StampeCassaFile scfForReq = new StampeCassaFile();
		scfForReq.setBilancio(getBilancio2015Test());
		scfForReq.setTipoDocumento(TipoDocumento.GIORNALE_CASSA);
		scfForReq.setCassaEconomale(ce);
		scfForReq.setTipoStampa(TipoStampa.DEFINITIVA);
		
		RicercaUltimaStampaDefinitivaGiornaleCassa reqRicercaUltima = new RicercaUltimaStampaDefinitivaGiornaleCassa();
		reqRicercaUltima.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRicercaUltima.setStampeCassaFile(scfForReq);
		
		RicercaUltimaStampaDefinitivaGiornaleCassaResponse resRicercaUltima = ricercaUltimaStampaDefinitivaGiornaleCassaService.executeService(reqRicercaUltima);
		log.debug(methodName, "responseUltimo: " + resRicercaUltima);
		
		
		assertNotNull(resRicercaUltima);
			
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConto() {
		BigDecimal importoQuota= new BigDecimal(30);
		 BigDecimal aliguota= new BigDecimal(22);
		BigDecimal CENTO = BilUtilities.BIG_DECIMAL_ONE_HUNDRED;
		BigDecimal multiplicand = CENTO.divide(CENTO.add(aliguota), MathContext.DECIMAL128);
		BigDecimal subtrahend = importoQuota.multiply(multiplicand).setScale(2, RoundingMode.HALF_DOWN);
		BigDecimal result = importoQuota.subtract(subtrahend);
		
		assertNotNull(result);
		
	}
	
	@Test
	public void testFindMovimenti(){
		final String methodName = "testFindMovimenti";
		GregorianCalendar cg = new GregorianCalendar(2017,Calendar.FEBRUARY,15);
		List<Movimento> movs = movimentoDad.findByDataMovimentoCassaEconId(cg.getTime(), 2, 1, 16);
		for (Movimento m : movs) {
			log.debug(methodName, "<<<<<<XXXX movimento con uid: " +  m.getUid() );
		}
		
	}
	
	@Test
	public void verificaStampaRendicontoCassa() {
		VerificaStampaRendicontoCassa req = new VerificaStampaRendicontoCassa();
		
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setDataOra(new Date());
		req.setBilancio(getBilancioTest(16, 2015));
		req.setCassaEconomale(create(CassaEconomale.class, 2));
		req.setPeriodoDaRendicontareDataInizio(parseDate("01/01/2016"));
		req.setPeriodoDaRendicontareDataFine(parseDate("31/12/2016"));
		//req.setTipoStampa(TipoStampa.BOZZA);
		req.setTipoStampa(TipoStampa.DEFINITIVA);
		
		VerificaStampaRendicontoCassaResponse res = verificaStampaRendicontoCassaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaRendicontoCassaDaStampare() {
		RicercaSinteticaRendicontoCassaDaStampare req = new RicercaSinteticaRendicontoCassaDaStampare();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setBilancio(getBilancioTest(133, 2018));
		req.setCassaEconomale(create(CassaEconomale.class, 101));
		req.setPeriodoDaRendicontareDataInizio(parseDate("26/07/2018"));
		req.setPeriodoDaRendicontareDataFine(parseDate("26/07/2018"));
		
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaRendicontoCassaDaStampareResponse res = ricercaSinteticaRendicontoCassaDaStampareService.executeService(req);
		assertNotNull(res);
	}
	
}
