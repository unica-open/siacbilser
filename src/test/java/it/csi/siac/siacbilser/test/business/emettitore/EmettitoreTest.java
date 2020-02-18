/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.emettitore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaElencoDaEmettereService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiIncassoDaElencoService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoService;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettereResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegatoModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoSpesaDLTest.
 */
public class EmettitoreTest extends BaseJunit4TestCase {
	
	
	@Autowired
	private EmetteOrdinativiDiPagamentoDaElencoService emetteOrdinativiDiPagamentoDaElencoService;
	
	@Autowired
	private EmetteOrdinativiDiIncassoDaElencoService emetteOrdinativiDiIncassoDaElencoService;
	
	@Autowired
	private RicercaElencoDaEmettereService ricercaElencoDaEmettereService;
	
	@Test
	public void testEmetteOrdinativiDiPagamentoDaElenco() {
		EmetteOrdinativiDiPagamentoDaElenco req = new EmetteOrdinativiDiPagamentoDaElenco();
		req.setBilancio(getBilancioTest(133, 2018));
		req.setFlagConvalidaManuale(Boolean.TRUE);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
//		
//		req.getElenchi().add(create(ElencoDocumentiAllegato.class, 27330));
//		req.getElenchi().add(create(ElencoDocumentiAllegato.class, 27331));
		
//		SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
//		subdoc.setUid(37350);
		
//		List<SubdocumentoSpesa> subdocumenti = new ArrayList<SubdocumentoSpesa>();
//		subdocumenti.add(subdoc);
	
//		req.setSubdocumenti(subdocumenti);
		
		//AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> reqWrapper = wrapRequestToAsync(req);
		EmetteOrdinativiDiPagamentoDaElencoResponse res = emetteOrdinativiDiPagamentoDaElencoService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void emetteOrdinativiDiIncassoDaElenco1() {
		EmetteOrdinativiDiIncassoDaElenco req = new EmetteOrdinativiDiIncassoDaElenco();
		req.setBilancio(getBilancioByProperties("consip", "regp", "2019"));
		req.setDataOra(new Date());
		req.setFlagConvalidaManuale(Boolean.TRUE);
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		List<ElencoDocumentiAllegato> elenchi = new ArrayList<ElencoDocumentiAllegato>();
		int[] uids = new int[] {27993};
		for(int uid : uids) {
			ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
			eda.setUid(uid);
			elenchi.add(eda);
		}
		req.setElenchi(elenchi);
		
//		List<SubdocumentoEntrata> subdocumenti = new ArrayList<SubdocumentoEntrata>();
//		SubdocumentoEntrata s1 = new SubdocumentoEntrata();
//		s1.setUid(37345);
//		subdocumenti.add(s1);
//		SubdocumentoEntrata s2 = new SubdocumentoEntrata();
//		s2.setUid(409);
//		subdocumenti.add(s2);
//		SubdocumentoEntrata s3 = new SubdocumentoEntrata();
//		s3.setUid(413);
//		subdocumenti.add(s3);
//		req.setSubdocumenti(subdocumenti);
		
		EmetteOrdinativiDiIncassoDaElencoResponse res = emetteOrdinativiDiIncassoDaElencoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void testRicercaElencoDaEmettere() {
		RicercaElencoDaEmettere req = new RicercaElencoDaEmettere();
		req.setDataOra(new Date());
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
//		eda.setUid(15);
		eda.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setElencoDocumentiAllegato(eda);
		
		AttoAmministrativo aa = new AttoAmministrativo();
		aa.setUid(44118);
		req.setAttoAmministrativo(aa);
		req.setStatiOperativiElencoDocumenti(Arrays.asList(StatoOperativoElencoDocumenti.COMPLETATO));
		req.setModelDetails(new ModelDetail[] {
				ElencoDocumentiAllegatoModelDetail.Stato,
				ElencoDocumentiAllegatoModelDetail.TotaleDaPagareIncassare,
				ElencoDocumentiAllegatoModelDetail.TotaleQuoteSpesaEntrata,
				ElencoDocumentiAllegatoModelDetail.ContieneQuoteACopertura,
				ElencoDocumentiAllegatoModelDetail.SubdocumentiTotale,
				ElencoDocumentiAllegatoModelDetail.TotaleDaConvalidareSpesaEntrata,
				ElencoDocumentiAllegatoModelDetail.HasImpegnoConfermaDataFineValiditaDurc
				});
		// Paginazione a 50
		req.setParametriPaginazione(getParametriPaginazioneTest());
		RicercaElencoDaEmettereResponse res = ricercaElencoDaEmettereService.executeService(req);
		
		for (ElencoDocumentiAllegato elencoDocumentiAllegato : res.getElenchiDocumentiAllegato()) {
			StringBuilder sb = new StringBuilder()
					.append(elencoDocumentiAllegato.getAnno())
					.append(" / ")
					.append(elencoDocumentiAllegato.getNumero())
					.append(" flag durc: ")
					.append(elencoDocumentiAllegato.getHasImpegnoConfermaDurc())
					.append(" e data fine validita: ")
					.append(elencoDocumentiAllegato.getDataFineValiditaDurc() != null? elencoDocumentiAllegato.getDataFineValiditaDurc() : "null");	
			System.out.println(sb.toString());
		}
	}

}
