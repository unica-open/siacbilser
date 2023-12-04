/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.emettitore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiIncassoDaElencoService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoSpesaDLTest.
 */
public class EmettitoreTest2 extends BaseJunit4TestCase {
	
	
	@Autowired
	private EmetteOrdinativiDiPagamentoDaElencoService emetteOrdinativiDiPagamentoDaElencoService;
	
	@Autowired
	private EmetteOrdinativiDiIncassoDaElencoService emetteOrdinativiDiIncassoDaElencoService;
	
	@Test
	public void testEmetteOrdinativiDiPagamentoDaElenco() {
		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
		req.setBilancio(getBilancioTest(131, 2017));
		req.setFlagConvalidaManuale(Boolean.TRUE);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		List<SubdocumentoSpesa> subdocumenti = new ArrayList<SubdocumentoSpesa>();
//		subdocumenti.add(create(SubdocumentoSpesa.class, 37297));
//		subdocumenti.add(create(SubdocumentoSpesa.class, 37298));
		subdocumenti.add(create(SubdocumentoSpesa.class, 37513));
	
		req.setSubdocumenti(subdocumenti);
		
		//AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> reqWrapper = wrapRequestToAsync(req);
		EmetteOrdinativiDiPagamentoDaElencoResponse res = emetteOrdinativiDiPagamentoDaElencoService.executeService(req);
		assertNotNull(res);
	}

	
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	@Test
	public void countQuote(){
		DocumentoSpesa documento = new DocumentoSpesa();
		documento.setUid(10);
		Long quoteNonEmesse = subdocumentoSpesaDad.countQuoteDocumentoNonEmesse(documento); 
	}
	
	@Test
	public void hasImpegnoGSA(){
		
		Boolean hasImpegnoGSA = subdocumentoSpesaDad.getFlagAttivaGsaImpegnoCollegato(37518); 
		System.out.println(hasImpegnoGSA);
	}
	
	@Test
	public void emetteOrdinativiDiIncassoDaElenco1() {
		EmetteOrdinativiDiIncassoDaElenco req = new EmetteOrdinativiDiIncassoDaElenco();
		req.setBilancio(getBilancioTest(131, 2017));
		req.setDataOra(new Date());
		req.setFlagConvalidaManuale(Boolean.TRUE);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
//		List<ElencoDocumentiAllegato> elenchi = new ArrayList<ElencoDocumentiAllegato>();
//		int[] uids = new int[] {20,21,23,25};
//		for(int uid : uids) {
//			ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
//			eda.setUid(uid);
//			elenchi.add(eda);
//		}
//		req.setElenchi(elenchi);
		
		List<SubdocumentoEntrata> subdocumenti = new ArrayList<SubdocumentoEntrata>();
		SubdocumentoEntrata s1 = new SubdocumentoEntrata();
		s1.setUid(37244);
		subdocumenti.add(s1);
//		SubdocumentoEntrata s2 = new SubdocumentoEntrata();
//		s2.setUid(409);
//		subdocumenti.add(s2);
//		SubdocumentoEntrata s3 = new SubdocumentoEntrata();
//		s3.setUid(413);
//		subdocumenti.add(s3);
		req.setSubdocumenti(subdocumenti);
		
		EmetteOrdinativiDiIncassoDaElencoResponse res = emetteOrdinativiDiIncassoDaElencoService.executeService(req);
		assertNotNull(res);
	}
	
	
	

	

}
