/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.classificatorebil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriBilByIdFiglioService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriBilByIdPadreService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiClassificatoriByRelazioneService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiElementoPianoDeiContiByCodiceAndAnnoService;
import it.csi.siac.siacbilser.business.service.classificatorebil.LeggiTreeSiopeSpesaService;
import it.csi.siac.siacbilser.business.service.classificatorebil.RicercaTipoClassificatoreGenericoService;
import it.csi.siac.siacbilser.business.service.classificatorebil.RicercaTipoClassificatoreService;
import it.csi.siac.siacbilser.business.service.conto.LeggiTreeCodiceBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglio;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenerico;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreGenericoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoClassificatoreResponse;
import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPdceContoRepository;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancioResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.CodiceBilancio;

// TODO: Auto-generated Javadoc
/**
 * The Class ClassificatoriBilDLTest.
 */
public class ClassificatoriBilTest extends BaseJunit4TestCase
{
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());

	/** The leggi classificatori by relazione service. */
	@Autowired
	private LeggiClassificatoriByRelazioneService leggiClassificatoriByRelazioneService;
	
	@Autowired
	private LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoService leggiClassificatoreByCodiceAndTipoAndAnnoService;
	
	@Autowired
	private LeggiElementoPianoDeiContiByCodiceAndAnnoService leggiElementoPianoDeiContiByCodiceAndAnnoService;
	
	@Autowired
	private LeggiClassificatoriBilByIdPadreService leggiClassificatoriBilByIdPadreService;
	@Autowired
	private LeggiClassificatoriBilByIdFiglioService leggiClassificatoriBilByIdFiglioService;
	@Autowired
	private RicercaTipoClassificatoreService ricercaTipoClassificatoreService;
	
	@Autowired
	private LeggiTreeSiopeSpesaService leggiTreeSiopeSpesaService;
	
	@Autowired
	private RicercaTipoClassificatoreGenericoService ricercaTipoClassificatoreGenericoService;
	
	@Autowired
	private LeggiTreeCodiceBilancioService leggiTreeCodiceBilancioService;

	@Autowired
	private SiacTClassRepository siacTClassRepository;

	@Autowired
	private SiacTPdceContoRepository siacTPdceContoRepository;
	
	@Autowired
	private CodificaBilDao codificaBilDao;

	
	/**
	 * Test leggi classificatori by relazione.
	 */
	@Test
	public void testLeggiClassificatoriByRelazione() {
		LeggiClassificatoriByRelazione req = new LeggiClassificatoriByRelazione();
		//req.setBilancio(getBilancioTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setFromAToB(true);
		req.setAnno(2014);
		
//		req.setIdClassif(19579);
		req.setIdClassif(5830);//previsione
//		req.setIdClassif(118324);
		
		


		LeggiClassificatoriByRelazioneResponse res = leggiClassificatoriByRelazioneService.executeService(req);
		res = marshallUnmarshall(res, LeggiClassificatoriByRelazioneResponse.class);
		log.logXmlTypeObject(res, "Unmarshalled response: ");
		
//		System.out.println("GEN: "+ res.getClassificatoriGenerici());
//		System.out.println("GER: "+ res.getClassificatoriGerarchici());
//		System.out.println("PDC: "+ res.getClassificatoriElementoPianoDeiConti());
//		System.out.println("TITOLO: "+ res.getClassificatoriTitoloSpesa());

	}
	
	@Test
	public void testLeggiClassificatoriBilByIdPadreService() {
		LeggiClassificatoriBilByIdPadre req = new LeggiClassificatoriBilByIdPadre();
		//req.setBilancio(getBilancioTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAnno(2015);
		req.setDataOra(new Date());
		req.setIdEnteProprietario(1);
//		req.setIdPadre(16);
		req.setIdPadre(125550);
		req.setIdPadre(7436);
		
		LeggiClassificatoriBilByIdPadreResponse res = leggiClassificatoriBilByIdPadreService.executeService(req);
		res = marshallUnmarshall(res, LeggiClassificatoriBilByIdPadreResponse.class);
		log.logXmlTypeObject(res, "Unmarshalled response: ");
		
	}
	
	@Test
	public void testLeggiClassificatoriGerarchicoByIdFiglioService() {
		LeggiClassificatoriBilByIdFiglio req = new LeggiClassificatoriBilByIdFiglio();
		//req.setBilancio(getBilancioTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAnno(2015);
		req.setDataOra(new Date());
		req.setIdEnteProprietario(1);
		req.setIdFiglio(123973);//quinto livello entrata
//		req.setIdFiglio(123972);//quarto livello entrata
//		req.setIdFiglio(123875);//terzo livello entrata
//		req.setIdFiglio(123874);//secondo livello entrata
//		req.setIdFiglio(123873);// primo livello entrata


		
		LeggiClassificatoriBilByIdFiglioResponse res = leggiClassificatoriBilByIdFiglioService.executeService(req);
		res = marshallUnmarshall(res, LeggiClassificatoriBilByIdFiglioResponse.class);
		log.logXmlTypeObject(res, "Unmarshalled response: ");
		
	}
	
	@Test
	public void testLeggiSiope() {
		LeggiTreeSiope req = new LeggiTreeSiope();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAnno(2014);
		req.setIdCodificaPadre(7478);
		req.setIdEnteProprietario(1);
		
		LeggiTreeSiopeResponse res = leggiTreeSiopeSpesaService.executeService(req);
		log.logXmlTypeObject(res, "tree siope: ");
		
	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		LeggiClassificatoriByRelazioneResponse res = new LeggiClassificatoriByRelazioneResponse();
		List<Codifica> classificatori = new ArrayList<Codifica>();
		
		ElementoPianoDeiConti pdc = new ElementoPianoDeiConti();		
		pdc.setCodice("E.2.00.00.00.000");
		pdc.setDescrizione("Trasferimenti correnti");
		TipoClassificatore tipoClassificatore = new TipoClassificatore();
		tipoClassificatore.setCodice("PDC_I");
		pdc.setTipoClassificatore(tipoClassificatore);		
		classificatori.add(pdc);
		
		
		
		TipoFinanziamento tf = new TipoFinanziamento();		
		tf.setCodice("TF1");
		tf.setDescrizione("tipo fin fittizio");
		TipoClassificatore tipoClassificatore2 = new TipoClassificatore();
		tipoClassificatore2.setCodice("TipoFinanziamento");
		tf.setTipoClassificatore(tipoClassificatore2);		
		classificatori.add(tf);	
		
		Missione m = new Missione();		
		m.setCodice("Mission 1");
		m.setDescrizione("Mission Impossible");
		TipoClassificatore tipoClassificatore3 = new TipoClassificatore();
		tipoClassificatore3.setCodice("Missione");
		m.setTipoClassificatore(tipoClassificatore3);		
		classificatori.add(m);	
		
		res.setClassificatori(classificatori);
		
		res = marshallUnmarshall(res, LeggiClassificatoriByRelazioneResponse.class);
		
		System.out.println(JAXBUtility.marshall(res));
		
		System.out.println("GEN: "+ res.getClassificatoriGenerici());
		System.out.println("GER: "+ res.getClassificatoriGerarchici());
		System.out.println("PDC: "+ res.getClassificatoriElementoPianoDeiConti());
		System.out.println("TF: "+ res.getClassificatoriTipoFinanziamento());
	}
	
	
	@Test
	public void leggiClassificatoreByCodiceAndTipoAndAnno() {
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno req = new LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno();
		req.setAnno(2015);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setTipologiaClassificatore(TipologiaClassificatore.PDC_V);
		
		ClassificatoreGerarchico classificatore = new ElementoPianoDeiConti();
		classificatore.setCodice("U.2.05.01.02.001");
		req.setClassificatore(classificatore);
		
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse res = leggiClassificatoreByCodiceAndTipoAndAnnoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void leggiElementoPianoDeiContiByCodiceAndAnno() {
		LeggiElementoPianoDeiContiByCodiceAndAnno req = new LeggiElementoPianoDeiContiByCodiceAndAnno();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAnno(2015);
		ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
		elementoPianoDeiConti.setCodice("U.2.05.01.02.001");
		req.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		LeggiElementoPianoDeiContiByCodiceAndAnnoResponse res = leggiElementoPianoDeiContiByCodiceAndAnnoService.executeService(req);
		assertNotNull(res);
	}

	@Test
	public void ricercaTipoClassificatore() {
		RicercaTipoClassificatore req = new RicercaTipoClassificatore();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setAnno(Integer.valueOf(2017));
		req.setTipologieClassificatore(Arrays.asList(
				TipologiaClassificatore.CLASSIFICATORE_1,
				TipologiaClassificatore.CLASSIFICATORE_2,
				TipologiaClassificatore.CLASSIFICATORE_3,
				TipologiaClassificatore.CLASSIFICATORE_4,
				TipologiaClassificatore.CLASSIFICATORE_5,
				TipologiaClassificatore.CLASSIFICATORE_6,
				TipologiaClassificatore.CLASSIFICATORE_7,
				TipologiaClassificatore.CLASSIFICATORE_8,
				TipologiaClassificatore.CLASSIFICATORE_9,
				TipologiaClassificatore.CLASSIFICATORE_10,
				TipologiaClassificatore.CLASSIFICATORE_31,
				TipologiaClassificatore.CLASSIFICATORE_32,
				TipologiaClassificatore.CLASSIFICATORE_33,
				TipologiaClassificatore.CLASSIFICATORE_34,
				TipologiaClassificatore.CLASSIFICATORE_35,
				TipologiaClassificatore.CLASSIFICATORE_36,
				TipologiaClassificatore.CLASSIFICATORE_37,
				TipologiaClassificatore.CLASSIFICATORE_38,
				TipologiaClassificatore.CLASSIFICATORE_39,
				TipologiaClassificatore.CLASSIFICATORE_40,
				TipologiaClassificatore.CLASSIFICATORE_41,
				TipologiaClassificatore.CLASSIFICATORE_42,
				TipologiaClassificatore.CLASSIFICATORE_43,
				TipologiaClassificatore.CLASSIFICATORE_44,
				TipologiaClassificatore.CLASSIFICATORE_45,
				TipologiaClassificatore.CLASSIFICATORE_46,
				TipologiaClassificatore.CLASSIFICATORE_47,
				TipologiaClassificatore.CLASSIFICATORE_48,
				TipologiaClassificatore.CLASSIFICATORE_49,
				TipologiaClassificatore.CLASSIFICATORE_50
				));
		
		RicercaTipoClassificatoreResponse res = ricercaTipoClassificatoreService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaTipoClassificatoreGenerico() {
		RicercaTipoClassificatoreGenerico req = new RicercaTipoClassificatoreGenerico();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setAnno(Integer.valueOf(2017));
		req.setTipologieClassificatore(Arrays.asList(
				TipologiaClassificatore.CLASSIFICATORE_1,
				TipologiaClassificatore.CLASSIFICATORE_2,
				TipologiaClassificatore.CLASSIFICATORE_3,
				TipologiaClassificatore.CLASSIFICATORE_4,
				TipologiaClassificatore.CLASSIFICATORE_5,
				TipologiaClassificatore.CLASSIFICATORE_6,
				TipologiaClassificatore.CLASSIFICATORE_7,
				TipologiaClassificatore.CLASSIFICATORE_8,
				TipologiaClassificatore.CLASSIFICATORE_9,
				TipologiaClassificatore.CLASSIFICATORE_10,
				TipologiaClassificatore.CLASSIFICATORE_31,
				TipologiaClassificatore.CLASSIFICATORE_32,
				TipologiaClassificatore.CLASSIFICATORE_33,
				TipologiaClassificatore.CLASSIFICATORE_34,
				TipologiaClassificatore.CLASSIFICATORE_35,
				TipologiaClassificatore.CLASSIFICATORE_36,
				TipologiaClassificatore.CLASSIFICATORE_37,
				TipologiaClassificatore.CLASSIFICATORE_38,
				TipologiaClassificatore.CLASSIFICATORE_39,
				TipologiaClassificatore.CLASSIFICATORE_40,
				TipologiaClassificatore.CLASSIFICATORE_41,
				TipologiaClassificatore.CLASSIFICATORE_42,
				TipologiaClassificatore.CLASSIFICATORE_43,
				TipologiaClassificatore.CLASSIFICATORE_44,
				TipologiaClassificatore.CLASSIFICATORE_45,
				TipologiaClassificatore.CLASSIFICATORE_46,
				TipologiaClassificatore.CLASSIFICATORE_47,
				TipologiaClassificatore.CLASSIFICATORE_48,
				TipologiaClassificatore.CLASSIFICATORE_49,
				TipologiaClassificatore.CLASSIFICATORE_50
				));
		req.setTipoElementoBilancio("CAP-UP");
		
		RicercaTipoClassificatoreGenericoResponse res = ricercaTipoClassificatoreGenericoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ba() {
		final String methodName = "leggiTreeCodiceBilancioService";
		
		//PARAMS
		Integer anno = 2018;
		Integer uidClasse = 277;
		Integer uidEnte = 2;
		Integer uidRichiedente = 52;
		String codiceFiscaleOperatore = "AAAAAA00A11C000K";
		//
		
		//REQUEST PARAMS
		LeggiTreeCodiceBilancio reqLTCB = new LeggiTreeCodiceBilancio();
		reqLTCB.setAnno(anno);
		reqLTCB.setAnnoBilancio(anno);
		ClassePiano classePiano = new ClassePiano();
		classePiano.setUid(uidClasse);
		reqLTCB.setClassePiano(classePiano);
		reqLTCB.setDataOra(new Date());
		reqLTCB.setRichiedente(getRichiedenteTest(codiceFiscaleOperatore, uidRichiedente, uidEnte));
		//
		
		LeggiTreeCodiceBilancioResponse resLTCB = leggiTreeCodiceBilancioService.executeService(reqLTCB);

		boolean trovato = false;
		
		for (CodiceBilancio iterable_element : resLTCB.getTreeCodiciBilancio()) {
			if (iterable_element.getUid() == 75647050
					|| iterable_element.getUid() == 75647051) {
				trovato = true;
				log.debug(methodName, "TROVATO");
				System.out.println("############################################################################################");
			}
		}
		
		assertTrue(trovato);
		assertNotNull(resLTCB);
		assertNotNull(resLTCB.getTreeCodiciBilancio());
		assertTrue(Esito.SUCCESSO.equals(resLTCB.getEsito()));
		
		
	}
}
