/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.test.business.carta;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.business.service.carta.AggiornaCartaContabileService;
import it.csi.siac.siacfinser.business.service.carta.InserisceCartaContabileService;
import it.csi.siac.siacfinser.business.service.carta.RegolarizzaCartaContabileService;
import it.csi.siac.siacfinser.business.service.carta.RicercaCartaContabilePerChiaveService;
import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.StampaRiepilogoCartaContabileService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.StampaRiepilogoCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.StampaRiepilogoCartaContabileResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;

public class CartaContabileTest extends BaseJunit4TestCase {

	@Autowired
	private RicercaCartaContabilePerChiaveService ricercaCartaContabilePerChiaveService;
	@Autowired
	private RegolarizzaCartaContabileService regolarizzaCartaContabileService;
	@Autowired
	private InserisceCartaContabileService inserisceCartaContabileService;
	@Autowired
	private AggiornaCartaContabileService aggiornaCartaContabileService;
	
	@Autowired
	private StampaRiepilogoCartaContabileService stampaRiepilogoCartaContabileService;
	
	@Test
	public void regolarizzaCartaContabile() {
		CartaContabile cartaContabile = ottieniCartaContabile();
		
		for(PreDocumentoCarta pdc : cartaContabile.getListaPreDocumentiCarta()) {
			SubdocumentoSpesa ss = new SubdocumentoSpesa();
			ss.setImpegno(pdc.getImpegno());
			ss.setImporto(pdc.getImportoDaRegolarizzare());
			pdc.setListaSubDocumentiSpesaCollegati(new ArrayList<SubdocumentoSpesa>());
			pdc.getListaSubDocumentiSpesaCollegati().add(ss);
		}
		
		// Regolarizzazione
		RegolarizzaCartaContabile req = new RegolarizzaCartaContabile();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioTest(131, 2017));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setCartaContabileDaRegolarizzare(cartaContabile);
		
		RegolarizzaCartaContabileResponse res = regolarizzaCartaContabileService.executeService(req);
		assertNotNull(res);
	}

	/**
	 * @return
	 */
	private CartaContabile ottieniCartaContabile() {
		RicercaCartaContabilePerChiave r1 = new RicercaCartaContabilePerChiave();
		r1.setDataOra(new Date());
		r1.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		r1.setEnte(r1.getRichiedente().getAccount().getEnte());
		
		
		r1.setCercaMdpCessionePerChiaveModPag(true);
		
		
		r1.setNumPagina(1);
		r1.setNumRisultatiPerPagina(10);
		RicercaCartaContabileK pRicercaCartaContabileK = new RicercaCartaContabileK();
		pRicercaCartaContabileK.setBilancio(getBilancioTest(131, 2017));
		CartaContabile cartaContabile = new CartaContabile();
		cartaContabile.setNumero(Integer.valueOf(177));
		pRicercaCartaContabileK.setCartaContabile(cartaContabile);
		r1.setpRicercaCartaContabileK(pRicercaCartaContabileK);
		
		RicercaCartaContabilePerChiaveResponse r2 = ricercaCartaContabilePerChiaveService.executeService(r1);
		
		cartaContabile = r2.getCartaContabile();
		return cartaContabile;
	}
	
	private CartaContabile ottieniCartaContabileByFile(String fileName) {
		CartaContabile carta = getTestFileObject(CartaContabile.class, fileName);
		return carta;
	}
	
	@Test
	public void inserisceCartaContabile() {
		StringBuilder sb = new StringBuilder();
		sb.append("<inserisceCartaContabile>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>349</uid>");
		sb.append("            <codice>AC-AMM-29-VTLNLN73T62C129D</codice>");
		sb.append("            <nome>AMMINISTRATORE</nome>");
		sb.append("            <descrizione>CRP-AMMINISTRATORE</descrizione>");
		sb.append("            <indirizzoMail></indirizzoMail>");
		sb.append("            <ente>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>5</uid>");
		sb.append("                <gestioneLivelli>");
		sb.append("                    <entry>");
		sb.append("                        <key>REV_ONERI_DISTINTA_MAN</key>");
		sb.append("                        <value>TRUE</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>GESTIONE_EVASIONE_ORDINI</key>");
		sb.append("                        <value>SENZA_VERIFICA</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>GESTIONE_CONSULTAZIONE_CAP_PRENOTAZIONI</key>");
		sb.append("                        <value>GESTIONE_CONSULTAZIONE_CAP_PRENOTAZIONI</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>GESTIONE_CONVALIDA_AUTOMATICA</key>");
		sb.append("                        <value>CONVALIDA_MANUALE</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>REV_ONERI_CONTO_MAN</key>");
		sb.append("                        <value>TRUE</value>");
		sb.append("                    </entry>");
		sb.append("                </gestioneLivelli>");
		sb.append("                <nome>Consiglio Regionale Piemonte</nome>");
		sb.append("            </ente>");
		sb.append("        </account>");
		sb.append("        <operatore>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <codiceFiscale>AAAAAA00A11E000M</codiceFiscale>");
		sb.append("            <cognome>Montuori</cognome>");
		sb.append("            <nome>Raffaela</nome>");
		sb.append("        </operatore>");
		sb.append("    </richiedente>");
		sb.append("    <numPagina>0</numPagina>");
		sb.append("    <numRisultatiPerPagina>0</numRisultatiPerPagina>");
		sb.append("    <bilancio>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>143</uid>");
		sb.append("        <anno>2017</anno>");
		sb.append("    </bilancio>");
		sb.append("    <cartaContabile>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>0</uid>");
		sb.append("        <attoAmministrativo>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>41206</uid>");
		sb.append("            <anno>2017</anno>");
		sb.append("            <numero>10101</numero>");
		sb.append("            <parereRegolaritaContabile>false</parereRegolaritaContabile>");
		sb.append("            <statoOperativo>DEFINITIVO</statoOperativo>");
		sb.append("            <strutturaAmmContabile>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>62958249</uid>");
		sb.append("                <codice>A03</codice>");
		sb.append("                <descrizione>AMMINISTRAZIONE, PERSONALE, SISTEMI INFORMATIVI E CORECOM</descrizione>");
		sb.append("                <livello>0</livello>");
		sb.append("            </strutturaAmmContabile>");
		sb.append("            <tipoAtto>");
		sb.append("                <loginOperazione>admin</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>347</uid>");
		sb.append("                <codice>AD</codice>");
		sb.append("                <descrizione>ATTO DIRIGENZIALE</descrizione>");
		sb.append("                <progressivoAutomatico>false</progressivoAutomatico>");
		sb.append("            </tipoAtto>");
		sb.append("        </attoAmministrativo>");
		sb.append("        <causale></causale>");
		sb.append("        <dataEsecuzionePagamento>2017-09-15T00:00:00+02:00</dataEsecuzionePagamento>");
		sb.append("        <dataScadenza>2017-09-30T00:00:00+02:00</dataScadenza>");
		sb.append("        <importo>1.00</importo>");
		sb.append("        <listaPreDocumentiCarta>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <contoTesoreria>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <codice>0000100</codice>");
		sb.append("            </contoTesoreria>");
		sb.append("            <dataDocumento>2017-09-15T00:00:00+02:00</dataDocumento>");
		sb.append("            <dataEsecuzioneRiga>2017-09-15T00:00:00+02:00</dataEsecuzioneRiga>");
		sb.append("            <descrizione>Quota 1</descrizione>");
		sb.append("            <impegno>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <annoCapitoloOrigine>0</annoCapitoloOrigine>");
		sb.append("                <annoMovimento>2017</annoMovimento>");
		sb.append("                <annoOriginePlur>0</annoOriginePlur>");
		sb.append("                <annoRiaccertato>0</annoRiaccertato>");
		sb.append("                <byPassControlloDodicesimi>false</byPassControlloDodicesimi>");
		sb.append("                <collegatoALiquidazioni>false</collegatoALiquidazioni>");
		sb.append("                <collegatoAOrdinativi>false</collegatoAOrdinativi>");
		sb.append("                <flagAttivaGsa>false</flagAttivaGsa>");
		sb.append("                <flagDaRiaccertamento>false</flagDaRiaccertamento>");
		sb.append("                <numero>565</numero>");
		sb.append("                <numeroArticoloOrigine>0</numeroArticoloOrigine>");
		sb.append("                <numeroCapitoloOrigine>0</numeroCapitoloOrigine>");
		sb.append("                <numeroSubNonAnnullati>0</numeroSubNonAnnullati>");
		sb.append("                <numeroUEBOrigine>0</numeroUEBOrigine>");
		sb.append("                <parereFinanziario>false</parereFinanziario>");
		sb.append("                <presenzaSubNonAnnullati>false</presenzaSubNonAnnullati>");
		sb.append("                <validato>true</validato>");
		sb.append("                <annoFinanziamento>0</annoFinanziamento>");
		sb.append("                <chiaveCapitoloUscitaGestione>0</chiaveCapitoloUscitaGestione>");
		sb.append("                <flagCassaEconomale>false</flagCassaEconomale>");
		sb.append("                <flagFrazionabile>false</flagFrazionabile>");
		sb.append("                <flagPrenotazione>false</flagPrenotazione>");
		sb.append("                <flagPrenotazioneLiquidabile>false</flagPrenotazioneLiquidabile>");
		sb.append("                <flagSDF>false</flagSDF>");
		sb.append("                <frazionabileValorizzato>false</frazionabileValorizzato>");
		sb.append("                <numeroAccFinanziamento>0</numeroAccFinanziamento>");
		sb.append("            </impegno>");
		sb.append("            <importo>1.00</importo>");
		sb.append("            <note></note>");
		sb.append("            <numero>1</numero>");
		sb.append("            <soggetto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <avviso>false</avviso>");
		sb.append("                <codiceSoggetto>7049</codiceSoggetto>");
		sb.append("                <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("                <denominazione>OLIVERO ALESSANDRO</denominazione>");
		sb.append("                <elencoModalitaPagamento>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1000000363</uid>");
		sb.append("                    <associatoA>Soggetto</associatoA>");
		sb.append("                    <cessioneCodModPag>3651</cessioneCodModPag>");
		sb.append("                    <cessioneCodSoggetto>6289</cessioneCodSoggetto>");
		sb.append("                    <codiceModalitaPagamento>2</codiceModalitaPagamento>");
		sb.append("                    <descrizione>Tipo accredito: CSI - cessione dell'incasso - Soggetto ricevente: 6289 - Tipo accredito: CB - BONIFICO</descrizione>");
		sb.append("                    <descrizioneInfo>");
		sb.append("                        <descrizioneArricchita>Soggetto ricevente : 6289 - COSTA RAFFAELE - Tipo accredito: CSI - cessione dell'incasso</descrizioneArricchita>");
		sb.append("                    </descrizioneInfo>");
		sb.append("                    <descrizioneStatoModalitaPagamento>VALIDO</descrizioneStatoModalitaPagamento>");
		sb.append("                    <inModifica>false</inModifica>");
		sb.append("                    <loginCreazione>AC-AMM-29-VTLNLN73T62C129D</loginCreazione>");
		sb.append("                    <loginUltimaModifica>AC-AMM-29-VTLNLN73T62C129D</loginUltimaModifica>");
		sb.append("                    <modalitaAccreditoSoggetto>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>CSI</codice>");
		sb.append("                        <descrizione>cessione dell'incasso</descrizione>");
		sb.append("                        <priorita>0</priorita>");
		sb.append("                    </modalitaAccreditoSoggetto>");
		sb.append("                    <modalitaPagamentoSoggettoCessione2>");
		sb.append("                        <loginOperazione>migr_soggetti</loginOperazione>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>3651</uid>");
		sb.append("                        <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento>");
		sb.append("                        <descrizione>IBAN: IT65P0690646480000000011569 - Tipo accredito: CB - BONIFICO</descrizione>");
		sb.append("                        <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento>");
		sb.append("                        <iban>IT65P0690646480000000011569</iban>");
		sb.append("                        <idStatoModalitaPagamento>22</idStatoModalitaPagamento>");
		sb.append("                        <inModifica>false</inModifica>");
		sb.append("                        <loginCreazione>migr_soggetti</loginCreazione>");
		sb.append("                        <loginModifica>batch_tr_dati</loginModifica>");
		sb.append("                        <modalitaAccreditoSoggetto>");
		sb.append("                            <stato>VALIDO</stato>");
		sb.append("                            <uid>0</uid>");
		sb.append("                            <codice>CB</codice>");
		sb.append("                            <descrizione>BONIFICO</descrizione>");
		sb.append("                            <priorita>0</priorita>");
		sb.append("                        </modalitaAccreditoSoggetto>");
		sb.append("                        <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza>");
		sb.append("                        <note>000000011569</note>");
		sb.append("                        <soggettoCessione>");
		sb.append("                            <stato>VALIDO</stato>");
		sb.append("                            <uid>0</uid>");
		sb.append("                            <avviso>false</avviso>");
		sb.append("                            <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("                            <residenteEstero>false</residenteEstero>");
		sb.append("                            <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("                        </soggettoCessione>");
		sb.append("                    </modalitaPagamentoSoggettoCessione2>");
		sb.append("                    <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza>");
		sb.append("                    <note></note>");
		sb.append("                    <soggettoCessione>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <avviso>false</avviso>");
		sb.append("                        <codiceSoggetto>6289</codiceSoggetto>");
		sb.append("                        <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("                        <denominazione>COSTA RAFFAELE</denominazione>");
		sb.append("                        <residenteEstero>false</residenteEstero>");
		sb.append("                        <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("                    </soggettoCessione>");
		sb.append("                    <tipoAccredito>CSI</tipoAccredito>");
		sb.append("                </elencoModalitaPagamento>");
		sb.append("                <residenteEstero>false</residenteEstero>");
		sb.append("                <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("            </soggetto>");
		sb.append("        </listaPreDocumentiCarta>");
		sb.append("        <motivoUrgenza></motivoUrgenza>");
		sb.append("        <note></note>");
		sb.append("        <oggetto>Test carta contabile CSI</oggetto>");
		sb.append("    </cartaContabile>");
		sb.append("    <ente>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>5</uid>");
		sb.append("        <gestioneLivelli>");
		sb.append("            <entry>");
		sb.append("                <key>REV_ONERI_DISTINTA_MAN</key>");
		sb.append("                <value>TRUE</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>GESTIONE_EVASIONE_ORDINI</key>");
		sb.append("                <value>SENZA_VERIFICA</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>GESTIONE_CONSULTAZIONE_CAP_PRENOTAZIONI</key>");
		sb.append("                <value>GESTIONE_CONSULTAZIONE_CAP_PRENOTAZIONI</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>GESTIONE_CONVALIDA_AUTOMATICA</key>");
		sb.append("                <value>CONVALIDA_MANUALE</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>REV_ONERI_CONTO_MAN</key>");
		sb.append("                <value>TRUE</value>");
		sb.append("            </entry>");
		sb.append("        </gestioneLivelli>");
		sb.append("        <nome>Consiglio Regionale Piemonte</nome>");
		sb.append("    </ente>");
		sb.append("</inserisceCartaContabile>");
		sb.append("");
		InserisceCartaContabile req = JAXBUtility.unmarshall(sb.toString(), InserisceCartaContabile.class);
		InserisceCartaContabileResponse res = inserisceCartaContabileService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaCartaContabile() {
		CartaContabile cartaContabile;
		cartaContabile = ottieniCartaContabile();
//		cartaContabile = ottieniCartaContabileByFile("cartacontabile/CartaContabile-SIAC-6145.xml");
		AggiornaCartaContabile r1 = new AggiornaCartaContabile();
		r1.setDataOra(new Date());
		r1.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		r1.setEnte(r1.getRichiedente().getAccount().getEnte());
		
		r1.setBilancio(getBilancioTest(131, 2017));
		r1.setAnnoBilancio(r1.getBilancio().getAnno());
		
		r1.setCartaContabile(cartaContabile);
		
		AggiornaCartaContabileResponse res = aggiornaCartaContabileService.executeService(r1);
		
		assertNotNull(res);
	}
	
	@Test
	public void stampaCartaContabileTest() {
		Bilancio bilancio = getBilancioTest(133,2018);
		StampaRiepilogoCartaContabile req = new StampaRiepilogoCartaContabile();
		req.setAnnoBilancio(2018);
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn2", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		CartaContabile cartaContabile = new CartaContabile();
		cartaContabile.setBilancio(bilancio);
//		cartaContabile.setNumero(Integer.valueOf(188));
		cartaContabile.setNumero(Integer.valueOf(120));
		req.setCartaContabile(cartaContabile);
		StampaRiepilogoCartaContabileResponse res = stampaRiepilogoCartaContabileService.executeService(req);
		assertNotNull(res);
	}
}
