/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentoivaspesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siacbilser.business.service.documentoivaspesa.AggiornaStatoSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.AggiornaSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.InserisceNotaCreditoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.InserisceSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaDettaglioSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaPuntualeSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.integration.dad.ContatoreRegistroIvaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * The Class SubdocumentoIvaSpesaDLTest.
 */
public class SubdocumentoIvaSpesaTest extends BaseJunit4TestCase {
	
	
	/** The inserisce subdocumento iva spesa service. */
	@Autowired
	private InserisceSubdocumentoIvaSpesaService inserisceSubdocumentoIvaSpesaService;
	
	@Autowired
	private InserisceNotaCreditoIvaSpesaService inserisceNotaCreditoIvaSpesaService;
	
	/** The aggiorna subdocumento iva spesa service. */
	@Autowired
	private AggiornaSubdocumentoIvaSpesaService aggiornaSubdocumentoIvaSpesaService;
	
	/** The ricerca dettaglio subdocumento iva spesa service. */
	@Autowired
	private RicercaDettaglioSubdocumentoIvaSpesaService ricercaDettaglioSubdocumentoIvaSpesaService;
	
	/** The ricerca puntuale subdocumento iva spesa service. */
	@Autowired
	private RicercaPuntualeSubdocumentoIvaSpesaService ricercaPuntualeSubdocumentoIvaSpesaService;
	
	@Autowired
	private AggiornaStatoSubdocumentoIvaSpesaService aggiornaStatoSubdocumentoIvaSpesaService;

	
	/**
	 * Ricerca dettaglio subdocumento iva spesa.
	 */
	@Test
	public void ricercaDettaglioSubdocumentoIvaSpesa() {
		
		
		RicercaDettaglioSubdocumentoIvaSpesa req = new RicercaDettaglioSubdocumentoIvaSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoIvaSpesa sd = new SubdocumentoIvaSpesa();
		sd.setUid(60);
		req.setSubdocumentoIvaSpesa(sd);
		
		RicercaDettaglioSubdocumentoIvaSpesaResponse res = ricercaDettaglioSubdocumentoIvaSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Ricerca puntuale subdocumento iva spesa.
	 */
	@Test
	public void ricercaPuntualeSubdocumentoIvaSpesa() {
		
		
		RicercaPuntualeSubdocumentoIvaSpesa req = new RicercaPuntualeSubdocumentoIvaSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoIvaSpesa sd = new SubdocumentoIvaSpesa();
		sd.setEnte(getEnteTest());
		
		sd.setAnnoEsercizio(2013);
		sd.setProgressivoIVA(10);
		
		
		
		req.setSubdocumentoIvaSpesa(sd);
		
		RicercaPuntualeSubdocumentoIvaSpesaResponse res = ricercaPuntualeSubdocumentoIvaSpesaService.executeService(req);
		assertNotNull(res);
	}

	
	
	/**
	 * Inserisce subdocumento iva spesa.
	 */
	@Test
	public void inserisceNotaIvaSpesa() {
	
	
	 // BuildMyString.com generated code. Please enjoy your StringBuilder responsibly.

	StringBuilder sb = new StringBuilder();

	sb.append("<inserisceNotaCreditoIvaSpesa>");
	sb.append("    <dataOra>2014-06-26T15:06:32.502+02:00</dataOra>");
	sb.append("    <richiedente>");
	sb.append("        <account>");
	sb.append("            <stato>VALIDO</stato>");
	sb.append("            <uid>1</uid>");
	sb.append("            <nome>Demo 21</nome>");
	sb.append("            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>");
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
	sb.append("                <nome>CittÃ  di Torino</nome>");
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
	sb.append("    <bilancio>");
	sb.append("        <stato>VALIDO</stato>");
	sb.append("        <uid>1</uid>");
	sb.append("        <anno>2013</anno>");
	sb.append("    </bilancio>");
	sb.append("    <subdocumentoIvaSpesa>");
	sb.append("        <stato>VALIDO</stato>");
	sb.append("        <uid>0</uid>");
	sb.append("        <annoEsercizio>2013</annoEsercizio>");
	sb.append("        <dataProtocolloProvvisorio>2014-06-30T00:00:00+02:00</dataProtocolloProvvisorio>");
	sb.append("        <dataRegistrazione>2014-06-26T00:00:00+02:00</dataRegistrazione>");
	sb.append("        <descrizioneIva>Nota bene</descrizioneIva>");
	sb.append("        <ente>");
	sb.append("            <stato>VALIDO</stato>");
	sb.append("            <uid>1</uid>");
	sb.append("            <gestioneLivelli>");
	sb.append("                <entry>");
	sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
	sb.append("                    <value>GESTIONE_UEB</value>");
	sb.append("                </entry>");
	sb.append("            </gestioneLivelli>");
	sb.append("            <nome>CittÃ  di Torino</nome>");
	sb.append("        </ente>");
	sb.append("        <importoInValuta>0</importoInValuta>");
	sb.append("        <listaAliquotaSubdocumentoIva>");
	sb.append("            <stato>VALIDO</stato>");
	sb.append("            <uid>0</uid>");
	sb.append("            <aliquotaIva>");
	sb.append("                <stato>VALIDO</stato>");
	sb.append("                <uid>2</uid>");
	sb.append("                <annoCatalogazione>0</annoCatalogazione>");
	sb.append("                <codice>010</codice>");
	sb.append("                <descrizione>iva 10%</descrizione>");
	sb.append("                <ente>");
	sb.append("                    <stato>VALIDO</stato>");
	sb.append("                    <uid>1</uid>");
	sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
	sb.append("                    <gestioneLivelli/>");
	sb.append("                    <nome>CittÃ  di Torino</nome>");
	sb.append("                </ente>");
	sb.append("                <percentualeAliquota>10</percentualeAliquota>");
	sb.append("                <percentualeIndetraibilita>0</percentualeIndetraibilita>");
	sb.append("                <tipoOperazioneIva>NON_IMPONIBILE</tipoOperazioneIva>");
	sb.append("            </aliquotaIva>");
	sb.append("            <imponibile>0.00</imponibile>");
	sb.append("            <imposta>0.00</imposta>");
	sb.append("            <impostaDetraibile>0.00</impostaDetraibile>");
	sb.append("            <impostaIndetraibile>0.00</impostaIndetraibile>");
	sb.append("            <totale>0.00</totale>");
	sb.append("        </listaAliquotaSubdocumentoIva>");
	sb.append("        <registroIva>");
	sb.append("            <stato>VALIDO</stato>");
	sb.append("            <uid>16</uid>");
	sb.append("            <tipoRegistroIva>ACQUISTI_IVA_DIFFERITA</tipoRegistroIva>");
	sb.append("        </registroIva>");
	sb.append("        <statoSubdocumentoIva>PROVVISORIO</statoSubdocumentoIva>");
	sb.append("        <subdocumentoIvaPadre xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/fin2/data/1.0\" xsi:type=\"ns3:subdocumentoIvaSpesa\">");
	sb.append("            <stato>VALIDO</stato>");
	sb.append("            <uid>1</uid>");
	sb.append("            <importoInValuta>0</importoInValuta>");
	sb.append("            <totaleMovimentiIva>0</totaleMovimentiIva>");
	sb.append("        </subdocumentoIvaPadre>");
	sb.append("        <tipoRegistrazioneIva>");
	sb.append("            <stato>VALIDO</stato>");
	sb.append("            <uid>1</uid>");
	sb.append("            <annoCatalogazione>0</annoCatalogazione>");
	sb.append("            <flagTipoRegistrazioneIvaEntrata>false</flagTipoRegistrazioneIvaEntrata>");
	sb.append("            <flagTipoRegistrazioneIvaSpesa>false</flagTipoRegistrazioneIvaSpesa>");
	sb.append("        </tipoRegistrazioneIva>");
	sb.append("        <tipoRelazione>NOTA_CREDITO_IVA</tipoRelazione>");
	sb.append("        <totaleMovimentiIva>0</totaleMovimentiIva>");
	sb.append("    </subdocumentoIvaSpesa>");
	sb.append("</inserisceNotaCreditoIvaSpesa>");
			

	InserisceNotaCreditoIvaSpesaResponse res = inserisceNotaCreditoIvaSpesaService.executeService(JAXBUtility.unmarshall(sb.toString(), InserisceNotaCreditoIvaSpesa.class));
	assertNotNull(res);
}


	
	/**
	 * Inserisce subdocumento iva spesa.
	 */
	@Test
	public void inserisceSubdocumentoIvaSpesa() {
			
		InserisceSubdocumentoIvaSpesa req = new InserisceSubdocumentoIvaSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoIvaSpesa sd = new SubdocumentoIvaSpesa();
		
		sd.setEnte(getEnteTest());
		
		sd.setAnnoEsercizio(2013);
//		AttivitaIva attivitaIva = new AttivitaIva();
//		attivitaIva.setUid(1);
//		sd.setAttivitaIva(attivitaIva);
		
		sd.setDataCassaDocumento(new Date());
		sd.setDataOrdinativoDocumento(new Date());
//		sd.setDataProtocolloDefinitivo(new Date());
//		sd.setDataProtocolloProvvisorio(new Date());
		sd.setDataRegistrazione(new Date());
		sd.setDescrizioneIva("prova per numero protocollo");
		
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(1);
		sd.setDocumento(doc);
		
		/*
		 --ricerca documenti di spesa:
			select * from siac_t_doc
			where doc_tipo_id in (select doc_tipo_id from siac_d_doc_tipo
			where ente_proprietario_id = 1
			and doc_fam_tipo_id = 6
			)
		 */
		
//		DocumentoIva documentoIva = new DocumentoIva();
//		sd.setDocumentoIva(documentoIva);
		
		sd.setFlagIntracomunitario(Boolean.FALSE);
		sd.setFlagNotaCredito(Boolean.TRUE);
		sd.setFlagRegistrazioneIva(Boolean.TRUE);
		sd.setFlagRilevanteIRAP(Boolean.TRUE);
		sd.setFlagStampaDefinitivoDefinitivo(Boolean.TRUE);
		sd.setFlagStampaDefinitivoProvvisorio(Boolean.TRUE);
		
		sd.setImportoInValuta(new BigDecimal("217.33"));
		
//		sd.setListaAliquotaSubdocumentoIva(listaAliquotaSubdocumentoIva);
//		sd.setListaNoteDiCredito(notaDiCredito);
//		sd.setListaQuoteIvaDifferita(quoteIvaDifferita);
//		sd.setListaRegistroIva(listaRegistroIva);
//		sd.setListaSubdocumenti(listaSubdocumento);
		
		
//		sd.setNumeroOrdinativoDocumento(1);
//		sd.setNumeroProtocolloDefinitivo(2);
//		sd.setNumeroProtocolloProvvisorio(1);
//		sd.setProgressivoIVA(10);
		
		RegistroIva registroIva = new RegistroIva();
		registroIva.setUid(23);
		sd.setRegistroIva(registroIva);
		
		sd.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		
		TipoRegistrazioneIva tipoRegistrazioneIva = new TipoRegistrazioneIva();
		tipoRegistrazioneIva.setUid(1);
		sd.setTipoRegistrazioneIva(tipoRegistrazioneIva);
		
		//sd.setTipoRelazione(tipoRelazione);
		
		Valuta valuta = new Valuta();
		valuta.setUid(1); //Euro ente 1
		sd.setValuta(valuta);	
		
		List<AliquotaSubdocumentoIva> listaAsi = new ArrayList<AliquotaSubdocumentoIva>();
		AliquotaSubdocumentoIva asi = new AliquotaSubdocumentoIva();
		asi.setImponibile(new BigDecimal("12.3"));
		asi.setImposta(new BigDecimal("12.34"));
		asi.setTotale(new BigDecimal("123.45"));
		AliquotaIva aliquotaIva = new AliquotaIva();
		aliquotaIva.setUid(1);
		asi.setAliquotaIva(aliquotaIva);
		listaAsi.add(asi);
		sd.setListaAliquotaSubdocumentoIva(listaAsi);
		
		
		req.setSubdocumentoIvaSpesa(sd);
		
		InserisceSubdocumentoIvaSpesaResponse res = inserisceSubdocumentoIvaSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	@Test
	public void inserisceSubdocumentoIvaTest1(){

		StringBuilder sb = new StringBuilder();

		sb.append("<inserisceSubdocumentoIvaSpesa>");
		sb.append("    <dataOra>2014-09-25T12:43:12.198+02:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <nome>Demo 21</nome>");
		sb.append("            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>");
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
		sb.append("                <nome>CittÃ  di Torino</nome>");
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
		sb.append("    <bilancio>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>1</uid>");
		sb.append("        <anno>2013</anno>");
		sb.append("    </bilancio>");
		sb.append("    <subdocumentoIvaSpesa>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>0</uid>");
		sb.append("        <subdocumentoIvaEntrataControregistrazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <dataProtocolloDefinitivo>2014-09-25T00:00:00+02:00</dataProtocolloDefinitivo>");
		sb.append("            <importoInValuta>0</importoInValuta>");
		sb.append("            <registroIva>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>18</uid>");
		sb.append("                <tipoRegistroIva>VENDITE_IVA_IMMEDIATA</tipoRegistroIva>");
		sb.append("            </registroIva>");
		sb.append("            <tipoRegistrazioneIva>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>3</uid>");
		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
		sb.append("                <flagTipoRegistrazioneIvaEntrata>false</flagTipoRegistrazioneIvaEntrata>");
		sb.append("                <flagTipoRegistrazioneIvaSpesa>false</flagTipoRegistrazioneIvaSpesa>");
		sb.append("            </tipoRegistrazioneIva>");
		sb.append("            <totaleMovimentiIva>0</totaleMovimentiIva>");
		sb.append("        </subdocumentoIvaEntrataControregistrazione>");
		sb.append("        <documentoSpesa>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>103</uid>");
		sb.append("            <dataModifica>2014-09-25T12:39:01.833+02:00</dataModifica>");
		sb.append("            <loginCreazione>AAAAAA00A11B000J</loginCreazione>");
		sb.append("            <loginModifica>AAAAAA00A11B000J</loginModifica>");
		sb.append("            <loginOperazione>AAAAAA00A11B000J</loginOperazione>");
		sb.append("            <subdocumentoSpesa>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>117</uid>");
		sb.append("                <dataModifica>2014-09-25T12:39:02.753+02:00</dataModifica>");
		sb.append("                <loginCreazione>AAAAAA00A11B000J</loginCreazione>");
		sb.append("                <loginModifica>AAAAAA00A11B000J</loginModifica>");
		sb.append("                <loginOperazione>AAAAAA00A11B000J</loginOperazione>");
		sb.append("                <documentoSpesa>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>103</uid>");
		sb.append("                    <dataModifica>2014-09-25T12:39:01.833+02:00</dataModifica>");
		sb.append("                    <loginCreazione>AAAAAA00A11B000J</loginCreazione>");
		sb.append("                    <loginModifica>AAAAAA00A11B000J</loginModifica>");
		sb.append("                    <loginOperazione>AAAAAA00A11B000J</loginOperazione>");
		sb.append("                    <anno>2013</anno>");
		sb.append("                    <arrotondamento>0</arrotondamento>");
		sb.append("                    <dataEmissione>2013-01-01T00:00:00+01:00</dataEmissione>");
		sb.append("                    <descrizione>cento SIAC-1242</descrizione>");
		sb.append("                    <ente>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>1</uid>");
		sb.append("                        <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                        <gestioneLivelli/>");
		sb.append("                        <nome>CittÃ  di Torino</nome>");
		sb.append("                    </ente>");
		sb.append("                    <importo>100.00</importo>");
		sb.append("                    <importoTotaleNoteCollegate>0</importoTotaleNoteCollegate>");
		sb.append("                    <numero>SIAC-1242</numero>");
		sb.append("                    <soggetto>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>7</uid>");
		sb.append("                        <avviso>false</avviso>");
		sb.append("                        <codiceFiscale>TRTMSM58L26L219F</codiceFiscale>");
		sb.append("                        <codiceSoggetto>1</codiceSoggetto>");
		sb.append("                        <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("                        <denominazione>TORTA MASSIMO</denominazione>");
		sb.append("                        <partitaIva></partitaIva>");
		sb.append("                        <residenteEstero>false</residenteEstero>");
		sb.append("                        <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("                    </soggetto>");
		sb.append("                    <tipoDocumento>");
		sb.append("                        <stato>VALIDO</stato>");
		sb.append("                        <uid>187</uid>");
		sb.append("                        <annoCatalogazione>0</annoCatalogazione>");
		sb.append("                        <codice>IVS</codice>");
		sb.append("                        <descrizione>IVA SPESA</descrizione>");
		sb.append("                        <tipoFamigliaDocumento>IVA_SPESA</tipoFamigliaDocumento>");
		sb.append("                    </tipoDocumento>");
		sb.append("                    <flagBeneficiarioMultiplo>false</flagBeneficiarioMultiplo>");
		sb.append("                </documentoSpesa>");
		sb.append("                <descrizione>cento SIAC-1242</descrizione>");
		sb.append("                <ente>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <flagAvviso>false</flagAvviso>");
		sb.append("                <flagConvalidaManuale>false</flagConvalidaManuale>");
		sb.append("                <flagEsproprio>false</flagEsproprio>");
		sb.append("                <flagOrdinativoManuale>false</flagOrdinativoManuale>");
		sb.append("                <flagOrdinativoSingolo>false</flagOrdinativoSingolo>");
		sb.append("                <flagRilevanteIVA>true</flagRilevanteIVA>");
		sb.append("                <importo>100.00</importo>");
		sb.append("                <importoDaDedurre>0</importoDaDedurre>");
		sb.append("                <numero>1</numero>");
		sb.append("                <importoDaPagare>0</importoDaPagare>");
		sb.append("            </subdocumentoSpesa>");
		sb.append("            <anno>2013</anno>");
		sb.append("            <arrotondamento>0</arrotondamento>");
		sb.append("            <dataEmissione>2013-01-01T00:00:00+01:00</dataEmissione>");
		sb.append("            <dataInizioValiditaStato>2014-09-25T12:39:01.833+02:00</dataInizioValiditaStato>");
		sb.append("            <descrizione>cento SIAC-1242</descrizione>");
		sb.append("            <ente>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                <gestioneLivelli/>");
		sb.append("                <nome>CittÃ  di Torino</nome>");
		sb.append("            </ente>");
		sb.append("            <importo>100.00</importo>");
		sb.append("            <importoTotaleNoteCollegate>0</importoTotaleNoteCollegate>");
		sb.append("            <numero>SIAC-1242</numero>");
		sb.append("            <soggetto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>7</uid>");
		sb.append("                <avviso>false</avviso>");
		sb.append("                <codiceFiscale>TRTMSM58L26L219F</codiceFiscale>");
		sb.append("                <codiceSoggetto>1</codiceSoggetto>");
		sb.append("                <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("                <denominazione>TORTA MASSIMO</denominazione>");
		sb.append("                <partitaIva></partitaIva>");
		sb.append("                <residenteEstero>false</residenteEstero>");
		sb.append("                <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("            </soggetto>");
		sb.append("            <statoOperativoDocumento>INCOMPLETO</statoOperativoDocumento>");
		sb.append("            <tipoDocumento>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>187</uid>");
		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
		sb.append("                <codice>IVS</codice>");
		sb.append("                <descrizione>IVA SPESA</descrizione>");
		sb.append("                <tipoFamigliaDocumento>IVA_SPESA</tipoFamigliaDocumento>");
		sb.append("            </tipoDocumento>");
		sb.append("            <flagBeneficiarioMultiplo>false</flagBeneficiarioMultiplo>");
		sb.append("        </documentoSpesa>");
		sb.append("        <annoEsercizio>2013</annoEsercizio>");
		sb.append("        <dataProtocolloDefinitivo>2014-09-25T00:00:00+02:00</dataProtocolloDefinitivo>");
		sb.append("        <dataRegistrazione>2014-09-25T00:00:00+02:00</dataRegistrazione>");
		sb.append("        <descrizioneIva></descrizioneIva>");
		sb.append("        <ente>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <gestioneLivelli>");
		sb.append("                <entry>");
		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                    <value>GESTIONE_UEB</value>");
		sb.append("                </entry>");
		sb.append("            </gestioneLivelli>");
		sb.append("            <nome>CittÃ  di Torino</nome>");
		sb.append("        </ente>");
		sb.append("        <flagIntracomunitario>true</flagIntracomunitario>");
		sb.append("        <importoInValuta>999.00</importoInValuta>");
		sb.append("        <listaAliquotaSubdocumentoIva>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <aliquotaIva>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
		sb.append("                <codice>004</codice>");
		sb.append("                <descrizione>iva 4%</descrizione>");
		sb.append("                <ente>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <percentualeAliquota>4</percentualeAliquota>");
		sb.append("                <percentualeIndetraibilita>0</percentualeIndetraibilita>");
		sb.append("                <tipoOperazioneIva>IMPONIBILE</tipoOperazioneIva>");
		sb.append("            </aliquotaIva>");
		sb.append("            <imponibile>31.73</imponibile>");
		sb.append("            <imposta>1.27</imposta>");
		sb.append("            <impostaDetraibile>1.27</impostaDetraibile>");
		sb.append("            <impostaIndetraibile>0.00</impostaIndetraibile>");
		sb.append("            <totale>33.00</totale>");
		sb.append("        </listaAliquotaSubdocumentoIva>");
		sb.append("        <registroIva>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>33</uid>");
		sb.append("            <tipoRegistroIva>ACQUISTI_IVA_IMMEDIATA</tipoRegistroIva>");
		sb.append("        </registroIva>");
		sb.append("        <statoSubdocumentoIva>PROVVISORIO</statoSubdocumentoIva>");
		sb.append("        <tipoRegistrazioneIva>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>3</uid>");
		sb.append("            <annoCatalogazione>0</annoCatalogazione>");
		sb.append("            <flagTipoRegistrazioneIvaEntrata>false</flagTipoRegistrazioneIvaEntrata>");
		sb.append("            <flagTipoRegistrazioneIvaSpesa>false</flagTipoRegistrazioneIvaSpesa>");
		sb.append("        </tipoRegistrazioneIva>");
		sb.append("        <totaleMovimentiIva>0</totaleMovimentiIva>");
		sb.append("        <valuta>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>41</uid>");
		sb.append("            <annoCatalogazione>0</annoCatalogazione>");
		sb.append("        </valuta>");
		sb.append("    </subdocumentoIvaSpesa>");
		sb.append("</inserisceSubdocumentoIvaSpesa>");
				
		InserisceSubdocumentoIvaSpesa req = JAXBUtility.unmarshall(sb.toString(), InserisceSubdocumentoIvaSpesa.class);
		
		System.out.println("subdocumento iva di controregistrazione: "+ req.getSubdocumentoIvaSpesa().getSubdocumentoIvaEntrata());
		
		
		InserisceSubdocumentoIvaSpesaResponse res = inserisceSubdocumentoIvaSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	public static void main(String[] args) {
		
		InserisceSubdocumentoIvaSpesa req = new InserisceSubdocumentoIvaSpesa();
		
		
		SubdocumentoIvaSpesa sis = new SubdocumentoIvaSpesa();
		SubdocumentoIvaEntrata sie = new SubdocumentoIvaEntrata();
		RegistroIva ri = new RegistroIva();
		ri.setCodice("vi");
		ri.setDescrizione("va");
		sie.setRegistroIva(ri);
		sis.setSubdocumentoIvaEntrata(sie);
		
		req.setSubdocumentoIvaSpesa(sis);
		
		String reqXml = JAXBUtility.marshall(req);
		
		System.out.println(reqXml);
		
		
		InserisceSubdocumentoIvaSpesa res = JAXBUtility.unmarshall(JAXBUtility.marshall(req), InserisceSubdocumentoIvaSpesa.class);
		
		System.out.println("subdocumento iva di controregistrazione: "+ res.getSubdocumentoIvaSpesa().getSubdocumentoIvaEntrata());
		
	}
	
	
	
	/**
	 * Aggiorna subdocumento iva spesa.
	 */
	@Test
	public void aggiornaSubdocumentoIvaSpesa() {
		
		AggiornaSubdocumentoIvaSpesa req = new AggiornaSubdocumentoIvaSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoIvaSpesa sd = new SubdocumentoIvaSpesa();
		sd.setUid(1);
		
		sd.setEnte(getEnteTest());
		
		sd.setAnnoEsercizio(2013);
		AttivitaIva attivitaIva = new AttivitaIva();
		attivitaIva.setUid(1);
		sd.setAttivitaIva(attivitaIva);
		
		sd.setDataCassaDocumento(new Date());
		sd.setDataOrdinativoDocumento(new Date());
		sd.setDataProtocolloDefinitivo(new Date());
		sd.setDataProtocolloProvvisorio(new Date());
		sd.setDataRegistrazione(new Date());
		sd.setDescrizioneIva("mia desc iva");
		
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(1);
		sd.setDocumento(doc);
		
		/*
		 --ricerca documenti di spesa:
			select * from siac_t_doc
			where doc_tipo_id in (select doc_tipo_id from siac_d_doc_tipo
			where ente_proprietario_id = 1
			and doc_fam_tipo_id = 6
			)
		 */
		
//		DocumentoIva documentoIva = new DocumentoIva();
//		sd.setDocumentoIva(documentoIva);
		
		sd.setFlagIntracomunitario(Boolean.TRUE);
		sd.setFlagNotaCredito(Boolean.TRUE);
		sd.setFlagRegistrazioneIva(Boolean.TRUE);
		sd.setFlagRilevanteIRAP(Boolean.TRUE);
		sd.setFlagStampaDefinitivoDefinitivo(Boolean.TRUE);
		sd.setFlagStampaDefinitivoProvvisorio(Boolean.TRUE);
		
		sd.setImportoInValuta(new BigDecimal("217.33"));
		
//		sd.setListaAliquotaSubdocumentoIva(listaAliquotaSubdocumentoIva);
//		sd.setListaNoteDiCredito(notaDiCredito);
//		sd.setListaQuoteIvaDifferita(quoteIvaDifferita);
//		sd.setListaRegistroIva(listaRegistroIva);
//		sd.setListaSubdocumenti(listaSubdocumento);
		
		
		sd.setNumeroOrdinativoDocumento(1);
		sd.setNumeroProtocolloDefinitivo(2);
		sd.setNumeroProtocolloProvvisorio(1);
		sd.setProgressivoIVA(10);
		
		RegistroIva registroIva = new RegistroIva();
		registroIva.setUid(3);
		sd.setRegistroIva(registroIva);
		
		sd.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		
		TipoRegistrazioneIva tipoRegistrazioneIva = new TipoRegistrazioneIva();
		tipoRegistrazioneIva.setUid(1);
		sd.setTipoRegistrazioneIva(tipoRegistrazioneIva);
		
		//sd.setTipoRelazione(tipoRelazione);
		
		Valuta valuta = new Valuta();
		valuta.setUid(49); //Euro ente 1
		sd.setValuta(valuta);	
		
		req.setSubdocumentoIvaSpesa(sd);
		
		AggiornaSubdocumentoIvaSpesaResponse res = aggiornaSubdocumentoIvaSpesaService.executeService(req);

		assertNotNull(res);
	
		

	}

	
	
	
	
	@Test
	public void aggiornaSubdocumentoIvaSpesa2(){

		StringBuilder sb = new StringBuilder();

		sb.append("<aggiornaSubdocumentoIvaSpesa>");
		sb.append("    <dataOra>2014-06-17T16:36:32.192+02:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <nome>Demo 21</nome>");
		sb.append("            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>");
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
		sb.append("                <nome>CittÃ  di Torino</nome>");
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
		sb.append("    <bilancio>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>1</uid>");
		sb.append("        <anno>2013</anno>");
		sb.append("    </bilancio>");
		sb.append("    <subdocumentoIvaSpesa>");
		sb.append("        <dataCreazione>2014-06-16+02:00</dataCreazione>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>1</uid>");
		sb.append("        <annoEsercizio>2013</annoEsercizio>");
		sb.append("        <dataProtocolloProvvisorio>2014-06-16T00:00:00+02:00</dataProtocolloProvvisorio>");
		sb.append("        <dataRegistrazione>2014-06-16T00:00:00+02:00</dataRegistrazione>");
		sb.append("        <descrizioneIva>Descrizione per SIS di test 1</descrizioneIva>");
		sb.append("        <documento xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/fin2/data/1.0\" xsi:type=\"ns3:documentoSpesa\">");
		sb.append("            <dataCreazione>2014-06-05+02:00</dataCreazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <dataModifica>2014-06-09T13:04:15.507+02:00</dataModifica>");
		sb.append("            <loginCreazione>AAAAAA00A11B000J</loginCreazione>");
		sb.append("            <loginModifica>AAAAAA00A11B000J</loginModifica>");
		sb.append("            <loginOperazione>AAAAAA00A11B000J</loginOperazione>");
		sb.append("            <anno>2013</anno>");
		sb.append("            <arrotondamento>0.00</arrotondamento>");
		sb.append("            <codiceBollo>");
		sb.append("                <dataCreazione>2014-02-28+01:00</dataCreazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>6</uid>");
		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
		sb.append("                <codice>77</codice>");
		sb.append("                <descrizione>IVA ASSOLTA</descrizione>");
		sb.append("            </codiceBollo>");
		sb.append("            <dataEmissione>2013-04-17T00:00:00+02:00</dataEmissione>");
		sb.append("            <dataInizioValiditaStato>2014-06-05T18:00:08.493+02:00</dataInizioValiditaStato>");
		sb.append("            <dataScadenza>2014-05-21T00:00:00+02:00</dataScadenza>");
		sb.append("            <descrizione>documento di prova</descrizione>");
		sb.append("            <ente>");
		sb.append("                <dataCreazione>2013-05-20+02:00</dataCreazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                <gestioneLivelli/>");
		sb.append("                <nome>CittÃ  di Torino</nome>");
		sb.append("            </ente>");
		sb.append("            <importo>30000.00</importo>");
		sb.append("            <importoTotaleNoteCollegate>0</importoTotaleNoteCollegate>");
		sb.append("            <listaSubdocumenti>");
		sb.append("                <dataCreazione>2014-06-05+02:00</dataCreazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <dataModifica>2014-06-05T18:00:09.150+02:00</dataModifica>");
		sb.append("                <loginCreazione>AAAAAA00A11B000J</loginCreazione>");
		sb.append("                <loginModifica>AAAAAA00A11B000J</loginModifica>");
		sb.append("                <loginOperazione>AAAAAA00A11B000J</loginOperazione>");
		sb.append("                <dataScadenza>2014-05-21T00:00:00+02:00</dataScadenza>");
		sb.append("                <descrizione>documento di prova</descrizione>");
		sb.append("                <documento>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <arrotondamento>0</arrotondamento>");
		sb.append("                    <importo>0</importo>");
		sb.append("                    <importoTotaleNoteCollegate>0</importoTotaleNoteCollegate>");
		sb.append("                </documento>");
		sb.append("                <ente>");
		sb.append("                    <dataCreazione>2013-05-20+02:00</dataCreazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <flagAvviso>false</flagAvviso>");
		sb.append("                <flagConvalidaManuale>false</flagConvalidaManuale>");
		sb.append("                <flagEsproprio>false</flagEsproprio>");
		sb.append("                <flagOrdinativoManuale>false</flagOrdinativoManuale>");
		sb.append("                <flagOrdinativoSingolo>false</flagOrdinativoSingolo>");
		sb.append("                <flagRilevanteIVA>false</flagRilevanteIVA>");
		sb.append("                <importo>23000.00</importo>");
		sb.append("                <importoDaDedurre>0</importoDaDedurre>");
		sb.append("                <numero>1</numero>");
		sb.append("            </listaSubdocumenti>");
		sb.append("            <note></note>");
		sb.append("            <numero>2345</numero>");
		sb.append("            <numeroRepertorio></numeroRepertorio>");
		sb.append("            <soggetto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>7</uid>");
		sb.append("                <codiceFiscale>TRTMSM58L26L219F</codiceFiscale>");
		sb.append("                <codiceSoggetto>1</codiceSoggetto>");
		sb.append("                <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("                <denominazione>TORTA MASSIMO</denominazione>");
		sb.append("                <partitaIva></partitaIva>");
		sb.append("                <residenteEstero>false</residenteEstero>");
		sb.append("                <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("            </soggetto>");
		sb.append("            <statoOperativoDocumento>INCOMPLETO</statoOperativoDocumento>");
		sb.append("            <terminePagamento>34</terminePagamento>");
		sb.append("            <tipoDocumento>");
		sb.append("                <dataCreazione>2014-03-19+01:00</dataCreazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
		sb.append("                <codice>AAM</codice>");
		sb.append("                <descrizione>BOLLETTE AAM</descrizione>");
		sb.append("                <flagIVA>true</flagIVA>");
		sb.append("                <flagNotaCredito>false</flagNotaCredito>");
		sb.append("                <flagPenaleAltro>false</flagPenaleAltro>");
		sb.append("                <flagRegolarizzazione>false</flagRegolarizzazione>");
		sb.append("                <flagRitenute>false</flagRitenute>");
		sb.append("                <flagSpesaCollegata>false</flagSpesaCollegata>");
		sb.append("                <flagSubordinato>false</flagSubordinato>");
		sb.append("            </tipoDocumento>");
		sb.append("            <causaleSospensione></causaleSospensione>");
		sb.append("            <codiceFiscalePignorato></codiceFiscalePignorato>");
		sb.append("            <flagBeneficiarioMultiplo>false</flagBeneficiarioMultiplo>");
		sb.append("            <tipoImpresa>");
		sb.append("                <dataCreazione>2014-03-13+01:00</dataCreazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>41168</uid>");
		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
		sb.append("                <codice>RTI</codice>");
		sb.append("                <descrizione>Raggruppamento temporaneo di imprese</descrizione>");
		sb.append("                <tipoClassificatore>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>0</uid>");
		sb.append("                    <codice>TIPO_IMPRESA</codice>");
		sb.append("                    <descrizione>Tipo impresa</descrizione>");
		sb.append("                </tipoClassificatore>");
		sb.append("            </tipoImpresa>");
		sb.append("        </documento>");
		sb.append("        <ente>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <gestioneLivelli>");
		sb.append("                <entry>");
		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                    <value>GESTIONE_UEB</value>");
		sb.append("                </entry>");
		sb.append("            </gestioneLivelli>");
		sb.append("            <nome>CittÃ  di Torino</nome>");
		sb.append("        </ente>");
		sb.append("        <flagIntracomunitario>false</flagIntracomunitario>");
		sb.append("        <flagNotaCredito>false</flagNotaCredito>");
		sb.append("        <flagRegistrazioneIva>false</flagRegistrazioneIva>");
		sb.append("        <flagRilevanteIRAP>true</flagRilevanteIRAP>");
		sb.append("        <importoInValuta>0</importoInValuta>");
		sb.append("        <listaAliquotaSubdocumentoIva>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <aliquotaIva>");
		sb.append("                <dataCreazione>2014-05-27+02:00</dataCreazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
		sb.append("                <codice>004</codice>");
		sb.append("                <descrizione>iva 4%</descrizione>");
		sb.append("                <ente>");
		sb.append("                    <dataCreazione>2013-05-20+02:00</dataCreazione>");
		sb.append("                    <stato>VALIDO</stato>");
		sb.append("                    <uid>1</uid>");
		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                    <gestioneLivelli/>");
		sb.append("                    <nome>CittÃ  di Torino</nome>");
		sb.append("                </ente>");
		sb.append("                <percentualeAliquota>4</percentualeAliquota>");
		sb.append("                <percentualeIndetraibilita>0</percentualeIndetraibilita>");
		sb.append("                <tipoOperazioneIva>IMPONIBILE</tipoOperazioneIva>");
		sb.append("            </aliquotaIva>");
		sb.append("            <imponibile>0.00</imponibile>");
		sb.append("            <imposta>0.00</imposta>");
		sb.append("            <impostaDetraibile>0.00</impostaDetraibile>");
		sb.append("            <impostaIndetraibile>0.00</impostaIndetraibile>");
		sb.append("            <totale>0.00</totale>");
		sb.append("        </listaAliquotaSubdocumentoIva>");
		sb.append("        <progressivoIVA>1</progressivoIVA>");
		sb.append("        <registroIva>");
		sb.append("            <dataCreazione>2014-06-10+02:00</dataCreazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>17</uid>");
		sb.append("            <codice>codice registro</codice>");
		sb.append("            <descrizione>descrizione registro</descrizione>");
		sb.append("            <ente>");
		sb.append("                <dataCreazione>2013-05-20+02:00</dataCreazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                <gestioneLivelli/>");
		sb.append("                <nome>CittÃ  di Torino</nome>");
		sb.append("            </ente>");
		sb.append("            <tipoRegistroIva>ACQUISTI_IVA_DIFFERITA</tipoRegistroIva>");
		sb.append("        </registroIva>");
		sb.append("        <statoSubdocumentoIva>PROVVISORIO</statoSubdocumentoIva>");
		sb.append("        <tipoRegistrazioneIva>");
		sb.append("            <dataCreazione>2014-06-09+02:00</dataCreazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <annoCatalogazione>0</annoCatalogazione>");
		sb.append("            <codice>01</codice>");
		sb.append("            <descrizione>Normale</descrizione>");
		sb.append("            <ente>");
		sb.append("                <dataCreazione>2013-05-20+02:00</dataCreazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
		sb.append("                <gestioneLivelli/>");
		sb.append("                <nome>CittÃ  di Torino</nome>");
		sb.append("            </ente>");
		sb.append("            <flagTipoRegistrazioneIvaEntrata>true</flagTipoRegistrazioneIvaEntrata>");
		sb.append("            <flagTipoRegistrazioneIvaSpesa>true</flagTipoRegistrazioneIvaSpesa>");
		sb.append("        </tipoRegistrazioneIva>");
		sb.append("    </subdocumentoIvaSpesa>");
		sb.append("</aggiornaSubdocumentoIvaSpesa>");
				
		AggiornaSubdocumentoIvaSpesa req = JAXBUtility.unmarshall(sb.toString(), AggiornaSubdocumentoIvaSpesa.class);
		
		AggiornaSubdocumentoIvaSpesaResponse res = aggiornaSubdocumentoIvaSpesaService.executeService(req);

		assertNotNull(res);
				
	}
	
	
	@Test
	public void aggiornaStatoSubdocumentoIvaSpesa() {
		
		AggiornaStatoSubdocumentoIvaSpesa req = new AggiornaStatoSubdocumentoIvaSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancioTest());
		req.setDataOra(new Date());
		SubdocumentoSpesa sd = new SubdocumentoSpesa();
			
		sd.setEnte(getEnteTest());
//		sd.setNumeroOrdinativo(222);
//		sd.setDataOrdinativo(new Date());
		sd.setNumeroRegistrazioneIVA("2013/2");
		
		req.setSubdocumentoSpesa(sd);
		
		AggiornaStatoSubdocumentoIvaSpesaResponse res = aggiornaStatoSubdocumentoIvaSpesaService.executeService(req);

		assertNotNull(res);
		
		
	}
	
	@Autowired
	private ContatoreRegistroIvaDad contatoreRegistroIvaDad;
	
	@Autowired
	private ApplicationContext appCtx;
	
	@Test
	public void testProtDef() throws InterruptedException{
		Calendar c = Calendar.getInstance();
		c.set(2015, 0, 27);
		contatoreRegistroIvaDad.setLoginOperazione("miaNonna");
//		Integer nPrec = contatoreRegistroIvaDad.staccaNumeroProtocolloDef(7, 121, null, c.getTime());
//		
//		for(int i=0 ; i<10; i++){
//			Integer n = contatoreRegistroIvaDad.staccaNumeroProtocolloDef(7, 121, null, c.getTime());
//			System.out.println(">>>>>>>>>>>>>>>>>>>  n:" +n + " nPrec: "+ nPrec);
//			assertTrue(n == nPrec+1);
//			nPrec = n;
//		}
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
            Runnable worker = new ProtDefThread(i, appCtx, 7, 121, null, c.getTime());
            executor.execute(worker);
         }
		
		executor.awaitTermination(1, TimeUnit.DAYS);
		
	}
	
	class ProtDefThread implements Runnable {
		
		Integer i;
		ApplicationContext appCtx;
		Integer uidRegistroIva;
		Integer uidPeriodo;
		Integer uidPeriodoPrec;
		Date dataProtocolloDef;
		
		public ProtDefThread(Integer i, ApplicationContext appCtx, Integer uidRegistroIva, Integer uidPeriodo, Integer uidPeriodoPrec,
				Date dataProtocolloDef) {
			this.i = i;
			this.appCtx = appCtx;
			this.uidRegistroIva = uidRegistroIva;
			this.uidPeriodo = uidPeriodo;
			this.uidPeriodoPrec = uidPeriodoPrec;
			this.dataProtocolloDef = dataProtocolloDef;
		}



		@Override
		public void run() {
			ContatoreRegistroIvaDad contatoreRegistroIvaDad = appCtx.getBean(ContatoreRegistroIvaDad.class);
			contatoreRegistroIvaDad.setLoginOperazione("miaNonna "+ i);
//			Integer n = contatoreRegistroIvaDad.staccaNumeroProtocolloDef(uidRegistroIva, uidPeriodo, uidPeriodoPrec, dataProtocolloDef);
//			System.out.println(">>>>>>>>"+i+">>>>>>>>>>>  n:" +n );
		}
		
	}
	     
	
}
