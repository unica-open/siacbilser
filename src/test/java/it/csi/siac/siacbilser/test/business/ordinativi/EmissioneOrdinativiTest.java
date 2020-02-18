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

import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiIncassoDaElencoAsyncService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiIncassoDaElencoService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoAsyncService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

public class EmissioneOrdinativiTest extends BaseJunit4TestCase {
	
	@Autowired
	private EmetteOrdinativiDiPagamentoDaElencoService emetteOrdinativiDiPagamentoDaElencoService;
	@Autowired
	private EmetteOrdinativiDiIncassoDaElencoService emetteOrdinativiDiIncassoDaElencoService;
	@Autowired
	private EmetteOrdinativiDiPagamentoDaElencoAsyncService emetteOrdinativiDiPagamentoDaElencoAsyncService;
	@Autowired
	private EmetteOrdinativiDiIncassoDaElencoAsyncService emetteOrdinativiDiIncassoDaElencoAsyncService;
	
	/**
	 * Test per l'emissione da Elenco
	 */
	@Test
	public void emetteOrdinativiDiPagamentoDaElenco() {
		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", "2019"));
		req.setElenchi(new ArrayList<ElencoDocumentiAllegato>());
		req.getElenchi().add(create(ElencoDocumentiAllegato.class, 27993));
//		req.setFlagConvalidaManuale(Boolean.TRUE);
//		req.setAllegatoAtto(create(AllegatoAtto.class, 11645));
//		req.getAllegatoAtto().setAttoAmministrativo(create(AttoAmministrativo.class, 44417));
		
		EmetteOrdinativiDiPagamentoDaElencoResponse res = emetteOrdinativiDiPagamentoDaElencoService.executeService(req);
		assertNotNull(res);
	}
	
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
	
	@Test
	public void emetteOrdinativiDiIncassoDaElenco1() {
		final String methodName = "emetteOrdinativiDiIncassoDaElenco1";
		EmetteOrdinativiDiIncassoDaElenco req = new EmetteOrdinativiDiIncassoDaElenco();
		req.setBilancio(getBilancioTest(131,2017));
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
		s1.setUid(37277);
		subdocumenti.add(s1);
		SubdocumentoEntrata s2 = new SubdocumentoEntrata();
		s2.setUid(37278);
		subdocumenti.add(s2);
//		SubdocumentoEntrata s3 = new SubdocumentoEntrata();
//		s3.setUid(413);
//		subdocumenti.add(s3);
		req.setSubdocumenti(subdocumenti);
		
		EmetteOrdinativiDiIncassoDaElencoResponse res = emetteOrdinativiDiIncassoDaElencoService.executeService(req);
		assertNotNull(res);
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			log.error(methodName, "InterruptedException waiting for async method to finish");
		}
	}
	
	@Test
	public void emetteOrdinativiDiPagamentoDaElencoAsync() {
		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		req.setAnnoBilancio(Integer.valueOf(2017));
		
		// Check subdocs
//		// Non esistente
		req.getSubdocumenti().add(create(SubdocumentoSpesa.class, 37108));
//		// Cancellato
//		req.getSubdocumenti().add(create(SubdocumentoSpesa.class, 36));
//		// Ente diverso
//		// Giusto
//		req.getSubdocumenti().add(create(SubdocumentoSpesa.class, 38740));
		
		// Check atto amministrativo
//		req.setAllegatoAtto(create(AllegatoAtto.class, 528));
//		req.getAllegatoAtto().setAttoAmministrativo(create(AttoAmministrativo.class, 33406));
		
		// Check Elenchi
//		req.getElenchi().add(create(ElencoDocumentiAllegato.class, 6081));
		
		AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> wrapper = wrapAsync(req, 4652);
		AsyncServiceResponse res = emetteOrdinativiDiPagamentoDaElencoAsyncService.executeService(wrapper);
		assertNotNull(res);
		System.out.println(">>>>>>>>>>>>>>>>>>> Mi metto in attesa per 20 secondi...");
		
		try {
			Thread.sleep(900000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println(">>>>>>>>>>>>>>>> sleep finito!");
		}
	}
	
	@Test
	public void emetteOrdinativiDiIncassoDaElencoAsync() {
		EmetteOrdinativiDiIncassoDaElenco req = new EmetteOrdinativiDiIncassoDaElenco();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		
		// Check subdocs
//		// Non esistente
//		req.getSubdocumenti().add(create(SubdocumentoEntrata.class, 1));
//		// Cancellato
//		req.getSubdocumenti().add(create(SubdocumentoEntrata.class, 36));
//		// Ente diverso
//		// Giusto
		req.getSubdocumenti().add(create(SubdocumentoEntrata.class, 37108));
		
		// Check atto amministrativo
//		req.setAllegatoAtto(create(AllegatoAtto.class, 528));
//		req.getAllegatoAtto().setAttoAmministrativo(create(AttoAmministrativo.class, 35896));
		
		// Check Elenchi
//		req.getElenchi().add(create(ElencoDocumentiAllegato.class, 11781));
//		
//		ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
//		eda.setAnno(2017);
//		eda.setNumero(11963);
//		req.getElenchi().add(eda);
//		
		AsyncServiceRequestWrapper<EmetteOrdinativiDiIncassoDaElenco> wrapper = wrapAsync(req, 4652);
		AsyncServiceResponse res = emetteOrdinativiDiIncassoDaElencoAsyncService.executeService(wrapper);
		assertNotNull(res);
		
		try {
			Thread.sleep(360000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println(">>>>>>>>>>>>>>>> sleep finito!");
		}
	}

}
