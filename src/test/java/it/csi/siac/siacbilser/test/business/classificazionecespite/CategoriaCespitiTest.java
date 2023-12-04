/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.classificazionecespite;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.classificazionecespiti.AggiornaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.AnnullaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.EliminaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.InserisciCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaDettaglioCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaSinteticaCategoriaCespitiService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.VerificaAnnullabilitaCategoriaCespitiService;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCalcoloTipoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccorser.model.Richiedente;

public class CategoriaCespitiTest extends BaseJunit4TestCase {

	@Autowired private InserisciCategoriaCespitiService inserisciCategoriaCespitiService;
	@Autowired private AggiornaCategoriaCespitiService aggiornaCategoriaCespitiService;
	@Autowired private RicercaDettaglioCategoriaCespitiService ricercaDettaglioCategoriaCespitiService;
	@Autowired private RicercaSinteticaCategoriaCespitiService ricercaSinteticaCategoriaCespitiService;
	@Autowired private EliminaCategoriaCespitiService eliminaCategoriaCespitiService;
	@Autowired private AnnullaCategoriaCespitiService annullaCategoriaCespitiService;
	@Autowired private VerificaAnnullabilitaCategoriaCespitiService verificaAnnullabilitaCategoriaCespitiService;
	
	@Test
	public void inserisciCategoriaCespiti(){
		InserisciCategoriaCespiti req = new InserisciCategoriaCespiti();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		
		CategoriaCespiti categoriaCespiti = new CategoriaCespiti();
		categoriaCespiti.setCodice("011");
		categoriaCespiti.setDescrizione("Monitors");
		categoriaCespiti.setAmbito(Ambito.AMBITO_FIN);
		categoriaCespiti.setAliquotaAnnua(new BigDecimal("5"));
		categoriaCespiti.setCategoriaCalcoloTipoCespite(create(CategoriaCalcoloTipoCespite.class, 4));
		
		req.setCategoriaCespiti(categoriaCespiti);
		
		InserisciCategoriaCespitiResponse res = inserisciCategoriaCespitiService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaCategoriaCespiti(){
		
		RicercaDettaglioCategoriaCespitiResponse ricercaDettaglio = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		
		AggiornaCategoriaCespiti req = new AggiornaCategoriaCespiti();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		
		CategoriaCespiti categoriaCespiti = ricercaDettaglio.getCategoriaCespiti();
		req.setCategoriaCespiti(categoriaCespiti);
		
		AggiornaCategoriaCespitiResponse res = aggiornaCategoriaCespitiService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioCategoriaCespiti(){
		RicercaDettaglioCategoriaCespitiResponse res = ottieniResponseRicercaDettaglio(getRichiedenteByProperties("consip", "regp"));
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaCategoriaCespiti() {
		RicercaSinteticaCategoriaCespiti req = new RicercaSinteticaCategoriaCespiti();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		CategoriaCespiti cat = new CategoriaCespiti();
//		cat.setDescrizione("cancelleria");
		cat.setCodice("001");
//		cat.setCategoriaCalcoloTipoCespite(create(CategoriaCalcoloTipoCespite.class, 1));
//		cat.setAliquotaAnnua(new BigDecimal("10"));
		req.setCategoriaCespiti(cat);
//		req.setEscludiAnnullati(Boolean.TRUE);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		RicercaSinteticaCategoriaCespitiResponse response = ricercaSinteticaCategoriaCespitiService.executeService(req);
		assertNotNull(response.getListaCategoriaCespiti());
//		CategoriaCespiti categoriaCespiti = response.getListaCategoriaCespiti().get(0);
//		System.out.println("Annullato? " + categoriaCespiti.getAnnullato());
	}

	/**
	 * @return
	 */
	private RicercaDettaglioCategoriaCespitiResponse ottieniResponseRicercaDettaglio(Richiedente richiedente) {
		RicercaDettaglioCategoriaCespiti req = new RicercaDettaglioCategoriaCespiti();
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		req.setAnnoBilancio(new Integer(2017));			
		req.setCategoriaCespiti(create(CategoriaCespiti.class, 1023));	
		RicercaDettaglioCategoriaCespitiResponse res = ricercaDettaglioCategoriaCespitiService.executeService(req);
		return res;
	}
	
	@Test
	public void eliminaCategoriaCespitiTest() {
		EliminaCategoriaCespiti req = new EliminaCategoriaCespiti();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		CategoriaCespiti cat = new CategoriaCespiti();
//		cat.setDescrizione("cancelleria");
		cat.setUid(1017);
		req.setCategoriaCespiti(cat);
		EliminaCategoriaCespitiResponse res = eliminaCategoriaCespitiService.executeService(req);
		assertNotNull(res);
		//eliminaCategoriaCespitiService
	}
	

	
	@Test
	public void annullaCategoriaCespiti(){
		AnnullaCategoriaCespiti req = new AnnullaCategoriaCespiti();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(new Integer(2017));
		req.setCategoriaCespiti(create(CategoriaCespiti.class, 982));
		AnnullaCategoriaCespitiResponse res = annullaCategoriaCespitiService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void verificaAnnullabilitaCategoriaCespiti() {
		VerificaAnnullabilitaCategoriaCespiti req = new VerificaAnnullabilitaCategoriaCespiti();

		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setCategoriaCespiti(create(CategoriaCespiti.class, 14));
//		req.setCategoriaCespiti(create(CategoriaCespiti.class, 38));

		VerificaAnnullabilitaCategoriaCespitiResponse res = verificaAnnullabilitaCategoriaCespitiService.executeService(req);
		assertNotNull(res);
	}
}






