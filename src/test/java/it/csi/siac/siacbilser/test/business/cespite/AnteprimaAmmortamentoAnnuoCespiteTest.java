/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cespite;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cespiti.InserisciAnteprimaAmmortamentoAnnuoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciPrimeNoteAmmortamentoAnnuoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteService;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail;

public class AnteprimaAmmortamentoAnnuoCespiteTest extends BaseJunit4TestCase {

	@Autowired private RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteService ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteService;
	
	@Autowired private InserisciAnteprimaAmmortamentoAnnuoCespiteService inserisciAnteprimaAmmortamentoAnnuoCespiteService; 
	
	@Autowired private InserisciPrimeNoteAmmortamentoAnnuoCespiteService inserisciPrimeNoteAmmortamentoAnnuoCespiteService;
	
	
	@Test
	public void testRicercaSinteticaDettaglioAnteprima() {
		RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite req = new RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite();
		req.setAnnoBilancio(2017);
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setModelDetails(new ModelDetail[] {DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail.AnteprimaAmmortamento});
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setAnteprimaAmmortamentoAnnuoCespite(create(AnteprimaAmmortamentoAnnuoCespite.class, 0));
		req.getAnteprimaAmmortamentoAnnuoCespite().setAnno(Integer.valueOf(2018));
		
		RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse res = ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteService.executeService(req);
		
	}
	
	@Test
	public void testInserimentoAnteprima() {
		InserisciAnteprimaAmmortamentoAnnuoCespite req = new InserisciAnteprimaAmmortamentoAnnuoCespite();
		req.setAnnoBilancio(2017);
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnno(Integer.valueOf(2018));
		InserisciAnteprimaAmmortamentoAnnuoCespiteResponse res = inserisciAnteprimaAmmortamentoAnnuoCespiteService.executeService(req);
	}
	
	
	@Test
	public void effettuaScritture() {
		InserisciPrimeNoteAmmortamentoAnnuoCespite req = new InserisciPrimeNoteAmmortamentoAnnuoCespite();
		req.setAnnoBilancio(2017);
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoAmmortamentoAnnuo(Integer.valueOf(2018));
		InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse res = inserisciPrimeNoteAmmortamentoAnnuoCespiteService.executeService(req);
	}
}
