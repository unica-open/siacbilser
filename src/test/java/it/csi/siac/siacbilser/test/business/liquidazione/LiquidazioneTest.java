/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.liquidazione;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.liquidazione.AggiornaLiquidazioneService;
import it.csi.siac.siacfinser.business.service.liquidazione.RicercaEstesaLiquidazioniFinService;
import it.csi.siac.siacfinser.business.service.liquidazione.RicercaLiquidazionePerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaLiquidazioniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;


// TODO: Auto-generated Javadoc
/**
 * The Class LiquidazioneTest.
 */
public class LiquidazioneTest extends BaseJunit4TestCase {
	
	/** The leggi conti corrente service. */
	@Autowired
	private RicercaLiquidazionePerChiaveService ricercaLiquidazionePerChiaveService;
	
	@Autowired
	private RicercaEstesaLiquidazioniFinService ricercaEstesaLiquidazioniFinService;
	
	@Test
	public void testRicercaLiquidazionePerChiaveService(){
		RicercaLiquidazionePerChiave req = new RicercaLiquidazionePerChiave();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn2", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		RicercaLiquidazioneK pRicercaLiquidazioneK = new RicercaLiquidazioneK();
		pRicercaLiquidazioneK.setAnnoEsercizio(Integer.valueOf(2017));
		Integer annoLiquidazione = Integer.valueOf(2017);
		pRicercaLiquidazioneK.setAnnoLiquidazione(annoLiquidazione);
		
		Bilancio bilancio = getBilancioTest(165, 2017);
		pRicercaLiquidazioneK.setBilancio(bilancio);
		BigDecimal numeroLiquidazione = new BigDecimal("10");
		pRicercaLiquidazioneK.setNumeroLiquidazione(numeroLiquidazione);
		
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setAnnoLiquidazione(annoLiquidazione);
		liquidazione.setNumeroLiquidazione(numeroLiquidazione);
		pRicercaLiquidazioneK.setLiquidazione(liquidazione);
		
		req.setpRicercaLiquidazioneK(pRicercaLiquidazioneK);
		
		RicercaLiquidazionePerChiaveResponse res = ricercaLiquidazionePerChiaveService.executeService(req);
		log.logXmlTypeObject(res, "RESPONSE");
	}
	
	@Autowired 
	private AggiornaLiquidazioneService aggiornaLiquidazioneService;
	
	@Test
	public void aggiornamentoPostRicercaService(){
		
		RicercaLiquidazionePerChiave reqRicerca = new RicercaLiquidazionePerChiave();
		
		reqRicerca.setDataOra(new Date());
		reqRicerca.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqRicerca.setEnte(reqRicerca.getRichiedente().getAccount().getEnte());
		
		RicercaLiquidazioneK prl = new RicercaLiquidazioneK(); 
		
		//setto anno e numero nella request:
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setAnnoLiquidazione(Integer.valueOf(2016));
		liquidazione.setNumeroLiquidazione(new BigDecimal("81099"));
		prl.setLiquidazione(liquidazione);
		
		prl.setTipoRicerca(Constanti.TIPO_RICERCA_DA_LIQUIDAZIONE);
		Bilancio bilancio = getBilancioByProperties("consip", "regp", "2016");
		prl.setBilancio(bilancio);
		prl.setAnnoEsercizio(Integer.valueOf(prl.getBilancio().getAnno()));

		prl.setTipoRicerca(Constanti.TIPO_RICERCA_DA_LIQUIDAZIONE);

		
		reqRicerca.setpRicercaLiquidazioneK(prl);
		
		RicercaLiquidazionePerChiaveResponse resRicerca = ricercaLiquidazionePerChiaveService.executeService(reqRicerca);
		if(resRicerca.hasErrori()) {
			return;
		}
		
		AggiornaLiquidazione reqAggiorna = new AggiornaLiquidazione();
		
		reqAggiorna.setDataOra(new Date());
		reqAggiorna.setRichiedente(reqRicerca.getRichiedente());
		reqAggiorna.setEnte(reqAggiorna.getRichiedente().getAccount().getEnte());
		reqAggiorna.setAnnoEsercizio(String.valueOf(reqRicerca.getpRicercaLiquidazioneK().getBilancio().getAnno()));
		reqAggiorna.setBilancio(reqRicerca.getpRicercaLiquidazioneK().getBilancio());
		
		reqAggiorna.setLiquidazione(resRicerca.getLiquidazione());
		AggiornaLiquidazioneResponse resAggiorna = aggiornaLiquidazioneService.executeService(reqAggiorna);
		log.logXmlTypeObject(resAggiorna, "AAAAA");
	}
	
/*
<idFascicolo>13cb26c93cc93ac63af830cb39cf19c337cf27c505d83ada30d821c330d901d325cf0a9b339930cc649f608762c9649d789b64cf6287379f669978ce379f319b339f679c3698608a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a758a</idFascicolo>
  <dtIns>2017-08-08 00:00:00.0 CEST</dtIns>
  <utenteIns>
    <codiceFiscale>SIAC</codiceFiscale>
    <cognome>REGP-NNCDNL61C70D969W</cognome>
    <nome>SIAC</nome>
  </utenteIns>
  <direzione>A18000</direzione>
  <numero>7712</numero>
  <settore>A1814A</settore>
  <codiceTipo>ALG</codiceTipo>
  <anno>2017</anno>
  <annoTitolario>null</annoTitolario>
  <causalePagamento>Beneficiari diversi - Salari e stipendi operai forestali OTD
e OTI + Impiegati forestali - Mese di lUGLIO 2017 - Area territoriale
Alessandria/Asti</causalePagamento>
  <codiceTitolario>null</codiceTitolario>
  <idStruttura>3618</idStruttura>
  <dtInserimento>2017-08-08 00:00:00.0 CEST</dtInserimento>
  <dtScadenza>2017-08-14 00:00:00.0 CEST</dtScadenza>
  <flAllegati>S</flAllegati>
  <flDatiSensibili>N</flDatiSensibili>
  <flFatture>N</flFatture>
  <flRitenute>N</flRitenute>
  <flSplitPayment>N</flSplitPayment>
  <importo>170383</importo>
  <ragsocBenef>Beneficiari specificati in elenco</ragsocBenef>
  <descrizioneTipo>Atto di Liquidazione</descrizioneTipo>
  <titolario>null</titolario>
  <utenteScrittore>REGP-NNCDNL61C70D969W</utenteScrittore>
  <descDirezione>A18000 - OPERE PUBBLICHE, DIFESA DEL SUOLO, MONTAGNA, FORESTE,
PROTEZIONE CIVILE, TRASPORTI E LOGISTICA</descDirezione>
</it.csi.bilsrvrp.attiliq.dto.DocumentoAtLiq>

 * 
 */
	@Test
	public void testRicercaEstesaLiquidazioniFinService(){
		
		RicercaEstesaLiquidazioni req = new RicercaEstesaLiquidazioni();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn2", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setAnnoEsercizio(2018);
		
		Richiedente richiedente = new Richiedente();

		Account account = new Account();
		account.setCodice("REGP-NNCDNL61C70D969W");
		richiedente.setAccount(account);
		
		
		
		req.setRichiedente(richiedente);

		AttoAmministrativo atto = new AttoAmministrativo();
		
		req.setAtto(atto);
		
		

		
		
		RicercaEstesaLiquidazioniResponse res = ricercaEstesaLiquidazioniFinService.executeService(req);
		
		log.logXmlTypeObject(res, "RESPONSE");
	}

	
}
