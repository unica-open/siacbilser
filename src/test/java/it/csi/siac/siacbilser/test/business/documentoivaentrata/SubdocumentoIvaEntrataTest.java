/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentoivaentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documentoivaentrata.AggiornaStatoSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.InserisceSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.RicercaSinteticaSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.Valuta;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoIvaSpesaDLTest.
 */
public class SubdocumentoIvaEntrataTest extends BaseJunit4TestCase {
	
	
	/** The inserisce subdocumento iva entrata service. */
	@Autowired
	private InserisceSubdocumentoIvaEntrataService inserisceSubdocumentoIvaEntrataService;
//	
//	/** The aggiorna subdocumento iva entrata service. */
//	@Autowired
//	private AggiornaSubdocumentoIvaEntrataService aggiornaSubdocumentoIvaEntrataService;
//	
//	/** The ricerca dettaglio subdocumento iva entrata service. */
//	@Autowired
//	private RicercaDettaglioSubdocumentoIvaEntrataService ricercaDettaglioSubdocumentoIvaEntrataService;
//	
	/** The ricerca sintetica subdocumento iva entrata service. */
	@Autowired
	private RicercaSinteticaSubdocumentoIvaEntrataService ricercaSinteticaSubdocumentoIvaEntrataService;
	
	@Autowired
	private AggiornaStatoSubdocumentoIvaEntrataService aggiornaStatoSubdocumentoIvaEntrataService;

	
//	/**
//	 * Ricerca dettaglio subdocumento iva spesa.
//	 */
//	@Test
//	public void ricercaDettaglioSubdocumentoIvaSpesa() {
//		
//		
//		RicercaDettaglioSubdocumentoIvaSpesa req = new RicercaDettaglioSubdocumentoIvaSpesa();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		SubdocumentoIvaSpesa sd = new SubdocumentoIvaSpesa();
//		sd.setUid(1);
//		req.setSubdocumentoIvaSpesa(sd);
//		
//		RicercaDettaglioSubdocumentoIvaSpesaResponse res = ricercaDettaglioSubdocumentoIvaSpesaService.executeService(req);
//				
//				
//	}
	
	
	
	/**
	 * Ricerca sintetica subdocumento iva entrata.
	 */
	@Test
	public void ricercaSinteticaSubdocumentoIvaEntrata() {
		
		
		RicercaSinteticaSubdocumentoIvaEntrata req = new RicercaSinteticaSubdocumentoIvaEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		SubdocumentoIvaEntrata sd = new SubdocumentoIvaEntrata();
		sd.setEnte(getEnteTest());
		AttivitaIva attivitaIva = new AttivitaIva();
		attivitaIva.setUid(1);
		sd.setAttivitaIva(attivitaIva);
//		RegistroIva registroIva= new RegistroIva();
//		registroIva.setUid(18);
//		sd.setRegistroIva(registroIva);
		req.setSubdocumentoIvaEntrata(sd);
		
		RicercaSinteticaSubdocumentoIvaEntrataResponse res = ricercaSinteticaSubdocumentoIvaEntrataService.executeService(req);
		
		assertNotNull(res);
	}
	
	
//	
//	/**
//	 * Ricerca puntuale subdocumento iva spesa.
//	 */
//	@Test
//	public void ricercaPuntualeSubdocumentoIvaSpesa() {
//		
//		
//		RicercaPuntualeSubdocumentoIvaSpesa req = new RicercaPuntualeSubdocumentoIvaSpesa();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		SubdocumentoIvaSpesa sd = new SubdocumentoIvaSpesa();
//		sd.setEnte(getEnteTest());
//		
//		sd.setAnnoEsercizio(2013);
//		sd.setProgressivoIVA(10);
//		
//		
//		
//		req.setSubdocumentoIvaSpesa(sd);
//		
//		RicercaPuntualeSubdocumentoIvaSpesaResponse res = ricercaPuntualeSubdocumentoIvaSpesaService.executeService(req);
//				
//				
//	}
//
//
//
//	
//	/**
//	 * Inserisce subdocumento iva spesa.
//	 */
	@Test
	public void inserisceSubdocumentoIvaEntrata() {
			
		InserisceSubdocumentoIvaEntrata req = new InserisceSubdocumentoIvaEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoIvaEntrata sd = new SubdocumentoIvaEntrata();
		
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
		
//		Documento doc = new Documento();
//		doc.setUid(1);
//		sd.setDocumento(doc);
		
		
		Subdocumento<?, ?> subdocumento = new SubdocumentoSpesa();
		subdocumento.setUid(12);
//		sd.setSubdocumento(subdocumento);
		
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
		registroIva.setUid(6);
		registroIva.setTipoRegistroIva(TipoRegistroIva.VENDITE_IVA_DIFFERITA);
		sd.setRegistroIva(registroIva);
		
		sd.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		
		TipoRegistrazioneIva tipoRegistrazioneIva = new TipoRegistrazioneIva();
		tipoRegistrazioneIva.setUid(1);
		sd.setTipoRegistrazioneIva(tipoRegistrazioneIva);
		
		//sd.setTipoRelazione(tipoRelazione);
		
		Valuta valuta = new Valuta();
		valuta.setUid(4); //Euro ente 1
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
		
		req.setSubdocumentoIvaEntrata(sd);
		
		InserisceSubdocumentoIvaEntrataResponse res = inserisceSubdocumentoIvaEntrataService.executeService(req);

		assertNotNull(res);
	}
	
//	
//	/**
//	 * Aggiorna subdocumento iva spesa.
//	 */
//	@Test
//	public void aggiornaSubdocumentoIvaSpesa() {
//		
//		AggiornaSubdocumentoIvaSpesa req = new AggiornaSubdocumentoIvaSpesa();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		SubdocumentoIvaSpesa sd = new SubdocumentoIvaSpesa();
//		sd.setUid(1);
//		
//		sd.setEnte(getEnteTest());
//		
//		sd.setAnnoEsercizio(2013);
//		AttivitaIva attivitaIva = new AttivitaIva();
//		attivitaIva.setUid(1);
//		sd.setAttivitaIva(attivitaIva);
//		
//		sd.setDataCassaDocumento(new Date());
//		sd.setDataOrdinativoDocumento(new Date());
//		sd.setDataProtocolloDefinitivo(new Date());
//		sd.setDataProtocolloProvvisorio(new Date());
//		sd.setDataRegistrazione(new Date());
//		sd.setDescrizioneIva("mia desc iva");
//		
//		Documento doc = new Documento();
//		doc.setUid(1);
//		sd.setDocumento(doc);
//		
//		/*
//		 --ricerca documenti di spesa:
//			select * from siac_t_doc
//			where doc_tipo_id in (select doc_tipo_id from siac_d_doc_tipo
//			where ente_proprietario_id = 1
//			and doc_fam_tipo_id = 6
//			)
//		 */
//		
//		DocumentoIva documentoIva = new DocumentoIva();
//		sd.setDocumentoIva(documentoIva);
//		
//		sd.setFlagIntracomunitario(Boolean.TRUE);
//		sd.setFlagNotaCredito(Boolean.TRUE);
//		sd.setFlagRegistrazioneIva(Boolean.TRUE);
//		sd.setFlagRilevanteIRAP(Boolean.TRUE);
//		sd.setFlagStampaDefinitivoDefinitivo(Boolean.TRUE);
//		sd.setFlagStampaDefinitivoProvvisorio(Boolean.TRUE);
//		
//		sd.setImportoInValuta(new BigDecimal("217.33"));
//		
////		sd.setListaAliquotaSubdocumentoIva(listaAliquotaSubdocumentoIva);
////		sd.setListaNoteDiCredito(notaDiCredito);
////		sd.setListaQuoteIvaDifferita(quoteIvaDifferita);
////		sd.setListaRegistroIva(listaRegistroIva);
////		sd.setListaSubdocumenti(listaSubdocumento);
//		
//		
//		sd.setNumeroOrdinativoDocumento(1);
//		sd.setNumeroProtocolloDefinitivo(2);
//		sd.setNumeroProtocolloProvvisorio(1);
//		sd.setProgressivoIVA(10);
//		
//		RegistroIva registroIva = new RegistroIva();
//		registroIva.setUid(3);
//		sd.setRegistroIva(registroIva);
//		
//		sd.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
//		
//		TipoRegistrazioneIva tipoRegistrazioneIva = new TipoRegistrazioneIva();
//		tipoRegistrazioneIva.setUid(1);
//		sd.setTipoRegistrazioneIva(tipoRegistrazioneIva);
//		
//		//sd.setTipoRelazione(tipoRelazione);
//		
//		Valuta valuta = new Valuta();
//		valuta.setUid(49); //Euro ente 1
//		sd.setValuta(valuta);	
//		
//		req.setSubdocumentoIvaSpesa(sd);
//		
//		AggiornaSubdocumentoIvaSpesaResponse res = aggiornaSubdocumentoIvaSpesaService.executeService(req);
//
//		assertNotNull(res);
//	
//		
//
//	}
//
//	
//	
//	
//	
//	@Test
//	public void aggiornaSubdocumentoIvaSpesa2(){
//
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("<aggiornaSubdocumentoIvaSpesa>");
//		sb.append("    <dataOra>2014-06-17T16:36:32.192+02:00</dataOra>");
//		sb.append("    <richiedente>");
//		sb.append("        <account>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>1</uid>");
//		sb.append("            <nome>Demo 21</nome>");
//		sb.append("            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>");
//		sb.append("            <indirizzoMail>email</indirizzoMail>");
//		sb.append("            <ente>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <gestioneLivelli>");
//		sb.append("                    <entry>");
//		sb.append("                        <key>LIVELLO_GESTIONE_BILANCIO</key>");
//		sb.append("                        <value>GESTIONE_UEB</value>");
//		sb.append("                    </entry>");
//		sb.append("                </gestioneLivelli>");
//		sb.append("                <nome>CittÃ  di Torino</nome>");
//		sb.append("            </ente>");
//		sb.append("        </account>");
//		sb.append("        <operatore>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>0</uid>");
//		sb.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
//		sb.append("            <cognome>AAAAAA00A11B000J</cognome>");
//		sb.append("            <nome>Demo</nome>");
//		sb.append("        </operatore>");
//		sb.append("    </richiedente>");
//		sb.append("    <bilancio>");
//		sb.append("        <stato>VALIDO</stato>");
//		sb.append("        <uid>1</uid>");
//		sb.append("        <anno>2013</anno>");
//		sb.append("    </bilancio>");
//		sb.append("    <subdocumentoIvaSpesa>");
//		sb.append("        <dataCreazione>2014-06-16+02:00</dataCreazione>");
//		sb.append("        <stato>VALIDO</stato>");
//		sb.append("        <uid>1</uid>");
//		sb.append("        <annoEsercizio>2013</annoEsercizio>");
//		sb.append("        <dataProtocolloProvvisorio>2014-06-16T00:00:00+02:00</dataProtocolloProvvisorio>");
//		sb.append("        <dataRegistrazione>2014-06-16T00:00:00+02:00</dataRegistrazione>");
//		sb.append("        <descrizioneIva>Descrizione per SIS di test 1</descrizioneIva>");
//		sb.append("        <documento xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/fin2/data/1.0\" xsi:type=\"ns3:documentoSpesa\">");
//		sb.append("            <dataCreazione>2014-06-05+02:00</dataCreazione>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>1</uid>");
//		sb.append("            <dataModifica>2014-06-09T13:04:15.507+02:00</dataModifica>");
//		sb.append("            <loginCreazione>AAAAAA00A11B000J</loginCreazione>");
//		sb.append("            <loginModifica>AAAAAA00A11B000J</loginModifica>");
//		sb.append("            <loginOperazione>AAAAAA00A11B000J</loginOperazione>");
//		sb.append("            <anno>2013</anno>");
//		sb.append("            <arrotondamento>0.00</arrotondamento>");
//		sb.append("            <codiceBollo>");
//		sb.append("                <dataCreazione>2014-02-28+01:00</dataCreazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>6</uid>");
//		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
//		sb.append("                <codice>77</codice>");
//		sb.append("                <descrizione>IVA ASSOLTA</descrizione>");
//		sb.append("            </codiceBollo>");
//		sb.append("            <dataEmissione>2013-04-17T00:00:00+02:00</dataEmissione>");
//		sb.append("            <dataInizioValiditaStato>2014-06-05T18:00:08.493+02:00</dataInizioValiditaStato>");
//		sb.append("            <dataScadenza>2014-05-21T00:00:00+02:00</dataScadenza>");
//		sb.append("            <descrizione>documento di prova</descrizione>");
//		sb.append("            <ente>");
//		sb.append("                <dataCreazione>2013-05-20+02:00</dataCreazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                <gestioneLivelli/>");
//		sb.append("                <nome>CittÃ  di Torino</nome>");
//		sb.append("            </ente>");
//		sb.append("            <importo>30000.00</importo>");
//		sb.append("            <importoTotaleNoteCollegate>0</importoTotaleNoteCollegate>");
//		sb.append("            <listaSubdocumenti>");
//		sb.append("                <dataCreazione>2014-06-05+02:00</dataCreazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <dataModifica>2014-06-05T18:00:09.150+02:00</dataModifica>");
//		sb.append("                <loginCreazione>AAAAAA00A11B000J</loginCreazione>");
//		sb.append("                <loginModifica>AAAAAA00A11B000J</loginModifica>");
//		sb.append("                <loginOperazione>AAAAAA00A11B000J</loginOperazione>");
//		sb.append("                <dataScadenza>2014-05-21T00:00:00+02:00</dataScadenza>");
//		sb.append("                <descrizione>documento di prova</descrizione>");
//		sb.append("                <documento>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>1</uid>");
//		sb.append("                    <arrotondamento>0</arrotondamento>");
//		sb.append("                    <importo>0</importo>");
//		sb.append("                    <importoTotaleNoteCollegate>0</importoTotaleNoteCollegate>");
//		sb.append("                </documento>");
//		sb.append("                <ente>");
//		sb.append("                    <dataCreazione>2013-05-20+02:00</dataCreazione>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>1</uid>");
//		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                    <gestioneLivelli/>");
//		sb.append("                    <nome>CittÃ  di Torino</nome>");
//		sb.append("                </ente>");
//		sb.append("                <flagAvviso>false</flagAvviso>");
//		sb.append("                <flagConvalidaManuale>false</flagConvalidaManuale>");
//		sb.append("                <flagEsproprio>false</flagEsproprio>");
//		sb.append("                <flagOrdinativoManuale>false</flagOrdinativoManuale>");
//		sb.append("                <flagOrdinativoSingolo>false</flagOrdinativoSingolo>");
//		sb.append("                <flagRilevanteIVA>false</flagRilevanteIVA>");
//		sb.append("                <importo>23000.00</importo>");
//		sb.append("                <importoDaDedurre>0</importoDaDedurre>");
//		sb.append("                <numero>1</numero>");
//		sb.append("            </listaSubdocumenti>");
//		sb.append("            <note></note>");
//		sb.append("            <numero>2345</numero>");
//		sb.append("            <numeroRepertorio></numeroRepertorio>");
//		sb.append("            <soggetto>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>7</uid>");
//		sb.append("                <codiceFiscale>TRTMSM58L26L219F</codiceFiscale>");
//		sb.append("                <codiceSoggetto>1</codiceSoggetto>");
//		sb.append("                <controlloSuSoggetto>true</controlloSuSoggetto>");
//		sb.append("                <denominazione>TORTA MASSIMO</denominazione>");
//		sb.append("                <partitaIva></partitaIva>");
//		sb.append("                <residenteEstero>false</residenteEstero>");
//		sb.append("                <uidSoggettoPadre>0</uidSoggettoPadre>");
//		sb.append("            </soggetto>");
//		sb.append("            <statoOperativoDocumento>INCOMPLETO</statoOperativoDocumento>");
//		sb.append("            <terminePagamento>34</terminePagamento>");
//		sb.append("            <tipoDocumento>");
//		sb.append("                <dataCreazione>2014-03-19+01:00</dataCreazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
//		sb.append("                <codice>AAM</codice>");
//		sb.append("                <descrizione>BOLLETTE AAM</descrizione>");
//		sb.append("                <flagIVA>true</flagIVA>");
//		sb.append("                <flagNotaCredito>false</flagNotaCredito>");
//		sb.append("                <flagPenaleAltro>false</flagPenaleAltro>");
//		sb.append("                <flagRegolarizzazione>false</flagRegolarizzazione>");
//		sb.append("                <flagRitenute>false</flagRitenute>");
//		sb.append("                <flagSpesaCollegata>false</flagSpesaCollegata>");
//		sb.append("                <flagSubordinato>false</flagSubordinato>");
//		sb.append("            </tipoDocumento>");
//		sb.append("            <causaleSospensione></causaleSospensione>");
//		sb.append("            <codiceFiscalePignorato></codiceFiscalePignorato>");
//		sb.append("            <flagBeneficiarioMultiplo>false</flagBeneficiarioMultiplo>");
//		sb.append("            <tipoImpresa>");
//		sb.append("                <dataCreazione>2014-03-13+01:00</dataCreazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>41168</uid>");
//		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
//		sb.append("                <codice>RTI</codice>");
//		sb.append("                <descrizione>Raggruppamento temporaneo di imprese</descrizione>");
//		sb.append("                <tipoClassificatore>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>0</uid>");
//		sb.append("                    <codice>TIPO_IMPRESA</codice>");
//		sb.append("                    <descrizione>Tipo impresa</descrizione>");
//		sb.append("                </tipoClassificatore>");
//		sb.append("            </tipoImpresa>");
//		sb.append("        </documento>");
//		sb.append("        <ente>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>1</uid>");
//		sb.append("            <gestioneLivelli>");
//		sb.append("                <entry>");
//		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
//		sb.append("                    <value>GESTIONE_UEB</value>");
//		sb.append("                </entry>");
//		sb.append("            </gestioneLivelli>");
//		sb.append("            <nome>CittÃ  di Torino</nome>");
//		sb.append("        </ente>");
//		sb.append("        <flagIntracomunitario>false</flagIntracomunitario>");
//		sb.append("        <flagNotaCredito>false</flagNotaCredito>");
//		sb.append("        <flagRegistrazioneIva>false</flagRegistrazioneIva>");
//		sb.append("        <flagRilevanteIRAP>true</flagRilevanteIRAP>");
//		sb.append("        <importoInValuta>0</importoInValuta>");
//		sb.append("        <listaAliquotaSubdocumentoIva>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>0</uid>");
//		sb.append("            <aliquotaIva>");
//		sb.append("                <dataCreazione>2014-05-27+02:00</dataCreazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <annoCatalogazione>0</annoCatalogazione>");
//		sb.append("                <codice>004</codice>");
//		sb.append("                <descrizione>iva 4%</descrizione>");
//		sb.append("                <ente>");
//		sb.append("                    <dataCreazione>2013-05-20+02:00</dataCreazione>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>1</uid>");
//		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                    <gestioneLivelli/>");
//		sb.append("                    <nome>CittÃ  di Torino</nome>");
//		sb.append("                </ente>");
//		sb.append("                <percentualeAliquota>4</percentualeAliquota>");
//		sb.append("                <percentualeIndetraibilita>0</percentualeIndetraibilita>");
//		sb.append("                <tipoOperazioneIva>IMPONIBILE</tipoOperazioneIva>");
//		sb.append("            </aliquotaIva>");
//		sb.append("            <imponibile>0.00</imponibile>");
//		sb.append("            <imposta>0.00</imposta>");
//		sb.append("            <impostaDetraibile>0.00</impostaDetraibile>");
//		sb.append("            <impostaIndetraibile>0.00</impostaIndetraibile>");
//		sb.append("            <totale>0.00</totale>");
//		sb.append("        </listaAliquotaSubdocumentoIva>");
//		sb.append("        <progressivoIVA>1</progressivoIVA>");
//		sb.append("        <registroIva>");
//		sb.append("            <dataCreazione>2014-06-10+02:00</dataCreazione>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>17</uid>");
//		sb.append("            <codice>codice registro</codice>");
//		sb.append("            <descrizione>descrizione registro</descrizione>");
//		sb.append("            <ente>");
//		sb.append("                <dataCreazione>2013-05-20+02:00</dataCreazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                <gestioneLivelli/>");
//		sb.append("                <nome>CittÃ  di Torino</nome>");
//		sb.append("            </ente>");
//		sb.append("            <tipoRegistroIva>ACQUISTI_IVA_DIFFERITA</tipoRegistroIva>");
//		sb.append("        </registroIva>");
//		sb.append("        <statoSubdocumentoIva>PROVVISORIO</statoSubdocumentoIva>");
//		sb.append("        <tipoRegistrazioneIva>");
//		sb.append("            <dataCreazione>2014-06-09+02:00</dataCreazione>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>1</uid>");
//		sb.append("            <annoCatalogazione>0</annoCatalogazione>");
//		sb.append("            <codice>01</codice>");
//		sb.append("            <descrizione>Normale</descrizione>");
//		sb.append("            <ente>");
//		sb.append("                <dataCreazione>2013-05-20+02:00</dataCreazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                <gestioneLivelli/>");
//		sb.append("                <nome>CittÃ  di Torino</nome>");
//		sb.append("            </ente>");
//		sb.append("            <flagTipoRegistrazioneIvaEntrata>true</flagTipoRegistrazioneIvaEntrata>");
//		sb.append("            <flagTipoRegistrazioneIvaSpesa>true</flagTipoRegistrazioneIvaSpesa>");
//		sb.append("        </tipoRegistrazioneIva>");
//		sb.append("    </subdocumentoIvaSpesa>");
//		sb.append("</aggiornaSubdocumentoIvaSpesa>");
//				
//		AggiornaSubdocumentoIvaSpesa req = JAXBUtility.unmarshall(sb.toString(), AggiornaSubdocumentoIvaSpesa.class);
//		
//		AggiornaSubdocumentoIvaSpesaResponse res = aggiornaSubdocumentoIvaSpesaService.executeService(req);
//
//		assertNotNull(res);
//				
//	}
	
	
	@Test
	public void aggiornaStatoSubdocumentoIvaEntrata() {
		
		AggiornaStatoSubdocumentoIvaEntrata req = new AggiornaStatoSubdocumentoIvaEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancioTest());
		req.setDataOra(new Date());
		SubdocumentoEntrata sd = new SubdocumentoEntrata();
		
		sd.setUid(12);
		sd.setEnte(getEnteTest());
//		sd.setNumeroOrdinativo(222);
//		sd.setDataOrdinativo(new Date());
		sd.setNumeroRegistrazioneIVA("2013/9");
		
		req.setSubdocumentoEntrata(sd);
		
		AggiornaStatoSubdocumentoIvaEntrataResponse res = aggiornaStatoSubdocumentoIvaEntrataService.executeService(req);

		assertNotNull(res);
		
		
	}
	
	
	

	

}
