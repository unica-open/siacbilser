/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.test.business.provvisorio;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfinser.business.service.provvisorio.RicercaProvvisoriDiCassaService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;

public class ProvvisorioDiCassaTest extends BaseJunit4TestCase {

	@Autowired private RicercaProvvisoriDiCassaService ricercaProvvisoriDiCassaService;
	

	
	@Test
	public void ricercaProvvisoriDiCassa() {
		RicercaProvvisoriDiCassa req = new RicercaProvvisoriDiCassa();
		
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		ParametroRicercaProvvisorio parametroRicercaProvvisorio = new ParametroRicercaProvvisorio();
		parametroRicercaProvvisorio.setAnnoDa(2020);
		parametroRicercaProvvisorio.setAnnoA(2020);
		parametroRicercaProvvisorio.setNumeroDa(67801);
		parametroRicercaProvvisorio.setNumeroA(67801);
//		parametroRicercaProvvisorio.setNumeroA(300418);
		parametroRicercaProvvisorio.setEscludiProvvisoriConImportoDaEmettereZero(Boolean.TRUE);
		
		req.setNumPagina(1);
		req.setNumRisultatiPerPagina(10);
		
		req.setParametroRicercaProvvisorio(parametroRicercaProvvisorio );
		
//		req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail,
//				CespiteModelDetail.ClassificazioneGiuridicaCespite);
				//CespiteModelDetail.DismissioniCespite);
		
		RicercaProvvisoriDiCassaResponse res = ricercaProvvisoriDiCassaService.executeService(req);
		assertNotNull(res);
	}
	
	
	
}
