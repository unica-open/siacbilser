/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cassaeconomale;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.CalcolaDisponibilitaCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaClassificatoriGenericiCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaMezziDiTrasportoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaTipoGiustificativoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaTipoOperazioneDiCassaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaTipoGiustificativoService;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.model.ImportiCassaEconomaleEnum;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasporto;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasportoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

// TODO: Auto-generated Javadoc
/**
 * The Class CassaEconomaleVTTest.
 */
public class CassaEconomaleTest extends BaseJunit4TestCase {

	/** The documento entrata service. */
	@Autowired
	private RicercaSinteticaCassaEconomaleService ricercaCassaEconomaleService;
	
	@Autowired
	private RicercaDettaglioCassaEconomaleService ricercaDettaglioCassaEconomaleService;
	
	@Autowired
	private AggiornaCassaEconomaleService aggiornaCassaEconomaleService;
	
	@Autowired
	private AnnullaCassaEconomaleService annullaCassaEconomaleService;
	
	@Autowired
	private RicercaSinteticaTipoOperazioneDiCassaService ricercaSinteticaTipoOperazioneDiCassaService;
	
	@Autowired
	private RicercaDettaglioTipoOperazioneDiCassaService ricercaDettaglioTipoOperazioneDiCassaService;
	
	@Autowired
	private AnnullaTipoOperazioneDiCassaService annullaTipoOperazioneDiCassaService;
	
	@Autowired
	private InserisceTipoOperazioneDiCassaService inserisceTipoOperazioneDiCassaService;
	
	@Autowired
	private AggiornaTipoOperazioneDiCassaService aggiornaTipoOperazioneDiCassaService;
	
	@Autowired
	private InserisceTipoGiustificativoService inserisceTipoGiustificativoService;
	
	@Autowired
	private AggiornaTipoGiustificativoService aggiornaTipoGiustificativoService;

	@Autowired
	private RicercaDettaglioTipoGiustificativoService ricercaDettaglioTipoGiustificativoService;
	
	@Autowired
	private AnnullaTipoGiustificativoService annullaTipoGiustificativoService;
	
	@Autowired
	private RicercaSinteticaTipoGiustificativoService ricercaSinteticaTipoGiustificativoService;
	
	@Autowired
	private RicercaMezziDiTrasportoService ricercaMezziDiTrasportoService;
	
	@Autowired
	private RicercaTipoGiustificativoService ricercaTipoGiustificativoService;
	
	@Autowired
	private CalcolaDisponibilitaCassaEconomaleService calcolaDisponibilitaCassaEconomaleService;
	
	
	@Test
	public void ricercaCassaEconomale() {

		RicercaSinteticaCassaEconomale req = new RicercaSinteticaCassaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancio2014Test());

		RicercaSinteticaCassaEconomaleResponse res = ricercaCassaEconomaleService.executeService(req);

		assertNotNull(res);
	}
	
	
	@Test
	public void ricercaDettaglioCassaEconomale() {

		RicercaDettaglioCassaEconomale req = new RicercaDettaglioCassaEconomale();
		
		CassaEconomale cassaEconomale = new CassaEconomale(); 
		cassaEconomale.setUid(4);		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setCassaEconomale(cassaEconomale);

		RicercaDettaglioCassaEconomaleResponse res = ricercaDettaglioCassaEconomaleService.executeService(req);

		assertNotNull(res);
	}
	
	
	@Test
	public void aggiornaCassaEconomale() {

		AggiornaCassaEconomale req = new AggiornaCassaEconomale();
		
		CassaEconomale cassaEconomale = new CassaEconomale(); 
		cassaEconomale.setUid(1);	
		cassaEconomale.setCodice("001-AGG");
		cassaEconomale.setDescrizione("prima cassa - aggiorntaBIS");
		cassaEconomale.setEnte(getEnteTest());
		
		
		
		cassaEconomale.setLimiteImporto(new BigDecimal("200000"));
		cassaEconomale.setNumeroContoCorrente("123456789");
		cassaEconomale.setResponsabile("sono un tipo responsabile! :) ");
		cassaEconomale.setTipoDiCassa(TipoDiCassa.CONTO_CORRENTE_BANCARIO);
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setCassaEconomale(cassaEconomale);

		AggiornaCassaEconomaleResponse res = aggiornaCassaEconomaleService.executeService(req);

		assertNotNull(res);
	}
	
	
	@Test
	public void annullaCassaEconomale() {

		AnnullaCassaEconomale req = new AnnullaCassaEconomale();
		
		CassaEconomale cassaEconomale = new CassaEconomale(); 
		cassaEconomale.setUid(1);	
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setCassaEconomale(cassaEconomale);

		AnnullaCassaEconomaleResponse res = annullaCassaEconomaleService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaTipoOperazioneCassa() {

		RicercaSinteticaTipoOperazioneDiCassa req = new RicercaSinteticaTipoOperazioneDiCassa();
		
		TipoOperazioneCassa tipoOperazioneCassa = new TipoOperazioneCassa();
		tipoOperazioneCassa.setCodice("");
		tipoOperazioneCassa.setDescrizione("prel");
				
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setTipoOperazioneCassa(tipoOperazioneCassa);

		RicercaSinteticaTipoOperazioneDiCassaResponse res = ricercaSinteticaTipoOperazioneDiCassaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioTipoOperazioneCassa() {

		RicercaDettaglioTipoOperazioneDiCassa req = new RicercaDettaglioTipoOperazioneDiCassa();
		
		TipoOperazioneCassa tipoOperazioneCassa = new TipoOperazioneCassa();
		tipoOperazioneCassa.setUid(1);
				
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoOperazioneCassa(tipoOperazioneCassa);

		RicercaDettaglioTipoOperazioneDiCassaResponse res = ricercaDettaglioTipoOperazioneDiCassaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void annullaTipoOperazioneCassa() {

		AnnullaTipoOperazioneDiCassa req = new AnnullaTipoOperazioneDiCassa();
		
		TipoOperazioneCassa tipoOperazioneCassa = new TipoOperazioneCassa();
		tipoOperazioneCassa.setUid(1);
				
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoOperazioneCassa(tipoOperazioneCassa);

		AnnullaTipoOperazioneDiCassaResponse res = annullaTipoOperazioneDiCassaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void inserisciTipoOperazioneCassa() {

		InserisceTipoOperazioneDiCassa req = new InserisceTipoOperazioneDiCassa();
		
		TipoOperazioneCassa tipoOperazioneCassa = new TipoOperazioneCassa();
		tipoOperazioneCassa.setCodice("COD-MIO-TEST-ANNULLA");
		tipoOperazioneCassa.setDescrizione("tipo operazione per test inserimento ");
		tipoOperazioneCassa.setInclusoInGiornale(Boolean.TRUE);
		tipoOperazioneCassa.setInclusoInRendiconto(Boolean.FALSE);
				
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoOperazioneCassa(tipoOperazioneCassa);
		req.setBilancio(getBilancio2015Test());
		InserisceTipoOperazioneDiCassaResponse res =inserisceTipoOperazioneDiCassaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void aggiornaDettaglioTipoOperazioneCassa() {

		AggiornaTipoOperazioneDiCassa req = new AggiornaTipoOperazioneDiCassa();
		
		TipoOperazioneCassa tipoOperazioneCassa = new TipoOperazioneCassa();
		tipoOperazioneCassa.setUid(14);
		tipoOperazioneCassa.setCodice("COD-01");
		tipoOperazioneCassa.setDescrizione("tipo operazione per test aggiornamento");
		tipoOperazioneCassa.setInclusoInGiornale(Boolean.FALSE);
		tipoOperazioneCassa.setInclusoInRendiconto(Boolean.FALSE);
				
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoOperazioneCassa(tipoOperazioneCassa);

		AggiornaTipoOperazioneDiCassaResponse res =aggiornaTipoOperazioneDiCassaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void inserisciTipoGiustificativo() {

		InserisceTipoGiustificativo req = new InserisceTipoGiustificativo();
		
		TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
		tipoGiustificativo.setCodice("SUPER-CODE2");
		tipoGiustificativo.setDescrizione("tipogiustificativo per test inserimento ");
		tipoGiustificativo.setTipologiaGiustificativo(TipologiaGiustificativo.ANTICIPO);
		tipoGiustificativo.setEnte(getEnteTest());
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoGiustificativo(tipoGiustificativo);
		InserisceTipoGiustificativoResponse res =inserisceTipoGiustificativoService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void aggiornaTipoGiustificativo() {

		AggiornaTipoGiustificativo req = new AggiornaTipoGiustificativo();
		
		TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
		tipoGiustificativo.setUid(1);
		tipoGiustificativo.setCodice("SUPER-CODE2");
		tipoGiustificativo.setDescrizione("tipogiustificativo per test inserimento ");
		tipoGiustificativo.setTipologiaGiustificativo(TipologiaGiustificativo.RIMBORSO);
		tipoGiustificativo.setEnte(getEnteTest());
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoGiustificativo(tipoGiustificativo);
		AggiornaTipoGiustificativoResponse res =aggiornaTipoGiustificativoService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioTipoGiustificativo() {

		RicercaDettaglioTipoGiustificativo req = new RicercaDettaglioTipoGiustificativo();
		
		TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
		tipoGiustificativo.setUid(8);
				
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoGiustificativo(tipoGiustificativo);

		RicercaDettaglioTipoGiustificativoResponse res = ricercaDettaglioTipoGiustificativoService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void annullaTipoGiustificativo() {

		AnnullaTipoGiustificativo req = new AnnullaTipoGiustificativo();
		
		TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
		tipoGiustificativo.setUid(1);
				
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoGiustificativo(tipoGiustificativo);

		AnnullaTipoGiustificativoResponse res = annullaTipoGiustificativoService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaTipoGiustificativo() {

		RicercaSinteticaTipoGiustificativo req = new RicercaSinteticaTipoGiustificativo();
		
		TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
//		tipoGiustificativo.setCodice("super");
//		tipoGiustificativo.setDescrizione("tipo");
//		tipoGiustificativo.setTipologiaGiustificativo(TipologiaGiustificativo.ANTICIPO);
				
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoGiustificativo(tipoGiustificativo);
		req.setParametriPaginazione(getParametriPaginazioneTest());

		RicercaSinteticaTipoGiustificativoResponse res = ricercaSinteticaTipoGiustificativoService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void ricercaMezziDiTrasporto() {
		RicercaMezziDiTrasporto req = new RicercaMezziDiTrasporto();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		
		RicercaMezziDiTrasportoResponse res = ricercaMezziDiTrasportoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaTipoGiustificativo() {
		RicercaTipoGiustificativo req = new RicercaTipoGiustificativo();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipologiaGiustificativo(TipologiaGiustificativo.ANTICIPO);
		req.setDataOra(new Date());
		
		RicercaTipoGiustificativoResponse res = ricercaTipoGiustificativoService.executeService(req);
		assertNotNull(res);
	}
	
	@Autowired
	private RicercaClassificatoriGenericiCassaEconomaleService ricercaClassificatoriGenericiCassaEconomaleService;
	
	@Test
	public void ricercaClassificatoriGenericiCassaEconomale() {
		final String methodName = "ricercaClassificatoriGenericiCassaEconomale";
		RicercaClassificatoriGenericiCassaEconomale req = new RicercaClassificatoriGenericiCassaEconomale();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RicercaClassificatoriGenericiCassaEconomaleResponse res = ricercaClassificatoriGenericiCassaEconomaleService.executeService(req);
		assertNotNull(res);
		
		log.debug(methodName, "51: #" + res.getListaClassificatori(TipologiaClassificatore.CLASSIFICATORE_51));
		log.debug(methodName, "52: #" + res.getListaClassificatori(TipologiaClassificatore.CLASSIFICATORE_52));
		log.debug(methodName, "53: #" + res.getListaClassificatori(TipologiaClassificatore.CLASSIFICATORE_53));
	}
	
	
	@Autowired
	private CassaEconomaleDad cassaEconomaleDad;
	
	@Test
	public void calcolaDisponibilitaCassaEconomaleService(){
		CalcolaDisponibilitaCassaEconomale req = new CalcolaDisponibilitaCassaEconomale();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancio2015Test());
		CassaEconomale cassaEconomale = new CassaEconomale();
		cassaEconomale.setUid(2);
		req.setCassaEconomale(cassaEconomale);
		req.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCassaEconomaleEnum.class));
		CalcolaDisponibilitaCassaEconomaleResponse res = calcolaDisponibilitaCassaEconomaleService.executeService(req);
		if(!res.isFallimento()){
			System.out.println(">>>>>>>>>> " + res.getCassaEconomale().getDisponibilitaCassaContanti());
			System.out.println(">>>>>>>>>> " + res.getCassaEconomale().getDisponibilitaCassaContoCorrente());
		}
		
		testBilanciAssociati(cassaEconomale);
	}


	private void testBilanciAssociati(CassaEconomale cassaEconomale) {
		List<Bilancio> bilanciAssociati = cassaEconomaleDad.findBilanciAssociati(cassaEconomale);
		int i = 1;
		for(Bilancio bilancio : bilanciAssociati){
			System.out.println(i +". "+bilancio.getUid());
		}
		System.out.println("numero bilanci associati alla cassa economale: "+bilanciAssociati.size());
	}
	
}
