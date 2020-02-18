/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cassaeconomale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleAnticipoPerTrasfertaDipendentiService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleAnticipoSpesePerMissioneService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleAnticipoSpeseService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomalePagamentoFattureService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleRimborsoSpeseService;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.DatiTrasfertaMissione;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class InserisceRichiestaEconomaleTest extends BaseJunit4TestCase {

	@Autowired
	private InserisceRichiestaEconomaleAnticipoPerTrasfertaDipendentiService inserisceRichiestaEconomaleAnticipoPerTrasfertaDipendentiService;
	@Autowired
	private InserisceRichiestaEconomaleAnticipoSpesePerMissioneService inserisceRichiestaEconomaleAnticipoSpesePerMissioneService;
	@Autowired
	private InserisceRichiestaEconomaleAnticipoSpeseService inserisceRichiestaEconomaleAnticipoSpeseService;
	@Autowired
	private InserisceRichiestaEconomaleRimborsoSpeseService inserisceRichiestaEconomaleRimborsoSpeseService;
	@Autowired
	private InserisceRichiestaEconomalePagamentoFattureService inserisceRichiestaEconomalePagamentoFattureService;
	
	
	@Test
	public void inserisceRichiestaEconomaleTrasferta(){
		InserisceRichiestaEconomale req = new InserisceRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RichiestaEconomale richiesta = new RichiestaEconomale();
		
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		richiesta.setStrutturaDiAppartenenza("S");
		
		DatiTrasfertaMissione datiTrasfertaMissione = new DatiTrasfertaMissione();
		datiTrasfertaMissione.setMotivo("voglio andare in trasferta");
		datiTrasfertaMissione.setFlagEstero(Boolean.TRUE);
		datiTrasfertaMissione.setLuogo("Svezia");
		datiTrasfertaMissione.setCodice("");
		datiTrasfertaMissione.setDataInizio(new Date());
		datiTrasfertaMissione.setDataFine(new Date());
		richiesta.setDatiTrasfertaMissione(datiTrasfertaMissione);
		
		req.setRichiestaEconomale(richiesta);
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleAnticipoPerTrasfertaDipendentiService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void inserisceRichiestaEconomaleAnticipoSpese(){
		InserisceRichiestaEconomale req = new InserisceRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		
		req.setRichiestaEconomale(create(RichiestaEconomale.class, 0));
		req.getRichiestaEconomale().setBilancio(getBilancioTest(133, 2018));
		req.setAnnoBilancio(req.getRichiestaEconomale().getBilancio().getAnno());
		req.getRichiestaEconomale().setMatricola("1");
		
//		req.getRichiestaEconomale().setSoggetto(getSoggetto());
		req.getRichiestaEconomale().setImpegno(create(Impegno.class, 102592));
		req.getRichiestaEconomale().setCassaEconomale(create(CassaEconomale.class, 101));
		
		req.getRichiestaEconomale().setImporto(new BigDecimal("0.01"));
		req.getRichiestaEconomale().setDescrizioneDellaRichiesta("SIAC-6265");
		
		req.getRichiestaEconomale().setMovimento(create(Movimento.class, 0));
		req.getRichiestaEconomale().getMovimento().setDataMovimento(new Date());
		req.getRichiestaEconomale().getMovimento().setModalitaPagamentoCassa(create(ModalitaPagamentoCassa.class, 2));
		req.getRichiestaEconomale().getMovimento().setModalitaPagamentoDipendente(create(ModalitaPagamentoDipendente.class, 2));
		req.getRichiestaEconomale().getMovimento().setDettaglioPagamento("mio iban! (dettaglio pagamento)");
		
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleAnticipoSpeseService.executeService(req);
		assertNotNull(res);
	}
	

	@Test
	public void inserisceRichiestaEconomaleMissione(){
		InserisceRichiestaEconomale req = new InserisceRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiesta = new RichiestaEconomale();
		richiesta.setBilancio(getBilancio2015Test());
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		
		DatiTrasfertaMissione datiTrasfertaMissione = new DatiTrasfertaMissione();
		datiTrasfertaMissione.setMotivo("voglio andare in trasferta");
		datiTrasfertaMissione.setFlagEstero(Boolean.TRUE);
		datiTrasfertaMissione.setLuogo("Svezia");
		datiTrasfertaMissione.setDataInizio(new Date());
		datiTrasfertaMissione.setDataFine(new Date());
		datiTrasfertaMissione.setCodice("");
		richiesta.setDatiTrasfertaMissione(datiTrasfertaMissione );
		
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		
		Giustificativo giustificativo1 = new Giustificativo();
		giustificativo1.setDataEmissione(new Date());
		giustificativo1.setImportoGiustificativo(new BigDecimal(300));
		giustificativo1.setFlagInclusoNelPagamento(Boolean.TRUE);
		TipoGiustificativo tipoGiustificativo1 = new TipoGiustificativo();
		tipoGiustificativo1.setUid(1);
		giustificativo1.setTipoGiustificativo(tipoGiustificativo1);
		Valuta valuta1 = new Valuta();
		valuta1.setUid(49);
		giustificativo1.setValuta(valuta1);
		
		
		Giustificativo giustificativo2 = new Giustificativo();
		giustificativo2.setDataEmissione(new Date());
		giustificativo2.setImportoGiustificativo(new BigDecimal(950));
		giustificativo2.setFlagInclusoNelPagamento(Boolean.TRUE);
		TipoGiustificativo tipoGiustificativo2 = new TipoGiustificativo();
		tipoGiustificativo2.setUid(6);
		giustificativo2.setTipoGiustificativo(tipoGiustificativo2);
		Valuta valuta2 = new Valuta();
		valuta2.setUid(49);
		giustificativo2.setValuta(valuta2);
		
		giustificativi.add(giustificativo1);
		giustificativi.add(giustificativo2);
		richiesta.setGiustificativi(giustificativi);
		
		
		req.setRichiestaEconomale(richiesta);
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleAnticipoSpesePerMissioneService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void inserisceRichiestaEconomaleFattura(){
		InserisceRichiestaEconomale req = new InserisceRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiesta = new RichiestaEconomale();
		
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		
		richiesta.setDescrizioneDellaRichiesta("richiesta di pagamento fatture");
		
//		DocumentoSpesa documento = new DocumentoSpesa();
//		documento.setUid(46);
//		documento.setImporto(new BigDecimal(10));
//		richiesta.setDocumentoSpesa(documento);
		
		req.setRichiestaEconomale(richiesta);
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomalePagamentoFattureService.executeService(req);
		assertNotNull(res);
	}

	
	@Test
	public void inserisceRichiestaEconomaleRimborso(){
		InserisceRichiestaEconomale req = new InserisceRichiestaEconomale();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiesta = new RichiestaEconomale();
		
		richiesta.setDescrizioneDellaRichiesta("mi dovete rimborsare un sacco di spese!!!");
		richiesta.setSoggetto(getSoggetto());
		richiesta.setImpegno(getImpegno());
		richiesta.setCassaEconomale(getCassa());
		
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		Giustificativo giustificativo1 = new Giustificativo();
		Giustificativo giustificativo2 = new Giustificativo();
		TipoGiustificativo tipoGiustificativo1 = new TipoGiustificativo();
		tipoGiustificativo1.setUid(1);
		
		TipoGiustificativo tipoGiustificativo2 = new TipoGiustificativo();
		tipoGiustificativo2.setUid(6);
		
		giustificativo1.setDataEmissione(new Date());
		giustificativo1.setImportoGiustificativo(new BigDecimal(300));
		giustificativo1.setFlagInclusoNelPagamento(Boolean.TRUE);
		giustificativo1.setTipoGiustificativo(tipoGiustificativo1);
		
		giustificativo2.setDataEmissione(new Date());
		giustificativo2.setImportoGiustificativo(new BigDecimal(950));
		giustificativo2.setFlagInclusoNelPagamento(Boolean.TRUE);
		giustificativo2.setTipoGiustificativo(tipoGiustificativo2);
		
		giustificativi.add(giustificativo1);
		giustificativi.add(giustificativo2);
		richiesta.setGiustificativi(giustificativi);
		
		req.setRichiestaEconomale(richiesta);
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleRimborsoSpeseService.executeService(req);
		assertNotNull(res);
	}
	

	private CassaEconomale getCassa() {
		CassaEconomale cassa = new CassaEconomale();
		cassa.setUid(1);
		return cassa;
	}

	private Impegno getImpegno() {
		Impegno impegno = new Impegno();
		impegno.setUid(1);
		return impegno;
	}

	private Soggetto getSoggetto() {
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		return soggetto;
	}
	
	@Test
	public void testInserisciRichiestraTrasfertaDipendente(){
		StringBuilder sb =new StringBuilder();
		sb.append("<inserisceRichiestaEconomale>")
				.append("    <dataOra>2015-02-05T10:30:47.550+01:00</dataOra> ")
				.append("    <richiedente>")
				.append("        <account> ")
				.append("            <stato>VALIDO</stato> ")				.append("            <uid>1</uid> ")
				.append("            <nome>Demo 21</nome> ")
				.append("            <descrizione>Demo 21 - Città di Torino</descrizione> ")
				.append("            <indirizzoMail>email</indirizzoMail> ")
				.append("            <ente> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>1</uid> ")
				.append("                <gestioneLivelli> ")
				.append("                    <entry> ")
				.append("                        <key>LIVELLO_GESTIONE_BILANCIO</key> ")
				.append("                        <value>GESTIONE_UEB</value> ")
				.append("                    </entry> ")
				.append("                </gestioneLivelli> ")
				.append("                <nome>Città di Torino</nome> ")
				.append("            </ente> ")
				.append("        </account> ")
				.append("        <operatore> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>0</uid> ")
				.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale> ")
				.append("            <cognome>AAAAAA00A11B000J</cognome> ")
				.append("            <nome>Demo</nome> ")
				.append("        </operatore> ")
				.append("    </richiedente> ")
				.append("    <richiestaEconomale> ")
				
				
				
				
				.append("                 <bilancio> ")
				.append("                    <loginOperazione>admin</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>16</uid> ")
				.append("                    <anno>2015</anno> ")
				.append("                    <faseEStatoAttualeBilancio> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <faseBilancio>ESERCIZIO_PROVVISORIO</faseBilancio> ")
				.append("                    </faseEStatoAttualeBilancio> ")
				.append("                </bilancio> ")
				
				
				
				.append("        <stato>VALIDO</stato> ")
				.append("        <uid>0</uid> ")
				.append("        <cassaEconomale> ")
				.append("            <loginOperazione>admin</loginOperazione> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>8</uid> ")
				.append("            <codice>2015-001</codice> ")
				.append("            <descrizione>Cassa economale 1 del 2015</descrizione> ")
				.append("            <ente> ")
				.append("                <loginOperazione>admin</loginOperazione> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>1</uid> ")
				.append("                <codiceFiscale>aaaa</codiceFiscale> ")
				.append("                <gestioneLivelli/> ")
				.append("                <nome>Città di Torino</nome> ")
				.append("            </ente> ")
				.append("            <importiCassaEconomale> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>12</uid> ")
				.append("                <bilancio> ")
				.append("                    <loginOperazione>admin</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>16</uid> ")
				.append("                    <anno>2015</anno> ")
				.append("                    <faseEStatoAttualeBilancio> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <faseBilancio>ESERCIZIO_PROVVISORIO</faseBilancio> ")
				.append("                    </faseEStatoAttualeBilancio> ")
				.append("                </bilancio> ")
				.append("                <stanziamentoCassaContoCorrente>10000</stanziamentoCassaContoCorrente> ")
				.append("            </importiCassaEconomale> ")
				.append("            <numeroContoCorrente>12345678</numeroContoCorrente> ")
				.append("            <responsabile>SIAC</responsabile> ")
				.append("            <statoOperativoCassaEconomale>VALIDA</statoOperativoCassaEconomale> ")
				.append("            <tipoDiCassa>MISTA</tipoDiCassa> ")
				.append("        </cassaEconomale> ")
				.append("        <classificatoriGenerici> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>453436</uid> ")
				.append("        </classificatoriGenerici> ")
				.append("        <classificatoriGenerici> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>453440</uid> ")
				.append("        </classificatoriGenerici> ")
				.append("        <classificatoriGenerici> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>453444</uid> ")
				.append("        </classificatoriGenerici> ")
				.append("        <datiTrasfertaMissione> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>0</uid> ")
				.append("            <codice></codice> ")
				.append("            <dataFine>2015-02-12T00:00:00+01:00</dataFine> ")
				.append("            <dataInizio>2015-02-11T00:00:00+01:00</dataInizio> ")
				.append("            <flagEstero>false</flagEstero> ")
				.append("            <luogo>123</luogo> ")
				.append("            <mezziDiTrasporto> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>5</uid> ")
				.append("            </mezziDiTrasporto> ")
				.append("            <motivo>1</motivo> ")
				.append("        </datiTrasfertaMissione> ")
				.append("        <delegatoAllIncasso>243</delegatoAllIncasso> ")
				.append("        <ente> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>1</uid> ")
				.append("            <gestioneLivelli> ")
				.append("                <entry> ")
				.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key> ")
				.append("                    <value>GESTIONE_UEB</value> ")
				.append("                </entry> ")
				.append("            </gestioneLivelli> ")
				.append("            <nome>Città di Torino</nome> ")
				.append("        </ente> ")
				.append("        <giustificativi> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>0</uid> ")
				.append("            <dataEmissione>2015-02-05T10:30:47.550+01:00</dataEmissione> ")
				.append("            <importoGiustificativo>24.00</importoGiustificativo> ")
				.append("            <importoSpettanteGiustificativo>2.00</importoSpettanteGiustificativo> ")
				.append("            <tipoGiustificativo> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>3</uid> ")
				.append("            </tipoGiustificativo> ")
				.append("            <valuta> ")
				.append("                <loginOperazione>admin</loginOperazione> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>49</uid> ")
				.append("                <codice>EUR</codice> ")
				.append("                <descrizione>Euro</descrizione> ")
				.append("            </valuta> ")
				.append("        </giustificativi> ")
				.append("        <impegno> ")
				.append("            <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>143</uid> ")
				.append("            <codCofog>01.1</codCofog> ")
				.append("            <codPdc>U.1.01.01.01.005</codPdc> ")
				.append("            <codRicorrenteSpesa>3</codRicorrenteSpesa> ")
				.append("            <codTransazioneEuropeaSpesa>3</codTransazioneEuropeaSpesa> ")
				.append("            <cup></cup> ")
				.append("            <descCofog>Organi esecutivi e legislativi, attività finanziari e fiscali e affari esteri</descCofog> ")
				.append("            <descPdc>Arretrati per anni precedenti corrisposti al personale a tempo determinato</descPdc> ")
				.append("            <descTransazioneEuropeaSpesa>per le spese finanziate da trasferimenti della UE, ivi compresi i programmi di cooperazione territoriale, a decorrere dalla nuova programmazione comunitaria 2014</descTransazioneEuropeaSpesa> ")
				.append("            <idPdc>125551</idPdc> ")
				.append("            <annoCapitoloOrigine>2015</annoCapitoloOrigine> ")
				.append("            <annoMovimento>2015</annoMovimento> ")
				.append("            <annoOriginePlur>0</annoOriginePlur> ")
				.append("            <annoRiaccertato>0</annoRiaccertato> ")
				.append("            <attoAmmAnno>2013</attoAmmAnno> ")
				.append("            <attoAmmNumero>1</attoAmmNumero> ")
				.append("            <attoAmmTipoAtto> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>70</uid> ")
				.append("                <codice>02</codice> ")
				.append("                <descrizione>DETERMINA</descrizione> ")
				.append("            </attoAmmTipoAtto> ")
				.append("            <attoAmministrativo> ")
				.append("                <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>14</uid> ")
				.append("                <anno>2013</anno> ")
				.append("                <ente> ")
				.append("                    <loginOperazione>admin</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>1</uid> ")
				.append("                    <codiceFiscale>aaaa</codiceFiscale> ")
				.append("                    <gestioneLivelli/> ")
				.append("                    <nome>Città di Torino</nome> ")
				.append("                </ente> ")
				.append("                <note></note> ")
				.append("                <numero>1</numero> ")
				.append("                <oggetto>Determina codice 02 per prove su quote documenti</oggetto> ")
				.append("                <statoOperativo>DEFINITIVO</statoOperativo> ")
				.append("                <strutturaAmmContabile> ")
				.append("                    <loginOperazione>admin</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>342</uid> ")
				.append("                    <codice>001</codice> ")
				.append("                    <descrizione>GABINETTO MIO</descrizione> ")
				.append("                    <livello>2</livello> ")
				.append("                    <tipoClassificatore> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>CDC</codice> ")
				.append("                    </tipoClassificatore> ")
				.append("                </strutturaAmmContabile> ")
				.append("                <tipoAtto> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>70</uid> ")
				.append("                    <codice>02</codice> ")
				.append("                    <descrizione>DETERMINA</descrizione> ")
				.append("                </tipoAtto> ")
				.append("            </attoAmministrativo> ")
				.append("            <dataEmissione>2015-01-16T11:09:38.361+01:00</dataEmissione> ")
				.append("            <dataEmissioneSupport>2015-01-16T11:09:38.361+01:00</dataEmissioneSupport> ")
				.append("            <dataModifica>2015-01-16T11:09:38.361+01:00</dataModifica> ")
				.append("            <flagDaRiaccertamento>false</flagDaRiaccertamento> ")
				.append("            <idStrutturaAmministrativa>342</idStrutturaAmministrativa> ")
				.append("            <importoAttuale>1000.00</importoAttuale> ")
				.append("            <importoIniziale>1000.00</importoIniziale> ")
				.append("            <numero>7</numero> ")
				.append("            <numeroArticoloOrigine>0</numeroArticoloOrigine> ")
				.append("            <numeroCapitoloOrigine>0</numeroCapitoloOrigine> ")
				.append("            <numeroUEBOrigine>0</numeroUEBOrigine> ")
				.append("            <soggetto> ")
				.append("                <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>36</uid> ")
				.append("                <avviso>false</avviso> ")
				.append("                <codiceFiscale>CRLVTR59P28L219G</codiceFiscale> ")
				.append("                <codiceSoggetto>15</codiceSoggetto> ")
				.append("                <codiceSoggettoNumber>15</codiceSoggettoNumber> ")
				.append("                <cognome>CAROLLO</cognome> ")
				.append("                <comuneNascita> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>269</uid> ")
				.append("                    <descrizione>Torino</descrizione> ")
				.append("                    <codiceBelfiore>Torino</codiceBelfiore> ")
				.append("                    <codiceIstat>001272</codiceIstat> ")
				.append("                    <nazioneCode>IT</nazioneCode> ")
				.append("                    <nazioneDesc>Italia</nazioneDesc> ")
				.append("                    <provinciaCode>1</provinciaCode> ")
				.append("                    <provinciaDesc>Torino</provinciaDesc> ")
				.append("                    <regioneCode>1</regioneCode> ")
				.append("                    <regioneDesc>PIEMONTE</regioneDesc> ")
				.append("                    <comuneIstatCode>001272</comuneIstatCode> ")
				.append("                </comuneNascita> ")
				.append("                <controlloSuSoggetto>true</controlloSuSoggetto> ")
				.append("                <dataNascita>1959-09-28T00:00:00+01:00</dataNascita> ")
				.append("                <dataStato>2014-12-03T10:18:09.033+01:00</dataStato> ")
				.append("                <dataValidita>2014-12-03T10:18:09.033+01:00</dataValidita> ")
				.append("                <denominazione>CAROLLO VITTORIO</denominazione> ")
				.append("                <elencoClass> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>4</uid> ")
				.append("                    <idSoggClasse>61</idSoggClasse> ")
				.append("                    <idTipoSoggClasse>2</idTipoSoggClasse> ")
				.append("                    <soggettoClasseCode>ASS</soggettoClasseCode> ")
				.append("                    <soggettoClasseDesc>ASSOCIAZIONI CULTURALI</soggettoClasseDesc> ")
				.append("                    <soggettoTipoClasseCode>ND</soggettoTipoClasseCode> ")
				.append("                    <soggettoTipoClasseDesc>Non definito</soggettoTipoClasseDesc> ")
				.append("                </elencoClass> ")
				.append("                <elencoModalitaPagamento> ")
				.append("                    <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>24</uid> ")
				.append("                    <associatoA>Soggetto</associatoA> ")
				.append("                    <codiceFiscaleQuietanzante>CRLVTR59P28L219G</codiceFiscaleQuietanzante> ")
				.append("                    <codiceModalitaPagamento>1</codiceModalitaPagamento> ")
				.append("                    <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento> ")
				.append("                    <comuneNascita> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <descrizione>Torino</descrizione> ")
				.append("                        <nazioneCode>1</nazioneCode> ")
				.append("                    </comuneNascita> ")
				.append("                    <descrizione>CAROLLO VITTORIO CRLVTR59P28L219G</descrizione> ")
				.append("                    <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento> ")
				.append("                    <idStatoModalitaPagamento>7</idStatoModalitaPagamento> ")
				.append("                    <inModifica>false</inModifica> ")
				.append("                    <loginCreazione>Demo 24 - Città di Torino - AMM.</loginCreazione> ")
				.append("                    <loginModifica>Demo 24 - Città di Torino - AMM.</loginModifica> ")
				.append("                    <modalitaAccreditoSoggetto> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>CON</codice> ")
				.append("                        <descrizione>Contanti</descrizione> ")
				.append("                        <priorita>0</priorita> ")
				.append("                    </modalitaAccreditoSoggetto> ")
				.append("                    <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza> ")
				.append("                    <note>tretret re</note> ")
				.append("                    <soggettoQuietanzante>CAROLLO VITTORIO</soggettoQuietanzante> ")
				.append("                </elencoModalitaPagamento> ")
				.append("                <elencoModalitaPagamento> ")
				.append("                    <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>25</uid> ")
				.append("                    <associatoA>Soggetto</associatoA> ")
				.append("                    <codiceFiscaleQuietanzante>CRLVTR59P28L219G</codiceFiscaleQuietanzante> ")
				.append("                    <codiceModalitaPagamento>2</codiceModalitaPagamento> ")
				.append("                    <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento> ")
				.append("                    <comuneNascita> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <descrizione>Torino</descrizione> ")
				.append("                        <nazioneCode>1</nazioneCode> ")
				.append("                    </comuneNascita> ")
				.append("                    <dataNascitaQuietanzante>28/09/1959</dataNascitaQuietanzante> ")
				.append("                    <descrizione>CAROLLO VITTORIO CRLVTR59P28L219G</descrizione> ")
				.append("                    <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento> ")
				.append("                    <idStatoModalitaPagamento>7</idStatoModalitaPagamento> ")
				.append("                    <inModifica>false</inModifica> ")
				.append("                    <loginCreazione>Demo 24 - Città di Torino - AMM.</loginCreazione> ")
				.append("                    <loginModifica>Demo 24 - Città di Torino - AMM.</loginModifica> ")
				.append("                    <modalitaAccreditoSoggetto> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>CON</codice> ")
				.append("                        <descrizione>Contanti</descrizione> ")
				.append("                        <priorita>0</priorita> ")
				.append("                    </modalitaAccreditoSoggetto> ")
				.append("                    <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza> ")
				.append("                    <note>note note note note</note> ")
				.append("                    <soggettoQuietanzante>CAROLLO VITTORIO</soggettoQuietanzante> ")
				.append("                </elencoModalitaPagamento> ")
				.append("                <elencoModalitaPagamento> ")
				.append("                    <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>26</uid> ")
				.append("                    <associatoA>Soggetto</associatoA> ")
				.append("                    <codiceModalitaPagamento>3</codiceModalitaPagamento> ")
				.append("                    <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento> ")
				.append("                    <contoCorrente>1234567</contoCorrente> ")
				.append("                    <descrizione>conto 1234567 </descrizione> ")
				.append("                    <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento> ")
				.append("                    <idStatoModalitaPagamento>7</idStatoModalitaPagamento> ")
				.append("                    <inModifica>false</inModifica> ")
				.append("                    <intestazioneConto>CAROLLO VITTORIO</intestazioneConto> ")
				.append("                    <loginCreazione>Demo 24 - Città di Torino - Decentrato </loginCreazione> ")
				.append("                    <loginModifica>Demo 24 - Città di Torino - AMM.</loginModifica> ")
				.append("                    <modalitaAccreditoSoggetto> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>CBI</codice> ")
				.append("                        <descrizione>Conto Banca d'Italia</descrizione> ")
				.append("                        <priorita>0</priorita> ")
				.append("                    </modalitaAccreditoSoggetto> ")
				.append("                    <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza> ")
				.append("                    <note>note note</note> ")
				.append("                </elencoModalitaPagamento> ")
				.append("                <elencoOneri> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>5</uid> ")
				.append("                    <idDOnere>3</idDOnere> ")
				.append("                    <onereCod>1052</onereCod> ")
				.append("                    <onereDesc>INDENNITA DI ESPROPRIO</onereDesc> ")
				.append("                </elencoOneri> ")
				.append("                <elencoOneri> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>3</uid> ")
				.append("                    <idDOnere>1</idDOnere> ")
				.append("                    <onereCod>1040</onereCod> ")
				.append("                    <onereDesc>RITENUTE ERARIALI PROFESSIONISTI PRESTATORI OPERA ED OCCASIONALI</onereDesc> ")
				.append("                </elencoOneri> ")
				.append("                <elencoOneri> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>4</uid> ")
				.append("                    <idDOnere>2</idDOnere> ")
				.append("                    <onereCod>1045</onereCod> ")
				.append("                    <onereDesc>CONTRIBUTI A SOCIETA</onereDesc> ")
				.append("                </elencoOneri> ")
				.append("                <idLegamiSoggettiPrecedenti>18</idLegamiSoggettiPrecedenti> ")
				.append("                <idStatoOperativoAnagrafica>2</idStatoOperativoAnagrafica> ")
				.append("                <idsSoggettiPrecedenti>7</idsSoggettiPrecedenti> ")
				.append("                <indirizzi> ")
				.append("                    <cap>10100</cap> ")
				.append("                    <denominazione>PO</denominazione> ")
				.append("                    <numeroCivico>1</numeroCivico> ")
				.append("                    <sedime>Via</sedime> ")
				.append("                    <avviso>false</avviso> ")
				.append("                    <checkAvviso>false</checkAvviso> ")
				.append("                    <checkPrincipale>false</checkPrincipale> ")
				.append("                    <codiceNazione>IT</codiceNazione> ")
				.append("                    <comune>Torino</comune> ")
				.append("                    <idComune>001272</idComune> ")
				.append("                    <idTipoIndirizzo>DOMICILIO</idTipoIndirizzo> ")
				.append("                    <idTipoIndirizzoDesc>domicilio fiscale</idTipoIndirizzoDesc> ")
				.append("                    <indirizzoId>30</indirizzoId> ")
				.append("                    <nazione>Italia</nazione> ")
				.append("                    <principale>true</principale> ")
				.append("                    <provincia>TO</provincia> ")
				.append("                </indirizzi> ")
				.append("                <loginControlloPerModifica>Demo 24 - Città di Torino - AMM.</loginControlloPerModifica> ")
				.append("                <loginCreazione>Demo 24 - Città di Torino - AMM.</loginCreazione> ")
				.append("                <loginModifica>Demo 24 - Città di Torino - AMM.</loginModifica> ")
				.append("                <matricola>5435</matricola> ")
				.append("                <nome>VITTORIO</nome> ")
				.append("                <note>NOTE</note> ")
				.append("                <residenteEstero>false</residenteEstero> ")
				.append("                <sesso>MASCHIO</sesso> ")
				.append("                <sessoStringa>MASCHIO</sessoStringa> ")
				.append("                <statoOperativo>VALIDO</statoOperativo> ")
				.append("                <tipoClassificazioneSoggettoId>ASS</tipoClassificazioneSoggettoId> ")
				.append("                <tipoOnereId>1052</tipoOnereId> ")
				.append("                <tipoOnereId>1040</tipoOnereId> ")
				.append("                <tipoOnereId>1045</tipoOnereId> ")
				.append("                <tipoSoggetto> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>15</uid> ")
				.append("                    <soggettoTipoCode>PF</soggettoTipoCode> ")
				.append("                    <soggettoTipoDesc>Persona fisica</soggettoTipoDesc> ")
				.append("                    <soggettoTipoId>1</soggettoTipoId> ")
				.append("                </tipoSoggetto> ")
				.append("                <uidSoggettoPadre>0</uidSoggettoPadre> ")
				.append("            </soggetto> ")
				.append("            <soggettoCode>15</soggettoCode> ")
				.append("            <tipoMovimento>I</tipoMovimento> ")
				.append("            <tipoMovimentoDesc>Impegno</tipoMovimentoDesc> ")
				.append("            <utenteCreazione>Demo 24 - Città di Torino - AMM.</utenteCreazione> ")
				.append("            <utenteModifica>Demo 24 - Città di Torino - AMM.</utenteModifica> ")
				.append("            <validato>true</validato> ")
				.append("            <annoFinanziamento>0</annoFinanziamento> ")
				.append("            <chiaveCapitoloUscitaGestione>5054</chiaveCapitoloUscitaGestione> ")
				.append(" <dataStatoOperativoMovimentoGestioneSpesa>2015-01-16T11:09:38.361+01:00</dataStatoOperativoMovimentoGestioneSpesa> ")
				.append("     <descrizioneStatoOperativoMovimentoGestioneSpesa>DEFINITIVO</descrizioneStatoOperativoMovimentoGestioneSpesa> ")
				.append("            <disponibilitaFinanziare>1000.00</disponibilitaFinanziare> ")
				.append("            <disponibilitaImpegnoModifica>1000.00</disponibilitaImpegnoModifica> ")
				.append("            <disponibilitaLiquidare>0</disponibilitaLiquidare> ")
				.append("            <disponibilitaPagare>1000.00</disponibilitaPagare> ")
				.append("            <disponibilitaSubimpegnare>0</disponibilitaSubimpegnare> ")
				.append("            <disponibilitaVincolare>1000.00</disponibilitaVincolare> ")
				.append("            <numeroAccFinanziamento>0</numeroAccFinanziamento> ")
				.append("            <sommaLiquidazioniDoc>1000.00</sommaLiquidazioniDoc> ")
				.append("            <statoOperativoMovimentoGestioneSpesa>D</statoOperativoMovimentoGestioneSpesa> ")
				.append("            <tipoImpegno> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>24321</uid> ")
				.append("                <codice>SVI</codice> ")
				.append("                <descrizione>Svincolato</descrizione> ")
				.append("            </tipoImpegno> ")
				.append("            <totaleSubImpegni>0</totaleSubImpegni> ")
				.append("            <capitoloUscitaGestione> ")
				.append("                <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>5054</uid> ")
				.append("                <annoCapitolo>2015</annoCapitolo> ")
				.append("                <annoCreazioneCapitolo>2015</annoCreazioneCapitolo> ")
				.append("                <bilancio> ")
				.append("                    <loginOperazione>admin</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>16</uid> ")
				.append("                    <anno>2015</anno> ")
				.append("                    <faseEStatoAttualeBilancio> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <faseBilancio>ESERCIZIO_PROVVISORIO</faseBilancio> ")
				.append("                    </faseEStatoAttualeBilancio> ")
				.append("                </bilancio> ")
				.append("                <categoriaCapitolo> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>2</uid> ")
				.append("                    <codice>STD</codice> ")
				.append("                    <descrizione>Standard</descrizione> ")
				.append("                </categoriaCapitolo> ")
				.append("                <descrizione>DESCR</descrizione> ")
				.append("                <descrizioneArticolo></descrizioneArticolo> ")
				.append("                <elementoPianoDeiConti> ")
				.append("                    <loginOperazione>pentaho_PDC_20141113_1</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>125546</uid> ")
				.append("                    <codice>U.1.01.01.01.000</codice> ")
				.append("                    <descrizione>Retribuzioni in denaro</descrizione> ")
				.append("                    <livello>4</livello> ")
				.append("                    <tipoClassificatore> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>PDC_IV</codice> ")
				.append("                        <descrizione>Quarto livello PDC</descrizione> ")
				.append("                    </tipoClassificatore> ")
				.append("                    <dataFineValiditaElementoPianoDeiConti>2015-12-31T00:00:00+01:00</dataFineValiditaElementoPianoDeiConti> ")
				.append("                </elementoPianoDeiConti> ")
				.append("                <ente> ")
				.append("                    <loginOperazione>admin</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>1</uid> ")
				.append("                    <codiceFiscale>aaaa</codiceFiscale> ")
				.append("                    <gestioneLivelli/> ")
				.append("                    <nome>Città di Torino</nome> ")
				.append("                </ente> ")
				.append("                <flagImpegnabile>true</flagImpegnabile> ")
				.append("                <flagRilevanteIva>false</flagRilevanteIva> ")
				.append("                <importiCapitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:importiCapitoloUG\"> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <annoCompetenza>2015</annoCompetenza> ")
				.append("                    <disponibilitaVariare>1000</disponibilitaVariare> ")
				.append("                    <fondoPluriennaleVinc>100000</fondoPluriennaleVinc> ")
				.append("                    <impegnatoPlur>5000</impegnatoPlur> ")
				.append("                    <stanziamento>1100000.00</stanziamento> ")
				.append("                    <stanziamentoCassa>1100000.00</stanziamentoCassa> ")
				.append("                    <stanziamentoCassaIniziale>100000</stanziamentoCassaIniziale> ")
				.append("                    <stanziamentoIniziale>100000</stanziamentoIniziale> ")
				.append("                    <stanziamentoProposto>100000</stanziamentoProposto> ")
				.append("                    <stanziamentoResiduo>100000</stanziamentoResiduo> ")
				.append("                    <stanziamentoResiduoIniziale>100000</stanziamentoResiduoIniziale> ")
				.append("                    <disponibilitaImpegnare>1000</disponibilitaImpegnare> ")
				.append("                    <disponibilitaImpegnareAnno1>996938.00</disponibilitaImpegnareAnno1> ")
				.append("                    <disponibilitaImpegnareAnno2>0.00</disponibilitaImpegnareAnno2> ")
				.append("                    <disponibilitaImpegnareAnno3>0.00</disponibilitaImpegnareAnno3> ")
				.append("                    <disponibilitaPagare>1000000.00</disponibilitaPagare> ")
				.append("                    <stanziamentoAsset>100000</stanziamentoAsset> ")
				.append("                    <stanziamentoCassaAsset>100000</stanziamentoCassaAsset> ")
				.append("                    <stanziamentoResAsset>100000</stanziamentoResAsset> ")
				.append("                    <totalePagato>1000</totalePagato> ")
				.append("                </importiCapitolo> ")
				.append("                <listaImportiCapitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:importiCapitoloUG\"> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <annoCompetenza>2015</annoCompetenza> ")
				.append("                    <disponibilitaVariare>1000</disponibilitaVariare> ")
				.append("                    <fondoPluriennaleVinc>100000</fondoPluriennaleVinc> ")
				.append("                    <impegnatoPlur>5000</impegnatoPlur> ")
				.append("                    <stanziamento>1100000.00</stanziamento> ")
				.append("                    <stanziamentoCassa>1100000.00</stanziamentoCassa> ")
				.append("                    <stanziamentoCassaIniziale>100000</stanziamentoCassaIniziale> ")
				.append("                    <stanziamentoIniziale>100000</stanziamentoIniziale> ")
				.append("                    <stanziamentoProposto>100000</stanziamentoProposto> ")
				.append("                    <stanziamentoResiduo>100000</stanziamentoResiduo> ")
				.append("                    <stanziamentoResiduoIniziale>100000</stanziamentoResiduoIniziale> ")
				.append("                    <disponibilitaImpegnare>1000</disponibilitaImpegnare> ")
				.append("                    <disponibilitaImpegnareAnno1>996938.00</disponibilitaImpegnareAnno1> ")
				.append("                    <disponibilitaImpegnareAnno2>0.00</disponibilitaImpegnareAnno2> ")
				.append("                    <disponibilitaImpegnareAnno3>0.00</disponibilitaImpegnareAnno3> ")
				.append("                    <disponibilitaPagare>1000000.00</disponibilitaPagare> ")
				.append("                    <stanziamentoAsset>100000</stanziamentoAsset> ")
				.append("                    <stanziamentoCassaAsset>100000</stanziamentoCassaAsset> ")
				.append("                    <stanziamentoResAsset>100000</stanziamentoResAsset> ")
				.append("                    <totalePagato>1000</totalePagato> ")
				.append("                </listaImportiCapitolo> ")
				.append("                <listaImportiCapitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:importiCapitoloUG\"> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <annoCompetenza>2016</annoCompetenza> ")
				.append("                    <disponibilitaVariare>0</disponibilitaVariare> ")
				.append("                    <fondoPluriennaleVinc>100000</fondoPluriennaleVinc> ")
				.append("                    <impegnatoPlur>0</impegnatoPlur> ")
				.append("                    <stanziamento>100000</stanziamento> ")
				.append("                    <stanziamentoCassa>100000</stanziamentoCassa> ")
				.append("                    <stanziamentoCassaIniziale>100000</stanziamentoCassaIniziale> ")
				.append("                    <stanziamentoIniziale>100000</stanziamentoIniziale> ")
				.append("                    <stanziamentoProposto>100000</stanziamentoProposto> ")
				.append("                    <stanziamentoResiduo>100000</stanziamentoResiduo> ")
				.append("                    <stanziamentoResiduoIniziale>100000</stanziamentoResiduoIniziale> ")
				.append("                    <disponibilitaImpegnare>0</disponibilitaImpegnare> ")
				.append("                    <disponibilitaImpegnareAnno1>0</disponibilitaImpegnareAnno1> ")
				.append("                    <disponibilitaImpegnareAnno2>0</disponibilitaImpegnareAnno2> ")
				.append("                    <disponibilitaImpegnareAnno3>0</disponibilitaImpegnareAnno3> ")
				.append("                    <disponibilitaPagare>0</disponibilitaPagare> ")
				.append("                    <stanziamentoAsset>100000</stanziamentoAsset> ")
				.append("                    <stanziamentoCassaAsset>100000</stanziamentoCassaAsset> ")
				.append("                    <stanziamentoResAsset>100000</stanziamentoResAsset> ")
				.append("                    <totalePagato>0</totalePagato> ")
				.append("                </listaImportiCapitolo> ")
				.append("                <listaImportiCapitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:importiCapitoloUG\"> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <annoCompetenza>2017</annoCompetenza> ")
				.append("                    <disponibilitaVariare>0</disponibilitaVariare> ")
				.append("                    <fondoPluriennaleVinc>100000</fondoPluriennaleVinc> ")
				.append("                    <impegnatoPlur>0</impegnatoPlur> ")
				.append("                    <stanziamento>100000</stanziamento> ")
				.append("                    <stanziamentoCassa>100000</stanziamentoCassa> ")
				.append("                    <stanziamentoCassaIniziale>100000</stanziamentoCassaIniziale> ")
				.append("                    <stanziamentoIniziale>100000</stanziamentoIniziale> ")
				.append("                    <stanziamentoProposto>100000</stanziamentoProposto> ")
				.append("                    <stanziamentoResiduo>100000</stanziamentoResiduo> ")
				.append("                    <stanziamentoResiduoIniziale>100000</stanziamentoResiduoIniziale> ")
				.append("                    <disponibilitaImpegnare>0</disponibilitaImpegnare> ")
				.append("                    <disponibilitaImpegnareAnno1>0</disponibilitaImpegnareAnno1> ")
				.append("                    <disponibilitaImpegnareAnno2>0</disponibilitaImpegnareAnno2> ")
				.append("                    <disponibilitaImpegnareAnno3>0</disponibilitaImpegnareAnno3> ")
				.append("                    <disponibilitaPagare>0</disponibilitaPagare> ")
				.append("                    <stanziamentoAsset>100000</stanziamentoAsset> ")
				.append("                    <stanziamentoCassaAsset>100000</stanziamentoCassaAsset> ")
				.append("                    <stanziamentoResAsset>100000</stanziamentoResAsset> ")
				.append("                    <totalePagato>0</totalePagato> ")
				.append("                </listaImportiCapitolo> ")
				.append("                <note></note> ")
				.append("                <numeroArticolo>1</numeroArticolo> ")
				.append("                <numeroCapitolo>1</numeroCapitolo> ")
				.append("                <numeroUEB>1</numeroUEB> ")
				.append("                <statoOperativoElementoDiBilancio>VALIDO</statoOperativoElementoDiBilancio> ")
				.append("                <strutturaAmministrativoContabile> ")
				.append("                    <loginOperazione>admin</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>346</uid> ")
				.append("                    <codice>005</codice> ")
				.append("                    <descrizione>ECONOMATO</descrizione> ")
				.append("                    <livello>2</livello> ")
				.append("                    <tipoClassificatore> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>CDC</codice> ")
				.append("                        <descrizione>Cdc(Settore)</descrizione> ")
				.append("                    </tipoClassificatore> ")
				.append("                </strutturaAmministrativoContabile> ")
				.append("                <tipoCapitolo>CAPITOLO_USCITA_GESTIONE</tipoCapitolo> ")
				.append("                <uidCapitoloEquivalente>0</uidCapitoloEquivalente> ")
				.append("                <uidExCapitolo>2</uidExCapitolo> ")
				.append("                <classificazioneCofog> ")
				.append("                    <loginOperazione>admin)</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>5276</uid> ")
				.append("                    <codice>01.1</codice> ")
				.append("                    <descrizione>Organi esecutivi e legislativi, attività finanziari e fiscali e affari esteri</descrizione> ")
				.append("                    <livello>2</livello> ")
				.append("                    <tipoClassificatore> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>GRUPPO_COFOG</codice> ")
				.append("                        <descrizione>Gruppo Cofog</descrizione> ")
				.append("                    </tipoClassificatore> ")
				.append("                </classificazioneCofog> ")
				.append("                <flagAssegnabile>false</flagAssegnabile> ")
				.append("                <flagFondoPluriennaleVinc>false</flagFondoPluriennaleVinc> ")
				.append("                <flagFondoSvalutazioneCred>false</flagFondoSvalutazioneCred> ")
				.append("                <flagTrasferimentiOrgComunitari>false</flagTrasferimentiOrgComunitari> ")
				.append("                <funzDelegateRegione>false</funzDelegateRegione> ")
				.append("                <importiCapitoloUG> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <annoCompetenza>2015</annoCompetenza> ")
				.append("                    <disponibilitaVariare>1000</disponibilitaVariare> ")
				.append("                    <fondoPluriennaleVinc>100000</fondoPluriennaleVinc> ")
				.append("                    <impegnatoPlur>5000</impegnatoPlur> ")
				.append("                    <stanziamento>1100000.00</stanziamento> ")
				.append("                    <stanziamentoCassa>1100000.00</stanziamentoCassa> ")
				.append("                    <stanziamentoCassaIniziale>100000</stanziamentoCassaIniziale> ")
				.append("                    <stanziamentoIniziale>100000</stanziamentoIniziale> ")
				.append("                    <stanziamentoProposto>100000</stanziamentoProposto> ")
				.append("                    <stanziamentoResiduo>100000</stanziamentoResiduo> ")
				.append("                    <stanziamentoResiduoIniziale>100000</stanziamentoResiduoIniziale> ")
				.append("                    <disponibilitaImpegnare>1000</disponibilitaImpegnare> ")
				.append("                    <disponibilitaImpegnareAnno1>996938.00</disponibilitaImpegnareAnno1> ")
				.append("                    <disponibilitaImpegnareAnno2>0.00</disponibilitaImpegnareAnno2> ")
				.append("                    <disponibilitaImpegnareAnno3>0.00</disponibilitaImpegnareAnno3> ")
				.append("                    <disponibilitaPagare>1000000.00</disponibilitaPagare> ")
				.append("                    <stanziamentoAsset>100000</stanziamentoAsset> ")
				.append("                    <stanziamentoCassaAsset>100000</stanziamentoCassaAsset> ")
				.append("                    <stanziamentoResAsset>100000</stanziamentoResAsset> ")
				.append("                    <totalePagato>1000</totalePagato> ")
				.append("                </importiCapitoloUG> ")
				.append("                <listaImportiCapitoloUG> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <annoCompetenza>2015</annoCompetenza> ")
				.append("                    <disponibilitaVariare>1000</disponibilitaVariare> ")
				.append("                    <fondoPluriennaleVinc>100000</fondoPluriennaleVinc> ")
				.append("                    <impegnatoPlur>5000</impegnatoPlur> ")
				.append("                    <stanziamento>1100000.00</stanziamento> ")
				.append("                    <stanziamentoCassa>1100000.00</stanziamentoCassa> ")
				.append("                    <stanziamentoCassaIniziale>100000</stanziamentoCassaIniziale> ")
				.append("                    <stanziamentoIniziale>100000</stanziamentoIniziale> ")
				.append("                    <stanziamentoProposto>100000</stanziamentoProposto> ")
				.append("                    <stanziamentoResiduo>100000</stanziamentoResiduo> ")
				.append("                    <stanziamentoResiduoIniziale>100000</stanziamentoResiduoIniziale> ")
				.append("                    <disponibilitaImpegnare>1000</disponibilitaImpegnare> ")
				.append("                    <disponibilitaImpegnareAnno1>996938.00</disponibilitaImpegnareAnno1> ")
				.append("                    <disponibilitaImpegnareAnno2>0.00</disponibilitaImpegnareAnno2> ")
				.append("                    <disponibilitaImpegnareAnno3>0.00</disponibilitaImpegnareAnno3> ")
				.append("                    <disponibilitaPagare>1000000.00</disponibilitaPagare> ")
				.append("                    <stanziamentoAsset>100000</stanziamentoAsset> ")
				.append("                    <stanziamentoCassaAsset>100000</stanziamentoCassaAsset> ")
				.append("                    <stanziamentoResAsset>100000</stanziamentoResAsset> ")
				.append("                    <totalePagato>1000</totalePagato> ")
				.append("                </listaImportiCapitoloUG> ")
				.append("                <listaImportiCapitoloUG> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <annoCompetenza>2016</annoCompetenza> ")
				.append("                    <disponibilitaVariare>0</disponibilitaVariare> ")
				.append("                    <fondoPluriennaleVinc>100000</fondoPluriennaleVinc> ")
				.append("                    <impegnatoPlur>0</impegnatoPlur> ")
				.append("                    <stanziamento>100000</stanziamento> ")
				.append("                    <stanziamentoCassa>100000</stanziamentoCassa> ")
				.append("                    <stanziamentoCassaIniziale>100000</stanziamentoCassaIniziale> ")
				.append("                    <stanziamentoIniziale>100000</stanziamentoIniziale> ")
				.append("                    <stanziamentoProposto>100000</stanziamentoProposto> ")
				.append("                    <stanziamentoResiduo>100000</stanziamentoResiduo> ")
				.append("                    <stanziamentoResiduoIniziale>100000</stanziamentoResiduoIniziale> ")
				.append("                    <disponibilitaImpegnare>0</disponibilitaImpegnare> ")
				.append("                    <disponibilitaImpegnareAnno1>0</disponibilitaImpegnareAnno1> ")
				.append("                    <disponibilitaImpegnareAnno2>0</disponibilitaImpegnareAnno2> ")
				.append("                    <disponibilitaImpegnareAnno3>0</disponibilitaImpegnareAnno3> ")
				.append("                    <disponibilitaPagare>0</disponibilitaPagare> ")
				.append("                    <stanziamentoAsset>100000</stanziamentoAsset> ")
				.append("                    <stanziamentoCassaAsset>100000</stanziamentoCassaAsset> ")
				.append("                    <stanziamentoResAsset>100000</stanziamentoResAsset> ")
				.append("                    <totalePagato>0</totalePagato> ")
				.append("                </listaImportiCapitoloUG> ")
				.append("                <listaImportiCapitoloUG> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <annoCompetenza>2017</annoCompetenza> ")
				.append("                    <disponibilitaVariare>0</disponibilitaVariare> ")
				.append("                    <fondoPluriennaleVinc>100000</fondoPluriennaleVinc> ")
				.append("                    <impegnatoPlur>0</impegnatoPlur> ")
				.append("                    <stanziamento>100000</stanziamento> ")
				.append("                    <stanziamentoCassa>100000</stanziamentoCassa> ")
				.append("                    <stanziamentoCassaIniziale>100000</stanziamentoCassaIniziale> ")
				.append("                    <stanziamentoIniziale>100000</stanziamentoIniziale> ")
				.append("                    <stanziamentoProposto>100000</stanziamentoProposto> ")
				.append("                    <stanziamentoResiduo>100000</stanziamentoResiduo> ")
				.append("                    <stanziamentoResiduoIniziale>100000</stanziamentoResiduoIniziale> ")
				.append("                    <disponibilitaImpegnare>0</disponibilitaImpegnare> ")
				.append("                    <disponibilitaImpegnareAnno1>0</disponibilitaImpegnareAnno1> ")
				.append("                    <disponibilitaImpegnareAnno2>0</disponibilitaImpegnareAnno2> ")
				.append("                    <disponibilitaImpegnareAnno3>0</disponibilitaImpegnareAnno3> ")
				.append("                    <disponibilitaPagare>0</disponibilitaPagare> ")
				.append("                    <stanziamentoAsset>100000</stanziamentoAsset> ")
				.append("                    <stanziamentoCassaAsset>100000</stanziamentoCassaAsset> ")
				.append("                    <stanziamentoResAsset>100000</stanziamentoResAsset> ")
				.append("                    <totalePagato>0</totalePagato> ")
				.append("                </listaImportiCapitoloUG> ")
				.append("                <macroaggregato> ")
				.append("                    <loginOperazione>pentaho_TM_20141113_1</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>118917</uid> ")
				.append("                    <codice>1010000</codice> ")
				.append("                    <descrizione>Redditi da lavoro dipendente</descrizione> ")
				.append("                    <livello>2</livello> ")
				.append("                    <tipoClassificatore> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>MACROAGGREGATO</codice> ")
				.append("                        <descrizione>Macroaggregato</descrizione> ")
				.append("                    </tipoClassificatore> ")
				.append("                    <dataFineValiditaMacroaggregato>2015-12-31T00:00:00+01:00</dataFineValiditaMacroaggregato> ")
				.append("                </macroaggregato> ")
				.append("                <missione> ")
				.append("                    <loginOperazione>pentaho_MP_20141113_1</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>118306</uid> ")
				.append("                    <codice>01</codice> ")
				.append("                    <descrizione>Servizi istituzionali,  generali e di gestione</descrizione> ")
				.append("                    <livello>1</livello> ")
				.append("                    <tipoClassificatore> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>MISSIONE</codice> ")
				.append("                    </tipoClassificatore> ")
				.append("                    <dataFineValiditaMissione>2015-12-31T00:00:00+01:00</dataFineValiditaMissione> ")
				.append("                </missione> ")
				.append("                <programma> ")
				.append("                    <loginOperazione>pentaho_MP_20141113_1</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>118307</uid> ")
				.append("                    <codice>0101</codice> ")
				.append("                    <descrizione>Organi istituzionali</descrizione> ")
				.append("                    <livello>2</livello> ")
				.append("                    <tipoClassificatore> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>PROGRAMMA</codice> ")
				.append("                        <descrizione>Programma</descrizione> ")
				.append("                    </tipoClassificatore> ")
				.append("                    <dataFineValiditaProgramma>2015-12-31T00:00:00+01:00</dataFineValiditaProgramma> ")
				.append("                </programma> ")
				.append("                <titoloSpesa> ")
				.append("                    <loginOperazione>pentaho_TM_20141113_1</loginOperazione> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>118916</uid> ")
				.append("                    <codice>1</codice> ")
				.append("                    <descrizione>Spese correnti</descrizione> ")
				.append("                    <livello>1</livello> ")
				.append("                    <tipoClassificatore> ")
				.append("                        <stato>VALIDO</stato> ")
				.append("                        <uid>0</uid> ")
				.append("                        <codice>TITOLO_SPESA</codice> ")
				.append("                    </tipoClassificatore> ")
				.append("                    <dataFineValiditaTitoloSpesa>2015-12-31T00:00:00+01:00</dataFineValiditaTitoloSpesa> ")
				.append("                </titoloSpesa> ")
				.append("            </capitoloUscitaGestione> ")
				.append("        </impegno> ")
				.append("        <movimento> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>0</uid> ")
				.append("            <dataMovimento>2015-02-05T00:00:00+01:00</dataMovimento> ")
				.append("            <modalitaPagamentoCassa> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>1</uid> ")
				.append("            </modalitaPagamentoCassa> ")
				.append("        </movimento> ")
				.append("        <note>234</note> ")
				.append("        <soggetto> ")
				.append("            <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("            <stato>VALIDO</stato> ")
				.append("            <uid>36</uid> ")
				.append("            <avviso>false</avviso> ")
				.append("            <codiceFiscale>CRLVTR59P28L219G</codiceFiscale> ")
				.append("            <codiceSoggetto>15</codiceSoggetto> ")
				.append("            <codiceSoggettoNumber>15</codiceSoggettoNumber> ")
				.append("            <cognome>CAROLLO</cognome> ")
				.append("            <comuneNascita> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>269</uid> ")
				.append("                <descrizione>Torino</descrizione> ")
				.append("                <codiceBelfiore>Torino</codiceBelfiore> ")
				.append("                <codiceIstat>001272</codiceIstat> ")
				.append("                <nazioneCode>IT</nazioneCode> ")
				.append("                <nazioneDesc>Italia</nazioneDesc> ")
				.append("                <provinciaCode>1</provinciaCode> ")
				.append("                <provinciaDesc>Torino</provinciaDesc> ")
				.append("                <regioneCode>1</regioneCode> ")
				.append("                <regioneDesc>PIEMONTE</regioneDesc> ")
				.append("                <comuneIstatCode>001272</comuneIstatCode> ")
				.append("            </comuneNascita> ")
				.append("            <controlloSuSoggetto>true</controlloSuSoggetto> ")
				.append("            <dataNascita>1959-09-28T00:00:00+01:00</dataNascita> ")
				.append("            <dataStato>2014-12-03T10:18:09.033+01:00</dataStato> ")
				.append("            <dataValidita>2014-12-03T10:18:09.033+01:00</dataValidita> ")
				.append("            <denominazione>CAROLLO VITTORIO</denominazione> ")
				.append("            <elencoClass> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>4</uid> ")
				.append("                <idSoggClasse>61</idSoggClasse> ")
				.append("                <idTipoSoggClasse>2</idTipoSoggClasse> ")
				.append("                <soggettoClasseCode>ASS</soggettoClasseCode> ")
				.append("                <soggettoClasseDesc>ASSOCIAZIONI CULTURALI</soggettoClasseDesc> ")
				.append("                <soggettoTipoClasseCode>ND</soggettoTipoClasseCode> ")
				.append("                <soggettoTipoClasseDesc>Non definito</soggettoTipoClasseDesc> ")
				.append("            </elencoClass> ")
				.append("            <elencoOneri> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>5</uid> ")
				.append("                <idDOnere>3</idDOnere> ")
				.append("                <onereCod>1052</onereCod> ")
				.append("                <onereDesc>INDENNITA DI ESPROPRIO</onereDesc> ")
				.append("            </elencoOneri> ")
				.append("            <elencoOneri> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>3</uid> ")
				.append("                <idDOnere>1</idDOnere> ")
				.append("                <onereCod>1040</onereCod> ")
				.append("                <onereDesc>RITENUTE ERARIALI PROFESSIONISTI PRESTATORI OPERA ED OCCASIONALI</onereDesc> ")
				.append("            </elencoOneri> ")
				.append("            <elencoOneri> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>4</uid> ")
				.append("                <idDOnere>2</idDOnere> ")
				.append("                <onereCod>1045</onereCod> ")
				.append("                <onereDesc>CONTRIBUTI A SOCIETA</onereDesc> ")
				.append("            </elencoOneri> ")
				.append("            <idLegamiSoggettiPrecedenti>18</idLegamiSoggettiPrecedenti> ")
				.append("            <idStatoOperativoAnagrafica>2</idStatoOperativoAnagrafica> ")
				.append("            <idsSoggettiPrecedenti>7</idsSoggettiPrecedenti> ")
				.append("            <indirizzi> ")
				.append("                <cap>10100</cap> ")
				.append("                <denominazione>PO</denominazione> ")
				.append("                <numeroCivico>1</numeroCivico> ")
				.append("                <sedime>Via</sedime> ")
				.append("                <avviso>false</avviso> ")
				.append("                <checkAvviso>false</checkAvviso> ")
				.append("                <checkPrincipale>false</checkPrincipale> ")
				.append("                <codiceNazione>IT</codiceNazione> ")
				.append("                <comune>Torino</comune> ")
				.append("                <idComune>001272</idComune> ")
				.append("                <idTipoIndirizzo>DOMICILIO</idTipoIndirizzo> ")
				.append("                <idTipoIndirizzoDesc>domicilio fiscale</idTipoIndirizzoDesc> ")
				.append("                <indirizzoId>30</indirizzoId> ")
				.append("                <nazione>Italia</nazione> ")
				.append("                <principale>true</principale> ")
				.append("                <provincia>TO</provincia> ")
				.append("            </indirizzi> ")
				.append("            <loginControlloPerModifica>Demo 24 - Città di Torino - AMM.</loginControlloPerModifica> ")
				.append("            <loginCreazione>Demo 24 - Città di Torino - AMM.</loginCreazione> ")
				.append("            <loginModifica>Demo 24 - Città di Torino - AMM.</loginModifica> ")
				.append("            <matricola>5435</matricola> ")
				.append("            <modalitaPagamentoList> ")
				.append("                <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>24</uid> ")
				.append("                <associatoA>Soggetto</associatoA> ")
				.append("                <codiceFiscaleQuietanzante>CRLVTR59P28L219G</codiceFiscaleQuietanzante> ")
				.append("                <codiceModalitaPagamento>1</codiceModalitaPagamento> ")
				.append("                <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento> ")
				.append("                <comuneNascita> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <descrizione>Torino</descrizione> ")
				.append("                    <nazioneCode>1</nazioneCode> ")
				.append("                </comuneNascita> ")
				.append("                <descrizione>CAROLLO VITTORIO CRLVTR59P28L219G</descrizione> ")
				.append("                <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento> ")
				.append("                <idStatoModalitaPagamento>7</idStatoModalitaPagamento> ")
				.append("                <inModifica>false</inModifica> ")
				.append("                <loginCreazione>Demo 24 - Città di Torino - AMM.</loginCreazione> ")
				.append("                <loginModifica>Demo 24 - Città di Torino - AMM.</loginModifica> ")
				.append("                <modalitaAccreditoSoggetto> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <codice>CON</codice> ")
				.append("                    <descrizione>Contanti</descrizione> ")
				.append("                    <priorita>0</priorita> ")
				.append("                </modalitaAccreditoSoggetto> ")
				.append("                <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza> ")
				.append("                <note>tretret re</note> ")
				.append("                <soggettoQuietanzante>CAROLLO VITTORIO</soggettoQuietanzante> ")
				.append("            </modalitaPagamentoList> ")
				.append("            <modalitaPagamentoList> ")
				.append("                <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>25</uid> ")
				.append("                <associatoA>Soggetto</associatoA> ")
				.append("                <codiceFiscaleQuietanzante>CRLVTR59P28L219G</codiceFiscaleQuietanzante> ")
				.append("                <codiceModalitaPagamento>2</codiceModalitaPagamento> ")
				.append("                <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento> ")
				.append("                <comuneNascita> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <descrizione>Torino</descrizione> ")
				.append("                    <nazioneCode>1</nazioneCode> ")
				.append("                </comuneNascita> ")
				.append("                <dataNascitaQuietanzante>28/09/1959</dataNascitaQuietanzante> ")
				.append("                <descrizione>CAROLLO VITTORIO CRLVTR59P28L219G</descrizione> ")
				.append("                <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento> ")
				.append("                <idStatoModalitaPagamento>7</idStatoModalitaPagamento> ")
				.append("                <inModifica>false</inModifica> ")
				.append("                <loginCreazione>Demo 24 - Città di Torino - AMM.</loginCreazione> ")
				.append("                <loginModifica>Demo 24 - Città di Torino - AMM.</loginModifica> ")
				.append("                <modalitaAccreditoSoggetto> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <codice>CON</codice> ")
				.append("                    <descrizione>Contanti</descrizione> ")
				.append("                    <priorita>0</priorita> ")
				.append("                </modalitaAccreditoSoggetto> ")
				.append("                <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza> ")
				.append("                <note>note note note note</note> ")
				.append("                <soggettoQuietanzante>CAROLLO VITTORIO</soggettoQuietanzante> ")
				.append("            </modalitaPagamentoList> ")
				.append("            <modalitaPagamentoList> ")
				.append("                <loginOperazione>Demo 24 - Città di Torino - AMM.</loginOperazione> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>26</uid> ")
				.append("                <associatoA>Soggetto</associatoA> ")
				.append("                <codiceModalitaPagamento>3</codiceModalitaPagamento> ")
				.append("                <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento> ")
				.append("                <contoCorrente>1234567</contoCorrente> ")
				.append("                <descrizione>conto 1234567 </descrizione> ")
				.append("                <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento> ")
				.append("                <idStatoModalitaPagamento>7</idStatoModalitaPagamento> ")
				.append("                <inModifica>false</inModifica> ")
				.append("                <intestazioneConto>CAROLLO VITTORIO</intestazioneConto> ")
				.append("                <loginCreazione>Demo 24 - Città di Torino - Decentrato </loginCreazione> ")
				.append("                <loginModifica>Demo 24 - Città di Torino - AMM.</loginModifica> ")
				.append("                <modalitaAccreditoSoggetto> ")
				.append("                    <stato>VALIDO</stato> ")
				.append("                    <uid>0</uid> ")
				.append("                    <codice>CBI</codice> ")
				.append("                    <descrizione>Conto Banca d'Italia</descrizione> ")
				.append("                    <priorita>0</priorita> ")
				.append("                </modalitaAccreditoSoggetto> ")
				.append("                <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza> ")
				.append("                <note>note note</note> ")
				.append("            </modalitaPagamentoList> ")
				.append("            <nome>VITTORIO</nome> ")
				.append("            <note>NOTE</note> ")
				.append("            <residenteEstero>false</residenteEstero> ")
				.append("            <sesso>MASCHIO</sesso> ")
				.append("            <sessoStringa>MASCHIO</sessoStringa> ")
				.append("            <statoOperativo>VALIDO</statoOperativo> ")
				.append("            <tipoClassificazioneSoggettoId>ASS</tipoClassificazioneSoggettoId> ")
				.append("            <tipoOnereId>1052</tipoOnereId> ")
				.append("            <tipoOnereId>1040</tipoOnereId> ")
				.append("            <tipoOnereId>1045</tipoOnereId> ")
				.append("            <tipoSoggetto> ")
				.append("                <stato>VALIDO</stato> ")
				.append("                <uid>15</uid> ")
				.append("                <soggettoTipoCode>PF</soggettoTipoCode> ")
				.append("                <soggettoTipoDesc>Persona fisica</soggettoTipoDesc> ")
				.append("                <soggettoTipoId>1</soggettoTipoId> ")
				.append("            </tipoSoggetto> ")
				.append("            <uidSoggettoPadre>0</uidSoggettoPadre> ")
				.append("        </soggetto> ")
				.append("        <strutturaDiAppartenenza>1</strutturaDiAppartenenza> ")
				.append("    </richiestaEconomale> ")
				.append("</inserisceRichiestaEconomale> ");
		
		InserisceRichiestaEconomale req = JAXBUtility.unmarshall(sb.toString(), InserisceRichiestaEconomale.class);
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleAnticipoPerTrasfertaDipendentiService.executeService(req);
		assertNotNull(res);


	}
	@Test
	public void inserisciRichiestaRimborsoSpese(){
		StringBuilder sb = new StringBuilder();
		sb.append("<inserisceRichiestaEconomale>");
		sb.append("    <dataOra>2015-09-23T10:28:21.930+02:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <nome>Demo 21</nome>");
		sb.append("            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>");
		sb.append("            <indirizzoMail>email</indirizzoMail>");
		sb.append("            <ente>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <gestioneLivelli>");
		sb.append("                    <entry>");
		sb.append("                        <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                        <value>GESTIONE_UEB</value>");
		sb.append("                    </entry>");
		sb.append("                </gestioneLivelli>");
		sb.append("                <nome>CittÃ  di Torino</nome>");
		sb.append("            </ente>");
		sb.append("        </account>");
		sb.append("        <operatore>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
		sb.append("            <cognome>AAAAAA00A11B000J</cognome>");
		sb.append("            <nome>Demo</nome>");
		sb.append("        </operatore>");
		sb.append("    </richiedente>");
		sb.append("    <richiestaEconomale>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>0</uid>");
		sb.append("        <giustificativi>");
		sb.append("            <giustificativo>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <dataEmissione>2015-09-23T00:00:00+02:00</dataEmissione>");
		sb.append("                <flagInclusoNelPagamento>true</flagInclusoNelPagamento>");
		sb.append("                <importoGiustificativo>10.00</importoGiustificativo>");
		sb.append("                <numeroGiustificativo></numeroGiustificativo>");
		sb.append("                <tipoGiustificativo>");
		sb.append("                    <loginOperazione>Demo 24 - CittÃ  di Torino - AMM.</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>17</uid>");
		sb.append("                    <codice>5</codice>");
		sb.append("                    <descrizione>ricevuta fiscale</descrizione>");
		sb.append("                    <cassaEconomale>");
		sb.append("                        <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>2</uid>");
		sb.append("                        <codice>02</codice>");
		sb.append("                        <descrizione>CASSA GESTIONE FATTUREEEEEEEEE</descrizione>");
		sb.append("                        <ente>");
		sb.append("                            <loginOperazione>admin</loginOperazione>");
		sb.append("                            <stato>VALIDO</stato>");
		sb.append("                            <uid>1</uid>");
		sb.append("                            <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                            <gestioneLivelli/>");
		sb.append("                            <nome>CittÃ  di Torino</nome>");
		sb.append("                        </ente>");
		sb.append("                    </cassaEconomale>");
		sb.append("                    <ente>");
		sb.append("                        <loginOperazione>admin</loginOperazione>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>1</uid>");
		sb.append("                        <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                        <gestioneLivelli/>");
		sb.append("                        <nome>CittÃ  di Torino</nome>");
		sb.append("                    </ente>");
		sb.append("                    <percentualeAnticipoMissione>100.00</percentualeAnticipoMissione>");
		sb.append("                    <percentualeAnticipoTrasferta>100.00</percentualeAnticipoTrasferta>");
		sb.append("                    <statoOperativoTipoGiustificativo>VALIDO</statoOperativoTipoGiustificativo>");
		sb.append("                    <tipologiaGiustificativo>RIMBORSO</tipologiaGiustificativo>");
		sb.append("                </tipoGiustificativo>");
		sb.append("                <valuta>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>49</uid>");
		sb.append("                </valuta>");
		sb.append("            </giustificativo>");
		sb.append("        </giustificativi>");
		sb.append("        <subdocumenti/>");
		sb.append("        <bilancio>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>16</uid>");
		sb.append("            <anno>2015</anno>");
		sb.append("        </bilancio>");
		sb.append("        <cassaEconomale>");
		sb.append("            <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>2</uid>");
		sb.append("            <codice>02</codice>");
		sb.append("            <descrizione>CASSA GESTIONE FATTUREEEEEEEEE</descrizione>");
		sb.append("            <ente>");
		sb.append("                <loginOperazione>admin</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                <gestioneLivelli/>");
		sb.append("                <nome>CittÃ  di Torino</nome>");
		sb.append("            </ente>");
		sb.append("            <limiteImporto>1.00</limiteImporto>");
		sb.append("            <numeroContoCorrente>S8D76F87S68DF</numeroContoCorrente>");
		sb.append("            <operazioniCassaEconomale>");
		sb.append("                <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
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
		sb.append("                <dataOperazione>2015-05-12T00:00:00+02:00</dataOperazione>");
		sb.append("                <ente>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <importo>25000.00</importo>");
		sb.append("                <note></note>");
		sb.append("                <numeroOperazione>3</numeroOperazione>");
		sb.append("            </operazioniCassaEconomale>");
		sb.append("            <operazioniCassaEconomale>");
		sb.append("                <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>2</uid>");
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
		sb.append("                <dataOperazione>2015-09-14T00:00:00+02:00</dataOperazione>");
		sb.append("                <ente>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <importo>100.00</importo>");
		sb.append("                <note></note>");
		sb.append("                <numeroOperazione>4</numeroOperazione>");
		sb.append("            </operazioniCassaEconomale>");
		sb.append("            <operazioniCassaEconomale>");
		sb.append("                <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>3</uid>");
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
		sb.append("                <dataOperazione>2015-09-21T00:00:00+02:00</dataOperazione>");
		sb.append("                <ente>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <importo>100.00</importo>");
		sb.append("                <note></note>");
		sb.append("                <numeroOperazione>5</numeroOperazione>");
		sb.append("            </operazioniCassaEconomale>");
		sb.append("            <operazioniCassaEconomale>");
		sb.append("                <loginOperazione>Demo 24 - CittÃ  di Torino - AMM.</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>4</uid>");
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
		sb.append("                <dataOperazione>2015-09-22T00:00:00+02:00</dataOperazione>");
		sb.append("                <ente>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <importo>150.00</importo>");
		sb.append("                <note></note>");
		sb.append("                <numeroOperazione>6</numeroOperazione>");
		sb.append("            </operazioniCassaEconomale>");
		sb.append("            <responsabile>Verdi GiovanniIIIIIIII</responsabile>");
		sb.append("            <statoOperativoCassaEconomale>VALIDA</statoOperativoCassaEconomale>");
		sb.append("            <tipoDiCassa>MISTA</tipoDiCassa>");
		sb.append("            <variabiliStampa>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <intestazioneCitta>citta cassa 2</intestazioneCitta>");
		sb.append("                <intestazioneDirezione>direzione cassa 2</intestazioneDirezione>");
		sb.append("                <intestazioneSettore>settore cassa 2</intestazioneSettore>");
		sb.append("                <intestazioneUfficio>ufficio cassa 2</intestazioneUfficio>");
		sb.append("            </variabiliStampa>");
		sb.append("        </cassaEconomale>");
		sb.append("        <classificatoriGenerici>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>453442</uid>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <classificatoriGenerici>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>453443</uid>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <classificatoriGenerici>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>453444</uid>");
		sb.append("        </classificatoriGenerici>");
		sb.append("        <codiceFiscale>CHKBLL68A01Z33LP</codiceFiscale>");
		sb.append("        <cognome>ROSSI</cognome>");
		sb.append("        <delegatoAllIncasso></delegatoAllIncasso>");
		sb.append("        <descrizioneDellaRichiesta>spesa di gestione (desc della spesa)</descrizioneDellaRichiesta>");
		sb.append("        <ente>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <gestioneLivelli>");
		sb.append("                <entry>");
		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                    <value>GESTIONE_UEB</value>");
		sb.append("                </entry>");
		sb.append("            </gestioneLivelli>");
		sb.append("            <nome>CittÃ  di Torino</nome>");
		sb.append("        </ente>");
		sb.append("        <impegno>");
		sb.append("            <loginOperazione>Demo 24 - CittÃ  di Torino - AMM.</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>74185</uid>");
		sb.append("            <codCofog>01.1</codCofog>");
		sb.append("            <codPdc>U.1.01.01.01.001</codPdc>");
		sb.append("            <codRicorrenteSpesa>3</codRicorrenteSpesa>");
		sb.append("            <codTransazioneEuropeaSpesa>3</codTransazioneEuropeaSpesa>");
		sb.append("            <cup></cup>");
		sb.append("            <descCofog>Organi esecutivi e legislativi, attivitÃ  finanziari e fiscali e affari esteri</descCofog>");
		sb.append("            <descPdc>Arretrati per anni precedenti corrisposti al personale a tempo indeterminato</descPdc>");
		sb.append("            <descTransazioneEuropeaSpesa>per le spese finanziate da trasferimenti della UE, ivi compresi i programmi di cooperazione territoriale, a decorrere dalla nuova programmazione comunitaria 2014</descTransazioneEuropeaSpesa>");
		sb.append("            <idPdc>125547</idPdc>");
		sb.append("            <annoCapitoloOrigine>2015</annoCapitoloOrigine>");
		sb.append("            <annoMovimento>2015</annoMovimento>");
		sb.append("            <annoOriginePlur>0</annoOriginePlur>");
		sb.append("            <annoRiaccertato>0</annoRiaccertato>");
		sb.append("            <attoAmmAnno>2015</attoAmmAnno>");
		sb.append("            <attoAmmNumero>11</attoAmmNumero>");
		sb.append("            <attoAmmTipoAtto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>27</uid>");
		sb.append("                <codice>DG</codice>");
		sb.append("                <descrizione>DELIBERA DI GIUNTA</descrizione>");
		sb.append("            </attoAmmTipoAtto>");
		sb.append("            <attoAmministrativo>");
		sb.append("                <loginOperazione>Demo 24 - CittÃ  di Torino - AMM.</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>59</uid>");
		sb.append("                <anno>2015</anno>");
		sb.append("                <ente>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <note>note - provvedimento n.11</note>");
		sb.append("                <numero>11</numero>");
		sb.append("                <oggetto>delibera di giunta numero 11</oggetto>");
		sb.append("                <statoOperativo>DEFINITIVO</statoOperativo>");
		sb.append("                <strutturaAmmContabile>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>505</uid>");
		sb.append("                    <codice>004</codice>");
		sb.append("                    <descrizione>VDG SERVIZI AMMINISTRATIVI</descrizione>");
		sb.append("                    <livello>1</livello>");
		sb.append("                    <tipoClassificatore>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>CDR</codice>");
		sb.append("                    </tipoClassificatore>");
		sb.append("                </strutturaAmmContabile>");
		sb.append("                <tipoAtto>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>27</uid>");
		sb.append("                    <codice>DG</codice>");
		sb.append("                    <descrizione>DELIBERA DI GIUNTA</descrizione>");
		sb.append("                </tipoAtto>");
		sb.append("            </attoAmministrativo>");
		sb.append("            <dataEmissione>2015-03-30T15:43:32.484+02:00</dataEmissione>");
		sb.append("            <dataEmissioneSupport>2015-03-30T15:43:32.484+02:00</dataEmissioneSupport>");
		sb.append("            <dataModifica>2015-03-30T15:43:32.484+02:00</dataModifica>");
		sb.append("            <descrizione>DESRIZIONE IMPEGNO - PROVA PER CEC</descrizione>");
		sb.append("            <flagDaRiaccertamento>false</flagDaRiaccertamento>");
		sb.append("            <idStrutturaAmministrativa>505</idStrutturaAmministrativa>");
		sb.append("            <importoAttuale>1000.00</importoAttuale>");
		sb.append("            <importoIniziale>1000.00</importoIniziale>");
		sb.append("            <numero>630</numero>");
		sb.append("            <numeroArticoloOrigine>0</numeroArticoloOrigine>");
		sb.append("            <numeroCapitoloOrigine>0</numeroCapitoloOrigine>");
		sb.append("            <numeroUEBOrigine>0</numeroUEBOrigine>");
		sb.append("            <parereFinanziario>true</parereFinanziario>");
		sb.append("            <soggetto>");
		sb.append("                <loginOperazione>Demo 24 - CittÃ  di Torino - AMM.</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>66</uid>");
		sb.append("                <avviso>false</avviso>");
		sb.append("                <codiceFiscale>VTLNLN73T62C129D</codiceFiscale>");
		sb.append("                <codiceSoggetto>31</codiceSoggetto>");
		sb.append("                <codiceSoggettoNumber>31</codiceSoggettoNumber>");
		sb.append("                <cognome>VITELLI</cognome>");
		sb.append("                <comuneNascita>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>5248</uid>");
		sb.append("                    <descrizione>Castellammare di Stabia</descrizione>");
		sb.append("                    <codiceBelfiore>Castellammare di Stabia</codiceBelfiore>");
		sb.append("                    <codiceIstat>063024</codiceIstat>");
		sb.append("                    <nazioneCode>1</nazioneCode>");
		sb.append("                    <nazioneDesc>Italia</nazioneDesc>");
		sb.append("                    <provinciaCode>63</provinciaCode>");
		sb.append("                    <provinciaDesc>Napoli</provinciaDesc>");
		sb.append("                    <regioneCode>15</regioneCode>");
		sb.append("                    <regioneDesc>CAMPANIA</regioneDesc>");
		sb.append("                    <comuneIstatCode>063024</comuneIstatCode>");
		sb.append("                </comuneNascita>");
		sb.append("                <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("                <dataModifica>2015-02-25T15:54:35.192+01:00</dataModifica>");
		sb.append("                <dataNascita>1973-12-22T00:00:00+01:00</dataNascita>");
		sb.append("                <dataStato>2015-01-23T10:51:40.518+01:00</dataStato>");
		sb.append("                <dataValidita>2015-01-23T10:51:40.518+01:00</dataValidita>");
		sb.append("                <denominazione>VITELLI ANNALINA</denominazione>");
		sb.append("                <elencoModalitaPagamento>");
		sb.append("                    <loginOperazione>Demo 24 - CittÃ  di Torino - AMM.</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>51</uid>");
		sb.append("                    <associatoA>Soggetto</associatoA>");
		sb.append("                    <codiceFiscaleQuietanzante>VTLNLN73T62C129D</codiceFiscaleQuietanzante>");
		sb.append("                    <codiceModalitaPagamento>1</codiceModalitaPagamento>");
		sb.append("                    <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento>");
		sb.append("                    <comuneNascita>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <descrizione>Castellammare di Stabia</descrizione>");
		sb.append("                        <nazioneCode>1</nazioneCode>");
		sb.append("                    </comuneNascita>");
		sb.append("                    <dataNascitaQuietanzante>22/12/1973</dataNascitaQuietanzante>");
		sb.append("                    <descrizione>quietanzante VITELLI ANNALINA - CF VTLNLN73T62C129D - nato il 22/12/1973 - a Castellammare di Stabia, 1</descrizione>");
		sb.append("                    <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento>");
		sb.append("                    <idStatoModalitaPagamento>7</idStatoModalitaPagamento>");
		sb.append("                    <inModifica>false</inModifica>");
		sb.append("                    <loginCreazione>Demo 24 - CittÃ  di Torino - AMM.</loginCreazione>");
		sb.append("                    <loginModifica>Demo 24 - CittÃ  di Torino - AMM.</loginModifica>");
		sb.append("                    <luogoNascitaQuietanzante>Castellammare di Stabia</luogoNascitaQuietanzante>");
		sb.append("                    <modalitaAccreditoSoggetto>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>CON</codice>");
		sb.append("                        <descrizione>Contanti</descrizione>");
		sb.append("                        <priorita>0</priorita>");
		sb.append("                    </modalitaAccreditoSoggetto>");
		sb.append("                    <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza>");
		sb.append("                    <note></note>");
		sb.append("                    <soggettoQuietanzante>VITELLI ANNALINA</soggettoQuietanzante>");
		sb.append("                    <statoNascitaQuietanzante>1</statoNascitaQuietanzante>");
		sb.append("                </elencoModalitaPagamento>");
		sb.append("                <elencoModalitaPagamento>");
		sb.append("                    <loginOperazione>Demo 24 - CittÃ  di Torino - AMM.</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>83888</uid>");
		sb.append("                    <associatoA>Soggetto</associatoA>");
		sb.append("                    <bic></bic>");
		sb.append("                    <codiceModalitaPagamento>4</codiceModalitaPagamento>");
		sb.append("                    <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento>");
		sb.append("                    <contoCorrente></contoCorrente>");
		sb.append("                    <descrizione>iban IT76H0103025900000000315296</descrizione>");
		sb.append("                    <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento>");
		sb.append("                    <iban>IT76H0103025900000000315296</iban>");
		sb.append("                    <idStatoModalitaPagamento>7</idStatoModalitaPagamento>");
		sb.append("                    <inModifica>false</inModifica>");
		sb.append("                    <intestazioneConto>VITELLI ANNALINA</intestazioneConto>");
		sb.append("                    <loginCreazione>Demo 24 - CittÃ  di Torino - AMM.</loginCreazione>");
		sb.append("                    <loginModifica>Demo 24 - CittÃ  di Torino - AMM.</loginModifica>");
		sb.append("                    <modalitaAccreditoSoggetto>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>CB</codice>");
		sb.append("                        <descrizione>CONTO CORRENTE BANCARIO</descrizione>");
		sb.append("                        <priorita>0</priorita>");
		sb.append("                    </modalitaAccreditoSoggetto>");
		sb.append("                    <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza>");
		sb.append("                    <note></note>");
		sb.append("                </elencoModalitaPagamento>");
		sb.append("                <idStatoOperativoAnagrafica>2</idStatoOperativoAnagrafica>");
		sb.append("                <indirizzi>");
		sb.append("                    <cap>10100</cap>");
		sb.append("                    <denominazione>ROMA</denominazione>");
		sb.append("                    <numeroCivico>10</numeroCivico>");
		sb.append("                    <sedime>Via</sedime>");
		sb.append("                    <avviso>false</avviso>");
		sb.append("                    <checkAvviso>false</checkAvviso>");
		sb.append("                    <checkPrincipale>false</checkPrincipale>");
		sb.append("                    <codiceNazione>1</codiceNazione>");
		sb.append("                    <comune>Cuneo</comune>");
		sb.append("                    <idComune>004078</idComune>");
		sb.append("                    <idTipoIndirizzo>DOMICILIO</idTipoIndirizzo>");
		sb.append("                    <idTipoIndirizzoDesc>domicilio fiscale</idTipoIndirizzoDesc>");
		sb.append("                    <indirizzoId>57</indirizzoId>");
		sb.append("                    <nazione>Italia</nazione>");
		sb.append("                    <principale>true</principale>");
		sb.append("                    <provincia>CN</provincia>");
		sb.append("                </indirizzi>");
		sb.append("                <indirizzi>");
		sb.append("                    <cap>10132</cap>");
		sb.append("                    <denominazione>CAVALCANTI</denominazione>");
		sb.append("                    <numeroCivico>11</numeroCivico>");
		sb.append("                    <sedime>Via</sedime>");
		sb.append("                    <avviso>false</avviso>");
		sb.append("                    <checkAvviso>false</checkAvviso>");
		sb.append("                    <checkPrincipale>false</checkPrincipale>");
		sb.append("                    <codiceNazione>1</codiceNazione>");
		sb.append("                    <comune>Torino</comune>");
		sb.append("                    <idComune>001272</idComune>");
		sb.append("                    <idTipoIndirizzo>DOMICILIO</idTipoIndirizzo>");
		sb.append("                    <idTipoIndirizzoDesc>domicilio fiscale</idTipoIndirizzoDesc>");
		sb.append("                    <indirizzoId>58</indirizzoId>");
		sb.append("                    <nazione>Italia</nazione>");
		sb.append("                    <principale>false</principale>");
		sb.append("                    <provincia>TO</provincia>");
		sb.append("                </indirizzi>");
		sb.append("                <indirizzi>");
		sb.append("                    <cap>10132</cap>");
		sb.append("                    <denominazione>CAVALCANTI</denominazione>");
		sb.append("                    <numeroCivico>11</numeroCivico>");
		sb.append("                    <sedime>Via</sedime>");
		sb.append("                    <avviso>false</avviso>");
		sb.append("                    <checkAvviso>false</checkAvviso>");
		sb.append("                    <checkPrincipale>false</checkPrincipale>");
		sb.append("                    <codiceNazione>1</codiceNazione>");
		sb.append("                    <comune>Torino</comune>");
		sb.append("                    <idComune>001272</idComune>");
		sb.append("                    <idTipoIndirizzo>DOMICILIO</idTipoIndirizzo>");
		sb.append("                    <idTipoIndirizzoDesc>domicilio fiscale</idTipoIndirizzoDesc>");
		sb.append("                    <indirizzoId>59</indirizzoId>");
		sb.append("                    <nazione>Italia</nazione>");
		sb.append("                    <principale>false</principale>");
		sb.append("                    <provincia>TO</provincia>");
		sb.append("                </indirizzi>");
		sb.append("                <loginControlloPerModifica>Demo 24 - CittÃ  di Torino - AMM.</loginControlloPerModifica>");
		sb.append("                <loginCreazione>Demo 24 - CittÃ  di Torino - AMM.</loginCreazione>");
		sb.append("                <loginModifica>Demo 24 - CittÃ  di Torino - AMM.</loginModifica>");
		sb.append("                <matricola>1528</matricola>");
		sb.append("                <nome>ANNALINA</nome>");
		sb.append("                <note></note>");
		sb.append("                <residenteEstero>false</residenteEstero>");
		sb.append("                <sesso>FEMMINA</sesso>");
		sb.append("                <sessoStringa>FEMMINA</sessoStringa>");
		sb.append("                <statoOperativo>VALIDO</statoOperativo>");
		sb.append("                <tipoSoggetto>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>31</uid>");
		sb.append("                    <soggettoTipoCode>PF</soggettoTipoCode>");
		sb.append("                    <soggettoTipoDesc>Persona fisica</soggettoTipoDesc>");
		sb.append("                    <soggettoTipoId>1</soggettoTipoId>");
		sb.append("                </tipoSoggetto>");
		sb.append("                <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("            </soggetto>");
		sb.append("            <soggettoCode>31</soggettoCode>");
		sb.append("            <tipoMovimento>I</tipoMovimento>");
		sb.append("            <tipoMovimentoDesc>Impegno</tipoMovimentoDesc>");
		sb.append("            <utenteCreazione>Demo 24 - CittÃ  di Torino - AMM.</utenteCreazione>");
		sb.append("            <utenteModifica>Demo 24 - CittÃ  di Torino - AMM.</utenteModifica>");
		sb.append("            <validato>true</validato>");
		sb.append("            <annoFinanziamento>0</annoFinanziamento>");
		sb.append("            <chiaveCapitoloUscitaGestione>18443</chiaveCapitoloUscitaGestione>");
		sb.append("            <dataStatoOperativoMovimentoGestioneSpesa>2015-03-30T15:43:32.484+02:00</dataStatoOperativoMovimentoGestioneSpesa>");
		sb.append("            <descrizioneStatoOperativoMovimentoGestioneSpesa>DEFINITIVO</descrizioneStatoOperativoMovimentoGestioneSpesa>");
		sb.append("            <disponibilitaFinanziare>1000.00</disponibilitaFinanziare>");
		sb.append("            <disponibilitaImpegnoModifica>100.00</disponibilitaImpegnoModifica>");
		sb.append("            <disponibilitaLiquidare>100.00</disponibilitaLiquidare>");
		sb.append("            <disponibilitaPagare>1000.00</disponibilitaPagare>");
		sb.append("            <disponibilitaSubimpegnare>0</disponibilitaSubimpegnare>");
		sb.append("            <disponibilitaVincolare>1000.00</disponibilitaVincolare>");
		sb.append("            <numeroAccFinanziamento>0</numeroAccFinanziamento>");
		sb.append("            <sommaLiquidazioniDoc>900.00</sommaLiquidazioniDoc>");
		sb.append("            <statoOperativoMovimentoGestioneSpesa>D</statoOperativoMovimentoGestioneSpesa>");
		sb.append("            <tipoImpegno>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>24321</uid>");
		sb.append("                <codice>SVI</codice>");
		sb.append("                <descrizione>Svincolato</descrizione>");
		sb.append("            </tipoImpegno>");
		sb.append("            <totaleSubImpegni>0</totaleSubImpegni>");
		sb.append("            <capitoloUscitaGestione>");
		sb.append("                <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>18443</uid>");
		sb.append("                <annoCapitolo>2015</annoCapitolo>");
		sb.append("                <annoCreazioneCapitolo>2015</annoCreazioneCapitolo>");
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
		sb.append("                <categoriaCapitolo>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>2</uid>");
		sb.append("                    <codice>STD</codice>");
		sb.append("                    <descrizione>Standard</descrizione>");
		sb.append("                </categoriaCapitolo>");
		sb.append("                <descrizione>capitolo per prova cassa economale</descrizione>");
		sb.append("                <descrizioneArticolo></descrizioneArticolo>");
		sb.append("                <elementoPianoDeiConti>");
		sb.append("                    <loginOperazione>pentaho_PDC_20141113_1</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>125547</uid>");
		sb.append("                    <codice>U.1.01.01.01.001</codice>");
		sb.append("                    <descrizione>Arretrati per anni precedenti corrisposti al personale a tempo indeterminato</descrizione>");
		sb.append("                    <livello>5</livello>");
		sb.append("                    <tipoClassificatore>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>PDC_V</codice>");
		sb.append("                        <descrizione>Quinto livello PDC</descrizione>");
		sb.append("                    </tipoClassificatore>");
		sb.append("                </elementoPianoDeiConti>");
		sb.append("                <ente>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <flagImpegnabile>true</flagImpegnabile>");
		sb.append("                <flagRilevanteIva>false</flagRilevanteIva>");
		sb.append("                <importiCapitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:importiCapitoloUG\">");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <annoCompetenza>2015</annoCompetenza>");
		sb.append("                    <disponibilitaVariare>0</disponibilitaVariare>");
		sb.append("                    <disponibilitaVariareAnno1>0</disponibilitaVariareAnno1>");
		sb.append("                    <disponibilitaVariareAnno2>0</disponibilitaVariareAnno2>");
		sb.append("                    <disponibilitaVariareAnno3>0</disponibilitaVariareAnno3>");
		sb.append("                    <disponibilitaVariareCassa>0</disponibilitaVariareCassa>");
		sb.append("                    <fondoPluriennaleVinc>0</fondoPluriennaleVinc>");
		sb.append("                    <impegnatoPlur>5000</impegnatoPlur>");
		sb.append("                    <stanziamento>10000.00</stanziamento>");
		sb.append("                    <stanziamentoCassa>12000.00</stanziamentoCassa>");
		sb.append("                    <stanziamentoCassaIniziale>12000.00</stanziamentoCassaIniziale>");
		sb.append("                    <stanziamentoIniziale>10000.00</stanziamentoIniziale>");
		sb.append("                    <stanziamentoProposto>0</stanziamentoProposto>");
		sb.append("                    <stanziamentoResiduo>2000.00</stanziamentoResiduo>");
		sb.append("                    <stanziamentoResiduoIniziale>0.00</stanziamentoResiduoIniziale>");
		sb.append("                    <disponibilitaImpegnare>-3001.00</disponibilitaImpegnare>");
		sb.append("                    <disponibilitaImpegnareAnno1>-3001.00</disponibilitaImpegnareAnno1>");
		sb.append("                    <disponibilitaImpegnareAnno2>0.00</disponibilitaImpegnareAnno2>");
		sb.append("                    <disponibilitaImpegnareAnno3>0.00</disponibilitaImpegnareAnno3>");
		sb.append("                    <disponibilitaPagare>4070.00</disponibilitaPagare>");
		sb.append("                    <stanziamentoAsset>0</stanziamentoAsset>");
		sb.append("                    <stanziamentoCassaAsset>0</stanziamentoCassaAsset>");
		sb.append("                    <stanziamentoResAsset>0</stanziamentoResAsset>");
		sb.append("                    <totalePagato>930.00</totalePagato>");
		sb.append("                </importiCapitolo>");
		sb.append("                <listaImportiCapitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:importiCapitoloUG\">");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <annoCompetenza>2015</annoCompetenza>");
		sb.append("                    <disponibilitaVariare>0</disponibilitaVariare>");
		sb.append("                    <disponibilitaVariareAnno1>0</disponibilitaVariareAnno1>");
		sb.append("                    <disponibilitaVariareAnno2>0</disponibilitaVariareAnno2>");
		sb.append("                    <disponibilitaVariareAnno3>0</disponibilitaVariareAnno3>");
		sb.append("                    <disponibilitaVariareCassa>0</disponibilitaVariareCassa>");
		sb.append("                    <fondoPluriennaleVinc>0</fondoPluriennaleVinc>");
		sb.append("                    <impegnatoPlur>5000</impegnatoPlur>");
		sb.append("                    <stanziamento>10000.00</stanziamento>");
		sb.append("                    <stanziamentoCassa>12000.00</stanziamentoCassa>");
		sb.append("                    <stanziamentoCassaIniziale>12000.00</stanziamentoCassaIniziale>");
		sb.append("                    <stanziamentoIniziale>10000.00</stanziamentoIniziale>");
		sb.append("                    <stanziamentoProposto>0</stanziamentoProposto>");
		sb.append("                    <stanziamentoResiduo>2000.00</stanziamentoResiduo>");
		sb.append("                    <stanziamentoResiduoIniziale>0.00</stanziamentoResiduoIniziale>");
		sb.append("                    <disponibilitaImpegnare>-3001.00</disponibilitaImpegnare>");
		sb.append("                    <disponibilitaImpegnareAnno1>-3001.00</disponibilitaImpegnareAnno1>");
		sb.append("                    <disponibilitaImpegnareAnno2>0.00</disponibilitaImpegnareAnno2>");
		sb.append("                    <disponibilitaImpegnareAnno3>0.00</disponibilitaImpegnareAnno3>");
		sb.append("                    <disponibilitaPagare>4070.00</disponibilitaPagare>");
		sb.append("                    <stanziamentoAsset>0</stanziamentoAsset>");
		sb.append("                    <stanziamentoCassaAsset>0</stanziamentoCassaAsset>");
		sb.append("                    <stanziamentoResAsset>0</stanziamentoResAsset>");
		sb.append("                    <totalePagato>930.00</totalePagato>");
		sb.append("                </listaImportiCapitolo>");
		sb.append("                <listaImportiCapitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:importiCapitoloUG\">");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <annoCompetenza>2016</annoCompetenza>");
		sb.append("                    <disponibilitaVariare>0</disponibilitaVariare>");
		sb.append("                    <disponibilitaVariareAnno1>0</disponibilitaVariareAnno1>");
		sb.append("                    <disponibilitaVariareAnno2>0</disponibilitaVariareAnno2>");
		sb.append("                    <disponibilitaVariareAnno3>0</disponibilitaVariareAnno3>");
		sb.append("                    <disponibilitaVariareCassa>0</disponibilitaVariareCassa>");
		sb.append("                    <fondoPluriennaleVinc>0</fondoPluriennaleVinc>");
		sb.append("                    <impegnatoPlur>0</impegnatoPlur>");
		sb.append("                    <stanziamento>0.00</stanziamento>");
		sb.append("                    <stanziamentoCassa>0.00</stanziamentoCassa>");
		sb.append("                    <stanziamentoCassaIniziale>0.00</stanziamentoCassaIniziale>");
		sb.append("                    <stanziamentoIniziale>0.00</stanziamentoIniziale>");
		sb.append("                    <stanziamentoProposto>0</stanziamentoProposto>");
		sb.append("                    <stanziamentoResiduo>0.00</stanziamentoResiduo>");
		sb.append("                    <stanziamentoResiduoIniziale>0.00</stanziamentoResiduoIniziale>");
		sb.append("                    <disponibilitaImpegnare>0</disponibilitaImpegnare>");
		sb.append("                    <disponibilitaImpegnareAnno1>0</disponibilitaImpegnareAnno1>");
		sb.append("                    <disponibilitaImpegnareAnno2>0</disponibilitaImpegnareAnno2>");
		sb.append("                    <disponibilitaImpegnareAnno3>0</disponibilitaImpegnareAnno3>");
		sb.append("                    <disponibilitaPagare>0</disponibilitaPagare>");
		sb.append("                    <stanziamentoAsset>0</stanziamentoAsset>");
		sb.append("                    <stanziamentoCassaAsset>0</stanziamentoCassaAsset>");
		sb.append("                    <stanziamentoResAsset>0</stanziamentoResAsset>");
		sb.append("                    <totalePagato>0</totalePagato>");
		sb.append("                </listaImportiCapitolo>");
		sb.append("                <listaImportiCapitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:importiCapitoloUG\">");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <annoCompetenza>2017</annoCompetenza>");
		sb.append("                    <disponibilitaVariare>0</disponibilitaVariare>");
		sb.append("                    <disponibilitaVariareAnno1>0</disponibilitaVariareAnno1>");
		sb.append("                    <disponibilitaVariareAnno2>0</disponibilitaVariareAnno2>");
		sb.append("                    <disponibilitaVariareAnno3>0</disponibilitaVariareAnno3>");
		sb.append("                    <disponibilitaVariareCassa>0</disponibilitaVariareCassa>");
		sb.append("                    <fondoPluriennaleVinc>0</fondoPluriennaleVinc>");
		sb.append("                    <impegnatoPlur>0</impegnatoPlur>");
		sb.append("                    <stanziamento>10000.00</stanziamento>");
		sb.append("                    <stanziamentoCassa>0.00</stanziamentoCassa>");
		sb.append("                    <stanziamentoCassaIniziale>0.00</stanziamentoCassaIniziale>");
		sb.append("                    <stanziamentoIniziale>0.00</stanziamentoIniziale>");
		sb.append("                    <stanziamentoProposto>0</stanziamentoProposto>");
		sb.append("                    <stanziamentoResiduo>0.00</stanziamentoResiduo>");
		sb.append("                    <stanziamentoResiduoIniziale>0.00</stanziamentoResiduoIniziale>");
		sb.append("                    <disponibilitaImpegnare>0</disponibilitaImpegnare>");
		sb.append("                    <disponibilitaImpegnareAnno1>0</disponibilitaImpegnareAnno1>");
		sb.append("                    <disponibilitaImpegnareAnno2>0</disponibilitaImpegnareAnno2>");
		sb.append("                    <disponibilitaImpegnareAnno3>0</disponibilitaImpegnareAnno3>");
		sb.append("                    <disponibilitaPagare>0</disponibilitaPagare>");
		sb.append("                    <stanziamentoAsset>0</stanziamentoAsset>");
		sb.append("                    <stanziamentoCassaAsset>0</stanziamentoCassaAsset>");
		sb.append("                    <stanziamentoResAsset>0</stanziamentoResAsset>");
		sb.append("                    <totalePagato>0</totalePagato>");
		sb.append("                </listaImportiCapitolo>");
		sb.append("                <note></note>");
		sb.append("                <numeroArticolo>1</numeroArticolo>");
		sb.append("                <numeroCapitolo>100</numeroCapitolo>");
		sb.append("                <numeroUEB>1</numeroUEB>");
		sb.append("                <statoOperativoElementoDiBilancio>VALIDO</statoOperativoElementoDiBilancio>");
		sb.append("                <strutturaAmministrativoContabile>");
		sb.append("                    <loginOperazione>admin</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>502</uid>");
		sb.append("                    <codice>001</codice>");
		sb.append("                    <descrizione>DIREZIONE GENERALE</descrizione>");
		sb.append("                    <livello>1</livello>");
		sb.append("                    <tipoClassificatore>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>CDR</codice>");
		sb.append("                        <descrizione>Centro di RespondabilitÃ Â (Direzione)</descrizione>");
		sb.append("                    </tipoClassificatore>");
		sb.append("                </strutturaAmministrativoContabile>");
		sb.append("                <tipoCapitolo>CAPITOLO_USCITA_GESTIONE</tipoCapitolo>");
		sb.append("                <uidCapitoloEquivalente>0</uidCapitoloEquivalente>");
		sb.append("                <uidExCapitolo>18445</uidExCapitolo>");
		sb.append("                <classificazioneCofog>");
		sb.append("                    <loginOperazione>admin)</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>5276</uid>");
		sb.append("                    <codice>01.1</codice>");
		sb.append("                    <descrizione>Organi esecutivi e legislativi, attivitÃ  finanziari e fiscali e affari esteri</descrizione>");
		sb.append("                    <livello>2</livello>");
		sb.append("                    <tipoClassificatore>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>GRUPPO_COFOG</codice>");
		sb.append("                        <descrizione>Gruppo Cofog</descrizione>");
		sb.append("                    </tipoClassificatore>");
		sb.append("                </classificazioneCofog>");
		sb.append("                <flagAssegnabile>false</flagAssegnabile>");
		sb.append("                <flagFondoPluriennaleVinc>false</flagFondoPluriennaleVinc>");
		sb.append("                <flagFondoSvalutazioneCred>false</flagFondoSvalutazioneCred>");
		sb.append("                <flagTrasferimentiOrgComunitari>false</flagTrasferimentiOrgComunitari>");
		sb.append("                <funzDelegateRegione>false</funzDelegateRegione>");
		sb.append("                <importiCapitoloUG>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <annoCompetenza>2015</annoCompetenza>");
		sb.append("                    <disponibilitaVariare>0</disponibilitaVariare>");
		sb.append("                    <disponibilitaVariareAnno1>0</disponibilitaVariareAnno1>");
		sb.append("                    <disponibilitaVariareAnno2>0</disponibilitaVariareAnno2>");
		sb.append("                    <disponibilitaVariareAnno3>0</disponibilitaVariareAnno3>");
		sb.append("                    <disponibilitaVariareCassa>0</disponibilitaVariareCassa>");
		sb.append("                    <fondoPluriennaleVinc>0</fondoPluriennaleVinc>");
		sb.append("                    <impegnatoPlur>5000</impegnatoPlur>");
		sb.append("                    <stanziamento>10000.00</stanziamento>");
		sb.append("                    <stanziamentoCassa>12000.00</stanziamentoCassa>");
		sb.append("                    <stanziamentoCassaIniziale>12000.00</stanziamentoCassaIniziale>");
		sb.append("                    <stanziamentoIniziale>10000.00</stanziamentoIniziale>");
		sb.append("                    <stanziamentoProposto>0</stanziamentoProposto>");
		sb.append("                    <stanziamentoResiduo>2000.00</stanziamentoResiduo>");
		sb.append("                    <stanziamentoResiduoIniziale>0.00</stanziamentoResiduoIniziale>");
		sb.append("                    <disponibilitaImpegnare>-3001.00</disponibilitaImpegnare>");
		sb.append("                    <disponibilitaImpegnareAnno1>-3001.00</disponibilitaImpegnareAnno1>");
		sb.append("                    <disponibilitaImpegnareAnno2>0.00</disponibilitaImpegnareAnno2>");
		sb.append("                    <disponibilitaImpegnareAnno3>0.00</disponibilitaImpegnareAnno3>");
		sb.append("                    <disponibilitaPagare>4070.00</disponibilitaPagare>");
		sb.append("                    <stanziamentoAsset>0</stanziamentoAsset>");
		sb.append("                    <stanziamentoCassaAsset>0</stanziamentoCassaAsset>");
		sb.append("                    <stanziamentoResAsset>0</stanziamentoResAsset>");
		sb.append("                    <totalePagato>930.00</totalePagato>");
		sb.append("                </importiCapitoloUG>");
		sb.append("                <listaImportiCapitoloUG>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <annoCompetenza>2015</annoCompetenza>");
		sb.append("                    <disponibilitaVariare>0</disponibilitaVariare>");
		sb.append("                    <disponibilitaVariareAnno1>0</disponibilitaVariareAnno1>");
		sb.append("                    <disponibilitaVariareAnno2>0</disponibilitaVariareAnno2>");
		sb.append("                    <disponibilitaVariareAnno3>0</disponibilitaVariareAnno3>");
		sb.append("                    <disponibilitaVariareCassa>0</disponibilitaVariareCassa>");
		sb.append("                    <fondoPluriennaleVinc>0</fondoPluriennaleVinc>");
		sb.append("                    <impegnatoPlur>5000</impegnatoPlur>");
		sb.append("                    <stanziamento>10000.00</stanziamento>");
		sb.append("                    <stanziamentoCassa>12000.00</stanziamentoCassa>");
		sb.append("                    <stanziamentoCassaIniziale>12000.00</stanziamentoCassaIniziale>");
		sb.append("                    <stanziamentoIniziale>10000.00</stanziamentoIniziale>");
		sb.append("                    <stanziamentoProposto>0</stanziamentoProposto>");
		sb.append("                    <stanziamentoResiduo>2000.00</stanziamentoResiduo>");
		sb.append("                    <stanziamentoResiduoIniziale>0.00</stanziamentoResiduoIniziale>");
		sb.append("                    <disponibilitaImpegnare>-3001.00</disponibilitaImpegnare>");
		sb.append("                    <disponibilitaImpegnareAnno1>-3001.00</disponibilitaImpegnareAnno1>");
		sb.append("                    <disponibilitaImpegnareAnno2>0.00</disponibilitaImpegnareAnno2>");
		sb.append("                    <disponibilitaImpegnareAnno3>0.00</disponibilitaImpegnareAnno3>");
		sb.append("                    <disponibilitaPagare>4070.00</disponibilitaPagare>");
		sb.append("                    <stanziamentoAsset>0</stanziamentoAsset>");
		sb.append("                    <stanziamentoCassaAsset>0</stanziamentoCassaAsset>");
		sb.append("                    <stanziamentoResAsset>0</stanziamentoResAsset>");
		sb.append("                    <totalePagato>930.00</totalePagato>");
		sb.append("                </listaImportiCapitoloUG>");
		sb.append("                <listaImportiCapitoloUG>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <annoCompetenza>2016</annoCompetenza>");
		sb.append("                    <disponibilitaVariare>0</disponibilitaVariare>");
		sb.append("                    <disponibilitaVariareAnno1>0</disponibilitaVariareAnno1>");
		sb.append("                    <disponibilitaVariareAnno2>0</disponibilitaVariareAnno2>");
		sb.append("                    <disponibilitaVariareAnno3>0</disponibilitaVariareAnno3>");
		sb.append("                    <disponibilitaVariareCassa>0</disponibilitaVariareCassa>");
		sb.append("                    <fondoPluriennaleVinc>0</fondoPluriennaleVinc>");
		sb.append("                    <impegnatoPlur>0</impegnatoPlur>");
		sb.append("                    <stanziamento>0.00</stanziamento>");
		sb.append("                    <stanziamentoCassa>0.00</stanziamentoCassa>");
		sb.append("                    <stanziamentoCassaIniziale>0.00</stanziamentoCassaIniziale>");
		sb.append("                    <stanziamentoIniziale>0.00</stanziamentoIniziale>");
		sb.append("                    <stanziamentoProposto>0</stanziamentoProposto>");
		sb.append("                    <stanziamentoResiduo>0.00</stanziamentoResiduo>");
		sb.append("                    <stanziamentoResiduoIniziale>0.00</stanziamentoResiduoIniziale>");
		sb.append("                    <disponibilitaImpegnare>0</disponibilitaImpegnare>");
		sb.append("                    <disponibilitaImpegnareAnno1>0</disponibilitaImpegnareAnno1>");
		sb.append("                    <disponibilitaImpegnareAnno2>0</disponibilitaImpegnareAnno2>");
		sb.append("                    <disponibilitaImpegnareAnno3>0</disponibilitaImpegnareAnno3>");
		sb.append("                    <disponibilitaPagare>0</disponibilitaPagare>");
		sb.append("                    <stanziamentoAsset>0</stanziamentoAsset>");
		sb.append("                    <stanziamentoCassaAsset>0</stanziamentoCassaAsset>");
		sb.append("                    <stanziamentoResAsset>0</stanziamentoResAsset>");
		sb.append("                    <totalePagato>0</totalePagato>");
		sb.append("                </listaImportiCapitoloUG>");
		sb.append("                <listaImportiCapitoloUG>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <annoCompetenza>2017</annoCompetenza>");
		sb.append("                    <disponibilitaVariare>0</disponibilitaVariare>");
		sb.append("                    <disponibilitaVariareAnno1>0</disponibilitaVariareAnno1>");
		sb.append("                    <disponibilitaVariareAnno2>0</disponibilitaVariareAnno2>");
		sb.append("                    <disponibilitaVariareAnno3>0</disponibilitaVariareAnno3>");
		sb.append("                    <disponibilitaVariareCassa>0</disponibilitaVariareCassa>");
		sb.append("                    <fondoPluriennaleVinc>0</fondoPluriennaleVinc>");
		sb.append("                    <impegnatoPlur>0</impegnatoPlur>");
		sb.append("                    <stanziamento>10000.00</stanziamento>");
		sb.append("                    <stanziamentoCassa>0.00</stanziamentoCassa>");
		sb.append("                    <stanziamentoCassaIniziale>0.00</stanziamentoCassaIniziale>");
		sb.append("                    <stanziamentoIniziale>0.00</stanziamentoIniziale>");
		sb.append("                    <stanziamentoProposto>0</stanziamentoProposto>");
		sb.append("                    <stanziamentoResiduo>0.00</stanziamentoResiduo>");
		sb.append("                    <stanziamentoResiduoIniziale>0.00</stanziamentoResiduoIniziale>");
		sb.append("                    <disponibilitaImpegnare>0</disponibilitaImpegnare>");
		sb.append("                    <disponibilitaImpegnareAnno1>0</disponibilitaImpegnareAnno1>");
		sb.append("                    <disponibilitaImpegnareAnno2>0</disponibilitaImpegnareAnno2>");
		sb.append("                    <disponibilitaImpegnareAnno3>0</disponibilitaImpegnareAnno3>");
		sb.append("                    <disponibilitaPagare>0</disponibilitaPagare>");
		sb.append("                    <stanziamentoAsset>0</stanziamentoAsset>");
		sb.append("                    <stanziamentoCassaAsset>0</stanziamentoCassaAsset>");
		sb.append("                    <stanziamentoResAsset>0</stanziamentoResAsset>");
		sb.append("                    <totalePagato>0</totalePagato>");
		sb.append("                </listaImportiCapitoloUG>");
		sb.append("                <macroaggregato>");
		sb.append("                    <loginOperazione>pentaho_TM_20141113_1</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>118917</uid>");
		sb.append("                    <codice>1010000</codice>");
		sb.append("                    <descrizione>Redditi da lavoro dipendente (anno 2015)</descrizione>");
		sb.append("                    <livello>2</livello>");
		sb.append("                    <tipoClassificatore>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>MACROAGGREGATO</codice>");
		sb.append("                        <descrizione>Macroaggregato</descrizione>");
		sb.append("                    </tipoClassificatore>");
		sb.append("                </macroaggregato>");
		sb.append("                <missione>");
		sb.append("                    <loginOperazione>pentaho_MP_20141113_1</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>118306</uid>");
		sb.append("                    <codice>01</codice>");
		sb.append("                    <descrizione>Servizi istituzionali,  generali e di gestione (anno 2015)</descrizione>");
		sb.append("                    <livello>1</livello>");
		sb.append("                    <tipoClassificatore>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>MISSIONE</codice>");
		sb.append("                    </tipoClassificatore>");
		sb.append("                </missione>");
		sb.append("                <programma>");
		sb.append("                    <loginOperazione>pentaho_MP_20141113_1</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>118307</uid>");
		sb.append("                    <codice>0101</codice>");
		sb.append("                    <descrizione>Organi istituzionali (anno 2015)</descrizione>");
		sb.append("                    <livello>2</livello>");
		sb.append("                    <tipoClassificatore>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>PROGRAMMA</codice>");
		sb.append("                        <descrizione>Programma</descrizione>");
		sb.append("                    </tipoClassificatore>");
		sb.append("                </programma>");
		sb.append("                <titoloSpesa>");
		sb.append("                    <loginOperazione>pentaho_TM_20141113_1</loginOperazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>118916</uid>");
		sb.append("                    <codice>1</codice>");
		sb.append("                    <descrizione>Spese correnti (anno 2015)</descrizione>");
		sb.append("                    <livello>1</livello>");
		sb.append("                    <tipoClassificatore>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>0</uid>");
		sb.append("                        <codice>TITOLO_SPESA</codice>");
		sb.append("                    </tipoClassificatore>");
		sb.append("                </titoloSpesa>");
		sb.append("            </capitoloUscitaGestione>");
		sb.append("        </impegno>");
		sb.append("        <matricola>12345</matricola>");
		sb.append("        <movimento>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <bic></bic>");
		sb.append("            <contoCorrente></contoCorrente>");
		sb.append("            <dataMovimento>2015-09-23T00:00:00+02:00</dataMovimento>");
		sb.append("            <dettaglioPagamento>moneta canta che ti passa!</dettaglioPagamento>");
		sb.append("            <modalitaPagamentoCassa>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>7</uid>");
		sb.append("            </modalitaPagamentoCassa>");
		sb.append("            <modalitaPagamentoDipendente>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>4</uid>");
		sb.append("            </modalitaPagamentoDipendente>");
		sb.append("        </movimento>");
		sb.append("        <nome>GIOVANNI</nome>");
		sb.append("        <note></note>");
		sb.append("        <soggetto>");
		sb.append("            <loginOperazione>Demo 24 - CittÃ  di Torino - AMM.</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>38279</uid>");
		sb.append("            <avviso>false</avviso>");
		sb.append("            <codiceFiscale>CHKBLL68A01Z33LP</codiceFiscale>");
		sb.append("            <codiceSoggetto>24</codiceSoggetto>");
		sb.append("            <codiceSoggettoNumber>24</codiceSoggettoNumber>");
		sb.append("            <cognome>ROSSI</cognome>");
		sb.append("            <comuneNascita>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>46095</uid>");
		sb.append("                <descrizione>TORINO </descrizione>");
		sb.append("                <codiceBelfiore>TORINO </codiceBelfiore>");
		sb.append("                <nazioneCode>1</nazioneCode>");
		sb.append("                <nazioneDesc>Italia</nazioneDesc>");
		sb.append("            </comuneNascita>");
		sb.append("            <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("            <dataModifica>2015-03-04T16:28:27.578+01:00</dataModifica>");
		sb.append("            <dataNascita>1968-01-01T00:00:00+01:00</dataNascita>");
		sb.append("            <dataStato>2015-03-04T16:28:27.578+01:00</dataStato>");
		sb.append("            <dataValidita>2015-03-04T16:28:27.578+01:00</dataValidita>");
		sb.append("            <denominazione>ROSSI GIOVANNI</denominazione>");
		sb.append("            <idStatoOperativoAnagrafica>2</idStatoOperativoAnagrafica>");
		sb.append("            <indirizzi>");
		sb.append("                <cap>10093</cap>");
		sb.append("                <denominazione>ROMA</denominazione>");
		sb.append("                <numeroCivico>12</numeroCivico>");
		sb.append("                <sedime>Via</sedime>");
		sb.append("                <avviso>false</avviso>");
		sb.append("                <checkAvviso>false</checkAvviso>");
		sb.append("                <checkPrincipale>false</checkPrincipale>");
		sb.append("                <codiceNazione>1</codiceNazione>");
		sb.append("                <comune>TORINO </comune>");
		sb.append("                <idTipoIndirizzo>DOMICILIO</idTipoIndirizzo>");
		sb.append("                <idTipoIndirizzoDesc>domicilio fiscale</idTipoIndirizzoDesc>");
		sb.append("                <indirizzoId>38594</indirizzoId>");
		sb.append("                <nazione>Italia</nazione>");
		sb.append("                <principale>true</principale>");
		sb.append("            </indirizzi>");
		sb.append("            <loginControlloPerModifica>Demo 24 - CittÃ  di Torino - AMM.</loginControlloPerModifica>");
		sb.append("            <loginCreazione>Demo 24 - CittÃ  di Torino - AMM.</loginCreazione>");
		sb.append("            <loginModifica>Demo 24 - CittÃ  di Torino - AMM.</loginModifica>");
		sb.append("            <matricola>12345</matricola>");
		sb.append("            <nome>GIOVANNI</nome>");
		sb.append("            <note></note>");
		sb.append("            <residenteEstero>false</residenteEstero>");
		sb.append("            <sesso>MASCHIO</sesso>");
		sb.append("            <sessoStringa>MASCHIO</sessoStringa>");
		sb.append("            <statoOperativo>VALIDO</statoOperativo>");
		sb.append("            <tipoSoggetto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>35244</uid>");
		sb.append("                <soggettoTipoCode>PF</soggettoTipoCode>");
		sb.append("                <soggettoTipoDesc>Persona fisica</soggettoTipoDesc>");
		sb.append("                <soggettoTipoId>1</soggettoTipoId>");
		sb.append("            </tipoSoggetto>");
		sb.append("            <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("        </soggetto>");
		sb.append("        <strutturaDiAppartenenza></strutturaDiAppartenenza>");
		sb.append("    </richiestaEconomale>");
		sb.append("</inserisceRichiestaEconomale>");
		
		InserisceRichiestaEconomale req = JAXBUtility.unmarshall(sb.toString(), InserisceRichiestaEconomale.class);
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleRimborsoSpeseService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(500000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	@Test
	public void findCapitoloAssociatoAllaRichiesta(){
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setUid(37);
		richiestaEconomaleDad.findCapitoloAssociatoAllaRichiesta(richiestaEconomale);
	}
	
	
	@Test
	public void insRicEconAntSpese(){
//		byte[] byteArray;
//		try {
//			byteArray = getTestFileBytes("src/test/java/it/csi/siac/siacbilser/test/business/cassaeconomale/InserisceRichiestaEconomaleAnticipoSpese.xml");
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			return;
//		}
//		String xml;
//		try {
//			xml = new String(byteArray, "UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//			return;
//		}
//		
//		InserisceRichiestaEconomale req = JAXBUtility.unmarshall(xml, InserisceRichiestaEconomale.class);
		
		InserisceRichiestaEconomale req = getTestFileObject(InserisceRichiestaEconomale.class, "it/csi/siac/siacbilser/test/business/cassaeconomale/InserisceRichiestaEconomaleAnticipoSpese.xml");
		
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleRimborsoSpeseService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {
			System.out.println("Chi mi ha Interrotto!!!!!!!!!!!");
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void inserisceRichiestaEconomaleMissioneFromTestFile(){
		InserisceRichiestaEconomale req = getTestFileObject(InserisceRichiestaEconomale.class, 
				"it/csi/siac/siacbilser/test/business/cassaeconomale/InserisceRichiestaEconomaleTest.xml");
		
		
		InserisceRichiestaEconomaleResponse res = inserisceRichiestaEconomaleAnticipoSpesePerMissioneService.executeService(req);
		assertNotNull(res);
	}
	
}
