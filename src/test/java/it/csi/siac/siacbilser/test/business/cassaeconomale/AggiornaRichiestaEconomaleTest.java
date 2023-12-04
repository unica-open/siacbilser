/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cassaeconomale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleAnticipoPerTrasfertaDipendentiService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleAnticipoSpesePerMissioneService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleAnticipoSpeseService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomalePagamentoFattureService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleRimborsoSpeseService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.DatiTrasfertaMissione;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.Sospeso;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class CassaEconomaleVTTest.
 */
public class AggiornaRichiestaEconomaleTest extends BaseJunit4TestCase {

	/** The documento entrata service. */
	@Autowired
	private AggiornaRichiestaEconomaleAnticipoPerTrasfertaDipendentiService aggiornaRichiestaEconomaleAnticipoPerTrasfertaDipendentiService;
	@Autowired
	private AggiornaRichiestaEconomaleAnticipoSpesePerMissioneService aggiornaRichiestaEconomaleAnticipoSpesePerMissioneService;
	@Autowired
	private AggiornaRichiestaEconomaleAnticipoSpeseService aggiornaRichiestaEconomaleAnticipoSpeseService;
	@Autowired
	private AggiornaRichiestaEconomaleRimborsoSpeseService aggiornaRichiestaEconomaleRimborsoSpeseService;
	@Autowired
	private AggiornaRichiestaEconomalePagamentoFattureService aggiornaRichiestaEconomalePagamentoFattureService;
	
	
	@Test
	public void aggiornaRichiestaEconomaleTrasferta(){
		AggiornaRichiestaEconomale req = new AggiornaRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RichiestaEconomale richiesta = new RichiestaEconomale();
		
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		richiesta.setStrutturaDiAppartenenza("S");
		
		richiesta.setUid(23);
		richiesta.setNumeroRichiesta(7);
		Sospeso sospeso = new Sospeso();
		sospeso.setNumeroSospeso(7);
		richiesta.setSospeso(sospeso);
		
		DatiTrasfertaMissione datiTrasfertaMissione = new DatiTrasfertaMissione();
		datiTrasfertaMissione.setMotivo("voglio andare in trasferta");
		datiTrasfertaMissione.setFlagEstero(Boolean.TRUE);
		datiTrasfertaMissione.setLuogo("Svezia");
		datiTrasfertaMissione.setCodice("");
		datiTrasfertaMissione.setDataInizio(new Date());
		datiTrasfertaMissione.setDataFine(new Date());
		richiesta.setDatiTrasfertaMissione(datiTrasfertaMissione);
		
		req.setRichiestaEconomale(richiesta);
		AggiornaRichiestaEconomaleResponse res = aggiornaRichiestaEconomaleAnticipoPerTrasfertaDipendentiService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void aggiornaRichiestaEconomaleAnticipoSpese(){
		AggiornaRichiestaEconomale req = new AggiornaRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiesta = new RichiestaEconomale();
		
		richiesta.setUid(29);
		richiesta.setNumeroRichiesta(11);
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		
		richiesta.setImporto(new BigDecimal(1750));
		richiesta.setDescrizioneDellaRichiesta("anticipo spese per andare in VACANZA");
		
		Sospeso sospeso = new Sospeso();
		sospeso.setNumeroSospeso(11);
		richiesta.setSospeso(sospeso);
		
		Movimento movimento = new Movimento();
		movimento.setNumeroMovimento(2);
		movimento.setDataMovimento(new Date());
		ModalitaPagamentoCassa modalitaPagamentoCassa = new ModalitaPagamentoCassa();
		modalitaPagamentoCassa.setUid(1);
		movimento.setModalitaPagamentoCassa(modalitaPagamentoCassa);
		richiesta.setMovimento(movimento);
		
		req.setRichiestaEconomale(richiesta);
		AggiornaRichiestaEconomaleResponse res = aggiornaRichiestaEconomaleAnticipoSpeseService.executeService(req);
		assertNotNull(res);
	}
	

	@Test
	public void aggiornaRichiestaEconomaleMissione(){
		AggiornaRichiestaEconomale req = new AggiornaRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiesta = new RichiestaEconomale();
		
		
		richiesta.setNumeroRichiesta(12);
		richiesta.setUid(35);
		
		Sospeso sospeso = new Sospeso();
		sospeso.setNumeroSospeso(12);
		richiesta.setSospeso(sospeso);
		
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		
		Movimento movimento = new Movimento();
		movimento.setDataMovimento(new Date());
		ModalitaPagamentoCassa modalitaPagamentoCassa = new ModalitaPagamentoCassa();
		modalitaPagamentoCassa.setUid(1);
		movimento.setModalitaPagamentoCassa(modalitaPagamentoCassa);
		richiesta.setMovimento(movimento);
		
		DatiTrasfertaMissione datiTrasfertaMissione = new DatiTrasfertaMissione();
		datiTrasfertaMissione.setMotivo("voglio andare in trasferta");
		datiTrasfertaMissione.setFlagEstero(Boolean.TRUE);
		datiTrasfertaMissione.setLuogo("Svezia");
		datiTrasfertaMissione.setDataInizio(new Date());
		datiTrasfertaMissione.setDataFine(new Date());
		datiTrasfertaMissione.setCodice("");
		richiesta.setDatiTrasfertaMissione(datiTrasfertaMissione );
		
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		Giustificativo giustificativo1 = new Giustificativo();
		Giustificativo giustificativo2 = new Giustificativo();
		TipoGiustificativo tipoGiustificativo1 = new TipoGiustificativo();
		tipoGiustificativo1.setUid(1);
		
		TipoGiustificativo tipoGiustificativo2 = new TipoGiustificativo();
		tipoGiustificativo2.setUid(6);
		
		giustificativo1.setDataEmissione(new Date());
		giustificativo1.setImportoGiustificativo(new BigDecimal(300));
		giustificativo1.setFlagInclusoNelPagamento(Boolean.TRUE);
		giustificativo1.setTipoGiustificativo(tipoGiustificativo1);
		
		giustificativo2.setDataEmissione(new Date());
		giustificativo2.setImportoGiustificativo(new BigDecimal(950));
		giustificativo2.setFlagInclusoNelPagamento(Boolean.TRUE);
		giustificativo2.setTipoGiustificativo(tipoGiustificativo2);
		
		giustificativi.add(giustificativo1);
		giustificativi.add(giustificativo2);
		richiesta.setGiustificativi(giustificativi);
		
		
		req.setRichiestaEconomale(richiesta);
		AggiornaRichiestaEconomaleResponse res = aggiornaRichiestaEconomaleAnticipoSpesePerMissioneService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void aggiornaRichiestaEconomaleFattura(){
		AggiornaRichiestaEconomale req = new AggiornaRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiesta = new RichiestaEconomale();
		
		
		richiesta.setUid(38);
		richiesta.setNumeroRichiesta(14);
		
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		
		richiesta.setDescrizioneDellaRichiesta("richiesta di pagamento fatture");
		
//		DocumentoSpesa documento = new DocumentoSpesa();
//		documento.setUid(46);
//		documento.setImporto(new BigDecimal(10));
//		richiesta.setDocumentoSpesa(documento);
		
		req.setRichiestaEconomale(richiesta);
		AggiornaRichiestaEconomaleResponse res = aggiornaRichiestaEconomalePagamentoFattureService.executeService(req);
		assertNotNull(res);
	}

	
	@Test
	public void aggiornaRichiestaEconomaleRimborso(){
		AggiornaRichiestaEconomale req = new AggiornaRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiesta = new RichiestaEconomale();
		
		richiesta.setNumeroRichiesta(13);
		richiesta.setUid(36);
		
		Movimento movimento = new Movimento();
		movimento.setDataMovimento(new Date());
		ModalitaPagamentoCassa modalitaPagamentoCassa = new ModalitaPagamentoCassa();
		modalitaPagamentoCassa.setUid(1);
		movimento.setModalitaPagamentoCassa(modalitaPagamentoCassa);
		richiesta.setMovimento(movimento);
		
		richiesta.setDescrizioneDellaRichiesta("mi dovete rimborsare un sacco di spese!!!");
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		Giustificativo giustificativo1 = new Giustificativo();
		Giustificativo giustificativo2 = new Giustificativo();
		TipoGiustificativo tipoGiustificativo1 = new TipoGiustificativo();
		tipoGiustificativo1.setUid(1);
		
		TipoGiustificativo tipoGiustificativo2 = new TipoGiustificativo();
		tipoGiustificativo2.setUid(6);
		
		giustificativo1.setDataEmissione(new Date());
		giustificativo1.setImportoGiustificativo(new BigDecimal(300));
		giustificativo1.setFlagInclusoNelPagamento(Boolean.TRUE);
		giustificativo1.setTipoGiustificativo(tipoGiustificativo1);
		
		giustificativo2.setDataEmissione(new Date());
		giustificativo2.setImportoGiustificativo(new BigDecimal(950));
		giustificativo2.setFlagInclusoNelPagamento(Boolean.TRUE);
		giustificativo2.setTipoGiustificativo(tipoGiustificativo2);
		
		giustificativi.add(giustificativo1);
		giustificativi.add(giustificativo2);
		richiesta.setGiustificativi(giustificativi);
		
		req.setRichiestaEconomale(richiesta);
		AggiornaRichiestaEconomaleResponse res = aggiornaRichiestaEconomaleRimborsoSpeseService.executeService(req);
		assertNotNull(res);
	}
	

	private CassaEconomale getCassa() {
		CassaEconomale cassa = new CassaEconomale();
		cassa.setUid(1);
		return cassa;
	}

	private Impegno getImpegno() {
		Impegno impegno = new Impegno();
		impegno.setUid(1);
		return impegno;
	}

	private Soggetto getSoggetto() {
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		return soggetto;
	}
	
	
}
