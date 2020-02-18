/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.elencodocumentiallegato;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaElencoService;
import it.csi.siac.siacbilser.business.service.elencodocumentiallegato.RicercaTotaliPredocumentiPerElencoService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegatoModelDetail;

public class ElencoDocumentiAllegatoTest extends BaseJunit4TestCase {

	@Autowired
	private RicercaTotaliPredocumentiPerElencoService ricercaTotaliPredocumentiPerElencoService;
	
	@Test
	public void ricercaTotaliPredocumentiPerElenco() {
		RicercaTotaliPredocumentiPerElenco req = new RicercaTotaliPredocumentiPerElenco();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setElencoDocumentiAllegato(create(ElencoDocumentiAllegato.class, 418));
		
		RicercaTotaliPredocumentiPerElencoResponse res = ricercaTotaliPredocumentiPerElencoService.executeService(req);
		assertNotNull(res);
	}
	
	@Autowired
	private RicercaElencoService ricercaElencoservice;
	
	@Test
	public void testRicercaElenco(){
		RicercaElenco req = new RicercaElenco();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
		eda.setAnno(2015);
		//eda.setNumero(319);
		eda.setEnte(getEnteTest());
		req.setElencoDocumentiAllegato(eda);
		req.setNumeroDa(318);
		req.setNumeroA(320);
		ElencoDocumentiAllegatoModelDetail[] modelDetails = new ElencoDocumentiAllegatoModelDetail[] {ElencoDocumentiAllegatoModelDetail.AttoAllegatoExtended};
		req.setModelDetails(modelDetails);
		ParametriPaginazione pp = new ParametriPaginazione(0,10);
		req.setParametriPaginazione(pp);
		
		RicercaElencoResponse res =  ricercaElencoservice.executeService(req);
		assertNotNull(res);
		if(res.getElenchiDocumentiAllegato() != null) {
			ElencoDocumentiAllegato elencoDocumentiAllegato = res.getElenchiDocumentiAllegato().get(0);
			System.out.println("elencoDocumentiAllegato numero: " + elencoDocumentiAllegato.getNumero() + " e' collegato ad un atto con data scadenza " + elencoDocumentiAllegato.getAllegatoAtto().getDataScadenza());
		}
	}
	
}
