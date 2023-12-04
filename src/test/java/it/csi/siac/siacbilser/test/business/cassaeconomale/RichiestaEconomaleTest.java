/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cassaeconomale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRendicontoRichiestaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleAnticipoSpesePerMissioneService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleAnticipoSpesePerMissioneService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleAnticipoSpeseService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleRimborsoSpeseService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaRichiestaEconomaleService;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class RichiestaEconomaleTest.
 */
public class RichiestaEconomaleTest extends BaseJunit4TestCase {

	@Autowired
	private InserisceRichiestaEconomaleAnticipoSpeseService inserisceRichiestaEconomaleAnticipoSpeseService;
	@Autowired
	private InserisceRichiestaEconomaleAnticipoSpesePerMissioneService inserisceRichiestaEconomaleAnticipoSpesePerMissioneService;
	@Autowired
	private AggiornaRichiestaEconomaleAnticipoSpesePerMissioneService aggiornaRichiestaEconomaleAnticipoSpesePerMissioneService;
	@Autowired
	private RicercaDettaglioRichiestaEconomaleService ricercaDettaglioRichiestaEconomaleService;
	@Autowired
	private RicercaSinteticaRichiestaEconomaleService ricercaSinteticaRichiestaEconomaleService;
	@Autowired
	private AggiornaRendicontoRichiestaService aggiornaRendicontoRichiestaService;
	
	@Test
	public void inserisceRichiestaEconomaleAnticipoSpese() {

		InserisceRichiestaEconomale req = new InserisceRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		//richiestaEconomale.setEnte(getEnteTest());
		
		req.setRichiestaEconomale(richiestaEconomale);
		

		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleAnticipoSpeseService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void agigornaRichiestaEconomaleAnticipoSpesePerMissione() {
		StringBuilder sb = new StringBuilder();

		sb.append("<aggiornaRichiestaEconomale>");
		sb.append("    <dataOra>2015-02-04T13:06:10.146+01:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <uid>1</uid>");
		sb.append("            <ente>");
		sb.append("                <uid>1</uid>");
		sb.append("            </ente>");
		sb.append("        </account>");
		sb.append("        <operatore>");
		sb.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
		sb.append("            <cognome>AAAAAA00A11B000J</cognome>");
		sb.append("            <nome>Demo</nome>");
		sb.append("        </operatore>");
		sb.append("    </richiedente>");
		sb.append("    <richiestaEconomale>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>51</uid>");
		sb.append("        <cassaEconomale>");
		sb.append("            <loginOperazione>admin</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>8</uid>");
		sb.append("            <codice>2015-001</codice>");
		sb.append("            <descrizione>Cassa economale 1 del 2015</descrizione>");
		sb.append("            <ente>");
		sb.append("                <loginOperazione>admin</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                <gestioneLivelli/>");
		sb.append("                <nome>Città di Torino</nome>");
		sb.append("            </ente>");
		sb.append("            <importiCassaEconomale>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>12</uid>");
		sb.append("                <bilancio>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>16</uid>");
		sb.append("                    <anno>2015</anno>");
		sb.append("                    <faseEStatoAttualeBilancio>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <faseBilancio>ESERCIZIO_PROVVISORIO</faseBilancio>");
		sb.append("                    </faseEStatoAttualeBilancio>");
		sb.append("                </bilancio>");
		sb.append("                <stanziamentoCassaContoCorrente>10000</stanziamentoCassaContoCorrente>");
		sb.append("            </importiCassaEconomale>");
		sb.append("            <numeroContoCorrente>12345678</numeroContoCorrente>");
		sb.append("            <responsabile>SIAC</responsabile>");
		sb.append("            <statoOperativoCassaEconomale>VALIDA</statoOperativoCassaEconomale>");
		sb.append("            <tipoDiCassa>MISTA</tipoDiCassa>");
		sb.append("        </cassaEconomale>");
		sb.append("        <classificatoriGenerici>");
		sb.append("            <loginOperazione>admin</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>453436</uid>");
		sb.append("            <codice>01</codice>");
		sb.append("            <descrizione>Classificatore 51-01</descrizione>");
		sb.append("            <tipoClassificatore>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <codice>CLASSIFICATORE_51</codice>");
		sb.append("                <descrizione>classificatore 1 per Cassa Economale</descrizione>");
		sb.append("            </tipoClassificatore>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <classificatoriGenerici>");
		sb.append("            <loginOperazione>admin</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>453440</uid>");
		sb.append("            <codice>02</codice>");
		sb.append("            <descrizione>Classificatore 52-02</descrizione>");
		sb.append("            <tipoClassificatore>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <codice>CLASSIFICATORE_52</codice>");
		sb.append("                <descrizione>classificatore 2 per Cassa Economale</descrizione>");
		sb.append("            </tipoClassificatore>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <classificatoriGenerici>");
		sb.append("            <loginOperazione>admin</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>453444</uid>");
		sb.append("            <codice>03</codice>");
		sb.append("            <descrizione>Classificatore 53-03</descrizione>");
		sb.append("            <tipoClassificatore>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <codice>CLASSIFICATORE_53</codice>");
		sb.append("                <descrizione>classificatore 3 per Cassa Economale</descrizione>");
		sb.append("            </tipoClassificatore>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <datiTrasfertaMissione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <codice></codice>");
		sb.append("            <dataFine>2015-01-30T00:00:00+01:00</dataFine>");
		sb.append("            <dataInizio>2015-01-21T00:00:00+01:00</dataInizio>");
		sb.append("            <flagEstero>false</flagEstero>");
		sb.append("            <luogo>Marche</luogo>");
		sb.append("            <mezziDiTrasporto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>6</uid>");
		sb.append("            </mezziDiTrasporto>");
		sb.append("            <mezziDiTrasporto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("            </mezziDiTrasporto>");
		sb.append("            <motivo>Eleweb</motivo>");
		sb.append("        </datiTrasfertaMissione>");
		sb.append("        <delegatoAllIncasso>Marchino Alessandro</delegatoAllIncasso>");
		sb.append("        <ente>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <gestioneLivelli>");
		sb.append("                <entry>");
		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                    <value>GESTIONE_UEB</value>");
		sb.append("                </entry>");
		sb.append("            </gestioneLivelli>");
		sb.append("            <nome>Città di Torino</nome>");
		sb.append("        </ente>");
		sb.append("        <giustificativi>");
		sb.append("            <loginOperazione>Demo 21 - Città di Torino</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>34</uid>");
		sb.append("            <dataEmissione>2015-01-30T09:13:22.969+01:00</dataEmissione>");
		sb.append("            <ente>");
		sb.append("                <loginOperazione>admin</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                <gestioneLivelli/>");
		sb.append("                <nome>Città di Torino</nome>");
		sb.append("            </ente>");
		sb.append("            <flagInclusoNelPagamento>true</flagInclusoNelPagamento>");
		sb.append("            <importoGiustificativo>50.00</importoGiustificativo>");
		sb.append("            <tipoGiustificativo>");
		sb.append("                <loginOperazione>Demo 21 - Città di Torino</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>6</uid>");
		sb.append("                <codice>FERR-ANT</codice>");
		sb.append("                <descrizione>Biglietto ferroviario</descrizione>");
		sb.append("                <ente>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>Città di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <percentualeAnticipoMissione>100.00</percentualeAnticipoMissione>");
		sb.append("                <percentualeAnticipoTrasferta>100.00</percentualeAnticipoTrasferta>");
		sb.append("                <statoOperativoTipoGiustificativo>VALIDO</statoOperativoTipoGiustificativo>");
		sb.append("                <tipologiaGiustificativo>ANTICIPO</tipologiaGiustificativo>");
		sb.append("            </tipoGiustificativo>");
		sb.append("            <valuta>");
		sb.append("                <loginOperazione>admin</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>49</uid>");
		sb.append("                <codice>EUR</codice>");
		sb.append("                <descrizione>Euro</descrizione>");
		sb.append("            </valuta>");
		sb.append("        </giustificativi>");
		sb.append("        <giustificativi>");
		sb.append("            <loginOperazione>Demo 21 - Città di Torino</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>35</uid>");
		sb.append("            <dataEmissione>2015-01-30T09:13:22.969+01:00</dataEmissione>");
		sb.append("            <ente>");
		sb.append("                <loginOperazione>admin</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                <gestioneLivelli/>");
		sb.append("                <nome>Città di Torino</nome>");
		sb.append("            </ente>");
		sb.append("            <flagInclusoNelPagamento>true</flagInclusoNelPagamento>");
		sb.append("            <importoGiustificativo>80.00</importoGiustificativo>");
		sb.append("            <tipoGiustificativo>");
		sb.append("                <loginOperazione>Demo 21 - Città di Torino</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>6</uid>");
		sb.append("                <codice>FERR-ANT</codice>");
		sb.append("                <descrizione>Biglietto ferroviario</descrizione>");
		sb.append("                <ente>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>Città di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <percentualeAnticipoMissione>100.00</percentualeAnticipoMissione>");
		sb.append("                <percentualeAnticipoTrasferta>100.00</percentualeAnticipoTrasferta>");
		sb.append("                <statoOperativoTipoGiustificativo>VALIDO</statoOperativoTipoGiustificativo>");
		sb.append("                <tipologiaGiustificativo>ANTICIPO</tipologiaGiustificativo>");
		sb.append("            </tipoGiustificativo>");
		sb.append("            <valuta>");
		sb.append("                <loginOperazione>admin</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>49</uid>");
		sb.append("                <codice>EUR</codice>");
		sb.append("                <descrizione>Euro</descrizione>");
		sb.append("            </valuta>");
		sb.append("        </giustificativi>");
		sb.append("        <impegno>");
		sb.append("            <uid>143</uid>");
		sb.append("        </impegno>");
		sb.append("        <movimento>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>24</uid>");
		sb.append("            <dataMovimento>2015-01-30T00:00:00+01:00</dataMovimento>");
		sb.append("            <modalitaPagamentoCassa>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("            </modalitaPagamentoCassa>");
		sb.append("            <numeroMovimento>8</numeroMovimento>");
		sb.append("        </movimento>");
		sb.append("        <note>Test in aggiornamento</note>");
		sb.append("        <numeroRichiesta>1</numeroRichiesta>");
		sb.append("        <soggetto>");
		sb.append("            <uid>19</uid>");
		sb.append("        </soggetto>");
		sb.append("        <statoOperativoRichiestaEconomale>DA_RENDICONTARE</statoOperativoRichiestaEconomale>");
		sb.append("        <strutturaDiAppartenenza>ProLogic</strutturaDiAppartenenza>");
		sb.append("    </richiestaEconomale>");
		sb.append("</aggiornaRichiestaEconomale>");
		
		AggiornaRichiestaEconomale req = JAXBUtility.unmarshall(sb.toString(), AggiornaRichiestaEconomale.class);
		AggiornaRichiestaEconomaleResponse res = aggiornaRichiestaEconomaleAnticipoSpesePerMissioneService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioRichiestaEconomale() {
		RicercaDettaglioRichiestaEconomale req = new RicercaDettaglioRichiestaEconomale();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setUid(56);
		req.setRichiestaEconomale(richiestaEconomale);
		
		RicercaDettaglioRichiestaEconomaleResponse res = ricercaDettaglioRichiestaEconomaleService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDettaglioRichiestaEconomaleByNumero() {
		RicercaDettaglioRichiestaEconomale req = new RicercaDettaglioRichiestaEconomale();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setNumeroRichiesta(133);
		richiestaEconomale.setBilancio(getBilancio2015Test());
		CassaEconomale cassaEconomale = new CassaEconomale();
		cassaEconomale.setUid(5);
		richiestaEconomale.setCassaEconomale(cassaEconomale);
		
		req.setRichiestaEconomale(richiestaEconomale);
		
		RicercaDettaglioRichiestaEconomaleResponse res = ricercaDettaglioRichiestaEconomaleService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaSinteticaRichiestaEconomale() {
		RicercaSinteticaRichiestaEconomale req = new RicercaSinteticaRichiestaEconomale();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setBilancio(getBilancio2015Test());
		
		Calendar cal = Calendar.getInstance();
		cal.set(2015, 2, 10);
//		Date dataCreazione = cal.getTime();
//		richiestaEconomale.setDataCreazione(dataCreazione);
		
		req.setDataMovimentoDa(cal.getTime());
		req.setDataMovimentoA(cal.getTime());
		
		TipoRichiestaEconomale tipoRichiestaEconomale = new TipoRichiestaEconomale();
		tipoRichiestaEconomale.setUid(3);
		richiestaEconomale.setTipoRichiestaEconomale(tipoRichiestaEconomale);
		
		CassaEconomale cassaEconomale = new CassaEconomale();
		cassaEconomale.setUid(2);
		richiestaEconomale.setCassaEconomale(cassaEconomale);
		
		req.setRichiestaEconomale(richiestaEconomale);
		
		RicercaSinteticaRichiestaEconomaleResponse res = ricercaSinteticaRichiestaEconomaleService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisceRichiestaEconomaleAnticipoSpesePerMissione() {
		StringBuilder sb = new StringBuilder();

		sb.append("<inserisceRichiestaEconomale>");
		sb.append("    <dataOra>2015-02-13T11:38:56.301+01:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <uid>1</uid>");
		sb.append("            <ente>");
		sb.append("                <uid>1</uid>");
		sb.append("            </ente>");
		sb.append("        </account>");
		sb.append("        <operatore>");
		sb.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
		sb.append("        </operatore>");
		sb.append("    </richiedente>");
		sb.append("    <richiestaEconomale>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>0</uid>");
		sb.append("        <bilancio>");
		sb.append("            <uid>16</uid>");
		sb.append("        </bilancio>");
		sb.append("        <cassaEconomale>");
		sb.append("            <uid>8</uid>");
		sb.append("        </cassaEconomale>");
		sb.append("        <classificatoriGenerici>");
		sb.append("		    <uid>453436</uid>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <classificatoriGenerici>");
		sb.append("            <uid>453440</uid>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <classificatoriGenerici>");
		sb.append("            <uid>453444</uid>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <codiceFiscale>MRTSNO62L42H355H</codiceFiscale>");
		sb.append("        <cognome>MARTINI</cognome>");
		sb.append("        <datiTrasfertaMissione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <codice></codice>");
		sb.append("            <dataFine>2015-02-26T00:00:00+01:00</dataFine>");
		sb.append("            <dataInizio>2015-02-05T00:00:00+01:00</dataInizio>");
		sb.append("            <flagEstero>false</flagEstero>");
		sb.append("            <luogo>Marche</luogo>");
		sb.append("            <mezziDiTrasporto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("            </mezziDiTrasporto>");
		sb.append("            <motivo>Eleweb</motivo>");
		sb.append("        </datiTrasfertaMissione>");
		sb.append("        <delegatoAllIncasso>Marchino Alessandro</delegatoAllIncasso>");
		sb.append("        <ente>");
		sb.append("            <uid>1</uid>");
		sb.append("        </ente>");
		sb.append("        <giustificativi>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <dataEmissione>2015-02-13T11:37:16.750+01:00</dataEmissione>");
		sb.append("            <importoGiustificativo>50.00</importoGiustificativo>");
		sb.append("            <importoSpettanteGiustificativo>50.00</importoSpettanteGiustificativo>");
		sb.append("            <tipoGiustificativo>");
		sb.append("                <uid>6</uid>");
		sb.append("			</tipoGiustificativo>");
		sb.append("            <valuta>");
		sb.append("                <uid>49</uid>");
		sb.append("            </valuta>");
		sb.append("        </giustificativi>");
		sb.append("        <giustificativi>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <dataEmissione>2015-02-13T11:37:22.206+01:00</dataEmissione>");
		sb.append("            <importoGiustificativo>80.00</importoGiustificativo>");
		sb.append("            <importoSpettanteGiustificativo>80.00</importoSpettanteGiustificativo>");
		sb.append("            <tipoGiustificativo>");
		sb.append("                <uid>6</uid>");
		sb.append("            </tipoGiustificativo>");
		sb.append("            <valuta>");
		sb.append("                <uid>49</uid>");
		sb.append("            </valuta>");
		sb.append("        </giustificativi>");
		sb.append("        <impegno>");
		sb.append("            <uid>143</uid>");
		sb.append("        </impegno>");
		sb.append("        <matricola>12345</matricola>");
		sb.append("        <movimento>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <bic>53425432</bic>");
		sb.append("            <contoCorrente>4532543252</contoCorrente>");
		sb.append("            <dataMovimento>2015-02-13T00:00:00+01:00</dataMovimento>");
		sb.append("            <dettaglioPagamento>5432532</dettaglioPagamento>");
		sb.append("            <modalitaPagamentoCassa>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("            </modalitaPagamentoCassa>");
		sb.append("            <modalitaPagamentoDipendente>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>4</uid>");
		sb.append("            </modalitaPagamentoDipendente>");
		sb.append("            <modalitaPagamentoSoggetto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>13</uid>");
		sb.append("            </modalitaPagamentoSoggetto>");
		sb.append("        </movimento>");
		sb.append("        <nome>SONIA</nome>");
		sb.append("        <note>Dettaglio VPN</note>");
		sb.append("        <soggetto>");
		sb.append("            <uid>30</uid>");
		sb.append("        </soggetto>");
		sb.append("        <strutturaDiAppartenenza>CSI</strutturaDiAppartenenza>");
		sb.append("    </richiestaEconomale>");
		sb.append("</inserisceRichiestaEconomale>");
		
		InserisceRichiestaEconomale req = JAXBUtility.unmarshall(sb.toString(), InserisceRichiestaEconomale.class);
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleAnticipoSpesePerMissioneService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	
	@Test
	public void impegnoBilDad() {
		Impegno impegno = new Impegno();
		impegno.setUid(74185);
		impegnoBilDad.popolaInformazioniSoggetto(impegno);
		
		assertNotNull(impegno.getSoggetto());
		System.out.println(impegno.getSoggetto().toString());
	}
	
	
	
	
	@Autowired
	private InserisceRichiestaEconomaleRimborsoSpeseService inserisceRichiestaEconomaleRimborsoSpeseService;
	
	@Test
	public void inserisceRichiestaEconomaleRimborsoSpese() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2016);
		cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 12);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		InserisceRichiestaEconomale req = new InserisceRichiestaEconomale();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		req.setRichiestaEconomale(richiestaEconomale);
		
		richiestaEconomale.setBilancio(getBilancio2015Test());
		richiestaEconomale.setEnte(getEnteTest());
		richiestaEconomale.setCodiceFiscale("VTLNLN73T62C129D");
		richiestaEconomale.setCognome("VERDONE");
		richiestaEconomale.setNome("ALBERTA");
		richiestaEconomale.setDescrizioneDellaRichiesta("Ottimizzazione");
		richiestaEconomale.setFlagPagamentoRitenutaSuFattura(Boolean.FALSE);
		richiestaEconomale.setMatricola("456");
		
		Impegno impegno = new Impegno();
		impegno.setUid(43817);
		richiestaEconomale.setImpegno(impegno);
		
		SubImpegno subImpegno = new SubImpegno();
		subImpegno.setUid(94152);
		subImpegno.setNumeroBigDecimal(new BigDecimal("7"));
		richiestaEconomale.setSubImpegno(subImpegno);
		
		CassaEconomale cassaEconomale = new CassaEconomale();
		cassaEconomale.setUid(2);
		richiestaEconomale.setCassaEconomale(cassaEconomale);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(38293);
		richiestaEconomale.setSoggetto(soggetto);
		
		Movimento movimento = new Movimento();
		movimento.setDataMovimento(cal.getTime());
		ModalitaPagamentoCassa mpc = new ModalitaPagamentoCassa();
		mpc.setUid(1);
		movimento.setModalitaPagamentoCassa(mpc);
		ModalitaPagamentoDipendente mpd = new ModalitaPagamentoDipendente();
		mpd.setUid(61);
		movimento.setModalitaPagamentoDipendente(mpd);
		movimento.setDettaglioPagamento("IT76H0103025900000000315296");
		richiestaEconomale.setMovimento(movimento);
		
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		richiestaEconomale.setGiustificativi(giustificativi);
		
		Giustificativo g = new Giustificativo();
		g.setFlagInclusoNelPagamento(Boolean.TRUE);
		g.setImportoGiustificativo(new BigDecimal("1"));
		TipoGiustificativo tg = new TipoGiustificativo();
		tg.setUid(16);
		g.setTipoGiustificativo(tg);
		Valuta v = new Valuta();
		v.setUid(49);
		g.setValuta(v);
		giustificativi.add(g);
		
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleRimborsoSpeseService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaRendicontoRichiesta() {
		AggiornaRendicontoRichiesta req = new AggiornaRendicontoRichiesta();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		req.setRendicontoRichiesta(create(RendicontoRichiesta.class, 76));
		req.getRendicontoRichiesta().setDataRendiconto(parseDate("2017-08-21", "yyyy-MM-dd"));
		req.getRendicontoRichiesta().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getRendicontoRichiesta().setImpegno(create(Impegno.class, 95593));
		req.getRendicontoRichiesta().setImportoIntegrato(BigDecimal.ZERO);
		req.getRendicontoRichiesta().setImportoRestituito(new BigDecimal("70"));
		
		req.getRendicontoRichiesta().setRichiestaEconomale(create(RichiestaEconomale.class, 347));
		req.getRendicontoRichiesta().getRichiestaEconomale().setImporto(new BigDecimal("100"));
		
		req.getRendicontoRichiesta().setMovimento(create(Movimento.class, 517));
		req.getRendicontoRichiesta().getMovimento().setDataMovimento(parseDate("2017-08-21", "yyyy-MM-dd"));
		req.getRendicontoRichiesta().getMovimento().setDettaglioPagamento("123456789012");
		req.getRendicontoRichiesta().getMovimento().setEnte(req.getRichiedente().getAccount().getEnte());
		req.getRendicontoRichiesta().getMovimento().setModalitaPagamentoCassa(create(ModalitaPagamentoCassa.class, 1));
		req.getRendicontoRichiesta().getMovimento().setModalitaPagamentoDipendente(create(ModalitaPagamentoDipendente.class, 37));
		req.getRendicontoRichiesta().getMovimento().setNumeroMovimento(Integer.valueOf(322));
		
		Giustificativo g1 = create(Giustificativo.class, 359);
		g1.setEnte(req.getRichiedente().getAccount().getEnte());
		g1.setFlagInclusoNelPagamento(Boolean.TRUE);
		g1.setImportoGiustificativo(new BigDecimal("50"));
		g1.setNumeroGiustificativo("");
		g1.setRendicontoRichiesta(create(RendicontoRichiesta.class, 76));
		g1.setStatoOperativoGiustificativi(StatoOperativoGiustificativi.VALIDO);
		g1.setTipoGiustificativo(create(TipoGiustificativo.class, 17));
		g1.setValuta(create(Valuta.class, 49));
		req.getRendicontoRichiesta().addGiustificativo(g1);
		
		Giustificativo g2 = create(Giustificativo.class, 0);
		g2.setEnte(req.getRichiedente().getAccount().getEnte());
		g2.setFlagInclusoNelPagamento(Boolean.TRUE);
		g2.setImportoGiustificativo(new BigDecimal("20"));
		g2.setNumeroGiustificativo("");
		g2.setRendicontoRichiesta(create(RendicontoRichiesta.class, 76));
		g2.setStatoOperativoGiustificativi(StatoOperativoGiustificativi.VALIDO);
		g2.setTipoGiustificativo(create(TipoGiustificativo.class, 17));
		g2.setValuta(create(Valuta.class, 49));
		req.getRendicontoRichiesta().addGiustificativo(g2);
		
		AggiornaRendicontoRichiestaResponse res = aggiornaRendicontoRichiestaService.executeService(req);
		assertNotNull(res);
	}
}
