/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.test.business.ordinativo;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfinser.business.service.ordinativo.ReintroitoOrdinativoPagamentoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.ric.OrdinativoKey;

public class ReintroitoOrdinativoPagamentoTest extends BaseJunit4TestCase {
	
	@Autowired
	private ReintroitoOrdinativoPagamentoService reintroitoOrdinativoPagamentoService;
	
	
	
	@Test
	public void reintroitoOrdinativoPagamento() {
		ReintroitoOrdinativoPagamento req = new ReintroitoOrdinativoPagamento();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		//ordinativo da reintroitare
		req.setOrdinativoPagamento(new OrdinativoKey());
		req.getOrdinativoPagamento().setAnno(Integer.valueOf(2017));
		req.getOrdinativoPagamento().setNumero(Integer.valueOf(13525));
		req.getOrdinativoPagamento().setAnnoBilancio(req.getBilancio().getAnno());
		
		//movimento di gestione di destinazione
		req.setImpegnoSuCuiSpostare(new MovimentoKey());
		req.getImpegnoSuCuiSpostare().setAnnoEsercizio(req.getBilancio().getAnno());
		req.getImpegnoSuCuiSpostare().setAnnoMovimento(2017);
		req.getImpegnoSuCuiSpostare().setNumeroMovimento(new BigDecimal(2537));
		
		req.setUtilizzaProvvedimentoDaMovimento(true);
		
		ReintroitoOrdinativoPagamentoResponse res = reintroitoOrdinativoPagamentoService.executeService(req);
		assertNotNull(res);
	}

}
