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

import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRendicontoRichiestaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioRendicontoRichiestaService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class InserisceRendicontoRichiestaEconomaleDLTest.
 */
public class InserisceRendicontoRichiestaEconomaleTest extends BaseJunit4TestCase {

	
	@Autowired
	private InserisceRendicontoRichiestaService inserisceRendicontoRichiestaService;
	
	@Autowired
	private RicercaDettaglioRendicontoRichiestaService ricercaDettaglioRendicontoRichiestaService;
	
	@Test
	public void inserisceRendicontoRichiesta(){
		InserisceRendicontoRichiesta req = new InserisceRendicontoRichiesta();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		
		RendicontoRichiesta rendicontoRichiesta = new RendicontoRichiesta();
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		
		Giustificativo g1 = new Giustificativo();
		g1.setCambio(new BigDecimal(1));
		g1.setDataEmissione(new Date());
		g1.setImportoGiustificativo(new BigDecimal(10));
		g1.setNumeroGiustificativo("G1");
		g1.setNumeroProtocollo("P1");
		g1.setStatoOperativoGiustificativi(StatoOperativoGiustificativi.VALIDO);
		TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
		tipoGiustificativo.setUid(1);
		g1.setTipoGiustificativo(tipoGiustificativo);
		g1.setFlagInclusoNelPagamento(Boolean.TRUE);
		giustificativi.add(g1);
		
		rendicontoRichiesta.setGiustificativi(giustificativi);
		
		rendicontoRichiesta.setImportoIntegrato(BigDecimal.ZERO);
		rendicontoRichiesta.setImportoRestituito(new BigDecimal(100));
		
		Movimento movimento = new Movimento();
		ModalitaPagamentoCassa modalitaPagamentoCassa = new ModalitaPagamentoCassa();
		modalitaPagamentoCassa.setUid(1);
		movimento.setModalitaPagamentoCassa(modalitaPagamentoCassa);
		rendicontoRichiesta.setMovimento(movimento);
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setUid(29);
		rendicontoRichiesta.setRichiestaEconomale(richiestaEconomale);
		req.setRendicontoRichiesta(rendicontoRichiesta);
		
		rendicontoRichiesta.setDataRendiconto(new Date());
		
		
		InserisceRendicontoRichiestaResponse res = inserisceRendicontoRichiestaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioRendicontoRichiesta(){
		RicercaDettaglioRendicontoRichiesta req = new RicercaDettaglioRendicontoRichiesta();
		RendicontoRichiesta rendicontoRichiesta = new RendicontoRichiesta();
		rendicontoRichiesta.setUid(8);
		req.setRendicontoRichiesta(rendicontoRichiesta);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RicercaDettaglioRendicontoRichiestaResponse res = ricercaDettaglioRendicontoRichiestaService.executeService(req);
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
