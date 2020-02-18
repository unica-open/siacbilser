/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.RicercaQuoteDaAssociareService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociare;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociareResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

public class RicercaQuoteDaAssociareTest extends BaseJunit4TestCase{
	
	@Autowired
	private RicercaQuoteDaAssociareService ricercaQuoteDaAssociareService;
	
	@Test
	public void ricercaQuoteDaAssociare() {
		StringBuilder sb = new StringBuilder();
		//Forn 2
		RicercaQuoteDaAssociare req = JAXBUtility.unmarshall(obtainStringRequest(), RicercaQuoteDaAssociare.class);
		RicercaQuoteDaAssociareResponse res = ricercaQuoteDaAssociareService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void cercaQuoteDaAssociareRequest() {
		RicercaQuoteDaAssociare req = new RicercaQuoteDaAssociare();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioTest(131, 2017));
		req.setAnnoDocumento(2017);
		req.setNumeroDocumento("3001-03");
//		TipoDocumento create = create(TipoDocumento.class, 39);
//		req.setTipoDocumento(create);
//		req.setNumeroDocumento("030817_2");
//		req.setNumeroMovimento(new BigDecimal("2426"));
//		req.setAnnoMovimento(2017);
//		req.setSoggetto(create(Soggetto.class, 130002));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		req.setTipoFamigliaDocumento(TipoFamigliaDocumento.SPESA);
		
		
		RicercaQuoteDaAssociareResponse res = ricercaQuoteDaAssociareService.executeService(req);
		assertNotNull(res);
		for (Subdocumento<?, ?> subdocumento : res.getListaSubdocumenti()) {
			StringBuilder sb = new StringBuilder();
			sb.append("subdocumento con uid: " + subdocumento.getUid());
			if(subdocumento instanceof SubdocumentoSpesa) {
				SubdocumentoSpesa sub = (SubdocumentoSpesa) subdocumento;
				DocumentoSpesa documento = sub.getDocumento();
				sb.append(" e documento con uid = ")
					.append(documento.getUid())
					.append(". ")
					.append( "La testata documento")
					.append(documento.getAnno())
					.append(" / ")
					.append(documento.getNumero())
					.append("- ")
					.append(documento.getTipoDocumento().getCodice())
					.append( " ")
					.append( documento.getListaDocumentiSpesaPadre().size())
					.append(" documenti padre e ")
					.append(documento.getListaDocumentiSpesaFiglio().size())
					.append(" documenti figli. ");
//				System.out.println("DOCUMENTO PADRE");
//				log.logXmlTypeObject(documento, "documentoSpesaPadre");
				sb.append("I documenti SpesaFiglio sono: ");
				for (DocumentoSpesa documentoSpesaFiglio : documento.getListaDocumentiSpesaFiglio()) {
					sb.append(documentoSpesaFiglio.getAnno())
					.append(" / ")
					.append(documentoSpesaFiglio.getNumero())
					.append("- ")
					.append(documentoSpesaFiglio.getTipoDocumento().getCodice())
					.append(" uid[")
					.append(documentoSpesaFiglio.getUid())
					.append("]. ");
//					System.out.println("NCD: " + documentoSpesaFiglio.getAnno() + documentoSpesaFiglio.getAnno());
				}
				sb.append("I documenti SpesaPadre sono: ");
				for (DocumentoSpesa documentoSpesaPadre : documento.getListaDocumentiSpesaPadre()) {
					sb.append(documentoSpesaPadre.getAnno())
					.append(" / ")
					.append(documentoSpesaPadre.getNumero())
					.append(documentoSpesaPadre.getTipoDocumento().getCodice())
					.append(" ")
					.append(" uid[")
					.append(documentoSpesaPadre.getUid())
					.append("]. ");
//					System.out.println("NCD: " + documentoSpesaPadre.getAnno() + documentoSpesaPadre.getAnno());
				}
			}else {
				SubdocumentoEntrata subE = (SubdocumentoEntrata) subdocumento;
				DocumentoEntrata documentoE = subE.getDocumento();
//				System.out.println("DOCUMENTO PADRE");
//				log.logXmlTypeObject(documentoE, "documentoEntrataPadre");
			}
			
//			for (DocumentoSpesa docFiglio : documento.getListaDocumentiSpesaFiglio()) {
//				if(docFiglio.getTipoDocumento().isNotaCredito()) {
//					System.out.println("sub: " + sub.getUid() + " con importo da dedurre " + sub.getImportoDaDedurre() + " e nota credito: "  + docFiglio.getAnno() + " / " + docFiglio.getNumero() );
//				}
//			}
			if(subdocumento.getUid() == 37104) {
				System.out.println("************************************************************");
				System.out.println("************************************************************");
				System.out.println(sb.toString());
				System.out.println("************************************************************");
			}
		}
	}
	
	private String obtainStringRequest() {
		 // BuildMyString.com generated code. Please enjoy your string responsibly.

		StringBuilder sb = new StringBuilder();

		sb.append("<ricercaQuoteDaAssociare>");
		sb.append("    <dataOra>2017-12-05T08:49:51.431+01:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>52</uid>");
		sb.append("            <codice>2 -  ACCOUNT  - AMMINISTRATORE</codice>");
		sb.append("            <nome>Demo 22</nome>");
		sb.append("            <descrizione>Demo 22 -  ACCOUNT  - AMMINISTRATORE - Regione Piemonte</descrizione>");
		sb.append("            <indirizzoMail>email</indirizzoMail>");
		sb.append("            <ente>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>2</uid>");
		sb.append("                <gestioneLivelli>");
		sb.append("                    <entry>");
		sb.append("                        <key>REV_ONERI_CONTO_MAN</key>");
		sb.append("                        <value>TRUE</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>GESTIONE_EVASIONE_ORDINI</key>");
		sb.append("                        <value>SENZA_VERIFICA</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>REV_ONERI_DISTINTA_MAN</key>");
		sb.append("                        <value>FALSE</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>CARICA_MISSIONE_DA_ESTERNO</key>");
		sb.append("                        <value>CARICA_MISSIONE_DA_ESTERNO_ATTIVA</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>GESTIONE_PARERE_FINANZIARIO</key>");
		sb.append("                        <value>GESTIONE_PARERE_FINANZIARIO</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>GESTIONE_CONVALIDA_AUTOMATICA</key>");
		sb.append("                        <value>CONVALIDA_MANUALE</value>");
		sb.append("                    </entry>");
		sb.append("                    <entry>");
		sb.append("                        <key>GESTIONE_GSA</key>");
		sb.append("                        <value>A14</value>");
		sb.append("                    </entry>");
		sb.append("                </gestioneLivelli>");
		sb.append("                <nome>Regione Piemonte</nome>");
		sb.append("            </ente>");
		sb.append("        </account>");
		sb.append("        <operatore>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <codiceFiscale>AAAAAA00A11C000K</codiceFiscale>");
		sb.append("            <cognome>Montuori</cognome>");
		sb.append("			<nome>Raffaela</nome>");
		sb.append("        </operatore>");
		sb.append("    </richiedente>");
		sb.append("    <annoDocumento>2017</annoDocumento>");
		sb.append("    <annoMovimento>2017</annoMovimento>");
		sb.append("    <associatoAProvvedimentoOAdElenco>false</associatoAProvvedimentoOAdElenco>");
		sb.append("    <bilancio>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>131</uid>");
		sb.append("        <anno>2017</anno>");
		sb.append("    </bilancio>");
		sb.append("    <collegatoAMovimentoDelloStessoBilancio>true</collegatoAMovimentoDelloStessoBilancio>");
		sb.append("    <ente>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>2</uid>");
		sb.append("        <gestioneLivelli>");
		sb.append("            <entry>");
		sb.append("                <key>REV_ONERI_CONTO_MAN</key>");
		sb.append("                <value>TRUE</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>GESTIONE_EVASIONE_ORDINI</key>");
		sb.append("                <value>SENZA_VERIFICA</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>REV_ONERI_DISTINTA_MAN</key>");
		sb.append("                <value>FALSE</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>CARICA_MISSIONE_DA_ESTERNO</key>");
		sb.append("                <value>CARICA_MISSIONE_DA_ESTERNO_ATTIVA</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>GESTIONE_PARERE_FINANZIARIO</key>");
		sb.append("                <value>GESTIONE_PARERE_FINANZIARIO</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>GESTIONE_CONVALIDA_AUTOMATICA</key>");
		sb.append("                <value>CONVALIDA_MANUALE</value>");
		sb.append("            </entry>");
		sb.append("            <entry>");
		sb.append("                <key>GESTIONE_GSA</key>");
		sb.append("                <value>A14</value>");
		sb.append("            </entry>");
		sb.append("        </gestioneLivelli>");
		sb.append("        <nome>Regione Piemonte</nome>");
		sb.append("    </ente>");
		sb.append("    <importoDaPagareZero>false</importoDaPagareZero>");
		sb.append("    <numeroDocumento>112</numeroDocumento>");
		sb.append("    <numeroMovimento>2426</numeroMovimento>");
		sb.append("    <parametriPaginazione>");
		sb.append("	<elementiPerPagina>50</elementiPerPagina>");
		sb.append("        <numeroPagina>0</numeroPagina>");
		sb.append("    </parametriPaginazione>");
		sb.append("    <rilevatiIvaConRegistrazioneONonRilevantiIva>true</rilevatiIvaConRegistrazioneONonRilevantiIva>");
		sb.append("    <soggetto>");
		sb.append("        <loginOperazione>migr_soggetti</loginOperazione>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>130002</uid>");
		sb.append("        <avviso>true</avviso>");
		sb.append("        <codiceFiscale>00478050040</codiceFiscale>");
		sb.append("        <codiceSoggetto>10508</codiceSoggetto>");
		sb.append("        <codiceSoggettoNumber>10508</codiceSoggettoNumber>");
		sb.append("        <contatti>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>20619</uid>");
		sb.append("            <avviso>N</avviso>");
		sb.append("            <contattoCod>telefono</contattoCod>");
		sb.append("            <contattoCodModo>telefono</contattoCodModo>");
		sb.append("            <descrizione>0171999190</descrizione>");
		sb.append("            <descrizioneModo>telefono</descrizioneModo>");
		sb.append("            <idTipoContatto>1</idTipoContatto>");
		sb.append("        </contatti>");
		sb.append("        <contatti>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>20620</uid>");
		sb.append("            <avviso>N</avviso>");
		sb.append("            <contattoCod>fax</contattoCod>");
		sb.append("            <contattoCodModo>fax</contattoCodModo>");
		sb.append("            <descrizione>999190</descrizione>");
		sb.append("            <descrizioneModo>fax</descrizioneModo>");
		sb.append("            <idTipoContatto>3</idTipoContatto>");
		sb.append("        </contatti>");
		sb.append("        <contatti>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>20621</uid>");
		sb.append("            <avviso>S</avviso>");
		sb.append("            <contattoCod>email</contattoCod>");
		sb.append("            <contattoCodModo>email</contattoCodModo>");
		sb.append("            <descrizione>segreteria@comune.celledimacra.cn.it</descrizione>");
		sb.append("            <descrizioneModo>email</descrizioneModo>");
		sb.append("            <idTipoContatto>5</idTipoContatto>");
		sb.append("        </contatti>");
		sb.append("        <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("        <dataModifica>2017-02-03T12:30:13.756+01:00</dataModifica>");
		sb.append("        <dataStato>2017-02-03T00:00:00+01:00</dataStato>");
		sb.append("        <dataValidita>2017-02-03T00:00:00+01:00</dataValidita>");
		sb.append("        <denominazione>COMUNE DI CELLE DI MACRA</denominazione>");
		sb.append("        <elencoClass>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>47038</uid>");
		sb.append("			<idSoggClasse>256</idSoggClasse>");
		sb.append("            <idTipoSoggClasse>1</idTipoSoggClasse>");
		sb.append("            <soggettoClasseCode>C-5000</soggettoClasseCode>");
		sb.append("            <soggettoClasseDesc>COMUNI INFERIORI A 5000 ABITANTI (PER IMPEGNI)</soggettoClasseDesc>");
		sb.append("            <soggettoTipoClasseCode>ND</soggettoTipoClasseCode>");
		sb.append("            <soggettoTipoClasseDesc>Non definito</soggettoTipoClasseDesc>");
		sb.append("        </elencoClass>");
		sb.append("        <elencoClass>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>47039</uid>");
		sb.append("            <idSoggClasse>258</idSoggClasse>");
		sb.append("            <idTipoSoggClasse>1</idTipoSoggClasse>");
		sb.append("            <soggettoClasseCode>COM-CN</soggettoClasseCode>");
		sb.append("            <soggettoClasseDesc>COMUNI PROVINCIA DI CUNEO</soggettoClasseDesc>");
		sb.append("            <soggettoTipoClasseCode>ND</soggettoTipoClasseCode>");
		sb.append("            <soggettoTipoClasseDesc>Non definito</soggettoTipoClasseDesc>");
		sb.append("        </elencoClass>");
		sb.append("        <elencoClass>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>46227</uid>");
		sb.append("            <idSoggClasse>230</idSoggClasse>");
		sb.append("            <idTipoSoggClasse>1</idTipoSoggClasse>");
		sb.append("            <soggettoClasseCode>COM</soggettoClasseCode>");
		sb.append("            <soggettoClasseDesc>COM</soggettoClasseDesc>");
		sb.append("            <soggettoTipoClasseCode>ND</soggettoTipoClasseCode>");
		sb.append("            <soggettoTipoClasseDesc>Non definito</soggettoTipoClasseDesc>");
		sb.append("        </elencoClass>");
		sb.append("        <idStatoOperativoAnagrafica>8</idStatoOperativoAnagrafica>");
		sb.append("        <indirizzi>");
		sb.append("            <loginOperazione>migr_soggetti</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>107874</uid>");
		sb.append("            <cap>12020</cap>");
		sb.append("            <denominazione>KENNEDY, 1</denominazione>");
		sb.append("            <sedime>Piazza</sedime>");
		sb.append("            <avviso>true</avviso>");
		sb.append("            <checkAvviso>false</checkAvviso>");
		sb.append("            <checkPrincipale>false</checkPrincipale>");
		sb.append("            <codiceNazione>1</codiceNazione>");
		sb.append("            <comune>Celle di Macra</comune>");
		sb.append("            <idComune>004060</idComune>");
		sb.append("            <idTipoIndirizzo>SEDE_AMM</idTipoIndirizzo>");
		sb.append("            <idTipoIndirizzoDesc>sede amministrativa</idTipoIndirizzoDesc>");
		sb.append("            <indirizzoId>107874</indirizzoId>");
		sb.append("            <nazione>Italia</nazione>");
		sb.append("            <principale>true</principale>");
		sb.append("            <provincia>CN</provincia>");
		sb.append("        </indirizzi>");
		sb.append("        <loginCreazione>migr_soggetti</loginCreazione>");
		sb.append("        <modalitaPagamentoList>");
		sb.append("            <loginOperazione>migr_soggetti</loginOperazione>");
		sb.append("			 <stato>VALIDO</stato>");
		sb.append("            <uid>142088</uid>");
		sb.append("            <associatoA>Soggetto</associatoA>");
		sb.append("            <codiceModalitaPagamento>1</codiceModalitaPagamento>");
		sb.append("            <codiceStatoModalitaPagamento>VALIDO</codiceStatoModalitaPagamento>");
		sb.append("            <contoCorrente>0300345</contoCorrente>");
		sb.append("            <descrizione>Conto: 0300345 - Tipo accredito: GF - GIRO FONDI</descrizione>");
		sb.append("            <descrizioneInfo>");
		sb.append("                <descrizioneArricchita>Tipo accredito: GF - GIRO FONDI - Conto: 0300345</descrizioneArricchita>");
		sb.append("            </descrizioneInfo>");
		sb.append("            <descrizioneStatoModalitaPagamento>valido</descrizioneStatoModalitaPagamento>");
		sb.append("            <idStatoModalitaPagamento>2</idStatoModalitaPagamento>");
		sb.append("            <inModifica>false</inModifica>");
		sb.append("            <loginCreazione>migr_soggetti</loginCreazione>");
		sb.append("            <modalitaAccreditoSoggetto>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <codice>GF</codice>");
		sb.append("                <descrizione>GIRO FONDI</descrizione>");
		sb.append("                <priorita>0</priorita>");
		sb.append("            </modalitaAccreditoSoggetto>");
		sb.append("            <modificaPropriaOccorrenza>false</modificaPropriaOccorrenza>");
		sb.append("            <soggettoCessione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>0</uid>");
		sb.append("                <avviso>false</avviso>");
		sb.append("                <controlloSuSoggetto>true</controlloSuSoggetto>");
		sb.append("                <residenteEstero>false</residenteEstero>");
		sb.append("                <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("            </soggettoCessione>");
		sb.append("        </modalitaPagamentoList>");
		sb.append("        <naturaGiuridicaSoggetto>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>474</uid>");
		sb.append("            <soggettoTipoCode>EN</soggettoTipoCode>");
		sb.append("            <soggettoTipoDesc>ENTE GENERICO</soggettoTipoDesc>");
		sb.append("        </naturaGiuridicaSoggetto>");
		sb.append("        <partitaIva>00478050040</partitaIva>");
		sb.append("        <residenteEstero>false</residenteEstero>");
		sb.append("        <statoOperativo>VALIDO</statoOperativo>");
		sb.append("        <tipoClassificazioneSoggettoId>C-5000</tipoClassificazioneSoggettoId>");
		sb.append("        <tipoClassificazioneSoggettoId>COM-CN</tipoClassificazioneSoggettoId>");
		sb.append("        <tipoClassificazioneSoggettoId>COM</tipoClassificazioneSoggettoId>");
		sb.append("        <tipoSoggetto>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>111827</uid>");
		sb.append("            <soggettoTipoCode>PGI</soggettoTipoCode>");
		sb.append("            <soggettoTipoDesc>Persona giuridica</soggettoTipoDesc>");
		sb.append("            <soggettoTipoId>8</soggettoTipoId>");
		sb.append("        </tipoSoggetto>");
		sb.append("        <uidSoggettoPadre>0</uidSoggettoPadre>");
		sb.append("		</soggetto>");
		sb.append("    <statiOperativoDocumento>VALIDO</statiOperativoDocumento>");
		sb.append("    <statiOperativoDocumento>PARZIALMENTE_LIQUIDATO</statiOperativoDocumento>");
		sb.append("    <statiOperativoDocumento>PARZIALMENTE_EMESSO</statiOperativoDocumento>");
		sb.append("    <tipoDocumento>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>39</uid>");
		sb.append("    </tipoDocumento>");
		sb.append("    <tipoFamigliaDocumento>SPESA</tipoFamigliaDocumento>");
		sb.append("</ricercaQuoteDaAssociare>");
				

		return sb.toString();
	}

}
