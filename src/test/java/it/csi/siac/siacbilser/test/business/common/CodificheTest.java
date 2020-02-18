/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.common;

import org.dozer.Mapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.common.RicercaCodificaService;
import it.csi.siac.siacbilser.business.service.common.RicercaCodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifica;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.integration.dao.CodificheFactory;
import it.csi.siac.siacbilser.integration.dao.SiacRAccreditoTipoCassaEconRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRAccreditoTipoCassaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siacbilser.model.ModalitaAffidamentoProgetto;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.model.MezziDiTrasporto;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccommon.util.JAXBUtility;


/**
 * The Class CodificheDLTest.
 */
public class CodificheTest extends BaseJunit4TestCase {
		
	@Autowired
	private RicercaCodificheService ricercaCodificheService;
	
	@Autowired
	private RicercaCodificaService ricercaCodificaService;
	
	
	@Autowired
	private SiacRAccreditoTipoCassaEconRepository siacRAccreditoTipoCasaEconRepository;
	
	@Autowired
	private CodificheFactory cf;
	
	@Autowired
	private Mapper mapper;
	
	/**
	 * Test ricerca codifiche.
	 */
	@Test
	public void testRicercaCodifiche() {
		RicercaCodifiche req = new RicercaCodifiche();
	
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
				
//		req.addTipiCodifica(MezziDiTrasporto.class,TipoGiustificativo.class,TipoAtto.class);
		//req.addTipiCodifica(ModalitaPagamentoDipendente.class);
		
//		req.addTipoCodifica(Evento.class);
//		req.addTipoCodifica(ClassePiano.class);
//		req.addTipoCodifica(StatoDebito.class);
//		req.addNomeCodifica("ClassePiano_GSA");
//		req.addNomiCodifiche("ClassePiano_GSA", "ClassePiano_FIN");
		
		req.addTipoCodifica(ModalitaAffidamentoProgetto.class);
//		req.addTipoCodifica(SiopeDocumentoTipo.class);
//		req.addTipoCodifica(SiopeDocumentoTipoAnalogico.class);
//		req.addTipoCodifica(SiopeAssenzaMotivazione.class);
//		req.addTipoCodifica(SiopeScadenzaMotivo.class);
//		req.addTipoCodifica(SiopeTipoDebito.class);
		
		RicercaCodificheResponse resp = ricercaCodificheService.executeService(req);
		assertNotNull(resp);
		
//		List<Codifica> codifiche = resp.getCodifiche();
//		for(Codifica c : codifiche) { 
//			RicercaCodifica reqRCA = new RicercaCodifica();
//			reqRCA.setRichiedente(getRichiedenteByProperties("consip","regp"));
//			reqRCA.setCodifica(c.getClass());
//			reqRCA.setCodice(c.getCodice());
//			RicercaCodificaResponse res = ricercaCodificaService.executeService(reqRCA);
//			assertNotNull(res);
//			assertNotNull(res.getCodifica());
//			assertNotNull(res.getCodifica().getUid());
//			assertEquals("Codice non uguale!", c.getCodice(), res.getCodifica().getCodice());
//			break;
//		}
		
		
		
	}
	
	
	@Test
	public void testRicercaCodifica() {
		RicercaCodifica reqRCA = new RicercaCodifica();
		reqRCA.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRCA.setCodifica(it.csi.siac.siacfin2ser.model.TipoDocumento.class);
		reqRCA.setCodice("FAT");
		RicercaCodificaResponse res = ricercaCodificaService.executeService(reqRCA);
		assertNotNull(res);
		assertNotNull(res.getCodifica());
		assertNotNull(res.getCodifica().getUid());
	}
	
	
	
	@Test
	public void testCodificheFactory(){
		MezziDiTrasporto mdt = cf.ricercaCodifica(MezziDiTrasporto.class, "AUTOMOBILE", 1);
		
		System.out.println(JAXBUtility.marshall(mdt));
		
		//Ad esempio quando non trova la codifica: 
		//java.lang.IllegalStateException: Impossibile trovare un mapping su SiacDTrasportoMezzo per mtraCode='AUTO' dell'ente con id: 1
	}
	
	@Test
	@Transactional
	public void testNonSoCheSuccede() throws Exception {
		SiacRAccreditoTipoCassaEcon r = siacRAccreditoTipoCasaEconRepository.findOne(1);
		ModalitaPagamentoDipendente mpd = mapper.map(r, ModalitaPagamentoDipendente.class, CecMapId.SiacRAccreditoTipoCassaEcon_ModalitaPagamentoDipendente.name());
		
		System.out.println(JAXBUtility.marshall(mpd));
	}
	
	
	
}
