/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.movimentogestione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.business.service.movgest.AnnullaMovimentoSpesaService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaSinteticaImpegniSubimpegniService;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaVTTest.
 */
public class MovimentoGestioneTest extends BaseJunit4TestCase {
	
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	@Autowired private RicercaSinteticaImpegniSubimpegniService ricercaSinteticaImpegniSubimpegniService;
	@Autowired private AnnullaMovimentoSpesaService annullaMovimentoSpesaService;
	/**
	 * Inserisci gruppo attivita.*/
	@Test
	public void testRicercaAccertamentoPerChiaveOttimizzato() {
		
		Bilancio bilancio = getBilancioTest(131, 2017);
		
		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = new RicercaAccertamentoPerChiaveOttimizzato();
		reqRAPCO.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqRAPCO.setEnte(reqRAPCO.getRichiedente().getAccount().getEnte());
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoEsercizio(bilancio.getAnno());
		pRicercaAccertamentoK.setAnnoAccertamento(2017);
		pRicercaAccertamentoK.setNumeroAccertamento(new BigDecimal(1));
		
		reqRAPCO.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		reqRAPCO.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		reqRAPCO.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		reqRAPCO.setEscludiSubAnnullati(true);
		reqRAPCO.setCaricaFlagPresenteStoricizzazioneNelBilancio(true);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(reqRAPCO);
		assertNotNull(response);
	}
	
	@Test
	public void ricercaSinteticaImpegniSubImpegniTEST() {
		
		String methodName = "ricercaSinteticaImpegniSubImpegniTEST";

		RicercaSinteticaImpegniSubImpegni request = new RicercaSinteticaImpegniSubImpegni();
		request.setNumPagina(1);
		request.setNumRisultatiPerPagina(100);
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		request.setEnte(request.getRichiedente().getAccount().getEnte());
		
		ParametroRicercaImpSub pr = new ParametroRicercaImpSub();
		pr.setAnnoEsercizio(Integer.valueOf(2019));
		
		/* ***PROGETTO*** */
		pr.setCodiceProgetto("mas-2019 01");
		pr.setProgetto("mas-2019 01");
//		pr.setUidCronoprogramma(869);
		
		/* ***PROVVEDIMENTO*** */
//		pr.setAnnoProvvedimento(Integer.valueOf(2017));
//		pr.setNumeroProvvedimento(Integer.valueOf(11));
		//pr.setTipoProvvedimento(impostaEntitaFacoltativa(attoAmministrativo.getTipoAtto()));
		
		request.setParametroRicercaImpSub(pr);
		
		RicercaSinteticaImpegniSubimpegniResponse response = ricercaSinteticaImpegniSubimpegniService.executeService(request);

		assertNotNull(response);
		assertTrue(response.getListaImpegni().size() > 0);
		
		for (Impegno imp : response.getListaImpegni()) {
			log.debug(methodName, imp.getAnnoMovimento() + "/" + imp.getNumero());
		}
		
		System.out.println("--- TEST FINITO ---");
	}
	
	/**
	 * Test
	 */
	@Test
	public void annullaMovimentoSpesa() {
		
		AnnullaMovimentoSpesa req = new AnnullaMovimentoSpesa();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setBilancio(create(Bilancio.class, 133));
		req.getBilancio().setAnno(2018);
		
		Impegno impegno = create(Impegno.class, 120614);
		
		List<ModificaMovimentoGestioneSpesa> listaModifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
		ModificaMovimentoGestioneSpesa modDaAnnullare = new ModificaMovimentoGestioneSpesa();
		modDaAnnullare.setUid(29478);
		listaModifiche.add(modDaAnnullare);
		
		impegno.setListaModificheMovimentoGestioneSpesa(listaModifiche);
		
		req.setImpegno(impegno);
		
		AnnullaMovimentoSpesaResponse response = annullaMovimentoSpesaService.executeService(req);
		assertNotNull(response);
	}
	
	
		
}