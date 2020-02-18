/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AggiornaCapitoloDiUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

public class CapitoloUscitaPrevisioneTest extends BaseJunit4TestCase {
	
	@Autowired private AggiornaCapitoloDiUscitaPrevisioneService aggiornaCapitoloDiUscitaPrevisioneService;
	
	@Test
	public void aggiornaCapitoloDiUscitaPrevisione() {
		AggiornaCapitoloDiUscitaPrevisione req = new AggiornaCapitoloDiUscitaPrevisione();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setDataOra(new Date());
		
		req.setCapitoloUscitaPrevisione(create(CapitoloUscitaPrevisione.class, 201858));
		req.getCapitoloUscitaPrevisione().setAnnoCapitolo(req.getAnnoBilancio());
		req.getCapitoloUscitaPrevisione().setDescrizione("Test variazione importi su aggiornamento capitolo previsione\r\nVerifica per traslazione controlli su componenti capitolo");
		req.getCapitoloUscitaPrevisione().setDescrizioneArticolo("");
		req.getCapitoloUscitaPrevisione().setFlagImpegnabile(Boolean.TRUE);
		req.getCapitoloUscitaPrevisione().setFlagPerMemoria(Boolean.FALSE);
		req.getCapitoloUscitaPrevisione().setFlagRilevanteIva(Boolean.FALSE);
		req.getCapitoloUscitaPrevisione().setFunzDelegateRegione(Boolean.FALSE);
		req.getCapitoloUscitaPrevisione().setNote("");
		req.getCapitoloUscitaPrevisione().setNumeroCapitolo(Integer.valueOf(20190930));
		req.getCapitoloUscitaPrevisione().setNumeroArticolo(Integer.valueOf(0));
		req.getCapitoloUscitaPrevisione().setNumeroUEB(Integer.valueOf(1));
		req.getCapitoloUscitaPrevisione().setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		req.getCapitoloUscitaPrevisione().setBilancio(getBilancioByProperties("consip", "regp", 2020));
		req.getCapitoloUscitaPrevisione().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getCapitoloUscitaPrevisione().setMissione(create(Missione.class, 118535));
		req.getCapitoloUscitaPrevisione().setProgramma(create(Programma.class, 118536));
		req.getCapitoloUscitaPrevisione().setTitoloSpesa(create(TitoloSpesa.class, 118965));
		req.getCapitoloUscitaPrevisione().setMacroaggregato(create(Macroaggregato.class, 118968));
		req.getCapitoloUscitaPrevisione().setElementoPianoDeiConti(create(ElementoPianoDeiConti.class, 130348));
		req.getCapitoloUscitaPrevisione().setStrutturaAmministrativoContabile(create(StrutturaAmministrativoContabile.class, 1719615));
		req.getCapitoloUscitaPrevisione().setClassificazioneCofogProgramma(create(ClassificazioneCofogProgramma.class, 9101));
		req.getCapitoloUscitaPrevisione().setCategoriaCapitolo(create(CategoriaCapitolo.class, 3));
//		req.getCapitoloUscitaPrevisione().setTipoFinanziamento(create(TipoFinanziamento.class, 0));
//		req.getCapitoloUscitaPrevisione().setTipoFondo(create(TipoFondo.class, 0));
		req.getCapitoloUscitaPrevisione().setSiopeSpesa(create(SiopeSpesa.class, 75639324));
//		req.getCapitoloUscitaPrevisione().setRicorrenteSpesa(create(RicorrenteSpesa.class, 0));
//		req.getCapitoloUscitaPrevisione().setPerimetroSanitarioSpesa(create(PerimetroSanitarioSpesa.class, 0));
//		req.getCapitoloUscitaPrevisione().setTransazioneUnioneEuropeaSpesa(create(TransazioneUnioneEuropeaSpesa.class, 0));
//		req.getCapitoloUscitaPrevisione().setPoliticheRegionaliUnitarie(create(PoliticheRegionaliUnitarie.class, 0));
		
		
		ImportiCapitoloUP icup = new ImportiCapitoloUP();
		icup.setStanziamento(new BigDecimal("14.00"));
		icup.setStanziamentoResiduo(new BigDecimal("7.00"));
		icup.setStanziamentoCassa(new BigDecimal("21.00"));
		req.getCapitoloUscitaPrevisione().getListaImportiCapitolo().add(icup);
		
		icup = new ImportiCapitoloUP();
		icup.setStanziamento(new BigDecimal("25.00"));
		req.getCapitoloUscitaPrevisione().getListaImportiCapitolo().add(icup);
		
		icup = new ImportiCapitoloUP();
		icup.setStanziamento(new BigDecimal("35.00"));
		req.getCapitoloUscitaPrevisione().getListaImportiCapitolo().add(icup);
		
		AggiornaCapitoloDiUscitaPrevisioneResponse res = aggiornaCapitoloDiUscitaPrevisioneService.executeService(req);
		assertNotNull(res);
	}
	
}
