/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.fattureelettroniche;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.inviofatturapa.InvioFatturaPAService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.InvioFatturaPA;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.InvioFatturaPAResponse;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.DatiPortaleFattureType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.DatiPortaleFattureType.EstremiEsito;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.DatiPortaleFattureType.EstremiEsito.Utente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.EmbeddedXMLType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.InformazioneType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.InformazioniAggiuntiveType;

// TODO: Auto-generated Javadoc
/**
 * The Class FatturaFelTest.
 */
public class InvioFatturaPATest extends BaseJunit4TestCase {
	
	@Autowired
	private InvioFatturaPAService invioFatturaPAService;
	
	@Test
	public void invioFatturaPA() throws DatatypeConfigurationException, IOException{
		InvioFatturaPA req = new InvioFatturaPA();
		req.setCodiceEnte("R_PIEMON-01");
		
		DatiPortaleFattureType datiPortaleFattureType = new DatiPortaleFattureType();
		req.setDatiPortaleFatture(datiPortaleFattureType);
		
		datiPortaleFattureType.setIdentificativoFEL(310325001L);
		datiPortaleFattureType.setIdentificativoSDI(310325001L);
		datiPortaleFattureType.setNomeFileFattura("IT09468600011_02648.xml");
		
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.set(Calendar.YEAR, 2015);
		gregorianCalendar.set(Calendar.MONTH, Calendar.MARCH);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 30);
		XMLGregorianCalendar dataRicezione = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		datiPortaleFattureType.setDataRicezione(dataRicezione);
		
		EstremiEsito estremiEsito = new EstremiEsito();
		estremiEsito.setDataOra(dataRicezione);
		estremiEsito.setStatoFattura("ACCETTATA");
		
		Utente utente = new Utente();
		utente.setCodice("100");
		utente.setNome("Prova csi");
		utente.setCognome("Prova trasmissione");
		
		estremiEsito.setUtente(utente);
		datiPortaleFattureType.setEstremiEsito(estremiEsito);
		
		InformazioniAggiuntiveType informazioniAggiuntiveType = new InformazioniAggiuntiveType();
		req.setInformazioniAggiuntive(informazioniAggiuntiveType);
		
		addInformazione(informazioniAggiuntiveType, "Indice_classificazione_estesa", "C.arc, CRP.e, CR Piemonte .ra, TITCRP.t, 3.v, 12.v, DETA01000.std, CR.arm, 1/2013.nd");
		addInformazione(informazioniAggiuntiveType, "Oggetto", "fattura di prova");
		addInformazione(informazioniAggiuntiveType, "Numero_reg_protocollo", "DB2100/00000095/2015");
		addInformazione(informazioniAggiuntiveType, "Data_reg_protocollo", "28/01/2015");
		addInformazione(informazioniAggiuntiveType, "PrincipalId_archiviazione", "AF256AF4AFBC47899ED");
		addInformazione(informazioniAggiuntiveType, "Id_classificazione", "1AF256AF4AFBC47899ED");
		
		Source source = new StreamSource(new File("docs/test/fatture/IT01879020517_c2e02.xml"));
		EmbeddedXMLType embeddedXMLType = new EmbeddedXMLType();
		embeddedXMLType.setContent(source);
		req.setFatturaElettronica(embeddedXMLType);
		
		InvioFatturaPAResponse res = invioFatturaPAService.executeService(req);
		assertNotNull(res);
	}

	private void addInformazione(InformazioniAggiuntiveType informazioniAggiuntiveType, String nome, String valore) {
		InformazioneType informazioneType = new InformazioneType();
		informazioneType.setNome(nome);
		informazioneType.setValore(valore);
		informazioniAggiuntiveType.getInformazione().add(informazioneType);
	}
	
	@Test
	public void invioFatturaPAFromFile() {
		byte[] contenuto;
		try {
//			contenuto = getTestFileBytes("docs/test/fatture/SIAC-4397_IT01234567890_FPA01.xml");
			contenuto = getTestFileBytes("docs/test/fatture/Elaborated_IT01578251009_B2084.xml");
//			contenuto = getTestFileBytes("docs/test/fatture/SIAC-4397_IT01234567890_FPA03.xml");
		} catch (IOException e) {
			e.printStackTrace();
			fail("impossibile leggere il file di test: "+e.getMessage());
			return;
		}
		String xml = new String(contenuto);
		InvioFatturaPA req = JAXBUtility.unmarshall(xml, InvioFatturaPA.class);
		InvioFatturaPAResponse res = invioFatturaPAService.executeService(req);
		assertNotNull(res);
	}
	
	public static void main(String[] args) throws Exception {
		String head = new String(getTestFileBytes("docs/test/fatture/fattura_csi_head.snip"));
		String tail = new String(getTestFileBytes("docs/test/fatture/fattura_csi_tail.snip"));
		String[] files = new String[] {"IT01578251009_B2084.xml"}; //, "IT01234567890_FPA02.xml", "IT01234567890_FPA03.xml"};
		Charset charset = Charset.forName("UTF-8");
		
		for(String file : files) {
			byte[] contenuto = getTestFileBytes("docs/test/fatture/" + file);
			String base64 = DatatypeConverter.printBase64Binary(contenuto);
			String full = head + base64 + tail;
			printTestFileBytes("docs/test/fatture/Elaborated_" + file, full.getBytes(charset));
			
			String xml = new String(contenuto);
			System.out.println(xml);
		}
	}
	
}
