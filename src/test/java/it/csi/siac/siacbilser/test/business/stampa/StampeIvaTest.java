/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.stampa;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.StampaLiquidazioneIvaService;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.StampaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.StampaRiepilogoAnnualeIvaService;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoStampa;

/**
 * The Class StampeIvaTest.
 */
public class StampeIvaTest extends BaseJunit4TestCase {

	@Autowired
	private StampaRegistroIvaService stampaRegistroIvaService;
	@Autowired
	private StampaLiquidazioneIvaService stampaLiquidazioneIvaService;
	@Autowired
	private StampaRiepilogoAnnualeIvaService stampaRiepilogoAnnualeIvaService;
	
	@Autowired
	private RegistroIvaDad registroIvaDad;
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	
	@Test
	public void stampaRegistroIva() {
		final String methodName = "stampaRegistroIva";
		StampaRegistroIva req = new StampaRegistroIva();
		
		req.setDataOra(new Date());
		req.setBilancio(getBilancioTest(188, 2018));
		req.setRichiedente(getRichiedenteByProperties("consip", "coal"));
		req.setDocumentiPagati(Boolean.TRUE);
		req.setDocumentiIncassati(Boolean.FALSE);
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setPeriodo(Periodo.MAGGIO);
		req.setTipoStampa(TipoStampa.BOZZA);
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		
//		req.setRegistroIva(create(RegistroIva.class, 203));
//		req.getRegistroIva().setGruppoAttivitaIva(create(GruppoAttivitaIva.class, 353));
//		req.setRegistroIva(create(RegistroIva.class, 119));
//		req.getRegistroIva().setGruppoAttivitaIva(create(GruppoAttivitaIva.class, 352));
		req.setRegistroIva(create(RegistroIva.class, 179));
		req.getRegistroIva().setGruppoAttivitaIva(create(GruppoAttivitaIva.class, 352));
		
		req.getRegistroIva().getGruppoAttivitaIva().setTipoChiusura(TipoChiusura.MENSILE);
		
//		req.getRichiedente().getAccount().setLoginOperazione("TEST_JUNIT");
		
		StampaRegistroIvaResponse response = stampaRegistroIvaService.executeService(req);
		assertNotNull(response);
		try {
			Thread.sleep(60 * 60 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void stampaLiquidazioneIva() {
		StampaLiquidazioneIva req = new StampaLiquidazioneIva();
		req.setRichiedente(getRichiedenteByProperties("consip", "edisu"));
		req.setDataOra(new Date());
		
		req.setBilancio(getBilancioTest(179, 2018));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setAnnoBilancio(Integer.valueOf(req.getBilancio().getAnno()));
		
//		GruppoAttivitaIva gai = gruppoAttivitaIvaDad.findGruppoAttivitaIvaByIdAndAnno(24, 2015);
//		GruppoAttivitaIva gai = new GruppoAttivitaIva();
//		gai.setUid(11);
//		log.logXmlTypeObject(gai, "Gruppo Attivita Iva");
		req.setGruppoAttivitaIva(create(GruppoAttivitaIva.class, 106));
		
		req.setPeriodo(Periodo.GENNAIO);
		req.setTipoStampa(TipoStampa.BOZZA);
		
//		Bilancio b = new Bilancio();
//		b.setUid(6);
//		req.setBilancio(getBilancio2015Test());
		
		StampaLiquidazioneIvaResponse res = stampaLiquidazioneIvaService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void stampaLiquidazioneIvaFORN2() {
		StampaLiquidazioneIva req = new StampaLiquidazioneIva();
		req.setRichiedente(getRichiedenteForn2());
		req.setEnte(getEnteForn2());
		
		GruppoAttivitaIva gai = new GruppoAttivitaIva();
		gai.setEnte(getEnteForn2());
		gai.setCodice("GCM");
		gai.setUid(100);
		log.logXmlTypeObject(gai, "Gruppo Attivita Iva");
		req.setGruppoAttivitaIva(gai);
		
		Periodo p = Periodo.FEBBRAIO;
		req.setPeriodo(p);
		TipoStampa ts = TipoStampa.BOZZA;
		req.setTipoStampa(ts);
		
		Bilancio b = new Bilancio();
		b.setUid(55);
		req.setBilancio(b);
		
		StampaLiquidazioneIvaResponse res = stampaLiquidazioneIvaService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void inserisceStampaIvaFORN2() {
//		InserisceStampaIva req = new InserisceStampaIva();
//		req.setRichiedente(getRichiedenteForn2());
//		
//		StampaIva stampaIva = new StampaIva();
//		stampaIva.setStato(StatoEntita.VALIDO);
//		stampaIva.setAnnoEsercizio(2016);
//		stampaIva.setCodice("");
//		stampaIva.setEnte(getEnteForn2());
//		
//		StampaLiquidazioneIvaResponse res = stampaLiquidazioneIvaService.executeService(req);
//		assertNotNull(res);
//		
//		try {
//			Thread.sleep(20 * 60 * 1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
	@Test
	public void stampaRiepilogoAnnualeIva() {
		
		StampaRiepilogoAnnualeIva req = new StampaRiepilogoAnnualeIva();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		GruppoAttivitaIva gruppoAttivitaIva = new GruppoAttivitaIva();
		gruppoAttivitaIva.setUid(11);
		
		req.setGruppoAttivitaIva(gruppoAttivitaIva);
		req.setBilancio(getBilancioTest(17, 2015));
		log.logXmlTypeObject(req, "request ");

		StampaRiepilogoAnnualeIvaResponse res = stampaRiepilogoAnnualeIvaService.executeService(req);
		log.logXmlTypeObject(res, "response ");
		assertNotNull(res);
		
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void stampaRiepilogoAnnualeIvaFORN2() {
		
		StampaRiepilogoAnnualeIva req = new StampaRiepilogoAnnualeIva();
		req.setRichiedente(getRichiedenteForn2());
		req.setEnte(getEnteForn2());
		GruppoAttivitaIva gruppoAttivitaIva = new GruppoAttivitaIva();
		gruppoAttivitaIva.setUid(106);
		
		req.setGruppoAttivitaIva(gruppoAttivitaIva);
		req.setBilancio(getBilancioForn2());
		
		StampaRiepilogoAnnualeIvaResponse res = stampaRiepilogoAnnualeIvaService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
//	 private Ente getEnteForn2() {
//			return   getEnteTest(13);
//		}
//		
//		private Bilancio getBilancioForn2(){
//			Bilancio bilancio = new Bilancio();
//			bilancio.setUid(51);
//			bilancio.setAnno(2016);
//			return bilancio;
//		}
		
		
		/**
		 * Gets the account test.
		 *
		 * @param uidAccount
		 *            l'uid dell'account
		 * @param uidEnte
		 *            l'uid dell'ente
		 * 
		 * @return the account test
		 */
		private Account getAccountTestFORN2(int uidAccount, int uidEnte) {
			Account account = new Account();
			account.setUid(uidAccount);
			account.setEnte(getEnteTest(uidEnte));
			account.setNome("AMMINISTRATORE");
			// account.setLoginOperazione("AC-AMM-13-VTLNLN73T62C129D");
			return account;
		}

		private Richiedente getRichiedenteForn2() {
			// EDISU
			// UID 967 ,757 E' AMMINISTRTATORE
			Richiedente richiedente = new Richiedente();
			Operatore operatore = new Operatore();
			operatore.setCodiceFiscale("AAAAAA00A11E000M");
			operatore.setNome("DEMO24");
			operatore.setUid(0);
			richiedente.setOperatore(operatore);
			Account account = getAccountTestFORN2(757, 13);
			richiedente.setAccount(account);
			return richiedente;

//			return getRichiedenteTest("AAAAAA00A11E000M",757,13);

			// return getRichiedenteTest("admin",7,1);
		}
		
		@Autowired
		protected SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
		
		
		@Test
		public void stampaRegistroIvaTest(){
//			List<SubdocumentoIvaEntrata> listaSubdocumentoIvaEntrataNelPeriodo = new ArrayList<SubdocumentoIvaEntrata>();
//
//			Integer annoEsercizio = getBilancio2015Test().getAnno();
//			Periodo p = Periodo.GENNAIO;
//			
//			SubdocumentoIvaEntrata sie = new SubdocumentoIvaEntrata();
//			sie.setAnnoEsercizio(annoEsercizio);
//			sie.setEnte(getEnteTest());
//			RegistroIva registroIva = registroIvaDad.findRegistroIvaById(4);
//			if(registroIva == null){
//				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Registro Iva", "con uid:"));
//			}
//					/*new RegistroIva();
//			registroIva.setUid(4);
//			registroIva.setEnte(getEnteTest());
//			registroIva.setGruppoAttivitaIva(gruppoAttivitaIva);*/
//			sie.setRegistroIva(registroIva);
//			sie.setStatoSubdocumentoIva(StatoSubdocumentoIva.DEFINITIVO);
//
//			//log.logXmlTypeObject(sie, "subdocumento da cercare");
//			log.debug("test", "inizio periodo " + p.getInizioPeriodo(annoEsercizio) + " fine periodo " + p.getFinePeriodo(annoEsercizio) );
//			listaSubdocumentoIvaEntrataNelPeriodo = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrata(sie,null, null,
//					p.getInizioPeriodo(annoEsercizio), p.getFinePeriodo(annoEsercizio));
//			log.debug("test", "lista vuota?" + listaSubdocumentoIvaEntrataNelPeriodo.isEmpty());
//			log.logXmlTypeObject(listaSubdocumentoIvaEntrataNelPeriodo, "subdocumenti trovati");
//			log.debug("test", "inizio periodo " + p.getInizioPeriodo(annoEsercizio) + "fine periodo " + p.getFinePeriodo(annoEsercizio)); 
		}
		
		
		
		@Test
		public void stampaRegistroIvaFORN2() {
			final String methodName = "stampaRegistroIva";
			StampaRegistroIva req = new StampaRegistroIva();
			req.setRichiedente(getRichiedenteByProperties("forn2", "coal"));
			req.setEnte(getEnteForn2());
			
			// ACQUISTI_IVA_IMMEDIATA
//			final int uidRegistroIva = 1;
			// ACQUISTI_IVA_DIFFERITA
			final int uidRegistroIva = 3;
			
			// CORRISPETTIVI
//			final int uidRegistroIva = 54;
			// VENDITE_IVA_IMMEDIATA
//			final int uidRegistroIva = 41;
			// VENDITE_IVA_DIFFERITA
//			final int uidRegistroIva = 9;
			
			RegistroIva registroIva = registroIvaDad.findRegistroIvaById(uidRegistroIva);
			log.debug(methodName, "Stampa registro iva con uid " + uidRegistroIva + " di tipo " + registroIva.getTipoRegistroIva());
			log.logXmlTypeObject(registroIva, "Registro Iva");
			
			req.setRegistroIva(registroIva);
			
			Periodo p = Periodo.MAGGIO;
			req.setPeriodo(p);
			TipoStampa ts = TipoStampa.BOZZA;
			req.setTipoStampa(ts);
			
			req.setDocumentiPagati(Boolean.TRUE);
			req.setDocumentiIncassati(Boolean.FALSE);
			
//			req.setBilancio(188);
			
			StampaRegistroIvaResponse response = stampaRegistroIvaService.executeService(req);
			assertNotNull(response);
			try {
				Thread.sleep(20 * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error("stampaAllegatoAtto", "InterruptedException during test", e);
				throw new RuntimeException("Test failure", e);
			}
			
		}
		
//		public static void main(String[] args) {
//			
//			StringBuilder sb1 = new StringBuilder();
//			StringBuilder sb2 = new StringBuilder();
//			StringBuilder sb3 = new StringBuilder();
//			sb1.append("<riepilogo><riepiloghiIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>3543</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>284</uid><codice>004 - C</codice><descrizione>IVA 4%</descrizione><aliquotaIvaTipo>COMMERCIALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>4</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>7500.00</imponibile><imposta>300.00</imposta><impostaDetraibile>300.00</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>7800.00</totale></aliquotaSubdocumentoIva><imponibile>17884.61</imponibile><iva>715.39</iva><progressivoImponibile>17884.61</progressivoImponibile><progressivoIva>715.39</progressivoIva><totale>18600.00</totale><totaleProgressivo>18600.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>3545</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>286</uid><codice>010 - C</codice><descrizione>IVA 10%</descrizione><aliquotaIvaTipo>COMMERCIALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>10</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>1318.18</imponibile><imposta>131.82</imposta><impostaDetraibile>131.82</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>1450.00</totale></aliquotaSubdocumentoIva><imponibile>1581.82</imponibile><iva>158.18</iva><progressivoImponibile>9839.09</progressivoImponibile><progressivoIva>983.91</progressivoIva><totale>1740.00</totale><totaleProgressivo>10823.00</totaleProgressivo></riepilogoIva></riepiloghiIva><periodo>MARZO</periodo><totaleIVA>873.57</totaleIVA><totaleImponibile>19466.43</totaleImponibile><totaleProgressivoImponibile>27723.70</totaleProgressivoImponibile><totaleProgressivoIva>1699.30</totaleProgressivoIva><totaleTotale>20340.00</totaleTotale><totaleTotaleProgressivo>29423.00</totaleTotaleProgressivo></riepilogo>");
//									sb2.append( "<riepilogo><riepiloghiIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>2396</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>195</uid><codice>010</codice><descrizione>IVA 10%</descrizione><aliquotaIvaTipo>ISTITUZIONALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>10</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>35.45</imponibile><imposta>3.55</imposta><impostaDetraibile>3.55</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>39.00</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>19620.00</progressivoImponibile><progressivoIva>1962.00</progressivoIva><totale>0</totale><totaleProgressivo>21582.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>3296</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>196</uid><codice>E10</codice><descrizione>ESENTE ART. 10 DPR 633/72</descrizione><aliquotaIvaTipo>ISTITUZIONALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>0</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>900.00</imponibile><imposta>0.00</imposta><impostaDetraibile>0.00</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>900.00</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>3600.00</progressivoImponibile><progressivoIva>0.00</progressivoIva><totale>0</totale><totaleProgressivo>3600.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>2358</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>194</uid><codice>004</codice><descrizione>IVA 4%</descrizione><aliquotaIvaTipo>ISTITUZIONALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>4</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>165.87</imponibile><imposta>6.63</imposta><impostaDetraibile>6.63</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>172.50</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>1067.32</progressivoImponibile><progressivoIva>42.68</progressivoIva><totale>0</totale><totaleProgressivo>1110.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>2348</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>220</uid><codice>022</codice><descrizione>IVA 22%</descrizione><aliquotaIvaTipo>ISTITUZIONALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>22</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>3000.00</imponibile><imposta>660.00</imposta><impostaDetraibile>660.00</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>3660.00</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>6000.00</progressivoImponibile><progressivoIva>1320.00</progressivoIva><totale>0</totale><totaleProgressivo>7320.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>3043</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>286</uid><codice>010 - C</codice><descrizione>IVA 10%</descrizione><aliquotaIvaTipo>COMMERCIALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>10</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>35.45</imponibile><imposta>3.55</imposta><impostaDetraibile>3.55</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>39.00</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>16514.54</progressivoImponibile><progressivoIva>1651.46</progressivoIva><totale>0</totale><totaleProgressivo>18166.00</totaleProgressivo></riepilogoIva></riepiloghiIva><periodo>FEBBRAIO</periodo><totaleIVA>0</totaleIVA><totaleImponibile>0</totaleImponibile><totaleProgressivoImponibile>46801.86</totaleProgressivoImponibile><totaleProgressivoIva>4976.14</totaleProgressivoIva><totaleTotale>0</totaleTotale><totaleTotaleProgressivo>51778.00</totaleTotaleProgressivo></riepilogo>");
//											sb3.append("<riepilogo><riepiloghiIva/><periodo>GENNAIO</periodo><totaleIVA>0</totaleIVA><totaleImponibile>0</totaleImponibile><totaleProgressivoImponibile>0</totaleProgressivoImponibile><totaleProgressivoIva>0</totaleProgressivoIva><totaleTotale>0</totaleTotale><totaleTotaleProgressivo>0</totaleTotaleProgressivo></riepilogo></listaRiepilogo>");
//			
//			StampaRegistroIvaRiepilogo el1 = JAXBUtility.unmarshall(sb1.toString(), StampaRegistroIvaRiepilogo.class);
//			StampaRegistroIvaRiepilogo el2 = JAXBUtility.unmarshall(sb2.toString(), StampaRegistroIvaRiepilogo.class);
//			StampaRegistroIvaRiepilogo el3 = JAXBUtility.unmarshall(sb3.toString(), StampaRegistroIvaRiepilogo.class);
//			
//			List<StampaRegistroIvaRiepilogo> sezione2 = new ArrayList<StampaRegistroIvaRiepilogo>();
//			sezione2.add(el1);
//			sezione2.add(el2);
//			sezione2.add(el3);
//			
//			
//			List<StampaRegistroIvaRiepilogo> result = elaboraSezione2(sezione2);
//			
//			for(StampaRegistroIvaRiepilogo r : result){
//				log.logXmlTypeObject(registroIva, "Registro Iva");
//			}
//			
//		}
		
		@Test
		public void prova(){
//			StringBuilder sb1 = new StringBuilder();
//			StringBuilder sb2 = new StringBuilder();
//			StringBuilder sb3 = new StringBuilder();
//			sb1.append("<riepilogo><riepiloghiIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>3543</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>284</uid><codice>004 - C</codice><descrizione>IVA 4%</descrizione><aliquotaIvaTipo>COMMERCIALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>4</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>7500.00</imponibile><imposta>300.00</imposta><impostaDetraibile>300.00</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>7800.00</totale></aliquotaSubdocumentoIva><imponibile>17884.61</imponibile><iva>715.39</iva><progressivoImponibile>17884.61</progressivoImponibile><progressivoIva>715.39</progressivoIva><totale>18600.00</totale><totaleProgressivo>18600.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>3545</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>286</uid><codice>010 - C</codice><descrizione>IVA 10%</descrizione><aliquotaIvaTipo>COMMERCIALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>10</percentualeAliquota><percentualeIndetraibilita>5</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>1318.18</imponibile><imposta>131.82</imposta><impostaDetraibile>131.82</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>1450.00</totale></aliquotaSubdocumentoIva><imponibile>1581.82</imponibile><iva>158.18</iva><progressivoImponibile>9839.09</progressivoImponibile><progressivoIva>983.91</progressivoIva><totale>1740.00</totale><totaleProgressivo>10823.00</totaleProgressivo></riepilogoIva></riepiloghiIva><periodo>MARZO</periodo><totaleIVA>873.57</totaleIVA><totaleImponibile>19466.43</totaleImponibile><totaleProgressivoImponibile>27723.70</totaleProgressivoImponibile><totaleProgressivoIva>1699.30</totaleProgressivoIva><totaleTotale>20340.00</totaleTotale><totaleTotaleProgressivo>29423.00</totaleTotaleProgressivo></riepilogo>");
//									sb2.append( "<riepilogo><riepiloghiIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>2396</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>195</uid><codice>010</codice><descrizione>IVA 10%</descrizione><aliquotaIvaTipo>ISTITUZIONALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>10</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>35.45</imponibile><imposta>3.55</imposta><impostaDetraibile>3.55</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>39.00</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>19620.00</progressivoImponibile><progressivoIva>1962.00</progressivoIva><totale>0</totale><totaleProgressivo>21582.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>3296</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>196</uid><codice>E10</codice><descrizione>ESENTE ART. 10 DPR 633/72</descrizione><aliquotaIvaTipo>ISTITUZIONALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>0</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>900.00</imponibile><imposta>0.00</imposta><impostaDetraibile>0.00</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>900.00</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>3600.00</progressivoImponibile><progressivoIva>0.00</progressivoIva><totale>0</totale><totaleProgressivo>3600.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>2358</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>194</uid><codice>004</codice><descrizione>IVA 4%</descrizione><aliquotaIvaTipo>ISTITUZIONALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>4</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>165.87</imponibile><imposta>6.63</imposta><impostaDetraibile>6.63</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>172.50</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>1067.32</progressivoImponibile><progressivoIva>42.68</progressivoIva><totale>0</totale><totaleProgressivo>1110.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>2348</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>220</uid><codice>022</codice><descrizione>IVA 22%</descrizione><aliquotaIvaTipo>ISTITUZIONALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>22</percentualeAliquota><percentualeIndetraibilita>0</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>3000.00</imponibile><imposta>660.00</imposta><impostaDetraibile>660.00</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>3660.00</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>6000.00</progressivoImponibile><progressivoIva>1320.00</progressivoIva><totale>0</totale><totaleProgressivo>7320.00</totaleProgressivo></riepilogoIva><riepilogoIva><aliquotaSubdocumentoIva><loginOperazione>EDISU1-DLEDNC70P53D764P</loginOperazione><stato>VALIDO</stato><uid>3043</uid><aliquotaIva><loginOperazione>admin</loginOperazione><stato>VALIDO</stato><uid>286</uid><codice>010 - C</codice><descrizione>IVA 10%</descrizione><aliquotaIvaTipo>COMMERCIALE</aliquotaIvaTipo><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><percentualeAliquota>10</percentualeAliquota><percentualeIndetraibilita>5</percentualeIndetraibilita><tipoOperazioneIva codice='NI' descrizione='Non imponibile'>NON_IMPONIBILE</tipoOperazioneIva></aliquotaIva><ente><loginOperazione>fnc_siac_bko_crea_ente_da_modello</loginOperazione><stato>VALIDO</stato><uid>13</uid><codiceFiscale>97547570016</codiceFiscale><gestioneLivelli/><nome>E.D.I.S.U</nome></ente><imponibile>35.45</imponibile><imposta>3.55</imposta><impostaDetraibile>3.55</impostaDetraibile><impostaIndetraibile>0.00</impostaIndetraibile><totale>39.00</totale></aliquotaSubdocumentoIva><imponibile>0</imponibile><iva>0</iva><progressivoImponibile>16514.54</progressivoImponibile><progressivoIva>1651.46</progressivoIva><totale>0</totale><totaleProgressivo>18166.00</totaleProgressivo></riepilogoIva></riepiloghiIva><periodo>FEBBRAIO</periodo><totaleIVA>0</totaleIVA><totaleImponibile>0</totaleImponibile><totaleProgressivoImponibile>46801.86</totaleProgressivoImponibile><totaleProgressivoIva>4976.14</totaleProgressivoIva><totaleTotale>0</totaleTotale><totaleTotaleProgressivo>51778.00</totaleTotaleProgressivo></riepilogo>");
//											sb3.append("<riepilogo><riepiloghiIva/><periodo>GENNAIO</periodo><totaleIVA>0</totaleIVA><totaleImponibile>0</totaleImponibile><totaleProgressivoImponibile>0</totaleProgressivoImponibile><totaleProgressivoIva>0</totaleProgressivoIva><totaleTotale>0</totaleTotale><totaleTotaleProgressivo>0</totaleTotaleProgressivo></riepilogo>");
//			
//			StampaRegistroIvaRiepilogo el1 = JAXBUtility.unmarshall(sb1.toString(), StampaRegistroIvaRiepilogo.class);
//			StampaRegistroIvaRiepilogo el2 = JAXBUtility.unmarshall(sb2.toString(), StampaRegistroIvaRiepilogo.class);
//			StampaRegistroIvaRiepilogo el3 = JAXBUtility.unmarshall(sb3.toString(), StampaRegistroIvaRiepilogo.class);
//			
//			List<StampaRegistroIvaRiepilogo> sezione2 = new ArrayList<StampaRegistroIvaRiepilogo>();
//			sezione2.add(el1);
//			sezione2.add(el2);
//			sezione2.add(el3);
//			
//			
//			List<StampaRegistroIvaRiepilogo> result = elaboraSezione2(sezione2);
//			
//			for(StampaRegistroIvaRiepilogo r : result){
//				log.logXmlTypeObject(r, "riepilogo");
//			}
		}
		
//		private List<StampaRegistroIvaRiepilogo> elaboraSezione2(List<StampaRegistroIvaRiepilogo> sezione2) {
//			
//			List<StampaRegistroIvaRiepilogo> listaDefinitiva = new ArrayList<StampaRegistroIvaRiepilogo>();
//			StampaRegistroIvaRiepilogo riepilogoUnico = new StampaRegistroIvaRiepilogo();
//			List<StampaRegistroIvaRiepilogoIva> riepiloghiIva = new ArrayList<StampaRegistroIvaRiepilogoIva>();
//			List<String> aliquoteGiaInserite = new ArrayList<String>();
//			
//			 BigDecimal totaleImponibile = BigDecimal.ZERO;
//			 BigDecimal totaleIVA = BigDecimal.ZERO;
//			 BigDecimal totaleTotale = BigDecimal.ZERO;
//			 BigDecimal totaleProgressivoImponibile = BigDecimal.ZERO;
//			 BigDecimal totaleProgressivoIva = BigDecimal.ZERO;
//			 BigDecimal totaleTotaleProgressivo = BigDecimal.ZERO;
//			
//			//totali parziali--ci sono solo per acquisti
//			 BigDecimal totaleImponibileIndetraibile = BigDecimal.ZERO;
//			 BigDecimal totaleImponibileDetraibile = BigDecimal.ZERO;
//			 BigDecimal totaleImponibileEsente = BigDecimal.ZERO;
//			 BigDecimal totaleIVAIndetraibile = BigDecimal.ZERO;
//			 BigDecimal totaleIVADetraibile = BigDecimal.ZERO;
//			 BigDecimal totaleProgressivoImponibileIndetraibile = BigDecimal.ZERO;
//			 BigDecimal totaleProgressivoImponibileDetraibile = BigDecimal.ZERO;
//			 BigDecimal totaleProgressivoImponibileEsente = BigDecimal.ZERO;
//			 BigDecimal totaleProgressivoIvaIndetraibile = BigDecimal.ZERO;
//			 BigDecimal totaleProgressivoIvaDetraibile = BigDecimal.ZERO;
//			 
//			for(StampaRegistroIvaRiepilogo srir : sezione2){
//				for(StampaRegistroIvaRiepilogoIva r : srir.getListaRiepiloghiIva()){
//					if(aliquoteGiaInserite.contains(r.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice())){
//						continue;
//					}
//					if(!srir.getPeriodo().equals(Periodo.MARZO)){
//						r.setImponibile(BigDecimal.ZERO);
//						r.setIva(BigDecimal.ZERO);
//						r.setTotale(BigDecimal.ZERO);
//					}
//					riepiloghiIva.add(r);
//					aliquoteGiaInserite.add(r.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice());
//					BigDecimal percIndetr = r.getAliquotaSubdocumentoIva().getAliquotaIva().getPercentualeIndetraibilita();
//					BigDecimal percDetr = BilUtilities.BIG_DECIMAL_ONE_HUNDRED.subtract(r.getAliquotaSubdocumentoIva().getAliquotaIva().getPercentualeIndetraibilita());
//					
//					totaleImponibile = totaleImponibile.add(r.getImponibile());
//					totaleIVA = totaleIVA.add(r.getIva());
//					totaleTotale = totaleTotale.add(r.getTotale());
//					totaleProgressivoImponibile = totaleProgressivoImponibile.add(r.getProgressivoImponibile());
//					totaleProgressivoIva = totaleProgressivoIva.add(r.getProgressivoIva());
//					totaleTotaleProgressivo = totaleTotaleProgressivo.add(r.getTotaleProgressivo());
//					
//					boolean isEsente = TipoOperazioneIva.NON_IMPONIBILE.equals(r.getAliquotaSubdocumentoIva().getAliquotaIva().getTipoOperazioneIva())
//							|| TipoOperazioneIva.ESENTE.equals(r.getAliquotaSubdocumentoIva().getAliquotaIva().getTipoOperazioneIva())
//							|| TipoOperazioneIva.ESCLUSO_FCI.equals(r.getAliquotaSubdocumentoIva().getAliquotaIva().getTipoOperazioneIva());
//					if (isEsente) {
//						totaleImponibileEsente = totaleImponibileEsente.add(r.getImponibile());
//						totaleProgressivoImponibileEsente = totaleProgressivoImponibileEsente.add(r.getProgressivoImponibile());
//					}
//					
//					totaleImponibileIndetraibile = totaleImponibileIndetraibile.add(r.getImponibile().multiply(percIndetr).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED));
//					totaleIVAIndetraibile = totaleIVAIndetraibile.add(r.getIva().multiply(percIndetr).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED));
//					totaleProgressivoImponibileIndetraibile = totaleProgressivoImponibileIndetraibile.add(r.getProgressivoImponibile().multiply(percIndetr).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED));
//					totaleProgressivoIvaIndetraibile = totaleProgressivoIvaIndetraibile.add(r.getProgressivoIva().multiply(percIndetr).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED));
//					
//					totaleImponibileDetraibile = totaleImponibileDetraibile.add(r.getImponibile().multiply(percDetr).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED));
//					totaleIVADetraibile = totaleIVADetraibile.add(r.getIva().multiply(percDetr).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED));
//					totaleProgressivoImponibileDetraibile = totaleProgressivoImponibileDetraibile.add(r.getProgressivoImponibile().multiply(percDetr).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED));
//					totaleProgressivoIvaDetraibile = totaleProgressivoIvaDetraibile.add(r.getProgressivoIva().multiply(percDetr).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED));
//					
//				}
//			}
//			riepilogoUnico.setListaRiepiloghiIva(riepiloghiIva);
//			
//			riepilogoUnico.setTotaleImponibile(totaleImponibile);
//			riepilogoUnico.setTotaleIVA(totaleIVA);
//			riepilogoUnico.setTotaleTotale(totaleTotale);
//			riepilogoUnico.setTotaleProgressivoImponibile(totaleProgressivoImponibile);
//			riepilogoUnico.setTotaleProgressivoIva(totaleProgressivoIva);
//			riepilogoUnico.setTotaleTotaleProgressivo(totaleTotaleProgressivo);
//			
//			riepilogoUnico.setTotaleImponibileDetraibile(totaleImponibileDetraibile);
//			riepilogoUnico.setTotaleImponibileEsente(totaleImponibileEsente);
//			riepilogoUnico.setTotaleImponibileIndetraibile(totaleImponibileIndetraibile);
//			riepilogoUnico.setTotaleIVADetraibile(totaleIVADetraibile);
//			riepilogoUnico.setTotaleIVAIndetraibile(totaleIVAIndetraibile);
//			riepilogoUnico.setTotaleProgressivoImponibileIndetraibile(totaleProgressivoImponibileIndetraibile);
//			riepilogoUnico.setTotaleProgressivoImponibileDetraibile(totaleProgressivoImponibileDetraibile);
//			riepilogoUnico.setTotaleProgressivoImponibileEsente(totaleProgressivoImponibileEsente);
//			riepilogoUnico.setTotaleProgressivoIvaIndetraibile(totaleProgressivoIvaIndetraibile);
//			riepilogoUnico.setTotaleProgressivoIvaDetraibile(totaleProgressivoIvaDetraibile);
//			
//			listaDefinitiva.add(riepilogoUnico);
//			
//			return listaDefinitiva;
//		}
//		
	
		@Test 
		public void ottieniSubdocumenti(){
			
		}
}
