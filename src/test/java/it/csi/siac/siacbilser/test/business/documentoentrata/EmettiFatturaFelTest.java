/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentoentrata;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import it.csi.siac.siacbilser.business.service.documentoentrata.EmettiFatturaFelEntrataService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.XmlUtils;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentoEntrataMVTest.
 */
public class EmettiFatturaFelTest extends BaseJunit4TestCase {

	/** The documento entrata service. */
	@Autowired
	private EmettiFatturaFelEntrataService emettiFatturaFelentrataservice;
	
	
	@Test
	public void emettiFatturaFel() {
		
		int[] uids = new int[] {
//				94283,
//				94284 //,
				94286

		};
		for (int uid : uids) {
			chiamaEmettiFattura(uid);			
		}
	}


	/**
	 * @param uidDocumentoDiEntrata
	 */
	private void chiamaEmettiFattura(int uidDocumentoDiEntrata) {
		EmettiFatturaFelEntrata request = new EmettiFatturaFelEntrata();
		request.setDataOra(new Date());
		request.setAnnoBilancio(2019);
		request.setDocumentoEntrata(create(DocumentoEntrata.class, uidDocumentoDiEntrata));
		request.setRichiedente(getRichiedenteByProperties("forn2", "edisu"));
		
		EmettiFatturaFelEntrataResponse res = emettiFatturaFelentrataservice.executeService(request);
		StringBuilder result = new StringBuilder();
		result.append("Chiamata al servizio con uid:" )
			.append(uidDocumentoDiEntrata)
			.append(" , terminata con esito: ")
			.append(res.getEsito()!= null? res.getEsito().name() : "null");
		
		if(res.hasErrori()) {
			for (Errore err : res.getErrori()) {
				result.append(err.getTesto());
				result.append("\n");
			}
		}
	}
	
	@Test
	public void validaXml() {
		
		try {
			XmlUtils.validateXmlWithXsd(new URL("http://www.fatturapa.gov.it/export/fatturazione/sdi/fatturapa/v1.2/Schema_del_file_xml_FatturaPA_versione_1.2.xsd"), getTestFileBytes("docs/test/fatture_attive_fel/fatturaAttiva.xml"));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
