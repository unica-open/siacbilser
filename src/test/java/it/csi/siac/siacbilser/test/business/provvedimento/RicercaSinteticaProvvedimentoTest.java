/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.provvedimento;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaSinteticaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaSinteticaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativoModelDetail;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.service.provvedimento.RicercaSinteticaProvvedimentoService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

public class RicercaSinteticaProvvedimentoTest extends BaseJunit4TestCase {
	@Autowired
	private RicercaSinteticaProvvedimentoService ricercaSinteticaProvvedimentoService;
	
	@Test
	public void riceraSinteticaProvvedimento() {
		RicercaSinteticaProvvedimento req = new RicercaSinteticaProvvedimento();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
//		ParametriPaginazione pp = new ParametriPaginazione(7, 10);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(2015);
		TipoAtto tipoAtto = new TipoAtto();
	//	tipoAtto.setUid(128);
		tipoAtto.setUid(70);
		ricercaAtti.setTipoAtto(tipoAtto);
//		ricercaAtti.setNumeroAtto(11);
//		ricercaAtti.setOggetto("prova inserimento automatico");
//		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
//		strutturaAmministrativoContabile.setUid(357);
//		ricercaAtti.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
//		ricercaAtti.setNote("Test 4");
		ricercaAtti.setUid(37649);
//		ricercaAtti.setConDocumentoAssociato(true);
//		ricercaAtti.setStatoOperativo(StatoOperativoAtti.DEFINITIVO.name());
		req.setRicercaAtti(ricercaAtti);
		
		req.setAttoAmministrativoModelDetail(AttoAmministrativoModelDetail.TipoAtto, AttoAmministrativoModelDetail.AllegatoAtto);
		
		RicercaSinteticaProvvedimentoResponse res = ricercaSinteticaProvvedimentoService.executeService(req);
		assertNotNull(res);
	}

}
