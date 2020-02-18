/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.componenteimpcap;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.RicercaSinteticaTipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.RicercaTipoComponenteImportiCapitoloPerCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

public class TipoComponenteImportiCapitoloTest extends BaseJunit4TestCase {
	
	@Autowired private RicercaTipoComponenteImportiCapitoloPerCapitoloService ricercaTipoComponenteImportiCapitoloPerCapitoloService;
	@Autowired private RicercaSinteticaTipoComponenteImportiCapitoloService ricercaSinteticaTipoComponenteImportiCapitoloService;
	
	@Test
	public void ricercaTipoComponenteImportiCapitoloPerCapitolo() {
		RicercaTipoComponenteImportiCapitoloPerCapitolo req = new RicercaTipoComponenteImportiCapitoloPerCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setCapitolo(create(CapitoloUscitaGestione.class, 201854));
		
		RicercaTipoComponenteImportiCapitoloPerCapitoloResponse res = ricercaTipoComponenteImportiCapitoloPerCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaTipoComponenteImporti() {
		RicercaSinteticaTipoComponenteImportiCapitolo req = new RicercaSinteticaTipoComponenteImportiCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2019));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.getParametriPaginazione().setElementiPerPagina(Integer.MAX_VALUE);
		req.setTipoComponenteImportiCapitolo(new TipoComponenteImportiCapitolo());
		req.getTipoComponenteImportiCapitolo().setDescrizione("7152");
		req.getTipoComponenteImportiCapitolo().setStatoTipoComponenteImportiCapitolo(StatoTipoComponenteImportiCapitolo.ANNULLATO);
//		req.setPropostaDefaultComponenteImportiCapitoloDaEscludere(PropostaDefaultComponenteImportiCapitolo.SI, PropostaDefaultComponenteImportiCapitolo.SOLO_GESTIONE);
//		req.setSoloValidiPerBilancio(true);
//		req.setMacrotipoComponenteImportiCapitoloDaEscludere(MacrotipoComponenteImportiCapitolo.AVANZO);
//		req.setSottotipoComponenteImportiCapitoloDaEscludere(new SottotipoComponenteImportiCapitolo[] { SottotipoComponenteImportiCapitolo.CUMULATO, SottotipoComponenteImportiCapitolo.APPLICATO});
		
		
		RicercaSinteticaTipoComponenteImportiCapitoloResponse res = ricercaSinteticaTipoComponenteImportiCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
}
