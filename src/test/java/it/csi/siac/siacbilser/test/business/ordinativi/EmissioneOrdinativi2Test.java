/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.ordinativi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiIncassoDaElencoService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

public class EmissioneOrdinativi2Test extends BaseJunit4TestCase {
	
	@Autowired
	private EmetteOrdinativiDiPagamentoDaElencoService /*AsyncService*/ emetteOrdinativiDiPagamentoDaElenco;
	@Autowired
	private EmetteOrdinativiDiIncassoDaElencoService emetteOrdinativiDiIncassoDaElencoService;
	
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	/**
	 * Test per l'emissione da Elenco
	 */
	@Test
	public void emetteOrdinativiDiPagamentoDaElenco1() {
		final String methodName = "emetteOrdinativiDiPagamentoDaElenco1";
		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
		req.setBilancio(getBilancio2014Test());
		req.setFlagConvalidaManuale(Boolean.TRUE);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
//		req.setEnte(getEnteTest());
//		req.setAzioneRichiesta(getAzioneRichiestaPagamento());
		
		ContoTesoreria contoTesoreria = new ContoTesoreria();
		contoTesoreria.setUid(5);
		req.setContoTesoreria(contoTesoreria);
		
		List<ElencoDocumentiAllegato> elenchi = new ArrayList<ElencoDocumentiAllegato>();
		int[] uids = new int[] {

//		1, 2, 3, 4, 5, 6, 7, 8, 9, 
//		10, 
//		11, 
//		12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 26, 27, 25, 28, 
//		//29 //, Multiplo
//		
//		30, 31, 32, 33, 34, 35, 36, 37,
//				38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 
				52, 
//				53,
//				54, 55, 56

		// 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 20, 21, 51, 52, 7, 53,
		// 54, 55, 23, 24, 56, 57, 58, 25, 26, 27, 28,
		// 29, 30, 31, 32, 33, 34,
		// 35, /*c'è*/
		// 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 59, 60,
		// 61, 62, 63, 64, 65, 66, 67, 68, 69,
		// 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
		// 81 /*c'è*/,
		// 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93,
		};
		
		for(int uid : uids) {
			ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
			eda.setUid(uid);
			elenchi.add(eda);
		}
		req.setElenchi(elenchi);
		
		//AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> reqWrapper = wrapRequest(req);
		
		EmetteOrdinativiDiPagamentoDaElencoResponse res = emetteOrdinativiDiPagamentoDaElenco.executeService(req);
//		assertNotNull(res);
//		
//		try {
//			Thread.sleep(5 * 60 * 1000);
//		} catch (InterruptedException e) {
//			log.error(methodName, "InterruptedException waiting for async method to finish");
//		}
	}
//	
//	/**
//	 * Test per l'emissione da Quote
//	 */
//	@Test
//	public void emetteOrdinativiDiPagamentoDaElenco2() {
//		final String methodName = "emetteOrdinativiDiPagamentoDaElenco2";
//		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
//		req.setBilancio(getBilancio2014Test());
//		req.setDataOra(new Date());
//		req.setEnte(getEnteTest());
//		req.setFlagConvalidaManuale(Boolean.TRUE);
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setAzioneRichiesta(getAzioneRichiestaPagamento());
//		// TODO
//		
//		try {
//			Thread.sleep(60 * 1000);
//		} catch (InterruptedException e) {
//			log.error(methodName, "InterruptedException waiting for async method to finish");
//		}
//	}
//	
//	/**
//	 * Test per l'emissione da Elenco
//	 */
//	@Test
//	public void emetteOrdinativiDiIncassoDaElenco1() {
//		final String methodName = "emetteOrdinativiDiIncassoDaElenco1";
//		EmetteOrdinativiDiIncassoDaElenco req = new EmetteOrdinativiDiIncassoDaElenco();
//		req.setBilancio(getBilancio2014Test());
//		req.setDataOra(new Date());
//		req.setEnte(getEnteTest());
//		req.setFlagConvalidaManuale(Boolean.TRUE);
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setAzioneRichiesta(getAzioneRichiestaIncasso());
//		// TODO
//		
//		try {
//			Thread.sleep(60 * 1000);
//		} catch (InterruptedException e) {
//			log.error(methodName, "InterruptedException waiting for async method to finish");
//		}
//	}
//	
//	/**
//	 * Test per l'emissione da Quote
//	 */
//	@Test
//	public void emetteOrdinativiDiIncassoDaElenco2() {
//		final String methodName = "emetteOrdinativiDiIncassoDaElenco2";
//		EmetteOrdinativiDiIncassoDaElenco req = new EmetteOrdinativiDiIncassoDaElenco();
//		req.setBilancio(getBilancio2014Test());
//		req.setDataOra(new Date());
//		req.setEnte(getEnteTest());
//		req.setFlagConvalidaManuale(Boolean.TRUE);
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setAzioneRichiesta(getAzioneRichiestaIncasso());
//		// TODO
//		
//		try {
//			Thread.sleep(5 * 60 * 1000);
//		} catch (InterruptedException e) {
//			log.error(methodName, "InterruptedException waiting for async method to finish");
//		}
//	}
//	
	/**
	 * @return the azione richiesta pagamento
	 */
	private AzioneRichiesta getAzioneRichiestaPagamento() {
		return getAzioneRichiesta(4754);
	}
	
	/**
	 * @return the azione richiesta pagamento
	 */
	private AzioneRichiesta getAzioneRichiestaIncasso() {
		return getAzioneRichiesta(4753);
	}
	
	/**
	 * @param uid the uid
	 * @return the azione richiesta
	 */
	private AzioneRichiesta getAzioneRichiesta(int uid) {
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		Azione azione = new Azione();
		azione.setUid(uid);
		azioneRichiesta.setAzione(azione);
		return azioneRichiesta;
	}
//	
//	/**
//	 * Wrap della request.
//	 * 
//	 * @param req la request da wrappare
//	 * 
//	 * @return il wrapper
//	 */
//	private AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> wrapRequest(EmetteOrdinativiDiPagamentoDaElenco req) {
//		return AsyncRequestMapper.toAsyncServiceRequestWrapper(req);
//	}
//	
//	/**
//	 * Wrap della request.
//	 * 
//	 * @param req la request da wrappare
//	 * 
//	 * @return il wrapper
//	 */
//	private AsyncServiceRequestWrapper<EmetteOrdinativiDiIncassoDaElenco> wrapRequest(EmetteOrdinativiDiIncassoDaElenco req) {
//		return AsyncRequestMapper.toAsyncServiceRequestWrapper(req);
//	}
	
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
	
	@Test
	public void testSubdoc() {
		Ente ente = new Ente();
		ente.setUid(2);
		subdocumentoSpesaDad.setEnte(ente);
		subdocumentoSpesaDad.countSubdocumentiSpesaDaEmettereByAttoAmministrativo(
				Integer.valueOf(33),
				Integer.valueOf(2018),
				Integer.valueOf(33));
	}


}
