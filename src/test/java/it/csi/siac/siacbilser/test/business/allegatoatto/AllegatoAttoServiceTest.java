/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.allegatoatto;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.allegatoatto.AggiornaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaSinteticaQuoteElencoService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class AllegatoAttoDLTest.
 */
public class AllegatoAttoServiceTest extends BaseJunit4TestCase {
	
	
//	@Autowired
//	private InserisceAllegatoAttoService inserisceAllegatoAttoService;
	@Autowired
	private RicercaSinteticaQuoteElencoService ricercaSinteticaQuoteElencoService;
	
	
	@Autowired
	private AggiornaAllegatoAttoService aggiornaAllegatoAttoService;
	@Autowired
	private RicercaAllegatoAttoService ricercaAllegatoAttoService;
	
	
	
//	@Test
//	public void inserisceAllegatoAtto() {
//			
//		InserisceAllegatoAtto req = new InserisceAllegatoAtto();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		AllegatoAtto aa = new AllegatoAtto();
//		aa = new AllegatoAtto();
//		aa.setAltriAllegati("altri allegati");
//		aa.setAnnotazioni("mie annotazioni");
//		
//		AttoAmministrativo atto = new AttoAmministrativo();
//		atto.setUid(2);
//		aa.setAttoAmministrativo(atto);
//		
//		aa.setCausale("mia causale");
//		aa.setDatiSensibili(Boolean.TRUE);
//		
//		List<DatiSoggettoAllegato> datiSoggettiAllegati = new ArrayList<DatiSoggettoAllegato>();
//		DatiSoggettoAllegato dsa = new DatiSoggettoAllegato();
//		dsa.setCausaleSospensione("mia causale sospensione");
//		dsa.setDataRiattivazione(new Date());
//		
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(3);
//		dsa.setSoggetto(soggetto);
//		
//		datiSoggettiAllegati.add(dsa);
//		aa.setDatiSoggettiAllegati(datiSoggettiAllegati);
//		
//		
//		List<ElencoDocumentiAllegato> elenchiDocumentiAllegato = new ArrayList<ElencoDocumentiAllegato>();
//		ElencoDocumentiAllegato elenco = new ElencoDocumentiAllegato();
//		elenco.setUid(4);
//		elenchiDocumentiAllegato.add(elenco);
//		aa.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
//		
//		aa.setEnte(getEnteTest());
//		
//		aa.setNote("miee noooteeee!!");
//		
//		aa.setPratica("mia pratica!");
//		aa.setResponsabileAmministrativo("mio resp amm");
//		aa.setResponsabileContabile("mio resp cont");
//		aa.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
//		
//		
//		req.setAllegatoAtto(aa);
//		InserisceAllegatoAttoResponse res = inserisceAllegatoAttoService.executeService(req);
//
//		assertNotNull(res);
//	}
//
//	@Test
//	public void ricercaDettaglioAllegatoAtto() {
//		RicercaDettaglioAllegatoAtto req = new RicercaDettaglioAllegatoAtto();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setUid(10);
//		req.setAllegatoAtto(allegatoAtto);
//		RicercaDettaglioAllegatoAttoResponse res = allegatoAttoService.ricercaDettaglioAllegatoAtto(req);
//		assertNotNull(res);
//	}
//
	
	@Test
	public void aggiornaAllegatoAtto() {
		String methodName="aggiornaAllegatoAtto";
		RicercaAllegatoAtto reqAA = new RicercaAllegatoAtto();
		reqAA.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqAA.setParametriPaginazione(getParametriPaginazione(0,1));
		
		AllegatoAtto allegatoAttoPR = new AllegatoAtto();
		allegatoAttoPR.setCausale("PERPAOLODAALESSSIO");
		//allegatoAttoPR.setUid(11325);
		Ente ente = new Ente();
		ente.setUid(2);
		allegatoAttoPR.setEnte(ente);
		reqAA.setAllegatoAtto(allegatoAttoPR);
		
		RicercaAllegatoAttoResponse resAA = ricercaAllegatoAttoService.executeService(reqAA);
		
		log.info(methodName, ">>>" + resAA.getAllegatoAtto().get(0).getCausale());
		
		AggiornaAllegatoAtto reqAAA = new AggiornaAllegatoAtto();
		reqAAA.setDataOra(new Date());
		reqAAA.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		AllegatoAtto allegatoAttoCR = resAA.getAllegatoAtto().get(0);
		allegatoAttoCR.setNote("Esperamo"); 
		reqAAA.setAllegatoAtto(allegatoAttoCR);

		
		
		AggiornaAllegatoAttoResponse resAAA = aggiornaAllegatoAttoService.executeService(reqAAA);
		assertNotNull(resAAA);
		log.info(methodName, "---");
		
	}
	
	@Test
	public void ricercaSinteticaQuoteElenco() {
		//SIAC-5589
		RicercaSinteticaQuoteElenco req = new RicercaSinteticaQuoteElenco();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setUid(11550);
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato); 

		Soggetto soggetto = new Soggetto();
		soggetto.setUid(0);
		soggetto.setCodiceSoggetto("10016");
		req.setSoggetto(soggetto);
		req.setParametriPaginazione(getParametriPaginazioneTest());

		RicercaSinteticaQuoteElencoResponse res = ricercaSinteticaQuoteElencoService.executeService(req);
		
		assertNotNull(res);

	}

//	
//	@Test
//	public void ricercaDettaglioElenco() {
//		RicercaDettaglioElenco req = new RicercaDettaglioElenco();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		ElencoDocumentiAllegato elenco = new ElencoDocumentiAllegato();
//		elenco.setUid(14);
//		req.setElencoDocumentiAllegato(elenco);
//		RicercaDettaglioElencoResponse res = allegatoAttoService.ricercaDettaglioElenco(req);
//		assertNotNull(res);
//	}
//	
//	
//	@Test
//	public void ricercaSinteticaAllegatoAtto() {
//		RicercaAllegatoAtto req = new RicercaAllegatoAtto();
//		req.setParametriPaginazione(getParametriPaginazioneTest());
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setEnte(getEnteTest());
//		// Causale
////		allegatoAtto.setCausale("mi"); // Errore: la causale deve avere almeno tre caratteri
////		allegatoAtto.setCausale("per");
//		// Stato
////		allegatoAtto.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
//		// Atto Amministrativo
////		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
////		attoAmministrativo.setUid(6);
////		allegatoAtto.setAttoAmministrativo(attoAmministrativo);
//		// Soggetto
////		DatiSoggettoAllegato datiSoggettoAllegato = new DatiSoggettoAllegato();
////		Soggetto soggetto = new Soggetto();
////		soggetto.setUid(3);
////		datiSoggettoAllegato.setSoggetto(soggetto);
////		allegatoAtto.getDatiSoggettiAllegati().add(datiSoggettoAllegato);
//		req.setAllegatoAtto(allegatoAtto);
//		
//		// Data scadenza
//		Calendar cal = Calendar.getInstance();
//		cal.set(2013, 9, 31);
//		Date dataScadenzaDa = cal.getTime();
//		cal.set(2018, 10, 1);
//		Date dataScadenzaA = cal.getTime();
//		req.setDataScadenzaDa(dataScadenzaDa);
//		req.setDataScadenzaA(dataScadenzaA);
//		
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(3);
//		req.setSoggetto(soggetto);
//
//		Impegno imp = new Impegno();
//		imp.setUid(1);
//		req.setImpegno(imp);
//		
//		// Elenco
//		//ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
//		//elencoDocumentiAllegato.setUid(19);
//		//req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
//		
//		RicercaAllegatoAttoResponse res = allegatoAttoService.ricercaAllegatoAtto(req);
//		assertNotNull(res);
//	}
//	
//	
//	
//	@Test
//	public void inserisciTantiElenchiService(){
//		
//		inserisciElenco(19,20,21);
//		inserisciElenco(12,16,18);
//		inserisciElenco(54,56);
//		inserisciElenco(53,55,57);
//		inserisciElenco(115,116,117);
//		inserisciElenco(173,174,175);
//		inserisciElenco(200,201,202);
//		
//	}
//	
//	
//	
//	private void inserisciElenco(int... idSubdocs){
//		InserisceElenco req = new InserisceElenco();
//		
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setBilancio(getBilancio2014Test());
//		
//		ElencoDocumentiAllegato e = new ElencoDocumentiAllegato();
//		
//		
//		
////		AllegatoAtto allegatoAtto = new AllegatoAtto();
////		allegatoAtto.setEnte(getEnteTest());
////		allegatoAtto.setUid(2);
////		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
////		attoAmministrativo.setUid(6);
////		allegatoAtto.setAttoAmministrativo(attoAmministrativo);
////		e.setAllegatoAtto(allegatoAtto);
//		
//		e.setSysEsterno("Sono uno da sistema esterno!!");
//		e.setAnnoSysEsterno(2014);
//		
//		e.setAnno(2014);
//		e.setNumero(1);
//		e.setDataTrasmissione(new Date());
//		e.setEnte(getEnteTest());
//		
//		
//		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?, ?>>();
//		
//		for(int id : idSubdocs){
//			SubdocumentoSpesa s1 = new SubdocumentoSpesa();
//			s1.setUid(id);
//			subdocumenti.add(s1);
//		}
//		
//		e.setSubdocumenti(subdocumenti);
//		e.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
//		
//		
//		req.setElencoDocumentiAllegato(e);
//		
//		InserisceElencoResponse res = allegatoAttoService.inserisceElenco(req);
//	}
//	
//	@Test
//	public void inserisceElencoService() {
//		InserisceElenco req = new InserisceElenco();
//	
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setBilancio(getBilancio2014Test());
//		
//		ElencoDocumentiAllegato e = new ElencoDocumentiAllegato();
//		
//		
//		
////		AllegatoAtto allegatoAtto = new AllegatoAtto();
////		allegatoAtto.setEnte(getEnteTest());
////		allegatoAtto.setUid(2);
////		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
////		attoAmministrativo.setUid(6);
////		allegatoAtto.setAttoAmministrativo(attoAmministrativo);
////		e.setAllegatoAtto(allegatoAtto);
//		
//		e.setSysEsterno("Sono uno da sistema esterno!!");
//		e.setAnnoSysEsterno(2014);
//		
//		e.setAnno(2014);
//		e.setNumero(1);
//		e.setDataTrasmissione(new Date());
//		e.setEnte(getEnteTest());
//		
//		
//		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?, ?>>();
//		
//		
//		SubdocumentoSpesa s1 = new SubdocumentoSpesa();
//		s1.setUid(28);
//		subdocumenti.add(s1);
//		
//		SubdocumentoSpesa s2 = new SubdocumentoSpesa();
//		s2.setUid(29);
//		subdocumenti.add(s2);
//		
//		SubdocumentoSpesa s3 = new SubdocumentoSpesa();
//		s3.setUid(30);
//		subdocumenti.add(s3);
//		
//		e.setSubdocumenti(subdocumenti);
//		e.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
//		
//		
//		req.setElencoDocumentiAllegato(e);
//		
//		InserisceElencoResponse res = allegatoAttoService.inserisceElenco(req);
//		assertNotNull(res);
//	}
//	
//	@Test
//	public void inserisciElencoConDocumenti() {
//		
//
//		String reqStr = "<inserisceElenco>" +
//		"    <dataOra>2014-10-15T13:02:58.560+02:00</dataOra>" +
//		"    <richiedente>" +
//		"        <account>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>1</uid>" +
//		"            <nome>Demo 21</nome>" +
//		"            <descrizione>Demo 21 - Città di Torino</descrizione>" +
//		"            <indirizzoMail>email</indirizzoMail>" +
//		"            <ente>" +
//		"                <stato>VALIDO</stato>" +
//		"                <uid>1</uid>" +
//		"                <gestioneLivelli>" +
//		"                    <entry>" +
//		"                        <key>LIVELLO_GESTIONE_BILANCIO</key>" +
//		"                        <value>GESTIONE_UEB</value>" +
//		"                    </entry>" +
//		"                </gestioneLivelli>" +
//		"                <nome>Città di Torino</nome>" +
//		"            </ente>" +
//		"        </account>" +
//		"        <operatore>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>0</uid>" +
//		"            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>" +
//		"            <cognome>AAAAAA00A11B000J</cognome>" +
//		"            <nome>Demo</nome>" +
//		"        </operatore>" +
//		"    </richiedente>" +
//		"    <bilancio>" +
//		"        <stato>VALIDO</stato>" +
//		"        <uid>1</uid>" +
//		"        <anno>2013</anno>" +
//		"    </bilancio>" +
//		"    <elencoDocumentiAllegato>" +
//		"        <stato>VALIDO</stato>" +
//		"        <uid>0</uid>" +
//		"        <subdocumentoEntrata>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>0</uid>" +
//		"            <importo>100.00</importo>" +
//		"            <importoDaDedurre>0</importoDaDedurre>" +
//		"            <accertamento>" +
//		"                <stato>VALIDO</stato>" +
//		"                <uid>2</uid>" +
//		"                <annoCapitoloOrigine>0</annoCapitoloOrigine>" +
//		"                <annoMovimento>2013</annoMovimento>" +
//		"                <annoOriginePlur>0</annoOriginePlur>" +
//		"                <annoRiaccertato>0</annoRiaccertato>" +
//		"                <dataEmissione>2014-07-23T17:30:10+02:00</dataEmissione>" +
//		"                <dataEmissioneSupport>2014-07-23T17:30:10+02:00</dataEmissioneSupport>" +
//		"                <dataModifica>2014-07-23T17:30:10+02:00</dataModifica>" +
//		"                <descrizione>Accertamento di test</descrizione>" +
//		"                <flagDaRiaccertamento>false</flagDaRiaccertamento>" +
//		"                <importoAttuale>300</importoAttuale>" +
//		"                <importoIniziale>300</importoIniziale>" +
//		"                <numero>1</numero>" +
//		"                <numeroArticoloOrigine>0</numeroArticoloOrigine>" +
//		"                <numeroCapitoloOrigine>0</numeroCapitoloOrigine>" +
//		"                <numeroUEBOrigine>0</numeroUEBOrigine>" +
//		"                <soggetto>" +
//		"                    <stato>VALIDO</stato>" +
//		"                    <uid>9</uid>" +
//		"                    <avviso>false</avviso>" +
//		"                    <codiceFiscale>01032450072</codiceFiscale>" +
//		"                    <codiceSoggetto>3</codiceSoggetto>" +
//		"                    <codiceSoggettoNumber>3</codiceSoggettoNumber>" +
//		"                    <controlloSuSoggetto>true</controlloSuSoggetto>" +
//		"                    <dataModifica>2014-07-23T14:41:39.554+02:00</dataModifica>" +
//		"                    <dataStato>2014-07-23T01:00:01+02:00</dataStato>" +
//		"                    <dataValidita>2014-07-23T00:00:00+02:00</dataValidita>" +
//		"                    <denominazione>PISCINA DI PROVA</denominazione>" +
//		"                    <idLegamiSoggettiSuccessivi>2</idLegamiSoggettiSuccessivi>" +
//		"                    <idLegamiSoggettiSuccessivi>1</idLegamiSoggettiSuccessivi>" +
//		"                    <idStatoOperativoAnagrafica>2</idStatoOperativoAnagrafica>" +
//		"                    <idsSoggettiSuccessivi>16</idsSoggettiSuccessivi>" +
//		"                    <idsSoggettiSuccessivi>14</idsSoggettiSuccessivi>" +
//		"                    <loginCreazione>admin</loginCreazione>" +
//		"                    <loginOperazione>admin</loginOperazione>" +
//		"                    <partitaIva>01032450072</partitaIva>" +
//		"                    <residenteEstero>false</residenteEstero>" +
//		"                    <statoOperativo>VALIDO</statoOperativo>" +
//		"                    <uidSoggettoPadre>0</uidSoggettoPadre>" +
//		"                </soggetto>" +
//		"                <soggettoCode>3</soggettoCode>" +
//		"                <tipoMovimento>A</tipoMovimento>" +
//		"                <tipoMovimentoDesc>Accertamento</tipoMovimentoDesc>" +
//		"                <utenteCreazione>admin</utenteCreazione>" +
//		"                <validato>true</validato>" +
//		"                <automatico>false</automatico>" +
//		"                <capitoloEntrataGestione>" +
//		"                    <stato>VALIDO</stato>" +
//		"                    <uid>0</uid>" +
//		"                    <tipoCapitolo>CAPITOLO_ENTRATA_GESTIONE</tipoCapitolo>" +
//		"                    <uidExCapitolo>0</uidExCapitolo>" +
//		"                </capitoloEntrataGestione>" +
//		"                <chiaveCapitoloEntrataGestione>0</chiaveCapitoloEntrataGestione>" +
//		"                <dataStatoOperativoMovimentoGestioneEntrata>2014-07-23T00:00:00+02:00</dataStatoOperativoMovimentoGestioneEntrata>" +
//		"                <descrizioneStatoOperativoMovimentoGestioneEntrata>DEFINITIVO</descrizioneStatoOperativoMovimentoGestioneEntrata>" +
//		"                <disponibilitaIncassare>300</disponibilitaIncassare>" +
//		"                <disponibilitaSubAccertare>300</disponibilitaSubAccertare>" +
//		"                <statoOperativoMovimentoGestioneEntrata>D</statoOperativoMovimentoGestioneEntrata>" +
//		"                <totaleSubAccertamenti>0</totaleSubAccertamenti>" +
//		"            </accertamento>" +
//		"            <subAccertamento>" +
//		"                <stato>VALIDO</stato>" +
//		"                <uid>0</uid>" +
//		"                <annoCapitoloOrigine>0</annoCapitoloOrigine>" +
//		"                <annoMovimento>0</annoMovimento>" +
//		"                <annoOriginePlur>0</annoOriginePlur>" +
//		"                <annoRiaccertato>0</annoRiaccertato>" +
//		"                <flagDaRiaccertamento>false</flagDaRiaccertamento>" +
//		"                <numeroArticoloOrigine>0</numeroArticoloOrigine>" +
//		"                <numeroCapitoloOrigine>0</numeroCapitoloOrigine>" +
//		"                <numeroUEBOrigine>0</numeroUEBOrigine>" +
//		"                <validato>true</validato>" +
//		"                <automatico>false</automatico>" +
//		"                <chiaveCapitoloEntrataGestione>0</chiaveCapitoloEntrataGestione>" +
//		"                <annoAccertamentoPadre>0</annoAccertamentoPadre>" +
//		"            </subAccertamento>" +
//		"        </subdocumentoEntrata>" +
//		"        <allegatoAtto>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>4</uid>" +
//		"        </allegatoAtto>" +
//		"        <anno>2013</anno>" +
//		"        <ente>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>1</uid>" +
//		"            <gestioneLivelli>" +
//		"                <entry>" +
//		"                    <key>LIVELLO_GESTIONE_BILANCIO</key>" +
//		"                    <value>GESTIONE_UEB</value>" +
//		"                </entry>" +
//		"            </gestioneLivelli>" +
//		"            <nome>Città di Torino</nome>" +
//		"        </ente>" +
//		"        <statoOperativoElencoDocumenti>BOZZA</statoOperativoElencoDocumenti>" +
//		"    </elencoDocumentiAllegato>" +
//		"</inserisceElenco>";
//				
//
//		InserisceElenco req = JAXBUtility.unmarshall(reqStr, InserisceElenco.class);
//
//		InserisceElencoResponse res = allegatoAttoService.inserisceElencoConDocumentiConQuote(req);
//		assertNotNull(res);
//	}
//	
//	@Test
//	public void inserisciElenco2(){
//
//		String reqStr = "<inserisceElenco>" +
//		"    <dataOra>2014-10-15T17:38:26.138+02:00</dataOra>" +
//		"    <richiedente>" +
//		"        <account>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>1</uid>" +
//		"            <nome>Demo 21</nome>" +
//		"            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>" +
//		"            <indirizzoMail>email</indirizzoMail>" +
//		"            <ente>" +
//		"                <stato>VALIDO</stato>" +
//		"                <uid>1</uid>" +
//		"                <gestioneLivelli>" +
//		"                    <entry>" +
//		"                        <key>LIVELLO_GESTIONE_BILANCIO</key>" +
//		"                        <value>GESTIONE_UEB</value>" +
//		"                    </entry>" +
//		"                </gestioneLivelli>" +
//		"                <nome>CittÃ  di Torino</nome>" +
//		"            </ente>" +
//		"        </account>" +
//		"        <operatore>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>0</uid>" +
//		"            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>" +
//		"            <cognome>AAAAAA00A11B000J</cognome>" +
//		"            <nome>Demo</nome>" +
//		"        </operatore>" +
//		"    </richiedente>" +
//		"    <bilancio>" +
//		"        <stato>VALIDO</stato>" +
//		"        <uid>1</uid>" +
//		"        <anno>2013</anno>" +
//		"    </bilancio>" +
//		"    <elencoDocumentiAllegato>" +
//		"        <stato>VALIDO</stato>" +
//		"        <uid>0</uid>" +
//		"        <subdocumentoSpesa>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>13</uid>" +
//		"            <importo>0</importo>" +
//		"            <importoDaDedurre>0</importoDaDedurre>" +
//		"        </subdocumentoSpesa>" +
//		"        <allegatoAtto>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>4</uid>" +
//		"        </allegatoAtto>" +
//		"        <anno>2013</anno>" +
//		"        <ente>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>1</uid>" +
//		"            <gestioneLivelli>" +
//		"                <entry>" +
//		"                    <key>LIVELLO_GESTIONE_BILANCIO</key>" +
//		"                    <value>GESTIONE_UEB</value>" +
//		"                </entry>" +
//		"            </gestioneLivelli>" +
//		"            <nome>CittÃ  di Torino</nome>" +
//		"        </ente>" +
//		"        <statoOperativoElencoDocumenti>BOZZA</statoOperativoElencoDocumenti>" +
//		"    </elencoDocumentiAllegato>" +
//		"</inserisceElenco>";
//		
//		InserisceElenco req = JAXBUtility.unmarshall(reqStr, InserisceElenco.class);
//
//		InserisceElencoResponse res = allegatoAttoService.inserisceElenco(req);
//		assertNotNull(res);
//
//	}
//	
//	@Test
//	public void ricercaElenco() {
//		RicercaElenco request = new RicercaElenco();
//		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		request.setDataOra(new Date());
//		request.setParametriPaginazione(getParametriPaginazioneTest());
//		
//		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
//		elencoDocumentiAllegato.setEnte(getEnteTest());
//		elencoDocumentiAllegato.setNumero(2);
//		request.setElencoDocumentiAllegato(elencoDocumentiAllegato);
//		
//		request.setStatiOperativiElencoDocumenti(Arrays.asList(StatoOperativoElencoDocumenti.BOZZA));
//		
//		RicercaElencoResponse response = allegatoAttoService.ricercaElenco(request);
//		assertNotNull(response);
//	}
//	
//	@Test
//	public void associaElenco() {
//		AssociaElenco req = new AssociaElenco();
//		req.setBilancio(getBilancio2014Test());
//		req.setDataOra(new Date());
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
//		elencoDocumentiAllegato.setUid(9);
//		elencoDocumentiAllegato.setEnte(getEnteTest());
//		
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setUid(3);
//		elencoDocumentiAllegato.setAllegatoAtto(allegatoAtto);
//		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
//		
//		AssociaElencoResponse res = allegatoAttoService.associaElenco(req);
//		assertNotNull(res);
//	}
//	
//	@Test
//	public void disassociaElenco() {
//		DisassociaElenco req = new DisassociaElenco();
//		req.setBilancio(getBilancio2014Test());
//		req.setDataOra(new Date());
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
//		elencoDocumentiAllegato.setUid(9);
//		elencoDocumentiAllegato.setEnte(getEnteTest());
//		
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setUid(3);
//		elencoDocumentiAllegato.setAllegatoAtto(allegatoAtto);
//		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
//		
//		DisassociaElencoResponse res = allegatoAttoService.disassociaElenco(req);
//		assertNotNull(res);
//	}
//	
//	@Test
//	public void aggiornaDatiSoggettoAllegatoAtto() {
//		AggiornaDatiSoggettoAllegatoAtto req = new AggiornaDatiSoggettoAllegatoAtto();
//		req.setDataOra(new Date());
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		DatiSoggettoAllegato datiSoggettoAllegato = new DatiSoggettoAllegato();
//		
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setUid(3);
//		datiSoggettoAllegato.setAllegatoAtto(allegatoAtto);
//		
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(6538);
//		datiSoggettoAllegato.setSoggetto(soggetto);
//		
//		datiSoggettoAllegato.setEnte(getEnteTest());
//		
//		datiSoggettoAllegato.setCausaleSospensione("Nessuna vera, solo test");
//		datiSoggettoAllegato.setDataSospensione(new Date());
////		datiSoggettoAllegato.setUid(1);
//		req.setDatiSoggettoAllegato(datiSoggettoAllegato);
//		
//		AggiornaDatiSoggettoAllegatoAttoResponse res = allegatoAttoService.aggiornaDatiSoggettoAllegatoAtto(req);
//		assertNotNull(res);
//	}
//	
//	@Test
//	public void completaAllegatoAtto(){
//		CompletaAllegatoAtto req = new CompletaAllegatoAtto();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setUid(156);
//		allegatoAtto.setEnte(getEnteTest());
//		req.setAllegatoAtto(allegatoAtto);
//		
//		req.setBilancio(getBilancioTest());
//		
//		AsyncServiceRequestWrapper<CompletaAllegatoAtto> reqAsync = new AsyncServiceRequestWrapper<CompletaAllegatoAtto>();
//		reqAsync.setRequest(req);
//		
//		AsyncServiceResponse res = allegatoAttoService.completaAllegatoAttoAsync(reqAsync);
//		assertNotNull(res);
//	}
//	
//	
//	@Test
//	public void annullaAllegatoAtto(){
//
//		String sb = "<annullaAllegatoAtto>" +
//		"    <dataOra>2014-10-29T15:32:58.613+01:00</dataOra>" +
//		"    <richiedente>" +
//		"        <account>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>1</uid>" +
//		"            <nome>Demo 21</nome>" +
//		"            <descrizione>Demo 21 - Città di Torino</descrizione>" +
//		"            <indirizzoMail>email</indirizzoMail>" +
//		"            <ente>" +
//		"                <stato>VALIDO</stato>" +
//		"                <uid>1</uid>" +
//		"                <gestioneLivelli>" +
//		"                    <entry>" +
//		"                        <key>LIVELLO_GESTIONE_BILANCIO</key>" +
//		"                        <value>GESTIONE_UEB</value>" +
//		"                    </entry>" +
//		"                </gestioneLivelli>" +
//		"                <nome>Città di Torino</nome>" +
//		"            </ente>" +
//		"        </account>" +
//		"        <operatore>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>0</uid>" +
//		"            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>" +
//		"            <cognome>AAAAAA00A11B000J</cognome>" +
//		"            <nome>Demo</nome>" +
//		"        </operatore>" +
//		"    </richiedente>" +
//		"    <allegatoAtto>" +
//		"        <stato>VALIDO</stato>" +
//		"        <uid>8</uid>" +
//		"        <ente>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>1</uid>" +
//		"            <gestioneLivelli>" +
//		"                <entry>" +
//		"                    <key>LIVELLO_GESTIONE_BILANCIO</key>" +
//		"                    <value>GESTIONE_UEB</value>" +
//		"                </entry>" +
//		"            </gestioneLivelli>" +
//		"            <nome>Città di Torino</nome>" +
//		"        </ente>" +
//		"    </allegatoAtto>" +
//		"</annullaAllegatoAtto>";
//				
//
//		
//		AnnullaAllegatoAttoResponse res = allegatoAttoService.annullaAllegatoAtto(JAXBUtility.unmarshall(sb, AnnullaAllegatoAtto.class));
//		assertNotNull(res);
//				
//	}
//	
//	
//	
//	@Test
//	public void testAggiornaLiquidazioni(){
//		
//		this.ente = getEnteTest();
//		this.req = new ConvalidaAllegatoAtto();
//		
//		req.setDataOra(new Date());
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		Liquidazione liquidazione = new Liquidazione();
//		liquidazione.setAnnoLiquidazione(2015);
//		liquidazione.setNumeroLiquidazione(new BigDecimal(207));
//		Bilancio bilancio = getBilancio2015Test();
//		
//		RicercaLiquidazionePerChiaveResponse resRLPC= ricercaLiquidazionePerChiave(liquidazione, bilancio);
//		liquidazione = resRLPC.getLiquidazione();
//		if(liquidazione.getImpegno()==null){
//			liquidazione.setImpegno(resRLPC.getImpegno());
//		}
//		if(liquidazione.getCapitoloUscitaGestione()==null){
//			liquidazione.setCapitoloUscitaGestione(resRLPC.getCapitoloUscitaGestione());
//		}
//		
//		//modificaFlagTipoConvalidaEStato(subdoc.getFlagConvalidaManuale(), liquidazione);
//		
//		modificaFlagTipoConvalidaEStato(false, liquidazione);
//		
//		aggiornaLiquidazione(liquidazione, bilancio);
//		
//		RicercaLiquidazionePerChiaveResponse resRLPC2= ricercaLiquidazionePerChiave(liquidazione, bilancio);
//		Liquidazione liquidazione2 = resRLPC2.getLiquidazione();
//		
//	}
//	
//	private void modificaFlagTipoConvalidaEStato(Boolean flagConvalidaManuale, Liquidazione liquidazione) {
//		
//		//Imposto lo stato a VALIDO
//		liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
//		//liquidazione.setStatoOperativoLiquidazioneDaAggiornare(StatoOperativoLiquidazione.VALIDO);//TODO!!!?????bu
//		
//		//Modifico il tipo convalida coerentemente con il subdocumetno
//		if(Boolean.TRUE.equals(flagConvalidaManuale)){
//			liquidazione.setLiqManuale(Constanti.LIQUIDAZIONE_MAUALE);
//		} else {
//			liquidazione.setLiqManuale(Constanti.LIQUIDAZIONE_AUTOMATICA);
//		}
//	}
//	
//	@Autowired
//	private LiquidazioneService liquidazioneService;
//	
//	private RicercaLiquidazionePerChiaveResponse ricercaLiquidazionePerChiave(Liquidazione liquidazione, Bilancio bilancio)	{
//		RicercaLiquidazionePerChiave reqRLPC = new RicercaLiquidazionePerChiave();
//		reqRLPC.setRichiedente(req.getRichiedente());
//		reqRLPC.setEnte(ente);
//		reqRLPC.setDataOra(req.getDataOra());
//		RicercaLiquidazioneK k = new RicercaLiquidazioneK();
//		k.setAnnoEsercizio(bilancio.getAnno());
//		k.setAnnoLiquidazione(liquidazione.getAnnoLiquidazione());
//		k.setBilancio(bilancio);
//		k.setLiquidazione(liquidazione);
//		k.setNumeroLiquidazione(liquidazione.getNumeroLiquidazione());
//		//k.setTipoRicerca(""); //Lascio il default
//		reqRLPC.setpRicercaLiquidazioneK(k);
//		RicercaLiquidazionePerChiaveResponse resRLPC = liquidazioneService.ricercaLiquidazionePerChiave(reqRLPC);
//		checkServiceResponseFallimento(resRLPC);
//		
//		return resRLPC;
//	}
//
//
//	private void aggiornaLiquidazione(Liquidazione liquidazione, Bilancio bilancio) {
//		AggiornaLiquidazione reqAL = new AggiornaLiquidazione();
//		reqAL.setRichiedente(req.getRichiedente());
//		reqAL.setBilancio(bilancio);
//		reqAL.setDataOra(req.getDataOra());
//		reqAL.setEnte(ente);
//		reqAL.setAnnoEsercizio(String.valueOf(bilancio.getAnno()));
//		reqAL.setLiquidazione(liquidazione);
//		
//		
//		AggiornaLiquidazioneResponse resAL = liquidazioneService.aggiornaLiquidazione(reqAL);
//		log.logXmlTypeObject(resAL, "Risposta ottenuta dal servizio AggiornaLiquidazione.");
//		checkServiceResponseFallimento(resAL);
//		
//	}
//	
//
//	private void checkServiceResponseFallimento(ServiceResponse resRLPC) {
//		// TODO Auto-generated method stub
//		
//	}
	
}
