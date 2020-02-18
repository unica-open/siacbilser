/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentoiva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAliquotaIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaTipoRegistrazioneIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaValutaService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.RicercaSinteticaSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaSinteticaSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaRegistroIvaService;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAliquota;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
//import it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.TipoOperazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentoIvaAMTest.
 */
public class DocumentoIvaTest extends BaseJunit4TestCase {
	
	/** The l. */
	private LogUtil l = new LogUtil(getClass());
	
	/** The mapper. */
	@Autowired
	private Mapper mapper;
	
	/** The ricerca aliquota iva service. */
	@Autowired
	private RicercaAliquotaIvaService ricercaAliquotaIvaService;
	
	/** The ricerca attivita iva service. */
	@Autowired
	private RicercaAttivitaIvaService ricercaAttivitaIvaService;
	
	/** The ricerca tipo registrazione iva service. */
	@Autowired
	private RicercaTipoRegistrazioneIvaService ricercaTipoRegistrazioneIvaService;
//	@Autowired
//	private RicercaTipoRegistroIvaService ricercaTipoRegistroIvaService;
	
	/**The ricerca registro iva service */
	@Autowired
	private RicercaRegistroIvaService ricercaRegistroIvaService;
	
	/** The ricerca valuta service. */
	@Autowired
	private RicercaValutaService ricercaValutaService;
	
	@Autowired
	private RicercaSinteticaSubdocumentoIvaSpesaService ricercaSinteticaSubdocumentoIvaSpesaService;
	@Autowired
	private RicercaSinteticaSubdocumentoIvaEntrataService ricercaSinteticaSubdocumentoIvaEntrataService;

	
	/**
	 * Test mapping aliquota iva.
	 */
	@Test
	public void testMappingAliquotaIva() {
		AliquotaIva ai = new AliquotaIva();
		ai.setCodice("0001");
		ai.setDescrizione("Descrizione");
		ai.setPercentualeAliquota(new BigDecimal("0.25"));
		ai.setPercentualeIndetraibilita(new BigDecimal("0.15"));
		
		ai.setTipoOperazioneIva(TipoOperazioneIva.ESENTE);
		ai.setEnte(getEnteTest());
		
		l.logXmlTypeObject(ai, "AliquotaIva");
		
		createAndLogMappedEntity(ai, SiacTIvaAliquota.class, BilMapId.SiacTIvaAliquota_AliquotaIva);
	}
	
	/**
	 * Test mapping attivita iva.
	 */
	@Test
	public void testMappingAttivitaIva() {
		AttivitaIva ai = new AttivitaIva();
		ai.setCodice("0001");
		ai.setDescrizione("Descrizione");
		ai.setFlagRilevanteIRAP(Boolean.TRUE);
		
		GruppoAttivitaIva gai = new GruppoAttivitaIva();
		gai.setUid(250);
		
		ai.setGruppoAttivitaIva(gai);
		ai.setEnte(getEnteTest());
		
		l.logXmlTypeObject(ai, "AliquotaIva");
		
		// Attenzione! JAXB trova un ciclo (SiacTIvaAttivita -> SiacRIvaAttAttr -> SiacTIvaAttivita)
		createAndLogMappedEntity(ai, SiacTIvaAttivita.class, BilMapId.SiacTIvaAttivita_AttivitaIva_Base);
	}

	/**
	 * Test ricerca aliquota iva service.
	 */
	@Test
	public void testRicercaAliquotaIvaService() {
		RicercaAliquotaIva request = new RicercaAliquotaIva();
		
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RicercaAliquotaIvaResponse response = ricercaAliquotaIvaService.executeService(request);
		assertTrue("Non sono state trovate aliquote", !response.getListaAliquotaIva().isEmpty());
	}
	
	/**
	 * Test ricerca attivita iva service.
	 */
	@Test
	public void testRicercaAttivitaIvaService() {
		RicercaAttivitaIva request = new RicercaAttivitaIva();
		
		AttivitaIva ai = new AttivitaIva();
		ai.setDescrizione("Ref");
		
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setAttivitaIva(ai);
		
		RicercaAttivitaIvaResponse response = ricercaAttivitaIvaService.executeService(request);
		assertTrue("Non sono state trovate attivita", !response.getListaAttivitaIva().isEmpty());
	}
	
	/**
	 * Test ricerca tipo registrazione iva service.
	 */
	@Test
	public void testRicercaTipoRegistrazioneIvaService() {
		RicercaTipoRegistrazioneIva request = new RicercaTipoRegistrazioneIva();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		TipoRegistrazioneIva tri = new TipoRegistrazioneIva();
		tri.setEnte(getEnteTest());
		request.setTipoRegistrazioneIva(tri);
		
		RicercaTipoRegistrazioneIvaResponse response = ricercaTipoRegistrazioneIvaService.executeService(request);
		assertTrue("Non sono state trovati tipi di registrazione", !response.getListaTipoRegistrazioneIva().isEmpty());
	}
	
	/**
	 * Test ricerca valuta service.
	 */
	@Test
	public void testRicercaValutaService() {
		RicercaValuta request = new RicercaValuta();
		
		request.setDataOra(new Date());
		request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RicercaValutaResponse response = ricercaValutaService.executeService(request);
		assertTrue("Non sono state trovate valute", !response.getListaValuta().isEmpty());
	}
	
//	@Test
//	public void testRicercaTipoRegistroIvaService() {
//		RicercaTipoRegistroIva request = new RicercaTipoRegistroIva();
//		
//		request.setDataOra(new Date());
//		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		TipoRegistroIva tri = new TipoRegistroIva();
//		tri.setEnte(getEnteTest());
//		tri.setTipoEsigibilitaIva(TipoEsigibilitaIva.DIFFERITA);
//		request.setTipoRegistroIva(tri);
//		
//		RicercaTipoRegistroIvaResponse response = ricercaTipoRegistroIvaService.executeService(request);
//		assertTrue("Non sono state trovati registri", !response.getListaTipoRegistroIva().isEmpty());
//	}
	
 /** Test per la ricerca del registro IVA*/
	@Test
	public void testRicercaRegistroIva() {
		
		RicercaRegistroIva request = new RicercaRegistroIva();
		
		RegistroIva registroIvaTest = new RegistroIva();
		GruppoAttivitaIva gruppoAttivitaIvaTest = new GruppoAttivitaIva();
		List<AttivitaIva> listaAttivitaIvaTest = new ArrayList<AttivitaIva>();
		AttivitaIva attivitaIvaIstruzione = new AttivitaIva();
		AttivitaIva attivitaIvaTrasporti = new AttivitaIva();
		
		//setto Gruppo Attivit&agrave Iva, attivitàIVA e tiporegistro
		TipoRegistroIva tipoRegistroIvaTest = TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA;
		attivitaIvaIstruzione.setUid(18);//18-->01-Istruzione, 30--> 02-Cultura
		listaAttivitaIvaTest.add(attivitaIvaIstruzione);
		attivitaIvaTrasporti.setUid(42); // 42--> Trasporti
		listaAttivitaIvaTest.add(attivitaIvaTrasporti);
		gruppoAttivitaIvaTest.setUid(127); //127 -->01-gruppo lotto K, comprende trasporti e cultura
		gruppoAttivitaIvaTest.setListaAttivitaIva(listaAttivitaIvaTest);
		
		
		registroIvaTest.setEnte(getEnteTest());
		registroIvaTest.setTipoRegistroIva(tipoRegistroIvaTest);
		registroIvaTest.setGruppoAttivitaIva(gruppoAttivitaIvaTest);
		
		//request.setEnte(getEnteTest());
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setDataOra(new Date());
	//	RicercaRegistroIva request = creaRequest(RicercaRegistroIva.class);
		//request.setRegistroIva(ri);
			
			//RegistroIva ri = new RegistroIva();
			//ri.setEnte(getEnte());
			//ri.setTipoRegistroIva(tipoRegistroIva);
			
			request.setRegistroIva(registroIvaTest);
						
			RicercaRegistroIvaResponse response = ricercaRegistroIvaService.executeService(request);
			assertTrue("Non sono state trovati registri", response.getListaRegistroIva().isEmpty());
	}
	
	
	
	
	@Test
	public void ricercaSinteticaSubdocumentoIvaSpesaService(){
		RicercaSinteticaSubdocumentoIvaSpesa request = new RicercaSinteticaSubdocumentoIvaSpesa();
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		SubdocumentoIvaSpesa subdocumentoIvaSpesa = new SubdocumentoIvaSpesa();
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setEnte(getEnteTest());
		doc.setAnno(2015);
		doc.setContabilizzaGenPcc(false);
		doc.setNumero("JIRA");
//		it.csi.siac.siacfin2ser.model.TipoDocumento tipoDocumento = new it.csi.siac.siacfin2ser.model.TipoDocumento();
//		tipoDocumento.setUid(191);
//		doc.setTipoDocumento(tipoDocumento);
		
		subdocumentoIvaSpesa.setEnte(getEnteTest());
		subdocumentoIvaSpesa.setDocumento(doc);
		request.setSubdocumentoIvaSpesa(subdocumentoIvaSpesa);
		request.setDataOra(new Date());
		ParametriPaginazione parametriPaginazione= new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(10);
		request.setParametriPaginazione(parametriPaginazione);

		RicercaSinteticaSubdocumentoIvaSpesaResponse response =  ricercaSinteticaSubdocumentoIvaSpesaService.executeService(request);
		log.logXmlTypeObject(request, "request del servizio ricercaSinteticaSubdocivaspesaServie");

		log.logXmlTypeObject(response, "response del servizio ricercaSinteticaSubdocivaspesaServie");

	}
	
	
	
	@Test
	public void ricercaSinteticaSubdocumentoIvaEntrataService(){
		RicercaSinteticaSubdocumentoIvaEntrata request = new RicercaSinteticaSubdocumentoIvaEntrata();
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		SubdocumentoIvaEntrata subdocumentoIvaEntrata = new SubdocumentoIvaEntrata();
		DocumentoEntrata doc = new DocumentoEntrata();
		doc.setEnte(getEnteTest());
		doc.setAnno(2015);
		doc.setContabilizzaGenPcc(false);
		doc.setNumero("a");
//		it.csi.siac.siacfin2ser.model.TipoDocumento tipoDocumento = new it.csi.siac.siacfin2ser.model.TipoDocumento();
//		tipoDocumento.setUid(191);
//		doc.setTipoDocumento(tipoDocumento);
		
		subdocumentoIvaEntrata.setEnte(getEnteTest());
		subdocumentoIvaEntrata.setDocumento(doc);
		request.setSubdocumentoIvaEntrata(subdocumentoIvaEntrata);
		request.setDataOra(new Date());
		ParametriPaginazione parametriPaginazione= new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(10);
		request.setParametriPaginazione(parametriPaginazione);

		RicercaSinteticaSubdocumentoIvaEntrataResponse response =  ricercaSinteticaSubdocumentoIvaEntrataService.executeService(request);
		log.logXmlTypeObject(request, "request del servizio ricercaSinteticaSubdocivaEntrataServie");

		log.logXmlTypeObject(response, "response del servizio ricercaSinteticaSubdocivaEntrataServie");

	}
	
	
	
	
	
	
	
	/**
 * Creates the and log mapped entity.
 *
 * @param <M> the generic type
 * @param <E> the element type
 * @param ai the ai
 * @param clazz the clazz
 * @param mapId the map id
 */
@Transactional
	private <M, E> void createAndLogMappedEntity(M ai, Class<E> clazz, BilMapId mapId) {
		E entity = mapper.map(ai, clazz, mapId.name());
		l.logXmlTypeObject(entity, clazz.getSimpleName());
	}
}
