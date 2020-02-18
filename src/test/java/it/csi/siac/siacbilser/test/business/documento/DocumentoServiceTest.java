/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documento.RicercaAttivitaOnereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaCausale770Service;
import it.csi.siac.siacbilser.business.service.documento.RicercaCodicePCCService;
import it.csi.siac.siacbilser.business.service.documento.RicercaCodiceUfficioDestinatarioPCCService;
import it.csi.siac.siacbilser.business.service.documento.RicercaQuoteDaAssociareService;
import it.csi.siac.siacbilser.business.service.documento.RicercaTipoDocumentoService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociare;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociareResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

public class DocumentoServiceTest extends BaseJunit4TestCase {
	
	@Autowired
	private RicercaCausale770Service ricercaCausale770Service;
	@Autowired
	private RicercaAttivitaOnereService ricercaAttivitaOnereService;
	@Autowired
	private RicercaCodicePCCService ricercaCodicePCCService;
	@Autowired
	private RicercaCodiceUfficioDestinatarioPCCService ricercaUfficioPCCService;
	@Autowired
	private RicercaQuoteDaAssociareService ricercaQuoteDaAssociareService;
	@Autowired
	private RicercaTipoDocumentoService ricercaTipoDocumentoService;
	
	@Test
	public void ricercaCausale770() {
		RicercaCausale770 req = new RicercaCausale770();
		req.setDataOra(new Date());
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoOnere(null);
		
		RicercaCausale770Response res = ricercaCausale770Service.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaAttivitaOnere() {
		RicercaAttivitaOnere req = new RicercaAttivitaOnere();
		req.setDataOra(new Date());
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipoOnere(null);
		
		RicercaAttivitaOnereResponse res = ricercaAttivitaOnereService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaCodicePcc() {
		RicercaCodicePCC req = new RicercaCodicePCC();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn2", "regp"));
		
		List<StrutturaAmministrativoContabile> struttureAmministrativoContabili = new ArrayList<StrutturaAmministrativoContabile>();
		for(int uid : new int[] {508, 509}) {
			StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
			sac.setUid(uid);
			struttureAmministrativoContabili.add(sac);
		}
		//req.setStruttureAmministrativoContabili(struttureAmministrativoContabili);
		CodiceUfficioDestinatarioPCC codiceUfficioDestinatarioPCC = new CodiceUfficioDestinatarioPCC();
		codiceUfficioDestinatarioPCC.setUid(254);
		req.setCodiceUfficioDestinatarioPCC(codiceUfficioDestinatarioPCC);
		RicercaCodicePCCResponse res = ricercaCodicePCCService.executeService(req);
		assertNotNull(res);
		List<CodicePCC> codiciPCC = res.getCodiciPCC();
		for (CodicePCC cod : codiciPCC) {
			System.out.println(cod.getCodice() + " - " + cod.getDescrizione());
		}
	}
	
	@Test
	public void ricercaCodiceUfficioDestinatarioPcc() {
		RicercaCodiceUfficioDestinatarioPCC req = new RicercaCodiceUfficioDestinatarioPCC();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		List<StrutturaAmministrativoContabile> struttureAmministrativoContabili = new ArrayList<StrutturaAmministrativoContabile>();
		for(int uid : new int[] {508, 509}) {
			StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
			sac.setUid(uid);
			struttureAmministrativoContabili.add(sac);
		}
		req.setStruttureAmministrativoContabili(struttureAmministrativoContabili);
		
		RicercaCodiceUfficioDestinatarioPCCResponse res = ricercaUfficioPCCService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaQuoteDaAssociare() {
		RicercaQuoteDaAssociare req = new RicercaQuoteDaAssociare();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		req.setBilancio(getBilancioTest(131, 2017));
		
		// Dati comuni
		req.setStatiOperativoDocumento(Arrays.asList(StatoOperativoDocumento.VALIDO, StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO,
				StatoOperativoDocumento.PARZIALMENTE_EMESSO));
		req.setCollegatoAMovimentoDelloStessoBilancio(Boolean.TRUE);
		req.setAssociatoAProvvedimentoOAdElenco(Boolean.FALSE);
		req.setImportoDaPagareZero(Boolean.FALSE);
		req.setRilevatiIvaConRegistrazioneONonRilevantiIva(Boolean.TRUE);
		
		req.setTipoFamigliaDocumento(TipoFamigliaDocumento.SPESA);
		req.setTipoDocumento(create(TipoDocumento.class, 42));
		
		RicercaQuoteDaAssociareResponse res = ricercaQuoteDaAssociareService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaTipoDocumento() {
		RicercaTipoDocumento req = new RicercaTipoDocumento();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setTipoFamDoc(TipoFamigliaDocumento.SPESA);
		
		RicercaTipoDocumentoResponse res = ricercaTipoDocumentoService.executeService(req);
		assertNotNull(res);
		
	}
}
