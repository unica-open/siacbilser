/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaSinteticaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di test del capitolo entrata previsione
 * @author Pro Logic
 *
 */
public class CapitoloEntrataPrevisioneTest extends BaseJunit4TestCase {
	
	@Autowired private RicercaSinteticaCapitoloEntrataPrevisioneService ricercaSinteticaCapitoloEntrataPrevisioneService;
	
	@Test
	public void ricercaSinteticaCapitoloEntrataPrevisione(){
		RicercaSinteticaCapitoloEntrataPrevisione req = new RicercaSinteticaCapitoloEntrataPrevisione();
		req.setAnnoBilancio(Integer.valueOf(2021));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setCalcolaTotaleImporti(Boolean.FALSE);
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CDR);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setRicercaSinteticaCapitoloEPrev(new RicercaSinteticaCapitoloEPrev());
		req.getRicercaSinteticaCapitoloEPrev().setAnnoEsercizio(2021);
		req.getRicercaSinteticaCapitoloEPrev().setAnnoCapitolo(2021);
		req.getRicercaSinteticaCapitoloEPrev().setNumeroCapitolo(17745);
		req.getRicercaSinteticaCapitoloEPrev().setRichiediAccantonamentoFondiDubbiaEsigibilita(Boolean.TRUE);
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse res = ricercaSinteticaCapitoloEntrataPrevisioneService.executeService(req);
		assertNotNull(res);
	}

}
